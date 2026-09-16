package dev.rdf453.fakeName.glue;

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

    public static boolean isGlued()

}
