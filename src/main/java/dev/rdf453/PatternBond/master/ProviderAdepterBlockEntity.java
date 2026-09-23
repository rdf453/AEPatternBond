package dev.rdf453.PatternBond.master;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import appeng.blockentity.crafting.PatternProviderBlockEntity;
import appeng.core.definitions.AEBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class ProviderAdepterBlockEntity extends PatternProviderBlockEntity   {
    public Map<BlockPos,Integer> SlotHashMap = new HashMap<>();
    public Set<BlockPos> providerPos = new HashSet<>();
    
    public ProviderAdepterBlockEntity(BlockPos pos,BlockState state) {
        super(AEBlockEntities.PATTERN_PROVIDER.get(), pos, state);
        
    }
    //주변 패턴제공자를 더미로 종속화
    @SubscribeEvent 
    public void rightClick(PlayerInteractEvent.RightClickBlock e) {
        if(this.providerPos.isEmpty()) {
                    findProvider(level,worldPosition);
                }
    }
    //수정 필요
    private void findProvider(LevelAccessor level,BlockPos pos) {
        Set<BlockPos> visited = new HashSet<>();
        ArrayDeque<BlockPos> stack = new ArrayDeque<>();
        stack.push(pos);

        while(!stack.isEmpty()){
            BlockPos currentPos = stack.pop();

            if(!visited.add(currentPos))
                continue;

            for(Direction dir : Direction.values()) {
                BlockPos targetPos = currentPos.relative(dir);
                if(visited.contains(targetPos)) 
                    continue;
                BlockEntity targetEntity = level.getBlockEntity(targetPos);
                if(targetEntity instanceof PatternProviderBlockEntity) {
                    providerPos.add(targetPos.immutable());
                    stack.push(targetPos);
            }
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
