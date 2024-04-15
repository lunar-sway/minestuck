package com.mraof.minestuck.block;

import com.mraof.minestuck.block.plant.AspectSaplingBlock;
import com.mraof.minestuck.block.plant.FlammableLeavesBlock;
import com.mraof.minestuck.block.plant.FlammableLogBlock;
import com.mraof.minestuck.block.plant.StrippableFlammableLogBlock;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.world.gen.feature.tree.aspect.*;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.copy;

public final class AspectTreeBlocks
{
	public static void init() {}
	
	//Blood
	public static final DeferredBlock<Block> BLOOD_ASPECT_LOG = MSBlocks.REGISTER.register("blood_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.BLOOD_ASPECT_STRIPPED_LOG.get().defaultBlockState()));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_LOG);
	
	public static final DeferredBlock<Block> BLOOD_ASPECT_WOOD = MSBlocks.REGISTER.register("blood_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.BLOOD_ASPECT_STRIPPED_WOOD.get().defaultBlockState()));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_WOOD);
	
	public static final DeferredBlock<Block> BLOOD_ASPECT_STRIPPED_LOG = MSBlocks.REGISTER.register("blood_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_STRIPPED_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_STRIPPED_LOG);
	
	public static final DeferredBlock<Block> BLOOD_ASPECT_STRIPPED_WOOD = MSBlocks.REGISTER.register("blood_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_STRIPPED_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_STRIPPED_WOOD);
	
	public static final DeferredBlock<Block> BLOOD_ASPECT_LEAVES = MSBlocks.REGISTER.register("blood_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_LEAVES_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_LEAVES);
	
	public static final DeferredBlock<Block> BLOOD_ASPECT_SAPLING = MSBlocks.REGISTER.register("blood_aspect_sapling",
			() -> new AspectSaplingBlock(new BloodAspectTree(), Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	
	public static final DeferredBlock<Block> BLOOD_ASPECT_PLANKS = MSBlocks.REGISTER.register("blood_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_PLANKS);
	
	public static final DeferredBlock<Block> BLOOD_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("blood_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_CARVED_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_CARVED_PLANKS);
	
	public static final DeferredBlock<StairBlock> BLOOD_ASPECT_STAIRS = MSBlocks.REGISTER.register("blood_aspect_stairs",
			() -> new StairBlock(() -> BLOOD_ASPECT_PLANKS.get().defaultBlockState(), copy(BLOOD_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_STAIRS);
	
	public static final DeferredBlock<SlabBlock> BLOOD_ASPECT_SLAB = MSBlocks.REGISTER.register("blood_aspect_slab",
			() -> new SlabBlock(copy(BLOOD_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_SLAB);
	
	public static final DeferredBlock<FenceBlock> BLOOD_ASPECT_FENCE = MSBlocks.REGISTER.register("blood_aspect_fence",
			() -> new FenceBlock(copy(BLOOD_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_FENCE);
	
	public static final DeferredBlock<FenceGateBlock> BLOOD_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("blood_aspect_fence_gate",
			() -> new FenceGateBlock(copy(BLOOD_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_FENCE_GATE);
	
	public static final DeferredBlock<DoorBlock> BLOOD_ASPECT_DOOR = MSBlocks.REGISTER.register("blood_aspect_door",
			() -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), MSBlockSetType.BLOOD));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_DOOR);
	
	public static final DeferredBlock<TrapDoorBlock> BLOOD_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("blood_aspect_trapdoor",
			() -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR),MSBlockSetType.BLOOD));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_TRAPDOOR);
	
	public static final DeferredBlock<PressurePlateBlock> BLOOD_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("blood_aspect_pressure_plate",
			() -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(BLOOD_ASPECT_PLANKS.get()), MSBlockSetType.BLOOD));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_PRESSURE_PLATE);
	
	public static final DeferredBlock<ButtonBlock> BLOOD_ASPECT_BUTTON = MSBlocks.REGISTER.register("blood_aspect_button",
			() -> new ButtonBlock(copy(BLOOD_ASPECT_PLANKS.get()), MSBlockSetType.BLOOD, 10, true));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_BUTTON);
	
	public static final DeferredBlock<Block> BLOOD_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("blood_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_BOOKSHELF);
	
	public static final DeferredBlock<Block> BLOOD_ASPECT_LADDER = MSBlocks.REGISTER.register("blood_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_LADDER);
	
	public static final DeferredBlock<StandingSignBlock> BLOOD_ASPECT_SIGN = MSBlocks.REGISTER.register("blood_aspect_sign",
			() -> new MSStandingSignBlock(Block.Properties.copy(Blocks.OAK_SIGN), MSWoodTypes.BLOOD));
	public static final DeferredBlock<WallSignBlock> BLOOD_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("blood_aspect_wall_sign",
			() -> new MSWallSignBlock(Block.Properties.copy(Blocks.OAK_WALL_SIGN), MSWoodTypes.BLOOD));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("blood_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), BLOOD_ASPECT_SIGN.get(), BLOOD_ASPECT_WALL_SIGN.get()));
	public static final DeferredBlock<Block> BLOOD_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("blood_aspect_hanging_sign",
			() -> new MSHangingSignBlock(Block.Properties.copy(Blocks.OAK_HANGING_SIGN), MSWoodTypes.BLOOD));
	public static final DeferredBlock<Block> BLOOD_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("blood_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(Block.Properties.copy(Blocks.OAK_WALL_HANGING_SIGN), MSWoodTypes.BLOOD));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("blood_aspect_hanging_sign",
			() -> new HangingSignItem(BLOOD_ASPECT_HANGING_SIGN.get(), BLOOD_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
	
	//Breath
	public static final DeferredBlock<Block> BREATH_ASPECT_LOG = MSBlocks.REGISTER.register("breath_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.BREATH_ASPECT_STRIPPED_LOG.get().defaultBlockState()));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_LOG);
	
	public static final DeferredBlock<Block> BREATH_ASPECT_WOOD = MSBlocks.REGISTER.register("breath_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.BREATH_ASPECT_STRIPPED_WOOD.get().defaultBlockState()));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_WOOD);
	
	public static final DeferredBlock<Block> BREATH_ASPECT_STRIPPED_LOG = MSBlocks.REGISTER.register("breath_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_STRIPPED_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_STRIPPED_LOG);
	
	public static final DeferredBlock<Block> BREATH_ASPECT_STRIPPED_WOOD = MSBlocks.REGISTER.register("breath_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_STRIPPED_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_STRIPPED_WOOD);
	
	public static final DeferredBlock<Block> BREATH_ASPECT_LEAVES = MSBlocks.REGISTER.register("breath_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_LEAVES_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_LEAVES);
	
	public static final DeferredBlock<Block> BREATH_ASPECT_SAPLING = MSBlocks.REGISTER.register("breath_aspect_sapling",
			() -> new AspectSaplingBlock(new BreathAspectTree(), Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	
	public static final DeferredBlock<Block> BREATH_ASPECT_PLANKS = MSBlocks.REGISTER.register("breath_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_PLANKS);
	
	public static final DeferredBlock<Block> BREATH_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("breath_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_CARVED_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_CARVED_PLANKS);
	
	public static final DeferredBlock<StairBlock> BREATH_ASPECT_STAIRS = MSBlocks.REGISTER.register("breath_aspect_stairs",
			() -> new StairBlock(() -> BREATH_ASPECT_PLANKS.get().defaultBlockState(), copy(BREATH_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_STAIRS);
	
	public static final DeferredBlock<SlabBlock> BREATH_ASPECT_SLAB = MSBlocks.REGISTER.register("breath_aspect_slab",
			() -> new SlabBlock(copy(BREATH_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_SLAB);
	
	public static final DeferredBlock<FenceBlock> BREATH_ASPECT_FENCE = MSBlocks.REGISTER.register("breath_aspect_fence",
			() -> new FenceBlock(copy(BREATH_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_FENCE);
	
	public static final DeferredBlock<FenceGateBlock> BREATH_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("breath_aspect_fence_gate",
			() -> new FenceGateBlock(copy(BREATH_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_FENCE_GATE);
	
	public static final DeferredBlock<DoorBlock> BREATH_ASPECT_DOOR = MSBlocks.REGISTER.register("breath_aspect_door",
			() -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), MSBlockSetType.BREATH));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_DOOR);
	
	public static final DeferredBlock<TrapDoorBlock> BREATH_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("breath_aspect_trapdoor",
			() -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), MSBlockSetType.BREATH));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_TRAPDOOR);
	
	public static final DeferredBlock<PressurePlateBlock> BREATH_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("breath_aspect_pressure_plate",
			() -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(BREATH_ASPECT_PLANKS.get()), MSBlockSetType.BREATH));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_PRESSURE_PLATE);
	
	public static final DeferredBlock<ButtonBlock> BREATH_ASPECT_BUTTON = MSBlocks.REGISTER.register("breath_aspect_button",
			() -> new ButtonBlock(copy(BREATH_ASPECT_PLANKS.get()), MSBlockSetType.BREATH, 10, true));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_BUTTON);
	
	public static final DeferredBlock<Block> BREATH_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("breath_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_BOOKSHELF);
	
	public static final DeferredBlock<Block> BREATH_ASPECT_LADDER = MSBlocks.REGISTER.register("breath_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_LADDER);
	
	public static final DeferredBlock<StandingSignBlock> BREATH_ASPECT_SIGN = MSBlocks.REGISTER.register("breath_aspect_sign",
			() -> new MSStandingSignBlock(Block.Properties.copy(Blocks.OAK_SIGN), MSWoodTypes.BREATH));
	public static final DeferredBlock<WallSignBlock> BREATH_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("breath_aspect_wall_sign",
			() -> new MSWallSignBlock(Block.Properties.copy(Blocks.OAK_WALL_SIGN), MSWoodTypes.BREATH));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("breath_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), BREATH_ASPECT_SIGN.get(), BREATH_ASPECT_WALL_SIGN.get()));
	public static final DeferredBlock<Block> BREATH_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("breath_aspect_hanging_sign",
			() -> new MSHangingSignBlock(Block.Properties.copy(Blocks.OAK_HANGING_SIGN), MSWoodTypes.BREATH));
	public static final DeferredBlock<Block> BREATH_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("breath_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(Block.Properties.copy(Blocks.OAK_WALL_HANGING_SIGN), MSWoodTypes.BREATH));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("breath_aspect_hanging_sign",
			() -> new HangingSignItem(BREATH_ASPECT_HANGING_SIGN.get(), BREATH_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
	
	//Doom
	public static final DeferredBlock<Block> DOOM_ASPECT_LOG = MSBlocks.REGISTER.register("doom_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.DOOM_ASPECT_STRIPPED_LOG.get().defaultBlockState()));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_LOG);
	
	public static final DeferredBlock<Block> DOOM_ASPECT_WOOD = MSBlocks.REGISTER.register("doom_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.DOOM_ASPECT_STRIPPED_WOOD.get().defaultBlockState()));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_WOOD);
	
	public static final DeferredBlock<Block> DOOM_ASPECT_STRIPPED_LOG = MSBlocks.REGISTER.register("doom_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_STRIPPED_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_STRIPPED_LOG);
	
	public static final DeferredBlock<Block> DOOM_ASPECT_STRIPPED_WOOD = MSBlocks.REGISTER.register("doom_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_STRIPPED_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_STRIPPED_WOOD);
	
	public static final DeferredBlock<Block> DOOM_ASPECT_LEAVES = MSBlocks.REGISTER.register("doom_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_LEAVES_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_LEAVES);
	
	public static final DeferredBlock<Block> DOOM_ASPECT_SAPLING = MSBlocks.REGISTER.register("doom_aspect_sapling",
			() -> new AspectSaplingBlock(new DoomAspectTree(), Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	
	public static final DeferredBlock<Block> DOOM_ASPECT_PLANKS = MSBlocks.REGISTER.register("doom_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_PLANKS);
	
	public static final DeferredBlock<Block> DOOM_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("doom_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_CARVED_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_CARVED_PLANKS);
	
	public static final DeferredBlock<StairBlock> DOOM_ASPECT_STAIRS = MSBlocks.REGISTER.register("doom_aspect_stairs",
			() -> new StairBlock(() -> DOOM_ASPECT_PLANKS.get().defaultBlockState(), copy(DOOM_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_STAIRS);
	
	public static final DeferredBlock<SlabBlock> DOOM_ASPECT_SLAB = MSBlocks.REGISTER.register("doom_aspect_slab",
			() -> new SlabBlock(copy(DOOM_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_SLAB);
	
	public static final DeferredBlock<FenceBlock> DOOM_ASPECT_FENCE = MSBlocks.REGISTER.register("doom_aspect_fence",
			() -> new FenceBlock(copy(DOOM_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_FENCE);
	
	public static final DeferredBlock<FenceGateBlock> DOOM_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("doom_aspect_fence_gate",
			() -> new FenceGateBlock(copy(DOOM_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_FENCE_GATE);
	
	public static final DeferredBlock<DoorBlock> DOOM_ASPECT_DOOR = MSBlocks.REGISTER.register("doom_aspect_door",
			() -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), MSBlockSetType.DOOM));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_DOOR);
	
	public static final DeferredBlock<TrapDoorBlock> DOOM_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("doom_aspect_trapdoor",
			() -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), MSBlockSetType.DOOM));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_TRAPDOOR);
	
	public static final DeferredBlock<PressurePlateBlock> DOOM_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("doom_aspect_pressure_plate",
			() -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(DOOM_ASPECT_PLANKS.get()), MSBlockSetType.DOOM));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_PRESSURE_PLATE);
	
	public static final DeferredBlock<ButtonBlock> DOOM_ASPECT_BUTTON = MSBlocks.REGISTER.register("doom_aspect_button",
			() -> new ButtonBlock(copy(DOOM_ASPECT_PLANKS.get()), MSBlockSetType.DOOM, 10, true));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_BUTTON);
	
	public static final DeferredBlock<Block> DOOM_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("doom_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_BOOKSHELF);
	
	public static final DeferredBlock<Block> DOOM_ASPECT_LADDER = MSBlocks.REGISTER.register("doom_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_LADDER);
	
	public static final DeferredBlock<StandingSignBlock> DOOM_ASPECT_SIGN = MSBlocks.REGISTER.register("doom_aspect_sign",
			() -> new MSStandingSignBlock(Block.Properties.copy(Blocks.OAK_SIGN), MSWoodTypes.DOOM));
	public static final DeferredBlock<WallSignBlock> DOOM_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("doom_aspect_wall_sign",
			() -> new MSWallSignBlock(Block.Properties.copy(Blocks.OAK_WALL_SIGN), MSWoodTypes.DOOM));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("doom_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), DOOM_ASPECT_SIGN.get(), DOOM_ASPECT_WALL_SIGN.get()));
	public static final DeferredBlock<Block> DOOM_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("doom_aspect_hanging_sign",
			() -> new MSHangingSignBlock(Block.Properties.copy(Blocks.OAK_HANGING_SIGN), MSWoodTypes.DOOM));
	public static final DeferredBlock<Block> DOOM_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("doom_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(Block.Properties.copy(Blocks.OAK_WALL_HANGING_SIGN), MSWoodTypes.DOOM));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("doom_aspect_hanging_sign",
			() -> new HangingSignItem(DOOM_ASPECT_HANGING_SIGN.get(), DOOM_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
	
	//Heart
	public static final DeferredBlock<Block> HEART_ASPECT_LOG = MSBlocks.REGISTER.register("heart_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.HEART_ASPECT_STRIPPED_LOG.get().defaultBlockState()));
	public static final DeferredItem<BlockItem> HEART_ASPECT_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_LOG);
	
	public static final DeferredBlock<Block> HEART_ASPECT_WOOD = MSBlocks.REGISTER.register("heart_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.HEART_ASPECT_STRIPPED_WOOD.get().defaultBlockState()));
	public static final DeferredItem<BlockItem> HEART_ASPECT_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_WOOD);
	
	public static final DeferredBlock<Block> HEART_ASPECT_STRIPPED_LOG = MSBlocks.REGISTER.register("heart_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> HEART_ASPECT_STRIPPED_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_STRIPPED_LOG);
	
	public static final DeferredBlock<Block> HEART_ASPECT_STRIPPED_WOOD = MSBlocks.REGISTER.register("heart_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> HEART_ASPECT_STRIPPED_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_STRIPPED_WOOD);
	
	public static final DeferredBlock<Block> HEART_ASPECT_LEAVES = MSBlocks.REGISTER.register("heart_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final DeferredItem<BlockItem> HEART_ASPECT_LEAVES_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_LEAVES);
	
	public static final DeferredBlock<Block> HEART_ASPECT_SAPLING = MSBlocks.REGISTER.register("heart_aspect_sapling",
			() -> new AspectSaplingBlock(new HeartAspectTree(), Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final DeferredItem<BlockItem> HEART_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	
	public static final DeferredBlock<Block> HEART_ASPECT_PLANKS = MSBlocks.REGISTER.register("heart_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> HEART_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_PLANKS);
	
	public static final DeferredBlock<Block> HEART_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("heart_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> HEART_ASPECT_CARVED_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_CARVED_PLANKS);
	
	public static final DeferredBlock<StairBlock> HEART_ASPECT_STAIRS = MSBlocks.REGISTER.register("heart_aspect_stairs",
			() -> new StairBlock(() -> HEART_ASPECT_PLANKS.get().defaultBlockState(), copy(HEART_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> HEART_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_STAIRS);
	
	public static final DeferredBlock<SlabBlock> HEART_ASPECT_SLAB = MSBlocks.REGISTER.register("heart_aspect_slab",
			() -> new SlabBlock(copy(HEART_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> HEART_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_SLAB);
	
	public static final DeferredBlock<FenceBlock> HEART_ASPECT_FENCE = MSBlocks.REGISTER.register("heart_aspect_fence",
			() -> new FenceBlock(copy(HEART_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> HEART_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_FENCE);
	
	public static final DeferredBlock<FenceGateBlock> HEART_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("heart_aspect_fence_gate",
			() -> new FenceGateBlock(copy(HEART_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredItem<BlockItem> HEART_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_FENCE_GATE);
	
	public static final DeferredBlock<DoorBlock> HEART_ASPECT_DOOR = MSBlocks.REGISTER.register("heart_aspect_door",
			() -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), MSBlockSetType.HEART));
	public static final DeferredItem<BlockItem> HEART_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_DOOR);
	
	public static final DeferredBlock<TrapDoorBlock> HEART_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("heart_aspect_trapdoor",
			() -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), MSBlockSetType.HEART));
	public static final DeferredItem<BlockItem> HEART_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_TRAPDOOR);
	
	public static final DeferredBlock<PressurePlateBlock> HEART_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("heart_aspect_pressure_plate",
			() -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(HEART_ASPECT_PLANKS.get()), MSBlockSetType.HEART));
	public static final DeferredItem<BlockItem> HEART_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_PRESSURE_PLATE);
	
	public static final DeferredBlock<ButtonBlock> HEART_ASPECT_BUTTON = MSBlocks.REGISTER.register("heart_aspect_button",
			() -> new ButtonBlock(copy(HEART_ASPECT_PLANKS.get()), MSBlockSetType.HEART, 10, true));
	public static final DeferredItem<BlockItem> HEART_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_BUTTON);
	
	public static final DeferredBlock<Block> HEART_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("heart_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> HEART_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_BOOKSHELF);
	
	public static final DeferredBlock<Block> HEART_ASPECT_LADDER = MSBlocks.REGISTER.register("heart_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final DeferredItem<BlockItem> HEART_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_LADDER);
	
	public static final DeferredBlock<StandingSignBlock> HEART_ASPECT_SIGN = MSBlocks.REGISTER.register("heart_aspect_sign",
			() -> new MSStandingSignBlock(Block.Properties.copy(Blocks.OAK_SIGN), MSWoodTypes.HEART));
	public static final DeferredBlock<WallSignBlock> HEART_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("heart_aspect_wall_sign",
			() -> new MSWallSignBlock(Block.Properties.copy(Blocks.OAK_WALL_SIGN), MSWoodTypes.HEART));
	public static final DeferredItem<BlockItem> HEART_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("heart_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), HEART_ASPECT_SIGN.get(), HEART_ASPECT_WALL_SIGN.get()));
	public static final DeferredBlock<Block> HEART_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("heart_aspect_hanging_sign",
			() -> new MSHangingSignBlock(Block.Properties.copy(Blocks.OAK_HANGING_SIGN), MSWoodTypes.HEART));
	public static final DeferredBlock<Block> HEART_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("heart_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(Block.Properties.copy(Blocks.OAK_WALL_HANGING_SIGN), MSWoodTypes.HEART));
	public static final DeferredItem<BlockItem> HEART_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("heart_aspect_hanging_sign",
			() -> new HangingSignItem(HEART_ASPECT_HANGING_SIGN.get(), HEART_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
	
	//Hope
	public static final DeferredBlock<Block> HOPE_ASPECT_LOG = MSBlocks.REGISTER.register("hope_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.HOPE_ASPECT_STRIPPED_LOG.get().defaultBlockState()));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_LOG);
	
	public static final DeferredBlock<Block> HOPE_ASPECT_WOOD = MSBlocks.REGISTER.register("hope_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.HOPE_ASPECT_STRIPPED_WOOD.get().defaultBlockState()));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_WOOD);
	
	public static final DeferredBlock<Block> HOPE_ASPECT_STRIPPED_LOG = MSBlocks.REGISTER.register("hope_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_STRIPPED_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_STRIPPED_LOG);
	
	public static final DeferredBlock<Block> HOPE_ASPECT_STRIPPED_WOOD = MSBlocks.REGISTER.register("hope_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_STRIPPED_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_STRIPPED_WOOD);
	
	public static final DeferredBlock<Block> HOPE_ASPECT_LEAVES = MSBlocks.REGISTER.register("hope_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_LEAVES_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_LEAVES);
	
	public static final DeferredBlock<Block> HOPE_ASPECT_SAPLING = MSBlocks.REGISTER.register("hope_aspect_sapling",
			() -> new AspectSaplingBlock(new HopeAspectTree(), Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	
	public static final DeferredBlock<Block> HOPE_ASPECT_PLANKS = MSBlocks.REGISTER.register("hope_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_PLANKS);
	
	public static final DeferredBlock<Block> HOPE_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("hope_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_CARVED_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_CARVED_PLANKS);
	
	public static final DeferredBlock<StairBlock> HOPE_ASPECT_STAIRS = MSBlocks.REGISTER.register("hope_aspect_stairs",
			() -> new StairBlock(() -> HOPE_ASPECT_PLANKS.get().defaultBlockState(), copy(HOPE_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_STAIRS);
	
	public static final DeferredBlock<SlabBlock> HOPE_ASPECT_SLAB = MSBlocks.REGISTER.register("hope_aspect_slab",
			() -> new SlabBlock(copy(HOPE_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_SLAB);
	
	public static final DeferredBlock<FenceBlock> HOPE_ASPECT_FENCE = MSBlocks.REGISTER.register("hope_aspect_fence",
			() -> new FenceBlock(copy(HOPE_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_FENCE);
	
	public static final DeferredBlock<FenceGateBlock> HOPE_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("hope_aspect_fence_gate",
			() -> new FenceGateBlock(copy(HOPE_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_FENCE_GATE);
	
	public static final DeferredBlock<DoorBlock> HOPE_ASPECT_DOOR = MSBlocks.REGISTER.register("hope_aspect_door",
			() -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), MSBlockSetType.HOPE));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_DOOR);
	
	public static final DeferredBlock<TrapDoorBlock> HOPE_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("hope_aspect_trapdoor",
			() -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), MSBlockSetType.HOPE));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_TRAPDOOR);
	
	public static final DeferredBlock<PressurePlateBlock> HOPE_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("hope_aspect_pressure_plate",
			() -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(HOPE_ASPECT_PLANKS.get()), MSBlockSetType.HOPE));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_PRESSURE_PLATE);
	
	public static final DeferredBlock<ButtonBlock> HOPE_ASPECT_BUTTON = MSBlocks.REGISTER.register("hope_aspect_button",
			() -> new ButtonBlock(copy(HOPE_ASPECT_PLANKS.get()), MSBlockSetType.HOPE, 10, true));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_BUTTON);
	
	public static final DeferredBlock<Block> HOPE_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("hope_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_BOOKSHELF);
	
	public static final DeferredBlock<Block> HOPE_ASPECT_LADDER = MSBlocks.REGISTER.register("hope_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_LADDER);
	
	public static final DeferredBlock<StandingSignBlock> HOPE_ASPECT_SIGN = MSBlocks.REGISTER.register("hope_aspect_sign",
			() -> new MSStandingSignBlock(Block.Properties.copy(Blocks.OAK_SIGN), MSWoodTypes.HOPE));
	public static final DeferredBlock<WallSignBlock> HOPE_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("hope_aspect_wall_sign",
			() -> new MSWallSignBlock(Block.Properties.copy(Blocks.OAK_WALL_SIGN), MSWoodTypes.HOPE));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("hope_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), HOPE_ASPECT_SIGN.get(), HOPE_ASPECT_WALL_SIGN.get()));
	public static final DeferredBlock<Block> HOPE_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("hope_aspect_hanging_sign",
			() -> new MSHangingSignBlock(Block.Properties.copy(Blocks.OAK_HANGING_SIGN), MSWoodTypes.HOPE));
	public static final DeferredBlock<Block> HOPE_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("hope_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(Block.Properties.copy(Blocks.OAK_WALL_HANGING_SIGN), MSWoodTypes.HOPE));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("hope_aspect_hanging_sign",
			() -> new HangingSignItem(HOPE_ASPECT_HANGING_SIGN.get(), HOPE_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
	
	//Life
	public static final DeferredBlock<Block> LIFE_ASPECT_LOG = MSBlocks.REGISTER.register("life_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.LIFE_ASPECT_STRIPPED_LOG.get().defaultBlockState()));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_LOG);
	
	public static final DeferredBlock<Block> LIFE_ASPECT_WOOD = MSBlocks.REGISTER.register("life_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.LIFE_ASPECT_STRIPPED_WOOD.get().defaultBlockState()));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_WOOD);
	
	public static final DeferredBlock<Block> LIFE_ASPECT_STRIPPED_LOG = MSBlocks.REGISTER.register("life_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_STRIPPED_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_STRIPPED_LOG);
	
	public static final DeferredBlock<Block> LIFE_ASPECT_STRIPPED_WOOD = MSBlocks.REGISTER.register("life_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_STRIPPED_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_STRIPPED_WOOD);
	
	public static final DeferredBlock<Block> LIFE_ASPECT_LEAVES = MSBlocks.REGISTER.register("life_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_LEAVES_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_LEAVES);
	
	public static final DeferredBlock<Block> LIFE_ASPECT_SAPLING = MSBlocks.REGISTER.register("life_aspect_sapling",
			() -> new AspectSaplingBlock(new LifeAspectTree(), Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	
	public static final DeferredBlock<Block> LIFE_ASPECT_PLANKS = MSBlocks.REGISTER.register("life_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_PLANKS);
	
	public static final DeferredBlock<Block> LIFE_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("life_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_CARVED_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_CARVED_PLANKS);
	
	public static final DeferredBlock<StairBlock> LIFE_ASPECT_STAIRS = MSBlocks.REGISTER.register("life_aspect_stairs",
			() -> new StairBlock(() -> LIFE_ASPECT_PLANKS.get().defaultBlockState(), copy(LIFE_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_STAIRS);
	
	public static final DeferredBlock<SlabBlock> LIFE_ASPECT_SLAB = MSBlocks.REGISTER.register("life_aspect_slab",
			() -> new SlabBlock(copy(LIFE_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_SLAB);
	
	public static final DeferredBlock<FenceBlock> LIFE_ASPECT_FENCE = MSBlocks.REGISTER.register("life_aspect_fence",
			() -> new FenceBlock(copy(LIFE_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_FENCE);
	
	public static final DeferredBlock<FenceGateBlock> LIFE_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("life_aspect_fence_gate",
			() -> new FenceGateBlock(copy(LIFE_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_FENCE_GATE);
	
	public static final DeferredBlock<DoorBlock> LIFE_ASPECT_DOOR = MSBlocks.REGISTER.register("life_aspect_door",
			() -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), MSBlockSetType.LIFE));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_DOOR);
	
	public static final DeferredBlock<TrapDoorBlock> LIFE_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("life_aspect_trapdoor",
			() -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), MSBlockSetType.LIFE));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_TRAPDOOR);
	
	public static final DeferredBlock<PressurePlateBlock> LIFE_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("life_aspect_pressure_plate",
			() -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(LIFE_ASPECT_PLANKS.get()), MSBlockSetType.LIFE));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_PRESSURE_PLATE);
	
	public static final DeferredBlock<ButtonBlock> LIFE_ASPECT_BUTTON = MSBlocks.REGISTER.register("life_aspect_button",
			() -> new ButtonBlock(copy(LIFE_ASPECT_PLANKS.get()), MSBlockSetType.LIFE, 10, true));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_BUTTON);
	
	public static final DeferredBlock<Block> LIFE_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("life_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_BOOKSHELF);
	
	public static final DeferredBlock<Block> LIFE_ASPECT_LADDER = MSBlocks.REGISTER.register("life_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_LADDER);
	
	public static final DeferredBlock<StandingSignBlock> LIFE_ASPECT_SIGN = MSBlocks.REGISTER.register("life_aspect_sign",
			() -> new MSStandingSignBlock(Block.Properties.copy(Blocks.OAK_SIGN), MSWoodTypes.LIFE));
	public static final DeferredBlock<WallSignBlock> LIFE_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("life_aspect_wall_sign",
			() -> new MSWallSignBlock(Block.Properties.copy(Blocks.OAK_WALL_SIGN), MSWoodTypes.LIFE));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("life_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), LIFE_ASPECT_SIGN.get(), LIFE_ASPECT_WALL_SIGN.get()));
	public static final DeferredBlock<Block> LIFE_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("life_aspect_hanging_sign",
			() -> new MSHangingSignBlock(Block.Properties.copy(Blocks.OAK_HANGING_SIGN), MSWoodTypes.LIFE));
	public static final DeferredBlock<Block> LIFE_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("life_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(Block.Properties.copy(Blocks.OAK_WALL_HANGING_SIGN), MSWoodTypes.LIFE));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("life_aspect_hanging_sign",
			() -> new HangingSignItem(LIFE_ASPECT_HANGING_SIGN.get(), LIFE_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
	
	//Light
	public static final DeferredBlock<Block> LIGHT_ASPECT_LOG = MSBlocks.REGISTER.register("light_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.LIGHT_ASPECT_STRIPPED_LOG.get().defaultBlockState()));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_LOG);
	
	public static final DeferredBlock<Block> LIGHT_ASPECT_WOOD = MSBlocks.REGISTER.register("light_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.LIGHT_ASPECT_STRIPPED_WOOD.get().defaultBlockState()));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_WOOD);
	
	public static final DeferredBlock<Block> LIGHT_ASPECT_STRIPPED_LOG = MSBlocks.REGISTER.register("light_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_STRIPPED_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_STRIPPED_LOG);
	
	public static final DeferredBlock<Block> LIGHT_ASPECT_STRIPPED_WOOD = MSBlocks.REGISTER.register("light_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_STRIPPED_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_STRIPPED_WOOD);
	
	public static final DeferredBlock<Block> LIGHT_ASPECT_LEAVES = MSBlocks.REGISTER.register("light_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_LEAVES_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_LEAVES);
	
	public static final DeferredBlock<Block> LIGHT_ASPECT_SAPLING = MSBlocks.REGISTER.register("light_aspect_sapling",
			() -> new AspectSaplingBlock(new LightAspectTree(), Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	
	public static final DeferredBlock<Block> LIGHT_ASPECT_PLANKS = MSBlocks.REGISTER.register("light_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_PLANKS);
	
	public static final DeferredBlock<Block> LIGHT_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("light_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_CARVED_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_CARVED_PLANKS);
	
	public static final DeferredBlock<StairBlock> LIGHT_ASPECT_STAIRS = MSBlocks.REGISTER.register("light_aspect_stairs",
			() -> new StairBlock(() -> LIGHT_ASPECT_PLANKS.get().defaultBlockState(), copy(LIGHT_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_STAIRS);
	
	public static final DeferredBlock<SlabBlock> LIGHT_ASPECT_SLAB = MSBlocks.REGISTER.register("light_aspect_slab",
			() -> new SlabBlock(copy(LIGHT_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_SLAB);
	
	public static final DeferredBlock<FenceBlock> LIGHT_ASPECT_FENCE = MSBlocks.REGISTER.register("light_aspect_fence",
			() -> new FenceBlock(copy(LIGHT_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_FENCE);
	
	public static final DeferredBlock<FenceGateBlock> LIGHT_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("light_aspect_fence_gate",
			() -> new FenceGateBlock(copy(LIGHT_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_FENCE_GATE);
	
	public static final DeferredBlock<DoorBlock> LIGHT_ASPECT_DOOR = MSBlocks.REGISTER.register("light_aspect_door",
			() -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), MSBlockSetType.LIGHT));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_DOOR);
	
	public static final DeferredBlock<TrapDoorBlock> LIGHT_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("light_aspect_trapdoor",
			() -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), MSBlockSetType.LIGHT));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_TRAPDOOR);
	
	public static final DeferredBlock<PressurePlateBlock> LIGHT_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("light_aspect_pressure_plate",
			() -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(LIGHT_ASPECT_PLANKS.get()), MSBlockSetType.LIGHT));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_PRESSURE_PLATE);
	
	public static final DeferredBlock<ButtonBlock> LIGHT_ASPECT_BUTTON = MSBlocks.REGISTER.register("light_aspect_button",
			() -> new ButtonBlock(copy(LIGHT_ASPECT_PLANKS.get()), MSBlockSetType.LIGHT, 10, true));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_BUTTON);
	
	public static final DeferredBlock<Block> LIGHT_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("light_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_BOOKSHELF);
	
	public static final DeferredBlock<Block> LIGHT_ASPECT_LADDER = MSBlocks.REGISTER.register("light_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_LADDER);
	
	public static final DeferredBlock<StandingSignBlock> LIGHT_ASPECT_SIGN = MSBlocks.REGISTER.register("light_aspect_sign",
			() -> new MSStandingSignBlock(Block.Properties.copy(Blocks.OAK_SIGN), MSWoodTypes.LIGHT));
	public static final DeferredBlock<WallSignBlock> LIGHT_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("light_aspect_wall_sign",
			() -> new MSWallSignBlock(Block.Properties.copy(Blocks.OAK_WALL_SIGN), MSWoodTypes.LIGHT));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("light_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), LIGHT_ASPECT_SIGN.get(), LIGHT_ASPECT_WALL_SIGN.get()));
	public static final DeferredBlock<Block> LIGHT_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("light_aspect_hanging_sign",
			() -> new MSHangingSignBlock(Block.Properties.copy(Blocks.OAK_HANGING_SIGN), MSWoodTypes.LIGHT));
	public static final DeferredBlock<Block> LIGHT_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("light_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(Block.Properties.copy(Blocks.OAK_WALL_HANGING_SIGN), MSWoodTypes.LIGHT));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("light_aspect_hanging_sign",
			() -> new HangingSignItem(LIGHT_ASPECT_HANGING_SIGN.get(), LIGHT_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
	
	//Mind
	public static final DeferredBlock<Block> MIND_ASPECT_LOG = MSBlocks.REGISTER.register("mind_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.MIND_ASPECT_STRIPPED_LOG.get().defaultBlockState()));
	public static final DeferredItem<BlockItem> MIND_ASPECT_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_LOG);
	
	public static final DeferredBlock<Block> MIND_ASPECT_WOOD = MSBlocks.REGISTER.register("mind_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.MIND_ASPECT_STRIPPED_WOOD.get().defaultBlockState()));
	public static final DeferredItem<BlockItem> MIND_ASPECT_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_WOOD);
	
	public static final DeferredBlock<Block> MIND_ASPECT_STRIPPED_LOG = MSBlocks.REGISTER.register("mind_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> MIND_ASPECT_STRIPPED_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_STRIPPED_LOG);
	
	public static final DeferredBlock<Block> MIND_ASPECT_STRIPPED_WOOD = MSBlocks.REGISTER.register("mind_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> MIND_ASPECT_STRIPPED_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_STRIPPED_WOOD);
	
	public static final DeferredBlock<Block> MIND_ASPECT_LEAVES = MSBlocks.REGISTER.register("mind_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final DeferredItem<BlockItem> MIND_ASPECT_LEAVES_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_LEAVES);
	
	public static final DeferredBlock<Block> MIND_ASPECT_SAPLING = MSBlocks.REGISTER.register("mind_aspect_sapling",
			() -> new AspectSaplingBlock(new MindAspectTree(), Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final DeferredItem<BlockItem> MIND_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	
	public static final DeferredBlock<Block> MIND_ASPECT_PLANKS = MSBlocks.REGISTER.register("mind_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> MIND_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_PLANKS);
	
		public static final DeferredBlock<Block> MIND_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("mind_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> MIND_ASPECT_PLANKS_CARVED_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_CARVED_PLANKS);
	
	public static final DeferredBlock<StairBlock> MIND_ASPECT_STAIRS = MSBlocks.REGISTER.register("mind_aspect_stairs",
			() -> new StairBlock(() -> MIND_ASPECT_PLANKS.get().defaultBlockState(), copy(MIND_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> MIND_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_STAIRS);
	
	public static final DeferredBlock<SlabBlock> MIND_ASPECT_SLAB = MSBlocks.REGISTER.register("mind_aspect_slab",
			() -> new SlabBlock(copy(MIND_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> MIND_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_SLAB);
	
	public static final DeferredBlock<FenceBlock> MIND_ASPECT_FENCE = MSBlocks.REGISTER.register("mind_aspect_fence",
			() -> new FenceBlock(copy(MIND_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> MIND_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_FENCE);
	
	public static final DeferredBlock<FenceGateBlock> MIND_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("mind_aspect_fence_gate",
			() -> new FenceGateBlock(copy(MIND_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredItem<BlockItem> MIND_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_FENCE_GATE);
	
	public static final DeferredBlock<DoorBlock> MIND_ASPECT_DOOR = MSBlocks.REGISTER.register("mind_aspect_door",
			() -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), MSBlockSetType.MIND));
	public static final DeferredItem<BlockItem> MIND_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_DOOR);
	
	public static final DeferredBlock<TrapDoorBlock> MIND_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("mind_aspect_trapdoor",
			() -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), MSBlockSetType.MIND));
	public static final DeferredItem<BlockItem> MIND_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_TRAPDOOR);
	
	public static final DeferredBlock<PressurePlateBlock> MIND_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("mind_aspect_pressure_plate",
			() -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(MIND_ASPECT_PLANKS.get()), MSBlockSetType.MIND));
	public static final DeferredItem<BlockItem> MIND_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_PRESSURE_PLATE);
	
	public static final DeferredBlock<ButtonBlock> MIND_ASPECT_BUTTON = MSBlocks.REGISTER.register("mind_aspect_button",
			() -> new ButtonBlock(copy(MIND_ASPECT_PLANKS.get()), MSBlockSetType.MIND, 10, true));
	public static final DeferredItem<BlockItem> MIND_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_BUTTON);
	
	public static final DeferredBlock<Block> MIND_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("mind_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> MIND_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_BOOKSHELF);
	
	public static final DeferredBlock<Block> MIND_ASPECT_LADDER = MSBlocks.REGISTER.register("mind_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final DeferredItem<BlockItem> MIND_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_LADDER);
	
	public static final DeferredBlock<StandingSignBlock> MIND_ASPECT_SIGN = MSBlocks.REGISTER.register("mind_aspect_sign",
			() -> new MSStandingSignBlock(Block.Properties.copy(Blocks.OAK_SIGN), MSWoodTypes.MIND));
	public static final DeferredBlock<WallSignBlock> MIND_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("mind_aspect_wall_sign",
			() -> new MSWallSignBlock(Block.Properties.copy(Blocks.OAK_WALL_SIGN), MSWoodTypes.MIND));
	public static final DeferredItem<BlockItem> MIND_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("mind_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), MIND_ASPECT_SIGN.get(), MIND_ASPECT_WALL_SIGN.get()));
	public static final DeferredBlock<Block> MIND_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("mind_aspect_hanging_sign",
			() -> new MSHangingSignBlock(Block.Properties.copy(Blocks.OAK_HANGING_SIGN), MSWoodTypes.MIND));
	public static final DeferredBlock<Block> MIND_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("mind_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(Block.Properties.copy(Blocks.OAK_WALL_HANGING_SIGN), MSWoodTypes.MIND));
	public static final DeferredItem<BlockItem> MIND_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("mind_aspect_hanging_sign",
			() -> new HangingSignItem(MIND_ASPECT_HANGING_SIGN.get(), MIND_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
	
	//Rage
	public static final DeferredBlock<Block> RAGE_ASPECT_LOG = MSBlocks.REGISTER.register("rage_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.RAGE_ASPECT_STRIPPED_LOG.get().defaultBlockState()));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_LOG);
	
	public static final DeferredBlock<Block> RAGE_ASPECT_WOOD = MSBlocks.REGISTER.register("rage_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.RAGE_ASPECT_STRIPPED_WOOD.get().defaultBlockState()));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_WOOD);
	
	public static final DeferredBlock<Block> RAGE_ASPECT_STRIPPED_LOG = MSBlocks.REGISTER.register("rage_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_STRIPPED_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_STRIPPED_LOG);
	
	public static final DeferredBlock<Block> RAGE_ASPECT_STRIPPED_WOOD = MSBlocks.REGISTER.register("rage_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_STRIPPED_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_STRIPPED_WOOD);
	
	public static final DeferredBlock<Block> RAGE_ASPECT_LEAVES = MSBlocks.REGISTER.register("rage_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_LEAVES_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_LEAVES);
	
	public static final DeferredBlock<Block> RAGE_ASPECT_SAPLING = MSBlocks.REGISTER.register("rage_aspect_sapling",
			() -> new AspectSaplingBlock(new RageAspectTree(), Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	
	public static final DeferredBlock<Block> RAGE_ASPECT_PLANKS = MSBlocks.REGISTER.register("rage_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_PLANKS);
	
	public static final DeferredBlock<Block> RAGE_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("rage_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_CARVED_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_CARVED_PLANKS);
	
	public static final DeferredBlock<StairBlock> RAGE_ASPECT_STAIRS = MSBlocks.REGISTER.register("rage_aspect_stairs",
			() -> new StairBlock(() -> RAGE_ASPECT_PLANKS.get().defaultBlockState(), copy(RAGE_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_STAIRS);
	
	public static final DeferredBlock<SlabBlock> RAGE_ASPECT_SLAB = MSBlocks.REGISTER.register("rage_aspect_slab",
			() -> new SlabBlock(copy(RAGE_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_SLAB);
	
	public static final DeferredBlock<FenceBlock> RAGE_ASPECT_FENCE = MSBlocks.REGISTER.register("rage_aspect_fence",
			() -> new FenceBlock(copy(RAGE_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_FENCE);
	
	public static final DeferredBlock<FenceGateBlock> RAGE_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("rage_aspect_fence_gate",
			() -> new FenceGateBlock(copy(RAGE_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_FENCE_GATE);
	
	public static final DeferredBlock<DoorBlock> RAGE_ASPECT_DOOR = MSBlocks.REGISTER.register("rage_aspect_door",
			() -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), MSBlockSetType.RAGE));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_DOOR);
	
	public static final DeferredBlock<TrapDoorBlock> RAGE_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("rage_aspect_trapdoor",
			() -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), MSBlockSetType.RAGE));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_TRAPDOOR);
	
	public static final DeferredBlock<PressurePlateBlock> RAGE_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("rage_aspect_pressure_plate",
			() -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(RAGE_ASPECT_PLANKS.get()), MSBlockSetType.RAGE));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_PRESSURE_PLATE);
	
	public static final DeferredBlock<ButtonBlock> RAGE_ASPECT_BUTTON = MSBlocks.REGISTER.register("rage_aspect_button",
			() -> new ButtonBlock(copy(RAGE_ASPECT_PLANKS.get()), MSBlockSetType.RAGE, 10, true));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_BUTTON);
	
	public static final DeferredBlock<Block> RAGE_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("rage_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_BOOKSHELF);
	
	public static final DeferredBlock<Block> RAGE_ASPECT_LADDER = MSBlocks.REGISTER.register("rage_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_LADDER);
	
	public static final DeferredBlock<StandingSignBlock> RAGE_ASPECT_SIGN = MSBlocks.REGISTER.register("rage_aspect_sign",
			() -> new MSStandingSignBlock(Block.Properties.copy(Blocks.OAK_SIGN), MSWoodTypes.RAGE));
	public static final DeferredBlock<WallSignBlock> RAGE_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("rage_aspect_wall_sign",
			() -> new MSWallSignBlock(Block.Properties.copy(Blocks.OAK_WALL_SIGN), MSWoodTypes.RAGE));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("rage_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), RAGE_ASPECT_SIGN.get(), RAGE_ASPECT_WALL_SIGN.get()));
	public static final DeferredBlock<Block> RAGE_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("rage_aspect_hanging_sign",
			() -> new MSHangingSignBlock(Block.Properties.copy(Blocks.OAK_HANGING_SIGN), MSWoodTypes.RAGE));
	public static final DeferredBlock<Block> RAGE_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("rage_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(Block.Properties.copy(Blocks.OAK_WALL_HANGING_SIGN), MSWoodTypes.RAGE));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("rage_aspect_hanging_sign",
			() -> new HangingSignItem(RAGE_ASPECT_HANGING_SIGN.get(), RAGE_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
	
	//Space
	public static final DeferredBlock<Block> SPACE_ASPECT_LOG = MSBlocks.REGISTER.register("space_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.SPACE_ASPECT_STRIPPED_LOG.get().defaultBlockState()));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_LOG);
	
	public static final DeferredBlock<Block> SPACE_ASPECT_WOOD = MSBlocks.REGISTER.register("space_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.SPACE_ASPECT_STRIPPED_WOOD.get().defaultBlockState()));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_WOOD);
	
	public static final DeferredBlock<Block> SPACE_ASPECT_STRIPPED_LOG = MSBlocks.REGISTER.register("space_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_STRIPPED_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_STRIPPED_LOG);
	
	public static final DeferredBlock<Block> SPACE_ASPECT_STRIPPED_WOOD = MSBlocks.REGISTER.register("space_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_STRIPPED_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_STRIPPED_WOOD);
	
	public static final DeferredBlock<Block> SPACE_ASPECT_LEAVES = MSBlocks.REGISTER.register("space_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_LEAVES_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_LEAVES);
	
	public static final DeferredBlock<Block> SPACE_ASPECT_SAPLING = MSBlocks.REGISTER.register("space_aspect_sapling",
			() -> new AspectSaplingBlock(new SpaceAspectTree(), Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	
	public static final DeferredBlock<Block> SPACE_ASPECT_PLANKS = MSBlocks.REGISTER.register("space_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_PLANKS);
	
	public static final DeferredBlock<Block> SPACE_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("space_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_CARVED_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_CARVED_PLANKS);
	
	public static final DeferredBlock<StairBlock> SPACE_ASPECT_STAIRS = MSBlocks.REGISTER.register("space_aspect_stairs",
			() -> new StairBlock(() -> SPACE_ASPECT_PLANKS.get().defaultBlockState(), copy(SPACE_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_STAIRS);
	
	public static final DeferredBlock<SlabBlock> SPACE_ASPECT_SLAB = MSBlocks.REGISTER.register("space_aspect_slab",
			() -> new SlabBlock(copy(SPACE_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_SLAB);
	
	public static final DeferredBlock<FenceBlock> SPACE_ASPECT_FENCE = MSBlocks.REGISTER.register("space_aspect_fence",
			() -> new FenceBlock(copy(SPACE_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_FENCE);
	
	public static final DeferredBlock<FenceGateBlock> SPACE_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("space_aspect_fence_gate",
			() -> new FenceGateBlock(copy(SPACE_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_FENCE_GATE);
	
	public static final DeferredBlock<DoorBlock> SPACE_ASPECT_DOOR = MSBlocks.REGISTER.register("space_aspect_door",
			() -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), MSBlockSetType.SPACE));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_DOOR);
	
	public static final DeferredBlock<TrapDoorBlock> SPACE_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("space_aspect_trapdoor",
			() -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), MSBlockSetType.SPACE));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_TRAPDOOR);
	
	public static final DeferredBlock<PressurePlateBlock> SPACE_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("space_aspect_pressure_plate",
			() -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(SPACE_ASPECT_PLANKS.get()), MSBlockSetType.SPACE));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_PRESSURE_PLATE);
	
	public static final DeferredBlock<ButtonBlock> SPACE_ASPECT_BUTTON = MSBlocks.REGISTER.register("space_aspect_button",
			() -> new ButtonBlock(copy(SPACE_ASPECT_PLANKS.get()), MSBlockSetType.SPACE, 10, true));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_BUTTON);
	
	public static final DeferredBlock<Block> SPACE_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("space_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_BOOKSHELF);
	
	public static final DeferredBlock<Block> SPACE_ASPECT_LADDER = MSBlocks.REGISTER.register("space_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_LADDER);
	
	public static final DeferredBlock<StandingSignBlock> SPACE_ASPECT_SIGN = MSBlocks.REGISTER.register("space_aspect_sign",
			() -> new MSStandingSignBlock(Block.Properties.copy(Blocks.OAK_SIGN), MSWoodTypes.SPACE));
	public static final DeferredBlock<WallSignBlock> SPACE_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("space_aspect_wall_sign",
			() -> new MSWallSignBlock(Block.Properties.copy(Blocks.OAK_WALL_SIGN), MSWoodTypes.SPACE));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("space_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), SPACE_ASPECT_SIGN.get(), SPACE_ASPECT_WALL_SIGN.get()));
	public static final DeferredBlock<Block> SPACE_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("space_aspect_hanging_sign",
			() -> new MSHangingSignBlock(Block.Properties.copy(Blocks.OAK_HANGING_SIGN), MSWoodTypes.SPACE));
	public static final DeferredBlock<Block> SPACE_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("space_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(Block.Properties.copy(Blocks.OAK_WALL_HANGING_SIGN), MSWoodTypes.SPACE));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("space_aspect_hanging_sign",
			() -> new HangingSignItem(SPACE_ASPECT_HANGING_SIGN.get(), SPACE_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
	
	//Time
	public static final DeferredBlock<Block> TIME_ASPECT_LOG = MSBlocks.REGISTER.register("time_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.TIME_ASPECT_STRIPPED_LOG.get().defaultBlockState()));
	public static final DeferredItem<BlockItem> TIME_ASPECT_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_LOG);
	
	public static final DeferredBlock<Block> TIME_ASPECT_WOOD = MSBlocks.REGISTER.register("time_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.TIME_ASPECT_STRIPPED_WOOD.get().defaultBlockState()));
	public static final DeferredItem<BlockItem> TIME_ASPECT_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_WOOD);
	
	public static final DeferredBlock<Block> TIME_ASPECT_STRIPPED_LOG = MSBlocks.REGISTER.register("time_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> TIME_ASPECT_STRIPPED_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_STRIPPED_LOG);
	
	public static final DeferredBlock<Block> TIME_ASPECT_STRIPPED_WOOD = MSBlocks.REGISTER.register("time_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> TIME_ASPECT_STRIPPED_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_STRIPPED_WOOD);
	
	public static final DeferredBlock<Block> TIME_ASPECT_LEAVES = MSBlocks.REGISTER.register("time_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final DeferredItem<BlockItem> TIME_ASPECT_LEAVES_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_LEAVES);
	
	public static final DeferredBlock<Block> TIME_ASPECT_SAPLING = MSBlocks.REGISTER.register("time_aspect_sapling",
			() -> new AspectSaplingBlock(new TimeAspectTree(), Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final DeferredItem<BlockItem> TIME_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	
	public static final DeferredBlock<Block> TIME_ASPECT_PLANKS = MSBlocks.REGISTER.register("time_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> TIME_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_PLANKS);
	
	public static final DeferredBlock<Block> TIME_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("time_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> TIME_ASPECT_CARVED_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_CARVED_PLANKS);
	
	public static final DeferredBlock<StairBlock> TIME_ASPECT_STAIRS = MSBlocks.REGISTER.register("time_aspect_stairs",
			() -> new StairBlock(() -> TIME_ASPECT_PLANKS.get().defaultBlockState(), copy(TIME_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> TIME_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_STAIRS);
	
	public static final DeferredBlock<SlabBlock> TIME_ASPECT_SLAB = MSBlocks.REGISTER.register("time_aspect_slab",
			() -> new SlabBlock(copy(TIME_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> TIME_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_SLAB);
	
	public static final DeferredBlock<FenceBlock> TIME_ASPECT_FENCE = MSBlocks.REGISTER.register("time_aspect_fence",
			() -> new FenceBlock(copy(TIME_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> TIME_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_FENCE);
	
	public static final DeferredBlock<FenceGateBlock> TIME_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("time_aspect_fence_gate",
			() -> new FenceGateBlock(copy(TIME_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredItem<BlockItem> TIME_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_FENCE_GATE);
	
	public static final DeferredBlock<DoorBlock> TIME_ASPECT_DOOR = MSBlocks.REGISTER.register("time_aspect_door",
			() -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), MSBlockSetType.TIME));
	public static final DeferredItem<BlockItem> TIME_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_DOOR);
	
	public static final DeferredBlock<TrapDoorBlock> TIME_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("time_aspect_trapdoor",
			() -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), MSBlockSetType.TIME));
	public static final DeferredItem<BlockItem> TIME_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_TRAPDOOR);
	
	public static final DeferredBlock<PressurePlateBlock> TIME_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("time_aspect_pressure_plate",
			() -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(TIME_ASPECT_PLANKS.get()), MSBlockSetType.TIME));
	public static final DeferredItem<BlockItem> TIME_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_PRESSURE_PLATE);
	
	public static final DeferredBlock<ButtonBlock> TIME_ASPECT_BUTTON = MSBlocks.REGISTER.register("time_aspect_button",
			() -> new ButtonBlock(copy(TIME_ASPECT_PLANKS.get()), MSBlockSetType.TIME, 10, true));
	public static final DeferredItem<BlockItem> TIME_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_BUTTON);
	
	public static final DeferredBlock<Block> TIME_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("time_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> TIME_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_BOOKSHELF);
	
	public static final DeferredBlock<Block> TIME_ASPECT_LADDER = MSBlocks.REGISTER.register("time_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final DeferredItem<BlockItem> TIME_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_LADDER);
	
	public static final DeferredBlock<StandingSignBlock> TIME_ASPECT_SIGN = MSBlocks.REGISTER.register("time_aspect_sign",
			() -> new MSStandingSignBlock(Block.Properties.copy(Blocks.OAK_SIGN), MSWoodTypes.TIME));
	public static final DeferredBlock<WallSignBlock> TIME_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("time_aspect_wall_sign",
			() -> new MSWallSignBlock(Block.Properties.copy(Blocks.OAK_WALL_SIGN), MSWoodTypes.TIME));
	public static final DeferredItem<BlockItem> TIME_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("time_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), TIME_ASPECT_SIGN.get(), TIME_ASPECT_WALL_SIGN.get()));
	public static final DeferredBlock<Block> TIME_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("time_aspect_hanging_sign",
			() -> new MSHangingSignBlock(Block.Properties.copy(Blocks.OAK_HANGING_SIGN), MSWoodTypes.TIME));
	public static final DeferredBlock<Block> TIME_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("time_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(Block.Properties.copy(Blocks.OAK_WALL_HANGING_SIGN), MSWoodTypes.TIME));
	public static final DeferredItem<BlockItem> TIME_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("time_aspect_hanging_sign",
			() -> new HangingSignItem(TIME_ASPECT_HANGING_SIGN.get(), TIME_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
	
	//Void
	public static final DeferredBlock<Block> VOID_ASPECT_LOG = MSBlocks.REGISTER.register("void_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.VOID_ASPECT_STRIPPED_LOG.get().defaultBlockState()));
	public static final DeferredItem<BlockItem> VOID_ASPECT_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_LOG);
	
	public static final DeferredBlock<Block> VOID_ASPECT_WOOD = MSBlocks.REGISTER.register("void_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> AspectTreeBlocks.VOID_ASPECT_STRIPPED_WOOD.get().defaultBlockState()));
	public static final DeferredItem<BlockItem> VOID_ASPECT_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_WOOD);
	
	public static final DeferredBlock<Block> VOID_ASPECT_STRIPPED_LOG = MSBlocks.REGISTER.register("void_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> VOID_ASPECT_STRIPPED_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_STRIPPED_LOG);
	
	public static final DeferredBlock<Block> VOID_ASPECT_STRIPPED_WOOD = MSBlocks.REGISTER.register("void_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> VOID_ASPECT_STRIPPED_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_STRIPPED_WOOD);
	
	public static final DeferredBlock<Block> VOID_ASPECT_LEAVES = MSBlocks.REGISTER.register("void_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final DeferredItem<BlockItem> VOID_ASPECT_LEAVES_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_LEAVES);
	
	public static final DeferredBlock<Block> VOID_ASPECT_SAPLING = MSBlocks.REGISTER.register("void_aspect_sapling",
			() -> new AspectSaplingBlock(new VoidAspectTree(), Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final DeferredItem<BlockItem> VOID_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	
	public static final DeferredBlock<Block> VOID_ASPECT_PLANKS = MSBlocks.REGISTER.register("void_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> VOID_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_PLANKS);
	
	public static final DeferredBlock<Block> VOID_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("void_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> VOID_ASPECT_CARVED_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_CARVED_PLANKS);
	
	public static final DeferredBlock<StairBlock> VOID_ASPECT_STAIRS = MSBlocks.REGISTER.register("void_aspect_stairs",
			() -> new StairBlock(() -> VOID_ASPECT_PLANKS.get().defaultBlockState(), copy(VOID_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> VOID_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_STAIRS);
	
	public static final DeferredBlock<SlabBlock> VOID_ASPECT_SLAB = MSBlocks.REGISTER.register("void_aspect_slab",
			() -> new SlabBlock(copy(VOID_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> VOID_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_SLAB);
	
	public static final DeferredBlock<FenceBlock> VOID_ASPECT_FENCE = MSBlocks.REGISTER.register("void_aspect_fence",
			() -> new FenceBlock(copy(VOID_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> VOID_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_FENCE);
	
	public static final DeferredBlock<FenceGateBlock> VOID_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("void_aspect_fence_gate",
			() -> new FenceGateBlock(copy(VOID_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredItem<BlockItem> VOID_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_FENCE_GATE);
	
	public static final DeferredBlock<DoorBlock> VOID_ASPECT_DOOR = MSBlocks.REGISTER.register("void_aspect_door",
			() -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), MSBlockSetType.VOID));
	public static final DeferredItem<BlockItem> VOID_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_DOOR);
	
	public static final DeferredBlock<TrapDoorBlock> VOID_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("void_aspect_trapdoor",
			() -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), MSBlockSetType.VOID));
	public static final DeferredItem<BlockItem> VOID_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_TRAPDOOR);
	
	public static final DeferredBlock<PressurePlateBlock> VOID_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("void_aspect_pressure_plate",
			() -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(VOID_ASPECT_PLANKS.get()), MSBlockSetType.VOID));
	public static final DeferredItem<BlockItem> VOID_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_PRESSURE_PLATE);
	
	public static final DeferredBlock<ButtonBlock> VOID_ASPECT_BUTTON = MSBlocks.REGISTER.register("void_aspect_button",
			() -> new ButtonBlock(copy(VOID_ASPECT_PLANKS.get()), MSBlockSetType.VOID, 10, true));
	public static final DeferredItem<BlockItem> VOID_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_BUTTON);
	
	public static final DeferredBlock<Block> VOID_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("void_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> VOID_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_BOOKSHELF);
	
	public static final DeferredBlock<Block> VOID_ASPECT_LADDER = MSBlocks.REGISTER.register("void_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final DeferredItem<BlockItem> VOID_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_LADDER);
	
	public static final DeferredBlock<StandingSignBlock> VOID_ASPECT_SIGN = MSBlocks.REGISTER.register("void_aspect_sign",
			() -> new MSStandingSignBlock(Block.Properties.copy(Blocks.OAK_SIGN), MSWoodTypes.VOID));
	public static final DeferredBlock<WallSignBlock> VOID_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("void_aspect_wall_sign",
			() -> new MSWallSignBlock(Block.Properties.copy(Blocks.OAK_WALL_SIGN), MSWoodTypes.VOID));
	public static final DeferredItem<BlockItem> VOID_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("void_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), VOID_ASPECT_SIGN.get(), VOID_ASPECT_WALL_SIGN.get()));
	public static final DeferredBlock<Block> VOID_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("void_aspect_hanging_sign",
			() -> new MSHangingSignBlock(Block.Properties.copy(Blocks.OAK_HANGING_SIGN), MSWoodTypes.VOID));
	public static final DeferredBlock<Block> VOID_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("void_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(Block.Properties.copy(Blocks.OAK_WALL_HANGING_SIGN), MSWoodTypes.VOID));
	public static final DeferredItem<BlockItem> VOID_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("void_aspect_hanging_sign",
			() -> new HangingSignItem(VOID_ASPECT_HANGING_SIGN.get(), VOID_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
}
