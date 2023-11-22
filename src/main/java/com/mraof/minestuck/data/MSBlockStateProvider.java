package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.CruxiteDowelBlock;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.block.TrajectoryBlock;
import com.mraof.minestuck.block.redstone.*;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
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
				id -> empty(id.getPath(), itemTexture(id)));
		flatItem(MSItems.SKAIA_PORTAL, MSBlockStateProvider::itemTexture);
		
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
		simpleHorizontalWithItem(MSBlocks.GREEN_STONE_BRICK_EMBEDDED_LADDER,
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
		flatItem(MSItems.RAINBOW_SAPLING, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.END_SAPLING,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.END_SAPLING, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.SHADEWOOD_SAPLING,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.SHADEWOOD_SAPLING, MSBlockStateProvider::texture);
		
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
		flatItem(MSItems.BLOOD_ASPECT_SAPLING, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.BREATH_ASPECT_SAPLING,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.BREATH_ASPECT_SAPLING, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.DOOM_ASPECT_SAPLING,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.DOOM_ASPECT_SAPLING, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.HEART_ASPECT_SAPLING,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.HEART_ASPECT_SAPLING, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.HOPE_ASPECT_SAPLING,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.HOPE_ASPECT_SAPLING, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.LIFE_ASPECT_SAPLING,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.LIFE_ASPECT_SAPLING, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.LIGHT_ASPECT_SAPLING,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.LIGHT_ASPECT_SAPLING, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.MIND_ASPECT_SAPLING,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.MIND_ASPECT_SAPLING, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.RAGE_ASPECT_SAPLING,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.RAGE_ASPECT_SAPLING, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.SPACE_ASPECT_SAPLING,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.SPACE_ASPECT_SAPLING, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.TIME_ASPECT_SAPLING,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.TIME_ASPECT_SAPLING, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.VOID_ASPECT_SAPLING,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.VOID_ASPECT_SAPLING, MSBlockStateProvider::texture);
		
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
		simpleHorizontal(MSBlocks.BLOOD_ASPECT_LADDER, this::ladder);
		flatItem(MSItems.BLOOD_ASPECT_LADDER, MSBlockStateProvider::texture);
		simpleHorizontal(MSBlocks.BREATH_ASPECT_LADDER, this::ladder);
		flatItem(MSItems.BREATH_ASPECT_LADDER, MSBlockStateProvider::texture);
		simpleHorizontal(MSBlocks.DOOM_ASPECT_LADDER, this::ladder);
		flatItem(MSItems.DOOM_ASPECT_LADDER, MSBlockStateProvider::texture);
		simpleHorizontal(MSBlocks.HEART_ASPECT_LADDER, this::ladder);
		flatItem(MSItems.HEART_ASPECT_LADDER, MSBlockStateProvider::texture);
		simpleHorizontal(MSBlocks.HOPE_ASPECT_LADDER, this::ladder);
		flatItem(MSItems.HOPE_ASPECT_LADDER, MSBlockStateProvider::texture);
		simpleHorizontal(MSBlocks.LIFE_ASPECT_LADDER, this::ladder);
		flatItem(MSItems.LIFE_ASPECT_LADDER, MSBlockStateProvider::texture);
		simpleHorizontal(MSBlocks.LIGHT_ASPECT_LADDER, this::ladder);
		flatItem(MSItems.LIGHT_ASPECT_LADDER, MSBlockStateProvider::texture);
		simpleHorizontal(MSBlocks.MIND_ASPECT_LADDER, this::ladder);
		flatItem(MSItems.MIND_ASPECT_LADDER, MSBlockStateProvider::texture);
		simpleHorizontal(MSBlocks.RAGE_ASPECT_LADDER, this::ladder);
		flatItem(MSItems.RAGE_ASPECT_LADDER, MSBlockStateProvider::texture);
		simpleHorizontal(MSBlocks.SPACE_ASPECT_LADDER, this::ladder);
		flatItem(MSItems.SPACE_ASPECT_LADDER, MSBlockStateProvider::texture);
		simpleHorizontal(MSBlocks.TIME_ASPECT_LADDER, this::ladder);
		flatItem(MSItems.TIME_ASPECT_LADDER, MSBlockStateProvider::texture);
		simpleHorizontal(MSBlocks.VOID_ASPECT_LADDER, this::ladder);
		flatItem(MSItems.VOID_ASPECT_LADDER, MSBlockStateProvider::texture);
		
		simpleHorizontal(MSBlocks.GLOWING_LADDER, this::ladder);
		flatItem(MSItems.GLOWING_LADDER, MSBlockStateProvider::texture);
		simpleHorizontal(MSBlocks.FROST_LADDER, this::ladder);
		flatItem(MSItems.FROST_LADDER, MSBlockStateProvider::texture);
		simpleHorizontal(MSBlocks.RAINBOW_LADDER, this::ladder);
		flatItem(MSItems.RAINBOW_LADDER, MSBlockStateProvider::texture);
		simpleHorizontal(MSBlocks.END_LADDER, this::ladder);
		flatItem(MSItems.END_LADDER, MSBlockStateProvider::texture);
		simpleHorizontal(MSBlocks.DEAD_LADDER, this::ladder);
		flatItem(MSItems.DEAD_LADDER, MSBlockStateProvider::texture);
		simpleHorizontal(MSBlocks.TREATED_LADDER, this::ladder);
		flatItem(MSItems.TREATED_LADDER, MSBlockStateProvider::texture);
		
		//Land Plant Blocks
		simpleBlock(MSBlocks.DESERT_BUSH,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.DESERT_BUSH, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.PETRIFIED_GRASS,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.PETRIFIED_GRASS, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.PETRIFIED_POPPY,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.PETRIFIED_POPPY, MSBlockStateProvider::texture);
		
		simpleBlock(MSBlocks.GLOWING_MUSHROOM_VINES,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.GLOWING_MUSHROOM_VINES, MSBlockStateProvider::texture);
		
		directionalWithItem(MSBlocks.STRAWBERRY,
				id -> models().cubeBottomTop(id.getPath(),
						texture(id.withSuffix("_side")),
						texture(id.withSuffix("_bottom")),
						texture(id.withSuffix("_top"))));
		simpleHorizontal(MSBlocks.ATTACHED_STRAWBERRY_STEM, 270,
				id -> models().withExistingParent(id.getPath(), "block/attached_melon_stem").renderType("cutout"));
		
		flatItem(MSItems.TALL_END_GRASS, id -> texture(id.withSuffix("_top")));
		simpleBlock(MSBlocks.GLOWFLOWER,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.GLOWFLOWER, MSBlockStateProvider::texture);
		
		//Special Land Blocks
		simpleBlockWithItem(MSBlocks.GLOWY_GOOP);
		simpleBlockWithItem(MSBlocks.COAGULATED_BLOOD);
		unflippedColumnWithItem(MSBlocks.PIPE, this::existing);
		simpleBlockWithItem(MSBlocks.PIPE_INTERSECTION);
		simpleHorizontalWithItem(MSBlocks.PARCEL_PYXIS, this::existing);
		simpleHorizontalWithItem(MSBlocks.PYXIS_LID,
				id -> models().getExistingFile(id));
		simpleHorizontalWithItem(MSBlocks.NAKAGATOR_STATUE, this::existing);
		
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
		
		simpleHorizontalWithItem(MSBlocks.STEEP_GREEN_STONE_BRICK_STAIRS_BASE, this::existing);
		simpleHorizontalWithItem(MSBlocks.STEEP_GREEN_STONE_BRICK_STAIRS_TOP, this::existing);
		
		slabWithItem(MSBlocks.BLACK_CHESS_BRICK_SLAB, MSBlocks.BLACK_CHESS_BRICKS);
		slabWithItem(MSBlocks.DARK_GRAY_CHESS_BRICK_SLAB, MSBlocks.DARK_GRAY_CHESS_BRICKS);
		slabWithItem(MSBlocks.LIGHT_GRAY_CHESS_BRICK_SLAB, MSBlocks.LIGHT_GRAY_CHESS_BRICKS);
		slabWithItem(MSBlocks.WHITE_CHESS_BRICK_SLAB, MSBlocks.WHITE_CHESS_BRICKS);
		slabWithItem(MSBlocks.FLOWERY_MOSSY_STONE_BRICK_SLAB, MSBlocks.FLOWERY_MOSSY_STONE_BRICKS.getId().withSuffix("1"));
		slabWithItem(MSBlocks.COARSE_STONE_SLAB, MSBlocks.COARSE_STONE);
		slabWithItem(MSBlocks.COARSE_STONE_BRICK_SLAB, MSBlocks.COARSE_STONE_BRICKS);
		slabWithItem(MSBlocks.SHADE_SLAB, MSBlocks.SHADE_STONE);
		slabWithItem(MSBlocks.SHADE_BRICK_SLAB, MSBlocks.SHADE_BRICKS);
		slabWithItem(MSBlocks.FROST_TILE_SLAB, MSBlocks.FROST_TILE);
		slabWithItem(MSBlocks.FROST_BRICK_SLAB, MSBlocks.FROST_BRICKS);
		slabWithItem(MSBlocks.BLACK_STONE_SLAB, MSBlocks.BLACK_STONE);
		slabWithItem(MSBlocks.BLACK_STONE_BRICK_SLAB, MSBlocks.BLACK_STONE_BRICKS);
		slabWithItem(MSBlocks.MYCELIUM_SLAB, MSBlocks.MYCELIUM_STONE);
		slabWithItem(MSBlocks.MYCELIUM_BRICK_SLAB, MSBlocks.MYCELIUM_BRICKS);
		slabWithItem(MSBlocks.CHALK_SLAB, MSBlocks.CHALK);
		slabWithItem(MSBlocks.CHALK_BRICK_SLAB, MSBlocks.CHALK_BRICKS);
		slabWithItem(MSBlocks.PINK_STONE_SLAB, MSBlocks.PINK_STONE);
		slabWithItem(MSBlocks.PINK_STONE_BRICK_SLAB, MSBlocks.PINK_STONE_BRICKS);
		slabWithItem(MSBlocks.BROWN_STONE_SLAB, MSBlocks.BROWN_STONE);
		slabWithItem(MSBlocks.BROWN_STONE_BRICK_SLAB, MSBlocks.BROWN_STONE_BRICKS);
		slabWithItem(MSBlocks.GREEN_STONE_SLAB, MSBlocks.GREEN_STONE);
		slabWithItem(MSBlocks.GREEN_STONE_BRICK_SLAB, MSBlocks.GREEN_STONE_BRICKS.getId(),
				texture("horizontal_green_stone_bricks"), texture("polished_green_stone"));
		slabWithItem(MSBlocks.RAINBOW_PLANKS_SLAB, MSBlocks.RAINBOW_PLANKS);
		slabWithItem(MSBlocks.END_PLANKS_SLAB, MSBlocks.END_PLANKS);
		slabWithItem(MSBlocks.DEAD_PLANKS_SLAB, MSBlocks.DEAD_PLANKS);
		slabWithItem(MSBlocks.TREATED_PLANKS_SLAB, MSBlocks.TREATED_PLANKS);
		
		{
			ModelFile verticalUnpowered = models()
					.cubeColumn("trajectory_block_vertical_unpowered",
					texture("redstone_machine_block"),
					texture("trajectory_block_vertical_top_unpowered"));
			ModelFile verticalPowered = models()
					.cubeColumn("trajectory_block_vertical_powered",
							texture("redstone_machine_block"),
							texture("trajectory_block_vertical_top_powered"));
			ModelFile horizontalUnpowered = models().getExistingFile(id("trajectory_block_horizontal_unpowered"));
			ModelFile horizontalPowered = models().getExistingFile(id("trajectory_block_horizontal_powered"));
			directional(MSBlocks.TRAJECTORY_BLOCK, state -> {
				Direction direction = state.getValue(TrajectoryBlock.FACING);
				boolean powered = state.getValue(TrajectoryBlock.POWERED);
				
				if(direction.getAxis() == Direction.Axis.Y)
					return powered ? verticalPowered : verticalUnpowered;
				else
					return powered ? horizontalPowered : horizontalUnpowered;
			}, BlockStateProperties.POWER);
			simpleBlockItem(MSBlocks.TRAJECTORY_BLOCK.get(),
					models().getExistingFile(id("trajectory_block_vertical_unpowered")));
		}
		{
			Function<ResourceLocation, ModelFile> modelProvider = modelId -> models().cubeBottomTop(modelId.getPath(),
					texture("redstone_machine_block"),
					texture(modelId),
					texture(modelId));
			powerVariableWithItem(MSBlocks.STAT_STORER,
					modelProvider.apply(id("stat_storer_high_power")),
					modelProvider.apply(id("stat_storer_medium_power")),
					modelProvider.apply(id("stat_storer_low_power")),
					modelProvider.apply(id("stat_storer_unpowered")));
		}
		{
			ModelFile powered = cubeAll(id("remote_observer_powered"));
			ModelFile unpowered = cubeAll(id("remote_observer_unpowered"));
			getVariantBuilder(MSBlocks.REMOTE_OBSERVER.get())
					.partialState().with(RemoteObserverBlock.POWERED, true).modelForState().modelFile(powered).addModel()
					.partialState().with(RemoteObserverBlock.POWERED, false).modelForState().modelFile(unpowered).addModel();
			simpleBlockItem(MSBlocks.REMOTE_OBSERVER.get(), unpowered);
		}
		{
			ModelFile poweredModel = models().getExistingFile(id("wireless_redstone_transmitter_powered"));
			ModelFile unpoweredModel = models().getExistingFile(id("wireless_redstone_transmitter_unpowered"));
			horizontal(MSBlocks.WIRELESS_REDSTONE_TRANSMITTER,
					state -> state.getValue(WirelessRedstoneTransmitterBlock.POWERED) ? poweredModel : unpoweredModel,
					BlockStateProperties.POWER);
			simpleBlockItem(MSBlocks.WIRELESS_REDSTONE_TRANSMITTER.get(), unpoweredModel);
		}
		{
			ModelFile poweredModel = models().getExistingFile(id("wireless_redstone_receiver_powered"));
			ModelFile unpoweredModel = models().getExistingFile(id("wireless_redstone_receiver_unpowered"));
			horizontal(MSBlocks.WIRELESS_REDSTONE_RECEIVER,
					state -> state.getValue(WirelessRedstoneReceiverBlock.POWERED) ? poweredModel : unpoweredModel,
					MSProperties.MACHINE_TOGGLE, BlockStateProperties.POWER);
			simpleBlockItem(MSBlocks.WIRELESS_REDSTONE_RECEIVER.get(), unpoweredModel);
		}
		{
			ModelFile powered = cubeAll(id("solid_switch_powered"));
			ModelFile unpowered = cubeAll(id("solid_switch_unpowered"));
			getVariantBuilder(MSBlocks.SOLID_SWITCH.get())
					.partialState().with(SolidSwitchBlock.POWERED, true).modelForState().modelFile(powered).addModel()
					.partialState().with(SolidSwitchBlock.POWERED, false).modelForState().modelFile(unpowered).addModel();
			simpleBlockItem(MSBlocks.SOLID_SWITCH.get(), unpowered);
		}
		{
			powerVariableWithItem(MSBlocks.VARIABLE_SOLID_SWITCH,
					cubeAll(id("variable_solid_switch_high_power")),
					cubeAll(id("variable_solid_switch_medium_power")),
					cubeAll(id("variable_solid_switch_low_power")),
					cubeAll(id("variable_solid_switch_unpowered")));
		}
		{
			ModelFile highPower = cubeAll(id("timed_solid_switch_high_power"));
			ModelFile mediumPower = cubeAll(id("timed_solid_switch_medium_power"));
			ModelFile lowPower = cubeAll(id("timed_solid_switch_low_power"));
			ModelFile unpowered = cubeAll(id("timed_solid_switch_unpowered"));
			powerVariableWithItem(MSBlocks.ONE_SECOND_INTERVAL_TIMED_SOLID_SWITCH, highPower, mediumPower, lowPower, unpowered);
			powerVariableWithItem(MSBlocks.TWO_SECOND_INTERVAL_TIMED_SOLID_SWITCH, highPower, mediumPower, lowPower, unpowered);
		}
		{
			ModelFile triggered = cubeAll(id("summoner_triggered"));
			ModelFile untriggered = cubeAll(id("summoner_untriggered"));
			getVariantBuilder(MSBlocks.SUMMONER.get())
					.partialState().with(SummonerBlock.TRIGGERED, true).modelForState().modelFile(triggered).addModel()
					.partialState().with(SummonerBlock.TRIGGERED, false).modelForState().modelFile(untriggered).addModel();
			simpleBlockItem(MSBlocks.SUMMONER.get(), untriggered);
		}
		{
			ModelFile poweredModel = models().getExistingFile(id("area_effect_block_powered"));
			ModelFile unpoweredModel = models().getExistingFile(id("area_effect_block_unpowered"));
			horizontal(MSBlocks.AREA_EFFECT_BLOCK,
					state -> state.getValue(AreaEffectBlock.POWERED) ? poweredModel : unpoweredModel,
					AreaEffectBlock.ALL_MOBS, AreaEffectBlock.SHUT_DOWN);
			simpleBlockItem(MSBlocks.AREA_EFFECT_BLOCK.get(), unpoweredModel);
		}
		directionalWithItem(MSBlocks.ROTATOR,
				id -> models().cubeBottomTop(id.getPath(),
						texture(id.withSuffix("_side")),
						texture(id.withSuffix("_bottom")),
						texture(id.withSuffix("_top"))));
		directionalWithItem(MSBlocks.TOGGLER,
				id -> models().cubeBottomTop(id.getPath(),
						texture("rotator_side"),
						texture("rotator_bottom"),
						texture("toggler_top")));
		simpleHorizontalWithItem(MSBlocks.STRUCTURE_CORE,
				id -> models().cubeBottomTop(id.getPath(),
						texture(id.withSuffix("_side")),
						texture(id.withSuffix("_bottom")),
						texture(id.withSuffix("_top"))));
		simpleBlockWithItem(MSBlocks.FALL_PAD,
				id -> models().cubeBottomTop(id.getPath(),
						texture(id.withSuffix("_side")),
						texture(id.withSuffix("_bottom")),
						texture(id.withSuffix("_top"))));
		simpleBlockWithItem(MSBlocks.FRAGILE_STONE);
		simpleHorizontalWithItem(MSBlocks.SPIKES, this::existing);
		{
			ModelFile extended = existing(id("retractable_spikes_extended"));
			ModelFile retracted = models().cubeBottomTop("retractable_spikes_retracted",
					texture("spikes"),
					texture("spikes"),
					texture("retractable_spikes_top_retracted"));
			getVariantBuilder(MSBlocks.RETRACTABLE_SPIKES.get())
					.partialState().with(RetractableSpikesBlock.POWERED, true).modelForState().modelFile(extended).addModel()
					.partialState().with(RetractableSpikesBlock.POWERED, false).modelForState().modelFile(retracted).addModel();
			simpleBlockItem(MSBlocks.RETRACTABLE_SPIKES.get(), retracted);
		}
		{
			ModelFile retracted = models().cubeBottomTop("block_pressure_plate_retracted",
					texture("block_pressure_plate_side"),
					texture("block_pressure_plate_bottom"),
					texture("block_pressure_plate_top"));
			ModelFile extended = existing(id("block_pressure_plate_extended"));
			getVariantBuilder(MSBlocks.BLOCK_PRESSURE_PLATE.get())
					.partialState().with(RetractableSpikesBlock.POWERED, true).modelForState().modelFile(retracted).addModel()
					.partialState().with(RetractableSpikesBlock.POWERED, false).modelForState().modelFile(extended).addModel();
			simpleBlockItem(MSBlocks.BLOCK_PRESSURE_PLATE.get(), extended);
		}
		simpleBlockWithItem(MSBlocks.PUSHABLE_BLOCK,
				id -> models().cubeBottomTop(id.getPath(),
						texture(id.withSuffix("_side")),
						texture(id.withSuffix("_bottom")),
						texture(id.withSuffix("_top"))));
		
		simpleHorizontalWithItem(MSBlocks.AND_GATE_BLOCK,
				id -> models().getExistingFile(id));
		simpleHorizontalWithItem(MSBlocks.OR_GATE_BLOCK,
				id -> models().getExistingFile(id));
		simpleHorizontalWithItem(MSBlocks.XOR_GATE_BLOCK,
				id -> models().getExistingFile(id));
		simpleHorizontalWithItem(MSBlocks.NAND_GATE_BLOCK,
				id -> models().getExistingFile(id));
		simpleHorizontalWithItem(MSBlocks.NOR_GATE_BLOCK,
				id -> models().getExistingFile(id));
		simpleHorizontalWithItem(MSBlocks.XNOR_GATE_BLOCK,
				id -> models().getExistingFile(id));
		
		//Core Functional Land Blocks
		{
			ModelFile model = empty("gate", texture("node_spiro_inner"));
			simpleBlock(MSBlocks.GATE.get(), model);
			simpleBlock(MSBlocks.GATE_MAIN.get(), model);
			simpleBlock(MSBlocks.RETURN_NODE.get(), model);
			simpleBlock(MSBlocks.RETURN_NODE_MAIN.get(), model);
		}
		
		//Sburb Machines
		simpleBlockWithItem(MSBlocks.CRUXTRUDER_LID, this::existing);
		simpleHorizontal(MSBlocks.CRUXTRUDER.CORNER,
				id -> existing(id("cruxtruder_base_corner")));
		simpleHorizontal(MSBlocks.CRUXTRUDER.SIDE,
				id -> existing(id("cruxtruder_base_side")));
		simpleHorizontal(MSBlocks.CRUXTRUDER.CENTER, this::existing);
		simpleHorizontal(MSBlocks.CRUXTRUDER.TOP_CORNER, this::existing);
		simpleHorizontal(MSBlocks.CRUXTRUDER.TOP_SIDE, this::existing);
		simpleHorizontal(MSBlocks.CRUXTRUDER.TOP_CENTER, this::existing);
		simpleHorizontal(MSBlocks.CRUXTRUDER.TUBE, this::existing);
		flatItem(MSItems.CRUXTRUDER, MSBlockStateProvider::itemTexture);
		simpleHorizontal(MSBlocks.TOTEM_LATHE.BOTTOM_LEFT, this::existing);
		simpleHorizontal(MSBlocks.TOTEM_LATHE.BOTTOM_RIGHT, this::existing);
		simpleHorizontal(MSBlocks.TOTEM_LATHE.MIDDLE, this::existing);
		simpleHorizontal(MSBlocks.TOTEM_LATHE.WHEEL, this::existing);
		simpleBlock(MSBlocks.TOTEM_LATHE.DOWEL_ROD.get(), empty("totem_lathe_rod", texture("machine/particle")));
		simpleHorizontal(MSBlocks.TOTEM_LATHE.TOP, this::existing);
		simpleHorizontal(MSBlocks.TOTEM_LATHE.TOP_CORNER, this::existing);
		flatItem(MSItems.TOTEM_LATHE, MSBlockStateProvider::itemTexture);
		simpleHorizontal(MSBlocks.ALCHEMITER.CENTER, this::existing);
		simpleHorizontal(MSBlocks.ALCHEMITER.CORNER, this::existing);
		simpleHorizontal(MSBlocks.ALCHEMITER.LEFT_SIDE, this::existing);
		simpleHorizontal(MSBlocks.ALCHEMITER.RIGHT_SIDE, this::existing);
		simpleHorizontal(MSBlocks.ALCHEMITER.TOTEM_CORNER, this::existing);
		flatItem(MSItems.ALCHEMITER, MSBlockStateProvider::itemTexture);
		simpleHorizontal(MSBlocks.PUNCH_DESIGNIX.LEFT_LEG, this::existing);
		simpleHorizontal(MSBlocks.PUNCH_DESIGNIX.RIGHT_LEG, this::existing);
		simpleHorizontal(MSBlocks.PUNCH_DESIGNIX.KEYBOARD, this::existing);
		flatItem(MSItems.PUNCH_DESIGNIX, MSBlockStateProvider::itemTexture);
		
		simpleHorizontalWithItem(MSBlocks.MINI_CRUXTRUDER, this::existing);
		simpleHorizontalWithItem(MSBlocks.MINI_TOTEM_LATHE, 0, this::existing);
		simpleHorizontalWithItem(MSBlocks.MINI_ALCHEMITER, 0, this::existing);
		simpleHorizontalWithItem(MSBlocks.MINI_PUNCH_DESIGNIX, 0, this::existing);
		
		//Misc Machines
		simpleHorizontal(MSBlocks.TRANSPORTALIZER, 0, this::existing);
		flatItem(MSItems.TRANSPORTALIZER, MSBlockStateProvider::itemTexture);
		simpleHorizontal(MSBlocks.TRANS_PORTALIZER, 0, this::existing);
		flatItem(MSItems.TRANS_PORTALIZER, MSBlockStateProvider::itemTexture);
		simpleHorizontalWithItem(MSBlocks.SENDIFICATOR, 0, this::existing);
		simpleHorizontalWithItem(MSBlocks.URANIUM_COOKER, this::existing);
		simpleHorizontalWithItem(MSBlocks.GRIST_COLLECTOR, this::existing);
		simpleHorizontalWithItem(MSBlocks.ANTHVIL, this::existing);
		simpleBlockWithItem(MSBlocks.SKAIANET_DENIER,
				id -> models().cubeColumn(id.getPath(),
						texture(id.withSuffix("_side")),
						texture(id.withSuffix("_top"))));
		simpleHorizontalWithItem(MSBlocks.POWER_HUB, this::existing);
		
		//Misc Core Objects
		getVariantBuilder(MSBlocks.CRUXITE_DOWEL.get())
				.partialState().with(CruxiteDowelBlock.DOWEL_TYPE, CruxiteDowelBlock.Type.DOWEL)
				.modelForState().modelFile(models().getExistingFile(id("cruxite_dowel"))).addModel()
				.partialState().with(CruxiteDowelBlock.DOWEL_TYPE, CruxiteDowelBlock.Type.TOTEM)
				.modelForState().modelFile(models().getExistingFile(id("cruxite_totem"))).addModel();
		simpleBlock(MSBlocks.EMERGING_CRUXITE_DOWEL.get(),
				models().getExistingFile(id("cruxtruder_dowel")));
		simpleHorizontal(MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.CORNER, this::existing);
		
		//Misc Alchemy Semi-Plants
		simpleBlock(MSBlocks.GOLD_SEEDS,
				id -> models().crop(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.GOLD_SEEDS, MSBlockStateProvider::itemTexture);
		simpleBlockWithItem(MSBlocks.WOODEN_CACTUS,
				id -> models().getExistingFile(id));
		
		cake(MSBlocks.APPLE_CAKE);
		flatItem(MSItems.APPLE_CAKE, MSBlockStateProvider::itemTexture);
		cake(MSBlocks.BLUE_CAKE);
		flatItem(MSItems.BLUE_CAKE, MSBlockStateProvider::itemTexture);
		cake(MSBlocks.COLD_CAKE);
		flatItem(MSItems.COLD_CAKE, MSBlockStateProvider::itemTexture);
		cake(MSBlocks.RED_CAKE);
		flatItem(MSItems.RED_CAKE, MSBlockStateProvider::itemTexture);
		cake(MSBlocks.HOT_CAKE);
		flatItem(MSItems.HOT_CAKE, MSBlockStateProvider::itemTexture);
		cake(MSBlocks.REVERSE_CAKE);
		flatItem(MSItems.REVERSE_CAKE, MSBlockStateProvider::itemTexture);
		cake(MSBlocks.FUCHSIA_CAKE);
		flatItem(MSItems.FUCHSIA_CAKE, MSBlockStateProvider::itemTexture);
		cake(MSBlocks.NEGATIVE_CAKE);
		flatItem(MSItems.NEGATIVE_CAKE, MSBlockStateProvider::itemTexture);
		cake(MSBlocks.CARROT_CAKE);
		flatItem(MSItems.CARROT_CAKE, MSBlockStateProvider::itemTexture);
		simpleBlockWithItem(MSBlocks.LARGE_CAKE);
		
		//Explosives
		{
			ModelFile model = models().getExistingFile(new ResourceLocation("tnt"));
			ModelFile itemModel = itemModels().getExistingFile(new ResourceLocation("tnt"));
			for(RegistryObject<Block> block : Arrays.asList(MSBlocks.PRIMED_TNT, MSBlocks.UNSTABLE_TNT, MSBlocks.INSTANT_TNT))
			{
				getVariantBuilder(block.get()).partialState().setModels(ConfiguredModel.allYRotations(model, 0, false));
				simpleBlockItem(block.get(), itemModel);
			}
		}
		simpleBlockItem(MSBlocks.WOODEN_EXPLOSIVE_BUTTON.get(),
				models().getExistingFile(id("wooden_explosive_button_inventory")));
		simpleBlockItem(MSBlocks.STONE_EXPLOSIVE_BUTTON.get(),
				models().getExistingFile(id("stone_explosive_button_inventory")));
		
		simpleHorizontal(MSBlocks.BLENDER, this::existing);
		flatItem(MSItems.BLENDER, MSBlockStateProvider::itemTexture);
		simpleHorizontal(MSBlocks.CHESSBOARD, this::existing);
		flatItem(MSItems.CHESSBOARD, MSBlockStateProvider::itemTexture);
		simpleHorizontal(MSBlocks.MINI_FROG_STATUE, 0, this::existing);
		flatItem(MSItems.MINI_FROG_STATUE, MSBlockStateProvider::itemTexture);
		flatItem(MSItems.MINI_WIZARD_STATUE, MSBlockStateProvider::itemTexture);
		simpleHorizontal(MSBlocks.MINI_WIZARD_STATUE, this::existing);
		simpleHorizontalWithItem(MSBlocks.MINI_TYPHEUS_STATUE, this::existing);
		{
			ModelFile model = empty("horse_clock", vanillaTexture("dark_oak_planks"));
			simpleHorizontal(MSBlocks.HORSE_CLOCK.BOTTOM, $ -> model);
			simpleHorizontal(MSBlocks.HORSE_CLOCK.CENTER, $ -> model);
			simpleHorizontal(MSBlocks.HORSE_CLOCK.TOP, $ -> model);
		}
		simpleHorizontal(MSBlocks.MIRROR, this::existing);
		flatItem(MSItems.MIRROR, MSBlockStateProvider::itemTexture);
		
		fluid(MSBlocks.OIL);
		fluid(MSBlocks.BLOOD);
		fluid(MSBlocks.BRAIN_JUICE);
		fluid(MSBlocks.WATER_COLORS);
		fluid(MSBlocks.ENDER);
		fluid(MSBlocks.LIGHT_WATER);
		
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
	
	private ModelFile cubeAll(ResourceLocation id)
	{
		return models().cubeAll(id.getPath(), texture(id));
	}
	
	private ModelFile empty(String path, ResourceLocation particleTexture)
	{
		return models().getBuilder(path).texture("particle", particleTexture);
	}
	
	private ModelFile fluidModel(ResourceLocation id)
	{
		return empty(id.getPath(), texture(id.withPrefix("still_")));
	}
	
	private void fluid(RegistryObject<LiquidBlock> block)
	{
		simpleBlock(block, this::fluidModel);
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
	
	private void simpleHorizontal(RegistryObject<? extends Block> block, Function<ResourceLocation, ModelFile> modelProvider)
	{
		simpleHorizontal(block, 180, modelProvider);
	}
	
	private void simpleHorizontal(RegistryObject<? extends Block> block, int angleOffset, Function<ResourceLocation, ModelFile> modelProvider)
	{
		var model = modelProvider.apply(block.getId());
		horizontal(block, angleOffset, $ -> model, BlockStateProperties.WATERLOGGED, MSProperties.MACHINE_TOGGLE, BlockStateProperties.POWERED, BlockStateProperties.POWER);
	}
	
	private void horizontal(RegistryObject<? extends Block> block, Function<BlockState, ModelFile> modelProvider, Property<?>... ignored)
	{
		horizontal(block, 180, modelProvider, ignored);
	}
	
	private void horizontal(RegistryObject<? extends Block> block, int angleOffset, Function<BlockState, ModelFile> modelProvider, Property<?>... ignored)
	{
		getVariantBuilder(block.get())
				.forAllStatesExcept(state -> ConfiguredModel.builder()
								.modelFile(modelProvider.apply(state))
								.rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + angleOffset) % 360)
								.build(),
						ignored
				);
	}
	
	private void simpleHorizontalWithItem(RegistryObject<Block> block, Function<ResourceLocation, ModelFile> modelProvider)
	{
		var model = modelProvider.apply(block.getId());
		simpleHorizontal(block, $ -> model);
		simpleBlockItem(block.get(), model);
	}
	
	@SuppressWarnings("SameParameterValue")
	private void simpleHorizontalWithItem(RegistryObject<Block> block, int angleOffset, Function<ResourceLocation, ModelFile> modelProvider)
	{
		var model = modelProvider.apply(block.getId());
		simpleHorizontal(block, angleOffset, $ -> model);
		simpleBlockItem(block.get(), model);
	}
	
	private void directionalWithItem(RegistryObject<? extends Block> block, Function<ResourceLocation, ModelFile> modelProvider)
	{
		var model = modelProvider.apply(block.getId());
		directional(block, $ -> model, MSProperties.MACHINE_TOGGLE, BlockStateProperties.POWERED);
		simpleBlockItem(block.get(), model);
	}
	
	private void directional(RegistryObject<? extends Block> block, Function<BlockState, ModelFile> modelProvider, Property<?>... ignored)
	{
		getVariantBuilder(block.get())
				.forAllStatesExcept(state -> {
					Direction dir = state.getValue(BlockStateProperties.FACING);
					return ConfiguredModel.builder()
							.modelFile(modelProvider.apply(state))
							.rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
							.rotationY(dir.getAxis().isVertical() ? 0 : ((int) dir.toYRot() + 180) % 360)
							.build();
				}, ignored);
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
	
	private void slabWithItem(RegistryObject<SlabBlock> block, RegistryObject<Block> sourceBlock)
	{
		slabWithItem(block, sourceBlock.getId());
	}
	
	private void slabWithItem(RegistryObject<SlabBlock> block, ResourceLocation sourceBlock)
	{
		ResourceLocation texture = texture(sourceBlock);
		slabWithItem(block, sourceBlock, texture, texture);
	}
	
	private void slabWithItem(RegistryObject<SlabBlock> block, ResourceLocation sourceBlock, ResourceLocation side, ResourceLocation topBottom)
	{
		ModelFile slab = models().slab(block.getId().getPath(), side, topBottom, topBottom);
		slabBlock(block.get(), slab,
				models().slabTop(block.getId().getPath() + "_top", side, topBottom, topBottom),
				models().getExistingFile(sourceBlock));
		simpleBlockItem(block.get(), slab);
	}
	
	private void powerVariableWithItem(RegistryObject<Block> block, ModelFile highPowerModel, ModelFile mediumPowerModel, ModelFile lowPowerModel, ModelFile unpoweredModel)
	{
		getVariantBuilder(block.get())
				.forAllStates(state -> {
					int power = state.getValue(BlockStateProperties.POWER);
					ModelFile model;
					if(power > 10)
						model = highPowerModel;
					else if(power > 5)
						model = mediumPowerModel;
					else if(power > 0)
						model = lowPowerModel;
					else
						model = unpoweredModel;
					return ConfiguredModel.builder().modelFile(model).build();
				});
		simpleBlockItem(block.get(), unpoweredModel);
	}
	
	private ModelFile cakeModel(ResourceLocation id, int bites)
	{
		if(bites == 0)
		{
			return models().getBuilder(id.getPath() + "_uneaten")
					.texture("particle", texture(id.withSuffix("_side")))
					.texture("bottom", texture(id.withSuffix("_bottom")))
					.texture("top", texture(id.withSuffix("_top")))
					.texture("side", texture(id.withSuffix("_side")))
					.element()
					.from(1, 0, 1).to(15, 8, 15)
					.allFaces((direction, faceBuilder) -> {
						if(direction == Direction.UP)
							faceBuilder.texture("#top");
						else if(direction == Direction.DOWN)
							faceBuilder.texture("#bottom").cullface(Direction.DOWN);
						else
							faceBuilder.texture("#side");
					})
					.end();
		} else
		{
			return models().getBuilder(id.getPath() + "_slice" + bites)
					.texture("particle", texture(id.withSuffix("_side")))
					.texture("bottom", texture(id.withSuffix("_bottom")))
					.texture("top", texture(id.withSuffix("_top")))
					.texture("side", texture(id.withSuffix("_side")))
					.texture("inside", texture(id.withSuffix("_inner")))
					.element()
					.from(1 + 2*bites, 0, 1).to(15, 8, 15)
					.allFaces((direction, faceBuilder) -> {
						if(direction == Direction.UP)
							faceBuilder.texture("#top");
						else if(direction == Direction.DOWN)
							faceBuilder.texture("#bottom").cullface(Direction.DOWN);
						else if(direction == Direction.WEST)
							faceBuilder.texture("#inside");
						else
							faceBuilder.texture("#side");
					})
					.end();
		}
	}
	
	private void cake(RegistryObject<Block> block)
	{
		ResourceLocation id = block.getId();
		getVariantBuilder(block.get()).forAllStates(state -> {
			int bites = state.getValue(CakeBlock.BITES);
			return ConfiguredModel.builder().modelFile(cakeModel(id, bites)).build();
		});
	}
	
	private void flatItem(RegistryObject<? extends BlockItem> item, Function<ResourceLocation, ResourceLocation> textureProvider)
	{
		itemModels().withExistingParent(item.getId().getPath(),
				new ResourceLocation("item/generated")).texture("layer0",
				textureProvider.apply(item.getId()));
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
	
	public static ResourceLocation id(String path)
	{
		return new ResourceLocation(Minestuck.MOD_ID, path);
	}
}
