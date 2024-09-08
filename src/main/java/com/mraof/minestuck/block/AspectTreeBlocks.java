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
			() -> new AspectSaplingBlock(new TreeGrower(Minestuck.id("blood_aspect").toString(), Optional.empty(), Optional.of(MSCFeatures.BLOOD_TREE), Optional.empty()),
					Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final DeferredBlock<Block> POTTED_BLOOD_ASPECT_SAPLING = MSBlocks.REGISTER.register("potted_blood_aspect_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, BLOOD_ASPECT_SAPLING, ofFullCopy(Blocks.POTTED_OAK_SAPLING)));
	
	public static final DeferredBlock<Block> BLOOD_ASPECT_PLANKS = MSBlocks.REGISTER.register("blood_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_PLANKS);
	
	public static final DeferredBlock<Block> BLOOD_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("blood_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_CARVED_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_CARVED_PLANKS);
	
	public static final DeferredBlock<StairBlock> BLOOD_ASPECT_STAIRS = MSBlocks.REGISTER.register("blood_aspect_stairs",
			() -> new StairBlock(() -> BLOOD_ASPECT_PLANKS.get().defaultBlockState(), ofFullCopy(BLOOD_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_STAIRS);
	
	public static final DeferredBlock<SlabBlock> BLOOD_ASPECT_SLAB = MSBlocks.REGISTER.register("blood_aspect_slab",
			() -> new SlabBlock(ofFullCopy(BLOOD_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_SLAB);
	
	public static final DeferredBlock<FenceBlock> BLOOD_ASPECT_FENCE = MSBlocks.REGISTER.register("blood_aspect_fence",
			() -> new FenceBlock(ofFullCopy(BLOOD_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_FENCE);
	
	public static final DeferredBlock<FenceGateBlock> BLOOD_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("blood_aspect_fence_gate",
			() -> new FenceGateBlock(ofFullCopy(BLOOD_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_FENCE_GATE);
	
	public static final DeferredBlock<DoorBlock> BLOOD_ASPECT_DOOR = MSBlocks.REGISTER.register("blood_aspect_door",
			() -> new DoorBlock(MSBlockSetType.BLOOD, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_DOOR);
	
	public static final DeferredBlock<TrapDoorBlock> BLOOD_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("blood_aspect_trapdoor",
			() -> new TrapDoorBlock(MSBlockSetType.BLOOD, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_TRAPDOOR);
	
	public static final DeferredBlock<PressurePlateBlock> BLOOD_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("blood_aspect_pressure_plate",
			() -> new PressurePlateBlock(MSBlockSetType.BLOOD, ofFullCopy(BLOOD_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_PRESSURE_PLATE);
	
	public static final DeferredBlock<ButtonBlock> BLOOD_ASPECT_BUTTON = MSBlocks.REGISTER.register("blood_aspect_button",
			() -> new ButtonBlock(MSBlockSetType.BLOOD, 10, ofFullCopy(BLOOD_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_BUTTON);
	
	public static final DeferredBlock<Block> BLOOD_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("blood_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_BOOKSHELF);
	
	public static final DeferredBlock<Block> BLOOD_ASPECT_LADDER = MSBlocks.REGISTER.register("blood_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final DeferredItem<BlockItem> BLOOD_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_LADDER);
	
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
			() -> new AspectSaplingBlock(new TreeGrower(Minestuck.id("breath_aspect").toString(), Optional.empty(), Optional.of(MSCFeatures.BREATH_TREE), Optional.empty()),
					Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final DeferredBlock<Block> POTTED_BREATH_ASPECT_SAPLING = MSBlocks.REGISTER.register("potted_breath_aspect_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, BREATH_ASPECT_SAPLING, ofFullCopy(Blocks.POTTED_OAK_SAPLING)));
	
	public static final DeferredBlock<Block> BREATH_ASPECT_PLANKS = MSBlocks.REGISTER.register("breath_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_PLANKS);
	
	public static final DeferredBlock<Block> BREATH_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("breath_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_CARVED_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_CARVED_PLANKS);
	
	public static final DeferredBlock<StairBlock> BREATH_ASPECT_STAIRS = MSBlocks.REGISTER.register("breath_aspect_stairs",
			() -> new StairBlock(() -> BREATH_ASPECT_PLANKS.get().defaultBlockState(), ofFullCopy(BREATH_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_STAIRS);
	
	public static final DeferredBlock<SlabBlock> BREATH_ASPECT_SLAB = MSBlocks.REGISTER.register("breath_aspect_slab",
			() -> new SlabBlock(ofFullCopy(BREATH_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_SLAB);
	
	public static final DeferredBlock<FenceBlock> BREATH_ASPECT_FENCE = MSBlocks.REGISTER.register("breath_aspect_fence",
			() -> new FenceBlock(ofFullCopy(BREATH_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_FENCE);
	
	public static final DeferredBlock<FenceGateBlock> BREATH_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("breath_aspect_fence_gate",
			() -> new FenceGateBlock(ofFullCopy(BREATH_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_FENCE_GATE);
	
	public static final DeferredBlock<DoorBlock> BREATH_ASPECT_DOOR = MSBlocks.REGISTER.register("breath_aspect_door",
			() -> new DoorBlock(MSBlockSetType.BREATH, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_DOOR);
	
	public static final DeferredBlock<TrapDoorBlock> BREATH_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("breath_aspect_trapdoor",
			() -> new TrapDoorBlock(MSBlockSetType.BREATH, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_TRAPDOOR);
	
	public static final DeferredBlock<PressurePlateBlock> BREATH_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("breath_aspect_pressure_plate",
			() -> new PressurePlateBlock(MSBlockSetType.BREATH, ofFullCopy(BREATH_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_PRESSURE_PLATE);
	
	public static final DeferredBlock<ButtonBlock> BREATH_ASPECT_BUTTON = MSBlocks.REGISTER.register("breath_aspect_button",
			() -> new ButtonBlock(MSBlockSetType.BREATH, 10, ofFullCopy(BREATH_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_BUTTON);
	
	public static final DeferredBlock<Block> BREATH_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("breath_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_BOOKSHELF);
	
	public static final DeferredBlock<Block> BREATH_ASPECT_LADDER = MSBlocks.REGISTER.register("breath_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final DeferredItem<BlockItem> BREATH_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_LADDER);
	
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
			() -> new AspectSaplingBlock(new TreeGrower(Minestuck.id("doom_aspect").toString(), Optional.empty(), Optional.of(MSCFeatures.DOOM_TREE), Optional.empty()),
					Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final DeferredBlock<Block> POTTED_DOOM_ASPECT_SAPLING = MSBlocks.REGISTER.register("potted_doom_aspect_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, DOOM_ASPECT_SAPLING, ofFullCopy(Blocks.POTTED_OAK_SAPLING)));
	
	public static final DeferredBlock<Block> DOOM_ASPECT_PLANKS = MSBlocks.REGISTER.register("doom_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_PLANKS);
	
	public static final DeferredBlock<Block> DOOM_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("doom_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_CARVED_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_CARVED_PLANKS);
	
	public static final DeferredBlock<StairBlock> DOOM_ASPECT_STAIRS = MSBlocks.REGISTER.register("doom_aspect_stairs",
			() -> new StairBlock(() -> DOOM_ASPECT_PLANKS.get().defaultBlockState(), ofFullCopy(DOOM_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_STAIRS);
	
	public static final DeferredBlock<SlabBlock> DOOM_ASPECT_SLAB = MSBlocks.REGISTER.register("doom_aspect_slab",
			() -> new SlabBlock(ofFullCopy(DOOM_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_SLAB);
	
	public static final DeferredBlock<FenceBlock> DOOM_ASPECT_FENCE = MSBlocks.REGISTER.register("doom_aspect_fence",
			() -> new FenceBlock(ofFullCopy(DOOM_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_FENCE);
	
	public static final DeferredBlock<FenceGateBlock> DOOM_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("doom_aspect_fence_gate",
			() -> new FenceGateBlock(ofFullCopy(DOOM_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_FENCE_GATE);
	
	public static final DeferredBlock<DoorBlock> DOOM_ASPECT_DOOR = MSBlocks.REGISTER.register("doom_aspect_door",
			() -> new DoorBlock(MSBlockSetType.DOOM, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_DOOR);
	
	public static final DeferredBlock<TrapDoorBlock> DOOM_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("doom_aspect_trapdoor",
			() -> new TrapDoorBlock(MSBlockSetType.DOOM, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_TRAPDOOR);
	
	public static final DeferredBlock<PressurePlateBlock> DOOM_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("doom_aspect_pressure_plate",
			() -> new PressurePlateBlock(MSBlockSetType.DOOM, ofFullCopy(DOOM_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_PRESSURE_PLATE);
	
	public static final DeferredBlock<ButtonBlock> DOOM_ASPECT_BUTTON = MSBlocks.REGISTER.register("doom_aspect_button",
			() -> new ButtonBlock(MSBlockSetType.DOOM, 10, ofFullCopy(DOOM_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_BUTTON);
	
	public static final DeferredBlock<Block> DOOM_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("doom_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_BOOKSHELF);
	
	public static final DeferredBlock<Block> DOOM_ASPECT_LADDER = MSBlocks.REGISTER.register("doom_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final DeferredItem<BlockItem> DOOM_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_LADDER);
	
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
			() -> new AspectSaplingBlock(new TreeGrower(Minestuck.id("heart_aspect").toString(), Optional.empty(), Optional.of(MSCFeatures.HEART_TREE), Optional.empty()),
					Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final DeferredItem<BlockItem> HEART_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final DeferredBlock<Block> POTTED_HEART_ASPECT_SAPLING = MSBlocks.REGISTER.register("potted_heart_aspect_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, HEART_ASPECT_SAPLING, ofFullCopy(Blocks.POTTED_OAK_SAPLING)));
	
	public static final DeferredBlock<Block> HEART_ASPECT_PLANKS = MSBlocks.REGISTER.register("heart_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> HEART_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_PLANKS);
	
	public static final DeferredBlock<Block> HEART_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("heart_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> HEART_ASPECT_CARVED_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_CARVED_PLANKS);
	
	public static final DeferredBlock<StairBlock> HEART_ASPECT_STAIRS = MSBlocks.REGISTER.register("heart_aspect_stairs",
			() -> new StairBlock(() -> HEART_ASPECT_PLANKS.get().defaultBlockState(), ofFullCopy(HEART_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> HEART_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_STAIRS);
	
	public static final DeferredBlock<SlabBlock> HEART_ASPECT_SLAB = MSBlocks.REGISTER.register("heart_aspect_slab",
			() -> new SlabBlock(ofFullCopy(HEART_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> HEART_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_SLAB);
	
	public static final DeferredBlock<FenceBlock> HEART_ASPECT_FENCE = MSBlocks.REGISTER.register("heart_aspect_fence",
			() -> new FenceBlock(ofFullCopy(HEART_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> HEART_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_FENCE);
	
	public static final DeferredBlock<FenceGateBlock> HEART_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("heart_aspect_fence_gate",
			() -> new FenceGateBlock(ofFullCopy(HEART_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredItem<BlockItem> HEART_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_FENCE_GATE);
	
	public static final DeferredBlock<DoorBlock> HEART_ASPECT_DOOR = MSBlocks.REGISTER.register("heart_aspect_door",
			() -> new DoorBlock(MSBlockSetType.HEART, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredItem<BlockItem> HEART_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_DOOR);
	
	public static final DeferredBlock<TrapDoorBlock> HEART_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("heart_aspect_trapdoor",
			() -> new TrapDoorBlock(MSBlockSetType.HEART, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredItem<BlockItem> HEART_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_TRAPDOOR);
	
	public static final DeferredBlock<PressurePlateBlock> HEART_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("heart_aspect_pressure_plate",
			() -> new PressurePlateBlock(MSBlockSetType.HEART, ofFullCopy(HEART_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> HEART_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_PRESSURE_PLATE);
	
	public static final DeferredBlock<ButtonBlock> HEART_ASPECT_BUTTON = MSBlocks.REGISTER.register("heart_aspect_button",
			() -> new ButtonBlock(MSBlockSetType.HEART, 10, ofFullCopy(HEART_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> HEART_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_BUTTON);
	
	public static final DeferredBlock<Block> HEART_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("heart_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> HEART_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_BOOKSHELF);
	
	public static final DeferredBlock<Block> HEART_ASPECT_LADDER = MSBlocks.REGISTER.register("heart_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final DeferredItem<BlockItem> HEART_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_LADDER);
	
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
			() -> new AspectSaplingBlock(new TreeGrower(Minestuck.id("hope_aspect").toString(), Optional.empty(), Optional.of(MSCFeatures.HOPE_TREE), Optional.empty()),
					Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final DeferredBlock<Block> POTTED_HOPE_ASPECT_SAPLING = MSBlocks.REGISTER.register("potted_hope_aspect_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, HOPE_ASPECT_SAPLING, ofFullCopy(Blocks.POTTED_OAK_SAPLING)));
	
	public static final DeferredBlock<Block> HOPE_ASPECT_PLANKS = MSBlocks.REGISTER.register("hope_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_PLANKS);
	
	public static final DeferredBlock<Block> HOPE_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("hope_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_CARVED_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_CARVED_PLANKS);
	
	public static final DeferredBlock<StairBlock> HOPE_ASPECT_STAIRS = MSBlocks.REGISTER.register("hope_aspect_stairs",
			() -> new StairBlock(() -> HOPE_ASPECT_PLANKS.get().defaultBlockState(), ofFullCopy(HOPE_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_STAIRS);
	
	public static final DeferredBlock<SlabBlock> HOPE_ASPECT_SLAB = MSBlocks.REGISTER.register("hope_aspect_slab",
			() -> new SlabBlock(ofFullCopy(HOPE_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_SLAB);
	
	public static final DeferredBlock<FenceBlock> HOPE_ASPECT_FENCE = MSBlocks.REGISTER.register("hope_aspect_fence",
			() -> new FenceBlock(ofFullCopy(HOPE_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_FENCE);
	
	public static final DeferredBlock<FenceGateBlock> HOPE_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("hope_aspect_fence_gate",
			() -> new FenceGateBlock(ofFullCopy(HOPE_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_FENCE_GATE);
	
	public static final DeferredBlock<DoorBlock> HOPE_ASPECT_DOOR = MSBlocks.REGISTER.register("hope_aspect_door",
			() -> new DoorBlock(MSBlockSetType.HOPE, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_DOOR);
	
	public static final DeferredBlock<TrapDoorBlock> HOPE_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("hope_aspect_trapdoor",
			() -> new TrapDoorBlock(MSBlockSetType.HOPE, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_TRAPDOOR);
	
	public static final DeferredBlock<PressurePlateBlock> HOPE_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("hope_aspect_pressure_plate",
			() -> new PressurePlateBlock(MSBlockSetType.HOPE, ofFullCopy(HOPE_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_PRESSURE_PLATE);
	
	public static final DeferredBlock<ButtonBlock> HOPE_ASPECT_BUTTON = MSBlocks.REGISTER.register("hope_aspect_button",
			() -> new ButtonBlock(MSBlockSetType.HOPE, 10, ofFullCopy(HOPE_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_BUTTON);
	
	public static final DeferredBlock<Block> HOPE_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("hope_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_BOOKSHELF);
	
	public static final DeferredBlock<Block> HOPE_ASPECT_LADDER = MSBlocks.REGISTER.register("hope_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final DeferredItem<BlockItem> HOPE_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_LADDER);
	
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
			() -> new AspectSaplingBlock(new TreeGrower(Minestuck.id("life_aspect").toString(), Optional.empty(), Optional.of(MSCFeatures.LIFE_TREE), Optional.empty()),
					Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final DeferredBlock<Block> POTTED_LIFE_ASPECT_SAPLING = MSBlocks.REGISTER.register("potted_life_aspect_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, LIFE_ASPECT_SAPLING, ofFullCopy(Blocks.POTTED_OAK_SAPLING)));
	
	public static final DeferredBlock<Block> LIFE_ASPECT_PLANKS = MSBlocks.REGISTER.register("life_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_PLANKS);
	
	public static final DeferredBlock<Block> LIFE_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("life_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_CARVED_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_CARVED_PLANKS);
	
	public static final DeferredBlock<StairBlock> LIFE_ASPECT_STAIRS = MSBlocks.REGISTER.register("life_aspect_stairs",
			() -> new StairBlock(() -> LIFE_ASPECT_PLANKS.get().defaultBlockState(), ofFullCopy(LIFE_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_STAIRS);
	
	public static final DeferredBlock<SlabBlock> LIFE_ASPECT_SLAB = MSBlocks.REGISTER.register("life_aspect_slab",
			() -> new SlabBlock(ofFullCopy(LIFE_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_SLAB);
	
	public static final DeferredBlock<FenceBlock> LIFE_ASPECT_FENCE = MSBlocks.REGISTER.register("life_aspect_fence",
			() -> new FenceBlock(ofFullCopy(LIFE_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_FENCE);
	
	public static final DeferredBlock<FenceGateBlock> LIFE_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("life_aspect_fence_gate",
			() -> new FenceGateBlock(ofFullCopy(LIFE_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_FENCE_GATE);
	
	public static final DeferredBlock<DoorBlock> LIFE_ASPECT_DOOR = MSBlocks.REGISTER.register("life_aspect_door",
			() -> new DoorBlock(MSBlockSetType.LIFE, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_DOOR);
	
	public static final DeferredBlock<TrapDoorBlock> LIFE_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("life_aspect_trapdoor",
			() -> new TrapDoorBlock(MSBlockSetType.LIFE, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_TRAPDOOR);
	
	public static final DeferredBlock<PressurePlateBlock> LIFE_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("life_aspect_pressure_plate",
			() -> new PressurePlateBlock(MSBlockSetType.LIFE, ofFullCopy(LIFE_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_PRESSURE_PLATE);
	
	public static final DeferredBlock<ButtonBlock> LIFE_ASPECT_BUTTON = MSBlocks.REGISTER.register("life_aspect_button",
			() -> new ButtonBlock(MSBlockSetType.LIFE, 10, ofFullCopy(LIFE_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_BUTTON);
	
	public static final DeferredBlock<Block> LIFE_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("life_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_BOOKSHELF);
	
	public static final DeferredBlock<Block> LIFE_ASPECT_LADDER = MSBlocks.REGISTER.register("life_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final DeferredItem<BlockItem> LIFE_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_LADDER);
	
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
			() -> new AspectSaplingBlock(new TreeGrower(Minestuck.id("light_aspect").toString(), Optional.empty(), Optional.of(MSCFeatures.LIGHT_TREE), Optional.empty()),
					Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final DeferredBlock<Block> POTTED_LIGHT_ASPECT_SAPLING = MSBlocks.REGISTER.register("potted_light_aspect_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, LIGHT_ASPECT_SAPLING, ofFullCopy(Blocks.POTTED_OAK_SAPLING)));
	
	public static final DeferredBlock<Block> LIGHT_ASPECT_PLANKS = MSBlocks.REGISTER.register("light_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_PLANKS);
	
	public static final DeferredBlock<Block> LIGHT_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("light_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_CARVED_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_CARVED_PLANKS);
	
	public static final DeferredBlock<StairBlock> LIGHT_ASPECT_STAIRS = MSBlocks.REGISTER.register("light_aspect_stairs",
			() -> new StairBlock(() -> LIGHT_ASPECT_PLANKS.get().defaultBlockState(), ofFullCopy(LIGHT_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_STAIRS);
	
	public static final DeferredBlock<SlabBlock> LIGHT_ASPECT_SLAB = MSBlocks.REGISTER.register("light_aspect_slab",
			() -> new SlabBlock(ofFullCopy(LIGHT_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_SLAB);
	
	public static final DeferredBlock<FenceBlock> LIGHT_ASPECT_FENCE = MSBlocks.REGISTER.register("light_aspect_fence",
			() -> new FenceBlock(ofFullCopy(LIGHT_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_FENCE);
	
	public static final DeferredBlock<FenceGateBlock> LIGHT_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("light_aspect_fence_gate",
			() -> new FenceGateBlock(ofFullCopy(LIGHT_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_FENCE_GATE);
	
	public static final DeferredBlock<DoorBlock> LIGHT_ASPECT_DOOR = MSBlocks.REGISTER.register("light_aspect_door",
			() -> new DoorBlock(MSBlockSetType.LIGHT, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_DOOR);
	
	public static final DeferredBlock<TrapDoorBlock> LIGHT_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("light_aspect_trapdoor",
			() -> new TrapDoorBlock(MSBlockSetType.LIGHT, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_TRAPDOOR);
	
	public static final DeferredBlock<PressurePlateBlock> LIGHT_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("light_aspect_pressure_plate",
			() -> new PressurePlateBlock(MSBlockSetType.LIGHT, ofFullCopy(LIGHT_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_PRESSURE_PLATE);
	
	public static final DeferredBlock<ButtonBlock> LIGHT_ASPECT_BUTTON = MSBlocks.REGISTER.register("light_aspect_button",
			() -> new ButtonBlock(MSBlockSetType.LIGHT, 10, ofFullCopy(LIGHT_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_BUTTON);
	
	public static final DeferredBlock<Block> LIGHT_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("light_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_BOOKSHELF);
	
	public static final DeferredBlock<Block> LIGHT_ASPECT_LADDER = MSBlocks.REGISTER.register("light_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final DeferredItem<BlockItem> LIGHT_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_LADDER);
	
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
			() -> new AspectSaplingBlock(new TreeGrower(Minestuck.id("mind_aspect").toString(), Optional.empty(), Optional.of(MSCFeatures.MIND_TREE), Optional.empty()),
					Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final DeferredItem<BlockItem> MIND_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final DeferredBlock<Block> POTTED_MIND_ASPECT_SAPLING = MSBlocks.REGISTER.register("potted_mind_aspect_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, MIND_ASPECT_SAPLING, ofFullCopy(Blocks.POTTED_OAK_SAPLING)));
	
	public static final DeferredBlock<Block> MIND_ASPECT_PLANKS = MSBlocks.REGISTER.register("mind_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> MIND_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_PLANKS);
	
		public static final DeferredBlock<Block> MIND_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("mind_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> MIND_ASPECT_PLANKS_CARVED_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_CARVED_PLANKS);
	
	public static final DeferredBlock<StairBlock> MIND_ASPECT_STAIRS = MSBlocks.REGISTER.register("mind_aspect_stairs",
			() -> new StairBlock(() -> MIND_ASPECT_PLANKS.get().defaultBlockState(), ofFullCopy(MIND_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> MIND_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_STAIRS);
	
	public static final DeferredBlock<SlabBlock> MIND_ASPECT_SLAB = MSBlocks.REGISTER.register("mind_aspect_slab",
			() -> new SlabBlock(ofFullCopy(MIND_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> MIND_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_SLAB);
	
	public static final DeferredBlock<FenceBlock> MIND_ASPECT_FENCE = MSBlocks.REGISTER.register("mind_aspect_fence",
			() -> new FenceBlock(ofFullCopy(MIND_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> MIND_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_FENCE);
	
	public static final DeferredBlock<FenceGateBlock> MIND_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("mind_aspect_fence_gate",
			() -> new FenceGateBlock(ofFullCopy(MIND_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredItem<BlockItem> MIND_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_FENCE_GATE);
	
	public static final DeferredBlock<DoorBlock> MIND_ASPECT_DOOR = MSBlocks.REGISTER.register("mind_aspect_door",
			() -> new DoorBlock(MSBlockSetType.MIND, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredItem<BlockItem> MIND_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_DOOR);
	
	public static final DeferredBlock<TrapDoorBlock> MIND_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("mind_aspect_trapdoor",
			() -> new TrapDoorBlock(MSBlockSetType.MIND, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredItem<BlockItem> MIND_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_TRAPDOOR);
	
	public static final DeferredBlock<PressurePlateBlock> MIND_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("mind_aspect_pressure_plate",
			() -> new PressurePlateBlock(MSBlockSetType.MIND, ofFullCopy(MIND_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> MIND_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_PRESSURE_PLATE);
	
	public static final DeferredBlock<ButtonBlock> MIND_ASPECT_BUTTON = MSBlocks.REGISTER.register("mind_aspect_button",
			() -> new ButtonBlock(MSBlockSetType.MIND, 10, ofFullCopy(MIND_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> MIND_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_BUTTON);
	
	public static final DeferredBlock<Block> MIND_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("mind_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> MIND_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_BOOKSHELF);
	
	public static final DeferredBlock<Block> MIND_ASPECT_LADDER = MSBlocks.REGISTER.register("mind_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final DeferredItem<BlockItem> MIND_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_LADDER);
	
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
			() -> new AspectSaplingBlock(new TreeGrower(Minestuck.id("rage_aspect").toString(), Optional.empty(), Optional.of(MSCFeatures.RAGE_TREE), Optional.empty()),
					Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final DeferredBlock<Block> POTTED_RAGE_ASPECT_SAPLING = MSBlocks.REGISTER.register("potted_rage_aspect_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, RAGE_ASPECT_SAPLING, ofFullCopy(Blocks.POTTED_OAK_SAPLING)));
	
	public static final DeferredBlock<Block> RAGE_ASPECT_PLANKS = MSBlocks.REGISTER.register("rage_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_PLANKS);
	
	public static final DeferredBlock<Block> RAGE_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("rage_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_CARVED_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_CARVED_PLANKS);
	
	public static final DeferredBlock<StairBlock> RAGE_ASPECT_STAIRS = MSBlocks.REGISTER.register("rage_aspect_stairs",
			() -> new StairBlock(() -> RAGE_ASPECT_PLANKS.get().defaultBlockState(), ofFullCopy(RAGE_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_STAIRS);
	
	public static final DeferredBlock<SlabBlock> RAGE_ASPECT_SLAB = MSBlocks.REGISTER.register("rage_aspect_slab",
			() -> new SlabBlock(ofFullCopy(RAGE_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_SLAB);
	
	public static final DeferredBlock<FenceBlock> RAGE_ASPECT_FENCE = MSBlocks.REGISTER.register("rage_aspect_fence",
			() -> new FenceBlock(ofFullCopy(RAGE_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_FENCE);
	
	public static final DeferredBlock<FenceGateBlock> RAGE_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("rage_aspect_fence_gate",
			() -> new FenceGateBlock(ofFullCopy(RAGE_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_FENCE_GATE);
	
	public static final DeferredBlock<DoorBlock> RAGE_ASPECT_DOOR = MSBlocks.REGISTER.register("rage_aspect_door",
			() -> new DoorBlock(MSBlockSetType.RAGE, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_DOOR);
	
	public static final DeferredBlock<TrapDoorBlock> RAGE_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("rage_aspect_trapdoor",
			() -> new TrapDoorBlock(MSBlockSetType.RAGE, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_TRAPDOOR);
	
	public static final DeferredBlock<PressurePlateBlock> RAGE_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("rage_aspect_pressure_plate",
			() -> new PressurePlateBlock(MSBlockSetType.RAGE, ofFullCopy(RAGE_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_PRESSURE_PLATE);
	
	public static final DeferredBlock<ButtonBlock> RAGE_ASPECT_BUTTON = MSBlocks.REGISTER.register("rage_aspect_button",
			() -> new ButtonBlock(MSBlockSetType.RAGE, 10, ofFullCopy(RAGE_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_BUTTON);
	
	public static final DeferredBlock<Block> RAGE_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("rage_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_BOOKSHELF);
	
	public static final DeferredBlock<Block> RAGE_ASPECT_LADDER = MSBlocks.REGISTER.register("rage_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final DeferredItem<BlockItem> RAGE_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_LADDER);
	
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
			() -> new AspectSaplingBlock(new TreeGrower(Minestuck.id("space_aspect").toString(), Optional.empty(), Optional.of(MSCFeatures.SPACE_TREE), Optional.empty()),
					Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final DeferredBlock<Block> POTTED_SPACE_ASPECT_SAPLING = MSBlocks.REGISTER.register("potted_space_aspect_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, SPACE_ASPECT_SAPLING, ofFullCopy(Blocks.POTTED_OAK_SAPLING)));
	
	public static final DeferredBlock<Block> SPACE_ASPECT_PLANKS = MSBlocks.REGISTER.register("space_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_PLANKS);
	
	public static final DeferredBlock<Block> SPACE_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("space_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_CARVED_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_CARVED_PLANKS);
	
	public static final DeferredBlock<StairBlock> SPACE_ASPECT_STAIRS = MSBlocks.REGISTER.register("space_aspect_stairs",
			() -> new StairBlock(() -> SPACE_ASPECT_PLANKS.get().defaultBlockState(), ofFullCopy(SPACE_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_STAIRS);
	
	public static final DeferredBlock<SlabBlock> SPACE_ASPECT_SLAB = MSBlocks.REGISTER.register("space_aspect_slab",
			() -> new SlabBlock(ofFullCopy(SPACE_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_SLAB);
	
	public static final DeferredBlock<FenceBlock> SPACE_ASPECT_FENCE = MSBlocks.REGISTER.register("space_aspect_fence",
			() -> new FenceBlock(ofFullCopy(SPACE_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_FENCE);
	
	public static final DeferredBlock<FenceGateBlock> SPACE_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("space_aspect_fence_gate",
			() -> new FenceGateBlock(ofFullCopy(SPACE_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_FENCE_GATE);
	
	public static final DeferredBlock<DoorBlock> SPACE_ASPECT_DOOR = MSBlocks.REGISTER.register("space_aspect_door",
			() -> new DoorBlock(MSBlockSetType.SPACE, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_DOOR);
	
	public static final DeferredBlock<TrapDoorBlock> SPACE_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("space_aspect_trapdoor",
			() -> new TrapDoorBlock(MSBlockSetType.SPACE, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_TRAPDOOR);
	
	public static final DeferredBlock<PressurePlateBlock> SPACE_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("space_aspect_pressure_plate",
			() -> new PressurePlateBlock(MSBlockSetType.SPACE, ofFullCopy(SPACE_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_PRESSURE_PLATE);
	
	public static final DeferredBlock<ButtonBlock> SPACE_ASPECT_BUTTON = MSBlocks.REGISTER.register("space_aspect_button",
			() -> new ButtonBlock(MSBlockSetType.SPACE, 10, ofFullCopy(SPACE_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_BUTTON);
	
	public static final DeferredBlock<Block> SPACE_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("space_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_BOOKSHELF);
	
	public static final DeferredBlock<Block> SPACE_ASPECT_LADDER = MSBlocks.REGISTER.register("space_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final DeferredItem<BlockItem> SPACE_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_LADDER);
	
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
			() -> new AspectSaplingBlock(new TreeGrower(Minestuck.id("time_aspect").toString(), Optional.empty(), Optional.of(MSCFeatures.TIME_TREE), Optional.empty()),
					Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final DeferredItem<BlockItem> TIME_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final DeferredBlock<Block> POTTED_TIME_ASPECT_SAPLING = MSBlocks.REGISTER.register("potted_time_aspect_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, TIME_ASPECT_SAPLING, ofFullCopy(Blocks.POTTED_OAK_SAPLING)));
	
	public static final DeferredBlock<Block> TIME_ASPECT_PLANKS = MSBlocks.REGISTER.register("time_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> TIME_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_PLANKS);
	
	public static final DeferredBlock<Block> TIME_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("time_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> TIME_ASPECT_CARVED_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_CARVED_PLANKS);
	
	public static final DeferredBlock<StairBlock> TIME_ASPECT_STAIRS = MSBlocks.REGISTER.register("time_aspect_stairs",
			() -> new StairBlock(() -> TIME_ASPECT_PLANKS.get().defaultBlockState(), ofFullCopy(TIME_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> TIME_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_STAIRS);
	
	public static final DeferredBlock<SlabBlock> TIME_ASPECT_SLAB = MSBlocks.REGISTER.register("time_aspect_slab",
			() -> new SlabBlock(ofFullCopy(TIME_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> TIME_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_SLAB);
	
	public static final DeferredBlock<FenceBlock> TIME_ASPECT_FENCE = MSBlocks.REGISTER.register("time_aspect_fence",
			() -> new FenceBlock(ofFullCopy(TIME_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> TIME_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_FENCE);
	
	public static final DeferredBlock<FenceGateBlock> TIME_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("time_aspect_fence_gate",
			() -> new FenceGateBlock(ofFullCopy(TIME_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredItem<BlockItem> TIME_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_FENCE_GATE);
	
	public static final DeferredBlock<DoorBlock> TIME_ASPECT_DOOR = MSBlocks.REGISTER.register("time_aspect_door",
			() -> new DoorBlock(MSBlockSetType.TIME, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredItem<BlockItem> TIME_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_DOOR);
	
	public static final DeferredBlock<TrapDoorBlock> TIME_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("time_aspect_trapdoor",
			() -> new TrapDoorBlock(MSBlockSetType.TIME, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredItem<BlockItem> TIME_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_TRAPDOOR);
	
	public static final DeferredBlock<PressurePlateBlock> TIME_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("time_aspect_pressure_plate",
			() -> new PressurePlateBlock(MSBlockSetType.TIME, ofFullCopy(TIME_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> TIME_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_PRESSURE_PLATE);
	
	public static final DeferredBlock<ButtonBlock> TIME_ASPECT_BUTTON = MSBlocks.REGISTER.register("time_aspect_button",
			() -> new ButtonBlock(MSBlockSetType.TIME, 10, ofFullCopy(TIME_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> TIME_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_BUTTON);
	
	public static final DeferredBlock<Block> TIME_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("time_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> TIME_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_BOOKSHELF);
	
	public static final DeferredBlock<Block> TIME_ASPECT_LADDER = MSBlocks.REGISTER.register("time_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final DeferredItem<BlockItem> TIME_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_LADDER);
	
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
			() -> new AspectSaplingBlock(new TreeGrower(Minestuck.id("void_aspect").toString(), Optional.empty(), Optional.of(MSCFeatures.VOID_TREE), Optional.empty()),
					Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final DeferredItem<BlockItem> VOID_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final DeferredBlock<Block> POTTED_VOID_ASPECT_SAPLING = MSBlocks.REGISTER.register("potted_void_aspect_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, VOID_ASPECT_SAPLING, ofFullCopy(Blocks.POTTED_OAK_SAPLING)));
	
	public static final DeferredBlock<Block> VOID_ASPECT_PLANKS = MSBlocks.REGISTER.register("void_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> VOID_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_PLANKS);
	
	public static final DeferredBlock<Block> VOID_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("void_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> VOID_ASPECT_CARVED_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_CARVED_PLANKS);
	
	public static final DeferredBlock<StairBlock> VOID_ASPECT_STAIRS = MSBlocks.REGISTER.register("void_aspect_stairs",
			() -> new StairBlock(() -> VOID_ASPECT_PLANKS.get().defaultBlockState(), ofFullCopy(VOID_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> VOID_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_STAIRS);
	
	public static final DeferredBlock<SlabBlock> VOID_ASPECT_SLAB = MSBlocks.REGISTER.register("void_aspect_slab",
			() -> new SlabBlock(ofFullCopy(VOID_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> VOID_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_SLAB);
	
	public static final DeferredBlock<FenceBlock> VOID_ASPECT_FENCE = MSBlocks.REGISTER.register("void_aspect_fence",
			() -> new FenceBlock(ofFullCopy(VOID_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> VOID_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_FENCE);
	
	public static final DeferredBlock<FenceGateBlock> VOID_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("void_aspect_fence_gate",
			() -> new FenceGateBlock(ofFullCopy(VOID_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredItem<BlockItem> VOID_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_FENCE_GATE);
	
	public static final DeferredBlock<DoorBlock> VOID_ASPECT_DOOR = MSBlocks.REGISTER.register("void_aspect_door",
			() -> new DoorBlock(MSBlockSetType.VOID, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredItem<BlockItem> VOID_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_DOOR);
	
	public static final DeferredBlock<TrapDoorBlock> VOID_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("void_aspect_trapdoor",
			() -> new TrapDoorBlock(MSBlockSetType.VOID, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredItem<BlockItem> VOID_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_TRAPDOOR);
	
	public static final DeferredBlock<PressurePlateBlock> VOID_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("void_aspect_pressure_plate",
			() -> new PressurePlateBlock(MSBlockSetType.VOID, ofFullCopy(VOID_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> VOID_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_PRESSURE_PLATE);
	
	public static final DeferredBlock<ButtonBlock> VOID_ASPECT_BUTTON = MSBlocks.REGISTER.register("void_aspect_button",
			() -> new ButtonBlock(MSBlockSetType.VOID, 10, ofFullCopy(VOID_ASPECT_PLANKS.get())));
	public static final DeferredItem<BlockItem> VOID_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_BUTTON);
	
	public static final DeferredBlock<Block> VOID_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("void_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredItem<BlockItem> VOID_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_BOOKSHELF);
	
	public static final DeferredBlock<Block> VOID_ASPECT_LADDER = MSBlocks.REGISTER.register("void_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final DeferredItem<BlockItem> VOID_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_LADDER);
	
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
