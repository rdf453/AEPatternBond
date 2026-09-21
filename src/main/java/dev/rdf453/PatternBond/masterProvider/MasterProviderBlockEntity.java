package dev.rdf453.PatternBond.masterProvider;

import java.util.Set;

import appeng.blockentity.crafting.PatternProviderBlockEntity;
import appeng.helpers.patternprovider.PatternProviderLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;


public class MasterProviderBlockEntity extends PatternProviderBlockEntity{
    
    

    public static int sendSlot(LevelAccessor level,BlockPos pos) {
        if(level == null) {
            return 0;
        }              
            if(level.getBlockEntity(pos) instanceof PatternProviderBlockEntity entity) {
                return entity.getLogic().getPatternInv().size();   
        }
        return 0;
    }


}
//엔티티의 통합 블럭 엔티티
//슬롯 매핑을 위한 위치 정보, 슬롯 갯수 반환
//https://github.com/AppliedEnergistics/Applied-Energistics-2/blob/main/src/main/java/appeng/blockentity/crafting/PatternProviderBlockEntity.java