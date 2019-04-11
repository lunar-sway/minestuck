package com.mraof.minestuck.util;

import net.minecraft.item.*;

import java.util.ArrayList;
import java.util.List;

import com.mraof.minestuck.item.MinestuckItems;

public class KindAbstratusList {
	
	static ArrayList<KindAbstratusType> typeList;
	static ArrayList<String> namesList = new ArrayList<String>();
	
	public static final KindAbstratusType blank = new KindAbstratusType("blank");
	public static final KindAbstratusType bladekind = new KindAbstratusType("sword", ItemSword.class, "sword");
	public static final KindAbstratusType bowkind = new KindAbstratusType("bow", ItemBow.class);
	public static final KindAbstratusType pickaxekind = new KindAbstratusType("pickaxe", ItemPickaxe.class, "pickaxe");
	public static final KindAbstratusType axekind = new KindAbstratusType("axe", ItemAxe.class, "axe");
	public static final KindAbstratusType hoekind = new KindAbstratusType("hoe", ItemHoe.class, "hoe");
	public static final KindAbstratusType spadekind = new KindAbstratusType("shovel", ItemSpade.class, "shovel");
	public static final KindAbstratusType hammerkind = new KindAbstratusType("hammer", "hammer");
	public static final KindAbstratusType canekind = new KindAbstratusType("cane");
	public static final KindAbstratusType clubkind = new KindAbstratusType("club");
	public static final KindAbstratusType sicklekind = new KindAbstratusType("sickle", "sickle");
	public static final KindAbstratusType spoonkind = new KindAbstratusType("spoon");
	public static final KindAbstratusType forkkind = new KindAbstratusType("fork");
	public static final KindAbstratusType fistkind = new KindAbstratusType("fist").includesFist();
	public static final KindAbstratusType gauntletkind = new KindAbstratusType("gauntlet", "gauntlet");
	public static final KindAbstratusType clawkind = new KindAbstratusType("claw");
	public static final KindAbstratusType dicekind = new KindAbstratusType("dice");
	public static final KindAbstratusType fshngrodkind = new KindAbstratusType("fishing_rod", ItemFishingRod.class);
	public static final KindAbstratusType jokerkind = new KindAbstratusType("joker", MinestuckItems.zillyhooHammer, MinestuckItems.zillywairCutlass, MinestuckItems.clawSickle);
	
	public static void registerTypes()
	{
		if(typeList != null)
			return;
		
		typeList = new ArrayList<>();
		registerType(blank);
		registerType(bladekind);
		registerType(bowkind);
		registerType(pickaxekind);
		registerType(axekind);
		registerType(hoekind);
		registerType(spadekind);
		registerType(hammerkind);
		registerType(canekind);
		registerType(clubkind);
		registerType(sicklekind);
		registerType(spoonkind);
		registerType(forkkind);
		registerType(fistkind);
		registerType(gauntletkind);
		registerType(clawkind);
		registerType(dicekind);
		registerType(fshngrodkind);
		registerType(jokerkind);
		
		
		
	}
	
	public static void registerType(KindAbstratusType type) {
		if(getTypeFromName(type.getUnlocalizedName()) == null)
		{
			typeList.add(type);
			namesList.add(type.getUnlocalizedName());
		}
		else Debug.warnf("The %s abstrata already exists!", type.getUnlocalizedName());
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
