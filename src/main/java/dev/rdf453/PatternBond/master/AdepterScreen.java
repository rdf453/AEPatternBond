package dev.rdf453.PatternBond.master;

import net.minecraft.network.chat.Component;

import appeng.client.gui.me.patternaccess.PatternAccessTermScreen;
import appeng.client.gui.style.ScreenStyle;
import net.minecraft.world.entity.player.Inventory;



public class AdepterScreen extends PatternAccessTermScreen<AdepterMenu> {
    //GuideME 의존성 설정 필요
    public AdepterScreen(AdepterMenu menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu,playerInventory,title,style);
    }
}
//https://github.com/AppliedEnergistics/Applied-Energistics-2/blob/main/src/client/java/appeng/client/gui/me/patternaccess/PatternAccessTermScreen.java