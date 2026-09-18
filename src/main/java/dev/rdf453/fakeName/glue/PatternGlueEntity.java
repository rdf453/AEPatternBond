package dev.rdf453.fakeName.glue;

import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;

public class PatternGlueEntity extends BlockEntity implements IEntityWithComplexSpawn {
    
    public static AABB span(BlockPos start, BlockPos end) {
        return new AABB(Vec3.atLowerCornerOf(start),Vec3.atLowerCornerOf(end)).expandTowards(1,1,1);
    }

    public static boolean isGlued(LevelAccess level, BlockPos blockPos, Direction direction, Set<PatternGlueEntity> cached) {
        BlockPos targetPos = blockPos.relative(direction);
        for (PatternGlueEntity pEntity: cached) {
            if(pEntity.contain(blockPos)&& pEntity.contain(targetPos)) {
                return true;
            }
        }
        for(PatternGlueEntity pEntity: level.getEntitesOfClass(PatternGlueEntity.class,
            span(blockPos, targetPos).inflate(16))) {
                if(!pEntity.contain(blockPos)||pEntity.contain(targetPos) {
                    
                })
            }
    } 

}
