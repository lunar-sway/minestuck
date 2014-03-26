package com.mraof.minestuck.util;

import java.util.ArrayList;

import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemSword;

import com.mraof.minestuck.item.weapon.ItemBlade;
import com.mraof.minestuck.item.weapon.ItemCane;
import com.mraof.minestuck.item.weapon.ItemClub;
import com.mraof.minestuck.item.weapon.ItemHammer;
import com.mraof.minestuck.item.weapon.ItemSickle;
import com.mraof.minestuck.item.weapon.ItemSpork;

public class KindAbstratusList {
	
	static ArrayList<KindAbstratusType> typeList;
	
	public static void registerTypes() {
		if(typeList != null)
			return;
		typeList = new ArrayList();
		typeList.add(new KindAbstratusType("sword").addItemClass(ItemSword.class).addItemClass(ItemBlade.class));
		typeList.add(new KindAbstratusType("bow").addItemClass(ItemBow.class));
		typeList.add(new KindAbstratusType("pickaxe").addItemClass(ItemPickaxe.class));
		typeList.add(new KindAbstratusType("axe").addItemClass(ItemAxe.class));
		typeList.add(new KindAbstratusType("hoe").addItemClass(ItemHoe.class));
		typeList.add(new KindAbstratusType("shovel").addItemClass(ItemSpade.class));
		typeList.add(new KindAbstratusType("hammer").addItemClass(ItemHammer.class));
		typeList.add(new KindAbstratusType("cane").addItemClass(ItemCane.class));
		typeList.add(new KindAbstratusType("club").addItemClass(ItemClub.class));
		typeList.add(new KindAbstratusType("sickle").addItemClass(ItemSickle.class));
		typeList.add(new KindAbstratusType("spork").addItemClass(ItemSpork.class));
	}
	
	public static void registerType(KindAbstratusType type) {
		if(getTypeFromName(type.getUnlocalizedName()) == null)
			typeList.add(type);
	}
	
	public static KindAbstratusType getTypeFromName(String unlocName) {
		for(KindAbstratusType type : typeList)
			if(type.getUnlocalizedName().equals(unlocName))
				return type;
		return null;
	}
	
}
