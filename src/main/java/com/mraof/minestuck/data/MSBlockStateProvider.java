package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

public class MSBlockStateProvider extends BlockStateProvider
{
	public MSBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper)
	{
		super(output, Minestuck.MOD_ID, exFileHelper);
	}
	
	@Override
	protected void registerStatesAndModels()
	{
		//Skaia
		simpleBlockWithItem(MSBlocks.BLACK_CHESS_DIRT);
		simpleBlockWithItem(MSBlocks.WHITE_CHESS_DIRT);
		simpleBlockWithItem(MSBlocks.DARK_GRAY_CHESS_DIRT);
		simpleBlockWithItem(MSBlocks.LIGHT_GRAY_CHESS_DIRT);
		simpleBlock(MSBlocks.SKAIA_PORTAL,
				id -> models().getBuilder(id.getPath()).texture("particle", id.withPrefix("item/")));
		flatItem(MSBlocks.SKAIA_PORTAL, MSBlockStateProvider::itemTexture);
		
		simpleBlockWithItem(MSBlocks.BLACK_CHESS_BRICKS);
		simpleBlockWithItem(MSBlocks.DARK_GRAY_CHESS_BRICKS);
		simpleBlockWithItem(MSBlocks.LIGHT_GRAY_CHESS_BRICKS);
		simpleBlockWithItem(MSBlocks.WHITE_CHESS_BRICKS);
		simpleBlockWithItem(MSBlocks.BLACK_CHESS_BRICK_SMOOTH);
		simpleBlockWithItem(MSBlocks.DARK_GRAY_CHESS_BRICK_SMOOTH);
		simpleBlockWithItem(MSBlocks.LIGHT_GRAY_CHESS_BRICK_SMOOTH);
		simpleBlockWithItem(MSBlocks.WHITE_CHESS_BRICK_SMOOTH);
		trimWithItem(MSBlocks.BLACK_CHESS_BRICK_TRIM,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture("black_chess_bricks")),
				id -> models().cubeColumn(
						id.withSuffix("_flipped").getPath(),
						texture(id.withSuffix("_flipped")),
						texture("black_chess_bricks")));
		trimWithItem(MSBlocks.DARK_GRAY_CHESS_BRICK_TRIM,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture("dark_gray_chess_bricks")),
				id -> models().cubeColumn(
						id.withSuffix("_flipped").getPath(),
						texture(id.withSuffix("_flipped")),
						texture("dark_gray_chess_bricks")));
		trimWithItem(MSBlocks.LIGHT_GRAY_CHESS_BRICK_TRIM,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture("light_gray_chess_bricks")),
				id -> models().cubeColumn(
						id.withSuffix("_flipped").getPath(),
						texture(id.withSuffix("_flipped")),
						texture("light_gray_chess_bricks")));
		trimWithItem(MSBlocks.WHITE_CHESS_BRICK_TRIM,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture("white_chess_bricks")),
				id -> models().cubeColumn(
						id.withSuffix("_flipped").getPath(),
						texture(id.withSuffix("_flipped")),
						texture("white_chess_bricks")));
		
		simpleBlockWithItem(MSBlocks.CHECKERED_STAINED_GLASS,
				id -> models().cubeAll(id.getPath(), texture(id)).renderType("translucent"));
		simpleBlockWithItem(MSBlocks.BLACK_CROWN_STAINED_GLASS,
				id -> models().cubeAll(id.getPath(), texture(id)).renderType("translucent"));
		simpleBlockWithItem(MSBlocks.BLACK_PAWN_STAINED_GLASS,
				id -> models().cubeAll(id.getPath(), texture(id)).renderType("translucent"));
		simpleBlockWithItem(MSBlocks.WHITE_CROWN_STAINED_GLASS,
				id -> models().cubeAll(id.getPath(), texture(id)).renderType("translucent"));
		simpleBlockWithItem(MSBlocks.WHITE_PAWN_STAINED_GLASS,
				id -> models().cubeAll(id.getPath(), texture(id)).renderType("translucent"));
		
		simpleBlockWithItem(MSBlocks.STONE_CRUXITE_ORE);
		simpleBlockWithItem(MSBlocks.NETHERRACK_CRUXITE_ORE);
		simpleBlockWithItem(MSBlocks.COBBLESTONE_CRUXITE_ORE);
		simpleBlockWithItem(MSBlocks.SANDSTONE_CRUXITE_ORE,
				id -> models().cubeBottomTop(id.getPath(),
						texture(id.withSuffix("_side")),
						texture(id.withSuffix("_bottom")),
						texture(id.withSuffix("_top"))));
		simpleBlockWithItem(MSBlocks.RED_SANDSTONE_CRUXITE_ORE,
				id -> models().cubeBottomTop(id.getPath(),
						texture(id.withSuffix("_side")),
						texture(id.withSuffix("_bottom")),
						texture(id.withSuffix("_top"))));
		simpleBlockWithItem(MSBlocks.END_STONE_CRUXITE_ORE);
		simpleBlockWithItem(MSBlocks.SHADE_STONE_CRUXITE_ORE);
		simpleBlockWithItem(MSBlocks.PINK_STONE_CRUXITE_ORE);
		simpleBlockWithItem(MSBlocks.MYCELIUM_STONE_CRUXITE_ORE);
		
		simpleBlockWithItem(MSBlocks.STONE_URANIUM_ORE);
		simpleBlockWithItem(MSBlocks.DEEPSLATE_URANIUM_ORE);
		simpleBlockWithItem(MSBlocks.NETHERRACK_URANIUM_ORE);
		simpleBlockWithItem(MSBlocks.COBBLESTONE_URANIUM_ORE);
		simpleBlockWithItem(MSBlocks.SANDSTONE_URANIUM_ORE,
				id -> models().cubeBottomTop(id.getPath(),
						texture(id.withSuffix("_side")),
						texture(id.withSuffix("_bottom")),
						texture(id.withSuffix("_top"))));
		simpleBlockWithItem(MSBlocks.RED_SANDSTONE_URANIUM_ORE,
				id -> models().cubeBottomTop(id.getPath(),
						texture(id.withSuffix("_side")),
						texture(id.withSuffix("_bottom")),
						texture(id.withSuffix("_top"))));
		simpleBlockWithItem(MSBlocks.END_STONE_URANIUM_ORE);
		simpleBlockWithItem(MSBlocks.SHADE_STONE_URANIUM_ORE);
		simpleBlockWithItem(MSBlocks.PINK_STONE_URANIUM_ORE);
		simpleBlockWithItem(MSBlocks.MYCELIUM_STONE_URANIUM_ORE);
		
		simpleBlockWithItem(MSBlocks.NETHERRACK_COAL_ORE);
		simpleBlockWithItem(MSBlocks.SHADE_STONE_COAL_ORE);
		simpleBlockWithItem(MSBlocks.PINK_STONE_COAL_ORE);
		
		simpleBlockWithItem(MSBlocks.END_STONE_IRON_ORE);
		simpleBlockWithItem(MSBlocks.SANDSTONE_IRON_ORE,
				id -> models().cubeBottomTop(id.getPath(),
						texture(id.withSuffix("_side")),
						texture(id.withSuffix("_bottom")),
						texture(id.withSuffix("_top"))));
		simpleBlockWithItem(MSBlocks.RED_SANDSTONE_IRON_ORE,
				id -> models().cubeBottomTop(id.getPath(),
						texture(id.withSuffix("_side")),
						texture(id.withSuffix("_bottom")),
						texture(id.withSuffix("_top"))));
		
		simpleBlockWithItem(MSBlocks.SANDSTONE_GOLD_ORE,
				id -> models().cubeBottomTop(id.getPath(),
						texture(id.withSuffix("_side")),
						texture(id.withSuffix("_bottom")),
						texture(id.withSuffix("_top"))));
		simpleBlockWithItem(MSBlocks.RED_SANDSTONE_GOLD_ORE,
				id -> models().cubeBottomTop(id.getPath(),
						texture(id.withSuffix("_side")),
						texture(id.withSuffix("_bottom")),
						texture(id.withSuffix("_top"))));
		simpleBlockWithItem(MSBlocks.SHADE_STONE_GOLD_ORE);
		simpleBlockWithItem(MSBlocks.PINK_STONE_GOLD_ORE);
		
		simpleBlockWithItem(MSBlocks.END_STONE_REDSTONE_ORE);
		simpleBlockWithItem(MSBlocks.STONE_QUARTZ_ORE);
		simpleBlockWithItem(MSBlocks.PINK_STONE_LAPIS_ORE);
		simpleBlockWithItem(MSBlocks.PINK_STONE_DIAMOND_ORE);
		
		//Resource Blocks
		simpleBlockWithItem(MSBlocks.CRUXITE_BLOCK);
		simpleBlockWithItem(MSBlocks.URANIUM_BLOCK);
		simpleBlockWithItem(MSBlocks.GENERIC_OBJECT);
		
		//Land Environment
		simpleBlockWithItem(MSBlocks.THOUGHT_DIRT);
		
		simpleBlockWithItem(MSBlocks.COARSE_STONE);
		simpleBlockWithItem(MSBlocks.CHISELED_COARSE_STONE);
		simpleBlockWithItem(MSBlocks.COARSE_STONE_BRICKS);
		unflippedColumnWithItem(MSBlocks.COARSE_STONE_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		simpleBlockWithItem(MSBlocks.CHISELED_COARSE_STONE_BRICKS);
		simpleBlockWithItem(MSBlocks.CRACKED_COARSE_STONE_BRICKS);
		simpleBlockWithItem(MSBlocks.MOSSY_COARSE_STONE_BRICKS);
		
		simpleBlockWithItem(MSBlocks.SHADE_STONE);
		simpleBlockWithItem(MSBlocks.SMOOTH_SHADE_STONE);
		simpleBlockWithItem(MSBlocks.SHADE_BRICKS);
		unflippedColumnWithItem(MSBlocks.SHADE_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture("shade_stone_column"),
						texture("shade_stone_column_top")));
		simpleBlockWithItem(MSBlocks.CHISELED_SHADE_BRICKS);
		simpleBlockWithItem(MSBlocks.CRACKED_SHADE_BRICKS);
		simpleBlockWithItem(MSBlocks.MOSSY_SHADE_BRICKS);
		simpleBlockWithItem(MSBlocks.BLOOD_SHADE_BRICKS);
		simpleBlockWithItem(MSBlocks.TAR_SHADE_BRICKS);
		
		simpleBlockWithItem(MSBlocks.FROST_TILE);
		simpleBlockWithItem(MSBlocks.CHISELED_FROST_TILE);
		simpleBlockWithItem(MSBlocks.FROST_BRICKS);
		unflippedColumnWithItem(MSBlocks.FROST_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		simpleBlockWithItem(MSBlocks.CHISELED_FROST_BRICKS,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id).withSuffix("_side"),
						texture(id)));
		simpleBlockWithItem(MSBlocks.CRACKED_FROST_BRICKS);
		simpleBlockWithItem(MSBlocks.FLOWERY_FROST_BRICKS);
		
		simpleBlockWithItem(MSBlocks.CAST_IRON);
		simpleBlockWithItem(MSBlocks.CHISELED_CAST_IRON);
		unflippedColumnWithItem(MSBlocks.STEEL_BEAM,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id.withSuffix("_side")),
						texture(id.withSuffix("_top"))));
		
		simpleBlockWithItem(MSBlocks.NATIVE_SULFUR);
		
		simpleBlockWithItem(MSBlocks.MYCELIUM_STONE);
		simpleBlockWithItem(MSBlocks.MYCELIUM_COBBLESTONE);
		simpleBlockWithItem(MSBlocks.POLISHED_MYCELIUM_STONE);
		simpleBlockWithItem(MSBlocks.MYCELIUM_BRICKS);
		unflippedColumnWithItem(MSBlocks.MYCELIUM_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		simpleBlockWithItem(MSBlocks.CHISELED_MYCELIUM_BRICKS);
		simpleBlockWithItem(MSBlocks.SUSPICIOUS_CHISELED_MYCELIUM_BRICKS);
		simpleBlockWithItem(MSBlocks.CRACKED_MYCELIUM_BRICKS);
		simpleBlockWithItem(MSBlocks.MOSSY_MYCELIUM_BRICKS);
		simpleBlockWithItem(MSBlocks.FLOWERY_MYCELIUM_BRICKS);
		
		simpleBlockWithItem(MSBlocks.BLACK_SAND);
		simpleBlockWithItem(MSBlocks.BLACK_STONE);
		simpleBlockWithItem(MSBlocks.BLACK_COBBLESTONE);
		simpleBlockWithItem(MSBlocks.POLISHED_BLACK_STONE);
		simpleBlockWithItem(MSBlocks.BLACK_STONE_BRICKS);
		unflippedColumnWithItem(MSBlocks.BLACK_STONE_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		simpleBlockWithItem(MSBlocks.CHISELED_BLACK_STONE_BRICKS);
		simpleBlockWithItem(MSBlocks.CRACKED_BLACK_STONE_BRICKS);
		
		simpleBlockWithItem(MSBlocks.FLOWERY_MOSSY_COBBLESTONE);
		simpleBlockWithItem(MSBlocks.COARSE_END_STONE);
		
		simpleBlockWithItem(MSBlocks.CHALK);
		simpleBlockWithItem(MSBlocks.POLISHED_CHALK);
		simpleBlockWithItem(MSBlocks.CHALK_BRICKS);
		unflippedColumnWithItem(MSBlocks.CHALK_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		simpleBlockWithItem(MSBlocks.CHISELED_CHALK_BRICKS);
		simpleBlockWithItem(MSBlocks.MOSSY_CHALK_BRICKS);
		simpleBlockWithItem(MSBlocks.FLOWERY_CHALK_BRICKS);
		
		simpleBlockWithItem(MSBlocks.PINK_STONE);
		simpleBlockWithItem(MSBlocks.POLISHED_PINK_STONE);
		simpleBlockWithItem(MSBlocks.PINK_STONE_BRICKS);
		unflippedColumnWithItem(MSBlocks.PINK_STONE_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture(id.withSuffix("_end"))));
		simpleBlockWithItem(MSBlocks.CHISELED_PINK_STONE_BRICKS);
		simpleBlockWithItem(MSBlocks.CRACKED_PINK_STONE_BRICKS);
		simpleBlockWithItem(MSBlocks.MOSSY_PINK_STONE_BRICKS);
		
		simpleBlockWithItem(MSBlocks.BROWN_STONE);
		simpleBlockWithItem(MSBlocks.POLISHED_BROWN_STONE);
		simpleBlockWithItem(MSBlocks.BROWN_STONE_BRICKS);
		simpleBlockWithItem(MSBlocks.CRACKED_BROWN_STONE_BRICKS);
		unflippedColumnWithItem(MSBlocks.BROWN_STONE_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture("polished_brown_stone")));
		
		simpleBlockWithItem(MSBlocks.GREEN_STONE);
		simpleBlockWithItem(MSBlocks.POLISHED_GREEN_STONE);
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICKS);
		unflippedColumnWithItem(MSBlocks.GREEN_STONE_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture("polished_green_stone")));
		simpleBlockWithItem(MSBlocks.CHISELED_GREEN_STONE_BRICKS);
		simpleBlockWithItem(MSBlocks.HORIZONTAL_GREEN_STONE_BRICKS);
		simpleBlockWithItem(MSBlocks.VERTICAL_GREEN_STONE_BRICKS);
		horizontalWithItem(MSBlocks.GREEN_STONE_BRICK_EMBEDDED_LADDER,
				id -> models().getExistingFile(id));
		trimWithItem(MSBlocks.GREEN_STONE_BRICK_TRIM,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture("green_stone_bricks")),
				id -> models().cubeColumn(
						id.withSuffix("_flipped").getPath(),
						texture(id.withSuffix("_flipped")),
						texture("green_stone_bricks")));
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICK_FROG);
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICK_IGUANA_LEFT);
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICK_IGUANA_RIGHT);
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICK_LOTUS);
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICK_NAK_LEFT);
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICK_NAK_RIGHT);
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICK_SALAMANDER_LEFT);
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICK_SALAMANDER_RIGHT);
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICK_SKAIA);
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICK_TURTLE);
		
		unflippedColumnWithItem(MSBlocks.SANDSTONE_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture(id.withSuffix("_end"))));
		directionalWithItem(MSBlocks.CHISELED_SANDSTONE_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture("sandstone_column_end")));
		unflippedColumnWithItem(MSBlocks.RED_SANDSTONE_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture(id.withSuffix("_end"))));
		directionalWithItem(MSBlocks.CHISELED_RED_SANDSTONE_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture("red_sandstone_column_end")));
		
		simpleBlockWithItem(MSBlocks.UNCARVED_WOOD);
		simpleBlockWithItem(MSBlocks.CHIPBOARD);
		simpleBlockWithItem(MSBlocks.WOOD_SHAVINGS);
		simpleBlockWithItem(MSBlocks.CARVED_HEAVY_PLANKS);
		simpleBlockWithItem(MSBlocks.CARVED_PLANKS);
		simpleBlockWithItem(MSBlocks.POLISHED_UNCARVED_WOOD);
		simpleBlockWithItem(MSBlocks.CARVED_KNOTTED_WOOD);
		
		simpleBlockWithItem(MSBlocks.DENSE_CLOUD);
		simpleBlockWithItem(MSBlocks.BRIGHT_DENSE_CLOUD);
		simpleBlockWithItem(MSBlocks.SUGAR_CUBE);
		
		//Land Tree Blocks
		axisWithItem(MSBlocks.GLOWING_LOG,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		axisWithItem(MSBlocks.FROST_LOG,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		axisWithItem(MSBlocks.RAINBOW_LOG,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		axisWithItem(MSBlocks.VINE_LOG,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						vanillaTexture("oak_log_top")));
		axisWithItem(MSBlocks.FLOWERY_VINE_LOG,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						vanillaTexture("oak_log_top")));
		axisWithItem(MSBlocks.DEAD_LOG,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		axisWithItem(MSBlocks.PETRIFIED_LOG,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		axisWithItem(MSBlocks.SHADEWOOD_LOG,
				id -> models().cubeColumn(id.getPath(),
						texture("shadewood"),
						texture("shadewood_top")));
		axisWithItem(MSBlocks.SCARRED_SHADEWOOD_LOG,
				id -> models().getExistingFile(id));
		axisWithItem(MSBlocks.ROTTED_SHADEWOOD_LOG,
				id -> models().cubeColumn(id.getPath(),
						texture("rotted_shadewood"),
						texture(id.withSuffix("_top"))));
		axisWithItem(MSBlocks.STRIPPED_SHADEWOOD_LOG,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		axisWithItem(MSBlocks.STRIPPED_SCARRED_SHADEWOOD_LOG,
				id -> models().getExistingFile(id));
		axisWithItem(MSBlocks.STRIPPED_ROTTED_SHADEWOOD_LOG,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		
		axisWithItem(MSBlocks.GLOWING_WOOD,
				id -> models().cubeColumn(id.getPath(),
						texture("glowing_log"),
						texture("glowing_log")));
		axisWithItem(MSBlocks.SHADEWOOD,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture(id)));
		axisWithItem(MSBlocks.SCARRED_SHADEWOOD,
				id -> models().cubeColumn(id.getPath(),
						texture(id.withSuffix("_rotated")),
						texture("shadewood")));
		axisWithItem(MSBlocks.ROTTED_SHADEWOOD,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture(id)));
		axisWithItem(MSBlocks.STRIPPED_SHADEWOOD,
				id -> models().cubeColumn(id.getPath(),
						texture("stripped_shadewood_log"),
						texture("stripped_shadewood_log")));
		axisWithItem(MSBlocks.STRIPPED_SCARRED_SHADEWOOD,
				id -> models().cubeColumn(id.getPath(),
						texture(id.withSuffix("_rotated")),
						texture("stripped_shadewood_log")));
		axisWithItem(MSBlocks.STRIPPED_ROTTED_SHADEWOOD,
				id -> models().cubeColumn(id.getPath(),
						texture("stripped_rotted_shadewood_log"),
						texture("stripped_rotted_shadewood_log")));
		axisWithItem(MSBlocks.FROST_WOOD,
				id -> models().cubeColumn(id.getPath(),
						texture("frost_log"),
						texture("frost_log")));
		axisWithItem(MSBlocks.RAINBOW_WOOD,
				id -> models().cubeColumn(id.getPath(),
						texture("rainbow_log"),
						texture("rainbow_log")));
		axisWithItem(MSBlocks.END_WOOD,
				id -> models().cubeColumn(id.getPath(),
						texture("end_log"),
						texture("end_log")));
		axisWithItem(MSBlocks.VINE_WOOD,
				id -> models().cubeColumn(id.getPath(),
						texture("vine_log"),
						texture("vine_log")));
		axisWithItem(MSBlocks.FLOWERY_VINE_WOOD,
				id -> models().cubeColumn(id.getPath(),
						texture("flowery_vine_log"),
						texture("flowery_vine_log")));
		axisWithItem(MSBlocks.DEAD_WOOD,
				id -> models().cubeColumn(id.getPath(),
						texture("dead_log"),
						texture("dead_log")));
		axisWithItem(MSBlocks.PETRIFIED_WOOD,
				id -> models().cubeColumn(id.getPath(),
						texture("petrified_log"),
						texture("petrified_log")));
		
		simpleBlockWithItem(MSBlocks.GLOWING_PLANKS);
		simpleBlockWithItem(MSBlocks.FROST_PLANKS);
		simpleBlockWithItem(MSBlocks.RAINBOW_PLANKS);
		simpleBlockWithItem(MSBlocks.END_PLANKS);
		simpleBlockWithItem(MSBlocks.DEAD_PLANKS);
		simpleBlockWithItem(MSBlocks.TREATED_PLANKS);
		simpleBlockWithItem(MSBlocks.SHADEWOOD_PLANKS);
		
		simpleBlockWithItem(MSBlocks.FROST_LEAVES);
		simpleBlockWithItem(MSBlocks.RAINBOW_LEAVES,
				id -> models().singleTexture(id.getPath(), new ResourceLocation("block/leaves"), "all", texture(id)));
		simpleBlockWithItem(MSBlocks.END_LEAVES);
		simpleBlockWithItem(MSBlocks.SHADEWOOD_LEAVES);
		simpleBlockWithItem(MSBlocks.SHROOMY_SHADEWOOD_LEAVES);
		
		simpleBlock(MSBlocks.RAINBOW_SAPLING,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSBlocks.RAINBOW_SAPLING, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.END_SAPLING,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSBlocks.END_SAPLING, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.SHADEWOOD_SAPLING,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSBlocks.SHADEWOOD_SAPLING, MSBlockStateProvider::texture);
		
		axisWithItem(MSBlocks.BLOOD_ASPECT_LOG,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		axisWithItem(MSBlocks.BREATH_ASPECT_LOG,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		axisWithItem(MSBlocks.DOOM_ASPECT_LOG,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		axisWithItem(MSBlocks.HEART_ASPECT_LOG,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		axisWithItem(MSBlocks.HOPE_ASPECT_LOG,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		axisWithItem(MSBlocks.LIFE_ASPECT_LOG,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		axisWithItem(MSBlocks.LIGHT_ASPECT_LOG,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		axisWithItem(MSBlocks.MIND_ASPECT_LOG,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		axisWithItem(MSBlocks.RAGE_ASPECT_LOG,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		axisWithItem(MSBlocks.SPACE_ASPECT_LOG,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		axisWithItem(MSBlocks.TIME_ASPECT_LOG,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		axisWithItem(MSBlocks.VOID_ASPECT_LOG,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		
		simpleBlockWithItem(MSBlocks.BLOOD_ASPECT_PLANKS);
		simpleBlockWithItem(MSBlocks.BREATH_ASPECT_PLANKS);
		simpleBlockWithItem(MSBlocks.DOOM_ASPECT_PLANKS);
		simpleBlockWithItem(MSBlocks.HEART_ASPECT_PLANKS);
		simpleBlockWithItem(MSBlocks.HOPE_ASPECT_PLANKS);
		simpleBlockWithItem(MSBlocks.LIFE_ASPECT_PLANKS);
		simpleBlockWithItem(MSBlocks.LIGHT_ASPECT_PLANKS);
		simpleBlockWithItem(MSBlocks.MIND_ASPECT_PLANKS);
		simpleBlockWithItem(MSBlocks.RAGE_ASPECT_PLANKS);
		simpleBlockWithItem(MSBlocks.SPACE_ASPECT_PLANKS);
		simpleBlockWithItem(MSBlocks.TIME_ASPECT_PLANKS);
		simpleBlockWithItem(MSBlocks.VOID_ASPECT_PLANKS);
		
		simpleBlockWithItem(MSBlocks.BLOOD_ASPECT_LEAVES);
		simpleBlockWithItem(MSBlocks.BREATH_ASPECT_LEAVES);
		simpleBlockWithItem(MSBlocks.DOOM_ASPECT_LEAVES);
		simpleBlockWithItem(MSBlocks.HEART_ASPECT_LEAVES);
		simpleBlockWithItem(MSBlocks.HOPE_ASPECT_LEAVES);
		simpleBlockWithItem(MSBlocks.LIFE_ASPECT_LEAVES);
		simpleBlockWithItem(MSBlocks.LIGHT_ASPECT_LEAVES);
		simpleBlockWithItem(MSBlocks.MIND_ASPECT_LEAVES);
		simpleBlockWithItem(MSBlocks.RAGE_ASPECT_LEAVES);
		simpleBlockWithItem(MSBlocks.SPACE_ASPECT_LEAVES);
		simpleBlockWithItem(MSBlocks.TIME_ASPECT_LEAVES);
		simpleBlockWithItem(MSBlocks.VOID_ASPECT_LEAVES);
		
		simpleBlock(MSBlocks.BLOOD_ASPECT_SAPLING,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSBlocks.BLOOD_ASPECT_SAPLING, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.BREATH_ASPECT_SAPLING,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSBlocks.BREATH_ASPECT_SAPLING, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.DOOM_ASPECT_SAPLING,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSBlocks.DOOM_ASPECT_SAPLING, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.HEART_ASPECT_SAPLING,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSBlocks.HEART_ASPECT_SAPLING, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.HOPE_ASPECT_SAPLING,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSBlocks.HOPE_ASPECT_SAPLING, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.LIFE_ASPECT_SAPLING,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSBlocks.LIFE_ASPECT_SAPLING, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.LIGHT_ASPECT_SAPLING,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSBlocks.LIGHT_ASPECT_SAPLING, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.MIND_ASPECT_SAPLING,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSBlocks.MIND_ASPECT_SAPLING, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.RAGE_ASPECT_SAPLING,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSBlocks.RAGE_ASPECT_SAPLING, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.SPACE_ASPECT_SAPLING,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSBlocks.SPACE_ASPECT_SAPLING, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.TIME_ASPECT_SAPLING,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSBlocks.TIME_ASPECT_SAPLING, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.VOID_ASPECT_SAPLING,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSBlocks.VOID_ASPECT_SAPLING, MSBlockStateProvider::texture);
		
		simpleBlockWithItem(MSBlocks.BLOOD_ASPECT_BOOKSHELF,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture("blood_aspect_planks")));
		simpleBlockWithItem(MSBlocks.BREATH_ASPECT_BOOKSHELF,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture("breath_aspect_planks")));
		simpleBlockWithItem(MSBlocks.DOOM_ASPECT_BOOKSHELF,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture("doom_aspect_planks")));
		simpleBlockWithItem(MSBlocks.HEART_ASPECT_BOOKSHELF,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture("heart_aspect_planks")));
		simpleBlockWithItem(MSBlocks.HOPE_ASPECT_BOOKSHELF,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture("hope_aspect_planks")));
		simpleBlockWithItem(MSBlocks.LIFE_ASPECT_BOOKSHELF,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture("life_aspect_planks")));
		simpleBlockWithItem(MSBlocks.LIGHT_ASPECT_BOOKSHELF,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture("light_aspect_planks")));
		simpleBlockWithItem(MSBlocks.MIND_ASPECT_BOOKSHELF,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture("mind_aspect_planks")));
		simpleBlockWithItem(MSBlocks.RAGE_ASPECT_BOOKSHELF,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture("rage_aspect_planks")));
		simpleBlockWithItem(MSBlocks.SPACE_ASPECT_BOOKSHELF,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture("space_aspect_planks")));
		simpleBlockWithItem(MSBlocks.TIME_ASPECT_BOOKSHELF,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture("time_aspect_planks")));
		simpleBlockWithItem(MSBlocks.VOID_ASPECT_BOOKSHELF,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture("void_aspect_planks")));
		
		simpleBlockWithItem(MSBlocks.GLOWING_BOOKSHELF,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture("glowing_planks")));
		simpleBlockWithItem(MSBlocks.FROST_BOOKSHELF,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture("frost_planks")));
		simpleBlockWithItem(MSBlocks.RAINBOW_BOOKSHELF,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture("rainbow_planks")));
		simpleBlockWithItem(MSBlocks.END_BOOKSHELF,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture("end_planks")));
		simpleBlockWithItem(MSBlocks.DEAD_BOOKSHELF,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture("dead_planks")));
		simpleBlockWithItem(MSBlocks.TREATED_BOOKSHELF,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture("treated_planks")));
		
		//Ladders
		horizontal(MSBlocks.BLOOD_ASPECT_LADDER, this::ladder);
		flatItem(MSBlocks.BLOOD_ASPECT_LADDER, MSBlockStateProvider::texture);
		horizontal(MSBlocks.BREATH_ASPECT_LADDER, this::ladder);
		flatItem(MSBlocks.BREATH_ASPECT_LADDER, MSBlockStateProvider::texture);
		horizontal(MSBlocks.DOOM_ASPECT_LADDER, this::ladder);
		flatItem(MSBlocks.DOOM_ASPECT_LADDER, MSBlockStateProvider::texture);
		horizontal(MSBlocks.HEART_ASPECT_LADDER, this::ladder);
		flatItem(MSBlocks.HEART_ASPECT_LADDER, MSBlockStateProvider::texture);
		horizontal(MSBlocks.HOPE_ASPECT_LADDER, this::ladder);
		flatItem(MSBlocks.HOPE_ASPECT_LADDER, MSBlockStateProvider::texture);
		horizontal(MSBlocks.LIFE_ASPECT_LADDER, this::ladder);
		flatItem(MSBlocks.LIFE_ASPECT_LADDER, MSBlockStateProvider::texture);
		horizontal(MSBlocks.LIGHT_ASPECT_LADDER, this::ladder);
		flatItem(MSBlocks.LIGHT_ASPECT_LADDER, MSBlockStateProvider::texture);
		horizontal(MSBlocks.MIND_ASPECT_LADDER, this::ladder);
		flatItem(MSBlocks.MIND_ASPECT_LADDER, MSBlockStateProvider::texture);
		horizontal(MSBlocks.RAGE_ASPECT_LADDER, this::ladder);
		flatItem(MSBlocks.RAGE_ASPECT_LADDER, MSBlockStateProvider::texture);
		horizontal(MSBlocks.SPACE_ASPECT_LADDER, this::ladder);
		flatItem(MSBlocks.SPACE_ASPECT_LADDER, MSBlockStateProvider::texture);
		horizontal(MSBlocks.TIME_ASPECT_LADDER, this::ladder);
		flatItem(MSBlocks.TIME_ASPECT_LADDER, MSBlockStateProvider::texture);
		horizontal(MSBlocks.VOID_ASPECT_LADDER, this::ladder);
		flatItem(MSBlocks.VOID_ASPECT_LADDER, MSBlockStateProvider::texture);
		
		horizontal(MSBlocks.GLOWING_LADDER, this::ladder);
		flatItem(MSBlocks.GLOWING_LADDER, MSBlockStateProvider::texture);
		horizontal(MSBlocks.FROST_LADDER, this::ladder);
		flatItem(MSBlocks.FROST_LADDER, MSBlockStateProvider::texture);
		horizontal(MSBlocks.RAINBOW_LADDER, this::ladder);
		flatItem(MSBlocks.RAINBOW_LADDER, MSBlockStateProvider::texture);
		horizontal(MSBlocks.END_LADDER, this::ladder);
		flatItem(MSBlocks.END_LADDER, MSBlockStateProvider::texture);
		horizontal(MSBlocks.DEAD_LADDER, this::ladder);
		flatItem(MSBlocks.DEAD_LADDER, MSBlockStateProvider::texture);
		horizontal(MSBlocks.TREATED_LADDER, this::ladder);
		flatItem(MSBlocks.TREATED_LADDER, MSBlockStateProvider::texture);
		
		//Land Plant Blocks
		simpleBlock(MSBlocks.DESERT_BUSH,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSBlocks.DESERT_BUSH, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.PETRIFIED_GRASS,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSBlocks.PETRIFIED_GRASS, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.PETRIFIED_POPPY,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSBlocks.PETRIFIED_POPPY, MSBlockStateProvider::texture);
		
		simpleBlock(MSBlocks.GLOWING_MUSHROOM_VINES,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSBlocks.GLOWING_MUSHROOM_VINES, MSBlockStateProvider::texture);
		
		directionalWithItem(MSBlocks.STRAWBERRY,
				id -> models().cubeBottomTop(id.getPath(),
						texture(id.withSuffix("_side")),
						texture(id.withSuffix("_bottom")),
						texture(id.withSuffix("_top"))));
		horizontal(MSBlocks.ATTACHED_STRAWBERRY_STEM, 270,
				id -> models().withExistingParent(id.getPath(), "block/attached_melon_stem").renderType("cutout"));
		
		flatItem(MSBlocks.TALL_END_GRASS, id -> texture(id.withSuffix("_top")));
		simpleBlock(MSBlocks.GLOWFLOWER,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSBlocks.GLOWFLOWER, MSBlockStateProvider::texture);
		
		//Special Land Blocks
		simpleBlockWithItem(MSBlocks.GLOWY_GOOP);
		simpleBlockWithItem(MSBlocks.COAGULATED_BLOOD);
		unflippedColumnWithItem(MSBlocks.PIPE, this::existing);
		simpleBlockWithItem(MSBlocks.PIPE_INTERSECTION);
		horizontalWithItem(MSBlocks.PARCEL_PYXIS, this::existing);
		horizontalWithItem(MSBlocks.PYXIS_LID,
				id -> models().getExistingFile(id));
		horizontalWithItem(MSBlocks.NAKAGATOR_STATUE, this::existing);
		
		//Structure Land Blocks
		stairsWithItem(MSBlocks.BLACK_CHESS_BRICK_STAIRS, "black_chess_brick", texture(MSBlocks.BLACK_CHESS_BRICKS));
		stairsWithItem(MSBlocks.DARK_GRAY_CHESS_BRICK_STAIRS, "dark_gray_chess_brick", texture(MSBlocks.DARK_GRAY_CHESS_BRICKS));
		stairsWithItem(MSBlocks.LIGHT_GRAY_CHESS_BRICK_STAIRS, "light_gray_chess_brick", texture(MSBlocks.LIGHT_GRAY_CHESS_BRICKS));
		stairsWithItem(MSBlocks.WHITE_CHESS_BRICK_STAIRS, "white_chess_brick", texture(MSBlocks.WHITE_CHESS_BRICKS));
		stairsWithItem(MSBlocks.COARSE_STONE_STAIRS, MSBlocks.COARSE_STONE);
		stairsWithItem(MSBlocks.COARSE_STONE_BRICK_STAIRS, "coarse_stone_brick", texture(MSBlocks.COARSE_STONE_BRICKS));
		stairsWithItem(MSBlocks.SHADE_STAIRS, "shade", texture(MSBlocks.SHADE_STONE));
		stairsWithItem(MSBlocks.SHADE_BRICK_STAIRS, "shade_brick", texture(MSBlocks.SHADE_BRICKS));
		stairsWithItem(MSBlocks.FROST_TILE_STAIRS, MSBlocks.FROST_TILE);
		stairsWithItem(MSBlocks.FROST_BRICK_STAIRS, "frost_brick", texture(MSBlocks.FROST_BRICKS));
		stairsWithItem(MSBlocks.CAST_IRON_STAIRS, MSBlocks.CAST_IRON);
		stairsWithItem(MSBlocks.BLACK_STONE_STAIRS, MSBlocks.BLACK_STONE);
		stairsWithItem(MSBlocks.BLACK_STONE_BRICK_STAIRS, "black_stone_brick", texture(MSBlocks.BLACK_STONE_BRICKS));
		stairsWithItem(MSBlocks.FLOWERY_MOSSY_STONE_BRICK_STAIRS, "flowery_mossy_stone_brick", texture("flowery_mossy_stone_bricks1"));
		stairsWithItem(MSBlocks.MYCELIUM_STAIRS, "mycelium", texture(MSBlocks.MYCELIUM_STONE));
		stairsWithItem(MSBlocks.MYCELIUM_BRICK_STAIRS, "mycelium_brick", texture(MSBlocks.MYCELIUM_BRICKS));
		stairsWithItem(MSBlocks.CHALK_STAIRS, MSBlocks.CHALK);
		stairsWithItem(MSBlocks.CHALK_BRICK_STAIRS, "chalk_brick", texture(MSBlocks.CHALK_BRICKS));
		stairsWithItem(MSBlocks.PINK_STONE_STAIRS, MSBlocks.PINK_STONE);
		stairsWithItem(MSBlocks.PINK_STONE_BRICK_STAIRS, "pink_stone_brick", texture(MSBlocks.PINK_STONE_BRICKS));
		stairsWithItem(MSBlocks.BROWN_STONE_STAIRS, MSBlocks.BROWN_STONE);
		stairsWithItem(MSBlocks.BROWN_STONE_BRICK_STAIRS, "brown_stone_brick", texture(MSBlocks.BROWN_STONE_BRICKS));
		stairsWithItem(MSBlocks.GREEN_STONE_STAIRS, MSBlocks.GREEN_STONE);
		stairsWithItem(MSBlocks.GREEN_STONE_BRICK_STAIRS, "green_stone_brick",
				texture(MSBlocks.HORIZONTAL_GREEN_STONE_BRICKS), texture(MSBlocks.POLISHED_GREEN_STONE), texture(MSBlocks.POLISHED_GREEN_STONE));
		stairsWithItem(MSBlocks.RAINBOW_PLANKS_STAIRS, MSBlocks.RAINBOW_PLANKS);
		stairsWithItem(MSBlocks.END_PLANKS_STAIRS, MSBlocks.END_PLANKS);
		stairsWithItem(MSBlocks.DEAD_PLANKS_STAIRS, MSBlocks.DEAD_PLANKS);
		stairsWithItem(MSBlocks.TREATED_PLANKS_STAIRS, MSBlocks.TREATED_PLANKS);
		
		horizontalWithItem(MSBlocks.STEEP_GREEN_STONE_BRICK_STAIRS_BASE, this::existing);
		horizontalWithItem(MSBlocks.STEEP_GREEN_STONE_BRICK_STAIRS_TOP, this::existing);
		
	}
	
	private ModelFile existing(ResourceLocation id)
	{
		return this.models().getExistingFile(id);
	}
	
	private ModelFile ladder(ResourceLocation id)
	{
		ResourceLocation texture = texture(id);
		String textureKey = "texture";
		return this.models().getBuilder(id.getPath())
				.ao(false)
				.renderType("cutout")
				.texture("particle", texture)
				.texture(textureKey, texture)
				.element()
				.from(0, 0, 15.2F)
				.to(16, 16, 15.2F)
				.shade(false)
				.face(Direction.NORTH).uvs(0, 0, 16, 16).texture('#' + textureKey).end()
				.face(Direction.SOUTH).uvs(0, 0, 16, 16).texture('#' + textureKey).end()
				.end();
	}
	
	private void simpleBlockWithItem(RegistryObject<Block> block)
	{
		simpleBlockWithItem(block.get(), cubeAll(block.get()));
	}
	
	private void simpleBlock(RegistryObject<? extends Block> block, Function<ResourceLocation, ModelFile> modelProvider)
	{
		simpleBlock(block.get(), modelProvider.apply(block.getId()));
	}
	
	private void simpleBlockWithItem(RegistryObject<Block> block, Function<ResourceLocation, ModelFile> modelProvider)
	{
		simpleBlockWithItem(block.get(), modelProvider.apply(block.getId()));
	}
	
	private void horizontal(RegistryObject<? extends Block> block, Function<ResourceLocation, ModelFile> modelProvider)
	{
		horizontal(block, 180, modelProvider);
	}
	
	private void horizontal(RegistryObject<? extends Block> block, int angleOffset, Function<ResourceLocation, ModelFile> modelProvider)
	{
		var model = modelProvider.apply(block.getId());
		getVariantBuilder(block.get())
				.forAllStatesExcept(state -> ConfiguredModel.builder()
								.modelFile(model)
								.rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + angleOffset) % 360)
								.build(),
						BlockStateProperties.WATERLOGGED
				);
	}
	
	private void horizontalWithItem(RegistryObject<Block> block, Function<ResourceLocation, ModelFile> modelProvider)
	{
		var model = modelProvider.apply(block.getId());
		getVariantBuilder(block.get())
				.forAllStatesExcept(state -> ConfiguredModel.builder()
								.modelFile(model)
								.rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
								.build(),
						BlockStateProperties.WATERLOGGED
				);
		simpleBlockItem(block.get(), model);
	}
	
	private void directionalWithItem(RegistryObject<? extends Block> block, Function<ResourceLocation, ModelFile> modelProvider)
	{
		var model = modelProvider.apply(block.getId());
		directionalBlock(block.get(), model);
		simpleBlockItem(block.get(), model);
	}
	
	/**
	 * While the standard directional block has the down-facing state rotated 180 degrees along the y-axis,
	 * blocks with this function are instead not rotated in both the up-facing state and the down-facing state.
	 */
	private void unflippedColumnWithItem(RegistryObject<Block> block, Function<ResourceLocation, ModelFile> modelProvider)
	{
		var model = modelProvider.apply(block.getId());
		getVariantBuilder(block.get())
				.forAllStatesExcept(state -> {
					Direction dir = state.getValue(BlockStateProperties.FACING);
					return ConfiguredModel.builder()
							.modelFile(model)
							.rotationX(dir.getAxis().isHorizontal() ? 90 : 0)
							.rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
							.build();
				}, BlockStateProperties.WATERLOGGED);
		simpleBlockItem(block.get(), model);
	}
	
	private void trimWithItem(RegistryObject<Block> block, Function<ResourceLocation, ModelFile> modelProvider, Function<ResourceLocation, ModelFile> flippedModelProvider)
	{
		var model = modelProvider.apply(block.getId());
		var flippedModel = flippedModelProvider.apply(block.getId());
		getVariantBuilder(block.get())
				.forAllStates(state -> {
					Direction dir = state.getValue(BlockStateProperties.FACING);
					return ConfiguredModel.builder()
							.modelFile(dir == Direction.DOWN || dir == Direction.SOUTH || dir == Direction.WEST
									? flippedModel : model)
							.rotationX(dir.getAxis().isHorizontal() ? 90 : 0)
							.rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
							.build();
				});
		simpleBlockItem(block.get(), model);
	}
	
	private void axisWithItem(RegistryObject<Block> block, Function<ResourceLocation, ModelFile> modelProvider)
	{
		var model = modelProvider.apply(block.getId());
		axisBlock((RotatedPillarBlock) block.get(), model, model);
		simpleBlockItem(block.get(), model);
	}
	
	private void stairsWithItem(RegistryObject<StairBlock> block, RegistryObject<? extends Block> sourceBlock)
	{
		stairsWithItem(block, sourceBlock.getId().getPath(), texture(sourceBlock));
	}
	
	private void stairsWithItem(RegistryObject<StairBlock> block, String baseName, ResourceLocation texture)
	{
		stairsWithItem(block, baseName, texture, texture, texture);
	}
	
	private void stairsWithItem(RegistryObject<StairBlock> block, String baseName, ResourceLocation side, ResourceLocation bottom, ResourceLocation top)
	{
		ModelFile stairs = models().stairs(baseName + "_stairs", side, bottom, top);
		ModelFile stairsInner = models().stairsInner(baseName + "_inner_stairs", side, bottom, top);
		ModelFile stairsOuter = models().stairsOuter(baseName + "_outer_stairs", side, bottom, top);
		stairsBlock(block.get(), stairs, stairsInner, stairsOuter);
		simpleBlockItem(block.get(), stairs);
	}
	
	private ItemModelBuilder flatItem(RegistryObject<? extends Block> block, Function<ResourceLocation, ResourceLocation> textureProvider)
	{
		return itemModels().withExistingParent(block.getId().getPath(),
				new ResourceLocation("item/generated")).texture("layer0",
				textureProvider.apply(block.getId()));
	}
	
	public static ResourceLocation texture(RegistryObject<? extends Block> block)
	{
		return texture(block.getId());
	}
	
	public static ResourceLocation texture(ResourceLocation id)
	{
		return id.withPrefix(ModelProvider.BLOCK_FOLDER + "/");
	}
	
	public static ResourceLocation texture(String path)
	{
		return new ResourceLocation(Minestuck.MOD_ID, ModelProvider.BLOCK_FOLDER + "/" + path);
	}
	
	public static ResourceLocation itemTexture(ResourceLocation id)
	{
		return id.withPrefix(ModelProvider.ITEM_FOLDER + "/");
	}
	
	public static ResourceLocation vanillaTexture(String path)
	{
		return new ResourceLocation(ModelProvider.BLOCK_FOLDER + "/" + path);
	}
}
