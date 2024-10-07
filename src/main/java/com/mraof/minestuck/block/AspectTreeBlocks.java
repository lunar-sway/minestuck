package com.mraof.minestuck.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.plant.AspectSaplingBlock;
import com.mraof.minestuck.block.plant.FlammableLeavesBlock;
import com.mraof.minestuck.block.plant.FlammableLogBlock;
import com.mraof.minestuck.block.plant.StrippableFlammableLogBlock;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.world.gen.feature.MSCFeatures;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.Optional;

import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.ofFullCopy;

public final class AspectTreeBlocks
{
	public static void init() {}
	
	//Blood
	
	public static final ItemBlockPair<Block, BlockItem> BLOOD_ASPECT_LOG = ItemBlockPair.register("blood_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.BLOOD_ASPECT_STRIPPED_LOG.asBlock().defaultBlockState()));
	
	public static final ItemBlockPair<Block, BlockItem> BLOOD_ASPECT_WOOD = ItemBlockPair.register("blood_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.BLOOD_ASPECT_STRIPPED_WOOD.asBlock().defaultBlockState()));
	
	public static final ItemBlockPair<Block, BlockItem> BLOOD_ASPECT_STRIPPED_LOG = ItemBlockPair.register("blood_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> BLOOD_ASPECT_STRIPPED_WOOD = ItemBlockPair.register("blood_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> BLOOD_ASPECT_LEAVES = ItemBlockPair.register("blood_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	
	public static final ItemBlockPair<Block, BlockItem> BLOOD_ASPECT_SAPLING = ItemBlockPair.register("blood_aspect_sapling",
			() -> new AspectSaplingBlock(new TreeGrower(Minestuck.id("blood_aspect").toString(), Optional.empty(), Optional.of(MSCFeatures.BLOOD_TREE), Optional.empty()),
					Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));

	public static final DeferredBlock<Block> POTTED_BLOOD_ASPECT_SAPLING = MSBlocks.REGISTER.register("potted_blood_aspect_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, BLOOD_ASPECT_SAPLING.blockHolder(), ofFullCopy(Blocks.POTTED_OAK_SAPLING)));
	
	public static final ItemBlockPair<Block, BlockItem> BLOOD_ASPECT_PLANKS = ItemBlockPair.register("blood_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> BLOOD_ASPECT_CARVED_PLANKS = ItemBlockPair.register("blood_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<StairBlock, BlockItem> BLOOD_ASPECT_STAIRS = ItemBlockPair.register("blood_aspect_stairs",
			() -> new StairBlock(() -> BLOOD_ASPECT_PLANKS.asBlock().defaultBlockState(), ofFullCopy(BLOOD_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<SlabBlock, BlockItem> BLOOD_ASPECT_SLAB = ItemBlockPair.register("blood_aspect_slab",
			() -> new SlabBlock(ofFullCopy(BLOOD_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<FenceBlock, BlockItem>  BLOOD_ASPECT_FENCE = ItemBlockPair.register("blood_aspect_fence",
			() -> new FenceBlock(ofFullCopy(BLOOD_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<FenceGateBlock, BlockItem> BLOOD_ASPECT_FENCE_GATE = ItemBlockPair.register("blood_aspect_fence_gate",
			() -> new FenceGateBlock(ofFullCopy(BLOOD_ASPECT_PLANKS.asBlock()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	
	public static final ItemBlockPair<DoorBlock, BlockItem> BLOOD_ASPECT_DOOR = ItemBlockPair.register("blood_aspect_door",
			() -> new DoorBlock(MSBlockSetType.BLOOD, ofFullCopy(Blocks.OAK_DOOR)));
	
	public static final ItemBlockPair<TrapDoorBlock, BlockItem> BLOOD_ASPECT_TRAPDOOR = ItemBlockPair.register("blood_aspect_trapdoor",
			() -> new TrapDoorBlock(MSBlockSetType.BLOOD, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	
	public static final ItemBlockPair<PressurePlateBlock, BlockItem> BLOOD_ASPECT_PRESSURE_PLATE = ItemBlockPair.register("blood_aspect_pressure_plate",
			() -> new PressurePlateBlock(MSBlockSetType.BLOOD, ofFullCopy(BLOOD_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<ButtonBlock, BlockItem> BLOOD_ASPECT_BUTTON = ItemBlockPair.register("blood_aspect_button",
			() -> new ButtonBlock(MSBlockSetType.BLOOD, 10, ofFullCopy(BLOOD_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<Block, BlockItem> BLOOD_ASPECT_BOOKSHELF = ItemBlockPair.register("blood_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> BLOOD_ASPECT_LADDER = ItemBlockPair.register("blood_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	
	public static final DeferredBlock<StandingSignBlock> BLOOD_ASPECT_SIGN = MSBlocks.REGISTER.register("blood_aspect_sign",
			() -> new MSStandingSignBlock(MSWoodTypes.BLOOD, ofFullCopy(Blocks.OAK_SIGN)));
	public static final DeferredBlock<WallSignBlock> BLOOD_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("blood_aspect_wall_sign",
			() -> new MSWallSignBlock(MSWoodTypes.BLOOD, ofFullCopy(Blocks.OAK_WALL_SIGN)));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("blood_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), BLOOD_ASPECT_SIGN.get(), BLOOD_ASPECT_WALL_SIGN.get()));
	public static final DeferredBlock<Block> BLOOD_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("blood_aspect_hanging_sign",
			() -> new MSHangingSignBlock(MSWoodTypes.BLOOD, ofFullCopy(Blocks.OAK_HANGING_SIGN)));
	public static final DeferredBlock<Block> BLOOD_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("blood_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(MSWoodTypes.BLOOD, ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN)));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("blood_aspect_hanging_sign",
			() -> new HangingSignItem(BLOOD_ASPECT_HANGING_SIGN.get(), BLOOD_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
	
	//Breath
	
	public static final ItemBlockPair<Block, BlockItem> BREATH_ASPECT_LOG = ItemBlockPair.register("breath_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.BREATH_ASPECT_STRIPPED_LOG.asBlock().defaultBlockState()));
	
	public static final ItemBlockPair<Block, BlockItem> BREATH_ASPECT_WOOD = ItemBlockPair.register("breath_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.BREATH_ASPECT_STRIPPED_WOOD.asBlock().defaultBlockState()));
	
	public static final ItemBlockPair<Block, BlockItem> BREATH_ASPECT_STRIPPED_LOG = ItemBlockPair.register("breath_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> BREATH_ASPECT_STRIPPED_WOOD = ItemBlockPair.register("breath_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> BREATH_ASPECT_LEAVES = ItemBlockPair.register("breath_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	
	public static final ItemBlockPair<Block, BlockItem> BREATH_ASPECT_SAPLING = ItemBlockPair.register("breath_aspect_sapling",
			() -> new AspectSaplingBlock(new TreeGrower(Minestuck.id("breath_aspect").toString(), Optional.empty(), Optional.of(MSCFeatures.BREATH_TREE), Optional.empty()),
					Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	
	public static final DeferredBlock<Block> POTTED_BREATH_ASPECT_SAPLING = MSBlocks.REGISTER.register("potted_breath_aspect_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, BREATH_ASPECT_SAPLING.blockHolder(), ofFullCopy(Blocks.POTTED_OAK_SAPLING)));
	
	public static final ItemBlockPair<Block, BlockItem> BREATH_ASPECT_PLANKS = ItemBlockPair.register("breath_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> BREATH_ASPECT_CARVED_PLANKS = ItemBlockPair.register("breath_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<StairBlock, BlockItem> BREATH_ASPECT_STAIRS = ItemBlockPair.register("breath_aspect_stairs",
			() -> new StairBlock(() -> BREATH_ASPECT_PLANKS.asBlock().defaultBlockState(), ofFullCopy(BREATH_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<SlabBlock, BlockItem> BREATH_ASPECT_SLAB = ItemBlockPair.register("breath_aspect_slab",
			() -> new SlabBlock(ofFullCopy(BREATH_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<FenceBlock, BlockItem>  BREATH_ASPECT_FENCE = ItemBlockPair.register("breath_aspect_fence",
			() -> new FenceBlock(ofFullCopy(BREATH_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<FenceGateBlock, BlockItem> BREATH_ASPECT_FENCE_GATE = ItemBlockPair.register("breath_aspect_fence_gate",
			() -> new FenceGateBlock(ofFullCopy(BREATH_ASPECT_PLANKS.asBlock()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	
	public static final ItemBlockPair<DoorBlock, BlockItem> BREATH_ASPECT_DOOR = ItemBlockPair.register("breath_aspect_door",
			() -> new DoorBlock(MSBlockSetType.BREATH, ofFullCopy(Blocks.OAK_DOOR)));
	
	public static final ItemBlockPair<TrapDoorBlock, BlockItem> BREATH_ASPECT_TRAPDOOR = ItemBlockPair.register("breath_aspect_trapdoor",
			() -> new TrapDoorBlock(MSBlockSetType.BREATH, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	
	public static final ItemBlockPair<PressurePlateBlock, BlockItem> BREATH_ASPECT_PRESSURE_PLATE = ItemBlockPair.register("breath_aspect_pressure_plate",
			() -> new PressurePlateBlock(MSBlockSetType.BREATH, ofFullCopy(BREATH_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<ButtonBlock, BlockItem> BREATH_ASPECT_BUTTON = ItemBlockPair.register("breath_aspect_button",
			() -> new ButtonBlock(MSBlockSetType.BREATH, 10, ofFullCopy(BREATH_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<Block, BlockItem> BREATH_ASPECT_BOOKSHELF = ItemBlockPair.register("breath_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> BREATH_ASPECT_LADDER = ItemBlockPair.register("breath_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	
	public static final DeferredBlock<StandingSignBlock> BREATH_ASPECT_SIGN = MSBlocks.REGISTER.register("breath_aspect_sign",
			() -> new MSStandingSignBlock(MSWoodTypes.BREATH, ofFullCopy(Blocks.OAK_SIGN)));
	public static final DeferredBlock<WallSignBlock> BREATH_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("breath_aspect_wall_sign",
			() -> new MSWallSignBlock(MSWoodTypes.BREATH, ofFullCopy(Blocks.OAK_WALL_SIGN)));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("breath_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), BREATH_ASPECT_SIGN.get(), BREATH_ASPECT_WALL_SIGN.get()));
	public static final DeferredBlock<Block> BREATH_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("breath_aspect_hanging_sign",
			() -> new MSHangingSignBlock(MSWoodTypes.BREATH, ofFullCopy(Blocks.OAK_HANGING_SIGN)));
	public static final DeferredBlock<Block> BREATH_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("breath_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(MSWoodTypes.BREATH, ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN)));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("breath_aspect_hanging_sign",
			() -> new HangingSignItem(BREATH_ASPECT_HANGING_SIGN.get(), BREATH_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
	//Doom
	
	public static final ItemBlockPair<Block, BlockItem> DOOM_ASPECT_LOG = ItemBlockPair.register("doom_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.DOOM_ASPECT_STRIPPED_LOG.asBlock().defaultBlockState()));
	
	public static final ItemBlockPair<Block, BlockItem> DOOM_ASPECT_WOOD = ItemBlockPair.register("doom_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.DOOM_ASPECT_STRIPPED_WOOD.asBlock().defaultBlockState()));
	
	public static final ItemBlockPair<Block, BlockItem> DOOM_ASPECT_STRIPPED_LOG = ItemBlockPair.register("doom_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> DOOM_ASPECT_STRIPPED_WOOD = ItemBlockPair.register("doom_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> DOOM_ASPECT_LEAVES = ItemBlockPair.register("doom_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	
	public static final ItemBlockPair<Block, BlockItem> DOOM_ASPECT_SAPLING = ItemBlockPair.register("doom_aspect_sapling",
			() -> new AspectSaplingBlock(new TreeGrower(Minestuck.id("doom_aspect").toString(), Optional.empty(), Optional.of(MSCFeatures.DOOM_TREE), Optional.empty()),
					Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	
	public static final DeferredBlock<Block> POTTED_DOOM_ASPECT_SAPLING = MSBlocks.REGISTER.register("potted_doom_aspect_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, DOOM_ASPECT_SAPLING.blockHolder(), ofFullCopy(Blocks.POTTED_OAK_SAPLING)));
	
	public static final ItemBlockPair<Block, BlockItem> DOOM_ASPECT_PLANKS = ItemBlockPair.register("doom_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> DOOM_ASPECT_CARVED_PLANKS = ItemBlockPair.register("doom_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<StairBlock, BlockItem> DOOM_ASPECT_STAIRS = ItemBlockPair.register("doom_aspect_stairs",
			() -> new StairBlock(() -> DOOM_ASPECT_PLANKS.asBlock().defaultBlockState(), ofFullCopy(DOOM_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<SlabBlock, BlockItem> DOOM_ASPECT_SLAB = ItemBlockPair.register("doom_aspect_slab",
			() -> new SlabBlock(ofFullCopy(DOOM_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<FenceBlock, BlockItem>  DOOM_ASPECT_FENCE = ItemBlockPair.register("doom_aspect_fence",
			() -> new FenceBlock(ofFullCopy(DOOM_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<FenceGateBlock, BlockItem> DOOM_ASPECT_FENCE_GATE = ItemBlockPair.register("doom_aspect_fence_gate",
			() -> new FenceGateBlock(ofFullCopy(DOOM_ASPECT_PLANKS.asBlock()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	
	public static final ItemBlockPair<DoorBlock, BlockItem> DOOM_ASPECT_DOOR = ItemBlockPair.register("doom_aspect_door",
			() -> new DoorBlock(MSBlockSetType.DOOM, ofFullCopy(Blocks.OAK_DOOR)));
	
	public static final ItemBlockPair<TrapDoorBlock, BlockItem> DOOM_ASPECT_TRAPDOOR = ItemBlockPair.register("doom_aspect_trapdoor",
			() -> new TrapDoorBlock(MSBlockSetType.DOOM, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	
	public static final ItemBlockPair<PressurePlateBlock, BlockItem> DOOM_ASPECT_PRESSURE_PLATE = ItemBlockPair.register("doom_aspect_pressure_plate",
			() -> new PressurePlateBlock(MSBlockSetType.DOOM, ofFullCopy(DOOM_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<ButtonBlock, BlockItem> DOOM_ASPECT_BUTTON = ItemBlockPair.register("doom_aspect_button",
			() -> new ButtonBlock(MSBlockSetType.DOOM, 10, ofFullCopy(DOOM_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<Block, BlockItem> DOOM_ASPECT_BOOKSHELF = ItemBlockPair.register("doom_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> DOOM_ASPECT_LADDER = ItemBlockPair.register("doom_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	
	public static final DeferredBlock<StandingSignBlock> DOOM_ASPECT_SIGN = MSBlocks.REGISTER.register("doom_aspect_sign",
			() -> new MSStandingSignBlock(MSWoodTypes.DOOM, ofFullCopy(Blocks.OAK_SIGN)));
	public static final DeferredBlock<WallSignBlock> DOOM_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("doom_aspect_wall_sign",
			() -> new MSWallSignBlock(MSWoodTypes.DOOM, ofFullCopy(Blocks.OAK_WALL_SIGN)));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("doom_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), DOOM_ASPECT_SIGN.get(), DOOM_ASPECT_WALL_SIGN.get()));
	public static final DeferredBlock<Block> DOOM_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("doom_aspect_hanging_sign",
			() -> new MSHangingSignBlock(MSWoodTypes.DOOM, ofFullCopy(Blocks.OAK_HANGING_SIGN)));
	public static final DeferredBlock<Block> DOOM_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("doom_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(MSWoodTypes.DOOM, ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN)));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("doom_aspect_hanging_sign",
			() -> new HangingSignItem(DOOM_ASPECT_HANGING_SIGN.get(), DOOM_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
	//Heart
	
	public static final ItemBlockPair<Block, BlockItem> HEART_ASPECT_LOG = ItemBlockPair.register("heart_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.HEART_ASPECT_STRIPPED_LOG.asBlock().defaultBlockState()));
	
	public static final ItemBlockPair<Block, BlockItem> HEART_ASPECT_WOOD = ItemBlockPair.register("heart_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.HEART_ASPECT_STRIPPED_WOOD.asBlock().defaultBlockState()));
	
	public static final ItemBlockPair<Block, BlockItem> HEART_ASPECT_STRIPPED_LOG = ItemBlockPair.register("heart_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> HEART_ASPECT_STRIPPED_WOOD = ItemBlockPair.register("heart_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> HEART_ASPECT_LEAVES = ItemBlockPair.register("heart_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	
	public static final ItemBlockPair<Block, BlockItem> HEART_ASPECT_SAPLING = ItemBlockPair.register("heart_aspect_sapling",
			() -> new AspectSaplingBlock(new TreeGrower(Minestuck.id("heart_aspect").toString(), Optional.empty(), Optional.of(MSCFeatures.HEART_TREE), Optional.empty()),
					Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	
	public static final DeferredBlock<Block> POTTED_HEART_ASPECT_SAPLING = MSBlocks.REGISTER.register("potted_heart_aspect_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, HEART_ASPECT_SAPLING.blockHolder(), ofFullCopy(Blocks.POTTED_OAK_SAPLING)));
	
	public static final ItemBlockPair<Block, BlockItem> HEART_ASPECT_PLANKS = ItemBlockPair.register("heart_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> HEART_ASPECT_CARVED_PLANKS = ItemBlockPair.register("heart_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<StairBlock, BlockItem> HEART_ASPECT_STAIRS = ItemBlockPair.register("heart_aspect_stairs",
			() -> new StairBlock(() -> HEART_ASPECT_PLANKS.asBlock().defaultBlockState(), ofFullCopy(HEART_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<SlabBlock, BlockItem> HEART_ASPECT_SLAB = ItemBlockPair.register("heart_aspect_slab",
			() -> new SlabBlock(ofFullCopy(HEART_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<FenceBlock, BlockItem>  HEART_ASPECT_FENCE = ItemBlockPair.register("heart_aspect_fence",
			() -> new FenceBlock(ofFullCopy(HEART_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<FenceGateBlock, BlockItem> HEART_ASPECT_FENCE_GATE = ItemBlockPair.register("heart_aspect_fence_gate",
			() -> new FenceGateBlock(ofFullCopy(HEART_ASPECT_PLANKS.asBlock()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	
	public static final ItemBlockPair<DoorBlock, BlockItem> HEART_ASPECT_DOOR = ItemBlockPair.register("heart_aspect_door",
			() -> new DoorBlock(MSBlockSetType.HEART, ofFullCopy(Blocks.OAK_DOOR)));
	
	public static final ItemBlockPair<TrapDoorBlock, BlockItem> HEART_ASPECT_TRAPDOOR = ItemBlockPair.register("heart_aspect_trapdoor",
			() -> new TrapDoorBlock(MSBlockSetType.HEART, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	
	public static final ItemBlockPair<PressurePlateBlock, BlockItem> HEART_ASPECT_PRESSURE_PLATE = ItemBlockPair.register("heart_aspect_pressure_plate",
			() -> new PressurePlateBlock(MSBlockSetType.HEART, ofFullCopy(HEART_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<ButtonBlock, BlockItem> HEART_ASPECT_BUTTON = ItemBlockPair.register("heart_aspect_button",
			() -> new ButtonBlock(MSBlockSetType.HEART, 10, ofFullCopy(HEART_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<Block, BlockItem> HEART_ASPECT_BOOKSHELF = ItemBlockPair.register("heart_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> HEART_ASPECT_LADDER = ItemBlockPair.register("heart_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	
	public static final DeferredBlock<StandingSignBlock> HEART_ASPECT_SIGN = MSBlocks.REGISTER.register("heart_aspect_sign",
			() -> new MSStandingSignBlock(MSWoodTypes.HEART, ofFullCopy(Blocks.OAK_SIGN)));
	public static final DeferredBlock<WallSignBlock> HEART_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("heart_aspect_wall_sign",
			() -> new MSWallSignBlock(MSWoodTypes.HEART, ofFullCopy(Blocks.OAK_WALL_SIGN)));
	public static final DeferredItem<BlockItem> HEART_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("heart_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), HEART_ASPECT_SIGN.get(), HEART_ASPECT_WALL_SIGN.get()));
	public static final DeferredBlock<Block> HEART_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("heart_aspect_hanging_sign",
			() -> new MSHangingSignBlock(MSWoodTypes.HEART, ofFullCopy(Blocks.OAK_HANGING_SIGN)));
	public static final DeferredBlock<Block> HEART_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("heart_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(MSWoodTypes.HEART, ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN)));
	public static final DeferredItem<BlockItem> HEART_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("heart_aspect_hanging_sign",
			() -> new HangingSignItem(HEART_ASPECT_HANGING_SIGN.get(), HEART_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
	//Hope
	
	public static final ItemBlockPair<Block, BlockItem> HOPE_ASPECT_LOG = ItemBlockPair.register("hope_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.HOPE_ASPECT_STRIPPED_LOG.asBlock().defaultBlockState()));
	
	public static final ItemBlockPair<Block, BlockItem> HOPE_ASPECT_WOOD = ItemBlockPair.register("hope_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.HOPE_ASPECT_STRIPPED_WOOD.asBlock().defaultBlockState()));
	
	public static final ItemBlockPair<Block, BlockItem> HOPE_ASPECT_STRIPPED_LOG = ItemBlockPair.register("hope_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> HOPE_ASPECT_STRIPPED_WOOD = ItemBlockPair.register("hope_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> HOPE_ASPECT_LEAVES = ItemBlockPair.register("hope_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	
	public static final ItemBlockPair<Block, BlockItem> HOPE_ASPECT_SAPLING = ItemBlockPair.register("hope_aspect_sapling",
			() -> new AspectSaplingBlock(new TreeGrower(Minestuck.id("hope_aspect").toString(), Optional.empty(), Optional.of(MSCFeatures.HOPE_TREE), Optional.empty()),
					Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	
	public static final DeferredBlock<Block> POTTED_HOPE_ASPECT_SAPLING = MSBlocks.REGISTER.register("potted_hope_aspect_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, HOPE_ASPECT_SAPLING.blockHolder(), ofFullCopy(Blocks.POTTED_OAK_SAPLING)));
	
	public static final ItemBlockPair<Block, BlockItem> HOPE_ASPECT_PLANKS = ItemBlockPair.register("hope_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> HOPE_ASPECT_CARVED_PLANKS = ItemBlockPair.register("hope_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<StairBlock, BlockItem> HOPE_ASPECT_STAIRS = ItemBlockPair.register("hope_aspect_stairs",
			() -> new StairBlock(() -> HOPE_ASPECT_PLANKS.asBlock().defaultBlockState(), ofFullCopy(HOPE_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<SlabBlock, BlockItem> HOPE_ASPECT_SLAB = ItemBlockPair.register("hope_aspect_slab",
			() -> new SlabBlock(ofFullCopy(HOPE_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<FenceBlock, BlockItem>  HOPE_ASPECT_FENCE = ItemBlockPair.register("hope_aspect_fence",
			() -> new FenceBlock(ofFullCopy(HOPE_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<FenceGateBlock, BlockItem> HOPE_ASPECT_FENCE_GATE = ItemBlockPair.register("hope_aspect_fence_gate",
			() -> new FenceGateBlock(ofFullCopy(HOPE_ASPECT_PLANKS.asBlock()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	
	public static final ItemBlockPair<DoorBlock, BlockItem> HOPE_ASPECT_DOOR = ItemBlockPair.register("hope_aspect_door",
			() -> new DoorBlock(MSBlockSetType.HOPE, ofFullCopy(Blocks.OAK_DOOR)));
	
	public static final ItemBlockPair<TrapDoorBlock, BlockItem> HOPE_ASPECT_TRAPDOOR = ItemBlockPair.register("hope_aspect_trapdoor",
			() -> new TrapDoorBlock(MSBlockSetType.HOPE, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	
	public static final ItemBlockPair<PressurePlateBlock, BlockItem> HOPE_ASPECT_PRESSURE_PLATE = ItemBlockPair.register("hope_aspect_pressure_plate",
			() -> new PressurePlateBlock(MSBlockSetType.HOPE, ofFullCopy(HOPE_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<ButtonBlock, BlockItem> HOPE_ASPECT_BUTTON = ItemBlockPair.register("hope_aspect_button",
			() -> new ButtonBlock(MSBlockSetType.HOPE, 10, ofFullCopy(HOPE_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<Block, BlockItem> HOPE_ASPECT_BOOKSHELF = ItemBlockPair.register("hope_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> HOPE_ASPECT_LADDER = ItemBlockPair.register("hope_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	
	public static final DeferredBlock<StandingSignBlock> HOPE_ASPECT_SIGN = MSBlocks.REGISTER.register("hope_aspect_sign",
			() -> new MSStandingSignBlock(MSWoodTypes.HOPE, ofFullCopy(Blocks.OAK_SIGN)));
	public static final DeferredBlock<WallSignBlock> HOPE_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("hope_aspect_wall_sign",
			() -> new MSWallSignBlock(MSWoodTypes.HOPE, ofFullCopy(Blocks.OAK_WALL_SIGN)));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("hope_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), HOPE_ASPECT_SIGN.get(), HOPE_ASPECT_WALL_SIGN.get()));
	public static final DeferredBlock<Block> HOPE_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("hope_aspect_hanging_sign",
			() -> new MSHangingSignBlock(MSWoodTypes.HOPE, ofFullCopy(Blocks.OAK_HANGING_SIGN)));
	public static final DeferredBlock<Block> HOPE_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("hope_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(MSWoodTypes.HOPE, ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN)));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("hope_aspect_hanging_sign",
			() -> new HangingSignItem(HOPE_ASPECT_HANGING_SIGN.get(), HOPE_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
	//Life
	
	public static final ItemBlockPair<Block, BlockItem> LIFE_ASPECT_LOG = ItemBlockPair.register("life_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.LIFE_ASPECT_STRIPPED_LOG.asBlock().defaultBlockState()));
	
	public static final ItemBlockPair<Block, BlockItem> LIFE_ASPECT_WOOD = ItemBlockPair.register("life_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.LIFE_ASPECT_STRIPPED_WOOD.asBlock().defaultBlockState()));
	
	public static final ItemBlockPair<Block, BlockItem> LIFE_ASPECT_STRIPPED_LOG = ItemBlockPair.register("life_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> LIFE_ASPECT_STRIPPED_WOOD = ItemBlockPair.register("life_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> LIFE_ASPECT_LEAVES = ItemBlockPair.register("life_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	
	public static final ItemBlockPair<Block, BlockItem> LIFE_ASPECT_SAPLING = ItemBlockPair.register("life_aspect_sapling",
			() -> new AspectSaplingBlock(new TreeGrower(Minestuck.id("life_aspect").toString(), Optional.empty(), Optional.of(MSCFeatures.LIFE_TREE), Optional.empty()),
					Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	
	public static final DeferredBlock<Block> POTTED_LIFE_ASPECT_SAPLING = MSBlocks.REGISTER.register("potted_life_aspect_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, LIFE_ASPECT_SAPLING.blockHolder(), ofFullCopy(Blocks.POTTED_OAK_SAPLING)));
	
	public static final ItemBlockPair<Block, BlockItem> LIFE_ASPECT_PLANKS = ItemBlockPair.register("life_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> LIFE_ASPECT_CARVED_PLANKS = ItemBlockPair.register("life_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<StairBlock, BlockItem> LIFE_ASPECT_STAIRS = ItemBlockPair.register("life_aspect_stairs",
			() -> new StairBlock(() -> LIFE_ASPECT_PLANKS.asBlock().defaultBlockState(), ofFullCopy(LIFE_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<SlabBlock, BlockItem> LIFE_ASPECT_SLAB = ItemBlockPair.register("life_aspect_slab",
			() -> new SlabBlock(ofFullCopy(LIFE_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<FenceBlock, BlockItem>  LIFE_ASPECT_FENCE = ItemBlockPair.register("life_aspect_fence",
			() -> new FenceBlock(ofFullCopy(LIFE_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<FenceGateBlock, BlockItem> LIFE_ASPECT_FENCE_GATE = ItemBlockPair.register("life_aspect_fence_gate",
			() -> new FenceGateBlock(ofFullCopy(LIFE_ASPECT_PLANKS.asBlock()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	
	public static final ItemBlockPair<DoorBlock, BlockItem> LIFE_ASPECT_DOOR = ItemBlockPair.register("life_aspect_door",
			() -> new DoorBlock(MSBlockSetType.LIFE, ofFullCopy(Blocks.OAK_DOOR)));
	
	public static final ItemBlockPair<TrapDoorBlock, BlockItem> LIFE_ASPECT_TRAPDOOR = ItemBlockPair.register("life_aspect_trapdoor",
			() -> new TrapDoorBlock(MSBlockSetType.LIFE, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	
	public static final ItemBlockPair<PressurePlateBlock, BlockItem> LIFE_ASPECT_PRESSURE_PLATE = ItemBlockPair.register("life_aspect_pressure_plate",
			() -> new PressurePlateBlock(MSBlockSetType.LIFE, ofFullCopy(LIFE_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<ButtonBlock, BlockItem> LIFE_ASPECT_BUTTON = ItemBlockPair.register("life_aspect_button",
			() -> new ButtonBlock(MSBlockSetType.LIFE, 10, ofFullCopy(LIFE_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<Block, BlockItem> LIFE_ASPECT_BOOKSHELF = ItemBlockPair.register("life_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> LIFE_ASPECT_LADDER = ItemBlockPair.register("life_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	
	public static final DeferredBlock<StandingSignBlock> LIFE_ASPECT_SIGN = MSBlocks.REGISTER.register("life_aspect_sign",
			() -> new MSStandingSignBlock(MSWoodTypes.LIFE, ofFullCopy(Blocks.OAK_SIGN)));
	public static final DeferredBlock<WallSignBlock> LIFE_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("life_aspect_wall_sign",
			() -> new MSWallSignBlock(MSWoodTypes.LIFE, ofFullCopy(Blocks.OAK_WALL_SIGN)));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("life_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), LIFE_ASPECT_SIGN.get(), LIFE_ASPECT_WALL_SIGN.get()));
	public static final DeferredBlock<Block> LIFE_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("life_aspect_hanging_sign",
			() -> new MSHangingSignBlock(MSWoodTypes.LIFE, ofFullCopy(Blocks.OAK_HANGING_SIGN)));
	public static final DeferredBlock<Block> LIFE_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("life_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(MSWoodTypes.LIFE, ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN)));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("life_aspect_hanging_sign",
			() -> new HangingSignItem(LIFE_ASPECT_HANGING_SIGN.get(), LIFE_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
	//Light
	
	public static final ItemBlockPair<Block, BlockItem> LIGHT_ASPECT_LOG = ItemBlockPair.register("light_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.LIGHT_ASPECT_STRIPPED_LOG.asBlock().defaultBlockState()));
	
	public static final ItemBlockPair<Block, BlockItem> LIGHT_ASPECT_WOOD = ItemBlockPair.register("light_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.LIGHT_ASPECT_STRIPPED_WOOD.asBlock().defaultBlockState()));
	
	public static final ItemBlockPair<Block, BlockItem> LIGHT_ASPECT_STRIPPED_LOG = ItemBlockPair.register("light_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> LIGHT_ASPECT_STRIPPED_WOOD = ItemBlockPair.register("light_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> LIGHT_ASPECT_LEAVES = ItemBlockPair.register("light_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	
	public static final ItemBlockPair<Block, BlockItem> LIGHT_ASPECT_SAPLING = ItemBlockPair.register("light_aspect_sapling",
			() -> new AspectSaplingBlock(new TreeGrower(Minestuck.id("light_aspect").toString(), Optional.empty(), Optional.of(MSCFeatures.LIGHT_TREE), Optional.empty()),
					Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	
	public static final DeferredBlock<Block> POTTED_LIGHT_ASPECT_SAPLING = MSBlocks.REGISTER.register("potted_light_aspect_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, LIGHT_ASPECT_SAPLING.blockHolder(), ofFullCopy(Blocks.POTTED_OAK_SAPLING)));
	
	public static final ItemBlockPair<Block, BlockItem> LIGHT_ASPECT_PLANKS = ItemBlockPair.register("light_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> LIGHT_ASPECT_CARVED_PLANKS = ItemBlockPair.register("light_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<StairBlock, BlockItem> LIGHT_ASPECT_STAIRS = ItemBlockPair.register("light_aspect_stairs",
			() -> new StairBlock(() -> LIGHT_ASPECT_PLANKS.asBlock().defaultBlockState(), ofFullCopy(LIGHT_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<SlabBlock, BlockItem> LIGHT_ASPECT_SLAB = ItemBlockPair.register("light_aspect_slab",
			() -> new SlabBlock(ofFullCopy(LIGHT_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<FenceBlock, BlockItem>  LIGHT_ASPECT_FENCE = ItemBlockPair.register("light_aspect_fence",
			() -> new FenceBlock(ofFullCopy(LIGHT_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<FenceGateBlock, BlockItem> LIGHT_ASPECT_FENCE_GATE = ItemBlockPair.register("light_aspect_fence_gate",
			() -> new FenceGateBlock(ofFullCopy(LIGHT_ASPECT_PLANKS.asBlock()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	
	public static final ItemBlockPair<DoorBlock, BlockItem> LIGHT_ASPECT_DOOR = ItemBlockPair.register("light_aspect_door",
			() -> new DoorBlock(MSBlockSetType.LIGHT, ofFullCopy(Blocks.OAK_DOOR)));
	
	public static final ItemBlockPair<TrapDoorBlock, BlockItem> LIGHT_ASPECT_TRAPDOOR = ItemBlockPair.register("light_aspect_trapdoor",
			() -> new TrapDoorBlock(MSBlockSetType.LIGHT, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	
	public static final ItemBlockPair<PressurePlateBlock, BlockItem> LIGHT_ASPECT_PRESSURE_PLATE = ItemBlockPair.register("light_aspect_pressure_plate",
			() -> new PressurePlateBlock(MSBlockSetType.LIGHT, ofFullCopy(LIGHT_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<ButtonBlock, BlockItem> LIGHT_ASPECT_BUTTON = ItemBlockPair.register("light_aspect_button",
			() -> new ButtonBlock(MSBlockSetType.LIGHT, 10, ofFullCopy(LIGHT_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<Block, BlockItem> LIGHT_ASPECT_BOOKSHELF = ItemBlockPair.register("light_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> LIGHT_ASPECT_LADDER = ItemBlockPair.register("light_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	
	public static final DeferredBlock<StandingSignBlock> LIGHT_ASPECT_SIGN = MSBlocks.REGISTER.register("light_aspect_sign",
			() -> new MSStandingSignBlock(MSWoodTypes.LIGHT, ofFullCopy(Blocks.OAK_SIGN)));
	public static final DeferredBlock<WallSignBlock> LIGHT_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("light_aspect_wall_sign",
			() -> new MSWallSignBlock(MSWoodTypes.LIGHT, ofFullCopy(Blocks.OAK_WALL_SIGN)));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("light_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), LIGHT_ASPECT_SIGN.get(), LIGHT_ASPECT_WALL_SIGN.get()));
	public static final DeferredBlock<Block> LIGHT_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("light_aspect_hanging_sign",
			() -> new MSHangingSignBlock(MSWoodTypes.LIGHT, ofFullCopy(Blocks.OAK_HANGING_SIGN)));
	public static final DeferredBlock<Block> LIGHT_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("light_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(MSWoodTypes.LIGHT, ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN)));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("light_aspect_hanging_sign",
			() -> new HangingSignItem(LIGHT_ASPECT_HANGING_SIGN.get(), LIGHT_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
	//Mind
	
	public static final ItemBlockPair<Block, BlockItem> MIND_ASPECT_LOG = ItemBlockPair.register("mind_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.MIND_ASPECT_STRIPPED_LOG.asBlock().defaultBlockState()));
	
	public static final ItemBlockPair<Block, BlockItem> MIND_ASPECT_WOOD = ItemBlockPair.register("mind_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.MIND_ASPECT_STRIPPED_WOOD.asBlock().defaultBlockState()));
	
	public static final ItemBlockPair<Block, BlockItem> MIND_ASPECT_STRIPPED_LOG = ItemBlockPair.register("mind_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> MIND_ASPECT_STRIPPED_WOOD = ItemBlockPair.register("mind_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> MIND_ASPECT_LEAVES = ItemBlockPair.register("mind_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	
	public static final ItemBlockPair<Block, BlockItem> MIND_ASPECT_SAPLING = ItemBlockPair.register("mind_aspect_sapling",
			() -> new AspectSaplingBlock(new TreeGrower(Minestuck.id("mind_aspect").toString(), Optional.empty(), Optional.of(MSCFeatures.MIND_TREE), Optional.empty()),
					Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	
	public static final DeferredBlock<Block> POTTED_MIND_ASPECT_SAPLING = MSBlocks.REGISTER.register("potted_mind_aspect_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, MIND_ASPECT_SAPLING.blockHolder(), ofFullCopy(Blocks.POTTED_OAK_SAPLING)));
	
	public static final ItemBlockPair<Block, BlockItem> MIND_ASPECT_PLANKS = ItemBlockPair.register("mind_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> MIND_ASPECT_CARVED_PLANKS = ItemBlockPair.register("mind_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<StairBlock, BlockItem> MIND_ASPECT_STAIRS = ItemBlockPair.register("mind_aspect_stairs",
			() -> new StairBlock(() -> MIND_ASPECT_PLANKS.asBlock().defaultBlockState(), ofFullCopy(MIND_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<SlabBlock, BlockItem> MIND_ASPECT_SLAB = ItemBlockPair.register("mind_aspect_slab",
			() -> new SlabBlock(ofFullCopy(MIND_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<FenceBlock, BlockItem>  MIND_ASPECT_FENCE = ItemBlockPair.register("mind_aspect_fence",
			() -> new FenceBlock(ofFullCopy(MIND_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<FenceGateBlock, BlockItem> MIND_ASPECT_FENCE_GATE = ItemBlockPair.register("mind_aspect_fence_gate",
			() -> new FenceGateBlock(ofFullCopy(MIND_ASPECT_PLANKS.asBlock()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	
	public static final ItemBlockPair<DoorBlock, BlockItem> MIND_ASPECT_DOOR = ItemBlockPair.register("mind_aspect_door",
			() -> new DoorBlock(MSBlockSetType.MIND, ofFullCopy(Blocks.OAK_DOOR)));
	
	public static final ItemBlockPair<TrapDoorBlock, BlockItem> MIND_ASPECT_TRAPDOOR = ItemBlockPair.register("mind_aspect_trapdoor",
			() -> new TrapDoorBlock(MSBlockSetType.MIND, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	
	public static final ItemBlockPair<PressurePlateBlock, BlockItem> MIND_ASPECT_PRESSURE_PLATE = ItemBlockPair.register("mind_aspect_pressure_plate",
			() -> new PressurePlateBlock(MSBlockSetType.MIND, ofFullCopy(MIND_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<ButtonBlock, BlockItem> MIND_ASPECT_BUTTON = ItemBlockPair.register("mind_aspect_button",
			() -> new ButtonBlock(MSBlockSetType.MIND, 10, ofFullCopy(MIND_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<Block, BlockItem> MIND_ASPECT_BOOKSHELF = ItemBlockPair.register("mind_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> MIND_ASPECT_LADDER = ItemBlockPair.register("mind_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	
	public static final DeferredBlock<StandingSignBlock> MIND_ASPECT_SIGN = MSBlocks.REGISTER.register("mind_aspect_sign",
			() -> new MSStandingSignBlock(MSWoodTypes.MIND, ofFullCopy(Blocks.OAK_SIGN)));
	public static final DeferredBlock<WallSignBlock> MIND_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("mind_aspect_wall_sign",
			() -> new MSWallSignBlock(MSWoodTypes.MIND, ofFullCopy(Blocks.OAK_WALL_SIGN)));
	public static final DeferredItem<BlockItem> MIND_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("mind_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), MIND_ASPECT_SIGN.get(), MIND_ASPECT_WALL_SIGN.get()));
	public static final DeferredBlock<Block> MIND_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("mind_aspect_hanging_sign",
			() -> new MSHangingSignBlock(MSWoodTypes.MIND, ofFullCopy(Blocks.OAK_HANGING_SIGN)));
	public static final DeferredBlock<Block> MIND_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("mind_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(MSWoodTypes.MIND, ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN)));
	public static final DeferredItem<BlockItem> MIND_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("mind_aspect_hanging_sign",
			() -> new HangingSignItem(MIND_ASPECT_HANGING_SIGN.get(), MIND_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
	//Rage
	
	public static final ItemBlockPair<Block, BlockItem> RAGE_ASPECT_LOG = ItemBlockPair.register("rage_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.RAGE_ASPECT_STRIPPED_LOG.asBlock().defaultBlockState()));
	
	public static final ItemBlockPair<Block, BlockItem> RAGE_ASPECT_WOOD = ItemBlockPair.register("rage_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.RAGE_ASPECT_STRIPPED_WOOD.asBlock().defaultBlockState()));
	
	public static final ItemBlockPair<Block, BlockItem> RAGE_ASPECT_STRIPPED_LOG = ItemBlockPair.register("rage_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> RAGE_ASPECT_STRIPPED_WOOD = ItemBlockPair.register("rage_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> RAGE_ASPECT_LEAVES = ItemBlockPair.register("rage_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	
	public static final ItemBlockPair<Block, BlockItem> RAGE_ASPECT_SAPLING = ItemBlockPair.register("rage_aspect_sapling",
			() -> new AspectSaplingBlock(new TreeGrower(Minestuck.id("rage_aspect").toString(), Optional.empty(), Optional.of(MSCFeatures.RAGE_TREE), Optional.empty()),
					Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	
	public static final DeferredBlock<Block> POTTED_RAGE_ASPECT_SAPLING = MSBlocks.REGISTER.register("potted_rage_aspect_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, RAGE_ASPECT_SAPLING.blockHolder(), ofFullCopy(Blocks.POTTED_OAK_SAPLING)));
	
	public static final ItemBlockPair<Block, BlockItem> RAGE_ASPECT_PLANKS = ItemBlockPair.register("rage_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> RAGE_ASPECT_CARVED_PLANKS = ItemBlockPair.register("rage_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<StairBlock, BlockItem> RAGE_ASPECT_STAIRS = ItemBlockPair.register("rage_aspect_stairs",
			() -> new StairBlock(() -> RAGE_ASPECT_PLANKS.asBlock().defaultBlockState(), ofFullCopy(RAGE_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<SlabBlock, BlockItem> RAGE_ASPECT_SLAB = ItemBlockPair.register("rage_aspect_slab",
			() -> new SlabBlock(ofFullCopy(RAGE_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<FenceBlock, BlockItem>  RAGE_ASPECT_FENCE = ItemBlockPair.register("rage_aspect_fence",
			() -> new FenceBlock(ofFullCopy(RAGE_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<FenceGateBlock, BlockItem> RAGE_ASPECT_FENCE_GATE = ItemBlockPair.register("rage_aspect_fence_gate",
			() -> new FenceGateBlock(ofFullCopy(RAGE_ASPECT_PLANKS.asBlock()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	
	public static final ItemBlockPair<DoorBlock, BlockItem> RAGE_ASPECT_DOOR = ItemBlockPair.register("rage_aspect_door",
			() -> new DoorBlock(MSBlockSetType.RAGE, ofFullCopy(Blocks.OAK_DOOR)));
	
	public static final ItemBlockPair<TrapDoorBlock, BlockItem> RAGE_ASPECT_TRAPDOOR = ItemBlockPair.register("rage_aspect_trapdoor",
			() -> new TrapDoorBlock(MSBlockSetType.RAGE, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	
	public static final ItemBlockPair<PressurePlateBlock, BlockItem> RAGE_ASPECT_PRESSURE_PLATE = ItemBlockPair.register("rage_aspect_pressure_plate",
			() -> new PressurePlateBlock(MSBlockSetType.RAGE, ofFullCopy(RAGE_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<ButtonBlock, BlockItem> RAGE_ASPECT_BUTTON = ItemBlockPair.register("rage_aspect_button",
			() -> new ButtonBlock(MSBlockSetType.RAGE, 10, ofFullCopy(RAGE_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<Block, BlockItem> RAGE_ASPECT_BOOKSHELF = ItemBlockPair.register("rage_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> RAGE_ASPECT_LADDER = ItemBlockPair.register("rage_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	
	public static final DeferredBlock<StandingSignBlock> RAGE_ASPECT_SIGN = MSBlocks.REGISTER.register("rage_aspect_sign",
			() -> new MSStandingSignBlock(MSWoodTypes.RAGE, ofFullCopy(Blocks.OAK_SIGN)));
	public static final DeferredBlock<WallSignBlock> RAGE_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("rage_aspect_wall_sign",
			() -> new MSWallSignBlock(MSWoodTypes.RAGE, ofFullCopy(Blocks.OAK_WALL_SIGN)));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("rage_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), RAGE_ASPECT_SIGN.get(), RAGE_ASPECT_WALL_SIGN.get()));
	public static final DeferredBlock<Block> RAGE_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("rage_aspect_hanging_sign",
			() -> new MSHangingSignBlock(MSWoodTypes.RAGE, ofFullCopy(Blocks.OAK_HANGING_SIGN)));
	public static final DeferredBlock<Block> RAGE_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("rage_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(MSWoodTypes.RAGE, ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN)));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("rage_aspect_hanging_sign",
			() -> new HangingSignItem(RAGE_ASPECT_HANGING_SIGN.get(), RAGE_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
	//Space
	
	public static final ItemBlockPair<Block, BlockItem> SPACE_ASPECT_LOG = ItemBlockPair.register("space_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.SPACE_ASPECT_STRIPPED_LOG.asBlock().defaultBlockState()));
	
	public static final ItemBlockPair<Block, BlockItem> SPACE_ASPECT_WOOD = ItemBlockPair.register("space_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.SPACE_ASPECT_STRIPPED_WOOD.asBlock().defaultBlockState()));
	
	public static final ItemBlockPair<Block, BlockItem> SPACE_ASPECT_STRIPPED_LOG = ItemBlockPair.register("space_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> SPACE_ASPECT_STRIPPED_WOOD = ItemBlockPair.register("space_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> SPACE_ASPECT_LEAVES = ItemBlockPair.register("space_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	
	public static final ItemBlockPair<Block, BlockItem> SPACE_ASPECT_SAPLING = ItemBlockPair.register("space_aspect_sapling",
			() -> new AspectSaplingBlock(new TreeGrower(Minestuck.id("space_aspect").toString(), Optional.empty(), Optional.of(MSCFeatures.SPACE_TREE), Optional.empty()),
					Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	
	public static final DeferredBlock<Block> POTTED_SPACE_ASPECT_SAPLING = MSBlocks.REGISTER.register("potted_space_aspect_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, SPACE_ASPECT_SAPLING.blockHolder(), ofFullCopy(Blocks.POTTED_OAK_SAPLING)));
	
	public static final ItemBlockPair<Block, BlockItem> SPACE_ASPECT_PLANKS = ItemBlockPair.register("space_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> SPACE_ASPECT_CARVED_PLANKS = ItemBlockPair.register("space_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<StairBlock, BlockItem> SPACE_ASPECT_STAIRS = ItemBlockPair.register("space_aspect_stairs",
			() -> new StairBlock(() -> SPACE_ASPECT_PLANKS.asBlock().defaultBlockState(), ofFullCopy(SPACE_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<SlabBlock, BlockItem> SPACE_ASPECT_SLAB = ItemBlockPair.register("space_aspect_slab",
			() -> new SlabBlock(ofFullCopy(SPACE_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<FenceBlock, BlockItem>  SPACE_ASPECT_FENCE = ItemBlockPair.register("space_aspect_fence",
			() -> new FenceBlock(ofFullCopy(SPACE_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<FenceGateBlock, BlockItem> SPACE_ASPECT_FENCE_GATE = ItemBlockPair.register("space_aspect_fence_gate",
			() -> new FenceGateBlock(ofFullCopy(SPACE_ASPECT_PLANKS.asBlock()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	
	public static final ItemBlockPair<DoorBlock, BlockItem> SPACE_ASPECT_DOOR = ItemBlockPair.register("space_aspect_door",
			() -> new DoorBlock(MSBlockSetType.SPACE, ofFullCopy(Blocks.OAK_DOOR)));
	
	public static final ItemBlockPair<TrapDoorBlock, BlockItem> SPACE_ASPECT_TRAPDOOR = ItemBlockPair.register("space_aspect_trapdoor",
			() -> new TrapDoorBlock(MSBlockSetType.SPACE, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	
	public static final ItemBlockPair<PressurePlateBlock, BlockItem> SPACE_ASPECT_PRESSURE_PLATE = ItemBlockPair.register("space_aspect_pressure_plate",
			() -> new PressurePlateBlock(MSBlockSetType.SPACE, ofFullCopy(SPACE_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<ButtonBlock, BlockItem> SPACE_ASPECT_BUTTON = ItemBlockPair.register("space_aspect_button",
			() -> new ButtonBlock(MSBlockSetType.SPACE, 10, ofFullCopy(SPACE_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<Block, BlockItem> SPACE_ASPECT_BOOKSHELF = ItemBlockPair.register("space_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> SPACE_ASPECT_LADDER = ItemBlockPair.register("space_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	
	public static final DeferredBlock<StandingSignBlock> SPACE_ASPECT_SIGN = MSBlocks.REGISTER.register("space_aspect_sign",
			() -> new MSStandingSignBlock(MSWoodTypes.SPACE, ofFullCopy(Blocks.OAK_SIGN)));
	public static final DeferredBlock<WallSignBlock> SPACE_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("space_aspect_wall_sign",
			() -> new MSWallSignBlock(MSWoodTypes.SPACE, ofFullCopy(Blocks.OAK_WALL_SIGN)));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("space_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), SPACE_ASPECT_SIGN.get(), SPACE_ASPECT_WALL_SIGN.get()));
	public static final DeferredBlock<Block> SPACE_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("space_aspect_hanging_sign",
			() -> new MSHangingSignBlock(MSWoodTypes.SPACE, ofFullCopy(Blocks.OAK_HANGING_SIGN)));
	public static final DeferredBlock<Block> SPACE_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("space_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(MSWoodTypes.SPACE, ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN)));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("space_aspect_hanging_sign",
			() -> new HangingSignItem(SPACE_ASPECT_HANGING_SIGN.get(), SPACE_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
	//Time
	
	public static final ItemBlockPair<Block, BlockItem> TIME_ASPECT_LOG = ItemBlockPair.register("time_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.TIME_ASPECT_STRIPPED_LOG.asBlock().defaultBlockState()));
	
	public static final ItemBlockPair<Block, BlockItem> TIME_ASPECT_WOOD = ItemBlockPair.register("time_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.TIME_ASPECT_STRIPPED_WOOD.asBlock().defaultBlockState()));
	
	public static final ItemBlockPair<Block, BlockItem> TIME_ASPECT_STRIPPED_LOG = ItemBlockPair.register("time_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> TIME_ASPECT_STRIPPED_WOOD = ItemBlockPair.register("time_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> TIME_ASPECT_LEAVES = ItemBlockPair.register("time_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	
	public static final ItemBlockPair<Block, BlockItem> TIME_ASPECT_SAPLING = ItemBlockPair.register("time_aspect_sapling",
			() -> new AspectSaplingBlock(new TreeGrower(Minestuck.id("time_aspect").toString(), Optional.empty(), Optional.of(MSCFeatures.TIME_TREE), Optional.empty()),
					Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	
	public static final DeferredBlock<Block> POTTED_TIME_ASPECT_SAPLING = MSBlocks.REGISTER.register("potted_time_aspect_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, TIME_ASPECT_SAPLING.blockHolder(), ofFullCopy(Blocks.POTTED_OAK_SAPLING)));
	
	public static final ItemBlockPair<Block, BlockItem> TIME_ASPECT_PLANKS = ItemBlockPair.register("time_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> TIME_ASPECT_CARVED_PLANKS = ItemBlockPair.register("time_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<StairBlock, BlockItem> TIME_ASPECT_STAIRS = ItemBlockPair.register("time_aspect_stairs",
			() -> new StairBlock(() -> TIME_ASPECT_PLANKS.asBlock().defaultBlockState(), ofFullCopy(TIME_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<SlabBlock, BlockItem> TIME_ASPECT_SLAB = ItemBlockPair.register("time_aspect_slab",
			() -> new SlabBlock(ofFullCopy(TIME_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<FenceBlock, BlockItem>  TIME_ASPECT_FENCE = ItemBlockPair.register("time_aspect_fence",
			() -> new FenceBlock(ofFullCopy(TIME_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<FenceGateBlock, BlockItem> TIME_ASPECT_FENCE_GATE = ItemBlockPair.register("time_aspect_fence_gate",
			() -> new FenceGateBlock(ofFullCopy(TIME_ASPECT_PLANKS.asBlock()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	
	public static final ItemBlockPair<DoorBlock, BlockItem> TIME_ASPECT_DOOR = ItemBlockPair.register("time_aspect_door",
			() -> new DoorBlock(MSBlockSetType.TIME, ofFullCopy(Blocks.OAK_DOOR)));
	
	public static final ItemBlockPair<TrapDoorBlock, BlockItem> TIME_ASPECT_TRAPDOOR = ItemBlockPair.register("time_aspect_trapdoor",
			() -> new TrapDoorBlock(MSBlockSetType.TIME, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	
	public static final ItemBlockPair<PressurePlateBlock, BlockItem> TIME_ASPECT_PRESSURE_PLATE = ItemBlockPair.register("time_aspect_pressure_plate",
			() -> new PressurePlateBlock(MSBlockSetType.TIME, ofFullCopy(TIME_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<ButtonBlock, BlockItem> TIME_ASPECT_BUTTON = ItemBlockPair.register("time_aspect_button",
			() -> new ButtonBlock(MSBlockSetType.TIME, 10, ofFullCopy(TIME_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<Block, BlockItem> TIME_ASPECT_BOOKSHELF = ItemBlockPair.register("time_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> TIME_ASPECT_LADDER = ItemBlockPair.register("time_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	
	public static final DeferredBlock<StandingSignBlock> TIME_ASPECT_SIGN = MSBlocks.REGISTER.register("time_aspect_sign",
			() -> new MSStandingSignBlock(MSWoodTypes.TIME, ofFullCopy(Blocks.OAK_SIGN)));
	public static final DeferredBlock<WallSignBlock> TIME_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("time_aspect_wall_sign",
			() -> new MSWallSignBlock(MSWoodTypes.TIME, ofFullCopy(Blocks.OAK_WALL_SIGN)));
	public static final DeferredItem<BlockItem> TIME_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("time_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), TIME_ASPECT_SIGN.get(), TIME_ASPECT_WALL_SIGN.get()));
	public static final DeferredBlock<Block> TIME_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("time_aspect_hanging_sign",
			() -> new MSHangingSignBlock(MSWoodTypes.TIME, ofFullCopy(Blocks.OAK_HANGING_SIGN)));
	public static final DeferredBlock<Block> TIME_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("time_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(MSWoodTypes.TIME, ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN)));
	public static final DeferredItem<BlockItem> TIME_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("time_aspect_hanging_sign",
			() -> new HangingSignItem(TIME_ASPECT_HANGING_SIGN.get(), TIME_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
	//Void
	
	public static final ItemBlockPair<Block, BlockItem> VOID_ASPECT_LOG = ItemBlockPair.register("void_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.VOID_ASPECT_STRIPPED_LOG.asBlock().defaultBlockState()));
	
	public static final ItemBlockPair<Block, BlockItem> VOID_ASPECT_WOOD = ItemBlockPair.register("void_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.VOID_ASPECT_STRIPPED_WOOD.asBlock().defaultBlockState()));
	
	public static final ItemBlockPair<Block, BlockItem> VOID_ASPECT_STRIPPED_LOG = ItemBlockPair.register("void_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> VOID_ASPECT_STRIPPED_WOOD = ItemBlockPair.register("void_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> VOID_ASPECT_LEAVES = ItemBlockPair.register("void_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	
	public static final ItemBlockPair<Block, BlockItem> VOID_ASPECT_SAPLING = ItemBlockPair.register("void_aspect_sapling",
			() -> new AspectSaplingBlock(new TreeGrower(Minestuck.id("void_aspect").toString(), Optional.empty(), Optional.of(MSCFeatures.VOID_TREE), Optional.empty()),
					Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	
	public static final DeferredBlock<Block> POTTED_VOID_ASPECT_SAPLING = MSBlocks.REGISTER.register("potted_void_aspect_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, VOID_ASPECT_SAPLING.blockHolder(), ofFullCopy(Blocks.POTTED_OAK_SAPLING)));
	
	public static final ItemBlockPair<Block, BlockItem> VOID_ASPECT_PLANKS = ItemBlockPair.register("void_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> VOID_ASPECT_CARVED_PLANKS = ItemBlockPair.register("void_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<StairBlock, BlockItem> VOID_ASPECT_STAIRS = ItemBlockPair.register("void_aspect_stairs",
			() -> new StairBlock(() -> VOID_ASPECT_PLANKS.asBlock().defaultBlockState(), ofFullCopy(VOID_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<SlabBlock, BlockItem> VOID_ASPECT_SLAB = ItemBlockPair.register("void_aspect_slab",
			() -> new SlabBlock(ofFullCopy(VOID_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<FenceBlock, BlockItem>  VOID_ASPECT_FENCE = ItemBlockPair.register("void_aspect_fence",
			() -> new FenceBlock(ofFullCopy(VOID_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<FenceGateBlock, BlockItem> VOID_ASPECT_FENCE_GATE = ItemBlockPair.register("void_aspect_fence_gate",
			() -> new FenceGateBlock(ofFullCopy(VOID_ASPECT_PLANKS.asBlock()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	
	public static final ItemBlockPair<DoorBlock, BlockItem> VOID_ASPECT_DOOR = ItemBlockPair.register("void_aspect_door",
			() -> new DoorBlock(MSBlockSetType.VOID, ofFullCopy(Blocks.OAK_DOOR)));
	
	public static final ItemBlockPair<TrapDoorBlock, BlockItem> VOID_ASPECT_TRAPDOOR = ItemBlockPair.register("void_aspect_trapdoor",
			() -> new TrapDoorBlock(MSBlockSetType.VOID, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	
	public static final ItemBlockPair<PressurePlateBlock, BlockItem> VOID_ASPECT_PRESSURE_PLATE = ItemBlockPair.register("void_aspect_pressure_plate",
			() -> new PressurePlateBlock(MSBlockSetType.VOID, ofFullCopy(VOID_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<ButtonBlock, BlockItem> VOID_ASPECT_BUTTON = ItemBlockPair.register("void_aspect_button",
			() -> new ButtonBlock(MSBlockSetType.VOID, 10, ofFullCopy(VOID_ASPECT_PLANKS.asBlock())));
	
	public static final ItemBlockPair<Block, BlockItem> VOID_ASPECT_BOOKSHELF = ItemBlockPair.register("void_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final ItemBlockPair<Block, BlockItem> VOID_ASPECT_LADDER = ItemBlockPair.register("void_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	
	public static final DeferredBlock<StandingSignBlock> VOID_ASPECT_SIGN = MSBlocks.REGISTER.register("void_aspect_sign",
			() -> new MSStandingSignBlock(MSWoodTypes.VOID, ofFullCopy(Blocks.OAK_SIGN)));
	public static final DeferredBlock<WallSignBlock> VOID_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("void_aspect_wall_sign",
			() -> new MSWallSignBlock(MSWoodTypes.VOID, ofFullCopy(Blocks.OAK_WALL_SIGN)));
	public static final DeferredItem<BlockItem> VOID_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("void_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), VOID_ASPECT_SIGN.get(), VOID_ASPECT_WALL_SIGN.get()));
	public static final DeferredBlock<Block> VOID_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("void_aspect_hanging_sign",
			() -> new MSHangingSignBlock(MSWoodTypes.VOID, ofFullCopy(Blocks.OAK_HANGING_SIGN)));
	public static final DeferredBlock<Block> VOID_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("void_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(MSWoodTypes.VOID, ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN)));
	public static final DeferredItem<BlockItem> VOID_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("void_aspect_hanging_sign",
			() -> new HangingSignItem(VOID_ASPECT_HANGING_SIGN.get(), VOID_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
}
