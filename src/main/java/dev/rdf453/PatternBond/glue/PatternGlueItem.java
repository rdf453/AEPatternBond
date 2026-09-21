package dev.rdf453.fakeName.glue;

import net.minecraft.core.BlockPos;
import net.minecraft.util.TriState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;


import appeng.blockentity.crafting.PatternProviderBlockEntity;

//pattern provider을 묶기
@EventBusSubscriber 
public class PatternGlueItem extends Item {
    
    @SubscribeEvent 
    public static void whenUsedGlue(PlayerInteractEvent.RightClickBlock e) {
        if (!(e.getItemStack().getItem() instanceof PatternGlueItem)) {
            return;
        }
        
        if (e.getLevel().getBlockEntity(e.getPos()) instanceof PatternProviderBlockEntity) {
            e.setUseBlock(TriState.FALSE);
        }
        
        
    }

    public PatternGlueItem(Properties prob) {
        super(prob); 
    }

    @Override 
    public boolean canDestroyBlock(ItemStack itemStack, BlockState state, Level level, BlockPos pos, LivingEntity user) {
        return false;
    }

}
//https://github.com/Creators-of-Create/Create/blob/mc1.21.1/dev/src/main/java/com/simibubi/create/content/contraptions/glue/SuperGlueItem.java
//참고용