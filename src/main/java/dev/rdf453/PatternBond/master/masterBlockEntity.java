package dev.rdf453.PatternBond.master;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import appeng.blockentity.crafting.PatternProviderBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class masterBlockEntity extends PatternProviderBlockEntity   {
    public Map<BlockPos,Integer> SlotHashMap = new HashMap<>();
    public Set<BlockPos> providerPos = new HashSet<>();
    
    @SubscribeEvent 
    public void rightClick(PlayerInteractEvent.RightClickBlock e) {
        if(this.providerPos.isEmpty()) {
                    findProvider(level,worldPosition);
                }
    }
    //수정 필요
    private void findProvider(LevelAccessor level,BlockPos pos) {
        for(Direction dir : Direction.values()) {
            BlockPos targetPos = pos.relative(dir);
            BlockEntity targetEntity = level.getBlockEntity(targetPos);
            if(targetEntity instanceof PatternProviderBlockEntity) {
                providerPos.add(targetPos.immutable());
                pos = targetPos;
                continue;
            }
        }
    }

    public int setSlot(LevelAccessor level, BlockPos pos,Set<PatternGlueEntity> data) {
        if(level == null) return 0;
        int slotCount;
        int totalSlots = 0;
        for(BlockPos position : BlockPos.betweenClosed(getBoundingBox())) {
            BlockEntity bEntity = level.getBlockEntity(position);

            if(bEntity instanceof PatternProviderBlockEntity) {
                slotCount= getSlot(level,position);
                SlotHashMap.put(position.immutable(), slotCount);
                totalSlots += slotCount;
            }
        }
        return totalSlots;
    }

    public int getSlot(LevelAccessor level,BlockPos pos) {
        if(level == null) {
            return 0;
        }              
            if(level.getBlockEntity(pos) instanceof PatternProviderBlockEntity entity) {
                return entity.getLogic().getPatternInv().size();   
        }
        return 0;
    }
}
