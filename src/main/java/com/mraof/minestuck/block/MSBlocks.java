package com.mraof.minestuck.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.fluid.MSLiquidBlock;
import com.mraof.minestuck.block.fluid.WaterColorsBlock;
import com.mraof.minestuck.block.machine.*;
import com.mraof.minestuck.block.plant.*;
import com.mraof.minestuck.block.redstone.*;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.fluid.MSFluids;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.CustomVoxelShape;
import com.mraof.minestuck.world.gen.feature.tree.ShadewoodTree;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.copy;

public class MSBlocks
{
	public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, Minestuck.MOD_ID);
	
	//Skaia
	public static final RegistryObject<Block> SKAIA_PORTAL = REGISTER.register("skaia_portal", () -> new SkaiaPortalBlock(Block.Properties.of().mapColor(MapColor.COLOR_CYAN).pushReaction(PushReaction.BLOCK).noCollission().lightLevel(state -> 11).strength(-1.0F, 3600000.0F).noLootTable()));
	
	public static final RegistryObject<Block> BLACK_CHESS_DIRT = REGISTER.register("black_chess_dirt", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_BLACK).strength(0.5F).sound(SoundType.GRAVEL)));
	public static final RegistryObject<Block> WHITE_CHESS_DIRT = REGISTER.register("white_chess_dirt", () -> new Block(Block.Properties.of().mapColor(MapColor.SNOW).strength(0.5F).sound(SoundType.GRAVEL)));
	public static final RegistryObject<Block> DARK_GRAY_CHESS_DIRT = REGISTER.register("dark_gray_chess_dirt", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(0.5F).sound(SoundType.GRAVEL)));
	public static final RegistryObject<Block> LIGHT_GRAY_CHESS_DIRT = REGISTER.register("light_gray_chess_dirt", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).strength(0.5F).sound(SoundType.GRAVEL)));
	
	//Black Chess Bricks
	public static final RegistryObject<Block> BLACK_CHESS_BRICKS = REGISTER.register("black_chess_bricks", () -> new Block(Block.Properties.of().mapColor(MapColor.TERRACOTTA_BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final RegistryObject<StairBlock> BLACK_CHESS_BRICK_STAIRS = REGISTER.register("black_chess_brick_stairs", () -> new StairBlock(() -> MSBlocks.BLACK_CHESS_BRICKS.get().defaultBlockState(), copy(BLACK_CHESS_BRICKS.get())));
	public static final RegistryObject<SlabBlock> BLACK_CHESS_BRICK_SLAB = REGISTER.register("black_chess_brick_slab", () -> new SlabBlock(copy(BLACK_CHESS_BRICKS.get())));
	public static final RegistryObject<WallBlock> BLACK_CHESS_BRICK_WALL = REGISTER.register("black_chess_brick_wall", () -> new WallBlock(copy(BLACK_CHESS_BRICKS.get())));
	
	public static final RegistryObject<Block> BLACK_CHESS_BRICK_SMOOTH = REGISTER.register("black_chess_brick_smooth", () -> new Block(copy(BLACK_CHESS_BRICKS.get())));
	public static final RegistryObject<StairBlock> BLACK_CHESS_BRICK_SMOOTH_STAIRS = REGISTER.register("black_chess_brick_smooth_stairs", () -> new StairBlock(() -> MSBlocks.BLACK_CHESS_BRICK_SMOOTH.get().defaultBlockState(), copy(BLACK_CHESS_BRICK_SMOOTH.get())));
	public static final RegistryObject<SlabBlock> BLACK_CHESS_BRICK_SMOOTH_SLAB = REGISTER.register("black_chess_brick_smooth_slab", () -> new SlabBlock(copy(BLACK_CHESS_BRICK_SMOOTH.get())));
	public static final RegistryObject<WallBlock> BLACK_CHESS_BRICK_SMOOTH_WALL = REGISTER.register("black_chess_brick_smooth_wall", () -> new WallBlock(copy(BLACK_CHESS_BRICK_SMOOTH.get())));
	public static final RegistryObject<ButtonBlock> BLACK_CHESS_BRICK_SMOOTH_BUTTON = REGISTER.register("black_chess_brick_smooth_button", () -> new ButtonBlock(copy(BLACK_CHESS_BRICK_SMOOTH.get()), BlockSetType.STONE, 10, true));
	public static final RegistryObject<PressurePlateBlock> BLACK_CHESS_BRICK_SMOOTH_PRESSURE_PLATE = REGISTER.register("black_chess_brick_smooth_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(BLACK_CHESS_BRICK_SMOOTH.get()), BlockSetType.STONE));
	
	public static final RegistryObject<Block> BLACK_CHESS_BRICK_TRIM = REGISTER.register("black_chess_brick_trim", () -> new MSDirectionalBlock(copy(BLACK_CHESS_BRICKS.get())));
	
	//Dark Gray Chess Bricks
	public static final RegistryObject<Block> DARK_GRAY_CHESS_BRICKS = REGISTER.register("dark_gray_chess_bricks", () -> new Block(Block.Properties.of().mapColor(MapColor.TERRACOTTA_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final RegistryObject<StairBlock> DARK_GRAY_CHESS_BRICK_STAIRS = REGISTER.register("dark_gray_chess_brick_stairs", () -> new StairBlock(() -> MSBlocks.DARK_GRAY_CHESS_BRICKS.get().defaultBlockState(), copy(DARK_GRAY_CHESS_BRICKS.get())));
	public static final RegistryObject<SlabBlock> DARK_GRAY_CHESS_BRICK_SLAB = REGISTER.register("dark_gray_chess_brick_slab", () -> new SlabBlock(copy(DARK_GRAY_CHESS_BRICKS.get())));
	public static final RegistryObject<WallBlock> DARK_GRAY_CHESS_BRICK_WALL = REGISTER.register("dark_gray_chess_brick_wall", () -> new WallBlock(copy(DARK_GRAY_CHESS_BRICKS.get())));
	
	public static final RegistryObject<Block> DARK_GRAY_CHESS_BRICK_SMOOTH = REGISTER.register("dark_gray_chess_brick_smooth", () -> new Block(copy(DARK_GRAY_CHESS_BRICKS.get())));
	public static final RegistryObject<StairBlock> DARK_GRAY_CHESS_BRICK_SMOOTH_STAIRS = REGISTER.register("dark_gray_chess_brick_smooth_stairs", () -> new StairBlock(() -> MSBlocks.DARK_GRAY_CHESS_BRICK_SMOOTH.get().defaultBlockState(), copy(DARK_GRAY_CHESS_BRICK_SMOOTH.get())));
	public static final RegistryObject<SlabBlock> DARK_GRAY_CHESS_BRICK_SMOOTH_SLAB = REGISTER.register("dark_gray_chess_brick_smooth_slab", () -> new SlabBlock(copy(DARK_GRAY_CHESS_BRICK_SMOOTH.get())));
	public static final RegistryObject<WallBlock> DARK_GRAY_CHESS_BRICK_SMOOTH_WALL = REGISTER.register("dark_gray_chess_brick_smooth_wall", () -> new WallBlock(copy(DARK_GRAY_CHESS_BRICK_SMOOTH.get())));
	public static final RegistryObject<ButtonBlock> DARK_GRAY_CHESS_BRICK_SMOOTH_BUTTON = REGISTER.register("dark_gray_chess_brick_smooth_button", () -> new ButtonBlock(copy(DARK_GRAY_CHESS_BRICK_SMOOTH.get()), BlockSetType.STONE, 10, true));
	public static final RegistryObject<PressurePlateBlock> DARK_GRAY_CHESS_BRICK_SMOOTH_PRESSURE_PLATE = REGISTER.register("dark_gray_chess_brick_smooth_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(DARK_GRAY_CHESS_BRICKS.get()), BlockSetType.STONE));
	
	public static final RegistryObject<Block> DARK_GRAY_CHESS_BRICK_TRIM = REGISTER.register("dark_gray_chess_brick_trim", () -> new MSDirectionalBlock(copy(DARK_GRAY_CHESS_BRICKS.get())));
	
	//Light Gray Chess Bricks
	public static final RegistryObject<Block> LIGHT_GRAY_CHESS_BRICKS = REGISTER.register("light_gray_chess_bricks", () -> new Block(Block.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final RegistryObject<StairBlock> LIGHT_GRAY_CHESS_BRICK_STAIRS = REGISTER.register("light_gray_chess_brick_stairs", () -> new StairBlock(() -> MSBlocks.LIGHT_GRAY_CHESS_BRICKS.get().defaultBlockState(), copy(LIGHT_GRAY_CHESS_BRICKS.get())));
	public static final RegistryObject<SlabBlock> LIGHT_GRAY_CHESS_BRICK_SLAB = REGISTER.register("light_gray_chess_brick_slab", () -> new SlabBlock(copy(LIGHT_GRAY_CHESS_BRICKS.get())));
	public static final RegistryObject<WallBlock> LIGHT_GRAY_CHESS_BRICK_WALL = REGISTER.register("light_gray_chess_brick_wall", () -> new WallBlock(copy(LIGHT_GRAY_CHESS_BRICKS.get())));
	
	public static final RegistryObject<Block> LIGHT_GRAY_CHESS_BRICK_SMOOTH = REGISTER.register("light_gray_chess_brick_smooth", () -> new Block(copy(LIGHT_GRAY_CHESS_BRICKS.get())));
	public static final RegistryObject<StairBlock> LIGHT_GRAY_CHESS_BRICK_SMOOTH_STAIRS = REGISTER.register("light_gray_chess_brick_smooth_stairs", () -> new StairBlock(() -> MSBlocks.LIGHT_GRAY_CHESS_BRICK_SMOOTH.get().defaultBlockState(), copy(LIGHT_GRAY_CHESS_BRICK_SMOOTH.get())));
	public static final RegistryObject<SlabBlock> LIGHT_GRAY_CHESS_BRICK_SMOOTH_SLAB = REGISTER.register("light_gray_chess_brick_smooth_slab", () -> new SlabBlock(copy(LIGHT_GRAY_CHESS_BRICK_SMOOTH.get())));
	public static final RegistryObject<WallBlock> LIGHT_GRAY_CHESS_BRICK_SMOOTH_WALL = REGISTER.register("light_gray_chess_brick_smooth_wall", () -> new WallBlock(copy(LIGHT_GRAY_CHESS_BRICK_SMOOTH.get())));
	public static final RegistryObject<ButtonBlock> LIGHT_GRAY_CHESS_BRICK_SMOOTH_BUTTON = REGISTER.register("light_gray_chess_brick_smooth_button", () -> new ButtonBlock(copy(LIGHT_GRAY_CHESS_BRICK_SMOOTH.get()), BlockSetType.STONE, 10, true));
	public static final RegistryObject<PressurePlateBlock> LIGHT_GRAY_CHESS_BRICK_SMOOTH_PRESSURE_PLATE = REGISTER.register("light_gray_chess_brick_smooth_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(LIGHT_GRAY_CHESS_BRICK_SMOOTH.get()), BlockSetType.STONE));
	
	public static final RegistryObject<Block> LIGHT_GRAY_CHESS_BRICK_TRIM = REGISTER.register("light_gray_chess_brick_trim", () -> new MSDirectionalBlock(copy(LIGHT_GRAY_CHESS_BRICKS.get())));
	
	//White Chess Bricks
	public static final RegistryObject<Block> WHITE_CHESS_BRICKS = REGISTER.register("white_chess_bricks", () -> new Block(Block.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final RegistryObject<StairBlock> WHITE_CHESS_BRICK_STAIRS = REGISTER.register("white_chess_brick_stairs", () -> new StairBlock(() -> MSBlocks.WHITE_CHESS_BRICKS.get().defaultBlockState(), copy(WHITE_CHESS_BRICKS.get())));
	public static final RegistryObject<SlabBlock> WHITE_CHESS_BRICK_SLAB = REGISTER.register("white_chess_brick_slab", () -> new SlabBlock(copy(WHITE_CHESS_BRICKS.get())));
	public static final RegistryObject<WallBlock> WHITE_CHESS_BRICK_WALL = REGISTER.register("white_chess_brick_wall", () -> new WallBlock(copy(WHITE_CHESS_BRICKS.get())));
	
	public static final RegistryObject<Block> WHITE_CHESS_BRICK_SMOOTH = REGISTER.register("white_chess_brick_smooth", () -> new Block(copy(WHITE_CHESS_BRICKS.get())));
	public static final RegistryObject<StairBlock> WHITE_CHESS_BRICK_SMOOTH_STAIRS = REGISTER.register("white_chess_brick_smooth_stairs", () -> new StairBlock(() -> MSBlocks.WHITE_CHESS_BRICK_SMOOTH.get().defaultBlockState(), copy(WHITE_CHESS_BRICK_SMOOTH.get())));
	public static final RegistryObject<SlabBlock> WHITE_CHESS_BRICK_SMOOTH_SLAB = REGISTER.register("white_chess_brick_smooth_slab", () -> new SlabBlock(copy(WHITE_CHESS_BRICK_SMOOTH.get())));
	public static final RegistryObject<WallBlock> WHITE_CHESS_BRICK_SMOOTH_WALL = REGISTER.register("white_chess_brick_smooth_wall", () -> new WallBlock(copy(WHITE_CHESS_BRICK_SMOOTH.get())));
	public static final RegistryObject<ButtonBlock> WHITE_CHESS_BRICK_SMOOTH_BUTTON = REGISTER.register("white_chess_brick_smooth_button", () -> new ButtonBlock(copy(WHITE_CHESS_BRICK_SMOOTH.get()), BlockSetType.STONE, 10, true));
	public static final RegistryObject<PressurePlateBlock> WHITE_CHESS_BRICK_SMOOTH_PRESSURE_PLATE = REGISTER.register("white_chess_brick_smooth_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(WHITE_CHESS_BRICK_SMOOTH.get()), BlockSetType.STONE));
	
	public static final RegistryObject<Block> WHITE_CHESS_BRICK_TRIM = REGISTER.register("white_chess_brick_trim", () -> new MSDirectionalBlock(copy(WHITE_CHESS_BRICKS.get())));
	
	//Stained Glass
	public static final RegistryObject<Block> CHECKERED_STAINED_GLASS = REGISTER.register("checkered_stained_glass", () -> new StainedGlassBlock(DyeColor.BLUE, Block.Properties.of().mapColor(DyeColor.BLUE).instrument(NoteBlockInstrument.HAT).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn(MSBlocks::never).isRedstoneConductor(MSBlocks::never).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never)));
	public static final RegistryObject<Block> BLACK_CROWN_STAINED_GLASS = REGISTER.register("black_crown_stained_glass", () -> new StainedGlassBlock(DyeColor.BLUE, copy(CHECKERED_STAINED_GLASS.get())));
	public static final RegistryObject<Block> BLACK_PAWN_STAINED_GLASS = REGISTER.register("black_pawn_stained_glass", () -> new StainedGlassBlock(DyeColor.BLUE, copy(CHECKERED_STAINED_GLASS.get())));
	public static final RegistryObject<Block> WHITE_CROWN_STAINED_GLASS = REGISTER.register("white_crown_stained_glass", () -> new StainedGlassBlock(DyeColor.BLUE, copy(CHECKERED_STAINED_GLASS.get())));
	public static final RegistryObject<Block> WHITE_PAWN_STAINED_GLASS = REGISTER.register("white_pawn_stained_glass", () -> new StainedGlassBlock(DyeColor.BLUE, copy(CHECKERED_STAINED_GLASS.get())));
	
	//Cruxite ores
	public static final RegistryObject<Block> STONE_CRUXITE_ORE = REGISTER.register("stone_cruxite_ore", () -> cruxiteOre(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 3.0F).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> NETHERRACK_CRUXITE_ORE = REGISTER.register("netherrack_cruxite_ore", () -> cruxiteOre(copy(STONE_CRUXITE_ORE.get())));
	public static final RegistryObject<Block> COBBLESTONE_CRUXITE_ORE = REGISTER.register("cobblestone_cruxite_ore", () -> cruxiteOre(copy(STONE_CRUXITE_ORE.get())));
	public static final RegistryObject<Block> SANDSTONE_CRUXITE_ORE = REGISTER.register("sandstone_cruxite_ore", () -> cruxiteOre(copy(STONE_CRUXITE_ORE.get())));
	public static final RegistryObject<Block> RED_SANDSTONE_CRUXITE_ORE = REGISTER.register("red_sandstone_cruxite_ore", () -> cruxiteOre(copy(STONE_CRUXITE_ORE.get())));
	public static final RegistryObject<Block> END_STONE_CRUXITE_ORE = REGISTER.register("end_stone_cruxite_ore", () -> cruxiteOre(copy(STONE_CRUXITE_ORE.get())));
	public static final RegistryObject<Block> SHADE_STONE_CRUXITE_ORE = REGISTER.register("shade_stone_cruxite_ore", () -> cruxiteOre(copy(STONE_CRUXITE_ORE.get())));
	public static final RegistryObject<Block> PINK_STONE_CRUXITE_ORE = REGISTER.register("pink_stone_cruxite_ore", () -> cruxiteOre(copy(STONE_CRUXITE_ORE.get())));
	public static final RegistryObject<Block> MYCELIUM_STONE_CRUXITE_ORE = REGISTER.register("mycelium_stone_cruxite_ore", () -> cruxiteOre(copy(STONE_CRUXITE_ORE.get())));
	
	private static Block cruxiteOre(BlockBehaviour.Properties properties)
	{
		return new DropExperienceBlock(properties, UniformInt.of(2, 5));
	}
	
	//Uranium ores
	public static final RegistryObject<Block> STONE_URANIUM_ORE = REGISTER.register("stone_uranium_ore", () -> uraniumOre(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 3.0F).requiresCorrectToolForDrops().lightLevel(state -> 3)));
	public static final RegistryObject<Block> DEEPSLATE_URANIUM_ORE = REGISTER.register("deepslate_uranium_ore", () -> uraniumOre(copy(STONE_CRUXITE_ORE.get()).mapColor(MapColor.DEEPSLATE).strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE)));
	public static final RegistryObject<Block> NETHERRACK_URANIUM_ORE = REGISTER.register("netherrack_uranium_ore", () -> uraniumOre(copy(STONE_CRUXITE_ORE.get())));
	public static final RegistryObject<Block> COBBLESTONE_URANIUM_ORE = REGISTER.register("cobblestone_uranium_ore", () -> uraniumOre(copy(STONE_CRUXITE_ORE.get())));
	public static final RegistryObject<Block> SANDSTONE_URANIUM_ORE = REGISTER.register("sandstone_uranium_ore", () -> uraniumOre(copy(STONE_CRUXITE_ORE.get())));
	public static final RegistryObject<Block> RED_SANDSTONE_URANIUM_ORE = REGISTER.register("red_sandstone_uranium_ore", () -> uraniumOre(copy(STONE_CRUXITE_ORE.get())));
	public static final RegistryObject<Block> END_STONE_URANIUM_ORE = REGISTER.register("end_stone_uranium_ore", () -> uraniumOre(copy(STONE_CRUXITE_ORE.get())));
	public static final RegistryObject<Block> SHADE_STONE_URANIUM_ORE = REGISTER.register("shade_stone_uranium_ore", () -> uraniumOre(copy(STONE_CRUXITE_ORE.get())));
	public static final RegistryObject<Block> PINK_STONE_URANIUM_ORE = REGISTER.register("pink_stone_uranium_ore", () -> uraniumOre(copy(STONE_CRUXITE_ORE.get())));
	public static final RegistryObject<Block> MYCELIUM_STONE_URANIUM_ORE = REGISTER.register("mycelium_stone_uranium_ore", () -> uraniumOre(copy(STONE_CRUXITE_ORE.get())));
	
	private static Block uraniumOre(BlockBehaviour.Properties properties)
	{
		return new DropExperienceBlock(properties, UniformInt.of(2, 5));
	}
	
	//Land-specific vanilla ores
	public static final RegistryObject<Block> NETHERRACK_COAL_ORE = REGISTER.register("netherrack_coal_ore", () -> coalOre(copy(Blocks.COAL_ORE)));
	public static final RegistryObject<Block> SHADE_STONE_COAL_ORE = REGISTER.register("shade_stone_coal_ore", () -> coalOre(copy(Blocks.COAL_ORE)));
	public static final RegistryObject<Block> PINK_STONE_COAL_ORE = REGISTER.register("pink_stone_coal_ore", () -> coalOre(copy(Blocks.COAL_ORE)));
	
	private static Block coalOre(BlockBehaviour.Properties properties)
	{
		return new DropExperienceBlock(properties, UniformInt.of(0, 2));
	}
	
	public static final RegistryObject<Block> END_STONE_IRON_ORE = REGISTER.register("end_stone_iron_ore", () -> new DropExperienceBlock(copy(Blocks.IRON_ORE)));
	public static final RegistryObject<Block> SANDSTONE_IRON_ORE = REGISTER.register("sandstone_iron_ore", () -> new DropExperienceBlock(copy(Blocks.IRON_ORE)));
	public static final RegistryObject<Block> RED_SANDSTONE_IRON_ORE = REGISTER.register("red_sandstone_iron_ore", () -> new DropExperienceBlock(copy(Blocks.IRON_ORE)));
	
	public static final RegistryObject<Block> SANDSTONE_GOLD_ORE = REGISTER.register("sandstone_gold_ore", () -> new DropExperienceBlock(copy(Blocks.GOLD_ORE)));
	public static final RegistryObject<Block> RED_SANDSTONE_GOLD_ORE = REGISTER.register("red_sandstone_gold_ore", () -> new DropExperienceBlock(copy(Blocks.GOLD_ORE)));
	public static final RegistryObject<Block> SHADE_STONE_GOLD_ORE = REGISTER.register("shade_stone_gold_ore", () -> new DropExperienceBlock(copy(Blocks.GOLD_ORE)));
	public static final RegistryObject<Block> PINK_STONE_GOLD_ORE = REGISTER.register("pink_stone_gold_ore", () -> new DropExperienceBlock(copy(Blocks.GOLD_ORE)));
	
	public static final RegistryObject<Block> END_STONE_REDSTONE_ORE = REGISTER.register("end_stone_redstone_ore", () -> new RedStoneOreBlock(copy(Blocks.REDSTONE_ORE)));
	public static final RegistryObject<Block> STONE_QUARTZ_ORE = REGISTER.register("stone_quartz_ore", () -> new DropExperienceBlock(copy(Blocks.NETHER_QUARTZ_ORE), UniformInt.of(2, 5)));
	public static final RegistryObject<Block> PINK_STONE_LAPIS_ORE = REGISTER.register("pink_stone_lapis_ore", () -> new DropExperienceBlock(copy(Blocks.LAPIS_ORE), UniformInt.of(2, 5)));
	public static final RegistryObject<Block> PINK_STONE_DIAMOND_ORE = REGISTER.register("pink_stone_diamond_ore", () -> new DropExperienceBlock(copy(Blocks.DIAMOND_ORE), UniformInt.of(3, 7)));
	
	//Resource Blocks
	public static final RegistryObject<Block> CRUXITE_BLOCK = REGISTER.register("cruxite_block", () -> new Block(Block.Properties.of().mapColor(DyeColor.LIGHT_BLUE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F).requiresCorrectToolForDrops()));
	public static final RegistryObject<StairBlock> CRUXITE_STAIRS = REGISTER.register("cruxite_stairs", () -> new StairBlock(() -> MSBlocks.CRUXITE_BLOCK.get().defaultBlockState(), copy(CRUXITE_BLOCK.get())));
	public static final RegistryObject<SlabBlock> CRUXITE_SLAB = REGISTER.register("cruxite_slab", () -> new SlabBlock(copy(CRUXITE_BLOCK.get())));
	public static final RegistryObject<WallBlock> CRUXITE_WALL = REGISTER.register("cruxite_wall", () -> new WallBlock(copy(CRUXITE_BLOCK.get())));
	public static final RegistryObject<ButtonBlock> CRUXITE_BUTTON = REGISTER.register("cruxite_button", () -> new ButtonBlock(copy(CRUXITE_BLOCK.get()), BlockSetType.STONE, 10, true));
	public static final RegistryObject<PressurePlateBlock> CRUXITE_PRESSURE_PLATE = REGISTER.register("cruxite_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(CRUXITE_BLOCK.get()), BlockSetType.OAK));
	
	public static final RegistryObject<Block> URANIUM_BLOCK = REGISTER.register("uranium_block", () -> new Block(Block.Properties.of().mapColor(DyeColor.LIME).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F).requiresCorrectToolForDrops().lightLevel(state -> 7)));
	public static final RegistryObject<StairBlock> URANIUM_STAIRS = REGISTER.register("uranium_stairs", () -> new StairBlock(() -> MSBlocks.URANIUM_BLOCK.get().defaultBlockState(), copy(URANIUM_BLOCK.get())));
	public static final RegistryObject<SlabBlock> URANIUM_SLAB = REGISTER.register("uranium_slab", () -> new SlabBlock(copy(URANIUM_BLOCK.get())));
	public static final RegistryObject<WallBlock> URANIUM_WALL = REGISTER.register("uranium_wall", () -> new WallBlock(copy(URANIUM_BLOCK.get())));
	public static final RegistryObject<ButtonBlock> URANIUM_BUTTON = REGISTER.register("uranium_button", () -> new ButtonBlock(copy(URANIUM_BLOCK.get()), BlockSetType.STONE, 10, true));
	public static final RegistryObject<PressurePlateBlock> URANIUM_PRESSURE_PLATE = REGISTER.register("uranium_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(URANIUM_BLOCK.get()), BlockSetType.OAK));
	
	public static final RegistryObject<Block> GENERIC_OBJECT = REGISTER.register("generic_object", () -> new Block(Block.Properties.of().mapColor(DyeColor.LIME).strength(1.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<StairBlock> PERFECTLY_GENERIC_STAIRS = REGISTER.register("perfectly_generic_stairs", () -> new StairBlock(() -> MSBlocks.GENERIC_OBJECT.get().defaultBlockState(), copy(GENERIC_OBJECT.get())));
	public static final RegistryObject<SlabBlock> PERFECTLY_GENERIC_SLAB = REGISTER.register("perfectly_generic_slab", () -> new SlabBlock(copy(GENERIC_OBJECT.get())));
	public static final RegistryObject<WallBlock> PERFECTLY_GENERIC_WALL = REGISTER.register("perfectly_generic_wall", () -> new WallBlock(copy(GENERIC_OBJECT.get())));
	public static final RegistryObject<FenceBlock> PERFECTLY_GENERIC_FENCE = REGISTER.register("perfectly_generic_fence", () -> new FenceBlock(copy(GENERIC_OBJECT.get())));
	public static final RegistryObject<FenceGateBlock> PERFECTLY_GENERIC_FENCE_GATE = REGISTER.register("perfectly_generic_fence_gate", () -> new FenceGateBlock(copy(GENERIC_OBJECT.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final RegistryObject<ButtonBlock> PERFECTLY_GENERIC_BUTTON = REGISTER.register("perfectly_generic_button", () -> new ButtonBlock(copy(GENERIC_OBJECT.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<PressurePlateBlock> PERFECTLY_GENERIC_PRESSURE_PLATE = REGISTER.register("perfectly_generic_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(GENERIC_OBJECT.get()), BlockSetType.OAK));
	public static final RegistryObject<DoorBlock> PERFECTLY_GENERIC_DOOR = REGISTER.register("perfectly_generic_door", () -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<TrapDoorBlock> PERFECTLY_GENERIC_TRAPDOOR = REGISTER.register("perfectly_generic_trapdoor", () -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	
	//Land Environment
	//Aspect Terrain Dirt
	public static final RegistryObject<Block> BLUE_DIRT = REGISTER.register("blue_dirt", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_BLUE).strength(0.5F).sound(SoundType.GRAVEL)));
	public static final RegistryObject<Block> THOUGHT_DIRT = REGISTER.register("thought_dirt", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN).strength(0.5F).sound(SoundType.GRAVEL)));
	
	//Coarse
	public static final RegistryObject<Block> COARSE_STONE = REGISTER.register("coarse_stone", () -> new Block(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0F, 6.0F)));
	public static final RegistryObject<StairBlock> COARSE_STONE_STAIRS = REGISTER.register("coarse_stone_stairs", () -> new StairBlock(() -> MSBlocks.COARSE_STONE.get().defaultBlockState(), copy(COARSE_STONE.get())));
	public static final RegistryObject<SlabBlock> COARSE_STONE_SLAB = REGISTER.register("coarse_stone_slab", () -> new SlabBlock(copy(COARSE_STONE.get())));
	public static final RegistryObject<WallBlock> COARSE_STONE_WALL = REGISTER.register("coarse_stone_wall", () -> new WallBlock(copy(COARSE_STONE.get())));
	public static final RegistryObject<ButtonBlock> COARSE_STONE_BUTTON = REGISTER.register("coarse_stone_button", () -> new ButtonBlock(copy(COARSE_STONE.get()), BlockSetType.STONE, 10, true));
	public static final RegistryObject<PressurePlateBlock> COARSE_STONE_PRESSURE_PLATE = REGISTER.register("coarse_stone_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(COARSE_STONE.get()), BlockSetType.STONE));
	
	public static final RegistryObject<Block> COARSE_STONE_BRICKS = REGISTER.register("coarse_stone_bricks", () -> new Block(copy(COARSE_STONE.get())));
	public static final RegistryObject<StairBlock> COARSE_STONE_BRICK_STAIRS = REGISTER.register("coarse_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.COARSE_STONE_BRICKS.get().defaultBlockState(), copy(COARSE_STONE_BRICKS.get())));
	public static final RegistryObject<SlabBlock> COARSE_STONE_BRICK_SLAB = REGISTER.register("coarse_stone_brick_slab", () -> new SlabBlock(copy(COARSE_STONE_BRICKS.get())));
	public static final RegistryObject<WallBlock> COARSE_STONE_BRICK_WALL = REGISTER.register("coarse_stone_brick_wall", () -> new WallBlock(copy(COARSE_STONE_BRICKS.get())));
	
	public static final RegistryObject<Block> COARSE_STONE_COLUMN = REGISTER.register("coarse_stone_column", () -> new MSDirectionalBlock(copy(COARSE_STONE.get())));
	public static final RegistryObject<Block> CHISELED_COARSE_STONE_BRICKS = REGISTER.register("chiseled_coarse_stone_bricks", () -> new Block(copy(COARSE_STONE.get())));
	public static final RegistryObject<Block> CRACKED_COARSE_STONE_BRICKS = REGISTER.register("cracked_coarse_stone_bricks", () -> new Block(copy(COARSE_STONE.get())));
	public static final RegistryObject<Block> MOSSY_COARSE_STONE_BRICKS = REGISTER.register("mossy_coarse_stone_bricks", () -> new Block(copy(COARSE_STONE.get())));
	public static final RegistryObject<Block> CHISELED_COARSE_STONE = REGISTER.register("chiseled_coarse_stone", () -> new Block(copy(COARSE_STONE.get())));
	
	//Shade
	public static final RegistryObject<Block> SHADE_STONE = REGISTER.register("shade_stone", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_BLUE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final RegistryObject<StairBlock> SHADE_STAIRS = REGISTER.register("shade_stairs", () -> new StairBlock(() -> MSBlocks.SHADE_STONE.get().defaultBlockState(), copy(SHADE_STONE.get())));
	public static final RegistryObject<SlabBlock> SHADE_SLAB = REGISTER.register("shade_slab", () -> new SlabBlock(copy(SHADE_STONE.get())));
	public static final RegistryObject<WallBlock> SHADE_WALL = REGISTER.register("shade_wall", () -> new WallBlock(copy(SHADE_STONE.get())));
	public static final RegistryObject<ButtonBlock> SHADE_BUTTON = REGISTER.register("shade_button", () -> new ButtonBlock(copy(SHADE_STONE.get()), BlockSetType.STONE, 10, true));
	public static final RegistryObject<PressurePlateBlock> SHADE_PRESSURE_PLATE = REGISTER.register("shade_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(SHADE_STONE.get()), BlockSetType.STONE));
	
	public static final RegistryObject<Block> SHADE_BRICKS = REGISTER.register("shade_bricks", () -> new Block(copy(SHADE_STONE.get())));
	public static final RegistryObject<StairBlock> SHADE_BRICK_STAIRS = REGISTER.register("shade_brick_stairs", () -> new StairBlock(() -> MSBlocks.SHADE_BRICKS.get().defaultBlockState(), copy(SHADE_BRICKS.get())));
	public static final RegistryObject<SlabBlock> SHADE_BRICK_SLAB = REGISTER.register("shade_brick_slab", () -> new SlabBlock(copy(SHADE_BRICKS.get())));
	public static final RegistryObject<WallBlock> SHADE_BRICK_WALL = REGISTER.register("shade_brick_wall", () -> new WallBlock(copy(SHADE_BRICKS.get())));
	
	public static final RegistryObject<Block> SMOOTH_SHADE_STONE = REGISTER.register("smooth_shade_stone", () -> new Block(copy(SHADE_STONE.get())));
	public static final RegistryObject<StairBlock> SMOOTH_SHADE_STONE_STAIRS = REGISTER.register("smooth_shade_stone_stairs", () -> new StairBlock(() -> MSBlocks.SMOOTH_SHADE_STONE.get().defaultBlockState(), copy(SMOOTH_SHADE_STONE.get())));
	public static final RegistryObject<SlabBlock> SMOOTH_SHADE_STONE_SLAB = REGISTER.register("smooth_shade_stone_slab", () -> new SlabBlock(copy(SMOOTH_SHADE_STONE.get())));
	public static final RegistryObject<WallBlock> SMOOTH_SHADE_STONE_WALL = REGISTER.register("smooth_shade_stone_wall", () -> new WallBlock(copy(SMOOTH_SHADE_STONE.get())));
	
	public static final RegistryObject<Block> SHADE_COLUMN = REGISTER.register("shade_column", () -> new MSDirectionalBlock(copy(SHADE_STONE.get())));
	public static final RegistryObject<Block> CHISELED_SHADE_BRICKS = REGISTER.register("chiseled_shade_bricks", () -> new Block(copy(SHADE_BRICKS.get())));
	public static final RegistryObject<Block> CRACKED_SHADE_BRICKS = REGISTER.register("cracked_shade_bricks", () -> new Block(copy(SHADE_BRICKS.get())));
	
	public static final RegistryObject<Block> MOSSY_SHADE_BRICKS = REGISTER.register("mossy_shade_bricks", () -> new Block(copy(SHADE_BRICKS.get())));
	public static final RegistryObject<StairBlock> MOSSY_SHADE_BRICK_STAIRS = REGISTER.register("mossy_shade_brick_stairs", () -> new StairBlock(() -> MSBlocks.MOSSY_SHADE_BRICKS.get().defaultBlockState(), copy(MOSSY_SHADE_BRICKS.get())));
	public static final RegistryObject<SlabBlock> MOSSY_SHADE_BRICK_SLAB = REGISTER.register("mossy_shade_brick_slab", () -> new SlabBlock(copy(MOSSY_SHADE_BRICKS.get())));
	public static final RegistryObject<WallBlock> MOSSY_SHADE_BRICK_WALL = REGISTER.register("mossy_shade_brick_wall", () -> new WallBlock(copy(MOSSY_SHADE_BRICKS.get())));
	
	public static final RegistryObject<Block> BLOOD_SHADE_BRICKS = REGISTER.register("blood_shade_bricks", () -> new Block(copy(SHADE_BRICKS.get())));
	public static final RegistryObject<StairBlock> BLOOD_SHADE_BRICK_STAIRS = REGISTER.register("blood_shade_brick_stairs", () -> new StairBlock(() -> MSBlocks.BLOOD_SHADE_BRICKS.get().defaultBlockState(), copy(BLOOD_SHADE_BRICKS.get())));
	public static final RegistryObject<SlabBlock> BLOOD_SHADE_BRICK_SLAB = REGISTER.register("blood_shade_brick_slab", () -> new SlabBlock(copy(BLOOD_SHADE_BRICKS.get())));
	public static final RegistryObject<WallBlock> BLOOD_SHADE_BRICK_WALL = REGISTER.register("blood_shade_brick_wall", () -> new WallBlock(copy(BLOOD_SHADE_BRICKS.get())));
	
	public static final RegistryObject<Block> TAR_SHADE_BRICKS = REGISTER.register("tar_shade_bricks", () -> new Block(copy(SHADE_BRICKS.get())));
	public static final RegistryObject<StairBlock> TAR_SHADE_BRICK_STAIRS = REGISTER.register("tar_shade_brick_stairs", () -> new StairBlock(() -> MSBlocks.TAR_SHADE_BRICKS.get().defaultBlockState(), copy(TAR_SHADE_BRICKS.get())));
	public static final RegistryObject<SlabBlock> TAR_SHADE_BRICK_SLAB = REGISTER.register("tar_shade_brick_slab", () -> new SlabBlock(copy(TAR_SHADE_BRICKS.get())));
	public static final RegistryObject<WallBlock> TAR_SHADE_BRICK_WALL = REGISTER.register("tar_shade_brick_wall", () -> new WallBlock(copy(TAR_SHADE_BRICKS.get())));
	
	//Frost
	public static final RegistryObject<Block> FROST_TILE = REGISTER.register("frost_tile", () -> new Block(Block.Properties.of().mapColor(MapColor.ICE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final RegistryObject<StairBlock> FROST_TILE_STAIRS = REGISTER.register("frost_tile_stairs", () -> new StairBlock(() -> MSBlocks.FROST_TILE.get().defaultBlockState(), copy(FROST_TILE.get())));
	public static final RegistryObject<SlabBlock> FROST_TILE_SLAB = REGISTER.register("frost_tile_slab", () -> new SlabBlock(copy(FROST_TILE.get())));
	public static final RegistryObject<WallBlock> FROST_TILE_WALL = REGISTER.register("frost_tile_wall", () -> new WallBlock(copy(FROST_TILE.get())));
	
	public static final RegistryObject<Block> FROST_BRICKS = REGISTER.register("frost_bricks", () -> new Block(copy(FROST_TILE.get())));
	public static final RegistryObject<StairBlock> FROST_BRICK_STAIRS = REGISTER.register("frost_brick_stairs", () -> new StairBlock(() -> MSBlocks.FROST_BRICKS.get().defaultBlockState(), copy(FROST_BRICKS.get())));
	public static final RegistryObject<SlabBlock> FROST_BRICK_SLAB = REGISTER.register("frost_brick_slab", () -> new SlabBlock(copy(FROST_BRICKS.get())));
	public static final RegistryObject<WallBlock> FROST_BRICK_WALL = REGISTER.register("frost_brick_wall", () -> new WallBlock(copy(FROST_BRICKS.get())));
	
	public static final RegistryObject<Block> CHISELED_FROST_TILE = REGISTER.register("chiseled_frost_tile", () -> new Block(copy(FROST_TILE.get())));
	public static final RegistryObject<Block> FROST_COLUMN = REGISTER.register("frost_column", () -> new MSDirectionalBlock(copy(FROST_TILE.get())));
	public static final RegistryObject<Block> CHISELED_FROST_BRICKS = REGISTER.register("chiseled_frost_bricks", () -> new Block(copy(FROST_BRICKS.get()))); //while it is a pillar block, it cannot be rotated, making it similar to cut sandstone
	public static final RegistryObject<Block> CRACKED_FROST_BRICKS = REGISTER.register("cracked_frost_bricks", () -> new Block(copy(FROST_BRICKS.get())));
	
	public static final RegistryObject<Block> FLOWERY_FROST_BRICKS = REGISTER.register("flowery_frost_bricks", () -> new Block(copy(FROST_BRICKS.get())));
	public static final RegistryObject<StairBlock> FLOWERY_FROST_BRICK_STAIRS = REGISTER.register("flowery_frost_brick_stairs", () -> new StairBlock(() -> MSBlocks.FLOWERY_FROST_BRICKS.get().defaultBlockState(), copy(FLOWERY_FROST_BRICKS.get())));
	public static final RegistryObject<SlabBlock> FLOWERY_FROST_BRICK_SLAB = REGISTER.register("flowery_frost_brick_slab", () -> new SlabBlock(copy(FLOWERY_FROST_BRICKS.get())));
	public static final RegistryObject<WallBlock> FLOWERY_FROST_BRICK_WALL = REGISTER.register("flowery_frost_brick_wall", () -> new WallBlock(copy(FLOWERY_FROST_BRICKS.get())));
	
	
	//Cast Iron
	public static final RegistryObject<Block> CAST_IRON = REGISTER.register("cast_iron", () -> new Block(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3.0F, 9.0F)));
	public static final RegistryObject<StairBlock> CAST_IRON_STAIRS = REGISTER.register("cast_iron_stairs", () -> new StairBlock(() -> MSBlocks.CAST_IRON.get().defaultBlockState(), copy(CAST_IRON.get())));
	public static final RegistryObject<SlabBlock> CAST_IRON_SLAB = REGISTER.register("cast_iron_slab", () -> new SlabBlock(copy(CAST_IRON.get())));
	public static final RegistryObject<WallBlock> CAST_IRON_WALL = REGISTER.register("cast_iron_wall", () -> new WallBlock(copy(CAST_IRON.get())));
	public static final RegistryObject<ButtonBlock> CAST_IRON_BUTTON = REGISTER.register("cast_iron_button", () -> new ButtonBlock(copy(CAST_IRON.get()), BlockSetType.STONE, 10, true));
	public static final RegistryObject<PressurePlateBlock> CAST_IRON_PRESSURE_PLATE = REGISTER.register("cast_iron_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(CAST_IRON.get()), BlockSetType.IRON));
	
	public static final RegistryObject<Block> CHISELED_CAST_IRON = REGISTER.register("chiseled_cast_iron", () -> new Block(copy(CAST_IRON.get())));
	public static final RegistryObject<Block> STEEL_BEAM = REGISTER.register("steel_beam", () -> new MSDirectionalBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3.0F, 9.0F)));
	
	//Sulfur
	public static final RegistryObject<Block> NATIVE_SULFUR = REGISTER.register("native_sulfur", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_YELLOW).strength(0.5F, 1.0F)));
	
	//Mycelium
	public static final RegistryObject<Block> MYCELIUM_STONE = REGISTER.register("mycelium_stone", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_MAGENTA).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final RegistryObject<StairBlock> MYCELIUM_STAIRS = REGISTER.register("mycelium_stairs", () -> new StairBlock(() -> MSBlocks.MYCELIUM_STONE.get().defaultBlockState(), copy(MYCELIUM_STONE.get())));
	public static final RegistryObject<SlabBlock> MYCELIUM_SLAB = REGISTER.register("mycelium_slab", () -> new SlabBlock(copy(MYCELIUM_STONE.get())));
	public static final RegistryObject<WallBlock> MYCELIUM_STONE_WALL = REGISTER.register("mycelium_stone_wall", () -> new WallBlock(copy(MYCELIUM_STONE.get())));
	public static final RegistryObject<ButtonBlock> MYCELIUM_STONE_BUTTON = REGISTER.register("mycelium_stone_button", () -> new ButtonBlock(copy(MYCELIUM_STONE.get()), BlockSetType.STONE, 10, true));
	public static final RegistryObject<PressurePlateBlock> MYCELIUM_STONE_PRESSURE_PLATE = REGISTER.register("mycelium_stone_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(MYCELIUM_STONE.get()), BlockSetType.STONE));
	
	public static final RegistryObject<Block> MYCELIUM_COBBLESTONE = REGISTER.register("mycelium_cobblestone", () -> new Block(copy(MYCELIUM_STONE.get())));
	public static final RegistryObject<StairBlock> MYCELIUM_COBBLESTONE_STAIRS = REGISTER.register("mycelium_cobblestone_stairs", () -> new StairBlock(() -> MSBlocks.MYCELIUM_COBBLESTONE.get().defaultBlockState(), copy(MYCELIUM_COBBLESTONE.get())));
	public static final RegistryObject<SlabBlock> MYCELIUM_COBBLESTONE_SLAB = REGISTER.register("mycelium_cobblestone_slab", () -> new SlabBlock(copy(MYCELIUM_COBBLESTONE.get())));
	public static final RegistryObject<WallBlock> MYCELIUM_COBBLESTONE_WALL = REGISTER.register("mycelium_cobblestone_wall", () -> new WallBlock(copy(MYCELIUM_COBBLESTONE.get())));
	
	public static final RegistryObject<Block> POLISHED_MYCELIUM_STONE = REGISTER.register("polished_mycelium_stone", () -> new Block(copy(MYCELIUM_STONE.get())));
	public static final RegistryObject<StairBlock> POLISHED_MYCELIUM_STONE_STAIRS = REGISTER.register("polished_mycelium_stone_stairs", () -> new StairBlock(() -> MSBlocks.POLISHED_MYCELIUM_STONE.get().defaultBlockState(), copy(POLISHED_MYCELIUM_STONE.get())));
	public static final RegistryObject<SlabBlock> POLISHED_MYCELIUM_STONE_SLAB = REGISTER.register("polished_mycelium_stone_slab", () -> new SlabBlock(copy(POLISHED_MYCELIUM_STONE.get())));
	public static final RegistryObject<WallBlock> POLISHED_MYCELIUM_STONE_WALL = REGISTER.register("polished_mycelium_stone_wall", () -> new WallBlock(copy(POLISHED_MYCELIUM_STONE.get())));
	
	public static final RegistryObject<Block> MYCELIUM_BRICKS = REGISTER.register("mycelium_bricks", () -> new Block(copy(MYCELIUM_STONE.get())));
	public static final RegistryObject<StairBlock> MYCELIUM_BRICK_STAIRS = REGISTER.register("mycelium_brick_stairs", () -> new StairBlock(() -> MSBlocks.MYCELIUM_BRICKS.get().defaultBlockState(), copy(MYCELIUM_BRICKS.get())));
	public static final RegistryObject<SlabBlock> MYCELIUM_BRICK_SLAB = REGISTER.register("mycelium_brick_slab", () -> new SlabBlock(copy(MYCELIUM_BRICKS.get())));
	public static final RegistryObject<WallBlock> MYCELIUM_BRICK_WALL = REGISTER.register("mycelium_brick_wall", () -> new WallBlock(copy(MYCELIUM_BRICKS.get())));
	
	public static final RegistryObject<Block> MYCELIUM_COLUMN = REGISTER.register("mycelium_column", () -> new MSDirectionalBlock(copy(MYCELIUM_STONE.get())));
	public static final RegistryObject<Block> CHISELED_MYCELIUM_BRICKS = REGISTER.register("chiseled_mycelium_bricks", () -> new Block(copy(MYCELIUM_BRICKS.get())));
	public static final RegistryObject<Block> SUSPICIOUS_CHISELED_MYCELIUM_BRICKS = REGISTER.register("suspicious_chiseled_mycelium_bricks", () -> new Block(copy(MYCELIUM_BRICKS.get())));
	public static final RegistryObject<Block> CRACKED_MYCELIUM_BRICKS = REGISTER.register("cracked_mycelium_bricks", () -> new Block(copy(MYCELIUM_BRICKS.get())));
	
	public static final RegistryObject<Block> MOSSY_MYCELIUM_BRICKS = REGISTER.register("mossy_mycelium_bricks", () -> new Block(copy(MYCELIUM_BRICKS.get())));
	public static final RegistryObject<StairBlock> MOSSY_MYCELIUM_BRICK_STAIRS = REGISTER.register("mossy_mycelium_brick_stairs", () -> new StairBlock(() -> MSBlocks.MOSSY_MYCELIUM_BRICKS.get().defaultBlockState(), copy(MOSSY_MYCELIUM_BRICKS.get())));
	public static final RegistryObject<SlabBlock> MOSSY_MYCELIUM_BRICK_SLAB = REGISTER.register("mossy_mycelium_brick_slab", () -> new SlabBlock(copy(MOSSY_MYCELIUM_BRICKS.get())));
	public static final RegistryObject<WallBlock> MOSSY_MYCELIUM_BRICK_WALL = REGISTER.register("mossy_mycelium_brick_wall", () -> new WallBlock(copy(MOSSY_MYCELIUM_BRICKS.get())));
	
	public static final RegistryObject<Block> FLOWERY_MYCELIUM_BRICKS = REGISTER.register("flowery_mycelium_bricks", () -> new Block(copy(MYCELIUM_BRICKS.get())));
	public static final RegistryObject<StairBlock> FLOWERY_MYCELIUM_BRICK_STAIRS = REGISTER.register("flowery_mycelium_brick_stairs", () -> new StairBlock(() -> MSBlocks.FLOWERY_MYCELIUM_BRICKS.get().defaultBlockState(), copy(FLOWERY_MYCELIUM_BRICKS.get())));
	public static final RegistryObject<SlabBlock> FLOWERY_MYCELIUM_BRICK_SLAB = REGISTER.register("flowery_mycelium_brick_slab", () -> new SlabBlock(copy(FLOWERY_MYCELIUM_BRICKS.get())));
	public static final RegistryObject<WallBlock> FLOWERY_MYCELIUM_BRICK_WALL = REGISTER.register("flowery_mycelium_brick_wall", () -> new WallBlock(copy(FLOWERY_MYCELIUM_BRICKS.get())));
	
	//Black
	public static final RegistryObject<Block> BLACK_SAND = REGISTER.register("black_sand", () -> new SandBlock(0x181915, Block.Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.SNARE).strength(0.5F).sound(SoundType.SAND)));
	
	public static final RegistryObject<Block> BLACK_STONE = REGISTER.register("black_stone", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.5F, 6.0F)));
	public static final RegistryObject<StairBlock> BLACK_STONE_STAIRS = REGISTER.register("black_stone_stairs", () -> new StairBlock(() -> MSBlocks.BLACK_STONE.get().defaultBlockState(), copy(BLACK_STONE.get())));
	public static final RegistryObject<SlabBlock> BLACK_STONE_SLAB = REGISTER.register("black_stone_slab", () -> new SlabBlock(copy(BLACK_STONE.get())));
	public static final RegistryObject<WallBlock> BLACK_STONE_WALL = REGISTER.register("black_stone_wall", () -> new WallBlock(copy(BLACK_STONE.get())));
	public static final RegistryObject<ButtonBlock> BLACK_STONE_BUTTON = REGISTER.register("black_stone_button", () -> new ButtonBlock(copy(BLACK_STONE.get()), BlockSetType.STONE, 10, true));
	public static final RegistryObject<PressurePlateBlock> BLACK_STONE_PRESSURE_PLATE = REGISTER.register("black_stone_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(BLACK_STONE.get()), BlockSetType.STONE));
	
	public static final RegistryObject<Block> BLACK_COBBLESTONE = REGISTER.register("black_cobblestone", () -> new Block(copy(BLACK_STONE.get())));
	public static final RegistryObject<StairBlock> BLACK_COBBLESTONE_STAIRS = REGISTER.register("black_cobblestone_stairs", () -> new StairBlock(() -> MSBlocks.BLACK_COBBLESTONE.get().defaultBlockState(), copy(BLACK_COBBLESTONE.get())));
	public static final RegistryObject<SlabBlock> BLACK_COBBLESTONE_SLAB = REGISTER.register("black_cobblestone_slab", () -> new SlabBlock(copy(BLACK_COBBLESTONE.get())));
	public static final RegistryObject<WallBlock> BLACK_COBBLESTONE_WALL = REGISTER.register("black_cobblestone_wall", () -> new WallBlock(copy(BLACK_COBBLESTONE.get())));
	
	public static final RegistryObject<Block> POLISHED_BLACK_STONE = REGISTER.register("polished_black_stone", () -> new Block(copy(BLACK_STONE.get())));
	public static final RegistryObject<StairBlock> POLISHED_BLACK_STONE_STAIRS = REGISTER.register("polished_black_stone_stairs", () -> new StairBlock(() -> MSBlocks.POLISHED_BLACK_STONE.get().defaultBlockState(), copy(POLISHED_BLACK_STONE.get())));
	public static final RegistryObject<SlabBlock> POLISHED_BLACK_STONE_SLAB = REGISTER.register("polished_black_stone_slab", () -> new SlabBlock(copy(POLISHED_BLACK_STONE.get())));
	public static final RegistryObject<WallBlock> POLISHED_BLACK_STONE_WALL = REGISTER.register("polished_black_stone_wall", () -> new WallBlock(copy(POLISHED_BLACK_STONE.get())));
	
	public static final RegistryObject<Block> BLACK_STONE_BRICKS = REGISTER.register("black_stone_bricks", () -> new Block(copy(BLACK_STONE.get())));
	public static final RegistryObject<StairBlock> BLACK_STONE_BRICK_STAIRS = REGISTER.register("black_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.BLACK_STONE_BRICKS.get().defaultBlockState(), copy(BLACK_STONE_BRICKS.get())));
	public static final RegistryObject<SlabBlock> BLACK_STONE_BRICK_SLAB = REGISTER.register("black_stone_brick_slab", () -> new SlabBlock(copy(BLACK_STONE_BRICKS.get())));
	public static final RegistryObject<WallBlock> BLACK_STONE_BRICK_WALL = REGISTER.register("black_stone_brick_wall", () -> new WallBlock(copy(BLACK_STONE_BRICKS.get())));
	
	public static final RegistryObject<Block> BLACK_STONE_COLUMN = REGISTER.register("black_stone_column", () -> new MSDirectionalBlock(copy(BLACK_STONE.get())));
	public static final RegistryObject<Block> CHISELED_BLACK_STONE_BRICKS = REGISTER.register("chiseled_black_stone_bricks", () -> new Block(copy(BLACK_STONE_BRICKS.get())));
	public static final RegistryObject<Block> CRACKED_BLACK_STONE_BRICKS = REGISTER.register("cracked_black_stone_bricks", () -> new Block(copy(BLACK_STONE_BRICKS.get())));
	
	//Flowery
	public static final RegistryObject<Block> FLOWERY_MOSSY_COBBLESTONE = REGISTER.register("flowery_mossy_cobblestone", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final RegistryObject<StairBlock> FLOWERY_MOSSY_COBBLESTONE_STAIRS = REGISTER.register("flowery_mossy_cobblestone_stairs", () -> new StairBlock(() -> MSBlocks.FLOWERY_MOSSY_COBBLESTONE.get().defaultBlockState(), copy(FLOWERY_MOSSY_COBBLESTONE.get())));
	public static final RegistryObject<SlabBlock> FLOWERY_MOSSY_COBBLESTONE_SLAB = REGISTER.register("flowery_mossy_cobblestone_slab", () -> new SlabBlock(copy(FLOWERY_MOSSY_COBBLESTONE.get())));
	public static final RegistryObject<WallBlock> FLOWERY_MOSSY_COBBLESTONE_WALL = REGISTER.register("flowery_mossy_cobblestone_wall", () -> new WallBlock(copy(FLOWERY_MOSSY_COBBLESTONE.get())));
	
	public static final RegistryObject<Block> FLOWERY_MOSSY_STONE_BRICKS = REGISTER.register("flowery_mossy_stone_bricks", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final RegistryObject<StairBlock> FLOWERY_MOSSY_STONE_BRICK_STAIRS = REGISTER.register("flowery_mossy_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.FLOWERY_MOSSY_STONE_BRICKS.get().defaultBlockState(), copy(FLOWERY_MOSSY_STONE_BRICKS.get())));
	public static final RegistryObject<SlabBlock> FLOWERY_MOSSY_STONE_BRICK_SLAB = REGISTER.register("flowery_mossy_stone_brick_slab", () -> new SlabBlock(copy(FLOWERY_MOSSY_STONE_BRICKS.get())));
	public static final RegistryObject<WallBlock> FLOWERY_MOSSY_STONE_BRICK_WALL = REGISTER.register("flowery_mossy_stone_brick_wall", () -> new WallBlock(copy(FLOWERY_MOSSY_STONE_BRICKS.get())));
	
	//Decrepit
	public static final RegistryObject<Block> DECREPIT_STONE_BRICKS = REGISTER.register("decrepit_stone_bricks", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final RegistryObject<StairBlock> DECREPIT_STONE_BRICK_STAIRS = REGISTER.register("decrepit_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.DECREPIT_STONE_BRICKS.get().defaultBlockState(), copy(DECREPIT_STONE_BRICKS.get())));
	public static final RegistryObject<SlabBlock> DECREPIT_STONE_BRICK_SLAB = REGISTER.register("decrepit_stone_brick_slab", () -> new SlabBlock(copy(DECREPIT_STONE_BRICKS.get())));
	public static final RegistryObject<WallBlock> DECREPIT_STONE_BRICK_WALL = REGISTER.register("decrepit_stone_brick_wall", () -> new WallBlock(copy(DECREPIT_STONE_BRICKS.get())));
	
	public static final RegistryObject<Block> MOSSY_DECREPIT_STONE_BRICKS = REGISTER.register("mossy_decrepit_stone_bricks", () -> new Block(copy(DECREPIT_STONE_BRICKS.get())));
	public static final RegistryObject<StairBlock> MOSSY_DECREPIT_STONE_BRICK_STAIRS = REGISTER.register("mossy_decrepit_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.MOSSY_DECREPIT_STONE_BRICKS.get().defaultBlockState(), copy(MOSSY_DECREPIT_STONE_BRICKS.get())));
	public static final RegistryObject<SlabBlock> MOSSY_DECREPIT_STONE_BRICK_SLAB = REGISTER.register("mossy_decrepit_stone_brick_slab", () -> new SlabBlock(copy(MOSSY_DECREPIT_STONE_BRICKS.get())));
	public static final RegistryObject<WallBlock> MOSSY_DECREPIT_STONE_BRICK_WALL = REGISTER.register("mossy_decrepit_stone_brick_wall", () -> new WallBlock(copy(MOSSY_DECREPIT_STONE_BRICKS.get())));
	
	//End
	public static final RegistryObject<Block> COARSE_END_STONE = REGISTER.register("coarse_end_stone", () -> new TillableBlock(Block.Properties.of().mapColor(MapColor.SAND).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 9.0F), Blocks.END_STONE::defaultBlockState));
	
	//Chalk
	public static final RegistryObject<Block> CHALK = REGISTER.register("chalk", () -> new Block(Block.Properties.of().mapColor(MapColor.SNOW).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final RegistryObject<StairBlock> CHALK_STAIRS = REGISTER.register("chalk_stairs", () -> new StairBlock(() -> MSBlocks.CHALK.get().defaultBlockState(), copy(CHALK.get())));
	public static final RegistryObject<SlabBlock> CHALK_SLAB = REGISTER.register("chalk_slab", () -> new SlabBlock(copy(CHALK.get())));
	public static final RegistryObject<WallBlock> CHALK_WALL = REGISTER.register("chalk_wall", () -> new WallBlock(copy(CHALK.get())));
	public static final RegistryObject<ButtonBlock> CHALK_BUTTON = REGISTER.register("chalk_button", () -> new ButtonBlock(copy(CHALK.get()), BlockSetType.STONE, 10, true));
	public static final RegistryObject<PressurePlateBlock> CHALK_PRESSURE_PLATE = REGISTER.register("chalk_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(CHALK.get()), BlockSetType.STONE));
	
	public static final RegistryObject<Block> POLISHED_CHALK = REGISTER.register("polished_chalk", () -> new Block(copy(CHALK.get())));
	public static final RegistryObject<StairBlock> POLISHED_CHALK_STAIRS = REGISTER.register("polished_chalk_stairs", () -> new StairBlock(() -> MSBlocks.POLISHED_CHALK.get().defaultBlockState(), copy(POLISHED_CHALK.get())));
	public static final RegistryObject<SlabBlock> POLISHED_CHALK_SLAB = REGISTER.register("polished_chalk_slab", () -> new SlabBlock(copy(POLISHED_CHALK.get())));
	public static final RegistryObject<WallBlock> POLISHED_CHALK_WALL = REGISTER.register("polished_chalk_wall", () -> new WallBlock(copy(POLISHED_CHALK.get())));
	
	public static final RegistryObject<Block> CHALK_BRICKS = REGISTER.register("chalk_bricks", () -> new Block(copy(CHALK.get())));
	public static final RegistryObject<StairBlock> CHALK_BRICK_STAIRS = REGISTER.register("chalk_brick_stairs", () -> new StairBlock(() -> MSBlocks.CHALK_BRICKS.get().defaultBlockState(), copy(CHALK_BRICKS.get())));
	public static final RegistryObject<SlabBlock> CHALK_BRICK_SLAB = REGISTER.register("chalk_brick_slab", () -> new SlabBlock(copy(CHALK_BRICKS.get())));
	public static final RegistryObject<WallBlock> CHALK_BRICK_WALL = REGISTER.register("chalk_brick_wall", () -> new WallBlock(copy(CHALK_BRICKS.get())));
	
	public static final RegistryObject<Block> CHALK_COLUMN = REGISTER.register("chalk_column", () -> new MSDirectionalBlock(copy(CHALK.get())));
	public static final RegistryObject<Block> CHISELED_CHALK_BRICKS = REGISTER.register("chiseled_chalk_bricks", () -> new Block(copy(CHALK_BRICKS.get())));
	
	public static final RegistryObject<Block> MOSSY_CHALK_BRICKS = REGISTER.register("mossy_chalk_bricks", () -> new Block(copy(CHALK_BRICKS.get())));
	public static final RegistryObject<StairBlock> MOSSY_CHALK_BRICK_STAIRS = REGISTER.register("mossy_chalk_brick_stairs", () -> new StairBlock(() -> MSBlocks.MOSSY_CHALK_BRICKS.get().defaultBlockState(), copy(MOSSY_CHALK_BRICKS.get())));
	public static final RegistryObject<SlabBlock> MOSSY_CHALK_BRICK_SLAB = REGISTER.register("mossy_chalk_brick_slab", () -> new SlabBlock(copy(MOSSY_CHALK_BRICKS.get())));
	public static final RegistryObject<WallBlock> MOSSY_CHALK_BRICK_WALL = REGISTER.register("mossy_chalk_brick_wall", () -> new WallBlock(copy(MOSSY_CHALK_BRICKS.get())));
	
	public static final RegistryObject<Block> FLOWERY_CHALK_BRICKS = REGISTER.register("flowery_chalk_bricks", () -> new Block(copy(CHALK_BRICKS.get())));
	public static final RegistryObject<StairBlock> FLOWERY_CHALK_BRICK_STAIRS = REGISTER.register("flowery_chalk_brick_stairs", () -> new StairBlock(() -> MSBlocks.FLOWERY_CHALK_BRICKS.get().defaultBlockState(), copy(FLOWERY_CHALK_BRICKS.get())));
	public static final RegistryObject<SlabBlock> FLOWERY_CHALK_BRICK_SLAB = REGISTER.register("flowery_chalk_brick_slab", () -> new SlabBlock(copy(FLOWERY_CHALK_BRICKS.get())));
	public static final RegistryObject<WallBlock> FLOWERY_CHALK_BRICK_WALL = REGISTER.register("flowery_chalk_brick_wall", () -> new WallBlock(copy(FLOWERY_CHALK_BRICKS.get())));
	
	//Pink
	public static final RegistryObject<Block> PINK_STONE = REGISTER.register("pink_stone", () -> new Block(Block.Properties.of().mapColor(MapColor.SNOW).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final RegistryObject<StairBlock> PINK_STONE_STAIRS = REGISTER.register("pink_stone_stairs", () -> new StairBlock(() -> MSBlocks.PINK_STONE.get().defaultBlockState(), copy(PINK_STONE.get())));
	public static final RegistryObject<SlabBlock> PINK_STONE_SLAB = REGISTER.register("pink_stone_slab", () -> new SlabBlock(copy(PINK_STONE.get())));
	public static final RegistryObject<WallBlock> PINK_STONE_WALL = REGISTER.register("pink_stone_wall", () -> new WallBlock(copy(PINK_STONE.get())));
	public static final RegistryObject<ButtonBlock> PINK_STONE_BUTTON = REGISTER.register("pink_stone_button", () -> new ButtonBlock(copy(PINK_STONE.get()), BlockSetType.STONE, 10, true));
	public static final RegistryObject<PressurePlateBlock> PINK_STONE_PRESSURE_PLATE = REGISTER.register("pink_stone_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(PINK_STONE.get()), BlockSetType.STONE));
	
	public static final RegistryObject<Block> POLISHED_PINK_STONE = REGISTER.register("polished_pink_stone", () -> new Block(copy(PINK_STONE.get())));
	public static final RegistryObject<StairBlock> POLISHED_PINK_STONE_STAIRS = REGISTER.register("polished_pink_stone_stairs", () -> new StairBlock(() -> MSBlocks.POLISHED_PINK_STONE.get().defaultBlockState(), copy(POLISHED_PINK_STONE.get())));
	public static final RegistryObject<SlabBlock> POLISHED_PINK_STONE_SLAB = REGISTER.register("polished_pink_stone_slab", () -> new SlabBlock(copy(POLISHED_PINK_STONE.get())));
	public static final RegistryObject<WallBlock> POLISHED_PINK_STONE_WALL = REGISTER.register("polished_pink_stone_wall", () -> new WallBlock(copy(POLISHED_PINK_STONE.get())));
	
	public static final RegistryObject<Block> PINK_STONE_BRICKS = REGISTER.register("pink_stone_bricks", () -> new Block(copy(PINK_STONE.get())));
	public static final RegistryObject<StairBlock> PINK_STONE_BRICK_STAIRS = REGISTER.register("pink_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.PINK_STONE_BRICKS.get().defaultBlockState(), copy(PINK_STONE_BRICKS.get())));
	public static final RegistryObject<SlabBlock> PINK_STONE_BRICK_SLAB = REGISTER.register("pink_stone_brick_slab", () -> new SlabBlock(copy(PINK_STONE_BRICKS.get())));
	public static final RegistryObject<WallBlock> PINK_STONE_BRICK_WALL = REGISTER.register("pink_stone_brick_wall", () -> new WallBlock(copy(PINK_STONE_BRICKS.get())));
	
	public static final RegistryObject<Block> PINK_STONE_COLUMN = REGISTER.register("pink_stone_column", () -> new MSDirectionalBlock(copy(PINK_STONE.get())));
	public static final RegistryObject<Block> CHISELED_PINK_STONE_BRICKS = REGISTER.register("chiseled_pink_stone_bricks", () -> new Block(copy(PINK_STONE_BRICKS.get())));
	public static final RegistryObject<Block> CRACKED_PINK_STONE_BRICKS = REGISTER.register("cracked_pink_stone_bricks", () -> new Block(copy(PINK_STONE_BRICKS.get())));
	
	public static final RegistryObject<Block> MOSSY_PINK_STONE_BRICKS = REGISTER.register("mossy_pink_stone_bricks", () -> new Block(copy(PINK_STONE_BRICKS.get())));
	public static final RegistryObject<StairBlock> MOSSY_PINK_STONE_BRICK_STAIRS = REGISTER.register("mossy_pink_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.MOSSY_PINK_STONE_BRICKS.get().defaultBlockState(), copy(MOSSY_PINK_STONE_BRICKS.get())));
	public static final RegistryObject<SlabBlock> MOSSY_PINK_STONE_BRICK_SLAB = REGISTER.register("mossy_pink_stone_brick_slab", () -> new SlabBlock(copy(MOSSY_PINK_STONE_BRICKS.get())));
	public static final RegistryObject<WallBlock> MOSSY_PINK_STONE_BRICK_WALL = REGISTER.register("mossy_pink_stone_brick_wall", () -> new WallBlock(copy(MOSSY_PINK_STONE_BRICKS.get())));
	
	//Brown
	public static final RegistryObject<Block> BROWN_STONE = REGISTER.register("brown_stone", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_BROWN).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.5F, 7.0F)));
	public static final RegistryObject<StairBlock> BROWN_STONE_STAIRS = REGISTER.register("brown_stone_stairs", () -> new StairBlock(() -> MSBlocks.BROWN_STONE.get().defaultBlockState(), copy(BROWN_STONE.get())));
	public static final RegistryObject<SlabBlock> BROWN_STONE_SLAB = REGISTER.register("brown_stone_slab", () -> new SlabBlock(copy(BROWN_STONE.get())));
	public static final RegistryObject<WallBlock> BROWN_STONE_WALL = REGISTER.register("brown_stone_wall", () -> new WallBlock(copy(BROWN_STONE.get())));
	public static final RegistryObject<ButtonBlock> BROWN_STONE_BUTTON = REGISTER.register("brown_stone_button", () -> new ButtonBlock(copy(BROWN_STONE.get()), BlockSetType.STONE, 10, true));
	public static final RegistryObject<PressurePlateBlock> BROWN_STONE_PRESSURE_PLATE = REGISTER.register("brown_stone_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(BROWN_STONE.get()), BlockSetType.STONE));
	
	public static final RegistryObject<Block> POLISHED_BROWN_STONE = REGISTER.register("polished_brown_stone", () -> new Block(copy(BROWN_STONE.get())));
	public static final RegistryObject<StairBlock> POLISHED_BROWN_STONE_STAIRS = REGISTER.register("polished_brown_stone_stairs", () -> new StairBlock(() -> MSBlocks.POLISHED_BROWN_STONE.get().defaultBlockState(), copy(POLISHED_BROWN_STONE.get())));
	public static final RegistryObject<SlabBlock> POLISHED_BROWN_STONE_SLAB = REGISTER.register("polished_brown_stone_slab", () -> new SlabBlock(copy(POLISHED_BROWN_STONE.get())));
	public static final RegistryObject<WallBlock> POLISHED_BROWN_STONE_WALL = REGISTER.register("polished_brown_stone_wall", () -> new WallBlock(copy(POLISHED_BROWN_STONE.get())));
	
	public static final RegistryObject<Block> BROWN_STONE_BRICKS = REGISTER.register("brown_stone_bricks", () -> new Block(copy(BROWN_STONE.get())));
	public static final RegistryObject<StairBlock> BROWN_STONE_BRICK_STAIRS = REGISTER.register("brown_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.BROWN_STONE_BRICKS.get().defaultBlockState(), copy(BROWN_STONE_BRICKS.get())));
	public static final RegistryObject<SlabBlock> BROWN_STONE_BRICK_SLAB = REGISTER.register("brown_stone_brick_slab", () -> new SlabBlock(copy(BROWN_STONE_BRICKS.get())));
	public static final RegistryObject<WallBlock> BROWN_STONE_BRICK_WALL = REGISTER.register("brown_stone_brick_wall", () -> new WallBlock(copy(BROWN_STONE_BRICKS.get())));
	
	public static final RegistryObject<Block> CRACKED_BROWN_STONE_BRICKS = REGISTER.register("cracked_brown_stone_bricks", () -> new Block(copy(BROWN_STONE.get())));
	public static final RegistryObject<Block> BROWN_STONE_COLUMN = REGISTER.register("brown_stone_column", () -> new MSDirectionalBlock(copy(BROWN_STONE.get())));
	
	//Green
	public static final RegistryObject<Block> GREEN_STONE = REGISTER.register("green_stone", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_GREEN).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.5F, 7.0F)));
	public static final RegistryObject<StairBlock> GREEN_STONE_STAIRS = REGISTER.register("green_stone_stairs", () -> new StairBlock(() -> MSBlocks.GREEN_STONE.get().defaultBlockState(), copy(GREEN_STONE.get())));
	public static final RegistryObject<SlabBlock> GREEN_STONE_SLAB = REGISTER.register("green_stone_slab", () -> new SlabBlock(copy(GREEN_STONE.get())));
	public static final RegistryObject<WallBlock> GREEN_STONE_WALL = REGISTER.register("green_stone_wall", () -> new WallBlock(copy(GREEN_STONE.get())));
	public static final RegistryObject<ButtonBlock> GREEN_STONE_BUTTON = REGISTER.register("green_stone_button", () -> new ButtonBlock(copy(GREEN_STONE.get()), BlockSetType.STONE, 10, true));
	public static final RegistryObject<PressurePlateBlock> GREEN_STONE_PRESSURE_PLATE = REGISTER.register("green_stone_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(GREEN_STONE.get()), BlockSetType.STONE));
	
	public static final RegistryObject<Block> POLISHED_GREEN_STONE = REGISTER.register("polished_green_stone", () -> new Block(copy(GREEN_STONE.get())));
	public static final RegistryObject<StairBlock> POLISHED_GREEN_STONE_STAIRS = REGISTER.register("polished_green_stone_stairs", () -> new StairBlock(() -> MSBlocks.POLISHED_GREEN_STONE.get().defaultBlockState(), copy(POLISHED_GREEN_STONE.get())));
	public static final RegistryObject<SlabBlock> POLISHED_GREEN_STONE_SLAB = REGISTER.register("polished_green_stone_slab", () -> new SlabBlock(copy(POLISHED_GREEN_STONE.get())));
	public static final RegistryObject<WallBlock> POLISHED_GREEN_STONE_WALL = REGISTER.register("polished_green_stone_wall", () -> new WallBlock(copy(POLISHED_GREEN_STONE.get())));
	
	public static final RegistryObject<Block> GREEN_STONE_BRICKS = REGISTER.register("green_stone_bricks", () -> new Block(copy(GREEN_STONE.get())));
	public static final RegistryObject<StairBlock> GREEN_STONE_BRICK_STAIRS = REGISTER.register("green_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.GREEN_STONE_BRICKS.get().defaultBlockState(), copy(GREEN_STONE_BRICKS.get())));
	public static final RegistryObject<SlabBlock> GREEN_STONE_BRICK_SLAB = REGISTER.register("green_stone_brick_slab", () -> new SlabBlock(copy(GREEN_STONE_BRICKS.get())));
	public static final RegistryObject<WallBlock> GREEN_STONE_BRICK_WALL = REGISTER.register("green_stone_brick_wall", () -> new WallBlock(copy(GREEN_STONE_BRICKS.get())));
	
	public static final RegistryObject<Block> GREEN_STONE_COLUMN = REGISTER.register("green_stone_column", () -> new MSDirectionalBlock(copy(GREEN_STONE.get())));
	public static final RegistryObject<Block> CHISELED_GREEN_STONE_BRICKS = REGISTER.register("chiseled_green_stone_bricks", () -> new Block(copy(GREEN_STONE_BRICKS.get())));
	
	public static final RegistryObject<Block> HORIZONTAL_GREEN_STONE_BRICKS = REGISTER.register("horizontal_green_stone_bricks", () -> new Block(copy(GREEN_STONE_BRICKS.get())));
	public static final RegistryObject<StairBlock> HORIZONTAL_GREEN_STONE_BRICK_STAIRS = REGISTER.register("horizontal_green_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.HORIZONTAL_GREEN_STONE_BRICKS.get().defaultBlockState(), copy(HORIZONTAL_GREEN_STONE_BRICKS.get())));
	public static final RegistryObject<SlabBlock> HORIZONTAL_GREEN_STONE_BRICK_SLAB = REGISTER.register("horizontal_green_stone_brick_slab", () -> new SlabBlock(copy(HORIZONTAL_GREEN_STONE_BRICKS.get())));
	public static final RegistryObject<WallBlock> HORIZONTAL_GREEN_STONE_BRICK_WALL = REGISTER.register("horizontal_green_stone_brick_wall", () -> new WallBlock(copy(HORIZONTAL_GREEN_STONE_BRICKS.get())));
	
	public static final RegistryObject<Block> VERTICAL_GREEN_STONE_BRICKS = REGISTER.register("vertical_green_stone_bricks", () -> new Block(copy(GREEN_STONE_BRICKS.get())));
	public static final RegistryObject<StairBlock> VERTICAL_GREEN_STONE_BRICK_STAIRS = REGISTER.register("vertical_green_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.VERTICAL_GREEN_STONE_BRICKS.get().defaultBlockState(), copy(VERTICAL_GREEN_STONE_BRICKS.get())));
	public static final RegistryObject<SlabBlock> VERTICAL_GREEN_STONE_BRICK_SLAB = REGISTER.register("vertical_green_stone_brick_slab", () -> new SlabBlock(copy(VERTICAL_GREEN_STONE_BRICKS.get())));
	public static final RegistryObject<WallBlock> VERTICAL_GREEN_STONE_BRICK_WALL = REGISTER.register("vertical_green_stone_brick_wall", () -> new WallBlock(copy(VERTICAL_GREEN_STONE_BRICKS.get())));
	
	public static final RegistryObject<Block> GREEN_STONE_BRICK_EMBEDDED_LADDER = REGISTER.register("green_stone_brick_embedded_ladder", () -> new CustomShapeBlock(copy(GREEN_STONE_BRICKS.get()), MSBlockShapes.EMBEDDED_STAIRS)); //uses the tag CLIMBABLE
	public static final RegistryObject<Block> GREEN_STONE_BRICK_TRIM = REGISTER.register("green_stone_brick_trim", () -> new MSDirectionalBlock(copy(GREEN_STONE_BRICKS.get())));
	public static final RegistryObject<Block> GREEN_STONE_BRICK_FROG = REGISTER.register("green_stone_brick_frog", () -> new HieroglyphBlock(copy(GREEN_STONE_BRICKS.get())));
	public static final RegistryObject<Block> GREEN_STONE_BRICK_IGUANA_LEFT = REGISTER.register("green_stone_brick_iguana_left", () -> new HieroglyphBlock(copy(GREEN_STONE_BRICKS.get())));
	public static final RegistryObject<Block> GREEN_STONE_BRICK_IGUANA_RIGHT = REGISTER.register("green_stone_brick_iguana_right", () -> new HieroglyphBlock(copy(GREEN_STONE_BRICKS.get())));
	public static final RegistryObject<Block> GREEN_STONE_BRICK_LOTUS = REGISTER.register("green_stone_brick_lotus", () -> new HieroglyphBlock(copy(GREEN_STONE_BRICKS.get())));
	public static final RegistryObject<Block> GREEN_STONE_BRICK_NAK_LEFT = REGISTER.register("green_stone_brick_nak_left", () -> new HieroglyphBlock(copy(GREEN_STONE_BRICKS.get())));
	public static final RegistryObject<Block> GREEN_STONE_BRICK_NAK_RIGHT = REGISTER.register("green_stone_brick_nak_right", () -> new HieroglyphBlock(copy(GREEN_STONE_BRICKS.get())));
	public static final RegistryObject<Block> GREEN_STONE_BRICK_SALAMANDER_LEFT = REGISTER.register("green_stone_brick_salamander_left", () -> new HieroglyphBlock(copy(GREEN_STONE_BRICKS.get())));
	public static final RegistryObject<Block> GREEN_STONE_BRICK_SALAMANDER_RIGHT = REGISTER.register("green_stone_brick_salamander_right", () -> new HieroglyphBlock(copy(GREEN_STONE_BRICKS.get())));
	public static final RegistryObject<Block> GREEN_STONE_BRICK_SKAIA = REGISTER.register("green_stone_brick_skaia", () -> new HieroglyphBlock(copy(GREEN_STONE_BRICKS.get())));
	public static final RegistryObject<Block> GREEN_STONE_BRICK_TURTLE = REGISTER.register("green_stone_brick_turtle", () -> new HieroglyphBlock(copy(GREEN_STONE_BRICKS.get())));
	//TODO when walking down steep stairs, players take fall damage that cannot be circumvented by the fallOn function. This includes when there is only an air block underneath them
	public static final RegistryObject<Block> STEEP_GREEN_STONE_BRICK_STAIRS_BASE = REGISTER.register("steep_green_stone_brick_stairs_base", () -> new CustomShapeBlock(copy(GREEN_STONE.get()), MSBlockShapes.STEEP_STAIRS_BASE));
	public static final RegistryObject<Block> STEEP_GREEN_STONE_BRICK_STAIRS_TOP = REGISTER.register("steep_green_stone_brick_stairs_top", () -> new CustomShapeBlock(copy(GREEN_STONE.get()), MSBlockShapes.STEEP_STAIRS_TOP));
	
	//Sandstone
	public static final RegistryObject<Block> SANDSTONE_COLUMN = REGISTER.register("sandstone_column", () -> new MSDirectionalBlock(Block.Properties.of().mapColor(MapColor.SAND).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));
	public static final RegistryObject<Block> CHISELED_SANDSTONE_COLUMN = REGISTER.register("chiseled_sandstone_column", () -> new MSDirectionalBlock(Block.Properties.of().mapColor(MapColor.SAND).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));
	public static final RegistryObject<Block> RED_SANDSTONE_COLUMN = REGISTER.register("red_sandstone_column", () -> new MSDirectionalBlock(Block.Properties.of().mapColor(MapColor.COLOR_ORANGE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));
	public static final RegistryObject<Block> CHISELED_RED_SANDSTONE_COLUMN = REGISTER.register("chiseled_red_sandstone_column", () -> new MSDirectionalBlock(Block.Properties.of().mapColor(MapColor.COLOR_ORANGE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));
	
	//Wood
	public static final RegistryObject<Block> UNCARVED_WOOD = REGISTER.register("uncarved_wood", () -> new Block(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).requiresCorrectToolForDrops().sound(SoundType.WOOD)));
	public static final RegistryObject<StairBlock> UNCARVED_WOOD_STAIRS = REGISTER.register("uncarved_wood_stairs", () -> new StairBlock(() -> MSBlocks.UNCARVED_WOOD.get().defaultBlockState(), copy(UNCARVED_WOOD.get())));
	public static final RegistryObject<SlabBlock> UNCARVED_WOOD_SLAB = REGISTER.register("uncarved_wood_slab", () -> new SlabBlock(copy(UNCARVED_WOOD.get())));
	public static final RegistryObject<ButtonBlock> UNCARVED_WOOD_BUTTON = REGISTER.register("uncarved_wood_button", () -> new ButtonBlock(copy(UNCARVED_WOOD.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<PressurePlateBlock> UNCARVED_WOOD_PRESSURE_PLATE = REGISTER.register("uncarved_wood_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(UNCARVED_WOOD.get()), BlockSetType.OAK));
	public static final RegistryObject<FenceBlock> UNCARVED_WOOD_FENCE = REGISTER.register("uncarved_wood_fence", () -> new FenceBlock(copy(UNCARVED_WOOD.get())));
	public static final RegistryObject<FenceGateBlock> UNCARVED_WOOD_FENCE_GATE = REGISTER.register("uncarved_wood_fence_gate", () -> new FenceGateBlock(copy(UNCARVED_WOOD.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	
	public static final RegistryObject<Block> CHIPBOARD = REGISTER.register("chipboard", () -> new Block(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(1.0F).requiresCorrectToolForDrops().sound(SoundType.SCAFFOLDING)));
	public static final RegistryObject<StairBlock> CHIPBOARD_STAIRS = REGISTER.register("chipboard_stairs", () -> new StairBlock(() -> MSBlocks.CHIPBOARD.get().defaultBlockState(), copy(CHIPBOARD.get())));
	public static final RegistryObject<SlabBlock> CHIPBOARD_SLAB = REGISTER.register("chipboard_slab", () -> new SlabBlock(copy(CHIPBOARD.get())));
	public static final RegistryObject<ButtonBlock> CHIPBOARD_BUTTON = REGISTER.register("chipboard_button", () -> new ButtonBlock(copy(CHIPBOARD.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<PressurePlateBlock> CHIPBOARD_PRESSURE_PLATE = REGISTER.register("chipboard_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(CHIPBOARD.get()), BlockSetType.OAK));
	public static final RegistryObject<FenceBlock> CHIPBOARD_FENCE = REGISTER.register("chipboard_fence", () -> new FenceBlock(copy(CHIPBOARD.get())));
	public static final RegistryObject<FenceGateBlock> CHIPBOARD_FENCE_GATE = REGISTER.register("chipboard_fence_gate", () -> new FenceGateBlock(copy(CHIPBOARD.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	
	public static final RegistryObject<Block> WOOD_SHAVINGS = REGISTER.register("wood_shavings", () -> new Block(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(0.4F).sound(SoundType.SAND)));
	public static final RegistryObject<Block> CARVED_HEAVY_PLANKS = REGISTER.register("carved_heavy_planks", () -> new Block(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).requiresCorrectToolForDrops().sound(SoundType.WOOD)));
	public static final RegistryObject<StairBlock> CARVED_HEAVY_PLANK_STAIRS = REGISTER.register("carved_heavy_plank_stairs", () -> new StairBlock(() -> MSBlocks.CARVED_HEAVY_PLANKS.get().defaultBlockState(), copy(CARVED_HEAVY_PLANKS.get())));
	public static final RegistryObject<SlabBlock> CARVED_HEAVY_PLANK_SLAB = REGISTER.register("carved_heavy_plank_slab", () -> new SlabBlock(copy(CARVED_HEAVY_PLANKS.get())));
	
	public static final RegistryObject<Block> CARVED_PLANKS = REGISTER.register("carved_planks", () -> new Block(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).requiresCorrectToolForDrops().sound(SoundType.WOOD)));
	public static final RegistryObject<StairBlock> CARVED_STAIRS = REGISTER.register("carved_stairs", () -> new StairBlock(() -> MSBlocks.CARVED_PLANKS.get().defaultBlockState(), copy(CARVED_PLANKS.get())));
	public static final RegistryObject<SlabBlock> CARVED_SLAB = REGISTER.register("carved_slab", () -> new SlabBlock(copy(CARVED_PLANKS.get())));
	public static final RegistryObject<ButtonBlock> CARVED_BUTTON = REGISTER.register("carved_button", () -> new ButtonBlock(copy(CARVED_PLANKS.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<PressurePlateBlock> CARVED_PRESSURE_PLATE = REGISTER.register("carved_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(CARVED_PLANKS.get()), BlockSetType.OAK));
	public static final RegistryObject<FenceBlock> CARVED_FENCE = REGISTER.register("carved_fence", () -> new FenceBlock(copy(CARVED_PLANKS.get())));
	public static final RegistryObject<FenceGateBlock> CARVED_FENCE_GATE = REGISTER.register("carved_fence_gate", () -> new FenceGateBlock(copy(CARVED_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final RegistryObject<DoorBlock> CARVED_DOOR = REGISTER.register("carved_door", () -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<TrapDoorBlock> CARVED_TRAPDOOR = REGISTER.register("carved_trapdoor", () -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	
	public static final RegistryObject<Block> POLISHED_UNCARVED_WOOD = REGISTER.register("polished_carved_wood", () -> new Block(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).requiresCorrectToolForDrops().sound(SoundType.WOOD)));
	public static final RegistryObject<StairBlock> POLISHED_UNCARVED_STAIRS = REGISTER.register("polished_uncarved_stairs", () -> new StairBlock(() -> MSBlocks.POLISHED_UNCARVED_WOOD.get().defaultBlockState(), copy(POLISHED_UNCARVED_WOOD.get())));
	public static final RegistryObject<SlabBlock> POLISHED_UNCARVED_SLAB = REGISTER.register("polished_uncarved_slab", () -> new SlabBlock(copy(POLISHED_UNCARVED_WOOD.get())));
	
	public static final RegistryObject<Block> CARVED_KNOTTED_WOOD = REGISTER.register("carved_knotted_wood", () -> new Block(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).requiresCorrectToolForDrops().sound(SoundType.WOOD)));
	
	//Cloud
	public static final RegistryObject<Block> DENSE_CLOUD = REGISTER.register("dense_cloud", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_YELLOW).instrument(NoteBlockInstrument.HAT).strength(0.5F).sound(SoundType.SNOW).isRedstoneConductor(MSBlocks::never)));
	public static final RegistryObject<Block> BRIGHT_DENSE_CLOUD = REGISTER.register("bright_dense_cloud", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).instrument(NoteBlockInstrument.HAT).strength(0.5F).sound(SoundType.SNOW).isRedstoneConductor(MSBlocks::never)));
	public static final RegistryObject<Block> SUGAR_CUBE = REGISTER.register("sugar_cube", () -> new Block(Block.Properties.of().mapColor(MapColor.SNOW).instrument(NoteBlockInstrument.SNARE).strength(0.4F).sound(SoundType.SAND)));
	
	//Land Tree Blocks
	//Glowing
	public static final RegistryObject<Block> GLOWING_LOG = REGISTER.register("glowing_log", () -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).lightLevel(state -> 11).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> GLOWING_WOOD = REGISTER.register("glowing_wood", () -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).lightLevel(state -> 11).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> GLOWING_PLANKS = REGISTER.register("glowing_planks", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).lightLevel(state -> 7).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> GLOWING_BOOKSHELF = REGISTER.register("glowing_bookshelf", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> GLOWING_LADDER = REGISTER.register("glowing_ladder", () -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<StairBlock> GLOWING_STAIRS = REGISTER.register("glowing_stairs", () -> new StairBlock(() -> MSBlocks.GLOWING_PLANKS.get().defaultBlockState(), copy(GLOWING_PLANKS.get())));
	public static final RegistryObject<SlabBlock> GLOWING_SLAB = REGISTER.register("glowing_slab", () -> new SlabBlock(copy(GLOWING_PLANKS.get())));
	public static final RegistryObject<ButtonBlock> GLOWING_BUTTON = REGISTER.register("glowing_button", () -> new ButtonBlock(copy(GLOWING_PLANKS.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<PressurePlateBlock> GLOWING_PRESSURE_PLATE = REGISTER.register("glowing_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(GLOWING_PLANKS.get()), BlockSetType.OAK));
	public static final RegistryObject<FenceBlock> GLOWING_FENCE = REGISTER.register("glowing_fence", () -> new FenceBlock(copy(GLOWING_PLANKS.get())));
	public static final RegistryObject<FenceGateBlock> GLOWING_FENCE_GATE = REGISTER.register("glowing_fence_gate", () -> new FenceGateBlock(copy(GLOWING_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final RegistryObject<DoorBlock> GLOWING_DOOR = REGISTER.register("glowing_door", () -> new DoorBlock(Block.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).lightLevel(state -> 7).sound(SoundType.WOOD), BlockSetType.OAK));
	public static final RegistryObject<TrapDoorBlock> GLOWING_TRAPDOOR = REGISTER.register("glowing_trapdoor", () -> new TrapDoorBlock(Block.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).lightLevel(state -> 7).sound(SoundType.WOOD), BlockSetType.OAK));
	
	//Frost
	public static final RegistryObject<Block> FROST_LOG = REGISTER.register("frost_log", () -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.ICE).strength(2.0F).ignitedByLava().instrument(NoteBlockInstrument.BASS).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> FROST_WOOD = REGISTER.register("frost_wood", () -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.ICE).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> FROST_PLANKS = REGISTER.register("frost_planks", () -> new FlammableBlock(5, 5, Block.Properties.of().mapColor(MapColor.ICE).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> FROST_LEAVES = REGISTER.register("frost_leaves", () -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(MSBlocks::leafSpawns).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never).isRedstoneConductor(MSBlocks::never)));
	public static final RegistryObject<Block> FROST_BOOKSHELF = REGISTER.register("frost_bookshelf", () -> new FlammableBlock(5, 5, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> FROST_LADDER = REGISTER.register("frost_ladder", () -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<StairBlock> FROST_STAIRS = REGISTER.register("frost_stairs", () -> new StairBlock(() -> MSBlocks.FROST_PLANKS.get().defaultBlockState(), copy(FROST_PLANKS.get())));
	public static final RegistryObject<SlabBlock> FROST_SLAB = REGISTER.register("frost_slab", () -> new SlabBlock(copy(FROST_PLANKS.get())));
	public static final RegistryObject<ButtonBlock> FROST_BUTTON = REGISTER.register("frost_button", () -> new ButtonBlock(copy(FROST_PLANKS.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<PressurePlateBlock> FROST_PRESSURE_PLATE = REGISTER.register("frost_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(FROST_PLANKS.get()), BlockSetType.OAK));
	public static final RegistryObject<FenceBlock> FROST_FENCE = REGISTER.register("frost_fence", () -> new FenceBlock(copy(FROST_PLANKS.get())));
	public static final RegistryObject<FenceGateBlock> FROST_FENCE_GATE = REGISTER.register("frost_fence_gate", () -> new FenceGateBlock(copy(FROST_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final RegistryObject<DoorBlock> FROST_DOOR = REGISTER.register("frost_door", () -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<TrapDoorBlock> FROST_TRAPDOOR = REGISTER.register("frost_trapdoor", () -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	
	//Rainbow
	public static final RegistryObject<Block> RAINBOW_LOG = REGISTER.register("rainbow_log", () -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> RAINBOW_WOOD = REGISTER.register("rainbow_wood", () -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> RAINBOW_PLANKS = REGISTER.register("rainbow_planks", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<StairBlock> RAINBOW_PLANKS_STAIRS = REGISTER.register("rainbow_planks_stairs", () -> new StairBlock(() -> MSBlocks.RAINBOW_PLANKS.get().defaultBlockState(), copy(RAINBOW_PLANKS.get())));
	public static final RegistryObject<SlabBlock> RAINBOW_PLANKS_SLAB = REGISTER.register("rainbow_planks_slab", () -> new SlabBlock(copy(RAINBOW_PLANKS.get())));
	public static final RegistryObject<Block> RAINBOW_LEAVES = REGISTER.register("rainbow_leaves", () -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(MSBlocks::leafSpawns).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never).isRedstoneConductor(MSBlocks::never)));
	public static final RegistryObject<BushBlock> RAINBOW_SAPLING = REGISTER.register("rainbow_sapling", () -> new RainbowSaplingBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<Block> RAINBOW_BOOKSHELF = REGISTER.register("rainbow_bookshelf", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> RAINBOW_LADDER = REGISTER.register("rainbow_ladder", () -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<ButtonBlock> RAINBOW_BUTTON = REGISTER.register("rainbow_button", () -> new ButtonBlock(copy(RAINBOW_PLANKS.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<PressurePlateBlock> RAINBOW_PRESSURE_PLATE = REGISTER.register("rainbow_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(RAINBOW_PLANKS.get()), BlockSetType.OAK));
	public static final RegistryObject<FenceBlock> RAINBOW_FENCE = REGISTER.register("rainbow_fence", () -> new FenceBlock(copy(RAINBOW_PLANKS.get())));
	public static final RegistryObject<FenceGateBlock> RAINBOW_FENCE_GATE = REGISTER.register("rainbow_fence_gate", () -> new FenceGateBlock(copy(RAINBOW_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final RegistryObject<DoorBlock> RAINBOW_DOOR = REGISTER.register("rainbow_door", () -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<TrapDoorBlock> RAINBOW_TRAPDOOR = REGISTER.register("rainbow_trapdoor", () -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	
	//End
	public static final RegistryObject<Block> END_LOG = REGISTER.register("end_log", () -> new DoubleLogBlock(1, 250, Block.Properties.of().mapColor(MapColor.SAND).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> END_WOOD = REGISTER.register("end_wood", () -> new FlammableLogBlock(1, 250, Block.Properties.of().mapColor(MapColor.SAND).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> END_PLANKS = REGISTER.register("end_planks", () -> new FlammableBlock(1, 250, Block.Properties.of().mapColor(MapColor.SAND).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<StairBlock> END_PLANKS_STAIRS = REGISTER.register("end_planks_stairs", () -> new StairBlock(() -> MSBlocks.END_PLANKS.get().defaultBlockState(), copy(END_PLANKS.get())));
	public static final RegistryObject<SlabBlock> END_PLANKS_SLAB = REGISTER.register("end_planks_slab", () -> new SlabBlock(copy(END_PLANKS.get())));
	public static final RegistryObject<Block> END_LEAVES = REGISTER.register("end_leaves", () -> new EndLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(MSBlocks::leafSpawns).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never).isRedstoneConductor(MSBlocks::never)));
	public static final RegistryObject<BushBlock> END_SAPLING = REGISTER.register("end_sapling", () -> new EndSaplingBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<Block> END_LADDER = REGISTER.register("end_ladder", () -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<Block> END_BOOKSHELF = REGISTER.register("end_bookshelf", () -> new FlammableBlock(1, 250, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<ButtonBlock> END_BUTTON = REGISTER.register("end_button", () -> new ButtonBlock(copy(END_PLANKS.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<PressurePlateBlock> END_PRESSURE_PLATE = REGISTER.register("end_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(END_PLANKS.get()), BlockSetType.OAK));
	public static final RegistryObject<FenceBlock> END_FENCE = REGISTER.register("end_fence", () -> new FenceBlock(copy(END_PLANKS.get())));
	public static final RegistryObject<FenceGateBlock> END_FENCE_GATE = REGISTER.register("end_fence_gate", () -> new FenceGateBlock(copy(END_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final RegistryObject<DoorBlock> END_DOOR = REGISTER.register("end_door", () -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<TrapDoorBlock> END_TRAPDOOR = REGISTER.register("end_trapdoor", () -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	
	//Vine
	public static final RegistryObject<Block> VINE_LOG = REGISTER.register("vine_log", () -> new FlammableLogBlock(Block.Properties.of().mapColor(logColors(MapColor.WOOD, MapColor.PODZOL)).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> VINE_WOOD = REGISTER.register("vine_wood", () -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.PODZOL).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	
	public static final RegistryObject<Block> FLOWERY_VINE_LOG = REGISTER.register("flowery_vine_log", () -> new FlammableLogBlock(Block.Properties.of().mapColor(logColors(MapColor.WOOD, MapColor.PODZOL)).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> FLOWERY_VINE_WOOD = REGISTER.register("flowery_vine_wood", () -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.PODZOL).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	
	//Dead
	public static final RegistryObject<Block> DEAD_LOG = REGISTER.register("dead_log", () -> new FlammableLogBlock(Block.Properties.of().mapColor(logColors(MapColor.WOOD, MapColor.PODZOL)).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> DEAD_WOOD = REGISTER.register("dead_wood", () -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.PODZOL).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> DEAD_PLANKS = REGISTER.register("dead_planks", () -> new FlammableBlock(5, 5, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<StairBlock> DEAD_PLANKS_STAIRS = REGISTER.register("dead_planks_stairs", () -> new StairBlock(() -> MSBlocks.DEAD_PLANKS.get().defaultBlockState(), copy(DEAD_PLANKS.get())));
	public static final RegistryObject<SlabBlock> DEAD_PLANKS_SLAB = REGISTER.register("dead_planks_slab", () -> new SlabBlock(copy(DEAD_PLANKS.get())));
	public static final RegistryObject<Block> DEAD_BOOKSHELF = REGISTER.register("dead_bookshelf", () -> new FlammableBlock(5, 5, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> DEAD_LADDER = REGISTER.register("dead_ladder", () -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<ButtonBlock> DEAD_BUTTON = REGISTER.register("dead_button", () -> new ButtonBlock(copy(DEAD_PLANKS.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<PressurePlateBlock> DEAD_PRESSURE_PLATE = REGISTER.register("dead_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(DEAD_PLANKS.get()), BlockSetType.OAK));
	public static final RegistryObject<FenceBlock> DEAD_FENCE = REGISTER.register("dead_fence", () -> new FenceBlock(copy(DEAD_PLANKS.get())));
	public static final RegistryObject<FenceGateBlock> DEAD_FENCE_GATE = REGISTER.register("dead_fence_gate", () -> new FenceGateBlock(copy(DEAD_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final RegistryObject<DoorBlock> DEAD_DOOR = REGISTER.register("dead_door", () -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<TrapDoorBlock> DEAD_TRAPDOOR = REGISTER.register("dead_trapdoor", () -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	
	//Petrified
	public static final RegistryObject<Block> PETRIFIED_LOG = REGISTER.register("petrified_log", () -> new RotatedPillarBlock(Block.Properties.of().mapColor(logColors(MapColor.WOOD, MapColor.PODZOL)).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> PETRIFIED_WOOD = REGISTER.register("petrified_wood", () -> new RotatedPillarBlock(Block.Properties.of().mapColor(MapColor.PODZOL).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.STONE)));
	
	//Shadewood
	public static final RegistryObject<Block> SHADEWOOD_LOG = REGISTER.register("shadewood_log", () -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD), () -> MSBlocks.STRIPPED_SHADEWOOD_LOG.get().defaultBlockState()));
	public static final RegistryObject<Block> SHADEWOOD = REGISTER.register("shadewood", () -> new StrippableFlammableLogBlock(copy(SHADEWOOD_LOG.get()), () -> MSBlocks.STRIPPED_SHADEWOOD.get().defaultBlockState()));
	public static final RegistryObject<Block> SHADEWOOD_PLANKS = REGISTER.register("shadewood_planks", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<StairBlock> SHADEWOOD_STAIRS = REGISTER.register("shadewood_stairs", () -> new StairBlock(() -> MSBlocks.SHADEWOOD_PLANKS.get().defaultBlockState(), copy(SHADEWOOD_PLANKS.get())));
	public static final RegistryObject<SlabBlock> SHADEWOOD_SLAB = REGISTER.register("shadewood_slab", () -> new SlabBlock(copy(SHADEWOOD_PLANKS.get())));
	public static final RegistryObject<ButtonBlock> SHADEWOOD_BUTTON = REGISTER.register("shadewood_button", () -> new ButtonBlock(copy(SHADEWOOD_PLANKS.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<PressurePlateBlock> SHADEWOOD_PRESSURE_PLATE = REGISTER.register("shadewood_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(SHADEWOOD_PLANKS.get()), BlockSetType.OAK));
	public static final RegistryObject<FenceBlock> SHADEWOOD_FENCE = REGISTER.register("shadewood_fence", () -> new FenceBlock(copy(SHADEWOOD_PLANKS.get())));
	public static final RegistryObject<FenceGateBlock> SHADEWOOD_FENCE_GATE = REGISTER.register("shadewood_fence_gate", () -> new FenceGateBlock(copy(SHADEWOOD_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final RegistryObject<DoorBlock> SHADEWOOD_DOOR = REGISTER.register("shadewood_door", () -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<TrapDoorBlock> SHADEWOOD_TRAPDOOR = REGISTER.register("shadewood_trapdoor", () -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	
	public static final RegistryObject<Block> SHADEWOOD_LEAVES = REGISTER.register("shadewood_leaves", () -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(MSBlocks::leafSpawns).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never).isRedstoneConductor(MSBlocks::never)));
	public static final RegistryObject<Block> SHROOMY_SHADEWOOD_LEAVES = REGISTER.register("shroomy_shadewood_leaves", () -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().lightLevel(state -> 11).sound(SoundType.GRASS).noOcclusion().isValidSpawn(MSBlocks::leafSpawns).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never).isRedstoneConductor(MSBlocks::never)));
	
	public static final RegistryObject<BushBlock> SHADEWOOD_SAPLING = REGISTER.register("shadewood_sapling", () -> new SaplingBlock(new ShadewoodTree(), Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().lightLevel(state -> 8).strength(0).sound(SoundType.GRASS)));
	
	public static final RegistryObject<Block> SCARRED_SHADEWOOD_LOG = REGISTER.register("scarred_shadewood_log", () -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).lightLevel(state -> 6).sound(SoundType.WOOD), () -> MSBlocks.STRIPPED_SCARRED_SHADEWOOD_LOG.get().defaultBlockState()));
	public static final RegistryObject<Block> SCARRED_SHADEWOOD = REGISTER.register("scarred_shadewood", () -> new StrippableFlammableLogBlock(copy(SCARRED_SHADEWOOD_LOG.get()), () -> MSBlocks.STRIPPED_SCARRED_SHADEWOOD.get().defaultBlockState()));
	
	public static final RegistryObject<Block> ROTTED_SHADEWOOD_LOG = REGISTER.register("rotted_shadewood_log", () -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).lightLevel(state -> 5).sound(SoundType.WOOD), () -> MSBlocks.STRIPPED_ROTTED_SHADEWOOD_LOG.get().defaultBlockState()));
	public static final RegistryObject<Block> ROTTED_SHADEWOOD = REGISTER.register("rotted_shadewood", () -> new StrippableFlammableLogBlock(copy(ROTTED_SHADEWOOD_LOG.get()), () -> MSBlocks.STRIPPED_ROTTED_SHADEWOOD.get().defaultBlockState()));
	
	public static final RegistryObject<Block> STRIPPED_SHADEWOOD_LOG = REGISTER.register("stripped_shadewood_log", () -> new FlammableLogBlock(copy(SHADEWOOD_LOG.get())));
	public static final RegistryObject<Block> STRIPPED_SHADEWOOD = REGISTER.register("stripped_shadewood", () -> new FlammableLogBlock(copy(SHADEWOOD_LOG.get())));
	
	public static final RegistryObject<Block> STRIPPED_SCARRED_SHADEWOOD_LOG = REGISTER.register("stripped_scarred_shadewood_log", () -> new FlammableLogBlock(copy(SCARRED_SHADEWOOD_LOG.get())));
	public static final RegistryObject<Block> STRIPPED_SCARRED_SHADEWOOD = REGISTER.register("stripped_scarred_shadewood", () -> new FlammableLogBlock(copy(SCARRED_SHADEWOOD_LOG.get())));
	
	public static final RegistryObject<Block> STRIPPED_ROTTED_SHADEWOOD_LOG = REGISTER.register("stripped_rotted_shadewood_log", () -> new FlammableLogBlock(copy(ROTTED_SHADEWOOD_LOG.get())));
	public static final RegistryObject<Block> STRIPPED_ROTTED_SHADEWOOD = REGISTER.register("stripped_rotted_shadewood", () -> new FlammableLogBlock(copy(ROTTED_SHADEWOOD_LOG.get())));
	
	//Treated
	public static final RegistryObject<Block> TREATED_PLANKS = REGISTER.register("treated_planks", () -> new FlammableBlock(0, 0, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<StairBlock> TREATED_PLANKS_STAIRS = REGISTER.register("treated_planks_stairs", () -> new StairBlock(() -> MSBlocks.TREATED_PLANKS.get().defaultBlockState(), copy(TREATED_PLANKS.get())));
	public static final RegistryObject<SlabBlock> TREATED_PLANKS_SLAB = REGISTER.register("treated_planks_slab", () -> new SlabBlock(copy(TREATED_PLANKS.get())));
	public static final RegistryObject<Block> TREATED_BOOKSHELF = REGISTER.register("treated_bookshelf", () -> new FlammableBlock(0, 0, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> TREATED_LADDER = REGISTER.register("treated_ladder", () -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<ButtonBlock> TREATED_BUTTON = REGISTER.register("treated_button", () -> new ButtonBlock(copy(TREATED_PLANKS.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<PressurePlateBlock> TREATED_PRESSURE_PLATE = REGISTER.register("treated_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(TREATED_PLANKS.get()), BlockSetType.OAK));
	public static final RegistryObject<FenceBlock> TREATED_FENCE = REGISTER.register("treated_fence", () -> new FenceBlock(copy(TREATED_PLANKS.get())));
	public static final RegistryObject<FenceGateBlock> TREATED_FENCE_GATE = REGISTER.register("treated_fence_gate", () -> new FenceGateBlock(copy(TREATED_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final RegistryObject<DoorBlock> TREATED_DOOR = REGISTER.register("treated_door", () -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<TrapDoorBlock> TREATED_TRAPDOOR = REGISTER.register("treated_trapdoor", () -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	
	//Aspect tree blocks
	//Blood
	public static final RegistryObject<Block> BLOOD_ASPECT_BOOKSHELF = REGISTER.register("blood_aspect_bookshelf", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> BLOOD_ASPECT_LADDER = REGISTER.register("blood_aspect_ladder", () -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<Block> BLOOD_ASPECT_LEAVES = REGISTER.register("blood_aspect_leaves", () -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final RegistryObject<Block> BLOOD_ASPECT_LOG = REGISTER.register("blood_aspect_log", () -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> BLOOD_ASPECT_PLANKS = REGISTER.register("blood_aspect_planks", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> BLOOD_ASPECT_SAPLING = REGISTER.register("blood_aspect_sapling", () -> new AspectSaplingBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<StairBlock> BLOOD_ASPECT_STAIRS = REGISTER.register("blood_aspect_stairs", () -> new StairBlock(() -> MSBlocks.BLOOD_ASPECT_PLANKS.get().defaultBlockState(), copy(BLOOD_ASPECT_PLANKS.get())));
	public static final RegistryObject<SlabBlock> BLOOD_ASPECT_SLAB = REGISTER.register("blood_aspect_slab", () -> new SlabBlock(copy(BLOOD_ASPECT_PLANKS.get())));
	public static final RegistryObject<ButtonBlock> BLOOD_ASPECT_BUTTON = REGISTER.register("blood_aspect_button", () -> new ButtonBlock(copy(BLOOD_ASPECT_PLANKS.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<PressurePlateBlock> BLOOD_ASPECT_PRESSURE_PLATE = REGISTER.register("blood_aspect_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(BLOOD_ASPECT_PLANKS.get()), BlockSetType.OAK));
	public static final RegistryObject<FenceBlock> BLOOD_ASPECT_FENCE = REGISTER.register("blood_aspect_fence", () -> new FenceBlock(copy(BLOOD_ASPECT_PLANKS.get())));
	public static final RegistryObject<FenceGateBlock> BLOOD_ASPECT_FENCE_GATE = REGISTER.register("blood_aspect_fence_gate", () -> new FenceGateBlock(copy(BLOOD_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final RegistryObject<DoorBlock> BLOOD_ASPECT_DOOR = REGISTER.register("blood_aspect_door", () -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<TrapDoorBlock> BLOOD_ASPECT_TRAPDOOR = REGISTER.register("blood_aspect_trapdoor", () -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	
	//Breath
	public static final RegistryObject<Block> BREATH_ASPECT_BOOKSHELF = REGISTER.register("breath_aspect_bookshelf", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> BREATH_ASPECT_LADDER = REGISTER.register("breath_aspect_ladder", () -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<Block> BREATH_ASPECT_LEAVES = REGISTER.register("breath_aspect_leaves", () -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final RegistryObject<Block> BREATH_ASPECT_LOG = REGISTER.register("breath_aspect_log", () -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> BREATH_ASPECT_PLANKS = REGISTER.register("breath_aspect_planks", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> BREATH_ASPECT_SAPLING = REGISTER.register("breath_aspect_sapling", () -> new AspectSaplingBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<StairBlock> BREATH_ASPECT_STAIRS = REGISTER.register("breath_aspect_stairs", () -> new StairBlock(() -> MSBlocks.BREATH_ASPECT_PLANKS.get().defaultBlockState(), copy(BREATH_ASPECT_PLANKS.get())));
	public static final RegistryObject<SlabBlock> BREATH_ASPECT_SLAB = REGISTER.register("breath_aspect_slab", () -> new SlabBlock(copy(BREATH_ASPECT_PLANKS.get())));
	public static final RegistryObject<ButtonBlock> BREATH_ASPECT_BUTTON = REGISTER.register("breath_aspect_button", () -> new ButtonBlock(copy(BREATH_ASPECT_PLANKS.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<PressurePlateBlock> BREATH_ASPECT_PRESSURE_PLATE = REGISTER.register("breath_aspect_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(BREATH_ASPECT_PLANKS.get()), BlockSetType.OAK));
	public static final RegistryObject<FenceBlock> BREATH_ASPECT_FENCE = REGISTER.register("breath_aspect_fence", () -> new FenceBlock(copy(BREATH_ASPECT_PLANKS.get())));
	public static final RegistryObject<FenceGateBlock> BREATH_ASPECT_FENCE_GATE = REGISTER.register("breath_aspect_fence_gate", () -> new FenceGateBlock(copy(BREATH_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final RegistryObject<DoorBlock> BREATH_ASPECT_DOOR = REGISTER.register("breath_aspect_door", () -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<TrapDoorBlock> BREATH_ASPECT_TRAPDOOR = REGISTER.register("breath_aspect_trapdoor", () -> new TrapDoorBlock(copy(BREATH_ASPECT_PLANKS.get()), BlockSetType.OAK));
	
	//Doom
	public static final RegistryObject<Block> DOOM_ASPECT_BOOKSHELF = REGISTER.register("doom_aspect_bookshelf", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> DOOM_ASPECT_LADDER = REGISTER.register("doom_aspect_ladder", () -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<Block> DOOM_ASPECT_LEAVES = REGISTER.register("doom_aspect_leaves", () -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final RegistryObject<Block> DOOM_ASPECT_LOG = REGISTER.register("doom_aspect_log", () -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> DOOM_ASPECT_PLANKS = REGISTER.register("doom_aspect_planks", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> DOOM_ASPECT_SAPLING = REGISTER.register("doom_aspect_sapling", () -> new AspectSaplingBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<StairBlock> DOOM_ASPECT_STAIRS = REGISTER.register("doom_aspect_stairs", () -> new StairBlock(() -> MSBlocks.DOOM_ASPECT_PLANKS.get().defaultBlockState(), copy(DOOM_ASPECT_PLANKS.get())));
	public static final RegistryObject<SlabBlock> DOOM_ASPECT_SLAB = REGISTER.register("doom_aspect_slab", () -> new SlabBlock(copy(DOOM_ASPECT_PLANKS.get())));
	public static final RegistryObject<ButtonBlock> DOOM_ASPECT_BUTTON = REGISTER.register("doom_aspect_button", () -> new ButtonBlock(copy(DOOM_ASPECT_PLANKS.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<PressurePlateBlock> DOOM_ASPECT_PRESSURE_PLATE = REGISTER.register("doom_aspect_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(DOOM_ASPECT_PLANKS.get()), BlockSetType.OAK));
	public static final RegistryObject<FenceBlock> DOOM_ASPECT_FENCE = REGISTER.register("doom_aspect_fence", () -> new FenceBlock(copy(DOOM_ASPECT_PLANKS.get())));
	public static final RegistryObject<FenceGateBlock> DOOM_ASPECT_FENCE_GATE = REGISTER.register("doom_aspect_fence_gate", () -> new FenceGateBlock(copy(DOOM_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final RegistryObject<DoorBlock> DOOM_ASPECT_DOOR = REGISTER.register("doom_aspect_door", () -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<TrapDoorBlock> DOOM_ASPECT_TRAPDOOR = REGISTER.register("doom_aspect_trapdoor", () -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	
	//Heart
	public static final RegistryObject<Block> HEART_ASPECT_BOOKSHELF = REGISTER.register("heart_aspect_bookshelf", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> HEART_ASPECT_LADDER = REGISTER.register("heart_aspect_ladder", () -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<Block> HEART_ASPECT_LEAVES = REGISTER.register("heart_aspect_leaves", () -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final RegistryObject<Block> HEART_ASPECT_LOG = REGISTER.register("heart_aspect_log", () -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> HEART_ASPECT_PLANKS = REGISTER.register("heart_aspect_planks", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> HEART_ASPECT_SAPLING = REGISTER.register("heart_aspect_sapling", () -> new AspectSaplingBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<StairBlock> HEART_ASPECT_STAIRS = REGISTER.register("heart_aspect_stairs", () -> new StairBlock(() -> MSBlocks.HEART_ASPECT_PLANKS.get().defaultBlockState(), copy(HEART_ASPECT_PLANKS.get())));
	public static final RegistryObject<SlabBlock> HEART_ASPECT_SLAB = REGISTER.register("heart_aspect_slab", () -> new SlabBlock(copy(HEART_ASPECT_PLANKS.get())));
	public static final RegistryObject<ButtonBlock> HEART_ASPECT_BUTTON = REGISTER.register("heart_aspect_button", () -> new ButtonBlock(copy(HEART_ASPECT_PLANKS.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<PressurePlateBlock> HEART_ASPECT_PRESSURE_PLATE = REGISTER.register("heart_aspect_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(HEART_ASPECT_PLANKS.get()), BlockSetType.OAK));
	public static final RegistryObject<FenceBlock> HEART_ASPECT_FENCE = REGISTER.register("heart_aspect_fence", () -> new FenceBlock(copy(HEART_ASPECT_PLANKS.get())));
	public static final RegistryObject<FenceGateBlock> HEART_ASPECT_FENCE_GATE = REGISTER.register("heart_aspect_fence_gate", () -> new FenceGateBlock(copy(HEART_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final RegistryObject<DoorBlock> HEART_ASPECT_DOOR = REGISTER.register("heart_aspect_door", () -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<TrapDoorBlock> HEART_ASPECT_TRAPDOOR = REGISTER.register("heart_aspect_trapdoor", () -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	
	//Hope
	public static final RegistryObject<Block> HOPE_ASPECT_BOOKSHELF = REGISTER.register("hope_aspect_bookshelf", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> HOPE_ASPECT_LADDER = REGISTER.register("hope_aspect_ladder", () -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<Block> HOPE_ASPECT_LEAVES = REGISTER.register("hope_aspect_leaves", () -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final RegistryObject<Block> HOPE_ASPECT_LOG = REGISTER.register("hope_aspect_log", () -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> HOPE_ASPECT_PLANKS = REGISTER.register("hope_aspect_planks", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> HOPE_ASPECT_SAPLING = REGISTER.register("hope_aspect_sapling", () -> new AspectSaplingBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<StairBlock> HOPE_ASPECT_STAIRS = REGISTER.register("hope_aspect_stairs", () -> new StairBlock(() -> MSBlocks.HOPE_ASPECT_PLANKS.get().defaultBlockState(), copy(HOPE_ASPECT_PLANKS.get())));
	public static final RegistryObject<SlabBlock> HOPE_ASPECT_SLAB = REGISTER.register("hope_aspect_slab", () -> new SlabBlock(copy(HOPE_ASPECT_PLANKS.get())));
	public static final RegistryObject<ButtonBlock> HOPE_ASPECT_BUTTON = REGISTER.register("hope_aspect_button", () -> new ButtonBlock(copy(HOPE_ASPECT_PLANKS.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<PressurePlateBlock> HOPE_ASPECT_PRESSURE_PLATE = REGISTER.register("hope_aspect_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(HOPE_ASPECT_PLANKS.get()), BlockSetType.OAK));
	public static final RegistryObject<FenceBlock> HOPE_ASPECT_FENCE = REGISTER.register("hope_aspect_fence", () -> new FenceBlock(copy(HOPE_ASPECT_PLANKS.get())));
	public static final RegistryObject<FenceGateBlock> HOPE_ASPECT_FENCE_GATE = REGISTER.register("hope_aspect_fence_gate", () -> new FenceGateBlock(copy(HOPE_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final RegistryObject<DoorBlock> HOPE_ASPECT_DOOR = REGISTER.register("hope_aspect_door", () -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<TrapDoorBlock> HOPE_ASPECT_TRAPDOOR = REGISTER.register("hope_aspect_trapdoor", () -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	
	//Life
	public static final RegistryObject<Block> LIFE_ASPECT_BOOKSHELF = REGISTER.register("life_aspect_bookshelf", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> LIFE_ASPECT_LADDER = REGISTER.register("life_aspect_ladder", () -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<Block> LIFE_ASPECT_LEAVES = REGISTER.register("life_aspect_leaves", () -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final RegistryObject<Block> LIFE_ASPECT_LOG = REGISTER.register("life_aspect_log", () -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> LIFE_ASPECT_PLANKS = REGISTER.register("life_aspect_planks", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> LIFE_ASPECT_SAPLING = REGISTER.register("life_aspect_sapling", () -> new AspectSaplingBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<StairBlock> LIFE_ASPECT_STAIRS = REGISTER.register("life_aspect_stairs", () -> new StairBlock(() -> MSBlocks.LIFE_ASPECT_PLANKS.get().defaultBlockState(), copy(LIFE_ASPECT_PLANKS.get())));
	public static final RegistryObject<SlabBlock> LIFE_ASPECT_SLAB = REGISTER.register("life_aspect_slab", () -> new SlabBlock(copy(LIFE_ASPECT_PLANKS.get())));
	public static final RegistryObject<ButtonBlock> LIFE_ASPECT_BUTTON = REGISTER.register("life_aspect_button", () -> new ButtonBlock(copy(LIFE_ASPECT_PLANKS.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<PressurePlateBlock> LIFE_ASPECT_PRESSURE_PLATE = REGISTER.register("life_aspect_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(LIFE_ASPECT_PLANKS.get()), BlockSetType.OAK));
	public static final RegistryObject<FenceBlock> LIFE_ASPECT_FENCE = REGISTER.register("life_aspect_fence", () -> new FenceBlock(copy(LIFE_ASPECT_PLANKS.get())));
	public static final RegistryObject<FenceGateBlock> LIFE_ASPECT_FENCE_GATE = REGISTER.register("life_aspect_fence_gate", () -> new FenceGateBlock(copy(LIFE_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final RegistryObject<DoorBlock> LIFE_ASPECT_DOOR = REGISTER.register("life_aspect_door", () -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<TrapDoorBlock> LIFE_ASPECT_TRAPDOOR = REGISTER.register("life_aspect_trapdoor", () -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	
	//Light
	public static final RegistryObject<Block> LIGHT_ASPECT_BOOKSHELF = REGISTER.register("light_aspect_bookshelf", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> LIGHT_ASPECT_LADDER = REGISTER.register("light_aspect_ladder", () -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<Block> LIGHT_ASPECT_LEAVES = REGISTER.register("light_aspect_leaves", () -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final RegistryObject<Block> LIGHT_ASPECT_LOG = REGISTER.register("light_aspect_log", () -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> LIGHT_ASPECT_PLANKS = REGISTER.register("light_aspect_planks", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> LIGHT_ASPECT_SAPLING = REGISTER.register("light_aspect_sapling", () -> new AspectSaplingBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<StairBlock> LIGHT_ASPECT_STAIRS = REGISTER.register("light_aspect_stairs", () -> new StairBlock(() -> MSBlocks.LIGHT_ASPECT_PLANKS.get().defaultBlockState(), copy(LIGHT_ASPECT_PLANKS.get())));
	public static final RegistryObject<SlabBlock> LIGHT_ASPECT_SLAB = REGISTER.register("light_aspect_slab", () -> new SlabBlock(copy(LIGHT_ASPECT_PLANKS.get())));
	public static final RegistryObject<ButtonBlock> LIGHT_ASPECT_BUTTON = REGISTER.register("light_aspect_button", () -> new ButtonBlock(copy(LIGHT_ASPECT_PLANKS.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<PressurePlateBlock> LIGHT_ASPECT_PRESSURE_PLATE = REGISTER.register("light_aspect_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(LIGHT_ASPECT_PLANKS.get()), BlockSetType.OAK));
	public static final RegistryObject<FenceBlock> LIGHT_ASPECT_FENCE = REGISTER.register("light_aspect_fence", () -> new FenceBlock(copy(LIGHT_ASPECT_PLANKS.get())));
	public static final RegistryObject<FenceGateBlock> LIGHT_ASPECT_FENCE_GATE = REGISTER.register("light_aspect_fence_gate", () -> new FenceGateBlock(copy(LIGHT_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final RegistryObject<DoorBlock> LIGHT_ASPECT_DOOR = REGISTER.register("light_aspect_door", () -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<TrapDoorBlock> LIGHT_ASPECT_TRAPDOOR = REGISTER.register("light_aspect_trapdoor", () -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	
	//Mind
	public static final RegistryObject<Block> MIND_ASPECT_BOOKSHELF = REGISTER.register("mind_aspect_bookshelf", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> MIND_ASPECT_LADDER = REGISTER.register("mind_aspect_ladder", () -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<Block> MIND_ASPECT_LEAVES = REGISTER.register("mind_aspect_leaves", () -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final RegistryObject<Block> MIND_ASPECT_LOG = REGISTER.register("mind_aspect_log", () -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> MIND_ASPECT_PLANKS = REGISTER.register("mind_aspect_planks", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> MIND_ASPECT_SAPLING = REGISTER.register("mind_aspect_sapling", () -> new AspectSaplingBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<StairBlock> MIND_ASPECT_STAIRS = REGISTER.register("mind_aspect_stairs", () -> new StairBlock(() -> MSBlocks.MIND_ASPECT_PLANKS.get().defaultBlockState(), copy(MIND_ASPECT_PLANKS.get())));
	public static final RegistryObject<SlabBlock> MIND_ASPECT_SLAB = REGISTER.register("mind_aspect_slab", () -> new SlabBlock(copy(MIND_ASPECT_PLANKS.get())));
	public static final RegistryObject<ButtonBlock> MIND_ASPECT_BUTTON = REGISTER.register("mind_aspect_button", () -> new ButtonBlock(copy(MIND_ASPECT_PLANKS.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<PressurePlateBlock> MIND_ASPECT_PRESSURE_PLATE = REGISTER.register("mind_aspect_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(MIND_ASPECT_PLANKS.get()), BlockSetType.OAK));
	public static final RegistryObject<FenceBlock> MIND_ASPECT_FENCE = REGISTER.register("mind_aspect_fence", () -> new FenceBlock(copy(MIND_ASPECT_PLANKS.get())));
	public static final RegistryObject<FenceGateBlock> MIND_ASPECT_FENCE_GATE = REGISTER.register("mind_aspect_fence_gate", () -> new FenceGateBlock(copy(MIND_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final RegistryObject<DoorBlock> MIND_ASPECT_DOOR = REGISTER.register("mind_aspect_door", () -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<TrapDoorBlock> MIND_ASPECT_TRAPDOOR = REGISTER.register("mind_aspect_trapdoor", () -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	
	//Rage
	public static final RegistryObject<Block> RAGE_ASPECT_BOOKSHELF = REGISTER.register("rage_aspect_bookshelf", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> RAGE_ASPECT_LADDER = REGISTER.register("rage_aspect_ladder", () -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<Block> RAGE_ASPECT_LEAVES = REGISTER.register("rage_aspect_leaves", () -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final RegistryObject<Block> RAGE_ASPECT_LOG = REGISTER.register("rage_aspect_log", () -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> RAGE_ASPECT_PLANKS = REGISTER.register("rage_aspect_planks", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> RAGE_ASPECT_SAPLING = REGISTER.register("rage_aspect_sapling", () -> new AspectSaplingBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<StairBlock> RAGE_ASPECT_STAIRS = REGISTER.register("rage_aspect_stairs", () -> new StairBlock(() -> MSBlocks.RAGE_ASPECT_PLANKS.get().defaultBlockState(), copy(RAGE_ASPECT_PLANKS.get())));
	public static final RegistryObject<SlabBlock> RAGE_ASPECT_SLAB = REGISTER.register("rage_aspect_slab", () -> new SlabBlock(copy(RAGE_ASPECT_PLANKS.get())));
	public static final RegistryObject<ButtonBlock> RAGE_ASPECT_BUTTON = REGISTER.register("rage_aspect_button", () -> new ButtonBlock(copy(RAGE_ASPECT_PLANKS.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<PressurePlateBlock> RAGE_ASPECT_PRESSURE_PLATE = REGISTER.register("rage_aspect_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(RAGE_ASPECT_PLANKS.get()), BlockSetType.OAK));
	public static final RegistryObject<FenceBlock> RAGE_ASPECT_FENCE = REGISTER.register("rage_aspect_fence", () -> new FenceBlock(copy(RAGE_ASPECT_PLANKS.get())));
	public static final RegistryObject<FenceGateBlock> RAGE_ASPECT_FENCE_GATE = REGISTER.register("rage_aspect_fence_gate", () -> new FenceGateBlock(copy(RAGE_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final RegistryObject<DoorBlock> RAGE_ASPECT_DOOR = REGISTER.register("rage_aspect_door", () -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<TrapDoorBlock> RAGE_ASPECT_TRAPDOOR = REGISTER.register("rage_aspect_trapdoor", () -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	
	//Space
	public static final RegistryObject<Block> SPACE_ASPECT_BOOKSHELF = REGISTER.register("space_aspect_bookshelf", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> SPACE_ASPECT_LADDER = REGISTER.register("space_aspect_ladder", () -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<Block> SPACE_ASPECT_LEAVES = REGISTER.register("space_aspect_leaves", () -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final RegistryObject<Block> SPACE_ASPECT_LOG = REGISTER.register("space_aspect_log", () -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> SPACE_ASPECT_PLANKS = REGISTER.register("space_aspect_planks", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> SPACE_ASPECT_SAPLING = REGISTER.register("space_aspect_sapling", () -> new AspectSaplingBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<StairBlock> SPACE_ASPECT_STAIRS = REGISTER.register("space_aspect_stairs", () -> new StairBlock(() -> MSBlocks.SPACE_ASPECT_PLANKS.get().defaultBlockState(), copy(SPACE_ASPECT_PLANKS.get())));
	public static final RegistryObject<SlabBlock> SPACE_ASPECT_SLAB = REGISTER.register("space_aspect_slab", () -> new SlabBlock(copy(SPACE_ASPECT_PLANKS.get())));
	public static final RegistryObject<ButtonBlock> SPACE_ASPECT_BUTTON = REGISTER.register("space_aspect_button", () -> new ButtonBlock(copy(SPACE_ASPECT_PLANKS.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<PressurePlateBlock> SPACE_ASPECT_PRESSURE_PLATE = REGISTER.register("space_aspect_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(SPACE_ASPECT_PLANKS.get()), BlockSetType.OAK));
	public static final RegistryObject<FenceBlock> SPACE_ASPECT_FENCE = REGISTER.register("space_aspect_fence", () -> new FenceBlock(copy(SPACE_ASPECT_PLANKS.get())));
	public static final RegistryObject<FenceGateBlock> SPACE_ASPECT_FENCE_GATE = REGISTER.register("space_aspect_fence_gate", () -> new FenceGateBlock(copy(SPACE_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final RegistryObject<DoorBlock> SPACE_ASPECT_DOOR = REGISTER.register("space_aspect_door", () -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<TrapDoorBlock> SPACE_ASPECT_TRAPDOOR = REGISTER.register("space_aspect_trapdoor", () -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	
	//Time
	public static final RegistryObject<Block> TIME_ASPECT_BOOKSHELF = REGISTER.register("time_aspect_bookshelf", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> TIME_ASPECT_LADDER = REGISTER.register("time_aspect_ladder", () -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<Block> TIME_ASPECT_LEAVES = REGISTER.register("time_aspect_leaves", () -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final RegistryObject<Block> TIME_ASPECT_LOG = REGISTER.register("time_aspect_log", () -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> TIME_ASPECT_PLANKS = REGISTER.register("time_aspect_planks", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> TIME_ASPECT_SAPLING = REGISTER.register("time_aspect_sapling", () -> new AspectSaplingBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<StairBlock> TIME_ASPECT_STAIRS = REGISTER.register("time_aspect_stairs", () -> new StairBlock(() -> MSBlocks.TIME_ASPECT_PLANKS.get().defaultBlockState(), copy(TIME_ASPECT_PLANKS.get())));
	public static final RegistryObject<SlabBlock> TIME_ASPECT_SLAB = REGISTER.register("time_aspect_slab", () -> new SlabBlock(copy(TIME_ASPECT_PLANKS.get())));
	public static final RegistryObject<ButtonBlock> TIME_ASPECT_BUTTON = REGISTER.register("time_aspect_button", () -> new ButtonBlock(copy(TIME_ASPECT_PLANKS.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<PressurePlateBlock> TIME_ASPECT_PRESSURE_PLATE = REGISTER.register("time_aspect_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(TIME_ASPECT_PLANKS.get()), BlockSetType.OAK));
	public static final RegistryObject<FenceBlock> TIME_ASPECT_FENCE = REGISTER.register("time_aspect_fence", () -> new FenceBlock(copy(TIME_ASPECT_PLANKS.get())));
	public static final RegistryObject<FenceGateBlock> TIME_ASPECT_FENCE_GATE = REGISTER.register("time_aspect_fence_gate", () -> new FenceGateBlock(copy(TIME_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final RegistryObject<DoorBlock> TIME_ASPECT_DOOR = REGISTER.register("time_aspect_door", () -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<TrapDoorBlock> TIME_ASPECT_TRAPDOOR = REGISTER.register("time_aspect_trapdoor", () -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	
	//Void
	public static final RegistryObject<Block> VOID_ASPECT_BOOKSHELF = REGISTER.register("void_aspect_bookshelf", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> VOID_ASPECT_LADDER = REGISTER.register("void_aspect_ladder", () -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<Block> VOID_ASPECT_LEAVES = REGISTER.register("void_aspect_leaves", () -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isRedstoneConductor(MSBlocks::never)));
	public static final RegistryObject<Block> VOID_ASPECT_LOG = REGISTER.register("void_aspect_log", () -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> VOID_ASPECT_PLANKS = REGISTER.register("void_aspect_planks", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> VOID_ASPECT_SAPLING = REGISTER.register("void_aspect_sapling", () -> new AspectSaplingBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<StairBlock> VOID_ASPECT_STAIRS = REGISTER.register("void_aspect_stairs", () -> new StairBlock(() -> MSBlocks.VOID_ASPECT_PLANKS.get().defaultBlockState(), copy(VOID_ASPECT_PLANKS.get())));
	public static final RegistryObject<SlabBlock> VOID_ASPECT_SLAB = REGISTER.register("void_aspect_slab", () -> new SlabBlock(copy(VOID_ASPECT_PLANKS.get())));
	public static final RegistryObject<ButtonBlock> VOID_ASPECT_BUTTON = REGISTER.register("void_aspect_button", () -> new ButtonBlock(copy(VOID_ASPECT_PLANKS.get()), BlockSetType.OAK, 10, true));
	public static final RegistryObject<PressurePlateBlock> VOID_ASPECT_PRESSURE_PLATE = REGISTER.register("void_aspect_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, copy(VOID_ASPECT_PLANKS.get()), BlockSetType.OAK));
	public static final RegistryObject<FenceBlock> VOID_ASPECT_FENCE = REGISTER.register("void_aspect_fence", () -> new FenceBlock(copy(VOID_ASPECT_PLANKS.get())));
	public static final RegistryObject<FenceGateBlock> VOID_ASPECT_FENCE_GATE = REGISTER.register("void_aspect_fence_gate", () -> new FenceGateBlock(copy(VOID_ASPECT_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final RegistryObject<DoorBlock> VOID_ASPECT_DOOR = REGISTER.register("void_aspect_door", () -> new DoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	public static final RegistryObject<TrapDoorBlock> VOID_ASPECT_TRAPDOOR = REGISTER.register("void_aspect_trapdoor", () -> new TrapDoorBlock(copy(Blocks.OAK_TRAPDOOR), BlockSetType.OAK));
	
	
	//Land Plant Blocks
	public static final RegistryObject<Block> GLOWING_MUSHROOM = REGISTER.register("glowing_mushroom", () -> new GlowingMushroomBlock(Block.Properties.of().mapColor(MapColor.DIAMOND).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS).lightLevel(state -> 11).offsetType(BlockBehaviour.OffsetType.XZ)));
	public static final RegistryObject<Block> DESERT_BUSH = REGISTER.register("desert_bush", () -> new DesertFloraBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().strength(0).sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));
	public static final RegistryObject<Block> BLOOMING_CACTUS = REGISTER.register("blooming_cactus", () -> new DesertFloraBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().strength(0).sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));
	public static final RegistryObject<Block> PETRIFIED_GRASS = REGISTER.register("petrified_grass", () -> new PetrifiedFloraBlock(Block.Properties.of().mapColor(DyeColor.GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().noCollission().strength(0).sound(SoundType.STONE).offsetType(BlockBehaviour.OffsetType.XYZ), PetrifiedFloraBlock.GRASS_SHAPE));
	public static final RegistryObject<Block> PETRIFIED_POPPY = REGISTER.register("petrified_poppy", () -> new PetrifiedFloraBlock(Block.Properties.of().mapColor(DyeColor.GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().noCollission().strength(0).sound(SoundType.STONE).offsetType(BlockBehaviour.OffsetType.XZ), PetrifiedFloraBlock.FLOWER_SHAPE));
	public static final RegistryObject<Block> GLOWING_MUSHROOM_VINES = REGISTER.register("glowing_mushroom_vines", () -> new GlowingMushroomVinesBlock(Block.Properties.of().mapColor(MapColor.DIAMOND).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS).lightLevel(state -> 11).offsetType(BlockBehaviour.OffsetType.XZ)));
	public static final RegistryObject<StemGrownBlock> STRAWBERRY = REGISTER.register("strawberry", () -> new StrawberryBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).pushReaction(PushReaction.DESTROY).strength(1.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<AttachedStemBlock> ATTACHED_STRAWBERRY_STEM = REGISTER.register("attached_strawberry_stem", () -> new AttachedStemBlock(STRAWBERRY.get(), MSItems.STRAWBERRY_CHUNK, Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.WOOD)));
	public static final RegistryObject<StemBlock> STRAWBERRY_STEM = REGISTER.register("strawberry_stem", () -> new StemBlock(STRAWBERRY.get(), MSItems.STRAWBERRY_CHUNK, Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> END_GRASS = REGISTER.register("end_grass", () -> new EndGrassBlock(Block.Properties.of().mapColor(MapColor.COLOR_PURPLE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 9.0F)));
	public static final RegistryObject<Block> TALL_END_GRASS = REGISTER.register("tall_end_grass", () -> new TallEndGrassBlock(Block.Properties.of().mapColor(DyeColor.GREEN).replaceable().ignitedByLava().pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0.1F).sound(SoundType.NETHER_WART).offsetType(BlockBehaviour.OffsetType.XYZ)));
	public static final RegistryObject<Block> GLOWFLOWER = REGISTER.register("glowflower", () -> new FlowerBlock(MobEffects.GLOWING, 20, Block.Properties.of().mapColor(DyeColor.YELLOW).pushReaction(PushReaction.DESTROY).noCollission().strength(0).lightLevel(state -> 12).sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));
	
	
	//Special Land Blocks
	public static final RegistryObject<Block> GLOWY_GOOP = REGISTER.register("glowy_goop", () -> new SlimeBlock(Block.Properties.of().mapColor(MapColor.CLAY).strength(0.1F).sound(SoundType.SLIME_BLOCK).lightLevel(state -> 14)));
	public static final RegistryObject<Block> COAGULATED_BLOOD = REGISTER.register("coagulated_blood", () -> new SlimeBlock(Block.Properties.of().mapColor(MapColor.CLAY).strength(0.1F).sound(SoundType.SLIME_BLOCK)));
	public static final RegistryObject<Block> PIPE = REGISTER.register("pipe", () -> new DirectionalCustomShapeBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(4.0F).sound(SoundType.METAL), MSBlockShapes.PIPE));
	public static final RegistryObject<Block> PIPE_INTERSECTION = REGISTER.register("pipe_intersection", () -> new Block(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(4.0F).sound(SoundType.METAL))); //the intention is that later down the line, someone will improve the code of pipe blocks to allow for intersections or a separate intersection blockset will be made that actually work
	public static final RegistryObject<Block> PARCEL_PYXIS = REGISTER.register("parcel_pyxis", () -> new CustomShapeBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(4.0F), MSBlockShapes.PARCEL_PYXIS));
	public static final RegistryObject<Block> PYXIS_LID = REGISTER.register("pyxis_lid", () -> new CustomShapeBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(1.0F), MSBlockShapes.PYXIS_LID));
	public static final RegistryObject<Block> STONE_TABLET = REGISTER.register("stone_tablet", () -> new StoneTabletBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.3F)));
	public static final RegistryObject<Block> NAKAGATOR_STATUE = REGISTER.register("nakagator_statue", () -> new CustomShapeBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(0.5F), MSBlockShapes.NAKAGATOR_STATUE));
	
	
	//Redstone Blocks
	public static final RegistryObject<Block> TRAJECTORY_BLOCK = REGISTER.register("trajectory_block", () -> new TrajectoryBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final RegistryObject<Block> STAT_STORER = REGISTER.register("stat_storer", () -> new StatStorerBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final RegistryObject<Block> REMOTE_OBSERVER = REGISTER.register("remote_observer", () -> new RemoteObserverBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final RegistryObject<Block> WIRELESS_REDSTONE_TRANSMITTER = REGISTER.register("wireless_redstone_transmitter", () -> new WirelessRedstoneTransmitterBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final RegistryObject<Block> WIRELESS_REDSTONE_RECEIVER = REGISTER.register("wireless_redstone_receiver", () -> new WirelessRedstoneReceiverBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL).randomTicks()));
	public static final RegistryObject<Block> SOLID_SWITCH = REGISTER.register("solid_switch", () -> new SolidSwitchBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL).lightLevel(state -> state.getValue(SolidSwitchBlock.POWERED) ? 15 : 0)));
	public static final RegistryObject<Block> VARIABLE_SOLID_SWITCH = REGISTER.register("variable_solid_switch", () -> new VariableSolidSwitchBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL).lightLevel(state -> state.getValue(VariableSolidSwitchBlock.POWER))));
	public static final RegistryObject<Block> ONE_SECOND_INTERVAL_TIMED_SOLID_SWITCH = REGISTER.register("one_second_interval_timed_solid_switch", () -> new TimedSolidSwitchBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL).lightLevel(state -> state.getValue(TimedSolidSwitchBlock.POWER)), 20));
	public static final RegistryObject<Block> TWO_SECOND_INTERVAL_TIMED_SOLID_SWITCH = REGISTER.register("two_second_interval_timed_solid_switch", () -> new TimedSolidSwitchBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL).lightLevel(state -> state.getValue(TimedSolidSwitchBlock.POWER)), 40));
	public static final RegistryObject<Block> SUMMONER = REGISTER.register("summoner", () -> new SummonerBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final RegistryObject<Block> AREA_EFFECT_BLOCK = REGISTER.register("area_effect_block", () -> new AreaEffectBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(6).sound(SoundType.METAL)));
	public static final RegistryObject<Block> PLATFORM_GENERATOR = REGISTER.register("platform_generator", () -> new PlatformGeneratorBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(6).sound(SoundType.METAL)));
	public static final RegistryObject<Block> PLATFORM_BLOCK = REGISTER.register("platform_block", () -> new PlatformBlock(Block.Properties.of().pushReaction(PushReaction.BLOCK).strength(0.2F).sound(SoundType.SCAFFOLDING).lightLevel(state -> 6).randomTicks().noOcclusion().isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never)));
	public static final RegistryObject<Block> PLATFORM_RECEPTACLE = REGISTER.register("platform_receptacle", () -> new PlatformReceptacleBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final RegistryObject<Block> ITEM_MAGNET = REGISTER.register("item_magnet", () -> new ItemMagnetBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL), new CustomVoxelShape(new double[]{0, 0, 0, 16, 1, 16}, new double[]{1, 1, 1, 15, 15, 15}, new double[]{0, 15, 0, 16, 16, 16})));
	public static final RegistryObject<Block> REDSTONE_CLOCK = REGISTER.register("redstone_clock", () -> new RedstoneClockBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final RegistryObject<Block> ROTATOR = REGISTER.register("rotator", () -> new RotatorBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final RegistryObject<Block> TOGGLER = REGISTER.register("toggler", () -> new TogglerBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final RegistryObject<Block> REMOTE_COMPARATOR = REGISTER.register("remote_comparator", () -> new RemoteComparatorBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final RegistryObject<Block> STRUCTURE_CORE = REGISTER.register("structure_core", () -> new StructureCoreBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(6).sound(SoundType.METAL)));
	public static final RegistryObject<Block> FALL_PAD = REGISTER.register("fall_pad", () -> new FallPadBlock(Block.Properties.of().mapColor(MapColor.WOOL).requiresCorrectToolForDrops().strength(1).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> FRAGILE_STONE = REGISTER.register("fragile_stone", () -> new FragileBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1).sound(SoundType.STONE)));
	public static final RegistryObject<Block> SPIKES = REGISTER.register("spikes", () -> new SpikeBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(2).sound(SoundType.METAL), MSBlockShapes.SPIKES));
	public static final RegistryObject<Block> RETRACTABLE_SPIKES = REGISTER.register("retractable_spikes", () -> new RetractableSpikesBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(1).sound(SoundType.METAL)));
	public static final RegistryObject<Block> BLOCK_PRESSURE_PLATE = REGISTER.register("block_pressure_plate", () -> new BlockPressurePlateBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1).sound(SoundType.STONE)));
	public static final RegistryObject<Block> PUSHABLE_BLOCK = REGISTER.register("pushable_block", () -> new PushableBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1).sound(SoundType.GILDED_BLACKSTONE), PushableBlock.Maneuverability.PUSH_AND_PULL));
	
	public static final RegistryObject<Block> AND_GATE_BLOCK = REGISTER.register("and_gate_block", () -> new LogicGateBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(1).sound(SoundType.METAL), LogicGateBlock.State.AND));
	public static final RegistryObject<Block> OR_GATE_BLOCK = REGISTER.register("or_gate_block", () -> new LogicGateBlock(copy(AND_GATE_BLOCK.get()), LogicGateBlock.State.OR));
	public static final RegistryObject<Block> XOR_GATE_BLOCK = REGISTER.register("xor_gate_block", () -> new LogicGateBlock(copy(AND_GATE_BLOCK.get()), LogicGateBlock.State.XOR));
	public static final RegistryObject<Block> NAND_GATE_BLOCK = REGISTER.register("nand_gate_block", () -> new LogicGateBlock(copy(AND_GATE_BLOCK.get()), LogicGateBlock.State.NAND));
	public static final RegistryObject<Block> NOR_GATE_BLOCK = REGISTER.register("nor_gate_block", () -> new LogicGateBlock(copy(AND_GATE_BLOCK.get()), LogicGateBlock.State.NOR));
	public static final RegistryObject<Block> XNOR_GATE_BLOCK = REGISTER.register("xnor_gate_block", () -> new LogicGateBlock(copy(AND_GATE_BLOCK.get()), LogicGateBlock.State.XNOR));
	
	
	//Core Functional Land Blocks
	public static final RegistryObject<Block> GATE = REGISTER.register("gate", () -> new GateBlock(Block.Properties.of().pushReaction(PushReaction.BLOCK).noCollission().strength(-1.0F, 25.0F).sound(SoundType.GLASS).lightLevel(state -> 11).noLootTable()));
	public static final RegistryObject<Block> GATE_MAIN = REGISTER.register("gate_main", () -> new GateBlock.Main(Block.Properties.of().pushReaction(PushReaction.BLOCK).noCollission().strength(-1.0F, 25.0F).sound(SoundType.GLASS).lightLevel(state -> 11).noLootTable()));
	public static final RegistryObject<Block> RETURN_NODE = REGISTER.register("return_node", () -> new ReturnNodeBlock(Block.Properties.of().pushReaction(PushReaction.BLOCK).noCollission().strength(-1.0F, 10.0F).sound(SoundType.GLASS).lightLevel(state -> 11).noLootTable()));
	public static final RegistryObject<Block> RETURN_NODE_MAIN = REGISTER.register("return_node_main", () -> new ReturnNodeBlock.Main(Block.Properties.of().pushReaction(PushReaction.BLOCK).noCollission().strength(-1.0F, 10.0F).sound(SoundType.GLASS).lightLevel(state -> 11).noLootTable()));
	
	
	//Misc Functional Land Blocks
	
	//Sburb Machines
	public static final RegistryObject<Block> CRUXTRUDER_LID = REGISTER.register("cruxtruder_lid", () -> new CruxtruderLidBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(1.0F)));
	public static final CruxtruderMultiblock CRUXTRUDER = new CruxtruderMultiblock(REGISTER);
	public static final TotemLatheMultiblock TOTEM_LATHE = new TotemLatheMultiblock(REGISTER);
	public static final AlchemiterMultiblock ALCHEMITER = new AlchemiterMultiblock(REGISTER);
	public static final PunchDesignixMultiblock PUNCH_DESIGNIX = new PunchDesignixMultiblock(REGISTER);
	
	public static final RegistryObject<Block> MINI_CRUXTRUDER = REGISTER.register("mini_cruxtruder", () -> new SmallMachineBlock<>(MSBlockShapes.SMALL_CRUXTRUDER.createRotatedShapes(), MSBlockEntityTypes.MINI_CRUXTRUDER, Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3.0F)));
	public static final RegistryObject<Block> MINI_TOTEM_LATHE = REGISTER.register("mini_totem_lathe", () -> new SmallMachineBlock<>(MSBlockShapes.SMALL_TOTEM_LATHE.createRotatedShapes(), MSBlockEntityTypes.MINI_TOTEM_LATHE, Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3.0F)));
	public static final RegistryObject<Block> MINI_ALCHEMITER = REGISTER.register("mini_alchemiter", () -> new MiniAlchemiterBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3.0F)));
	public static final RegistryObject<Block> MINI_PUNCH_DESIGNIX = REGISTER.register("mini_punch_designix", () -> new SmallMachineBlock<>(MSBlockShapes.SMALL_PUNCH_DESIGNIX.createRotatedShapes(), MSBlockEntityTypes.MINI_PUNCH_DESIGNIX, Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3.0F)));
	
	public static final RegistryObject<Block> HOLOPAD = REGISTER.register("holopad", () -> new HolopadBlock(Block.Properties.of().mapColor(MapColor.SNOW).requiresCorrectToolForDrops().strength(3.0F)));
	public static final RegistryObject<Block> INTELLIBEAM_LASERSTATION = REGISTER.register("intellibeam_laserstation", () -> new IntellibeamLaserstationBlock(Block.Properties.of().mapColor(MapColor.SNOW).requiresCorrectToolForDrops().strength(3.0F)));
	
	
	//Misc Machines
	public static final RegistryObject<Block> COMPUTER = REGISTER.register("computer", () -> new ComputerBlock(ComputerBlock.COMPUTER_SHAPE, ComputerBlock.COMPUTER_SHAPE, Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(4.0F)));
	public static final RegistryObject<Block> LAPTOP = REGISTER.register("laptop", () -> new ComputerBlock(ComputerBlock.LAPTOP_OPEN_SHAPE, ComputerBlock.LAPTOP_CLOSED_SHAPE, Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(4.0F)));
	public static final RegistryObject<Block> CROCKERTOP = REGISTER.register("crockertop", () -> new ComputerBlock(ComputerBlock.LAPTOP_OPEN_SHAPE, ComputerBlock.LAPTOP_CLOSED_SHAPE, Block.Properties.of().mapColor(MapColor.COLOR_RED).requiresCorrectToolForDrops().strength(4.0F)));
	public static final RegistryObject<Block> HUBTOP = REGISTER.register("hubtop", () -> new ComputerBlock(ComputerBlock.LAPTOP_OPEN_SHAPE, ComputerBlock.LAPTOP_CLOSED_SHAPE, Block.Properties.of().mapColor(MapColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(4.0F)));
	public static final RegistryObject<Block> LUNCHTOP = REGISTER.register("lunchtop", () -> new ComputerBlock(ComputerBlock.LUNCHTOP_OPEN_SHAPE, ComputerBlock.LUNCHTOP_CLOSED_SHAPE, Block.Properties.of().mapColor(MapColor.COLOR_RED).requiresCorrectToolForDrops().strength(4.0F)));
	public static final RegistryObject<Block> OLD_COMPUTER = REGISTER.register("old_computer", () -> new ComputerBlock(ComputerBlock.OLD_COMPUTER_SHAPE, ComputerBlock.OLD_COMPUTER_SHAPE, Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(4.0F)));
	public static final RegistryObject<Block> TRANSPORTALIZER = REGISTER.register("transportalizer", () -> new TransportalizerBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final RegistryObject<Block> TRANS_PORTALIZER = REGISTER.register("trans_portalizer", () -> new TransportalizerBlock(copy(TRANSPORTALIZER.get())));
	public static final RegistryObject<Block> SENDIFICATOR = REGISTER.register("sendificator", () -> new SendificatorBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final RegistryObject<Block> GRIST_WIDGET = REGISTER.register("grist_widget", () -> new GristWidgetBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final RegistryObject<Block> URANIUM_COOKER = REGISTER.register("uranium_cooker", () -> new SmallMachineBlock<>(new CustomVoxelShape(new double[]{4, 0, 4, 12, 6, 12}).createRotatedShapes(), MSBlockEntityTypes.URANIUM_COOKER, Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3.0F)));
	public static final RegistryObject<Block> GRIST_COLLECTOR = REGISTER.register("grist_collector", () -> new GristCollectorBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final RegistryObject<Block> ANTHVIL = REGISTER.register("anthvil", () -> new AnthvilBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final RegistryObject<Block> SKAIANET_DENIER = REGISTER.register("skaianet_denier", () -> new Block(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(6).sound(SoundType.METAL)));
	public static final RegistryObject<Block> POWER_HUB = REGISTER.register("power_hub", () -> new PowerHubBlock(Block.Properties.of().mapColor(MapColor.METAL).strength(2).sound(SoundType.METAL)));
	
	
	//Misc Core Objects
	public static final RegistryObject<Block> CRUXITE_DOWEL = REGISTER.register("cruxite_dowel", () -> new CruxiteDowelBlock(Block.Properties.of().instrument(NoteBlockInstrument.HAT).pushReaction(PushReaction.DESTROY).strength(0.0F)));
	public static final RegistryObject<Block> EMERGING_CRUXITE_DOWEL = REGISTER.register("emerging_cruxite_dowel", () -> new EmergingCruxiteDowelBlock(Block.Properties.of().instrument(NoteBlockInstrument.HAT).strength(0.0F)));
	public static final LotusTimeCapsuleMultiblock LOTUS_TIME_CAPSULE_BLOCK = new LotusTimeCapsuleMultiblock(REGISTER);
	
	
	//Misc Alchemy Semi-Plants
	public static final RegistryObject<Block> GOLD_SEEDS = REGISTER.register("gold_seeds", () -> new GoldSeedsBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).strength(0.1F).sound(SoundType.METAL).noCollission()));
	public static final RegistryObject<Block> WOODEN_CACTUS = REGISTER.register("wooden_cactus", () -> new SpecialCactusBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).randomTicks().strength(1.0F, 2.5F).sound(SoundType.WOOD)));
	
	
	//Cakes
	public static final RegistryObject<Block> APPLE_CAKE = REGISTER.register("apple_cake", () -> new SimpleCakeBlock(copy(Blocks.CAKE), 2, 0.5F, null));
	public static final RegistryObject<Block> BLUE_CAKE = REGISTER.register("blue_cake", () -> new SimpleCakeBlock(copy(Blocks.CAKE), 2, 0.3F, player -> player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 150, 0))));
	public static final RegistryObject<Block> COLD_CAKE = REGISTER.register("cold_cake", () -> new SimpleCakeBlock(copy(Blocks.CAKE), 2, 0.3F, player -> {
		player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 1));
		player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1));
	}));
	public static final RegistryObject<Block> RED_CAKE = REGISTER.register("red_cake", () -> new SimpleCakeBlock(copy(Blocks.CAKE), 2, 0.1F, player -> player.heal(1)));
	public static final RegistryObject<Block> HOT_CAKE = REGISTER.register("hot_cake", () -> new SimpleCakeBlock(copy(Blocks.CAKE), 2, 0.1F, player -> player.setSecondsOnFire(4)));
	public static final RegistryObject<Block> REVERSE_CAKE = REGISTER.register("reverse_cake", () -> new SimpleCakeBlock(copy(Blocks.CAKE), 2, 0.1F, null));
	public static final RegistryObject<Block> FUCHSIA_CAKE = REGISTER.register("fuchsia_cake", () -> new SimpleCakeBlock(copy(Blocks.CAKE), 3, 0.5F, player -> {
		player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 350, 1));
		player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0));
	}));
	public static final RegistryObject<Block> NEGATIVE_CAKE = REGISTER.register("negative_cake", () -> new SimpleCakeBlock(copy(Blocks.CAKE), 2, 0.3F, player -> {
		player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 300, 0));
		player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 250, 0));
	}));
	public static final RegistryObject<Block> CARROT_CAKE = REGISTER.register("carrot_cake", () -> new SimpleCakeBlock(copy(Blocks.CAKE), 2, 0.3F, player -> player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 200, 0))));
	public static final RegistryObject<Block> LARGE_CAKE = REGISTER.register("large_cake", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_BROWN).strength(0.4F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> PINK_FROSTED_TOP_LARGE_CAKE = REGISTER.register("pink_frosted_top_large_cake", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_BROWN).strength(0.5F).sound(SoundType.WOOL)));
	
	
	//Explosives
	public static final RegistryObject<Block> PRIMED_TNT = REGISTER.register("primed_tnt", () -> new SpecialTNTBlock(Block.Properties.of().mapColor(MapColor.FIRE).ignitedByLava().strength(0.0F).sound(SoundType.GRASS).isRedstoneConductor(MSBlocks::never), true, false, false));
	public static final RegistryObject<Block> UNSTABLE_TNT = REGISTER.register("unstable_tnt", () -> new SpecialTNTBlock(Block.Properties.of().mapColor(MapColor.FIRE).ignitedByLava().strength(0.0F).sound(SoundType.GRASS).isRedstoneConductor(MSBlocks::never).randomTicks(), false, true, false));
	public static final RegistryObject<Block> INSTANT_TNT = REGISTER.register("instant_tnt", () -> new SpecialTNTBlock(Block.Properties.of().mapColor(MapColor.FIRE).ignitedByLava().strength(0.0F).sound(SoundType.GRASS).isRedstoneConductor(MSBlocks::never), false, false, true));
	public static final RegistryObject<ButtonBlock> WOODEN_EXPLOSIVE_BUTTON = REGISTER.register("wooden_explosive_button", () -> new SpecialButtonBlock(Block.Properties.of().pushReaction(PushReaction.DESTROY).noCollission().strength(0.5F).sound(SoundType.WOOD), true, BlockSetType.OAK, 30, true));
	public static final RegistryObject<ButtonBlock> STONE_EXPLOSIVE_BUTTON = REGISTER.register("stone_explosive_button", () -> new SpecialButtonBlock(Block.Properties.of().pushReaction(PushReaction.DESTROY).noCollission().strength(0.5F).sound(SoundType.STONE), true, BlockSetType.STONE, 20, false));
	
	//Misc Clutter
	public static final RegistryObject<Block> BLENDER = REGISTER.register("blender", () -> new CustomShapeBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.5F).sound(SoundType.METAL), MSBlockShapes.BLENDER));
	public static final RegistryObject<Block> CHESSBOARD = REGISTER.register("chessboard", () -> new CustomShapeBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.5F), MSBlockShapes.CHESSBOARD));
	public static final RegistryObject<Block> MINI_FROG_STATUE = REGISTER.register("mini_frog_statue", () -> new CustomShapeBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.5F), MSBlockShapes.FROG_STATUE));
	public static final RegistryObject<Block> MINI_WIZARD_STATUE = REGISTER.register("mini_wizard_statue", () -> new CustomShapeBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.5F), MSBlockShapes.WIZARD_STATUE));
	public static final RegistryObject<Block> MINI_TYPHEUS_STATUE = REGISTER.register("mini_typheus_statue", () -> new CustomShapeBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.5F), MSBlockShapes.DENIZEN_STATUE));
	public static final RegistryObject<CassettePlayerBlock> CASSETTE_PLAYER = REGISTER.register("cassette_player", () -> new CassettePlayerBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(0.5F).sound(SoundType.METAL), MSBlockShapes.CASSETTE_PLAYER));
	public static final HorseClockMultiblock HORSE_CLOCK = new HorseClockMultiblock(REGISTER);
	public static final RegistryObject<Block> GLOWYSTONE_DUST = REGISTER.register("glowystone_dust", () -> new GlowystoneWireBlock(Block.Properties.of().pushReaction(PushReaction.DESTROY).strength(0.0F).lightLevel(state -> 16).noCollission()));
	public static final RegistryObject<Block> MIRROR = REGISTER.register("mirror", () -> new CustomShapeBlock(Block.Properties.copy(Blocks.OAK_PLANKS), MSBlockShapes.MIRROR));
	
	public static final RegistryObject<LiquidBlock> OIL = REGISTER.register("oil", () -> new MSLiquidBlock(MSFluids.OIL, new Vec3(0.0, 0.0, 0.0), 0.80f, false, Block.Properties.of().mapColor(MapColor.COLOR_BLACK).replaceable().pushReaction(PushReaction.DESTROY).liquid().noCollission().strength(100.0F).noLootTable().sound(SoundType.EMPTY)));
	public static final RegistryObject<LiquidBlock> BLOOD = REGISTER.register("blood", () -> new MSLiquidBlock(MSFluids.BLOOD, new Vec3(0.8, 0.0, 0.0), 0.35f, true, Block.Properties.of().mapColor(MapColor.COLOR_RED).replaceable().pushReaction(PushReaction.DESTROY).liquid().noCollission().strength(100.0F).noLootTable().sound(SoundType.EMPTY)));
	public static final RegistryObject<LiquidBlock> BRAIN_JUICE = REGISTER.register("brain_juice", () -> new MSLiquidBlock(MSFluids.BRAIN_JUICE, new Vec3(0.55, 0.25, 0.7), 0.25f, true, Block.Properties.of().mapColor(MapColor.COLOR_PURPLE).replaceable().pushReaction(PushReaction.DESTROY).liquid().noCollission().strength(100.0F).noLootTable().sound(SoundType.EMPTY)));
	public static final RegistryObject<LiquidBlock> WATER_COLORS = REGISTER.register("water_colors", () -> new WaterColorsBlock(MSFluids.WATER_COLORS, 0.20f, Block.Properties.of().mapColor(MapColor.WATER).replaceable().pushReaction(PushReaction.DESTROY).liquid().noCollission().strength(100.0F).noLootTable().sound(SoundType.EMPTY)));
	public static final RegistryObject<LiquidBlock> ENDER = REGISTER.register("ender", () -> new MSLiquidBlock(MSFluids.ENDER, new Vec3(0, 0.35, 0.35), (Float.MAX_VALUE), false, Block.Properties.of().mapColor(MapColor.COLOR_CYAN).replaceable().pushReaction(PushReaction.DESTROY).liquid().noCollission().strength(100.0F).noLootTable().sound(SoundType.EMPTY)));
	public static final RegistryObject<LiquidBlock> LIGHT_WATER = REGISTER.register("light_water", () -> new MSLiquidBlock(MSFluids.LIGHT_WATER, new Vec3(0.2, 0.3, 1.0), 0.20f, true, Block.Properties.of().mapColor(MapColor.WATER).replaceable().pushReaction(PushReaction.DESTROY).liquid().noCollission().strength(100.0F).lightLevel(state -> 8).noLootTable().sound(SoundType.EMPTY)));
	
	
	private static Function<BlockState, MapColor> logColors(MapColor topColor, MapColor barkColor)
	{
		return state -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? topColor : barkColor;
	}
	
	private static Boolean leafSpawns(BlockState state, BlockGetter level, BlockPos pos, EntityType<?> type)
	{
		return type == EntityType.OCELOT || type == EntityType.PARROT;
	}
	
	private static boolean never(BlockState state, BlockGetter level, BlockPos pos)
	{
		return false;
	}
	
	private static Boolean never(BlockState state, BlockGetter level, BlockPos pos, EntityType<?> type)
	{
		return false;
	}
}
