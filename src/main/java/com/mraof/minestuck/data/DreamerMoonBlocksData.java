package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.data.loot_table.MSBlockLootTables;
import com.mraof.minestuck.data.recipe.CommonRecipes;
import com.mraof.minestuck.data.tag.MinestuckBlockTagsProvider;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.crafting.Ingredient;

import static com.mraof.minestuck.block.DreamerMoonBlocks.*;
import static com.mraof.minestuck.data.MSBlockStateProvider.texture;
import static com.mraof.minestuck.data.recipe.MinestuckRecipeProvider.*;
import static net.minecraft.tags.BlockTags.*;

public class DreamerMoonBlocksData
{
	public static void addEnUsTranslations(MinestuckLanguageProvider provider){
		
		//ITEMS
		provider.addItem(PROSPIT_DROSS, "Prospit Dross");
		provider.addItem(DERSE_RESIDUE, "Derse Residue");
		
		//PROSPIT
		provider.add(PROSPIT_GILDING,"Prospit Gilding");
		provider.add(PROSPIT_GILDED_PATH,"Prospit Gilded Path");
		provider.add(PROSPIT_FERROSTRATA,"Prospit Ferrostrata");
		provider.add(PROSPIT_FERROSTRATA_STAIRS,"Prospit Ferrostrata Stairs");
		provider.add(PROSPIT_FERROSTRATA_SLAB,"Prospit Ferrostrata Slab");
		provider.add(PROSPIT_FERROSTRATA_WALL,"Prospit Ferrostrata Wall");
		provider.add(PROSPIT_FERROSTRATA_BUTTON, "Prospit Ferrostrata Button");
		provider.add(PROSPIT_FERROSTRATA_PRESSURE_PLATE, "Prospit Ferrostrata Pressure Plate");
		provider.add(PROSPIT_ROUGH_FERROSTRATA,"Rough Prospit Ferrostrata");
		provider.add(PROSPIT_ROUGH_FERROSTRATA_STAIRS,"Rough Prospit Ferrostrata Stairs");
		provider.add(PROSPIT_ROUGH_FERROSTRATA_SLAB,"Rough Prospit Ferrostrata Slab");
		provider.add(PROSPIT_ROUGH_FERROSTRATA_WALL,"Rough Prospit Ferrostrata Wall");
		provider.add(PROSPIT_REFINED_FERROSTRATA,"Refined Prospit Ferrostrata");
		provider.add(PROSPIT_REFINED_FERROSTRATA_STAIRS,"Refined Prospit Ferrostrata Stairs");
		provider.add(PROSPIT_REFINED_FERROSTRATA_SLAB,"Refined Prospit Ferrostrata Slab");
		provider.add(PROSPIT_REFINED_FERROSTRATA_WALL,"Refined Prospit Ferrostrata Wall");
		provider.add(PROSPIT_CORE,"Prospit Core");
		provider.add(PROSPIT_CORE_STAIRS,"Prospit Core Stairs");
		provider.add(PROSPIT_CORE_SLAB,"Prospit Core Slab");
		provider.add(PROSPIT_CORE_WALL,"Prospit Core Wall");
		provider.add(PROSPIT_ROUGH_CORE,"Rough Prospit Core");
		provider.add(PROSPIT_ROUGH_CORE_STAIRS,"Rough Prospit Core Stairs");
		provider.add(PROSPIT_ROUGH_CORE_SLAB,"Rough Prospit Core Slab");
		provider.add(PROSPIT_ROUGH_CORE_WALL,"Rough Prospit Core Wall");
		provider.add(PROSPIT_REFINED_CORE,"Refined Prospit Core");
		provider.add(PROSPIT_REFINED_CORE_STAIRS,"Refined Prospit Core Stairs");
		provider.add(PROSPIT_REFINED_CORE_SLAB,"Refined Prospit Core Slab");
		provider.add(PROSPIT_REFINED_CORE_WALL,"Refined Prospit Core Wall");
		provider.add(PROSPIT_CABLE,"Prospit Cable");
		provider.add(PROSPIT_THICK_CABLE,"Thick Prospit Cable");
		provider.add(PROSPIT_PILLAR,"Prospit Pillar");
		provider.add(PROSPIT_DOOR,"Prospit Door");
		provider.add(PROSPIT_TRAPDOOR,"Prospit Trapdoor");
		provider.add(PROSPIT_BRICK,"Prospit Brick");
		provider.add(PROSPIT_CRACKED_BRICK,"Cracked Prospit Brick");
		provider.add(PROSPIT_CHISELED_BRICK,"Chiseled Prospit Brick");
		provider.add(PROSPIT_PAVING_BRICK,"Prospit Paving Brick");
		provider.add(PROSPIT_FANCY_BRICK,"Fancy Prospit Brick");
		provider.add(PROSPIT_TARNISHED_BRICK,"Tarnished Prospit Brick");
		provider.add(PROSPIT_STACKED_BRICK,"Stacked Prospit Brick");
		provider.add(PROSPIT_TILES,"Prospit Tiles");
		provider.add(PROSPIT_BRICK_STAIRS,"Prospit Brick Stairs");
		provider.add(PROSPIT_BRICK_SLAB,"Prospit Brick Slab");
		provider.add(PROSPIT_BRICK_WALL,"Prospit Brick Wall");
		provider.add(PROSPIT_CRACKED_BRICK_STAIRS,"Cracked Prospit Brick Stairs");
		provider.add(PROSPIT_CRACKED_BRICK_SLAB,"Cracked Prospit Brick Slab");
		provider.add(PROSPIT_CRACKED_BRICK_WALL,"Cracked Prospit Brick Wall");
		provider.add(PROSPIT_TARNISHED_BRICK_STAIRS,"Tarnished Prospit Brick Stairs");
		provider.add(PROSPIT_TARNISHED_BRICK_SLAB,"Tarnished Prospit Brick Slab");
		provider.add(PROSPIT_TARNISHED_BRICK_WALL,"Tarnished Prospit Brick Wall");
		provider.add(PROSPIT_CORE_BRICK,"Prospit Core Brick");
		provider.add(PROSPIT_CRACKED_CORE_BRICK,"Cracked Prospit Core Brick");
		provider.add(PROSPIT_CORE_TILES,"Prospit Core Tiles");
		provider.add(PROSPIT_CRACKED_CORE_TILES,"Cracked Prospit Core Tiles");
		provider.add(PROSPIT_CHISELED_CORE,"Chiseled Prospit Core");
		provider.add(PROSPIT_CORE_BRICK_STAIRS,"Prospit Core Brick Stairs");
		provider.add(PROSPIT_CORE_BRICK_SLAB,"Prospit Core Brick Slab");
		provider.add(PROSPIT_CORE_BRICK_WALL,"Prospit Core Brick Wall");
		provider.add(PROSPIT_CRACKED_CORE_BRICK_STAIRS,"Cracked Prospit Brick Stairs");
		provider.add(PROSPIT_CRACKED_CORE_BRICK_SLAB,"Cracked Prospit Brick Slab");
		provider.add(PROSPIT_CRACKED_CORE_BRICK_WALL,"Cracked Prospit Brick Wall");
		provider.add(PROSPIT_CORE_TILE_STAIRS,"Prospit Core Tile Stairs");
		provider.add(PROSPIT_CORE_TILE_SLAB,"Prospit Core Tile Slab");
		provider.add(PROSPIT_CORE_TILE_WALL,"Prospit Core Tile Wall");
		
		//DERSE
		provider.add(DERSE_PLATING,"Derse Plating");
		provider.add(DERSE_PLATED_PATH,"Derse Plated Path");
		provider.add(DERSE_METALLITH,"Derse Metallith");
		provider.add(DERSE_METALLITH_STAIRS,"Derse Metallith Stairs");
		provider.add(DERSE_METALLITH_SLAB,"Derse Metallith Slab");
		provider.add(DERSE_METALLITH_WALL,"Derse Metallith Wall");
		provider.add(DERSE_METALLITH_BUTTON, "Derse Metallith Button");
		provider.add(DERSE_METALLITH_PRESSURE_PLATE, "Derse Metallith Pressure Plate");
		provider.add(DERSE_ROUGH_METALLITH,"Rough Derse Metallith");
		provider.add(DERSE_ROUGH_METALLITH_STAIRS,"Rough Derse Metallith Stairs");
		provider.add(DERSE_ROUGH_METALLITH_SLAB,"Rough Derse Metallith Slab");
		provider.add(DERSE_ROUGH_METALLITH_WALL,"Rough Derse Metallith Wall");
		provider.add(DERSE_REFINED_METALLITH,"Refined Derse Metallith");
		provider.add(DERSE_REFINED_METALLITH_STAIRS,"Refined Derse Metallith Stairs");
		provider.add(DERSE_REFINED_METALLITH_SLAB,"Refined Derse Metallith Slab");
		provider.add(DERSE_REFINED_METALLITH_WALL,"Refined Derse Metallith Wall");
		provider.add(DERSE_CORE,"Derse Core");
		provider.add(DERSE_CORE_STAIRS,"Derse Core Stairs");
		provider.add(DERSE_CORE_SLAB,"Derse Core Slab");
		provider.add(DERSE_CORE_WALL,"Derse Core Wall");
		provider.add(DERSE_ROUGH_CORE,"Rough Derse Core");
		provider.add(DERSE_ROUGH_CORE_STAIRS,"Rough Derse Core Stairs");
		provider.add(DERSE_ROUGH_CORE_SLAB,"Rough Derse Core Slab");
		provider.add(DERSE_ROUGH_CORE_WALL,"Rough Derse Core Wall");
		provider.add(DERSE_REFINED_CORE,"Refined Derse Core");
		provider.add(DERSE_REFINED_CORE_STAIRS,"Refined Derse Core Stairs");
		provider.add(DERSE_REFINED_CORE_SLAB,"Refined Derse Core Slab");
		provider.add(DERSE_REFINED_CORE_WALL,"Refined Derse Core Wall");
		provider.add(DERSE_CABLE,"Derse Cable");
		provider.add(DERSE_THICK_CABLE,"Thick Derse Cable");
		provider.add(DERSE_PILLAR,"Derse Pillar");
		provider.add(DERSE_DOOR,"Derse Door");
		provider.add(DERSE_TRAPDOOR,"Derse Trapdoor");
		provider.add(DERSE_BRICK,"Derse Brick");
		provider.add(DERSE_CRACKED_BRICK,"Cracked Derse Brick");
		provider.add(DERSE_CHISELED_BRICK,"Chiseled Derse Brick");
		provider.add(DERSE_PAVING_BRICK,"Derse Paving Brick");
		provider.add(DERSE_FANCY_BRICK,"Fancy Derse Brick");
		provider.add(DERSE_TARNISHED_BRICK,"Tarnished Derse Brick");
		provider.add(DERSE_STACKED_BRICK,"Stacked Derse Brick");
		provider.add(DERSE_TILES,"Derse Tiles");
		provider.add(DERSE_BRICK_STAIRS,"Derse Brick Stairs");
		provider.add(DERSE_BRICK_SLAB,"Derse Brick Slab");
		provider.add(DERSE_BRICK_WALL,"Derse Brick Wall");
		provider.add(DERSE_CRACKED_BRICK_STAIRS,"Cracked Derse Brick Stairs");
		provider.add(DERSE_CRACKED_BRICK_SLAB,"Cracked Derse Brick Slab");
		provider.add(DERSE_CRACKED_BRICK_WALL,"Cracked Derse Brick Wall");
		provider.add(DERSE_TARNISHED_BRICK_STAIRS,"Tarnished Derse Brick Stairs");
		provider.add(DERSE_TARNISHED_BRICK_SLAB,"Tarnished Derse Brick Slab");
		provider.add(DERSE_TARNISHED_BRICK_WALL,"Tarnished Derse Brick Wall");
		provider.add(DERSE_CORE_BRICK,"Derse Core Brick");
		provider.add(DERSE_CRACKED_CORE_BRICK,"Cracked Derse Core Brick");
		provider.add(DERSE_CORE_TILES,"Derse Core Tiles");
		provider.add(DERSE_CRACKED_CORE_TILES,"Cracked Derse Core Tiles");
		provider.add(DERSE_CHISELED_CORE,"Chiseled Derse Core");
		provider.add(DERSE_CORE_BRICK_STAIRS,"Derse Core Brick Stairs");
		provider.add(DERSE_CORE_BRICK_SLAB,"Derse Core Brick Slab");
		provider.add(DERSE_CORE_BRICK_WALL,"Derse Core Brick Wall");
		provider.add(DERSE_CRACKED_CORE_BRICK_STAIRS,"Cracked Derse Brick Stairs");
		provider.add(DERSE_CRACKED_CORE_BRICK_SLAB,"Cracked Derse Brick Slab");
		provider.add(DERSE_CRACKED_CORE_BRICK_WALL,"Cracked Derse Brick Wall");
		provider.add(DERSE_CORE_TILE_STAIRS,"Derse Core Tile Stairs");
		provider.add(DERSE_CORE_TILE_SLAB,"Derse Core Tile Slab");
		provider.add(DERSE_CORE_TILE_WALL,"Derse Core Tile Wall");
	}
	
	public static void addModels(MSBlockStateProvider provider){
		provider.simpleBlockWithItem(PROSPIT_GILDING);
		provider.simpleBlockWithItem(PROSPIT_GILDED_PATH);
		provider.variantsWithItem(PROSPIT_FERROSTRATA.blockHolder(), 4,
				i -> provider.models().cubeAll("prospit_ferrostrata_" + (i),
						texture(provider.id("prospit_ferrostrata_" + (i)))));
		provider.stairsWithItem(PROSPIT_FERROSTRATA_STAIRS::asBlock,"prospit_ferrostrata_0", texture("prospit_ferrostrata_0"));
		provider.slabWithItem(PROSPIT_FERROSTRATA_SLAB::asBlock,"prospit_ferrostrata_0", texture("prospit_ferrostrata_0"));
		provider.wallWithItem(PROSPIT_FERROSTRATA_WALL::asBlock,"prospit_ferrostrata_0", texture("prospit_ferrostrata_0"));
		provider.buttonWithItem(PROSPIT_FERROSTRATA_BUTTON::asBlock,"prospit_ferrostrata_0", texture("prospit_ferrostrata_0"));
		provider.pressurePlateWithItem(PROSPIT_FERROSTRATA_PRESSURE_PLATE::asBlock,"prospit_ferrostrata_0", texture("prospit_ferrostrata_0"));
		provider.simpleBlockWithItem(PROSPIT_ROUGH_FERROSTRATA);
		provider.stairsWithItem(PROSPIT_ROUGH_FERROSTRATA_STAIRS::asBlock,PROSPIT_ROUGH_FERROSTRATA.blockHolder());
		provider.slabWithItem(PROSPIT_ROUGH_FERROSTRATA_SLAB::asBlock,PROSPIT_ROUGH_FERROSTRATA.blockHolder());
		provider.wallWithItem(PROSPIT_ROUGH_FERROSTRATA_WALL::asBlock,PROSPIT_ROUGH_FERROSTRATA.blockHolder());
		provider.simpleBlockWithItem(PROSPIT_REFINED_FERROSTRATA);
		provider.stairsWithItem(PROSPIT_REFINED_FERROSTRATA_STAIRS::asBlock,PROSPIT_REFINED_FERROSTRATA.blockHolder());
		provider.slabWithItem(PROSPIT_REFINED_FERROSTRATA_SLAB::asBlock,PROSPIT_REFINED_FERROSTRATA.blockHolder());
		provider.wallWithItem(PROSPIT_REFINED_FERROSTRATA_WALL::asBlock,PROSPIT_REFINED_FERROSTRATA.blockHolder());
		provider.simpleBlockWithItem(PROSPIT_CORE);
		provider.stairsWithItem(PROSPIT_CORE_STAIRS::asBlock,PROSPIT_CORE.blockHolder());
		provider.slabWithItem(PROSPIT_CORE_SLAB::asBlock,PROSPIT_CORE.blockHolder());
		provider.wallWithItem(PROSPIT_CORE_WALL::asBlock,PROSPIT_CORE.blockHolder());
		provider.simpleBlockWithItem(PROSPIT_ROUGH_CORE);
		provider.stairsWithItem(PROSPIT_ROUGH_CORE_STAIRS::asBlock,PROSPIT_ROUGH_CORE.blockHolder());
		provider.slabWithItem(PROSPIT_ROUGH_CORE_SLAB::asBlock,PROSPIT_ROUGH_CORE.blockHolder());
		provider.wallWithItem(PROSPIT_ROUGH_CORE_WALL::asBlock,PROSPIT_ROUGH_CORE.blockHolder());
		provider.simpleBlockWithItem(PROSPIT_REFINED_CORE);
		provider.stairsWithItem(PROSPIT_REFINED_CORE_STAIRS::asBlock,PROSPIT_REFINED_CORE.blockHolder());
		provider.slabWithItem(PROSPIT_REFINED_CORE_SLAB::asBlock,PROSPIT_REFINED_CORE.blockHolder());
		provider.wallWithItem(PROSPIT_REFINED_CORE_WALL::asBlock,PROSPIT_REFINED_CORE.blockHolder());
		provider.axisWithItem(PROSPIT_CABLE.blockHolder(),
				id -> provider.models().cubeColumn(
						id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.axisWithItem(PROSPIT_THICK_CABLE.blockHolder(),
				id -> provider.models().cubeColumn(
						id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.axisWithItem(PROSPIT_PILLAR.blockHolder(),
				id -> provider.models().cubeColumn(
						id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.simpleDoorBlock(PROSPIT_DOOR.blockHolder());
		provider.flatItem(PROSPIT_DOOR.itemHolder(), MSBlockStateProvider::itemTexture);
		provider.flatItem(PROSPIT_DOOR.itemHolder(), MSBlockStateProvider::itemTexture);
		provider.trapDoorWithItem(PROSPIT_TRAPDOOR.blockHolder());
		provider.simpleBlockWithItem(PROSPIT_BRICK);
		provider.simpleBlockWithItem(PROSPIT_CRACKED_BRICK);
		provider.simpleBlockWithItem(PROSPIT_CHISELED_BRICK);
		provider.weightedVariantsWithItem(PROSPIT_PAVING_BRICK.blockHolder(), new int[]{12, 6, 1, 1},
				i -> provider.models().cubeAll("prospit_paving_brick" + i, texture("prospit_paving_brick_" + i)));
		provider.simpleBlockWithItem(PROSPIT_FANCY_BRICK);
		provider.simpleBlockWithItem(PROSPIT_TARNISHED_BRICK);
		provider.simpleBlockWithItem(PROSPIT_STACKED_BRICK);
		provider.simpleBlockWithItem(PROSPIT_TILES);
		provider.stairsWithItem(PROSPIT_BRICK_STAIRS::asBlock,PROSPIT_BRICK.blockHolder());
		provider.slabWithItem(PROSPIT_BRICK_SLAB::asBlock,PROSPIT_BRICK.blockHolder());
		provider.wallWithItem(PROSPIT_BRICK_WALL::asBlock,PROSPIT_BRICK.blockHolder());
		provider.stairsWithItem(PROSPIT_CRACKED_BRICK_STAIRS::asBlock,PROSPIT_CRACKED_BRICK.blockHolder());
		provider.slabWithItem(PROSPIT_CRACKED_BRICK_SLAB::asBlock,PROSPIT_CRACKED_BRICK.blockHolder());
		provider.wallWithItem(PROSPIT_CRACKED_BRICK_WALL::asBlock,PROSPIT_CRACKED_BRICK.blockHolder());
		provider.stairsWithItem(PROSPIT_TARNISHED_BRICK_STAIRS::asBlock,PROSPIT_TARNISHED_BRICK.blockHolder());
		provider.slabWithItem(PROSPIT_TARNISHED_BRICK_SLAB::asBlock,PROSPIT_TARNISHED_BRICK.blockHolder());
		provider.wallWithItem(PROSPIT_TARNISHED_BRICK_WALL::asBlock,PROSPIT_TARNISHED_BRICK.blockHolder());
		provider.simpleBlockWithItem(PROSPIT_CORE_BRICK);
		provider.simpleBlockWithItem(PROSPIT_CRACKED_CORE_BRICK);
		provider.simpleBlockWithItem(PROSPIT_CORE_TILES);
		provider.simpleBlockWithItem(PROSPIT_CRACKED_CORE_TILES);
		provider.simpleBlockWithItem(PROSPIT_CHISELED_CORE);
		provider.stairsWithItem(PROSPIT_CORE_BRICK_STAIRS::asBlock,PROSPIT_CORE_BRICK.blockHolder());
		provider.slabWithItem(PROSPIT_CORE_BRICK_SLAB::asBlock,PROSPIT_CORE_BRICK.blockHolder());
		provider.wallWithItem(PROSPIT_CORE_BRICK_WALL::asBlock,PROSPIT_CORE_BRICK.blockHolder());
		provider.stairsWithItem(PROSPIT_CRACKED_CORE_BRICK_STAIRS::asBlock,PROSPIT_CRACKED_CORE_BRICK.blockHolder());
		provider.slabWithItem(PROSPIT_CRACKED_CORE_BRICK_SLAB::asBlock,PROSPIT_CRACKED_CORE_BRICK.blockHolder());
		provider.wallWithItem(PROSPIT_CRACKED_CORE_BRICK_WALL::asBlock,PROSPIT_CRACKED_CORE_BRICK.blockHolder());
		provider.stairsWithItem(PROSPIT_CORE_TILE_STAIRS::asBlock,PROSPIT_CORE_TILES.blockHolder());
		provider.slabWithItem(PROSPIT_CORE_TILE_SLAB::asBlock,PROSPIT_CORE_TILES.blockHolder());
		provider.wallWithItem(PROSPIT_CORE_TILE_WALL::asBlock,PROSPIT_CORE_TILES.blockHolder());
		
		provider.simpleBlockWithItem(DERSE_PLATING);
		provider.simpleBlockWithItem(DERSE_PLATED_PATH);
		provider.variantsWithItem(DERSE_METALLITH.blockHolder(), 4,
				i -> provider.models().cubeAll("derse_metallith_" + (i),
						texture(provider.id("derse_metallith_" + (i)))));
		provider.stairsWithItem(DERSE_METALLITH_STAIRS::asBlock,"derse_metallith_0", texture("derse_metallith_0"));
		provider.slabWithItem(DERSE_METALLITH_SLAB::asBlock,"derse_metallith_0", texture("derse_metallith_0"));
		provider.wallWithItem(DERSE_METALLITH_WALL::asBlock,"derse_metallith_0", texture("derse_metallith_0"));
		provider.buttonWithItem(DERSE_METALLITH_BUTTON::asBlock,"derse_metallith_0", texture("derse_metallith_0"));
		provider.pressurePlateWithItem(DERSE_METALLITH_PRESSURE_PLATE::asBlock,"derse_metallith_0", texture("derse_metallith_0"));
		provider.simpleBlockWithItem(DERSE_ROUGH_METALLITH);
		provider.stairsWithItem(DERSE_ROUGH_METALLITH_STAIRS::asBlock,DERSE_ROUGH_METALLITH.blockHolder());
		provider.slabWithItem(DERSE_ROUGH_METALLITH_SLAB::asBlock,DERSE_ROUGH_METALLITH.blockHolder());
		provider.wallWithItem(DERSE_ROUGH_METALLITH_WALL::asBlock,DERSE_ROUGH_METALLITH.blockHolder());
		provider.simpleBlockWithItem(DERSE_REFINED_METALLITH);
		provider.stairsWithItem(DERSE_REFINED_METALLITH_STAIRS::asBlock,DERSE_REFINED_METALLITH.blockHolder());
		provider.slabWithItem(DERSE_REFINED_METALLITH_SLAB::asBlock,DERSE_REFINED_METALLITH.blockHolder());
		provider.wallWithItem(DERSE_REFINED_METALLITH_WALL::asBlock,DERSE_REFINED_METALLITH.blockHolder());
		provider.simpleBlockWithItem(DERSE_CORE);
		provider.stairsWithItem(DERSE_CORE_STAIRS::asBlock,DERSE_CORE.blockHolder());
		provider.slabWithItem(DERSE_CORE_SLAB::asBlock,DERSE_CORE.blockHolder());
		provider.wallWithItem(DERSE_CORE_WALL::asBlock,DERSE_CORE.blockHolder());
		provider.simpleBlockWithItem(DERSE_ROUGH_CORE);
		provider.stairsWithItem(DERSE_ROUGH_CORE_STAIRS::asBlock,DERSE_ROUGH_CORE.blockHolder());
		provider.slabWithItem(DERSE_ROUGH_CORE_SLAB::asBlock,DERSE_ROUGH_CORE.blockHolder());
		provider.wallWithItem(DERSE_ROUGH_CORE_WALL::asBlock,DERSE_ROUGH_CORE.blockHolder());
		provider.simpleBlockWithItem(DERSE_REFINED_CORE);
		provider.stairsWithItem(DERSE_REFINED_CORE_STAIRS::asBlock,DERSE_REFINED_CORE.blockHolder());
		provider.slabWithItem(DERSE_REFINED_CORE_SLAB::asBlock,DERSE_REFINED_CORE.blockHolder());
		provider.wallWithItem(DERSE_REFINED_CORE_WALL::asBlock,DERSE_REFINED_CORE.blockHolder());
		provider.axisWithItem(DERSE_CABLE.blockHolder(),
				id -> provider.models().cubeColumn(
						id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.axisWithItem(DERSE_THICK_CABLE.blockHolder(),
				id -> provider.models().cubeColumn(
						id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.axisWithItem(DERSE_PILLAR.blockHolder(),
				id -> provider.models().cubeColumn(
						id.getPath(),
						texture(id),
						texture(id.withSuffix("_top"))));
		provider.simpleDoorBlock(DERSE_DOOR.blockHolder());
		provider.flatItem(DERSE_DOOR.itemHolder(), MSBlockStateProvider::itemTexture);
		provider.trapDoorWithItem(DERSE_TRAPDOOR.blockHolder());
		provider.simpleBlockWithItem(DERSE_BRICK);
		provider.simpleBlockWithItem(DERSE_CRACKED_BRICK);
		provider.simpleBlockWithItem(DERSE_CHISELED_BRICK);
		provider.weightedVariantsWithItem(DERSE_PAVING_BRICK.blockHolder(), new int[]{12, 6, 1, 1},
				i -> provider.models().cubeAll("derse_paving_brick" + i, texture("derse_paving_brick_" + i)));
		provider.simpleBlockWithItem(DERSE_FANCY_BRICK);
		provider.simpleBlockWithItem(DERSE_TARNISHED_BRICK);
		provider.simpleBlockWithItem(DERSE_STACKED_BRICK);
		provider.simpleBlockWithItem(DERSE_TILES);
		provider.stairsWithItem(DERSE_BRICK_STAIRS::asBlock,DERSE_BRICK.blockHolder());
		provider.slabWithItem(DERSE_BRICK_SLAB::asBlock,DERSE_BRICK.blockHolder());
		provider.wallWithItem(DERSE_BRICK_WALL::asBlock,DERSE_BRICK.blockHolder());
		provider.stairsWithItem(DERSE_CRACKED_BRICK_STAIRS::asBlock,DERSE_CRACKED_BRICK.blockHolder());
		provider.slabWithItem(DERSE_CRACKED_BRICK_SLAB::asBlock,DERSE_CRACKED_BRICK.blockHolder());
		provider.wallWithItem(DERSE_CRACKED_BRICK_WALL::asBlock,DERSE_CRACKED_BRICK.blockHolder());
		provider.stairsWithItem(DERSE_TARNISHED_BRICK_STAIRS::asBlock,DERSE_TARNISHED_BRICK.blockHolder());
		provider.slabWithItem(DERSE_TARNISHED_BRICK_SLAB::asBlock,DERSE_TARNISHED_BRICK.blockHolder());
		provider.wallWithItem(DERSE_TARNISHED_BRICK_WALL::asBlock,DERSE_TARNISHED_BRICK.blockHolder());
		provider.simpleBlockWithItem(DERSE_CORE_BRICK);
		provider.simpleBlockWithItem(DERSE_CRACKED_CORE_BRICK);
		provider.simpleBlockWithItem(DERSE_CORE_TILES);
		provider.simpleBlockWithItem(DERSE_CRACKED_CORE_TILES);
		provider.simpleBlockWithItem(DERSE_CHISELED_CORE);
		provider.stairsWithItem(DERSE_CORE_BRICK_STAIRS::asBlock,DERSE_CORE_BRICK.blockHolder());
		provider.slabWithItem(DERSE_CORE_BRICK_SLAB::asBlock,DERSE_CORE_BRICK.blockHolder());
		provider.wallWithItem(DERSE_CORE_BRICK_WALL::asBlock,DERSE_CORE_BRICK.blockHolder());
		provider.stairsWithItem(DERSE_CRACKED_CORE_BRICK_STAIRS::asBlock,DERSE_CRACKED_CORE_BRICK.blockHolder());
		provider.slabWithItem(DERSE_CRACKED_CORE_BRICK_SLAB::asBlock,DERSE_CRACKED_CORE_BRICK.blockHolder());
		provider.wallWithItem(DERSE_CRACKED_CORE_BRICK_WALL::asBlock,DERSE_CRACKED_CORE_BRICK.blockHolder());
		provider.stairsWithItem(DERSE_CORE_TILE_STAIRS::asBlock,DERSE_CORE_TILES.blockHolder());
		provider.slabWithItem(DERSE_CORE_TILE_SLAB::asBlock,DERSE_CORE_TILES.blockHolder());
		provider.wallWithItem(DERSE_CORE_TILE_WALL::asBlock,DERSE_CORE_TILES.blockHolder());
	}
	
	public static void addItems(MinestuckItemModelProvider provider){
		provider.simpleItem(PROSPIT_DROSS);
		provider.simpleItem(DERSE_RESIDUE);
	}
	
	public static void addLootTables(MSBlockLootTables provider){
		
		provider.dropSelf(PROSPIT_GILDING.asBlock());
		provider.add(PROSPIT_GILDED_PATH.asBlock(), block ->
				provider.stoneSilktouchDrop(block, PROSPIT_GILDING.asItem()));
		provider.add(PROSPIT_FERROSTRATA.asBlock(), block ->
				provider.stoneSilktouchDrop(block, PROSPIT_ROUGH_FERROSTRATA.asItem()));
		provider.dropSelf(PROSPIT_FERROSTRATA_STAIRS.asBlock());
		provider.add(PROSPIT_FERROSTRATA_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(PROSPIT_FERROSTRATA_WALL.asBlock());
		provider.dropSelf(PROSPIT_FERROSTRATA_BUTTON.asBlock());
		provider.dropSelf(PROSPIT_FERROSTRATA_PRESSURE_PLATE.asBlock());
		provider.dropSelf(PROSPIT_ROUGH_FERROSTRATA.asBlock());
		provider.dropSelf(PROSPIT_ROUGH_FERROSTRATA_STAIRS.asBlock());
		provider.add(PROSPIT_ROUGH_FERROSTRATA_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(PROSPIT_ROUGH_FERROSTRATA_WALL.asBlock());
		provider.dropSelf(PROSPIT_REFINED_FERROSTRATA.asBlock());
		provider.dropSelf(PROSPIT_REFINED_FERROSTRATA_STAIRS.asBlock());
		provider.add(PROSPIT_REFINED_FERROSTRATA_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(PROSPIT_REFINED_FERROSTRATA_WALL.asBlock());
		provider.add(PROSPIT_CORE.asBlock(), block ->
				provider.stoneSilktouchDrop(block, PROSPIT_ROUGH_CORE.asItem()));
		provider.dropSelf(PROSPIT_CORE_STAIRS.asBlock());
		provider.add(PROSPIT_CORE_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(PROSPIT_CORE_WALL.asBlock());
		provider.dropSelf(PROSPIT_ROUGH_CORE.asBlock());
		provider.dropSelf(PROSPIT_ROUGH_CORE_STAIRS.asBlock());
		provider.add(PROSPIT_ROUGH_CORE_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(PROSPIT_ROUGH_CORE_WALL.asBlock());
		provider.dropSelf(PROSPIT_REFINED_CORE.asBlock());
		provider.dropSelf(PROSPIT_REFINED_CORE_STAIRS.asBlock());
		provider.add(PROSPIT_REFINED_CORE_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(PROSPIT_REFINED_CORE_WALL.asBlock());
		provider.dropSelf(PROSPIT_CABLE.asBlock());
		provider.dropSelf(PROSPIT_THICK_CABLE.asBlock());
		provider.dropSelf(PROSPIT_PILLAR.asBlock());
		provider.add(PROSPIT_DOOR.asBlock(), provider::createDoorTable);
		provider.dropSelf(PROSPIT_TRAPDOOR.asBlock());
		provider.dropSelf(PROSPIT_BRICK.asBlock());
		provider.dropSelf(PROSPIT_CRACKED_BRICK.asBlock());
		provider.dropSelf(PROSPIT_CHISELED_BRICK.asBlock());
		provider.dropSelf(PROSPIT_PAVING_BRICK.asBlock());
		provider.dropSelf(PROSPIT_FANCY_BRICK.asBlock());
		provider.dropSelf(PROSPIT_TARNISHED_BRICK.asBlock());
		provider.dropSelf(PROSPIT_STACKED_BRICK.asBlock());
		provider.dropSelf(PROSPIT_TILES.asBlock());
		provider.dropSelf(PROSPIT_BRICK_STAIRS.asBlock());
		provider.add(PROSPIT_BRICK_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(PROSPIT_BRICK_WALL.asBlock());
		provider.dropSelf(PROSPIT_CRACKED_BRICK_STAIRS.asBlock());
		provider.add(PROSPIT_CRACKED_BRICK_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(PROSPIT_CRACKED_BRICK_WALL.asBlock());
		provider.dropSelf(PROSPIT_TARNISHED_BRICK_STAIRS.asBlock());
		provider.add(PROSPIT_TARNISHED_BRICK_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(PROSPIT_TARNISHED_BRICK_WALL.asBlock());
		provider.dropSelf(PROSPIT_CORE_BRICK.asBlock());
		provider.dropSelf(PROSPIT_CRACKED_CORE_BRICK.asBlock());
		provider.dropSelf(PROSPIT_CORE_TILES.asBlock());
		provider.dropSelf(PROSPIT_CRACKED_CORE_TILES.asBlock());
		provider.dropSelf(PROSPIT_CHISELED_CORE.asBlock());
		provider.dropSelf(PROSPIT_CORE_BRICK_STAIRS.asBlock());
		provider.add(PROSPIT_CORE_BRICK_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(PROSPIT_CORE_BRICK_WALL.asBlock());
		provider.dropSelf(PROSPIT_CRACKED_CORE_BRICK_STAIRS.asBlock());
		provider.add(PROSPIT_CRACKED_CORE_BRICK_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(PROSPIT_CRACKED_CORE_BRICK_WALL.asBlock());
		provider.dropSelf(PROSPIT_CORE_TILE_STAIRS.asBlock());
		provider.add(PROSPIT_CORE_TILE_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(PROSPIT_CORE_TILE_WALL.asBlock());
		
		provider.dropSelf(DERSE_PLATING.asBlock());
		provider.add(DERSE_PLATED_PATH.asBlock(), block ->
				provider.stoneSilktouchDrop(block, DERSE_PLATING.asItem()));
		provider.add(DERSE_METALLITH.asBlock(), block ->
				provider.stoneSilktouchDrop(block, DERSE_ROUGH_METALLITH.asItem()));
		provider.dropSelf(DERSE_PLATED_PATH.asBlock());
		provider.dropSelf(DERSE_METALLITH.asBlock());
		provider.dropSelf(DERSE_METALLITH_STAIRS.asBlock());
		provider.add(DERSE_METALLITH_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(DERSE_METALLITH_WALL.asBlock());
		provider.dropSelf(DERSE_METALLITH_BUTTON.asBlock());
		provider.dropSelf(DERSE_METALLITH_PRESSURE_PLATE.asBlock());
		provider.dropSelf(DERSE_ROUGH_METALLITH.asBlock());
		provider.dropSelf(DERSE_ROUGH_METALLITH_STAIRS.asBlock());
		provider.add(DERSE_ROUGH_METALLITH_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(DERSE_ROUGH_METALLITH_WALL.asBlock());
		provider.dropSelf(DERSE_REFINED_METALLITH.asBlock());
		provider.dropSelf(DERSE_REFINED_METALLITH_STAIRS.asBlock());
		provider.add(DERSE_REFINED_METALLITH_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(DERSE_REFINED_METALLITH_WALL.asBlock());
		provider.add(DERSE_CORE.asBlock(), block ->
				provider.stoneSilktouchDrop(block, DERSE_ROUGH_CORE.asItem()));
		provider.dropSelf(DERSE_CORE.asBlock());
		provider.dropSelf(DERSE_CORE_STAIRS.asBlock());
		provider.add(DERSE_CORE_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(DERSE_CORE_WALL.asBlock());
		provider.dropSelf(DERSE_ROUGH_CORE.asBlock());
		provider.dropSelf(DERSE_ROUGH_CORE_STAIRS.asBlock());
		provider.add(DERSE_ROUGH_CORE_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(DERSE_ROUGH_CORE_WALL.asBlock());
		provider.dropSelf(DERSE_REFINED_CORE.asBlock());
		provider.dropSelf(DERSE_REFINED_CORE_STAIRS.asBlock());
		provider.add(DERSE_REFINED_CORE_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(DERSE_REFINED_CORE_WALL.asBlock());
		provider.dropSelf(DERSE_CABLE.asBlock());
		provider.dropSelf(DERSE_THICK_CABLE.asBlock());
		provider.dropSelf(DERSE_PILLAR.asBlock());
		provider.add(DERSE_DOOR.asBlock(), provider::createDoorTable);
		provider.dropSelf(DERSE_TRAPDOOR.asBlock());
		provider.dropSelf(DERSE_BRICK.asBlock());
		provider.dropSelf(DERSE_CRACKED_BRICK.asBlock());
		provider.dropSelf(DERSE_CHISELED_BRICK.asBlock());
		provider.dropSelf(DERSE_PAVING_BRICK.asBlock());
		provider.dropSelf(DERSE_FANCY_BRICK.asBlock());
		provider.dropSelf(DERSE_TARNISHED_BRICK.asBlock());
		provider.dropSelf(DERSE_STACKED_BRICK.asBlock());
		provider.dropSelf(DERSE_TILES.asBlock());
		provider.dropSelf(DERSE_BRICK_STAIRS.asBlock());
		provider.add(DERSE_BRICK_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(DERSE_BRICK_WALL.asBlock());
		provider.dropSelf(DERSE_CRACKED_BRICK_STAIRS.asBlock());
		provider.add(DERSE_CRACKED_BRICK_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(DERSE_CRACKED_BRICK_WALL.asBlock());
		provider.dropSelf(DERSE_TARNISHED_BRICK_STAIRS.asBlock());
		provider.add(DERSE_TARNISHED_BRICK_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(DERSE_TARNISHED_BRICK_WALL.asBlock());
		provider.dropSelf(DERSE_CORE_BRICK.asBlock());
		provider.dropSelf(DERSE_CRACKED_CORE_BRICK.asBlock());
		provider.dropSelf(DERSE_CORE_TILES.asBlock());
		provider.dropSelf(DERSE_CRACKED_CORE_TILES.asBlock());
		provider.dropSelf(DERSE_CHISELED_CORE.asBlock());
		provider.dropSelf(DERSE_CORE_BRICK_STAIRS.asBlock());
		provider.add(DERSE_CORE_BRICK_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(DERSE_CORE_BRICK_WALL.asBlock());
		provider.dropSelf(DERSE_CRACKED_CORE_BRICK_STAIRS.asBlock());
		provider.add(DERSE_CRACKED_CORE_BRICK_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(DERSE_CRACKED_CORE_BRICK_WALL.asBlock());
		provider.dropSelf(DERSE_CORE_TILE_STAIRS.asBlock());
		provider.add(DERSE_CORE_TILE_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(DERSE_CORE_TILE_WALL.asBlock());
	}
	
	public static void addToBlockTags(MinestuckBlockTagsProvider provider){
		
		//PROSPIT
		//blocks
		provider.tag(MINEABLE_WITH_PICKAXE).add(PROSPIT_GILDING.asBlock(), PROSPIT_GILDED_PATH.asBlock(), PROSPIT_FERROSTRATA.asBlock(), PROSPIT_ROUGH_FERROSTRATA.asBlock(), PROSPIT_REFINED_FERROSTRATA.asBlock(), PROSPIT_CORE.asBlock(), PROSPIT_ROUGH_CORE.asBlock(), PROSPIT_REFINED_CORE.asBlock(), PROSPIT_CABLE.asBlock(), PROSPIT_THICK_CABLE.asBlock(), PROSPIT_PILLAR.asBlock(), PROSPIT_DOOR.asBlock(), PROSPIT_TRAPDOOR.asBlock(), PROSPIT_BRICK.asBlock(), PROSPIT_CRACKED_BRICK.asBlock(), PROSPIT_CHISELED_BRICK.asBlock(), PROSPIT_PAVING_BRICK.asBlock(), PROSPIT_FANCY_BRICK.asBlock(), PROSPIT_TARNISHED_BRICK.asBlock(), PROSPIT_STACKED_BRICK.asBlock(), PROSPIT_TILES.asBlock(), PROSPIT_CORE_BRICK.asBlock(), PROSPIT_CRACKED_CORE_BRICK.asBlock(), PROSPIT_CORE_TILES.asBlock(), PROSPIT_CRACKED_CORE_TILES.asBlock(), PROSPIT_CHISELED_CORE.asBlock());
		provider.needsWoodPickaxe(PROSPIT_GILDING.asBlock(), PROSPIT_GILDED_PATH.asBlock(), PROSPIT_FERROSTRATA.asBlock(), PROSPIT_ROUGH_FERROSTRATA.asBlock(), PROSPIT_REFINED_FERROSTRATA.asBlock(), PROSPIT_CORE.asBlock(), PROSPIT_ROUGH_CORE.asBlock(), PROSPIT_REFINED_CORE.asBlock(), PROSPIT_CABLE.asBlock(), PROSPIT_THICK_CABLE.asBlock(), PROSPIT_PILLAR.asBlock(), PROSPIT_DOOR.asBlock(), PROSPIT_TRAPDOOR.asBlock(), PROSPIT_BRICK.asBlock(), PROSPIT_CRACKED_BRICK.asBlock(), PROSPIT_CHISELED_BRICK.asBlock(), PROSPIT_PAVING_BRICK.asBlock(), PROSPIT_FANCY_BRICK.asBlock(), PROSPIT_TARNISHED_BRICK.asBlock(), PROSPIT_STACKED_BRICK.asBlock(), PROSPIT_TILES.asBlock(), PROSPIT_CORE_BRICK.asBlock(), PROSPIT_CRACKED_CORE_BRICK.asBlock(), PROSPIT_CORE_TILES.asBlock(), PROSPIT_CRACKED_CORE_TILES.asBlock(), PROSPIT_CHISELED_CORE.asBlock());
		
		//stairs
		provider.tag(MINEABLE_WITH_PICKAXE).add(PROSPIT_FERROSTRATA_STAIRS.asBlock(), PROSPIT_ROUGH_FERROSTRATA_STAIRS.asBlock(), PROSPIT_REFINED_FERROSTRATA_STAIRS.asBlock(), PROSPIT_CORE_STAIRS.asBlock(), PROSPIT_ROUGH_CORE_STAIRS.asBlock(), PROSPIT_REFINED_CORE_STAIRS.asBlock(), PROSPIT_BRICK_STAIRS.asBlock(), PROSPIT_CRACKED_BRICK_STAIRS.asBlock(), PROSPIT_TARNISHED_BRICK_STAIRS.asBlock(), PROSPIT_CORE_BRICK_STAIRS.asBlock(), PROSPIT_CRACKED_CORE_BRICK_STAIRS.asBlock(), PROSPIT_CORE_TILE_STAIRS.asBlock());
		provider.needsWoodPickaxe(PROSPIT_FERROSTRATA_STAIRS.asBlock(), PROSPIT_ROUGH_FERROSTRATA_STAIRS.asBlock(), PROSPIT_REFINED_FERROSTRATA_STAIRS.asBlock(), PROSPIT_CORE_STAIRS.asBlock(), PROSPIT_ROUGH_CORE_STAIRS.asBlock(), PROSPIT_REFINED_CORE_STAIRS.asBlock(), PROSPIT_BRICK_STAIRS.asBlock(), PROSPIT_CRACKED_BRICK_STAIRS.asBlock(), PROSPIT_TARNISHED_BRICK_STAIRS.asBlock(), PROSPIT_CORE_BRICK_STAIRS.asBlock(), PROSPIT_CRACKED_CORE_BRICK_STAIRS.asBlock(), PROSPIT_CORE_TILE_STAIRS.asBlock());
		provider.tag(STAIRS).add(PROSPIT_FERROSTRATA_STAIRS.asBlock(), PROSPIT_ROUGH_FERROSTRATA_STAIRS.asBlock(), PROSPIT_REFINED_FERROSTRATA_STAIRS.asBlock(), PROSPIT_CORE_STAIRS.asBlock(), PROSPIT_ROUGH_CORE_STAIRS.asBlock(), PROSPIT_REFINED_CORE_STAIRS.asBlock(), PROSPIT_BRICK_STAIRS.asBlock(), PROSPIT_CRACKED_BRICK_STAIRS.asBlock(), PROSPIT_TARNISHED_BRICK_STAIRS.asBlock(), PROSPIT_CORE_BRICK_STAIRS.asBlock(), PROSPIT_CRACKED_CORE_BRICK_STAIRS.asBlock(), PROSPIT_CORE_TILE_STAIRS.asBlock());
		
		//slabs
		provider.tag(MINEABLE_WITH_PICKAXE).add(PROSPIT_FERROSTRATA_SLAB.asBlock(), PROSPIT_ROUGH_FERROSTRATA_SLAB.asBlock(), PROSPIT_REFINED_FERROSTRATA_SLAB.asBlock(), PROSPIT_CORE_SLAB.asBlock(), PROSPIT_ROUGH_CORE_SLAB.asBlock(), PROSPIT_REFINED_CORE_SLAB.asBlock(), PROSPIT_BRICK_SLAB.asBlock(), PROSPIT_CRACKED_BRICK_SLAB.asBlock(), PROSPIT_TARNISHED_BRICK_SLAB.asBlock(), PROSPIT_CORE_BRICK_SLAB.asBlock(), PROSPIT_CRACKED_CORE_BRICK_SLAB.asBlock(), PROSPIT_CORE_TILE_SLAB.asBlock());
		provider.needsWoodPickaxe(PROSPIT_FERROSTRATA_SLAB.asBlock(), PROSPIT_ROUGH_FERROSTRATA_SLAB.asBlock(), PROSPIT_REFINED_FERROSTRATA_SLAB.asBlock(), PROSPIT_CORE_SLAB.asBlock(), PROSPIT_ROUGH_CORE_SLAB.asBlock(), PROSPIT_REFINED_CORE_SLAB.asBlock(), PROSPIT_BRICK_SLAB.asBlock(), PROSPIT_CRACKED_BRICK_SLAB.asBlock(), PROSPIT_TARNISHED_BRICK_SLAB.asBlock(), PROSPIT_CORE_BRICK_SLAB.asBlock(), PROSPIT_CRACKED_CORE_BRICK_SLAB.asBlock(), PROSPIT_CORE_TILE_SLAB.asBlock());
		provider.tag(SLABS).add(PROSPIT_FERROSTRATA_SLAB.asBlock(), PROSPIT_ROUGH_FERROSTRATA_SLAB.asBlock(), PROSPIT_REFINED_FERROSTRATA_SLAB.asBlock(), PROSPIT_CORE_SLAB.asBlock(), PROSPIT_ROUGH_CORE_SLAB.asBlock(), PROSPIT_REFINED_CORE_SLAB.asBlock(), PROSPIT_BRICK_SLAB.asBlock(), PROSPIT_CRACKED_BRICK_SLAB.asBlock(), PROSPIT_TARNISHED_BRICK_SLAB.asBlock(), PROSPIT_CORE_BRICK_SLAB.asBlock(), PROSPIT_CRACKED_CORE_BRICK_SLAB.asBlock(), PROSPIT_CORE_TILE_SLAB.asBlock());
		
		//walls
		provider.tag(MINEABLE_WITH_PICKAXE).add(PROSPIT_FERROSTRATA_WALL.asBlock(), PROSPIT_ROUGH_FERROSTRATA_WALL.asBlock(), PROSPIT_REFINED_FERROSTRATA_WALL.asBlock(), PROSPIT_CORE_WALL.asBlock(), PROSPIT_ROUGH_CORE_WALL.asBlock(), PROSPIT_REFINED_CORE_WALL.asBlock(), PROSPIT_BRICK_WALL.asBlock(), PROSPIT_CRACKED_BRICK_WALL.asBlock(), PROSPIT_TARNISHED_BRICK_WALL.asBlock(), PROSPIT_CORE_BRICK_WALL.asBlock(), PROSPIT_CRACKED_CORE_BRICK_WALL.asBlock(), PROSPIT_CORE_TILE_WALL.asBlock());
		provider.needsWoodPickaxe(PROSPIT_FERROSTRATA_WALL.asBlock(), PROSPIT_ROUGH_FERROSTRATA_WALL.asBlock(), PROSPIT_REFINED_FERROSTRATA_WALL.asBlock(), PROSPIT_CORE_WALL.asBlock(), PROSPIT_ROUGH_CORE_WALL.asBlock(), PROSPIT_REFINED_CORE_WALL.asBlock(), PROSPIT_BRICK_WALL.asBlock(), PROSPIT_CRACKED_BRICK_WALL.asBlock(), PROSPIT_TARNISHED_BRICK_WALL.asBlock(), PROSPIT_CORE_BRICK_WALL.asBlock(), PROSPIT_CRACKED_CORE_BRICK_WALL.asBlock(), PROSPIT_CORE_TILE_WALL.asBlock());
		provider.tag(WALLS).add(PROSPIT_FERROSTRATA_WALL.asBlock(), PROSPIT_ROUGH_FERROSTRATA_WALL.asBlock(), PROSPIT_REFINED_FERROSTRATA_WALL.asBlock(), PROSPIT_CORE_WALL.asBlock(), PROSPIT_ROUGH_CORE_WALL.asBlock(), PROSPIT_REFINED_CORE_WALL.asBlock(), PROSPIT_BRICK_WALL.asBlock(), PROSPIT_CRACKED_BRICK_WALL.asBlock(), PROSPIT_TARNISHED_BRICK_WALL.asBlock(), PROSPIT_CORE_BRICK_WALL.asBlock(), PROSPIT_CRACKED_CORE_BRICK_WALL.asBlock(), PROSPIT_CORE_TILE_WALL.asBlock());
		
		//buttons & pressure plates
		provider.tag(MINEABLE_WITH_PICKAXE).add(PROSPIT_FERROSTRATA_BUTTON.asBlock(), PROSPIT_FERROSTRATA_PRESSURE_PLATE.asBlock());
		//provider.needsWoodPickaxe(PROSPIT_FERROSTRATA_BUTTON.asBlock(), PROSPIT_FERROSTRATA_PRESSURE_PLATE.asBlock());
		provider.tag(BUTTONS).add(PROSPIT_FERROSTRATA_BUTTON.asBlock());
		provider.tag(PRESSURE_PLATES).add(PROSPIT_FERROSTRATA_PRESSURE_PLATE.asBlock());
		
		//DERSE
		//blocks
		provider.tag(MINEABLE_WITH_PICKAXE).add(DERSE_PLATING.asBlock(), DERSE_PLATED_PATH.asBlock(), DERSE_METALLITH.asBlock(), DERSE_ROUGH_METALLITH.asBlock(), DERSE_REFINED_METALLITH.asBlock(), DERSE_CORE.asBlock(), DERSE_ROUGH_CORE.asBlock(), DERSE_REFINED_CORE.asBlock(), DERSE_CABLE.asBlock(), DERSE_THICK_CABLE.asBlock(), DERSE_PILLAR.asBlock(), DERSE_DOOR.asBlock(), DERSE_TRAPDOOR.asBlock(), DERSE_BRICK.asBlock(), DERSE_CRACKED_BRICK.asBlock(), DERSE_CHISELED_BRICK.asBlock(), DERSE_PAVING_BRICK.asBlock(), DERSE_FANCY_BRICK.asBlock(), DERSE_TARNISHED_BRICK.asBlock(), DERSE_STACKED_BRICK.asBlock(), DERSE_TILES.asBlock(), DERSE_CORE_BRICK.asBlock(), DERSE_CRACKED_CORE_BRICK.asBlock(), DERSE_CORE_TILES.asBlock(), DERSE_CRACKED_CORE_TILES.asBlock(), DERSE_CHISELED_CORE.asBlock());
		provider.needsWoodPickaxe(DERSE_PLATING.asBlock(), DERSE_PLATED_PATH.asBlock(), DERSE_METALLITH.asBlock(), DERSE_ROUGH_METALLITH.asBlock(), DERSE_REFINED_METALLITH.asBlock(), DERSE_CORE.asBlock(), DERSE_ROUGH_CORE.asBlock(), DERSE_REFINED_CORE.asBlock(), DERSE_CABLE.asBlock(), DERSE_THICK_CABLE.asBlock(), DERSE_PILLAR.asBlock(), DERSE_DOOR.asBlock(), DERSE_TRAPDOOR.asBlock(), DERSE_BRICK.asBlock(), DERSE_CRACKED_BRICK.asBlock(), DERSE_CHISELED_BRICK.asBlock(), DERSE_PAVING_BRICK.asBlock(), DERSE_FANCY_BRICK.asBlock(), DERSE_TARNISHED_BRICK.asBlock(), DERSE_STACKED_BRICK.asBlock(), DERSE_TILES.asBlock(), DERSE_CORE_BRICK.asBlock(), DERSE_CRACKED_CORE_BRICK.asBlock(), DERSE_CORE_TILES.asBlock(), DERSE_CRACKED_CORE_TILES.asBlock(), DERSE_CHISELED_CORE.asBlock());
		
		//stairs
		provider.tag(MINEABLE_WITH_PICKAXE).add(DERSE_METALLITH_STAIRS.asBlock(), DERSE_ROUGH_METALLITH_STAIRS.asBlock(), DERSE_REFINED_METALLITH_STAIRS.asBlock(), DERSE_CORE_STAIRS.asBlock(), DERSE_ROUGH_CORE_STAIRS.asBlock(), DERSE_REFINED_CORE_STAIRS.asBlock(), DERSE_BRICK_STAIRS.asBlock(), DERSE_CRACKED_BRICK_STAIRS.asBlock(), DERSE_TARNISHED_BRICK_STAIRS.asBlock(), DERSE_CORE_BRICK_STAIRS.asBlock(), DERSE_CRACKED_CORE_BRICK_STAIRS.asBlock(), DERSE_CORE_TILE_STAIRS.asBlock());
		provider.needsWoodPickaxe(DERSE_METALLITH_STAIRS.asBlock(), DERSE_ROUGH_METALLITH_STAIRS.asBlock(), DERSE_REFINED_METALLITH_STAIRS.asBlock(), DERSE_CORE_STAIRS.asBlock(), DERSE_ROUGH_CORE_STAIRS.asBlock(), DERSE_REFINED_CORE_STAIRS.asBlock(), DERSE_BRICK_STAIRS.asBlock(), DERSE_CRACKED_BRICK_STAIRS.asBlock(), DERSE_TARNISHED_BRICK_STAIRS.asBlock(), DERSE_CORE_BRICK_STAIRS.asBlock(), DERSE_CRACKED_CORE_BRICK_STAIRS.asBlock(), DERSE_CORE_TILE_STAIRS.asBlock());
		provider.tag(STAIRS).add(DERSE_METALLITH_STAIRS.asBlock(), DERSE_ROUGH_METALLITH_STAIRS.asBlock(), DERSE_REFINED_METALLITH_STAIRS.asBlock(), DERSE_CORE_STAIRS.asBlock(), DERSE_ROUGH_CORE_STAIRS.asBlock(), DERSE_REFINED_CORE_STAIRS.asBlock(), DERSE_BRICK_STAIRS.asBlock(), DERSE_CRACKED_BRICK_STAIRS.asBlock(), DERSE_TARNISHED_BRICK_STAIRS.asBlock(), DERSE_CORE_BRICK_STAIRS.asBlock(), DERSE_CRACKED_CORE_BRICK_STAIRS.asBlock(), DERSE_CORE_TILE_STAIRS.asBlock());
		
		//slabs
		provider.tag(MINEABLE_WITH_PICKAXE).add(DERSE_METALLITH_SLAB.asBlock(), DERSE_ROUGH_METALLITH_SLAB.asBlock(), DERSE_REFINED_METALLITH_SLAB.asBlock(), DERSE_CORE_SLAB.asBlock(), DERSE_ROUGH_CORE_SLAB.asBlock(), DERSE_REFINED_CORE_SLAB.asBlock(), DERSE_BRICK_SLAB.asBlock(), DERSE_CRACKED_BRICK_SLAB.asBlock(), DERSE_TARNISHED_BRICK_SLAB.asBlock(), DERSE_CORE_BRICK_SLAB.asBlock(), DERSE_CRACKED_CORE_BRICK_SLAB.asBlock(), DERSE_CORE_TILE_SLAB.asBlock());
		provider.needsWoodPickaxe(DERSE_METALLITH_SLAB.asBlock(), DERSE_ROUGH_METALLITH_SLAB.asBlock(), DERSE_REFINED_METALLITH_SLAB.asBlock(), DERSE_CORE_SLAB.asBlock(), DERSE_ROUGH_CORE_SLAB.asBlock(), DERSE_REFINED_CORE_SLAB.asBlock(), DERSE_BRICK_SLAB.asBlock(), DERSE_CRACKED_BRICK_SLAB.asBlock(), DERSE_TARNISHED_BRICK_SLAB.asBlock(), DERSE_CORE_BRICK_SLAB.asBlock(), DERSE_CRACKED_CORE_BRICK_SLAB.asBlock(), DERSE_CORE_TILE_SLAB.asBlock());
		provider.tag(SLABS).add(DERSE_METALLITH_SLAB.asBlock(), DERSE_ROUGH_METALLITH_SLAB.asBlock(), DERSE_REFINED_METALLITH_SLAB.asBlock(), DERSE_CORE_SLAB.asBlock(), DERSE_ROUGH_CORE_SLAB.asBlock(), DERSE_REFINED_CORE_SLAB.asBlock(), DERSE_BRICK_SLAB.asBlock(), DERSE_CRACKED_BRICK_SLAB.asBlock(), DERSE_TARNISHED_BRICK_SLAB.asBlock(), DERSE_CORE_BRICK_SLAB.asBlock(), DERSE_CRACKED_CORE_BRICK_SLAB.asBlock(), DERSE_CORE_TILE_SLAB.asBlock());
		
		//walls
		provider.tag(MINEABLE_WITH_PICKAXE).add(DERSE_METALLITH_WALL.asBlock(), DERSE_ROUGH_METALLITH_WALL.asBlock(), DERSE_REFINED_METALLITH_WALL.asBlock(), DERSE_CORE_WALL.asBlock(), DERSE_ROUGH_CORE_WALL.asBlock(), DERSE_REFINED_CORE_WALL.asBlock(), DERSE_BRICK_WALL.asBlock(), DERSE_CRACKED_BRICK_WALL.asBlock(), DERSE_TARNISHED_BRICK_WALL.asBlock(), DERSE_CORE_BRICK_WALL.asBlock(), DERSE_CRACKED_CORE_BRICK_WALL.asBlock(), DERSE_CORE_TILE_WALL.asBlock());
		provider.needsWoodPickaxe(DERSE_METALLITH_WALL.asBlock(), DERSE_ROUGH_METALLITH_WALL.asBlock(), DERSE_REFINED_METALLITH_WALL.asBlock(), DERSE_CORE_WALL.asBlock(), DERSE_ROUGH_CORE_WALL.asBlock(), DERSE_REFINED_CORE_WALL.asBlock(), DERSE_BRICK_WALL.asBlock(), DERSE_CRACKED_BRICK_WALL.asBlock(), DERSE_TARNISHED_BRICK_WALL.asBlock(), DERSE_CORE_BRICK_WALL.asBlock(), DERSE_CRACKED_CORE_BRICK_WALL.asBlock(), DERSE_CORE_TILE_WALL.asBlock());
		provider.tag(WALLS).add(DERSE_METALLITH_WALL.asBlock(), DERSE_ROUGH_METALLITH_WALL.asBlock(), DERSE_REFINED_METALLITH_WALL.asBlock(), DERSE_CORE_WALL.asBlock(), DERSE_ROUGH_CORE_WALL.asBlock(), DERSE_REFINED_CORE_WALL.asBlock(), DERSE_BRICK_WALL.asBlock(), DERSE_CRACKED_BRICK_WALL.asBlock(), DERSE_TARNISHED_BRICK_WALL.asBlock(), DERSE_CORE_BRICK_WALL.asBlock(), DERSE_CRACKED_CORE_BRICK_WALL.asBlock(), DERSE_CORE_TILE_WALL.asBlock());
		
		//buttons & pressure plates
		provider.tag(MINEABLE_WITH_PICKAXE).add(DERSE_METALLITH_BUTTON.asBlock(), DERSE_METALLITH_PRESSURE_PLATE.asBlock());
		//provider.needsWoodPickaxe(DERSE_METALLITH_BUTTON.asBlock(), DERSE_METALLITH_PRESSURE_PLATE.asBlock());
		provider.tag(BUTTONS).add(DERSE_METALLITH_BUTTON.asBlock());
		provider.tag(PRESSURE_PLATES).add(DERSE_METALLITH_PRESSURE_PLATE.asBlock());
	}
	
	public static void addRecipes(RecipeOutput recipeSaver){
		
		//PROSPIT_GILDING - no recipe
		//PROSPIT_GILDED_PATH
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_GILDING), RecipeCategory.BUILDING_BLOCKS, PROSPIT_GILDED_PATH)
				.unlockedBy("has_prospit_gilding", has(PROSPIT_GILDING)).save(recipeSaver, Minestuck.id("prospit_gilded_path_from_stonecutting"));
		
		//PROSPIT_FERROSTRATA
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(PROSPIT_ROUGH_FERROSTRATA), RecipeCategory.BUILDING_BLOCKS, PROSPIT_FERROSTRATA, 0.1f, 200)
				.unlockedBy("has_prospit_ferrostrata", has(PROSPIT_FERROSTRATA)).save(recipeSaver);
		
		//PROSPIT_FERROSTRATA_STAIRS
		CommonRecipes.stairsRecipe(PROSPIT_FERROSTRATA_STAIRS, PROSPIT_FERROSTRATA).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_FERROSTRATA), RecipeCategory.BUILDING_BLOCKS, PROSPIT_FERROSTRATA_STAIRS)
				.unlockedBy("has_prospit_ferrostrata", has(PROSPIT_FERROSTRATA)).save(recipeSaver, Minestuck.id("prospit_ferrostrata_stairs_from_stonecutting"));
		
		//PROSPIT_FERROSTRATA_SLAB
		CommonRecipes.slabRecipe(PROSPIT_FERROSTRATA_SLAB, PROSPIT_FERROSTRATA).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_FERROSTRATA), RecipeCategory.BUILDING_BLOCKS, PROSPIT_FERROSTRATA_SLAB, 2)
				.unlockedBy("has_prospit_ferrostrata", has(PROSPIT_FERROSTRATA)).save(recipeSaver, Minestuck.id("prospit_ferrostrata_slab_from_stonecutting"));
		
		//PROSPIT_FERROSTRATA_WALL
		CommonRecipes.wallRecipe(PROSPIT_FERROSTRATA_WALL, PROSPIT_FERROSTRATA).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_FERROSTRATA), RecipeCategory.BUILDING_BLOCKS, PROSPIT_FERROSTRATA_WALL)
				.unlockedBy("has_prospit_ferrostrata", has(PROSPIT_FERROSTRATA)).save(recipeSaver, Minestuck.id("prospit_ferrostrata_wall_from_stonecutting"));
		
		//PROSPIT_FERROSTRATA_BUTTON
		CommonRecipes.buttonRecipe(PROSPIT_FERROSTRATA_BUTTON, PROSPIT_FERROSTRATA).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_FERROSTRATA), RecipeCategory.BUILDING_BLOCKS, PROSPIT_FERROSTRATA_BUTTON)
				.unlockedBy("has_prospit_ferrostrata", has(PROSPIT_FERROSTRATA)).save(recipeSaver, Minestuck.id("prospit_ferrostrata_button_from_stonecutting"));
		
		//PROSPIT_FERROSTRATA_PRESSURE_PLATE
		CommonRecipes.buttonRecipe(PROSPIT_FERROSTRATA_PRESSURE_PLATE, PROSPIT_FERROSTRATA).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_FERROSTRATA), RecipeCategory.BUILDING_BLOCKS, PROSPIT_FERROSTRATA_PRESSURE_PLATE)
				.unlockedBy("has_prospit_ferrostrata", has(PROSPIT_FERROSTRATA)).save(recipeSaver, Minestuck.id("prospit_ferrostrata_pressure_plate_from_stonecutting"));
		
		//PROSPIT_ROUGH_FERROSTRATA - no recipe
		
		//PROSPIT_ROUGH_FERROSTRATA_STAIRS
		CommonRecipes.stairsRecipe(PROSPIT_ROUGH_FERROSTRATA_STAIRS, PROSPIT_ROUGH_FERROSTRATA).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_ROUGH_FERROSTRATA), RecipeCategory.BUILDING_BLOCKS, PROSPIT_ROUGH_FERROSTRATA_STAIRS)
				.unlockedBy("has_prospit_rough_ferrostrata", has(PROSPIT_ROUGH_FERROSTRATA)).save(recipeSaver, Minestuck.id("prospit_rough_ferrostrata_stairs_from_stonecutting"));
		
		//PROSPIT_ROUGH_FERROSTRATA_SLAB
		CommonRecipes.slabRecipe(PROSPIT_ROUGH_FERROSTRATA_SLAB, PROSPIT_ROUGH_FERROSTRATA).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_ROUGH_FERROSTRATA), RecipeCategory.BUILDING_BLOCKS, PROSPIT_ROUGH_FERROSTRATA_SLAB, 2)
				.unlockedBy("has_prospit_ferrostrata", has(PROSPIT_ROUGH_FERROSTRATA)).save(recipeSaver, Minestuck.id("prospit_rough_ferrostrata_slab_from_stonecutting"));
		
		//PROSPIT_ROUGH_FERROSTRATA_WALL
		CommonRecipes.wallRecipe(PROSPIT_ROUGH_FERROSTRATA_WALL, PROSPIT_ROUGH_FERROSTRATA).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_ROUGH_FERROSTRATA), RecipeCategory.BUILDING_BLOCKS, PROSPIT_ROUGH_FERROSTRATA_WALL)
				.unlockedBy("has_prospit_ferrostrata", has(PROSPIT_ROUGH_FERROSTRATA)).save(recipeSaver, Minestuck.id("prospit_rough_ferrostrata_wall_from_stonecutting"));
		
		//PROSPIT_REFINED_FERROSTRATA
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(PROSPIT_FERROSTRATA), RecipeCategory.BUILDING_BLOCKS, PROSPIT_REFINED_FERROSTRATA, 0.1f, 200)
				.unlockedBy("has_prospit_rough_ferrostrata", has(PROSPIT_ROUGH_FERROSTRATA)).save(recipeSaver);
		
		//PROSPIT_REFINED_FERROSTRATA_STAIRS
		CommonRecipes.stairsRecipe(PROSPIT_REFINED_FERROSTRATA_STAIRS, PROSPIT_REFINED_FERROSTRATA).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_REFINED_FERROSTRATA), RecipeCategory.BUILDING_BLOCKS, PROSPIT_REFINED_FERROSTRATA_STAIRS)
				.unlockedBy("has_prospit_refined_ferrostrata", has(PROSPIT_REFINED_FERROSTRATA)).save(recipeSaver, Minestuck.id("prospit_refined_ferrostrata_stairs_from_stonecutting"));
		
		//PROSPIT_REFINED_FERROSTRATA_SLAB
		CommonRecipes.stairsRecipe(PROSPIT_REFINED_FERROSTRATA_SLAB, PROSPIT_REFINED_FERROSTRATA).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_REFINED_FERROSTRATA), RecipeCategory.BUILDING_BLOCKS, PROSPIT_REFINED_FERROSTRATA_SLAB, 2)
				.unlockedBy("has_prospit_refined_ferrostrata", has(PROSPIT_REFINED_FERROSTRATA)).save(recipeSaver, Minestuck.id("prospit_refined_ferrostrata_slab_from_stonecutting"));
		
		//PROSPIT_REFINED_FERROSTRATA_WALL
		CommonRecipes.stairsRecipe(PROSPIT_REFINED_FERROSTRATA_WALL, PROSPIT_REFINED_FERROSTRATA).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_REFINED_FERROSTRATA), RecipeCategory.BUILDING_BLOCKS, PROSPIT_REFINED_FERROSTRATA_WALL)
				.unlockedBy("has_prospit_refined_ferrostrata", has(PROSPIT_REFINED_FERROSTRATA)).save(recipeSaver, Minestuck.id("prospit_refined_ferrostrata_wall_from_stonecutting"));
		
		//PROSPIT_CORE
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(PROSPIT_ROUGH_CORE), RecipeCategory.BUILDING_BLOCKS, PROSPIT_CORE, 0.1f, 200)
				.unlockedBy("has_prospit_core", has(PROSPIT_CORE)).save(recipeSaver);
		
		//PROSPIT_CORE_STAIRS
		CommonRecipes.stairsRecipe(PROSPIT_CORE_STAIRS, PROSPIT_CORE).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_CORE), RecipeCategory.BUILDING_BLOCKS, PROSPIT_CORE_STAIRS)
				.unlockedBy("has_prospit_core", has(PROSPIT_CORE)).save(recipeSaver, Minestuck.id("prospit_core_stairs_from_stonecutting"));
		
		//PROSPIT_CORE_SLAB
		CommonRecipes.slabRecipe(PROSPIT_CORE_SLAB, PROSPIT_CORE).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_CORE), RecipeCategory.BUILDING_BLOCKS, PROSPIT_CORE_SLAB, 2)
				.unlockedBy("has_prospit_core", has(PROSPIT_CORE)).save(recipeSaver, Minestuck.id("prospit_core_slab_from_stonecutting"));
		
		//PROSPIT_CORE_WALL
		CommonRecipes.wallRecipe(PROSPIT_CORE_WALL, PROSPIT_CORE).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_CORE), RecipeCategory.BUILDING_BLOCKS, PROSPIT_CORE_WALL)
				.unlockedBy("has_prospit_core", has(PROSPIT_CORE)).save(recipeSaver, Minestuck.id("prospit_core_wall_from_stonecutting"));
		
		//PROSPIT_ROUGH_CORE - no recipe
		
		//PROSPIT_ROUGH_CORE_STAIRS
		CommonRecipes.stairsRecipe(PROSPIT_ROUGH_CORE_STAIRS, PROSPIT_ROUGH_CORE).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_ROUGH_CORE), RecipeCategory.BUILDING_BLOCKS, PROSPIT_ROUGH_CORE_STAIRS)
				.unlockedBy("has_prospit_rough_core", has(PROSPIT_ROUGH_CORE)).save(recipeSaver, Minestuck.id("prospit_rough_core_stairs_from_stonecutting"));
		
		//PROSPIT_ROUGH_CORE_SLAB
		CommonRecipes.slabRecipe(PROSPIT_ROUGH_CORE_SLAB, PROSPIT_ROUGH_CORE).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_ROUGH_CORE), RecipeCategory.BUILDING_BLOCKS, PROSPIT_ROUGH_CORE_SLAB, 2)
				.unlockedBy("has_prospit_core", has(PROSPIT_ROUGH_CORE)).save(recipeSaver, Minestuck.id("prospit_rough_core_slab_from_stonecutting"));
		
		//PROSPIT_ROUGH_CORE_WALL
		CommonRecipes.wallRecipe(PROSPIT_ROUGH_CORE_WALL, PROSPIT_ROUGH_CORE).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_ROUGH_CORE), RecipeCategory.BUILDING_BLOCKS, PROSPIT_ROUGH_CORE_WALL)
				.unlockedBy("has_prospit_core", has(PROSPIT_ROUGH_CORE)).save(recipeSaver, Minestuck.id("prospit_rough_core_wall_from_stonecutting"));
		
		//PROSPIT_REFINED_CORE
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, PROSPIT_REFINED_CORE)
				.define('#', PROSPIT_ROUGH_CORE)
				.pattern("##")
				.pattern("##")
				.unlockedBy("has_prospit_rough_core", has(PROSPIT_ROUGH_CORE));
		
		//PROSPIT_REFINED_CORE_STAIRS
		CommonRecipes.stairsRecipe(PROSPIT_REFINED_CORE_STAIRS, PROSPIT_REFINED_CORE).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_REFINED_CORE), RecipeCategory.BUILDING_BLOCKS, PROSPIT_REFINED_CORE_STAIRS)
				.unlockedBy("has_prospit_refined_core", has(PROSPIT_REFINED_CORE)).save(recipeSaver, Minestuck.id("prospit_refined_core_stairs_from_stonecutting"));
		
		//PROSPIT_REFINED_CORE_SLAB
		CommonRecipes.slabRecipe(PROSPIT_REFINED_CORE_SLAB, PROSPIT_REFINED_CORE).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_REFINED_CORE), RecipeCategory.BUILDING_BLOCKS, PROSPIT_REFINED_CORE_SLAB, 2)
				.unlockedBy("has_prospit_refined_core", has(PROSPIT_REFINED_CORE)).save(recipeSaver, Minestuck.id("prospit_refined_core_slab_from_stonecutting"));
		
		//PROSPIT_REFINED_CORE_WALL
		CommonRecipes.wallRecipe(PROSPIT_REFINED_CORE_WALL, PROSPIT_REFINED_CORE).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_REFINED_CORE), RecipeCategory.BUILDING_BLOCKS, PROSPIT_REFINED_CORE_WALL)
				.unlockedBy("has_prospit_refined_core", has(PROSPIT_REFINED_CORE)).save(recipeSaver, Minestuck.id("prospit_refined_core_wall_from_stonecutting"));
		
		//PROSPIT_FERROSTRATA_CRUXITE_ORE - no recipe
		//PROSPIT_FERROSTRATA_URANIUM_ORE - no recipe
		//PROSPIT_CORE_CRUXITE_ORE - no recipe
		//PROSPIT_CORE_URANIUM_ORE - no recipe
		//PROSPIT_CABLE
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, PROSPIT_CABLE)
				.define('#', PROSPIT_DROSS)
				.pattern("##")
				.pattern("##")
				.unlockedBy("has_prospit_dross", has(PROSPIT_DROSS));
		
		//PROSPIT_THICK_CABLE
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, PROSPIT_THICK_CABLE)
				.define('#', PROSPIT_CABLE)
				.pattern("##")
				.pattern("##")
				.unlockedBy("has_prospit_cable", has(PROSPIT_CABLE));
		
		//PROSPIT_PILLAR
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, PROSPIT_PILLAR, 2)
				.define('#', PROSPIT_ROUGH_FERROSTRATA)
				.pattern("#")
				.pattern("#")
				.unlockedBy("has_prospit_rough_ferrostrata", has(PROSPIT_ROUGH_FERROSTRATA));
		
		//PROSPIT_DOOR
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, PROSPIT_DOOR, 3)
				.define('#', PROSPIT_REFINED_FERROSTRATA)
				.pattern("##")
				.pattern("##")
				.pattern("##")
				.unlockedBy("has_prospit_refined_ferrostrata", has(PROSPIT_REFINED_FERROSTRATA));
		
		//PROSPIT_TRAPDOOR
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, PROSPIT_TRAPDOOR, 2)
				.define('#', PROSPIT_REFINED_FERROSTRATA)
				.pattern("###")
				.pattern("###")
				.unlockedBy("has_prospit_refined_ferrostrata", has(PROSPIT_REFINED_FERROSTRATA));
		
		//PROSPIT_BRICK
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, PROSPIT_BRICK, 4)
				.define('#', PROSPIT_ROUGH_FERROSTRATA)
				.pattern("##")
				.pattern("##")
				.unlockedBy("has_prospit_rough_ferrostrata", has(PROSPIT_ROUGH_FERROSTRATA));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_ROUGH_FERROSTRATA), RecipeCategory.BUILDING_BLOCKS, PROSPIT_BRICK)
				.unlockedBy("has_prospit_rough_ferrostrata", has(PROSPIT_ROUGH_FERROSTRATA)).save(recipeSaver, Minestuck.id("prospit_brick_from_stonecutting"));
		
		//PROSPIT_CRACKED_BRICK
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(PROSPIT_BRICK), RecipeCategory.BUILDING_BLOCKS, PROSPIT_CRACKED_BRICK, 0.1f, 200)
				.unlockedBy("has_prospit_brick", has(PROSPIT_BRICK)).save(recipeSaver);
		
		//PROSPIT_CHISELED_BRICK
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, PROSPIT_CHISELED_BRICK)
				.define('#', PROSPIT_BRICK_SLAB)
				.pattern("#")
				.pattern("#")
				.unlockedBy("has_prospit_brick", has(PROSPIT_BRICK));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_FERROSTRATA), RecipeCategory.BUILDING_BLOCKS, PROSPIT_CHISELED_BRICK)
				.unlockedBy("has_prospit_ferrostrata", has(PROSPIT_FERROSTRATA)).save(recipeSaver, Minestuck.id("prospit_chiseled_brick_from_raw_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_BRICK), RecipeCategory.BUILDING_BLOCKS, PROSPIT_CHISELED_BRICK)
				.unlockedBy("has_prospit_brick", has(PROSPIT_BRICK)).save(recipeSaver, Minestuck.id("prospit_chiseled_brick_from_stonecutting"));
				
		//PROSPIT_PAVING_BRICK
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_FERROSTRATA), RecipeCategory.BUILDING_BLOCKS, PROSPIT_PAVING_BRICK)
				.unlockedBy("has_prospit_ferrostrata", has(PROSPIT_FERROSTRATA)).save(recipeSaver, Minestuck.id("prospit_paving_brick_from_raw_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_BRICK), RecipeCategory.BUILDING_BLOCKS, PROSPIT_PAVING_BRICK)
				.unlockedBy("has_prospit_brick", has(PROSPIT_BRICK)).save(recipeSaver, Minestuck.id("prospit_paving_brick_from_stonecutting"));
		
		//PROSPIT_FANCY_BRICK
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_FERROSTRATA), RecipeCategory.BUILDING_BLOCKS, PROSPIT_FANCY_BRICK)
				.unlockedBy("has_prospit_ferrostrata", has(PROSPIT_FERROSTRATA)).save(recipeSaver, Minestuck.id("prospit_fancy_brick_from_raw_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_BRICK), RecipeCategory.BUILDING_BLOCKS, PROSPIT_FANCY_BRICK)
				.unlockedBy("has_prospit_brick", has(PROSPIT_BRICK)).save(recipeSaver, Minestuck.id("prospit_fancy_brick_from_stonecutting"));
		
		//PROSPIT_TARNISHED_BRICK
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, PROSPIT_TARNISHED_BRICK, 1)
				.requires(PROSPIT_BRICK)
				.requires(PROSPIT_DROSS)
				.unlockedBy("has_prospit_brick", has(PROSPIT_BRICK))
				.unlockedBy("has_prospit_dross", has(PROSPIT_DROSS))
				.save(recipeSaver, Minestuck.id("prospit_tarnished_brick_shapeless_recipe"));
				
		//PROSPIT_STACKED_BRICK
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_FERROSTRATA), RecipeCategory.BUILDING_BLOCKS, PROSPIT_STACKED_BRICK)
				.unlockedBy("has_prospit_ferrostrata", has(PROSPIT_FERROSTRATA)).save(recipeSaver, Minestuck.id("prospit_stacked_brick_from_raw_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_BRICK), RecipeCategory.BUILDING_BLOCKS, PROSPIT_STACKED_BRICK)
				.unlockedBy("has_prospit_brick", has(PROSPIT_BRICK)).save(recipeSaver, Minestuck.id("prospit_stacked_brick_from_stonecutting"));
		
		//PROSPIT_TILES
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_FERROSTRATA), RecipeCategory.BUILDING_BLOCKS, PROSPIT_TILES)
				.unlockedBy("has_prospit_ferrostrata", has(PROSPIT_FERROSTRATA)).save(recipeSaver, Minestuck.id("prospit_tiles_from_raw_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_BRICK), RecipeCategory.BUILDING_BLOCKS, PROSPIT_TILES)
				.unlockedBy("has_prospit_brick", has(PROSPIT_BRICK)).save(recipeSaver, Minestuck.id("prospit_tiles_from_stonecutting"));
		
		//PROSPIT_BRICK_STAIRS
		CommonRecipes.stairsRecipe(PROSPIT_BRICK_STAIRS, PROSPIT_BRICK).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_BRICK), RecipeCategory.BUILDING_BLOCKS, PROSPIT_BRICK_STAIRS)
				.unlockedBy("has_prospit_brick", has(PROSPIT_BRICK)).save(recipeSaver, Minestuck.id("prospit_brick_stairs_from_stonecutting"));
		
		//PROSPIT_BRICK_SLAB
		CommonRecipes.slabRecipe(PROSPIT_BRICK_SLAB, PROSPIT_BRICK).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_BRICK), RecipeCategory.BUILDING_BLOCKS, PROSPIT_BRICK_SLAB, 2)
				.unlockedBy("has_prospit_brick", has(PROSPIT_BRICK)).save(recipeSaver, Minestuck.id("prospit_brick_slab_from_stonecutting"));
		
		//PROSPIT_BRICK_WALL
		CommonRecipes.wallRecipe(PROSPIT_BRICK_WALL, PROSPIT_BRICK).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_BRICK), RecipeCategory.BUILDING_BLOCKS, PROSPIT_BRICK_WALL)
				.unlockedBy("has_prospit_brick", has(PROSPIT_BRICK)).save(recipeSaver, Minestuck.id("prospit_brick_wall_from_stonecutting"));
		
		//PROSPIT_CRACKED_BRICK_STAIRS
		CommonRecipes.stairsRecipe(PROSPIT_CRACKED_BRICK_STAIRS, PROSPIT_CRACKED_BRICK).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_CRACKED_BRICK), RecipeCategory.BUILDING_BLOCKS, PROSPIT_CRACKED_BRICK_STAIRS)
				.unlockedBy("has_prospit_brick", has(PROSPIT_CRACKED_BRICK)).save(recipeSaver, Minestuck.id("prospit_cracked_brick_stairs_from_stonecutting"));
		
		//PROSPIT_CRACKED_BRICK_SLAB
		CommonRecipes.slabRecipe(PROSPIT_CRACKED_BRICK_SLAB, PROSPIT_CRACKED_BRICK).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_CRACKED_BRICK), RecipeCategory.BUILDING_BLOCKS, PROSPIT_CRACKED_BRICK_SLAB, 2)
				.unlockedBy("has_prospit_brick", has(PROSPIT_CRACKED_BRICK)).save(recipeSaver, Minestuck.id("prospit_cracked_brick_slab_from_stonecutting"));
		
		//PROSPIT_CRACKED_BRICK_WALL
		CommonRecipes.wallRecipe(PROSPIT_CRACKED_BRICK_WALL, PROSPIT_CRACKED_BRICK).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_CRACKED_BRICK), RecipeCategory.BUILDING_BLOCKS, PROSPIT_CRACKED_BRICK_WALL)
				.unlockedBy("has_prospit_brick", has(PROSPIT_CRACKED_BRICK)).save(recipeSaver, Minestuck.id("prospit_cracked_brick_wall_from_stonecutting"));
		
		//PROSPIT_TARNISHED_BRICK_STAIRS
		CommonRecipes.stairsRecipe(PROSPIT_TARNISHED_BRICK_STAIRS, PROSPIT_TARNISHED_BRICK).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_TARNISHED_BRICK), RecipeCategory.BUILDING_BLOCKS, PROSPIT_TARNISHED_BRICK_STAIRS)
				.unlockedBy("has_prospit_brick", has(PROSPIT_TARNISHED_BRICK)).save(recipeSaver, Minestuck.id("prospit_tarnished_brick_stairs_from_stonecutting"));
		
		//PROSPIT_TARNISHED_BRICK_SLAB
		CommonRecipes.slabRecipe(PROSPIT_TARNISHED_BRICK_SLAB, PROSPIT_TARNISHED_BRICK).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_TARNISHED_BRICK), RecipeCategory.BUILDING_BLOCKS, PROSPIT_TARNISHED_BRICK_SLAB, 2)
				.unlockedBy("has_prospit_brick", has(PROSPIT_TARNISHED_BRICK)).save(recipeSaver, Minestuck.id("prospit_tarnished_brick_slab_from_stonecutting"));
		
		//PROSPIT_TARNISHED_BRICK_WALL
		CommonRecipes.wallRecipe(PROSPIT_TARNISHED_BRICK_WALL, PROSPIT_TARNISHED_BRICK).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_TARNISHED_BRICK), RecipeCategory.BUILDING_BLOCKS, PROSPIT_TARNISHED_BRICK_WALL)
				.unlockedBy("has_prospit_brick", has(PROSPIT_TARNISHED_BRICK)).save(recipeSaver, Minestuck.id("prospit_tarnished_brick_wall_from_stonecutting"));
		
		//PROSPIT_CORE_BRICK
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, PROSPIT_CORE_BRICK, 4)
				.define('#', PROSPIT_REFINED_CORE)
				.pattern("##")
				.pattern("##")
				.unlockedBy("has_prospit_refined_core", has(PROSPIT_REFINED_CORE));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_REFINED_CORE), RecipeCategory.BUILDING_BLOCKS, PROSPIT_CORE_BRICK)
				.unlockedBy("has_prospit_refined_core", has(PROSPIT_REFINED_CORE)).save(recipeSaver, Minestuck.id("prospit_core_brick_from_stonecutting"));
		
		//PROSPIT_CRACKED_CORE_BRICK
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(PROSPIT_CORE_BRICK), RecipeCategory.BUILDING_BLOCKS, PROSPIT_CRACKED_CORE_BRICK, 0.1f, 200)
				.unlockedBy("has_prospit_core_brick", has(PROSPIT_CORE_BRICK)).save(recipeSaver);
		
		//PROSPIT_CORE_TILES
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, PROSPIT_CORE_TILES, 4)
				.define('#', PROSPIT_CORE_BRICK)
				.pattern("##")
				.pattern("##")
				.unlockedBy("has_prospit_refined_core", has(PROSPIT_CORE_BRICK));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_CORE_BRICK), RecipeCategory.BUILDING_BLOCKS, PROSPIT_CORE_TILES)
				.unlockedBy("has_prospit_refined_core", has(PROSPIT_CORE_BRICK)).save(recipeSaver, Minestuck.id("prospit_core_tiles_stonecutting"));
				
		//PROSPIT_CRACKED_CORE_TILES
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(PROSPIT_CORE_TILES), RecipeCategory.BUILDING_BLOCKS, PROSPIT_CRACKED_CORE_TILES, 0.1f, 200)
				.unlockedBy("has_prospit_core_brick", has(PROSPIT_CORE_BRICK)).save(recipeSaver);
		
		//PROSPIT_CHISELED_CORE
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, PROSPIT_CHISELED_CORE)
				.define('#', PROSPIT_CORE_BRICK_SLAB)
				.pattern("#")
				.pattern("#")
				.unlockedBy("has_prospit_core_brick", has(PROSPIT_CORE_BRICK));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_CORE), RecipeCategory.BUILDING_BLOCKS, PROSPIT_CHISELED_CORE)
				.unlockedBy("has_prospit_core", has(PROSPIT_CORE)).save(recipeSaver, Minestuck.id("prospit_chiseled_core_from_raw_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_CORE_BRICK), RecipeCategory.BUILDING_BLOCKS, PROSPIT_CHISELED_CORE)
				.unlockedBy("has_prospit_core_brick", has(PROSPIT_CORE_BRICK)).save(recipeSaver, Minestuck.id("prospit_chiseled_core_from_stonecutting"));
		
		//PROSPIT_CORE_BRICK_STAIRS
		CommonRecipes.stairsRecipe(PROSPIT_CORE_BRICK_STAIRS, PROSPIT_CORE_BRICK).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_CORE_BRICK), RecipeCategory.BUILDING_BLOCKS, PROSPIT_CORE_BRICK_STAIRS)
				.unlockedBy("has_prospit_core_brick", has(PROSPIT_CORE_BRICK)).save(recipeSaver, Minestuck.id("prospit_core_brick_stairs_from_stonecutting"));
		
		//PROSPIT_CORE_BRICK_SLAB
		CommonRecipes.slabRecipe(PROSPIT_CORE_BRICK_SLAB, PROSPIT_CORE_BRICK).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_CORE_BRICK), RecipeCategory.BUILDING_BLOCKS, PROSPIT_CORE_BRICK_SLAB, 2)
				.unlockedBy("has_prospit_core_brick", has(PROSPIT_CORE_BRICK)).save(recipeSaver, Minestuck.id("prospit_core_brick_slab_from_stonecutting"));
		
		//PROSPIT_CORE_BRICK_WALL
		CommonRecipes.wallRecipe(PROSPIT_CORE_BRICK_WALL, PROSPIT_CORE_BRICK).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_CORE_BRICK), RecipeCategory.BUILDING_BLOCKS, PROSPIT_CORE_BRICK_WALL)
				.unlockedBy("has_prospit_core_brick", has(PROSPIT_CORE_BRICK)).save(recipeSaver, Minestuck.id("prospit_core_brick_wall_from_stonecutting"));
		
		//PROSPIT_CRACKED_CORE_BRICK_STAIRS
		CommonRecipes.stairsRecipe(PROSPIT_CRACKED_CORE_BRICK_STAIRS, PROSPIT_CRACKED_CORE_BRICK).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_CRACKED_CORE_BRICK), RecipeCategory.BUILDING_BLOCKS, PROSPIT_CRACKED_CORE_BRICK_STAIRS)
				.unlockedBy("has_prospit_cracked_core_brick", has(PROSPIT_CRACKED_CORE_BRICK)).save(recipeSaver, Minestuck.id("prospit_cracked_core_brick_stairs_from_stonecutting"));
		
		//PROSPIT_CRACKED_CORE_BRICK_SLAB
		CommonRecipes.slabRecipe(PROSPIT_CRACKED_CORE_BRICK_SLAB, PROSPIT_CRACKED_CORE_BRICK).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_CRACKED_CORE_BRICK), RecipeCategory.BUILDING_BLOCKS, PROSPIT_CRACKED_CORE_BRICK_SLAB, 2)
				.unlockedBy("has_prospit_brick", has(PROSPIT_CRACKED_CORE_BRICK)).save(recipeSaver, Minestuck.id("prospit_cracked_core_brick_slab_from_stonecutting"));
		
		//PROSPIT_CRACKED_CORE_BRICK_WALL
		CommonRecipes.stairsRecipe(PROSPIT_CRACKED_CORE_BRICK_WALL, PROSPIT_CRACKED_CORE_BRICK).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_CRACKED_CORE_BRICK), RecipeCategory.BUILDING_BLOCKS, PROSPIT_CRACKED_CORE_BRICK_WALL)
				.unlockedBy("has_prospit_brick", has(PROSPIT_CRACKED_CORE_BRICK)).save(recipeSaver, Minestuck.id("prospit_cracked_core_brick_wall_from_stonecutting"));
		
		//PROSPIT_CORE_TILE_STAIRS
		CommonRecipes.stairsRecipe(PROSPIT_CORE_TILE_STAIRS, PROSPIT_CORE_TILES).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_CORE_TILES), RecipeCategory.BUILDING_BLOCKS, PROSPIT_CORE_TILE_STAIRS)
				.unlockedBy("has_prospit_brick", has(PROSPIT_CORE_TILES)).save(recipeSaver, Minestuck.id("prospit_core_tile_stairs_from_stonecutting"));
		
		//PROSPIT_CORE_TILE_SLAB
		CommonRecipes.slabRecipe(PROSPIT_CORE_TILE_SLAB, PROSPIT_CORE_TILES).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_CORE_TILES), RecipeCategory.BUILDING_BLOCKS, PROSPIT_CORE_TILE_SLAB, 2)
				.unlockedBy("has_prospit_brick", has(PROSPIT_CORE_TILES)).save(recipeSaver, Minestuck.id("prospit_core_tile_slab_from_stonecutting"));
		
		//PROSPIT_CORE_TILE_WALL
		CommonRecipes.wallRecipe(PROSPIT_CORE_TILE_WALL, PROSPIT_CORE_TILES).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(PROSPIT_CORE_TILES), RecipeCategory.BUILDING_BLOCKS, PROSPIT_CORE_TILE_WALL)
				.unlockedBy("has_prospit_brick", has(PROSPIT_CORE_TILES)).save(recipeSaver, Minestuck.id("prospit_core_tile_wall_from_stonecutting"));
		
		//DERSE_PLATING - no recipe
		//DERSE_PLATED_PATH
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_PLATING), RecipeCategory.BUILDING_BLOCKS, DERSE_PLATED_PATH)
				.unlockedBy("has_derse_plating", has(DERSE_PLATING)).save(recipeSaver, Minestuck.id("derse_plated_path_from_stonecutting"));
		
		//DERSE_METALLITH
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(DERSE_ROUGH_METALLITH), RecipeCategory.BUILDING_BLOCKS, DERSE_METALLITH, 0.1f, 200)
				.unlockedBy("has_derse_metallith", has(DERSE_METALLITH)).save(recipeSaver);
		
		//DERSE_METALLITH_STAIRS
		CommonRecipes.stairsRecipe(DERSE_METALLITH_STAIRS, DERSE_METALLITH).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_METALLITH), RecipeCategory.BUILDING_BLOCKS, DERSE_METALLITH_STAIRS)
				.unlockedBy("has_derse_metallith", has(DERSE_METALLITH)).save(recipeSaver, Minestuck.id("derse_metallith_stairs_from_stonecutting"));
		
		//DERSE_METALLITH_SLAB
		CommonRecipes.slabRecipe(DERSE_METALLITH_SLAB, DERSE_METALLITH).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_METALLITH), RecipeCategory.BUILDING_BLOCKS, DERSE_METALLITH_SLAB, 2)
				.unlockedBy("has_derse_metallith", has(DERSE_METALLITH)).save(recipeSaver, Minestuck.id("derse_metallith_slab_from_stonecutting"));
		
		//DERSE_METALLITH_WALL
		CommonRecipes.wallRecipe(DERSE_METALLITH_WALL, DERSE_METALLITH).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_METALLITH), RecipeCategory.BUILDING_BLOCKS, DERSE_METALLITH_WALL)
				.unlockedBy("has_derse_metallith", has(DERSE_METALLITH)).save(recipeSaver, Minestuck.id("derse_metallith_wall_from_stonecutting"));
		
		//DERSE_METALLITH_BUTTON
		CommonRecipes.buttonRecipe(DERSE_METALLITH_BUTTON, DERSE_METALLITH).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_METALLITH), RecipeCategory.BUILDING_BLOCKS, DERSE_METALLITH_BUTTON)
				.unlockedBy("has_derse_metallith", has(DERSE_METALLITH)).save(recipeSaver, Minestuck.id("derse_metallith_button_from_stonecutting"));
		
		//DERSE_METALLITH_PRESSURE_PLATE
		CommonRecipes.buttonRecipe(DERSE_METALLITH_PRESSURE_PLATE, DERSE_METALLITH).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_METALLITH), RecipeCategory.BUILDING_BLOCKS, DERSE_METALLITH_PRESSURE_PLATE)
				.unlockedBy("has_derse_metallith", has(DERSE_METALLITH)).save(recipeSaver, Minestuck.id("derse_metallith_pressure_plate_from_stonecutting"));
		
		//DERSE_ROUGH_METALLITH - no recipe
		
		//DERSE_ROUGH_METALLITH_STAIRS
		CommonRecipes.stairsRecipe(DERSE_ROUGH_METALLITH_STAIRS, DERSE_ROUGH_METALLITH).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_ROUGH_METALLITH), RecipeCategory.BUILDING_BLOCKS, DERSE_ROUGH_METALLITH_STAIRS)
				.unlockedBy("has_derse_rough_metallith", has(DERSE_ROUGH_METALLITH)).save(recipeSaver, Minestuck.id("derse_rough_metallith_stairs_from_stonecutting"));
		
		//DERSE_ROUGH_METALLITH_SLAB
		CommonRecipes.slabRecipe(DERSE_ROUGH_METALLITH_SLAB, DERSE_ROUGH_METALLITH).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_ROUGH_METALLITH), RecipeCategory.BUILDING_BLOCKS, DERSE_ROUGH_METALLITH_SLAB, 2)
				.unlockedBy("has_derse_metallith", has(DERSE_ROUGH_METALLITH)).save(recipeSaver, Minestuck.id("derse_rough_metallith_slab_from_stonecutting"));
		
		//DERSE_ROUGH_METALLITH_WALL
		CommonRecipes.wallRecipe(DERSE_ROUGH_METALLITH_WALL, DERSE_ROUGH_METALLITH).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_ROUGH_METALLITH), RecipeCategory.BUILDING_BLOCKS, DERSE_ROUGH_METALLITH_WALL)
				.unlockedBy("has_derse_metallith", has(DERSE_ROUGH_METALLITH)).save(recipeSaver, Minestuck.id("derse_rough_metallith_wall_from_stonecutting"));
		
		//DERSE_REFINED_METALLITH
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(DERSE_METALLITH), RecipeCategory.BUILDING_BLOCKS, DERSE_REFINED_METALLITH, 0.1f, 200)
				.unlockedBy("has_derse_rough_metallith", has(DERSE_ROUGH_METALLITH)).save(recipeSaver);
		
		//DERSE_REFINED_METALLITH_STAIRS
		CommonRecipes.stairsRecipe(DERSE_REFINED_METALLITH_STAIRS, DERSE_REFINED_METALLITH).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_REFINED_METALLITH), RecipeCategory.BUILDING_BLOCKS, DERSE_REFINED_METALLITH_STAIRS)
				.unlockedBy("has_derse_refined_metallith", has(DERSE_REFINED_METALLITH)).save(recipeSaver, Minestuck.id("derse_refined_metallith_stairs_from_stonecutting"));
		
		//DERSE_REFINED_METALLITH_SLAB
		CommonRecipes.stairsRecipe(DERSE_REFINED_METALLITH_SLAB, DERSE_REFINED_METALLITH).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_REFINED_METALLITH), RecipeCategory.BUILDING_BLOCKS, DERSE_REFINED_METALLITH_SLAB, 2)
				.unlockedBy("has_derse_refined_metallith", has(DERSE_REFINED_METALLITH)).save(recipeSaver, Minestuck.id("derse_refined_metallith_slab_from_stonecutting"));
		
		//DERSE_REFINED_METALLITH_WALL
		CommonRecipes.stairsRecipe(DERSE_REFINED_METALLITH_WALL, DERSE_REFINED_METALLITH).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_REFINED_METALLITH), RecipeCategory.BUILDING_BLOCKS, DERSE_REFINED_METALLITH_WALL)
				.unlockedBy("has_derse_refined_metallith", has(DERSE_REFINED_METALLITH)).save(recipeSaver, Minestuck.id("derse_refined_metallith_wall_from_stonecutting"));
		
		//DERSE_CORE
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(DERSE_ROUGH_CORE), RecipeCategory.BUILDING_BLOCKS, DERSE_CORE, 0.1f, 200)
				.unlockedBy("has_derse_core", has(DERSE_CORE)).save(recipeSaver);
		
		//DERSE_CORE_STAIRS
		CommonRecipes.stairsRecipe(DERSE_CORE_STAIRS, DERSE_CORE).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_CORE), RecipeCategory.BUILDING_BLOCKS, DERSE_CORE_STAIRS)
				.unlockedBy("has_derse_core", has(DERSE_CORE)).save(recipeSaver, Minestuck.id("derse_core_stairs_from_stonecutting"));
		
		//DERSE_CORE_SLAB
		CommonRecipes.slabRecipe(DERSE_CORE_SLAB, DERSE_CORE).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_CORE), RecipeCategory.BUILDING_BLOCKS, DERSE_CORE_SLAB, 2)
				.unlockedBy("has_derse_core", has(DERSE_CORE)).save(recipeSaver, Minestuck.id("derse_core_slab_from_stonecutting"));
		
		//DERSE_CORE_WALL
		CommonRecipes.wallRecipe(DERSE_CORE_WALL, DERSE_CORE).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_CORE), RecipeCategory.BUILDING_BLOCKS, DERSE_CORE_WALL)
				.unlockedBy("has_derse_core", has(DERSE_CORE)).save(recipeSaver, Minestuck.id("derse_core_wall_from_stonecutting"));
		
		//DERSE_ROUGH_CORE - no recipe
		
		//DERSE_ROUGH_CORE_STAIRS
		CommonRecipes.stairsRecipe(DERSE_ROUGH_CORE_STAIRS, DERSE_ROUGH_CORE).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_ROUGH_CORE), RecipeCategory.BUILDING_BLOCKS, DERSE_ROUGH_CORE_STAIRS)
				.unlockedBy("has_derse_rough_core", has(DERSE_ROUGH_CORE)).save(recipeSaver, Minestuck.id("derse_rough_core_stairs_from_stonecutting"));
		
		//DERSE_ROUGH_CORE_SLAB
		CommonRecipes.slabRecipe(DERSE_ROUGH_CORE_SLAB, DERSE_ROUGH_CORE).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_ROUGH_CORE), RecipeCategory.BUILDING_BLOCKS, DERSE_ROUGH_CORE_SLAB, 2)
				.unlockedBy("has_derse_core", has(DERSE_ROUGH_CORE)).save(recipeSaver, Minestuck.id("derse_rough_core_slab_from_stonecutting"));
		
		//DERSE_ROUGH_CORE_WALL
		CommonRecipes.wallRecipe(DERSE_ROUGH_CORE_WALL, DERSE_ROUGH_CORE).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_ROUGH_CORE), RecipeCategory.BUILDING_BLOCKS, DERSE_ROUGH_CORE_WALL)
				.unlockedBy("has_derse_core", has(DERSE_ROUGH_CORE)).save(recipeSaver, Minestuck.id("derse_rough_core_wall_from_stonecutting"));
		
		//DERSE_REFINED_CORE
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, DERSE_REFINED_CORE)
				.define('#', DERSE_ROUGH_CORE)
				.pattern("##")
				.pattern("##")
				.unlockedBy("has_derse_rough_core", has(DERSE_ROUGH_CORE));
		
		//DERSE_REFINED_CORE_STAIRS
		CommonRecipes.stairsRecipe(DERSE_REFINED_CORE_STAIRS, DERSE_REFINED_CORE).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_REFINED_CORE), RecipeCategory.BUILDING_BLOCKS, DERSE_REFINED_CORE_STAIRS)
				.unlockedBy("has_derse_refined_core", has(DERSE_REFINED_CORE)).save(recipeSaver, Minestuck.id("derse_refined_core_stairs_from_stonecutting"));
		
		//DERSE_REFINED_CORE_SLAB
		CommonRecipes.slabRecipe(DERSE_REFINED_CORE_SLAB, DERSE_REFINED_CORE).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_REFINED_CORE), RecipeCategory.BUILDING_BLOCKS, DERSE_REFINED_CORE_SLAB, 2)
				.unlockedBy("has_derse_refined_core", has(DERSE_REFINED_CORE)).save(recipeSaver, Minestuck.id("derse_refined_core_slab_from_stonecutting"));
		
		//DERSE_REFINED_CORE_WALL
		CommonRecipes.wallRecipe(DERSE_REFINED_CORE_WALL, DERSE_REFINED_CORE).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_REFINED_CORE), RecipeCategory.BUILDING_BLOCKS, DERSE_REFINED_CORE_WALL)
				.unlockedBy("has_derse_refined_core", has(DERSE_REFINED_CORE)).save(recipeSaver, Minestuck.id("derse_refined_core_wall_from_stonecutting"));
		
		//DERSE_METALLITH_CRUXITE_ORE - no recipe
		//DERSE_METALLITH_URANIUM_ORE - no recipe
		//DERSE_CORE_CRUXITE_ORE - no recipe
		//DERSE_CORE_URANIUM_ORE - no recipe
		//DERSE_CABLE
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, DERSE_CABLE)
				.define('#', DERSE_RESIDUE)
				.pattern("##")
				.pattern("##")
				.unlockedBy("has_derse_dross", has(DERSE_RESIDUE));
		
		//DERSE_THICK_CABLE
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, DERSE_THICK_CABLE)
				.define('#', DERSE_CABLE)
				.pattern("##")
				.pattern("##")
				.unlockedBy("has_derse_cable", has(DERSE_CABLE));
		
		//DERSE_PILLAR
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, DERSE_PILLAR, 2)
				.define('#', DERSE_ROUGH_METALLITH)
				.pattern("#")
				.pattern("#")
				.unlockedBy("has_derse_rough_metallith", has(DERSE_ROUGH_METALLITH));
		
		//DERSE_DOOR
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, DERSE_DOOR, 3)
				.define('#', DERSE_REFINED_METALLITH)
				.pattern("##")
				.pattern("##")
				.pattern("##")
				.unlockedBy("has_derse_refined_metallith", has(DERSE_REFINED_METALLITH));
		
		//DERSE_TRAPDOOR
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, DERSE_TRAPDOOR, 2)
				.define('#', DERSE_REFINED_METALLITH)
				.pattern("###")
				.pattern("###")
				.unlockedBy("has_derse_refined_metallith", has(DERSE_REFINED_METALLITH));
		
		//DERSE_BRICK
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, DERSE_BRICK, 4)
				.define('#', DERSE_ROUGH_METALLITH)
				.pattern("##")
				.pattern("##")
				.unlockedBy("has_derse_rough_metallith", has(DERSE_ROUGH_METALLITH));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_ROUGH_METALLITH), RecipeCategory.BUILDING_BLOCKS, DERSE_BRICK)
				.unlockedBy("has_derse_rough_metallith", has(DERSE_ROUGH_METALLITH)).save(recipeSaver, Minestuck.id("derse_brick_from_stonecutting"));
		
		//DERSE_CRACKED_BRICK
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(DERSE_BRICK), RecipeCategory.BUILDING_BLOCKS, DERSE_CRACKED_BRICK, 0.1f, 200)
				.unlockedBy("has_derse_brick", has(DERSE_BRICK)).save(recipeSaver);
		
		//DERSE_CHISELED_BRICK
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, DERSE_CHISELED_BRICK)
				.define('#', DERSE_BRICK_SLAB)
				.pattern("#")
				.pattern("#")
				.unlockedBy("has_derse_brick", has(DERSE_BRICK));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_METALLITH), RecipeCategory.BUILDING_BLOCKS, DERSE_CHISELED_BRICK)
				.unlockedBy("has_derse_metallith", has(DERSE_METALLITH)).save(recipeSaver, Minestuck.id("derse_chiseled_brick_from_raw_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_BRICK), RecipeCategory.BUILDING_BLOCKS, DERSE_CHISELED_BRICK)
				.unlockedBy("has_derse_brick", has(DERSE_BRICK)).save(recipeSaver, Minestuck.id("derse_chiseled_brick_from_stonecutting"));
		
		//DERSE_PAVING_BRICK
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_METALLITH), RecipeCategory.BUILDING_BLOCKS, DERSE_PAVING_BRICK)
				.unlockedBy("has_derse_metallith", has(DERSE_METALLITH)).save(recipeSaver, Minestuck.id("derse_paving_brick_from_raw_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_BRICK), RecipeCategory.BUILDING_BLOCKS, DERSE_PAVING_BRICK)
				.unlockedBy("has_derse_brick", has(DERSE_BRICK)).save(recipeSaver, Minestuck.id("derse_paving_brick_from_stonecutting"));
		
		//DERSE_FANCY_BRICK
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_METALLITH), RecipeCategory.BUILDING_BLOCKS, DERSE_FANCY_BRICK)
				.unlockedBy("has_derse_metallith", has(DERSE_METALLITH)).save(recipeSaver, Minestuck.id("derse_fancy_brick_from_raw_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_BRICK), RecipeCategory.BUILDING_BLOCKS, DERSE_FANCY_BRICK)
				.unlockedBy("has_derse_brick", has(DERSE_BRICK)).save(recipeSaver, Minestuck.id("derse_fancy_brick_from_stonecutting"));
		
		//DERSE_TARNISHED_BRICK
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, DERSE_TARNISHED_BRICK, 1)
				.requires(DERSE_BRICK)
				.requires(DERSE_RESIDUE)
				.unlockedBy("has_derse_brick", has(DERSE_BRICK))
				.unlockedBy("has_derse_dross", has(DERSE_RESIDUE))
				.save(recipeSaver, Minestuck.id("derse_tarnished_brick_shapeless_recipe"));
		
		//DERSE_STACKED_BRICK
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_METALLITH), RecipeCategory.BUILDING_BLOCKS, DERSE_STACKED_BRICK)
				.unlockedBy("has_derse_metallith", has(DERSE_METALLITH)).save(recipeSaver, Minestuck.id("derse_stacked_brick_from_raw_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_BRICK), RecipeCategory.BUILDING_BLOCKS, DERSE_STACKED_BRICK)
				.unlockedBy("has_derse_brick", has(DERSE_BRICK)).save(recipeSaver, Minestuck.id("derse_stacked_brick_from_stonecutting"));
		
		//DERSE_TILES
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_METALLITH), RecipeCategory.BUILDING_BLOCKS, DERSE_TILES)
				.unlockedBy("has_derse_metallith", has(DERSE_METALLITH)).save(recipeSaver, Minestuck.id("derse_tiles_from_raw_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_BRICK), RecipeCategory.BUILDING_BLOCKS, DERSE_TILES)
				.unlockedBy("has_derse_brick", has(DERSE_BRICK)).save(recipeSaver, Minestuck.id("derse_tiles_from_stonecutting"));
		
		//DERSE_BRICK_STAIRS
		CommonRecipes.stairsRecipe(DERSE_BRICK_STAIRS, DERSE_BRICK).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_BRICK), RecipeCategory.BUILDING_BLOCKS, DERSE_BRICK_STAIRS)
				.unlockedBy("has_derse_brick", has(DERSE_BRICK)).save(recipeSaver, Minestuck.id("derse_brick_stairs_from_stonecutting"));
		
		//DERSE_BRICK_SLAB
		CommonRecipes.slabRecipe(DERSE_BRICK_SLAB, DERSE_BRICK).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_BRICK), RecipeCategory.BUILDING_BLOCKS, DERSE_BRICK_SLAB, 2)
				.unlockedBy("has_derse_brick", has(DERSE_BRICK)).save(recipeSaver, Minestuck.id("derse_brick_slab_from_stonecutting"));
		
		//DERSE_BRICK_WALL
		CommonRecipes.wallRecipe(DERSE_BRICK_WALL, DERSE_BRICK).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_BRICK), RecipeCategory.BUILDING_BLOCKS, DERSE_BRICK_WALL)
				.unlockedBy("has_derse_brick", has(DERSE_BRICK)).save(recipeSaver, Minestuck.id("derse_brick_wall_from_stonecutting"));
		
		//DERSE_CRACKED_BRICK_STAIRS
		CommonRecipes.stairsRecipe(DERSE_CRACKED_BRICK_STAIRS, DERSE_CRACKED_BRICK).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_CRACKED_BRICK), RecipeCategory.BUILDING_BLOCKS, DERSE_CRACKED_BRICK_STAIRS)
				.unlockedBy("has_derse_brick", has(DERSE_CRACKED_BRICK)).save(recipeSaver, Minestuck.id("derse_cracked_brick_stairs_from_stonecutting"));
		
		//DERSE_CRACKED_BRICK_SLAB
		CommonRecipes.slabRecipe(DERSE_CRACKED_BRICK_SLAB, DERSE_CRACKED_BRICK).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_CRACKED_BRICK), RecipeCategory.BUILDING_BLOCKS, DERSE_CRACKED_BRICK_SLAB, 2)
				.unlockedBy("has_derse_brick", has(DERSE_CRACKED_BRICK)).save(recipeSaver, Minestuck.id("derse_cracked_brick_slab_from_stonecutting"));
		
		//DERSE_CRACKED_BRICK_WALL
		CommonRecipes.wallRecipe(DERSE_CRACKED_BRICK_WALL, DERSE_CRACKED_BRICK).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_CRACKED_BRICK), RecipeCategory.BUILDING_BLOCKS, DERSE_CRACKED_BRICK_WALL)
				.unlockedBy("has_derse_brick", has(DERSE_CRACKED_BRICK)).save(recipeSaver, Minestuck.id("derse_cracked_brick_wall_from_stonecutting"));
		
		//DERSE_TARNISHED_BRICK_STAIRS
		CommonRecipes.stairsRecipe(DERSE_TARNISHED_BRICK_STAIRS, DERSE_TARNISHED_BRICK).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_TARNISHED_BRICK), RecipeCategory.BUILDING_BLOCKS, DERSE_TARNISHED_BRICK_STAIRS)
				.unlockedBy("has_derse_brick", has(DERSE_TARNISHED_BRICK)).save(recipeSaver, Minestuck.id("derse_tarnished_brick_stairs_from_stonecutting"));
		
		//DERSE_TARNISHED_BRICK_SLAB
		CommonRecipes.slabRecipe(DERSE_TARNISHED_BRICK_SLAB, DERSE_TARNISHED_BRICK).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_TARNISHED_BRICK), RecipeCategory.BUILDING_BLOCKS, DERSE_TARNISHED_BRICK_SLAB, 2)
				.unlockedBy("has_derse_brick", has(DERSE_TARNISHED_BRICK)).save(recipeSaver, Minestuck.id("derse_tarnished_brick_slab_from_stonecutting"));
		
		//DERSE_TARNISHED_BRICK_WALL
		CommonRecipes.wallRecipe(DERSE_TARNISHED_BRICK_WALL, DERSE_TARNISHED_BRICK).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_TARNISHED_BRICK), RecipeCategory.BUILDING_BLOCKS, DERSE_TARNISHED_BRICK_WALL)
				.unlockedBy("has_derse_brick", has(DERSE_TARNISHED_BRICK)).save(recipeSaver, Minestuck.id("derse_tarnished_brick_wall_from_stonecutting"));
		
		//DERSE_CORE_BRICK
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, DERSE_CORE_BRICK, 4)
				.define('#', DERSE_REFINED_CORE)
				.pattern("##")
				.pattern("##")
				.unlockedBy("has_derse_refined_core", has(DERSE_REFINED_CORE));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_REFINED_CORE), RecipeCategory.BUILDING_BLOCKS, DERSE_CORE_BRICK)
				.unlockedBy("has_derse_refined_core", has(DERSE_REFINED_CORE)).save(recipeSaver, Minestuck.id("derse_core_brick_from_stonecutting"));
		
		//DERSE_CRACKED_CORE_BRICK
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(DERSE_CORE_BRICK), RecipeCategory.BUILDING_BLOCKS, DERSE_CRACKED_CORE_BRICK, 0.1f, 200)
				.unlockedBy("has_derse_core_brick", has(DERSE_CORE_BRICK)).save(recipeSaver);
		
		//DERSE_CORE_TILES
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, DERSE_CORE_TILES, 4)
				.define('#', DERSE_CORE_BRICK)
				.pattern("##")
				.pattern("##")
				.unlockedBy("has_derse_refined_core", has(DERSE_CORE_BRICK));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_CORE_BRICK), RecipeCategory.BUILDING_BLOCKS, DERSE_CORE_TILES)
				.unlockedBy("has_derse_refined_core", has(DERSE_CORE_BRICK)).save(recipeSaver, Minestuck.id("derse_core_tiles_stonecutting"));
		
		//DERSE_CRACKED_CORE_TILES
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(DERSE_CORE_TILES), RecipeCategory.BUILDING_BLOCKS, DERSE_CRACKED_CORE_TILES, 0.1f, 200)
				.unlockedBy("has_derse_core_brick", has(DERSE_CORE_BRICK)).save(recipeSaver);
		
		//DERSE_CHISELED_CORE
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, DERSE_CHISELED_CORE)
				.define('#', DERSE_CORE_BRICK_SLAB)
				.pattern("#")
				.pattern("#")
				.unlockedBy("has_derse_core_brick", has(DERSE_CORE_BRICK));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_CORE), RecipeCategory.BUILDING_BLOCKS, DERSE_CHISELED_CORE)
				.unlockedBy("has_derse_core", has(DERSE_CORE)).save(recipeSaver, Minestuck.id("derse_chiseled_core_from_raw_stonecutting"));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_CORE_BRICK), RecipeCategory.BUILDING_BLOCKS, DERSE_CHISELED_CORE)
				.unlockedBy("has_derse_core_brick", has(DERSE_CORE_BRICK)).save(recipeSaver, Minestuck.id("derse_chiseled_core_from_stonecutting"));
		
		//DERSE_CORE_BRICK_STAIRS
		CommonRecipes.stairsRecipe(DERSE_CORE_BRICK_STAIRS, DERSE_CORE_BRICK).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_CORE_BRICK), RecipeCategory.BUILDING_BLOCKS, DERSE_CORE_BRICK_STAIRS)
				.unlockedBy("has_derse_core_brick", has(DERSE_CORE_BRICK)).save(recipeSaver, Minestuck.id("derse_core_brick_stairs_from_stonecutting"));
		
		//DERSE_CORE_BRICK_SLAB
		CommonRecipes.slabRecipe(DERSE_CORE_BRICK_SLAB, DERSE_CORE_BRICK).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_CORE_BRICK), RecipeCategory.BUILDING_BLOCKS, DERSE_CORE_BRICK_SLAB, 2)
				.unlockedBy("has_derse_core_brick", has(DERSE_CORE_BRICK)).save(recipeSaver, Minestuck.id("derse_core_brick_slab_from_stonecutting"));
		
		//DERSE_CORE_BRICK_WALL
		CommonRecipes.wallRecipe(DERSE_CORE_BRICK_WALL, DERSE_CORE_BRICK).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_CORE_BRICK), RecipeCategory.BUILDING_BLOCKS, DERSE_CORE_BRICK_WALL)
				.unlockedBy("has_derse_core_brick", has(DERSE_CORE_BRICK)).save(recipeSaver, Minestuck.id("derse_core_brick_wall_from_stonecutting"));
		
		//DERSE_CRACKED_CORE_BRICK_STAIRS
		CommonRecipes.stairsRecipe(DERSE_CRACKED_CORE_BRICK_STAIRS, DERSE_CRACKED_CORE_BRICK).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_CRACKED_CORE_BRICK), RecipeCategory.BUILDING_BLOCKS, DERSE_CRACKED_CORE_BRICK_STAIRS)
				.unlockedBy("has_derse_cracked_core_brick", has(DERSE_CRACKED_CORE_BRICK)).save(recipeSaver, Minestuck.id("derse_cracked_core_brick_stairs_from_stonecutting"));
		
		//DERSE_CRACKED_CORE_BRICK_SLAB
		CommonRecipes.slabRecipe(DERSE_CRACKED_CORE_BRICK_SLAB, DERSE_CRACKED_CORE_BRICK).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_CRACKED_CORE_BRICK), RecipeCategory.BUILDING_BLOCKS, DERSE_CRACKED_CORE_BRICK_SLAB, 2)
				.unlockedBy("has_derse_brick", has(DERSE_CRACKED_CORE_BRICK)).save(recipeSaver, Minestuck.id("derse_cracked_core_brick_slab_from_stonecutting"));
		
		//DERSE_CRACKED_CORE_BRICK_WALL
		CommonRecipes.stairsRecipe(DERSE_CRACKED_CORE_BRICK_WALL, DERSE_CRACKED_CORE_BRICK).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_CRACKED_CORE_BRICK), RecipeCategory.BUILDING_BLOCKS, DERSE_CRACKED_CORE_BRICK_WALL)
				.unlockedBy("has_derse_brick", has(DERSE_CRACKED_CORE_BRICK)).save(recipeSaver, Minestuck.id("derse_cracked_core_brick_wall_from_stonecutting"));
		
		//DERSE_CORE_TILE_STAIRS
		CommonRecipes.stairsRecipe(DERSE_CORE_TILE_STAIRS, DERSE_CORE_TILES).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_CORE_TILES), RecipeCategory.BUILDING_BLOCKS, DERSE_CORE_TILE_STAIRS)
				.unlockedBy("has_derse_brick", has(DERSE_CORE_TILES)).save(recipeSaver, Minestuck.id("derse_core_tile_stairs_from_stonecutting"));
		
		//DERSE_CORE_TILE_SLAB
		CommonRecipes.slabRecipe(DERSE_CORE_TILE_SLAB, DERSE_CORE_TILES).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_CORE_TILES), RecipeCategory.BUILDING_BLOCKS, DERSE_CORE_TILE_SLAB, 2)
				.unlockedBy("has_derse_brick", has(DERSE_CORE_TILES)).save(recipeSaver, Minestuck.id("derse_core_tile_slab_from_stonecutting"));
		
		//DERSE_CORE_TILE_WALL
		CommonRecipes.wallRecipe(DERSE_CORE_TILE_WALL, DERSE_CORE_TILES).save(recipeSaver);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(DERSE_CORE_TILES), RecipeCategory.BUILDING_BLOCKS, DERSE_CORE_TILE_WALL)
				.unlockedBy("has_derse_brick", has(DERSE_CORE_TILES)).save(recipeSaver, Minestuck.id("derse_core_tile_wall_from_stonecutting"));
		
		
		addGristCosts(recipeSaver);
		addCombinations(recipeSaver);
	}
	
	private static void addGristCosts(RecipeOutput recipeSaver) {
	//TODO
	
	}
	
	private static void addCombinations(RecipeOutput recipeSaver){
	//TODO
	
	}
}
