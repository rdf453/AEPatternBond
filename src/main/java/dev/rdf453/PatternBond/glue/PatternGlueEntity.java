package dev.rdf453.PatternBond.glue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import appeng.blockentity.crafting.PatternProviderBlockEntity;

public class PatternGlueEntity extends Entity implements IEntityWithComplexSpawn {
    public Map<BlockPos,Integer> SlotHashMap = new HashMap<>();



    public PatternGlueEntity(EntityType<?> type, Level level) {
        super(type,level);
    }



    public static AABB span(BlockPos start, BlockPos end) {
        return new AABB(Vec3.atLowerCornerOf(start),Vec3.atLowerCornerOf(end)).expandTowards(1,1,1);
    }

    public int setSlot(LevelAccessor level, BlockPos pos,Set<PatternGlueEntity> data) {
        if(level == null) return 0;
        int slotCount;
        int totalSlots = 0;
        for(BlockPos position : BlockPos.betweenClosed(getBoundingBox())) {
            BlockEntity bEntity = level.getBlockEntity(position);

            if(bEntity instanceof PatternProviderBlockEntity) {
                slotCount= sendSlot(level,position);
                SlotHashMap.put(position.immutable(), slotCount);
                totalSlots += slotCount;
            }
        }
        return totalSlots;
    }


    public int sendSlot(LevelAccessor level,BlockPos pos) {
        if(level == null) {
            return 0;
        }              
            if(level.getBlockEntity(pos) instanceof PatternProviderBlockEntity entity) {
                return entity.getLogic().getPatternInv().size();   
        }
        return 0;
    }
    //엔티티가 생성될때 서버에서 클라이언트로 보낼 값
    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'writeSpawnData'");
    }
    //클라이언트가 서버에서 받은 값 읽기
    @Override
    public void readSpawnData(RegistryFriendlyByteBuf additionalData) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readSpawnData'");
    }
    //nbt 저장하기
    @Override
    protected void addAdditionalSaveData(ValueOutput arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addAdditionalSaveData'");
    }
    //클라이언트 동기화 데이터 정의 (서버와 클라이언트 양쪽에서 사용해야하는 값이 있다면 여기에 EntityDataAccessor 등록)
    @Override
    protected void defineSynchedData(Builder arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'defineSynchedData'");
    }
    //서버에서 엔티티가 공격받았을때 호출
    @Override
    public boolean hurtServer(ServerLevel arg0, DamageSource arg1, float arg2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hurtServer'");
    }
    //nbt 불러오기
    @Override
    protected void readAdditionalSaveData(ValueInput arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readAdditionalSaveData'");
    }

}
//영역 표시 그냥 니가 다하는거야 제공자 보다 먼저 채널 잡아먹고 제공자의 슬롯을 복사하여 슬롯을 표시하고
