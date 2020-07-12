package com.mraof.minestuck.player;

import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.Debug;
import net.minecraft.item.*;
import net.minecraftforge.common.ToolType;

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
	static ArrayList<String> namesList = new ArrayList<String>();
	
	public static final KindAbstratusType blank = new KindAbstratusType("blank").setSelectable(false);
	public static final KindAbstratusType bladekind = new KindAbstratusType(SWORD, SwordItem.class);
	public static final KindAbstratusType bowkind = new KindAbstratusType(BOW, BowItem.class);
	public static final KindAbstratusType pickaxekind = new KindAbstratusType(PICKAXE, PickaxeItem.class, ToolType.PICKAXE);
	public static final KindAbstratusType axekind = new KindAbstratusType(AXE, AxeItem.class, ToolType.AXE);
	public static final KindAbstratusType hoekind = new KindAbstratusType(HOE, HoeItem.class);
	public static final KindAbstratusType spadekind = new KindAbstratusType(SHOVEL, ShovelItem.class, ToolType.SHOVEL);
	public static final KindAbstratusType hammerkind = new KindAbstratusType(HAMMER);
	public static final KindAbstratusType canekind = new KindAbstratusType(CANE);
	public static final KindAbstratusType clubkind = new KindAbstratusType(CLUB);
	public static final KindAbstratusType sicklekind = new KindAbstratusType(SICKLE);
	public static final KindAbstratusType spoonkind = new KindAbstratusType("spoon");
	public static final KindAbstratusType forkkind = new KindAbstratusType("fork");
	public static final KindAbstratusType fistkind = new KindAbstratusType("fist").includesFist();
	public static final KindAbstratusType gauntletkind = new KindAbstratusType("gauntlet");
	public static final KindAbstratusType clawkind = new KindAbstratusType("claw");
	public static final KindAbstratusType dicekind = new KindAbstratusType("dice");
	public static final KindAbstratusType fshngrodkind = new KindAbstratusType("fishing_rod", FishingRodItem.class);
	public static final KindAbstratusType jokerkind = new KindAbstratusType("joker", MSItems.ZILLYHOO_HAMMER, MSItems.CUTLASS_OF_ZILLYWAIR, MSItems.CLAW_SICKLE).setSelectable(false);
	
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
