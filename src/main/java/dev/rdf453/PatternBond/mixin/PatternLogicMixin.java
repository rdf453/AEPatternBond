package dev.rdf453.PatternBond.mixin;

import java.util.List;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import appeng.api.crafting.IPatternDetails;
import appeng.api.networking.IManagedGridNode;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.helpers.patternprovider.PatternProviderLogic;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import appeng.util.inv.AppEngInternalInventory;

@Mixin(PatternProviderLogic.class)
    public interface PatternLogicMixin {
        @Accessor("mainNode")
        public  IManagedGridNode mainNode();

        @Accessor ("patterns")
        public  List<IPatternDetails> patterns();


        @Accessor ("patternInputs")
        public Set<AEKey> patternInputs();

        @Accessor ("host")
        public PatternProviderLogicHost host();

        @Accessor ("sendList")
        public List<GenericStack> sendList();

        @Mutable 
        @Accessor ("patternInventory")
        public AppEngInternalInventory patternInventory(AppEngInternalInventory inventory);





    }