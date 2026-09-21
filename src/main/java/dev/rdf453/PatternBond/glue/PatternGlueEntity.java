package dev.rdf453.PatternBond.glue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import appeng.blockentity.crafting.PatternProviderBlockEntity;
import dev.rdf453.PatternBond.masterProvider.MasterProviderBlockEntity;
public class PatternGlueEntity extends Entity implements IEntityWithComplexSpawn {
    public Map<BlockPos,Integer> SlotHashMap = new HashMap<>();

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
                slotCount= MasterProviderBlockEntity.sendSlot(level,position);
                SlotHashMap.put(position.immutable(), slotCount);
                totalSlots += slotCount;
            }
        }
        return totalSlots;
    }


}
//영역 표시 
