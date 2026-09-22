package dev.rdf453.PatternBond.registry;

import dev.rdf453.PatternBond.glue.PatternGlueEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EntityRegistry {
    
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE = 
    DeferredRegister.create(Registries.ENTITY_TYPE, "ae_pattern_bond");
    
    public static final DeferredHolder<EntityType<?>, EntityType<PatternGlueEntity>> ENTITY_HOLDER =
}
