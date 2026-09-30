/*
 * This file contains modified code from Applied Energistics 2.
 * Original code Copyright (c) 2013-2020 AlgorithmX2 et al.
 * 
 * Licensed under the GNU Lesser General Public License v3.0 (LGPLv3).
 * Modifications by [rdf453]
 */

package dev.rdf453.PatternBond.master;

import appeng.api.storage.IPatternAccessTermMenuHost;
import appeng.menu.implementations.PatternAccessTermMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
public class AdepterMenu extends PatternAccessTermMenu {
    
    public AdepterMenu(MenuType<?> menuType, int id, Inventory ip, IPatternAccessTermMenuHost host,
            boolean bindInventory) {
        super(menuType, id, ip, host, true);
        // 메뉴타입 생성하고 넣을것
    }


}
//https://github.com/AppliedEnergistics/Applied-Energistics-2/blob/main/src/main/java/appeng/menu/implementations/PatternAccessTermMenu.java