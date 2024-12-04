package com.mraof.minestuck.data;

import com.mraof.minestuck.data.loot_table.MSBlockLootTables;
import com.mraof.minestuck.data.tag.MinestuckBlockTagsProvider;
import net.minecraft.data.recipes.RecipeOutput;

import static com.mraof.minestuck.block.DreamerMoonBlocks.*;
import static com.mraof.minestuck.data.MSBlockStateProvider.texture;
import static net.minecraft.tags.BlockTags.*;

public class DreamerMoonBlocksData
{
	public static void addEnUsTranslations(MinestuckLanguageProvider provider){
		provider.add(PROSPIT_GILDING,"Prospit Gilding");
		provider.add(PROSPIT_GILDED_PATH,"Prospit Gilded Path");
		provider.add(PROSPIT_FERROSTRATA,"Prospit Ferrostrata");
		provider.add(PROSPIT_FERROSTRATA_STAIRS,"Prospit Ferrostrata Stairs");
		provider.add(PROSPIT_FERROSTRATA_SLAB,"Prospit Ferrostrata Slab");
		provider.add(PROSPIT_FERROSTRATA_WALL,"Prospit Ferrostrata Wall");
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
		provider.add(PROSPIT_FERROSTRATA_CRUXITE_ORE,"Prospit Ferrostrata Cruxite Ore");
		provider.add(PROSPIT_FERROSTRATA_URANIUM_ORE,"Prospit Ferrostrata Uranium Ore");
		provider.add(PROSPIT_FERROSTRATA_PROSPIT_ORE,"Prospit Ferrostrata Prospit Ore");
		provider.add(PROSPIT_CORE_CRUXITE_ORE,"Prospit Core Cruxite Ore");
		provider.add(PROSPIT_CORE_URANIUM_ORE,"Prospit Core Uranium Ore");
		provider.add(PROSPIT_CORE_PROSPIT_ORE,"Prospit Core Prospit Ore");
		provider.add(PROSPIT_INGOT_BLOCK,"Prospit Block");
		provider.add(PROSPIT_ORE_BLOCK,"Prospit Ore Block");
		provider.add(PROSPIT_CABLE,"Prospit Cable");
		provider.add(PROSPIT_THICK_CABLE,"Thick Prospit Cable");
		provider.add(PROSPIT_PILLAR,"Prospit Pillar");
		provider.add(PROSPIT_DOOR,"Prospit Door");
		provider.add(PROSPIT_TRAPDOOR,"Prospit Trapdoor");
		provider.add(PROSPIT_BRICK,"Prospit Brick");
		provider.add(PROSPIT_CRACKED_BRICK,"Cracked Prospit Brick");
		provider.add(PROSPIT_BIG_BRICK,"Big Prospit Brick");
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
	}
	
	public static void addModels(MSBlockStateProvider provider){
		provider.simpleBlockWithItem(PROSPIT_GILDING);
		provider.simpleBlockWithItem(PROSPIT_GILDED_PATH);
		provider.simpleBlockWithItem(PROSPIT_FERROSTRATA);
		provider.stairsWithItem(PROSPIT_FERROSTRATA_STAIRS::asBlock,PROSPIT_FERROSTRATA.blockHolder());
		provider.slabWithItem(PROSPIT_FERROSTRATA_SLAB::asBlock,PROSPIT_FERROSTRATA.blockHolder());
		provider.wallWithItem(PROSPIT_FERROSTRATA_WALL::asBlock,PROSPIT_FERROSTRATA.blockHolder());
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
		provider.simpleBlockWithItem(PROSPIT_FERROSTRATA_CRUXITE_ORE);
		provider.simpleBlockWithItem(PROSPIT_FERROSTRATA_URANIUM_ORE);
		provider.simpleBlockWithItem(PROSPIT_FERROSTRATA_PROSPIT_ORE);
		provider.simpleBlockWithItem(PROSPIT_CORE_CRUXITE_ORE);
		provider.simpleBlockWithItem(PROSPIT_CORE_URANIUM_ORE);
		provider.simpleBlockWithItem(PROSPIT_CORE_PROSPIT_ORE);
		provider.simpleBlockWithItem(PROSPIT_INGOT_BLOCK);
		provider.simpleBlockWithItem(PROSPIT_ORE_BLOCK);
		provider.simpleBlockWithItem(PROSPIT_CABLE);
		provider.simpleBlockWithItem(PROSPIT_THICK_CABLE);
		provider.simpleBlockWithItem(PROSPIT_PILLAR);
		provider.simpleDoorBlock(PROSPIT_DOOR.blockHolder());
		provider.flatItem(PROSPIT_DOOR.itemHolder(), MSBlockStateProvider::itemTexture);
		provider.trapDoorWithItem(PROSPIT_TRAPDOOR.blockHolder());
		provider.simpleBlockWithItem(PROSPIT_BRICK);
		provider.simpleBlockWithItem(PROSPIT_CRACKED_BRICK);
		provider.simpleBlockWithItem(PROSPIT_BIG_BRICK);
		provider.simpleBlockWithItem(PROSPIT_CHISELED_BRICK);
		provider.simpleBlockWithItem(PROSPIT_PAVING_BRICK);
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
		provider.simpleBlockWithItem(DERSE_METALLITH);
		provider.stairsWithItem(DERSE_METALLITH_STAIRS::asBlock,DERSE_METALLITH.blockHolder());
		provider.slabWithItem(DERSE_METALLITH_SLAB::asBlock,DERSE_METALLITH.blockHolder());
		provider.wallWithItem(DERSE_METALLITH_WALL::asBlock,DERSE_METALLITH.blockHolder());
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
		provider.simpleBlockWithItem(DERSE_METALLITH_CRUXITE_ORE);
		provider.simpleBlockWithItem(DERSE_METALLITH_URANIUM_ORE);
		provider.simpleBlockWithItem(DERSE_METALLITH_DERSE_ORE);
		provider.simpleBlockWithItem(DERSE_CORE_CRUXITE_ORE);
		provider.simpleBlockWithItem(DERSE_CORE_URANIUM_ORE);
		provider.simpleBlockWithItem(DERSE_CORE_DERSE_ORE);
		provider.simpleBlockWithItem(DERSE_INGOT_BLOCK);
		provider.simpleBlockWithItem(DERSE_ORE_BLOCK);
		provider.simpleBlockWithItem(DERSE_CABLE);
		provider.simpleBlockWithItem(DERSE_THICK_CABLE);
		provider.simpleBlockWithItem(DERSE_PILLAR);
		provider.simpleDoorBlock(DERSE_DOOR.blockHolder());
		provider.flatItem(DERSE_DOOR.itemHolder(), MSBlockStateProvider::itemTexture);
		provider.trapDoorWithItem(DERSE_TRAPDOOR.blockHolder());
		provider.simpleBlockWithItem(DERSE_BRICK);
		provider.simpleBlockWithItem(DERSE_CRACKED_BRICK);
		provider.simpleBlockWithItem(DERSE_BIG_BRICK);
		provider.simpleBlockWithItem(DERSE_CHISELED_BRICK);
		provider.simpleBlockWithItem(DERSE_PAVING_BRICK);
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
	
	public static void addLootTables(MSBlockLootTables provider){
		
		provider.dropSelf(PROSPIT_GILDING.asBlock());
		provider.dropSelf(PROSPIT_GILDED_PATH.asBlock());
		provider.dropSelf(PROSPIT_FERROSTRATA.asBlock());
		provider.dropSelf(PROSPIT_FERROSTRATA_STAIRS.asBlock());
		provider.add(PROSPIT_FERROSTRATA_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(PROSPIT_FERROSTRATA_WALL.asBlock());
		provider.dropSelf(PROSPIT_ROUGH_FERROSTRATA.asBlock());
		provider.dropSelf(PROSPIT_ROUGH_FERROSTRATA_STAIRS.asBlock());
		provider.add(PROSPIT_ROUGH_FERROSTRATA_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(PROSPIT_ROUGH_FERROSTRATA_WALL.asBlock());
		provider.dropSelf(PROSPIT_REFINED_FERROSTRATA.asBlock());
		provider.dropSelf(PROSPIT_REFINED_FERROSTRATA_STAIRS.asBlock());
		provider.add(PROSPIT_REFINED_FERROSTRATA_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(PROSPIT_REFINED_FERROSTRATA_WALL.asBlock());
		provider.dropSelf(PROSPIT_CORE.asBlock());
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
		provider.dropSelf(PROSPIT_FERROSTRATA_CRUXITE_ORE.asBlock());
		provider.dropSelf(PROSPIT_FERROSTRATA_URANIUM_ORE.asBlock());
		provider.dropSelf(PROSPIT_FERROSTRATA_PROSPIT_ORE.asBlock());
		provider.dropSelf(PROSPIT_CORE_CRUXITE_ORE.asBlock());
		provider.dropSelf(PROSPIT_CORE_URANIUM_ORE.asBlock());
		provider.dropSelf(PROSPIT_CORE_PROSPIT_ORE.asBlock());
		provider.dropSelf(PROSPIT_INGOT_BLOCK.asBlock());
		provider.dropSelf(PROSPIT_ORE_BLOCK.asBlock());
		provider.dropSelf(PROSPIT_CABLE.asBlock());
		provider.dropSelf(PROSPIT_THICK_CABLE.asBlock());
		provider.dropSelf(PROSPIT_PILLAR.asBlock());
		provider.add(PROSPIT_DOOR.asBlock(), provider::createDoorTable);
		provider.dropSelf(PROSPIT_TRAPDOOR.asBlock());
		provider.dropSelf(PROSPIT_BRICK.asBlock());
		provider.dropSelf(PROSPIT_CRACKED_BRICK.asBlock());
		provider.dropSelf(PROSPIT_BIG_BRICK.asBlock());
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
		provider.dropSelf(DERSE_PLATED_PATH.asBlock());
		provider.dropSelf(DERSE_METALLITH.asBlock());
		provider.dropSelf(DERSE_METALLITH_STAIRS.asBlock());
		provider.add(DERSE_METALLITH_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(DERSE_METALLITH_WALL.asBlock());
		provider.dropSelf(DERSE_ROUGH_METALLITH.asBlock());
		provider.dropSelf(DERSE_ROUGH_METALLITH_STAIRS.asBlock());
		provider.add(DERSE_ROUGH_METALLITH_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(DERSE_ROUGH_METALLITH_WALL.asBlock());
		provider.dropSelf(DERSE_REFINED_METALLITH.asBlock());
		provider.dropSelf(DERSE_REFINED_METALLITH_STAIRS.asBlock());
		provider.add(DERSE_REFINED_METALLITH_SLAB.asBlock(), provider::createSlabItemTable);
		provider.dropSelf(DERSE_REFINED_METALLITH_WALL.asBlock());
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
		provider.dropSelf(DERSE_METALLITH_CRUXITE_ORE.asBlock());
		provider.dropSelf(DERSE_METALLITH_URANIUM_ORE.asBlock());
		provider.dropSelf(DERSE_METALLITH_DERSE_ORE.asBlock());
		provider.dropSelf(DERSE_CORE_CRUXITE_ORE.asBlock());
		provider.dropSelf(DERSE_CORE_URANIUM_ORE.asBlock());
		provider.dropSelf(DERSE_CORE_DERSE_ORE.asBlock());
		provider.dropSelf(DERSE_INGOT_BLOCK.asBlock());
		provider.dropSelf(DERSE_ORE_BLOCK.asBlock());
		provider.dropSelf(DERSE_CABLE.asBlock());
		provider.dropSelf(DERSE_THICK_CABLE.asBlock());
		provider.dropSelf(DERSE_PILLAR.asBlock());
		provider.add(DERSE_DOOR.asBlock(), provider::createDoorTable);
		provider.dropSelf(DERSE_TRAPDOOR.asBlock());
		provider.dropSelf(DERSE_BRICK.asBlock());
		provider.dropSelf(DERSE_CRACKED_BRICK.asBlock());
		provider.dropSelf(DERSE_BIG_BRICK.asBlock());
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
		provider.tag(MINEABLE_WITH_PICKAXE).add(PROSPIT_GILDING.asBlock(), PROSPIT_GILDED_PATH.asBlock(), PROSPIT_FERROSTRATA.asBlock(), PROSPIT_ROUGH_FERROSTRATA.asBlock(), PROSPIT_REFINED_FERROSTRATA.asBlock(), PROSPIT_CORE.asBlock(), PROSPIT_ROUGH_CORE.asBlock(), PROSPIT_REFINED_CORE.asBlock(), PROSPIT_FERROSTRATA_CRUXITE_ORE.asBlock(), PROSPIT_FERROSTRATA_URANIUM_ORE.asBlock(), PROSPIT_FERROSTRATA_PROSPIT_ORE.asBlock(), PROSPIT_CORE_CRUXITE_ORE.asBlock(), PROSPIT_CORE_URANIUM_ORE.asBlock(), PROSPIT_CORE_PROSPIT_ORE.asBlock(), PROSPIT_INGOT_BLOCK.asBlock(), PROSPIT_ORE_BLOCK.asBlock(), PROSPIT_CABLE.asBlock(), PROSPIT_THICK_CABLE.asBlock(), PROSPIT_PILLAR.asBlock(), PROSPIT_DOOR.asBlock(), PROSPIT_TRAPDOOR.asBlock(), PROSPIT_BRICK.asBlock(), PROSPIT_CRACKED_BRICK.asBlock(), PROSPIT_BIG_BRICK.asBlock(), PROSPIT_CHISELED_BRICK.asBlock(), PROSPIT_PAVING_BRICK.asBlock(), PROSPIT_FANCY_BRICK.asBlock(), PROSPIT_TARNISHED_BRICK.asBlock(), PROSPIT_STACKED_BRICK.asBlock(), PROSPIT_TILES.asBlock(), PROSPIT_CORE_BRICK.asBlock(), PROSPIT_CRACKED_CORE_BRICK.asBlock(), PROSPIT_CORE_TILES.asBlock(), PROSPIT_CRACKED_CORE_TILES.asBlock(), PROSPIT_CHISELED_CORE.asBlock());
		provider.needsWoodPickaxe(PROSPIT_GILDING.asBlock(), PROSPIT_GILDED_PATH.asBlock(), PROSPIT_FERROSTRATA.asBlock(), PROSPIT_ROUGH_FERROSTRATA.asBlock(), PROSPIT_REFINED_FERROSTRATA.asBlock(), PROSPIT_CORE.asBlock(), PROSPIT_ROUGH_CORE.asBlock(), PROSPIT_REFINED_CORE.asBlock(), PROSPIT_FERROSTRATA_CRUXITE_ORE.asBlock(), PROSPIT_FERROSTRATA_URANIUM_ORE.asBlock(), PROSPIT_FERROSTRATA_PROSPIT_ORE.asBlock(), PROSPIT_CORE_CRUXITE_ORE.asBlock(), PROSPIT_CORE_URANIUM_ORE.asBlock(), PROSPIT_CORE_PROSPIT_ORE.asBlock(), PROSPIT_INGOT_BLOCK.asBlock(), PROSPIT_ORE_BLOCK.asBlock(), PROSPIT_CABLE.asBlock(), PROSPIT_THICK_CABLE.asBlock(), PROSPIT_PILLAR.asBlock(), PROSPIT_DOOR.asBlock(), PROSPIT_TRAPDOOR.asBlock(), PROSPIT_BRICK.asBlock(), PROSPIT_CRACKED_BRICK.asBlock(), PROSPIT_BIG_BRICK.asBlock(), PROSPIT_CHISELED_BRICK.asBlock(), PROSPIT_PAVING_BRICK.asBlock(), PROSPIT_FANCY_BRICK.asBlock(), PROSPIT_TARNISHED_BRICK.asBlock(), PROSPIT_STACKED_BRICK.asBlock(), PROSPIT_TILES.asBlock(), PROSPIT_CORE_BRICK.asBlock(), PROSPIT_CRACKED_CORE_BRICK.asBlock(), PROSPIT_CORE_TILES.asBlock(), PROSPIT_CRACKED_CORE_TILES.asBlock(), PROSPIT_CHISELED_CORE.asBlock());
		
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
		
		//DERSE
		//blocks
		provider.tag(MINEABLE_WITH_PICKAXE).add(DERSE_PLATING.asBlock(), DERSE_PLATED_PATH.asBlock(), DERSE_METALLITH.asBlock(), DERSE_ROUGH_METALLITH.asBlock(), DERSE_REFINED_METALLITH.asBlock(), DERSE_CORE.asBlock(), DERSE_ROUGH_CORE.asBlock(), DERSE_REFINED_CORE.asBlock(), DERSE_METALLITH_CRUXITE_ORE.asBlock(), DERSE_METALLITH_URANIUM_ORE.asBlock(), DERSE_METALLITH_DERSE_ORE.asBlock(), DERSE_CORE_CRUXITE_ORE.asBlock(), DERSE_CORE_URANIUM_ORE.asBlock(), DERSE_CORE_DERSE_ORE.asBlock(), DERSE_INGOT_BLOCK.asBlock(), DERSE_ORE_BLOCK.asBlock(), DERSE_CABLE.asBlock(), DERSE_THICK_CABLE.asBlock(), DERSE_PILLAR.asBlock(), DERSE_DOOR.asBlock(), DERSE_TRAPDOOR.asBlock(), DERSE_BRICK.asBlock(), DERSE_CRACKED_BRICK.asBlock(), DERSE_BIG_BRICK.asBlock(), DERSE_CHISELED_BRICK.asBlock(), DERSE_PAVING_BRICK.asBlock(), DERSE_FANCY_BRICK.asBlock(), DERSE_TARNISHED_BRICK.asBlock(), DERSE_STACKED_BRICK.asBlock(), DERSE_TILES.asBlock(), DERSE_CORE_BRICK.asBlock(), DERSE_CRACKED_CORE_BRICK.asBlock(), DERSE_CORE_TILES.asBlock(), DERSE_CRACKED_CORE_TILES.asBlock(), DERSE_CHISELED_CORE.asBlock());
		provider.needsWoodPickaxe(DERSE_PLATING.asBlock(), DERSE_PLATED_PATH.asBlock(), DERSE_METALLITH.asBlock(), DERSE_ROUGH_METALLITH.asBlock(), DERSE_REFINED_METALLITH.asBlock(), DERSE_CORE.asBlock(), DERSE_ROUGH_CORE.asBlock(), DERSE_REFINED_CORE.asBlock(), DERSE_METALLITH_CRUXITE_ORE.asBlock(), DERSE_METALLITH_URANIUM_ORE.asBlock(), DERSE_METALLITH_DERSE_ORE.asBlock(), DERSE_CORE_CRUXITE_ORE.asBlock(), DERSE_CORE_URANIUM_ORE.asBlock(), DERSE_CORE_DERSE_ORE.asBlock(), DERSE_INGOT_BLOCK.asBlock(), DERSE_ORE_BLOCK.asBlock(), DERSE_CABLE.asBlock(), DERSE_THICK_CABLE.asBlock(), DERSE_PILLAR.asBlock(), DERSE_DOOR.asBlock(), DERSE_TRAPDOOR.asBlock(), DERSE_BRICK.asBlock(), DERSE_CRACKED_BRICK.asBlock(), DERSE_BIG_BRICK.asBlock(), DERSE_CHISELED_BRICK.asBlock(), DERSE_PAVING_BRICK.asBlock(), DERSE_FANCY_BRICK.asBlock(), DERSE_TARNISHED_BRICK.asBlock(), DERSE_STACKED_BRICK.asBlock(), DERSE_TILES.asBlock(), DERSE_CORE_BRICK.asBlock(), DERSE_CRACKED_CORE_BRICK.asBlock(), DERSE_CORE_TILES.asBlock(), DERSE_CRACKED_CORE_TILES.asBlock(), DERSE_CHISELED_CORE.asBlock());
		
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
		
	}
	
	public static void addRecipes(RecipeOutput recipeSaver){
		//todo literally all the recipes
		
		addGristCosts(recipeSaver);
		addCombinations(recipeSaver);
	}
	
	private static void addGristCosts(RecipeOutput recipeSaver) {}
	
	private static void addCombinations(RecipeOutput recipeSaver){}
}
