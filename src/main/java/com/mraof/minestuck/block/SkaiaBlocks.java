package com.mraof.minestuck.block;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.RegistryObject;

import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.copy;

public final class SkaiaBlocks
{
	public static final RegistryObject<Block> SKAIA_PORTAL = MSBlocks.REGISTER.register("skaia_portal", () -> new SkaiaPortalBlock(Block.Properties.of().mapColor(MapColor.COLOR_CYAN).pushReaction(PushReaction.BLOCK).noCollission().lightLevel(state -> 11).strength(-1.0F, 3600000.0F).noLootTable()));
	
	public static final RegistryObject<Block> BLACK_CHESS_DIRT = MSBlocks.REGISTER.register("black_chess_dirt", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_BLACK).strength(0.5F).sound(SoundType.GRAVEL)));
	public static final RegistryObject<Block> WHITE_CHESS_DIRT = MSBlocks.REGISTER.register("white_chess_dirt", () -> new Block(Block.Properties.of().mapColor(MapColor.SNOW).strength(0.5F).sound(SoundType.GRAVEL)));
	public static final RegistryObject<Block> DARK_GRAY_CHESS_DIRT = MSBlocks.REGISTER.register("dark_gray_chess_dirt", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(0.5F).sound(SoundType.GRAVEL)));
	public static final RegistryObject<Block> LIGHT_GRAY_CHESS_DIRT = MSBlocks.REGISTER.register("light_gray_chess_dirt", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).strength(0.5F).sound(SoundType.GRAVEL)));
	
	
	public static final RegistryObject<Block> BLACK_CHESS_BRICKS = MSBlocks.REGISTER.register("black_chess_bricks", () -> new Block(Block.Properties.of().mapColor(MapColor.TERRACOTTA_BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final RegistryObject<Block> BLACK_CHESS_BRICK_TRIM = MSBlocks.REGISTER.register("black_chess_brick_trim", () -> new MSDirectionalBlock(copy(BLACK_CHESS_BRICKS.get())));
	public static final RegistryObject<WallBlock> BLACK_CHESS_BRICK_WALL = MSBlocks.REGISTER.register("black_chess_brick_wall", () -> new WallBlock(copy(BLACK_CHESS_BRICKS.get())));
	public static final RegistryObject<SlabBlock> BLACK_CHESS_BRICK_SLAB = MSBlocks.REGISTER.register("black_chess_brick_slab", () -> new SlabBlock(copy(BLACK_CHESS_BRICKS.get())));
	public static final RegistryObject<StairBlock> BLACK_CHESS_BRICK_STAIRS = MSBlocks.REGISTER.register("black_chess_brick_stairs", () -> new StairBlock(() -> BLACK_CHESS_BRICKS.get().defaultBlockState(), copy(BLACK_CHESS_BRICKS.get())));
	
	public static final RegistryObject<Block> BLACK_CHESS_BRICK_SMOOTH = MSBlocks.REGISTER.register("black_chess_brick_smooth", () -> new Block(copy(BLACK_CHESS_BRICKS.get())));
	public static final RegistryObject<WallBlock> BLACK_CHESS_BRICK_SMOOTH_WALL = MSBlocks.REGISTER.register("black_chess_brick_smooth_wall", () -> new WallBlock(copy(BLACK_CHESS_BRICK_SMOOTH.get())));
	public static final RegistryObject<SlabBlock> BLACK_CHESS_BRICK_SMOOTH_SLAB = MSBlocks.REGISTER.register("black_chess_brick_smooth_slab", () -> new SlabBlock(copy(BLACK_CHESS_BRICK_SMOOTH.get())));
	public static final RegistryObject<StairBlock> BLACK_CHESS_BRICK_SMOOTH_STAIRS = MSBlocks.REGISTER.register("black_chess_brick_smooth_stairs", () -> new StairBlock(() -> BLACK_CHESS_BRICK_SMOOTH.get().defaultBlockState(), copy(BLACK_CHESS_BRICK_SMOOTH.get())));
	public static final RegistryObject<PressurePlateBlock> BLACK_CHESS_BRICK_SMOOTH_PRESSURE_PLATE = MSBlocks.REGISTER.register("black_chess_brick_smooth_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(BLACK_CHESS_BRICK_SMOOTH.get()), BlockSetType.STONE));
	public static final RegistryObject<ButtonBlock> BLACK_CHESS_BRICK_SMOOTH_BUTTON = MSBlocks.REGISTER.register("black_chess_brick_smooth_button", () -> new ButtonBlock(copy(BLACK_CHESS_BRICK_SMOOTH.get()), BlockSetType.STONE, 10, true));
	
	
	public static final RegistryObject<Block> DARK_GRAY_CHESS_BRICKS = MSBlocks.REGISTER.register("dark_gray_chess_bricks", () -> new Block(Block.Properties.of().mapColor(MapColor.TERRACOTTA_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final RegistryObject<Block> DARK_GRAY_CHESS_BRICK_TRIM = MSBlocks.REGISTER.register("dark_gray_chess_brick_trim", () -> new MSDirectionalBlock(copy(DARK_GRAY_CHESS_BRICKS.get())));
	public static final RegistryObject<WallBlock> DARK_GRAY_CHESS_BRICK_WALL = MSBlocks.REGISTER.register("dark_gray_chess_brick_wall", () -> new WallBlock(copy(DARK_GRAY_CHESS_BRICKS.get())));
	public static final RegistryObject<SlabBlock> DARK_GRAY_CHESS_BRICK_SLAB = MSBlocks.REGISTER.register("dark_gray_chess_brick_slab", () -> new SlabBlock(copy(DARK_GRAY_CHESS_BRICKS.get())));
	public static final RegistryObject<StairBlock> DARK_GRAY_CHESS_BRICK_STAIRS = MSBlocks.REGISTER.register("dark_gray_chess_brick_stairs", () -> new StairBlock(() -> DARK_GRAY_CHESS_BRICKS.get().defaultBlockState(), copy(DARK_GRAY_CHESS_BRICKS.get())));
	
	public static final RegistryObject<Block> DARK_GRAY_CHESS_BRICK_SMOOTH = MSBlocks.REGISTER.register("dark_gray_chess_brick_smooth", () -> new Block(copy(DARK_GRAY_CHESS_BRICKS.get())));
	public static final RegistryObject<WallBlock> DARK_GRAY_CHESS_BRICK_SMOOTH_WALL = MSBlocks.REGISTER.register("dark_gray_chess_brick_smooth_wall", () -> new WallBlock(copy(DARK_GRAY_CHESS_BRICK_SMOOTH.get())));
	public static final RegistryObject<SlabBlock> DARK_GRAY_CHESS_BRICK_SMOOTH_SLAB = MSBlocks.REGISTER.register("dark_gray_chess_brick_smooth_slab", () -> new SlabBlock(copy(DARK_GRAY_CHESS_BRICK_SMOOTH.get())));
	public static final RegistryObject<StairBlock> DARK_GRAY_CHESS_BRICK_SMOOTH_STAIRS = MSBlocks.REGISTER.register("dark_gray_chess_brick_smooth_stairs", () -> new StairBlock(() -> DARK_GRAY_CHESS_BRICK_SMOOTH.get().defaultBlockState(), copy(DARK_GRAY_CHESS_BRICK_SMOOTH.get())));
	public static final RegistryObject<PressurePlateBlock> DARK_GRAY_CHESS_BRICK_SMOOTH_PRESSURE_PLATE = MSBlocks.REGISTER.register("dark_gray_chess_brick_smooth_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(DARK_GRAY_CHESS_BRICKS.get()), BlockSetType.STONE));
	public static final RegistryObject<ButtonBlock> DARK_GRAY_CHESS_BRICK_SMOOTH_BUTTON = MSBlocks.REGISTER.register("dark_gray_chess_brick_smooth_button", () -> new ButtonBlock(copy(DARK_GRAY_CHESS_BRICK_SMOOTH.get()), BlockSetType.STONE, 10, true));
	
	
	public static final RegistryObject<Block> LIGHT_GRAY_CHESS_BRICKS = MSBlocks.REGISTER.register("light_gray_chess_bricks", () -> new Block(Block.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final RegistryObject<Block> LIGHT_GRAY_CHESS_BRICK_TRIM = MSBlocks.REGISTER.register("light_gray_chess_brick_trim", () -> new MSDirectionalBlock(copy(LIGHT_GRAY_CHESS_BRICKS.get())));
	public static final RegistryObject<WallBlock> LIGHT_GRAY_CHESS_BRICK_WALL = MSBlocks.REGISTER.register("light_gray_chess_brick_wall", () -> new WallBlock(copy(LIGHT_GRAY_CHESS_BRICKS.get())));
	public static final RegistryObject<SlabBlock> LIGHT_GRAY_CHESS_BRICK_SLAB = MSBlocks.REGISTER.register("light_gray_chess_brick_slab", () -> new SlabBlock(copy(LIGHT_GRAY_CHESS_BRICKS.get())));
	public static final RegistryObject<StairBlock> LIGHT_GRAY_CHESS_BRICK_STAIRS = MSBlocks.REGISTER.register("light_gray_chess_brick_stairs", () -> new StairBlock(() -> LIGHT_GRAY_CHESS_BRICKS.get().defaultBlockState(), copy(LIGHT_GRAY_CHESS_BRICKS.get())));
	
	public static final RegistryObject<Block> LIGHT_GRAY_CHESS_BRICK_SMOOTH = MSBlocks.REGISTER.register("light_gray_chess_brick_smooth", () -> new Block(copy(LIGHT_GRAY_CHESS_BRICKS.get())));
	public static final RegistryObject<WallBlock> LIGHT_GRAY_CHESS_BRICK_SMOOTH_WALL = MSBlocks.REGISTER.register("light_gray_chess_brick_smooth_wall", () -> new WallBlock(copy(LIGHT_GRAY_CHESS_BRICK_SMOOTH.get())));
	public static final RegistryObject<SlabBlock> LIGHT_GRAY_CHESS_BRICK_SMOOTH_SLAB = MSBlocks.REGISTER.register("light_gray_chess_brick_smooth_slab", () -> new SlabBlock(copy(LIGHT_GRAY_CHESS_BRICK_SMOOTH.get())));
	public static final RegistryObject<StairBlock> LIGHT_GRAY_CHESS_BRICK_SMOOTH_STAIRS = MSBlocks.REGISTER.register("light_gray_chess_brick_smooth_stairs", () -> new StairBlock(() -> LIGHT_GRAY_CHESS_BRICK_SMOOTH.get().defaultBlockState(), copy(LIGHT_GRAY_CHESS_BRICK_SMOOTH.get())));
	public static final RegistryObject<PressurePlateBlock> LIGHT_GRAY_CHESS_BRICK_SMOOTH_PRESSURE_PLATE = MSBlocks.REGISTER.register("light_gray_chess_brick_smooth_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(LIGHT_GRAY_CHESS_BRICK_SMOOTH.get()), BlockSetType.STONE));
	public static final RegistryObject<ButtonBlock> LIGHT_GRAY_CHESS_BRICK_SMOOTH_BUTTON = MSBlocks.REGISTER.register("light_gray_chess_brick_smooth_button", () -> new ButtonBlock(copy(LIGHT_GRAY_CHESS_BRICK_SMOOTH.get()), BlockSetType.STONE, 10, true));
	
	
	public static final RegistryObject<Block> WHITE_CHESS_BRICKS = MSBlocks.REGISTER.register("white_chess_bricks", () -> new Block(Block.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final RegistryObject<Block> WHITE_CHESS_BRICK_TRIM = MSBlocks.REGISTER.register("white_chess_brick_trim", () -> new MSDirectionalBlock(copy(WHITE_CHESS_BRICKS.get())));
	public static final RegistryObject<WallBlock> WHITE_CHESS_BRICK_WALL = MSBlocks.REGISTER.register("white_chess_brick_wall", () -> new WallBlock(copy(WHITE_CHESS_BRICKS.get())));
	public static final RegistryObject<SlabBlock> WHITE_CHESS_BRICK_SLAB = MSBlocks.REGISTER.register("white_chess_brick_slab", () -> new SlabBlock(copy(WHITE_CHESS_BRICKS.get())));
	public static final RegistryObject<StairBlock> WHITE_CHESS_BRICK_STAIRS = MSBlocks.REGISTER.register("white_chess_brick_stairs", () -> new StairBlock(() -> WHITE_CHESS_BRICKS.get().defaultBlockState(), copy(WHITE_CHESS_BRICKS.get())));
	
	public static final RegistryObject<Block> WHITE_CHESS_BRICK_SMOOTH = MSBlocks.REGISTER.register("white_chess_brick_smooth", () -> new Block(copy(WHITE_CHESS_BRICKS.get())));
	public static final RegistryObject<WallBlock> WHITE_CHESS_BRICK_SMOOTH_WALL = MSBlocks.REGISTER.register("white_chess_brick_smooth_wall", () -> new WallBlock(copy(WHITE_CHESS_BRICK_SMOOTH.get())));
	public static final RegistryObject<SlabBlock> WHITE_CHESS_BRICK_SMOOTH_SLAB = MSBlocks.REGISTER.register("white_chess_brick_smooth_slab", () -> new SlabBlock(copy(WHITE_CHESS_BRICK_SMOOTH.get())));
	public static final RegistryObject<StairBlock> WHITE_CHESS_BRICK_SMOOTH_STAIRS = MSBlocks.REGISTER.register("white_chess_brick_smooth_stairs", () -> new StairBlock(() -> WHITE_CHESS_BRICK_SMOOTH.get().defaultBlockState(), copy(WHITE_CHESS_BRICK_SMOOTH.get())));
	public static final RegistryObject<PressurePlateBlock> WHITE_CHESS_BRICK_SMOOTH_PRESSURE_PLATE = MSBlocks.REGISTER.register("white_chess_brick_smooth_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(WHITE_CHESS_BRICK_SMOOTH.get()), BlockSetType.STONE));
	public static final RegistryObject<ButtonBlock> WHITE_CHESS_BRICK_SMOOTH_BUTTON = MSBlocks.REGISTER.register("white_chess_brick_smooth_button", () -> new ButtonBlock(copy(WHITE_CHESS_BRICK_SMOOTH.get()), BlockSetType.STONE, 10, true));
	
	
	public static final RegistryObject<Block> CHECKERED_STAINED_GLASS = MSBlocks.REGISTER.register("checkered_stained_glass", () -> new StainedGlassBlock(DyeColor.BLUE, Block.Properties.of().mapColor(DyeColor.BLUE).instrument(NoteBlockInstrument.HAT).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn(MSBlocks::never).isRedstoneConductor(MSBlocks::never).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never)));
	public static final RegistryObject<Block> WHITE_PAWN_STAINED_GLASS = MSBlocks.REGISTER.register("white_pawn_stained_glass", () -> new StainedGlassBlock(DyeColor.BLUE, copy(CHECKERED_STAINED_GLASS.get())));
	public static final RegistryObject<Block> WHITE_CROWN_STAINED_GLASS = MSBlocks.REGISTER.register("white_crown_stained_glass", () -> new StainedGlassBlock(DyeColor.BLUE, copy(CHECKERED_STAINED_GLASS.get())));
	public static final RegistryObject<Block> BLACK_PAWN_STAINED_GLASS = MSBlocks.REGISTER.register("black_pawn_stained_glass", () -> new StainedGlassBlock(DyeColor.BLUE, copy(CHECKERED_STAINED_GLASS.get())));
	public static final RegistryObject<Block> BLACK_CROWN_STAINED_GLASS = MSBlocks.REGISTER.register("black_crown_stained_glass", () -> new StainedGlassBlock(DyeColor.BLUE, copy(CHECKERED_STAINED_GLASS.get())));
	
	public static void init()
	{
	}
}
