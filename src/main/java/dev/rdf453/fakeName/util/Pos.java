package dev.rdf453.fakeName.util;

import net.minecraft.core.BlockPos;

public class Pos {
    private BlockPos firBlockPos;
    private BlockPos secBlockPos;

    public void setFirstBlockPos(BlockPos pos) {
        this.firBlockPos = pos;
    }

    public void setSecondBlockPos(BlockPos pos) {
        this.secBlockPos = pos;
    }

    public Area getSpaceBlockPos() {
        return new Area(firBlockPos,secBlockPos);
    }

    public record Area(BlockPos first,BlockPos second) {
    }
}
