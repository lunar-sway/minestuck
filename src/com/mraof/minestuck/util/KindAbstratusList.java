package com.mraof.minestuck.util;

import net.minecraft.item.*;

import java.util.ArrayList;
import java.util.List;

import com.mraof.minestuck.item.MinestuckItems;

public class KindAbstratusList {
	
	static ArrayList<KindAbstratusType> typeList;
	static ArrayList<String> namesList = new ArrayList<String>();
	
	public static void registerTypes()
	{
		if(typeList != null)
			return;
		typeList = new ArrayList<KindAbstratusType>();
		typeList.add(new KindAbstratusType("blank"));
		typeList.add(new KindAbstratusType("sword").addItemClass(ItemSword.class).addToolClass("sword"));
		typeList.add(new KindAbstratusType("bow").addItemClass(ItemBow.class));
		typeList.add(new KindAbstratusType("pickaxe").addItemClass(ItemPickaxe.class));
		typeList.add(new KindAbstratusType("axe").addItemClass(ItemAxe.class).addToolClass("axe"));
		typeList.add(new KindAbstratusType("hoe").addItemClass(ItemHoe.class));
		typeList.add(new KindAbstratusType("shovel").addItemClass(ItemSpade.class).addToolClass("shovel"));
		typeList.add(new KindAbstratusType("hammer"));
		typeList.add(new KindAbstratusType("cane"));
		typeList.add(new KindAbstratusType("club"));
		typeList.add(new KindAbstratusType("sickle").addToolClass("sickle"));
		typeList.add(new KindAbstratusType("spoon"));
		typeList.add(new KindAbstratusType("fork"));
		typeList.add(new KindAbstratusType("fist").includesFist());
		typeList.add(new KindAbstratusType("gauntlet").addToolClass("gauntlet"));
		typeList.add(new KindAbstratusType("claw").addItemId(MinestuckItems.catClaws));
		typeList.add(new KindAbstratusType("dice"));
		typeList.add(new KindAbstratusType("fishing_rod").addItemClass(ItemFishingRod.class));
		
		registerNames();
	}
	
	public static void registerType(KindAbstratusType type) {
		if(getTypeFromName(type.getUnlocalizedName()) == null)
		{
			typeList.add(type);
			//namesList.add(type.getUnlocalizedName());
		}
	}
	
	public static void registerNames()
	{
		for(KindAbstratusType type : typeList)
			namesList.add(type.getUnlocalizedName());
	}
	
	public static KindAbstratusType getTypeFromName(String unlocName) {
		for(KindAbstratusType type : typeList)
			if(type.getUnlocalizedName().equals(unlocName))
				return type;
		return null;
	}
	
	public static List<KindAbstratusType> getTypeList() {
		return new ArrayList<KindAbstratusType>(typeList);
	}
	
	public static List<String> getNamesList()
	{
		return new ArrayList<String>(namesList);
	}
}
