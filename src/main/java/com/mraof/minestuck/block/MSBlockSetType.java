package com.mraof.minestuck.block;

import com.mraof.minestuck.Minestuck;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;

import static net.minecraft.world.level.block.state.properties.BlockSetType.register;

public class MSBlockSetType
{
	//WOOD
	public static final BlockSetType CARVED = register(new BlockSetType(Minestuck.MOD_ID + ":carved"));
	public static final BlockSetType DEAD = register(new BlockSetType(Minestuck.MOD_ID + ":dead"));
	public static final BlockSetType END = register(new BlockSetType(Minestuck.MOD_ID + ":end"));
	public static final BlockSetType FROST = register(new BlockSetType(Minestuck.MOD_ID + ":frost"));
	public static final BlockSetType GLOWING = register(new BlockSetType(Minestuck.MOD_ID + ":glowing"));
	public static final BlockSetType RAINBOW = register(new BlockSetType(Minestuck.MOD_ID + ":rainbow"));
	public static final BlockSetType SHADEWOOD = register(new BlockSetType(Minestuck.MOD_ID + ":shadewood"));
	public static final BlockSetType TREATED = register(new BlockSetType(Minestuck.MOD_ID + ":treated"));
	public static final BlockSetType LACQUERED = register(new BlockSetType(Minestuck.MOD_ID + ":lacquered"));
	public static final BlockSetType PERFECTLY_GENERIC = register(new BlockSetType(Minestuck.MOD_ID + ":perfectly_generic"));
	public static final BlockSetType CINDERED = register(new BlockSetType(Minestuck.MOD_ID + ":cindered"));
	
	//ASPECT WOOD
	public static final BlockSetType BLOOD = register(new BlockSetType(Minestuck.MOD_ID + ":blood"));
	public static final BlockSetType BREATH = register(new BlockSetType(Minestuck.MOD_ID + ":breath"));
	public static final BlockSetType DOOM = register(new BlockSetType(Minestuck.MOD_ID + ":doom"));
	public static final BlockSetType HEART = register(new BlockSetType(Minestuck.MOD_ID + ":heart"));
	public static final BlockSetType HOPE = register(new BlockSetType(Minestuck.MOD_ID + ":hope"));
	public static final BlockSetType LIFE = register(new BlockSetType(Minestuck.MOD_ID + ":life"));
	public static final BlockSetType LIGHT = register(new BlockSetType(Minestuck.MOD_ID + ":light"));
	public static final BlockSetType MIND = register(new BlockSetType(Minestuck.MOD_ID + ":mind"));
	public static final BlockSetType RAGE = register(new BlockSetType(Minestuck.MOD_ID + ":rage"));
	public static final BlockSetType SPACE = register(new BlockSetType(Minestuck.MOD_ID + ":space"));
	public static final BlockSetType TIME = register(new BlockSetType(Minestuck.MOD_ID + ":time"));
	public static final BlockSetType VOID = register(new BlockSetType(Minestuck.MOD_ID + ":void"));
	
	//STONE/METAL
	public static final BlockSetType CRUXITE = register(new BlockSetType(Minestuck.MOD_ID + ":cruxite", true, SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));
	public static final BlockSetType URANIUM = register(new BlockSetType(Minestuck.MOD_ID + ":uranium", true, SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));
	public static final BlockSetType COARSE_STONE = register(new BlockSetType(Minestuck.MOD_ID + ":coarse_stone", true, SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));
	public static final BlockSetType SHADE_STONE = register(new BlockSetType(Minestuck.MOD_ID + ":shade_stone", true, SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));
	public static final BlockSetType CAST_IRON = register(new BlockSetType(Minestuck.MOD_ID + ":cast_iron", false, SoundType.METAL, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));
	public static final BlockSetType MYCELIUM_STONE = register(new BlockSetType(Minestuck.MOD_ID + ":mycelium_stone", true, SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));
	public static final BlockSetType BLACK_STONE = register(new BlockSetType(Minestuck.MOD_ID + ":black_stone", true, SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));
	public static final BlockSetType CHALK = register(new BlockSetType(Minestuck.MOD_ID + ":chalk", true, SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));
	public static final BlockSetType PINK_STONE = register(new BlockSetType(Minestuck.MOD_ID + ":pink_stone", true, SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));
	public static final BlockSetType BROWN_STONE = register(new BlockSetType(Minestuck.MOD_ID + ":brown_stone", true, SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));
	public static final BlockSetType BLACK_CHESS = register(new BlockSetType(Minestuck.MOD_ID + ":black_chess", true, SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));
	public static final BlockSetType DARK_GRAY_CHESS = register(new BlockSetType(Minestuck.MOD_ID + ":dark_gray_chess", true, SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));
	public static final BlockSetType LIGHT_GRAY_CHESS = register(new BlockSetType(Minestuck.MOD_ID + ":light_gray_chess", true, SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));
	public static final BlockSetType WHITE_CHESS = register(new BlockSetType(Minestuck.MOD_ID + ":white_chess", true, SoundType.STONE, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));
	
}
