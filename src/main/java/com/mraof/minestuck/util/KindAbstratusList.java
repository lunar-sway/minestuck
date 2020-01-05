package com.mraof.minestuck.util;

import net.minecraft.item.*;

import java.util.ArrayList;
import java.util.List;

public class KindAbstratusList {
	
	public static final String SWORD = "minestuck.sword";
	public static final String BOW = "minestuck.bow";
	public static final String PICKAXE = "minestuck.pickaxe";
	public static final String AXE = "minestuck.axe";
	public static final String HOE = "minestuck.hoe";
	public static final String SHOVEL = "minestuck.shovel";
	public static final String HAMMER = "minestuck.hammer";
	public static final String CANE = "minestuck.cane";
	public static final String CLUB = "minestuck.club";
	public static final String SICKLE = "minestuck.sickle";
	public static final String SPORK = "minestuck.spork";
	
	static ArrayList<KindAbstratusType> typeList;
	
	public static void registerTypes()
	{
		if(typeList != null)
			return;
		typeList = new ArrayList<>();
		typeList.add(new KindAbstratusType(SWORD).addItemClass(SwordItem.class));
		typeList.add(new KindAbstratusType(BOW).addItemClass(BowItem.class));
		typeList.add(new KindAbstratusType(PICKAXE).addItemClass(PickaxeItem.class));
		typeList.add(new KindAbstratusType(AXE).addItemClass(AxeItem.class));
		typeList.add(new KindAbstratusType(HOE).addItemClass(HoeItem.class));
		typeList.add(new KindAbstratusType(SHOVEL).addItemClass(ShovelItem.class));
		typeList.add(new KindAbstratusType(HAMMER));
		typeList.add(new KindAbstratusType(CANE));
		typeList.add(new KindAbstratusType(CLUB));
		typeList.add(new KindAbstratusType(SICKLE));
		typeList.add(new KindAbstratusType(SPORK));
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
