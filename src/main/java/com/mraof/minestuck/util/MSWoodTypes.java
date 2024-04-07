package com.mraof.minestuck.util;

import com.mraof.minestuck.Minestuck;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.world.level.block.state.properties.WoodType;

public class MSWoodTypes
{
	public static final WoodType CARVED = WoodType.register(new WoodType(Minestuck.MOD_ID + ":carved", MSBlockSetType.CARVED));
	public static final WoodType DEAD = WoodType.register(new WoodType(Minestuck.MOD_ID + ":dead", MSBlockSetType.DEAD));
	public static final WoodType END = WoodType.register(new WoodType(Minestuck.MOD_ID + ":end", MSBlockSetType.END));
	public static final WoodType FROST = WoodType.register(new WoodType(Minestuck.MOD_ID + ":frost", MSBlockSetType.FROST));
	public static final WoodType GLOWING = WoodType.register(new WoodType(Minestuck.MOD_ID + ":glowing", MSBlockSetType.GLOWING));
	public static final WoodType RAINBOW = WoodType.register(new WoodType(Minestuck.MOD_ID + ":rainbow", MSBlockSetType.RAINBOW));
	public static final WoodType SHADEWOOD = WoodType.register(new WoodType(Minestuck.MOD_ID + ":shadewood", MSBlockSetType.SHADEWOOD));
	public static final WoodType TREATED = WoodType.register(new WoodType(Minestuck.MOD_ID + ":treated", MSBlockSetType.TREATED));
	public static final WoodType LACQUERED = WoodType.register(new WoodType(Minestuck.MOD_ID + ":lacquered", MSBlockSetType.LACQUERED));
	public static final WoodType PERFECTLY_GENERIC = WoodType.register(new WoodType(Minestuck.MOD_ID + ":perfectly_generic", MSBlockSetType.PERFECTLY_GENERIC));
	public static final WoodType CINDERED = WoodType.register(new WoodType(Minestuck.MOD_ID + ":cindered", MSBlockSetType.CINDERED));
	
	public static final WoodType BLOOD = WoodType.register(new WoodType(Minestuck.MOD_ID + ":blood_aspect", MSBlockSetType.BLOOD));
	public static final WoodType BREATH = WoodType.register(new WoodType(Minestuck.MOD_ID + ":breath_aspect", MSBlockSetType.BREATH));
	public static final WoodType DOOM = WoodType.register(new WoodType(Minestuck.MOD_ID + ":doom_aspect", MSBlockSetType.DOOM));
	public static final WoodType HEART = WoodType.register(new WoodType(Minestuck.MOD_ID + ":heart_aspect", MSBlockSetType.HEART));
	public static final WoodType HOPE = WoodType.register(new WoodType(Minestuck.MOD_ID + ":hope_aspect", MSBlockSetType.HOPE));
	public static final WoodType LIFE = WoodType.register(new WoodType(Minestuck.MOD_ID + ":life_aspect", MSBlockSetType.LIFE));
	public static final WoodType LIGHT = WoodType.register(new WoodType(Minestuck.MOD_ID + ":light_aspect", MSBlockSetType.LIGHT));
	public static final WoodType MIND = WoodType.register(new WoodType(Minestuck.MOD_ID + ":mind_aspect", MSBlockSetType.MIND));
	public static final WoodType RAGE = WoodType.register(new WoodType(Minestuck.MOD_ID + ":rage_aspect", MSBlockSetType.RAGE));
	public static final WoodType SPACE = WoodType.register(new WoodType(Minestuck.MOD_ID + ":space_aspect", MSBlockSetType.SPACE));
	public static final WoodType TIME = WoodType.register(new WoodType(Minestuck.MOD_ID + ":time_aspect", MSBlockSetType.TIME));
	public static final WoodType VOID = WoodType.register(new WoodType(Minestuck.MOD_ID + ":void_aspect", MSBlockSetType.VOID));
	
	public static void init() {
		
		Sheets.addWoodType(MSWoodTypes.CARVED);
		Sheets.addWoodType(MSWoodTypes.DEAD);
		Sheets.addWoodType(MSWoodTypes.END);
		Sheets.addWoodType(MSWoodTypes.FROST);
		Sheets.addWoodType(MSWoodTypes.GLOWING);
		Sheets.addWoodType(MSWoodTypes.RAINBOW);
		Sheets.addWoodType(MSWoodTypes.SHADEWOOD);
		Sheets.addWoodType(MSWoodTypes.TREATED);
		Sheets.addWoodType(MSWoodTypes.LACQUERED);
		
		Sheets.addWoodType(MSWoodTypes.BLOOD);
		Sheets.addWoodType(MSWoodTypes.BREATH);
		Sheets.addWoodType(MSWoodTypes.DOOM);
		Sheets.addWoodType(MSWoodTypes.HEART);
		Sheets.addWoodType(MSWoodTypes.HOPE);
		Sheets.addWoodType(MSWoodTypes.LIFE);
		Sheets.addWoodType(MSWoodTypes.LIGHT);
		Sheets.addWoodType(MSWoodTypes.MIND);
		Sheets.addWoodType(MSWoodTypes.RAGE);
		Sheets.addWoodType(MSWoodTypes.SPACE);
		Sheets.addWoodType(MSWoodTypes.TIME);
		Sheets.addWoodType(MSWoodTypes.VOID);
		
	}
	
}