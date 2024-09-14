package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.*;
import com.mraof.minestuck.block.machine.*;
import com.mraof.minestuck.block.redstone.*;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

public class MSBlockStateProvider extends BlockStateProvider
{
	public MSBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper)
	{
		super(output, Minestuck.MOD_ID, exFileHelper);
	}
	
	@Override
	protected void registerStatesAndModels()
	{
		SkaiaBlocksData.addModels(this);
		AspectTreeBlocksData.addModels(this);
		
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
		simpleBlockWithItem(MSBlocks.UNCARVED_WOOD_CRUXITE_ORE);
		simpleBlockWithItem(MSBlocks.BLACK_STONE_CRUXITE_ORE);
		
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
		simpleBlockWithItem(MSBlocks.UNCARVED_WOOD_URANIUM_ORE);
		simpleBlockWithItem(MSBlocks.BLACK_STONE_URANIUM_ORE);
		
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
		simpleBlockWithItem(MSBlocks.UNCARVED_WOOD_IRON_ORE);
		
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
		simpleBlockWithItem(MSBlocks.BLACK_STONE_GOLD_ORE);
		
		simpleBlockWithItem(MSBlocks.END_STONE_REDSTONE_ORE);
		simpleBlockWithItem(MSBlocks.UNCARVED_WOOD_REDSTONE_ORE);
		simpleBlockWithItem(MSBlocks.BLACK_STONE_REDSTONE_ORE);
		
		simpleBlockWithItem(MSBlocks.STONE_QUARTZ_ORE);
		simpleBlockWithItem(MSBlocks.BLACK_STONE_QUARTZ_ORE);
		
		simpleBlockWithItem(MSBlocks.PINK_STONE_LAPIS_ORE);
		
		simpleBlockWithItem(MSBlocks.PINK_STONE_DIAMOND_ORE);
		
		simpleBlockWithItem(MSBlocks.UNCARVED_WOOD_EMERALD_ORE);
		
		//Resource Blocks
		simpleBlockWithItem(MSBlocks.CRUXITE_BLOCK);
		stairsWithItem(MSBlocks.CRUXITE_STAIRS, MSBlocks.CRUXITE_BLOCK);
		slabWithItem(MSBlocks.CRUXITE_SLAB, MSBlocks.CRUXITE_BLOCK);
		wallWithItem(MSBlocks.CRUXITE_WALL, MSBlocks.CRUXITE_BLOCK);
		buttonWithItem(MSBlocks.CRUXITE_BUTTON, MSBlocks.CRUXITE_BLOCK);
		pressurePlateWithItem(MSBlocks.CRUXITE_PRESSURE_PLATE, MSBlocks.CRUXITE_BLOCK);
		
		simpleDoorBlock(MSBlocks.CRUXITE_DOOR);
		flatItem(MSItems.CRUXITE_DOOR, MSBlockStateProvider::itemTexture);
		trapDoorWithItem(MSBlocks.CRUXITE_TRAPDOOR);
		simpleBlockWithItem(MSBlocks.POLISHED_CRUXITE_BLOCK);
		stairsWithItem(MSBlocks.POLISHED_CRUXITE_STAIRS, MSBlocks.POLISHED_CRUXITE_BLOCK);
		slabWithItem(MSBlocks.POLISHED_CRUXITE_SLAB, MSBlocks.POLISHED_CRUXITE_BLOCK);
		wallWithItem(MSBlocks.POLISHED_CRUXITE_WALL, MSBlocks.POLISHED_CRUXITE_BLOCK);
		simpleBlockWithItem(MSBlocks.CRUXITE_BRICKS);
		stairsWithItem(MSBlocks.CRUXITE_BRICK_STAIRS, MSBlocks.CRUXITE_BRICKS);
		slabWithItem(MSBlocks.CRUXITE_BRICK_SLAB, MSBlocks.CRUXITE_BRICKS);
		wallWithItem(MSBlocks.CRUXITE_BRICK_WALL, MSBlocks.CRUXITE_BRICKS);
		simpleBlockWithItem(MSBlocks.SMOOTH_CRUXITE_BLOCK);
		simpleBlockWithItem(MSBlocks.CHISELED_CRUXITE_BLOCK);
		unflippedColumnWithItem(MSBlocks.CRUXITE_PILLAR,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		customLampWithItem(MSBlocks.CRUXITE_LAMP);
		
		
		simpleBlockWithItem(MSBlocks.URANIUM_BLOCK);
		stairsWithItem(MSBlocks.URANIUM_STAIRS, MSBlocks.URANIUM_BLOCK);
		slabWithItem(MSBlocks.URANIUM_SLAB, MSBlocks.URANIUM_BLOCK);
		wallWithItem(MSBlocks.URANIUM_WALL, MSBlocks.URANIUM_BLOCK);
		buttonWithItem(MSBlocks.URANIUM_BUTTON, MSBlocks.URANIUM_BLOCK);
		pressurePlateWithItem(MSBlocks.URANIUM_PRESSURE_PLATE, MSBlocks.URANIUM_BLOCK);
		
		simpleBlockWithItem(MSBlocks.GENERIC_OBJECT);
		
		//Land Environment
		weightedVariantsWithItem(MSBlocks.BLUE_DIRT, new int[]{12, 6, 1, 1},
				i -> models().cubeAll("blue_dirt" + i, texture("blue_dirt/" + i)));
		simpleBlockWithItem(MSBlocks.THOUGHT_DIRT);
		
		simpleBlockWithItem(MSBlocks.COARSE_STONE);
		wallWithItem(MSBlocks.COARSE_STONE_WALL, MSBlocks.COARSE_STONE);
		buttonWithItem(MSBlocks.COARSE_STONE_BUTTON, MSBlocks.COARSE_STONE);
		pressurePlateWithItem(MSBlocks.COARSE_STONE_PRESSURE_PLATE, MSBlocks.COARSE_STONE);
		
		simpleBlockWithItem(MSBlocks.CHISELED_COARSE_STONE);
		
		simpleBlockWithItem(MSBlocks.COARSE_STONE_BRICKS);
		wallWithItem(MSBlocks.COARSE_STONE_BRICK_WALL, MSBlocks.COARSE_STONE_BRICKS);
		
		unflippedColumnWithItem(MSBlocks.COARSE_STONE_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		simpleBlockWithItem(MSBlocks.CHISELED_COARSE_STONE_BRICKS);
		simpleBlockWithItem(MSBlocks.CRACKED_COARSE_STONE_BRICKS);
		simpleBlockWithItem(MSBlocks.MOSSY_COARSE_STONE_BRICKS);
		
		simpleBlockWithItem(MSBlocks.SHADE_STONE);
		wallWithItem(MSBlocks.SHADE_WALL, MSBlocks.SHADE_STONE);
		buttonWithItem(MSBlocks.SHADE_BUTTON, MSBlocks.SHADE_STONE);
		pressurePlateWithItem(MSBlocks.SHADE_PRESSURE_PLATE, MSBlocks.SHADE_STONE);
		
		simpleBlockWithItem(MSBlocks.SMOOTH_SHADE_STONE);
		stairsWithItem(MSBlocks.SMOOTH_SHADE_STONE_STAIRS, MSBlocks.SMOOTH_SHADE_STONE);
		slabWithItem(MSBlocks.SMOOTH_SHADE_STONE_SLAB, MSBlocks.SMOOTH_SHADE_STONE);
		wallWithItem(MSBlocks.SMOOTH_SHADE_STONE_WALL, MSBlocks.SMOOTH_SHADE_STONE);
		
		simpleBlockWithItem(MSBlocks.SHADE_BRICKS);
		wallWithItem(MSBlocks.SHADE_BRICK_WALL, MSBlocks.SHADE_BRICKS);
		
		unflippedColumnWithItem(MSBlocks.SHADE_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture("shade_stone_column"),
						texture("shade_stone_column_top")));
		simpleBlockWithItem(MSBlocks.CHISELED_SHADE_BRICKS);
		simpleBlockWithItem(MSBlocks.CRACKED_SHADE_BRICKS);
		
		simpleBlockWithItem(MSBlocks.MOSSY_SHADE_BRICKS);
		stairsWithItem(MSBlocks.MOSSY_SHADE_BRICK_STAIRS, MSBlocks.MOSSY_SHADE_BRICKS);
		slabWithItem(MSBlocks.MOSSY_SHADE_BRICK_SLAB, MSBlocks.MOSSY_SHADE_BRICKS);
		wallWithItem(MSBlocks.MOSSY_SHADE_BRICK_WALL, MSBlocks.MOSSY_SHADE_BRICKS);
		
		simpleBlockWithItem(MSBlocks.BLOOD_SHADE_BRICKS);
		stairsWithItem(MSBlocks.BLOOD_SHADE_BRICK_STAIRS, MSBlocks.BLOOD_SHADE_BRICKS);
		slabWithItem(MSBlocks.BLOOD_SHADE_BRICK_SLAB, MSBlocks.BLOOD_SHADE_BRICKS);
		wallWithItem(MSBlocks.BLOOD_SHADE_BRICK_WALL, MSBlocks.BLOOD_SHADE_BRICKS);
		
		simpleBlockWithItem(MSBlocks.TAR_SHADE_BRICKS);
		stairsWithItem(MSBlocks.TAR_SHADE_BRICK_STAIRS, MSBlocks.TAR_SHADE_BRICKS);
		slabWithItem(MSBlocks.TAR_SHADE_BRICK_SLAB, MSBlocks.TAR_SHADE_BRICKS);
		wallWithItem(MSBlocks.TAR_SHADE_BRICK_WALL, MSBlocks.TAR_SHADE_BRICKS);
		
		simpleBlockWithItem(MSBlocks.FROST_TILE);
		wallWithItem(MSBlocks.FROST_TILE_WALL, MSBlocks.FROST_TILE);
		
		simpleBlockWithItem(MSBlocks.CHISELED_FROST_TILE);
		
		simpleBlockWithItem(MSBlocks.FROST_BRICKS);
		wallWithItem(MSBlocks.FROST_BRICK_WALL, MSBlocks.FROST_BRICKS);
		
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
		stairsWithItem(MSBlocks.FLOWERY_FROST_BRICK_STAIRS, MSBlocks.FLOWERY_FROST_BRICKS);
		slabWithItem(MSBlocks.FLOWERY_FROST_BRICK_SLAB, MSBlocks.FLOWERY_FROST_BRICKS);
		wallWithItem(MSBlocks.FLOWERY_FROST_BRICK_WALL, MSBlocks.FLOWERY_FROST_BRICKS);
		
		simpleBlockWithItem(MSBlocks.CAST_IRON);
		stairsWithItem(MSBlocks.CAST_IRON_STAIRS, MSBlocks.CAST_IRON);
		slabWithItem(MSBlocks.CAST_IRON_SLAB, MSBlocks.CAST_IRON);
		wallWithItem(MSBlocks.CAST_IRON_WALL, MSBlocks.CAST_IRON);
		buttonWithItem(MSBlocks.CAST_IRON_BUTTON, MSBlocks.CAST_IRON);
		pressurePlateWithItem(MSBlocks.CAST_IRON_PRESSURE_PLATE, MSBlocks.CAST_IRON);
		
		simpleBlockWithItem(MSBlocks.CAST_IRON_TILE);
		stairsWithItem(MSBlocks.CAST_IRON_TILE_STAIRS, MSBlocks.CAST_IRON_TILE);
		slabWithItem(MSBlocks.CAST_IRON_TILE_SLAB, MSBlocks.CAST_IRON_TILE);
		
		simpleBlockWithItem(MSBlocks.CAST_IRON_SHEET);
		stairsWithItem(MSBlocks.CAST_IRON_SHEET_STAIRS, MSBlocks.CAST_IRON_SHEET);
		slabWithItem(MSBlocks.CAST_IRON_SHEET_SLAB, MSBlocks.CAST_IRON_SHEET);
		
		simpleBlockWithItem(MSBlocks.CHISELED_CAST_IRON);
		unflippedColumnWithItem(MSBlocks.CAST_IRON_FRAME,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture(id.withSuffix("_top")))
						.renderType("cutout"));
		
		unflippedColumnWithItem(MSBlocks.STEEL_BEAM,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id.withSuffix("_side")),
						texture(id.withSuffix("_top"))));
		
		simpleBlockWithItem(MSBlocks.NATIVE_SULFUR);
		
		simpleBlockWithItem(MSBlocks.MYCELIUM_STONE);
		wallWithItem(MSBlocks.MYCELIUM_STONE_WALL, MSBlocks.MYCELIUM_STONE);
		buttonWithItem(MSBlocks.MYCELIUM_STONE_BUTTON, MSBlocks.MYCELIUM_STONE);
		pressurePlateWithItem(MSBlocks.MYCELIUM_STONE_PRESSURE_PLATE, MSBlocks.MYCELIUM_STONE);
		
		simpleBlockWithItem(MSBlocks.MYCELIUM_COBBLESTONE);
		stairsWithItem(MSBlocks.MYCELIUM_COBBLESTONE_STAIRS, MSBlocks.MYCELIUM_COBBLESTONE);
		slabWithItem(MSBlocks.MYCELIUM_COBBLESTONE_SLAB, MSBlocks.MYCELIUM_COBBLESTONE);
		wallWithItem(MSBlocks.MYCELIUM_COBBLESTONE_WALL, MSBlocks.MYCELIUM_COBBLESTONE);
		
		simpleBlockWithItem(MSBlocks.POLISHED_MYCELIUM_STONE);
		stairsWithItem(MSBlocks.POLISHED_MYCELIUM_STONE_STAIRS, MSBlocks.POLISHED_MYCELIUM_STONE);
		slabWithItem(MSBlocks.POLISHED_MYCELIUM_STONE_SLAB, MSBlocks.POLISHED_MYCELIUM_STONE);
		wallWithItem(MSBlocks.POLISHED_MYCELIUM_STONE_WALL, MSBlocks.POLISHED_MYCELIUM_STONE);
		
		simpleBlockWithItem(MSBlocks.MYCELIUM_BRICKS);
		wallWithItem(MSBlocks.MYCELIUM_BRICK_WALL, MSBlocks.MYCELIUM_BRICKS);
		
		unflippedColumnWithItem(MSBlocks.MYCELIUM_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		simpleBlockWithItem(MSBlocks.CHISELED_MYCELIUM_BRICKS);
		simpleBlockWithItem(MSBlocks.SUSPICIOUS_CHISELED_MYCELIUM_BRICKS);
		simpleBlockWithItem(MSBlocks.CRACKED_MYCELIUM_BRICKS);
		
		simpleBlockWithItem(MSBlocks.MOSSY_MYCELIUM_BRICKS);
		stairsWithItem(MSBlocks.MOSSY_MYCELIUM_BRICK_STAIRS, MSBlocks.MOSSY_MYCELIUM_BRICKS);
		slabWithItem(MSBlocks.MOSSY_MYCELIUM_BRICK_SLAB, MSBlocks.MOSSY_MYCELIUM_BRICKS);
		wallWithItem(MSBlocks.MOSSY_MYCELIUM_BRICK_WALL, MSBlocks.MOSSY_MYCELIUM_BRICKS);
		
		simpleBlockWithItem(MSBlocks.FLOWERY_MYCELIUM_BRICKS);
		stairsWithItem(MSBlocks.FLOWERY_MYCELIUM_BRICK_STAIRS, MSBlocks.FLOWERY_MYCELIUM_BRICKS);
		slabWithItem(MSBlocks.FLOWERY_MYCELIUM_BRICK_SLAB, MSBlocks.FLOWERY_MYCELIUM_BRICKS);
		wallWithItem(MSBlocks.FLOWERY_MYCELIUM_BRICK_WALL, MSBlocks.FLOWERY_MYCELIUM_BRICKS);
		
		simpleBlockWithItem(MSBlocks.BLACK_SAND);
		
		simpleBlockWithItem(MSBlocks.BLACK_STONE);
		stairsWithItem(MSBlocks.BLACK_STONE_STAIRS, MSBlocks.BLACK_STONE);
		slabWithItem(MSBlocks.BLACK_STONE_SLAB, MSBlocks.BLACK_STONE);
		wallWithItem(MSBlocks.BLACK_STONE_WALL, MSBlocks.BLACK_STONE);
		buttonWithItem(MSBlocks.BLACK_STONE_BUTTON, MSBlocks.BLACK_STONE);
		pressurePlateWithItem(MSBlocks.BLACK_STONE_PRESSURE_PLATE, MSBlocks.BLACK_STONE);
		
		simpleBlockWithItem(MSBlocks.BLACK_COBBLESTONE);
		stairsWithItem(MSBlocks.BLACK_COBBLESTONE_STAIRS, MSBlocks.BLACK_COBBLESTONE);
		slabWithItem(MSBlocks.BLACK_COBBLESTONE_SLAB, MSBlocks.BLACK_COBBLESTONE);
		wallWithItem(MSBlocks.BLACK_COBBLESTONE_WALL, MSBlocks.BLACK_COBBLESTONE);
		
		simpleBlockWithItem(MSBlocks.POLISHED_BLACK_STONE);
		stairsWithItem(MSBlocks.POLISHED_BLACK_STONE_STAIRS, MSBlocks.POLISHED_BLACK_STONE);
		slabWithItem(MSBlocks.POLISHED_BLACK_STONE_SLAB, MSBlocks.POLISHED_BLACK_STONE);
		wallWithItem(MSBlocks.POLISHED_BLACK_STONE_WALL, MSBlocks.POLISHED_BLACK_STONE);
		
		simpleBlockWithItem(MSBlocks.BLACK_STONE_BRICKS);
		stairsWithItem(MSBlocks.BLACK_STONE_BRICK_STAIRS, MSBlocks.BLACK_STONE_BRICKS);
		slabWithItem(MSBlocks.BLACK_STONE_BRICK_SLAB, MSBlocks.BLACK_STONE_BRICKS);
		wallWithItem(MSBlocks.BLACK_STONE_BRICK_WALL, MSBlocks.BLACK_STONE_BRICKS);
		
		unflippedColumnWithItem(MSBlocks.BLACK_STONE_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		simpleBlockWithItem(MSBlocks.CHISELED_BLACK_STONE_BRICKS);
		simpleBlockWithItem(MSBlocks.CRACKED_BLACK_STONE_BRICKS);
		
		simpleBlockWithItem(MSBlocks.MAGMATIC_BLACK_STONE_BRICKS);
		stairsWithItem(MSBlocks.MAGMATIC_BLACK_STONE_BRICK_STAIRS, MSBlocks.MAGMATIC_BLACK_STONE_BRICKS);
		slabWithItem(MSBlocks.MAGMATIC_BLACK_STONE_BRICK_SLAB, MSBlocks.MAGMATIC_BLACK_STONE_BRICKS);
		wallWithItem(MSBlocks.MAGMATIC_BLACK_STONE_BRICK_WALL, MSBlocks.MAGMATIC_BLACK_STONE_BRICKS);
		
		simpleBlockWithItem(MSBlocks.IGNEOUS_STONE);
		stairsWithItem(MSBlocks.IGNEOUS_STONE_STAIRS, MSBlocks.IGNEOUS_STONE);
		slabWithItem(MSBlocks.IGNEOUS_STONE_SLAB, MSBlocks.IGNEOUS_STONE);
		wallWithItem(MSBlocks.IGNEOUS_STONE_WALL, MSBlocks.IGNEOUS_STONE);
		buttonWithItem(MSBlocks.IGNEOUS_STONE_BUTTON, MSBlocks.IGNEOUS_STONE);
		pressurePlateWithItem(MSBlocks.IGNEOUS_STONE_PRESSURE_PLATE, MSBlocks.IGNEOUS_STONE);
		
		simpleBlockWithItem(MSBlocks.POLISHED_IGNEOUS_STONE);
		stairsWithItem(MSBlocks.POLISHED_IGNEOUS_STAIRS, MSBlocks.POLISHED_IGNEOUS_STONE);
		slabWithItem(MSBlocks.POLISHED_IGNEOUS_SLAB, MSBlocks.POLISHED_IGNEOUS_STONE);
		wallWithItem(MSBlocks.POLISHED_IGNEOUS_WALL, MSBlocks.POLISHED_IGNEOUS_STONE);
		
		simpleBlockWithItem(MSBlocks.POLISHED_IGNEOUS_BRICKS);
		stairsWithItem(MSBlocks.POLISHED_IGNEOUS_BRICK_STAIRS, MSBlocks.POLISHED_IGNEOUS_BRICKS);
		slabWithItem(MSBlocks.POLISHED_IGNEOUS_BRICK_SLAB, MSBlocks.POLISHED_IGNEOUS_BRICKS);
		wallWithItem(MSBlocks.POLISHED_IGNEOUS_BRICK_WALL, MSBlocks.POLISHED_IGNEOUS_BRICKS);
		
		unflippedColumnWithItem(MSBlocks.POLISHED_IGNEOUS_PILLAR,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		simpleBlockWithItem(MSBlocks.CHISELED_IGNEOUS_STONE);
		simpleBlockWithItem(MSBlocks.CRACKED_POLISHED_IGNEOUS_BRICKS);
		
		simpleBlockWithItem(MSBlocks.MAGMATIC_POLISHED_IGNEOUS_BRICKS);
		stairsWithItem(MSBlocks.MAGMATIC_POLISHED_IGNEOUS_BRICK_STAIRS, MSBlocks.MAGMATIC_POLISHED_IGNEOUS_BRICKS);
		slabWithItem(MSBlocks.MAGMATIC_POLISHED_IGNEOUS_BRICK_SLAB, MSBlocks.MAGMATIC_POLISHED_IGNEOUS_BRICKS);
		wallWithItem(MSBlocks.MAGMATIC_POLISHED_IGNEOUS_BRICK_WALL, MSBlocks.MAGMATIC_POLISHED_IGNEOUS_BRICKS);
		
		simpleBlockWithItem(MSBlocks.MAGMATIC_IGNEOUS_STONE);
		
		simpleBlockWithItem(MSBlocks.PUMICE_STONE);
		stairsWithItem(MSBlocks.PUMICE_STONE_STAIRS, MSBlocks.PUMICE_STONE);
		slabWithItem(MSBlocks.PUMICE_STONE_SLAB, MSBlocks.PUMICE_STONE);
		wallWithItem(MSBlocks.PUMICE_STONE_WALL, MSBlocks.PUMICE_STONE);
		buttonWithItem(MSBlocks.PUMICE_STONE_BUTTON, MSBlocks.PUMICE_STONE);
		pressurePlateWithItem(MSBlocks.PUMICE_STONE_PRESSURE_PLATE, MSBlocks.PUMICE_STONE);
		
		simpleBlockWithItem(MSBlocks.PUMICE_BRICKS);
		stairsWithItem(MSBlocks.PUMICE_BRICK_STAIRS, MSBlocks.PUMICE_BRICKS);
		slabWithItem(MSBlocks.PUMICE_BRICK_SLAB, MSBlocks.PUMICE_BRICKS);
		wallWithItem(MSBlocks.PUMICE_BRICK_WALL, MSBlocks.PUMICE_BRICKS);
		
		simpleBlockWithItem(MSBlocks.PUMICE_TILES);
		stairsWithItem(MSBlocks.PUMICE_TILE_STAIRS, MSBlocks.PUMICE_TILES);
		slabWithItem(MSBlocks.PUMICE_TILE_SLAB, MSBlocks.PUMICE_TILES);
		wallWithItem(MSBlocks.PUMICE_TILE_WALL, MSBlocks.PUMICE_TILES);
		
		simpleBlockWithItem(MSBlocks.HEAT_LAMP);
		
		simpleBlockWithItem(MSBlocks.FLOWERY_MOSSY_COBBLESTONE);
		stairsWithItem(MSBlocks.FLOWERY_MOSSY_COBBLESTONE_STAIRS, MSBlocks.FLOWERY_MOSSY_COBBLESTONE);
		slabWithItem(MSBlocks.FLOWERY_MOSSY_COBBLESTONE_SLAB, MSBlocks.FLOWERY_MOSSY_COBBLESTONE);
		wallWithItem(MSBlocks.FLOWERY_MOSSY_COBBLESTONE_WALL, MSBlocks.FLOWERY_MOSSY_COBBLESTONE);
		
		variantsWithItem(MSBlocks.FLOWERY_MOSSY_STONE_BRICKS, 4,
				i -> models().cubeAll("flowery_mossy_stone_bricks" + (i + 1),
						texture(id("flowery_mossy_stone_bricks/" + (i + 1)))));
		stairsWithItem(MSBlocks.FLOWERY_MOSSY_STONE_BRICK_STAIRS, "flowery_mossy_stone_brick", texture("flowery_mossy_stone_bricks/1"));
		slabWithItem(MSBlocks.FLOWERY_MOSSY_STONE_BRICK_SLAB, "flowery_mossy_stone_bricks1", texture("flowery_mossy_stone_bricks/1"));
		wallWithItem(MSBlocks.FLOWERY_MOSSY_STONE_BRICK_WALL, "flowery_mossy_stone_brick", texture("flowery_mossy_stone_bricks/1"));
		
		variantsWithItem(MSBlocks.DECREPIT_STONE_BRICKS, 4,
				i -> models().cubeAll("decrepit_stone_bricks" + (i + 1),
						texture(id("decrepit_stone_bricks/" + (i + 1)))));
		stairsWithItem(MSBlocks.DECREPIT_STONE_BRICK_STAIRS, "decrepit_stone_brick", texture("decrepit_stone_bricks/1"));
		slabWithItem(MSBlocks.DECREPIT_STONE_BRICK_SLAB, "decrepit_stone_bricks1", texture("decrepit_stone_bricks/1"));
		wallWithItem(MSBlocks.DECREPIT_STONE_BRICK_WALL, "decrepit_stone_brick", texture("decrepit_stone_bricks/1"));
		
		variantsWithItem(MSBlocks.MOSSY_DECREPIT_STONE_BRICKS, 4,
				i -> models().cubeAll("mossy_decrepit_stone_bricks" + (i + 1),
						texture(id("mossy_decrepit_stone_bricks/" + (i + 1)))));
		stairsWithItem(MSBlocks.MOSSY_DECREPIT_STONE_BRICK_STAIRS, "mossy_decrepit_stone_brick", texture("mossy_decrepit_stone_bricks/1"));
		slabWithItem(MSBlocks.MOSSY_DECREPIT_STONE_BRICK_SLAB, "mossy_decrepit_stone_bricks1", texture("mossy_decrepit_stone_bricks/1"));
		wallWithItem(MSBlocks.MOSSY_DECREPIT_STONE_BRICK_WALL, "mossy_decrepit_stone_brick", texture("mossy_decrepit_stone_bricks/1"));
		
		simpleBlockWithItem(MSBlocks.COARSE_END_STONE);
		{
			ModelFile model = models().cubeBottomTop("end_grass",
					texture("end_grass_side"),
					texture("end_stone"),
					texture("end_grass_top"));
			getVariantBuilder(MSBlocks.END_GRASS.get()).partialState().setModels(ConfiguredModel.allYRotations(model, 0, false));
			simpleBlockItem(MSBlocks.END_GRASS.get(), model);
		}
		
		simpleBlockWithItem(MSBlocks.CHALK);
		wallWithItem(MSBlocks.CHALK_WALL, MSBlocks.CHALK);
		buttonWithItem(MSBlocks.CHALK_BUTTON, MSBlocks.CHALK);
		pressurePlateWithItem(MSBlocks.CHALK_PRESSURE_PLATE, MSBlocks.CHALK);
		
		simpleBlockWithItem(MSBlocks.POLISHED_CHALK);
		stairsWithItem(MSBlocks.POLISHED_CHALK_STAIRS, MSBlocks.POLISHED_CHALK);
		slabWithItem(MSBlocks.POLISHED_CHALK_SLAB, MSBlocks.POLISHED_CHALK);
		wallWithItem(MSBlocks.POLISHED_CHALK_WALL, MSBlocks.POLISHED_CHALK);
		
		simpleBlockWithItem(MSBlocks.CHALK_BRICKS);
		wallWithItem(MSBlocks.CHALK_BRICK_WALL, MSBlocks.CHALK_BRICKS);
		
		unflippedColumnWithItem(MSBlocks.CHALK_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		simpleBlockWithItem(MSBlocks.CHISELED_CHALK_BRICKS);
		
		simpleBlockWithItem(MSBlocks.MOSSY_CHALK_BRICKS);
		stairsWithItem(MSBlocks.MOSSY_CHALK_BRICK_STAIRS, MSBlocks.MOSSY_CHALK_BRICKS);
		slabWithItem(MSBlocks.MOSSY_CHALK_BRICK_SLAB, MSBlocks.MOSSY_CHALK_BRICKS);
		wallWithItem(MSBlocks.MOSSY_CHALK_BRICK_WALL, MSBlocks.MOSSY_CHALK_BRICKS);
		
		simpleBlockWithItem(MSBlocks.FLOWERY_CHALK_BRICKS);
		stairsWithItem(MSBlocks.FLOWERY_CHALK_BRICK_STAIRS, MSBlocks.FLOWERY_CHALK_BRICKS);
		slabWithItem(MSBlocks.FLOWERY_CHALK_BRICK_SLAB, MSBlocks.FLOWERY_CHALK_BRICKS);
		wallWithItem(MSBlocks.FLOWERY_CHALK_BRICK_WALL, MSBlocks.FLOWERY_CHALK_BRICKS);
		
		simpleBlockWithItem(MSBlocks.PINK_STONE);
		wallWithItem(MSBlocks.PINK_STONE_WALL, MSBlocks.PINK_STONE);
		buttonWithItem(MSBlocks.PINK_STONE_BUTTON, MSBlocks.PINK_STONE);
		pressurePlateWithItem(MSBlocks.PINK_STONE_PRESSURE_PLATE, MSBlocks.PINK_STONE);
		
		simpleBlockWithItem(MSBlocks.POLISHED_PINK_STONE);
		stairsWithItem(MSBlocks.POLISHED_PINK_STONE_STAIRS, MSBlocks.POLISHED_PINK_STONE);
		slabWithItem(MSBlocks.POLISHED_PINK_STONE_SLAB, MSBlocks.POLISHED_PINK_STONE);
		wallWithItem(MSBlocks.POLISHED_PINK_STONE_WALL, MSBlocks.POLISHED_PINK_STONE);
		
		simpleBlockWithItem(MSBlocks.PINK_STONE_BRICKS);
		wallWithItem(MSBlocks.PINK_STONE_BRICK_WALL, MSBlocks.PINK_STONE_BRICKS);
		
		unflippedColumnWithItem(MSBlocks.PINK_STONE_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture(id.withSuffix("_end"))));
		simpleBlockWithItem(MSBlocks.CHISELED_PINK_STONE_BRICKS);
		simpleBlockWithItem(MSBlocks.CRACKED_PINK_STONE_BRICKS);
		
		simpleBlockWithItem(MSBlocks.MOSSY_PINK_STONE_BRICKS);
		stairsWithItem(MSBlocks.MOSSY_PINK_STONE_BRICK_STAIRS, MSBlocks.MOSSY_PINK_STONE_BRICKS);
		slabWithItem(MSBlocks.MOSSY_PINK_STONE_BRICK_SLAB, MSBlocks.MOSSY_PINK_STONE_BRICKS);
		wallWithItem(MSBlocks.MOSSY_PINK_STONE_BRICK_WALL, MSBlocks.MOSSY_PINK_STONE_BRICKS);
		
		simpleBlockWithItem(MSBlocks.BROWN_STONE);
		wallWithItem(MSBlocks.BROWN_STONE_WALL, MSBlocks.BROWN_STONE);
		buttonWithItem(MSBlocks.BROWN_STONE_BUTTON, MSBlocks.BROWN_STONE);
		pressurePlateWithItem(MSBlocks.BROWN_STONE_PRESSURE_PLATE, MSBlocks.BROWN_STONE);
		
		simpleBlockWithItem(MSBlocks.POLISHED_BROWN_STONE);
		stairsWithItem(MSBlocks.POLISHED_BROWN_STONE_STAIRS, MSBlocks.POLISHED_BROWN_STONE);
		slabWithItem(MSBlocks.POLISHED_BROWN_STONE_SLAB, MSBlocks.POLISHED_BROWN_STONE);
		wallWithItem(MSBlocks.POLISHED_BROWN_STONE_WALL, MSBlocks.POLISHED_BROWN_STONE);
		
		simpleBlockWithItem(MSBlocks.BROWN_STONE_BRICKS);
		wallWithItem(MSBlocks.BROWN_STONE_BRICK_WALL, MSBlocks.BROWN_STONE_BRICKS);
		
		simpleBlockWithItem(MSBlocks.CRACKED_BROWN_STONE_BRICKS);
		unflippedColumnWithItem(MSBlocks.BROWN_STONE_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture("polished_brown_stone")));
		
		simpleBlockWithItem(MSBlocks.GREEN_STONE);
		wallWithItem(MSBlocks.GREEN_STONE_WALL, MSBlocks.GREEN_STONE);
		buttonWithItem(MSBlocks.GREEN_STONE_BUTTON, MSBlocks.GREEN_STONE);
		pressurePlateWithItem(MSBlocks.GREEN_STONE_PRESSURE_PLATE, MSBlocks.GREEN_STONE);
		
		simpleBlockWithItem(MSBlocks.POLISHED_GREEN_STONE);
		stairsWithItem(MSBlocks.POLISHED_GREEN_STONE_STAIRS, MSBlocks.POLISHED_GREEN_STONE);
		slabWithItem(MSBlocks.POLISHED_GREEN_STONE_SLAB, MSBlocks.POLISHED_GREEN_STONE);
		wallWithItem(MSBlocks.POLISHED_GREEN_STONE_WALL, MSBlocks.POLISHED_GREEN_STONE);
		
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICKS);
		wallWithItem(MSBlocks.GREEN_STONE_BRICK_WALL, MSBlocks.GREEN_STONE_BRICKS);
		
		unflippedColumnWithItem(MSBlocks.GREEN_STONE_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture("polished_green_stone")));
		simpleBlockWithItem(MSBlocks.CHISELED_GREEN_STONE_BRICKS);
		
		simpleBlockWithItem(MSBlocks.HORIZONTAL_GREEN_STONE_BRICKS);
		stairsWithItem(MSBlocks.HORIZONTAL_GREEN_STONE_BRICK_STAIRS, MSBlocks.HORIZONTAL_GREEN_STONE_BRICKS);
		slabWithItem(MSBlocks.HORIZONTAL_GREEN_STONE_BRICK_SLAB, MSBlocks.HORIZONTAL_GREEN_STONE_BRICKS);
		wallWithItem(MSBlocks.HORIZONTAL_GREEN_STONE_BRICK_WALL, MSBlocks.HORIZONTAL_GREEN_STONE_BRICKS);
		
		simpleBlockWithItem(MSBlocks.VERTICAL_GREEN_STONE_BRICKS);
		stairsWithItem(MSBlocks.VERTICAL_GREEN_STONE_BRICK_STAIRS, MSBlocks.VERTICAL_GREEN_STONE_BRICKS);
		slabWithItem(MSBlocks.VERTICAL_GREEN_STONE_BRICK_SLAB, MSBlocks.VERTICAL_GREEN_STONE_BRICKS);
		wallWithItem(MSBlocks.VERTICAL_GREEN_STONE_BRICK_WALL, MSBlocks.VERTICAL_GREEN_STONE_BRICKS);
		
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
		directionalUpWithItem(MSBlocks.CHISELED_SANDSTONE_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture("sandstone_column_end")));
		unflippedColumnWithItem(MSBlocks.RED_SANDSTONE_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture(id.withSuffix("_end"))));
		directionalUpWithItem(MSBlocks.CHISELED_RED_SANDSTONE_COLUMN,
				id -> models().cubeColumn(
						id.getPath(),
						texture(id),
						texture("red_sandstone_column_end")));
		
		axisWithItem(MSBlocks.CARVED_LOG,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		simpleHorizontal(MSBlocks.CARVED_WOODEN_LEAF, this::existing);
		flatItem(MSItems.CARVED_WOODEN_LEAF, MSBlockStateProvider::texture);
		
		simpleBlockWithItem(MSBlocks.UNCARVED_WOOD);
		stairsWithItem(MSBlocks.UNCARVED_WOOD_STAIRS, MSBlocks.UNCARVED_WOOD);
		slabWithItem(MSBlocks.UNCARVED_WOOD_SLAB, MSBlocks.UNCARVED_WOOD);
		buttonWithItem(MSBlocks.UNCARVED_WOOD_BUTTON, MSBlocks.UNCARVED_WOOD);
		pressurePlateWithItem(MSBlocks.UNCARVED_WOOD_PRESSURE_PLATE, MSBlocks.UNCARVED_WOOD);
		fenceWithItem(MSBlocks.UNCARVED_WOOD_FENCE, MSBlocks.UNCARVED_WOOD);
		fenceGateWithItem(MSBlocks.UNCARVED_WOOD_FENCE_GATE, MSBlocks.UNCARVED_WOOD);
		signBlock(MSBlocks.CARVED_SIGN.get(), MSBlocks.CARVED_WALL_SIGN.get(),
				blockTexture(MSBlocks.CARVED_PLANKS.get()));
		hangingSignBlock(MSBlocks.CARVED_HANGING_SIGN.get(), MSBlocks.CARVED_WALL_HANGING_SIGN.get(),
				blockTexture(MSBlocks.CARVED_PLANKS.get()));
		
		simpleBlockWithItem(MSBlocks.CHIPBOARD);
		stairsWithItem(MSBlocks.CHIPBOARD_STAIRS, MSBlocks.CHIPBOARD);
		slabWithItem(MSBlocks.CHIPBOARD_SLAB, MSBlocks.CHIPBOARD);
		buttonWithItem(MSBlocks.CHIPBOARD_BUTTON, MSBlocks.CHIPBOARD);
		pressurePlateWithItem(MSBlocks.CHIPBOARD_PRESSURE_PLATE, MSBlocks.CHIPBOARD);
		fenceWithItem(MSBlocks.CHIPBOARD_FENCE, MSBlocks.CHIPBOARD);
		fenceGateWithItem(MSBlocks.CHIPBOARD_FENCE_GATE, MSBlocks.CHIPBOARD);
		
		simpleBlockWithItem(MSBlocks.WOOD_SHAVINGS);
		
		simpleBlockWithItem(MSBlocks.CARVED_HEAVY_PLANKS);
		stairsWithItem(MSBlocks.CARVED_HEAVY_PLANK_STAIRS, MSBlocks.CARVED_HEAVY_PLANKS);
		slabWithItem(MSBlocks.CARVED_HEAVY_PLANK_SLAB, MSBlocks.CARVED_HEAVY_PLANKS);
		
		simpleBlockWithItem(MSBlocks.CARVED_PLANKS);
		stairsWithItem(MSBlocks.CARVED_STAIRS, MSBlocks.CARVED_PLANKS);
		slabWithItem(MSBlocks.CARVED_SLAB, MSBlocks.CARVED_PLANKS);
		buttonWithItem(MSBlocks.CARVED_BUTTON, MSBlocks.CARVED_PLANKS);
		pressurePlateWithItem(MSBlocks.CARVED_PRESSURE_PLATE, MSBlocks.CARVED_PLANKS);
		fenceWithItem(MSBlocks.CARVED_FENCE, MSBlocks.CARVED_PLANKS);
		fenceGateWithItem(MSBlocks.CARVED_FENCE_GATE, MSBlocks.CARVED_PLANKS);
		simpleDoorBlock(MSBlocks.CARVED_DOOR);
		trapDoorWithItem(MSBlocks.CARVED_TRAPDOOR);
		flatItem(MSItems.CARVED_DOOR, MSBlockStateProvider::itemTexture);
		
		simpleBlockWithItem(MSBlocks.POLISHED_UNCARVED_WOOD);
		stairsWithItem(MSBlocks.POLISHED_UNCARVED_STAIRS, MSBlocks.POLISHED_UNCARVED_WOOD);
		slabWithItem(MSBlocks.POLISHED_UNCARVED_SLAB, MSBlocks.POLISHED_UNCARVED_WOOD);
		
		simpleBlock(MSBlocks.CARVED_BUSH,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.CARVED_BUSH, MSBlockStateProvider::texture);
		simpleHorizontalWithItem(MSBlocks.CARVED_KNOTTED_WOOD,
				id -> models().singleTexture(id.getPath(), new ResourceLocation("template_glazed_terracotta"), "pattern", texture(id)));
		simpleBlock(MSBlocks.WOODEN_GRASS,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.WOODEN_GRASS, MSBlockStateProvider::texture);
		
		simpleBlockWithItem(MSBlocks.TREATED_UNCARVED_WOOD);
		stairsWithItem(MSBlocks.TREATED_UNCARVED_WOOD_STAIRS, MSBlocks.TREATED_UNCARVED_WOOD);
		slabWithItem(MSBlocks.TREATED_UNCARVED_WOOD_SLAB, MSBlocks.TREATED_UNCARVED_WOOD);
		buttonWithItem(MSBlocks.TREATED_UNCARVED_WOOD_BUTTON, MSBlocks.TREATED_UNCARVED_WOOD);
		pressurePlateWithItem(MSBlocks.TREATED_UNCARVED_WOOD_PRESSURE_PLATE, MSBlocks.TREATED_UNCARVED_WOOD);
		fenceWithItem(MSBlocks.TREATED_UNCARVED_WOOD_FENCE, MSBlocks.TREATED_UNCARVED_WOOD);
		fenceGateWithItem(MSBlocks.TREATED_UNCARVED_WOOD_FENCE_GATE, MSBlocks.TREATED_UNCARVED_WOOD);
		
		simpleBlockWithItem(MSBlocks.TREATED_CHIPBOARD);
		stairsWithItem(MSBlocks.TREATED_CHIPBOARD_STAIRS, MSBlocks.TREATED_CHIPBOARD);
		slabWithItem(MSBlocks.TREATED_CHIPBOARD_SLAB, MSBlocks.TREATED_CHIPBOARD);
		buttonWithItem(MSBlocks.TREATED_CHIPBOARD_BUTTON, MSBlocks.TREATED_CHIPBOARD);
		pressurePlateWithItem(MSBlocks.TREATED_CHIPBOARD_PRESSURE_PLATE, MSBlocks.TREATED_CHIPBOARD);
		fenceWithItem(MSBlocks.TREATED_CHIPBOARD_FENCE, MSBlocks.TREATED_CHIPBOARD);
		fenceGateWithItem(MSBlocks.TREATED_CHIPBOARD_FENCE_GATE, MSBlocks.TREATED_CHIPBOARD);
		
		simpleBlockWithItem(MSBlocks.TREATED_WOOD_SHAVINGS);
		
		simpleBlockWithItem(MSBlocks.TREATED_HEAVY_PLANKS);
		stairsWithItem(MSBlocks.TREATED_HEAVY_PLANK_STAIRS, MSBlocks.TREATED_HEAVY_PLANKS);
		slabWithItem(MSBlocks.TREATED_HEAVY_PLANK_SLAB, MSBlocks.TREATED_HEAVY_PLANKS);
		
		simpleBlockWithItem(MSBlocks.TREATED_PLANKS);
		stairsWithItem(MSBlocks.TREATED_PLANKS_STAIRS, MSBlocks.TREATED_PLANKS);
		slabWithItem(MSBlocks.TREATED_PLANKS_SLAB, MSBlocks.TREATED_PLANKS);
		buttonWithItem(MSBlocks.TREATED_BUTTON, MSBlocks.TREATED_PLANKS);
		pressurePlateWithItem(MSBlocks.TREATED_PRESSURE_PLATE, MSBlocks.TREATED_PLANKS);
		fenceWithItem(MSBlocks.TREATED_FENCE, MSBlocks.TREATED_PLANKS);
		fenceGateWithItem(MSBlocks.TREATED_FENCE_GATE, MSBlocks.TREATED_PLANKS);
		simpleDoorBlock(MSBlocks.TREATED_DOOR);
		trapDoorWithItem(MSBlocks.TREATED_TRAPDOOR);
		flatItem(MSItems.TREATED_DOOR, MSBlockStateProvider::itemTexture);
		signBlock(MSBlocks.TREATED_SIGN.get(), MSBlocks.TREATED_WALL_SIGN.get(),
				blockTexture(MSBlocks.TREATED_PLANKS.get()));
		hangingSignBlock(MSBlocks.TREATED_HANGING_SIGN.get(), MSBlocks.TREATED_WALL_HANGING_SIGN.get(),
				blockTexture(MSBlocks.TREATED_PLANKS.get()));
		
		simpleBlockWithItem(MSBlocks.POLISHED_TREATED_UNCARVED_WOOD);
		stairsWithItem(MSBlocks.POLISHED_TREATED_UNCARVED_STAIRS, MSBlocks.POLISHED_TREATED_UNCARVED_WOOD);
		slabWithItem(MSBlocks.POLISHED_TREATED_UNCARVED_SLAB, MSBlocks.POLISHED_TREATED_UNCARVED_WOOD);
		
		simpleHorizontalWithItem(MSBlocks.TREATED_CARVED_KNOTTED_WOOD,
				id -> models().singleTexture(id.getPath(), new ResourceLocation("template_glazed_terracotta"), "pattern", texture(id)));
		simpleBlock(MSBlocks.TREATED_WOODEN_GRASS,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.TREATED_WOODEN_GRASS, MSBlockStateProvider::texture);
		
		simpleBlockWithItem(MSBlocks.LACQUERED_UNCARVED_WOOD);
		stairsWithItem(MSBlocks.LACQUERED_UNCARVED_WOOD_STAIRS, MSBlocks.LACQUERED_UNCARVED_WOOD);
		slabWithItem(MSBlocks.LACQUERED_UNCARVED_WOOD_SLAB, MSBlocks.LACQUERED_UNCARVED_WOOD);
		buttonWithItem(MSBlocks.LACQUERED_UNCARVED_WOOD_BUTTON, MSBlocks.LACQUERED_UNCARVED_WOOD);
		pressurePlateWithItem(MSBlocks.LACQUERED_UNCARVED_WOOD_PRESSURE_PLATE, MSBlocks.LACQUERED_UNCARVED_WOOD);
		fenceWithItem(MSBlocks.LACQUERED_UNCARVED_WOOD_FENCE, MSBlocks.LACQUERED_UNCARVED_WOOD);
		fenceGateWithItem(MSBlocks.LACQUERED_UNCARVED_WOOD_FENCE_GATE, MSBlocks.LACQUERED_UNCARVED_WOOD);
		
		simpleBlockWithItem(MSBlocks.LACQUERED_CHIPBOARD);
		stairsWithItem(MSBlocks.LACQUERED_CHIPBOARD_STAIRS, MSBlocks.LACQUERED_CHIPBOARD);
		slabWithItem(MSBlocks.LACQUERED_CHIPBOARD_SLAB, MSBlocks.LACQUERED_CHIPBOARD);
		buttonWithItem(MSBlocks.LACQUERED_CHIPBOARD_BUTTON, MSBlocks.LACQUERED_CHIPBOARD);
		pressurePlateWithItem(MSBlocks.LACQUERED_CHIPBOARD_PRESSURE_PLATE, MSBlocks.LACQUERED_CHIPBOARD);
		fenceWithItem(MSBlocks.LACQUERED_CHIPBOARD_FENCE, MSBlocks.LACQUERED_CHIPBOARD);
		fenceGateWithItem(MSBlocks.LACQUERED_CHIPBOARD_FENCE_GATE, MSBlocks.LACQUERED_CHIPBOARD);
		
		simpleBlockWithItem(MSBlocks.LACQUERED_WOOD_SHAVINGS);
		
		simpleBlockWithItem(MSBlocks.LACQUERED_HEAVY_PLANKS);
		stairsWithItem(MSBlocks.LACQUERED_HEAVY_PLANK_STAIRS, MSBlocks.LACQUERED_HEAVY_PLANKS);
		slabWithItem(MSBlocks.LACQUERED_HEAVY_PLANK_SLAB, MSBlocks.LACQUERED_HEAVY_PLANKS);
		
		simpleBlockWithItem(MSBlocks.LACQUERED_PLANKS);
		stairsWithItem(MSBlocks.LACQUERED_STAIRS, MSBlocks.LACQUERED_PLANKS);
		slabWithItem(MSBlocks.LACQUERED_SLAB, MSBlocks.LACQUERED_PLANKS);
		buttonWithItem(MSBlocks.LACQUERED_BUTTON, MSBlocks.LACQUERED_PLANKS);
		pressurePlateWithItem(MSBlocks.LACQUERED_PRESSURE_PLATE, MSBlocks.LACQUERED_PLANKS);
		fenceWithItem(MSBlocks.LACQUERED_FENCE, MSBlocks.LACQUERED_PLANKS);
		fenceGateWithItem(MSBlocks.LACQUERED_FENCE_GATE, MSBlocks.LACQUERED_PLANKS);
		simpleDoorBlock(MSBlocks.LACQUERED_DOOR);
		trapDoorWithItem(MSBlocks.LACQUERED_TRAPDOOR);
		flatItem(MSItems.LACQUERED_DOOR, MSBlockStateProvider::itemTexture);
		signBlock(MSBlocks.LACQUERED_SIGN.get(), MSBlocks.LACQUERED_WALL_SIGN.get(),
				blockTexture(MSBlocks.LACQUERED_PLANKS.get()));
		hangingSignBlock(MSBlocks.LACQUERED_HANGING_SIGN.get(), MSBlocks.LACQUERED_WALL_HANGING_SIGN.get(),
				blockTexture(MSBlocks.LACQUERED_PLANKS.get()));
		
		simpleBlockWithItem(MSBlocks.POLISHED_LACQUERED_UNCARVED_WOOD);
		stairsWithItem(MSBlocks.POLISHED_LACQUERED_UNCARVED_STAIRS, MSBlocks.POLISHED_LACQUERED_UNCARVED_WOOD);
		slabWithItem(MSBlocks.POLISHED_LACQUERED_UNCARVED_SLAB, MSBlocks.POLISHED_LACQUERED_UNCARVED_WOOD);
		
		simpleHorizontalWithItem(MSBlocks.LACQUERED_CARVED_KNOTTED_WOOD,
				id -> models().singleTexture(id.getPath(), new ResourceLocation("template_glazed_terracotta"), "pattern", texture(id)));
		simpleBlock(MSBlocks.LACQUERED_WOODEN_MUSHROOM,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.LACQUERED_WOODEN_MUSHROOM, MSBlockStateProvider::texture);
		
		simpleBlockWithItem(MSBlocks.WOODEN_LAMP);
		
		simpleBlockWithItem(MSBlocks.DENSE_CLOUD);
		simpleBlockWithItem(MSBlocks.BRIGHT_DENSE_CLOUD);
		simpleBlockWithItem(MSBlocks.SUGAR_CUBE);
		
		//Land Tree Blocks
		axisWithItem(MSBlocks.GLOWING_LOG,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		axisWithItem(MSBlocks.STRIPPED_GLOWING_LOG,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		axisWithItem(MSBlocks.FROST_LOG,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		axisWithItem(MSBlocks.STRIPPED_FROST_LOG,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		axisWithItem(MSBlocks.RAINBOW_LOG,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		axisWithItem(MSBlocks.STRIPPED_RAINBOW_LOG,
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
		axisWithItem(MSBlocks.STRIPPED_DEAD_LOG,
				id -> models().cubeColumn(id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		axisWithItem(MSBlocks.CINDERED_LOG,
				id -> models().cube(id.getPath(),
								texture(id.withSuffix("_bottom")),
								texture(id.withSuffix("_top")),
								texture(id.withSuffix("_north")),
								texture(id.withSuffix("_south")),
								texture(id.withSuffix("_east")),
								texture(id.withSuffix("_west")))
						.texture("particle", texture(id.withSuffix("_north"))));
		axisWithItem(MSBlocks.STRIPPED_CINDERED_LOG,
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
		axisWithItem(MSBlocks.STRIPPED_GLOWING_WOOD,
				id -> models().cubeColumn(id.getPath(),
						texture("stripped_glowing_log"),
						texture("stripped_glowing_log")));
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
		axisWithItem(MSBlocks.STRIPPED_FROST_WOOD,
				id -> models().cubeColumn(id.getPath(),
						texture("stripped_frost_log"),
						texture("stripped_frost_log")));
		axisWithItem(MSBlocks.RAINBOW_WOOD,
				id -> models().cubeColumn(id.getPath(),
						texture("rainbow_log"),
						texture("rainbow_log")));
		axisWithItem(MSBlocks.STRIPPED_RAINBOW_WOOD,
				id -> models().cubeColumn(id.getPath(),
						texture("stripped_rainbow_log"),
						texture("stripped_rainbow_log")));
		axisWithItem(MSBlocks.END_WOOD,
				id -> models().cubeColumn(id.getPath(),
						texture("end_log"),
						texture("end_log")));
		axisWithItem(MSBlocks.STRIPPED_END_WOOD,
				id -> models().cubeColumn(id.getPath(),
						texture("stripped_end_log"),
						texture("stripped_end_log")));
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
		axisWithItem(MSBlocks.STRIPPED_DEAD_WOOD,
				id -> models().cubeColumn(id.getPath(),
						texture("stripped_dead_log"),
						texture("stripped_dead_log")));
		axisWithItem(MSBlocks.CINDERED_WOOD,
				id -> models().cubeColumn(id.getPath(),
						texture("cindered_log_west"),
						texture("cindered_log_west")));
		axisWithItem(MSBlocks.STRIPPED_CINDERED_WOOD,
				id -> models().cubeColumn(id.getPath(),
						texture("stripped_cindered_log"),
						texture("stripped_cindered_log")));
		axisWithItem(MSBlocks.PETRIFIED_WOOD,
				id -> models().cubeColumn(id.getPath(),
						texture("petrified_log"),
						texture("petrified_log")));
		
		simpleBlockWithItem(MSBlocks.GLOWING_PLANKS);
		stairsWithItem(MSBlocks.GLOWING_STAIRS, MSBlocks.GLOWING_PLANKS);
		slabWithItem(MSBlocks.GLOWING_SLAB, MSBlocks.GLOWING_PLANKS);
		buttonWithItem(MSBlocks.GLOWING_BUTTON, MSBlocks.GLOWING_PLANKS);
		pressurePlateWithItem(MSBlocks.GLOWING_PRESSURE_PLATE, MSBlocks.GLOWING_PLANKS);
		fenceWithItem(MSBlocks.GLOWING_FENCE, MSBlocks.GLOWING_PLANKS);
		fenceGateWithItem(MSBlocks.GLOWING_FENCE_GATE, MSBlocks.GLOWING_PLANKS);
		simpleDoorBlock(MSBlocks.GLOWING_DOOR);
		trapDoorWithItem(MSBlocks.GLOWING_TRAPDOOR);
		flatItem(MSItems.GLOWING_DOOR, MSBlockStateProvider::itemTexture);
		signBlock(MSBlocks.GLOWING_SIGN.get(), MSBlocks.GLOWING_WALL_SIGN.get(),
				blockTexture(MSBlocks.GLOWING_PLANKS.get()));
		hangingSignBlock(MSBlocks.GLOWING_HANGING_SIGN.get(), MSBlocks.GLOWING_WALL_HANGING_SIGN.get(),
				blockTexture(MSBlocks.GLOWING_PLANKS.get()));
		
		simpleBlockWithItem(MSBlocks.FROST_PLANKS);
		stairsWithItem(MSBlocks.FROST_STAIRS, MSBlocks.FROST_PLANKS);
		slabWithItem(MSBlocks.FROST_SLAB, MSBlocks.FROST_PLANKS);
		buttonWithItem(MSBlocks.FROST_BUTTON, MSBlocks.FROST_PLANKS);
		pressurePlateWithItem(MSBlocks.FROST_PRESSURE_PLATE, MSBlocks.FROST_PLANKS);
		fenceWithItem(MSBlocks.FROST_FENCE, MSBlocks.FROST_PLANKS);
		fenceGateWithItem(MSBlocks.FROST_FENCE_GATE, MSBlocks.FROST_PLANKS);
		simpleDoorBlock(MSBlocks.FROST_DOOR);
		trapDoorWithItem(MSBlocks.FROST_TRAPDOOR);
		flatItem(MSItems.FROST_DOOR, MSBlockStateProvider::itemTexture);
		signBlock(MSBlocks.FROST_SIGN.get(), MSBlocks.FROST_WALL_SIGN.get(),
				blockTexture(MSBlocks.FROST_PLANKS.get()));
		hangingSignBlock(MSBlocks.FROST_HANGING_SIGN.get(), MSBlocks.FROST_WALL_HANGING_SIGN.get(),
				blockTexture(MSBlocks.FROST_PLANKS.get()));
		
		simpleBlockWithItem(MSBlocks.RAINBOW_PLANKS);
		buttonWithItem(MSBlocks.RAINBOW_BUTTON, MSBlocks.RAINBOW_PLANKS);
		pressurePlateWithItem(MSBlocks.RAINBOW_PRESSURE_PLATE, MSBlocks.RAINBOW_PLANKS);
		fenceWithItem(MSBlocks.RAINBOW_FENCE, MSBlocks.RAINBOW_PLANKS);
		fenceGateWithItem(MSBlocks.RAINBOW_FENCE_GATE, MSBlocks.RAINBOW_PLANKS);
		simpleDoorBlock(MSBlocks.RAINBOW_DOOR);
		trapDoorWithItem(MSBlocks.RAINBOW_TRAPDOOR);
		flatItem(MSItems.RAINBOW_DOOR, MSBlockStateProvider::itemTexture);
		signBlock(MSBlocks.RAINBOW_SIGN.get(), MSBlocks.RAINBOW_WALL_SIGN.get(),
				blockTexture(MSBlocks.RAINBOW_PLANKS.get()));
		hangingSignBlock(MSBlocks.RAINBOW_HANGING_SIGN.get(), MSBlocks.RAINBOW_WALL_HANGING_SIGN.get(),
				blockTexture(MSBlocks.RAINBOW_PLANKS.get()));
		
		simpleBlockWithItem(MSBlocks.END_PLANKS);
		buttonWithItem(MSBlocks.END_BUTTON, MSBlocks.END_PLANKS);
		pressurePlateWithItem(MSBlocks.END_PRESSURE_PLATE, MSBlocks.END_PLANKS);
		fenceWithItem(MSBlocks.END_FENCE, MSBlocks.END_PLANKS);
		fenceGateWithItem(MSBlocks.END_FENCE_GATE, MSBlocks.END_PLANKS);
		simpleDoorBlock(MSBlocks.END_DOOR);
		trapDoorWithItem(MSBlocks.END_TRAPDOOR);
		flatItem(MSItems.END_DOOR, MSBlockStateProvider::itemTexture);
		signBlock(MSBlocks.END_SIGN.get(), MSBlocks.END_WALL_SIGN.get(),
				blockTexture(MSBlocks.END_PLANKS.get()));
		hangingSignBlock(MSBlocks.END_HANGING_SIGN.get(), MSBlocks.END_WALL_HANGING_SIGN.get(),
				blockTexture(MSBlocks.END_PLANKS.get()));
		
		
		simpleBlockWithItem(MSBlocks.DEAD_PLANKS);
		buttonWithItem(MSBlocks.DEAD_BUTTON, MSBlocks.DEAD_PLANKS);
		pressurePlateWithItem(MSBlocks.DEAD_PRESSURE_PLATE, MSBlocks.DEAD_PLANKS);
		fenceWithItem(MSBlocks.DEAD_FENCE, MSBlocks.DEAD_PLANKS);
		fenceGateWithItem(MSBlocks.DEAD_FENCE_GATE, MSBlocks.DEAD_PLANKS);
		simpleDoorBlock(MSBlocks.DEAD_DOOR);
		trapDoorWithItem(MSBlocks.DEAD_TRAPDOOR);
		flatItem(MSItems.DEAD_DOOR, MSBlockStateProvider::itemTexture);
		signBlock(MSBlocks.DEAD_SIGN.get(), MSBlocks.DEAD_WALL_SIGN.get(),
				blockTexture(MSBlocks.DEAD_PLANKS.get()));
		hangingSignBlock(MSBlocks.DEAD_HANGING_SIGN.get(), MSBlocks.DEAD_WALL_HANGING_SIGN.get(),
				blockTexture(MSBlocks.DEAD_PLANKS.get()));
		
		simpleBlockWithItem(MSBlocks.CINDERED_PLANKS);
		stairsWithItem(MSBlocks.CINDERED_STAIRS, MSBlocks.CINDERED_PLANKS);
		slabWithItem(MSBlocks.CINDERED_SLAB, MSBlocks.CINDERED_PLANKS);
		buttonWithItem(MSBlocks.CINDERED_BUTTON, MSBlocks.CINDERED_PLANKS);
		pressurePlateWithItem(MSBlocks.CINDERED_PRESSURE_PLATE, MSBlocks.CINDERED_PLANKS);
		fenceWithItem(MSBlocks.CINDERED_FENCE, MSBlocks.CINDERED_PLANKS);
		fenceGateWithItem(MSBlocks.CINDERED_FENCE_GATE, MSBlocks.CINDERED_PLANKS);
		simpleDoorBlock(MSBlocks.CINDERED_DOOR);
		trapDoorWithItem(MSBlocks.CINDERED_TRAPDOOR);
		flatItem(MSItems.CINDERED_DOOR, MSBlockStateProvider::itemTexture);
		signBlock(MSBlocks.CINDERED_SIGN.get(), MSBlocks.CINDERED_WALL_SIGN.get(),
				blockTexture(MSBlocks.CINDERED_PLANKS.get()));
		hangingSignBlock(MSBlocks.CINDERED_HANGING_SIGN.get(), MSBlocks.CINDERED_WALL_HANGING_SIGN.get(),
				blockTexture(MSBlocks.CINDERED_PLANKS.get()));
		
		simpleBlockWithItem(MSBlocks.SHADEWOOD_PLANKS);
		stairsWithItem(MSBlocks.SHADEWOOD_STAIRS, MSBlocks.SHADEWOOD_PLANKS);
		slabWithItem(MSBlocks.SHADEWOOD_SLAB, MSBlocks.SHADEWOOD_PLANKS);
		buttonWithItem(MSBlocks.SHADEWOOD_BUTTON, MSBlocks.SHADEWOOD_PLANKS);
		pressurePlateWithItem(MSBlocks.SHADEWOOD_PRESSURE_PLATE, MSBlocks.SHADEWOOD_PLANKS);
		fenceWithItem(MSBlocks.SHADEWOOD_FENCE, MSBlocks.SHADEWOOD_PLANKS);
		fenceGateWithItem(MSBlocks.SHADEWOOD_FENCE_GATE, MSBlocks.SHADEWOOD_PLANKS);
		simpleDoorBlock(MSBlocks.SHADEWOOD_DOOR, "translucent");
		trapDoorWithItem(MSBlocks.SHADEWOOD_TRAPDOOR, "translucent");
		flatItem(MSItems.SHADEWOOD_DOOR, MSBlockStateProvider::itemTexture);
		signBlock(MSBlocks.SHADEWOOD_SIGN.get(), MSBlocks.SHADEWOOD_WALL_SIGN.get(),
				blockTexture(MSBlocks.SHADEWOOD_PLANKS.get()));
		hangingSignBlock(MSBlocks.SHADEWOOD_HANGING_SIGN.get(), MSBlocks.SHADEWOOD_WALL_HANGING_SIGN.get(),
				blockTexture(MSBlocks.SHADEWOOD_PLANKS.get()));
		
		simpleBlockWithItem(MSBlocks.FROST_LEAVES);
		simpleBlockWithItem(MSBlocks.FROST_LEAVES_FLOWERING);
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
		simpleBlock(MSBlocks.FROST_SAPLING,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.FROST_SAPLING, MSBlockStateProvider::texture);
		
		pottedSaplingBlock(MSBlocks.POTTED_FROST_SAPLING, MSBlocks.FROST_SAPLING);
		pottedSaplingBlock(MSBlocks.POTTED_RAINBOW_SAPLING, MSBlocks.RAINBOW_SAPLING);
		pottedSaplingBlock(MSBlocks.POTTED_END_SAPLING, MSBlocks.END_SAPLING);
		pottedSaplingBlock(MSBlocks.POTTED_SHADEWOOD_SAPLING, MSBlocks.SHADEWOOD_SAPLING);
		
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
		getVariantBuilder(MSBlocks.GLOWING_MUSHROOM.get()).partialState().setModels(weightedVariantModels(new int[]{2, 3, 2, 1, 2, 2, 1, 1},
				i -> models().cross("glowing_mushroom" + i, texture("glowing_mushroom/" + i)).renderType("cutout")));
		flatItem(MSItems.GLOWING_MUSHROOM, id -> texture("glowing_mushroom/1"));
		simpleBlock(MSBlocks.DESERT_BUSH,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.DESERT_BUSH, MSBlockStateProvider::texture);
		getVariantBuilder(MSBlocks.BLOOMING_CACTUS.get()).partialState().setModels(variantModels(3,
				i -> models().cross("blooming_cactus" + i, texture("blooming_cactus_" + i)).renderType("cutout")));
		flatItem(MSItems.BLOOMING_CACTUS, id -> texture("blooming_cactus_2"));
		getVariantBuilder(MSBlocks.SANDY_GRASS.get()).partialState().setModels(variantModels(2,
				i -> models().cross("sandy_grass" + i, texture("sandy_grass_" + i)).renderType("cutout")));
		flatItem(MSItems.SANDY_GRASS, id -> texture("sandy_grass_0"));
		simpleBlock(MSBlocks.DEAD_FOLIAGE,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.DEAD_FOLIAGE, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.PETRIFIED_GRASS,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.PETRIFIED_GRASS, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.PETRIFIED_POPPY,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.PETRIFIED_POPPY, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.IGNEOUS_SPIKE,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.IGNEOUS_SPIKE, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.SINGED_GRASS,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.SINGED_GRASS, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.SINGED_FOLIAGE,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.SINGED_FOLIAGE, MSBlockStateProvider::texture);
		simpleBlock(MSBlocks.SULFUR_BUBBLE,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.SULFUR_BUBBLE, MSBlockStateProvider::texture);
		
		simpleBlock(MSBlocks.GLOWING_MUSHROOM_VINES,
				id -> models().cross(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.GLOWING_MUSHROOM_VINES, MSBlockStateProvider::texture);
		
		directionalUpWithItem(MSBlocks.STRAWBERRY,
				id -> models().cubeBottomTop(id.getPath(),
						texture(id.withSuffix("_side")),
						texture(id.withSuffix("_bottom")),
						texture(id.withSuffix("_top"))));
		simpleHorizontal(MSBlocks.ATTACHED_STRAWBERRY_STEM, 270,
				id -> models().withExistingParent(id.getPath(), "block/attached_melon_stem").renderType("cutout"));
		getVariantBuilder(MSBlocks.STRAWBERRY_STEM.get()).forAllStates(state -> {
			int age = state.getValue(StemBlock.AGE);
			ModelFile model = models().withExistingParent("strawberry_stem_stage" + age, "melon_stem_stage" + age).renderType("cutout");
			return ConfiguredModel.builder().modelFile(model).build();
		});
		
		flatItem(MSItems.TALL_DEAD_BUSH, id -> texture(id.withSuffix("_top")));
		flatItem(MSItems.TALL_SANDY_GRASS, id -> texture(id.withSuffix("_top")));
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
		stairsWithItem(MSBlocks.COARSE_STONE_STAIRS, MSBlocks.COARSE_STONE);
		stairsWithItem(MSBlocks.COARSE_STONE_BRICK_STAIRS, "coarse_stone_brick", texture(MSBlocks.COARSE_STONE_BRICKS));
		stairsWithItem(MSBlocks.SHADE_STAIRS, "shade", texture(MSBlocks.SHADE_STONE));
		stairsWithItem(MSBlocks.SHADE_BRICK_STAIRS, "shade_brick", texture(MSBlocks.SHADE_BRICKS));
		stairsWithItem(MSBlocks.FROST_TILE_STAIRS, MSBlocks.FROST_TILE);
		stairsWithItem(MSBlocks.FROST_BRICK_STAIRS, "frost_brick", texture(MSBlocks.FROST_BRICKS));
		
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
		stairsWithItem(MSBlocks.RAINBOW_STAIRS, MSBlocks.RAINBOW_PLANKS);
		stairsWithItem(MSBlocks.END_STAIRS, MSBlocks.END_PLANKS);
		stairsWithItem(MSBlocks.DEAD_STAIRS, MSBlocks.DEAD_PLANKS);
		
		simpleHorizontalWithItem(MSBlocks.STEEP_GREEN_STONE_BRICK_STAIRS_BASE, this::existing);
		simpleHorizontalWithItem(MSBlocks.STEEP_GREEN_STONE_BRICK_STAIRS_TOP, this::existing);
		
		slabWithItem(MSBlocks.COARSE_STONE_SLAB, MSBlocks.COARSE_STONE);
		slabWithItem(MSBlocks.COARSE_STONE_BRICK_SLAB, MSBlocks.COARSE_STONE_BRICKS);
		slabWithItem(MSBlocks.SHADE_SLAB, MSBlocks.SHADE_STONE);
		slabWithItem(MSBlocks.SHADE_BRICK_SLAB, MSBlocks.SHADE_BRICKS);
		slabWithItem(MSBlocks.FROST_TILE_SLAB, MSBlocks.FROST_TILE);
		slabWithItem(MSBlocks.FROST_BRICK_SLAB, MSBlocks.FROST_BRICKS);
		slabWithItem(MSBlocks.MYCELIUM_SLAB, MSBlocks.MYCELIUM_STONE);
		slabWithItem(MSBlocks.MYCELIUM_BRICK_SLAB, MSBlocks.MYCELIUM_BRICKS);
		slabWithItem(MSBlocks.CHALK_SLAB, MSBlocks.CHALK);
		slabWithItem(MSBlocks.CHALK_BRICK_SLAB, MSBlocks.CHALK_BRICKS);
		slabWithItem(MSBlocks.PINK_STONE_SLAB, MSBlocks.PINK_STONE);
		slabWithItem(MSBlocks.PINK_STONE_BRICK_SLAB, MSBlocks.PINK_STONE_BRICKS);
		slabWithItem(MSBlocks.BROWN_STONE_SLAB, MSBlocks.BROWN_STONE);
		slabWithItem(MSBlocks.BROWN_STONE_BRICK_SLAB, MSBlocks.BROWN_STONE_BRICKS);
		slabWithItem(MSBlocks.GREEN_STONE_SLAB, MSBlocks.GREEN_STONE);
		slabWithItem(MSBlocks.GREEN_STONE_BRICK_SLAB, MSBlocks.GREEN_STONE_BRICKS.getId().getPath(),
				texture("horizontal_green_stone_bricks"), texture("polished_green_stone"));
		slabWithItem(MSBlocks.RAINBOW_SLAB, MSBlocks.RAINBOW_PLANKS);
		slabWithItem(MSBlocks.END_SLAB, MSBlocks.END_PLANKS);
		slabWithItem(MSBlocks.DEAD_SLAB, MSBlocks.DEAD_PLANKS);
		
		{
			ModelFile verticalUnpowered = models()
					.cubeColumn("trajectory_block_vertical_unpowered",
							texture("redstone_machine_block"),
							texture("trajectory_block/vertical_top_unpowered"));
			ModelFile verticalPowered = models()
					.cubeColumn("trajectory_block_vertical_powered",
							texture("redstone_machine_block"),
							texture("trajectory_block/vertical_top_powered"));
			ModelFile horizontalUnpowered = models().getExistingFile(id("trajectory_block_horizontal_unpowered"));
			ModelFile horizontalPowered = models().getExistingFile(id("trajectory_block_horizontal_powered"));
			directionalUp(MSBlocks.TRAJECTORY_BLOCK, state -> {
				boolean powered = state.getValue(TrajectoryBlock.POWERED);
				
				if(state.getValue(TrajectoryBlock.FACING).getAxis() == Direction.Axis.Y)
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
					models().cubeAll("variable_solid_switch_high_power", texture("variable_solid_switch/high_power")),
					models().cubeAll("variable_solid_switch_medium_power", texture("variable_solid_switch/medium_power")),
					models().cubeAll("variable_solid_switch_low_power", texture("variable_solid_switch/low_power")),
					models().cubeAll("variable_solid_switch_unpowered", texture("variable_solid_switch/unpowered")));
		}
		{
			ModelFile highPower = models().cubeAll("timed_solid_switch_high_power", texture("timed_solid_switch/high_power"));
			ModelFile mediumPower = models().cubeAll("timed_solid_switch_medium_power", texture("timed_solid_switch/medium_power"));
			ModelFile lowPower = models().cubeAll("timed_solid_switch_low_power", texture("timed_solid_switch/low_power"));
			ModelFile unpowered = models().cubeAll("timed_solid_switch_unpowered", texture("timed_solid_switch/unpowered"));
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
		{
			ModelFile poweredModel = models().cubeBottomTop("platform_generator_powered",
					texture("platform_generator/side"),
					texture("platform_generator/bottom"),
					texture("platform_generator/top_powered"));
			ModelFile unpoweredModel = models().cubeBottomTop("platform_generator_unpowered",
					texture("platform_generator/side"),
					texture("platform_generator/bottom"),
					texture("platform_generator/top_unpowered"));
			directionalUp(MSBlocks.PLATFORM_GENERATOR, state -> state.getValue(PlatformGeneratorBlock.POWERED) ? poweredModel : unpoweredModel,
					PlatformGeneratorBlock.INVISIBLE_MODE, PlatformGeneratorBlock.POWER);
			simpleBlockItem(MSBlocks.PLATFORM_GENERATOR.get(), unpoweredModel);
			simpleBlock(MSBlocks.PLATFORM_BLOCK, id -> cubeAll(id).renderType("translucent"));
		}
		{
			ModelFile unpowered = cubeAll(id("platform_receptacle_unpowered"));
			ModelFile powered = cubeAll(id("platform_receptacle_powered"));
			ModelFile absorbing = cubeAll(id("platform_receptacle_powered_absorbing"));
			getVariantBuilder(MSBlocks.PLATFORM_RECEPTACLE.get()).forAllStates(state -> {
				if(state.getValue(PlatformReceptacleBlock.POWERED))
					return ConfiguredModel.builder().modelFile(state.getValue(PlatformReceptacleBlock.ABSORBING) ? absorbing : powered).build();
				else
					return ConfiguredModel.builder().modelFile(unpowered).build();
			});
		}
		directionalNorthWithItem(MSBlocks.ITEM_MAGNET, this::existing);
		{
			ModelFile powered = existing(id("redstone_clock_powered"));
			ModelFile unpowered = existing(id("redstone_clock_unpowered"));
			directionalNorth(MSBlocks.REDSTONE_CLOCK, state -> state.getValue(RedstoneClockBlock.POWERED) ? powered : unpowered);
			simpleBlockItem(MSBlocks.REDSTONE_CLOCK.get(), unpowered);
		}
		directionalUpWithItem(MSBlocks.ROTATOR,
				id -> models().cubeBottomTop(id.getPath(),
						texture(id.withSuffix("_side")),
						texture(id.withSuffix("_bottom")),
						texture(id.withSuffix("_top"))));
		directionalUpWithItem(MSBlocks.TOGGLER,
				id -> models().cubeBottomTop(id.getPath(),
						texture("rotator_side"),
						texture("rotator_bottom"),
						texture("toggler_top")));
		{
			ModelFile powered = existing(id("remote_comparator_powered"));
			ModelFile unpowered = existing(id("remote_comparator_unpowered"));
			directionalNorth(MSBlocks.REMOTE_COMPARATOR, state -> state.getValue(RemoteComparatorBlock.POWERED) ? powered : unpowered,
					RemoteComparatorBlock.CHECK_STATE, RemoteComparatorBlock.DISTANCE_1_16);
			simpleBlockItem(MSBlocks.REMOTE_COMPARATOR.get(), unpowered);
		}
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
					texture("block_pressure_plate/side"),
					texture("block_pressure_plate/bottom"),
					texture("block_pressure_plate/top"));
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
		simpleHorizontalWithItem(MSBlocks.BLOCK_TELEPORTER,
				id -> models().getExistingFile(id));
		
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
		{
			ModelFile noCard = existing(id("totem_lathe_card_slot"));
			ModelFile oneCard = existing(id("totem_lathe_card_slot_c"));
			ModelFile twoCards = existing(id("totem_lathe_card_slot_cc"));
			horizontal(MSBlocks.TOTEM_LATHE.CARD_SLOT, state -> switch(state.getValue(TotemLatheBlock.Slot.COUNT))
			{
				case 0 -> noCard;
				case 1 -> oneCard;
				default -> twoCards;
			});
		}
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
		{
			ModelFile withoutDowel = existing(id("alchemiter_totem_pad"));
			ModelFile dowel = existing(id("alchemiter_totem_pad_dowel"));
			ModelFile carvedDowel = existing(id("alchemiter_totem_pad_totem"));
			horizontal(MSBlocks.ALCHEMITER.TOTEM_PAD, state -> switch(state.getValue(AlchemiterBlock.Pad.DOWEL))
			{
				case NONE -> withoutDowel;
				case DOWEL -> dowel;
				case CARVED_DOWEL -> carvedDowel;
			});
		}
		flatItem(MSItems.ALCHEMITER, MSBlockStateProvider::itemTexture);
		simpleHorizontal(MSBlocks.PUNCH_DESIGNIX.LEFT_LEG, this::existing);
		simpleHorizontal(MSBlocks.PUNCH_DESIGNIX.RIGHT_LEG, this::existing);
		{
			ModelFile withoutCard = existing(id("punch_designix_slot"));
			ModelFile withCard = existing(id("punch_designix_slot_card"));
			horizontal(MSBlocks.PUNCH_DESIGNIX.SLOT,
					state -> state.getValue(PunchDesignixBlock.Slot.HAS_CARD) ? withCard : withoutCard);
		}
		simpleHorizontal(MSBlocks.PUNCH_DESIGNIX.KEYBOARD, this::existing);
		flatItem(MSItems.PUNCH_DESIGNIX, MSBlockStateProvider::itemTexture);
		
		simpleHorizontalWithItem(MSBlocks.MINI_CRUXTRUDER, this::existing);
		simpleHorizontalWithItem(MSBlocks.MINI_TOTEM_LATHE, 0, this::existing);
		simpleHorizontalWithItem(MSBlocks.MINI_ALCHEMITER, 0, this::existing);
		simpleHorizontalWithItem(MSBlocks.MINI_PUNCH_DESIGNIX, 0, this::existing);
		
		{
			ModelFile withoutCard = models().getExistingFile(id("holopad"));
			ModelFile withCard = models().getExistingFile(id("holopad_has_card"));
			horizontal(MSBlocks.HOLOPAD, state -> state.getValue(HolopadBlock.HAS_CARD) ? withCard : withoutCard);
			flatItem(MSItems.HOLOPAD, MSBlockStateProvider::itemTexture);
		}
		{
			ModelFile withoutCard = models().getExistingFile(id("intellibeam_laserstation_cardless"));
			ModelFile withCard = models().getExistingFile(id("intellibeam_laserstation"));
			horizontal(MSBlocks.INTELLIBEAM_LASERSTATION, state -> state.getValue(IntellibeamLaserstationBlock.HAS_CARD) ? withCard : withoutCard);
			flatItem(MSItems.INTELLIBEAM_LASERSTATION, MSBlockStateProvider::itemTexture);
		}
		
		//Misc Machines
		computerBlockWithExistingModels(MSBlocks.COMPUTER);
		flatItem(MSItems.COMPUTER, MSBlockStateProvider::itemTexture);
		computerBlockWithExistingModels(MSBlocks.LAPTOP);
		simpleBlockItem(MSBlocks.LAPTOP.get(), models().getExistingFile(id("laptop_on")));
		computerBlockWithExistingModels(MSBlocks.CROCKERTOP);
		simpleBlockItem(MSBlocks.CROCKERTOP.get(), models().getExistingFile(id("crockertop_on")));
		computerBlockWithExistingModels(MSBlocks.HUBTOP);
		simpleBlockItem(MSBlocks.HUBTOP.get(), models().getExistingFile(id("hubtop_on")));
		computerBlock(MSBlocks.LUNCHTOP,
				existing(id("lunchtop_off")),
				existing(id("lunchtop_on")),
				existing(id("lunchtop_on")),
				existing(id("lunchtop_bsod")));
		simpleBlockItem(MSBlocks.LUNCHTOP.get(), models().getExistingFile(id("lunchtop_off")));
		computerBlock(MSBlocks.OLD_COMPUTER,
				existing(id("old_computer_off")),
				existing(id("old_computer_on")),
				existing(id("old_computer_on")),
				existing(id("old_computer_bsod")));
		flatItem(MSItems.OLD_COMPUTER, MSBlockStateProvider::itemTexture);
		simpleHorizontal(MSBlocks.TRANSPORTALIZER, 0, this::existing);
		flatItem(MSItems.TRANSPORTALIZER, MSBlockStateProvider::itemTexture);
		simpleHorizontal(MSBlocks.TRANS_PORTALIZER, 0, this::existing);
		flatItem(MSItems.TRANS_PORTALIZER, MSBlockStateProvider::itemTexture);
		simpleHorizontalWithItem(MSBlocks.SENDIFICATOR, 0, this::existing);
		{
			ModelFile withoutCard = existing(id("grist_widget"));
			ModelFile withCard = existing(id("grist_widget_with_card"));
			horizontal(MSBlocks.GRIST_WIDGET, 0, state -> state.getValue(GristWidgetBlock.HAS_CARD) ? withCard : withoutCard);
			flatItem(MSItems.GRIST_WIDGET, MSBlockStateProvider::itemTexture);
		}
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
		flatItem(MSItems.LOTUS_TIME_CAPSULE, MSBlockStateProvider::itemTexture);
		
		//Misc Alchemy Semi-Plants
		simpleBlock(MSBlocks.GOLD_SEEDS,
				id -> models().crop(id.getPath(), texture(id)).renderType("cutout"));
		flatItem(MSItems.GOLD_SEEDS, MSBlockStateProvider::itemTexture);
		simpleBlockWithItem(MSBlocks.WOODEN_CACTUS,
				id -> models().withExistingParent(id.getPath(), "block/cactus").renderType("cutout")
						.texture("particle", texture(id.withSuffix("_side")))
						.texture("bottom", texture(id.withSuffix("_bottom")))
						.texture("top", texture(id.withSuffix("_top")))
						.texture("side", texture(id.withSuffix("_side"))));
		
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
		weightedVariantsWithItem(MSBlocks.PINK_FROSTED_TOP_LARGE_CAKE, new int[]{7, 2},
				i -> models().getExistingFile(id("pink_frosted_top_large_cake" + i)));
		cake(MSBlocks.CHOCOLATEY_CAKE);
		flatItem(MSItems.CHOCOLATEY_CAKE, MSBlockStateProvider::itemTexture);
		
		//Explosives
		{
			ModelFile model = models().getExistingFile(new ResourceLocation("tnt"));
			ModelFile itemModel = itemModels().getExistingFile(new ResourceLocation("tnt"));
			for(Supplier<Block> block : Arrays.asList(MSBlocks.PRIMED_TNT, MSBlocks.UNSTABLE_TNT, MSBlocks.INSTANT_TNT))
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
		fluid(MSBlocks.CAULK);
		fluid(MSBlocks.MOLTEN_AMBER);
		
		//DERIVATIVE BLOCKS
		stairsWithItem(MSBlocks.PERFECTLY_GENERIC_STAIRS, MSBlocks.GENERIC_OBJECT);
		slabWithItem(MSBlocks.PERFECTLY_GENERIC_SLAB, MSBlocks.GENERIC_OBJECT);
		wallWithItem(MSBlocks.PERFECTLY_GENERIC_WALL, MSBlocks.GENERIC_OBJECT);
		fenceWithItem(MSBlocks.PERFECTLY_GENERIC_FENCE, MSBlocks.GENERIC_OBJECT);
		fenceGateWithItem(MSBlocks.PERFECTLY_GENERIC_FENCE_GATE, MSBlocks.GENERIC_OBJECT);
		buttonWithItem(MSBlocks.PERFECTLY_GENERIC_BUTTON, MSBlocks.GENERIC_OBJECT);
		pressurePlateWithItem(MSBlocks.PERFECTLY_GENERIC_PRESSURE_PLATE, MSBlocks.GENERIC_OBJECT);
		simpleDoorBlock(MSBlocks.PERFECTLY_GENERIC_DOOR);
		flatItem(MSItems.PERFECTLY_GENERIC_DOOR, MSBlockStateProvider::itemTexture);
		trapDoorWithItem(MSBlocks.PERFECTLY_GENERIC_TRAPDOOR);
		signBlock(MSBlocks.PERFECTLY_GENERIC_SIGN.get(), MSBlocks.PERFECTLY_GENERIC_WALL_SIGN.get(),
				blockTexture(MSBlocks.GENERIC_OBJECT.get()));
		hangingSignBlock(MSBlocks.PERFECTLY_GENERIC_HANGING_SIGN.get(), MSBlocks.PERFECTLY_GENERIC_WALL_HANGING_SIGN.get(),
				blockTexture(MSBlocks.GENERIC_OBJECT.get()));
		
	}
	
	private ModelFile existing(ResourceLocation id)
	{
		return this.models().getExistingFile(id);
	}
	
	public ModelFile ladder(ResourceLocation id)
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
	
	public void hangingSignBlock(Block signBlock, Block wallSignBlock, ResourceLocation texture)
	{
		ModelFile sign = models().sign(name(signBlock), texture);
		hangingSignBlock(signBlock, wallSignBlock, sign);
	}
	
	public void hangingSignBlock(Block signBlock, Block wallSignBlock, ModelFile sign)
	{
		simpleBlock(signBlock, sign);
		simpleBlock(wallSignBlock, sign);
	}
	
	private String name(Block block)
	{
		return key(block).getPath();
	}
	
	private ResourceLocation key(Block block)
	{
		return BuiltInRegistries.BLOCK.getKey(block);
	}
	
	private BlockModelBuilder cubeAll(ResourceLocation id)
	{
		return models().cubeAll(id.getPath(), texture(id));
	}
	
	ModelFile empty(String path, ResourceLocation particleTexture)
	{
		return models().getBuilder(path).texture("particle", particleTexture);
	}
	
	private ModelFile fluidModel(ResourceLocation id)
	{
		return empty(id.getPath(), texture(id.withPrefix("still_")));
	}
	
	private ConfiguredModel[] variantModels(int count, IntFunction<ModelFile> modelProvider)
	{
		ConfiguredModel[] models = new ConfiguredModel[count];
		for(int i = 0; i < count; i++)
			models[i] = new ConfiguredModel(modelProvider.apply(i));
		
		return models;
	}
	
	private ConfiguredModel[] weightedVariantModels(int[] weights, IntFunction<ModelFile> modelProvider)
	{
		ConfiguredModel[] models = new ConfiguredModel[weights.length];
		for(int i = 0; i < weights.length; i++)
			models[i] = ConfiguredModel.builder().modelFile(modelProvider.apply(i)).weight(weights[i]).buildLast();
		
		return models;
	}
	
	@SuppressWarnings("SameParameterValue")
	private void variantsWithItem(DeferredBlock<?> block, int count, IntFunction<ModelFile> modelProvider)
	{
		ConfiguredModel[] models = variantModels(count, modelProvider);
		getVariantBuilder(block.get()).partialState().setModels(models);
		simpleBlockItem(block.get(), models[0].model);
	}
	
	private void weightedVariantsWithItem(DeferredBlock<?> block, int[] weights, IntFunction<ModelFile> modelProvider)
	{
		ConfiguredModel[] models = weightedVariantModels(weights, modelProvider);
		getVariantBuilder(block.get()).partialState().setModels(models);
		simpleBlockItem(block.get(), models[0].model);
	}
	
	private void fluid(DeferredBlock<LiquidBlock> block)
	{
		simpleBlock(block, this::fluidModel);
	}
	
	public void simpleBlockWithItem(ItemBlockPair<?, ?> pair)
	{
		simpleBlockWithItem(pair::asBlock);
	}
	
	public void simpleBlockWithItem(Supplier<Block> block)
	{
		simpleBlockWithItem(block.get(), cubeAll(block.get()));
	}
	
	public void simpleBlock(DeferredBlock<?> block, Function<ResourceLocation, ModelFile> modelProvider)
	{
		simpleBlock(block.get(), modelProvider.apply(block.getId()));
	}
	
	public void simpleBlockWithItem(DeferredBlock<?> block, Function<ResourceLocation, ModelFile> modelProvider)
	{
		simpleBlockWithItem(block.get(), modelProvider.apply(block.getId()));
	}
	
	public void simpleHorizontal(DeferredBlock<?> block, Function<ResourceLocation, ModelFile> modelProvider)
	{
		simpleHorizontal(block, 180, modelProvider);
	}
	
	private void simpleHorizontal(DeferredBlock<?> block, int angleOffset, Function<ResourceLocation, ModelFile> modelProvider)
	{
		var model = modelProvider.apply(block.getId());
		horizontal(block, angleOffset, $ -> model, BlockStateProperties.WATERLOGGED, MSProperties.MACHINE_TOGGLE, BlockStateProperties.POWERED, BlockStateProperties.POWER);
	}
	
	private void horizontal(DeferredBlock<?> block, Function<BlockState, ModelFile> modelProvider, Property<?>... ignored)
	{
		horizontal(block, 180, modelProvider, ignored);
	}
	
	private void horizontal(DeferredBlock<?> block, int angleOffset, Function<BlockState, ModelFile> modelProvider, Property<?>... ignored)
	{
		getVariantBuilder(block.get())
				.forAllStatesExcept(state -> ConfiguredModel.builder()
								.modelFile(modelProvider.apply(state))
								.rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + angleOffset) % 360)
								.build(),
						ignored
				);
	}
	
	private void simpleHorizontalWithItem(DeferredBlock<?> block, Function<ResourceLocation, ModelFile> modelProvider)
	{
		var model = modelProvider.apply(block.getId());
		simpleHorizontal(block, $ -> model);
		simpleBlockItem(block.get(), model);
	}
	
	@SuppressWarnings("SameParameterValue")
	private void simpleHorizontalWithItem(DeferredBlock<?> block, int angleOffset, Function<ResourceLocation, ModelFile> modelProvider)
	{
		var model = modelProvider.apply(block.getId());
		simpleHorizontal(block, angleOffset, $ -> model);
		simpleBlockItem(block.get(), model);
	}
	
	private void directionalUpWithItem(DeferredBlock<?> block, Function<ResourceLocation, ModelFile> modelProvider)
	{
		var model = modelProvider.apply(block.getId());
		directionalUp(block, $ -> model, MSProperties.MACHINE_TOGGLE, BlockStateProperties.POWERED);
		simpleBlockItem(block.get(), model);
	}
	
	/**
	 * Sets up a blockstate definition with rotation based on the facing direction with all six directions.
	 * Assumes that the model is facing upwards when unrotated.
	 */
	private void directionalUp(DeferredBlock<?> block, Function<BlockState, ModelFile> modelProvider, Property<?>... ignored)
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
	
	@SuppressWarnings("SameParameterValue")
	private void directionalNorthWithItem(DeferredBlock<?> block, Function<ResourceLocation, ModelFile> modelProvider)
	{
		var model = modelProvider.apply(block.getId());
		directionalNorth(block, $ -> model, MSProperties.MACHINE_TOGGLE, BlockStateProperties.POWERED, BlockStateProperties.POWER, BlockStateProperties.WATERLOGGED);
		simpleBlockItem(block.get(), model);
	}
	
	/**
	 * Sets up a blockstate definition with rotation based on the facing direction with all six directions.
	 * Assumes that the model is facing northwards when unrotated.
	 */
	private void directionalNorth(DeferredBlock<?> block, Function<BlockState, ModelFile> modelProvider, Property<?>... ignored)
	{
		getVariantBuilder(block.get())
				.forAllStatesExcept(state -> {
					Direction dir = state.getValue(BlockStateProperties.FACING);
					return ConfiguredModel.builder()
							.modelFile(modelProvider.apply(state))
							.rotationX(dir == Direction.DOWN ? 90 : dir == Direction.UP ? 270 : 0)
							.rotationY(dir == Direction.DOWN ? 180 : dir == Direction.UP ? 0 : ((int) dir.toYRot() + 180) % 360)
							.build();
				}, ignored);
	}
	
	/**
	 * While the standard directional block (with {@link MSBlockStateProvider#directionalUp(DeferredBlock, Function, Property[])}) has the down-facing state rotated 180 degrees along the y-axis,
	 * blocks with this function are instead not rotated in both the up-facing state and the down-facing state.
	 */
	private void unflippedColumnWithItem(DeferredBlock<?> block, Function<ResourceLocation, ModelFile> modelProvider)
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
	
	void trimWithItem(DeferredBlock<?> block, Function<ResourceLocation, ModelFile> modelProvider, Function<ResourceLocation, ModelFile> flippedModelProvider)
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
	
	public void axisWithItem(DeferredBlock<?> block, Function<ResourceLocation, ModelFile> modelProvider)
	{
		var model = modelProvider.apply(block.getId());
		axisBlock((RotatedPillarBlock) block.get(), model, model);
		simpleBlockItem(block.get(), model);
	}
	
	public void stairsWithItem(Supplier<StairBlock> block, DeferredBlock<?> sourceBlock)
	{
		stairsWithItem(block, sourceBlock.getId().getPath(), texture(sourceBlock));
	}
	
	void stairsWithItem(Supplier<StairBlock> block, String baseName, ResourceLocation texture)
	{
		stairsWithItem(block, baseName, texture, texture, texture);
	}
	
	private void stairsWithItem(Supplier<StairBlock> block, String baseName, ResourceLocation side, ResourceLocation bottom, ResourceLocation top)
	{
		ModelFile stairs = models().stairs(baseName + "_stairs", side, bottom, top);
		ModelFile stairsInner = models().stairsInner(baseName + "_inner_stairs", side, bottom, top);
		ModelFile stairsOuter = models().stairsOuter(baseName + "_outer_stairs", side, bottom, top);
		stairsBlock(block.get(), stairs, stairsInner, stairsOuter);
		simpleBlockItem(block.get(), stairs);
	}
	
	public void slabWithItem(Supplier<SlabBlock> block, DeferredBlock<?> sourceBlock)
	{
		slabWithItem(block, sourceBlock.getId().getPath(), texture(sourceBlock));
	}
	
	private void slabWithItem(Supplier<SlabBlock> block, String baseName, ResourceLocation texture)
	{
		slabWithItem(block, baseName, texture, texture);
	}
	
	private void slabWithItem(Supplier<SlabBlock> block, String baseName, ResourceLocation side, ResourceLocation topBottom)
	{
		ModelFile slabBottom = models().slab(baseName + "_slab", side, topBottom, topBottom);
		ModelFile slabTop = models().slabTop(baseName + "_slab_top", side, topBottom, topBottom);
		ModelFile doubleSlab = models().getExistingFile(new ResourceLocation("minestuck:" + baseName));
		slabBlock(block.get(), slabBottom, slabTop, doubleSlab);
		simpleBlockItem(block.get(), slabBottom);
	}
	
	void wallWithItem(Supplier<WallBlock> block, DeferredBlock<?> sourceBlock)
	{
		wallWithItem(block, sourceBlock.getId().getPath(), texture(sourceBlock));
	}
	
	private void wallWithItem(Supplier<WallBlock> block, String baseName, ResourceLocation texture)
	{
		wallBlock(block.get(), texture);
		
		ModelFile wallInventory = models().wallInventory(baseName + "_wall_inventory", texture);
		simpleBlockItem(block.get(), wallInventory);
	}
	
	public void fenceWithItem(Supplier<FenceBlock> block, DeferredBlock<?> sourceBlock)
	{
		fenceWithItem(block, sourceBlock.getId().getPath(), texture(sourceBlock));
	}
	
	private void fenceWithItem(Supplier<FenceBlock> block, String baseName, ResourceLocation texture)
	{
		ModelFile fenceInventory = models().fenceInventory(baseName + "_fence_inventory", texture);
		
		fenceBlock(block.get(), texture);
		simpleBlockItem(block.get(), fenceInventory);
	}
	
	public void fenceGateWithItem(Supplier<FenceGateBlock> block, DeferredBlock<?> sourceBlock)
	{
		fenceGateWithItem(block, sourceBlock.getId().getPath(), texture(sourceBlock));
	}
	
	private void fenceGateWithItem(Supplier<FenceGateBlock> block, String baseName, ResourceLocation texture)
	{
		ModelFile fenceGateInventory = models().fenceGate(baseName + "_fence_gate", texture);
		fenceGateBlock(block.get(), texture);
		simpleBlockItem(block.get(), fenceGateInventory);
	}
	
	public void simpleDoorBlock(DeferredBlock<DoorBlock> block)
	{
		String baseName = block.getId().getPath();
		ResourceLocation doorBottom = new ResourceLocation("minestuck:block/" + baseName + "_bottom");
		ResourceLocation doorTop = new ResourceLocation("minestuck:block/" + baseName + "_top");
		
		doorBlockWithRenderType(block.get(), doorBottom, doorTop, "cutout");
	}
	
	public void simpleDoorBlock(DeferredBlock<DoorBlock> block, String renderType)
	{
		String baseName = block.getId().getPath();
		ResourceLocation doorBottom = new ResourceLocation("minestuck:block/" + baseName + "_bottom");
		ResourceLocation doorTop = new ResourceLocation("minestuck:block/" + baseName + "_top");
		
		doorBlockWithRenderType(block.get(), doorBottom, doorTop, renderType);
	}
	
	public void trapDoorWithItem(DeferredBlock<TrapDoorBlock> block)
	{
		trapDoorWithItem(block, block.getId().getPath(), texture(block), "cutout");
	}
	
	public void trapDoorWithItem(DeferredBlock<TrapDoorBlock> block, String renderType)
	{
		trapDoorWithItem(block, block.getId().getPath(), texture(block), renderType);
	}
	
	private void trapDoorWithItem(Supplier<TrapDoorBlock> block, String baseName, ResourceLocation texture, String renderType)
	{
		ModelFile trapDoorInventory = models().trapdoorBottom(baseName + "_bottom", texture);
		
		trapdoorBlockWithRenderType(block.get(), texture, true, renderType);
		simpleBlockItem(block.get(), trapDoorInventory);
	}
	
	public void buttonWithItem(Supplier<ButtonBlock> block, DeferredBlock<?> sourceBlock)
	{
		buttonWithItem(block, sourceBlock.getId().getPath(), texture(sourceBlock));
	}
	
	private void buttonWithItem(Supplier<ButtonBlock> block, String baseName, ResourceLocation texture)
	{
		ModelFile buttonInventory = models().buttonInventory(baseName + "_button_inventory", texture);
		buttonBlock(block.get(), texture);
		simpleBlockItem(block.get(), buttonInventory);
	}
	
	public void pressurePlateWithItem(Supplier<PressurePlateBlock> block, DeferredBlock<?> sourceBlock)
	{
		pressurePlateWithItem(block, sourceBlock.getId().getPath(), texture(sourceBlock));
	}
	
	private void pressurePlateWithItem(Supplier<PressurePlateBlock> block, String baseName, ResourceLocation texture)
	{
		ModelFile pressurePlateInventory = models().pressurePlate(baseName + "_pressure_plate", texture);
		pressurePlateBlock(block.get(), texture);
		simpleBlockItem(block.get(), pressurePlateInventory);
	}
	
	private void customLampWithItem(DeferredBlock<?> block)
	{
		customLampWithItem(block, block.getId().getPath(), texture(block));
	}
	
	public void pottedSaplingBlock(DeferredBlock<Block> block, Supplier<? extends Block> saplingBlock) {
		simpleBlock(block.get(),
				models().withExistingParent(block.getId().getPath(), "block/flower_pot_cross")
						.renderType("cutout").texture("plant", blockTexture(saplingBlock.get())));
	}
	
	private void customLampWithItem(Supplier<? extends Block> block, String baseName, ResourceLocation texture)
	{
		ModelFile lampOn = models().cubeAll(baseName + "_on", new ResourceLocation(texture + "_on"));
		ModelFile lampOff = models().cubeAll(baseName + "_off", new ResourceLocation(texture + "_off"));
		
		getVariantBuilder(block.get()).forAllStates(state -> {
			if(state.getValue(CustomLampBlock.CLICKED))
			{
				return ConfiguredModel.builder().modelFile(lampOn).build();
			} else
			{
				return ConfiguredModel.builder().modelFile(lampOff).build();
			}
		});
		
		simpleBlockItem(block.get(), models().cubeAll(baseName + "_on",
				new ResourceLocation(Minestuck.MOD_ID, "block/" + baseName + "_on")));
	}
	
	private void powerVariableWithItem(DeferredBlock<?> block, ModelFile highPowerModel, ModelFile mediumPowerModel, ModelFile lowPowerModel, ModelFile unpoweredModel)
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
					.from(1 + 2 * bites, 0, 1).to(15, 8, 15)
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
	
	private void cake(DeferredBlock<?> block)
	{
		ResourceLocation id = block.getId();
		getVariantBuilder(block.get()).forAllStates(state -> {
			int bites = state.getValue(CakeBlock.BITES);
			return ConfiguredModel.builder().modelFile(cakeModel(id, bites)).build();
		});
	}
	
	private void computerBlockWithExistingModels(DeferredBlock<?> block)
	{
		computerBlock(block,
				existing(block.getId().withSuffix("_off")),
				existing(block.getId().withSuffix("_on")),
				existing(block.getId().withSuffix("_game_loaded")),
				existing(block.getId().withSuffix("_bsod")));
	}
	
	private void computerBlock(DeferredBlock<?> block, ModelFile off, ModelFile on, ModelFile gameLoaded, ModelFile bsod)
	{
		horizontal(block, state -> switch(state.getValue(ComputerBlock.STATE))
		{
			case OFF -> off;
			case ON -> on;
			case GAME_LOADED -> gameLoaded;
			case BROKEN -> bsod;
		});
	}
	
	public void flatItem(DeferredItem<?> item, Function<ResourceLocation, ResourceLocation> textureProvider)
	{
		itemModels().withExistingParent(item.getId().getPath(),
				new ResourceLocation("item/generated")).texture("layer0",
				textureProvider.apply(item.getId()));
	}
	
	public static ResourceLocation texture(DeferredBlock<?> block)
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
