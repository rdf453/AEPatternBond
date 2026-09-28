package dev.rdf453.PatternBond.master;

import java.util.List;

import appeng.api.crafting.IPatternDetails;
import appeng.api.inventories.InternalInventory;
import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.crafting.ICraftingProvider;
import appeng.api.stacks.KeyCounter;
import appeng.helpers.patternprovider.PatternProviderLogic;
import appeng.helpers.patternprovider.PatternProviderLogicHost;

public class AdpeterLogic extends PatternProviderLogic {

    public AdpeterLogic(IManagedGridNode mainNode, PatternProviderLogicHost host) {
        super(mainNode, host);
    }

    @Override
    public InternalInventory getPatternInv() {
        return ProviderAdepterBlockEntity.combined != null ? ProviderAdepterBlockEntity.combined : super.getPatternInv();
        // 마스터 화면/터미널이 합쳐진 더미 인벤토리를 보도록 반환
    }

    @Override
    public List<IPatternDetails> getAvailablePatterns() {
        return super.getAvailablePatterns();
        // 더미 로직들의 패턴 목록을 합쳐 반환
    }

    @Override
    public boolean pushPattern(IPatternDetails patternDetails, KeyCounter[] inputHolder) {
        return super.pushPattern(patternDetails, inputHolder);
        // 요청된 패턴을 가진 더미 로직에 실행을 위임
    }

    @Override
    public boolean isBusy() {
        return super.isBusy();
        //위임 가능한 더미가 없거나 전부 바쁜지 판단
    }


    @Override 
    public void updatePatterns() {
        //합성 인벤토리를 디코딩해 어댑터 자체 패턴 목록을 만들고, 마스터 provider 캐시 갱신
    }

    //더미의 패턴이 추가·삭제될 때 마스터의 provider 캐시를 갱신해야 합니다. 메서드 오버라이드만으로 자동 갱신되지는 않아요.
}
