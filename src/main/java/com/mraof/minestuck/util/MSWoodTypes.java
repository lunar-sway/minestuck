package com.mraof.minestuck.util;

import com.mraof.minestuck.Minestuck;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class MSWoodTypes
{
	public static final WoodType CARVED = WoodType.register(new WoodType(Minestuck.MOD_ID + ":carved", BlockSetType.OAK));
	public static final WoodType DEAD = WoodType.register(new WoodType(Minestuck.MOD_ID + ":dead", BlockSetType.OAK));
	public static final WoodType END = WoodType.register(new WoodType(Minestuck.MOD_ID + ":end", BlockSetType.OAK));
	public static final WoodType FROST = WoodType.register(new WoodType(Minestuck.MOD_ID + ":frost", BlockSetType.OAK));
	public static final WoodType GLOWING = WoodType.register(new WoodType(Minestuck.MOD_ID + ":glowing", BlockSetType.OAK));
	public static final WoodType RAINBOW = WoodType.register(new WoodType(Minestuck.MOD_ID + ":rainbow", BlockSetType.OAK));
	public static final WoodType SHADEWOOD = WoodType.register(new WoodType(Minestuck.MOD_ID + ":shadewood", BlockSetType.OAK));
	public static final WoodType TREATED = WoodType.register(new WoodType(Minestuck.MOD_ID + ":treated", BlockSetType.OAK));
}
