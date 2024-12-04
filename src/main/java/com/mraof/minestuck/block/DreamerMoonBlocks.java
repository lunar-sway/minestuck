package com.mraof.minestuck.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class DreamerMoonBlocks
{
	public static void init() {}
	//PROSPIT
	//surface block
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_GILDING = ItemBlockPair.register("prospit_gilding",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.DIRT)
					.mapColor(MapColor.GOLD) //todo custom map colors?? otherwise prospit maps will be solid yellow
					.instrument(NoteBlockInstrument.XYLOPHONE)
					.sound(SoundType.COPPER_GRATE) //todo 'walking on metallic surface' sfx
					.requiresCorrectToolForDrops()
			));
	
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_GILDED_PATH = ItemBlockPair.register("prospit_gilded_path",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.DIRT_PATH)
					.mapColor(MapColor.COLOR_YELLOW) //todo custom map colors?? otherwise prospit maps will be solid yellow
					.instrument(NoteBlockInstrument.XYLOPHONE)
					.sound(SoundType.COPPER_GRATE) //todo 'muted walking on metallic surface' sfx
					.requiresCorrectToolForDrops()
			));
	
	//underground 1 - stone equivalent
	//raw stone
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_FERROSTRATA = ItemBlockPair.register("prospit_ferrostrata",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.STONE)
					.mapColor(MapColor.SAND)
					.instrument(NoteBlockInstrument.BANJO)
					.sound(SoundType.COPPER_BULB) //todo 'different walking on metallix surface' sfx
			));
	public static final ItemBlockPair<StairBlock,BlockItem> PROSPIT_FERROSTRATA_STAIRS = ItemBlockPair.register("prospit_ferrostrata_stairs",
			() -> new StairBlock(PROSPIT_FERROSTRATA.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(PROSPIT_FERROSTRATA.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> PROSPIT_FERROSTRATA_SLAB = ItemBlockPair.register("prospit_ferrostrata_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(PROSPIT_FERROSTRATA.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> PROSPIT_FERROSTRATA_WALL = ItemBlockPair.register("prospit_ferrostrata_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(PROSPIT_FERROSTRATA.asBlock())));
	
	//cobblestone
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_ROUGH_FERROSTRATA = ItemBlockPair.register("prospit_rough_ferrostrata",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.COBBLESTONE)
					.mapColor(MapColor.SAND)
					.instrument(NoteBlockInstrument.BANJO)
					.sound(SoundType.COPPER_BULB) //todo 'different walking on metallix surface' sfx
			));
	public static final ItemBlockPair<StairBlock,BlockItem> PROSPIT_ROUGH_FERROSTRATA_STAIRS = ItemBlockPair.register("prospit_rough_ferrostrata_stairs",
			() -> new StairBlock(PROSPIT_ROUGH_FERROSTRATA.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(PROSPIT_ROUGH_FERROSTRATA.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> PROSPIT_ROUGH_FERROSTRATA_SLAB = ItemBlockPair.register("prospit_rough_ferrostrata_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(PROSPIT_ROUGH_FERROSTRATA.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> PROSPIT_ROUGH_FERROSTRATA_WALL = ItemBlockPair.register("prospit_rough_ferrostrata_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(PROSPIT_ROUGH_FERROSTRATA.asBlock())));
	
	//smooth stone
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_REFINED_FERROSTRATA = ItemBlockPair.register("prospit_refined_ferrostrata",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.SMOOTH_STONE)
					.mapColor(MapColor.SAND)
					.instrument(NoteBlockInstrument.BANJO)
					.sound(SoundType.COPPER_BULB) //todo 'different walking on metallix surface' sfx
			));
	public static final ItemBlockPair<StairBlock,BlockItem> PROSPIT_REFINED_FERROSTRATA_STAIRS = ItemBlockPair.register("prospit_refined_ferrostrata_stairs",
			() -> new StairBlock(PROSPIT_ROUGH_FERROSTRATA.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(PROSPIT_REFINED_FERROSTRATA.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> PROSPIT_REFINED_FERROSTRATA_SLAB = ItemBlockPair.register("prospit_refined_ferrostrata_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(PROSPIT_REFINED_FERROSTRATA.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> PROSPIT_REFINED_FERROSTRATA_WALL = ItemBlockPair.register("prospit_refined_ferrostrata_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(PROSPIT_REFINED_FERROSTRATA.asBlock())));
	
	//the idea is that the ferrostrata is developed naturally when the moon is created. mining it up results in
	//rough ferrostrata that can be smelted into raw ferrostrata then refined ferrostrata
	
	//underground 1 - deepslate equivalent
	//deepslate
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_CORE = ItemBlockPair.register("prospit_core",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.DEEPSLATE)
					.mapColor(MapColor.SAND)
					.instrument(NoteBlockInstrument.BANJO)
					.sound(SoundType.HEAVY_CORE) //todo 'different walking on metallix surface' sfx
			));
	public static final ItemBlockPair<StairBlock,BlockItem> PROSPIT_CORE_STAIRS = ItemBlockPair.register("prospit_core_stairs",
			() -> new StairBlock(PROSPIT_CORE.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(PROSPIT_CORE.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> PROSPIT_CORE_SLAB = ItemBlockPair.register("prospit_core_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(PROSPIT_CORE.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> PROSPIT_CORE_WALL = ItemBlockPair.register("prospit_core_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(PROSPIT_CORE.asBlock())));
	
	//cobbled deepslate
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_ROUGH_CORE = ItemBlockPair.register("prospit_rough_core",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE)
					.mapColor(MapColor.SAND)
					.instrument(NoteBlockInstrument.BANJO)
					.sound(SoundType.HEAVY_CORE) //todo 'different walking on metallix surface' sfx
			));
	public static final ItemBlockPair<StairBlock,BlockItem> PROSPIT_ROUGH_CORE_STAIRS = ItemBlockPair.register("prospit_rough_core_stairs",
			() -> new StairBlock(PROSPIT_ROUGH_CORE.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(PROSPIT_ROUGH_CORE.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> PROSPIT_ROUGH_CORE_SLAB = ItemBlockPair.register("prospit_rough_core_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(PROSPIT_ROUGH_CORE.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> PROSPIT_ROUGH_CORE_WALL = ItemBlockPair.register("prospit_rough_core_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(PROSPIT_ROUGH_CORE.asBlock())));
	
	//polished deepslate
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_REFINED_CORE = ItemBlockPair.register("prospit_refined_core",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.POLISHED_DEEPSLATE)
					.mapColor(MapColor.SAND)
					.instrument(NoteBlockInstrument.BANJO)
					.sound(SoundType.HEAVY_CORE) //todo 'different walking on metallix surface' sfx
			));
	public static final ItemBlockPair<StairBlock,BlockItem> PROSPIT_REFINED_CORE_STAIRS = ItemBlockPair.register("prospit_refined_core_stairs",
			() -> new StairBlock(PROSPIT_ROUGH_CORE.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(PROSPIT_REFINED_CORE.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> PROSPIT_REFINED_CORE_SLAB = ItemBlockPair.register("prospit_refined_core_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(PROSPIT_REFINED_CORE.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> PROSPIT_REFINED_CORE_WALL = ItemBlockPair.register("prospit_refined_core_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(PROSPIT_REFINED_CORE.asBlock())));
	
	//ORES
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_FERROSTRATA_CRUXITE_ORE = ItemBlockPair.register("prospit_ferrostrata_cruxite_ore",
			() -> new Block(Block.Properties.ofFullCopy(MSBlocks.STONE_CRUXITE_ORE.get())));
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_FERROSTRATA_URANIUM_ORE = ItemBlockPair.register("prospit_ferrostrata_uranium_ore",
			() -> new Block(Block.Properties.ofFullCopy(MSBlocks.STONE_URANIUM_ORE.get())));
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_FERROSTRATA_PROSPIT_ORE = ItemBlockPair.register("prospit_ferrostrata_prospit_ore",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.GOLD_ORE)));
	
	
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_CORE_CRUXITE_ORE = ItemBlockPair.register("prospit_core_cruxite_ore",
			() -> new Block(Block.Properties.ofFullCopy(MSBlocks.STONE_CRUXITE_ORE.get())));
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_CORE_URANIUM_ORE = ItemBlockPair.register("prospit_core_uranium_ore",
			() -> new Block(Block.Properties.ofFullCopy(MSBlocks.DEEPSLATE_URANIUM_ORE.get())));
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_CORE_PROSPIT_ORE = ItemBlockPair.register("prospit_core_prospit_ore",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.DEEPSLATE_GOLD_ORE)));
	
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_INGOT_BLOCK = ItemBlockPair.register("prospit_ingot_block",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.GOLD_BLOCK))); // todo added custom block properties
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_ORE_BLOCK = ItemBlockPair.register("prospit_ore_block",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.RAW_GOLD_BLOCK))); // todo added custom block properties
	
	
	//misc
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_CABLE = ItemBlockPair.register("prospit_cable",
			() -> new Block(Block.Properties.ofFullCopy(PROSPIT_INGOT_BLOCK.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_THICK_CABLE = ItemBlockPair.register("prospit_thick_cable",
			() -> new Block(Block.Properties.ofFullCopy(PROSPIT_INGOT_BLOCK.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_PILLAR = ItemBlockPair.register("prospit_pillar",
			() -> new Block(Block.Properties.ofFullCopy(PROSPIT_INGOT_BLOCK.asBlock())));
	public static final ItemBlockPair<DoorBlock,BlockItem> PROSPIT_DOOR = ItemBlockPair.register("prospit_door",
			() -> new DoorBlock(MSBlockSetType.PROSPIT, Block.Properties.ofFullCopy(Blocks.COPPER_DOOR)));
	public static final ItemBlockPair<TrapDoorBlock,BlockItem> PROSPIT_TRAPDOOR = ItemBlockPair.register("prospit_trapdoor",
			() -> new TrapDoorBlock(MSBlockSetType.PROSPIT, Block.Properties.ofFullCopy(Blocks.COPPER_TRAPDOOR)));
		
	//stone bricks (crafted with ferrostrata)
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_BRICK = ItemBlockPair.register("prospit_brick",
			() -> new Block(Block.Properties.ofFullCopy(PROSPIT_FERROSTRATA.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_CRACKED_BRICK = ItemBlockPair.register("prospit_cracked_brick",
			() -> new Block(Block.Properties.ofFullCopy(PROSPIT_FERROSTRATA.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_BIG_BRICK = ItemBlockPair.register("prospit_big_brick",
			() -> new Block(Block.Properties.ofFullCopy(PROSPIT_FERROSTRATA.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_CHISELED_BRICK = ItemBlockPair.register("prospit_chiseled_brick",
			() -> new Block(Block.Properties.ofFullCopy(PROSPIT_FERROSTRATA.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_PAVING_BRICK = ItemBlockPair.register("prospit_paving_brick",
			() -> new Block(Block.Properties.ofFullCopy(PROSPIT_FERROSTRATA.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_FANCY_BRICK = ItemBlockPair.register("prospit_fancy_brick",
			() -> new Block(Block.Properties.ofFullCopy(PROSPIT_FERROSTRATA.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> PROSPIT_TARNISHED_BRICK = ItemBlockPair.register("prospit_tarnished_brick",
			() -> new Block(Block.Properties.ofFullCopy(PROSPIT_FERROSTRATA.asBlock()))); //mossy brick equiv
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
	
	//deepslate bricks (crafted with core)
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
			() -> new StairBlock(PROSPIT_BRICK.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(PROSPIT_BRICK.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> PROSPIT_CORE_BRICK_SLAB = ItemBlockPair.register("prospit_core_brick_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(PROSPIT_BRICK.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> PROSPIT_CORE_BRICK_WALL = ItemBlockPair.register("prospit_core_brick_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(PROSPIT_BRICK.asBlock())));
	
	public static final ItemBlockPair<StairBlock,BlockItem> PROSPIT_CRACKED_CORE_BRICK_STAIRS = ItemBlockPair.register("prospit_cracked_core_brick_stairs",
			() -> new StairBlock(PROSPIT_CRACKED_BRICK.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(PROSPIT_CRACKED_BRICK.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> PROSPIT_CRACKED_CORE_BRICK_SLAB = ItemBlockPair.register("prospit_cracked_core_brick_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(PROSPIT_CRACKED_BRICK.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> PROSPIT_CRACKED_CORE_BRICK_WALL = ItemBlockPair.register("prospit_cracked_core_brick_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(PROSPIT_CRACKED_BRICK.asBlock())));
	
	public static final ItemBlockPair<StairBlock,BlockItem> PROSPIT_CORE_TILE_STAIRS = ItemBlockPair.register("prospit_core_tile_stairs",
			() -> new StairBlock(PROSPIT_CORE_TILES.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(PROSPIT_TARNISHED_BRICK.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> PROSPIT_CORE_TILE_SLAB = ItemBlockPair.register("prospit_core_tile_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(PROSPIT_TARNISHED_BRICK.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> PROSPIT_CORE_TILE_WALL = ItemBlockPair.register("prospit_core_tile_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(PROSPIT_TARNISHED_BRICK.asBlock())));
	
	
	//DERSE
	//surface block
	public static final ItemBlockPair<Block,BlockItem> DERSE_PLATING = ItemBlockPair.register("derse_plating",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.DIRT)
					.mapColor(MapColor.GOLD) //todo custom map colors?? otherwise derse maps will be solid yellow
					.instrument(NoteBlockInstrument.XYLOPHONE)
					.sound(SoundType.COPPER_GRATE) //todo 'walking on metallic surface' sfx
					.requiresCorrectToolForDrops()
			));
	
	public static final ItemBlockPair<Block,BlockItem> DERSE_PLATED_PATH = ItemBlockPair.register("derse_plated_path",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.DIRT_PATH)
					.mapColor(MapColor.COLOR_YELLOW) //todo custom map colors?? otherwise derse maps will be solid yellow
					.instrument(NoteBlockInstrument.XYLOPHONE)
					.sound(SoundType.COPPER_GRATE) //todo 'muted walking on metallic surface' sfx
					.requiresCorrectToolForDrops()
			));
	
	//underground 1 - stone equivalent
	//raw stone
	public static final ItemBlockPair<Block,BlockItem> DERSE_METALLITH = ItemBlockPair.register("derse_metallith",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.STONE)
					.mapColor(MapColor.SAND)
					.instrument(NoteBlockInstrument.BANJO)
					.sound(SoundType.COPPER_BULB) //todo 'different walking on metallix surface' sfx
			));
	public static final ItemBlockPair<StairBlock,BlockItem> DERSE_METALLITH_STAIRS = ItemBlockPair.register("derse_metallith_stairs",
			() -> new StairBlock(DERSE_METALLITH.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(DERSE_METALLITH.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> DERSE_METALLITH_SLAB = ItemBlockPair.register("derse_metallith_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(DERSE_METALLITH.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> DERSE_METALLITH_WALL = ItemBlockPair.register("derse_metallith_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(DERSE_METALLITH.asBlock())));
	
	//cobblestone
	public static final ItemBlockPair<Block,BlockItem> DERSE_ROUGH_METALLITH = ItemBlockPair.register("derse_rough_metallith",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.COBBLESTONE)
					.mapColor(MapColor.SAND)
					.instrument(NoteBlockInstrument.BANJO)
					.sound(SoundType.COPPER_BULB) //todo 'different walking on metallix surface' sfx
			));
	public static final ItemBlockPair<StairBlock,BlockItem> DERSE_ROUGH_METALLITH_STAIRS = ItemBlockPair.register("derse_rough_metallith_stairs",
			() -> new StairBlock(DERSE_ROUGH_METALLITH.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(DERSE_ROUGH_METALLITH.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> DERSE_ROUGH_METALLITH_SLAB = ItemBlockPair.register("derse_rough_metallith_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(DERSE_ROUGH_METALLITH.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> DERSE_ROUGH_METALLITH_WALL = ItemBlockPair.register("derse_rough_metallith_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(DERSE_ROUGH_METALLITH.asBlock())));
	
	//smooth stone
	public static final ItemBlockPair<Block,BlockItem> DERSE_REFINED_METALLITH = ItemBlockPair.register("derse_refined_metallith",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.SMOOTH_STONE)
					.mapColor(MapColor.SAND)
					.instrument(NoteBlockInstrument.BANJO)
					.sound(SoundType.COPPER_BULB) //todo 'different walking on metallix surface' sfx
			));
	public static final ItemBlockPair<StairBlock,BlockItem> DERSE_REFINED_METALLITH_STAIRS = ItemBlockPair.register("derse_refined_metallith_stairs",
			() -> new StairBlock(DERSE_ROUGH_METALLITH.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(DERSE_REFINED_METALLITH.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> DERSE_REFINED_METALLITH_SLAB = ItemBlockPair.register("derse_refined_metallith_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(DERSE_REFINED_METALLITH.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> DERSE_REFINED_METALLITH_WALL = ItemBlockPair.register("derse_refined_metallith_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(DERSE_REFINED_METALLITH.asBlock())));
	
	//the idea is that the metallith is developed naturally when the moon is created. mining it up results in
	//rough metallith that can be smelted into raw metallith then refined metallith
	
	//underground 1 - deepslate equivalent
	//deepslate
	public static final ItemBlockPair<Block,BlockItem> DERSE_CORE = ItemBlockPair.register("derse_core",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.DEEPSLATE)
					.mapColor(MapColor.SAND)
					.instrument(NoteBlockInstrument.BANJO)
					.sound(SoundType.HEAVY_CORE) //todo 'different walking on metallix surface' sfx
			));
	public static final ItemBlockPair<StairBlock,BlockItem> DERSE_CORE_STAIRS = ItemBlockPair.register("derse_core_stairs",
			() -> new StairBlock(DERSE_CORE.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(DERSE_CORE.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> DERSE_CORE_SLAB = ItemBlockPair.register("derse_core_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(DERSE_CORE.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> DERSE_CORE_WALL = ItemBlockPair.register("derse_core_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(DERSE_CORE.asBlock())));
	
	//cobbled deepslate
	public static final ItemBlockPair<Block,BlockItem> DERSE_ROUGH_CORE = ItemBlockPair.register("derse_rough_core",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE)
					.mapColor(MapColor.SAND)
					.instrument(NoteBlockInstrument.BANJO)
					.sound(SoundType.HEAVY_CORE) //todo 'different walking on metallix surface' sfx
			));
	public static final ItemBlockPair<StairBlock,BlockItem> DERSE_ROUGH_CORE_STAIRS = ItemBlockPair.register("derse_rough_core_stairs",
			() -> new StairBlock(DERSE_ROUGH_CORE.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(DERSE_ROUGH_CORE.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> DERSE_ROUGH_CORE_SLAB = ItemBlockPair.register("derse_rough_core_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(DERSE_ROUGH_CORE.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> DERSE_ROUGH_CORE_WALL = ItemBlockPair.register("derse_rough_core_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(DERSE_ROUGH_CORE.asBlock())));
	
	//polished deepslate
	public static final ItemBlockPair<Block,BlockItem> DERSE_REFINED_CORE = ItemBlockPair.register("derse_refined_core",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.POLISHED_DEEPSLATE)
					.mapColor(MapColor.SAND)
					.instrument(NoteBlockInstrument.BANJO)
					.sound(SoundType.HEAVY_CORE) //todo 'different walking on metallix surface' sfx
			));
	public static final ItemBlockPair<StairBlock,BlockItem> DERSE_REFINED_CORE_STAIRS = ItemBlockPair.register("derse_refined_core_stairs",
			() -> new StairBlock(DERSE_ROUGH_CORE.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(DERSE_REFINED_CORE.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> DERSE_REFINED_CORE_SLAB = ItemBlockPair.register("derse_refined_core_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(DERSE_REFINED_CORE.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> DERSE_REFINED_CORE_WALL = ItemBlockPair.register("derse_refined_core_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(DERSE_REFINED_CORE.asBlock())));
	
	//ORES
	public static final ItemBlockPair<Block,BlockItem> DERSE_METALLITH_CRUXITE_ORE = ItemBlockPair.register("derse_metallith_cruxite_ore",
			() -> new Block(Block.Properties.ofFullCopy(MSBlocks.STONE_CRUXITE_ORE.get())));
	public static final ItemBlockPair<Block,BlockItem> DERSE_METALLITH_URANIUM_ORE = ItemBlockPair.register("derse_metallith_uranium_ore",
			() -> new Block(Block.Properties.ofFullCopy(MSBlocks.STONE_URANIUM_ORE.get())));
	public static final ItemBlockPair<Block,BlockItem> DERSE_METALLITH_DERSE_ORE = ItemBlockPair.register("derse_metallith_derse_ore",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.GOLD_ORE)));
	
	
	public static final ItemBlockPair<Block,BlockItem> DERSE_CORE_CRUXITE_ORE = ItemBlockPair.register("derse_core_cruxite_ore",
			() -> new Block(Block.Properties.ofFullCopy(MSBlocks.STONE_CRUXITE_ORE.get())));
	public static final ItemBlockPair<Block,BlockItem> DERSE_CORE_URANIUM_ORE = ItemBlockPair.register("derse_core_uranium_ore",
			() -> new Block(Block.Properties.ofFullCopy(MSBlocks.DEEPSLATE_URANIUM_ORE.get())));
	public static final ItemBlockPair<Block,BlockItem> DERSE_CORE_DERSE_ORE = ItemBlockPair.register("derse_core_derse_ore",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.DEEPSLATE_GOLD_ORE)));
	
	public static final ItemBlockPair<Block,BlockItem> DERSE_INGOT_BLOCK = ItemBlockPair.register("derse_ingot_block",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.GOLD_BLOCK))); // todo added custom block properties
	public static final ItemBlockPair<Block,BlockItem> DERSE_ORE_BLOCK = ItemBlockPair.register("derse_ore_block",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.RAW_GOLD_BLOCK))); // todo added custom block properties
	
	
	//misc
	public static final ItemBlockPair<Block,BlockItem> DERSE_CABLE = ItemBlockPair.register("derse_cable",
			() -> new Block(Block.Properties.ofFullCopy(DERSE_INGOT_BLOCK.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> DERSE_THICK_CABLE = ItemBlockPair.register("derse_thick_cable",
			() -> new Block(Block.Properties.ofFullCopy(DERSE_INGOT_BLOCK.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> DERSE_PILLAR = ItemBlockPair.register("derse_pillar",
			() -> new Block(Block.Properties.ofFullCopy(DERSE_INGOT_BLOCK.asBlock())));
	public static final ItemBlockPair<DoorBlock,BlockItem> DERSE_DOOR = ItemBlockPair.register("derse_door",
			() -> new DoorBlock(MSBlockSetType.DERSE, Block.Properties.ofFullCopy(Blocks.COPPER_DOOR)));
	public static final ItemBlockPair<TrapDoorBlock,BlockItem> DERSE_TRAPDOOR = ItemBlockPair.register("derse_trapdoor",
			() -> new TrapDoorBlock(MSBlockSetType.DERSE, Block.Properties.ofFullCopy(Blocks.COPPER_TRAPDOOR)));
	
	//stone bricks (crafted with metallith)
	public static final ItemBlockPair<Block,BlockItem> DERSE_BRICK = ItemBlockPair.register("derse_brick",
			() -> new Block(Block.Properties.ofFullCopy(DERSE_METALLITH.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> DERSE_CRACKED_BRICK = ItemBlockPair.register("derse_cracked_brick",
			() -> new Block(Block.Properties.ofFullCopy(DERSE_METALLITH.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> DERSE_BIG_BRICK = ItemBlockPair.register("derse_big_brick",
			() -> new Block(Block.Properties.ofFullCopy(DERSE_METALLITH.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> DERSE_CHISELED_BRICK = ItemBlockPair.register("derse_chiseled_brick",
			() -> new Block(Block.Properties.ofFullCopy(DERSE_METALLITH.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> DERSE_PAVING_BRICK = ItemBlockPair.register("derse_paving_brick",
			() -> new Block(Block.Properties.ofFullCopy(DERSE_METALLITH.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> DERSE_FANCY_BRICK = ItemBlockPair.register("derse_fancy_brick",
			() -> new Block(Block.Properties.ofFullCopy(DERSE_METALLITH.asBlock())));
	public static final ItemBlockPair<Block,BlockItem> DERSE_TARNISHED_BRICK = ItemBlockPair.register("derse_tarnished_brick",
			() -> new Block(Block.Properties.ofFullCopy(DERSE_METALLITH.asBlock()))); //mossy brick equiv
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
	
	//deepslate bricks (crafted with core)
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
			() -> new StairBlock(DERSE_BRICK.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(DERSE_BRICK.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> DERSE_CORE_BRICK_SLAB = ItemBlockPair.register("derse_core_brick_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(DERSE_BRICK.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> DERSE_CORE_BRICK_WALL = ItemBlockPair.register("derse_core_brick_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(DERSE_BRICK.asBlock())));
	
	public static final ItemBlockPair<StairBlock,BlockItem> DERSE_CRACKED_CORE_BRICK_STAIRS = ItemBlockPair.register("derse_cracked_core_brick_stairs",
			() -> new StairBlock(DERSE_CRACKED_BRICK.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(DERSE_CRACKED_BRICK.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> DERSE_CRACKED_CORE_BRICK_SLAB = ItemBlockPair.register("derse_cracked_core_brick_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(DERSE_CRACKED_BRICK.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> DERSE_CRACKED_CORE_BRICK_WALL = ItemBlockPair.register("derse_cracked_core_brick_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(DERSE_CRACKED_BRICK.asBlock())));
	
	public static final ItemBlockPair<StairBlock,BlockItem> DERSE_CORE_TILE_STAIRS = ItemBlockPair.register("derse_core_tile_stairs",
			() -> new StairBlock(DERSE_CORE_TILES.asBlock().defaultBlockState(), Block.Properties.ofFullCopy(DERSE_TARNISHED_BRICK.asBlock())));
	public static final ItemBlockPair<SlabBlock,BlockItem> DERSE_CORE_TILE_SLAB = ItemBlockPair.register("derse_core_tile_slab",
			() -> new SlabBlock(Block.Properties.ofFullCopy(DERSE_TARNISHED_BRICK.asBlock())));
	public static final ItemBlockPair<WallBlock,BlockItem> DERSE_CORE_TILE_WALL = ItemBlockPair.register("derse_core_tile_wall",
			() -> new WallBlock(Block.Properties.ofFullCopy(DERSE_TARNISHED_BRICK.asBlock())));
}
