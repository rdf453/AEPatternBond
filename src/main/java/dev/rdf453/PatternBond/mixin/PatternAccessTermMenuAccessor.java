package dev.rdf453.PatternBond.mixin;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import appeng.api.networking.IGrid;
import appeng.api.storage.IPatternAccessTermMenuHost;
import appeng.helpers.patternprovider.PatternContainer;
import appeng.menu.implementations.PatternAccessTermMenu;
import appeng.util.inv.AppEngInternalInventory;
import dev.rdf453.PatternBond.dummy.DummyBlockEntity;
import dev.rdf453.PatternBond.master.AdepterMenu;
import dev.rdf453.PatternBond.master.ProviderAdepterBlockEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

@Mixin(PatternAccessTermMenu.class)
public interface PatternAccessTermMenuAccessor {

        @Invoker("getGrid")
        public IGrid getGrid();

        @Invoker("visitPatternProviderHosts")
        public <T extends PatternContainer> void visitPatternProviderHosts(IGrid grid, Class<T> machineClass,
                        @Coerce Object state);

        @Accessor("pinnedHosts")
        public Set<PatternContainer> pinnedHosts();

        @Accessor("host")
        public IPatternAccessTermMenuHost host();

        
}
