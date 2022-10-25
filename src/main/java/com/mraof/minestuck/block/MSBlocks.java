package com.mraof.minestuck.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.fluid.FlowingModFluidBlock;
import com.mraof.minestuck.block.fluid.FlowingWaterColorsBlock;
import com.mraof.minestuck.block.machine.*;
import com.mraof.minestuck.block.plant.*;
import com.mraof.minestuck.block.redstone.*;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.fluid.MSFluids;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.CustomVoxelShape;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
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
	public static final RegistryObject<Block> BLACK_CHESS_DIRT = REGISTER.register("black_chess_dirt", () -> new Block(Block.Properties.of(Material.DIRT, MaterialColor.COLOR_BLACK).strength(0.5F).sound(SoundType.GRAVEL)));
	public static final RegistryObject<Block> WHITE_CHESS_DIRT = REGISTER.register("white_chess_dirt", () -> new Block(Block.Properties.of(Material.DIRT, MaterialColor.SNOW).strength(0.5F).sound(SoundType.GRAVEL)));
	public static final RegistryObject<Block> DARK_GRAY_CHESS_DIRT = REGISTER.register("dark_gray_chess_dirt", () -> new Block(Block.Properties.of(Material.DIRT, MaterialColor.COLOR_GRAY).strength(0.5F).sound(SoundType.GRAVEL)));
	public static final RegistryObject<Block> LIGHT_GRAY_CHESS_DIRT = REGISTER.register("light_gray_chess_dirt", () -> new Block(Block.Properties.of(Material.DIRT, MaterialColor.COLOR_LIGHT_GRAY).strength(0.5F).sound(SoundType.GRAVEL)));
	public static final RegistryObject<Block> SKAIA_PORTAL = REGISTER.register("skaia_portal", () -> new SkaiaPortalBlock(Block.Properties.of(Material.PORTAL, MaterialColor.COLOR_CYAN).noCollission().lightLevel(state -> 11).strength(-1.0F, 3600000.0F).noDrops()));
	
	public static final RegistryObject<Block> BLACK_CHESS_BRICKS = REGISTER.register("black_chess_bricks", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_BLACK).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final RegistryObject<Block> DARK_GRAY_CHESS_BRICKS = REGISTER.register("dark_gray_chess_bricks", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_GRAY).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final RegistryObject<Block> LIGHT_GRAY_CHESS_BRICKS = REGISTER.register("light_gray_chess_bricks", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final RegistryObject<Block> WHITE_CHESS_BRICKS = REGISTER.register("white_chess_bricks", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_WHITE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final RegistryObject<Block> BLACK_CHESS_BRICK_SMOOTH = REGISTER.register("black_chess_brick_smooth", () -> new Block(copy(BLACK_CHESS_BRICKS.get())));
	public static final RegistryObject<Block> DARK_GRAY_CHESS_BRICK_SMOOTH = REGISTER.register("dark_gray_chess_brick_smooth", () -> new Block(copy(DARK_GRAY_CHESS_BRICKS.get())));
	public static final RegistryObject<Block> LIGHT_GRAY_CHESS_BRICK_SMOOTH = REGISTER.register("light_gray_chess_brick_smooth", () -> new Block(copy(LIGHT_GRAY_CHESS_BRICKS.get())));
	public static final RegistryObject<Block> WHITE_CHESS_BRICK_SMOOTH = REGISTER.register("white_chess_brick_smooth", () -> new Block(copy(WHITE_CHESS_BRICKS.get())));
	public static final RegistryObject<Block> BLACK_CHESS_BRICK_TRIM = REGISTER.register("black_chess_brick_trim", () -> new MSDirectionalBlock(copy(BLACK_CHESS_BRICKS.get())));
	public static final RegistryObject<Block> DARK_GRAY_CHESS_BRICK_TRIM = REGISTER.register("dark_gray_chess_brick_trim", () -> new MSDirectionalBlock(copy(DARK_GRAY_CHESS_BRICKS.get())));
	public static final RegistryObject<Block> LIGHT_GRAY_CHESS_BRICK_TRIM = REGISTER.register("light_gray_chess_brick_trim", () -> new MSDirectionalBlock(copy(LIGHT_GRAY_CHESS_BRICKS.get())));
	public static final RegistryObject<Block> WHITE_CHESS_BRICK_TRIM = REGISTER.register("white_chess_brick_trim", () -> new MSDirectionalBlock(copy(WHITE_CHESS_BRICKS.get())));
	
	public static final RegistryObject<Block> CHECKERED_STAINED_GLASS = REGISTER.register("checkered_stained_glass", () -> new StainedGlassBlock(DyeColor.BLUE, Block.Properties.of(Material.GLASS, DyeColor.BLUE).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn(MSBlocks::never).isRedstoneConductor(MSBlocks::never).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never)));
	public static final RegistryObject<Block> BLACK_CROWN_STAINED_GLASS = REGISTER.register("black_crown_stained_glass", () -> new StainedGlassBlock(DyeColor.BLUE, copy(CHECKERED_STAINED_GLASS.get())));
	public static final RegistryObject<Block> BLACK_PAWN_STAINED_GLASS = REGISTER.register("black_pawn_stained_glass", () -> new StainedGlassBlock(DyeColor.BLUE, copy(CHECKERED_STAINED_GLASS.get())));
	public static final RegistryObject<Block> WHITE_CROWN_STAINED_GLASS = REGISTER.register("white_crown_stained_glass", () -> new StainedGlassBlock(DyeColor.BLUE, copy(CHECKERED_STAINED_GLASS.get())));
	public static final RegistryObject<Block> WHITE_PAWN_STAINED_GLASS = REGISTER.register("white_pawn_stained_glass", () -> new StainedGlassBlock(DyeColor.BLUE, copy(CHECKERED_STAINED_GLASS.get())));
	
	
	public static final RegistryObject<Block> STONE_CRUXITE_ORE = REGISTER.register("stone_cruxite_ore", () -> cruxiteOre(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> NETHERRACK_CRUXITE_ORE = REGISTER.register("netherrack_cruxite_ore", () -> cruxiteOre(copy(STONE_CRUXITE_ORE.get())));
	public static final RegistryObject<Block> COBBLESTONE_CRUXITE_ORE = REGISTER.register("cobblestone_cruxite_ore", () -> cruxiteOre(copy(STONE_CRUXITE_ORE.get())));
	public static final RegistryObject<Block> SANDSTONE_CRUXITE_ORE = REGISTER.register("sandstone_cruxite_ore", () -> cruxiteOre(copy(STONE_CRUXITE_ORE.get())));
	public static final RegistryObject<Block> RED_SANDSTONE_CRUXITE_ORE = REGISTER.register("red_sandstone_cruxite_ore", () -> cruxiteOre(copy(STONE_CRUXITE_ORE.get())));
	public static final RegistryObject<Block> END_STONE_CRUXITE_ORE = REGISTER.register("end_stone_cruxite_ore", () -> cruxiteOre(copy(STONE_CRUXITE_ORE.get())));
	public static final RegistryObject<Block> SHADE_STONE_CRUXITE_ORE = REGISTER.register("shade_stone_cruxite_ore", () -> cruxiteOre(copy(STONE_CRUXITE_ORE.get())));
	public static final RegistryObject<Block> PINK_STONE_CRUXITE_ORE = REGISTER.register("pink_stone_cruxite_ore", () -> cruxiteOre(copy(STONE_CRUXITE_ORE.get())));
	
	private static Block cruxiteOre(BlockBehaviour.Properties properties)
	{
		return new OreBlock(properties, UniformInt.of(2, 5));
	}
	
	public static final RegistryObject<Block> STONE_URANIUM_ORE = REGISTER.register("stone_uranium_ore", () -> uraniumOre(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().lightLevel(state -> 3)));
	public static final RegistryObject<Block> DEEPSLATE_URANIUM_ORE = REGISTER.register("deepslate_uranium_ore", () -> uraniumOre(copy(STONE_CRUXITE_ORE.get()).color(MaterialColor.DEEPSLATE).strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE)));
	public static final RegistryObject<Block> NETHERRACK_URANIUM_ORE = REGISTER.register("netherrack_uranium_ore", () -> uraniumOre(copy(STONE_CRUXITE_ORE.get())));
	public static final RegistryObject<Block> COBBLESTONE_URANIUM_ORE = REGISTER.register("cobblestone_uranium_ore", () -> uraniumOre(copy(STONE_CRUXITE_ORE.get())));
	public static final RegistryObject<Block> SANDSTONE_URANIUM_ORE = REGISTER.register("sandstone_uranium_ore", () -> uraniumOre(copy(STONE_CRUXITE_ORE.get())));
	public static final RegistryObject<Block> RED_SANDSTONE_URANIUM_ORE = REGISTER.register("red_sandstone_uranium_ore", () -> uraniumOre(copy(STONE_CRUXITE_ORE.get())));
	public static final RegistryObject<Block> END_STONE_URANIUM_ORE = REGISTER.register("end_stone_uranium_ore", () -> uraniumOre(copy(STONE_CRUXITE_ORE.get())));
	public static final RegistryObject<Block> SHADE_STONE_URANIUM_ORE = REGISTER.register("shade_stone_uranium_ore", () -> uraniumOre(copy(STONE_CRUXITE_ORE.get())));
	public static final RegistryObject<Block> PINK_STONE_URANIUM_ORE = REGISTER.register("pink_stone_uranium_ore", () -> uraniumOre(copy(STONE_CRUXITE_ORE.get())));
	
	private static Block uraniumOre(BlockBehaviour.Properties properties)
	{
		return new OreBlock(properties, UniformInt.of(2, 5));
	}
	
	public static final RegistryObject<Block> NETHERRACK_COAL_ORE = REGISTER.register("netherrack_coal_ore", () -> coalOre(copy(Blocks.COAL_ORE)));
	public static final RegistryObject<Block> SHADE_STONE_COAL_ORE = REGISTER.register("shade_stone_coal_ore", () -> coalOre(copy(Blocks.COAL_ORE)));
	public static final RegistryObject<Block> PINK_STONE_COAL_ORE = REGISTER.register("pink_stone_coal_ore", () -> coalOre(copy(Blocks.COAL_ORE)));
	
	private static Block coalOre(BlockBehaviour.Properties properties)
	{
		return new OreBlock(properties, UniformInt.of(0, 2));
	}
	
	public static final RegistryObject<Block> END_STONE_IRON_ORE = REGISTER.register("end_stone_iron_ore", () -> new OreBlock(copy(Blocks.IRON_ORE)));
	public static final RegistryObject<Block> SANDSTONE_IRON_ORE = REGISTER.register("sandstone_iron_ore", () -> new OreBlock(copy(Blocks.IRON_ORE)));
	public static final RegistryObject<Block> RED_SANDSTONE_IRON_ORE = REGISTER.register("red_sandstone_iron_ore", () -> new OreBlock(copy(Blocks.IRON_ORE)));
	
	public static final RegistryObject<Block> SANDSTONE_GOLD_ORE = REGISTER.register("sandstone_gold_ore", () -> new OreBlock(copy(Blocks.GOLD_ORE)));
	public static final RegistryObject<Block> RED_SANDSTONE_GOLD_ORE = REGISTER.register("red_sandstone_gold_ore", () -> new OreBlock(copy(Blocks.GOLD_ORE)));
	public static final RegistryObject<Block> SHADE_STONE_GOLD_ORE = REGISTER.register("shade_stone_gold_ore", () -> new OreBlock(copy(Blocks.GOLD_ORE)));
	public static final RegistryObject<Block> PINK_STONE_GOLD_ORE = REGISTER.register("pink_stone_gold_ore", () -> new OreBlock(copy(Blocks.GOLD_ORE)));
	
	public static final RegistryObject<Block> END_STONE_REDSTONE_ORE = REGISTER.register("end_stone_redstone_ore", () -> new RedStoneOreBlock(copy(Blocks.REDSTONE_ORE)));
	public static final RegistryObject<Block> STONE_QUARTZ_ORE = REGISTER.register("stone_quartz_ore", () -> new OreBlock(copy(Blocks.NETHER_QUARTZ_ORE), UniformInt.of(2, 5)));
	public static final RegistryObject<Block> PINK_STONE_LAPIS_ORE = REGISTER.register("pink_stone_lapis_ore", () -> new OreBlock(copy(Blocks.LAPIS_ORE), UniformInt.of(2, 5)));
	public static final RegistryObject<Block> PINK_STONE_DIAMOND_ORE = REGISTER.register("pink_stone_diamond_ore", () -> new OreBlock(copy(Blocks.DIAMOND_ORE), UniformInt.of(3, 7)));
	
	
	
	//Resource Blocks
	public static final RegistryObject<Block> CRUXITE_BLOCK = REGISTER.register("cruxite_block", () -> new Block(Block.Properties.of(Material.STONE, DyeColor.LIGHT_BLUE).strength(3.0F).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> URANIUM_BLOCK = REGISTER.register("uranium_block", () -> new Block(Block.Properties.of(Material.STONE, DyeColor.LIME).strength(3.0F).requiresCorrectToolForDrops().lightLevel(state -> 7)));
	public static final RegistryObject<Block> GENERIC_OBJECT = REGISTER.register("generic_object", () -> new Block(Block.Properties.of(Material.VEGETABLE, DyeColor.LIME).strength(1.0F).sound(SoundType.WOOD)));
	
	
	
	//Land Environment
	public static final RegistryObject<Block> BLUE_DIRT = REGISTER.register("blue_dirt", () -> new Block(Block.Properties.of(Material.DIRT, MaterialColor.COLOR_BLUE).strength(0.5F).sound(SoundType.GRAVEL)));
	public static final RegistryObject<Block> THOUGHT_DIRT = REGISTER.register("thought_dirt", () -> new Block(Block.Properties.of(Material.DIRT, MaterialColor.COLOR_LIGHT_GREEN).strength(0.5F).sound(SoundType.GRAVEL)));
	
	public static final RegistryObject<Block> COARSE_STONE = REGISTER.register("coarse_stone", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.STONE).requiresCorrectToolForDrops().strength(2.0F, 6.0F)));
	public static final RegistryObject<Block> CHISELED_COARSE_STONE = REGISTER.register("chiseled_coarse_stone", () -> new Block(copy(COARSE_STONE.get())));
	public static final RegistryObject<Block> COARSE_STONE_BRICKS = REGISTER.register("coarse_stone_bricks", () -> new Block(copy(COARSE_STONE.get())));
	public static final RegistryObject<Block> COARSE_STONE_COLUMN = REGISTER.register("coarse_stone_column", () -> new MSDirectionalBlock(copy(COARSE_STONE.get())));
	public static final RegistryObject<Block> CHISELED_COARSE_STONE_BRICKS = REGISTER.register("chiseled_coarse_stone_bricks", () -> new Block(copy(COARSE_STONE.get())));
	public static final RegistryObject<Block> CRACKED_COARSE_STONE_BRICKS = REGISTER.register("cracked_coarse_stone_bricks", () -> new Block(copy(COARSE_STONE.get())));
	public static final RegistryObject<Block> MOSSY_COARSE_STONE_BRICKS = REGISTER.register("mossy_coarse_stone_bricks", () -> new Block(copy(COARSE_STONE.get())));
	
	public static final RegistryObject<Block> SHADE_STONE = REGISTER.register("shade_stone", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final RegistryObject<Block> SMOOTH_SHADE_STONE = REGISTER.register("smooth_shade_stone", () -> new Block(copy(SHADE_STONE.get())));
	public static final RegistryObject<Block> SHADE_BRICKS = REGISTER.register("shade_bricks", () -> new Block(copy(SHADE_STONE.get())));
	public static final RegistryObject<Block> SHADE_COLUMN = REGISTER.register("shade_column", () -> new MSDirectionalBlock(copy(SHADE_STONE.get())));
	public static final RegistryObject<Block> CHISELED_SHADE_BRICKS = REGISTER.register("chiseled_shade_bricks", () -> new Block(copy(SHADE_BRICKS.get())));
	public static final RegistryObject<Block> CRACKED_SHADE_BRICKS = REGISTER.register("cracked_shade_bricks", () -> new Block(copy(SHADE_BRICKS.get())));
	public static final RegistryObject<Block> MOSSY_SHADE_BRICKS = REGISTER.register("mossy_shade_bricks", () -> new Block(copy(SHADE_BRICKS.get())));
	public static final RegistryObject<Block> BLOOD_SHADE_BRICKS = REGISTER.register("blood_shade_bricks", () -> new Block(copy(SHADE_BRICKS.get())));
	public static final RegistryObject<Block> TAR_SHADE_BRICKS = REGISTER.register("tar_shade_bricks", () -> new Block(copy(SHADE_BRICKS.get())));
	
	public static final RegistryObject<Block> FROST_TILE = REGISTER.register("frost_tile", () -> new Block(Block.Properties.of(Material.ICE_SOLID, MaterialColor.ICE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final RegistryObject<Block> CHISELED_FROST_TILE = REGISTER.register("chiseled_frost_tile", () -> new Block(copy(FROST_TILE.get())));
	public static final RegistryObject<Block> FROST_BRICKS = REGISTER.register("frost_bricks", () -> new Block(copy(FROST_TILE.get())));
	public static final RegistryObject<Block> FROST_COLUMN = REGISTER.register("frost_column", () -> new MSDirectionalBlock(copy(FROST_TILE.get())));
	public static final RegistryObject<Block> CHISELED_FROST_BRICKS = REGISTER.register("chiseled_frost_bricks", () -> new Block(copy(FROST_BRICKS.get()))); //while it is a pillar block, it cannot be rotated, making it similar to cut sandstone
	public static final RegistryObject<Block> CRACKED_FROST_BRICKS = REGISTER.register("cracked_frost_bricks", () -> new Block(copy(FROST_BRICKS.get())));
	public static final RegistryObject<Block> FLOWERY_FROST_BRICKS = REGISTER.register("flowery_frost_bricks", () -> new Block(copy(FROST_BRICKS.get())));
	
	public static final RegistryObject<Block> CAST_IRON = REGISTER.register("cast_iron", () -> new Block(Block.Properties.of(Material.METAL, MaterialColor.METAL).requiresCorrectToolForDrops().strength(3.0F, 9.0F)));
	public static final RegistryObject<Block> CHISELED_CAST_IRON = REGISTER.register("chiseled_cast_iron", () -> new Block(copy(CAST_IRON.get())));
	public static final RegistryObject<Block> STEEL_BEAM = REGISTER.register("steel_beam", () -> new MSDirectionalBlock(Block.Properties.of(Material.METAL, MaterialColor.METAL).requiresCorrectToolForDrops().strength(3.0F, 9.0F)));
	
	public static final RegistryObject<Block> MYCELIUM_STONE = REGISTER.register("mycelium_stone", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_MAGENTA).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final RegistryObject<Block> MYCELIUM_COBBLESTONE = REGISTER.register("mycelium_cobblestone", () -> new Block(copy(MYCELIUM_STONE.get())));
	public static final RegistryObject<Block> POLISHED_MYCELIUM_STONE = REGISTER.register("polished_mycelium_stone", () -> new Block(copy(MYCELIUM_STONE.get())));
	public static final RegistryObject<Block> MYCELIUM_BRICKS = REGISTER.register("mycelium_bricks", () -> new Block(copy(MYCELIUM_STONE.get())));
	public static final RegistryObject<Block> MYCELIUM_COLUMN = REGISTER.register("mycelium_column", () -> new MSDirectionalBlock(copy(MYCELIUM_STONE.get())));
	public static final RegistryObject<Block> CHISELED_MYCELIUM_BRICKS = REGISTER.register("chiseled_mycelium_bricks", () -> new Block(copy(MYCELIUM_BRICKS.get())));
	public static final RegistryObject<Block> CRACKED_MYCELIUM_BRICKS = REGISTER.register("cracked_mycelium_bricks", () -> new Block(copy(MYCELIUM_BRICKS.get())));
	public static final RegistryObject<Block> MOSSY_MYCELIUM_BRICKS = REGISTER.register("mossy_mycelium_bricks", () -> new Block(copy(MYCELIUM_BRICKS.get())));
	public static final RegistryObject<Block> FLOWERY_MYCELIUM_BRICKS = REGISTER.register("flowery_mycelium_bricks", () -> new Block(copy(MYCELIUM_BRICKS.get())));
	
	public static final RegistryObject<Block> BLACK_SAND = REGISTER.register("black_sand", () -> new SandBlock(0x181915, Block.Properties.of(Material.SAND, MaterialColor.COLOR_BLACK).strength(0.5F).sound(SoundType.SAND)));
	public static final RegistryObject<Block> BLACK_STONE = REGISTER.register("black_stone", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(2.5F, 6.0F)));
	public static final RegistryObject<Block> BLACK_COBBLESTONE = REGISTER.register("black_cobblestone", () -> new Block(copy(BLACK_STONE.get())));
	public static final RegistryObject<Block> POLISHED_BLACK_STONE = REGISTER.register("polished_black_stone", () -> new Block(copy(BLACK_STONE.get())));
	public static final RegistryObject<Block> BLACK_STONE_BRICKS = REGISTER.register("black_stone_bricks", () -> new Block(copy(BLACK_STONE.get())));
	public static final RegistryObject<Block> BLACK_STONE_COLUMN = REGISTER.register("black_stone_column", () -> new MSDirectionalBlock(copy(BLACK_STONE.get())));
	public static final RegistryObject<Block> CHISELED_BLACK_STONE_BRICKS = REGISTER.register("chiseled_black_stone_bricks", () -> new Block(copy(BLACK_STONE_BRICKS.get())));
	public static final RegistryObject<Block> CRACKED_BLACK_STONE_BRICKS = REGISTER.register("cracked_black_stone_bricks", () -> new Block(copy(BLACK_STONE_BRICKS.get())));
	
	public static final RegistryObject<Block> FLOWERY_MOSSY_COBBLESTONE = REGISTER.register("flowery_mossy_cobblestone", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final RegistryObject<Block> FLOWERY_MOSSY_STONE_BRICKS = REGISTER.register("flowery_mossy_stone_bricks", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final RegistryObject<Block> DECREPIT_STONE_BRICKS = REGISTER.register("decrepit_stone_bricks", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final RegistryObject<Block> MOSSY_DECREPIT_STONE_BRICKS = REGISTER.register("mossy_decrepit_stone_bricks", () -> new Block(copy(DECREPIT_STONE_BRICKS.get())));
	public static final RegistryObject<Block> COARSE_END_STONE = REGISTER.register("coarse_end_stone", () -> new TillableBlock(Block.Properties.of(Material.STONE, MaterialColor.SAND).requiresCorrectToolForDrops().strength(3.0F, 9.0F), Blocks.END_STONE::defaultBlockState));
	public static final RegistryObject<Block> END_GRASS = REGISTER.register("end_grass", () -> new EndGrassBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(3.0F, 9.0F)));
	
	public static final RegistryObject<Block> CHALK = REGISTER.register("chalk", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.SNOW).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final RegistryObject<Block> POLISHED_CHALK = REGISTER.register("polished_chalk", () -> new Block(copy(CHALK.get())));
	public static final RegistryObject<Block> CHALK_BRICKS = REGISTER.register("chalk_bricks", () -> new Block(copy(CHALK.get())));
	public static final RegistryObject<Block> CHALK_COLUMN = REGISTER.register("chalk_column", () -> new MSDirectionalBlock(copy(CHALK.get())));
	public static final RegistryObject<Block> CHISELED_CHALK_BRICKS = REGISTER.register("chiseled_chalk_bricks", () -> new Block(copy(CHALK_BRICKS.get())));
	public static final RegistryObject<Block> MOSSY_CHALK_BRICKS = REGISTER.register("mossy_chalk_bricks", () -> new Block(copy(CHALK_BRICKS.get())));
	public static final RegistryObject<Block> FLOWERY_CHALK_BRICKS = REGISTER.register("flowery_chalk_bricks", () -> new Block(copy(CHALK_BRICKS.get())));
	
	public static final RegistryObject<Block> PINK_STONE = REGISTER.register("pink_stone", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.SNOW).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final RegistryObject<Block> POLISHED_PINK_STONE = REGISTER.register("polished_pink_stone", () -> new Block(copy(PINK_STONE.get())));
	public static final RegistryObject<Block> PINK_STONE_BRICKS = REGISTER.register("pink_stone_bricks", () -> new Block(copy(PINK_STONE.get())));
	public static final RegistryObject<Block> PINK_STONE_COLUMN = REGISTER.register("pink_stone_column", () -> new MSDirectionalBlock(copy(PINK_STONE.get())));
	public static final RegistryObject<Block> CHISELED_PINK_STONE_BRICKS = REGISTER.register("chiseled_pink_stone_bricks", () -> new Block(copy(PINK_STONE_BRICKS.get())));
	public static final RegistryObject<Block> CRACKED_PINK_STONE_BRICKS = REGISTER.register("cracked_pink_stone_bricks", () -> new Block(copy(PINK_STONE_BRICKS.get())));
	public static final RegistryObject<Block> MOSSY_PINK_STONE_BRICKS = REGISTER.register("mossy_pink_stone_bricks", () -> new Block(copy(PINK_STONE_BRICKS.get())));
	
	public static final RegistryObject<Block> BROWN_STONE = REGISTER.register("brown_stone", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BROWN).requiresCorrectToolForDrops().strength(2.5F, 7.0F)));
	public static final RegistryObject<Block> POLISHED_BROWN_STONE = REGISTER.register("polished_brown_stone", () -> new Block(copy(BROWN_STONE.get())));
	public static final RegistryObject<Block> BROWN_STONE_BRICKS = REGISTER.register("brown_stone_bricks", () -> new Block(copy(BROWN_STONE.get())));
	public static final RegistryObject<Block> CRACKED_BROWN_STONE_BRICKS = REGISTER.register("cracked_brown_stone_bricks", () -> new Block(copy(BROWN_STONE.get())));
	public static final RegistryObject<Block> BROWN_STONE_COLUMN = REGISTER.register("brown_stone_column", () -> new MSDirectionalBlock(copy(BROWN_STONE.get())));
	
	public static final RegistryObject<Block> GREEN_STONE = REGISTER.register("green_stone", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(2.5F, 7.0F)));
	public static final RegistryObject<Block> POLISHED_GREEN_STONE = REGISTER.register("polished_green_stone", () -> new Block(copy(GREEN_STONE.get())));
	public static final RegistryObject<Block> GREEN_STONE_BRICKS = REGISTER.register("green_stone_bricks", () -> new Block(copy(GREEN_STONE.get())));
	public static final RegistryObject<Block> GREEN_STONE_COLUMN = REGISTER.register("green_stone_column", () -> new MSDirectionalBlock(copy(GREEN_STONE.get())));
	public static final RegistryObject<Block> CHISELED_GREEN_STONE_BRICKS = REGISTER.register("chiseled_green_stone_bricks", () -> new Block(copy(GREEN_STONE_BRICKS.get())));
	public static final RegistryObject<Block> HORIZONTAL_GREEN_STONE_BRICKS = REGISTER.register("horizontal_green_stone_bricks", () -> new Block(copy(GREEN_STONE_BRICKS.get())));
	public static final RegistryObject<Block> VERTICAL_GREEN_STONE_BRICKS = REGISTER.register("vertical_green_stone_bricks", () -> new Block(copy(GREEN_STONE_BRICKS.get())));
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
	
	public static final RegistryObject<Block> SANDSTONE_COLUMN = REGISTER.register("sandstone_column", () -> new MSDirectionalBlock(Block.Properties.of(Material.STONE, MaterialColor.SAND).requiresCorrectToolForDrops().strength(0.8F)));
	public static final RegistryObject<Block> CHISELED_SANDSTONE_COLUMN = REGISTER.register("chiseled_sandstone_column", () -> new MSDirectionalBlock(Block.Properties.of(Material.STONE, MaterialColor.SAND).requiresCorrectToolForDrops().strength(0.8F)));
	public static final RegistryObject<Block> RED_SANDSTONE_COLUMN = REGISTER.register("red_sandstone_column", () -> new MSDirectionalBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(0.8F)));
	public static final RegistryObject<Block> CHISELED_RED_SANDSTONE_COLUMN = REGISTER.register("chiseled_red_sandstone_column", () -> new MSDirectionalBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(0.8F)));
	
	public static final RegistryObject<Block> UNCARVED_WOOD = REGISTER.register("uncarved_wood", () -> new Block(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).requiresCorrectToolForDrops().sound(SoundType.WOOD)));
	public static final RegistryObject<Block> CHIPBOARD = REGISTER.register("chipboard", () -> new Block(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(1.0F).requiresCorrectToolForDrops().sound(SoundType.SCAFFOLDING)));
	public static final RegistryObject<Block> WOOD_SHAVINGS = REGISTER.register("wood_shavings", () -> new Block(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(0.4F).sound(SoundType.SAND)));
	
	public static final RegistryObject<Block> DENSE_CLOUD = REGISTER.register("dense_cloud", () -> new Block(Block.Properties.of(Material.GLASS, MaterialColor.COLOR_YELLOW).strength(0.5F).sound(SoundType.SNOW)));
	public static final RegistryObject<Block> BRIGHT_DENSE_CLOUD = REGISTER.register("bright_dense_cloud", () -> new Block(Block.Properties.of(Material.GLASS, MaterialColor.COLOR_LIGHT_GRAY).strength(0.5F).sound(SoundType.SNOW)));
	public static final RegistryObject<Block> SUGAR_CUBE = REGISTER.register("sugar_cube", () -> new Block(Block.Properties.of(Material.SAND, MaterialColor.SNOW).strength(0.4F).sound(SoundType.SAND)));
	
	
	
	//Land Tree Blocks
	public static final RegistryObject<Block> GLOWING_LOG = REGISTER.register("glowing_log", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.COLOR_LIGHT_BLUE).strength(2.0F).lightLevel(state -> 11).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> FROST_LOG = REGISTER.register("frost_log", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.ICE).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> RAINBOW_LOG = REGISTER.register("rainbow_log", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> END_LOG = REGISTER.register("end_log", () -> new DoubleLogBlock(1, 250, Block.Properties.of(Material.WOOD, MaterialColor.SAND).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> VINE_LOG = REGISTER.register("vine_log", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, logColors(MaterialColor.WOOD, MaterialColor.PODZOL)).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> FLOWERY_VINE_LOG = REGISTER.register("flowery_vine_log", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, logColors(MaterialColor.WOOD, MaterialColor.PODZOL)).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> DEAD_LOG = REGISTER.register("dead_log", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, logColors(MaterialColor.WOOD, MaterialColor.PODZOL)).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> PETRIFIED_LOG = REGISTER.register("petrified_log", () -> new RotatedPillarBlock(Block.Properties.of(Material.WOOD, logColors(MaterialColor.WOOD, MaterialColor.PODZOL)).strength(2.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> GLOWING_WOOD = REGISTER.register("glowing_wood", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.COLOR_LIGHT_BLUE).strength(2.0F).lightLevel(state -> 11).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> FROST_WOOD = REGISTER.register("frost_wood", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.ICE).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> RAINBOW_WOOD = REGISTER.register("rainbow_wood", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> END_WOOD = REGISTER.register("end_wood", () -> new FlammableLogBlock(1, 250, Block.Properties.of(Material.WOOD, MaterialColor.SAND).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> VINE_WOOD = REGISTER.register("vine_wood", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> FLOWERY_VINE_WOOD = REGISTER.register("flowery_vine_wood", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> DEAD_WOOD = REGISTER.register("dead_wood", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> PETRIFIED_WOOD = REGISTER.register("petrified_wood", () -> new RotatedPillarBlock(Block.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> GLOWING_PLANKS = REGISTER.register("glowing_planks", () -> new FlammableBlock(5, 20, Block.Properties.of(Material.WOOD, MaterialColor.COLOR_LIGHT_BLUE).strength(2.0F, 3.0F).lightLevel(state -> 7).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> FROST_PLANKS = REGISTER.register("frost_planks", () -> new FlammableBlock(5, 5, Block.Properties.of(Material.WOOD, MaterialColor.ICE).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final RegistryObject<Block> RAINBOW_PLANKS = REGISTER.register("rainbow_planks", () -> new FlammableBlock(5, 20, Block.Properties.of(Material.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final RegistryObject<Block> END_PLANKS = REGISTER.register("end_planks", () -> new FlammableBlock(1, 250, Block.Properties.of(Material.WOOD, MaterialColor.SAND).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final RegistryObject<Block> DEAD_PLANKS = REGISTER.register("dead_planks", () -> new FlammableBlock(5, 5, Block.Properties.of(Material.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final RegistryObject<Block> TREATED_PLANKS = REGISTER.register("treated_planks", () -> new FlammableBlock(0, 0, Block.Properties.of(Material.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final RegistryObject<Block> FROST_LEAVES = REGISTER.register("frost_leaves", () -> new FlammableLeavesBlock(Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(MSBlocks::leafSpawns).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never)));
	public static final RegistryObject<Block> RAINBOW_LEAVES = REGISTER.register("rainbow_leaves", () -> new FlammableLeavesBlock(Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(MSBlocks::leafSpawns).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never)));
	public static final RegistryObject<Block> END_LEAVES = REGISTER.register("end_leaves", () -> new EndLeavesBlock(Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(MSBlocks::leafSpawns).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never)));
	
	public static final RegistryObject<BushBlock> RAINBOW_SAPLING = REGISTER.register("rainbow_sapling", () -> new RainbowSaplingBlock(Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<BushBlock> END_SAPLING = REGISTER.register("end_sapling", () -> new EndSaplingBlock(Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	
	
	public static final RegistryObject<Block> BLOOD_ASPECT_LOG = REGISTER.register("blood_aspect_log", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> BREATH_ASPECT_LOG = REGISTER.register("breath_aspect_log", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> DOOM_ASPECT_LOG = REGISTER.register("doom_aspect_log", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> HEART_ASPECT_LOG = REGISTER.register("heart_aspect_log", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> HOPE_ASPECT_LOG = REGISTER.register("hope_aspect_log", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> LIFE_ASPECT_LOG = REGISTER.register("life_aspect_log", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> LIGHT_ASPECT_LOG = REGISTER.register("light_aspect_log", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> MIND_ASPECT_LOG = REGISTER.register("mind_aspect_log", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> RAGE_ASPECT_LOG = REGISTER.register("rage_aspect_log", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> SPACE_ASPECT_LOG = REGISTER.register("space_aspect_log", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> TIME_ASPECT_LOG = REGISTER.register("time_aspect_log", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> VOID_ASPECT_LOG = REGISTER.register("void_aspect_log", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD)));
	
	public static final RegistryObject<Block> BLOOD_ASPECT_PLANKS = REGISTER.register("blood_aspect_planks", () -> new FlammableBlock(5, 20, Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> BREATH_ASPECT_PLANKS = REGISTER.register("breath_aspect_planks", () -> new FlammableBlock(5, 20, Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> DOOM_ASPECT_PLANKS = REGISTER.register("doom_aspect_planks", () -> new FlammableBlock(5, 20, Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> HEART_ASPECT_PLANKS = REGISTER.register("heart_aspect_planks", () -> new FlammableBlock(5, 20, Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> HOPE_ASPECT_PLANKS = REGISTER.register("hope_aspect_planks", () -> new FlammableBlock(5, 20, Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> LIFE_ASPECT_PLANKS = REGISTER.register("life_aspect_planks", () -> new FlammableBlock(5, 20, Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> LIGHT_ASPECT_PLANKS = REGISTER.register("light_aspect_planks", () -> new FlammableBlock(5, 20, Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> MIND_ASPECT_PLANKS = REGISTER.register("mind_aspect_planks", () -> new FlammableBlock(5, 20, Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> RAGE_ASPECT_PLANKS = REGISTER.register("rage_aspect_planks", () -> new FlammableBlock(5, 20, Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> SPACE_ASPECT_PLANKS = REGISTER.register("space_aspect_planks", () -> new FlammableBlock(5, 20, Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> TIME_ASPECT_PLANKS = REGISTER.register("time_aspect_planks", () -> new FlammableBlock(5, 20, Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> VOID_ASPECT_PLANKS = REGISTER.register("void_aspect_planks", () -> new FlammableBlock(5, 20, Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	public static final RegistryObject<Block> BLOOD_ASPECT_LEAVES = REGISTER.register("blood_aspect_leaves", () -> new FlammableLeavesBlock(Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()));
	public static final RegistryObject<Block> BREATH_ASPECT_LEAVES = REGISTER.register("breath_aspect_leaves", () -> new FlammableLeavesBlock(Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()));
	public static final RegistryObject<Block> DOOM_ASPECT_LEAVES = REGISTER.register("doom_aspect_leaves", () -> new FlammableLeavesBlock(Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()));
	public static final RegistryObject<Block> HEART_ASPECT_LEAVES = REGISTER.register("heart_aspect_leaves", () -> new FlammableLeavesBlock(Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()));
	public static final RegistryObject<Block> HOPE_ASPECT_LEAVES = REGISTER.register("hope_aspect_leaves", () -> new FlammableLeavesBlock(Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()));
	public static final RegistryObject<Block> LIFE_ASPECT_LEAVES = REGISTER.register("life_aspect_leaves", () -> new FlammableLeavesBlock(Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()));
	public static final RegistryObject<Block> LIGHT_ASPECT_LEAVES = REGISTER.register("light_aspect_leaves", () -> new FlammableLeavesBlock(Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()));
	public static final RegistryObject<Block> MIND_ASPECT_LEAVES = REGISTER.register("mind_aspect_leaves", () -> new FlammableLeavesBlock(Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()));
	public static final RegistryObject<Block> RAGE_ASPECT_LEAVES = REGISTER.register("rage_aspect_leaves", () -> new FlammableLeavesBlock(Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()));
	public static final RegistryObject<Block> SPACE_ASPECT_LEAVES = REGISTER.register("space_aspect_leaves", () -> new FlammableLeavesBlock(Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()));
	public static final RegistryObject<Block> TIME_ASPECT_LEAVES = REGISTER.register("time_aspect_leaves", () -> new FlammableLeavesBlock(Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()));
	public static final RegistryObject<Block> VOID_ASPECT_LEAVES = REGISTER.register("void_aspect_leaves", () -> new FlammableLeavesBlock(Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()));
	
	public static final RegistryObject<Block> BLOOD_ASPECT_SAPLING = REGISTER.register("blood_aspect_sapling", () -> new AspectSaplingBlock(Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<Block> BREATH_ASPECT_SAPLING = REGISTER.register("breath_aspect_sapling", () -> new AspectSaplingBlock(Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<Block> DOOM_ASPECT_SAPLING = REGISTER.register("doom_aspect_sapling", () -> new AspectSaplingBlock(Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<Block> HEART_ASPECT_SAPLING = REGISTER.register("heart_aspect_sapling", () -> new AspectSaplingBlock(Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<Block> HOPE_ASPECT_SAPLING = REGISTER.register("hope_aspect_sapling", () -> new AspectSaplingBlock(Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<Block> LIFE_ASPECT_SAPLING = REGISTER.register("life_aspect_sapling", () -> new AspectSaplingBlock(Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<Block> LIGHT_ASPECT_SAPLING = REGISTER.register("light_aspect_sapling", () -> new AspectSaplingBlock(Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<Block> MIND_ASPECT_SAPLING = REGISTER.register("mind_aspect_sapling", () -> new AspectSaplingBlock(Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<Block> RAGE_ASPECT_SAPLING = REGISTER.register("rage_aspect_sapling", () -> new AspectSaplingBlock(Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<Block> SPACE_ASPECT_SAPLING = REGISTER.register("space_aspect_sapling", () -> new AspectSaplingBlock(Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<Block> TIME_ASPECT_SAPLING = REGISTER.register("time_aspect_sapling", () -> new AspectSaplingBlock(Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<Block> VOID_ASPECT_SAPLING = REGISTER.register("void_aspect_sapling", () -> new AspectSaplingBlock(Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	
	
	
	//Land Plant Blocks
	public static final RegistryObject<Block> GLOWING_MUSHROOM = REGISTER.register("glowing_mushroom", () -> new GlowingMushroomBlock(Block.Properties.of(Material.PLANT, MaterialColor.DIAMOND).noCollission().randomTicks().strength(0).sound(SoundType.GRASS).lightLevel(state -> 11)));
	public static final RegistryObject<Block> DESERT_BUSH = REGISTER.register("desert_bush", () -> new DesertFloraBlock(Block.Properties.of(Material.PLANT).noCollission().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<Block> BLOOMING_CACTUS = REGISTER.register("blooming_cactus", () -> new DesertFloraBlock(Block.Properties.of(Material.PLANT).noCollission().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<Block> PETRIFIED_GRASS = REGISTER.register("petrified_grass", () -> new PetrifiedFloraBlock(Block.Properties.of(Material.STONE, DyeColor.GRAY).requiresCorrectToolForDrops().noCollission().strength(0).sound(SoundType.STONE)));
	public static final RegistryObject<Block> PETRIFIED_POPPY = REGISTER.register("petrified_poppy", () -> new PetrifiedFloraBlock(Block.Properties.of(Material.STONE, DyeColor.GRAY).requiresCorrectToolForDrops().noCollission().strength(0).sound(SoundType.STONE)));
	
	public static final RegistryObject<StemGrownBlock> STRAWBERRY = REGISTER.register("strawberry", () -> new StrawberryBlock(Block.Properties.of(Material.VEGETABLE, MaterialColor.COLOR_RED).strength(1.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<AttachedStemBlock> ATTACHED_STRAWBERRY_STEM = REGISTER.register("attached_strawberry_stem", () -> new AttachedStemBlock(STRAWBERRY.get(), MSItems.STRAWBERRY_CHUNK, Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.WOOD)));
	public static final RegistryObject<StemBlock> STRAWBERRY_STEM = REGISTER.register("strawberry_stem", () -> new StemBlock(STRAWBERRY.get(), MSItems.STRAWBERRY_CHUNK, Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.WOOD)));
	
	public static final RegistryObject<Block> TALL_END_GRASS = REGISTER.register("tall_end_grass", () -> new TallEndGrassBlock(Block.Properties.of(Material.REPLACEABLE_PLANT, DyeColor.GREEN).noCollission().randomTicks().strength(0.1F).sound(SoundType.NETHER_WART)));
	public static final RegistryObject<Block> GLOWFLOWER = REGISTER.register("glowflower", () -> new FlowerBlock(MobEffects.GLOWING, 20, Block.Properties.of(Material.PLANT, DyeColor.YELLOW).noCollission().strength(0).lightLevel(state -> 12).sound(SoundType.GRASS)));
	
	
	
	//Special Land Blocks
	public static final RegistryObject<Block> GLOWY_GOOP = REGISTER.register("glowy_goop", () -> new SlimeBlock(Block.Properties.of(Material.CLAY).strength(0.1F).sound(SoundType.SLIME_BLOCK).lightLevel(state -> 14)));
	public static final RegistryObject<Block> COAGULATED_BLOOD = REGISTER.register("coagulated_blood", () -> new SlimeBlock(Block.Properties.of(Material.CLAY).strength(0.1F).sound(SoundType.SLIME_BLOCK)));
	public static final RegistryObject<Block> PIPE = REGISTER.register("pipe", () -> new DirectionalCustomShapeBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(4.0F).sound(SoundType.METAL), MSBlockShapes.PIPE));
	public static final RegistryObject<Block> PIPE_INTERSECTION = REGISTER.register("pipe_intersection", () -> new Block(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(4.0F).sound(SoundType.METAL))); //the intention is that later down the line, someone will improve the code of pipe blocks to allow for intersections or a separate intersection blockset will be made that actually work
	public static final RegistryObject<Block> PARCEL_PYXIS = REGISTER.register("parcel_pyxis", () -> new CustomShapeBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(4.0F), MSBlockShapes.PARCEL_PYXIS));
	public static final RegistryObject<Block> PYXIS_LID = REGISTER.register("pyxis_lid", () -> new CustomShapeBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(1.0F), MSBlockShapes.PYXIS_LID));
	public static final RegistryObject<Block> STONE_TABLET = REGISTER.register("stone_tablet", () -> new StoneTabletBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(0.3F)));
	public static final RegistryObject<Block> NAKAGATOR_STATUE = REGISTER.register("nakagator_statue", () -> new CustomShapeBlock(Block.Properties.of(Material.STONE).strength(0.5F), MSBlockShapes.NAKAGATOR_STATUE));
	
	
	
	//Structure Land Blocks
	public static final RegistryObject<Block> BLACK_CHESS_BRICK_STAIRS = REGISTER.register("black_chess_brick_stairs", () -> new StairBlock(() -> MSBlocks.BLACK_CHESS_BRICKS.get().defaultBlockState(), copy(BLACK_CHESS_BRICKS.get())));
	public static final RegistryObject<Block> DARK_GRAY_CHESS_BRICK_STAIRS = REGISTER.register("dark_gray_chess_brick_stairs", () -> new StairBlock(() -> MSBlocks.DARK_GRAY_CHESS_BRICKS.get().defaultBlockState(), copy(DARK_GRAY_CHESS_BRICKS.get())));
	public static final RegistryObject<Block> LIGHT_GRAY_CHESS_BRICK_STAIRS = REGISTER.register("light_gray_chess_brick_stairs", () -> new StairBlock(() -> MSBlocks.LIGHT_GRAY_CHESS_BRICKS.get().defaultBlockState(), copy(LIGHT_GRAY_CHESS_BRICKS.get())));
	public static final RegistryObject<Block> WHITE_CHESS_BRICK_STAIRS = REGISTER.register("white_chess_brick_stairs", () -> new StairBlock(() -> MSBlocks.WHITE_CHESS_BRICKS.get().defaultBlockState(), copy(WHITE_CHESS_BRICKS.get())));
	public static final RegistryObject<Block> COARSE_STONE_STAIRS = REGISTER.register("coarse_stone_stairs", () -> new StairBlock(() -> MSBlocks.COARSE_STONE.get().defaultBlockState(), copy(COARSE_STONE.get())));
	public static final RegistryObject<Block> COARSE_STONE_BRICK_STAIRS = REGISTER.register("coarse_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.COARSE_STONE_BRICKS.get().defaultBlockState(), copy(COARSE_STONE_BRICKS.get())));
	public static final RegistryObject<Block> SHADE_STAIRS = REGISTER.register("shade_stairs", () -> new StairBlock(() -> MSBlocks.SHADE_STONE.get().defaultBlockState(), copy(SHADE_STONE.get())));
	public static final RegistryObject<Block> SHADE_BRICK_STAIRS = REGISTER.register("shade_brick_stairs", () -> new StairBlock(() -> MSBlocks.SHADE_BRICKS.get().defaultBlockState(), copy(SHADE_BRICKS.get())));
	public static final RegistryObject<Block> FROST_TILE_STAIRS = REGISTER.register("frost_tile_stairs", () -> new StairBlock(() -> MSBlocks.FROST_TILE.get().defaultBlockState(), copy(FROST_TILE.get())));
	public static final RegistryObject<Block> FROST_BRICK_STAIRS = REGISTER.register("frost_brick_stairs", () -> new StairBlock(() -> MSBlocks.FROST_BRICKS.get().defaultBlockState(), copy(FROST_BRICKS.get())));
	public static final RegistryObject<Block> CAST_IRON_STAIRS = REGISTER.register("cast_iron_stairs", () -> new StairBlock(() -> MSBlocks.CAST_IRON.get().defaultBlockState(), copy(CAST_IRON.get())));
	public static final RegistryObject<Block> BLACK_STONE_STAIRS = REGISTER.register("black_stone_stairs", () -> new StairBlock(() -> MSBlocks.BLACK_STONE.get().defaultBlockState(), copy(BLACK_STONE.get())));
	public static final RegistryObject<Block> BLACK_STONE_BRICK_STAIRS = REGISTER.register("black_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.BLACK_STONE_BRICKS.get().defaultBlockState(), copy(BLACK_STONE_BRICKS.get())));
	public static final RegistryObject<Block> FLOWERY_MOSSY_STONE_BRICK_STAIRS = REGISTER.register("flowery_mossy_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.FLOWERY_MOSSY_STONE_BRICKS.get().defaultBlockState(), copy(FLOWERY_MOSSY_STONE_BRICKS.get())));
	public static final RegistryObject<Block> MYCELIUM_STAIRS = REGISTER.register("mycelium_stairs", () -> new StairBlock(() -> MSBlocks.MYCELIUM_STONE.get().defaultBlockState(), copy(MYCELIUM_STONE.get())));
	public static final RegistryObject<Block> MYCELIUM_BRICK_STAIRS = REGISTER.register("mycelium_brick_stairs", () -> new StairBlock(() -> MSBlocks.MYCELIUM_BRICKS.get().defaultBlockState(), copy(MYCELIUM_BRICKS.get())));
	public static final RegistryObject<Block> CHALK_STAIRS = REGISTER.register("chalk_stairs", () -> new StairBlock(() -> MSBlocks.CHALK.get().defaultBlockState(), copy(CHALK.get())));
	public static final RegistryObject<Block> CHALK_BRICK_STAIRS = REGISTER.register("chalk_brick_stairs", () -> new StairBlock(() -> MSBlocks.CHALK_BRICKS.get().defaultBlockState(), copy(CHALK_BRICKS.get())));
	public static final RegistryObject<Block> PINK_STONE_STAIRS = REGISTER.register("pink_stone_stairs", () -> new StairBlock(() -> MSBlocks.PINK_STONE.get().defaultBlockState(), copy(PINK_STONE.get())));
	public static final RegistryObject<Block> PINK_STONE_BRICK_STAIRS = REGISTER.register("pink_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.PINK_STONE_BRICKS.get().defaultBlockState(), copy(PINK_STONE_BRICKS.get())));
	public static final RegistryObject<Block> BROWN_STONE_STAIRS = REGISTER.register("brown_stone_stairs", () -> new StairBlock(() -> MSBlocks.BROWN_STONE.get().defaultBlockState(), copy(BROWN_STONE.get())));
	public static final RegistryObject<Block> BROWN_STONE_BRICK_STAIRS = REGISTER.register("brown_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.BROWN_STONE_BRICKS.get().defaultBlockState(), copy(BROWN_STONE_BRICKS.get())));
	public static final RegistryObject<Block> GREEN_STONE_STAIRS = REGISTER.register("green_stone_stairs", () -> new StairBlock(() -> MSBlocks.GREEN_STONE.get().defaultBlockState(), copy(GREEN_STONE.get())));
	public static final RegistryObject<Block> GREEN_STONE_BRICK_STAIRS = REGISTER.register("green_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.GREEN_STONE_BRICKS.get().defaultBlockState(), copy(GREEN_STONE_BRICKS.get())));
	public static final RegistryObject<Block> RAINBOW_PLANKS_STAIRS = REGISTER.register("rainbow_planks_stairs", () -> new StairBlock(() -> MSBlocks.RAINBOW_PLANKS.get().defaultBlockState(), copy(RAINBOW_PLANKS.get())));
	public static final RegistryObject<Block> END_PLANKS_STAIRS = REGISTER.register("end_planks_stairs", () -> new StairBlock(() -> MSBlocks.END_PLANKS.get().defaultBlockState(), copy(END_PLANKS.get())));
	public static final RegistryObject<Block> DEAD_PLANKS_STAIRS = REGISTER.register("dead_planks_stairs", () -> new StairBlock(() -> MSBlocks.DEAD_PLANKS.get().defaultBlockState(), copy(DEAD_PLANKS.get())));
	public static final RegistryObject<Block> TREATED_PLANKS_STAIRS = REGISTER.register("treated_planks_stairs", () -> new StairBlock(() -> MSBlocks.TREATED_PLANKS.get().defaultBlockState(), copy(TREATED_PLANKS.get())));
	
	public static final RegistryObject<Block> STEEP_GREEN_STONE_BRICK_STAIRS_BASE = REGISTER.register("steep_green_stone_brick_stairs_base", () -> new CustomShapeBlock(copy(GREEN_STONE.get()), MSBlockShapes.STEEP_STAIRS_BASE));
	public static final RegistryObject<Block> STEEP_GREEN_STONE_BRICK_STAIRS_TOP = REGISTER.register("steep_green_stone_brick_stairs_top", () -> new CustomShapeBlock(copy(GREEN_STONE.get()), MSBlockShapes.STEEP_STAIRS_TOP));
	
	public static final RegistryObject<Block> BLACK_CHESS_BRICK_SLAB = REGISTER.register("black_chess_brick_slab", () -> new SlabBlock(copy(BLACK_CHESS_BRICKS.get())));
	public static final RegistryObject<Block> DARK_GRAY_CHESS_BRICK_SLAB = REGISTER.register("dark_gray_chess_brick_slab", () -> new SlabBlock(copy(DARK_GRAY_CHESS_BRICKS.get())));
	public static final RegistryObject<Block> LIGHT_GRAY_CHESS_BRICK_SLAB = REGISTER.register("light_gray_chess_brick_slab", () -> new SlabBlock(copy(LIGHT_GRAY_CHESS_BRICKS.get())));
	public static final RegistryObject<Block> WHITE_CHESS_BRICK_SLAB = REGISTER.register("white_chess_brick_slab", () -> new SlabBlock(copy(WHITE_CHESS_BRICKS.get())));
	public static final RegistryObject<Block> FLOWERY_MOSSY_STONE_BRICK_SLAB = REGISTER.register("flowery_mossy_stone_brick_slab", () -> new SlabBlock(copy(FLOWERY_MOSSY_STONE_BRICKS.get())));
	public static final RegistryObject<Block> COARSE_STONE_SLAB = REGISTER.register("coarse_stone_slab", () -> new SlabBlock(copy(COARSE_STONE.get())));
	public static final RegistryObject<Block> COARSE_STONE_BRICK_SLAB = REGISTER.register("coarse_stone_brick_slab", () -> new SlabBlock(copy(COARSE_STONE_BRICKS.get())));
	public static final RegistryObject<Block> SHADE_SLAB = REGISTER.register("shade_slab", () -> new SlabBlock(copy(SHADE_STONE.get())));
	public static final RegistryObject<Block> SHADE_BRICK_SLAB = REGISTER.register("shade_brick_slab", () -> new SlabBlock(copy(SHADE_BRICKS.get())));
	public static final RegistryObject<Block> FROST_TILE_SLAB = REGISTER.register("frost_tile_slab", () -> new SlabBlock(copy(FROST_TILE.get())));
	public static final RegistryObject<Block> FROST_BRICK_SLAB = REGISTER.register("frost_brick_slab", () -> new SlabBlock(copy(FROST_BRICKS.get())));
	public static final RegistryObject<Block> BLACK_STONE_SLAB = REGISTER.register("black_stone_slab", () -> new SlabBlock(copy(BLACK_STONE.get())));
	public static final RegistryObject<Block> BLACK_STONE_BRICK_SLAB = REGISTER.register("black_stone_brick_slab", () -> new SlabBlock(copy(BLACK_STONE_BRICKS.get())));
	public static final RegistryObject<Block> MYCELIUM_SLAB = REGISTER.register("mycelium_slab", () -> new SlabBlock(copy(MYCELIUM_STONE.get())));
	public static final RegistryObject<Block> MYCELIUM_BRICK_SLAB = REGISTER.register("mycelium_brick_slab", () -> new SlabBlock(copy(MYCELIUM_BRICKS.get())));
	public static final RegistryObject<Block> CHALK_SLAB = REGISTER.register("chalk_slab", () -> new SlabBlock(copy(CHALK.get())));
	public static final RegistryObject<Block> CHALK_BRICK_SLAB = REGISTER.register("chalk_brick_slab", () -> new SlabBlock(copy(CHALK_BRICKS.get())));
	public static final RegistryObject<Block> PINK_STONE_SLAB = REGISTER.register("pink_stone_slab", () -> new SlabBlock(copy(PINK_STONE.get())));
	public static final RegistryObject<Block> PINK_STONE_BRICK_SLAB = REGISTER.register("pink_stone_brick_slab", () -> new SlabBlock(copy(PINK_STONE_BRICKS.get())));
	public static final RegistryObject<Block> BROWN_STONE_SLAB = REGISTER.register("brown_stone_slab", () -> new SlabBlock(copy(BROWN_STONE.get())));
	public static final RegistryObject<Block> BROWN_STONE_BRICK_SLAB = REGISTER.register("brown_stone_brick_slab", () -> new SlabBlock(copy(BROWN_STONE_BRICKS.get())));
	public static final RegistryObject<Block> GREEN_STONE_SLAB = REGISTER.register("green_stone_slab", () -> new SlabBlock(copy(GREEN_STONE.get())));
	public static final RegistryObject<Block> GREEN_STONE_BRICK_SLAB = REGISTER.register("green_stone_brick_slab", () -> new SlabBlock(copy(GREEN_STONE_BRICKS.get())));
	public static final RegistryObject<Block> RAINBOW_PLANKS_SLAB = REGISTER.register("rainbow_planks_slab", () -> new SlabBlock(copy(RAINBOW_PLANKS.get())));
	public static final RegistryObject<Block> END_PLANKS_SLAB = REGISTER.register("end_planks_slab", () -> new SlabBlock(copy(END_PLANKS.get())));
	public static final RegistryObject<Block> DEAD_PLANKS_SLAB = REGISTER.register("dead_planks_slab", () -> new SlabBlock(copy(DEAD_PLANKS.get())));
	public static final RegistryObject<Block> TREATED_PLANKS_SLAB = REGISTER.register("treated_planks_slab", () -> new SlabBlock(copy(TREATED_PLANKS.get())));
	
	
	public static final RegistryObject<Block> TRAJECTORY_BLOCK = REGISTER.register("trajectory_block", () -> new TrajectoryBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final RegistryObject<Block> STAT_STORER = REGISTER.register("stat_storer", () -> new StatStorerBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final RegistryObject<Block> REMOTE_OBSERVER = REGISTER.register("remote_observer", () -> new RemoteObserverBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final RegistryObject<Block> WIRELESS_REDSTONE_TRANSMITTER = REGISTER.register("wireless_redstone_transmitter", () -> new WirelessRedstoneTransmitterBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final RegistryObject<Block> WIRELESS_REDSTONE_RECEIVER = REGISTER.register("wireless_redstone_receiver", () -> new WirelessRedstoneReceiverBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL).randomTicks()));
	public static final RegistryObject<Block> SOLID_SWITCH = REGISTER.register("solid_switch", () -> new SolidSwitchBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL).lightLevel(state -> state.getValue(SolidSwitchBlock.POWERED) ? 15 : 0)));
	public static final RegistryObject<Block> VARIABLE_SOLID_SWITCH = REGISTER.register("variable_solid_switch", () -> new VariableSolidSwitchBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL).lightLevel(state -> state.getValue(VariableSolidSwitchBlock.POWER))));
	public static final RegistryObject<Block> ONE_SECOND_INTERVAL_TIMED_SOLID_SWITCH = REGISTER.register("one_second_interval_timed_solid_switch", () -> new TimedSolidSwitchBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL).lightLevel(state -> state.getValue(TimedSolidSwitchBlock.POWER)), 20));
	public static final RegistryObject<Block> TWO_SECOND_INTERVAL_TIMED_SOLID_SWITCH = REGISTER.register("two_second_interval_timed_solid_switch", () -> new TimedSolidSwitchBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL).lightLevel(state -> state.getValue(TimedSolidSwitchBlock.POWER)), 40));
	public static final RegistryObject<Block> SUMMONER = REGISTER.register("summoner", () -> new SummonerBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final RegistryObject<Block> AREA_EFFECT_BLOCK = REGISTER.register("area_effect_block", () -> new AreaEffectBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(6).sound(SoundType.METAL)));
	public static final RegistryObject<Block> PLATFORM_GENERATOR = REGISTER.register("platform_generator", () -> new PlatformGeneratorBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(6).sound(SoundType.METAL)));
	public static final RegistryObject<Block> PLATFORM_BLOCK = REGISTER.register("platform_block", () -> new PlatformBlock(Block.Properties.of(Material.BARRIER).strength(0.2F).sound(SoundType.SCAFFOLDING).lightLevel(state -> 6).randomTicks().noOcclusion().isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never)));
	public static final RegistryObject<Block> PLATFORM_RECEPTACLE = REGISTER.register("platform_receptacle", () -> new PlatformReceptacleBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final RegistryObject<Block> ITEM_MAGNET = REGISTER.register("item_magnet", () -> new ItemMagnetBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL), new CustomVoxelShape(new double[]{0, 0, 0, 16, 1, 16}, new double[]{1, 1, 1, 15, 15, 15}, new double[]{0, 15, 0, 16, 16, 16})));
	public static final RegistryObject<Block> REDSTONE_CLOCK = REGISTER.register("redstone_clock", () -> new RedstoneClockBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final RegistryObject<Block> ROTATOR = REGISTER.register("rotator", () -> new RotatorBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final RegistryObject<Block> TOGGLER = REGISTER.register("toggler", () -> new TogglerBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final RegistryObject<Block> REMOTE_COMPARATOR = REGISTER.register("remote_comparator", () -> new RemoteComparatorBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final RegistryObject<Block> STRUCTURE_CORE = REGISTER.register("structure_core", () -> new StructureCoreBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(6).sound(SoundType.METAL)));
	public static final RegistryObject<Block> FALL_PAD = REGISTER.register("fall_pad", () -> new FallPadBlock(Block.Properties.of(Material.CLOTH_DECORATION).requiresCorrectToolForDrops().strength(1).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> FRAGILE_STONE = REGISTER.register("fragile_stone", () -> new FragileBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1).sound(SoundType.STONE)));
	public static final RegistryObject<Block> SPIKES = REGISTER.register("spikes", () -> new SpikeBlock(Block.Properties.of(Material.METAL, MaterialColor.METAL).requiresCorrectToolForDrops().strength(2).sound(SoundType.METAL), MSBlockShapes.SPIKES));
	public static final RegistryObject<Block> RETRACTABLE_SPIKES = REGISTER.register("retractable_spikes", () -> new RetractableSpikesBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(1).sound(SoundType.METAL)));
	public static final RegistryObject<Block> BLOCK_PRESSURE_PLATE = REGISTER.register("block_pressure_plate", () -> new BlockPressurePlateBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1).sound(SoundType.STONE)));
	public static final RegistryObject<Block> PUSHABLE_BLOCK = REGISTER.register("pushable_block", () -> new PushableBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1).sound(SoundType.GILDED_BLACKSTONE), PushableBlock.Maneuverability.PUSH_AND_PULL));
	
	public static final RegistryObject<Block> AND_GATE_BLOCK = REGISTER.register("and_gate_block", () -> new LogicGateBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(1).sound(SoundType.METAL), LogicGateBlock.State.AND));
	public static final RegistryObject<Block> OR_GATE_BLOCK = REGISTER.register("or_gate_block", () -> new LogicGateBlock(copy(AND_GATE_BLOCK.get()), LogicGateBlock.State.OR));
	public static final RegistryObject<Block> XOR_GATE_BLOCK = REGISTER.register("xor_gate_block", () -> new LogicGateBlock(copy(AND_GATE_BLOCK.get()), LogicGateBlock.State.XOR));
	public static final RegistryObject<Block> NAND_GATE_BLOCK = REGISTER.register("nand_gate_block", () -> new LogicGateBlock(copy(AND_GATE_BLOCK.get()), LogicGateBlock.State.NAND));
	public static final RegistryObject<Block> NOR_GATE_BLOCK = REGISTER.register("nor_gate_block", () -> new LogicGateBlock(copy(AND_GATE_BLOCK.get()), LogicGateBlock.State.NOR));
	public static final RegistryObject<Block> XNOR_GATE_BLOCK = REGISTER.register("xnor_gate_block", () -> new LogicGateBlock(copy(AND_GATE_BLOCK.get()), LogicGateBlock.State.XNOR));
	
	
	
	//Core Functional Land Blocks
	public static final RegistryObject<Block> GATE = REGISTER.register("gate", () -> new GateBlock(Block.Properties.of(Material.PORTAL).noCollission().strength(-1.0F, 25.0F).sound(SoundType.GLASS).lightLevel(state -> 11).noDrops()));
	public static final RegistryObject<Block> GATE_MAIN = REGISTER.register("gate_main", () -> new GateBlock.Main(Block.Properties.of(Material.PORTAL).noCollission().strength(-1.0F, 25.0F).sound(SoundType.GLASS).lightLevel(state -> 11).noDrops()));
	public static final RegistryObject<Block> RETURN_NODE = REGISTER.register("return_node", () -> new ReturnNodeBlock(Block.Properties.of(Material.PORTAL).noCollission().strength(-1.0F, 10.0F).sound(SoundType.GLASS).lightLevel(state -> 11).noDrops()));
	public static final RegistryObject<Block> RETURN_NODE_MAIN = REGISTER.register("return_node_main", () -> new ReturnNodeBlock.Main(Block.Properties.of(Material.PORTAL).noCollission().strength(-1.0F, 10.0F).sound(SoundType.GLASS).lightLevel(state -> 11).noDrops()));
	
	
	//Misc Functional Land Blocks
	
	
	
	//Sburb Machines
	public static final RegistryObject<Block> CRUXTRUDER_LID = REGISTER.register("cruxtruder_lid", () -> new CruxtruderLidBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(1.0F)));
	public static final CruxtruderMultiblock CRUXTRUDER = new CruxtruderMultiblock(REGISTER);
	public static final TotemLatheMultiblock TOTEM_LATHE = new TotemLatheMultiblock(REGISTER);
	public static final AlchemiterMultiblock ALCHEMITER = new AlchemiterMultiblock(REGISTER);
	public static final PunchDesignixMultiblock PUNCH_DESIGNIX = new PunchDesignixMultiblock(REGISTER);
	
	public static final RegistryObject<Block> MINI_CRUXTRUDER = REGISTER.register("mini_cruxtruder", () -> new SmallMachineBlock<>(MSBlockShapes.SMALL_CRUXTRUDER.createRotatedShapes(), MSBlockEntityTypes.MINI_CRUXTRUDER, Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F)));
	public static final RegistryObject<Block> MINI_TOTEM_LATHE = REGISTER.register("mini_totem_lathe", () -> new SmallMachineBlock<>(MSBlockShapes.SMALL_TOTEM_LATHE.createRotatedShapes(), MSBlockEntityTypes.MINI_TOTEM_LATHE, Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F)));
	public static final RegistryObject<Block> MINI_ALCHEMITER = REGISTER.register("mini_alchemiter", () -> new MiniAlchemiterBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F)));
	public static final RegistryObject<Block> MINI_PUNCH_DESIGNIX = REGISTER.register("mini_punch_designix", () -> new SmallMachineBlock<>(MSBlockShapes.SMALL_PUNCH_DESIGNIX.createRotatedShapes(), MSBlockEntityTypes.MINI_PUNCH_DESIGNIX, Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F)));
	
	public static final RegistryObject<Block> HOLOPAD = REGISTER.register("holopad", () -> new HolopadBlock(Block.Properties.of(Material.METAL, MaterialColor.SNOW).requiresCorrectToolForDrops().strength(3.0F)));
	
	
	
	//Misc Machines
	public static final RegistryObject<Block> COMPUTER = REGISTER.register("computer", () -> new ComputerBlock(ComputerBlock.COMPUTER_SHAPE, ComputerBlock.COMPUTER_SHAPE, Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(4.0F)));
	public static final RegistryObject<Block> LAPTOP = REGISTER.register("laptop", () -> new ComputerBlock(ComputerBlock.LAPTOP_OPEN_SHAPE, ComputerBlock.LAPTOP_CLOSED_SHAPE, Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(4.0F)));
	public static final RegistryObject<Block> CROCKERTOP = REGISTER.register("crockertop", () -> new ComputerBlock(ComputerBlock.LAPTOP_OPEN_SHAPE, ComputerBlock.LAPTOP_CLOSED_SHAPE, Block.Properties.of(Material.METAL, MaterialColor.COLOR_RED).requiresCorrectToolForDrops().strength(4.0F)));
	public static final RegistryObject<Block> HUBTOP = REGISTER.register("hubtop", () -> new ComputerBlock(ComputerBlock.LAPTOP_OPEN_SHAPE, ComputerBlock.LAPTOP_CLOSED_SHAPE, Block.Properties.of(Material.METAL, MaterialColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(4.0F)));
	public static final RegistryObject<Block> LUNCHTOP = REGISTER.register("lunchtop", () -> new ComputerBlock(ComputerBlock.LUNCHTOP_OPEN_SHAPE, ComputerBlock.LUNCHTOP_CLOSED_SHAPE, Block.Properties.of(Material.METAL, MaterialColor.COLOR_RED).requiresCorrectToolForDrops().strength(4.0F)));
	public static final RegistryObject<Block> OLD_COMPUTER = REGISTER.register("old_computer", () -> new ComputerBlock(ComputerBlock.OLD_COMPUTER_SHAPE, ComputerBlock.OLD_COMPUTER_SHAPE, Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(4.0F)));
	public static final RegistryObject<Block> TRANSPORTALIZER = REGISTER.register("transportalizer", () -> new TransportalizerBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final RegistryObject<Block> TRANS_PORTALIZER = REGISTER.register("trans_portalizer", () -> new TransportalizerBlock(copy(TRANSPORTALIZER.get())));
	public static final RegistryObject<Block> SENDIFICATOR = REGISTER.register("sendificator", () -> new SendificatorBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final RegistryObject<Block> GRIST_WIDGET = REGISTER.register("grist_widget", () -> new GristWidgetBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final RegistryObject<Block> URANIUM_COOKER = REGISTER.register("uranium_cooker", () -> new SmallMachineBlock<>(new CustomVoxelShape(new double[]{4, 0, 4, 12, 6, 12}).createRotatedShapes(), MSBlockEntityTypes.URANIUM_COOKER, Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F)));
	
	
	
	//Misc Core Objects
	public static final RegistryObject<Block> CRUXITE_DOWEL = REGISTER.register("cruxite_dowel", () -> new CruxiteDowelBlock(Block.Properties.of(Material.GLASS).strength(0.0F)));
	public static final LotusTimeCapsuleMultiblock LOTUS_TIME_CAPSULE_BLOCK = new LotusTimeCapsuleMultiblock(REGISTER);
	
	
	
	//Misc Alchemy Semi-Plants
	public static final RegistryObject<Block> GOLD_SEEDS = REGISTER.register("gold_seeds", () -> new GoldSeedsBlock(Block.Properties.of(Material.PLANT).strength(0.1F).sound(SoundType.METAL).noCollission()));
	public static final RegistryObject<Block> WOODEN_CACTUS = REGISTER.register("wooden_cactus", () -> new SpecialCactusBlock(Block.Properties.of(Material.WOOD).randomTicks().strength(1.0F, 2.5F).sound(SoundType.WOOD)));
	
	
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
	
	
	
	//Explosives
	public static final RegistryObject<Block> PRIMED_TNT = REGISTER.register("primed_tnt", () -> new SpecialTNTBlock(Block.Properties.of(Material.EXPLOSIVE).strength(0.0F).sound(SoundType.GRASS), true, false, false));
	public static final RegistryObject<Block> UNSTABLE_TNT = REGISTER.register("unstable_tnt", () -> new SpecialTNTBlock(Block.Properties.of(Material.EXPLOSIVE).strength(0.0F).sound(SoundType.GRASS).randomTicks(), false, true, false));
	public static final RegistryObject<Block> INSTANT_TNT = REGISTER.register("instant_tnt", () -> new SpecialTNTBlock(Block.Properties.of(Material.EXPLOSIVE).strength(0.0F).sound(SoundType.GRASS), false, false, true));
	public static final RegistryObject<Block> WOODEN_EXPLOSIVE_BUTTON = REGISTER.register("wooden_explosive_button", () -> new SpecialButtonBlock(Block.Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.WOOD), true, true));
	public static final RegistryObject<Block> STONE_EXPLOSIVE_BUTTON = REGISTER.register("stone_explosive_button", () -> new SpecialButtonBlock(Block.Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.STONE), true, false));
	
	
	public static final RegistryObject<Block> BLENDER = REGISTER.register("blender", () -> new CustomShapeBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(0.5F).sound(SoundType.METAL), MSBlockShapes.BLENDER));
	public static final RegistryObject<Block> CHESSBOARD = REGISTER.register("chessboard", () -> new CustomShapeBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(0.5F), MSBlockShapes.CHESSBOARD));
	public static final RegistryObject<Block> MINI_FROG_STATUE = REGISTER.register("mini_frog_statue", () -> new CustomShapeBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(0.5F), MSBlockShapes.FROG_STATUE));
	public static final RegistryObject<Block> MINI_WIZARD_STATUE = REGISTER.register("mini_wizard_statue", () -> new CustomShapeBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(0.5F), MSBlockShapes.WIZARD_STATUE));
	public static final RegistryObject<Block> MINI_TYPHEUS_STATUE = REGISTER.register("mini_typheus_statue", () -> new CustomShapeBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(0.5F), MSBlockShapes.DENIZEN_STATUE));
	public static final RegistryObject<CassettePlayerBlock> CASSETTE_PLAYER = REGISTER.register("cassette_player", () -> new CassettePlayerBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(0.5F).sound(SoundType.METAL), MSBlockShapes.CASSETTE_PLAYER));
	public static final RegistryObject<Block> GLOWYSTONE_DUST = REGISTER.register("glowystone_dust", () -> new GlowystoneWireBlock(Block.Properties.of(Material.DECORATION).strength(0.0F).lightLevel(state -> 16).noCollission()));
	
	
	public static final RegistryObject<LiquidBlock> OIL = REGISTER.register("oil", () -> new FlowingModFluidBlock(MSFluids.OIL, new Vec3(0.0, 0.0, 0.0), 0.75f, Block.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
	public static final RegistryObject<LiquidBlock> BLOOD = REGISTER.register("blood", () -> new FlowingModFluidBlock(MSFluids.BLOOD, new Vec3(0.8, 0.0, 0.0), 0.25f, Block.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
	public static final RegistryObject<LiquidBlock> BRAIN_JUICE = REGISTER.register("brain_juice", () -> new FlowingModFluidBlock(MSFluids.BRAIN_JUICE, new Vec3(0.55, 0.25, 0.7), 0.25f, Block.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
	public static final RegistryObject<LiquidBlock> WATER_COLORS = REGISTER.register("water_colors", () -> new FlowingWaterColorsBlock(MSFluids.WATER_COLORS, 0.01f, Block.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
	public static final RegistryObject<LiquidBlock> ENDER = REGISTER.register("ender", () -> new FlowingModFluidBlock(MSFluids.ENDER, new Vec3(0, 0.35, 0.35), (Float.MAX_VALUE), Block.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
	public static final RegistryObject<LiquidBlock> LIGHT_WATER = REGISTER.register("light_water", () -> new FlowingModFluidBlock(MSFluids.LIGHT_WATER, new Vec3(0.2, 0.3, 1.0), 0.01f, Block.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
	
	
	private static Function<BlockState, MaterialColor> logColors(MaterialColor topColor, MaterialColor barkColor)
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