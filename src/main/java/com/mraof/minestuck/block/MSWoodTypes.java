package com.mraof.minestuck.block;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Minestuck.MOD_ID)
public class MSWoodTypes
{
	public static final WoodType CARVED = new WoodType(Minestuck.MOD_ID + ":carved", MSBlockSetType.CARVED);
	public static final WoodType DEAD = new WoodType(Minestuck.MOD_ID + ":dead", MSBlockSetType.DEAD);
	public static final WoodType END = new WoodType(Minestuck.MOD_ID + ":end", MSBlockSetType.END);
	public static final WoodType FROST = new WoodType(Minestuck.MOD_ID + ":frost", MSBlockSetType.FROST);
	public static final WoodType GLOWING = new WoodType(Minestuck.MOD_ID + ":glowing", MSBlockSetType.GLOWING);
	public static final WoodType RAINBOW = new WoodType(Minestuck.MOD_ID + ":rainbow", MSBlockSetType.RAINBOW);
	public static final WoodType SHADEWOOD = new WoodType(Minestuck.MOD_ID + ":shadewood", MSBlockSetType.SHADEWOOD);
	public static final WoodType TREATED = new WoodType(Minestuck.MOD_ID + ":treated", MSBlockSetType.TREATED);
	public static final WoodType LACQUERED = new WoodType(Minestuck.MOD_ID + ":lacquered", MSBlockSetType.LACQUERED);
	public static final WoodType PERFECTLY_GENERIC = new WoodType(Minestuck.MOD_ID + ":perfectly_generic", MSBlockSetType.PERFECTLY_GENERIC);
	public static final WoodType CINDERED = new WoodType(Minestuck.MOD_ID + ":cindered", MSBlockSetType.CINDERED);
	
	public static final WoodType BLOOD = new WoodType(Minestuck.MOD_ID + ":blood_aspect", MSBlockSetType.BLOOD);
	public static final WoodType BREATH = new WoodType(Minestuck.MOD_ID + ":breath_aspect", MSBlockSetType.BREATH);
	public static final WoodType DOOM = new WoodType(Minestuck.MOD_ID + ":doom_aspect", MSBlockSetType.DOOM);
	public static final WoodType HEART = new WoodType(Minestuck.MOD_ID + ":heart_aspect", MSBlockSetType.HEART);
	public static final WoodType HOPE = new WoodType(Minestuck.MOD_ID + ":hope_aspect", MSBlockSetType.HOPE);
	public static final WoodType LIFE = new WoodType(Minestuck.MOD_ID + ":life_aspect", MSBlockSetType.LIFE);
	public static final WoodType LIGHT = new WoodType(Minestuck.MOD_ID + ":light_aspect", MSBlockSetType.LIGHT);
	public static final WoodType MIND = new WoodType(Minestuck.MOD_ID + ":mind_aspect", MSBlockSetType.MIND);
	public static final WoodType RAGE = new WoodType(Minestuck.MOD_ID + ":rage_aspect", MSBlockSetType.RAGE);
	public static final WoodType SPACE = new WoodType(Minestuck.MOD_ID + ":space_aspect", MSBlockSetType.SPACE);
	public static final WoodType TIME = new WoodType(Minestuck.MOD_ID + ":time_aspect", MSBlockSetType.TIME);
	public static final WoodType VOID = new WoodType(Minestuck.MOD_ID + ":void_aspect", MSBlockSetType.VOID);
	
	@SubscribeEvent
	public static void register(RegisterEvent event)
	{
		if(!event.getRegistryKey().equals(Registries.BLOCK))
			return;
		
		// By registering wood types during registry loading,
		// we do not also have add them with Sheets.addWoodType()
		// (Sheets uses all registered wood types at static initialization, so we're fine as long as we register before then)
		
		WoodType.register(MSWoodTypes.CARVED);
		WoodType.register(MSWoodTypes.DEAD);
		WoodType.register(MSWoodTypes.END);
		WoodType.register(MSWoodTypes.FROST);
		WoodType.register(MSWoodTypes.GLOWING);
		WoodType.register(MSWoodTypes.RAINBOW);
		WoodType.register(MSWoodTypes.SHADEWOOD);
		WoodType.register(MSWoodTypes.TREATED);
		WoodType.register(MSWoodTypes.LACQUERED);
		WoodType.register(MSWoodTypes.PERFECTLY_GENERIC);
		WoodType.register(MSWoodTypes.CINDERED);
		
		WoodType.register(MSWoodTypes.BLOOD);
		WoodType.register(MSWoodTypes.BREATH);
		WoodType.register(MSWoodTypes.DOOM);
		WoodType.register(MSWoodTypes.HEART);
		WoodType.register(MSWoodTypes.HOPE);
		WoodType.register(MSWoodTypes.LIFE);
		WoodType.register(MSWoodTypes.LIGHT);
		WoodType.register(MSWoodTypes.MIND);
		WoodType.register(MSWoodTypes.RAGE);
		WoodType.register(MSWoodTypes.SPACE);
		WoodType.register(MSWoodTypes.TIME);
		WoodType.register(MSWoodTypes.VOID);
	}
}