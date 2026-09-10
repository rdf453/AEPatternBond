package dev.rdf453.Realistic_Water.Mixin;
//import net.minecraft.world.level.redstone.RedstoneWireEvaluator; 참고용
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.level.material.WaterFluid.Source;
import net.minecraft.world.ticks.TickAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.WaterFluid;
import net.minecraft.world.level.material.WaterFluid.Source;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

//랜덤틱으로 증발 부여,강 바다, 호수 바이옴에만 무한물 부여
// 실수 커버용 연결된 원천 삭제 아이템 추가 필요
public class WaterConfig {

    @Mixin (Source.class)
    @Unique 
    class sourceMixin {
        public int infiniteAmount() {
            return Integer.MAX_VALUE;
        }
        

        
    }
    @Mixin (FlowingFluid.class)
    class FlowingFluidMixin {
        @Inject (method = "tick",at = @At("Head"),cancellable = true)
        void realisticWater$onTick(ServerLevel level, BlockPos pos, BlockState blockState, FluidState fluidState, CallbackInfo ci) {
            Realistic_Water$Evaporation(level, pos, fluidState, ci);
        }
        
        void Realistic_Water$Evaporation(ServerLevel level,  BlockPos pos, FluidState fluidState, CallbackInfo ci) {
            if(!fluidState.is(FluidTags.WATER)||!fluidState.isSource()) return ;
                int randomTickSpeed =  level.getGameRules().get(GameRules.RANDOM_TICK_SPEED);
                float temperature = level.getBiome(pos).value().getBaseTemperature();
                //0.000926%*((바이옴온도 -0.15)/0.55)* (randomTickSpeed/3)
                double a = 0.00000926D* ((temperature-0.15d)/0.55d)*(randomTickSpeed/3.0D);

            if(level.getRandom().nextDouble() < a) {
                level.setBlock(pos,Blocks.AIR.defaultBlockState(),Block.UPDATE_ALL);
                ci.cancel();
            }
            
        }

    }
    
    
}
