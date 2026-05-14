package com.mraof.minestuck.player;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KindAbstratusList {
	
	public static final String SWORD = "minestuck.sword";
	public static final String HALF_SWORD = "minestuck.half_sword";
	public static final String BOW = "minestuck.bow";
	public static final String PICKAXE = "minestuck.pickaxe";
	public static final String AXE = "minestuck.axe";
	public static final String HOE = "minestuck.hoe";
	public static final String SHOVEL = "minestuck.shovel";
	public static final String HAMMER = "minestuck.hammer";
	public static final String KNIFE = "minestuck.knife";
	public static final String BATON = "minestuck.baton";
	public static final String DICE = "minestuck.dice";
	public static final String CANE = "minestuck.cane";
	public static final String CLUB = "minestuck.club";
	public static final String CLAW = "minestuck.claw";
	public static final String CHAINSAW = "minestuck.chainsaw";
	public static final String LANCE = "minestuck.lance";
	public static final String FAN = "minestuck.fan";
	public static final String SICKLE = "minestuck.sickle";
	public static final String SPORK = "minestuck.spork";
	public static final String SCYTHE = "minestuck.scythe";
	public static final String STAFF = "minestuck.staff";
	public static final String SPOON = "minestuck.spoon";
	public static final String FORK = "minestuck.fork";
	//	public static final String SPORK = "minestuck.spork";
	public static final String NEEDLE = "minestuck.needle";
	public static final String WAND = "minestuck.wand";
	public static final String STAR = "minestuck.star";
	public static final String CAKE = "minestuck.cake";
	public static final String CROSSBOW = "minestuck.crossbow";
	
	private static ArrayList<KindAbstratusType> typeList = new ArrayList<>();
	// [addons]
	private static final List<KindAbstratusType> externalTypes = new ArrayList<>();
	
	/*	public static void registerTypes()
		{
			if(typeList != null)
				return;
			typeList = new ArrayList<>();
			typeList.add(new KindAbstratusType(SWORD).addItemClass(SwordItem.class).addItemTag(MSTags.Items.KIND_SWORD));
			typeList.add(new KindAbstratusType(BOW).addItemClass(BowItem.class).addItemTag(MSTags.Items.KIND_BOW));
			typeList.add(new KindAbstratusType(PICKAXE).addItemClass(PickaxeItem.class).addItemTag(MSTags.Items.KIND_PICKAXE));
			typeList.add(new KindAbstratusType(AXE).addItemClass(AxeItem.class).addItemTag(MSTags.Items.KIND_AXE));
			typeList.add(new KindAbstratusType(HOE).addItemClass(HoeItem.class).addItemTag(MSTags.Items.KIND_HOE));
			typeList.add(new KindAbstratusType(SHOVEL).addItemClass(ShovelItem.class).addItemTag(MSTags.Items.KIND_SHOVEL));
			
			typeList.add(new KindAbstratusType(HAMMER).addItemTag(MSTags.Items.KIND_HAMMER));
			typeList.add(new KindAbstratusType(KNIFE).addItemTag(MSTags.Items.KIND_KNIFE));
			typeList.add(new KindAbstratusType(BATON).addItemTag(MSTags.Items.KIND_BATON));
			typeList.add(new KindAbstratusType(DICE).addItemTag(MSTags.Items.KIND_DICE));
			typeList.add(new KindAbstratusType(CANE).addItemTag(MSTags.Items.KIND_CANE));
			typeList.add(new KindAbstratusType(CLAW).addItemTag(MSTags.Items.KIND_CLAW));
			typeList.add(new KindAbstratusType(CHAINSAW).addItemTag(MSTags.Items.KIND_CHAINSAW));
			typeList.add(new KindAbstratusType(CLUB).addItemTag(MSTags.Items.KIND_CLUB));
			typeList.add(new KindAbstratusType(SICKLE).addItemTag(MSTags.Items.KIND_SICKLE));
			typeList.add(new KindAbstratusType(STAFF).addItemTag(MSTags.Items.KIND_STAFF));
			typeList.add(new KindAbstratusType(SPOON).addItemTag(MSTags.Items.KIND_SPOON));
			typeList.add(new KindAbstratusType(FORK).addItemTag(MSTags.Items.KIND_FORK));
	//		typeList.add(new KindAbstratusType(SPORK).addItemTag(MSTags.Items.KIND_SPORK));
			typeList.add(new KindAbstratusType(NEEDLE).addItemTag(MSTags.Items.KIND_NEEDLE));
			typeList.add(new KindAbstratusType(WAND).addItemTag(MSTags.Items.KIND_WAND));
			typeList.add(new KindAbstratusType(STAR).addItemTag(MSTags.Items.KIND_STAR));
			typeList.add(new KindAbstratusType(CAKE).addItemId(Items.CAKE).addItemTag(MSTags.Items.KIND_CAKE));
		}*/
	
	public static void reloadFromTags()
	{
		ArrayList<KindAbstratusType> newList = new ArrayList<>();
		
		BuiltInRegistries.ITEM.getTagNames()
				.filter(key -> key.location().getNamespace().equals(Minestuck.MOD_ID)
						&& key.location().getPath().startsWith("kind/")
						&& !key.location().getPath().equals("weapons"))
				.sorted(Comparator.comparing(key -> key.location().getPath()))
				.forEach(key -> {
					String suffix = key.location().getPath().substring("kind/".length());
					String typeName = Minestuck.MOD_ID + "." + suffix;
					newList.add(new KindAbstratusType(typeName).addItemTag(key));
				});
		for(KindAbstratusType external : externalTypes)
			if(newList.stream().noneMatch(t -> t.getUnlocalizedName().equals(external.getUnlocalizedName())))
				newList.add(external);
		
		typeList = newList;
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
