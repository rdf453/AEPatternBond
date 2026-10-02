package dev.rdf453.PatternBond.master;

import dev.rdf453.PatternBond.registry.Register;
import appeng.block.AEBaseEntityBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.ValueInput;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ProviderAdepterBlock extends AEBaseEntityBlock<ProviderAdepterBlockEntity>{
    
    public ProviderAdepterBlock(Properties props) {
        super(props);
    }

    

    public static final DeferredBlock<ProviderAdepterBlock> BLOCK_HOLDER = Register.BLOCKS.registerBlock(
        "Pattern Provider Adepter",
        ProviderAdepterBlock::new,
        properties -> properties.destroyTime(2.5f));

    public static final DeferredItem<BlockItem> BLOCK_ITEM = Register.ITEMS.registerSimpleBlockItem("Pattern Provider Adepter",BLOCK_HOLDER);

}
//https://github.com/AppliedEnergistics/Applied-Energistics-2/blob/main/src/main/java/appeng/block/AEBaseEntityBlock.java