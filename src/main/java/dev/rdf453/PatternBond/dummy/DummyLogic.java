package dev.rdf453.PatternBond.dummy;

import java.util.List;

import appeng.api.crafting.IPatternDetails;
import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.crafting.ICraftingProvider;
import appeng.helpers.patternprovider.PatternProviderLogic;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import appeng.util.inv.AppEngInternalInventory;
import dev.rdf453.PatternBond.master.AdpeterLogic;
import dev.rdf453.PatternBond.mixin.PatternLogicMixin;

public class DummyLogic extends PatternProviderLogic {
    //기존 제공자의 인벤토리 연결 복사해놓고 붙이기
    PatternLogicMixin ac = (PatternLogicMixin) this;
    DummyBlockEntity de = (DummyBlockEntity)ac.host().getBlockEntity();
    AdpeterLogic masterLogic = (AdpeterLogic) de.getMaster().getLogic();

    public DummyLogic(IManagedGridNode mainNode, PatternProviderLogicHost host, int patternInventorySize) {
        super(mainNode, host, patternInventorySize);
    }

    @Override
    public void onChangeInventory(AppEngInternalInventory inv, int slot) {
        masterLogic.onChangeInventory(inv, slot);
        copy();
    }

    @Override
    public void updatePatterns() {
        this.saveChanges();
        masterLogic.updatePatterns();
    }

    public void sync() {
        copy();
        ICraftingProvider.requestUpdate(ac.mainNode());
    }
    
    @Override
    public List<IPatternDetails> getAvailablePatterns() {
        return ac.patterns();
        // 니가 일해
    }
    private void copy() {
        ac.patterns().clear();
        ac.patterns().addAll(masterLogic.ac.patterns());
        ac.patternInputs().clear();
        ac.patternInputs().addAll(masterLogic.ac.patternInputs());
    }
}
