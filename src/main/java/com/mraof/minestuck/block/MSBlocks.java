package com.mraof.minestuck.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.fluid.FlowingModFluidBlock;
import com.mraof.minestuck.block.fluid.FlowingWaterColorsBlock;
import com.mraof.minestuck.block.machine.*;
import com.mraof.minestuck.block.plant.*;
import com.mraof.minestuck.block.redstone.*;
import com.mraof.minestuck.fluid.MSFluids;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
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
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;

import java.util.function.Function;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MSBlocks
{
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Minestuck.MOD_ID);
	
	//TODO @ObjectHolder(Minestuck.MOD_ID) was removed from the header, ensure that was ok to do
	//Skaia
	public static final RegistryObject<Block> BLACK_CHESS_DIRT = BLOCKS.register("black_chess_dirt", () -> new Block(Block.Properties.of(Material.DIRT, MaterialColor.COLOR_BLACK).strength(0.5F).sound(SoundType.GRAVEL)));
	public static final RegistryObject<Block> WHITE_CHESS_DIRT = BLOCKS.register("white_chess_dirt", () -> new Block(Block.Properties.of(Material.DIRT, MaterialColor.SNOW).strength(0.5F).sound(SoundType.GRAVEL)));
	public static final RegistryObject<Block> DARK_GRAY_CHESS_DIRT = BLOCKS.register("dark_gray_chess_dirt", () -> new Block(Block.Properties.of(Material.DIRT, MaterialColor.COLOR_GRAY).strength(0.5F).sound(SoundType.GRAVEL)));
	public static final RegistryObject<Block> LIGHT_GRAY_CHESS_DIRT = BLOCKS.register("light_gray_chess_dirt", () -> new Block(Block.Properties.of(Material.DIRT, MaterialColor.COLOR_LIGHT_GRAY).strength(0.5F).sound(SoundType.GRAVEL)));
	public static final RegistryObject<Block> SKAIA_PORTAL = BLOCKS.register("skaia_portal", () -> new SkaiaPortalBlock(Block.Properties.of(Material.PORTAL, MaterialColor.COLOR_CYAN).noCollission().lightLevel(state -> 11).strength(-1.0F, 3600000.0F).noDrops()));
	
	private static final BlockBehaviour.Properties blackChessBricks = Block.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_BLACK).requiresCorrectToolForDrops().strength(1.5F, 6.0F);
	private static final BlockBehaviour.Properties darkGrayChessBricks = Block.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_GRAY).requiresCorrectToolForDrops().strength(1.5F, 6.0F);
	private static final BlockBehaviour.Properties lightGrayChessBricks = Block.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 6.0F);
	private static final BlockBehaviour.Properties whiteChessBricks = Block.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_WHITE).requiresCorrectToolForDrops().strength(1.5F, 6.0F);
	public static final RegistryObject<Block> BLACK_CHESS_BRICKS = BLOCKS.register("black_chess_bricks", () -> new Block(blackChessBricks));
	public static final RegistryObject<Block> DARK_GRAY_CHESS_BRICKS = BLOCKS.register("dark_gray_chess_bricks", () -> new Block(darkGrayChessBricks));
	public static final RegistryObject<Block> LIGHT_GRAY_CHESS_BRICKS = BLOCKS.register("light_gray_chess_bricks", () -> new Block(lightGrayChessBricks));
	public static final RegistryObject<Block> WHITE_CHESS_BRICKS = BLOCKS.register("white_chess_bricks", () -> new Block(whiteChessBricks));
	public static final RegistryObject<Block> BLACK_CHESS_BRICK_SMOOTH = BLOCKS.register("black_chess_brick_smooth", () -> new Block(blackChessBricks));
	public static final RegistryObject<Block> DARK_GRAY_CHESS_BRICK_SMOOTH = BLOCKS.register("dark_gray_chess_brick_smooth", () -> new Block(darkGrayChessBricks));
	public static final RegistryObject<Block> LIGHT_GRAY_CHESS_BRICK_SMOOTH = BLOCKS.register("light_gray_chess_brick_smooth", () -> new Block(lightGrayChessBricks));
	public static final RegistryObject<Block> WHITE_CHESS_BRICK_SMOOTH = BLOCKS.register("white_chess_brick_smooth", () -> new Block(whiteChessBricks));
	public static final RegistryObject<Block> BLACK_CHESS_BRICK_TRIM = BLOCKS.register("black_chess_brick_trim", () -> new MSDirectionalBlock(blackChessBricks));
	public static final RegistryObject<Block> DARK_GRAY_CHESS_BRICK_TRIM = BLOCKS.register("dark_gray_chess_brick_trim", () -> new MSDirectionalBlock(darkGrayChessBricks));
	public static final RegistryObject<Block> LIGHT_GRAY_CHESS_BRICK_TRIM = BLOCKS.register("light_gray_chess_brick_trim", () -> new MSDirectionalBlock(lightGrayChessBricks));
	public static final RegistryObject<Block> WHITE_CHESS_BRICK_TRIM = BLOCKS.register("white_chess_brick_trim", () -> new MSDirectionalBlock(whiteChessBricks));
	
	private static final BlockBehaviour.Properties stainedGlass = Block.Properties.of(Material.GLASS, DyeColor.BLUE).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn(MSBlocks::never).isRedstoneConductor(MSBlocks::never).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never);
	public static final RegistryObject<Block> CHECKERED_STAINED_GLASS = BLOCKS.register("checkered_stained_glass", () -> new StainedGlassBlock(DyeColor.BLUE, stainedGlass));
	public static final RegistryObject<Block> BLACK_CROWN_STAINED_GLASS = BLOCKS.register("black_crown_stained_glass", () -> new StainedGlassBlock(DyeColor.BLUE, stainedGlass));
	public static final RegistryObject<Block> BLACK_PAWN_STAINED_GLASS = BLOCKS.register("black_pawn_stained_glass", () -> new StainedGlassBlock(DyeColor.BLUE, stainedGlass));
	public static final RegistryObject<Block> WHITE_CROWN_STAINED_GLASS = BLOCKS.register("white_crown_stained_glass", () -> new StainedGlassBlock(DyeColor.BLUE, stainedGlass));
	public static final RegistryObject<Block> WHITE_PAWN_STAINED_GLASS = BLOCKS.register("white_pawn_stained_glass", () -> new StainedGlassBlock(DyeColor.BLUE, stainedGlass));
	
	
	
	//Ores
	private static final BlockBehaviour.Properties cruxiteOre = Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops();
	public static final UniformInt cruxiteDrops = UniformInt.of(2, 5);
	public static final RegistryObject<Block> STONE_CRUXITE_ORE = BLOCKS.register("stone_cruxite_ore", () -> new OreBlock(cruxiteOre, cruxiteDrops));
	public static final RegistryObject<Block> NETHERRACK_CRUXITE_ORE = BLOCKS.register("netherrack_cruxite_ore", () -> new OreBlock(cruxiteOre, cruxiteDrops));
	public static final RegistryObject<Block> COBBLESTONE_CRUXITE_ORE = BLOCKS.register("cobblestone_cruxite_ore", () -> new OreBlock(cruxiteOre, cruxiteDrops));
	public static final RegistryObject<Block> SANDSTONE_CRUXITE_ORE = BLOCKS.register("sandstone_cruxite_ore", () -> new OreBlock(cruxiteOre, cruxiteDrops));
	public static final RegistryObject<Block> RED_SANDSTONE_CRUXITE_ORE = BLOCKS.register("red_sandstone_cruxite_ore", () -> new OreBlock(cruxiteOre, cruxiteDrops));
	public static final RegistryObject<Block> END_STONE_CRUXITE_ORE = BLOCKS.register("end_stone_cruxite_ore", () -> new OreBlock(cruxiteOre, cruxiteDrops));
	public static final RegistryObject<Block> SHADE_STONE_CRUXITE_ORE = BLOCKS.register("shade_stone_cruxite_ore", () -> new OreBlock(cruxiteOre, cruxiteDrops));
	public static final RegistryObject<Block> PINK_STONE_CRUXITE_ORE = BLOCKS.register("pink_stone_cruxite_ore", () -> new OreBlock(cruxiteOre, cruxiteDrops));
	
	private static final BlockBehaviour.Properties uraniumOre = Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().lightLevel(state -> 3);
	private static final UniformInt uraniumDrops = UniformInt.of(2, 5);
	public static final RegistryObject<Block> STONE_URANIUM_ORE = BLOCKS.register("stone_uranium_ore", () -> new OreBlock(uraniumOre, uraniumDrops));
	public static final RegistryObject<Block> NETHERRACK_URANIUM_ORE = BLOCKS.register("netherrack_uranium_ore", () -> new OreBlock(uraniumOre, uraniumDrops));
	public static final RegistryObject<Block> COBBLESTONE_URANIUM_ORE = BLOCKS.register("cobblestone_uranium_ore", () -> new OreBlock(uraniumOre, uraniumDrops));
	public static final RegistryObject<Block> SANDSTONE_URANIUM_ORE = BLOCKS.register("sandstone_uranium_ore", () -> new OreBlock(uraniumOre, uraniumDrops));
	public static final RegistryObject<Block> RED_SANDSTONE_URANIUM_ORE = BLOCKS.register("red_sandstone_uranium_ore", () -> new OreBlock(uraniumOre, uraniumDrops));
	public static final RegistryObject<Block> END_STONE_URANIUM_ORE = BLOCKS.register("end_stone_uranium_ore", () -> new OreBlock(uraniumOre, uraniumDrops));
	public static final RegistryObject<Block> SHADE_STONE_URANIUM_ORE = BLOCKS.register("shade_stone_uranium_ore", () -> new OreBlock(uraniumOre, uraniumDrops));
	public static final RegistryObject<Block> PINK_STONE_URANIUM_ORE = BLOCKS.register("pink_stone_uranium_ore", () -> new OreBlock(uraniumOre, uraniumDrops));
	
	private static final BlockBehaviour.Properties genericOre = Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops();
	private static final UniformInt coalDrops = UniformInt.of(0, 2);
	public static final RegistryObject<Block> NETHERRACK_COAL_ORE = BLOCKS.register("netherrack_coal_ore", () -> new OreBlock(genericOre, coalDrops));
	public static final RegistryObject<Block> SHADE_STONE_COAL_ORE = BLOCKS.register("shade_stone_coal_ore", () -> new OreBlock(genericOre, coalDrops));
	public static final RegistryObject<Block> PINK_STONE_COAL_ORE = BLOCKS.register("pink_stone_coal_ore", () -> new OreBlock(genericOre, coalDrops));
	
	public static final RegistryObject<Block> END_STONE_IRON_ORE = BLOCKS.register("end_stone_iron_ore", () -> new OreBlock(genericOre));
	public static final RegistryObject<Block> SANDSTONE_IRON_ORE = BLOCKS.register("sandstone_iron_ore", () -> new OreBlock(genericOre));
	public static final RegistryObject<Block> RED_SANDSTONE_IRON_ORE = BLOCKS.register("red_sandstone_iron_ore", () -> new OreBlock(genericOre));
	
	public static final RegistryObject<Block> SANDSTONE_GOLD_ORE = BLOCKS.register("sandstone_gold_ore", () -> new OreBlock(genericOre));
	public static final RegistryObject<Block> RED_SANDSTONE_GOLD_ORE = BLOCKS.register("red_sandstone_gold_ore", () -> new OreBlock(genericOre));
	public static final RegistryObject<Block> SHADE_STONE_GOLD_ORE = BLOCKS.register("shade_stone_gold_ore", () -> new OreBlock(genericOre));
	public static final RegistryObject<Block> PINK_STONE_GOLD_ORE = BLOCKS.register("pink_stone_gold_ore", () -> new OreBlock(genericOre));
	
	public static final RegistryObject<Block> END_STONE_REDSTONE_ORE = BLOCKS.register("end_stone_redstone_ore", () -> new OreBlock(genericOre, UniformInt.of(1, 5)));
	public static final RegistryObject<Block> STONE_QUARTZ_ORE = BLOCKS.register("stone_quartz_ore", () -> new OreBlock(genericOre, UniformInt.of(2, 5)));
	public static final RegistryObject<Block> PINK_STONE_LAPIS_ORE = BLOCKS.register("pink_stone_lapis_ore", () -> new OreBlock(genericOre, UniformInt.of(2, 5)));
	public static final RegistryObject<Block> PINK_STONE_DIAMOND_ORE = BLOCKS.register("pink_stone_diamond_ore", () -> new OreBlock(genericOre, UniformInt.of(3, 7)));
	
	
	
	//Resource Blocks
	public static final RegistryObject<Block> CRUXITE_BLOCK = BLOCKS.register("cruxite_block", () -> new Block(Block.Properties.of(Material.STONE, DyeColor.LIGHT_BLUE).strength(3.0F).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> URANIUM_BLOCK = BLOCKS.register("uranium_block", () -> new Block(Block.Properties.of(Material.STONE, DyeColor.LIME).strength(3.0F).requiresCorrectToolForDrops().lightLevel(state -> 7)));
	public static final RegistryObject<Block> GENERIC_OBJECT = BLOCKS.register("generic_object", () -> new Block(Block.Properties.of(Material.VEGETABLE, DyeColor.LIME).strength(1.0F).sound(SoundType.WOOD)));
	
	
	
	//Land Environment
	public static final RegistryObject<Block> BLUE_DIRT = BLOCKS.register("blue_dirt", () -> new Block(Block.Properties.of(Material.DIRT, MaterialColor.COLOR_BLUE).strength(0.5F).sound(SoundType.GRAVEL)));
	public static final RegistryObject<Block> THOUGHT_DIRT = BLOCKS.register("thought_dirt", () -> new Block(Block.Properties.of(Material.DIRT, MaterialColor.COLOR_LIGHT_GREEN).strength(0.5F).sound(SoundType.GRAVEL)));
	
	private static final BlockBehaviour.Properties coarseStone = Block.Properties.of(Material.STONE, MaterialColor.STONE).requiresCorrectToolForDrops().strength(2.0F, 6.0F);
	public static final RegistryObject<Block> COARSE_STONE = BLOCKS.register("coarse_stone", () -> new Block(coarseStone));
	public static final RegistryObject<Block> CHISELED_COARSE_STONE = BLOCKS.register("chiseled_coarse_stone", () -> new Block(coarseStone));
	public static final RegistryObject<Block> COARSE_STONE_BRICKS = BLOCKS.register("coarse_stone_bricks", () -> new Block(coarseStone));
	public static final RegistryObject<Block> COARSE_STONE_COLUMN = BLOCKS.register("coarse_stone_column", () -> new MSDirectionalBlock(coarseStone));
	public static final RegistryObject<Block> CHISELED_COARSE_STONE_BRICKS = BLOCKS.register("chiseled_coarse_stone_bricks", () -> new Block(coarseStone));
	public static final RegistryObject<Block> CRACKED_COARSE_STONE_BRICKS = BLOCKS.register("cracked_coarse_stone_bricks", () -> new Block(coarseStone));
	public static final RegistryObject<Block> MOSSY_COARSE_STONE_BRICKS = BLOCKS.register("mossy_coarse_stone_bricks", () -> new Block(coarseStone));
	
	private static final BlockBehaviour.Properties shadeStone = Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).requiresCorrectToolForDrops().strength(1.5F, 6.0F);
	public static final RegistryObject<Block> SHADE_STONE = BLOCKS.register("shade_stone", () -> new Block(shadeStone));
	public static final RegistryObject<Block> SMOOTH_SHADE_STONE = BLOCKS.register("smooth_shade_stone", () -> new Block(shadeStone));
	public static final RegistryObject<Block> SHADE_BRICKS = BLOCKS.register("shade_bricks", () -> new Block(shadeStone));
	public static final RegistryObject<Block> SHADE_COLUMN = BLOCKS.register("shade_column", () -> new MSDirectionalBlock(shadeStone));
	public static final RegistryObject<Block> CHISELED_SHADE_BRICKS = BLOCKS.register("chiseled_shade_bricks", () -> new Block(shadeStone));
	public static final RegistryObject<Block> CRACKED_SHADE_BRICKS = BLOCKS.register("cracked_shade_bricks", () -> new Block(shadeStone));
	public static final RegistryObject<Block> MOSSY_SHADE_BRICKS = BLOCKS.register("mossy_shade_bricks", () -> new Block(shadeStone));
	public static final RegistryObject<Block> BLOOD_SHADE_BRICKS = BLOCKS.register("blood_shade_bricks", () -> new Block(shadeStone));
	public static final RegistryObject<Block> TAR_SHADE_BRICKS = BLOCKS.register("tar_shade_bricks", () -> new Block(shadeStone));
	
	private static final BlockBehaviour.Properties frost = Block.Properties.of(Material.STONE, MaterialColor.ICE).requiresCorrectToolForDrops().strength(1.5F, 6.0F); //TODO consider changing material to ICE_SOLID
	public static final RegistryObject<Block> FROST_TILE = BLOCKS.register("frost_tile", () -> new Block(frost));
	public static final RegistryObject<Block> CHISELED_FROST_TILE = BLOCKS.register("chiseled_frost_tile", () -> new Block(frost));
	public static final RegistryObject<Block> FROST_BRICKS = BLOCKS.register("frost_bricks", () -> new Block(frost));
	public static final RegistryObject<Block> FROST_COLUMN = BLOCKS.register("frost_column", () -> new MSDirectionalBlock(frost));
	public static final RegistryObject<Block> CHISELED_FROST_BRICKS = BLOCKS.register("chiseled_frost_bricks", () -> new Block(frost)); //while it is a pillar block, it cannot be rotated, making it similar to cut sandstone
	public static final RegistryObject<Block> CRACKED_FROST_BRICKS = BLOCKS.register("cracked_frost_bricks", () -> new Block(frost));
	public static final RegistryObject<Block> FLOWERY_FROST_BRICKS = BLOCKS.register("flowery_frost_bricks", () -> new Block(frost));
	
	private static final BlockBehaviour.Properties metal = Block.Properties.of(Material.METAL, MaterialColor.METAL).requiresCorrectToolForDrops().strength(3.0F, 9.0F);
	public static final RegistryObject<Block> CAST_IRON = BLOCKS.register("cast_iron", () -> new Block(metal));
	public static final RegistryObject<Block> CHISELED_CAST_IRON = BLOCKS.register("chiseled_cast_iron", () -> new Block(metal));
	public static final RegistryObject<Block> STEEL_BEAM = BLOCKS.register("steel_beam", () -> new MSDirectionalBlock(metal));
	
	private static final BlockBehaviour.Properties myceliumStone = Block.Properties.of(Material.STONE, MaterialColor.COLOR_MAGENTA).requiresCorrectToolForDrops().strength(1.5F, 6.0F);
	public static final RegistryObject<Block> MYCELIUM_COBBLESTONE = BLOCKS.register("mycelium_cobblestone", () -> new Block(myceliumStone));
	public static final RegistryObject<Block> MYCELIUM_STONE = BLOCKS.register("mycelium_stone", () -> new Block(myceliumStone));
	public static final RegistryObject<Block> POLISHED_MYCELIUM_STONE = BLOCKS.register("polished_mycelium_stone", () -> new Block(myceliumStone));
	public static final RegistryObject<Block> MYCELIUM_BRICKS = BLOCKS.register("mycelium_bricks", () -> new Block(myceliumStone));
	public static final RegistryObject<Block> MYCELIUM_COLUMN = BLOCKS.register("mycelium_column", () -> new MSDirectionalBlock(myceliumStone));
	public static final RegistryObject<Block> CHISELED_MYCELIUM_BRICKS = BLOCKS.register("chiseled_mycelium_bricks", () -> new Block(myceliumStone));
	public static final RegistryObject<Block> CRACKED_MYCELIUM_BRICKS = BLOCKS.register("cracked_mycelium_bricks", () -> new Block(myceliumStone));
	public static final RegistryObject<Block> MOSSY_MYCELIUM_BRICKS = BLOCKS.register("mossy_mycelium_bricks", () -> new Block(myceliumStone));
	public static final RegistryObject<Block> FLOWERY_MYCELIUM_BRICKS = BLOCKS.register("flowery_mycelium_bricks", () -> new Block(myceliumStone));
	
	private static final BlockBehaviour.Properties blackStone = Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(2.5F, 6.0F);
	public static final RegistryObject<Block> BLACK_SAND = BLOCKS.register("black_sand", () -> new SandBlock(0x181915, Block.Properties.of(Material.SAND, MaterialColor.COLOR_BLACK).strength(0.5F).sound(SoundType.SAND)));
	public static final RegistryObject<Block> BLACK_COBBLESTONE = BLOCKS.register("black_cobblestone", () -> new Block(blackStone));
	public static final RegistryObject<Block> BLACK_STONE = BLOCKS.register("black_stone", () -> new Block(blackStone));
	public static final RegistryObject<Block> POLISHED_BLACK_STONE = BLOCKS.register("polished_black_stone", () -> new Block(blackStone));
	public static final RegistryObject<Block> BLACK_STONE_BRICKS = BLOCKS.register("black_stone_bricks", () -> new Block(blackStone));
	public static final RegistryObject<Block> BLACK_STONE_COLUMN = BLOCKS.register("black_stone_column", () -> new MSDirectionalBlock(blackStone));
	public static final RegistryObject<Block> CHISELED_BLACK_STONE_BRICKS = BLOCKS.register("chiseled_black_stone_bricks", () -> new Block(blackStone));
	public static final RegistryObject<Block> CRACKED_BLACK_STONE_BRICKS = BLOCKS.register("cracked_black_stone_bricks", () -> new Block(blackStone));
	
	private static final BlockBehaviour.Properties floweryOrDecrepitStone = Block.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(1.5F, 6.0F);
	public static final RegistryObject<Block> FLOWERY_MOSSY_COBBLESTONE = BLOCKS.register("flowery_mossy_cobblestone", () -> new Block(floweryOrDecrepitStone));
	public static final RegistryObject<Block> FLOWERY_MOSSY_STONE_BRICKS = BLOCKS.register("flowery_mossy_stone_bricks", () -> new Block(floweryOrDecrepitStone));
	public static final RegistryObject<Block> DECREPIT_STONE_BRICKS = BLOCKS.register("decrepit_stone_bricks", () -> new Block(floweryOrDecrepitStone));
	public static final RegistryObject<Block> MOSSY_DECREPIT_STONE_BRICKS = BLOCKS.register("mossy_decrepit_stone_bricks", () -> new Block(floweryOrDecrepitStone));
	public static final RegistryObject<Block> COARSE_END_STONE = BLOCKS.register("coarse_end_stone", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.SAND).requiresCorrectToolForDrops().strength(3.0F, 9.0F)));
	public static final RegistryObject<Block> END_GRASS = BLOCKS.register("end_grass", () -> new EndGrassBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(3.0F, 9.0F)));
	
	private static final BlockBehaviour.Properties chalk = Block.Properties.of(Material.STONE, MaterialColor.SNOW).requiresCorrectToolForDrops().strength(1.5F, 6.0F);
	public static final RegistryObject<Block> CHALK = BLOCKS.register("chalk", () -> new Block(chalk));
	public static final RegistryObject<Block> POLISHED_CHALK = BLOCKS.register("polished_chalk", () -> new Block(chalk));
	public static final RegistryObject<Block> CHALK_BRICKS = BLOCKS.register("chalk_bricks", () -> new Block(chalk));
	public static final RegistryObject<Block> CHALK_COLUMN = BLOCKS.register("chalk_column", () -> new MSDirectionalBlock(chalk));
	public static final RegistryObject<Block> CHISELED_CHALK_BRICKS = BLOCKS.register("chiseled_chalk_bricks", () -> new Block(chalk));
	public static final RegistryObject<Block> MOSSY_CHALK_BRICKS = BLOCKS.register("mossy_chalk_bricks", () -> new Block(chalk));
	public static final RegistryObject<Block> FLOWERY_CHALK_BRICKS = BLOCKS.register("flowery_chalk_bricks", () -> new Block(chalk));
	
	private static final BlockBehaviour.Properties pinkStone = Block.Properties.of(Material.STONE, MaterialColor.SNOW).requiresCorrectToolForDrops().strength(1.5F, 6.0F);
	public static final RegistryObject<Block> PINK_STONE = BLOCKS.register("pink_stone", () -> new Block(pinkStone));
	public static final RegistryObject<Block> POLISHED_PINK_STONE = BLOCKS.register("polished_pink_stone", () -> new Block(pinkStone));
	public static final RegistryObject<Block> PINK_STONE_BRICKS = BLOCKS.register("pink_stone_bricks", () -> new Block(pinkStone));
	public static final RegistryObject<Block> PINK_STONE_COLUMN = BLOCKS.register("pink_stone_column", () -> new MSDirectionalBlock(pinkStone));
	public static final RegistryObject<Block> CHISELED_PINK_STONE_BRICKS = BLOCKS.register("chiseled_pink_stone_bricks", () -> new Block(pinkStone));
	public static final RegistryObject<Block> CRACKED_PINK_STONE_BRICKS = BLOCKS.register("cracked_pink_stone_bricks", () -> new Block(pinkStone));
	public static final RegistryObject<Block> MOSSY_PINK_STONE_BRICKS = BLOCKS.register("mossy_pink_stone_bricks", () -> new Block(pinkStone));
	
	private static final BlockBehaviour.Properties brownStone = Block.Properties.of(Material.STONE, MaterialColor.COLOR_BROWN).requiresCorrectToolForDrops().strength(2.5F, 7.0F);
	public static final RegistryObject<Block> BROWN_STONE = BLOCKS.register("brown_stone", () -> new Block(brownStone));
	public static final RegistryObject<Block> POLISHED_BROWN_STONE = BLOCKS.register("polished_brown_stone", () -> new Block(brownStone));
	public static final RegistryObject<Block> BROWN_STONE_BRICKS = BLOCKS.register("brown_stone_bricks", () -> new Block(brownStone));
	public static final RegistryObject<Block> CRACKED_BROWN_STONE_BRICKS = BLOCKS.register("cracked_brown_stone_bricks", () -> new Block(brownStone));
	public static final RegistryObject<Block> BROWN_STONE_COLUMN = BLOCKS.register("brown_stone_column", () -> new MSDirectionalBlock(brownStone));
	
	private static final BlockBehaviour.Properties greenStone = Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(2.5F, 7.0F);
	public static final RegistryObject<Block> GREEN_STONE = BLOCKS.register("green_stone", () -> new Block(greenStone));
	public static final RegistryObject<Block> POLISHED_GREEN_STONE = BLOCKS.register("polished_green_stone", () -> new Block(greenStone));
	public static final RegistryObject<Block> GREEN_STONE_BRICKS = BLOCKS.register("green_stone_bricks", () -> new Block(greenStone));
	public static final RegistryObject<Block> GREEN_STONE_COLUMN = BLOCKS.register("green_stone_column", () -> new MSDirectionalBlock(greenStone));
	public static final RegistryObject<Block> CHISELED_GREEN_STONE_BRICKS = BLOCKS.register("chiseled_green_stone_bricks", () -> new Block(greenStone));
	public static final RegistryObject<Block> HORIZONTAL_GREEN_STONE_BRICKS = BLOCKS.register("horizontal_green_stone_bricks", () -> new Block(greenStone));
	public static final RegistryObject<Block> VERTICAL_GREEN_STONE_BRICKS = BLOCKS.register("vertical_green_stone_bricks", () -> new Block(greenStone));
	public static final RegistryObject<Block> GREEN_STONE_BRICK_TRIM = BLOCKS.register("green_stone_brick_trim", () -> new MSDirectionalBlock(greenStone));
	public static final RegistryObject<Block> GREEN_STONE_BRICK_FROG = BLOCKS.register("green_stone_brick_frog", () -> new HieroglyphBlock(greenStone));
	public static final RegistryObject<Block> GREEN_STONE_BRICK_IGUANA_LEFT = BLOCKS.register("green_stone_brick_iguana_left", () -> new HieroglyphBlock(greenStone));
	public static final RegistryObject<Block> GREEN_STONE_BRICK_IGUANA_RIGHT = BLOCKS.register("green_stone_brick_iguana_right", () -> new HieroglyphBlock(greenStone));
	public static final RegistryObject<Block> GREEN_STONE_BRICK_LOTUS = BLOCKS.register("green_stone_brick_lotus", () -> new HieroglyphBlock(greenStone));
	public static final RegistryObject<Block> GREEN_STONE_BRICK_NAK_LEFT = BLOCKS.register("green_stone_brick_nak_left", () -> new HieroglyphBlock(greenStone));
	public static final RegistryObject<Block> GREEN_STONE_BRICK_NAK_RIGHT = BLOCKS.register("green_stone_brick_nak_right", () -> new HieroglyphBlock(greenStone));
	public static final RegistryObject<Block> GREEN_STONE_BRICK_SALAMANDER_LEFT = BLOCKS.register("green_stone_brick_salamander_left", () -> new HieroglyphBlock(greenStone));
	public static final RegistryObject<Block> GREEN_STONE_BRICK_SALAMANDER_RIGHT = BLOCKS.register("green_stone_brick_salamander_right", () -> new HieroglyphBlock(greenStone));
	public static final RegistryObject<Block> GREEN_STONE_BRICK_SKAIA = BLOCKS.register("green_stone_brick_skaia", () -> new HieroglyphBlock(greenStone));
	public static final RegistryObject<Block> GREEN_STONE_BRICK_TURTLE = BLOCKS.register("green_stone_brick_turtle", () -> new HieroglyphBlock(greenStone));
	
	public static final RegistryObject<Block> SANDSTONE_COLUMN = BLOCKS.register("sandstone_column", () -> new MSDirectionalBlock(Block.Properties.of(Material.STONE, MaterialColor.SAND).requiresCorrectToolForDrops().strength(0.8F)));
	public static final RegistryObject<Block> CHISELED_SANDSTONE_COLUMN = BLOCKS.register("chiseled_sandstone_column", () -> new MSDirectionalBlock(Block.Properties.of(Material.STONE, MaterialColor.SAND).requiresCorrectToolForDrops().strength(0.8F)));
	public static final RegistryObject<Block> RED_SANDSTONE_COLUMN = BLOCKS.register("red_sandstone_column", () -> new MSDirectionalBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(0.8F)));
	public static final RegistryObject<Block> CHISELED_RED_SANDSTONE_COLUMN = BLOCKS.register("chiseled_red_sandstone_column", () -> new MSDirectionalBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(0.8F)));
	
	public static final RegistryObject<Block> UNCARVED_WOOD = BLOCKS.register("uncarved_wood", () -> new Block(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).requiresCorrectToolForDrops().sound(SoundType.WOOD)));
	public static final RegistryObject<Block> CHIPBOARD = BLOCKS.register("chipboard", () -> new Block(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(1.0F).requiresCorrectToolForDrops().sound(SoundType.SCAFFOLDING)));
	public static final RegistryObject<Block> WOOD_SHAVINGS = BLOCKS.register("wood_shavings", () -> new Block(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(0.4F).sound(SoundType.SAND)));
	
	public static final RegistryObject<Block> DENSE_CLOUD = BLOCKS.register("dense_cloud", () -> new Block(Block.Properties.of(Material.GLASS, MaterialColor.COLOR_YELLOW).strength(0.5F).sound(SoundType.SNOW)));
	public static final RegistryObject<Block> BRIGHT_DENSE_CLOUD = BLOCKS.register("bright_dense_cloud", () -> new Block(Block.Properties.of(Material.GLASS, MaterialColor.COLOR_LIGHT_GRAY).strength(0.5F).sound(SoundType.SNOW)));
	public static final RegistryObject<Block> SUGAR_CUBE = BLOCKS.register("sugar_cube", () -> new Block(Block.Properties.of(Material.SAND, MaterialColor.SNOW).strength(0.4F).sound(SoundType.SAND)));
	
	
	
	//Land Tree Blocks
	public static final RegistryObject<Block> GLOWING_LOG = BLOCKS.register("glowing_log", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.COLOR_LIGHT_BLUE).strength(2.0F).lightLevel(state -> 11).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> FROST_LOG = BLOCKS.register("frost_log", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.ICE).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> RAINBOW_LOG = BLOCKS.register("rainbow_log", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> END_LOG = BLOCKS.register("end_log", () -> new DoubleLogBlock(1, 250, Block.Properties.of(Material.WOOD, MaterialColor.SAND).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> VINE_LOG = BLOCKS.register("vine_log", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, logColors(MaterialColor.WOOD, MaterialColor.PODZOL)).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> FLOWERY_VINE_LOG = BLOCKS.register("flowery_vine_log", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, logColors(MaterialColor.WOOD, MaterialColor.PODZOL)).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> DEAD_LOG = BLOCKS.register("dead_log", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, logColors(MaterialColor.WOOD, MaterialColor.PODZOL)).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> PETRIFIED_LOG = BLOCKS.register("petrified_log", () -> new RotatedPillarBlock(Block.Properties.of(Material.WOOD, logColors(MaterialColor.WOOD, MaterialColor.PODZOL)).strength(2.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> GLOWING_WOOD = BLOCKS.register("glowing_wood", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.COLOR_LIGHT_BLUE).strength(2.0F).lightLevel(state -> 11).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> FROST_WOOD = BLOCKS.register("frost_wood", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.ICE).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> RAINBOW_WOOD = BLOCKS.register("rainbow_wood", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> END_WOOD = BLOCKS.register("end_wood", () -> new FlammableLogBlock(1, 250, Block.Properties.of(Material.WOOD, MaterialColor.SAND).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> VINE_WOOD = BLOCKS.register("vine_wood", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> FLOWERY_VINE_WOOD = BLOCKS.register("flowery_vine_wood", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> DEAD_WOOD = BLOCKS.register("dead_wood", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> PETRIFIED_WOOD = BLOCKS.register("petrified_wood", () -> new RotatedPillarBlock(Block.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> GLOWING_PLANKS = BLOCKS.register("glowing_planks", () -> new FlammableBlock(5, 20, Block.Properties.of(Material.WOOD, MaterialColor.COLOR_LIGHT_BLUE).strength(2.0F, 3.0F).lightLevel(state -> 7).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> FROST_PLANKS = BLOCKS.register("frost_planks", () -> new FlammableBlock(5, 5, Block.Properties.of(Material.WOOD, MaterialColor.ICE).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	
	private static final BlockBehaviour.Properties rainbowPlanks = Block.Properties.of(Material.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD);
	public static final RegistryObject<Block> RAINBOW_PLANKS = BLOCKS.register("rainbow_planks", () -> new FlammableBlock(5, 20, rainbowPlanks));
	
	private static final BlockBehaviour.Properties endPlanks = Block.Properties.of(Material.WOOD, MaterialColor.SAND).strength(2.0F, 3.0F).sound(SoundType.WOOD);
	public static final RegistryObject<Block> END_PLANKS = BLOCKS.register("end_planks", () -> new FlammableBlock(1, 250, endPlanks));
	
	private static final BlockBehaviour.Properties deadPlanks = Block.Properties.of(Material.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD);
	public static final RegistryObject<Block> DEAD_PLANKS = BLOCKS.register("dead_planks", () -> new FlammableBlock(5, 5, deadPlanks));
	
	private static final BlockBehaviour.Properties treatedPlanks = Block.Properties.of(Material.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD);
	public static final RegistryObject<Block> TREATED_PLANKS = BLOCKS.register("treated_planks", () -> new FlammableBlock(0, 0, treatedPlanks));
	
	private static final BlockBehaviour.Properties leaves = Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(MSBlocks::leafSpawns).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never);
	public static final RegistryObject<Block> FROST_LEAVES = BLOCKS.register("frost_leaves", () -> new FlammableLeavesBlock(leaves));
	public static final RegistryObject<Block> RAINBOW_LEAVES = BLOCKS.register("rainbow_leaves", () -> new FlammableLeavesBlock(leaves));
	public static final RegistryObject<Block> END_LEAVES = BLOCKS.register("end_leaves", () -> new EndLeavesBlock(leaves));
	
	private static final BlockBehaviour.Properties sapling = Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS);
	public static final RegistryObject<BushBlock> RAINBOW_SAPLING = BLOCKS.register("rainbow_sapling", () -> new RainbowSaplingBlock(sapling));
	public static final RegistryObject<BushBlock> END_SAPLING = BLOCKS.register("end_sapling", () -> new EndSaplingBlock(sapling));
	
	
	
	//Aspect Tree Blocks
	private static final BlockBehaviour.Properties aspectLog = Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD);
	public static final RegistryObject<Block> BLOOD_ASPECT_LOG = BLOCKS.register("blood_aspect_log", () -> new FlammableLogBlock(aspectLog));
	public static final RegistryObject<Block> BREATH_ASPECT_LOG = BLOCKS.register("breath_aspect_log", () -> new FlammableLogBlock(aspectLog));
	public static final RegistryObject<Block> DOOM_ASPECT_LOG = BLOCKS.register("doom_aspect_log", () -> new FlammableLogBlock(aspectLog));
	public static final RegistryObject<Block> HEART_ASPECT_LOG = BLOCKS.register("heart_aspect_log", () -> new FlammableLogBlock(aspectLog));
	public static final RegistryObject<Block> HOPE_ASPECT_LOG = BLOCKS.register("hope_aspect_log", () -> new FlammableLogBlock(aspectLog));
	public static final RegistryObject<Block> LIFE_ASPECT_LOG = BLOCKS.register("life_aspect_log", () -> new FlammableLogBlock(aspectLog));
	public static final RegistryObject<Block> LIGHT_ASPECT_LOG = BLOCKS.register("light_aspect_log", () -> new FlammableLogBlock(aspectLog));
	public static final RegistryObject<Block> MIND_ASPECT_LOG = BLOCKS.register("mind_aspect_log", () -> new FlammableLogBlock(aspectLog));
	public static final RegistryObject<Block> RAGE_ASPECT_LOG = BLOCKS.register("rage_aspect_log", () -> new FlammableLogBlock(aspectLog));
	public static final RegistryObject<Block> SPACE_ASPECT_LOG = BLOCKS.register("space_aspect_log", () -> new FlammableLogBlock(aspectLog));
	public static final RegistryObject<Block> TIME_ASPECT_LOG = BLOCKS.register("time_aspect_log", () -> new FlammableLogBlock(aspectLog));
	public static final RegistryObject<Block> VOID_ASPECT_LOG = BLOCKS.register("void_aspect_log", () -> new FlammableLogBlock(aspectLog));
	
	private static final BlockBehaviour.Properties aspectPlanks = Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD);
	public static final RegistryObject<Block> BLOOD_ASPECT_PLANKS = BLOCKS.register("blood_aspect_planks", () -> new FlammableBlock(5, 20, aspectPlanks));
	public static final RegistryObject<Block> BREATH_ASPECT_PLANKS = BLOCKS.register("breath_aspect_planks", () -> new FlammableBlock(5, 20, aspectPlanks));
	public static final RegistryObject<Block> DOOM_ASPECT_PLANKS = BLOCKS.register("doom_aspect_planks", () -> new FlammableBlock(5, 20, aspectPlanks));
	public static final RegistryObject<Block> HEART_ASPECT_PLANKS = BLOCKS.register("heart_aspect_planks", () -> new FlammableBlock(5, 20, aspectPlanks));
	public static final RegistryObject<Block> HOPE_ASPECT_PLANKS = BLOCKS.register("hope_aspect_planks", () -> new FlammableBlock(5, 20, aspectPlanks));
	public static final RegistryObject<Block> LIFE_ASPECT_PLANKS = BLOCKS.register("life_aspect_planks", () -> new FlammableBlock(5, 20, aspectPlanks));
	public static final RegistryObject<Block> LIGHT_ASPECT_PLANKS = BLOCKS.register("light_aspect_planks", () -> new FlammableBlock(5, 20, aspectPlanks));
	public static final RegistryObject<Block> MIND_ASPECT_PLANKS = BLOCKS.register("mind_aspect_planks", () -> new FlammableBlock(5, 20, aspectPlanks));
	public static final RegistryObject<Block> RAGE_ASPECT_PLANKS = BLOCKS.register("rage_aspect_planks", () -> new FlammableBlock(5, 20, aspectPlanks));
	public static final RegistryObject<Block> SPACE_ASPECT_PLANKS = BLOCKS.register("space_aspect_planks", () -> new FlammableBlock(5, 20, aspectPlanks));
	public static final RegistryObject<Block> TIME_ASPECT_PLANKS = BLOCKS.register("time_aspect_planks", () -> new FlammableBlock(5, 20, aspectPlanks));
	public static final RegistryObject<Block> VOID_ASPECT_PLANKS = BLOCKS.register("void_aspect_planks", () -> new FlammableBlock(5, 20, aspectPlanks));
	
	private static final BlockBehaviour.Properties aspectLeaves = Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion();
	public static final RegistryObject<Block> BLOOD_ASPECT_LEAVES = BLOCKS.register("blood_aspect_leaves", () -> new FlammableLeavesBlock(aspectLeaves));
	public static final RegistryObject<Block> BREATH_ASPECT_LEAVES = BLOCKS.register("breath_aspect_leaves", () -> new FlammableLeavesBlock(aspectLeaves));
	public static final RegistryObject<Block> DOOM_ASPECT_LEAVES = BLOCKS.register("doom_aspect_leaves", () -> new FlammableLeavesBlock(aspectLeaves));
	public static final RegistryObject<Block> HEART_ASPECT_LEAVES = BLOCKS.register("heart_aspect_leaves", () -> new FlammableLeavesBlock(aspectLeaves));
	public static final RegistryObject<Block> HOPE_ASPECT_LEAVES = BLOCKS.register("hope_aspect_leaves", () -> new FlammableLeavesBlock(aspectLeaves));
	public static final RegistryObject<Block> LIFE_ASPECT_LEAVES = BLOCKS.register("life_aspect_leaves", () -> new FlammableLeavesBlock(aspectLeaves));
	public static final RegistryObject<Block> LIGHT_ASPECT_LEAVES = BLOCKS.register("light_aspect_leaves", () -> new FlammableLeavesBlock(aspectLeaves));
	public static final RegistryObject<Block> MIND_ASPECT_LEAVES = BLOCKS.register("mind_aspect_leaves", () -> new FlammableLeavesBlock(aspectLeaves));
	public static final RegistryObject<Block> RAGE_ASPECT_LEAVES = BLOCKS.register("rage_aspect_leaves", () -> new FlammableLeavesBlock(aspectLeaves));
	public static final RegistryObject<Block> SPACE_ASPECT_LEAVES = BLOCKS.register("space_aspect_leaves", () -> new FlammableLeavesBlock(aspectLeaves));
	public static final RegistryObject<Block> TIME_ASPECT_LEAVES = BLOCKS.register("time_aspect_leaves", () -> new FlammableLeavesBlock(aspectLeaves));
	public static final RegistryObject<Block> VOID_ASPECT_LEAVES = BLOCKS.register("void_aspect_leaves", () -> new FlammableLeavesBlock(aspectLeaves));
	
	public static final RegistryObject<Block> BLOOD_ASPECT_SAPLING = BLOCKS.register("blood_aspect_sapling", () -> new AspectSaplingBlock(sapling));
	public static final RegistryObject<Block> BREATH_ASPECT_SAPLING = BLOCKS.register("breath_aspect_sapling", () -> new AspectSaplingBlock(sapling));
	public static final RegistryObject<Block> DOOM_ASPECT_SAPLING = BLOCKS.register("doom_aspect_sapling", () -> new AspectSaplingBlock(sapling));
	public static final RegistryObject<Block> HEART_ASPECT_SAPLING = BLOCKS.register("heart_aspect_sapling", () -> new AspectSaplingBlock(sapling));
	public static final RegistryObject<Block> HOPE_ASPECT_SAPLING = BLOCKS.register("hope_aspect_sapling", () -> new AspectSaplingBlock(sapling));
	public static final RegistryObject<Block> LIFE_ASPECT_SAPLING = BLOCKS.register("life_aspect_sapling", () -> new AspectSaplingBlock(sapling));
	public static final RegistryObject<Block> LIGHT_ASPECT_SAPLING = BLOCKS.register("light_aspect_sapling", () -> new AspectSaplingBlock(sapling));
	public static final RegistryObject<Block> MIND_ASPECT_SAPLING = BLOCKS.register("mind_aspect_sapling", () -> new AspectSaplingBlock(sapling));
	public static final RegistryObject<Block> RAGE_ASPECT_SAPLING = BLOCKS.register("rage_aspect_sapling", () -> new AspectSaplingBlock(sapling));
	public static final RegistryObject<Block> SPACE_ASPECT_SAPLING = BLOCKS.register("space_aspect_sapling", () -> new AspectSaplingBlock(sapling));
	public static final RegistryObject<Block> TIME_ASPECT_SAPLING = BLOCKS.register("time_aspect_sapling", () -> new AspectSaplingBlock(sapling));
	public static final RegistryObject<Block> VOID_ASPECT_SAPLING = BLOCKS.register("void_aspect_sapling", () -> new AspectSaplingBlock(sapling));
	
	
	
	//Land Plant Blocks
	public static final RegistryObject<Block> GLOWING_MUSHROOM = BLOCKS.register("glowing_mushroom", () -> new GlowingMushroomBlock(Block.Properties.of(Material.PLANT, MaterialColor.DIAMOND).noCollission().randomTicks().strength(0).sound(SoundType.GRASS).lightLevel(state -> 11)));
	public static final RegistryObject<Block> DESERT_BUSH = BLOCKS.register("desert_bush", () -> new DesertFloraBlock(Block.Properties.of(Material.PLANT).noCollission().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<Block> BLOOMING_CACTUS = BLOCKS.register("blooming_cactus", () -> new DesertFloraBlock(Block.Properties.of(Material.PLANT).noCollission().strength(0).sound(SoundType.GRASS)));
	public static final RegistryObject<Block> PETRIFIED_GRASS = BLOCKS.register("petrified_grass", () -> new PetrifiedFloraBlock(Block.Properties.of(Material.STONE, DyeColor.GRAY).requiresCorrectToolForDrops().noCollission().strength(0).sound(SoundType.STONE)));
	public static final RegistryObject<Block> PETRIFIED_POPPY = BLOCKS.register("petrified_poppy", () -> new PetrifiedFloraBlock(Block.Properties.of(Material.STONE, DyeColor.GRAY).requiresCorrectToolForDrops().noCollission().strength(0).sound(SoundType.STONE)));
	
	public static final RegistryObject<StemGrownBlock> STRAWBERRY = BLOCKS.register("strawberry", () -> new StrawberryBlock(Block.Properties.of(Material.VEGETABLE, MaterialColor.COLOR_RED).strength(1.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<AttachedStemBlock> ATTACHED_STRAWBERRY_STEM = BLOCKS.register("attached_strawberry_stem", () -> new AttachedStemBlock(STRAWBERRY.get(), MSItems.STRAWBERRY_CHUNK, Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.WOOD)));
	public static final RegistryObject<StemBlock> STRAWBERRY_STEM = BLOCKS.register("strawberry_stem", () -> new StemBlock(STRAWBERRY.get(), MSItems.STRAWBERRY_CHUNK, Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.WOOD)));
	
	public static final RegistryObject<Block> TALL_END_GRASS = BLOCKS.register("tall_end_grass", () -> new TallEndGrassBlock(Block.Properties.of(Material.REPLACEABLE_PLANT, DyeColor.GREEN).noCollission().randomTicks().strength(0.1F).sound(SoundType.NETHER_WART)));
	public static final RegistryObject<Block> GLOWFLOWER = BLOCKS.register("glowflower", () -> new FlowerBlock(MobEffects.GLOWING, 20, Block.Properties.of(Material.PLANT, DyeColor.YELLOW).noCollission().strength(0).lightLevel(state -> 12).sound(SoundType.GRASS)));
	
	
	
	//Special Land Blocks
	public static final RegistryObject<Block> GLOWY_GOOP = BLOCKS.register("glowy_goop", () -> new SlimeBlock(Block.Properties.of(Material.CLAY).strength(0.1F).sound(SoundType.SLIME_BLOCK).lightLevel(state -> 14)));
	public static final RegistryObject<Block> COAGULATED_BLOOD = BLOCKS.register("coagulated_blood", () -> new SlimeBlock(Block.Properties.of(Material.CLAY).strength(0.1F).sound(SoundType.SLIME_BLOCK)));
	public static final RegistryObject<Block> PIPE = BLOCKS.register("pipe", () -> new DirectionalCustomShapeBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(4.0F).sound(SoundType.METAL), MSBlockShapes.PIPE));
	public static final RegistryObject<Block> PIPE_INTERSECTION = BLOCKS.register("pipe_intersection", () -> new Block(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(4.0F).sound(SoundType.METAL))); //the intention is that later down the line, someone will improve the code of pipe blocks to allow for intersections or a separate intersection blockset will be made that actually work
	public static final RegistryObject<Block> PARCEL_PYXIS = BLOCKS.register("parcel_pyxis", () -> new CustomShapeBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(4.0F), MSBlockShapes.PARCEL_PYXIS));
	public static final RegistryObject<Block> PYXIS_LID = BLOCKS.register("pyxis_lid", () -> new CustomShapeBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(1.0F), MSBlockShapes.PYXIS_LID));
	public static final RegistryObject<Block> STONE_SLAB = BLOCKS.register("stone_slab", () -> new StoneTabletBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(0.3F))); //TODO consider renaming, same thing as stone tablet
	public static final RegistryObject<Block> NAKAGATOR_STATUE = BLOCKS.register("nakagator_statue", () -> new CustomShapeBlock(Block.Properties.of(Material.STONE).strength(0.5F), MSBlockShapes.NAKAGATOR_STATUE));
	
	
	
	//Structure Land Blocks
	public static final RegistryObject<Block> BLACK_CHESS_BRICK_STAIRS = BLOCKS.register("black_chess_brick_stairs", () -> new StairBlock(() -> MSBlocks.BLACK_CHESS_BRICKS.get().defaultBlockState(), blackChessBricks));
	public static final RegistryObject<Block> DARK_GRAY_CHESS_BRICK_STAIRS = BLOCKS.register("dark_gray_chess_brick_stairs", () -> new StairBlock(() -> MSBlocks.DARK_GRAY_CHESS_BRICKS.get().defaultBlockState(), darkGrayChessBricks));
	public static final RegistryObject<Block> LIGHT_GRAY_CHESS_BRICK_STAIRS = BLOCKS.register("light_gray_chess_brick_stairs", () -> new StairBlock(() -> MSBlocks.LIGHT_GRAY_CHESS_BRICKS.get().defaultBlockState(), lightGrayChessBricks));
	public static final RegistryObject<Block> WHITE_CHESS_BRICK_STAIRS = BLOCKS.register("white_chess_brick_stairs", () -> new StairBlock(() -> MSBlocks.WHITE_CHESS_BRICKS.get().defaultBlockState(), whiteChessBricks));
	public static final RegistryObject<Block> COARSE_STONE_STAIRS = BLOCKS.register("coarse_stone_stairs", () -> new StairBlock(() -> MSBlocks.COARSE_STONE.get().defaultBlockState(), coarseStone));
	public static final RegistryObject<Block> COARSE_STONE_BRICK_STAIRS = BLOCKS.register("coarse_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.COARSE_STONE_BRICKS.get().defaultBlockState(), coarseStone));
	public static final RegistryObject<Block> SHADE_STAIRS = BLOCKS.register("shade_stairs", () -> new StairBlock(() -> MSBlocks.SHADE_STONE.get().defaultBlockState(), shadeStone));
	public static final RegistryObject<Block> SHADE_BRICK_STAIRS = BLOCKS.register("shade_brick_stairs", () -> new StairBlock(() -> MSBlocks.SHADE_BRICKS.get().defaultBlockState(), shadeStone));
	public static final RegistryObject<Block> FROST_TILE_STAIRS = BLOCKS.register("frost_tile_stairs", () -> new StairBlock(() -> MSBlocks.FROST_TILE.get().defaultBlockState(), frost));
	public static final RegistryObject<Block> FROST_BRICK_STAIRS = BLOCKS.register("frost_brick_stairs", () -> new StairBlock(() -> MSBlocks.FROST_BRICKS.get().defaultBlockState(), frost));
	public static final RegistryObject<Block> CAST_IRON_STAIRS = BLOCKS.register("cast_iron_stairs", () -> new StairBlock(() -> MSBlocks.CAST_IRON.get().defaultBlockState(), metal));
	public static final RegistryObject<Block> BLACK_STONE_STAIRS = BLOCKS.register("black_stone_stairs", () -> new StairBlock(() -> MSBlocks.BLACK_STONE.get().defaultBlockState(), blackStone));
	public static final RegistryObject<Block> BLACK_STONE_BRICK_STAIRS = BLOCKS.register("black_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.BLACK_STONE_BRICKS.get().defaultBlockState(), blackStone));
	public static final RegistryObject<Block> FLOWERY_MOSSY_STONE_BRICK_STAIRS = BLOCKS.register("flowery_mossy_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.FLOWERY_MOSSY_STONE_BRICKS.get().defaultBlockState(), floweryOrDecrepitStone));
	public static final RegistryObject<Block> MYCELIUM_STAIRS = BLOCKS.register("mycelium_stairs", () -> new StairBlock(() -> MSBlocks.MYCELIUM_STONE.get().defaultBlockState(), myceliumStone));
	public static final RegistryObject<Block> MYCELIUM_BRICK_STAIRS = BLOCKS.register("mycelium_brick_stairs", () -> new StairBlock(() -> MSBlocks.MYCELIUM_BRICKS.get().defaultBlockState(), myceliumStone));
	public static final RegistryObject<Block> CHALK_STAIRS = BLOCKS.register("chalk_stairs", () -> new StairBlock(() -> MSBlocks.CHALK.get().defaultBlockState(), chalk));
	public static final RegistryObject<Block> CHALK_BRICK_STAIRS = BLOCKS.register("chalk_brick_stairs", () -> new StairBlock(() -> MSBlocks.CHALK_BRICKS.get().defaultBlockState(), chalk));
	public static final RegistryObject<Block> PINK_STONE_STAIRS = BLOCKS.register("pink_stone_stairs", () -> new StairBlock(() -> MSBlocks.PINK_STONE.get().defaultBlockState(), pinkStone));
	public static final RegistryObject<Block> PINK_STONE_BRICK_STAIRS = BLOCKS.register("pink_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.PINK_STONE_BRICKS.get().defaultBlockState(), pinkStone));
	public static final RegistryObject<Block> BROWN_STONE_STAIRS = BLOCKS.register("brown_stone_stairs", () -> new StairBlock(() -> MSBlocks.BROWN_STONE.get().defaultBlockState(), brownStone));
	public static final RegistryObject<Block> BROWN_STONE_BRICK_STAIRS = BLOCKS.register("brown_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.BROWN_STONE_BRICKS.get().defaultBlockState(), brownStone));
	public static final RegistryObject<Block> GREEN_STONE_STAIRS = BLOCKS.register("green_stone_stairs", () -> new StairBlock(() -> MSBlocks.GREEN_STONE.get().defaultBlockState(), greenStone));
	public static final RegistryObject<Block> GREEN_STONE_BRICK_STAIRS = BLOCKS.register("green_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.GREEN_STONE_BRICKS.get().defaultBlockState(), greenStone));
	public static final RegistryObject<Block> RAINBOW_PLANKS_STAIRS = BLOCKS.register("rainbow_planks_stairs", () -> new StairBlock(() -> MSBlocks.RAINBOW_PLANKS.get().defaultBlockState(), rainbowPlanks));
	public static final RegistryObject<Block> END_PLANKS_STAIRS = BLOCKS.register("end_planks_stairs", () -> new StairBlock(() -> MSBlocks.END_PLANKS.get().defaultBlockState(), endPlanks));
	public static final RegistryObject<Block> DEAD_PLANKS_STAIRS = BLOCKS.register("dead_planks_stairs", () -> new StairBlock(() -> MSBlocks.DEAD_PLANKS.get().defaultBlockState(), deadPlanks));
	public static final RegistryObject<Block> TREATED_PLANKS_STAIRS = BLOCKS.register("treated_planks_stairs", () -> new StairBlock(() -> MSBlocks.TREATED_PLANKS.get().defaultBlockState(), treatedPlanks));
	
	public static final RegistryObject<Block> STEEP_GREEN_STONE_BRICK_STAIRS_BASE = BLOCKS.register("steep_green_stone_brick_stairs_base", () -> new CustomShapeBlock(greenStone, MSBlockShapes.STEEP_STAIRS_BASE));
	public static final RegistryObject<Block> STEEP_GREEN_STONE_BRICK_STAIRS_TOP = BLOCKS.register("steep_green_stone_brick_stairs_top", () -> new CustomShapeBlock(greenStone, MSBlockShapes.STEEP_STAIRS_TOP));
	
	public static final RegistryObject<Block> BLACK_CHESS_BRICK_SLAB = BLOCKS.register("black_chess_brick_slab", () -> new SlabBlock(blackChessBricks));
	public static final RegistryObject<Block> DARK_GRAY_CHESS_BRICK_SLAB = BLOCKS.register("dark_gray_chess_brick_slab", () -> new SlabBlock(darkGrayChessBricks));
	public static final RegistryObject<Block> LIGHT_GRAY_CHESS_BRICK_SLAB = BLOCKS.register("light_gray_chess_brick_slab", () -> new SlabBlock(lightGrayChessBricks));
	public static final RegistryObject<Block> WHITE_CHESS_BRICK_SLAB = BLOCKS.register("white_chess_brick_slab", () -> new SlabBlock(whiteChessBricks));
	public static final RegistryObject<Block> FLOWERY_MOSSY_STONE_BRICK_SLAB = BLOCKS.register("flowery_mossy_stone_brick_slab", () -> new SlabBlock(floweryOrDecrepitStone));
	public static final RegistryObject<Block> COARSE_STONE_SLAB = BLOCKS.register("coarse_stone_slab", () -> new SlabBlock(coarseStone));
	public static final RegistryObject<Block> COARSE_STONE_BRICK_SLAB = BLOCKS.register("coarse_stone_brick_slab", () -> new SlabBlock(coarseStone));
	public static final RegistryObject<Block> SHADE_SLAB = BLOCKS.register("shade_slab", () -> new SlabBlock(shadeStone));
	public static final RegistryObject<Block> SHADE_BRICK_SLAB = BLOCKS.register("shade_brick_slab", () -> new SlabBlock(shadeStone));
	public static final RegistryObject<Block> FROST_TILE_SLAB = BLOCKS.register("frost_tile_slab", () -> new SlabBlock(frost));
	public static final RegistryObject<Block> FROST_BRICK_SLAB = BLOCKS.register("frost_brick_slab", () -> new SlabBlock(frost));
	public static final RegistryObject<Block> BLACK_STONE_SLAB = BLOCKS.register("black_stone_slab", () -> new SlabBlock(blackStone));
	public static final RegistryObject<Block> BLACK_STONE_BRICK_SLAB = BLOCKS.register("black_stone_brick_slab", () -> new SlabBlock(blackStone));
	public static final RegistryObject<Block> MYCELIUM_SLAB = BLOCKS.register("mycelium_slab", () -> new SlabBlock(myceliumStone));
	public static final RegistryObject<Block> MYCELIUM_BRICK_SLAB = BLOCKS.register("mycelium_brick_slab", () -> new SlabBlock(myceliumStone));
	public static final RegistryObject<Block> CHALK_SLAB = BLOCKS.register("chalk_slab", () -> new SlabBlock(chalk));
	public static final RegistryObject<Block> CHALK_BRICK_SLAB = BLOCKS.register("chalk_brick_slab", () -> new SlabBlock(chalk));
	public static final RegistryObject<Block> PINK_STONE_SLAB = BLOCKS.register("pink_stone_slab", () -> new SlabBlock(pinkStone));
	public static final RegistryObject<Block> PINK_STONE_BRICK_SLAB = BLOCKS.register("pink_stone_brick_slab", () -> new SlabBlock(pinkStone));
	public static final RegistryObject<Block> BROWN_STONE_SLAB = BLOCKS.register("brown_stone_slab", () -> new SlabBlock(brownStone));
	public static final RegistryObject<Block> BROWN_STONE_BRICK_SLAB = BLOCKS.register("brown_stone_brick_slab", () -> new SlabBlock(brownStone));
	public static final RegistryObject<Block> GREEN_STONE_SLAB = BLOCKS.register("green_stone_slab", () -> new SlabBlock(greenStone));
	public static final RegistryObject<Block> GREEN_STONE_BRICK_SLAB = BLOCKS.register("green_stone_brick_slab", () -> new SlabBlock(greenStone));
	public static final RegistryObject<Block> RAINBOW_PLANKS_SLAB = BLOCKS.register("rainbow_planks_slab", () -> new SlabBlock(rainbowPlanks));
	public static final RegistryObject<Block> END_PLANKS_SLAB = BLOCKS.register("end_planks_slab", () -> new SlabBlock(endPlanks));
	public static final RegistryObject<Block> DEAD_PLANKS_SLAB = BLOCKS.register("dead_planks_slab", () -> new SlabBlock(deadPlanks));
	public static final RegistryObject<Block> TREATED_PLANKS_SLAB = BLOCKS.register("treated_planks_slab", () -> new SlabBlock(treatedPlanks));
	
	
	
	//Dungeon Functional Blocks
	private static final BlockBehaviour.Properties metalMechanicalBlock = Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL); //also used by some machines in addition to puzzle blocks
	private static final BlockBehaviour.Properties durableMetalMechanicalBlock = Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(6).sound(SoundType.METAL);
	public static final RegistryObject<Block> TRAJECTORY_BLOCK = BLOCKS.register("trajectory_block", () -> new TrajectoryBlock(metalMechanicalBlock));
	public static final RegistryObject<Block> STAT_STORER = BLOCKS.register("stat_storer", () -> new StatStorerBlock(metalMechanicalBlock));
	public static final RegistryObject<Block> REMOTE_OBSERVER = BLOCKS.register("remote_observer", () -> new RemoteObserverBlock(metalMechanicalBlock));
	public static final RegistryObject<Block> WIRELESS_REDSTONE_TRANSMITTER = BLOCKS.register("wireless_redstone_transmitter", () -> new WirelessRedstoneTransmitterBlock(metalMechanicalBlock));
	public static final RegistryObject<Block> WIRELESS_REDSTONE_RECEIVER = BLOCKS.register("wireless_redstone_receiver", () -> new WirelessRedstoneReceiverBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL).randomTicks()));
	public static final RegistryObject<Block> SOLID_SWITCH = BLOCKS.register("solid_switch", () -> new SolidSwitchBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL).lightLevel(state -> state.getValue(SolidSwitchBlock.POWERED) ? 15 : 0)));
	public static final RegistryObject<Block> VARIABLE_SOLID_SWITCH = BLOCKS.register("variable_solid_switch", () -> new VariableSolidSwitchBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL).lightLevel(state -> state.getValue(VariableSolidSwitchBlock.POWER))));
	public static final RegistryObject<Block> ONE_SECOND_INTERVAL_TIMED_SOLID_SWITCH = BLOCKS.register("one_second_interval_timed_solid_switch", () -> new TimedSolidSwitchBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL).lightLevel(state -> state.getValue(TimedSolidSwitchBlock.POWER)), 20)); //TODO java.lang.IllegalArgumentException: Cannot get property IntegerProperty{name=power, clazz=class java.lang.Integer, values=[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]} as it does not exist in Block{null}
	public static final RegistryObject<Block> TWO_SECOND_INTERVAL_TIMED_SOLID_SWITCH = BLOCKS.register("two_second_interval_timed_solid_switch", () -> new TimedSolidSwitchBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL).lightLevel(state -> state.getValue(TimedSolidSwitchBlock.POWER)), 40));
	public static final RegistryObject<Block> SUMMONER = BLOCKS.register("summoner", () -> new SummonerBlock(metalMechanicalBlock));
	public static final RegistryObject<Block> AREA_EFFECT_BLOCK = BLOCKS.register("area_effect_block", () -> new AreaEffectBlock(durableMetalMechanicalBlock));
	public static final RegistryObject<Block> PLATFORM_GENERATOR = BLOCKS.register("platform_generator", () -> new PlatformGeneratorBlock(durableMetalMechanicalBlock));
	public static final RegistryObject<Block> PLATFORM_BLOCK = BLOCKS.register("platform_block", () -> new PlatformBlock(Block.Properties.of(Material.BARRIER).strength(0.2F).sound(SoundType.SCAFFOLDING).lightLevel(state -> 6).randomTicks().noOcclusion().isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never)));
	public static final RegistryObject<Block> PLATFORM_RECEPTACLE = BLOCKS.register("platform_receptacle", () -> new PlatformReceptacleBlock(metalMechanicalBlock));
	public static final RegistryObject<Block> ITEM_MAGNET = BLOCKS.register("item_magnet", () -> new ItemMagnetBlock(metalMechanicalBlock, new CustomVoxelShape(new double[]{0, 0, 0, 16, 1, 16}, new double[]{1, 1, 1, 15, 15, 15}, new double[]{0, 15, 0, 16, 16, 16})));
	public static final RegistryObject<Block> REDSTONE_CLOCK = BLOCKS.register("redstone_clock", () -> new RedstoneClockBlock(metalMechanicalBlock));
	public static final RegistryObject<Block> ROTATOR = BLOCKS.register("rotator", () -> new RotatorBlock(metalMechanicalBlock));
	public static final RegistryObject<Block> TOGGLER = BLOCKS.register("toggler", () -> new TogglerBlock(metalMechanicalBlock));
	public static final RegistryObject<Block> REMOTE_COMPARATOR = BLOCKS.register("remote_comparator", () -> new RemoteComparatorBlock(metalMechanicalBlock));
	public static final RegistryObject<Block> STRUCTURE_CORE = BLOCKS.register("structure_core", () -> new StructureCoreBlock(durableMetalMechanicalBlock));
	public static final RegistryObject<Block> FALL_PAD = BLOCKS.register("fall_pad", () -> new FallPadBlock(Block.Properties.of(Material.CLOTH_DECORATION).requiresCorrectToolForDrops().strength(1).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> FRAGILE_STONE = BLOCKS.register("fragile_stone", () -> new FragileBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1).sound(SoundType.STONE)));
	public static final RegistryObject<Block> SPIKES = BLOCKS.register("spikes", () -> new SpikeBlock(Block.Properties.of(Material.METAL, MaterialColor.METAL).requiresCorrectToolForDrops().strength(2).sound(SoundType.METAL), MSBlockShapes.SPIKES)); //TODO check for: Registry Object not present: minestuck:spikes
	public static final RegistryObject<Block> RETRACTABLE_SPIKES = BLOCKS.register("retractable_spikes", () -> new RetractableSpikesBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(1).sound(SoundType.METAL)));
	public static final RegistryObject<Block> BLOCK_PRESSURE_PLATE = BLOCKS.register("block_pressure_plate", () -> new BlockPressurePlateBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1).sound(SoundType.STONE)));
	public static final RegistryObject<Block> PUSHABLE_BLOCK = BLOCKS.register("pushable_block", () -> new PushableBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1).sound(SoundType.GILDED_BLACKSTONE)));
	
	private static final BlockBehaviour.Properties logicGate = Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1).sound(SoundType.STONE); //TODO potentially change material/sound to metal considering textures
	public static final RegistryObject<Block> AND_GATE_BLOCK = BLOCKS.register("and_gate_block", () -> new LogicGateBlock(logicGate, LogicGateBlock.State.AND));
	public static final RegistryObject<Block> OR_GATE_BLOCK = BLOCKS.register("or_gate_block", () -> new LogicGateBlock(logicGate, LogicGateBlock.State.OR));
	public static final RegistryObject<Block> XOR_GATE_BLOCK = BLOCKS.register("xor_gate_block", () -> new LogicGateBlock(logicGate, LogicGateBlock.State.XOR));
	public static final RegistryObject<Block> NAND_GATE_BLOCK = BLOCKS.register("nand_gate_block", () -> new LogicGateBlock(logicGate, LogicGateBlock.State.NAND));
	public static final RegistryObject<Block> NOR_GATE_BLOCK = BLOCKS.register("nor_gate_block", () -> new LogicGateBlock(logicGate, LogicGateBlock.State.NOR));
	public static final RegistryObject<Block> XNOR_GATE_BLOCK = BLOCKS.register("xnor_gate_block", () -> new LogicGateBlock(logicGate, LogicGateBlock.State.XNOR));
	
	
	
	//Core Functional Land Blocks
	public static final RegistryObject<Block> GATE = BLOCKS.register("gate", () -> new GateBlock(Block.Properties.of(Material.PORTAL).noCollission().strength(-1.0F, 25.0F).sound(SoundType.GLASS).lightLevel(state -> 11).noDrops()));
	public static final RegistryObject<Block> RETURN_NODE = BLOCKS.register("return_node", () -> new ReturnNodeBlock(Block.Properties.of(Material.PORTAL).noCollission().strength(-1.0F, 10.0F).sound(SoundType.GLASS).lightLevel(state -> 11).noDrops()));
	
	
	
	//Misc Functional Land Blocks
	
	
	
	//Sburb Machines
	public static final RegistryObject<Block> CRUXTRUDER_LID = BLOCKS.register("cruxtruder_lid", () -> new CruxtruderLidBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(1.0F)));
	public static final CruxtruderMultiblock CRUXTRUDER = new CruxtruderMultiblock(Minestuck.MOD_ID);
	public static final TotemLatheMultiblock TOTEM_LATHE = new TotemLatheMultiblock(Minestuck.MOD_ID);
	public static final AlchemiterMultiblock ALCHEMITER = new AlchemiterMultiblock(Minestuck.MOD_ID);
	public static final PunchDesignixMultiblock PUNCH_DESIGNIX = new PunchDesignixMultiblock(Minestuck.MOD_ID);
	
	public static final RegistryObject<Block> MINI_CRUXTRUDER = BLOCKS.register("mini_cruxtruder", () -> new SmallMachineBlock<>(MSBlockShapes.SMALL_CRUXTRUDER.createRotatedShapes(), MSTileEntityTypes.MINI_CRUXTRUDER, Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F)));
	public static final RegistryObject<Block> MINI_TOTEM_LATHE = BLOCKS.register("mini_totem_lathe", () -> new SmallMachineBlock<>(MSBlockShapes.SMALL_TOTEM_LATHE.createRotatedShapes(), MSTileEntityTypes.MINI_TOTEM_LATHE, Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F)));
	public static final RegistryObject<Block> MINI_ALCHEMITER = BLOCKS.register("mini_alchemiter", () -> new MiniAlchemiterBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F)));
	public static final RegistryObject<Block> MINI_PUNCH_DESIGNIX = BLOCKS.register("mini_punch_designix", () -> new SmallMachineBlock<>(MSBlockShapes.SMALL_PUNCH_DESIGNIX.createRotatedShapes(), MSTileEntityTypes.MINI_PUNCH_DESIGNIX, Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F)));
	
	public static final RegistryObject<Block> HOLOPAD = BLOCKS.register("holopad", () -> new HolopadBlock(Block.Properties.of(Material.METAL, MaterialColor.SNOW).requiresCorrectToolForDrops().strength(3.0F)));
	
	
	
	//Misc Machines
	public static final RegistryObject<Block> COMPUTER = BLOCKS.register("computer", () -> new ComputerBlock(ComputerBlock.COMPUTER_SHAPE, ComputerBlock.COMPUTER_SHAPE, Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(4.0F)));
	public static final RegistryObject<Block> LAPTOP = BLOCKS.register("laptop", () -> new ComputerBlock(ComputerBlock.LAPTOP_OPEN_SHAPE, ComputerBlock.LAPTOP_CLOSED_SHAPE, Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(4.0F)));
	public static final RegistryObject<Block> CROCKERTOP = BLOCKS.register("crockertop", () -> new ComputerBlock(ComputerBlock.LAPTOP_OPEN_SHAPE, ComputerBlock.LAPTOP_CLOSED_SHAPE, Block.Properties.of(Material.METAL, MaterialColor.COLOR_RED).requiresCorrectToolForDrops().strength(4.0F)));
	public static final RegistryObject<Block> HUBTOP = BLOCKS.register("hubtop", () -> new ComputerBlock(ComputerBlock.LAPTOP_OPEN_SHAPE, ComputerBlock.LAPTOP_CLOSED_SHAPE, Block.Properties.of(Material.METAL, MaterialColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(4.0F)));
	public static final RegistryObject<Block> LUNCHTOP = BLOCKS.register("lunchtop", () -> new ComputerBlock(ComputerBlock.LUNCHTOP_OPEN_SHAPE, ComputerBlock.LUNCHTOP_CLOSED_SHAPE, Block.Properties.of(Material.METAL, MaterialColor.COLOR_RED).requiresCorrectToolForDrops().strength(4.0F)));
	public static final RegistryObject<Block> OLD_COMPUTER = BLOCKS.register("old_computer", () -> new ComputerBlock(ComputerBlock.OLD_COMPUTER_SHAPE, ComputerBlock.OLD_COMPUTER_SHAPE, Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(4.0F)));
	public static final RegistryObject<Block> TRANSPORTALIZER = BLOCKS.register("transportalizer", () -> new TransportalizerBlock(metalMechanicalBlock));
	public static final RegistryObject<Block> TRANS_PORTALIZER = BLOCKS.register("trans_portalizer", () -> new TransportalizerBlock(metalMechanicalBlock));
	public static final RegistryObject<Block> SENDIFICATOR = BLOCKS.register("sendificator", () -> new SendificatorBlock(metalMechanicalBlock));
	public static final RegistryObject<Block> GRIST_WIDGET = BLOCKS.register("grist_widget", () -> new GristWidgetBlock(metalMechanicalBlock));
	public static final RegistryObject<Block> URANIUM_COOKER = BLOCKS.register("uranium_cooker", () -> new SmallMachineBlock<>(new CustomVoxelShape(new double[]{4, 0, 4, 12, 6, 12}).createRotatedShapes(), MSTileEntityTypes.URANIUM_COOKER, Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F)));
	
	
	
	//Misc Core Objects
	public static final RegistryObject<Block> CRUXITE_DOWEL = BLOCKS.register("cruxite_dowel", () -> new CruxiteDowelBlock(Block.Properties.of(Material.GLASS).strength(0.0F)));
	public static final LotusTimeCapsuleMultiblock LOTUS_TIME_CAPSULE_BLOCK = new LotusTimeCapsuleMultiblock(Minestuck.MOD_ID);
	
	
	
	//Misc Alchemy Semi-Plants
	public static final RegistryObject<Block> GOLD_SEEDS = BLOCKS.register("gold_seeds", () -> new GoldSeedsBlock(Block.Properties.of(Material.PLANT).strength(0.1F).sound(SoundType.METAL).noCollission()));
	public static final RegistryObject<Block> WOODEN_CACTUS = BLOCKS.register("wooden_cactus", () -> new SpecialCactusBlock(Block.Properties.of(Material.WOOD).randomTicks().strength(1.0F, 2.5F).sound(SoundType.WOOD)));
	
	
	
	//Cakes
	private static final BlockBehaviour.Properties cake = Block.Properties.of(Material.CAKE).strength(0.5F).sound(SoundType.WOOL);
	public static final RegistryObject<Block> APPLE_CAKE = BLOCKS.register("apple_cake", () -> new SimpleCakeBlock(cake, 2, 0.5F, null));
	public static final RegistryObject<Block> BLUE_CAKE = BLOCKS.register("blue_cake", () -> new SimpleCakeBlock(cake, 2, 0.3F, player -> player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 150, 0))));
	public static final RegistryObject<Block> COLD_CAKE = BLOCKS.register("cold_cake", () -> new SimpleCakeBlock(cake, 2, 0.3F, player -> {
		player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 1));
		player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1));
	}));
	public static final RegistryObject<Block> RED_CAKE = BLOCKS.register("red_cake", () -> new SimpleCakeBlock(cake, 2, 0.1F, player -> player.heal(1)));
	public static final RegistryObject<Block> HOT_CAKE = BLOCKS.register("hot_cake", () -> new SimpleCakeBlock(cake, 2, 0.1F, player -> player.setSecondsOnFire(4)));
	public static final RegistryObject<Block> REVERSE_CAKE = BLOCKS.register("reverse_cake", () -> new SimpleCakeBlock(cake, 2, 0.1F, null));
	public static final RegistryObject<Block> FUCHSIA_CAKE = BLOCKS.register("fuchsia_cake", () -> new SimpleCakeBlock(cake, 3, 0.5F, player -> {
		player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 350, 1));
		player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0));
	}));
	public static final RegistryObject<Block> NEGATIVE_CAKE = BLOCKS.register("negative_cake", () -> new SimpleCakeBlock(cake, 2, 0.3F, player -> {
		player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 300, 0));
		player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 250, 0));
	}));
	public static final RegistryObject<Block> CARROT_CAKE = BLOCKS.register("carrot_cake", () -> new SimpleCakeBlock(cake, 2, 0.3F, player -> player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 200, 0))));
	
	
	
	//Explosion and Redstone
	public static final RegistryObject<Block> PRIMED_TNT = BLOCKS.register("primed_tnt", () -> new SpecialTNTBlock(Block.Properties.of(Material.EXPLOSIVE).strength(0.0F).sound(SoundType.GRASS), true, false, false));
	public static final RegistryObject<Block> UNSTABLE_TNT = BLOCKS.register("unstable_tnt", () -> new SpecialTNTBlock(Block.Properties.of(Material.EXPLOSIVE).strength(0.0F).sound(SoundType.GRASS).randomTicks(), false, true, false));
	public static final RegistryObject<Block> INSTANT_TNT = BLOCKS.register("instant_tnt", () -> new SpecialTNTBlock(Block.Properties.of(Material.EXPLOSIVE).strength(0.0F).sound(SoundType.GRASS), false, false, true));
	public static final RegistryObject<Block> WOODEN_EXPLOSIVE_BUTTON = BLOCKS.register("wooden_explosive_button", () -> new SpecialButtonBlock(Block.Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.WOOD), true, true));
	public static final RegistryObject<Block> STONE_EXPLOSIVE_BUTTON = BLOCKS.register("stone_explosive_button", () -> new SpecialButtonBlock(Block.Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.STONE), true, false));
	
	
	
	//Misc Alchemy Objects
	static BlockBehaviour.Properties decor = Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(0.5F);
	public static final RegistryObject<Block> BLENDER = BLOCKS.register("blender", () -> new CustomShapeBlock(decor.sound(SoundType.METAL), MSBlockShapes.BLENDER));
	public static final RegistryObject<Block> CHESSBOARD = BLOCKS.register("chessboard", () -> new CustomShapeBlock(decor, MSBlockShapes.CHESSBOARD));
	public static final RegistryObject<Block> MINI_FROG_STATUE = BLOCKS.register("mini_frog_statue", () -> new CustomShapeBlock(decor, MSBlockShapes.FROG_STATUE));
	public static final RegistryObject<Block> MINI_WIZARD_STATUE = BLOCKS.register("mini_wizard_statue", () -> new CustomShapeBlock(decor, MSBlockShapes.WIZARD_STATUE));
	public static final RegistryObject<Block> MINI_TYPHEUS_STATUE = BLOCKS.register("mini_typheus_statue", () -> new CustomShapeBlock(decor, MSBlockShapes.DENIZEN_STATUE));
	public static final RegistryObject<CassettePlayerBlock> CASSETTE_PLAYER = BLOCKS.register("cassette_player", () -> new CassettePlayerBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(0.5F).sound(SoundType.METAL), MSBlockShapes.CASSETTE_PLAYER));
	public static final RegistryObject<Block> GLOWYSTONE_DUST = BLOCKS.register("glowystone_dust", () -> new GlowystoneWireBlock(Block.Properties.of(Material.DECORATION).strength(0.0F).lightLevel(state -> 16).noCollission()));
	
	
	
	//TODO no grist cost
	public static final RegistryObject<LiquidBlock> OIL = BLOCKS.register("oil", () -> new FlowingModFluidBlock(MSFluids.OIL, new Vec3(0.0, 0.0, 0.0), 0.75f, Block.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
	public static final RegistryObject<LiquidBlock> BLOOD = BLOCKS.register("blood", () -> new FlowingModFluidBlock(MSFluids.BLOOD, new Vec3(0.8, 0.0, 0.0), 0.25f, Block.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
	public static final RegistryObject<LiquidBlock> BRAIN_JUICE = BLOCKS.register("brain_juice", () -> new FlowingModFluidBlock(MSFluids.BRAIN_JUICE, new Vec3(0.55, 0.25, 0.7), 0.25f, Block.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
	public static final RegistryObject<LiquidBlock> WATER_COLORS = BLOCKS.register("water_colors", () -> new FlowingWaterColorsBlock(MSFluids.WATER_COLORS, 0.01f, Block.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
	public static final RegistryObject<LiquidBlock> ENDER = BLOCKS.register("ender", () -> new FlowingModFluidBlock(MSFluids.ENDER, new Vec3(0, 0.35, 0.35), (Float.MAX_VALUE), Block.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
	public static final RegistryObject<LiquidBlock> LIGHT_WATER = BLOCKS.register("light_water", () -> new FlowingModFluidBlock(MSFluids.LIGHT_WATER, new Vec3(0.2, 0.3, 1.0), 0.01f, Block.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
	
	
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		IForgeRegistry<Block> registry = event.getRegistry();
		CRUXTRUDER.registerBlocks(registry);
		TOTEM_LATHE.registerBlocks(registry);
		ALCHEMITER.registerBlocks(registry);
		PUNCH_DESIGNIX.registerBlocks(registry);
		
		LOTUS_TIME_CAPSULE_BLOCK.registerBlocks(registry);
	}
	
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