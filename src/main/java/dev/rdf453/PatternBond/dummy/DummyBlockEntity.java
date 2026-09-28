package dev.rdf453.PatternBond.dummy;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import appeng.blockentity.crafting.PatternProviderBlockEntity;
import appeng.core.definitions.AEBlockEntities;
import dev.rdf453.PatternBond.master.ProviderAdepterBlockEntity;

public class DummyBlockEntity extends PatternProviderBlockEntity{
    public BlockPos masterPos;
    public int slotSize;
    CompoundTag origin;
    public DummyBlockEntity(BlockPos pos,BlockState state,BlockPos masterPos,int slotSize) {
        super(AEBlockEntities.PATTERN_PROVIDER.get(), pos, state);
        this.masterPos=masterPos;
        this.slotSize=slotSize;
        //나중에 더미 블럭 엔티티 타입 생성해서 넣을것
    }

    @Override 
    public void saveAdditional(ValueOutput data) {
        origin = loadCompoundTag(worldPosition);
        if(origin != null) 
            data.store("OriginalProvider",CompoundTag.CODEC,origin);
        data.putLong("MasterPos", masterPos.asLong());
        data.putInt("SlotSize", slotSize);
        super.saveAdditional(data);
    }

    @Override 
    public void loadAdditional(ValueInput data) {
        this.origin=data.read("OriginalProvider",CompoundTag.CODEC).orElse(null);
        this.masterPos=data.getLong("MasterPos").map(BlockPos::of).orElse(null);
        this.slotSize=data.getIntOr("SlotSize", 9);
        super.loadAdditional(data);
    }
    //수정 필요
    public void rollback(BlockPos pos, BlockState blockState){
        BlockEntity temp = new PatternProviderBlockEntity(
            AEBlockEntities.PATTERN_PROVIDER.get()
            ,worldPosition,
            this.getBlockState());
        level.removeBlockEntity(worldPosition);
        level.setBlockEntity(temp);
    
    }
    //nbtSave에서 값 꺼내서 저장 구현

    private CompoundTag loadCompoundTag(BlockPos pos) {
        return ProviderAdepterBlockEntity.nbtSave.get(pos);
    }
}
//마스터 블럭에 의해서 생성 기존 제공자 블럭엔티티 대체 및 소멸시 기존 블럭 엔티티 생성