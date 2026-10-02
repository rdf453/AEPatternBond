package dev.rdf453.PatternBond.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Register {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister
            .create(Registries.BLOCK_ENTITY_TYPE, "ae_provider_adepter");

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks("ae_provider_adepter");
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems("ae_provider_adepter");
}
