## Plan: AE2 패턴 풀 시스템

현재 저장소는 AE2 의존성만 연결된 초기 상태이며, `PatternGlueItem`, 미완성 `PatternGlueEntity`, 빈 `providerBE`, 좌표 유틸리티만 존재한다. 목표는 Create Super Glue에서 영감을 받은 풀 영역 선택/표시를 기반으로 여러 AE2 Pattern Provider를 하나의 논리적 패턴 공급 풀로 묶고, 이후 멀티블록 또는 외부 기계 연결까지 확장 가능한 구조를 만드는 것이다.

**권장 핵심 설계**
- 한 좌표에는 BlockEntity 하나만 허용되므로 AE2의 `PatternProviderBlockEntity` 위에 별도 BlockEntity를 겹쳐 두지 않는다.
- 풀은 독립적인 서버 엔티티(`PoolAreaEntity`)로 표현한다. 엔티티는 UUID, 소유/컨트롤러 위치, 영역의 두 끝점 또는 정규화된 `AABB`, 연결된 provider 위치 목록을 저장하고, 매 틱 전체 영역을 검사하지 않는다.
- 풀 엔티티는 영역 선택과 시각화의 기준이다. 실제 AE2 패턴 통합 로직은 별도의 컨트롤러/서비스 계층에 둔다. 장기적으로는 자체 컨트롤러 블록과 BlockEntity를 마스터로 삼고, provider는 위치 참조로 연결한다.
- `PatternGlueItem`은 우클릭 시 패턴 프로바이더 GUI를 막는 역할만 하지 않고, 서버에서 선택 상태를 관리하고 첫 provider/두 번째 provider 또는 영역 지점을 기록한 뒤 풀 생성/수정 명령을 호출해야 한다.
- AE2 통합은 단순 `getCapability` 반환으로 끝나지 않는다. 일반 아이템/액체/에너지 capability 포워딩과 AE2 grid 노드/패턴 공급자 통합은 별도 작업으로 분리한다.

**단계**
1. **기반 정리 및 등록 구조 구축**
	- `Realistic_Water` 메인 클래스 및 NeoForge 등록 이벤트 패턴을 확인/정립한다.
	- `PatternGlueItem`, 풀 엔티티 타입, 필요 시 컨트롤러 블록/BlockEntity의 DeferredRegister 등록을 추가한다.
	- 현재 `PatternGlueEntity`는 실제 등록 전 자리표시자이므로, 엔티티로 구현할지 BlockEntity로 오인한 이름을 정리하고 `PoolAreaEntity` 같은 역할 중심 이름으로 재설계한다.
	- 목표 검증: 개발 클라이언트에서 아이템 획득과 엔티티 생성/저장이 가능하고 `compileJava`가 통과한다.

2. **풀 선택 상태와 서버 권위 생성 흐름**
	- `PatternGlueItem` 사용 시 아이템 종류를 먼저 검사하고, 패턴 프로바이더가 아닌 대상에는 아무 동작도 하지 않는다.
	- 플레이어별 선택 상태를 서버 메모리 또는 짧은 수명의 서버 플레이어 capability/attachment로 관리한다. 첫 클릭은 시작점/첫 provider를 저장하고, 두 번째 유효 클릭은 끝점/두 번째 provider를 저장한다.
	- 서버에서만 영역을 정규화해 min/max 좌표와 크기를 계산하고 `PoolAreaEntity`를 한 번만 생성한다. 중복 생성 방지를 위해 기존 UUID/소유자/컨트롤러 연결을 확인한다.
	- 클릭 이벤트에서 기본 AE2 GUI를 막는 시점과 선택을 소비하는 시점을 분리한다. 취소, 잘못된 대상, 손에 든 아이템 변경, 거리 초과, 차원 변경을 명시적으로 처리한다.
	- 목표 검증: 싱글플레이어와 서버에서 두 번의 선택으로 하나의 풀만 생성되고 재시작 후 중복되지 않는다.

3. **풀 엔티티 데이터 저장과 수명 관리**
	- 엔티티에 `BlockPos` 두 개 또는 정규화된 `AABB`, owner/controller 참조, provider 위치 목록/식별자, 포맷 버전을 저장한다.
	- `addAdditionalSaveData`/`readAdditionalSaveData`와 필요한 spawn data를 구현하고, 영역 자체는 엔티티의 실제 hitbox에 의존하지 않고 저장된 좌표를 기준으로 판정한다.
	- provider 또는 컨트롤러가 제거될 때 풀을 해제/갱신하고, 엔티티가 제거될 때 provider 매핑과 선택 상태를 정리한다. 청크 언로드 때 엔티티 UUID와 저장 좌표로 복구 가능한 정책을 둔다.
	- 목표 검증: 월드 저장/로드, 청크 언로드/로드, provider 블록 파괴 후 참조 정리가 동작한다.

4. **클라이언트 영역 표시**
	- NeoForge의 해당 `RenderLevelStageEvent` 렌더 단계와 클라이언트 전용 이벤트 버스를 사용해 풀의 AABB 선/반투명 면을 렌더링한다.
	- 서버에서 동기화된 엔티티 데이터와 플레이어가 glue 도구를 들고 있는지, 선택 중인지, 바라보는 풀인지에 따라 표시 여부와 색상을 결정한다.
	- 렌더러는 서버 로직이나 엔티티 tick에 의존하지 않고 카메라 좌표 기준으로만 그린다. 선택 중인 두 점의 임시 프리뷰는 클라이언트에서 렌더링하되 최종 생성은 서버가 승인한다.
	- 목표 검증: 1인칭/3인칭, 멀리서 보기, 청크 경계, 영역 크기 변화에서 선이 깨지거나 화면 전체를 채우지 않으며 클라이언트 전용 클래스가 서버에서 로드되지 않는다.

5. **provider 매핑과 마스터/더미 설계**
	- 1차 MVP에서는 자체 마스터 블록 없이 풀 엔티티가 provider 위치 목록을 관리하고, 연결된 `PatternProviderBlockEntity`를 주기적 또는 이벤트 기반으로 검증한다.
	- 장기 구조로는 자체 `PoolControllerBlock`/`PoolControllerBlockEntity` 하나를 마스터로 두고 풀 엔티티가 컨트롤러 UUID를 참조하게 한다. 한 좌표에 AE2 provider BE와 자체 BE를 겹치지 않는다.
	- provider 목록은 영역 내 블록 전체를 매 틱 스캔하지 말고 생성/수정 시 탐색, 블록 변경 이벤트, 정해진 재검증 주기로 갱신한다. provider가 다른 차원/청크에 있거나 제거된 경우 연결을 끊는다.
	- 목표 검증: 풀에 속한 provider만 목록에 들어가고, 영역 밖 provider·파괴된 provider·중복 provider가 제외된다.

6. **AE2 패턴 통합**
	- 먼저 AE2 API 버전에 맞는 Pattern Provider/ME grid API와 네트워크 스레드/서버 실행 규칙을 확인한다.
	- 연결된 provider의 패턴 슬롯을 단순히 복사하지 말고, 하나의 논리적 controller가 provider 목록을 집계하는 어댑터를 설계한다. 패턴 삽입/삭제, crafting request 전달, 채널/그리드 생명주기, provider 변경 알림을 명시한다.
	- AE2의 실제 `IGridNode`/서비스 등록 방식이 요구되면 자체 컨트롤러가 하나의 grid 노드와 패턴 공급 서비스 역할을 갖도록 구현하고, 기존 provider를 무리하게 상속하거나 같은 좌표에 재등록하지 않는다.
	- 목표 검증: AE2 네트워크가 풀을 하나의 공급원으로 인식하고 패턴 목록, 삽입/삭제, crafting 요청이 정상 동작한다. 네트워크 미연결/청크 언로드/컨트롤러 파괴도 오류 없이 처리한다.

7. **외부 기계와 더미 블록 연결**
	- 일반 NeoForge capability가 필요한 경우에만 더미/외벽 블록 또는 컨트롤러의 `getCapability`를 마스터 저장소로 포워딩한다. 방향별 접근, 빈 핸들러, 마스터 부재를 정의한다.
	- AE2 grid 연결은 capability 포워딩과 별개로 다룬다. 단순히 master의 capability를 반환한다고 AE2 케이블/패턴 공급 서비스가 자동 통합된다고 가정하지 않는다.
	- 필요성이 확인된 뒤에만 Dummy BlockEntity를 추가한다. 1차 구현에서는 BlockEntity 수를 늘리지 않고 master/controller의 위치 참조만 사용한다.
	- 목표 검증: 외부 기계가 dummy/controller의 각 면에 연결될 때 원하는 입출력만 노출되고, 멀티블록 해체 후 stale reference가 남지 않는다.

8. **멀티블록 해체/복구 및 테스트**
	- 구조 검증은 설치/파괴/인접 블록 변경 이벤트와 제한된 주기 재검증을 조합한다. 매 틱 전체 구조 검사는 제외한다.
	- 단일 마스터 원칙을 유지하고, 더미는 masterPos만 저장하는 얇은 포워더로 제한한다.
	- GameTest 또는 최소한 서버 통합 테스트 시나리오로 생성, 저장/로드, provider 추가/삭제, 컨트롤러 파괴, 청크 언로드, 네트워크 재연결, 외부 기계 연결을 검증한다.

**권장 MVP 순서**

`glue 선택 -> 풀 엔티티 저장 -> 클라이언트 AABB 표시 -> provider 목록 관리 -> AE2 통합 -> 외부 기계 포워딩`

**현재 주요 파일**

- `src/main/java/dev/rdf453/fakeName/glue/PatternGlueItem.java`: 도구 사용 조건과 우클릭 이벤트
- `src/main/java/dev/rdf453/fakeName/glue/PatternGlueEntity.java`: 풀 엔티티로 재설계할 미완성 자리표시자
- `src/main/java/dev/rdf453/fakeName/masterProvider/providerBE.java`: provider 집계와 패턴 통합의 개념적 자리
- `src/main/java/dev/rdf453/fakeName/util/Pos.java`: 영역 좌표 모델의 초기 흔적
- `build.gradle`: NeoForge, Java, AE2 버전 및 의존성
## Plan: AABB 공유 패턴 인벤토리

Create Super Glue 방식으로 지정한 AABB 엔티티를 공유 패턴 인벤토리로 사용한다. 영역 안에 있는 AE2 Pattern Provider들은 자신의 패턴 슬롯 대신 AABB의 공유 슬롯을 읽는다. Provider 자체는 계속 AE2 Grid와 채널을 사용하므로 채널 비용과 밸런스는 유지하고, 여러 Provider에 같은 패턴을 반복해서 넣고 관리하는 비용만 줄인다.

**핵심 구조**
- `PatternGlueEntity`는 정규화된 AABB와 공유 패턴 슬롯을 소유한다.
- `PatternGlueEntitySlot`은 공유 `ItemStack` 인벤토리의 슬롯 저장, 삽입·추출, 저장·로드를 담당한다.
- AABB 내부의 `PatternProviderBlockEntity`는 공유 인벤토리를 패턴 인벤토리로 읽고, 영역 밖이거나 연결되지 않으면 기존 인벤토리를 사용한다.
- 기존 Provider들은 실제 AE2 Grid 노드와 채널을 계속 사용한다. 공유 인벤토리는 채널을 대체하지 않는다.
- 기존 AE2 패턴 처리와 기계 작업 로직을 우선 재사용한다. 별도 스케줄러는 원본 로직으로 부족한 것이 확인된 뒤에만 추가한다.

**구현 단계**

1. **공유 인벤토리 최소 구현**
	- `PatternGlueEntitySlot`에 고정 크기의 `ItemStack` 슬롯을 만든다.
	- 처음에는 AE2 Pattern Provider 하나의 실제 슬롯 수를 넘지 않게 한다.
	- 빈 슬롯, 삽입, 추출, 슬롯 범위 검사, 저장·로드를 정의한다.
	- 패턴을 Provider마다 복사하지 않고 AABB 슬롯을 단일 원본으로 삼는다.

2. **AABB 엔티티와 Super Glue 연결**
	- `PatternGlueEntity`에 두 선택점 또는 정규화된 최소·최대 좌표, 차원, 공유 슬롯을 저장한다.
	- `PatternGlueItem`의 첫 클릭과 두 번째 클릭으로 영역을 생성하고 서버에서 최종 승인한다.
	- 취소, 잘못된 대상, 거리 초과, 차원 변경, 중복 영역을 처리한다.
	- 영역 생성·블록 변경·Provider 파괴·제한된 재검증 시점에만 내부 Provider 목록을 갱신한다.
	- 매 틱 전체 월드를 스캔하지 않는다.

3. **영역 시각화와 기본 UI**
	- 선택 중인 영역과 생성된 AABB를 클라이언트에서 표시한다.
	- 실제 엔티티의 hitbox가 아니라 저장된 좌표를 기준으로 표시하고 판정한다.
	- 공유 슬롯을 확인하고 패턴을 넣고 뺄 수 있는 최소 UI를 만든다.
	- 화면 정렬이 필요하면 기존 AE2 패턴 패널 규칙을 우선 사용하고, 별도 정렬 시에는 좌표가 작은 순서를 사용한다.

4. **AE2 패턴 인식 수직 검증**
	- 현재 AE2 버전의 `PatternProviderBlockEntity`, `PatternProviderLogic`, 패턴 인벤토리 접근 지점을 확인한다.
	- 공식 확장 지점이 없을 때만 Mixin으로 실제 패턴 조회 또는 패턴 열거 지점을 가로챈다.
	- Provider가 유효한 AABB 내부에 있으면 공유 인벤토리를 읽고, 아니면 원래 인벤토리를 읽게 한다.
	- 공유 인벤토리 크기는 처음부터 AE2가 기대하는 Provider 슬롯 범위를 넘기지 않는다.
	- 공유 슬롯 변경 시 AE2 패턴 목록과 Grid 캐시 갱신이 필요한지 확인하고 필요한 알림을 호출한다.
	- 첫 성공 기준은 `AABB 슬롯 1개 -> Provider 1개 -> AE2 터미널에서 패턴 인식`이다.

5. **여러 Provider의 공유와 복구**
	- Provider 여러 개가 같은 AABB 공유 슬롯을 읽도록 확장한다.
	- 동일 패턴이 여러 Provider에 노출되는 방식과 AE2의 중복 표시·작업 예약 동작을 확인한다.
	- 각 Provider의 채널과 실제 작업 위치는 유지되는지 확인한다.
	- AABB 제거, 영역 이탈, Provider 파괴, 청크 언로드 시 stale 참조가 남지 않고 원래 인벤토리와 Grid 연결로 안전하게 돌아가는지 검증한다.
	- 영역 중첩은 초기에는 금지하거나 명확한 우선순위를 둔다.

6. **병렬 처리와 요청 수량**
	- 공유 패턴 인식이 안정화된 뒤에만 병렬 처리 개선을 검토한다.
	- 목표는 여러 Provider의 채널 비용은 유지하면서, 논리적으로 적은 수의 패턴만 관리하는 것이다.
	- AE2 원본 로직이 각 Provider와 연결 기계의 작업을 이미 처리하는지 먼저 확인한다.
	- 원본 로직만으로 부족할 때만 작업 예약 ID, 기계 상태, 재료 투입, 결과 회수, 서버 재시작 복구를 갖춘 스케줄러를 추가한다.
	- 요청 가능한 아이템 수량은 단순 슬롯 수가 아니라 연결된 Provider와 기계의 처리 능력이 AE2에 어떻게 집계되는지 확인한 뒤 확장한다.

**현재 주요 파일**

- `src/main/java/dev/rdf453/fakeName/glue/PatternGlueEntitySlot.java`: 공유 패턴 인벤토리
- `src/main/java/dev/rdf453/fakeName/glue/PatternGlueEntity.java`: AABB와 공유 슬롯의 소유·저장·로드
- `src/main/java/dev/rdf453/fakeName/glue/PatternGlueItem.java`: Super Glue식 영역 선택과 연결 갱신
- `src/main/java/dev/rdf453/fakeName/dummyProvider/DummyProvider.java`: 공유 슬롯 참조를 검증할 초기 Provider 실험 대상
- `src/main/java/dev/rdf453/fakeName/masterProvider/masterProviderBlockEntity.java`: 더미-마스터 구조는 보류하고 필요할 때만 재검토
- `build.gradle`: NeoForge와 AE2 의존성

**검증 순서**

1. 각 구현 단계 후 `./gradlew compileJava` 실행
2. 공유 슬롯에 패턴 하나를 넣고 Provider 하나가 AE2 터미널에서 인식하는지 확인
3. 공유 슬롯의 삽입·추출·저장·로드 확인
4. Provider 여러 개가 같은 패턴을 읽고 AE2에서 중복이 어떻게 표시되는지 확인
5. AABB 삭제·Provider 파괴·청크 언로드 후 원래 인벤토리와 Grid 연결 확인
6. 공유 패턴 변경 직후 AE2 캐시가 갱신되는지 확인
7. 마지막에 실제 기계 병렬 처리와 요청 수량 증가를 테스트

**결정 사항**

- 더미-마스터 간 패턴 복사는 기본 설계에서 제외한다.
- AABB 공유 인벤토리를 패턴의 단일 원본으로 둔다.
- Provider는 계속 채널을 사용하며, 공유 인벤토리는 채널 절감 장치가 아니다.
- 공유 슬롯은 처음부터 Provider의 고정 슬롯 범위 안에서 시작한다.
- Mixin은 공식 확장 지점이 없을 때만 실제 패턴 조회·변경 알림 지점에 적용한다.
- 병렬 스케줄러와 무제한 슬롯 확장은 공유 패턴 인식 검증 뒤로 미룬다.

**제외 범위**

- 첫 단계에서 완전한 커스텀 AE2 Grid 서비스 구현
- Provider마다 패턴 복사 및 동기화
- AABB 하나가 채널을 대체하는 동작
- 모든 외부 기계 capability를 한 번에 지원
- 패턴 인벤토리의 무제한 동적 확장
