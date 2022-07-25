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

import javax.annotation.Nonnull;
import java.util.function.Function;

@ObjectHolder(Minestuck.MOD_ID)
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class MSBlocks
{
	public static DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, Minestuck.MOD_ID);
	
	//TODO make sure to re-add final after static to REGISTER and all of the below
	//TODO: rename all instances of "CASTLE_BRICKS" to "CHESS_BRICKS" (Black, DGrey, LGrey, White)
	//Skaia
	public static RegistryObject<Block> BLACK_CHESS_DIRT = REGISTER.register("black_chess_dirt", () -> new Block(Block.Properties.of(Material.DIRT, MaterialColor.COLOR_BLACK).strength(0.5F).sound(SoundType.GRAVEL)));
	public static RegistryObject<Block> WHITE_CHESS_DIRT = REGISTER.register("white_chess_dirt", () -> new Block(Block.Properties.of(Material.DIRT, MaterialColor.SNOW).strength(0.5F).sound(SoundType.GRAVEL)));
	public static RegistryObject<Block> DARK_GRAY_CHESS_DIRT = REGISTER.register("dark_gray_chess_dirt", () -> new Block(Block.Properties.of(Material.DIRT, MaterialColor.COLOR_GRAY).strength(0.5F).sound(SoundType.GRAVEL)));
	public static RegistryObject<Block> LIGHT_GRAY_CHESS_DIRT = REGISTER.register("light_gray_chess_dirt", () -> new Block(Block.Properties.of(Material.DIRT, MaterialColor.COLOR_LIGHT_GRAY).strength(0.5F).sound(SoundType.GRAVEL)));
	public static RegistryObject<Block> SKAIA_PORTAL = REGISTER.register("skaia_portal", () -> new SkaiaPortalBlock(Block.Properties.of(Material.PORTAL, MaterialColor.COLOR_CYAN).noCollission().lightLevel(state -> 11).strength(-1.0F, 3600000.0F).noDrops()));
	public static BlockBehaviour.Properties blackChessBricks = Block.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_BLACK).requiresCorrectToolForDrops().strength(1.5F, 6.0F);
	public static BlockBehaviour.Properties darkGrayChessBricks = Block.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_GRAY).requiresCorrectToolForDrops().strength(1.5F, 6.0F);
	public static BlockBehaviour.Properties lightGrayChessBricks = Block.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 6.0F);
	public static BlockBehaviour.Properties whiteChessBricks = Block.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_WHITE).requiresCorrectToolForDrops().strength(1.5F, 6.0F);
	public static RegistryObject<Block> BLACK_CASTLE_BRICKS = REGISTER.register("black_castle_bricks", () -> new Block(blackChessBricks));
	public static RegistryObject<Block> DARK_GRAY_CASTLE_BRICKS = REGISTER.register("dark_gray_castle_bricks", () -> new Block(darkGrayChessBricks));
	public static RegistryObject<Block> LIGHT_GRAY_CASTLE_BRICKS = REGISTER.register("light_gray_castle_bricks", () -> new Block(lightGrayChessBricks));
	public static RegistryObject<Block> WHITE_CASTLE_BRICKS = REGISTER.register("white_castle_bricks", () -> new Block(whiteChessBricks));
	public static RegistryObject<Block> BLACK_CASTLE_BRICK_SMOOTH = REGISTER.register("black_castle_brick_smooth", () -> new Block(blackChessBricks));
	public static RegistryObject<Block> DARK_GRAY_CASTLE_BRICK_SMOOTH = REGISTER.register("dark_gray_castle_brick_smooth", () -> new Block(darkGrayChessBricks));
	public static RegistryObject<Block> LIGHT_GRAY_CASTLE_BRICK_SMOOTH = REGISTER.register("light_gray_castle_brick_smooth", () -> new Block(lightGrayChessBricks));
	public static RegistryObject<Block> WHITE_CASTLE_BRICK_SMOOTH = REGISTER.register("white_castle_brick_smooth", () -> new Block(whiteChessBricks));
	public static RegistryObject<Block> BLACK_CASTLE_BRICK_TRIM = REGISTER.register("black_castle_brick_trim", () -> new MSDirectionalBlock(blackChessBricks));
	public static RegistryObject<Block> DARK_GRAY_CASTLE_BRICK_TRIM = REGISTER.register("dark_gray_castle_brick_trim", () -> new MSDirectionalBlock(darkGrayChessBricks));
	public static RegistryObject<Block> LIGHT_GRAY_CASTLE_BRICK_TRIM = REGISTER.register("light_gray_castle_brick_trim", () -> new MSDirectionalBlock(lightGrayChessBricks));
	public static RegistryObject<Block> WHITE_CASTLE_BRICK_TRIM = REGISTER.register("white_castle_brick_trim", () -> new MSDirectionalBlock(whiteChessBricks));
	public static BlockBehaviour.Properties stainedGlass = Block.Properties.of(Material.GLASS, DyeColor.BLUE).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn(MSBlocks::never).isRedstoneConductor(MSBlocks::never).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never);
	public static RegistryObject<Block> CHECKERED_STAINED_GLASS = REGISTER.register("checkered_stained_glass", () -> new StainedGlassBlock(DyeColor.BLUE, stainedGlass));
	public static RegistryObject<Block> BLACK_CROWN_STAINED_GLASS = REGISTER.register("black_crown_stained_glass", () -> new StainedGlassBlock(DyeColor.BLUE, stainedGlass));
	public static RegistryObject<Block> BLACK_PAWN_STAINED_GLASS = REGISTER.register("black_pawn_stained_glass", () -> new StainedGlassBlock(DyeColor.BLUE, stainedGlass));
	public static RegistryObject<Block> WHITE_CROWN_STAINED_GLASS = REGISTER.register("white_crown_stained_glass", () -> new StainedGlassBlock(DyeColor.BLUE, stainedGlass));
	public static RegistryObject<Block> WHITE_PAWN_STAINED_GLASS = REGISTER.register("white_pawn_stained_glass", () -> new StainedGlassBlock(DyeColor.BLUE, stainedGlass));
	
	//public static final Block /*BLACK_CHESS_DIRT = getNull(), */WHITE_CHESS_DIRT = getNull(), DARK_GRAY_CHESS_DIRT = getNull(), LIGHT_GRAY_CHESS_DIRT = getNull();
	//public static final Block SKAIA_PORTAL = getNull();
	//public static final Block BLACK_CASTLE_BRICKS = getNull();
	//public static final Block DARK_GRAY_CASTLE_BRICKS = getNull();
	//public static final Block LIGHT_GRAY_CASTLE_BRICKS = getNull();
	//public static final Block WHITE_CASTLE_BRICKS = getNull();
	//public static final Block BLACK_CASTLE_BRICK_SMOOTH = getNull();
	//public static final Block DARK_GRAY_CASTLE_BRICK_SMOOTH = getNull();
	//public static final Block LIGHT_GRAY_CASTLE_BRICK_SMOOTH = getNull();
	//public static final Block WHITE_CASTLE_BRICK_SMOOTH = getNull();
	//public static final Block BLACK_CASTLE_BRICK_TRIM = getNull();
	//public static final Block DARK_GRAY_CASTLE_BRICK_TRIM = getNull();
	//public static final Block LIGHT_GRAY_CASTLE_BRICK_TRIM = getNull();
	//public static final Block WHITE_CASTLE_BRICK_TRIM = getNull();
	//public static final Block CHECKERED_STAINED_GLASS = getNull();
	//public static final Block BLACK_CROWN_STAINED_GLASS = getNull();
	//public static final Block BLACK_PAWN_STAINED_GLASS = getNull();
	//public static final Block WHITE_CROWN_STAINED_GLASS = getNull();
	//public static final Block WHITE_PAWN_STAINED_GLASS = getNull();
	
	//Ores
	public static BlockBehaviour.Properties cruxiteOre = Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops();
	public static UniformInt cruxiteDrops = UniformInt.of(2, 5);
	public static RegistryObject<Block> STONE_CRUXITE_ORE9a5 = REGISTER.register("stone_cruxite_ore", () -> new OreBlock(cruxiteOre, cruxiteDrops));
	public static RegistryObject<Block> NETHERRACK_CRUXITE_ORE9a5 = REGISTER.register("netherrack_cruxite_ore", () -> new OreBlock(cruxiteOre, cruxiteDrops));
	public static RegistryObject<Block> COBBLESTONE_CRUXITE_ORE9a5 = REGISTER.register("cobblestone_cruxite_ore", () -> new OreBlock(cruxiteOre, cruxiteDrops));
	public static RegistryObject<Block> SANDSTONE_CRUXITE_ORE9a5 = REGISTER.register("sandstone_cruxite_ore", () -> new OreBlock(cruxiteOre, cruxiteDrops));
	public static RegistryObject<Block> RED_SANDSTONE_CRUXITE_ORE9a5 = REGISTER.register("red_sandstone_cruxite_ore", () -> new OreBlock(cruxiteOre, cruxiteDrops));
	public static RegistryObject<Block> END_STONE_CRUXITE_ORE9a5 = REGISTER.register("end_stone_cruxite_ore", () -> new OreBlock(cruxiteOre, cruxiteDrops));
	public static RegistryObject<Block> SHADE_STONE_CRUXITE_ORE9a5 = REGISTER.register("shade_stone_cruxite_ore", () -> new OreBlock(cruxiteOre, cruxiteDrops));
	public static RegistryObject<Block> PINK_STONE_CRUXITE_ORE9a5 = REGISTER.register("pink_stone_cruxite_ore", () -> new OreBlock(cruxiteOre, cruxiteDrops));
	public static BlockBehaviour.Properties uraniumOre = Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().lightLevel(state -> 3);
	public static UniformInt uraniumDrops = UniformInt.of(2, 5);
	public static RegistryObject<Block> STONE_URANIUM_ORE9a5 = REGISTER.register("stone_uranium_ore", () -> new OreBlock(uraniumOre, uraniumDrops));
	public static RegistryObject<Block> NETHERRACK_URANIUM_ORE9a5 = REGISTER.register("netherrack_uranium_ore", () -> new OreBlock(uraniumOre, uraniumDrops));
	public static RegistryObject<Block> COBBLESTONE_URANIUM_ORE9a5 = REGISTER.register("cobblestone_uranium_ore", () -> new OreBlock(uraniumOre, uraniumDrops));
	public static RegistryObject<Block> SANDSTONE_URANIUM_ORE9a5 = REGISTER.register("sandstone_uranium_ore", () -> new OreBlock(uraniumOre, uraniumDrops));
	public static RegistryObject<Block> RED_SANDSTONE_URANIUM_ORE9a5 = REGISTER.register("red_sandstone_uranium_ore", () -> new OreBlock(uraniumOre, uraniumDrops));
	public static RegistryObject<Block> END_STONE_URANIUM_ORE9a5 = REGISTER.register("end_stone_uranium_ore", () -> new OreBlock(uraniumOre, uraniumDrops));
	public static RegistryObject<Block> SHADE_STONE_URANIUM_ORE9a5 = REGISTER.register("shade_stone_uranium_ore", () -> new OreBlock(uraniumOre, uraniumDrops));
	public static RegistryObject<Block> PINK_STONE_URANIUM_ORE9a5 = REGISTER.register("pink_stone_uranium_ore", () -> new OreBlock(uraniumOre, uraniumDrops));
	public static BlockBehaviour.Properties genericOre = Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops();
	public static UniformInt coalDrops = UniformInt.of(0, 2);
	public static RegistryObject<Block> NETHERRACK_COAL_ORE9a5 = REGISTER.register("netherrack_coal_ore", () -> new OreBlock(genericOre, coalDrops));
	public static RegistryObject<Block> SHADE_STONE_COAL_ORE9a5 = REGISTER.register("shade_stone_coal_ore", () -> new OreBlock(genericOre, coalDrops));
	public static RegistryObject<Block> PINK_STONE_COAL_ORE9a5 = REGISTER.register("pink_stone_coal_ore", () -> new OreBlock(genericOre, coalDrops));
	public static RegistryObject<Block> END_STONE_IRON_ORE9a5 = REGISTER.register("end_stone_iron_ore", () -> new OreBlock(genericOre));
	public static RegistryObject<Block> SANDSTONE_IRON_ORE9a5 = REGISTER.register("sandstone_iron_ore", () -> new OreBlock(genericOre));
	public static RegistryObject<Block> RED_SANDSTONE_IRON_ORE9a5 = REGISTER.register("red_sandstone_iron_ore", () -> new OreBlock(genericOre));
	public static RegistryObject<Block> SANDSTONE_GOLD_ORE9a5 = REGISTER.register("sandstone_gold_ore", () -> new OreBlock(genericOre));
	public static RegistryObject<Block> RED_SANDSTONE_GOLD_ORE9a5 = REGISTER.register("red_sandstone_gold_ore", () -> new OreBlock(genericOre));
	public static RegistryObject<Block> SHADE_STONE_GOLD_ORE9a5 = REGISTER.register("shade_stone_gold_ore", () -> new OreBlock(genericOre));
	public static RegistryObject<Block> PINK_STONE_GOLD_ORE = REGISTER.register("pink_stone_gold_ore", () -> new OreBlock(genericOre));
	public static RegistryObject<Block> END_STONE_REDSTONE_ORE = REGISTER.register("end_stone_redstone_ore", () -> new OreBlock(genericOre, UniformInt.of(1, 5)));
	public static RegistryObject<Block> STONE_QUARTZ_ORE = REGISTER.register("stone_quartz_ore", () -> new OreBlock(genericOre, UniformInt.of(2, 5)));
	public static RegistryObject<Block> PINK_STONE_LAPIS_ORE = REGISTER.register("pink_stone_lapis_ore", () -> new OreBlock(genericOre, UniformInt.of(2, 5)));
	public static RegistryObject<Block> PINK_STONE_DIAMOND_ORE = REGISTER.register("pink_stone_diamond_ore", () -> new OreBlock(genericOre, UniformInt.of(3, 7)));
	
	/*public static final Block STONE_CRUXITE_ORE = getNull(), NETHERRACK_CRUXITE_ORE = getNull(), COBBLESTONE_CRUXITE_ORE = getNull(), SANDSTONE_CRUXITE_ORE = getNull();
	public static final Block RED_SANDSTONE_CRUXITE_ORE = getNull(), END_STONE_CRUXITE_ORE = getNull(), SHADE_STONE_CRUXITE_ORE = getNull(), PINK_STONE_CRUXITE_ORE = getNull();
	public static final Block STONE_URANIUM_ORE = getNull(), NETHERRACK_URANIUM_ORE = getNull(), COBBLESTONE_URANIUM_ORE = getNull(), SANDSTONE_URANIUM_ORE = getNull();
	public static final Block RED_SANDSTONE_URANIUM_ORE = getNull(), END_STONE_URANIUM_ORE = getNull(), SHADE_STONE_URANIUM_ORE = getNull(), PINK_STONE_URANIUM_ORE = getNull();
	public static final Block NETHERRACK_COAL_ORE = getNull(), SHADE_STONE_COAL_ORE = getNull(), PINK_STONE_COAL_ORE = getNull();
	public static final Block END_STONE_IRON_ORE = getNull(), SANDSTONE_IRON_ORE = getNull(), RED_SANDSTONE_IRON_ORE = getNull();
	public static final Block SANDSTONE_GOLD_ORE = getNull(), RED_SANDSTONE_GOLD_ORE = getNull(), SHADE_STONE_GOLD_ORE = getNull(), PINK_STONE_GOLD_ORE = getNull();
	public static final Block END_STONE_REDSTONE_ORE = getNull();
	public static final Block STONE_QUARTZ_ORE = getNull();
	public static final Block PINK_STONE_LAPIS_ORE = getNull();
	public static final Block PINK_STONE_DIAMOND_ORE = getNull();/**/
	
	//Resource Blocks
	public static RegistryObject<Block> CRUXITE_BLOCK9a5 = REGISTER.register("cruxite_block", () -> new Block(Block.Properties.of(Material.STONE, DyeColor.LIGHT_BLUE).strength(3.0F).requiresCorrectToolForDrops()));
	public static RegistryObject<Block> URANIUM_BLOCK9a5 = REGISTER.register("uranium_block", () -> new Block(Block.Properties.of(Material.STONE, DyeColor.LIME).strength(3.0F).requiresCorrectToolForDrops().lightLevel(state -> 7)));
	public static RegistryObject<Block> GENERIC_OBJECT9a5 = REGISTER.register("generic_object", () -> new Block(Block.Properties.of(Material.VEGETABLE, DyeColor.LIME).strength(1.0F).sound(SoundType.WOOD)));
	
	//Land Environment
	public static RegistryObject<Block> BLUE_DIRT9a5 = REGISTER.register("blue_dirt", () -> new Block(Block.Properties.of(Material.DIRT, MaterialColor.COLOR_BLUE).strength(0.5F).sound(SoundType.GRAVEL)));
	public static RegistryObject<Block> THOUGHT_DIRT9a5 = REGISTER.register("thought_dirt", () -> new Block(Block.Properties.of(Material.DIRT, MaterialColor.COLOR_LIGHT_GREEN).strength(0.5F).sound(SoundType.GRAVEL)));
	static BlockBehaviour.Properties coarseStone = Block.Properties.of(Material.STONE, MaterialColor.STONE).requiresCorrectToolForDrops().strength(2.0F, 6.0F);
	public static RegistryObject<Block> COARSE_STONE9a5 = REGISTER.register("coarse_stone", () -> new Block(coarseStone));
	public static RegistryObject<Block> CHISELED_COARSE_STONE9a5 = REGISTER.register("chiseled_coarse_stone", () -> new Block(coarseStone));
	public static RegistryObject<Block> COARSE_STONE_BRICKS9a5 = REGISTER.register("coarse_stone_bricks", () -> new Block(coarseStone));
	public static RegistryObject<Block> COARSE_STONE_COLUMN9a5 = REGISTER.register("coarse_stone_column", () -> new MSDirectionalBlock(coarseStone));
	public static RegistryObject<Block> CHISELED_COARSE_STONE_BRICKS9a5 = REGISTER.register("chiseled_coarse_stone_bricks", () -> new Block(coarseStone));
	public static RegistryObject<Block> CRACKED_COARSE_STONE_BRICKS9a5 = REGISTER.register("cracked_coarse_stone_bricks", () -> new Block(coarseStone));
	public static RegistryObject<Block> MOSSY_COARSE_STONE = REGISTER.register("mossy_coarse_stone", () -> new Block(coarseStone)); /*TODO Rename this blocks registry to be MOSSY_COARSE_STONE_BRICKS when 1.18 rolls around*/
	static BlockBehaviour.Properties shadeStone = Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).requiresCorrectToolForDrops().strength(1.5F, 6.0F);
	public static RegistryObject<Block> SHADE_STONE9a5 = REGISTER.register("shade_stone", () -> new Block(shadeStone));
	public static RegistryObject<Block> SMOOTH_SHADE_STONE9a5 = REGISTER.register("smooth_shade_stone", () -> new Block(shadeStone));
	public static RegistryObject<Block> SHADE_BRICKS9a5 = REGISTER.register("shade_bricks", () -> new Block(shadeStone));
	public static RegistryObject<Block> SHADE_COLUMN9a5 = REGISTER.register("shade_column", () -> new MSDirectionalBlock(shadeStone));
	public static RegistryObject<Block> CHISELED_SHADE_BRICKS9a5 = REGISTER.register("chiseled_shade_bricks", () -> new Block(shadeStone));
	public static RegistryObject<Block> CRACKED_SHADE_BRICKS9a5 = REGISTER.register("cracked_shade_bricks", () -> new Block(shadeStone));
	public static RegistryObject<Block> MOSSY_SHADE_BRICKS9a5 = REGISTER.register("mossy_shade_bricks", () -> new Block(shadeStone));
	public static RegistryObject<Block> BLOOD_SHADE_BRICKS9a5 = REGISTER.register("blood_shade_bricks", () -> new Block(shadeStone));
	public static RegistryObject<Block> TAR_SHADE_BRICKS9a5 = REGISTER.register("tar_shade_bricks", () -> new Block(shadeStone));
	static BlockBehaviour.Properties frost = Block.Properties.of(Material.STONE, MaterialColor.ICE).requiresCorrectToolForDrops().strength(1.5F, 6.0F);
	public static RegistryObject<Block> FROST_TILE9a5 = REGISTER.register("frost_tile", () -> new Block(frost));
	public static RegistryObject<Block> CHISELED_FROST_TILE9a5 = REGISTER.register("chiseled_frost_tile", () -> new Block(frost));
	public static RegistryObject<Block> FROST_BRICKS9a5 = REGISTER.register("frost_bricks", () -> new Block(frost));
	public static RegistryObject<Block> FROST_COLUMN9a5 = REGISTER.register("frost_column", () -> new MSDirectionalBlock(frost));
	public static RegistryObject<Block> CHISELED_FROST_BRICKS9a5 = REGISTER.register("chiseled_frost_bricks", () -> new Block(frost));
	public static RegistryObject<Block> CRACKED_FROST_BRICKS9a5 = REGISTER.register("cracked_frost_bricks", () -> new Block(frost));
	public static RegistryObject<Block> FLOWERY_FROST_BRICKS9a5 = REGISTER.register("flowery_frost_bricks", () -> new Block(frost));
	static BlockBehaviour.Properties metal = Block.Properties.of(Material.METAL, MaterialColor.METAL).requiresCorrectToolForDrops().strength(3.0F, 9.0F); //TODO the material for cast iron and chiseled cast iron(but not steel beam) used to be stone, ensure this change to metal is acceptable
	public static RegistryObject<Block> CAST_IRON9a5 = REGISTER.register("cast_iron", () -> new Block(metal));
	public static RegistryObject<Block> CHISELED_CAST_IRON9a5 = REGISTER.register("chiseled_cast_iron", () -> new Block(metal));
	public static RegistryObject<Block> STEEL_BEAM9a5 = REGISTER.register("steel_beam", () -> new MSDirectionalBlock(metal));
	static BlockBehaviour.Properties myceliumStone = Block.Properties.of(Material.STONE, MaterialColor.COLOR_MAGENTA).requiresCorrectToolForDrops().strength(1.5F, 6.0F);
	public static RegistryObject<Block> MYCELIUM_COBBLESTONE9a5 = REGISTER.register("mycelium_cobblestone", () -> new Block(myceliumStone));
	public static RegistryObject<Block> MYCELIUM_STONE9a5 = REGISTER.register("mycelium_stone", () -> new Block(myceliumStone));
	public static RegistryObject<Block> POLISHED_MYCELIUM_STONE9a5 = REGISTER.register("polished_mycelium_stone", () -> new Block(myceliumStone));
	public static RegistryObject<Block> MYCELIUM_BRICKS9a5 = REGISTER.register("mycelium_bricks", () -> new Block(myceliumStone));
	public static RegistryObject<Block> MYCELIUM_COLUMN9a5 = REGISTER.register("mycelium_column", () -> new MSDirectionalBlock(myceliumStone));
	public static RegistryObject<Block> CHISELED_MYCELIUM_BRICKS9a5 = REGISTER.register("chiseled_mycelium_bricks", () -> new Block(myceliumStone));
	public static RegistryObject<Block> CRACKED_MYCELIUM_BRICKS9a5 = REGISTER.register("cracked_mycelium_bricks", () -> new Block(myceliumStone));
	public static RegistryObject<Block> MOSSY_MYCELIUM_BRICKS9a5 = REGISTER.register("mossy_mycelium_bricks", () -> new Block(myceliumStone));
	public static RegistryObject<Block> FLOWERY_MYCELIUM_BRICKS9a5 = REGISTER.register("flowery_mycelium_bricks", () -> new Block(myceliumStone));
	static BlockBehaviour.Properties blackStone = Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(2.5F, 6.0F);
	public static RegistryObject<Block> BLACK_SAND9a5 = REGISTER.register("black_sand", () -> new SandBlock(0x181915, Block.Properties.of(Material.SAND, MaterialColor.COLOR_BLACK).strength(0.5F).sound(SoundType.SAND)));
	public static RegistryObject<Block> BLACK_COBBLESTONE9a5 = REGISTER.register("black_cobblestone", () -> new Block(blackStone));
	public static RegistryObject<Block> BLACK_STONE9a5 = REGISTER.register("black_stone", () -> new Block(blackStone));
	public static RegistryObject<Block> POLISHED_BLACK_STONE9a5 = REGISTER.register("polished_black_stone", () -> new Block(blackStone));
	public static RegistryObject<Block> BLACK_STONE_BRICKS9a5 = REGISTER.register("black_stone_bricks", () -> new Block(blackStone));
	public static RegistryObject<Block> BLACK_STONE_COLUMN9a5 = REGISTER.register("black_stone_column", () -> new MSDirectionalBlock(blackStone));
	public static RegistryObject<Block> CHISELED_BLACK_STONE_BRICKS9a5 = REGISTER.register("cracked_black_stone_bricks", () -> new Block(blackStone));
	public static RegistryObject<Block> CRACKED_BLACK_STONE_BRICKS9a5 = REGISTER.register("chiseled_black_stone_bricks", () -> new Block(blackStone));
	static BlockBehaviour.Properties floweryOrDecrepitStone = Block.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(1.5F, 6.0F);
	public static RegistryObject<Block> FLOWERY_MOSSY_COBBLESTONE9a5 = REGISTER.register("flowery_mossy_cobblestone", () -> new Block(floweryOrDecrepitStone));
	public static RegistryObject<Block> FLOWERY_MOSSY_STONE_BRICKS9a5 = REGISTER.register("flowery_mossy_stone_bricks", () -> new Block(floweryOrDecrepitStone));
	public static RegistryObject<Block> DECREPIT_STONE_BRICKS9a5 = REGISTER.register("decrepit_stone_bricks", () -> new Block(floweryOrDecrepitStone));
	public static RegistryObject<Block> MOSSY_DECREPIT_STONE_BRICKS9a5 = REGISTER.register("mossy_decrepit_stone_bricks", () -> new Block(floweryOrDecrepitStone));
	public static RegistryObject<Block> COARSE_END_STONE9a5 = REGISTER.register("coarse_end_stone", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.SAND).requiresCorrectToolForDrops().strength(3.0F, 9.0F)));
	public static RegistryObject<Block> END_GRASS9a5 = REGISTER.register("end_grass", () -> new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(3.0F, 9.0F)));
	static BlockBehaviour.Properties chalk = Block.Properties.of(Material.STONE, MaterialColor.SNOW).requiresCorrectToolForDrops().strength(1.5F, 6.0F);
	public static RegistryObject<Block> CHALK9a5 = REGISTER.register("chalk", () -> new Block(chalk));
	public static RegistryObject<Block> POLISHED_CHALK9a5 = REGISTER.register("polished_chalk", () -> new Block(chalk));
	public static RegistryObject<Block> CHALK_BRICKS9a5 = REGISTER.register("chalk_bricks", () -> new Block(chalk));
	public static RegistryObject<Block> CHALK_COLUMN9a5 = REGISTER.register("chalk_column", () -> new MSDirectionalBlock(chalk));
	public static RegistryObject<Block> CHISELED_CHALK_BRICKS9a5 = REGISTER.register("chiseled_chalk_bricks", () -> new Block(chalk));
	public static RegistryObject<Block> MOSSY_CHALK_BRICKS9a5 = REGISTER.register("mossy_chalk_bricks", () -> new Block(chalk));
	public static RegistryObject<Block> FLOWERY_CHALK_BRICKS9a5 = REGISTER.register("flowery_chalk_bricks", () -> new Block(chalk));
	static BlockBehaviour.Properties pinkStone = Block.Properties.of(Material.STONE, MaterialColor.SNOW).requiresCorrectToolForDrops().strength(1.5F, 6.0F);
	public static RegistryObject<Block> PINK_STONE9a5 = REGISTER.register("pink_stone", () -> new Block(pinkStone));
	public static RegistryObject<Block> POLISHED_PINK_STONE9a5 = REGISTER.register("polished_pink_stone", () -> new Block(pinkStone));
	public static RegistryObject<Block> PINK_STONE_BRICKS9a5 = REGISTER.register("pink_stone_bricks", () -> new Block(pinkStone));
	public static RegistryObject<Block> PINK_STONE_COLUMN9a5 = REGISTER.register("pink_stone_column", () -> new MSDirectionalBlock(pinkStone));
	public static RegistryObject<Block> CHISELED_PINK_STONE_BRICKS9a5 = REGISTER.register("chiseled_pink_stone_bricks", () -> new Block(pinkStone));
	public static RegistryObject<Block> CRACKED_PINK_STONE_BRICKS9a5 = REGISTER.register("cracked_pink_stone_bricks", () -> new Block(pinkStone));
	public static RegistryObject<Block> MOSSY_PINK_STONE_BRICKS9a5 = REGISTER.register("mossy_pink_stone_bricks", () -> new Block(pinkStone));
	static BlockBehaviour.Properties brownStone = Block.Properties.of(Material.STONE, MaterialColor.COLOR_BROWN).requiresCorrectToolForDrops().strength(2.5F, 7.0F);
	public static RegistryObject<Block> BROWN_STONE9a5 = REGISTER.register("brown_stone", () -> new Block(brownStone));
	public static RegistryObject<Block> POLISHED_BROWN_STONE9a5 = REGISTER.register("polished_brown_stone", () -> new Block(brownStone));
	public static RegistryObject<Block> BROWN_STONE_BRICKS9a5 = REGISTER.register("brown_stone_bricks", () -> new Block(brownStone));
	public static RegistryObject<Block> CRACKED_BROWN_STONE_BRICKS9a5 = REGISTER.register("cracked_brown_stone_bricks", () -> new Block(brownStone));
	public static RegistryObject<Block> BROWN_STONE_COLUMN9a5 = REGISTER.register("brown_stone_column", () -> new MSDirectionalBlock(brownStone));
	static BlockBehaviour.Properties greenStone = Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(2.5F, 7.0F);
	public static RegistryObject<Block> GREEN_STONE9a5 = REGISTER.register("green_stone", () -> new Block(greenStone));
	public static RegistryObject<Block> POLISHED_GREEN_STONE9a5 = REGISTER.register("polished_green_stone", () -> new Block(greenStone));
	public static RegistryObject<Block> GREEN_STONE_BRICKS9a5 = REGISTER.register("green_stone_bricks", () -> new Block(greenStone));
	public static RegistryObject<Block> GREEN_STONE_COLUMN9a5 = REGISTER.register("green_stone_column", () -> new MSDirectionalBlock(greenStone));
	public static RegistryObject<Block> CHISELED_GREEN_STONE_BRICKS9a5 = REGISTER.register("chiseled_green_stone_bricks", () -> new Block(greenStone));
	public static RegistryObject<Block> HORIZONTAL_GREEN_STONE_BRICKS9a5 = REGISTER.register("horizontal_green_stone_bricks", () -> new Block(greenStone));
	public static RegistryObject<Block> VERTICAL_GREEN_STONE_BRICKS9a5 = REGISTER.register("vertical_green_stone_bricks", () -> new Block(greenStone));
	public static RegistryObject<Block> GREEN_STONE_BRICK_TRIM9a5 = REGISTER.register("green_stone_brick_trim", () -> new MSDirectionalBlock(greenStone));
	public static RegistryObject<Block> GREEN_STONE_BRICK_FROG9a5 = REGISTER.register("green_stone_brick_frog", () -> new HieroglyphBlock(greenStone));
	public static RegistryObject<Block> GREEN_STONE_BRICK_IGUANA_LEFT9a5 = REGISTER.register("green_stone_brick_iguana_left", () -> new HieroglyphBlock(greenStone));
	public static RegistryObject<Block> GREEN_STONE_BRICK_IGUANA_RIGHT9a5 = REGISTER.register("green_stone_brick_iguana_right", () -> new HieroglyphBlock(greenStone));
	public static RegistryObject<Block> GREEN_STONE_BRICK_LOTUS9a5 = REGISTER.register("green_stone_brick_lotus", () -> new HieroglyphBlock(greenStone));
	public static RegistryObject<Block> GREEN_STONE_BRICK_NAK_LEFT9a5 = REGISTER.register("green_stone_brick_nak_left", () -> new HieroglyphBlock(greenStone));
	public static RegistryObject<Block> GREEN_STONE_BRICK_NAK_RIGHT9a5 = REGISTER.register("green_stone_brick_nak_right", () -> new HieroglyphBlock(greenStone));
	public static RegistryObject<Block> GREEN_STONE_BRICK_SALAMANDER_LEFT9a5 = REGISTER.register("green_stone_brick_salamander_left", () -> new HieroglyphBlock(greenStone));
	public static RegistryObject<Block> GREEN_STONE_BRICK_SALAMANDER_RIGHT9a5 = REGISTER.register("green_stone_brick_salamander_right", () -> new HieroglyphBlock(greenStone));
	public static RegistryObject<Block> GREEN_STONE_BRICK_SKAIA9a5 = REGISTER.register("green_stone_brick_skaia", () -> new HieroglyphBlock(greenStone));
	public static RegistryObject<Block> GREEN_STONE_BRICK_TURTLE9a5 = REGISTER.register("green_stone_brick_turtle", () -> new HieroglyphBlock(greenStone));
	public static RegistryObject<Block> SANDSTONE_COLUMN9a5 = REGISTER.register("sandstone_column", () -> new MSDirectionalBlock(Block.Properties.of(Material.STONE, MaterialColor.SAND).requiresCorrectToolForDrops().strength(0.8F)));
	public static RegistryObject<Block> CHISELED_SANDSTONE_COLUMN9a5 = REGISTER.register("chiseled_sandstone_column", () -> new MSDirectionalBlock(Block.Properties.of(Material.STONE, MaterialColor.SAND).requiresCorrectToolForDrops().strength(0.8F)));
	public static RegistryObject<Block> RED_SANDSTONE_COLUMN9a5 = REGISTER.register("red_sandstone_column", () -> new MSDirectionalBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(0.8F)));
	public static RegistryObject<Block> CHISELED_RED_SANDSTONE_COLUMN9a5 = REGISTER.register("chiseled_red_sandstone_column", () -> new MSDirectionalBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(0.8F)));
	public static RegistryObject<Block> UNCARVED_WOOD9a5 = REGISTER.register("uncarved_wood", () -> new Block(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).requiresCorrectToolForDrops().sound(SoundType.WOOD)));
	public static RegistryObject<Block> CHIPBOARD9a5 = REGISTER.register("chipboard", () -> new Block(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(1.0F).requiresCorrectToolForDrops().sound(SoundType.SCAFFOLDING)));
	public static RegistryObject<Block> WOOD_SHAVINGS9a5 = REGISTER.register("wood_shavings", () -> new Block(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(0.4F).sound(SoundType.SAND)));
	public static RegistryObject<Block> DENSE_CLOUD9a5 = REGISTER.register("dense_cloud", () -> new Block(Block.Properties.of(Material.GLASS, MaterialColor.COLOR_YELLOW).strength(0.5F).sound(SoundType.SNOW)));
	public static RegistryObject<Block> BRIGHT_DENSE_CLOUD9a5 = REGISTER.register("bright_dense_cloud", () -> new Block(Block.Properties.of(Material.GLASS, MaterialColor.COLOR_LIGHT_GRAY).strength(0.5F).sound(SoundType.SNOW)));
	public static RegistryObject<Block> SUGAR_CUBE9a5 = REGISTER.register("sugar_cube", () -> new Block(Block.Properties.of(Material.SAND, MaterialColor.SNOW).strength(0.4F).sound(SoundType.SAND)));
	
	/*
	public static final Block CRUXITE_BLOCK9a5 = getNull();
	public static final Block URANIUM_BLOCK9a5 = getNull();
	public static final Block GENERIC_OBJECT9a5 = getNull();
	
	//Land Environment Blocks
	public static final Block BLUE_DIRT9a5 = getNull(), THOUGHT_DIRT9a5 = getNull();
	public static final Block COARSE_STONE9a5 = getNull(), CHISELED_COARSE_STONE9a5 = getNull(), COARSE_STONE_BRICKS9a5 = getNull(), COARSE_STONE_COLUMN9a5 = getNull(), CHISELED_COARSE_STONE_BRICKS9a5 = getNull(), CRACKED_COARSE_STONE_BRICKS9a5 = getNull(), MOSSY_COARSE_STONE = getNull();
	public static final Block SHADE_STONE9a5 = getNull(), SMOOTH_SHADE_STONE9a5 = getNull(), SHADE_BRICKS9a5 = getNull(), SHADE_COLUMN9a5 = getNull(), CHISELED_SHADE_BRICKS9a5 = getNull(), CRACKED_SHADE_BRICKS9a5 = getNull(), MOSSY_SHADE_BRICKS9a5 = getNull(), BLOOD_SHADE_BRICKS9a5 = getNull(), TAR_SHADE_BRICKS9a5 = getNull();
	public static final Block FROST_BRICKS9a5 = getNull(), FROST_TILE9a5 = getNull(), CHISELED_FROST_TILE9a5 = getNull(), FROST_COLUMN9a5 = getNull(),  CHISELED_FROST_BRICKS9a5 = getNull(), CRACKED_FROST_BRICKS9a5 = getNull(), FLOWERY_FROST_BRICKS9a5 = getNull();
	public static final Block CAST_IRON9a5 = getNull(), CHISELED_CAST_IRON9a5 = getNull();
	public static final Block MYCELIUM_COBBLESTONE9a5 = getNull(), MYCELIUM_STONE9a5 = getNull(), POLISHED_MYCELIUM_STONE9a5 = getNull(), MYCELIUM_BRICKS9a5 = getNull(), MYCELIUM_COLUMN9a5 = getNull();
	public static final Block CHISELED_MYCELIUM_BRICKS9a5 = getNull(), CRACKED_MYCELIUM_BRICKS9a5 = getNull(), MOSSY_MYCELIUM_BRICKS9a5 = getNull(), FLOWERY_MYCELIUM_BRICKS9a5 = getNull();
	public static final Block STEEL_BEAM9a5 = getNull();
	public static final Block BLACK_STONE9a5 = getNull(), BLACK_COBBLESTONE9a5 = getNull(), POLISHED_BLACK_STONE9a5 = getNull(), BLACK_STONE_BRICKS9a5 = getNull(), BLACK_STONE_COLUMN9a5 = getNull(), CHISELED_BLACK_STONE_BRICKS9a5 = getNull(), CRACKED_BLACK_STONE_BRICKS9a5 = getNull(), BLACK_SAND9a5 = getNull();
	public static final Block DECREPIT_STONE_BRICKS9a5 = getNull(), FLOWERY_MOSSY_COBBLESTONE9a5 = getNull(), MOSSY_DECREPIT_STONE_BRICKS9a5 = getNull(), FLOWERY_MOSSY_STONE_BRICKS9a5 = getNull();
	public static final Block COARSE_END_STONE9a5 = getNull(), END_GRASS9a5 = getNull();
	public static final Block CHALK9a5 = getNull(), POLISHED_CHALK9a5 = getNull(), CHALK_BRICKS9a5 = getNull(), CHALK_COLUMN9a5 = getNull(), CHISELED_CHALK_BRICKS9a5 = getNull(), MOSSY_CHALK_BRICKS9a5 = getNull(), FLOWERY_CHALK_BRICKS9a5 = getNull();
	public static final Block PINK_STONE9a5 = getNull(), POLISHED_PINK_STONE9a5 = getNull(), PINK_STONE_BRICKS9a5 = getNull(), CHISELED_PINK_STONE_BRICKS9a5 = getNull(), PINK_STONE_COLUMN9a5 = getNull();
	public static final Block CRACKED_PINK_STONE_BRICKS9a5 = getNull(), MOSSY_PINK_STONE_BRICKS9a5 = getNull();
	public static final Block BROWN_STONE9a5 = getNull(), POLISHED_BROWN_STONE9a5 = getNull(), BROWN_STONE_BRICKS9a5 = getNull(), CRACKED_BROWN_STONE_BRICKS9a5 = getNull(), BROWN_STONE_COLUMN9a5 = getNull();
	public static final Block GREEN_STONE = getNull(), POLISHED_GREEN_STONE = getNull(), GREEN_STONE_BRICKS = getNull(), GREEN_STONE_COLUMN = getNull(), CHISELED_GREEN_STONE_BRICKS = getNull(), HORIZONTAL_GREEN_STONE_BRICKS = getNull(), VERTICAL_GREEN_STONE_BRICKS = getNull()
			, GREEN_STONE_BRICK_TRIM = getNull(), GREEN_STONE_BRICK_FROG = getNull(), GREEN_STONE_BRICK_IGUANA_LEFT = getNull(), GREEN_STONE_BRICK_IGUANA_RIGHT = getNull(), GREEN_STONE_BRICK_LOTUS = getNull(), GREEN_STONE_BRICK_NAK_LEFT = getNull(), GREEN_STONE_BRICK_NAK_RIGHT = getNull()
			, GREEN_STONE_BRICK_SALAMANDER_LEFT = getNull(), GREEN_STONE_BRICK_SALAMANDER_RIGHT = getNull(), GREEN_STONE_BRICK_SKAIA = getNull(), GREEN_STONE_BRICK_TURTLE = getNull();
	public static final Block SANDSTONE_COLUMN = getNull(), CHISELED_SANDSTONE_COLUMN = getNull(), RED_SANDSTONE_COLUMN = getNull(), CHISELED_RED_SANDSTONE_COLUMN = getNull();
	public static final Block UNCARVED_WOOD9a5 = getNull(), CHIPBOARD9a5 = getNull(), WOOD_SHAVINGS9a5 = getNull();
	public static final Block DENSE_CLOUD9a5 = getNull(), BRIGHT_DENSE_CLOUD9a5 = getNull();
	public static final Block SUGAR_CUBE9a5 = getNull();
	/**/
	
	//Land Tree Blocks
	public static RegistryObject<Block> GLOWING_LOG9a5 = REGISTER.register("glowing_log", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.COLOR_LIGHT_BLUE).strength(2.0F).lightLevel(state -> 11).sound(SoundType.WOOD)));
	public static RegistryObject<Block> FROST_LOG9a5 = REGISTER.register("frost_log", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.ICE).strength(2.0F).sound(SoundType.WOOD)));
	public static RegistryObject<Block> RAINBOW_LOG9a5 = REGISTER.register("rainbow_log", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD).strength(2.0F).sound(SoundType.WOOD)));
	public static RegistryObject<Block> END_LOG9a5 = REGISTER.register("end_log", () -> new DoubleLogBlock(1, 250, Block.Properties.of(Material.WOOD, MaterialColor.SAND).strength(2.0F).sound(SoundType.WOOD)));
	public static RegistryObject<Block> VINE_LOG9a5 = REGISTER.register("vine_log", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, logColors(MaterialColor.WOOD, MaterialColor.PODZOL)).strength(2.0F).sound(SoundType.WOOD)));
	public static RegistryObject<Block> FLOWERY_VINE_LOG9a5 = REGISTER.register("flowery_vine_log", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, logColors(MaterialColor.WOOD, MaterialColor.PODZOL)).strength(2.0F).sound(SoundType.WOOD)));
	public static RegistryObject<Block> DEAD_LOG9a5 = REGISTER.register("dead_log", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, logColors(MaterialColor.WOOD, MaterialColor.PODZOL)).strength(2.0F).sound(SoundType.WOOD)));
	public static RegistryObject<Block> PETRIFIED_LOG9a5 = REGISTER.register("petrified_log", () -> new RotatedPillarBlock(Block.Properties.of(Material.WOOD, logColors(MaterialColor.WOOD, MaterialColor.PODZOL)).strength(2.0F).sound(SoundType.STONE)));
	public static RegistryObject<Block> GLOWING_WOOD9a5 = REGISTER.register("glowing_wood", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.COLOR_LIGHT_BLUE).strength(2.0F).lightLevel(state -> 11).sound(SoundType.WOOD)));
	public static RegistryObject<Block> FROST_WOOD9a5 = REGISTER.register("frost_wood", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.ICE).strength(2.0F).sound(SoundType.WOOD)));
	public static RegistryObject<Block> RAINBOW_WOOD9a5 = REGISTER.register("rainbow_wood", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD).strength(2.0F).sound(SoundType.WOOD)));
	public static RegistryObject<Block> END_WOOD9a5 = REGISTER.register("end_wood", () -> new FlammableLogBlock(1, 250, Block.Properties.of(Material.WOOD, MaterialColor.SAND).strength(2.0F).sound(SoundType.WOOD)));
	public static RegistryObject<Block> VINE_WOOD9a5 = REGISTER.register("vine_wood", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2.0F).sound(SoundType.WOOD)));
	public static RegistryObject<Block> FLOWERY_VINE_WOOD9a5 = REGISTER.register("flowery_vine_wood", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2.0F).sound(SoundType.WOOD)));
	public static RegistryObject<Block> DEAD_WOOD9a5 = REGISTER.register("dead_wood", () -> new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2.0F).sound(SoundType.WOOD)));
	public static RegistryObject<Block> PETRIFIED_WOOD9a5 = REGISTER.register("petrified_wood", () -> new RotatedPillarBlock(Block.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2.0F).sound(SoundType.STONE)));
	public static RegistryObject<Block> GLOWING_PLANKS9a5 = REGISTER.register("glowing_planks", () -> new FlammableBlock(5, 20, Block.Properties.of(Material.WOOD, MaterialColor.COLOR_LIGHT_BLUE).strength(2.0F, 3.0F).lightLevel(state -> 7).sound(SoundType.WOOD)));
	public static RegistryObject<Block> FROST_PLANKS9a5 = REGISTER.register("frost_planks", () -> new FlammableBlock(5, 5, Block.Properties.of(Material.WOOD, MaterialColor.ICE).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	static BlockBehaviour.Properties rainbowPlanks = Block.Properties.of(Material.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD);
	public static RegistryObject<Block> RAINBOW_PLANKS9a5 = REGISTER.register("rainbow_planks", () -> new FlammableBlock(5, 20, rainbowPlanks));
	static BlockBehaviour.Properties endPlanks = Block.Properties.of(Material.WOOD, MaterialColor.SAND).strength(2.0F, 3.0F).sound(SoundType.WOOD);
	public static RegistryObject<Block> END_PLANKS9a5 = REGISTER.register("end_planks", () -> new FlammableBlock(1, 250, endPlanks));
	static BlockBehaviour.Properties deadPlanks = Block.Properties.of(Material.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD);
	public static RegistryObject<Block> DEAD_PLANKS9a5 = REGISTER.register("dead_planks", () -> new FlammableBlock(5, 5, deadPlanks));
	static BlockBehaviour.Properties treatedPlanks = Block.Properties.of(Material.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD);
	public static RegistryObject<Block> TREATED_PLANKS9a5 = REGISTER.register("treated_planks", () -> new FlammableBlock(0, 0, treatedPlanks));
	static BlockBehaviour.Properties leaves = Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(MSBlocks::leafSpawns).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never);
	public static RegistryObject<Block> FROST_LEAVES9a5 = REGISTER.register("frost_leaves", () -> new FlammableLeavesBlock(leaves));
	public static RegistryObject<Block> RAINBOW_LEAVES9a5 = REGISTER.register("rainbow_leaves", () -> new FlammableLeavesBlock(leaves));
	public static RegistryObject<Block> END_LEAVES9a5 = REGISTER.register("end_leaves", () -> new EndLeavesBlock(leaves));
	static BlockBehaviour.Properties sapling = Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS);
	public static RegistryObject<Block> RAINBOW_SAPLING9a5 = REGISTER.register("rainbow_sapling", () -> new RainbowSaplingBlock(sapling));
	public static RegistryObject<Block> END_SAPLING9a5 = REGISTER.register("end_sapling", () -> new EndSaplingBlock(sapling));
	
	/*
	public static final Block GLOWING_LOG = getNull(), FROST_LOG = getNull(), RAINBOW_LOG = getNull(), END_LOG = getNull();
	public static final Block VINE_LOG = getNull(), FLOWERY_VINE_LOG = getNull(), DEAD_LOG = getNull(), PETRIFIED_LOG = getNull();
	public static final Block GLOWING_WOOD = getNull(), FROST_WOOD = getNull(), RAINBOW_WOOD = getNull(), END_WOOD = getNull();
	public static final Block VINE_WOOD = getNull(), FLOWERY_VINE_WOOD = getNull(), DEAD_WOOD = getNull(), PETRIFIED_WOOD = getNull();
	public static final Block GLOWING_PLANKS = getNull(), FROST_PLANKS = getNull(), RAINBOW_PLANKS = getNull(), END_PLANKS = getNull();
	public static final Block DEAD_PLANKS = getNull(), TREATED_PLANKS = getNull();
	public static final Block FROST_LEAVES = getNull(), RAINBOW_LEAVES = getNull(), END_LEAVES = getNull();
	public static final BushBlock RAINBOW_SAPLING = getNull(), END_SAPLING = getNull();
	/**/
	
	//Aspect Tree Blocks
	static BlockBehaviour.Properties aspectLog = Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD);
	public static RegistryObject<Block> BLOOD_ASPECT_LOG9a5 = REGISTER.register("blood_aspect_log", () -> new FlammableLogBlock(aspectLog));
	public static RegistryObject<Block> BREATH_ASPECT_LOG9a5 = REGISTER.register("breath_aspect_log", () -> new FlammableLogBlock(aspectLog));
	public static RegistryObject<Block> DOOM_ASPECT_LOG9a5 = REGISTER.register("doom_aspect_log", () -> new FlammableLogBlock(aspectLog));
	public static RegistryObject<Block> HEART_ASPECT_LOG9a5 = REGISTER.register("heart_aspect_log", () -> new FlammableLogBlock(aspectLog));
	public static RegistryObject<Block> HOPE_ASPECT_LOG9a5 = REGISTER.register("hope_aspect_log", () -> new FlammableLogBlock(aspectLog));
	public static RegistryObject<Block> LIFE_ASPECT_LOG9a5 = REGISTER.register("life_aspect_log", () -> new FlammableLogBlock(aspectLog));
	public static RegistryObject<Block> LIGHT_ASPECT_LOG9a5 = REGISTER.register("light_aspect_log", () -> new FlammableLogBlock(aspectLog));
	public static RegistryObject<Block> MIND_ASPECT_LOG9a5 = REGISTER.register("mind_aspect_log", () -> new FlammableLogBlock(aspectLog));
	public static RegistryObject<Block> RAGE_ASPECT_LOG9a5 = REGISTER.register("rage_aspect_log", () -> new FlammableLogBlock(aspectLog));
	public static RegistryObject<Block> SPACE_ASPECT_LOG9a5 = REGISTER.register("space_aspect_log", () -> new FlammableLogBlock(aspectLog));
	public static RegistryObject<Block> TIME_ASPECT_LOG9a5 = REGISTER.register("time_aspect_log", () -> new FlammableLogBlock(aspectLog));
	public static RegistryObject<Block> VOID_ASPECT_LOG9a5 = REGISTER.register("void_aspect_log", () -> new FlammableLogBlock(aspectLog));
	static BlockBehaviour.Properties aspectPlanks = Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD);
	public static RegistryObject<Block> BLOOD_ASPECT_PLANKS9a5 = REGISTER.register("blood_aspect_planks", () -> new FlammableBlock(5, 20, aspectPlanks));
	public static RegistryObject<Block> BREATH_ASPECT_PLANKS9a5 = REGISTER.register("breath_aspect_planks", () -> new FlammableBlock(5, 20, aspectPlanks));
	public static RegistryObject<Block> DOOM_ASPECT_PLANKS9a5 = REGISTER.register("doom_aspect_planks", () -> new FlammableBlock(5, 20, aspectPlanks));
	public static RegistryObject<Block> HEART_ASPECT_PLANKS9a5 = REGISTER.register("heart_aspect_planks", () -> new FlammableBlock(5, 20, aspectPlanks));
	public static RegistryObject<Block> HOPE_ASPECT_PLANKS9a5 = REGISTER.register("hope_aspect_planks", () -> new FlammableBlock(5, 20, aspectPlanks));
	public static RegistryObject<Block> LIFE_ASPECT_PLANKS9a5 = REGISTER.register("life_aspect_planks", () -> new FlammableBlock(5, 20, aspectPlanks));
	public static RegistryObject<Block> LIGHT_ASPECT_PLANKS9a5 = REGISTER.register("light_aspect_planks", () -> new FlammableBlock(5, 20, aspectPlanks));
	public static RegistryObject<Block> MIND_ASPECT_PLANKS9a5 = REGISTER.register("mind_aspect_planks", () -> new FlammableBlock(5, 20, aspectPlanks));
	public static RegistryObject<Block> RAGE_ASPECT_PLANKS9a5 = REGISTER.register("rage_aspect_planks", () -> new FlammableBlock(5, 20, aspectPlanks));
	public static RegistryObject<Block> SPACE_ASPECT_PLANKS9a5 = REGISTER.register("space_aspect_planks", () -> new FlammableBlock(5, 20, aspectPlanks));
	public static RegistryObject<Block> TIME_ASPECT_PLANKS9a5 = REGISTER.register("time_aspect_planks", () -> new FlammableBlock(5, 20, aspectPlanks));
	public static RegistryObject<Block> VOID_ASPECT_PLANKS9a5 = REGISTER.register("void_aspect_planks", () -> new FlammableBlock(5, 20, aspectPlanks));
	static BlockBehaviour.Properties aspectLeaves = Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion();
	public static RegistryObject<Block> BLOOD_ASPECT_LEAVES9a5 = REGISTER.register("blood_aspect_leaves", () -> new FlammableLeavesBlock(aspectLeaves));
	public static RegistryObject<Block> BREATH_ASPECT_LEAVES9a5 = REGISTER.register("breath_aspect_leaves", () -> new FlammableLeavesBlock(aspectLeaves));
	public static RegistryObject<Block> DOOM_ASPECT_LEAVES9a5 = REGISTER.register("doom_aspect_leaves", () -> new FlammableLeavesBlock(aspectLeaves));
	public static RegistryObject<Block> HEART_ASPECT_LEAVES9a5 = REGISTER.register("heart_aspect_leaves", () -> new FlammableLeavesBlock(aspectLeaves));
	public static RegistryObject<Block> HOPE_ASPECT_LEAVES9a5 = REGISTER.register("hope_aspect_leaves", () -> new FlammableLeavesBlock(aspectLeaves));
	public static RegistryObject<Block> LIFE_ASPECT_LEAVES9a5 = REGISTER.register("life_aspect_leaves", () -> new FlammableLeavesBlock(aspectLeaves));
	public static RegistryObject<Block> LIGHT_ASPECT_LEAVES9a5 = REGISTER.register("light_aspect_leaves", () -> new FlammableLeavesBlock(aspectLeaves));
	public static RegistryObject<Block> MIND_ASPECT_LEAVES9a5 = REGISTER.register("mind_aspect_leaves", () -> new FlammableLeavesBlock(aspectLeaves));
	public static RegistryObject<Block> RAGE_ASPECT_LEAVES9a5 = REGISTER.register("rage_aspect_leaves", () -> new FlammableLeavesBlock(aspectLeaves));
	public static RegistryObject<Block> SPACE_ASPECT_LEAVES9a5 = REGISTER.register("space_aspect_leaves", () -> new FlammableLeavesBlock(aspectLeaves));
	public static RegistryObject<Block> TIME_ASPECT_LEAVES9a5 = REGISTER.register("time_aspect_leaves", () -> new FlammableLeavesBlock(aspectLeaves));
	public static RegistryObject<Block> VOID_ASPECT_LEAVES9a5 = REGISTER.register("void_aspect_leaves", () -> new FlammableLeavesBlock(aspectLeaves));
	public static RegistryObject<Block> BLOOD_ASPECT_SAPLING9a5 = REGISTER.register("blood_aspect_sapling", () -> new AspectSaplingBlock(sapling));
	public static RegistryObject<Block> BREATH_ASPECT_SAPLING9a5 = REGISTER.register("breath_aspect_sapling", () -> new AspectSaplingBlock(sapling));
	public static RegistryObject<Block> DOOM_ASPECT_SAPLING9a5 = REGISTER.register("doom_aspect_sapling", () -> new AspectSaplingBlock(sapling));
	public static RegistryObject<Block> HEART_ASPECT_SAPLING9a5 = REGISTER.register("heart_aspect_sapling", () -> new AspectSaplingBlock(sapling));
	public static RegistryObject<Block> HOPE_ASPECT_SAPLING9a5 = REGISTER.register("hope_aspect_sapling", () -> new AspectSaplingBlock(sapling));
	public static RegistryObject<Block> LIFE_ASPECT_SAPLING9a5 = REGISTER.register("life_aspect_sapling", () -> new AspectSaplingBlock(sapling));
	public static RegistryObject<Block> LIGHT_ASPECT_SAPLING9a5 = REGISTER.register("light_aspect_sapling", () -> new AspectSaplingBlock(sapling));
	public static RegistryObject<Block> MIND_ASPECT_SAPLING9a5 = REGISTER.register("mind_aspect_sapling", () -> new AspectSaplingBlock(sapling));
	public static RegistryObject<Block> RAGE_ASPECT_SAPLING9a5 = REGISTER.register("rage_aspect_sapling", () -> new AspectSaplingBlock(sapling));
	public static RegistryObject<Block> SPACE_ASPECT_SAPLING9a5 = REGISTER.register("space_aspect_sapling", () -> new AspectSaplingBlock(sapling));
	public static RegistryObject<Block> TIME_ASPECT_SAPLING9a5 = REGISTER.register("time_aspect_sapling", () -> new AspectSaplingBlock(sapling));
	public static RegistryObject<Block> VOID_ASPECT_SAPLING9a5 = REGISTER.register("void_aspect_sapling", () -> new AspectSaplingBlock(sapling));
	
	/*
	public static final Block BLOOD_ASPECT_LOG9a5 = getNull(), BREATH_ASPECT_LOG9a5 = getNull(), DOOM_ASPECT_LOG9a5 = getNull(), HEART_ASPECT_LOG9a5 = getNull();
	public static final Block HOPE_ASPECT_LOG9a5 = getNull(), LIFE_ASPECT_LOG9a5 = getNull(), LIGHT_ASPECT_LOG9a5 = getNull(), MIND_ASPECT_LOG9a5 = getNull();
	public static final Block RAGE_ASPECT_LOG9a5 = getNull(), SPACE_ASPECT_LOG9a5 = getNull(), TIME_ASPECT_LOG9a5 = getNull(), VOID_ASPECT_LOG9a5 = getNull();
	public static final Block BLOOD_ASPECT_PLANKS9a5 = getNull(), BREATH_ASPECT_PLANKS9a5 = getNull(), DOOM_ASPECT_PLANKS9a5 = getNull(), HEART_ASPECT_PLANKS9a5 = getNull();
	public static final Block HOPE_ASPECT_PLANKS9a5 = getNull(), LIFE_ASPECT_PLANKS9a5 = getNull(), LIGHT_ASPECT_PLANKS9a5 = getNull(), MIND_ASPECT_PLANKS9a5 = getNull();
	public static final Block RAGE_ASPECT_PLANKS9a5 = getNull(), SPACE_ASPECT_PLANKS9a5 = getNull(), TIME_ASPECT_PLANKS9a5 = getNull(), VOID_ASPECT_PLANKS9a5 = getNull();
	public static final Block BLOOD_ASPECT_LEAVES9a5 = getNull(), BREATH_ASPECT_LEAVES9a5 = getNull(), DOOM_ASPECT_LEAVES9a5 = getNull(), HEART_ASPECT_LEAVES9a5 = getNull();
	public static final Block HOPE_ASPECT_LEAVES9a5 = getNull(), LIFE_ASPECT_LEAVES9a5 = getNull(), LIGHT_ASPECT_LEAVES9a5 = getNull(), MIND_ASPECT_LEAVES9a5 = getNull();
	public static final Block RAGE_ASPECT_LEAVES9a5 = getNull(), SPACE_ASPECT_LEAVES9a5 = getNull(), TIME_ASPECT_LEAVES9a5 = getNull(), VOID_ASPECT_LEAVES9a5 = getNull();
	public static final Block BLOOD_ASPECT_SAPLING9a5 = getNull(), BREATH_ASPECT_SAPLING9a5 = getNull(), DOOM_ASPECT_SAPLING9a5 = getNull(), HEART_ASPECT_SAPLING9a5 = getNull();
	public static final Block HOPE_ASPECT_SAPLING9a5 = getNull(), LIFE_ASPECT_SAPLING9a5 = getNull(), LIGHT_ASPECT_SAPLING9a5 = getNull(), MIND_ASPECT_SAPLING9a5 = getNull();
	public static final Block RAGE_ASPECT_SAPLING9a5 = getNull(), SPACE_ASPECT_SAPLING9a5 = getNull(), TIME_ASPECT_SAPLING9a5 = getNull(), VOID_ASPECT_SAPLING9a5 = getNull();
	/**/
	
	//Land Plant Blocks
	public static RegistryObject<Block> GLOWING_MUSHROOM9a5 = REGISTER.register("glowing_mushroom", () -> new GlowingMushroomBlock(Block.Properties.of(Material.PLANT, MaterialColor.DIAMOND).noCollission().randomTicks().strength(0).sound(SoundType.GRASS).lightLevel(state -> 11)));
	public static RegistryObject<Block> DESERT_BUSH9a5 = REGISTER.register("desert_bush", () -> new DesertFloraBlock(Block.Properties.of(Material.PLANT).noCollission().strength(0).sound(SoundType.GRASS)));
	public static RegistryObject<Block> BLOOMING_CACTUS9a5 = REGISTER.register("blooming_cactus", () -> new DesertFloraBlock(Block.Properties.of(Material.PLANT).noCollission().strength(0).sound(SoundType.GRASS)));
	public static RegistryObject<Block> PETRIFIED_GRASS9a5 = REGISTER.register("petrified_grass", () -> new PetrifiedFloraBlock(Block.Properties.of(Material.STONE, DyeColor.GRAY).requiresCorrectToolForDrops().noCollission().strength(0).sound(SoundType.STONE)));
	public static RegistryObject<Block> PETRIFIED_POPPY9a5 = REGISTER.register("petrified_poppy", () -> new PetrifiedFloraBlock(Block.Properties.of(Material.STONE, DyeColor.GRAY).requiresCorrectToolForDrops().noCollission().strength(0).sound(SoundType.STONE)));
	//TODO StemGrownBlock
	public static RegistryObject<Block> STRAWBERRY9a5 = REGISTER.register("strawberry", () -> new StrawberryBlock(Block.Properties.of(Material.VEGETABLE, MaterialColor.COLOR_RED).strength(1.0F).sound(SoundType.WOOD)).setRegistryName("strawberry"));
	//TODO AttachedStemBlock
	public static RegistryObject<Block> ATTACHED_STRAWBERRY_STEM9a5 = REGISTER.register("attached_strawberry_stem", () -> new AttachedStemBlock(STRAWBERRY9a5, () -> MSItems.STRAWBERRY_CHUNK, Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.WOOD)));
	//TODO StemBlock
	public static RegistryObject<Block> STRAWBERRY_STEM9a5 = REGISTER.register("strawberry_stem", () -> new StemBlock(STRAWBERRY9a5, () -> MSItems.STRAWBERRY_CHUNK, Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.WOOD)));
	public static RegistryObject<Block> TALL_END_GRASS9a5 = REGISTER.register("tall_end_grass", () -> new TallEndGrassBlock(Block.Properties.of(Material.REPLACEABLE_PLANT, DyeColor.GREEN).noCollission().randomTicks().strength(0.1F).sound(SoundType.NETHER_WART)));
	public static RegistryObject<Block> GLOWFLOWER9a5 = REGISTER.register("glowflower", () -> new FlowerBlock(MobEffects.GLOWING, 20, Block.Properties.of(Material.PLANT, DyeColor.YELLOW).noCollission().strength(0).lightLevel(state -> 12).sound(SoundType.GRASS)));
	
	/*
	public static final Block GLOWING_MUSHROOM9a5 = getNull();
	public static final Block DESERT_BUSH9a5 = getNull();
	public static final Block BLOOMING_CACTUS9a5 = getNull();
	public static final Block PETRIFIED_GRASS9a5 = getNull();
	public static final Block PETRIFIED_POPPY9a5 = getNull();
	public static final StemGrownBlock STRAWBERRY9a5 = getNull();
	public static final AttachedStemBlock ATTACHED_STRAWBERRY_STEM9a5 = getNull();
	public static final StemBlock STRAWBERRY_STEM9a5 = getNull();
	public static final Block TALL_END_GRASS9a5 = getNull();
	public static final Block GLOWFLOWER9a5 = getNull();
	/**/
	
	//Special Land Blocks
	public static RegistryObject<Block> GLOWY_GOOP9a5 = REGISTER.register("glowy_goop", () -> new SlimeBlock(Block.Properties.of(Material.CLAY).strength(0.1F).sound(SoundType.SLIME_BLOCK).lightLevel(state -> 14)));
	public static RegistryObject<Block> COAGULATED_BLOOD9a5 = REGISTER.register("coagulated_blood", () -> new SlimeBlock(Block.Properties.of(Material.CLAY).strength(0.1F).sound(SoundType.SLIME_BLOCK)));
	public static RegistryObject<Block> PIPE9a5 = REGISTER.register("pipe", () -> new DirectionalCustomShapeBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(4.0F).sound(SoundType.METAL), MSBlockShapes.PIPE));
	public static RegistryObject<Block> PIPE_INTERSECTION9a5 = REGISTER.register("pipe_intersection", () -> new Block(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(4.0F).sound(SoundType.METAL)));
	public static RegistryObject<Block> PARCEL_PYXIS9a5 = REGISTER.register("parcel_pyxis", () -> new CustomShapeBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(4.0F), MSBlockShapes.PARCEL_PYXIS));
	public static RegistryObject<Block> PYXIS_LID9a5 = REGISTER.register("pyxis_lid", () -> new CustomShapeBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(1.0F), MSBlockShapes.PYXIS_LID));
	public static RegistryObject<Block> STONE_SLAB9a5 = REGISTER.register("stone_slab", () -> new StoneTabletBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(0.3F))); //TODO consider renaming, same thing as stone tablet
	public static RegistryObject<Block> NAKAGATOR_STATUE9a5 = REGISTER.register("nakagator_statue", () -> new CustomShapeBlock(Block.Properties.of(Material.STONE).strength(0.5F), MSBlockShapes.NAKAGATOR_STATUE));
	/*
	public static final Block GLOWY_GOOP9a5 = getNull();
	public static final Block COAGULATED_BLOOD9a5 = getNull();
	public static final Block PIPE9a5 = getNull();
	public static final Block PIPE_INTERSECTION9a5 = getNull();
	public static final Block PARCEL_PYXIS9a5 = getNull();
	public static final Block PYXIS_LID9a5 = getNull();
	public static final Block STONE_SLAB9a5 = getNull();
	public static final Block NAKAGATOR_STATUE9a5 = getNull();
	/**/
	
	//Structure Land Blocks
	public static RegistryObject<Block> BLACK_CASTLE_BRICK_STAIRS9a5 = REGISTER.register("black_castle_brick_stairs", () -> new StairBlock(() -> MSBlocks.BLACK_CASTLE_BRICKS.get().defaultBlockState(), blackChessBricks));
	public static RegistryObject<Block> DARK_GRAY_CASTLE_BRICK_STAIRS9a5 = REGISTER.register("dark_gray_castle_brick_stairs", () -> new StairBlock(() -> MSBlocks.DARK_GRAY_CASTLE_BRICKS.get().defaultBlockState(), darkGrayChessBricks));
	public static RegistryObject<Block> LIGHT_GRAY_CASTLE_BRICK_STAIRS9a5 = REGISTER.register("light_gray_castle_brick_stairs", () -> new StairBlock(() -> MSBlocks.LIGHT_GRAY_CASTLE_BRICKS.get().defaultBlockState(), lightGrayChessBricks));
	public static RegistryObject<Block> WHITE_CASTLE_BRICK_STAIRS9a5 = REGISTER.register("white_castle_brick_stairs", () -> new StairBlock(() -> MSBlocks.WHITE_CASTLE_BRICKS.get().defaultBlockState(), whiteChessBricks));
	public static RegistryObject<Block> COARSE_STONE_STAIRS9a5 = REGISTER.register("coarse_stone_stairs", () -> new StairBlock(() -> MSBlocks.COARSE_STONE9a5.get().defaultBlockState(), coarseStone));
	public static RegistryObject<Block> COARSE_STONE_BRICK_STAIRS9a5 = REGISTER.register("coarse_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.COARSE_STONE_BRICKS9a5.get().defaultBlockState(), coarseStone));
	public static RegistryObject<Block> SHADE_STAIRS9a5 = REGISTER.register("shade_stairs", () -> new StairBlock(() -> MSBlocks.SHADE_STONE9a5.get().defaultBlockState(), shadeStone));
	public static RegistryObject<Block> SHADE_BRICK_STAIRS9a5 = REGISTER.register("shade_brick_stairs", () -> new StairBlock(() -> MSBlocks.SHADE_BRICKS9a5.get().defaultBlockState(), shadeStone));
	public static RegistryObject<Block> FROST_TILE_STAIRS9a5 = REGISTER.register("frost_tile_stairs", () -> new StairBlock(() -> MSBlocks.FROST_TILE9a5.get().defaultBlockState(), frost));
	public static RegistryObject<Block> FROST_BRICK_STAIRS9a5 = REGISTER.register("frost_brick_stairs", () -> new StairBlock(() -> MSBlocks.FROST_BRICKS9a5.get().defaultBlockState(), frost));
	public static RegistryObject<Block> CAST_IRON_STAIRS9a5 = REGISTER.register("cast_iron_stairs", () -> new StairBlock(() -> MSBlocks.CAST_IRON9a5.get().defaultBlockState(), metal));
	public static RegistryObject<Block> BLACK_STONE_STAIRS9a5 = REGISTER.register("black_stone_stairs", () -> new StairBlock(() -> MSBlocks.BLACK_STONE9a5.get().defaultBlockState(), blackStone));
	public static RegistryObject<Block> BLACK_STONE_BRICK_STAIRS9a5 = REGISTER.register("black_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.BLACK_STONE_BRICKS9a5.get().defaultBlockState(), blackStone));
	public static RegistryObject<Block> FLOWERY_MOSSY_STONE_BRICK_STAIRS9a5 = REGISTER.register("mycelium_stairs", () -> new StairBlock(() -> MSBlocks.FLOWERY_MOSSY_STONE_BRICKS9a5.get().defaultBlockState(), floweryOrDecrepitStone));
	public static RegistryObject<Block> MYCELIUM_STAIRS9a5 = REGISTER.register("mycelium_brick_stairs", () -> new StairBlock(() -> MSBlocks.MYCELIUM_STONE9a5.get().defaultBlockState(), myceliumStone));
	public static RegistryObject<Block> MYCELIUM_BRICK_STAIRS9a5 = REGISTER.register("flowery_mossy_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.MYCELIUM_BRICKS9a5.get().defaultBlockState(), myceliumStone));
	public static RegistryObject<Block> CHALK_STAIRS9a5 = REGISTER.register("chalk_stairs", () -> new StairBlock(() -> MSBlocks.CHALK9a5.get().defaultBlockState(), chalk));
	public static RegistryObject<Block> CHALK_BRICK_STAIRS9a5 = REGISTER.register("chalk_brick_stairs", () -> new StairBlock(() -> MSBlocks.CHALK_BRICKS9a5.get().defaultBlockState(), chalk));
	public static RegistryObject<Block> PINK_STONE_STAIRS9a5 = REGISTER.register("pink_stone_stairs", () -> new StairBlock(() -> MSBlocks.PINK_STONE9a5.get().defaultBlockState(), pinkStone));
	public static RegistryObject<Block> PINK_STONE_BRICK_STAIRS9a5 = REGISTER.register("pink_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.PINK_STONE_BRICKS9a5.get().defaultBlockState(), pinkStone));
	public static RegistryObject<Block> BROWN_STONE_STAIRS9a5 = REGISTER.register("brown_stone_stairs", () -> new StairBlock(() -> MSBlocks.BROWN_STONE9a5.get().defaultBlockState(), brownStone));
	public static RegistryObject<Block> BROWN_STONE_BRICK_STAIRS9a5 = REGISTER.register("brown_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.BROWN_STONE_BRICKS9a5.get().defaultBlockState(), brownStone));
	public static RegistryObject<Block> GREEN_STONE_STAIRS9a5 = REGISTER.register("green_stone_stairs", () -> new StairBlock(() -> MSBlocks.GREEN_STONE9a5.get().defaultBlockState(), greenStone));
	public static RegistryObject<Block> GREEN_STONE_BRICK_STAIRS9a5 = REGISTER.register("green_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.GREEN_STONE_BRICKS9a5.get().defaultBlockState(), greenStone));
	public static RegistryObject<Block> RAINBOW_PLANKS_STAIRS9a5 = REGISTER.register("rainbow_planks_stairs", () -> new StairBlock(() -> MSBlocks.RAINBOW_PLANKS9a5.get().defaultBlockState(), rainbowPlanks));
	public static RegistryObject<Block> END_PLANKS_STAIRS9a5 = REGISTER.register("end_planks_stairs", () -> new StairBlock(() -> MSBlocks.END_PLANKS9a5.get().defaultBlockState(), endPlanks));
	public static RegistryObject<Block> DEAD_PLANKS_STAIRS9a5 = REGISTER.register("dead_planks_stairs", () -> new StairBlock(() -> MSBlocks.DEAD_PLANKS9a5.get().defaultBlockState(), deadPlanks));
	public static RegistryObject<Block> TREATED_PLANKS_STAIRS9a5 = REGISTER.register("treated_planks_stairs", () -> new StairBlock(() -> MSBlocks.TREATED_PLANKS9a5.get().defaultBlockState(), treatedPlanks));
	public static RegistryObject<Block> STEEP_GREEN_STONE_BRICK_STAIRS_BASE9a5 = REGISTER.register("steep_green_stone_brick_stairs_base", () -> new CustomShapeBlock(greenStone, MSBlockShapes.STEEP_STAIRS_BASE));
	public static RegistryObject<Block> STEEP_GREEN_STONE_BRICK_STAIRS_TOP9a5 = REGISTER.register("steep_green_stone_brick_stairs_top", () -> new CustomShapeBlock(greenStone, MSBlockShapes.STEEP_STAIRS_TOP));
	public static RegistryObject<Block> BLACK_CASTLE_BRICK_SLAB9a5 = REGISTER.register("black_castle_brick_slab", () -> new SlabBlock(blackChessBricks));
	public static RegistryObject<Block> DARK_GRAY_CASTLE_BRICK_SLAB9a5 = REGISTER.register("dark_gray_castle_brick_slab", () -> new SlabBlock(darkGrayChessBricks));
	public static RegistryObject<Block> LIGHT_GRAY_CASTLE_BRICK_SLAB9a5 = REGISTER.register("light_gray_castle_brick_slab", () -> new SlabBlock(lightGrayChessBricks));
	public static RegistryObject<Block> WHITE_CASTLE_BRICK_SLAB9a5 = REGISTER.register("white_castle_brick_slab", () -> new SlabBlock(whiteChessBricks));
	public static RegistryObject<Block> FLOWERY_MOSSY_STONE_BRICK_SLAB9a5 = REGISTER.register("flowery_mossy_stone_brick_slab", () -> new SlabBlock(floweryOrDecrepitStone));
	public static RegistryObject<Block> COARSE_STONE_SLAB9a5 = REGISTER.register("coarse_stone_slab", () -> new SlabBlock(coarseStone));
	public static RegistryObject<Block> COARSE_STONE_BRICK_SLAB9a5 = REGISTER.register("coarse_stone_brick_slab", () -> new SlabBlock(coarseStone));
	public static RegistryObject<Block> SHADE_SLAB9a5 = REGISTER.register("shade_slab", () -> new SlabBlock(shadeStone));
	public static RegistryObject<Block> SHADE_BRICK_SLAB9a5 = REGISTER.register("shade_brick_slab", () -> new SlabBlock(shadeStone));
	public static RegistryObject<Block> FROST_TILE_SLAB9a5 = REGISTER.register("frost_tile_slab", () -> new SlabBlock(frost));
	public static RegistryObject<Block> FROST_BRICK_SLAB9a5 = REGISTER.register("frost_brick_slab", () -> new SlabBlock(frost));
	public static RegistryObject<Block> BLACK_STONE_SLAB9a5 = REGISTER.register("black_stone_slab", () -> new SlabBlock(blackStone));
	public static RegistryObject<Block> BLACK_STONE_BRICK_SLAB9a5 = REGISTER.register("black_stone_brick_slab", () -> new SlabBlock(blackStone));
	public static RegistryObject<Block> MYCELIUM_SLAB9a5 = REGISTER.register("mycelium_slab", () -> new SlabBlock(myceliumStone));
	public static RegistryObject<Block> MYCELIUM_BRICK_SLAB9a5 = REGISTER.register("mycelium_brick_slab", () -> new SlabBlock(myceliumStone));
	public static RegistryObject<Block> CHALK_SLAB9a5 = REGISTER.register("chalk_slab", () -> new SlabBlock(chalk));
	public static RegistryObject<Block> CHALK_BRICK_SLAB9a5 = REGISTER.register("chalk_brick_slab", () -> new SlabBlock(chalk));
	public static RegistryObject<Block> PINK_STONE_SLAB9a5 = REGISTER.register("pink_stone_slab", () -> new SlabBlock(pinkStone));
	public static RegistryObject<Block> PINK_STONE_BRICK_SLAB9a5 = REGISTER.register("pink_stone_brick_slab", () -> new SlabBlock(pinkStone));
	public static RegistryObject<Block> BROWN_STONE_SLAB9a5 = REGISTER.register("brown_stone_slab", () -> new SlabBlock(brownStone));
	public static RegistryObject<Block> BROWN_STONE_BRICK_SLAB9a5 = REGISTER.register("brown_stone_brick_slab", () -> new SlabBlock(brownStone));
	public static RegistryObject<Block> GREEN_STONE_SLAB9a5 = REGISTER.register("green_stone_slab", () -> new SlabBlock(greenStone));
	public static RegistryObject<Block> GREEN_STONE_BRICK_SLAB9a5 = REGISTER.register("green_stone_brick_slab", () -> new SlabBlock(greenStone));
	public static RegistryObject<Block> RAINBOW_PLANKS_SLAB9a5 = REGISTER.register("rainbow_planks_slab", () -> new SlabBlock(rainbowPlanks));
	public static RegistryObject<Block> END_PLANKS_SLAB9a5 = REGISTER.register("end_planks_slab", () -> new SlabBlock(endPlanks));
	public static RegistryObject<Block> DEAD_PLANKS_SLAB9a5 = REGISTER.register("dead_planks_slab", () -> new SlabBlock(deadPlanks));
	public static RegistryObject<Block> TREATED_PLANKS_SLAB9a5 = REGISTER.register("treated_planks_slab", () -> new SlabBlock(treatedPlanks));
	/*
	public static final Block BLACK_CASTLE_BRICK_STAIRS9a5 = getNull(), DARK_GRAY_CASTLE_BRICK_STAIRS9a5 = getNull(), LIGHT_GRAY_CASTLE_BRICK_STAIRS9a5 = getNull(), WHITE_CASTLE_BRICK_STAIRS9a5 = getNull();
	public static final Block COARSE_STONE_STAIRS9a5 = getNull(), COARSE_STONE_BRICK_STAIRS9a5 = getNull(), SHADE_STAIRS9a5 = getNull(), SHADE_BRICK_STAIRS9a5 = getNull(), FROST_TILE_STAIRS9a5 = getNull(), FROST_BRICK_STAIRS9a5 = getNull(), CAST_IRON_STAIRS9a5 = getNull(), BLACK_STONE_STAIRS9a5 = getNull(), BLACK_STONE_BRICK_STAIRS9a5 = getNull();
	public static final Block FLOWERY_MOSSY_STONE_BRICK_STAIRS9a5 = getNull(), MYCELIUM_STAIRS9a5 = getNull(), MYCELIUM_BRICK_STAIRS9a5 = getNull(), CHALK_STAIRS9a5 = getNull(), CHALK_BRICK_STAIRS9a5 = getNull(), PINK_STONE_STAIRS9a5 = getNull(), PINK_STONE_BRICK_STAIRS9a5 = getNull(), BROWN_STONE_STAIRS9a5 = getNull(), BROWN_STONE_BRICK_STAIRS9a5 = getNull(), GREEN_STONE_STAIRS9a5 = getNull(), GREEN_STONE_BRICK_STAIRS9a5 = getNull();
	public static final Block RAINBOW_PLANKS_STAIRS9a5 = getNull(), END_PLANKS_STAIRS9a5 = getNull(), DEAD_PLANKS_STAIRS9a5 = getNull(), TREATED_PLANKS_STAIRS9a5 = getNull();
	public static final Block STEEP_GREEN_STONE_BRICK_STAIRS_BASE9a5 = getNull(), STEEP_GREEN_STONE_BRICK_STAIRS_TOP9a5 = getNull();
	public static final Block BLACK_CASTLE_BRICK_SLAB9a5 = getNull(), DARK_GRAY_CASTLE_BRICK_SLAB9a5 = getNull(), LIGHT_GRAY_CASTLE_BRICK_SLAB9a5 = getNull(), WHITE_CASTLE_BRICK_SLAB9a5 = getNull();
	public static final Block FLOWERY_MOSSY_STONE_BRICK_SLAB9a5 = getNull(), COARSE_STONE_SLAB9a5 = getNull(), COARSE_STONE_BRICK_SLAB9a5 = getNull(), SHADE_SLAB9a5 = getNull(), SHADE_BRICK_SLAB9a5 = getNull(), FROST_TILE_SLAB9a5 = getNull(), FROST_BRICK_SLAB9a5 = getNull(), BLACK_STONE_SLAB9a5 = getNull(), BLACK_STONE_BRICK_SLAB9a5 = getNull(), MYCELIUM_SLAB9a5 = getNull(), MYCELIUM_BRICK_SLAB9a5 = getNull(), CHALK_SLAB9a5 = getNull(), CHALK_BRICK_SLAB9a5 = getNull(), PINK_STONE_SLAB9a5 = getNull(), PINK_STONE_BRICK_SLAB9a5 = getNull(), BROWN_STONE_SLAB9a5 = getNull(), BROWN_STONE_BRICK_SLAB9a5 = getNull(), GREEN_STONE_SLAB9a5 = getNull(), GREEN_STONE_BRICK_SLAB9a5 = getNull();
	public static final Block RAINBOW_PLANKS_SLAB9a5 = getNull(), END_PLANKS_SLAB9a5 = getNull(), DEAD_PLANKS_SLAB9a5 = getNull(), TREATED_PLANKS_SLAB9a5 = getNull();
	/**/
	
	//Dungeon Functional Blocks
	static BlockBehaviour.Properties metalMechanicalBlock = Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL); //also used by some machines in addition to puzzle blocks
	static BlockBehaviour.Properties durableMetalMechanicalBlock = Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(6).sound(SoundType.METAL);
	public static RegistryObject<Block> TRAJECTORY_BLOCK9a5 = REGISTER.register("trajectory_block", () -> new TrajectoryBlock(metalMechanicalBlock));
	public static RegistryObject<Block> STAT_STORER9a5 = REGISTER.register("stat_storer", () -> new StatStorerBlock(metalMechanicalBlock));
	public static RegistryObject<Block> REMOTE_OBSERVER9a5 = REGISTER.register("remote_observer", () -> new RemoteObserverBlock(metalMechanicalBlock));
	public static RegistryObject<Block> WIRELESS_REDSTONE_TRANSMITTER9a5 = REGISTER.register("wireless_redstone_transmitter", () -> new WirelessRedstoneTransmitterBlock(metalMechanicalBlock));
	public static RegistryObject<Block> WIRELESS_REDSTONE_RECEIVER9a5 = REGISTER.register("wireless_redstone_receiver", () -> new WirelessRedstoneReceiverBlock(metalMechanicalBlock.randomTicks()));
	public static RegistryObject<Block> SOLID_SWITCH9a5 = REGISTER.register("solid_switch", () -> new SolidSwitchBlock(metalMechanicalBlock.lightLevel(state -> state.getValue(SolidSwitchBlock.POWERED) ? 15 : 0)));
	public static RegistryObject<Block> VARIABLE_SOLID_SWITCH9a5 = REGISTER.register("variable_solid_switch", () -> new VariableSolidSwitchBlock(metalMechanicalBlock.lightLevel(state -> state.getValue(VariableSolidSwitchBlock.POWER))));
	public static RegistryObject<Block> ONE_SECOND_INTERVAL_TIMED_SOLID_SWITCH9a5 = REGISTER.register("one_second_interval_timed_solid_switch", () -> new TimedSolidSwitchBlock(metalMechanicalBlock.lightLevel(state -> state.getValue(TimedSolidSwitchBlock.POWER)), 20));
	public static RegistryObject<Block> TWO_SECOND_INTERVAL_TIMED_SOLID_SWITCH9a5 = REGISTER.register("two_second_interval_timed_solid_switch", () -> new TimedSolidSwitchBlock(metalMechanicalBlock.lightLevel(state -> state.getValue(TimedSolidSwitchBlock.POWER)), 40));
	public static RegistryObject<Block> SUMMONER9a5 = REGISTER.register("summoner", () -> new SummonerBlock(metalMechanicalBlock));
	public static RegistryObject<Block> AREA_EFFECT_BLOCK9a5 = REGISTER.register("area_effect_block", () -> new AreaEffectBlock(durableMetalMechanicalBlock));
	public static RegistryObject<Block> PLATFORM_GENERATOR9a5 = REGISTER.register("platform_generator", () -> new PlatformGeneratorBlock(durableMetalMechanicalBlock));
	public static RegistryObject<Block> PLATFORM_BLOCK9a5 = REGISTER.register("platform_block", () -> new PlatformBlock(Block.Properties.of(Material.BARRIER).strength(0.2F).sound(SoundType.SCAFFOLDING).lightLevel(state -> 6).randomTicks().noOcclusion().isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never)));
	public static RegistryObject<Block> PLATFORM_RECEPTACLE9a5 = REGISTER.register("platform_receptacle", () -> new PlatformReceptacleBlock(metalMechanicalBlock));
	public static RegistryObject<Block> ITEM_MAGNET9a5 = REGISTER.register("item_magnet", () -> new ItemMagnetBlock(metalMechanicalBlock, new CustomVoxelShape(new double[]{0, 0, 0, 16, 1, 16}, new double[]{1, 1, 1, 15, 15, 15}, new double[]{0, 15, 0, 16, 16, 16})));
	public static RegistryObject<Block> REDSTONE_CLOCK9a5 = REGISTER.register("redstone_clock", () -> new RedstoneClockBlock(metalMechanicalBlock));
	public static RegistryObject<Block> ROTATOR9a5 = REGISTER.register("rotator", () -> new RotatorBlock(metalMechanicalBlock));
	public static RegistryObject<Block> TOGGLER9a5 = REGISTER.register("toggler", () -> new TogglerBlock(metalMechanicalBlock));
	public static RegistryObject<Block> REMOTE_COMPARATOR9a5 = REGISTER.register("remote_comparator", () -> new RemoteComparatorBlock(metalMechanicalBlock));
	public static RegistryObject<Block> STRUCTURE_CORE9a5 = REGISTER.register("structure_core", () -> new StructureCoreBlock(durableMetalMechanicalBlock));
	public static RegistryObject<Block> FALL_PAD9a5 = REGISTER.register("fall_pad", () -> new FallPadBlock(Block.Properties.of(Material.CLOTH_DECORATION).requiresCorrectToolForDrops().strength(1).sound(SoundType.WOOL)));
	public static RegistryObject<Block> FRAGILE_STONE9a5 = REGISTER.register("fragile_stone", () -> new FragileBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1).sound(SoundType.STONE)));
	public static RegistryObject<Block> SPIKES9a5 = REGISTER.register("spikes", () -> new SpikeBlock(Block.Properties.of(Material.METAL, MaterialColor.METAL).requiresCorrectToolForDrops().strength(2).sound(SoundType.METAL), MSBlockShapes.SPIKES));
	public static RegistryObject<Block> RETRACTABLE_SPIKES9a5 = REGISTER.register("retractable_spikes", () -> new RetractableSpikesBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(1).sound(SoundType.METAL)));
	public static RegistryObject<Block> BLOCK_PRESSURE_PLATE9a5 = REGISTER.register("block_pressure_plate", () -> new BlockPressurePlateBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1).sound(SoundType.STONE)));
	public static RegistryObject<Block> PUSHABLE_BLOCK9a5 = REGISTER.register("pushable_block", () -> new PushableBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1).sound(SoundType.GILDED_BLACKSTONE)));
	static BlockBehaviour.Properties logicGate = Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1).sound(SoundType.STONE); //TODO potentially change material/sound to metal considering textures
	public static RegistryObject<Block> AND_GATE_BLOCK9a5 = REGISTER.register("and_gate_block", () -> new LogicGateBlock(logicGate, LogicGateBlock.State.AND));
	public static RegistryObject<Block> OR_GATE_BLOCK9a5 = REGISTER.register("or_gate_block", () -> new LogicGateBlock(logicGate, LogicGateBlock.State.OR));
	public static RegistryObject<Block> XOR_GATE_BLOCK9a5 = REGISTER.register("xor_gate_block", () -> new LogicGateBlock(logicGate, LogicGateBlock.State.XOR));
	public static RegistryObject<Block> NAND_GATE_BLOCK9a5 = REGISTER.register("nand_gate_block", () -> new LogicGateBlock(logicGate, LogicGateBlock.State.NAND));
	public static RegistryObject<Block> NOR_GATE_BLOCK9a5 = REGISTER.register("nor_gate_block", () -> new LogicGateBlock(logicGate, LogicGateBlock.State.NOR));
	public static RegistryObject<Block> XNOR_GATE_BLOCK9a5 = REGISTER.register("xnor_gate_block", () -> new LogicGateBlock(logicGate, LogicGateBlock.State.XNOR));
	/*
	public static final Block TRAJECTORY_BLOCK9a5 = getNull();
	public static final Block STAT_STORER9a5 = getNull();
	public static final Block REMOTE_OBSERVER9a5 = getNull();
	public static final Block WIRELESS_REDSTONE_TRANSMITTER9a5 = getNull();
	public static final Block WIRELESS_REDSTONE_RECEIVER9a5 = getNull();
	public static final Block SOLID_SWITCH9a5 = getNull();
	public static final Block VARIABLE_SOLID_SWITCH9a5 = getNull();
	public static final Block ONE_SECOND_INTERVAL_TIMED_SOLID_SWITCH9a5 = getNull();
	public static final Block TWO_SECOND_INTERVAL_TIMED_SOLID_SWITCH9a5 = getNull();
	public static final Block SUMMONER9a5 = getNull();
	public static final Block AREA_EFFECT_BLOCK9a5 = getNull();
	public static final Block PLATFORM_GENERATOR9a5 = getNull();
	public static final Block PLATFORM_BLOCK9a5 = getNull();
	public static final Block PLATFORM_RECEPTACLE9a5 = getNull();
	public static final Block ITEM_MAGNET9a5 = getNull();
	public static final Block REDSTONE_CLOCK9a5 = getNull();
	public static final Block ROTATOR9a5 = getNull();
	public static final Block TOGGLER9a5 = getNull();
	public static final Block REMOTE_COMPARATOR9a5 = getNull();
	public static final Block STRUCTURE_CORE9a5 = getNull();
	public static final Block FALL_PAD9a5 = getNull();
	public static final Block FRAGILE_STONE9a5 = getNull();
	public static final Block RETRACTABLE_SPIKES9a5 = getNull();
	public static final Block BLOCK_PRESSURE_PLATE9a5 = getNull();
	public static final Block PUSHABLE_BLOCK9a5 = getNull();
	public static final Block AND_GATE_BLOCK9a5 = getNull();
	public static final Block OR_GATE_BLOCK9a5 = getNull();
	public static final Block XOR_GATE_BLOCK9a5 = getNull();
	public static final Block NAND_GATE_BLOCK9a5 = getNull();
	public static final Block NOR_GATE_BLOCK9a5 = getNull();
	public static final Block XNOR_GATE_BLOCK9a5 = getNull();
	/**/
	
	//Core Functional Land Blocks
	public static RegistryObject<Block> GATE9a5 = REGISTER.register("gate", () -> new GateBlock(Block.Properties.of(Material.PORTAL).noCollission().strength(-1.0F, 25.0F).sound(SoundType.GLASS).lightLevel(state -> 11).noDrops()));
	public static RegistryObject<Block> RETURN_NODE9a5 = REGISTER.register("return_node", () -> new ReturnNodeBlock(Block.Properties.of(Material.PORTAL).noCollission().strength(-1.0F, 10.0F).sound(SoundType.GLASS).lightLevel(state -> 11).noDrops()));
	/*
	public static final Block GATE9a5 = getNull();
	public static final Block RETURN_NODE9a5 = getNull();
	/**/
	
	//Misc Functional Land Blocks
	
	//Sburb Machines
	public static RegistryObject<Block> CRUXTRUDER_LID9a5 = REGISTER.register("cruxtruder_lid", () -> new Block(TEMPLATE));
	public static RegistryObject<Block> CRUXTRUDER9a5 = REGISTER.register("TEMPLATE", () -> new Block(TEMPLATE));
	CRUXTRUDER9a5.registerBlocks(registry);
	public static RegistryObject<Block> TOTEM_LATHE9a5 = REGISTER.register("TEMPLATE", () -> new Block(TEMPLATE));
	TOTEM_LATHE9a5.registerBlocks(registry);
	public static RegistryObject<Block> ALCHEMITER9a5 = REGISTER.register("TEMPLATE", () -> new Block(TEMPLATE));
	ALCHEMITER9a5.registerBlocks(registry);
	public static RegistryObject<Block> PUNCH_DESIGNIX9a5 = REGISTER.register("TEMPLATE", () -> new Block(TEMPLATE));
	PUNCH_DESIGNIX9a5.registerBlocks(registry);
	public static RegistryObject<Block> MINI_CRUXTRUDER9a5 = REGISTER.register("mini_cruxtruder", () -> new SmallMachineBlock<>(MSBlockShapes.SMALL_CRUXTRUDER.createRotatedShapes(), MSTileEntityTypes.MINI_CRUXTRUDER, Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F)));
	public static RegistryObject<Block> MINI_TOTEM_LATHE9a5 = REGISTER.register("mini_totem_lathe", () -> new SmallMachineBlock<>(MSBlockShapes.SMALL_TOTEM_LATHE.createRotatedShapes(), MSTileEntityTypes.MINI_TOTEM_LATHE, Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F)));
	public static RegistryObject<Block> MINI_ALCHEMITER9a5 = REGISTER.register("mini_alchemiter", () -> new MiniAlchemiterBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F)).setRegistryName("mini_alchemiter"));
	public static RegistryObject<Block> MINI_PUNCH_DESIGNIX9a5 = REGISTER.register("mini_punch_designix", () -> new SmallMachineBlock<>(MSBlockShapes.SMALL_PUNCH_DESIGNIX.createRotatedShapes(), MSTileEntityTypes.MINI_PUNCH_DESIGNIX, Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F)));
	public static RegistryObject<Block> HOLOPAD9a5 = REGISTER.register("holopad", () -> new HolopadBlock(Block.Properties.of(Material.METAL, MaterialColor.SNOW).requiresCorrectToolForDrops().strength(3.0F)));
	/*
	public static final Block CRUXTRUDER_LID9a5 = getNull();
	public static CruxtruderMultiblock CRUXTRUDER9a5 = new CruxtruderMultiblock(Minestuck.MOD_ID);
	public static TotemLatheMultiblock TOTEM_LATHE9a5 = new TotemLatheMultiblock(Minestuck.MOD_ID);
	public static AlchemiterMultiblock ALCHEMITER9a5 = new AlchemiterMultiblock(Minestuck.MOD_ID);
	public static PunchDesignixMultiblock PUNCH_DESIGNIX9a5 = new PunchDesignixMultiblock(Minestuck.MOD_ID);
	public static final Block MINI_CRUXTRUDER9a5 = getNull();
	public static final Block MINI_TOTEM_LATHE9a5 = getNull();
	public static final Block MINI_ALCHEMITER9a5 = getNull();
	public static final Block MINI_PUNCH_DESIGNIX9a5 = getNull();
	public static final Block HOLOPAD9a5 = getNull();
	/**/
	
	//Misc Machines
	public static RegistryObject<Block> COMPUTER9a5 = REGISTER.register("computer", () -> new ComputerBlock(ComputerBlock.COMPUTER_SHAPE, ComputerBlock.COMPUTER_SHAPE, Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(4.0F)));
	public static RegistryObject<Block> LAPTOP9a5 = REGISTER.register("laptop", () -> new ComputerBlock(ComputerBlock.LAPTOP_OPEN_SHAPE, ComputerBlock.LAPTOP_CLOSED_SHAPE, Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(4.0F)));
	public static RegistryObject<Block> CROCKERTOP9a5 = REGISTER.register("crockertop", () -> new ComputerBlock(ComputerBlock.LAPTOP_OPEN_SHAPE, ComputerBlock.LAPTOP_CLOSED_SHAPE, Block.Properties.of(Material.METAL, MaterialColor.COLOR_RED).requiresCorrectToolForDrops().strength(4.0F)));
	public static RegistryObject<Block> HUBTOP9a5 = REGISTER.register("hubtop", () -> new ComputerBlock(ComputerBlock.LAPTOP_OPEN_SHAPE, ComputerBlock.LAPTOP_CLOSED_SHAPE, Block.Properties.of(Material.METAL, MaterialColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(4.0F)));
	public static RegistryObject<Block> LUNCHTOP9a5 = REGISTER.register("lunchtop", () -> new ComputerBlock(ComputerBlock.LUNCHTOP_OPEN_SHAPE, ComputerBlock.LUNCHTOP_CLOSED_SHAPE, Block.Properties.of(Material.METAL, MaterialColor.COLOR_RED).requiresCorrectToolForDrops().strength(4.0F)));
	public static RegistryObject<Block> OLD_COMPUTER9a5 = REGISTER.register("old_computer", () -> new ComputerBlock(ComputerBlock.OLD_COMPUTER_SHAPE, ComputerBlock.OLD_COMPUTER_SHAPE, Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(4.0F)));
	public static RegistryObject<Block> TRANSPORTALIZER9a5 = REGISTER.register("transportalizer", () -> new TransportalizerBlock(metalMechanicalBlock));
	public static RegistryObject<Block> TRANS_PORTALIZER9a5 = REGISTER.register("trans_portalizer", () -> new TransportalizerBlock(metalMechanicalBlock));
	public static RegistryObject<Block> SENDIFICATOR9a5 = REGISTER.register("sendificator", () -> new SendificatorBlock(metalMechanicalBlock));
	public static RegistryObject<Block> GRIST_WIDGET9a5 = REGISTER.register("grist_widget", () -> new GristWidgetBlock(metalMechanicalBlock));
	public static RegistryObject<Block> URANIUM_COOKER9a5 = REGISTER.register("uranium_cooker", () -> new SmallMachineBlock<>(new CustomVoxelShape(new double[]{4, 0, 4, 12, 6, 12}).createRotatedShapes(), MSTileEntityTypes.URANIUM_COOKER, Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F)));
	/*
	public static final Block COMPUTER9a5 = getNull();
	public static final Block LAPTOP9a5 = getNull();
	public static final Block CROCKERTOP9a5 = getNull();
	public static final Block HUBTOP9a5 = getNull();
	public static final Block LUNCHTOP9a5 = getNull();
	public static final Block OLD_COMPUTER9a5 = getNull();
	public static final Block TRANSPORTALIZER9a5 = getNull();
	public static final Block TRANS_PORTALIZER9a5 = getNull();
	public static final Block SENDIFICATOR9a5 = getNull();
	public static final Block GRIST_WIDGET9a5 = getNull();
	public static final Block URANIUM_COOKER9a5 = getNull();
	/**/
	
	//Misc Core Objects
	public static RegistryObject<Block> CRUXITE_DOWEL9a5 = REGISTER.register("cruxite_dowel", () -> new CruxiteDowelBlock(Block.Properties.of(Material.GLASS).strength(0.0F)));
	public static RegistryObject<Block> LOTUS_TIME_CAPSULE_BLOCK9a5 = REGISTER.register("TEMPLATE", () -> new Block(TEMPLATE));
	LOTUS_TIME_CAPSULE_BLOCK9a5.registerBlocks(registry);
	/*
	public static final Block CRUXITE_DOWEL9a5 = getNull();
	public static LotusTimeCapsuleMultiblock LOTUS_TIME_CAPSULE_BLOCK9a5 = new LotusTimeCapsuleMultiblock(Minestuck.MOD_ID);
	/**/
	
	//Misc Alchemy Semi-Plants
	public static RegistryObject<Block> GOLD_SEEDS9a5 = REGISTER.register("gold_seeds", () -> new GoldSeedsBlock(Block.Properties.of(Material.PLANT).strength(0.1F).sound(SoundType.METAL).noCollission()));
	public static RegistryObject<Block> WOODEN_CACTUS9a5 = REGISTER.register("wooden_cactus", () -> new SpecialCactusBlock(Block.Properties.of(Material.WOOD).randomTicks().strength(1.0F, 2.5F).sound(SoundType.WOOD)));
	/*
	public static final Block GOLD_SEEDS9a5 = getNull();
	public static final Block WOODEN_CACTUS9a5 = getNull();
	/**/
	
	//Cakes
	static BlockBehaviour.Properties cake = Block.Properties.of(Material.CAKE).strength(0.5F).sound(SoundType.WOOL);
	public static RegistryObject<Block> APPLE_CAKE9a5 = REGISTER.register("apple_cake", () -> new SimpleCakeBlock(cake, 2, 0.5F, null));
	public static RegistryObject<Block> BLUE_CAKE9a5 = REGISTER.register("blue_cake", () -> new SimpleCakeBlock(cake, 2, 0.3F, player -> player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 150, 0))));
	public static RegistryObject<Block> COLD_CAKE9a5 = REGISTER.register("cold_cake", () -> new SimpleCakeBlock(cake, 2, 0.3F, player -> {player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 1));player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1));}));
	public static RegistryObject<Block> RED_CAKE9a5 = REGISTER.register("red_cake", () -> new SimpleCakeBlock(cake, 2, 0.1F, player -> player.heal(1)));
	public static RegistryObject<Block> HOT_CAKE9a5 = REGISTER.register("hot_cake", () -> new SimpleCakeBlock(cake, 2, 0.1F, player -> player.setSecondsOnFire(4)));
	public static RegistryObject<Block> REVERSE_CAKE9a5 = REGISTER.register("reverse_cake", () -> new SimpleCakeBlock(cake, 2, 0.1F, null));
	public static RegistryObject<Block> FUCHSIA_CAKE9a5 = REGISTER.register("fuchsia_cake", () -> new SimpleCakeBlock(cake, 3, 0.5F, player -> {player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 350, 1));player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0));}));
	public static RegistryObject<Block> NEGATIVE_CAKE9a5 = REGISTER.register("negative_cake", () -> new SimpleCakeBlock(cake, 2, 0.3F, player -> {player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 300, 0));player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 250, 0));}));
	public static RegistryObject<Block> CARROT_CAKE9a5 = REGISTER.register("carrot_cake", () -> new SimpleCakeBlock(cake, 2, 0.3F, player -> player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 200, 0))));
	/*
	public static final Block APPLE_CAKE9a5 = getNull();
	public static final Block BLUE_CAKE9a5 = getNull();
	public static final Block COLD_CAKE9a5 = getNull();
	public static final Block RED_CAKE9a5 = getNull();
	public static final Block HOT_CAKE9a5 = getNull();
	public static final Block REVERSE_CAKE9a5 = getNull();
	public static final Block FUCHSIA_CAKE9a5 = getNull();
	public static final Block NEGATIVE_CAKE9a5 = getNull();
	public static final Block CARROT_CAKE9a5 = getNull();
	/**/
	
	//Explosion and Redstone
	public static RegistryObject<Block> PRIMED_TNT9a5 = REGISTER.register("primed_tnt", () -> new SpecialTNTBlock(Block.Properties.of(Material.EXPLOSIVE).strength(0.0F).sound(SoundType.GRASS), true, false, false));
	public static RegistryObject<Block> UNSTABLE_TNT9a5 = REGISTER.register("unstable_tnt", () -> new SpecialTNTBlock(Block.Properties.of(Material.EXPLOSIVE).strength(0.0F).sound(SoundType.GRASS).randomTicks(), false, true, false));
	public static RegistryObject<Block> INSTANT_TNT9a5 = REGISTER.register("instant_tnt", () -> new SpecialTNTBlock(Block.Properties.of(Material.EXPLOSIVE).strength(0.0F).sound(SoundType.GRASS), false, false, true));
	public static RegistryObject<Block> WOODEN_EXPLOSIVE_BUTTON9a5 = REGISTER.register("wooden_explosive_button", () -> new SpecialButtonBlock(Block.Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.WOOD), true, true));
	public static RegistryObject<Block> STONE_EXPLOISVE_BUTTON9a5 = REGISTER.register("stone_explosive_button", () -> new SpecialButtonBlock(Block.Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.STONE), true, false));
	/*
	public static final Block PRIMED_TNT9a5 = getNull();
	public static final Block UNSTABLE_TNT9a5 = getNull();
	public static final Block INSTANT_TNT9a5 = getNull();
	public static final Block WOODEN_EXPLOSIVE_BUTTON9a5 = getNull();
	public static final Block STONE_EXPLOISVE_BUTTON9a5 = getNull();
	/**/
	
	//Misc Alchemy Objects
	static BlockBehaviour.Properties decor = Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(0.5F);
	public static RegistryObject<Block> BLENDER9a5 = REGISTER.register("blender", () -> new CustomShapeBlock(decor.sound(SoundType.METAL), MSBlockShapes.BLENDER));
	public static RegistryObject<Block> CHESSBOARD9a5 = REGISTER.register("chessboard", () -> new CustomShapeBlock(decor, MSBlockShapes.CHESSBOARD));
	public static RegistryObject<Block> MINI_FROG_STATUE9a5 = REGISTER.register("mini_frog_statue", () -> new CustomShapeBlock(decor, MSBlockShapes.FROG_STATUE));
	public static RegistryObject<Block> MINI_WIZARD_STATUE9a5 = REGISTER.register("mini_wizard_statue", () -> new CustomShapeBlock(decor, MSBlockShapes.WIZARD_STATUE));
	public static RegistryObject<Block> MINI_TYPHEUS_STATUE9a5 = REGISTER.register("mini_typheus_statue", () -> new CustomShapeBlock(decor, MSBlockShapes.DENIZEN_STATUE));
	public static RegistryObject<Block> CASSETTE_PLAYER9a5 = REGISTER.register("cassette_player", () -> new CassettePlayerBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(0.5F).sound(SoundType.METAL), MSBlockShapes.CASSETTE_PLAYER));
	public static RegistryObject<Block> GLOWYSTONE_DUST9a5 = REGISTER.register("glowystone_dust", () -> new GlowystoneWireBlock(Block.Properties.of(Material.DECORATION).strength(0.0F).lightLevel(state -> 16).noCollission()));
	
	public static RegistryObject<Block> OIL9a5 = REGISTER.register("TEMPLATE", () -> new FlowingModFluidBlock(MSFluids.OIL, new Vec3(0.0, 0.0, 0.0), 0.75f, Block.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
	public static RegistryObject<Block> BLOOD9a5 = REGISTER.register("TEMPLATE", () -> new FlowingModFluidBlock(MSFluids.BLOOD, new Vec3(0.8, 0.0, 0.0), 0.25f, Block.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
	public static RegistryObject<Block> BRAIN_JUICE9a5 = REGISTER.register("TEMPLATE", () -> new FlowingModFluidBlock(MSFluids.BRAIN_JUICE, new Vec3(0.55, 0.25, 0.7), 0.25f, Block.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
	public static RegistryObject<Block> WATER_COLORS9a5 = REGISTER.register("TEMPLATE", () -> new FlowingWaterColorsBlock(MSFluids.WATER_COLORS, 0.01f, Block.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()).setRegistryName("water_colors"));
	public static RegistryObject<Block> ENDER9a5 = REGISTER.register("TEMPLATE", () -> new FlowingModFluidBlock(MSFluids.ENDER, new Vec3(0, 0.35, 0.35), (Float.MAX_VALUE), Block.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
	public static RegistryObject<Block> LIGHT_WATER9a5 = REGISTER.register("TEMPLATE", () -> new FlowingModFluidBlock(MSFluids.LIGHT_WATER, new Vec3(0.2, 0.3, 1.0), 0.01f, Block.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
	
	/*
	public static final Block BLENDER9a5 = getNull();
	public static final Block CHESSBOARD9a5 = getNull();
	public static final Block MINI_FROG_STATUE9a5 = getNull();
	public static final Block MINI_WIZARD_STATUE9a5 = getNull();
	public static final Block MINI_TYPHEUS_STATUE9a5 = getNull();
	public static final Block GLOWYSTONE_DUST9a5 = getNull();
	public static final CassettePlayerBlock CASSETTE_PLAYER9a5 = getNull();
	
	public static final LiquidBlock OIL9a5 = getNull(), BLOOD9a5 = getNull(), BRAIN_JUICE9a5 = getNull();
	public static final LiquidBlock WATER_COLORS9a5 = getNull(), ENDER9a5 = getNull(), LIGHT_WATER9a5 = getNull();
	/**/
	
	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T getNull()
	{
		return null;
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		IForgeRegistry<Block> registry = event.getRegistry();
		
		//Skaia Blocks
		//registry.register(new Block(Block.Properties.of(Material.DIRT, MaterialColor.COLOR_BLACK).strength(0.5F).sound(SoundType.GRAVEL)).setRegistryName("black_chess_dirt"));
		//registry.register(new Block(Block.Properties.of(Material.DIRT, MaterialColor.SNOW).strength(0.5F).sound(SoundType.GRAVEL)).setRegistryName("white_chess_dirt"));
		//registry.register(new Block(Block.Properties.of(Material.DIRT, MaterialColor.COLOR_GRAY).strength(0.5F).sound(SoundType.GRAVEL)).setRegistryName("dark_gray_chess_dirt"));
		//registry.register(new Block(Block.Properties.of(Material.DIRT, MaterialColor.COLOR_LIGHT_GRAY).strength(0.5F).sound(SoundType.GRAVEL)).setRegistryName("light_gray_chess_dirt"));
		//registry.register(new SkaiaPortalBlock(Block.Properties.of(Material.PORTAL, MaterialColor.COLOR_CYAN).noCollission().lightLevel(state -> 11).strength(-1.0F, 3600000.0F).noDrops()).setRegistryName("skaia_portal"));
		//Block blackChessBricks = register(registry, new Block(Block.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_BLACK).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("black_castle_bricks"));
		//Block darkGrayChessBricks = register(registry, new Block(Block.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_GRAY).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("dark_gray_castle_bricks"));
		//Block lightGrayChessBricks = register(registry, new Block(Block.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("light_gray_castle_bricks"));
		//Block whiteChessBricks = register(registry, new Block(Block.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_WHITE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("white_castle_bricks"));
		//registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_BLACK).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("black_castle_brick_smooth"));
		//registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_GRAY).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("dark_gray_castle_brick_smooth"));
		//registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("light_gray_castle_brick_smooth"));
		//registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_WHITE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("white_castle_brick_smooth"));
		//registry.register(new MSDirectionalBlock(Block.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_BLACK).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("black_castle_brick_trim"));
		//registry.register(new MSDirectionalBlock(Block.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_GRAY).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("dark_gray_castle_brick_trim"));
		//registry.register(new MSDirectionalBlock(Block.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("light_gray_castle_brick_trim"));
		//registry.register(new MSDirectionalBlock(Block.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_WHITE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("white_castle_brick_trim"));
		//registry.register(new StainedGlassBlock(DyeColor.BLUE, Block.Properties.of(Material.GLASS, DyeColor.BLUE).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn(MSBlocks::never).isRedstoneConductor(MSBlocks::never).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never)).setRegistryName("checkered_stained_glass"));
		//registry.register(new StainedGlassBlock(DyeColor.BLUE, Block.Properties.of(Material.GLASS, DyeColor.BLUE).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn(MSBlocks::never).isRedstoneConductor(MSBlocks::never).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never)).setRegistryName("black_crown_stained_glass"));
		//registry.register(new StainedGlassBlock(DyeColor.BLUE, Block.Properties.of(Material.GLASS, DyeColor.BLUE).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn(MSBlocks::never).isRedstoneConductor(MSBlocks::never).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never)).setRegistryName("black_pawn_stained_glass"));
		//registry.register(new StainedGlassBlock(DyeColor.BLUE, Block.Properties.of(Material.GLASS, DyeColor.BLUE).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn(MSBlocks::never).isRedstoneConductor(MSBlocks::never).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never)).setRegistryName("white_crown_stained_glass"));
		//registry.register(new StainedGlassBlock(DyeColor.BLUE, Block.Properties.of(Material.GLASS, DyeColor.BLUE).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn(MSBlocks::never).isRedstoneConductor(MSBlocks::never).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never)).setRegistryName("white_pawn_stained_glass"));
		
		//Ores
		/*
		registry.register(new OreBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops(), UniformInt.of(2, 5)).setRegistryName("stone_cruxite_ore"));
		registry.register(new OreBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops(), UniformInt.of(2, 5)).setRegistryName("netherrack_cruxite_ore"));
		registry.register(new OreBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops(), UniformInt.of(2, 5)).setRegistryName("cobblestone_cruxite_ore"));
		registry.register(new OreBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops(), UniformInt.of(2, 5)).setRegistryName("sandstone_cruxite_ore"));
		registry.register(new OreBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops(), UniformInt.of(2, 5)).setRegistryName("red_sandstone_cruxite_ore"));
		registry.register(new OreBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops(), UniformInt.of(2, 5)).setRegistryName("end_stone_cruxite_ore"));
		registry.register(new OreBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops(), UniformInt.of(2, 5)).setRegistryName("shade_stone_cruxite_ore"));
		registry.register(new OreBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops(), UniformInt.of(2, 5)).setRegistryName("pink_stone_cruxite_ore"));
		registry.register(new OreBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().lightLevel(state -> 3), UniformInt.of(2, 5)).setRegistryName("stone_uranium_ore"));
		registry.register(new OreBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().lightLevel(state -> 3), UniformInt.of(2, 5)).setRegistryName("netherrack_uranium_ore"));
		registry.register(new OreBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().lightLevel(state -> 3), UniformInt.of(2, 5)).setRegistryName("cobblestone_uranium_ore"));
		registry.register(new OreBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().lightLevel(state -> 3), UniformInt.of(2, 5)).setRegistryName("sandstone_uranium_ore"));
		registry.register(new OreBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().lightLevel(state -> 3), UniformInt.of(2, 5)).setRegistryName("red_sandstone_uranium_ore"));
		registry.register(new OreBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().lightLevel(state -> 3), UniformInt.of(2, 5)).setRegistryName("end_stone_uranium_ore"));
		registry.register(new OreBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().lightLevel(state -> 3), UniformInt.of(2, 5)).setRegistryName("shade_stone_uranium_ore"));
		registry.register(new OreBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().lightLevel(state -> 3), UniformInt.of(2, 5)).setRegistryName("pink_stone_uranium_ore"));
		registry.register(new OreBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops(), UniformInt.of(0, 2)).setRegistryName("netherrack_coal_ore"));
		registry.register(new OreBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops(), UniformInt.of(0, 2)).setRegistryName("shade_stone_coal_ore"));
		registry.register(new OreBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops(), UniformInt.of(0, 2)).setRegistryName("pink_stone_coal_ore"));
		registry.register(new OreBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops()).setRegistryName("end_stone_iron_ore"));
		registry.register(new OreBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops()).setRegistryName("sandstone_iron_ore"));
		registry.register(new OreBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops()).setRegistryName("red_sandstone_iron_ore"));
		registry.register(new OreBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops()).setRegistryName("sandstone_gold_ore"));
		registry.register(new OreBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops()).setRegistryName("red_sandstone_gold_ore"));
		registry.register(new OreBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops()).setRegistryName("shade_stone_gold_ore"));
		registry.register(new OreBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops()).setRegistryName("pink_stone_gold_ore"));
		registry.register(new OreBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops(), UniformInt.of(1, 5)).setRegistryName("end_stone_redstone_ore"));
		registry.register(new OreBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops(), UniformInt.of(2, 5)).setRegistryName("stone_quartz_ore"));
		registry.register(new OreBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops(), UniformInt.of(2, 5)).setRegistryName("pink_stone_lapis_ore"));
		registry.register(new OreBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops(), UniformInt.of(3, 7)).setRegistryName("pink_stone_diamond_ore"));
		/**/
		
		/*registry.register(new Block(Block.Properties.of(Material.STONE, DyeColor.LIGHT_BLUE).strength(3.0F).requiresCorrectToolForDrops()).setRegistryName("cruxite_block"));
		registry.register(new Block(Block.Properties.of(Material.STONE, DyeColor.LIME).strength(3.0F).requiresCorrectToolForDrops().lightLevel(state -> 7)).setRegistryName("uranium_block"));
		registry.register(new Block(Block.Properties.of(Material.VEGETABLE, DyeColor.LIME).strength(1.0F).sound(SoundType.WOOD)).setRegistryName("generic_object"));
		
		registry.register(new Block(Block.Properties.of(Material.DIRT, MaterialColor.COLOR_BLUE).strength(0.5F).sound(SoundType.GRAVEL)).setRegistryName("blue_dirt"));
		registry.register(new Block(Block.Properties.of(Material.DIRT, MaterialColor.COLOR_LIGHT_GREEN).strength(0.5F).sound(SoundType.GRAVEL)).setRegistryName("thought_dirt"));
		Block coarseStone = register(registry, new Block(Block.Properties.of(Material.STONE, MaterialColor.STONE).requiresCorrectToolForDrops().strength(2.0F, 6.0F)).setRegistryName("coarse_stone"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.STONE).requiresCorrectToolForDrops().strength(2.0F, 6.0F)).setRegistryName("chiseled_coarse_stone"));
		Block coarseStoneBricks = register(registry, new Block(Block.Properties.of(Material.STONE, MaterialColor.STONE).requiresCorrectToolForDrops().strength(2.0F, 6.0F)).setRegistryName("coarse_stone_bricks"));
		registry.register(new MSDirectionalBlock(Block.Properties.of(Material.STONE, MaterialColor.STONE).requiresCorrectToolForDrops().strength(2.0F, 6.0F)).setRegistryName("coarse_stone_column"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.STONE).requiresCorrectToolForDrops().strength(2.0F, 6.0F)).setRegistryName("chiseled_coarse_stone_bricks"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.STONE).requiresCorrectToolForDrops().strength(2.0F, 6.0F)).setRegistryName("cracked_coarse_stone_bricks"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.STONE).requiresCorrectToolForDrops().strength(2.0F, 6.0F)).setRegistryName("mossy_coarse_stone"));
		Block shadeStone = register(registry, new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("shade_stone"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("smooth_shade_stone"));
		Block shadeBricks = register(registry, new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("shade_bricks"));
		registry.register(new MSDirectionalBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("shade_column"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("chiseled_shade_bricks"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("cracked_shade_bricks"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("mossy_shade_bricks"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("blood_shade_bricks"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("tar_shade_bricks"));
		Block frostTile = register(registry, new Block(Block.Properties.of(Material.STONE, MaterialColor.ICE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("frost_tile"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.ICE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("chiseled_frost_tile"));
		Block frostBricks = register(registry, new Block(Block.Properties.of(Material.STONE, MaterialColor.ICE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("frost_bricks"));
		registry.register(new MSDirectionalBlock(Block.Properties.of(Material.STONE, MaterialColor.ICE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("frost_column"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.ICE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("chiseled_frost_bricks"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.ICE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("cracked_frost_bricks"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.ICE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("flowery_frost_bricks"));
		Block castIron = register(registry, new Block(Block.Properties.of(Material.STONE, MaterialColor.METAL).requiresCorrectToolForDrops().strength(3.0F, 9.0F)).setRegistryName("cast_iron"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.METAL).requiresCorrectToolForDrops().strength(3.0F, 9.0F)).setRegistryName("chiseled_cast_iron"));
		registry.register(new MSDirectionalBlock(Block.Properties.of(Material.METAL, MaterialColor.METAL).requiresCorrectToolForDrops().strength(3.0F, 9.0F)).setRegistryName("steel_beam"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_MAGENTA).requiresCorrectToolForDrops().strength(1.5f, 6.0f)).setRegistryName("mycelium_cobblestone"));
		Block myceliumStone = register(registry, new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_MAGENTA).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("mycelium_stone"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_MAGENTA).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("polished_mycelium_stone"));
		Block myceliumBricks = register(registry, new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_MAGENTA).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("mycelium_bricks"));
		registry.register(new MSDirectionalBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_MAGENTA).requiresCorrectToolForDrops().strength(2.5F, 7.0F)).setRegistryName("mycelium_column"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_MAGENTA).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("chiseled_mycelium_bricks"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_MAGENTA).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("cracked_mycelium_bricks"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_MAGENTA).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("mossy_mycelium_bricks"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_MAGENTA).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("flowery_mycelium_bricks"));
		Block blackStone = register(registry, new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(2.5F, 6.0F)).setRegistryName("black_stone"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(2.5F, 6.0F)).setRegistryName("polished_black_stone"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(2.5f,6.0f)).setRegistryName("black_cobblestone"));
		Block blackStoneBricks = register(registry, new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(2.5f,6.0f)).setRegistryName("black_stone_bricks"));
		registry.register(new MSDirectionalBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(2.5f,6.0f)).setRegistryName("black_stone_column"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(2.5f,6.0f)).setRegistryName("cracked_black_stone_bricks"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(2.5f,6.0f)).setRegistryName("chiseled_black_stone_bricks"));
		registry.register(new SandBlock(0x181915, Block.Properties.of(Material.SAND, MaterialColor.COLOR_BLACK).strength(0.5F).sound(SoundType.SAND)).setRegistryName("black_sand"));
		registry.register(new Block(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("decrepit_stone_bricks"));
		registry.register(new Block(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("flowery_mossy_cobblestone"));
		registry.register(new Block(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("mossy_decrepit_stone_bricks"));
		Block floweryMossyStoneBricks = register(registry, new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(1.5f, 6.0f)).setRegistryName("flowery_mossy_stone_bricks"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.SAND).requiresCorrectToolForDrops().strength(3.0F, 9.0F)).setRegistryName("coarse_end_stone"));
		registry.register(new EndGrassBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(3.0F, 9.0F)).setRegistryName("end_grass"));
		Block chalk = register(registry, new Block(Block.Properties.of(Material.STONE, MaterialColor.SNOW).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("chalk"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.SNOW).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("polished_chalk"));
		Block chalkBricks = register(registry, new Block(Block.Properties.of(Material.STONE, MaterialColor.SNOW).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("chalk_bricks"));
		registry.register(new MSDirectionalBlock(Block.Properties.of(Material.STONE, MaterialColor.SNOW).requiresCorrectToolForDrops().strength(1.5f, 6.0f)).setRegistryName("chalk_column"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.SNOW).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("chiseled_chalk_bricks"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.SNOW).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("mossy_chalk_bricks"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.SNOW).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("flowery_chalk_bricks"));
		Block pinkStone = register(registry, new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_PINK).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("pink_stone"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_PINK).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("polished_pink_stone"));
		Block pinkStoneBricks = register(registry, new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_PINK).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("pink_stone_bricks"));
		registry.register(new MSDirectionalBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_PINK).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("pink_stone_column"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_PINK).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("chiseled_pink_stone_bricks"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_PINK).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("cracked_pink_stone_bricks"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_PINK).requiresCorrectToolForDrops().strength(1.5F, 6.0F)).setRegistryName("mossy_pink_stone_bricks"));
		Block brownStone = register(registry, new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BROWN).requiresCorrectToolForDrops().strength(2.5F, 7.0F)).setRegistryName("brown_stone"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BROWN).requiresCorrectToolForDrops().strength(2.5F, 7.0F)).setRegistryName("polished_brown_stone"));
		Block brownStoneBricks = register(registry, new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BROWN).requiresCorrectToolForDrops().strength(2.5F, 7.0F)).setRegistryName("brown_stone_bricks"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BROWN).requiresCorrectToolForDrops().strength(1.0F, 5.0F)).setRegistryName("cracked_brown_stone_bricks"));
		registry.register(new MSDirectionalBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BROWN).requiresCorrectToolForDrops().strength(2.5F, 7.0F)).setRegistryName("brown_stone_column"));
		Block greenStone = register(registry, new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(2.5F, 7.0F)).setRegistryName("green_stone"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(2.5F, 7.0F)).setRegistryName("polished_green_stone"));
		Block greenStoneBricks = register(registry, new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(2.5F, 7.0F)).setRegistryName("green_stone_bricks"));
		registry.register(new MSDirectionalBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(2.5F, 7.0F)).setRegistryName("green_stone_column"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(2.5F, 7.0F)).setRegistryName("chiseled_green_stone_bricks"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(2.5F, 7.0F)).setRegistryName("horizontal_green_stone_bricks"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(2.5F, 7.0F)).setRegistryName("vertical_green_stone_bricks"));
		registry.register(new MSDirectionalBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(2.5F, 7.0F)).setRegistryName("green_stone_brick_trim"));
		registry.register(new HieroglyphBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(2.5F, 7.0F)).setRegistryName("green_stone_brick_frog"));
		registry.register(new HieroglyphBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(2.5F, 7.0F)).setRegistryName("green_stone_brick_iguana_left"));
		registry.register(new HieroglyphBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(2.5F, 7.0F)).setRegistryName("green_stone_brick_iguana_right"));
		registry.register(new HieroglyphBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(2.5F, 7.0F)).setRegistryName("green_stone_brick_lotus"));
		registry.register(new HieroglyphBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(2.5F, 7.0F)).setRegistryName("green_stone_brick_nak_left"));
		registry.register(new HieroglyphBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(2.5F, 7.0F)).setRegistryName("green_stone_brick_nak_right"));
		registry.register(new HieroglyphBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(2.5F, 7.0F)).setRegistryName("green_stone_brick_salamander_left"));
		registry.register(new HieroglyphBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(2.5F, 7.0F)).setRegistryName("green_stone_brick_salamander_right"));
		registry.register(new HieroglyphBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(2.5F, 7.0F)).setRegistryName("green_stone_brick_skaia"));
		registry.register(new HieroglyphBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(2.5F, 7.0F)).setRegistryName("green_stone_brick_turtle"));
		registry.register(new MSDirectionalBlock(Block.Properties.of(Material.STONE, MaterialColor.SAND).requiresCorrectToolForDrops().strength(0.8F)).setRegistryName("sandstone_column"));
		registry.register(new MSDirectionalBlock(Block.Properties.of(Material.STONE, MaterialColor.SAND).requiresCorrectToolForDrops().strength(0.8F)).setRegistryName("chiseled_sandstone_column"));
		registry.register(new MSDirectionalBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(0.8F)).setRegistryName("red_sandstone_column"));
		registry.register(new MSDirectionalBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(0.8F)).setRegistryName("chiseled_red_sandstone_column"));
		registry.register(new Block(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).requiresCorrectToolForDrops().sound(SoundType.WOOD)).setRegistryName("uncarved_wood"));
		registry.register(new Block(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(1.0F).requiresCorrectToolForDrops().sound(SoundType.SCAFFOLDING)).setRegistryName("chipboard"));
		registry.register(new Block(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(0.4F).sound(SoundType.SAND)).setRegistryName("wood_shavings"));
		registry.register(new Block(Block.Properties.of(Material.GLASS, MaterialColor.COLOR_YELLOW).strength(0.5F).sound(SoundType.SNOW)).setRegistryName("dense_cloud"));
		registry.register(new Block(Block.Properties.of(Material.GLASS, MaterialColor.COLOR_LIGHT_GRAY).strength(0.5F).sound(SoundType.SNOW)).setRegistryName("bright_dense_cloud"));
		registry.register(new Block(Block.Properties.of(Material.SAND, MaterialColor.SNOW).strength(0.4F).sound(SoundType.SAND)).setRegistryName("sugar_cube"));
		/**/
		
		/*
		registry.register(new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.COLOR_LIGHT_BLUE).strength(2.0F).lightLevel(state -> 11).sound(SoundType.WOOD)).setRegistryName("glowing_log"));
		registry.register(new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.ICE).strength(2.0F).sound(SoundType.WOOD)).setRegistryName("frost_log"));
		registry.register(new FlammableLogBlock(Block.Properties.of(Material.WOOD).strength(2.0F).sound(SoundType.WOOD)).setRegistryName("rainbow_log"));
		registry.register(new DoubleLogBlock(1, 250, Block.Properties.of(Material.WOOD, MaterialColor.SAND).strength(2.0F).sound(SoundType.WOOD)).setRegistryName("end_log"));
		registry.register(new FlammableLogBlock(Block.Properties.of(Material.WOOD, logColors(MaterialColor.WOOD, MaterialColor.PODZOL)).strength(2.0F).sound(SoundType.WOOD)).setRegistryName("vine_log"));
		registry.register(new FlammableLogBlock(Block.Properties.of(Material.WOOD, logColors(MaterialColor.WOOD, MaterialColor.PODZOL)).strength(2.0F).sound(SoundType.WOOD)).setRegistryName("flowery_vine_log"));
		registry.register(new FlammableLogBlock(Block.Properties.of(Material.WOOD, logColors(MaterialColor.WOOD, MaterialColor.PODZOL)).strength(2.0F).sound(SoundType.WOOD)).setRegistryName("dead_log"));
		registry.register(new RotatedPillarBlock(Block.Properties.of(Material.WOOD, logColors(MaterialColor.WOOD, MaterialColor.PODZOL)).strength(2.0F).sound(SoundType.STONE)).setRegistryName("petrified_log"));
		registry.register(new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.COLOR_LIGHT_BLUE).strength(2.0F).lightLevel(state -> 11).sound(SoundType.WOOD)).setRegistryName("glowing_wood"));
		registry.register(new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.ICE).strength(2.0F).sound(SoundType.WOOD)).setRegistryName("frost_wood"));
		registry.register(new FlammableLogBlock(Block.Properties.of(Material.WOOD).strength(2.0F).sound(SoundType.WOOD)).setRegistryName("rainbow_wood"));
		registry.register(new FlammableLogBlock(1, 250, Block.Properties.of(Material.WOOD, MaterialColor.SAND).strength(2.0F).sound(SoundType.WOOD)).setRegistryName("end_wood"));
		registry.register(new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2.0F).sound(SoundType.WOOD)).setRegistryName("vine_wood"));
		registry.register(new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2.0F).sound(SoundType.WOOD)).setRegistryName("flowery_vine_wood"));
		registry.register(new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2.0F).sound(SoundType.WOOD)).setRegistryName("dead_wood"));
		registry.register(new RotatedPillarBlock(Block.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2.0F).sound(SoundType.STONE)).setRegistryName("petrified_wood"));
		registry.register(new FlammableBlock(5, 20, Block.Properties.of(Material.WOOD, MaterialColor.COLOR_LIGHT_BLUE).strength(2.0F, 3.0F).lightLevel(state -> 7).sound(SoundType.WOOD)).setRegistryName("glowing_planks"));
		registry.register(new FlammableBlock(5, 5, Block.Properties.of(Material.WOOD, MaterialColor.ICE).strength(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("frost_planks"));
		Block rainbowPlanks = register(registry, new FlammableBlock(5, 20, Block.Properties.of(Material.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("rainbow_planks"));
		Block endPlanks = register(registry, new FlammableBlock(1, 250, Block.Properties.of(Material.WOOD, MaterialColor.SAND).strength(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("end_planks"));
		Block deadPlanks = register(registry, new FlammableBlock(5, 5, Block.Properties.of(Material.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("dead_planks"));
		Block treatedPlanks = register(registry, new FlammableBlock(0, 0, Block.Properties.of(Material.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("treated_planks"));
		registry.register(new FlammableLeavesBlock(Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(MSBlocks::leafSpawns).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never)).setRegistryName("frost_leaves"));
		registry.register(new FlammableLeavesBlock(Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(MSBlocks::leafSpawns).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never)).setRegistryName("rainbow_leaves"));
		registry.register(new EndLeavesBlock(Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(MSBlocks::leafSpawns).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never)).setRegistryName("end_leaves"));
		registry.register(new RainbowSaplingBlock(Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)).setRegistryName("rainbow_sapling"));
		registry.register(new EndSaplingBlock(Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)).setRegistryName("end_sapling"));
		/**/
		
		/*
		registry.register(new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD)).setRegistryName("blood_aspect_log"));
		registry.register(new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD)).setRegistryName("breath_aspect_log"));
		registry.register(new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD)).setRegistryName("doom_aspect_log"));
		registry.register(new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD)).setRegistryName("heart_aspect_log"));
		registry.register(new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD)).setRegistryName("hope_aspect_log"));
		registry.register(new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD)).setRegistryName("life_aspect_log"));
		registry.register(new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD)).setRegistryName("light_aspect_log"));
		registry.register(new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD)).setRegistryName("mind_aspect_log"));
		registry.register(new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD)).setRegistryName("rage_aspect_log"));
		registry.register(new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD)).setRegistryName("space_aspect_log"));
		registry.register(new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD)).setRegistryName("time_aspect_log"));
		registry.register(new FlammableLogBlock(Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).sound(SoundType.WOOD)).setRegistryName("void_aspect_log"));
		registry.register(new FlammableBlock(5, 20, Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("blood_aspect_planks"));
		registry.register(new FlammableBlock(5, 20, Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("breath_aspect_planks"));
		registry.register(new FlammableBlock(5, 20, Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("doom_aspect_planks"));
		registry.register(new FlammableBlock(5, 20, Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("heart_aspect_planks"));
		registry.register(new FlammableBlock(5, 20, Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("hope_aspect_planks"));
		registry.register(new FlammableBlock(5, 20, Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("life_aspect_planks"));
		registry.register(new FlammableBlock(5, 20, Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("light_aspect_planks"));
		registry.register(new FlammableBlock(5, 20, Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("mind_aspect_planks"));
		registry.register(new FlammableBlock(5, 20, Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("rage_aspect_planks"));
		registry.register(new FlammableBlock(5, 20, Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("space_aspect_planks"));
		registry.register(new FlammableBlock(5, 20, Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("time_aspect_planks"));
		registry.register(new FlammableBlock(5, 20, Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("void_aspect_planks"));
		registry.register(new FlammableLeavesBlock(Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()).setRegistryName("blood_aspect_leaves"));
		registry.register(new FlammableLeavesBlock(Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()).setRegistryName("breath_aspect_leaves"));
		registry.register(new FlammableLeavesBlock(Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()).setRegistryName("doom_aspect_leaves"));
		registry.register(new FlammableLeavesBlock(Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()).setRegistryName("heart_aspect_leaves"));
		registry.register(new FlammableLeavesBlock(Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()).setRegistryName("hope_aspect_leaves"));
		registry.register(new FlammableLeavesBlock(Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()).setRegistryName("life_aspect_leaves"));
		registry.register(new FlammableLeavesBlock(Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()).setRegistryName("light_aspect_leaves"));
		registry.register(new FlammableLeavesBlock(Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()).setRegistryName("mind_aspect_leaves"));
		registry.register(new FlammableLeavesBlock(Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()).setRegistryName("rage_aspect_leaves"));
		registry.register(new FlammableLeavesBlock(Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()).setRegistryName("space_aspect_leaves"));
		registry.register(new FlammableLeavesBlock(Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()).setRegistryName("time_aspect_leaves"));
		registry.register(new FlammableLeavesBlock(Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()).setRegistryName("void_aspect_leaves"));
		registry.register(new AspectSaplingBlock(Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)).setRegistryName("blood_aspect_sapling"));
		registry.register(new AspectSaplingBlock(Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)).setRegistryName("breath_aspect_sapling"));
		registry.register(new AspectSaplingBlock(Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)).setRegistryName("doom_aspect_sapling"));
		registry.register(new AspectSaplingBlock(Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)).setRegistryName("heart_aspect_sapling"));
		registry.register(new AspectSaplingBlock(Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)).setRegistryName("hope_aspect_sapling"));
		registry.register(new AspectSaplingBlock(Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)).setRegistryName("life_aspect_sapling"));
		registry.register(new AspectSaplingBlock(Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)).setRegistryName("light_aspect_sapling"));
		registry.register(new AspectSaplingBlock(Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)).setRegistryName("mind_aspect_sapling"));
		registry.register(new AspectSaplingBlock(Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)).setRegistryName("rage_aspect_sapling"));
		registry.register(new AspectSaplingBlock(Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)).setRegistryName("space_aspect_sapling"));
		registry.register(new AspectSaplingBlock(Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)).setRegistryName("time_aspect_sapling"));
		registry.register(new AspectSaplingBlock(Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)).setRegistryName("void_aspect_sapling"));
		/**/
		
		/*
		registry.register(new GlowingMushroomBlock(Block.Properties.of(Material.PLANT, MaterialColor.DIAMOND).noCollission().randomTicks().strength(0).sound(SoundType.GRASS).lightLevel(state -> 11)).setRegistryName("glowing_mushroom"));
		registry.register(new DesertFloraBlock(Block.Properties.of(Material.PLANT).noCollission().strength(0).sound(SoundType.GRASS)).setRegistryName("desert_bush"));
		registry.register(new DesertFloraBlock(Block.Properties.of(Material.PLANT).noCollission().strength(0).sound(SoundType.GRASS)).setRegistryName("blooming_cactus"));
		registry.register(new PetrifiedFloraBlock(Block.Properties.of(Material.STONE, DyeColor.GRAY).requiresCorrectToolForDrops().noCollission().strength(0).sound(SoundType.STONE)).setRegistryName("petrified_grass"));
		registry.register(new PetrifiedFloraBlock(Block.Properties.of(Material.STONE, DyeColor.GRAY).requiresCorrectToolForDrops().noCollission().strength(0).sound(SoundType.STONE)).setRegistryName("petrified_poppy"));
		registry.register(new StrawberryBlock(Block.Properties.of(Material.VEGETABLE, MaterialColor.COLOR_RED).strength(1.0F).sound(SoundType.WOOD)).setRegistryName("strawberry"));
		registry.register(new AttachedStemBlock(STRAWBERRY9a5, () -> MSItems.STRAWBERRY_CHUNK, Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.WOOD)).setRegistryName("attached_strawberry_stem"));
		registry.register(new StemBlock(STRAWBERRY9a5, () -> MSItems.STRAWBERRY_CHUNK, Block.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.WOOD)).setRegistryName("strawberry_stem"));
		registry.register(new TallEndGrassBlock(Block.Properties.of(Material.REPLACEABLE_PLANT, DyeColor.GREEN).noCollission().randomTicks().strength(0.1F).sound(SoundType.NETHER_WART)).setRegistryName("tall_end_grass"));
		registry.register(new FlowerBlock(MobEffects.GLOWING, 20, Block.Properties.of(Material.PLANT, DyeColor.YELLOW).noCollission().strength(0).lightLevel(state -> 12).sound(SoundType.GRASS)).setRegistryName("glowflower"));
		/**/
		
		/*
		registry.register(new SlimeBlock(Block.Properties.of(Material.CLAY).strength(0.1F).sound(SoundType.SLIME_BLOCK).lightLevel(state -> 14)).setRegistryName("glowy_goop"));
		registry.register(new SlimeBlock(Block.Properties.of(Material.CLAY).strength(0.1F).sound(SoundType.SLIME_BLOCK)).setRegistryName("coagulated_blood"));
		registry.register(new DirectionalCustomShapeBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(4.0F).sound(SoundType.METAL), MSBlockShapes.PIPE).setRegistryName("pipe"));
		registry.register(new Block(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(4.0F).sound(SoundType.METAL)).setRegistryName("pipe_intersection"));
		registry.register(new StoneTabletBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(0.3F)).setRegistryName("stone_slab")); //same thing as stone tablet
		registry.register(new CustomShapeBlock(Block.Properties.of(Material.STONE).strength(0.5F), MSBlockShapes.NAKAGATOR_STATUE).setRegistryName("nakagator_statue"));
		/**/
		
		/*
		registry.register(new StairBlock(() -> MSBlocks.BLACK_CASTLE_BRICKS.get().defaultBlockState(), blackChessBricks).setRegistryName("black_castle_brick_stairs"));
		registry.register(new StairBlock(() -> MSBlocks.DARK_GRAY_CASTLE_BRICKS.get().defaultBlockState(), darkGrayChessBricks).setRegistryName("dark_gray_castle_brick_stairs"));
		registry.register(new StairBlock(() -> MSBlocks.LIGHT_GRAY_CASTLE_BRICKS.get().defaultBlockState(), lightGrayChessBricks).setRegistryName("light_gray_castle_brick_stairs"));
		registry.register(new StairBlock(() -> MSBlocks.WHITE_CASTLE_BRICKS.get().defaultBlockState(), whiteChessBricks).setRegistryName("white_castle_brick_stairs"));
		registry.register(new StairBlock(() -> MSBlocks.COARSE_STONE9a5.defaultBlockState(), Block.Properties.copy(coarseStone)).setRegistryName("coarse_stone_stairs"));
		registry.register(new StairBlock(() -> MSBlocks.COARSE_STONE_BRICKS9a5.defaultBlockState(), Block.Properties.copy(coarseStoneBricks)).setRegistryName("coarse_stone_brick_stairs"));
		registry.register(new StairBlock(() -> MSBlocks.SHADE_STONE9a5.defaultBlockState(), Block.Properties.copy(shadeStone)).setRegistryName("shade_stairs"));;
		registry.register(new StairBlock(() -> MSBlocks.SHADE_BRICKS9a5.defaultBlockState(), Block.Properties.copy(shadeBricks)).setRegistryName("shade_brick_stairs"));
		registry.register(new StairBlock(() -> MSBlocks.FROST_TILE9a5.defaultBlockState(), Block.Properties.copy(frostTile)).setRegistryName("frost_tile_stairs"));
		registry.register(new StairBlock(() -> MSBlocks.FROST_BRICKS9a5.defaultBlockState(), Block.Properties.copy(frostBricks)).setRegistryName("frost_brick_stairs"));
		registry.register(new StairBlock(() -> MSBlocks.CAST_IRON9a5.defaultBlockState(), Block.Properties.copy(castIron)).setRegistryName("cast_iron_stairs"));
		registry.register(new StairBlock(() -> MSBlocks.BLACK_STONE9a5.defaultBlockState(), Block.Properties.copy(blackStone)).setRegistryName("black_stone_stairs"));
		registry.register(new StairBlock(() -> MSBlocks.BLACK_STONE_BRICKS9a5.defaultBlockState(), Block.Properties.copy(blackStoneBricks)).setRegistryName("black_stone_brick_stairs"));
		registry.register(new StairBlock(() -> MSBlocks.MYCELIUM_STONE9a5.defaultBlockState(), Block.Properties.copy(myceliumStone)).setRegistryName("mycelium_stairs"));
		registry.register(new StairBlock(() -> MSBlocks.MYCELIUM_BRICKS9a5.defaultBlockState(), Block.Properties.copy(myceliumBricks)).setRegistryName("mycelium_brick_stairs"));
		registry.register(new StairBlock(() -> MSBlocks.FLOWERY_MOSSY_STONE_BRICKS9a5.defaultBlockState(), Block.Properties.copy(floweryMossyStoneBricks)).setRegistryName("flowery_mossy_stone_brick_stairs"));
		registry.register(new StairBlock(() -> MSBlocks.CHALK9a5.defaultBlockState(), Block.Properties.copy(chalk)).setRegistryName("chalk_stairs"));
		registry.register(new StairBlock(() -> MSBlocks.CHALK_BRICKS9a5.defaultBlockState(), Block.Properties.copy(chalkBricks)).setRegistryName("chalk_brick_stairs"));
		registry.register(new StairBlock(() -> MSBlocks.PINK_STONE9a5.defaultBlockState(), Block.Properties.copy(pinkStone)).setRegistryName("pink_stone_stairs"));
		registry.register(new StairBlock(() -> MSBlocks.PINK_STONE_BRICKS9a5.defaultBlockState(), Block.Properties.copy(pinkStoneBricks)).setRegistryName("pink_stone_brick_stairs"));
		registry.register(new StairBlock(() -> MSBlocks.BROWN_STONE9a5.defaultBlockState(), Block.Properties.copy(brownStone)).setRegistryName("brown_stone_stairs"));
		registry.register(new StairBlock(() -> MSBlocks.BROWN_STONE_BRICKS9a5.defaultBlockState(), Block.Properties.copy(brownStoneBricks)).setRegistryName("brown_stone_brick_stairs"));
		registry.register(new StairBlock(() -> MSBlocks.GREEN_STONE9a5.defaultBlockState(), Block.Properties.copy(greenStone)).setRegistryName("green_stone_stairs"));
		registry.register(new StairBlock(() -> MSBlocks.GREEN_STONE_BRICKS9a5.defaultBlockState(), Block.Properties.copy(greenStoneBricks)).setRegistryName("green_stone_brick_stairs"));
		registry.register(new StairBlock(() -> MSBlocks.RAINBOW_PLANKS9a5.defaultBlockState(), Block.Properties.copy(rainbowPlanks)).setRegistryName("rainbow_planks_stairs"));
		registry.register(new StairBlock(() -> MSBlocks.END_PLANKS9a5.defaultBlockState(), Block.Properties.copy(endPlanks)).setRegistryName("end_planks_stairs"));
		registry.register(new StairBlock(() -> MSBlocks.DEAD_PLANKS9a5.defaultBlockState(), Block.Properties.copy(deadPlanks)).setRegistryName("dead_planks_stairs"));
		registry.register(new StairBlock(() -> MSBlocks.TREATED_PLANKS9a5.defaultBlockState(), Block.Properties.copy(treatedPlanks)).setRegistryName("treated_planks_stairs"));
		registry.register(new CustomShapeBlock(Block.Properties.copy(greenStoneBricks), MSBlockShapes.STEEP_STAIRS_BASE).setRegistryName("steep_green_stone_brick_stairs_base"));
		registry.register(new CustomShapeBlock(Block.Properties.copy(greenStoneBricks), MSBlockShapes.STEEP_STAIRS_TOP).setRegistryName("steep_green_stone_brick_stairs_top"));
		registry.register(new SlabBlock(blackChessBricks).setRegistryName("black_castle_brick_slab"));
		registry.register(new SlabBlock(darkGrayChessBricks).setRegistryName("dark_gray_castle_brick_slab"));
		registry.register(new SlabBlock(lightGrayChessBricks).setRegistryName("light_gray_castle_brick_slab"));
		registry.register(new SlabBlock(whiteChessBricks).setRegistryName("white_castle_brick_slab"));
		registry.register(new SlabBlock(Block.Properties.copy(chalk)).setRegistryName("chalk_slab"));
		registry.register(new SlabBlock(Block.Properties.copy(chalkBricks)).setRegistryName("chalk_brick_slab"));
		registry.register(new SlabBlock(Block.Properties.copy(pinkStone)).setRegistryName("pink_stone_slab"));
		registry.register(new SlabBlock(Block.Properties.copy(pinkStoneBricks)).setRegistryName("pink_stone_brick_slab"));
		registry.register(new SlabBlock(Block.Properties.copy(blackStone)).setRegistryName("black_stone_slab"));
		registry.register(new SlabBlock(Block.Properties.copy(blackStoneBricks)).setRegistryName("black_stone_brick_slab"));
		registry.register(new SlabBlock(Block.Properties.copy(myceliumStone)).setRegistryName("mycelium_slab"));
		registry.register(new SlabBlock(Block.Properties.copy(myceliumBricks)).setRegistryName("mycelium_brick_slab"));
		registry.register(new SlabBlock(Block.Properties.copy(floweryMossyStoneBricks)).setRegistryName("flowery_mossy_stone_brick_slab"));
		registry.register(new SlabBlock(Block.Properties.copy(frostTile)).setRegistryName("frost_tile_slab"));
		registry.register(new SlabBlock(Block.Properties.copy(frostBricks)).setRegistryName("frost_brick_slab"));
		registry.register(new SlabBlock(Block.Properties.copy(shadeStone)).setRegistryName("shade_slab"));
		registry.register(new SlabBlock(Block.Properties.copy(shadeBricks)).setRegistryName("shade_brick_slab"));
		registry.register(new SlabBlock(Block.Properties.copy(coarseStone)).setRegistryName("coarse_stone_slab"));
		registry.register(new SlabBlock(Block.Properties.copy(coarseStoneBricks)).setRegistryName("coarse_stone_brick_slab"));
		registry.register(new SlabBlock(Block.Properties.copy(brownStone)).setRegistryName("brown_stone_slab"));
		registry.register(new SlabBlock(Block.Properties.copy(brownStoneBricks)).setRegistryName("brown_stone_brick_slab"));
		registry.register(new SlabBlock(Block.Properties.copy(greenStone)).setRegistryName("green_stone_slab"));
		registry.register(new SlabBlock(Block.Properties.copy(greenStoneBricks)).setRegistryName("green_stone_brick_slab"));
		registry.register(new SlabBlock(Block.Properties.copy(rainbowPlanks)).setRegistryName("rainbow_planks_slab"));
		registry.register(new SlabBlock(Block.Properties.copy(endPlanks)).setRegistryName("end_planks_slab"));
		registry.register(new SlabBlock(Block.Properties.copy(deadPlanks)).setRegistryName("dead_planks_slab"));
		registry.register(new SlabBlock(Block.Properties.copy(treatedPlanks)).setRegistryName("treated_planks_slab"));
		/**/
		
		/*
		registry.register(new TrajectoryBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)).setRegistryName("trajectory_block"));
		registry.register(new StatStorerBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)).setRegistryName("stat_storer"));
		registry.register(new RemoteObserverBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)).setRegistryName("remote_observer"));
		registry.register(new WirelessRedstoneTransmitterBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)).setRegistryName("wireless_redstone_transmitter"));
		registry.register(new WirelessRedstoneReceiverBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).randomTicks().sound(SoundType.METAL)).setRegistryName("wireless_redstone_receiver"));
		registry.register(new SolidSwitchBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL).lightLevel(state -> state.getValue(SolidSwitchBlock.POWERED) ? 15 : 0)).setRegistryName("solid_switch"));
		registry.register(new VariableSolidSwitchBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL).lightLevel(state -> state.getValue(VariableSolidSwitchBlock.POWER))).setRegistryName("variable_solid_switch"));
		registry.register(new TimedSolidSwitchBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL).lightLevel(state -> state.getValue(TimedSolidSwitchBlock.POWER)), 20).setRegistryName("one_second_interval_timed_solid_switch"));
		registry.register(new TimedSolidSwitchBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL).lightLevel(state -> state.getValue(TimedSolidSwitchBlock.POWER)), 40).setRegistryName("two_second_interval_timed_solid_switch"));
		registry.register(new SummonerBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)).setRegistryName("summoner"));
		registry.register(new AreaEffectBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(6).sound(SoundType.METAL)).setRegistryName("area_effect_block"));
		registry.register(new PlatformGeneratorBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(6).sound(SoundType.METAL)).setRegistryName("platform_generator"));
		registry.register(new PlatformBlock(Block.Properties.of(Material.BARRIER).strength(0.2F).sound(SoundType.SCAFFOLDING).lightLevel(state -> 6).randomTicks().noOcclusion().isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never)).setRegistryName("platform_block"));
		registry.register(new PlatformReceptacleBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)).setRegistryName("platform_receptacle"));
		registry.register(new ItemMagnetBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL), new CustomVoxelShape(new double[]{0, 0, 0, 16, 1, 16}, new double[]{1, 1, 1, 15, 15, 15}, new double[]{0, 15, 0, 16, 16, 16})).setRegistryName("item_magnet"));
		registry.register(new RedstoneClockBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)).setRegistryName("redstone_clock"));
		registry.register(new RotatorBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)).setRegistryName("rotator"));
		registry.register(new TogglerBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)).setRegistryName("toggler"));
		registry.register(new RemoteComparatorBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)).setRegistryName("remote_comparator"));
		registry.register(new StructureCoreBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(6).sound(SoundType.METAL)).setRegistryName("structure_core"));
		registry.register(new FallPadBlock(Block.Properties.of(Material.CLOTH_DECORATION).requiresCorrectToolForDrops().strength(1).sound(SoundType.WOOL)).setRegistryName("fall_pad"));
		registry.register(new FragileBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1).sound(SoundType.STONE)).setRegistryName("fragile_stone"));
		registry.register(new SpikeBlock(Block.Properties.of(Material.METAL, MaterialColor.METAL).requiresCorrectToolForDrops().strength(2).sound(SoundType.METAL), MSBlockShapes.SPIKES).setRegistryName("spikes"));
		registry.register(new RetractableSpikesBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(1).sound(SoundType.METAL)).setRegistryName("retractable_spikes"));
		registry.register(new BlockPressurePlateBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1).sound(SoundType.STONE)).setRegistryName("block_pressure_plate"));
		registry.register(new PushableBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1).sound(SoundType.GILDED_BLACKSTONE)).setRegistryName("pushable_block"));
		registry.register(new LogicGateBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1).sound(SoundType.STONE), LogicGateBlock.State.AND).setRegistryName("and_gate_block"));
		registry.register(new LogicGateBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1).sound(SoundType.STONE), LogicGateBlock.State.OR).setRegistryName("or_gate_block"));
		registry.register(new LogicGateBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1).sound(SoundType.STONE), LogicGateBlock.State.XOR).setRegistryName("xor_gate_block"));
		registry.register(new LogicGateBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1).sound(SoundType.STONE), LogicGateBlock.State.NAND).setRegistryName("nand_gate_block"));
		registry.register(new LogicGateBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1).sound(SoundType.STONE), LogicGateBlock.State.NOR).setRegistryName("nor_gate_block"));
		registry.register(new LogicGateBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1).sound(SoundType.STONE), LogicGateBlock.State.XNOR).setRegistryName("xnor_gate_block"));
		
		registry.register(new GateBlock(Block.Properties.of(Material.PORTAL).noCollission().strength(-1.0F, 25.0F).sound(SoundType.GLASS).lightLevel(state -> 11).noDrops()).setRegistryName("gate"));
		registry.register(new ReturnNodeBlock(Block.Properties.of(Material.PORTAL).noCollission().strength(-1.0F, 10.0F).sound(SoundType.GLASS).lightLevel(state -> 11).noDrops()).setRegistryName("return_node"));
		
		CRUXTRUDER9a5.registerBlocks(registry);
		registry.register(new CruxtruderLidBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(1.0F)).setRegistryName("cruxtruder_lid"));
		TOTEM_LATHE9a5.registerBlocks(registry);
		ALCHEMITER9a5.registerBlocks(registry);
		PUNCH_DESIGNIX9a5.registerBlocks(registry);
		registry.register(new SmallMachineBlock<>(MSBlockShapes.SMALL_CRUXTRUDER.createRotatedShapes(), MSTileEntityTypes.MINI_CRUXTRUDER, Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F)).setRegistryName("mini_cruxtruder"));
		registry.register(new SmallMachineBlock<>(MSBlockShapes.SMALL_TOTEM_LATHE.createRotatedShapes(), MSTileEntityTypes.MINI_TOTEM_LATHE, Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F)).setRegistryName("mini_totem_lathe"));
		registry.register(new MiniAlchemiterBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F)).setRegistryName("mini_alchemiter"));
		registry.register(new SmallMachineBlock<>(MSBlockShapes.SMALL_PUNCH_DESIGNIX.createRotatedShapes(), MSTileEntityTypes.MINI_PUNCH_DESIGNIX, Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F)).setRegistryName("mini_punch_designix"));
		registry.register(new HolopadBlock(Block.Properties.of(Material.METAL, MaterialColor.SNOW).requiresCorrectToolForDrops().strength(3.0F)).setRegistryName("holopad"));
		
		registry.register(new ComputerBlock(ComputerBlock.COMPUTER_SHAPE, ComputerBlock.COMPUTER_SHAPE, Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(4.0F)).setRegistryName("computer"));
		registry.register(new ComputerBlock(ComputerBlock.LAPTOP_OPEN_SHAPE, ComputerBlock.LAPTOP_CLOSED_SHAPE, Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(4.0F)).setRegistryName("laptop"));
		registry.register(new ComputerBlock(ComputerBlock.LAPTOP_OPEN_SHAPE, ComputerBlock.LAPTOP_CLOSED_SHAPE, Block.Properties.of(Material.METAL, MaterialColor.COLOR_RED).requiresCorrectToolForDrops().strength(4.0F)).setRegistryName("crockertop"));
		registry.register(new ComputerBlock(ComputerBlock.LAPTOP_OPEN_SHAPE, ComputerBlock.LAPTOP_CLOSED_SHAPE, Block.Properties.of(Material.METAL, MaterialColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(4.0F)).setRegistryName("hubtop"));
		registry.register(new ComputerBlock(ComputerBlock.LUNCHTOP_OPEN_SHAPE, ComputerBlock.LUNCHTOP_CLOSED_SHAPE, Block.Properties.of(Material.METAL, MaterialColor.COLOR_RED).requiresCorrectToolForDrops().strength(4.0F)).setRegistryName("lunchtop"));
		registry.register(new ComputerBlock(ComputerBlock.OLD_COMPUTER_SHAPE, ComputerBlock.OLD_COMPUTER_SHAPE, Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(4.0F)).setRegistryName("old_computer"));
		registry.register(new TransportalizerBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F)).setRegistryName("transportalizer"));
		registry.register(new TransportalizerBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F)).setRegistryName("trans_portalizer"));
		registry.register(new SendificatorBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F)).setRegistryName("sendificator"));
		registry.register(new GristWidgetBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F)).setRegistryName("grist_widget"));
		registry.register(new SmallMachineBlock<>(new CustomVoxelShape(new double[]{4, 0, 4, 12, 6, 12}).createRotatedShapes(), MSTileEntityTypes.URANIUM_COOKER, Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F)).setRegistryName("uranium_cooker"));
		 */
		
		/*
		registry.register(new CruxiteDowelBlock(Block.Properties.of(Material.GLASS).strength(0.0F)).setRegistryName("cruxite_dowel"));
		
		registry.register(new GoldSeedsBlock(Block.Properties.of(Material.PLANT).strength(0.1F).sound(SoundType.METAL).noCollission()).setRegistryName("gold_seeds"));
		registry.register(new SpecialCactusBlock(Block.Properties.of(Material.WOOD).randomTicks().strength(1.0F, 2.5F).sound(SoundType.WOOD)).setRegistryName("wooden_cactus"));
		
		registry.register(new SimpleCakeBlock(Block.Properties.of(Material.CAKE).strength(0.5F).sound(SoundType.WOOL), 2, 0.5F, null).setRegistryName("apple_cake"));
		registry.register(new SimpleCakeBlock(Block.Properties.of(Material.CAKE).strength(0.5F).sound(SoundType.WOOL), 2, 0.3F, player -> player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 150, 0))).setRegistryName("blue_cake"));
		registry.register(new SimpleCakeBlock(Block.Properties.of(Material.CAKE).strength(0.5F).sound(SoundType.WOOL), 2, 0.3F, player -> {player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 1));player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1));}).setRegistryName("cold_cake"));
		registry.register(new SimpleCakeBlock(Block.Properties.of(Material.CAKE).strength(0.5F).sound(SoundType.WOOL), 2, 0.1F, player -> player.heal(1)).setRegistryName("red_cake"));
		registry.register(new SimpleCakeBlock(Block.Properties.of(Material.CAKE).strength(0.5F).sound(SoundType.WOOL), 2, 0.1F, player -> player.setSecondsOnFire(4)).setRegistryName("hot_cake"));
		registry.register(new SimpleCakeBlock(Block.Properties.of(Material.CAKE).strength(0.5F).sound(SoundType.WOOL), 2, 0.1F, null).setRegistryName("reverse_cake"));
		registry.register(new SimpleCakeBlock(Block.Properties.of(Material.CAKE).strength(0.5F).sound(SoundType.WOOL), 3, 0.5F, player -> {player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 350, 1));player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0));}).setRegistryName("fuchsia_cake"));
		registry.register(new SimpleCakeBlock(Block.Properties.of(Material.CAKE).strength(0.5F).sound(SoundType.WOOL), 2, 0.3F, player -> {player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 300, 0));player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 250, 0));}).setRegistryName("negative_cake"));
		registry.register(new SimpleCakeBlock(Block.Properties.of(Material.CAKE).strength(0.5F).sound(SoundType.WOOL), 2, 0.3F, player -> player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 200, 0))).setRegistryName("carrot_cake"));
		
		registry.register(new SpecialTNTBlock(Block.Properties.of(Material.EXPLOSIVE).strength(0.0F).sound(SoundType.GRASS), true, false, false).setRegistryName("primed_tnt"));
		registry.register(new SpecialTNTBlock(Block.Properties.of(Material.EXPLOSIVE).strength(0.0F).sound(SoundType.GRASS).randomTicks(), false, true, false).setRegistryName("unstable_tnt"));
		registry.register(new SpecialTNTBlock(Block.Properties.of(Material.EXPLOSIVE).strength(0.0F).sound(SoundType.GRASS), false, false, true).setRegistryName("instant_tnt"));
		registry.register(new SpecialButtonBlock(Block.Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.WOOD), true, true).setRegistryName("wooden_explosive_button"));
		registry.register(new SpecialButtonBlock(Block.Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.STONE), true, false).setRegistryName("stone_explosive_button"));
		
		registry.register(new CustomShapeBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(0.5F).sound(SoundType.METAL), MSBlockShapes.BLENDER).setRegistryName("blender"));
		registry.register(new CustomShapeBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(0.5F), MSBlockShapes.CHESSBOARD).setRegistryName("chessboard"));
		registry.register(new CustomShapeBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(0.5F), MSBlockShapes.FROG_STATUE).setRegistryName("mini_frog_statue"));
		registry.register(new CustomShapeBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(0.5F), MSBlockShapes.WIZARD_STATUE).setRegistryName("mini_wizard_statue"));
		registry.register(new CustomShapeBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(0.5F), MSBlockShapes.DENIZEN_STATUE).setRegistryName("mini_typheus_statue"));
		registry.register(new CassettePlayerBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(0.5F).sound(SoundType.METAL), MSBlockShapes.CASSETTE_PLAYER).setRegistryName("cassette_player"));
		registry.register(new GlowystoneWireBlock(Block.Properties.of(Material.DECORATION).strength(0.0F).lightLevel(state -> 16).noCollission()).setRegistryName("glowystone_dust"));
		//registry.register(new CustomShapeBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(4.0F), MSBlockShapes.PARCEL_PYXIS).setRegistryName("parcel_pyxis"));
		//registry.register(new CustomShapeBlock(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(1.0F), MSBlockShapes.PYXIS_LID).setRegistryName("pyxis_lid"));
		
		registry.register(new FlowingModFluidBlock(MSFluids.OIL, new Vec3(0.0, 0.0, 0.0), 0.75f, Block.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()).setRegistryName("oil"));
		registry.register(new FlowingModFluidBlock(MSFluids.BLOOD, new Vec3(0.8, 0.0, 0.0), 0.25f, Block.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()).setRegistryName("blood"));
		registry.register(new FlowingModFluidBlock(MSFluids.BRAIN_JUICE, new Vec3(0.55, 0.25, 0.7), 0.25f, Block.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()).setRegistryName("brain_juice"));
		registry.register(new FlowingWaterColorsBlock(MSFluids.WATER_COLORS, 0.01f, Block.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()).setRegistryName("water_colors"));
		registry.register(new FlowingModFluidBlock(MSFluids.ENDER, new Vec3(0, 0.35, 0.35), (Float.MAX_VALUE), Block.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()).setRegistryName("ender"));
		registry.register(new FlowingModFluidBlock(MSFluids.LIGHT_WATER, new Vec3(0.2, 0.3, 1.0), 0.01f, Block.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()).setRegistryName("light_water"));
		/**/
	}
	
	private static Function<BlockState, MaterialColor> logColors(MaterialColor topColor, MaterialColor barkColor)
	{
		return state -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? topColor : barkColor;
	}
	
	private static Block register(IForgeRegistry<Block> registry, Block block) //Used because registry.register doesn't return the registered block
	{
		registry.register(block);
		return block;
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