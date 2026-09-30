/*
 * This file contains modified code from Applied Energistics 2.
 * Original code Copyright (c) 2013-2020 AlgorithmX2 et al.
 * 
 * Licensed under the GNU Lesser General Public License v3.0 (LGPLv3).
 * Modifications by [rdf453]
 */

package dev.rdf453.PatternBond.master;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import appeng.api.crafting.IPatternDetails;
import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.inventories.InternalInventory;
import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.crafting.ICraftingProvider;
import appeng.api.stacks.GenericStack;
import appeng.api.stacks.KeyCounter;
import appeng.helpers.patternprovider.PatternProviderLogic;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import appeng.util.inv.AppEngInternalInventory;
import dev.rdf453.PatternBond.dummy.DummyLogic;
import dev.rdf453.PatternBond.mixin.PatternLogicAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

public class AdpeterLogic extends PatternProviderLogic {
    public PatternLogicAccessor ac = (PatternLogicAccessor) this;
    private final ProviderAdepterBlockEntity be;

    public AdpeterLogic(IManagedGridNode mainNode, PatternProviderLogicHost host) {
        super(mainNode, host);
        this.be = (ProviderAdepterBlockEntity)host.getBlockEntity();
    }

    public AdpeterLogic(IManagedGridNode mainNode, PatternProviderLogicHost host, int patternInventorySize){
        //상수 초기화 직접 할것
        super(mainNode, host,patternInventorySize);
        this.be = (ProviderAdepterBlockEntity)host.getBlockEntity();
    }

    

    @Override
    public InternalInventory getPatternInv() {
        if (be.combined.isEmpty())
            return null;
        return be.combined;
        // 마스터 화면/터미널이 합쳐진 더미 인벤토리를 보도록 반환
    }

    @Override
    public List<IPatternDetails> getAvailablePatterns() {
        return List.of();
        // 일하지마
    }

    @Override
    public boolean pushPattern(IPatternDetails patternDetails, KeyCounter[] inputHolder) {
        return false;
        // 일하지마
    }

    @Override
    public boolean isBusy() {
        return true; //닌 노는게 일이다
    }

    @Override
    public void updatePatterns() {
        // 합성 인벤토리를 디코딩해 어댑터 자체 패턴 목록을 만들고, 마스터 provider 캐시 갱신
        be.reNewalCombinedInv();
        ServerLevel serverLevel = this.getServerLevel();
        if (serverLevel != null) {
            ac.patterns().clear();
            ac.patternInputs().clear();

            for (ItemStack stack : be.combined) {
                IPatternDetails details = PatternDetailsHelper.decodePattern(stack, serverLevel);
                if (details != null) {
                    ac.patterns().add(details);

                    for (IPatternDetails.IInput iinput : details.getInputs()) {
                        for (GenericStack inputCandidate : iinput.getPossibleInputs()) {
                            ac.patternInputs().add(inputCandidate.what().dropSecondary());
                        }
                    }
                }
            }

            syncPatternsFromMaster();
        }
        
    }

    @Override
    public void onChangeInventory(AppEngInternalInventory inv, int slot) {
        // 모든 더미의 변화 체크
        updatePatterns();
        be.reNewalCombinedInv();
    }

    @Nullable
    private ServerLevel getServerLevel() {
        return ac.host().getBlockEntity().getLevel() instanceof ServerLevel serverLevel ? serverLevel : null;
    }

    // 더미의 패턴이 추가·삭제될 때 마스터의 provider 캐시를 갱신해야 합니다. 메서드 오버라이드만으로 자동 갱신되지는 않아요.

    public void syncPatternsFromMaster() {
        for(BlockPos pos :be.dummyPos ){
            if (be.getDummy(pos).getLogic() instanceof DummyLogic logic)
                logic.sync();
        }
    }
}
