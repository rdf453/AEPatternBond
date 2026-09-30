package dev.rdf453.PatternBond.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import appeng.helpers.patternprovider.PatternContainer;
import appeng.menu.implementations.PatternAccessTermMenu;
import dev.rdf453.PatternBond.dummy.DummyBlockEntity;
import dev.rdf453.PatternBond.master.AdepterMenu;
import dev.rdf453.PatternBond.master.ProviderAdepterBlockEntity;

@Mixin(PatternAccessTermMenu.class)
public abstract class AdepterMenuMixin {
    @Inject(method = "isVisible", at = @At("HEAD"), cancellable = true)
    private void patternbond$filterAdapterProviders(
            PatternContainer container,
            CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof AdepterMenu menu) {
            boolean visible = container instanceof DummyBlockEntity dummy
                    && menu.getBlockEntity() instanceof ProviderAdepterBlockEntity adapter
                    && dummy.masterPos != null
                    && dummy.masterPos.equals(adapter.getBlockPos());

            cir.setReturnValue(visible);
        }
    }
}
