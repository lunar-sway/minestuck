package com.mraof.minestuck.block;

import com.mraof.minestuck.block.plant.AspectSaplingBlock;
import com.mraof.minestuck.block.plant.FlammableLeavesBlock;
import com.mraof.minestuck.block.plant.FlammableLogBlock;
import com.mraof.minestuck.block.plant.StrippableFlammableLogBlock;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.MSWoodTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.RegistryObject;

import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.copy;

public final class AspectTreeBlocks
{
	public static void init() {}
	
	//Blood
	public static final RegistryObject<Block> BLOOD_ASPECT_STRIPPED_LOG = MSBlocks.REGISTER.register("blood_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> BLOOD_ASPECT_STRIPPED_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_STRIPPED_LOG);
	
	public static final RegistryObject<Block> BLOOD_ASPECT_STRIPPED_WOOD = MSBlocks.REGISTER.register("blood_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> BLOOD_ASPECT_STRIPPED_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_STRIPPED_WOOD);
	
	public static final RegistryObject<Block> BLOOD_ASPECT_LOG = MSBlocks.REGISTER.register("blood_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> BLOOD_ASPECT_STRIPPED_LOG.get().defaultBlockState()));
	public static final RegistryObject<BlockItem> BLOOD_ASPECT_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_LOG);
	
	public static final RegistryObject<Block> BLOOD_ASPECT_WOOD = MSBlocks.REGISTER.register("blood_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> BLOOD_ASPECT_STRIPPED_WOOD.get().defaultBlockState()));
	public static final RegistryObject<BlockItem> BLOOD_ASPECT_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_WOOD);
	
	public static final RegistryObject<Block> BLOOD_ASPECT_LEAVES = MSBlocks.REGISTER.register("blood_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final RegistryObject<BlockItem> BLOOD_ASPECT_LEAVES_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_LEAVES);
	
	public static final RegistryObject<Block> BLOOD_ASPECT_SAPLING = MSBlocks.REGISTER.register("blood_aspect_sapling",
			() -> new AspectSaplingBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<BlockItem> BLOOD_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	
	public static final RegistryObject<Block> BLOOD_ASPECT_PLANKS = MSBlocks.REGISTER.register("blood_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> BLOOD_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_PLANKS);
	
	public static final RegistryObject<Block> BLOOD_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("blood_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> BLOOD_ASPECT_CARVED_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_CARVED_PLANKS);
	
	public static final RegistryObject<StairBlock> BLOOD_ASPECT_STAIRS = MSBlocks.REGISTER.register("blood_aspect_stairs",
			() -> new StairBlock(() -> BLOOD_ASPECT_PLANKS.get().defaultBlockState(), copy(BLOOD_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> BLOOD_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_STAIRS);
	
	public static final RegistryObject<SlabBlock> BLOOD_ASPECT_SLAB = MSBlocks.REGISTER.register("blood_aspect_slab",
			() -> new SlabBlock(copy(BLOOD_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> BLOOD_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_SLAB);
	
	public static final RegistryObject<FenceBlock> BLOOD_ASPECT_FENCE = MSBlocks.REGISTER.register("blood_aspect_fence",
			() -> new FenceBlock(copy(BLOOD_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> BLOOD_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_FENCE);
	
	public static final RegistryObject<FenceGateBlock> BLOOD_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("blood_aspect_fence_gate",
			() -> new FenceGateBlock(copy(BLOOD_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final RegistryObject<BlockItem> BLOOD_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_FENCE_GATE);
	
	public static final RegistryObject<DoorBlock> BLOOD_ASPECT_DOOR = MSBlocks.REGISTER.register("blood_aspect_door",
			() -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> BLOOD_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_DOOR);
	
	public static final RegistryObject<TrapDoorBlock> BLOOD_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("blood_aspect_trapdoor",
			() -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> BLOOD_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_TRAPDOOR);
	
	public static final RegistryObject<PressurePlateBlock> BLOOD_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("blood_aspect_pressure_plate",
			() -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(BLOOD_ASPECT_PLANKS.get()), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> BLOOD_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_PRESSURE_PLATE);
	
	public static final RegistryObject<ButtonBlock> BLOOD_ASPECT_BUTTON = MSBlocks.REGISTER.register("blood_aspect_button",
			() -> new ButtonBlock(copy(BLOOD_ASPECT_PLANKS.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<BlockItem> BLOOD_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_BUTTON);
	
	public static final RegistryObject<Block> BLOOD_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("blood_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> BLOOD_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_BOOKSHELF);
	
	public static final RegistryObject<Block> BLOOD_ASPECT_LADDER = MSBlocks.REGISTER.register("blood_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<BlockItem> BLOOD_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BLOOD_ASPECT_LADDER);
	
	public static final RegistryObject<Block> BLOOD_ASPECT_SIGN = MSBlocks.REGISTER.register("blood_aspect_sign",
			() -> new MSStandingSignBlock(Block.Properties.copy(Blocks.OAK_SIGN), MSWoodTypes.BLOOD));
	public static final RegistryObject<Block> BLOOD_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("blood_aspect_wall_sign",
			() -> new MSWallSignBlock(Block.Properties.copy(Blocks.OAK_WALL_SIGN), MSWoodTypes.BLOOD));
	public static final RegistryObject<Block> BLOOD_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("blood_aspect_hanging_sign",
			() -> new MSHangingSignBlock(Block.Properties.copy(Blocks.OAK_HANGING_SIGN), MSWoodTypes.BLOOD));
	public static final RegistryObject<Block> BLOOD_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("blood_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(Block.Properties.copy(Blocks.OAK_WALL_HANGING_SIGN), MSWoodTypes.BLOOD));
	
	public static final RegistryObject<Item> BLOOD_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("blood_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), BLOOD_ASPECT_SIGN.get(), BLOOD_ASPECT_WALL_SIGN.get()));
	public static final RegistryObject<Item> BLOOD_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("blood_aspect_hanging_sign",
			() -> new HangingSignItem(BLOOD_ASPECT_HANGING_SIGN.get(), BLOOD_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
	//Breath
	public static final RegistryObject<Block> BREATH_ASPECT_STRIPPED_LOG = MSBlocks.REGISTER.register("breath_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> BREATH_ASPECT_STRIPPED_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_STRIPPED_LOG);
	
	public static final RegistryObject<Block> BREATH_ASPECT_STRIPPED_WOOD = MSBlocks.REGISTER.register("breath_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> BREATH_ASPECT_STRIPPED_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_STRIPPED_WOOD);
	
	public static final RegistryObject<Block> BREATH_ASPECT_LOG = MSBlocks.REGISTER.register("breath_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> BREATH_ASPECT_STRIPPED_LOG.get().defaultBlockState()));
	public static final RegistryObject<BlockItem> BREATH_ASPECT_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_LOG);
	
	public static final RegistryObject<Block> BREATH_ASPECT_WOOD = MSBlocks.REGISTER.register("breath_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> BREATH_ASPECT_STRIPPED_WOOD.get().defaultBlockState()));
	public static final RegistryObject<BlockItem> BREATH_ASPECT_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_WOOD);
	
	public static final RegistryObject<Block> BREATH_ASPECT_LEAVES = MSBlocks.REGISTER.register("breath_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final RegistryObject<BlockItem> BREATH_ASPECT_LEAVES_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_LEAVES);
	
	public static final RegistryObject<Block> BREATH_ASPECT_SAPLING = MSBlocks.REGISTER.register("breath_aspect_sapling",
			() -> new AspectSaplingBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<BlockItem> BREATH_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	
	public static final RegistryObject<Block> BREATH_ASPECT_PLANKS = MSBlocks.REGISTER.register("breath_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> BREATH_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_PLANKS);
	
	public static final RegistryObject<Block> BREATH_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("breath_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> BREATH_ASPECT_CARVED_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_CARVED_PLANKS);
	
	public static final RegistryObject<StairBlock> BREATH_ASPECT_STAIRS = MSBlocks.REGISTER.register("breath_aspect_stairs",
			() -> new StairBlock(() -> BREATH_ASPECT_PLANKS.get().defaultBlockState(), copy(BREATH_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> BREATH_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_STAIRS);
	
	public static final RegistryObject<SlabBlock> BREATH_ASPECT_SLAB = MSBlocks.REGISTER.register("breath_aspect_slab",
			() -> new SlabBlock(copy(BREATH_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> BREATH_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_SLAB);
	
	public static final RegistryObject<FenceBlock> BREATH_ASPECT_FENCE = MSBlocks.REGISTER.register("breath_aspect_fence",
			() -> new FenceBlock(copy(BREATH_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> BREATH_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_FENCE);
	
	public static final RegistryObject<FenceGateBlock> BREATH_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("breath_aspect_fence_gate",
			() -> new FenceGateBlock(copy(BREATH_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final RegistryObject<BlockItem> BREATH_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_FENCE_GATE);
	
	public static final RegistryObject<DoorBlock> BREATH_ASPECT_DOOR = MSBlocks.REGISTER.register("breath_aspect_door",
			() -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> BREATH_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_DOOR);
	
	public static final RegistryObject<TrapDoorBlock> BREATH_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("breath_aspect_trapdoor",
			() -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> BREATH_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_TRAPDOOR);
	
	public static final RegistryObject<PressurePlateBlock> BREATH_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("breath_aspect_pressure_plate",
			() -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(BREATH_ASPECT_PLANKS.get()), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> BREATH_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_PRESSURE_PLATE);
	
	public static final RegistryObject<ButtonBlock> BREATH_ASPECT_BUTTON = MSBlocks.REGISTER.register("breath_aspect_button",
			() -> new ButtonBlock(copy(BREATH_ASPECT_PLANKS.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<BlockItem> BREATH_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_BUTTON);
	
	public static final RegistryObject<Block> BREATH_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("breath_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> BREATH_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_BOOKSHELF);
	
	public static final RegistryObject<Block> BREATH_ASPECT_LADDER = MSBlocks.REGISTER.register("breath_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<BlockItem> BREATH_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.BREATH_ASPECT_LADDER);
	
	public static final RegistryObject<Block> BREATH_ASPECT_SIGN = MSBlocks.REGISTER.register("breath_aspect_sign",
			() -> new MSStandingSignBlock(Block.Properties.copy(Blocks.OAK_SIGN), MSWoodTypes.BREATH));
	public static final RegistryObject<Block> BREATH_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("breath_aspect_wall_sign",
			() -> new MSWallSignBlock(Block.Properties.copy(Blocks.OAK_WALL_SIGN), MSWoodTypes.BREATH));
	public static final RegistryObject<Block> BREATH_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("breath_aspect_hanging_sign",
			() -> new MSHangingSignBlock(Block.Properties.copy(Blocks.OAK_HANGING_SIGN), MSWoodTypes.BREATH));
	public static final RegistryObject<Block> BREATH_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("breath_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(Block.Properties.copy(Blocks.OAK_WALL_HANGING_SIGN), MSWoodTypes.BREATH));
	
	public static final RegistryObject<Item> BREATH_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("breath_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), BREATH_ASPECT_SIGN.get(), BREATH_ASPECT_WALL_SIGN.get()));
	public static final RegistryObject<Item> BREATH_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("breath_aspect_hanging_sign",
			() -> new HangingSignItem(BREATH_ASPECT_HANGING_SIGN.get(), BREATH_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
	//Doom
	public static final RegistryObject<Block> DOOM_ASPECT_STRIPPED_LOG = MSBlocks.REGISTER.register("doom_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> DOOM_ASPECT_STRIPPED_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_STRIPPED_LOG);
	
	public static final RegistryObject<Block> DOOM_ASPECT_STRIPPED_WOOD = MSBlocks.REGISTER.register("doom_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> DOOM_ASPECT_STRIPPED_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_STRIPPED_WOOD);
	
	public static final RegistryObject<Block> DOOM_ASPECT_LOG = MSBlocks.REGISTER.register("doom_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> DOOM_ASPECT_STRIPPED_LOG.get().defaultBlockState()));
	public static final RegistryObject<BlockItem> DOOM_ASPECT_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_LOG);
	
	public static final RegistryObject<Block> DOOM_ASPECT_WOOD = MSBlocks.REGISTER.register("doom_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> DOOM_ASPECT_STRIPPED_WOOD.get().defaultBlockState()));
	public static final RegistryObject<BlockItem> DOOM_ASPECT_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_WOOD);
	
	public static final RegistryObject<Block> DOOM_ASPECT_LEAVES = MSBlocks.REGISTER.register("doom_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final RegistryObject<BlockItem> DOOM_ASPECT_LEAVES_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_LEAVES);
	
	public static final RegistryObject<Block> DOOM_ASPECT_SAPLING = MSBlocks.REGISTER.register("doom_aspect_sapling",
			() -> new AspectSaplingBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<BlockItem> DOOM_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	
	public static final RegistryObject<Block> DOOM_ASPECT_PLANKS = MSBlocks.REGISTER.register("doom_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> DOOM_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_PLANKS);
	
	public static final RegistryObject<Block> DOOM_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("doom_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> DOOM_ASPECT_CARVED_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_CARVED_PLANKS);
	
	public static final RegistryObject<StairBlock> DOOM_ASPECT_STAIRS = MSBlocks.REGISTER.register("doom_aspect_stairs",
			() -> new StairBlock(() -> DOOM_ASPECT_PLANKS.get().defaultBlockState(), copy(DOOM_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> DOOM_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_STAIRS);
	
	public static final RegistryObject<SlabBlock> DOOM_ASPECT_SLAB = MSBlocks.REGISTER.register("doom_aspect_slab",
			() -> new SlabBlock(copy(DOOM_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> DOOM_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_SLAB);
	
	public static final RegistryObject<FenceBlock> DOOM_ASPECT_FENCE = MSBlocks.REGISTER.register("doom_aspect_fence",
			() -> new FenceBlock(copy(DOOM_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> DOOM_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_FENCE);
	
	public static final RegistryObject<FenceGateBlock> DOOM_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("doom_aspect_fence_gate",
			() -> new FenceGateBlock(copy(DOOM_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final RegistryObject<BlockItem> DOOM_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_FENCE_GATE);
	
	public static final RegistryObject<DoorBlock> DOOM_ASPECT_DOOR = MSBlocks.REGISTER.register("doom_aspect_door",
			() -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> DOOM_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_DOOR);
	
	public static final RegistryObject<TrapDoorBlock> DOOM_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("doom_aspect_trapdoor",
			() -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> DOOM_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_TRAPDOOR);
	
	public static final RegistryObject<PressurePlateBlock> DOOM_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("doom_aspect_pressure_plate",
			() -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(DOOM_ASPECT_PLANKS.get()), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> DOOM_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_PRESSURE_PLATE);
	
	public static final RegistryObject<ButtonBlock> DOOM_ASPECT_BUTTON = MSBlocks.REGISTER.register("doom_aspect_button",
			() -> new ButtonBlock(copy(DOOM_ASPECT_PLANKS.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<BlockItem> DOOM_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_BUTTON);
	
	public static final RegistryObject<Block> DOOM_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("doom_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> DOOM_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_BOOKSHELF);
	
	public static final RegistryObject<Block> DOOM_ASPECT_LADDER = MSBlocks.REGISTER.register("doom_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<BlockItem> DOOM_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.DOOM_ASPECT_LADDER);
	
	public static final RegistryObject<Block> DOOM_ASPECT_SIGN = MSBlocks.REGISTER.register("doom_aspect_sign",
			() -> new MSStandingSignBlock(Block.Properties.copy(Blocks.OAK_SIGN), MSWoodTypes.DOOM));
	public static final RegistryObject<Block> DOOM_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("doom_aspect_wall_sign",
			() -> new MSWallSignBlock(Block.Properties.copy(Blocks.OAK_WALL_SIGN), MSWoodTypes.DOOM));
	public static final RegistryObject<Block> DOOM_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("doom_aspect_hanging_sign",
			() -> new MSHangingSignBlock(Block.Properties.copy(Blocks.OAK_HANGING_SIGN), MSWoodTypes.DOOM));
	public static final RegistryObject<Block> DOOM_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("doom_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(Block.Properties.copy(Blocks.OAK_WALL_HANGING_SIGN), MSWoodTypes.DOOM));
	
	public static final RegistryObject<Item> DOOM_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("doom_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), DOOM_ASPECT_SIGN.get(), DOOM_ASPECT_WALL_SIGN.get()));
	public static final RegistryObject<Item> DOOM_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("doom_aspect_hanging_sign",
			() -> new HangingSignItem(DOOM_ASPECT_HANGING_SIGN.get(), DOOM_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
	
	//Heart
	public static final RegistryObject<Block> HEART_ASPECT_STRIPPED_LOG = MSBlocks.REGISTER.register("heart_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> HEART_ASPECT_STRIPPED_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_STRIPPED_LOG);
	
	public static final RegistryObject<Block> HEART_ASPECT_STRIPPED_WOOD = MSBlocks.REGISTER.register("heart_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> HEART_ASPECT_STRIPPED_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_STRIPPED_WOOD);
	
	public static final RegistryObject<Block> HEART_ASPECT_LOG = MSBlocks.REGISTER.register("heart_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> HEART_ASPECT_STRIPPED_LOG.get().defaultBlockState()));
	public static final RegistryObject<BlockItem> HEART_ASPECT_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_LOG);
	
	public static final RegistryObject<Block> HEART_ASPECT_WOOD = MSBlocks.REGISTER.register("heart_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> HEART_ASPECT_STRIPPED_WOOD.get().defaultBlockState()));
	public static final RegistryObject<BlockItem> HEART_ASPECT_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_WOOD);
	
	public static final RegistryObject<Block> HEART_ASPECT_LEAVES = MSBlocks.REGISTER.register("heart_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final RegistryObject<BlockItem> HEART_ASPECT_LEAVES_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_LEAVES);
	
	public static final RegistryObject<Block> HEART_ASPECT_SAPLING = MSBlocks.REGISTER.register("heart_aspect_sapling",
			() -> new AspectSaplingBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<BlockItem> HEART_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	
	public static final RegistryObject<Block> HEART_ASPECT_PLANKS = MSBlocks.REGISTER.register("heart_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> HEART_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_PLANKS);
	
	public static final RegistryObject<Block> HEART_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("heart_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> HEART_ASPECT_CARVED_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_CARVED_PLANKS);
	
	public static final RegistryObject<StairBlock> HEART_ASPECT_STAIRS = MSBlocks.REGISTER.register("heart_aspect_stairs",
			() -> new StairBlock(() -> HEART_ASPECT_PLANKS.get().defaultBlockState(), copy(HEART_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> HEART_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_STAIRS);
	
	public static final RegistryObject<SlabBlock> HEART_ASPECT_SLAB = MSBlocks.REGISTER.register("heart_aspect_slab",
			() -> new SlabBlock(copy(HEART_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> HEART_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_SLAB);
	
	public static final RegistryObject<FenceBlock> HEART_ASPECT_FENCE = MSBlocks.REGISTER.register("heart_aspect_fence",
			() -> new FenceBlock(copy(HEART_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> HEART_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_FENCE);
	
	public static final RegistryObject<FenceGateBlock> HEART_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("heart_aspect_fence_gate",
			() -> new FenceGateBlock(copy(HEART_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final RegistryObject<BlockItem> HEART_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_FENCE_GATE);
	
	public static final RegistryObject<DoorBlock> HEART_ASPECT_DOOR = MSBlocks.REGISTER.register("heart_aspect_door",
			() -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> HEART_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_DOOR);
	
	public static final RegistryObject<TrapDoorBlock> HEART_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("heart_aspect_trapdoor",
			() -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> HEART_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_TRAPDOOR);
	
	public static final RegistryObject<PressurePlateBlock> HEART_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("heart_aspect_pressure_plate",
			() -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(HEART_ASPECT_PLANKS.get()), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> HEART_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_PRESSURE_PLATE);
	
	public static final RegistryObject<ButtonBlock> HEART_ASPECT_BUTTON = MSBlocks.REGISTER.register("heart_aspect_button",
			() -> new ButtonBlock(copy(HEART_ASPECT_PLANKS.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<BlockItem> HEART_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_BUTTON);
	
	public static final RegistryObject<Block> HEART_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("heart_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> HEART_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_BOOKSHELF);
	
	public static final RegistryObject<Block> HEART_ASPECT_LADDER = MSBlocks.REGISTER.register("heart_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<BlockItem> HEART_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HEART_ASPECT_LADDER);
	
	public static final RegistryObject<Block> HEART_ASPECT_SIGN = MSBlocks.REGISTER.register("heart_aspect_sign",
			() -> new MSStandingSignBlock(Block.Properties.copy(Blocks.OAK_SIGN), MSWoodTypes.HEART));
	public static final RegistryObject<Block> HEART_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("heart_aspect_wall_sign",
			() -> new MSWallSignBlock(Block.Properties.copy(Blocks.OAK_WALL_SIGN), MSWoodTypes.HEART));
	public static final RegistryObject<Block> HEART_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("heart_aspect_hanging_sign",
			() -> new MSHangingSignBlock(Block.Properties.copy(Blocks.OAK_HANGING_SIGN), MSWoodTypes.HEART));
	public static final RegistryObject<Block> HEART_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("heart_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(Block.Properties.copy(Blocks.OAK_WALL_HANGING_SIGN), MSWoodTypes.HEART));
	
	public static final RegistryObject<Item> HEART_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("heart_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), HEART_ASPECT_SIGN.get(), HEART_ASPECT_WALL_SIGN.get()));
	public static final RegistryObject<Item> HEART_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("heart_aspect_hanging_sign",
			() -> new HangingSignItem(HEART_ASPECT_HANGING_SIGN.get(), HEART_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
	//Hope
	public static final RegistryObject<Block> HOPE_ASPECT_STRIPPED_LOG = MSBlocks.REGISTER.register("hope_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> HOPE_ASPECT_STRIPPED_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_STRIPPED_LOG);
	
	public static final RegistryObject<Block> HOPE_ASPECT_STRIPPED_WOOD = MSBlocks.REGISTER.register("hope_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> HOPE_ASPECT_STRIPPED_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_STRIPPED_WOOD);
	
	public static final RegistryObject<Block> HOPE_ASPECT_LOG = MSBlocks.REGISTER.register("hope_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> HOPE_ASPECT_STRIPPED_LOG.get().defaultBlockState()));
	public static final RegistryObject<BlockItem> HOPE_ASPECT_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_LOG);
	
	public static final RegistryObject<Block> HOPE_ASPECT_WOOD = MSBlocks.REGISTER.register("hope_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> HOPE_ASPECT_STRIPPED_WOOD.get().defaultBlockState()));
	public static final RegistryObject<BlockItem> HOPE_ASPECT_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_WOOD);
	
	public static final RegistryObject<Block> HOPE_ASPECT_LEAVES = MSBlocks.REGISTER.register("hope_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final RegistryObject<BlockItem> HOPE_ASPECT_LEAVES_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_LEAVES);
	
	public static final RegistryObject<Block> HOPE_ASPECT_SAPLING = MSBlocks.REGISTER.register("hope_aspect_sapling",
			() -> new AspectSaplingBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<BlockItem> HOPE_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	
	public static final RegistryObject<Block> HOPE_ASPECT_PLANKS = MSBlocks.REGISTER.register("hope_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> HOPE_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_PLANKS);
	
	public static final RegistryObject<Block> HOPE_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("hope_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> HOPE_ASPECT_CARVED_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_CARVED_PLANKS);
	
	public static final RegistryObject<StairBlock> HOPE_ASPECT_STAIRS = MSBlocks.REGISTER.register("hope_aspect_stairs",
			() -> new StairBlock(() -> HOPE_ASPECT_PLANKS.get().defaultBlockState(), copy(HOPE_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> HOPE_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_STAIRS);
	
	public static final RegistryObject<SlabBlock> HOPE_ASPECT_SLAB = MSBlocks.REGISTER.register("hope_aspect_slab",
			() -> new SlabBlock(copy(HOPE_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> HOPE_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_SLAB);
	
	public static final RegistryObject<FenceBlock> HOPE_ASPECT_FENCE = MSBlocks.REGISTER.register("hope_aspect_fence",
			() -> new FenceBlock(copy(HOPE_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> HOPE_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_FENCE);
	
	public static final RegistryObject<FenceGateBlock> HOPE_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("hope_aspect_fence_gate",
			() -> new FenceGateBlock(copy(HOPE_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final RegistryObject<BlockItem> HOPE_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_FENCE_GATE);
	
	public static final RegistryObject<DoorBlock> HOPE_ASPECT_DOOR = MSBlocks.REGISTER.register("hope_aspect_door",
			() -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> HOPE_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_DOOR);
	
	public static final RegistryObject<TrapDoorBlock> HOPE_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("hope_aspect_trapdoor",
			() -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> HOPE_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_TRAPDOOR);
	
	public static final RegistryObject<PressurePlateBlock> HOPE_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("hope_aspect_pressure_plate",
			() -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(HOPE_ASPECT_PLANKS.get()), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> HOPE_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_PRESSURE_PLATE);
	
	public static final RegistryObject<ButtonBlock> HOPE_ASPECT_BUTTON = MSBlocks.REGISTER.register("hope_aspect_button",
			() -> new ButtonBlock(copy(HOPE_ASPECT_PLANKS.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<BlockItem> HOPE_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_BUTTON);
	
	public static final RegistryObject<Block> HOPE_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("hope_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> HOPE_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_BOOKSHELF);
	
	public static final RegistryObject<Block> HOPE_ASPECT_LADDER = MSBlocks.REGISTER.register("hope_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<BlockItem> HOPE_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.HOPE_ASPECT_LADDER);
	
	public static final RegistryObject<Block> HOPE_ASPECT_SIGN = MSBlocks.REGISTER.register("hope_aspect_sign",
			() -> new MSStandingSignBlock(Block.Properties.copy(Blocks.OAK_SIGN), MSWoodTypes.HOPE));
	public static final RegistryObject<Block> HOPE_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("hope_aspect_wall_sign",
			() -> new MSWallSignBlock(Block.Properties.copy(Blocks.OAK_WALL_SIGN), MSWoodTypes.HOPE));
	public static final RegistryObject<Block> HOPE_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("hope_aspect_hanging_sign",
			() -> new MSHangingSignBlock(Block.Properties.copy(Blocks.OAK_HANGING_SIGN), MSWoodTypes.HOPE));
	public static final RegistryObject<Block> HOPE_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("hope_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(Block.Properties.copy(Blocks.OAK_WALL_HANGING_SIGN), MSWoodTypes.HOPE));
	
	public static final RegistryObject<Item> HOPE_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("hope_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), HOPE_ASPECT_SIGN.get(), HOPE_ASPECT_WALL_SIGN.get()));
	public static final RegistryObject<Item> HOPE_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("hope_aspect_hanging_sign",
			() -> new HangingSignItem(HOPE_ASPECT_HANGING_SIGN.get(), HOPE_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
	
	//Life
	public static final RegistryObject<Block> LIFE_ASPECT_STRIPPED_LOG = MSBlocks.REGISTER.register("life_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> LIFE_ASPECT_STRIPPED_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_STRIPPED_LOG);
	
	public static final RegistryObject<Block> LIFE_ASPECT_STRIPPED_WOOD = MSBlocks.REGISTER.register("life_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> LIFE_ASPECT_STRIPPED_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_STRIPPED_WOOD);
	
	public static final RegistryObject<Block> LIFE_ASPECT_LOG = MSBlocks.REGISTER.register("life_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> LIFE_ASPECT_STRIPPED_LOG.get().defaultBlockState()));
	public static final RegistryObject<BlockItem> LIFE_ASPECT_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_LOG);
	
	public static final RegistryObject<Block> LIFE_ASPECT_WOOD = MSBlocks.REGISTER.register("life_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> LIFE_ASPECT_STRIPPED_WOOD.get().defaultBlockState()));
	public static final RegistryObject<BlockItem> LIFE_ASPECT_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_WOOD);
	
	public static final RegistryObject<Block> LIFE_ASPECT_LEAVES = MSBlocks.REGISTER.register("life_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final RegistryObject<BlockItem> LIFE_ASPECT_LEAVES_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_LEAVES);
	
	public static final RegistryObject<Block> LIFE_ASPECT_SAPLING = MSBlocks.REGISTER.register("life_aspect_sapling",
			() -> new AspectSaplingBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<BlockItem> LIFE_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	
	public static final RegistryObject<Block> LIFE_ASPECT_PLANKS = MSBlocks.REGISTER.register("life_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> LIFE_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_PLANKS);
	
	public static final RegistryObject<Block> LIFE_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("life_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> LIFE_ASPECT_CARVED_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_CARVED_PLANKS);
	
	public static final RegistryObject<StairBlock> LIFE_ASPECT_STAIRS = MSBlocks.REGISTER.register("life_aspect_stairs",
			() -> new StairBlock(() -> LIFE_ASPECT_PLANKS.get().defaultBlockState(), copy(LIFE_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> LIFE_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_STAIRS);
	
	public static final RegistryObject<SlabBlock> LIFE_ASPECT_SLAB = MSBlocks.REGISTER.register("life_aspect_slab",
			() -> new SlabBlock(copy(LIFE_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> LIFE_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_SLAB);
	
	public static final RegistryObject<FenceBlock> LIFE_ASPECT_FENCE = MSBlocks.REGISTER.register("life_aspect_fence",
			() -> new FenceBlock(copy(LIFE_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> LIFE_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_FENCE);
	
	public static final RegistryObject<FenceGateBlock> LIFE_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("life_aspect_fence_gate",
			() -> new FenceGateBlock(copy(LIFE_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final RegistryObject<BlockItem> LIFE_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_FENCE_GATE);
	
	public static final RegistryObject<DoorBlock> LIFE_ASPECT_DOOR = MSBlocks.REGISTER.register("life_aspect_door",
			() -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> LIFE_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_DOOR);
	
	public static final RegistryObject<TrapDoorBlock> LIFE_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("life_aspect_trapdoor",
			() -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> LIFE_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_TRAPDOOR);
	
	public static final RegistryObject<PressurePlateBlock> LIFE_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("life_aspect_pressure_plate",
			() -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(LIFE_ASPECT_PLANKS.get()), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> LIFE_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_PRESSURE_PLATE);
	
	public static final RegistryObject<ButtonBlock> LIFE_ASPECT_BUTTON = MSBlocks.REGISTER.register("life_aspect_button",
			() -> new ButtonBlock(copy(LIFE_ASPECT_PLANKS.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<BlockItem> LIFE_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_BUTTON);
	
	public static final RegistryObject<Block> LIFE_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("life_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> LIFE_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_BOOKSHELF);
	
	public static final RegistryObject<Block> LIFE_ASPECT_LADDER = MSBlocks.REGISTER.register("life_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<BlockItem> LIFE_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIFE_ASPECT_LADDER);
	
	public static final RegistryObject<Block> LIFE_ASPECT_SIGN = MSBlocks.REGISTER.register("life_aspect_sign",
			() -> new MSStandingSignBlock(Block.Properties.copy(Blocks.OAK_SIGN), MSWoodTypes.LIFE));
	public static final RegistryObject<Block> LIFE_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("life_aspect_wall_sign",
			() -> new MSWallSignBlock(Block.Properties.copy(Blocks.OAK_WALL_SIGN), MSWoodTypes.LIFE));
	public static final RegistryObject<Block> LIFE_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("life_aspect_hanging_sign",
			() -> new MSHangingSignBlock(Block.Properties.copy(Blocks.OAK_HANGING_SIGN), MSWoodTypes.LIFE));
	public static final RegistryObject<Block> LIFE_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("life_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(Block.Properties.copy(Blocks.OAK_WALL_HANGING_SIGN), MSWoodTypes.LIFE));
	
	public static final RegistryObject<Item> LIFE_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("life_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), LIFE_ASPECT_SIGN.get(), LIFE_ASPECT_WALL_SIGN.get()));
	public static final RegistryObject<Item> LIFE_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("life_aspect_hanging_sign",
			() -> new HangingSignItem(LIFE_ASPECT_HANGING_SIGN.get(), LIFE_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
	//Light
	public static final RegistryObject<Block> LIGHT_ASPECT_STRIPPED_LOG = MSBlocks.REGISTER.register("light_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> LIGHT_ASPECT_STRIPPED_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_STRIPPED_LOG);
	
	public static final RegistryObject<Block> LIGHT_ASPECT_STRIPPED_WOOD = MSBlocks.REGISTER.register("light_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> LIGHT_ASPECT_STRIPPED_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_STRIPPED_WOOD);
	
	public static final RegistryObject<Block> LIGHT_ASPECT_LOG = MSBlocks.REGISTER.register("light_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> LIGHT_ASPECT_STRIPPED_LOG.get().defaultBlockState()));
	public static final RegistryObject<BlockItem> LIGHT_ASPECT_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_LOG);
	
	public static final RegistryObject<Block> LIGHT_ASPECT_WOOD = MSBlocks.REGISTER.register("light_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> LIGHT_ASPECT_STRIPPED_WOOD.get().defaultBlockState()));
	public static final RegistryObject<BlockItem> LIGHT_ASPECT_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_WOOD);
	
	public static final RegistryObject<Block> LIGHT_ASPECT_LEAVES = MSBlocks.REGISTER.register("light_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final RegistryObject<BlockItem> LIGHT_ASPECT_LEAVES_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_LEAVES);
	
	public static final RegistryObject<Block> LIGHT_ASPECT_SAPLING = MSBlocks.REGISTER.register("light_aspect_sapling",
			() -> new AspectSaplingBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<BlockItem> LIGHT_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	
	public static final RegistryObject<Block> LIGHT_ASPECT_PLANKS = MSBlocks.REGISTER.register("light_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> LIGHT_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_PLANKS);
	
	public static final RegistryObject<Block> LIGHT_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("light_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> LIGHT_ASPECT_CARVED_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_CARVED_PLANKS);
	
	public static final RegistryObject<StairBlock> LIGHT_ASPECT_STAIRS = MSBlocks.REGISTER.register("light_aspect_stairs",
			() -> new StairBlock(() -> LIGHT_ASPECT_PLANKS.get().defaultBlockState(), copy(LIGHT_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> LIGHT_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_STAIRS);
	
	public static final RegistryObject<SlabBlock> LIGHT_ASPECT_SLAB = MSBlocks.REGISTER.register("light_aspect_slab",
			() -> new SlabBlock(copy(LIGHT_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> LIGHT_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_SLAB);
	
	public static final RegistryObject<FenceBlock> LIGHT_ASPECT_FENCE = MSBlocks.REGISTER.register("light_aspect_fence",
			() -> new FenceBlock(copy(LIGHT_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> LIGHT_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_FENCE);
	
	public static final RegistryObject<FenceGateBlock> LIGHT_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("light_aspect_fence_gate",
			() -> new FenceGateBlock(copy(LIGHT_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final RegistryObject<BlockItem> LIGHT_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_FENCE_GATE);
	
	public static final RegistryObject<DoorBlock> LIGHT_ASPECT_DOOR = MSBlocks.REGISTER.register("light_aspect_door",
			() -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> LIGHT_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_DOOR);
	
	public static final RegistryObject<TrapDoorBlock> LIGHT_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("light_aspect_trapdoor",
			() -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> LIGHT_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_TRAPDOOR);
	
	public static final RegistryObject<PressurePlateBlock> LIGHT_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("light_aspect_pressure_plate",
			() -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(LIGHT_ASPECT_PLANKS.get()), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> LIGHT_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_PRESSURE_PLATE);
	
	public static final RegistryObject<ButtonBlock> LIGHT_ASPECT_BUTTON = MSBlocks.REGISTER.register("light_aspect_button",
			() -> new ButtonBlock(copy(LIGHT_ASPECT_PLANKS.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<BlockItem> LIGHT_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_BUTTON);
	
	public static final RegistryObject<Block> LIGHT_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("light_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> LIGHT_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_BOOKSHELF);
	
	public static final RegistryObject<Block> LIGHT_ASPECT_LADDER = MSBlocks.REGISTER.register("light_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<BlockItem> LIGHT_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.LIGHT_ASPECT_LADDER);
	
	public static final RegistryObject<Block> LIGHT_ASPECT_SIGN = MSBlocks.REGISTER.register("light_aspect_sign",
			() -> new MSStandingSignBlock(Block.Properties.copy(Blocks.OAK_SIGN), MSWoodTypes.LIGHT));
	public static final RegistryObject<Block> LIGHT_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("light_aspect_wall_sign",
			() -> new MSWallSignBlock(Block.Properties.copy(Blocks.OAK_WALL_SIGN), MSWoodTypes.LIGHT));
	public static final RegistryObject<Block> LIGHT_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("light_aspect_hanging_sign",
			() -> new MSHangingSignBlock(Block.Properties.copy(Blocks.OAK_HANGING_SIGN), MSWoodTypes.LIGHT));
	public static final RegistryObject<Block> LIGHT_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("light_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(Block.Properties.copy(Blocks.OAK_WALL_HANGING_SIGN), MSWoodTypes.LIGHT));
	
	public static final RegistryObject<Item> LIGHT_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("light_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), LIGHT_ASPECT_SIGN.get(), LIGHT_ASPECT_WALL_SIGN.get()));
	public static final RegistryObject<Item> LIGHT_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("light_aspect_hanging_sign",
			() -> new HangingSignItem(LIGHT_ASPECT_HANGING_SIGN.get(), LIGHT_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
	
	//Mind
	public static final RegistryObject<Block> MIND_ASPECT_STRIPPED_LOG = MSBlocks.REGISTER.register("mind_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> MIND_ASPECT_STRIPPED_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_STRIPPED_LOG);
	
	public static final RegistryObject<Block> MIND_ASPECT_STRIPPED_WOOD = MSBlocks.REGISTER.register("mind_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> MIND_ASPECT_STRIPPED_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_STRIPPED_WOOD);
	
	public static final RegistryObject<Block> MIND_ASPECT_LOG = MSBlocks.REGISTER.register("mind_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> MIND_ASPECT_STRIPPED_LOG.get().defaultBlockState()));
	public static final RegistryObject<BlockItem> MIND_ASPECT_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_LOG);
	
	public static final RegistryObject<Block> MIND_ASPECT_WOOD = MSBlocks.REGISTER.register("mind_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> MIND_ASPECT_STRIPPED_WOOD.get().defaultBlockState()));
	public static final RegistryObject<BlockItem> MIND_ASPECT_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_WOOD);
	
	public static final RegistryObject<Block> MIND_ASPECT_LEAVES = MSBlocks.REGISTER.register("mind_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final RegistryObject<BlockItem> MIND_ASPECT_LEAVES_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_LEAVES);
	
	public static final RegistryObject<Block> MIND_ASPECT_SAPLING = MSBlocks.REGISTER.register("mind_aspect_sapling",
			() -> new AspectSaplingBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<BlockItem> MIND_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	
	public static final RegistryObject<Block> MIND_ASPECT_PLANKS = MSBlocks.REGISTER.register("mind_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> MIND_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_PLANKS);
	
		public static final RegistryObject<Block> MIND_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("mind_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> MIND_ASPECT_PLANKS_CARVED_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_CARVED_PLANKS);
	
	public static final RegistryObject<StairBlock> MIND_ASPECT_STAIRS = MSBlocks.REGISTER.register("mind_aspect_stairs",
			() -> new StairBlock(() -> MIND_ASPECT_PLANKS.get().defaultBlockState(), copy(MIND_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> MIND_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_STAIRS);
	
	public static final RegistryObject<SlabBlock> MIND_ASPECT_SLAB = MSBlocks.REGISTER.register("mind_aspect_slab",
			() -> new SlabBlock(copy(MIND_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> MIND_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_SLAB);
	
	public static final RegistryObject<FenceBlock> MIND_ASPECT_FENCE = MSBlocks.REGISTER.register("mind_aspect_fence",
			() -> new FenceBlock(copy(MIND_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> MIND_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_FENCE);
	
	public static final RegistryObject<FenceGateBlock> MIND_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("mind_aspect_fence_gate",
			() -> new FenceGateBlock(copy(MIND_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final RegistryObject<BlockItem> MIND_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_FENCE_GATE);
	
	public static final RegistryObject<DoorBlock> MIND_ASPECT_DOOR = MSBlocks.REGISTER.register("mind_aspect_door",
			() -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> MIND_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_DOOR);
	
	public static final RegistryObject<TrapDoorBlock> MIND_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("mind_aspect_trapdoor",
			() -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> MIND_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_TRAPDOOR);
	
	public static final RegistryObject<PressurePlateBlock> MIND_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("mind_aspect_pressure_plate",
			() -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(MIND_ASPECT_PLANKS.get()), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> MIND_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_PRESSURE_PLATE);
	
	public static final RegistryObject<ButtonBlock> MIND_ASPECT_BUTTON = MSBlocks.REGISTER.register("mind_aspect_button",
			() -> new ButtonBlock(copy(MIND_ASPECT_PLANKS.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<BlockItem> MIND_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_BUTTON);
	
	public static final RegistryObject<Block> MIND_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("mind_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> MIND_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_BOOKSHELF);
	
	public static final RegistryObject<Block> MIND_ASPECT_LADDER = MSBlocks.REGISTER.register("mind_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<BlockItem> MIND_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.MIND_ASPECT_LADDER);
	
	public static final RegistryObject<Block> MIND_ASPECT_SIGN = MSBlocks.REGISTER.register("mind_aspect_sign",
			() -> new MSStandingSignBlock(Block.Properties.copy(Blocks.OAK_SIGN), MSWoodTypes.MIND));
	public static final RegistryObject<Block> MIND_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("mind_aspect_wall_sign",
			() -> new MSWallSignBlock(Block.Properties.copy(Blocks.OAK_WALL_SIGN), MSWoodTypes.MIND));
	public static final RegistryObject<Block> MIND_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("mind_aspect_hanging_sign",
			() -> new MSHangingSignBlock(Block.Properties.copy(Blocks.OAK_HANGING_SIGN), MSWoodTypes.MIND));
	public static final RegistryObject<Block> MIND_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("mind_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(Block.Properties.copy(Blocks.OAK_WALL_HANGING_SIGN), MSWoodTypes.MIND));
	
	public static final RegistryObject<Item> MIND_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("mind_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), MIND_ASPECT_SIGN.get(), MIND_ASPECT_WALL_SIGN.get()));
	public static final RegistryObject<Item> MIND_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("mind_aspect_hanging_sign",
			() -> new HangingSignItem(MIND_ASPECT_HANGING_SIGN.get(), MIND_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
	//Rage
	public static final RegistryObject<Block> RAGE_ASPECT_STRIPPED_LOG = MSBlocks.REGISTER.register("rage_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> RAGE_ASPECT_STRIPPED_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_STRIPPED_LOG);
	
	public static final RegistryObject<Block> RAGE_ASPECT_STRIPPED_WOOD = MSBlocks.REGISTER.register("rage_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> RAGE_ASPECT_STRIPPED_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_STRIPPED_WOOD);
	
	public static final RegistryObject<Block> RAGE_ASPECT_LOG = MSBlocks.REGISTER.register("rage_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> RAGE_ASPECT_STRIPPED_LOG.get().defaultBlockState()));
	public static final RegistryObject<BlockItem> RAGE_ASPECT_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_LOG);
	
	public static final RegistryObject<Block> RAGE_ASPECT_WOOD = MSBlocks.REGISTER.register("rage_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> RAGE_ASPECT_STRIPPED_WOOD.get().defaultBlockState()));
	public static final RegistryObject<BlockItem> RAGE_ASPECT_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_WOOD);
	
	public static final RegistryObject<Block> RAGE_ASPECT_LEAVES = MSBlocks.REGISTER.register("rage_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final RegistryObject<BlockItem> RAGE_ASPECT_LEAVES_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_LEAVES);
	
	public static final RegistryObject<Block> RAGE_ASPECT_SAPLING = MSBlocks.REGISTER.register("rage_aspect_sapling",
			() -> new AspectSaplingBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<BlockItem> RAGE_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	
	public static final RegistryObject<Block> RAGE_ASPECT_PLANKS = MSBlocks.REGISTER.register("rage_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> RAGE_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_PLANKS);
	
	public static final RegistryObject<Block> RAGE_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("rage_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> RAGE_ASPECT_CARVED_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_CARVED_PLANKS);
	
	public static final RegistryObject<StairBlock> RAGE_ASPECT_STAIRS = MSBlocks.REGISTER.register("rage_aspect_stairs",
			() -> new StairBlock(() -> RAGE_ASPECT_PLANKS.get().defaultBlockState(), copy(RAGE_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> RAGE_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_STAIRS);
	
	public static final RegistryObject<SlabBlock> RAGE_ASPECT_SLAB = MSBlocks.REGISTER.register("rage_aspect_slab",
			() -> new SlabBlock(copy(RAGE_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> RAGE_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_SLAB);
	
	public static final RegistryObject<FenceBlock> RAGE_ASPECT_FENCE = MSBlocks.REGISTER.register("rage_aspect_fence",
			() -> new FenceBlock(copy(RAGE_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> RAGE_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_FENCE);
	
	public static final RegistryObject<FenceGateBlock> RAGE_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("rage_aspect_fence_gate",
			() -> new FenceGateBlock(copy(RAGE_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final RegistryObject<BlockItem> RAGE_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_FENCE_GATE);
	
	public static final RegistryObject<DoorBlock> RAGE_ASPECT_DOOR = MSBlocks.REGISTER.register("rage_aspect_door",
			() -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> RAGE_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_DOOR);
	
	public static final RegistryObject<TrapDoorBlock> RAGE_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("rage_aspect_trapdoor",
			() -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> RAGE_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_TRAPDOOR);
	
	public static final RegistryObject<PressurePlateBlock> RAGE_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("rage_aspect_pressure_plate",
			() -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(RAGE_ASPECT_PLANKS.get()), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> RAGE_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_PRESSURE_PLATE);
	
	public static final RegistryObject<ButtonBlock> RAGE_ASPECT_BUTTON = MSBlocks.REGISTER.register("rage_aspect_button",
			() -> new ButtonBlock(copy(RAGE_ASPECT_PLANKS.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<BlockItem> RAGE_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_BUTTON);
	
	public static final RegistryObject<Block> RAGE_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("rage_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> RAGE_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_BOOKSHELF);
	
	public static final RegistryObject<Block> RAGE_ASPECT_LADDER = MSBlocks.REGISTER.register("rage_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<BlockItem> RAGE_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.RAGE_ASPECT_LADDER);
	
	public static final RegistryObject<Block> RAGE_ASPECT_SIGN = MSBlocks.REGISTER.register("rage_aspect_sign",
			() -> new MSStandingSignBlock(Block.Properties.copy(Blocks.OAK_SIGN), MSWoodTypes.RAGE));
	public static final RegistryObject<Block> RAGE_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("rage_aspect_wall_sign",
			() -> new MSWallSignBlock(Block.Properties.copy(Blocks.OAK_WALL_SIGN), MSWoodTypes.RAGE));
	public static final RegistryObject<Block> RAGE_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("rage_aspect_hanging_sign",
			() -> new MSHangingSignBlock(Block.Properties.copy(Blocks.OAK_HANGING_SIGN), MSWoodTypes.RAGE));
	public static final RegistryObject<Block> RAGE_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("rage_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(Block.Properties.copy(Blocks.OAK_WALL_HANGING_SIGN), MSWoodTypes.RAGE));
	
	public static final RegistryObject<Item> RAGE_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("rage_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), RAGE_ASPECT_SIGN.get(), RAGE_ASPECT_WALL_SIGN.get()));
	public static final RegistryObject<Item> RAGE_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("rage_aspect_hanging_sign",
			() -> new HangingSignItem(RAGE_ASPECT_HANGING_SIGN.get(), RAGE_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
	//Space
	public static final RegistryObject<Block> SPACE_ASPECT_STRIPPED_LOG = MSBlocks.REGISTER.register("space_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> SPACE_ASPECT_STRIPPED_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_STRIPPED_LOG);
	
	public static final RegistryObject<Block> SPACE_ASPECT_STRIPPED_WOOD = MSBlocks.REGISTER.register("space_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> SPACE_ASPECT_STRIPPED_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_STRIPPED_WOOD);
	
	public static final RegistryObject<Block> SPACE_ASPECT_LOG = MSBlocks.REGISTER.register("space_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> SPACE_ASPECT_STRIPPED_LOG.get().defaultBlockState()));
	public static final RegistryObject<BlockItem> SPACE_ASPECT_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_LOG);
	
	public static final RegistryObject<Block> SPACE_ASPECT_WOOD = MSBlocks.REGISTER.register("space_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> SPACE_ASPECT_STRIPPED_WOOD.get().defaultBlockState()));
	public static final RegistryObject<BlockItem> SPACE_ASPECT_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_WOOD);
	
	public static final RegistryObject<Block> SPACE_ASPECT_LEAVES = MSBlocks.REGISTER.register("space_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final RegistryObject<BlockItem> SPACE_ASPECT_LEAVES_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_LEAVES);
	
	public static final RegistryObject<Block> SPACE_ASPECT_SAPLING = MSBlocks.REGISTER.register("space_aspect_sapling",
			() -> new AspectSaplingBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<BlockItem> SPACE_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	
	public static final RegistryObject<Block> SPACE_ASPECT_PLANKS = MSBlocks.REGISTER.register("space_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> SPACE_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_PLANKS);
	
	public static final RegistryObject<Block> SPACE_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("space_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> SPACE_ASPECT_CARVED_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_CARVED_PLANKS);
	
	public static final RegistryObject<StairBlock> SPACE_ASPECT_STAIRS = MSBlocks.REGISTER.register("space_aspect_stairs",
			() -> new StairBlock(() -> SPACE_ASPECT_PLANKS.get().defaultBlockState(), copy(SPACE_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> SPACE_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_STAIRS);
	
	public static final RegistryObject<SlabBlock> SPACE_ASPECT_SLAB = MSBlocks.REGISTER.register("space_aspect_slab",
			() -> new SlabBlock(copy(SPACE_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> SPACE_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_SLAB);
	
	public static final RegistryObject<FenceBlock> SPACE_ASPECT_FENCE = MSBlocks.REGISTER.register("space_aspect_fence",
			() -> new FenceBlock(copy(SPACE_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> SPACE_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_FENCE);
	
	public static final RegistryObject<FenceGateBlock> SPACE_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("space_aspect_fence_gate",
			() -> new FenceGateBlock(copy(SPACE_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final RegistryObject<BlockItem> SPACE_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_FENCE_GATE);
	
	public static final RegistryObject<DoorBlock> SPACE_ASPECT_DOOR = MSBlocks.REGISTER.register("space_aspect_door",
			() -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> SPACE_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_DOOR);
	
	public static final RegistryObject<TrapDoorBlock> SPACE_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("space_aspect_trapdoor",
			() -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> SPACE_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_TRAPDOOR);
	
	public static final RegistryObject<PressurePlateBlock> SPACE_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("space_aspect_pressure_plate",
			() -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(SPACE_ASPECT_PLANKS.get()), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> SPACE_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_PRESSURE_PLATE);
	
	public static final RegistryObject<ButtonBlock> SPACE_ASPECT_BUTTON = MSBlocks.REGISTER.register("space_aspect_button",
			() -> new ButtonBlock(copy(SPACE_ASPECT_PLANKS.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<BlockItem> SPACE_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_BUTTON);
	
	public static final RegistryObject<Block> SPACE_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("space_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> SPACE_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_BOOKSHELF);
	
	public static final RegistryObject<Block> SPACE_ASPECT_LADDER = MSBlocks.REGISTER.register("space_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<BlockItem> SPACE_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.SPACE_ASPECT_LADDER);
	
	public static final RegistryObject<Block> SPACE_ASPECT_SIGN = MSBlocks.REGISTER.register("space_aspect_sign",
			() -> new MSStandingSignBlock(Block.Properties.copy(Blocks.OAK_SIGN), MSWoodTypes.SPACE));
	public static final RegistryObject<Block> SPACE_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("space_aspect_wall_sign",
			() -> new MSWallSignBlock(Block.Properties.copy(Blocks.OAK_WALL_SIGN), MSWoodTypes.SPACE));
	public static final RegistryObject<Block> SPACE_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("space_aspect_hanging_sign",
			() -> new MSHangingSignBlock(Block.Properties.copy(Blocks.OAK_HANGING_SIGN), MSWoodTypes.SPACE));
	public static final RegistryObject<Block> SPACE_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("space_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(Block.Properties.copy(Blocks.OAK_WALL_HANGING_SIGN), MSWoodTypes.SPACE));
	
	public static final RegistryObject<Item> SPACE_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("space_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), SPACE_ASPECT_SIGN.get(), SPACE_ASPECT_WALL_SIGN.get()));
	public static final RegistryObject<Item> SPACE_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("space_aspect_hanging_sign",
			() -> new HangingSignItem(SPACE_ASPECT_HANGING_SIGN.get(), SPACE_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
	//Time
	public static final RegistryObject<Block> TIME_ASPECT_STRIPPED_LOG = MSBlocks.REGISTER.register("time_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> TIME_ASPECT_STRIPPED_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_STRIPPED_LOG);
	
	public static final RegistryObject<Block> TIME_ASPECT_STRIPPED_WOOD = MSBlocks.REGISTER.register("time_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> TIME_ASPECT_STRIPPED_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_STRIPPED_WOOD);
	
	public static final RegistryObject<Block> TIME_ASPECT_LOG = MSBlocks.REGISTER.register("time_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> TIME_ASPECT_STRIPPED_LOG.get().defaultBlockState()));
	public static final RegistryObject<BlockItem> TIME_ASPECT_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_LOG);
	
	public static final RegistryObject<Block> TIME_ASPECT_WOOD = MSBlocks.REGISTER.register("time_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> TIME_ASPECT_STRIPPED_WOOD.get().defaultBlockState()));
	public static final RegistryObject<BlockItem> TIME_ASPECT_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_WOOD);
	
	public static final RegistryObject<Block> TIME_ASPECT_LEAVES = MSBlocks.REGISTER.register("time_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final RegistryObject<BlockItem> TIME_ASPECT_LEAVES_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_LEAVES);
	
	public static final RegistryObject<Block> TIME_ASPECT_SAPLING = MSBlocks.REGISTER.register("time_aspect_sapling",
			() -> new AspectSaplingBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<BlockItem> TIME_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	
	public static final RegistryObject<Block> TIME_ASPECT_PLANKS = MSBlocks.REGISTER.register("time_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> TIME_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_PLANKS);
	
	public static final RegistryObject<Block> TIME_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("time_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> TIME_ASPECT_CARVED_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_CARVED_PLANKS);
	
	public static final RegistryObject<StairBlock> TIME_ASPECT_STAIRS = MSBlocks.REGISTER.register("time_aspect_stairs",
			() -> new StairBlock(() -> TIME_ASPECT_PLANKS.get().defaultBlockState(), copy(TIME_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> TIME_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_STAIRS);
	
	public static final RegistryObject<SlabBlock> TIME_ASPECT_SLAB = MSBlocks.REGISTER.register("time_aspect_slab",
			() -> new SlabBlock(copy(TIME_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> TIME_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_SLAB);
	
	public static final RegistryObject<FenceBlock> TIME_ASPECT_FENCE = MSBlocks.REGISTER.register("time_aspect_fence",
			() -> new FenceBlock(copy(TIME_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> TIME_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_FENCE);
	
	public static final RegistryObject<FenceGateBlock> TIME_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("time_aspect_fence_gate",
			() -> new FenceGateBlock(copy(TIME_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final RegistryObject<BlockItem> TIME_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_FENCE_GATE);
	
	public static final RegistryObject<DoorBlock> TIME_ASPECT_DOOR = MSBlocks.REGISTER.register("time_aspect_door",
			() -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> TIME_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_DOOR);
	
	public static final RegistryObject<TrapDoorBlock> TIME_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("time_aspect_trapdoor",
			() -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> TIME_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_TRAPDOOR);
	
	public static final RegistryObject<PressurePlateBlock> TIME_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("time_aspect_pressure_plate",
			() -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(TIME_ASPECT_PLANKS.get()), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> TIME_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_PRESSURE_PLATE);
	
	public static final RegistryObject<ButtonBlock> TIME_ASPECT_BUTTON = MSBlocks.REGISTER.register("time_aspect_button",
			() -> new ButtonBlock(copy(TIME_ASPECT_PLANKS.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<BlockItem> TIME_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_BUTTON);
	
	public static final RegistryObject<Block> TIME_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("time_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> TIME_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_BOOKSHELF);
	
	public static final RegistryObject<Block> TIME_ASPECT_LADDER = MSBlocks.REGISTER.register("time_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<BlockItem> TIME_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.TIME_ASPECT_LADDER);
	
	public static final RegistryObject<Block> TIME_ASPECT_SIGN = MSBlocks.REGISTER.register("time_aspect_sign",
			() -> new MSStandingSignBlock(Block.Properties.copy(Blocks.OAK_SIGN), MSWoodTypes.TIME));
	public static final RegistryObject<Block> TIME_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("time_aspect_wall_sign",
			() -> new MSWallSignBlock(Block.Properties.copy(Blocks.OAK_WALL_SIGN), MSWoodTypes.TIME));
	public static final RegistryObject<Block> TIME_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("time_aspect_hanging_sign",
			() -> new MSHangingSignBlock(Block.Properties.copy(Blocks.OAK_HANGING_SIGN), MSWoodTypes.TIME));
	public static final RegistryObject<Block> TIME_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("time_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(Block.Properties.copy(Blocks.OAK_WALL_HANGING_SIGN), MSWoodTypes.TIME));
	
	public static final RegistryObject<Item> TIME_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("time_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), TIME_ASPECT_SIGN.get(), TIME_ASPECT_WALL_SIGN.get()));
	public static final RegistryObject<Item> TIME_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("time_aspect_hanging_sign",
			() -> new HangingSignItem(TIME_ASPECT_HANGING_SIGN.get(), TIME_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
	//Void
	public static final RegistryObject<Block> VOID_ASPECT_STRIPPED_LOG = MSBlocks.REGISTER.register("void_aspect_stripped_log",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> VOID_ASPECT_STRIPPED_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_STRIPPED_LOG);
	
	public static final RegistryObject<Block> VOID_ASPECT_STRIPPED_WOOD = MSBlocks.REGISTER.register("void_aspect_stripped_wood",
			() -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> VOID_ASPECT_STRIPPED_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_STRIPPED_WOOD);
	
	public static final RegistryObject<Block> VOID_ASPECT_LOG = MSBlocks.REGISTER.register("void_aspect_log",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> VOID_ASPECT_STRIPPED_LOG.get().defaultBlockState()));
	public static final RegistryObject<BlockItem> VOID_ASPECT_LOG_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_LOG);
	
	public static final RegistryObject<Block> VOID_ASPECT_WOOD = MSBlocks.REGISTER.register("void_aspect_wood",
			() -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD),
					() -> VOID_ASPECT_STRIPPED_WOOD.get().defaultBlockState()));
	public static final RegistryObject<BlockItem> VOID_ASPECT_WOOD_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_WOOD);
	
	public static final RegistryObject<Block> VOID_ASPECT_LEAVES = MSBlocks.REGISTER.register("void_aspect_leaves",
			() -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks()
					.sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final RegistryObject<BlockItem> VOID_ASPECT_LEAVES_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_LEAVES);
	
	public static final RegistryObject<Block> VOID_ASPECT_SAPLING = MSBlocks.REGISTER.register("void_aspect_sapling",
			() -> new AspectSaplingBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<BlockItem> VOID_ASPECT_SAPLING_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_SAPLING, new Item.Properties().rarity(Rarity.UNCOMMON));
	
	public static final RegistryObject<Block> VOID_ASPECT_PLANKS = MSBlocks.REGISTER.register("void_aspect_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> VOID_ASPECT_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_PLANKS);
	
	public static final RegistryObject<Block> VOID_ASPECT_CARVED_PLANKS = MSBlocks.REGISTER.register("void_aspect_carved_planks",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> VOID_ASPECT_CARVED_PLANKS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_CARVED_PLANKS);
	
	public static final RegistryObject<StairBlock> VOID_ASPECT_STAIRS = MSBlocks.REGISTER.register("void_aspect_stairs",
			() -> new StairBlock(() -> VOID_ASPECT_PLANKS.get().defaultBlockState(), copy(VOID_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> VOID_ASPECT_STAIRS_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_STAIRS);
	
	public static final RegistryObject<SlabBlock> VOID_ASPECT_SLAB = MSBlocks.REGISTER.register("void_aspect_slab",
			() -> new SlabBlock(copy(VOID_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> VOID_ASPECT_SLAB_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_SLAB);
	
	public static final RegistryObject<FenceBlock> VOID_ASPECT_FENCE = MSBlocks.REGISTER.register("void_aspect_fence",
			() -> new FenceBlock(copy(VOID_ASPECT_PLANKS.get())));
	public static final RegistryObject<BlockItem> VOID_ASPECT_FENCE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_FENCE);
	
	public static final RegistryObject<FenceGateBlock> VOID_ASPECT_FENCE_GATE = MSBlocks.REGISTER.register("void_aspect_fence_gate",
			() -> new FenceGateBlock(copy(VOID_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final RegistryObject<BlockItem> VOID_ASPECT_FENCE_GATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_FENCE_GATE);
	
	public static final RegistryObject<DoorBlock> VOID_ASPECT_DOOR = MSBlocks.REGISTER.register("void_aspect_door",
			() -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> VOID_ASPECT_DOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_DOOR);
	
	public static final RegistryObject<TrapDoorBlock> VOID_ASPECT_TRAPDOOR = MSBlocks.REGISTER.register("void_aspect_trapdoor",
			() -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> VOID_ASPECT_TRAPDOOR_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_TRAPDOOR);
	
	public static final RegistryObject<PressurePlateBlock> VOID_ASPECT_PRESSURE_PLATE = MSBlocks.REGISTER.register("void_aspect_pressure_plate",
			() -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(VOID_ASPECT_PLANKS.get()), BlockSetType.OAK));
	public static final RegistryObject<BlockItem> VOID_ASPECT_PRESSURE_PLATE_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_PRESSURE_PLATE);
	
	public static final RegistryObject<ButtonBlock> VOID_ASPECT_BUTTON = MSBlocks.REGISTER.register("void_aspect_button",
			() -> new ButtonBlock(copy(VOID_ASPECT_PLANKS.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<BlockItem> VOID_ASPECT_BUTTON_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_BUTTON);
	
	public static final RegistryObject<Block> VOID_ASPECT_BOOKSHELF = MSBlocks.REGISTER.register("void_aspect_bookshelf",
			() -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<BlockItem> VOID_ASPECT_BOOKSHELF_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_BOOKSHELF);
	
	public static final RegistryObject<Block> VOID_ASPECT_LADDER = MSBlocks.REGISTER.register("void_aspect_ladder",
			() -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<BlockItem> VOID_ASPECT_LADDER_ITEM = MSItems.registerBlockItem(AspectTreeBlocks.VOID_ASPECT_LADDER);
	
	public static final RegistryObject<Block> VOID_ASPECT_SIGN = MSBlocks.REGISTER.register("void_aspect_sign",
			() -> new MSStandingSignBlock(Block.Properties.copy(Blocks.OAK_SIGN), MSWoodTypes.VOID));
	public static final RegistryObject<Block> VOID_ASPECT_WALL_SIGN = MSBlocks.REGISTER.register("void_aspect_wall_sign",
			() -> new MSWallSignBlock(Block.Properties.copy(Blocks.OAK_WALL_SIGN), MSWoodTypes.VOID));
	public static final RegistryObject<Block> VOID_ASPECT_HANGING_SIGN = MSBlocks.REGISTER.register("void_aspect_hanging_sign",
			() -> new MSHangingSignBlock(Block.Properties.copy(Blocks.OAK_HANGING_SIGN), MSWoodTypes.VOID));
	public static final RegistryObject<Block> VOID_ASPECT_WALL_HANGING_SIGN = MSBlocks.REGISTER.register("void_aspect_wall_hanging_sign",
			() -> new MSWallHangingSignBlock(Block.Properties.copy(Blocks.OAK_WALL_HANGING_SIGN), MSWoodTypes.VOID));
	
	public static final RegistryObject<Item> VOID_ASPECT_SIGN_ITEM = MSItems.REGISTER.register("void_aspect_sign",
			() -> new SignItem(new Item.Properties().stacksTo(16), VOID_ASPECT_SIGN.get(), VOID_ASPECT_WALL_SIGN.get()));
	public static final RegistryObject<Item> VOID_ASPECT_HANGING_SIGN_ITEM = MSItems.REGISTER.register("void_aspect_hanging_sign",
			() -> new HangingSignItem(VOID_ASPECT_HANGING_SIGN.get(), VOID_ASPECT_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
	
}
