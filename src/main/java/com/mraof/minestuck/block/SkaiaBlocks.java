package com.mraof.minestuck.block;

import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.blockentity.SkaiaPortalBlockEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.RegistryObject;

import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.copy;

public final class SkaiaBlocks
{
	public static final ItemBlockPair<Block, BlockItem> SKAIA_PORTAL = ItemBlockPair.register("skaia_portal",
			() -> new SkaiaPortalBlock(Block.Properties.of().mapColor(MapColor.COLOR_CYAN).pushReaction(PushReaction.BLOCK).noCollission().lightLevel(state -> 11).strength(-1.0F, 3600000.0F).noLootTable()),
			() -> new Item.Properties().rarity(Rarity.EPIC));
	@SuppressWarnings("DataFlowIssue")
	public static final RegistryObject<BlockEntityType<SkaiaPortalBlockEntity>> SKAIA_PORTAL_BE = MSBlockEntityTypes.REGISTER.register("skaia_portal", () -> BlockEntityType.Builder.of(SkaiaPortalBlockEntity::new, SKAIA_PORTAL.asBlock()).build(null));
	
	public static final ItemBlockPair<Block, BlockItem> BLACK_CHESS_DIRT = ItemBlockPair.register("black_chess_dirt", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_BLACK).strength(0.5F).sound(SoundType.GRAVEL)));
	public static final ItemBlockPair<Block, BlockItem> WHITE_CHESS_DIRT = ItemBlockPair.register("white_chess_dirt", () -> new Block(Block.Properties.of().mapColor(MapColor.SNOW).strength(0.5F).sound(SoundType.GRAVEL)));
	public static final ItemBlockPair<Block, BlockItem> DARK_GRAY_CHESS_DIRT = ItemBlockPair.register("dark_gray_chess_dirt", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(0.5F).sound(SoundType.GRAVEL)));
	public static final ItemBlockPair<Block, BlockItem> LIGHT_GRAY_CHESS_DIRT = ItemBlockPair.register("light_gray_chess_dirt", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).strength(0.5F).sound(SoundType.GRAVEL)));
	
	
	public static final ItemBlockPair<Block, BlockItem> BLACK_CHESS_BRICKS = ItemBlockPair.register("black_chess_bricks", () -> new Block(Block.Properties.of().mapColor(MapColor.TERRACOTTA_BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final ItemBlockPair<Block, BlockItem> BLACK_CHESS_BRICK_TRIM = ItemBlockPair.register("black_chess_brick_trim", () -> new MSDirectionalBlock(copy(BLACK_CHESS_BRICKS.asBlock())));
	public static final ItemBlockPair<WallBlock, BlockItem> BLACK_CHESS_BRICK_WALL = ItemBlockPair.register("black_chess_brick_wall", () -> new WallBlock(copy(BLACK_CHESS_BRICKS.asBlock())));
	public static final ItemBlockPair<SlabBlock, BlockItem> BLACK_CHESS_BRICK_SLAB = ItemBlockPair.register("black_chess_brick_slab", () -> new SlabBlock(copy(BLACK_CHESS_BRICKS.asBlock())));
	public static final ItemBlockPair<StairBlock, BlockItem> BLACK_CHESS_BRICK_STAIRS = ItemBlockPair.register("black_chess_brick_stairs", () -> new StairBlock(() -> BLACK_CHESS_BRICKS.asBlock().defaultBlockState(), copy(BLACK_CHESS_BRICKS.asBlock())));
	
	public static final ItemBlockPair<Block, BlockItem> BLACK_CHESS_BRICK_SMOOTH = ItemBlockPair.register("black_chess_brick_smooth", () -> new Block(copy(BLACK_CHESS_BRICKS.asBlock())));
	public static final ItemBlockPair<WallBlock, BlockItem> BLACK_CHESS_BRICK_SMOOTH_WALL = ItemBlockPair.register("black_chess_brick_smooth_wall", () -> new WallBlock(copy(BLACK_CHESS_BRICK_SMOOTH.asBlock())));
	public static final ItemBlockPair<SlabBlock, BlockItem> BLACK_CHESS_BRICK_SMOOTH_SLAB = ItemBlockPair.register("black_chess_brick_smooth_slab", () -> new SlabBlock(copy(BLACK_CHESS_BRICK_SMOOTH.asBlock())));
	public static final ItemBlockPair<StairBlock, BlockItem> BLACK_CHESS_BRICK_SMOOTH_STAIRS = ItemBlockPair.register("black_chess_brick_smooth_stairs", () -> new StairBlock(() -> BLACK_CHESS_BRICK_SMOOTH.asBlock().defaultBlockState(), copy(BLACK_CHESS_BRICK_SMOOTH.asBlock())));
	public static final ItemBlockPair<PressurePlateBlock, BlockItem> BLACK_CHESS_BRICK_SMOOTH_PRESSURE_PLATE = ItemBlockPair.register("black_chess_brick_smooth_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(BLACK_CHESS_BRICK_SMOOTH.asBlock()), BlockSetType.STONE));
	public static final ItemBlockPair<ButtonBlock, BlockItem> BLACK_CHESS_BRICK_SMOOTH_BUTTON = ItemBlockPair.register("black_chess_brick_smooth_button", () -> new ButtonBlock(copy(BLACK_CHESS_BRICK_SMOOTH.asBlock()), BlockSetType.STONE, 10, true));
	
	
	public static final ItemBlockPair<Block, BlockItem> DARK_GRAY_CHESS_BRICKS = ItemBlockPair.register("dark_gray_chess_bricks", () -> new Block(Block.Properties.of().mapColor(MapColor.TERRACOTTA_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final ItemBlockPair<Block, BlockItem> DARK_GRAY_CHESS_BRICK_TRIM = ItemBlockPair.register("dark_gray_chess_brick_trim", () -> new MSDirectionalBlock(copy(DARK_GRAY_CHESS_BRICKS.asBlock())));
	public static final ItemBlockPair<WallBlock, BlockItem> DARK_GRAY_CHESS_BRICK_WALL = ItemBlockPair.register("dark_gray_chess_brick_wall", () -> new WallBlock(copy(DARK_GRAY_CHESS_BRICKS.asBlock())));
	public static final ItemBlockPair<SlabBlock, BlockItem> DARK_GRAY_CHESS_BRICK_SLAB = ItemBlockPair.register("dark_gray_chess_brick_slab", () -> new SlabBlock(copy(DARK_GRAY_CHESS_BRICKS.asBlock())));
	public static final ItemBlockPair<StairBlock, BlockItem> DARK_GRAY_CHESS_BRICK_STAIRS = ItemBlockPair.register("dark_gray_chess_brick_stairs", () -> new StairBlock(() -> DARK_GRAY_CHESS_BRICKS.asBlock().defaultBlockState(), copy(DARK_GRAY_CHESS_BRICKS.asBlock())));
	
	public static final ItemBlockPair<Block, BlockItem> DARK_GRAY_CHESS_BRICK_SMOOTH = ItemBlockPair.register("dark_gray_chess_brick_smooth", () -> new Block(copy(DARK_GRAY_CHESS_BRICKS.asBlock())));
	public static final ItemBlockPair<WallBlock, BlockItem> DARK_GRAY_CHESS_BRICK_SMOOTH_WALL = ItemBlockPair.register("dark_gray_chess_brick_smooth_wall", () -> new WallBlock(copy(DARK_GRAY_CHESS_BRICK_SMOOTH.asBlock())));
	public static final ItemBlockPair<SlabBlock, BlockItem> DARK_GRAY_CHESS_BRICK_SMOOTH_SLAB = ItemBlockPair.register("dark_gray_chess_brick_smooth_slab", () -> new SlabBlock(copy(DARK_GRAY_CHESS_BRICK_SMOOTH.asBlock())));
	public static final ItemBlockPair<StairBlock, BlockItem> DARK_GRAY_CHESS_BRICK_SMOOTH_STAIRS = ItemBlockPair.register("dark_gray_chess_brick_smooth_stairs", () -> new StairBlock(() -> DARK_GRAY_CHESS_BRICK_SMOOTH.asBlock().defaultBlockState(), copy(DARK_GRAY_CHESS_BRICK_SMOOTH.asBlock())));
	public static final ItemBlockPair<PressurePlateBlock, BlockItem> DARK_GRAY_CHESS_BRICK_SMOOTH_PRESSURE_PLATE = ItemBlockPair.register("dark_gray_chess_brick_smooth_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(DARK_GRAY_CHESS_BRICKS.asBlock()), BlockSetType.STONE));
	public static final ItemBlockPair<ButtonBlock, BlockItem> DARK_GRAY_CHESS_BRICK_SMOOTH_BUTTON = ItemBlockPair.register("dark_gray_chess_brick_smooth_button", () -> new ButtonBlock(copy(DARK_GRAY_CHESS_BRICK_SMOOTH.asBlock()), BlockSetType.STONE, 10, true));
	
	
	public static final ItemBlockPair<Block, BlockItem> LIGHT_GRAY_CHESS_BRICKS = ItemBlockPair.register("light_gray_chess_bricks", () -> new Block(Block.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final ItemBlockPair<Block, BlockItem> LIGHT_GRAY_CHESS_BRICK_TRIM = ItemBlockPair.register("light_gray_chess_brick_trim", () -> new MSDirectionalBlock(copy(LIGHT_GRAY_CHESS_BRICKS.asBlock())));
	public static final ItemBlockPair<WallBlock, BlockItem> LIGHT_GRAY_CHESS_BRICK_WALL = ItemBlockPair.register("light_gray_chess_brick_wall", () -> new WallBlock(copy(LIGHT_GRAY_CHESS_BRICKS.asBlock())));
	public static final ItemBlockPair<SlabBlock, BlockItem> LIGHT_GRAY_CHESS_BRICK_SLAB = ItemBlockPair.register("light_gray_chess_brick_slab", () -> new SlabBlock(copy(LIGHT_GRAY_CHESS_BRICKS.asBlock())));
	public static final ItemBlockPair<StairBlock, BlockItem> LIGHT_GRAY_CHESS_BRICK_STAIRS = ItemBlockPair.register("light_gray_chess_brick_stairs", () -> new StairBlock(() -> LIGHT_GRAY_CHESS_BRICKS.asBlock().defaultBlockState(), copy(LIGHT_GRAY_CHESS_BRICKS.asBlock())));
	
	public static final ItemBlockPair<Block, BlockItem> LIGHT_GRAY_CHESS_BRICK_SMOOTH = ItemBlockPair.register("light_gray_chess_brick_smooth", () -> new Block(copy(LIGHT_GRAY_CHESS_BRICKS.asBlock())));
	public static final ItemBlockPair<WallBlock, BlockItem> LIGHT_GRAY_CHESS_BRICK_SMOOTH_WALL = ItemBlockPair.register("light_gray_chess_brick_smooth_wall", () -> new WallBlock(copy(LIGHT_GRAY_CHESS_BRICK_SMOOTH.asBlock())));
	public static final ItemBlockPair<SlabBlock, BlockItem> LIGHT_GRAY_CHESS_BRICK_SMOOTH_SLAB = ItemBlockPair.register("light_gray_chess_brick_smooth_slab", () -> new SlabBlock(copy(LIGHT_GRAY_CHESS_BRICK_SMOOTH.asBlock())));
	public static final ItemBlockPair<StairBlock, BlockItem> LIGHT_GRAY_CHESS_BRICK_SMOOTH_STAIRS = ItemBlockPair.register("light_gray_chess_brick_smooth_stairs", () -> new StairBlock(() -> LIGHT_GRAY_CHESS_BRICK_SMOOTH.asBlock().defaultBlockState(), copy(LIGHT_GRAY_CHESS_BRICK_SMOOTH.asBlock())));
	public static final ItemBlockPair<PressurePlateBlock, BlockItem> LIGHT_GRAY_CHESS_BRICK_SMOOTH_PRESSURE_PLATE = ItemBlockPair.register("light_gray_chess_brick_smooth_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(LIGHT_GRAY_CHESS_BRICK_SMOOTH.asBlock()), BlockSetType.STONE));
	public static final ItemBlockPair<ButtonBlock, BlockItem> LIGHT_GRAY_CHESS_BRICK_SMOOTH_BUTTON = ItemBlockPair.register("light_gray_chess_brick_smooth_button", () -> new ButtonBlock(copy(LIGHT_GRAY_CHESS_BRICK_SMOOTH.asBlock()), BlockSetType.STONE, 10, true));
	
	
	public static final ItemBlockPair<Block, BlockItem> WHITE_CHESS_BRICKS = ItemBlockPair.register("white_chess_bricks", () -> new Block(Block.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final ItemBlockPair<Block, BlockItem> WHITE_CHESS_BRICK_TRIM = ItemBlockPair.register("white_chess_brick_trim", () -> new MSDirectionalBlock(copy(WHITE_CHESS_BRICKS.asBlock())));
	public static final ItemBlockPair<WallBlock, BlockItem> WHITE_CHESS_BRICK_WALL = ItemBlockPair.register("white_chess_brick_wall", () -> new WallBlock(copy(WHITE_CHESS_BRICKS.asBlock())));
	public static final ItemBlockPair<SlabBlock, BlockItem> WHITE_CHESS_BRICK_SLAB = ItemBlockPair.register("white_chess_brick_slab", () -> new SlabBlock(copy(WHITE_CHESS_BRICKS.asBlock())));
	public static final ItemBlockPair<StairBlock, BlockItem> WHITE_CHESS_BRICK_STAIRS = ItemBlockPair.register("white_chess_brick_stairs", () -> new StairBlock(() -> WHITE_CHESS_BRICKS.asBlock().defaultBlockState(), copy(WHITE_CHESS_BRICKS.asBlock())));
	
	public static final ItemBlockPair<Block, BlockItem> WHITE_CHESS_BRICK_SMOOTH = ItemBlockPair.register("white_chess_brick_smooth", () -> new Block(copy(WHITE_CHESS_BRICKS.asBlock())));
	public static final ItemBlockPair<WallBlock, BlockItem> WHITE_CHESS_BRICK_SMOOTH_WALL = ItemBlockPair.register("white_chess_brick_smooth_wall", () -> new WallBlock(copy(WHITE_CHESS_BRICK_SMOOTH.asBlock())));
	public static final ItemBlockPair<SlabBlock, BlockItem> WHITE_CHESS_BRICK_SMOOTH_SLAB = ItemBlockPair.register("white_chess_brick_smooth_slab", () -> new SlabBlock(copy(WHITE_CHESS_BRICK_SMOOTH.asBlock())));
	public static final ItemBlockPair<StairBlock, BlockItem> WHITE_CHESS_BRICK_SMOOTH_STAIRS = ItemBlockPair.register("white_chess_brick_smooth_stairs", () -> new StairBlock(() -> WHITE_CHESS_BRICK_SMOOTH.asBlock().defaultBlockState(), copy(WHITE_CHESS_BRICK_SMOOTH.asBlock())));
	public static final ItemBlockPair<PressurePlateBlock, BlockItem> WHITE_CHESS_BRICK_SMOOTH_PRESSURE_PLATE = ItemBlockPair.register("white_chess_brick_smooth_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(WHITE_CHESS_BRICK_SMOOTH.asBlock()), BlockSetType.STONE));
	public static final ItemBlockPair<ButtonBlock, BlockItem> WHITE_CHESS_BRICK_SMOOTH_BUTTON = ItemBlockPair.register("white_chess_brick_smooth_button", () -> new ButtonBlock(copy(WHITE_CHESS_BRICK_SMOOTH.asBlock()), BlockSetType.STONE, 10, true));
	
	
	public static final ItemBlockPair<Block, BlockItem> CHECKERED_STAINED_GLASS = ItemBlockPair.register("checkered_stained_glass",
			() -> new StainedGlassBlock(DyeColor.BLUE, Block.Properties.of().mapColor(DyeColor.BLUE).instrument(NoteBlockInstrument.HAT).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn(MSBlocks::never).isRedstoneConductor(MSBlocks::never).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never)));
	public static final ItemBlockPair<Block, BlockItem> WHITE_PAWN_STAINED_GLASS = ItemBlockPair.register("white_pawn_stained_glass", () -> new StainedGlassBlock(DyeColor.BLUE, copy(CHECKERED_STAINED_GLASS.asBlock())));
	public static final ItemBlockPair<Block, BlockItem> WHITE_CROWN_STAINED_GLASS = ItemBlockPair.register("white_crown_stained_glass", () -> new StainedGlassBlock(DyeColor.BLUE, copy(CHECKERED_STAINED_GLASS.asBlock())));
	public static final ItemBlockPair<Block, BlockItem> BLACK_PAWN_STAINED_GLASS = ItemBlockPair.register("black_pawn_stained_glass", () -> new StainedGlassBlock(DyeColor.BLUE, copy(CHECKERED_STAINED_GLASS.asBlock())));
	public static final ItemBlockPair<Block, BlockItem> BLACK_CROWN_STAINED_GLASS = ItemBlockPair.register("black_crown_stained_glass", () -> new StainedGlassBlock(DyeColor.BLUE, copy(CHECKERED_STAINED_GLASS.asBlock())));
	
	public static void init()
	{
	}
}
