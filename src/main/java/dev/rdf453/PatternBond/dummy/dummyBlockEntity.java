package dev.rdf453.PatternBond.dummy;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import appeng.core.definitions.AEBlockEntities;

public class dummyBlockEntity extends BlockEntity{
    public dummyBlockEntity(BlockPos pos,BlockState state) {
        super(PATTERN_PROVIDER, pos, state);
        //나중에 더미 블럭 엔티티 타입 생성해서 넣을것
    }
}
//마스터 블럭에 의해서 생성 기존 제공자 블럭엔티티 대체 및 소멸시 기존 블럭 엔티티 생성