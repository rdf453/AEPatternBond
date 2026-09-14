package dev.rdf453.fakeName.glue;

import net.minecraft.world.item.Item;
import net.neoforged.fml.common.EventBusSubscriber;

//pattern provider을 묶기
@EventBusSubscriber 
public class PatternGlueItem extends Item {
    
    public PatternGlueItme(Properties prob) {
        super(prob);
    }


}
//https://github.com/Creators-of-Create/Create/blob/mc1.21.1/dev/src/main/java/com/simibubi/create/content/contraptions/glue/SuperGlueItem.java
//참고용