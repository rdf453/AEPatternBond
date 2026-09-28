package dev.rdf453.PatternBond.master;

import java.util.List;

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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

public class AdpeterLogic extends PatternProviderLogic {
    private final List<IPatternDetails> pattern;
    private final PatternProviderLogicHost host;

    public AdpeterLogic(IManagedGridNode mainNode, PatternProviderLogicHost host) {
        super(mainNode, host);
    }

    @Override
    public InternalInventory getPatternInv() {
        if (ProviderAdepterBlockEntity.combined.isEmpty())
            return null;
        return ProviderAdepterBlockEntity.combined;
        // 마스터 화면/터미널이 합쳐진 더미 인벤토리를 보도록 반환
    }

    @Override
    public List<IPatternDetails> getAvailablePatterns() {
        return super.getAvailablePatterns();
        // 더미 로직들의 패턴 목록을 합쳐 반환
    }

    @Override
    public boolean pushPattern(IPatternDetails patternDetails, KeyCounter[] inputHolder) {
        return super.pushPattern(patternDetails, inputHolder);
        // 요청된 패턴을 가진 더미 로직에 실행을 위임
    }

    @Override
    public boolean isBusy() {
        return super.isBusy();
        // 위임 가능한 더미가 없거나 전부 바쁜지 판단
    }

    @Override
    public void updatePatterns() {
        // 합성 인벤토리를 디코딩해 어댑터 자체 패턴 목록을 만들고, 마스터 provider 캐시 갱신
        ServerLevel serverLevel = this.getServerLevel();
        if (serverLevel != null) {
            this.pattern.clear();
            this.patternInputs.clear();

            for (ItemStack stack : this.patternInventory) {
                IPatternDetails details = PatternDetailsHelper.decodePattern(stack, serverLevel);
                if (details != null) {
                    this.pattern.add(details);

                    for (IPatternDetails.IInput iinput : details.getInputs()) {
                        for (GenericStack inputCandidate : iinput.getPossibleInputs()) {
                            this.patternInputs.add(inputCandidate.what().dropSecondary());
                        }
                    }
                }
            }

            ICraftingProvider.requestUpdate(this.mainNode);
        }
    }

    @Override
    public void onChangeInventory(AppEngInternalInventory inv, int slot) {
        // 모든 더미의 변화 체크
    }

    @Nullable
    private ServerLevel getServerLevel() {
        return this.host.getBlockEntity().getLevel() instanceof ServerLevel serverLevel ? serverLevel : null;
    }


    // 더미의 패턴이 추가·삭제될 때 마스터의 provider 캐시를 갱신해야 합니다. 메서드 오버라이드만으로 자동 갱신되지는 않아요.
}
