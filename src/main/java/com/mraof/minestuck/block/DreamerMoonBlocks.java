package com.mraof.minestuck.block;

import com.mraof.minestuck.item.MSItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredItem;


/**
 * Glossary:
 * GILDING - Prospit surface block
 * FERROSTRATA - Prospit stone equivalent
 * PLATING - Derse surface block
 * METALLITH - Derse stone equivalent
 * CORE - Prospit & Derse deepslate equivalent
 */

public class DreamerMoonBlocks
{
	public static void init() {}
	
	//ITEMS
	public static final DeferredItem<Item> PROSPIT_DROSS = MSItems.REGISTER.register("prospit_dross", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> DERSE_RESIDUE = MSItems.REGISTER.register("derse_residue", () -> new Item(new Item.Properties()));
	
	//PROSPIT
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_GILDING = ItemBlockPair.register("prospit_gilding",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.DIRT)
					.mapColor(MapColor.GOLD)
					.instrument(NoteBlockInstrument.XYLOPHONE)
					.sound(SoundType.COPPER_GRATE)
					.requiresCorrectToolForDrops()
			));
	
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_GILDED_PATH = ItemBlockPair.register("prospit_gilded_path",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.DIRT_PATH)
					.mapColor(MapColor.GOLD)
					.instrument(NoteBlockInstrument.XYLOPHONE)
					.sound(SoundType.COPPER_GRATE)
					.requiresCorrectToolForDrops()
			));
	
	
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_FERROSTRATA = ItemBlockPair.register("prospit_ferrostrata",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.STONE)
					.mapColor(MapColor.SAND)
					.instrument(NoteBlockInstrument.BANJO)
					.sound(SoundType.COPPER_BULB)
					.requiresCorrectToolForDrops()
			));
	public static final ItemBlockPair<StairBlock,BlockItem> PROSPIT_FERROSTRATA_STAIRS = ItemBlockPair.register("prospit_ferrostrata_stairs",
			() -> new StairBlock(PROSPIT_FERROSTRATA.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(PROSPIT_FERROSTRATA.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> PROSPIT_FERROSTRATA_SLAB = ItemBlockPair.register("prospit_ferrostrata_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(PROSPIT_FERROSTRATA.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> PROSPIT_FERROSTRATA_WALL = ItemBlockPair.register("prospit_ferrostrata_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(PROSPIT_FERROSTRATA.asBlock())));
	public static final ItemBlockPair<ButtonBlock,BlockItem> PROSPIT_FERROSTRATA_BUTTON = ItemBlockPair.register("prospit_ferrostrata_button",
			() -> new ButtonBlock(MSBlockSetType.PROSPIT, 10, Block.Properties.ofFullCopy(Blocks.STONE_BUTTON)));
	public static final ItemBlockPair<PressurePlateBlock,BlockItem> PROSPIT_FERROSTRATA_PRESSURE_PLATE = ItemBlockPair.register("prospit_ferrostrata_pressure_plate",
			() -> new PressurePlateBlock(MSBlockSetType.PROSPIT, Block.Properties.ofFullCopy(Blocks.STONE_PRESSURE_PLATE)));
	
	
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_ROUGH_FERROSTRATA = ItemBlockPair.register("prospit_rough_ferrostrata",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.COBBLESTONE)
					.mapColor(MapColor.SAND)
					.instrument(NoteBlockInstrument.BANJO)
					.sound(SoundType.COPPER_BULB)
			));
	public static final ItemBlockPair<StairBlock,BlockItem> PROSPIT_ROUGH_FERROSTRATA_STAIRS = ItemBlockPair.register("prospit_rough_ferrostrata_stairs",
			() -> new StairBlock(PROSPIT_ROUGH_FERROSTRATA.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(PROSPIT_ROUGH_FERROSTRATA.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> PROSPIT_ROUGH_FERROSTRATA_SLAB = ItemBlockPair.register("prospit_rough_ferrostrata_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(PROSPIT_ROUGH_FERROSTRATA.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> PROSPIT_ROUGH_FERROSTRATA_WALL = ItemBlockPair.register("prospit_rough_ferrostrata_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(PROSPIT_ROUGH_FERROSTRATA.asBlock())));
	
	
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_REFINED_FERROSTRATA = ItemBlockPair.register("prospit_refined_ferrostrata",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.SMOOTH_STONE)
					.mapColor(MapColor.SAND)
					.instrument(NoteBlockInstrument.BANJO)
					.sound(SoundType.COPPER_BULB)
			));
	public static final ItemBlockPair<StairBlock,BlockItem> PROSPIT_REFINED_FERROSTRATA_STAIRS = ItemBlockPair.register("prospit_refined_ferrostrata_stairs",
			() -> new StairBlock(PROSPIT_ROUGH_FERROSTRATA.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(PROSPIT_REFINED_FERROSTRATA.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> PROSPIT_REFINED_FERROSTRATA_SLAB = ItemBlockPair.register("prospit_refined_ferrostrata_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(PROSPIT_REFINED_FERROSTRATA.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> PROSPIT_REFINED_FERROSTRATA_WALL = ItemBlockPair.register("prospit_refined_ferrostrata_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(PROSPIT_REFINED_FERROSTRATA.asBlock())));
	
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_CORE = ItemBlockPair.register("prospit_core",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.DEEPSLATE)
					.mapColor(MapColor.TERRACOTTA_YELLOW)
					.instrument(NoteBlockInstrument.BANJO)
					.sound(SoundType.HEAVY_CORE)
			));
	public static final ItemBlockPair<StairBlock,BlockItem> PROSPIT_CORE_STAIRS = ItemBlockPair.register("prospit_core_stairs",
			() -> new StairBlock(PROSPIT_CORE.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(PROSPIT_CORE.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> PROSPIT_CORE_SLAB = ItemBlockPair.register("prospit_core_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(PROSPIT_CORE.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> PROSPIT_CORE_WALL = ItemBlockPair.register("prospit_core_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(PROSPIT_CORE.asBlock())));
	
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_ROUGH_CORE = ItemBlockPair.register("prospit_rough_core",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE)
					.mapColor(MapColor.TERRACOTTA_RED)
					.instrument(NoteBlockInstrument.BANJO)
					.sound(SoundType.HEAVY_CORE)
			));
	public static final ItemBlockPair<StairBlock,BlockItem> PROSPIT_ROUGH_CORE_STAIRS = ItemBlockPair.register("prospit_rough_core_stairs",
			() -> new StairBlock(PROSPIT_ROUGH_CORE.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(PROSPIT_ROUGH_CORE.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> PROSPIT_ROUGH_CORE_SLAB = ItemBlockPair.register("prospit_rough_core_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(PROSPIT_ROUGH_CORE.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> PROSPIT_ROUGH_CORE_WALL = ItemBlockPair.register("prospit_rough_core_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(PROSPIT_ROUGH_CORE.asBlock())));
	
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_REFINED_CORE = ItemBlockPair.register("prospit_refined_core",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.POLISHED_DEEPSLATE)
					.mapColor(MapColor.TERRACOTTA_RED)
					.instrument(NoteBlockInstrument.BANJO)
					.sound(SoundType.HEAVY_CORE)
			));
	public static final ItemBlockPair<StairBlock,BlockItem> PROSPIT_REFINED_CORE_STAIRS = ItemBlockPair.register("prospit_refined_core_stairs",
			() -> new StairBlock(PROSPIT_ROUGH_CORE.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(PROSPIT_REFINED_CORE.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> PROSPIT_REFINED_CORE_SLAB = ItemBlockPair.register("prospit_refined_core_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(PROSPIT_REFINED_CORE.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> PROSPIT_REFINED_CORE_WALL = ItemBlockPair.register("prospit_refined_core_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(PROSPIT_REFINED_CORE.asBlock())));

	public static final ItemBlockPair<RotatedPillarBlock,BlockItem> PROSPIT_CABLE = ItemBlockPair.register("prospit_cable",
			() -> new RotatedPillarBlock(Block.Properties.ofFullCopy(PROSPIT_FERROSTRATA.asBlock())));
	public static final ItemBlockPair<RotatedPillarBlock,BlockItem> PROSPIT_THICK_CABLE = ItemBlockPair.register("prospit_thick_cable",
			() -> new RotatedPillarBlock(Block.Properties.ofFullCopy(PROSPIT_FERROSTRATA.asBlock())));
	public static final ItemBlockPair<RotatedPillarBlock,BlockItem> PROSPIT_PILLAR = ItemBlockPair.register("prospit_pillar",
			() -> new RotatedPillarBlock(Block.Properties.ofFullCopy(PROSPIT_FERROSTRATA.asBlock())));
	public static final ItemBlockPair<DoorBlock,BlockItem> PROSPIT_DOOR = ItemBlockPair.register("prospit_door",
			() -> new DoorBlock(MSBlockSetType.PROSPIT, Block.Properties.ofFullCopy(Blocks.COPPER_DOOR)));
	public static final ItemBlockPair<TrapDoorBlock,BlockItem> PROSPIT_TRAPDOOR = ItemBlockPair.register("prospit_trapdoor",
			() -> new TrapDoorBlock(MSBlockSetType.PROSPIT, Block.Properties.ofFullCopy(Blocks.COPPER_TRAPDOOR)));
		
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_BRICK = ItemBlockPair.register("prospit_brick",
			() -> new Block(Block.Properties.ofFullCopy(PROSPIT_FERROSTRATA.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_CRACKED_BRICK = ItemBlockPair.register("prospit_cracked_brick",
			() -> new Block(Block.Properties.ofFullCopy(PROSPIT_FERROSTRATA.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_CHISELED_BRICK = ItemBlockPair.register("prospit_chiseled_brick",
			() -> new Block(Block.Properties.ofFullCopy(PROSPIT_FERROSTRATA.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_PAVING_BRICK = ItemBlockPair.register("prospit_paving_brick",
			() -> new Block(Block.Properties.ofFullCopy(PROSPIT_FERROSTRATA.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_FANCY_BRICK = ItemBlockPair.register("prospit_fancy_brick",
			() -> new Block(Block.Properties.ofFullCopy(PROSPIT_FERROSTRATA.asBlock())));
	public static final ItemBlockPair<ShearableBlock,BlockItem> PROSPIT_TARNISHED_BRICK = ItemBlockPair.register("prospit_tarnished_brick",
			() -> new ShearableBlock(Block.Properties.ofFullCopy(PROSPIT_FERROSTRATA.asBlock()), PROSPIT_BRICK.blockHolder(), PROSPIT_DROSS, 4)); //mossy brick equiv
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_STACKED_BRICK = ItemBlockPair.register("prospit_stacked_brick",
			() -> new Block(Block.Properties.ofFullCopy(PROSPIT_FERROSTRATA.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_TILES = ItemBlockPair.register("prospit_tiles",
			() -> new Block(Block.Properties.ofFullCopy(PROSPIT_FERROSTRATA.asBlock())));
	
	public static final ItemBlockPair<StairBlock,BlockItem> PROSPIT_BRICK_STAIRS = ItemBlockPair.register("prospit_brick_stairs",
			() -> new StairBlock(PROSPIT_BRICK.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(PROSPIT_BRICK.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> PROSPIT_BRICK_SLAB = ItemBlockPair.register("prospit_brick_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(PROSPIT_BRICK.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> PROSPIT_BRICK_WALL = ItemBlockPair.register("prospit_brick_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(PROSPIT_BRICK.asBlock())));
	
	public static final ItemBlockPair<StairBlock,BlockItem> PROSPIT_CRACKED_BRICK_STAIRS = ItemBlockPair.register("prospit_cracked_brick_stairs",
			() -> new StairBlock(PROSPIT_CRACKED_BRICK.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(PROSPIT_CRACKED_BRICK.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> PROSPIT_CRACKED_BRICK_SLAB = ItemBlockPair.register("prospit_cracked_brick_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(PROSPIT_CRACKED_BRICK.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> PROSPIT_CRACKED_BRICK_WALL = ItemBlockPair.register("prospit_cracked_brick_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(PROSPIT_CRACKED_BRICK.asBlock())));
	
	public static final ItemBlockPair<StairBlock,BlockItem> PROSPIT_TARNISHED_BRICK_STAIRS = ItemBlockPair.register("prospit_tarnished_brick_stairs",
			() -> new StairBlock(PROSPIT_BRICK.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(PROSPIT_TARNISHED_BRICK.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> PROSPIT_TARNISHED_BRICK_SLAB = ItemBlockPair.register("prospit_tarnished_brick_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(PROSPIT_TARNISHED_BRICK.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> PROSPIT_TARNISHED_BRICK_WALL = ItemBlockPair.register("prospit_tarnished_brick_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(PROSPIT_TARNISHED_BRICK.asBlock())));
	
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_CORE_BRICK = ItemBlockPair.register("prospit_core_brick",
			() -> new Block(Block.Properties.ofFullCopy(PROSPIT_CORE.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_CRACKED_CORE_BRICK = ItemBlockPair.register("prospit_cracked_core_brick",
			() -> new Block(Block.Properties.ofFullCopy(PROSPIT_CORE.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_CORE_TILES = ItemBlockPair.register("prospit_core_tiles",
			() -> new Block(Block.Properties.ofFullCopy(PROSPIT_CORE.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_CRACKED_CORE_TILES = ItemBlockPair.register("prospit_cracked_core_tiles",
			() -> new Block(Block.Properties.ofFullCopy(PROSPIT_CORE.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_CHISELED_CORE = ItemBlockPair.register("prospit_chiseled_core",
			() -> new Block(Block.Properties.ofFullCopy(PROSPIT_CORE.asBlock())));
	
	public static final ItemBlockPair<StairBlock,BlockItem> PROSPIT_CORE_BRICK_STAIRS = ItemBlockPair.register("prospit_core_brick_stairs",
			() -> new StairBlock(PROSPIT_BRICK.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(PROSPIT_CORE_BRICK.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> PROSPIT_CORE_BRICK_SLAB = ItemBlockPair.register("prospit_core_brick_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(PROSPIT_CORE_BRICK.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> PROSPIT_CORE_BRICK_WALL = ItemBlockPair.register("prospit_core_brick_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(PROSPIT_CORE_BRICK.asBlock())));
	
	public static final ItemBlockPair<StairBlock,BlockItem> PROSPIT_CRACKED_CORE_BRICK_STAIRS = ItemBlockPair.register("prospit_cracked_core_brick_stairs",
			() -> new StairBlock(PROSPIT_CRACKED_BRICK.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(PROSPIT_CRACKED_CORE_BRICK.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> PROSPIT_CRACKED_CORE_BRICK_SLAB = ItemBlockPair.register("prospit_cracked_core_brick_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(PROSPIT_CRACKED_CORE_BRICK.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> PROSPIT_CRACKED_CORE_BRICK_WALL = ItemBlockPair.register("prospit_cracked_core_brick_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(PROSPIT_CRACKED_CORE_BRICK.asBlock())));
	
	public static final ItemBlockPair<StairBlock,BlockItem> PROSPIT_CORE_TILE_STAIRS = ItemBlockPair.register("prospit_core_tile_stairs",
			() -> new StairBlock(PROSPIT_CORE_TILES.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(PROSPIT_CORE_TILES.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> PROSPIT_CORE_TILE_SLAB = ItemBlockPair.register("prospit_core_tile_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(PROSPIT_CORE_TILES.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> PROSPIT_CORE_TILE_WALL = ItemBlockPair.register("prospit_core_tile_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(PROSPIT_CORE_TILES.asBlock())));
	
	
	//DERSE

	public static final ItemBlockPair<Block,BlockItem> DERSE_PLATING = ItemBlockPair.register("derse_plating",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.DIRT)
					.mapColor(MapColor.COLOR_PURPLE)
					.instrument(NoteBlockInstrument.XYLOPHONE)
					.sound(SoundType.COPPER_GRATE)
					.requiresCorrectToolForDrops()
			));
	
	public static final ItemBlockPair<Block,BlockItem> DERSE_PLATED_PATH = ItemBlockPair.register("derse_plated_path",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.DIRT_PATH)
					.mapColor(MapColor.COLOR_PURPLE)
					.instrument(NoteBlockInstrument.XYLOPHONE)
					.sound(SoundType.COPPER_GRATE)
					.requiresCorrectToolForDrops()
			));
	
	public static final ItemBlockPair<Block,BlockItem> DERSE_METALLITH = ItemBlockPair.register("derse_metallith",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.STONE)
					.mapColor(MapColor.COLOR_MAGENTA)
					.instrument(NoteBlockInstrument.BANJO)
					.sound(SoundType.COPPER_BULB)
					.requiresCorrectToolForDrops()
			));
	public static final ItemBlockPair<StairBlock,BlockItem> DERSE_METALLITH_STAIRS = ItemBlockPair.register("derse_metallith_stairs",
			() -> new StairBlock(DERSE_METALLITH.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(DERSE_METALLITH.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> DERSE_METALLITH_SLAB = ItemBlockPair.register("derse_metallith_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(DERSE_METALLITH.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> DERSE_METALLITH_WALL = ItemBlockPair.register("derse_metallith_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(DERSE_METALLITH.asBlock())));
	public static final ItemBlockPair<ButtonBlock,BlockItem> DERSE_METALLITH_BUTTON = ItemBlockPair.register("derse_metallith_button",
			() -> new ButtonBlock(MSBlockSetType.DERSE, 10, Block.Properties.ofFullCopy(Blocks.STONE_BUTTON)));
	public static final ItemBlockPair<PressurePlateBlock,BlockItem> DERSE_METALLITH_PRESSURE_PLATE = ItemBlockPair.register("derse_metallith_pressure_plate",
			() -> new PressurePlateBlock(MSBlockSetType.DERSE, Block.Properties.ofFullCopy(Blocks.STONE_PRESSURE_PLATE)));
	
	public static final ItemBlockPair<Block,BlockItem> DERSE_ROUGH_METALLITH = ItemBlockPair.register("derse_rough_metallith",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.COBBLESTONE)
					.mapColor(MapColor.COLOR_MAGENTA)
					.instrument(NoteBlockInstrument.BANJO)
					.sound(SoundType.COPPER_BULB)
			));
	public static final ItemBlockPair<StairBlock,BlockItem> DERSE_ROUGH_METALLITH_STAIRS = ItemBlockPair.register("derse_rough_metallith_stairs",
			() -> new StairBlock(DERSE_ROUGH_METALLITH.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(DERSE_ROUGH_METALLITH.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> DERSE_ROUGH_METALLITH_SLAB = ItemBlockPair.register("derse_rough_metallith_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(DERSE_ROUGH_METALLITH.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> DERSE_ROUGH_METALLITH_WALL = ItemBlockPair.register("derse_rough_metallith_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(DERSE_ROUGH_METALLITH.asBlock())));
	
	public static final ItemBlockPair<Block,BlockItem> DERSE_REFINED_METALLITH = ItemBlockPair.register("derse_refined_metallith",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.SMOOTH_STONE)
					.mapColor(MapColor.COLOR_MAGENTA)
					.instrument(NoteBlockInstrument.BANJO)
					.sound(SoundType.COPPER_BULB)
			));
	public static final ItemBlockPair<StairBlock,BlockItem> DERSE_REFINED_METALLITH_STAIRS = ItemBlockPair.register("derse_refined_metallith_stairs",
			() -> new StairBlock(DERSE_ROUGH_METALLITH.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(DERSE_REFINED_METALLITH.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> DERSE_REFINED_METALLITH_SLAB = ItemBlockPair.register("derse_refined_metallith_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(DERSE_REFINED_METALLITH.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> DERSE_REFINED_METALLITH_WALL = ItemBlockPair.register("derse_refined_metallith_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(DERSE_REFINED_METALLITH.asBlock())));
	
	public static final ItemBlockPair<Block,BlockItem> DERSE_CORE = ItemBlockPair.register("derse_core",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.DEEPSLATE)
					.mapColor(MapColor.TERRACOTTA_CYAN)
					.instrument(NoteBlockInstrument.BANJO)
					.sound(SoundType.HEAVY_CORE)
			));
	public static final ItemBlockPair<StairBlock,BlockItem> DERSE_CORE_STAIRS = ItemBlockPair.register("derse_core_stairs",
			() -> new StairBlock(DERSE_CORE.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(DERSE_CORE.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> DERSE_CORE_SLAB = ItemBlockPair.register("derse_core_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(DERSE_CORE.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> DERSE_CORE_WALL = ItemBlockPair.register("derse_core_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(DERSE_CORE.asBlock())));
	
	public static final ItemBlockPair<Block,BlockItem> DERSE_ROUGH_CORE = ItemBlockPair.register("derse_rough_core",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE)
					.mapColor(MapColor.TERRACOTTA_CYAN)
					.instrument(NoteBlockInstrument.BANJO)
					.sound(SoundType.HEAVY_CORE)
			));
	public static final ItemBlockPair<StairBlock,BlockItem> DERSE_ROUGH_CORE_STAIRS = ItemBlockPair.register("derse_rough_core_stairs",
			() -> new StairBlock(DERSE_ROUGH_CORE.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(DERSE_ROUGH_CORE.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> DERSE_ROUGH_CORE_SLAB = ItemBlockPair.register("derse_rough_core_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(DERSE_ROUGH_CORE.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> DERSE_ROUGH_CORE_WALL = ItemBlockPair.register("derse_rough_core_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(DERSE_ROUGH_CORE.asBlock())));
	
	public static final ItemBlockPair<Block,BlockItem> DERSE_REFINED_CORE = ItemBlockPair.register("derse_refined_core",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.POLISHED_DEEPSLATE)
					.mapColor(MapColor.TERRACOTTA_CYAN)
					.instrument(NoteBlockInstrument.BANJO)
					.sound(SoundType.HEAVY_CORE)
			));
	public static final ItemBlockPair<StairBlock,BlockItem> DERSE_REFINED_CORE_STAIRS = ItemBlockPair.register("derse_refined_core_stairs",
			() -> new StairBlock(DERSE_ROUGH_CORE.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(DERSE_REFINED_CORE.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> DERSE_REFINED_CORE_SLAB = ItemBlockPair.register("derse_refined_core_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(DERSE_REFINED_CORE.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> DERSE_REFINED_CORE_WALL = ItemBlockPair.register("derse_refined_core_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(DERSE_REFINED_CORE.asBlock())));
	
	public static final ItemBlockPair<RotatedPillarBlock,BlockItem> DERSE_CABLE = ItemBlockPair.register("derse_cable",
			() -> new RotatedPillarBlock(Block.Properties.ofFullCopy(DERSE_METALLITH.asBlock())));
	public static final ItemBlockPair<RotatedPillarBlock,BlockItem> DERSE_THICK_CABLE = ItemBlockPair.register("derse_thick_cable",
			() -> new RotatedPillarBlock(Block.Properties.ofFullCopy(DERSE_METALLITH.asBlock())));
	public static final ItemBlockPair<RotatedPillarBlock,BlockItem> DERSE_PILLAR = ItemBlockPair.register("derse_pillar",
			() -> new RotatedPillarBlock(Block.Properties.ofFullCopy(DERSE_METALLITH.asBlock())));
	public static final ItemBlockPair<DoorBlock,BlockItem> DERSE_DOOR = ItemBlockPair.register("derse_door",
			() -> new DoorBlock(MSBlockSetType.DERSE, Block.Properties.ofFullCopy(Blocks.COPPER_DOOR)));
	public static final ItemBlockPair<TrapDoorBlock,BlockItem> DERSE_TRAPDOOR = ItemBlockPair.register("derse_trapdoor",
			() -> new TrapDoorBlock(MSBlockSetType.DERSE, Block.Properties.ofFullCopy(Blocks.COPPER_TRAPDOOR)));
	
	public static final ItemBlockPair<Block,BlockItem> DERSE_BRICK = ItemBlockPair.register("derse_brick",
			() -> new Block(Block.Properties.ofFullCopy(DERSE_METALLITH.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> DERSE_CRACKED_BRICK = ItemBlockPair.register("derse_cracked_brick",
			() -> new Block(Block.Properties.ofFullCopy(DERSE_METALLITH.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> DERSE_CHISELED_BRICK = ItemBlockPair.register("derse_chiseled_brick",
			() -> new Block(Block.Properties.ofFullCopy(DERSE_METALLITH.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> DERSE_PAVING_BRICK = ItemBlockPair.register("derse_paving_brick",
			() -> new Block(Block.Properties.ofFullCopy(DERSE_METALLITH.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> DERSE_FANCY_BRICK = ItemBlockPair.register("derse_fancy_brick",
			() -> new Block(Block.Properties.ofFullCopy(DERSE_METALLITH.asBlock())));
	public static final ItemBlockPair<ShearableBlock,BlockItem> DERSE_TARNISHED_BRICK = ItemBlockPair.register("derse_tarnished_brick",
			() -> new ShearableBlock(Block.Properties.ofFullCopy(DERSE_METALLITH.asBlock()), DERSE_BRICK.blockHolder(), DERSE_RESIDUE, 4)); //mossy brick equiv
	
	public static final ItemBlockPair<Block,BlockItem> DERSE_STACKED_BRICK = ItemBlockPair.register("derse_stacked_brick",
			() -> new Block(Block.Properties.ofFullCopy(DERSE_METALLITH.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> DERSE_TILES = ItemBlockPair.register("derse_tiles",
			() -> new Block(Block.Properties.ofFullCopy(DERSE_METALLITH.asBlock())));
	
	public static final ItemBlockPair<StairBlock,BlockItem> DERSE_BRICK_STAIRS = ItemBlockPair.register("derse_brick_stairs",
			() -> new StairBlock(DERSE_BRICK.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(DERSE_BRICK.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> DERSE_BRICK_SLAB = ItemBlockPair.register("derse_brick_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(DERSE_BRICK.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> DERSE_BRICK_WALL = ItemBlockPair.register("derse_brick_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(DERSE_BRICK.asBlock())));
	
	public static final ItemBlockPair<StairBlock,BlockItem> DERSE_CRACKED_BRICK_STAIRS = ItemBlockPair.register("derse_cracked_brick_stairs",
			() -> new StairBlock(DERSE_CRACKED_BRICK.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(DERSE_CRACKED_BRICK.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> DERSE_CRACKED_BRICK_SLAB = ItemBlockPair.register("derse_cracked_brick_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(DERSE_CRACKED_BRICK.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> DERSE_CRACKED_BRICK_WALL = ItemBlockPair.register("derse_cracked_brick_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(DERSE_CRACKED_BRICK.asBlock())));
	
	public static final ItemBlockPair<StairBlock,BlockItem> DERSE_TARNISHED_BRICK_STAIRS = ItemBlockPair.register("derse_tarnished_brick_stairs",
			() -> new StairBlock(DERSE_BRICK.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(DERSE_TARNISHED_BRICK.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> DERSE_TARNISHED_BRICK_SLAB = ItemBlockPair.register("derse_tarnished_brick_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(DERSE_TARNISHED_BRICK.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> DERSE_TARNISHED_BRICK_WALL = ItemBlockPair.register("derse_tarnished_brick_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(DERSE_TARNISHED_BRICK.asBlock())));
	
	public static final ItemBlockPair<Block,BlockItem> DERSE_CORE_BRICK = ItemBlockPair.register("derse_core_brick",
			() -> new Block(Block.Properties.ofFullCopy(DERSE_CORE.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> DERSE_CRACKED_CORE_BRICK = ItemBlockPair.register("derse_cracked_core_brick",
			() -> new Block(Block.Properties.ofFullCopy(DERSE_CORE.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> DERSE_CORE_TILES = ItemBlockPair.register("derse_core_tiles",
			() -> new Block(Block.Properties.ofFullCopy(DERSE_CORE.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> DERSE_CRACKED_CORE_TILES = ItemBlockPair.register("derse_cracked_core_tiles",
			() -> new Block(Block.Properties.ofFullCopy(DERSE_CORE.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> DERSE_CHISELED_CORE = ItemBlockPair.register("derse_chiseled_core",
			() -> new Block(Block.Properties.ofFullCopy(DERSE_CORE.asBlock())));
	
	public static final ItemBlockPair<StairBlock,BlockItem> DERSE_CORE_BRICK_STAIRS = ItemBlockPair.register("derse_core_brick_stairs",
			() -> new StairBlock(DERSE_BRICK.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(DERSE_CORE_BRICK.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> DERSE_CORE_BRICK_SLAB = ItemBlockPair.register("derse_core_brick_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(DERSE_CORE_BRICK.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> DERSE_CORE_BRICK_WALL = ItemBlockPair.register("derse_core_brick_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(DERSE_CORE_BRICK.asBlock())));
	
	public static final ItemBlockPair<StairBlock,BlockItem> DERSE_CRACKED_CORE_BRICK_STAIRS = ItemBlockPair.register("derse_cracked_core_brick_stairs",
			() -> new StairBlock(DERSE_CRACKED_BRICK.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(DERSE_CRACKED_CORE_BRICK.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> DERSE_CRACKED_CORE_BRICK_SLAB = ItemBlockPair.register("derse_cracked_core_brick_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(DERSE_CRACKED_CORE_BRICK.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> DERSE_CRACKED_CORE_BRICK_WALL = ItemBlockPair.register("derse_cracked_core_brick_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(DERSE_CRACKED_CORE_BRICK.asBlock())));
	
	public static final ItemBlockPair<StairBlock,BlockItem> DERSE_CORE_TILE_STAIRS = ItemBlockPair.register("derse_core_tile_stairs",
			() -> new StairBlock(DERSE_CORE_TILES.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(DERSE_CORE_TILES.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> DERSE_CORE_TILE_SLAB = ItemBlockPair.register("derse_core_tile_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(DERSE_CORE_TILES.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> DERSE_CORE_TILE_WALL = ItemBlockPair.register("derse_core_tile_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(DERSE_CORE_TILES.asBlock())));
	
}
