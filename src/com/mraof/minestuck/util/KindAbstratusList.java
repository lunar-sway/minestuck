package com.mraof.minestuck.util;

import net.minecraft.item.*;

import java.util.ArrayList;
import java.util.List;

public class KindAbstratusList {
	
	static ArrayList<KindAbstratusType> typeList;
	
	public static void registerTypes()
	{
		if(typeList != null)
			return;
		typeList = new ArrayList<KindAbstratusType>();
		typeList.add(new KindAbstratusType("sword").addItemClass(ItemSword.class));
		typeList.add(new KindAbstratusType("bow").addItemClass(ItemBow.class));
		typeList.add(new KindAbstratusType("pickaxe").addItemClass(ItemPickaxe.class));
		typeList.add(new KindAbstratusType("axe").addItemClass(ItemAxe.class));
		typeList.add(new KindAbstratusType("hoe").addItemClass(ItemHoe.class));
		typeList.add(new KindAbstratusType("shovel").addItemClass(ItemSpade.class));
		typeList.add(new KindAbstratusType("hammer"));
		typeList.add(new KindAbstratusType("cane"));
		typeList.add(new KindAbstratusType("club"));
		typeList.add(new KindAbstratusType("sickle"));
		typeList.add(new KindAbstratusType("spoon"));
		typeList.add(new KindAbstratusType("fork"));
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
	
	public static List<KindAbstratusType> getTypeList() {
		return new ArrayList<KindAbstratusType>(typeList);
	}
	
}
