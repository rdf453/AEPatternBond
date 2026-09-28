package dev.rdf453.PatternBond.master;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import appeng.api.inventories.InternalInventory;
import appeng.blockentity.crafting.PatternProviderBlockEntity;
import appeng.core.definitions.AEBlockEntities;
import appeng.helpers.patternprovider.PatternProviderLogic;
import appeng.util.inv.CombinedInternalInventory;
import dev.rdf453.PatternBond.dummy.DummyBlockEntity;
import dev.rdf453.PatternBond.master.AdpeterLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class ProviderAdepterBlockEntity extends PatternProviderBlockEntity {
    public Map<BlockPos, Integer> SlotHashMap = new HashMap<>();
    public Set<BlockPos> dummyPos = new HashSet<>();
    public static Map<BlockPos, CompoundTag> nbtSave = new HashMap<>();
    public static List<InternalInventory> inventories = new ArrayList<>();
    public static CombinedInternalInventory combined;
    public ProviderAdepterBlockEntity(BlockPos pos, BlockState state) {
        super(AEBlockEntities.PATTERN_PROVIDER.get(), pos, state);

    }

    @Override
    protected PatternProviderLogic createLogic() {
        return new AdpeterLogic(this.getMainNode(), this);
    }

    // 주변 패턴제공자를 더미로 종속화
    @SubscribeEvent
    public void rightClick(PlayerInteractEvent.RightClickBlock e) {
        
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (level == null || level.isClientSide())
            return;
        init();
        combined = new CombinedInternalInventory(inventories.toArray(InternalInventory[]::new));
    }

    private void findProvider(LevelAccessor level, BlockPos pos) {
        Set<BlockPos> visited = new HashSet<>();
        ArrayDeque<BlockPos> stack = new ArrayDeque<>();
        stack.push(pos);

        while (!stack.isEmpty()) {
            BlockPos currentPos = stack.pop();

            if (!visited.add(currentPos))
                continue;

            for (Direction dir : Direction.values()) {
                BlockPos targetPos = currentPos.relative(dir);
                if (visited.contains(targetPos))
                    continue;
                BlockEntity targetEntity = level.getBlockEntity(targetPos);
                if (targetEntity instanceof PatternProviderBlockEntity) {
                    dummyPos.add(targetPos.immutable());
                    stack.push(targetPos);
                }
            }
        }
    }

    private CompoundTag saveProviderDada(BlockEntity e) {
        TagValueOutput out = TagValueOutput.createWithContext(
                ProblemReporter.DISCARDING, level.registryAccess());

        e.saveWithId(out);
        return out.buildResult();

    }

    public int setSlot(LevelAccessor level, BlockPos pos) {
        if (level == null)
            return 0;
        int slotCount = 0;

        BlockEntity bEntity = level.getBlockEntity(pos);

        if (bEntity instanceof PatternProviderBlockEntity) {
            slotCount = getSlot(level, pos);
            SlotHashMap.put(pos.immutable(), slotCount);

        }

        return slotCount;
    }

    public int getSlot(LevelAccessor level, BlockPos pos) {
        if (level == null) {
            return 0;
        }
        if (level.getBlockEntity(pos) instanceof PatternProviderBlockEntity entity) {
            return entity.getLogic().getPatternInv().size();
        }
        return 0;
    }

    private int sumTotalSlot() {
        int temp = 0;
        for (int slotCount : SlotHashMap.values()) {
            temp += slotCount;
        }
        return temp;
    }

    private void init() {
        if (this.dummyPos.isEmpty()) {
            findProvider(level, worldPosition);
            for (BlockPos pos : dummyPos) {
                BlockEntity origin = level.getBlockEntity(pos);

                BlockEntity temp = new DummyBlockEntity(pos, level.getBlockState(pos), worldPosition,
                        setSlot(level, pos));
                nbtSave.put(pos.immutable(), saveProviderDada(origin));
                level.removeBlockEntity(pos);
                level.setBlockEntity(temp);

                if (level.getBlockEntity(pos) instanceof DummyBlockEntity provider) {
                    inventories.add(provider.getLogic().getPatternInv());
                }
            }

        }
    }
}
