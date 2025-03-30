package com.mraof.minestuck.block;

import com.mraof.minestuck.Minestuck;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class MSBlockSetType
{
	//WOOD
	public static final BlockSetType CARVED = new BlockSetType(Minestuck.MOD_ID + ":carved");
	public static final BlockSetType DEAD = new BlockSetType(Minestuck.MOD_ID + ":dead");
	public static final BlockSetType END = new BlockSetType(Minestuck.MOD_ID + ":end");
	public static final BlockSetType FROST = new BlockSetType(Minestuck.MOD_ID + ":frost");
	public static final BlockSetType GLOWING = new BlockSetType(Minestuck.MOD_ID + ":glowing");
	public static final BlockSetType RAINBOW = new BlockSetType(Minestuck.MOD_ID + ":rainbow");
	public static final BlockSetType SHADEWOOD = new BlockSetType(Minestuck.MOD_ID + ":shadewood");
	public static final BlockSetType TREATED = new BlockSetType(Minestuck.MOD_ID + ":treated");
	public static final BlockSetType LACQUERED = new BlockSetType(Minestuck.MOD_ID + ":lacquered");
	public static final BlockSetType PERFECTLY_GENERIC = new BlockSetType(Minestuck.MOD_ID + ":perfectly_generic");
	public static final BlockSetType CINDERED = new BlockSetType(Minestuck.MOD_ID + ":cindered");
	
	//ASPECT WOOD
	public static final BlockSetType BLOOD = new BlockSetType(Minestuck.MOD_ID + ":blood");
	public static final BlockSetType BREATH = new BlockSetType(Minestuck.MOD_ID + ":breath");
	public static final BlockSetType DOOM = new BlockSetType(Minestuck.MOD_ID + ":doom");
	public static final BlockSetType HEART = new BlockSetType(Minestuck.MOD_ID + ":heart");
	public static final BlockSetType HOPE = new BlockSetType(Minestuck.MOD_ID + ":hope");
	public static final BlockSetType LIFE = new BlockSetType(Minestuck.MOD_ID + ":life");
	public static final BlockSetType LIGHT = new BlockSetType(Minestuck.MOD_ID + ":light");
	public static final BlockSetType MIND = new BlockSetType(Minestuck.MOD_ID + ":mind");
	public static final BlockSetType RAGE = new BlockSetType(Minestuck.MOD_ID + ":rage");
	public static final BlockSetType SPACE = new BlockSetType(Minestuck.MOD_ID + ":space");
	public static final BlockSetType TIME = new BlockSetType(Minestuck.MOD_ID + ":time");
	public static final BlockSetType VOID = new BlockSetType(Minestuck.MOD_ID + ":void");
	
	//STONE/METAL
	public static final BlockSetType CRUXITE = new BlockSetType(Minestuck.MOD_ID + ":cruxite",
			true, true, true, BlockSetType.PressurePlateSensitivity.EVERYTHING,
			SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN,
			SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON);
	public static final BlockSetType URANIUM = new BlockSetType(Minestuck.MOD_ID + ":uranium",
			true, true, true, BlockSetType.PressurePlateSensitivity.EVERYTHING,
			SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN,
			SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON);
	public static final BlockSetType COARSE_STONE = new BlockSetType(Minestuck.MOD_ID + ":coarse_stone",
			true, true, true, BlockSetType.PressurePlateSensitivity.EVERYTHING,
			SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN,
			SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON);
	public static final BlockSetType SHADE_STONE = new BlockSetType(Minestuck.MOD_ID + ":shade_stone",
			true, true, true, BlockSetType.PressurePlateSensitivity.EVERYTHING,
			SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN,
			SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON);
	public static final BlockSetType CAST_IRON = new BlockSetType(Minestuck.MOD_ID + ":cast_iron",
			false, true, true, BlockSetType.PressurePlateSensitivity.EVERYTHING,
			SoundType.METAL, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN,
			SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON);
	public static final BlockSetType MYCELIUM_STONE = new BlockSetType(Minestuck.MOD_ID + ":mycelium_stone",
			true, true, true, BlockSetType.PressurePlateSensitivity.EVERYTHING,
			SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN,
			SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON);
	
	public static final BlockSetType BLACK_STONE = new BlockSetType(Minestuck.MOD_ID + ":black_stone",
			true, true, true, BlockSetType.PressurePlateSensitivity.EVERYTHING,
			SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN,
			SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON);
	public static final BlockSetType IGNEOUS_STONE = new BlockSetType(Minestuck.id("igneous_stone").toString(),
			true, true, true, BlockSetType.PressurePlateSensitivity.EVERYTHING,
			SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN,
			SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON);
	public static final BlockSetType PUMICE_STONE = new BlockSetType(Minestuck.id("pumice_stone").toString(),
			true, true, true, BlockSetType.PressurePlateSensitivity.EVERYTHING,
			SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN,
			SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON);
	
	public static final BlockSetType CHALK = new BlockSetType(Minestuck.MOD_ID + ":chalk",
			true, true, true, BlockSetType.PressurePlateSensitivity.EVERYTHING,
			SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN,
			SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON);
	public static final BlockSetType PINK_STONE = new BlockSetType(Minestuck.MOD_ID + ":pink_stone",
			true, true, true, BlockSetType.PressurePlateSensitivity.EVERYTHING,
			SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN,
			SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON);
	public static final BlockSetType BROWN_STONE = new BlockSetType(Minestuck.MOD_ID + ":brown_stone",
			true, true, true, BlockSetType.PressurePlateSensitivity.EVERYTHING,
			SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN,
			SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON);
	public static final BlockSetType GREEN_STONE = new BlockSetType(Minestuck.id("green_stone").toString(),
			true, true, true, BlockSetType.PressurePlateSensitivity.EVERYTHING,
			SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN,
			SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON);
	
	public static final BlockSetType BLACK_CHESS = new BlockSetType(Minestuck.MOD_ID + ":black_chess",
			true, true, true, BlockSetType.PressurePlateSensitivity.EVERYTHING,
			SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN,
			SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON);
	public static final BlockSetType DARK_GRAY_CHESS = new BlockSetType(Minestuck.MOD_ID + ":dark_gray_chess",
			true, true, true, BlockSetType.PressurePlateSensitivity.EVERYTHING,
			SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN,
			SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON);
	public static final BlockSetType LIGHT_GRAY_CHESS = new BlockSetType(Minestuck.MOD_ID + ":light_gray_chess",
			true, true, true, BlockSetType.PressurePlateSensitivity.EVERYTHING,
			SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN,
			SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON);
	public static final BlockSetType WHITE_CHESS = new BlockSetType(Minestuck.MOD_ID + ":white_chess",
			true, true, true, BlockSetType.PressurePlateSensitivity.EVERYTHING,
			SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN,
			SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON);
	
}
