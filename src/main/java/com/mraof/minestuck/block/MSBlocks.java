package com.mraof.minestuck.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.fluid.FlowingModFluidBlock;
import com.mraof.minestuck.block.fluid.FlowingWaterColorsBlock;
import com.mraof.minestuck.block.machine.*;
import com.mraof.minestuck.block.plant.*;
import com.mraof.minestuck.block.redstone.*;
import com.mraof.minestuck.fluid.MSFluids;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import com.mraof.minestuck.util.CustomVoxelShape;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.DyeColor;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;

@ObjectHolder(Minestuck.MOD_ID)
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class MSBlocks
{
	//Skaia
	public static final Block BLACK_CHESS_DIRT = getNull(), WHITE_CHESS_DIRT = getNull(), DARK_GRAY_CHESS_DIRT = getNull(), LIGHT_GRAY_CHESS_DIRT = getNull();
	public static final Block SKAIA_PORTAL = getNull();
	public static final Block BLACK_CASTLE_BRICKS = getNull();
	public static final Block DARK_GRAY_CASTLE_BRICKS = getNull();
	public static final Block LIGHT_GRAY_CASTLE_BRICKS = getNull();
	public static final Block WHITE_CASTLE_BRICKS = getNull();
	public static final Block BLACK_CASTLE_BRICK_SMOOTH = getNull();
	public static final Block DARK_GRAY_CASTLE_BRICK_SMOOTH = getNull();
	public static final Block LIGHT_GRAY_CASTLE_BRICK_SMOOTH = getNull();
	public static final Block WHITE_CASTLE_BRICK_SMOOTH = getNull();
	public static final Block BLACK_CASTLE_BRICK_TRIM = getNull();
	public static final Block DARK_GRAY_CASTLE_BRICK_TRIM = getNull();
	public static final Block LIGHT_GRAY_CASTLE_BRICK_TRIM = getNull();
	public static final Block WHITE_CASTLE_BRICK_TRIM = getNull();
	public static final Block CHECKERED_STAINED_GLASS = getNull();
	public static final Block BLACK_CROWN_STAINED_GLASS = getNull();
	public static final Block BLACK_PAWN_STAINED_GLASS = getNull();
	public static final Block WHITE_CROWN_STAINED_GLASS = getNull();
	public static final Block WHITE_PAWN_STAINED_GLASS = getNull();
	
	//Ores
	public static final Block STONE_CRUXITE_ORE = getNull(), NETHERRACK_CRUXITE_ORE = getNull(), COBBLESTONE_CRUXITE_ORE = getNull(), SANDSTONE_CRUXITE_ORE = getNull();
	public static final Block RED_SANDSTONE_CRUXITE_ORE = getNull(), END_STONE_CRUXITE_ORE = getNull(), SHADE_STONE_CRUXITE_ORE = getNull(), PINK_STONE_CRUXITE_ORE = getNull();
	public static final Block STONE_URANIUM_ORE = getNull(), NETHERRACK_URANIUM_ORE = getNull(), COBBLESTONE_URANIUM_ORE = getNull(), SANDSTONE_URANIUM_ORE = getNull();
	public static final Block RED_SANDSTONE_URANIUM_ORE = getNull(), END_STONE_URANIUM_ORE = getNull(), SHADE_STONE_URANIUM_ORE = getNull(), PINK_STONE_URANIUM_ORE = getNull();
	public static final Block NETHERRACK_COAL_ORE = getNull(), SHADE_STONE_COAL_ORE = getNull(), PINK_STONE_COAL_ORE = getNull();
	public static final Block END_STONE_IRON_ORE = getNull(), SANDSTONE_IRON_ORE = getNull(), RED_SANDSTONE_IRON_ORE = getNull();
	public static final Block SANDSTONE_GOLD_ORE = getNull(), RED_SANDSTONE_GOLD_ORE = getNull(), SHADE_STONE_GOLD_ORE = getNull(), PINK_STONE_GOLD_ORE = getNull();
	public static final Block END_STONE_REDSTONE_ORE = getNull();
	public static final Block STONE_QUARTZ_ORE = getNull();
	public static final Block PINK_STONE_LAPIS_ORE = getNull();
	public static final Block PINK_STONE_DIAMOND_ORE = getNull();
	
	//Resource Blocks
	public static final Block CRUXITE_BLOCK = getNull();
	public static final Block URANIUM_BLOCK = getNull();
	public static final Block GENERIC_OBJECT = getNull();
	
	//Land Environment Blocks
	public static final Block BLUE_DIRT = getNull(), THOUGHT_DIRT = getNull();
	public static final Block COARSE_STONE = getNull(), CHISELED_COARSE_STONE = getNull();
	public static final Block SHADE_STONE = getNull(), SHADE_BRICKS = getNull(), SMOOTH_SHADE_STONE = getNull();
	public static final Block FROST_BRICKS = getNull(), FROST_TILE = getNull(), CHISELED_FROST_BRICKS = getNull();
	public static final Block CAST_IRON = getNull(), CHISELED_CAST_IRON = getNull();
	public static final Block MYCELIUM_BRICKS = getNull();
	public static final Block STEEL_BEAM = getNull();
	public static final Block BLACK_STONE = getNull(), BLACK_SAND = getNull();
	public static final Block FLOWERY_MOSSY_COBBLESTONE = getNull(), FLOWERY_MOSSY_STONE_BRICKS = getNull();
	public static final Block COARSE_END_STONE = getNull(), END_GRASS = getNull();
	public static final Block CHALK = getNull(), POLISHED_CHALK = getNull(), CHALK_BRICKS = getNull(), CHISELED_CHALK_BRICKS = getNull();
	public static final Block PINK_STONE = getNull(), POLISHED_PINK_STONE = getNull(), PINK_STONE_BRICKS = getNull(), CHISELED_PINK_STONE_BRICKS = getNull();
	public static final Block CRACKED_PINK_STONE_BRICKS = getNull(), MOSSY_PINK_STONE_BRICKS = getNull();
	public static final Block BROWN_STONE = getNull(), POLISHED_BROWN_STONE = getNull(), BROWN_STONE_BRICKS = getNull(), CRACKED_BROWN_STONE_BRICKS = getNull(), BROWN_STONE_COLUMN = getNull();
	public static final Block GREEN_STONE = getNull(), POLISHED_GREEN_STONE = getNull(), GREEN_STONE_BRICKS = getNull(), GREEN_STONE_COLUMN = getNull(), CHISELED_GREEN_STONE_BRICKS = getNull(), HORIZONTAL_GREEN_STONE_BRICKS = getNull(), VERTICAL_GREEN_STONE_BRICKS = getNull()
			, GREEN_STONE_BRICK_TRIM = getNull(), GREEN_STONE_BRICK_FROG = getNull(), GREEN_STONE_BRICK_IGUANA_LEFT = getNull(), GREEN_STONE_BRICK_IGUANA_RIGHT = getNull(), GREEN_STONE_BRICK_LOTUS = getNull(), GREEN_STONE_BRICK_NAK_LEFT = getNull(), GREEN_STONE_BRICK_NAK_RIGHT = getNull()
			, GREEN_STONE_BRICK_SALAMANDER_LEFT = getNull(), GREEN_STONE_BRICK_SALAMANDER_RIGHT = getNull(), GREEN_STONE_BRICK_SKAIA = getNull(), GREEN_STONE_BRICK_TURTLE = getNull();
	public static final Block DENSE_CLOUD = getNull(), BRIGHT_DENSE_CLOUD = getNull();
	public static final Block SUGAR_CUBE = getNull();
	public static final Block SPIKES = getNull();
	
	//Land Tree Blocks
	public static final Block GLOWING_LOG = getNull(), FROST_LOG = getNull(), RAINBOW_LOG = getNull(), END_LOG = getNull();
	public static final Block VINE_LOG = getNull(), FLOWERY_VINE_LOG = getNull(), DEAD_LOG = getNull(), PETRIFIED_LOG = getNull();
	public static final Block GLOWING_WOOD = getNull(), FROST_WOOD = getNull(), RAINBOW_WOOD = getNull(), END_WOOD = getNull();
	public static final Block VINE_WOOD = getNull(), FLOWERY_VINE_WOOD = getNull(), DEAD_WOOD = getNull(), PETRIFIED_WOOD = getNull();
	public static final Block GLOWING_PLANKS = getNull(), FROST_PLANKS = getNull(), RAINBOW_PLANKS = getNull(), END_PLANKS = getNull();
	public static final Block DEAD_PLANKS = getNull(), TREATED_PLANKS = getNull();
	public static final Block FROST_LEAVES = getNull(), RAINBOW_LEAVES = getNull(), END_LEAVES = getNull();
	public static final BushBlock RAINBOW_SAPLING = getNull(), END_SAPLING = getNull();
	
	//Aspect Tree Blocks
	public static final Block BLOOD_ASPECT_LOG = getNull(), BREATH_ASPECT_LOG = getNull(), DOOM_ASPECT_LOG = getNull(), HEART_ASPECT_LOG = getNull();
	public static final Block HOPE_ASPECT_LOG = getNull(), LIFE_ASPECT_LOG = getNull(), LIGHT_ASPECT_LOG = getNull(), MIND_ASPECT_LOG = getNull();
	public static final Block RAGE_ASPECT_LOG = getNull(), SPACE_ASPECT_LOG = getNull(), TIME_ASPECT_LOG = getNull(), VOID_ASPECT_LOG = getNull();
	public static final Block BLOOD_ASPECT_PLANKS = getNull(), BREATH_ASPECT_PLANKS = getNull(), DOOM_ASPECT_PLANKS = getNull(), HEART_ASPECT_PLANKS = getNull();
	public static final Block HOPE_ASPECT_PLANKS = getNull(), LIFE_ASPECT_PLANKS = getNull(), LIGHT_ASPECT_PLANKS = getNull(), MIND_ASPECT_PLANKS = getNull();
	public static final Block RAGE_ASPECT_PLANKS = getNull(), SPACE_ASPECT_PLANKS = getNull(), TIME_ASPECT_PLANKS = getNull(), VOID_ASPECT_PLANKS = getNull();
	public static final Block BLOOD_ASPECT_LEAVES = getNull(), BREATH_ASPECT_LEAVES = getNull(), DOOM_ASPECT_LEAVES = getNull(), HEART_ASPECT_LEAVES = getNull();
	public static final Block HOPE_ASPECT_LEAVES = getNull(), LIFE_ASPECT_LEAVES = getNull(), LIGHT_ASPECT_LEAVES = getNull(), MIND_ASPECT_LEAVES = getNull();
	public static final Block RAGE_ASPECT_LEAVES = getNull(), SPACE_ASPECT_LEAVES = getNull(), TIME_ASPECT_LEAVES = getNull(), VOID_ASPECT_LEAVES = getNull();
	public static final Block BLOOD_ASPECT_SAPLING = getNull(), BREATH_ASPECT_SAPLING = getNull(), DOOM_ASPECT_SAPLING = getNull(), HEART_ASPECT_SAPLING = getNull();
	public static final Block HOPE_ASPECT_SAPLING = getNull(), LIFE_ASPECT_SAPLING = getNull(), LIGHT_ASPECT_SAPLING = getNull(), MIND_ASPECT_SAPLING = getNull();
	public static final Block RAGE_ASPECT_SAPLING = getNull(), SPACE_ASPECT_SAPLING = getNull(), TIME_ASPECT_SAPLING = getNull(), VOID_ASPECT_SAPLING = getNull();
	
	//Land Plant Blocks
	public static final Block GLOWING_MUSHROOM = getNull();
	public static final Block DESERT_BUSH = getNull();
	public static final Block BLOOMING_CACTUS = getNull();
	public static final Block PETRIFIED_GRASS = getNull();
	public static final Block PETRIFIED_POPPY = getNull();
	public static final Block STRAWBERRY = getNull(), ATTACHED_STRAWBERRY_STEM = getNull(), STRAWBERRY_STEM = getNull();
	public static final Block TALL_END_GRASS = getNull();
	public static final Block GLOWFLOWER = getNull();
	
	//Special Land Blocks
	public static final Block GLOWY_GOOP = getNull();
	public static final Block COAGULATED_BLOOD = getNull();
	public static final Block VEIN = getNull();
	public static final Block VEIN_CORNER = getNull();
	public static final Block INVERTED_VEIN_CORNER = getNull();
	public static final Block PIPE = getNull();
	public static final Block PIPE_INTERSECTION = getNull();
	public static final Block PARCEL_PYXIS = getNull();
	public static final Block PYXIS_LID = getNull();
	public static final Block STONE_SLAB = getNull();
	public static final Block NAKAGATOR_STATUE = getNull();
	
	//Structure Land Blocks
	public static final Block BLACK_CASTLE_BRICK_STAIRS = getNull(), DARK_GRAY_CASTLE_BRICK_STAIRS = getNull(), LIGHT_GRAY_CASTLE_BRICK_STAIRS = getNull(), WHITE_CASTLE_BRICK_STAIRS = getNull();
	public static final Block COARSE_STONE_STAIRS = getNull(), SHADE_BRICK_STAIRS = getNull(), FROST_BRICK_STAIRS = getNull(), CAST_IRON_STAIRS = getNull();
	public static final Block MYCELIUM_BRICK_STAIRS = getNull(), CHALK_STAIRS = getNull(), CHALK_BRICK_STAIRS = getNull(), PINK_STONE_BRICK_STAIRS = getNull(), BROWN_STONE_BRICK_STAIRS = getNull(), GREEN_STONE_BRICK_STAIRS = getNull();
	public static final Block RAINBOW_PLANKS_STAIRS = getNull(), END_PLANKS_STAIRS = getNull(), DEAD_PLANKS_STAIRS = getNull(), TREATED_PLANKS_STAIRS = getNull();
	public static final Block STEEP_GREEN_STONE_BRICK_STAIRS_BASE = getNull(), STEEP_GREEN_STONE_BRICK_STAIRS_TOP = getNull();
	public static final Block BLACK_CASTLE_BRICK_SLAB = getNull(), DARK_GRAY_CASTLE_BRICK_SLAB = getNull(), LIGHT_GRAY_CASTLE_BRICK_SLAB = getNull(), WHITE_CASTLE_BRICK_SLAB = getNull();
	public static final Block CHALK_SLAB = getNull(), CHALK_BRICK_SLAB = getNull(), PINK_STONE_BRICK_SLAB = getNull(), BROWN_STONE_BRICK_SLAB = getNull(), GREEN_STONE_BRICK_SLAB = getNull();
	public static final Block RAINBOW_PLANKS_SLAB = getNull(), END_PLANKS_SLAB = getNull(), DEAD_PLANKS_SLAB = getNull(), TREATED_PLANKS_SLAB = getNull();
	
	//Dungeon Functional Blocks
	public static final Block TRAJECTORY_BLOCK = getNull();
	public static final Block STAT_STORER = getNull();
	public static final Block REMOTE_OBSERVER = getNull();
	public static final Block WIRELESS_REDSTONE_TRANSMITTER = getNull();
	public static final Block WIRELESS_REDSTONE_RECEIVER = getNull();
	public static final Block SOLID_SWITCH = getNull();
	public static final Block VARIABLE_SOLID_SWITCH = getNull();
	public static final Block ONE_SECOND_INTERVAL_TIMED_SOLID_SWITCH = getNull();
	public static final Block TWO_SECOND_INTERVAL_TIMED_SOLID_SWITCH = getNull();
	public static final Block SUMMONER = getNull();
	public static final Block AREA_EFFECT_BLOCK = getNull();
	public static final Block PLATFORM_GENERATOR = getNull();
	public static final Block PLATFORM_BLOCK = getNull();
	public static final Block ITEM_MAGNET = getNull();
	public static final Block REDSTONE_CLOCK = getNull();
	public static final Block AND_GATE_BLOCK = getNull();
	public static final Block OR_GATE_BLOCK = getNull();
	public static final Block XOR_GATE_BLOCK = getNull();
	public static final Block NAND_GATE_BLOCK = getNull();
	public static final Block NOR_GATE_BLOCK = getNull();
	public static final Block XNOR_GATE_BLOCK = getNull();
	
	//Core Functional Land Blocks
	public static final Block GATE = getNull();
	public static final Block RETURN_NODE = getNull();
	
	//Misc Functional Land Blocks
	
	//Sburb Machines
	public static final Block CRUXTRUDER_LID = getNull();
	public static CruxtruderMultiblock CRUXTRUDER = new CruxtruderMultiblock(Minestuck.MOD_ID);
	public static TotemLatheMultiblock TOTEM_LATHE = new TotemLatheMultiblock(Minestuck.MOD_ID);
	public static AlchemiterMultiblock ALCHEMITER = new AlchemiterMultiblock(Minestuck.MOD_ID);
	public static PunchDesignixMultiblock PUNCH_DESIGNIX = new PunchDesignixMultiblock(Minestuck.MOD_ID);
	public static final Block MINI_CRUXTRUDER = getNull();
	public static final Block MINI_TOTEM_LATHE = getNull();
	public static final Block MINI_ALCHEMITER = getNull();
	public static final Block MINI_PUNCH_DESIGNIX = getNull();
	public static final Block HOLOPAD = getNull();
	
	//Misc Machines
	public static final Block COMPUTER = getNull();
	public static final Block LAPTOP = getNull();
	public static final Block CROCKERTOP = getNull();
	public static final Block HUBTOP = getNull();
	public static final Block LUNCHTOP = getNull();
	public static final Block OLD_COMPUTER = getNull();
	public static final Block TRANSPORTALIZER = getNull();
	public static final Block TRANS_PORTALIZER = getNull();
	public static final Block SENDIFICATOR = getNull();
	public static final Block GRIST_WIDGET = getNull();
	public static final Block URANIUM_COOKER = getNull();
	
	//Misc Core Objects
	public static final Block CRUXITE_DOWEL = getNull();
	public static LotusTimeCapsuleMultiblock LOTUS_TIME_CAPSULE_BLOCK = new LotusTimeCapsuleMultiblock(Minestuck.MOD_ID);
	
	//Misc Alchemy Semi-Plants
	public static final Block GOLD_SEEDS = getNull();
	public static final Block WOODEN_CACTUS = getNull();
	
	//Cakes
	public static final Block APPLE_CAKE = getNull();
	public static final Block BLUE_CAKE = getNull();
	public static final Block COLD_CAKE = getNull();
	public static final Block RED_CAKE = getNull();
	public static final Block HOT_CAKE = getNull();
	public static final Block REVERSE_CAKE = getNull();
	public static final Block FUCHSIA_CAKE = getNull();
	public static final Block NEGATIVE_CAKE = getNull();
	
	//Explosion and Redstone
	public static final Block PRIMED_TNT = getNull();
	public static final Block UNSTABLE_TNT = getNull();
	public static final Block INSTANT_TNT = getNull();
	public static final Block WOODEN_EXPLOSIVE_BUTTON = getNull();
	public static final Block STONE_EXPLOSIVE_BUTTON = getNull();
	
	//Misc Alchemy Objects
	public static final Block BLENDER = getNull();
	public static final Block CHESSBOARD = getNull();
	public static final Block MINI_FROG_STATUE = getNull();
	public static final Block MINI_WIZARD_STATUE = getNull();
	public static final Block GLOWYSTONE_DUST = getNull();
	public static final CassettePlayerBlock CASSETTE_PLAYER = getNull();
	
	public static final FlowingFluidBlock OIL = getNull(), BLOOD = getNull(), BRAIN_JUICE = getNull();
	public static final FlowingFluidBlock WATER_COLORS = getNull(), ENDER = getNull(), LIGHT_WATER = getNull();
	
	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T getNull()
	{
		return null;
	}
	
	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event)
	{
		IForgeRegistry<Block> registry = event.getRegistry();
		
		registry.register(new Block(Block.Properties.create(Material.EARTH, MaterialColor.BLACK).hardnessAndResistance(0.5F).harvestTool(ToolType.SHOVEL).sound(SoundType.GROUND)).setRegistryName("black_chess_dirt"));
		registry.register(new Block(Block.Properties.create(Material.EARTH, MaterialColor.SNOW).hardnessAndResistance(0.5F).harvestTool(ToolType.SHOVEL).sound(SoundType.GROUND)).setRegistryName("white_chess_dirt"));
		registry.register(new Block(Block.Properties.create(Material.EARTH, MaterialColor.GRAY).hardnessAndResistance(0.5F).harvestTool(ToolType.SHOVEL).sound(SoundType.GROUND)).setRegistryName("dark_gray_chess_dirt"));
		registry.register(new Block(Block.Properties.create(Material.EARTH, MaterialColor.LIGHT_GRAY).hardnessAndResistance(0.5F).harvestTool(ToolType.SHOVEL).sound(SoundType.GROUND)).setRegistryName("light_gray_chess_dirt"));
		registry.register(new SkaiaPortalBlock(Block.Properties.create(Material.PORTAL, MaterialColor.CYAN).doesNotBlockMovement().lightValue(11).hardnessAndResistance(-1.0F, 3600000.0F).noDrops()).setRegistryName("skaia_portal"));
		Block blackCastleBricks = register(registry, new Block(Block.Properties.create(Material.ROCK, MaterialColor.BLACK_TERRACOTTA).hardnessAndResistance(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("black_castle_bricks"));
		Block darkGrayCastleBricks = register(registry, new Block(Block.Properties.create(Material.ROCK, MaterialColor.GRAY_TERRACOTTA).hardnessAndResistance(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("dark_gray_castle_bricks"));
		Block lightGrayCastleBricks = register(registry, new Block(Block.Properties.create(Material.ROCK, MaterialColor.LIGHT_GRAY_TERRACOTTA).hardnessAndResistance(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("light_gray_castle_bricks"));
		Block whiteCastleBricks = register(registry, new Block(Block.Properties.create(Material.ROCK, MaterialColor.WHITE_TERRACOTTA).hardnessAndResistance(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("white_castle_bricks"));
		registry.register(new Block(Block.Properties.create(Material.ROCK, MaterialColor.BLACK_TERRACOTTA).hardnessAndResistance(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("black_castle_brick_smooth"));
		registry.register(new Block(Block.Properties.create(Material.ROCK, MaterialColor.GRAY_TERRACOTTA).hardnessAndResistance(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("dark_gray_castle_brick_smooth"));
		registry.register(new Block(Block.Properties.create(Material.ROCK, MaterialColor.LIGHT_GRAY_TERRACOTTA).hardnessAndResistance(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("light_gray_castle_brick_smooth"));
		registry.register(new Block(Block.Properties.create(Material.ROCK, MaterialColor.WHITE_TERRACOTTA).hardnessAndResistance(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("white_castle_brick_smooth"));
		registry.register(new MSDirectionalBlock(Block.Properties.create(Material.ROCK, MaterialColor.BLACK_TERRACOTTA).hardnessAndResistance(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("black_castle_brick_trim"));
		registry.register(new MSDirectionalBlock(Block.Properties.create(Material.ROCK, MaterialColor.GRAY_TERRACOTTA).hardnessAndResistance(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("dark_gray_castle_brick_trim"));
		registry.register(new MSDirectionalBlock(Block.Properties.create(Material.ROCK, MaterialColor.LIGHT_GRAY_TERRACOTTA).hardnessAndResistance(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("light_gray_castle_brick_trim"));
		registry.register(new MSDirectionalBlock(Block.Properties.create(Material.ROCK, MaterialColor.WHITE_TERRACOTTA).hardnessAndResistance(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("white_castle_brick_trim"));
		registry.register(new StainedGlassBlock(DyeColor.BLUE, Block.Properties.create(Material.GLASS, DyeColor.BLUE).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()).setRegistryName("checkered_stained_glass"));
		registry.register(new StainedGlassBlock(DyeColor.BLUE, Block.Properties.create(Material.GLASS, DyeColor.BLUE).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()).setRegistryName("black_crown_stained_glass"));
		registry.register(new StainedGlassBlock(DyeColor.BLUE, Block.Properties.create(Material.GLASS, DyeColor.BLUE).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()).setRegistryName("black_pawn_stained_glass"));
		registry.register(new StainedGlassBlock(DyeColor.BLUE, Block.Properties.create(Material.GLASS, DyeColor.BLUE).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()).setRegistryName("white_crown_stained_glass"));
		registry.register(new StainedGlassBlock(DyeColor.BLUE, Block.Properties.create(Material.GLASS, DyeColor.BLUE).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid()).setRegistryName("white_pawn_stained_glass"));
		
		registry.register(new CustomOreBlock(2, 5, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("stone_cruxite_ore"));
		registry.register(new CustomOreBlock(2, 5, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("netherrack_cruxite_ore"));
		registry.register(new CustomOreBlock(2, 5, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("cobblestone_cruxite_ore"));
		registry.register(new CustomOreBlock(2, 5, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("sandstone_cruxite_ore"));
		registry.register(new CustomOreBlock(2, 5, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("red_sandstone_cruxite_ore"));
		registry.register(new CustomOreBlock(2, 5, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("end_stone_cruxite_ore"));
		registry.register(new CustomOreBlock(2, 5, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("shade_stone_cruxite_ore"));
		registry.register(new CustomOreBlock(2, 5, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("pink_stone_cruxite_ore"));
		registry.register(new CustomOreBlock(2, 5, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1).lightValue(3)).setRegistryName("stone_uranium_ore"));
		registry.register(new CustomOreBlock(2, 5, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1).lightValue(3)).setRegistryName("netherrack_uranium_ore"));
		registry.register(new CustomOreBlock(2, 5, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1).lightValue(3)).setRegistryName("cobblestone_uranium_ore"));
		registry.register(new CustomOreBlock(2, 5, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1).lightValue(3)).setRegistryName("sandstone_uranium_ore"));
		registry.register(new CustomOreBlock(2, 5, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1).lightValue(3)).setRegistryName("red_sandstone_uranium_ore"));
		registry.register(new CustomOreBlock(2, 5, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1).lightValue(3)).setRegistryName("end_stone_uranium_ore"));
		registry.register(new CustomOreBlock(2, 5, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1).lightValue(3)).setRegistryName("shade_stone_uranium_ore"));
		registry.register(new CustomOreBlock(2, 5, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1).lightValue(3)).setRegistryName("pink_stone_uranium_ore"));
		registry.register(new CustomOreBlock(0, 2, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("netherrack_coal_ore"));
		registry.register(new CustomOreBlock(0, 2, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("shade_stone_coal_ore"));
		registry.register(new CustomOreBlock(0, 2, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("pink_stone_coal_ore"));
		registry.register(new CustomOreBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("end_stone_iron_ore"));
		registry.register(new CustomOreBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("sandstone_iron_ore"));
		registry.register(new CustomOreBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("red_sandstone_iron_ore"));
		registry.register(new CustomOreBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(2)).setRegistryName("sandstone_gold_ore"));
		registry.register(new CustomOreBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(2)).setRegistryName("red_sandstone_gold_ore"));
		registry.register(new CustomOreBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(2)).setRegistryName("shade_stone_gold_ore"));
		registry.register(new CustomOreBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(2)).setRegistryName("pink_stone_gold_ore"));
		registry.register(new CustomOreBlock(1, 5, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(2)).setRegistryName("end_stone_redstone_ore"));
		registry.register(new CustomOreBlock(2, 5, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("stone_quartz_ore"));
		registry.register(new CustomOreBlock(2, 5, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("pink_stone_lapis_ore"));
		registry.register(new CustomOreBlock(3, 7, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(2)).setRegistryName("pink_stone_diamond_ore"));
		
		registry.register(new Block(Block.Properties.create(Material.ROCK, DyeColor.LIGHT_BLUE).hardnessAndResistance(3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("cruxite_block"));
		registry.register(new Block(Block.Properties.create(Material.ROCK, DyeColor.LIME).hardnessAndResistance(3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0).lightValue(7)).setRegistryName("uranium_block"));
		registry.register(new Block(Block.Properties.create(Material.GOURD, DyeColor.LIME).hardnessAndResistance(1.0F).sound(SoundType.WOOD)).setRegistryName("generic_object"));
		
		registry.register(new Block(Block.Properties.create(Material.EARTH, MaterialColor.BLUE).hardnessAndResistance(0.5F).sound(SoundType.GROUND)).setRegistryName("blue_dirt"));
		registry.register(new Block(Block.Properties.create(Material.EARTH, MaterialColor.LIME).hardnessAndResistance(0.5F).sound(SoundType.GROUND)).setRegistryName("thought_dirt"));
		Block coarseStone = register(registry, new Block(Block.Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(2.0F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("coarse_stone"));
		registry.register(new Block(Block.Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(2.0F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("chiseled_coarse_stone"));
		registry.register(new Block(Block.Properties.create(Material.ROCK, MaterialColor.BLUE).hardnessAndResistance(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("shade_stone"));
		Block shadeBricks = register(registry, new Block(Block.Properties.create(Material.ROCK, MaterialColor.BLUE).hardnessAndResistance(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("shade_bricks"));
		registry.register(new Block(Block.Properties.create(Material.ROCK, MaterialColor.BLUE).hardnessAndResistance(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("smooth_shade_stone"));
		Block frostBricks = register(registry, new Block(Block.Properties.create(Material.ROCK, MaterialColor.ICE).hardnessAndResistance(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("frost_bricks"));
		registry.register(new Block(Block.Properties.create(Material.ROCK, MaterialColor.ICE).hardnessAndResistance(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("frost_tile"));
		registry.register(new Block(Block.Properties.create(Material.ROCK, MaterialColor.ICE).hardnessAndResistance(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("chiseled_frost_bricks"));
		Block castIron = register(registry, new Block(Block.Properties.create(Material.ROCK, MaterialColor.IRON).hardnessAndResistance(3.0F, 9.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("cast_iron"));
		registry.register(new Block(Block.Properties.create(Material.ROCK, MaterialColor.IRON).hardnessAndResistance(3.0F, 9.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("chiseled_cast_iron"));
		registry.register(new MSDirectionalBlock(Block.Properties.create(Material.IRON, MaterialColor.IRON).hardnessAndResistance(3.0F, 9.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("steel_beam"));
		Block myceliumBricks = register(registry, new Block(Block.Properties.create(Material.ROCK, MaterialColor.MAGENTA).hardnessAndResistance(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("mycelium_bricks"));
		registry.register(new Block(Block.Properties.create(Material.ROCK, MaterialColor.BLACK).hardnessAndResistance(2.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("black_stone"));
		registry.register(new SandBlock(0x181915, Block.Properties.create(Material.SAND, MaterialColor.BLACK).hardnessAndResistance(0.5F).sound(SoundType.SAND)).setRegistryName("black_sand"));
		registry.register(new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("flowery_mossy_cobblestone"));
		registry.register(new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("flowery_mossy_stone_bricks"));
		registry.register(new Block(Block.Properties.create(Material.ROCK, MaterialColor.SAND).hardnessAndResistance(3.0F, 9.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("coarse_end_stone"));
		registry.register(new EndGrassBlock(Block.Properties.create(Material.ROCK, MaterialColor.PURPLE).hardnessAndResistance(3.0F, 9.0F).harvestTool(ToolType.SHOVEL).tickRandomly()).setRegistryName("end_grass"));
		Block chalk = register(registry, new Block(Block.Properties.create(Material.ROCK, MaterialColor.SNOW).hardnessAndResistance(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("chalk"));
		registry.register(new Block(Block.Properties.create(Material.ROCK, MaterialColor.SNOW).hardnessAndResistance(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("polished_chalk"));
		Block chalkBricks = register(registry, new Block(Block.Properties.create(Material.ROCK, MaterialColor.SNOW).hardnessAndResistance(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("chalk_bricks"));
		registry.register(new Block(Block.Properties.create(Material.ROCK, MaterialColor.SNOW).hardnessAndResistance(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("chiseled_chalk_bricks"));
		registry.register(new Block(Block.Properties.create(Material.ROCK, MaterialColor.PINK).hardnessAndResistance(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("pink_stone"));
		registry.register(new Block(Block.Properties.create(Material.ROCK, MaterialColor.PINK).hardnessAndResistance(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("polished_pink_stone"));
		Block pinkStoneBricks = register(registry, new Block(Block.Properties.create(Material.ROCK, MaterialColor.PINK).hardnessAndResistance(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("pink_stone_bricks"));
		registry.register(new Block(Block.Properties.create(Material.ROCK, MaterialColor.PINK).hardnessAndResistance(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("chiseled_pink_stone_bricks"));
		registry.register(new Block(Block.Properties.create(Material.ROCK, MaterialColor.PINK).hardnessAndResistance(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("cracked_pink_stone_bricks"));
		registry.register(new Block(Block.Properties.create(Material.ROCK, MaterialColor.PINK).hardnessAndResistance(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("mossy_pink_stone_bricks"));
		registry.register(new Block(Block.Properties.create(Material.ROCK, MaterialColor.BROWN).hardnessAndResistance(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("brown_stone"));
		registry.register(new Block(Block.Properties.create(Material.ROCK, MaterialColor.BROWN).hardnessAndResistance(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("polished_brown_stone"));
		Block brownStoneBricks = register(registry, new Block(Block.Properties.create(Material.ROCK, MaterialColor.BROWN).hardnessAndResistance(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("brown_stone_bricks"));
		registry.register(new Block(Block.Properties.create(Material.ROCK, MaterialColor.BROWN).hardnessAndResistance(1.0F, 5.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("cracked_brown_stone_bricks"));
		registry.register(new MSDirectionalBlock(Block.Properties.create(Material.ROCK, MaterialColor.BROWN).hardnessAndResistance(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("brown_stone_column"));
		registry.register(new Block(Block.Properties.create(Material.ROCK, MaterialColor.GREEN).hardnessAndResistance(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("green_stone"));
		registry.register(new Block(Block.Properties.create(Material.ROCK, MaterialColor.GREEN).hardnessAndResistance(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("polished_green_stone"));
		Block greenStoneBricks = register(registry, new Block(Block.Properties.create(Material.ROCK, MaterialColor.GREEN).hardnessAndResistance(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("green_stone_bricks"));
		registry.register(new MSDirectionalBlock(Block.Properties.create(Material.ROCK, MaterialColor.GREEN).hardnessAndResistance(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("green_stone_column"));
		registry.register(new Block(Block.Properties.create(Material.ROCK, MaterialColor.GREEN).hardnessAndResistance(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("chiseled_green_stone_bricks"));
		registry.register(new Block(Block.Properties.create(Material.ROCK, MaterialColor.GREEN).hardnessAndResistance(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("horizontal_green_stone_bricks"));
		registry.register(new Block(Block.Properties.create(Material.ROCK, MaterialColor.GREEN).hardnessAndResistance(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("vertical_green_stone_bricks"));
		registry.register(new MSDirectionalBlock(Block.Properties.create(Material.ROCK, MaterialColor.GREEN).hardnessAndResistance(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("green_stone_brick_trim"));
		registry.register(new HieroglyphBlock(Block.Properties.create(Material.ROCK, MaterialColor.GREEN).hardnessAndResistance(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("green_stone_brick_frog"));
		registry.register(new HieroglyphBlock(Block.Properties.create(Material.ROCK, MaterialColor.GREEN).hardnessAndResistance(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("green_stone_brick_iguana_left"));
		registry.register(new HieroglyphBlock(Block.Properties.create(Material.ROCK, MaterialColor.GREEN).hardnessAndResistance(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("green_stone_brick_iguana_right"));
		registry.register(new HieroglyphBlock(Block.Properties.create(Material.ROCK, MaterialColor.GREEN).hardnessAndResistance(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("green_stone_brick_lotus"));
		registry.register(new HieroglyphBlock(Block.Properties.create(Material.ROCK, MaterialColor.GREEN).hardnessAndResistance(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("green_stone_brick_nak_left"));
		registry.register(new HieroglyphBlock(Block.Properties.create(Material.ROCK, MaterialColor.GREEN).hardnessAndResistance(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("green_stone_brick_nak_right"));
		registry.register(new HieroglyphBlock(Block.Properties.create(Material.ROCK, MaterialColor.GREEN).hardnessAndResistance(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("green_stone_brick_salamander_left"));
		registry.register(new HieroglyphBlock(Block.Properties.create(Material.ROCK, MaterialColor.GREEN).hardnessAndResistance(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("green_stone_brick_salamander_right"));
		registry.register(new HieroglyphBlock(Block.Properties.create(Material.ROCK, MaterialColor.GREEN).hardnessAndResistance(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("green_stone_brick_skaia"));
		registry.register(new HieroglyphBlock(Block.Properties.create(Material.ROCK, MaterialColor.GREEN).hardnessAndResistance(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("green_stone_brick_turtle"));
		registry.register(new Block(Block.Properties.create(Material.GLASS, MaterialColor.YELLOW).hardnessAndResistance(0.5F).sound(SoundType.SNOW)).setRegistryName("dense_cloud"));
		registry.register(new Block(Block.Properties.create(Material.GLASS, MaterialColor.LIGHT_GRAY).hardnessAndResistance(0.5F).sound(SoundType.SNOW)).setRegistryName("bright_dense_cloud"));
		registry.register(new Block(Block.Properties.create(Material.SAND, MaterialColor.SNOW).hardnessAndResistance(0.4F).sound(SoundType.SAND)).setRegistryName("sugar_cube"));
		registry.register(new SpikeBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(2).sound(SoundType.METAL), MSBlockShapes.SPIKES).setRegistryName("spikes"));
		
		registry.register(new FlammableLogBlock(MaterialColor.LIGHT_BLUE, Block.Properties.create(Material.WOOD, MaterialColor.LIGHT_BLUE).hardnessAndResistance(2.0F).harvestTool(ToolType.AXE).lightValue(11).sound(SoundType.WOOD)).setRegistryName("glowing_log"));
		registry.register(new FlammableLogBlock(MaterialColor.ICE, Block.Properties.create(Material.WOOD, MaterialColor.ICE).hardnessAndResistance(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("frost_log"));
		registry.register(new FlammableLogBlock(MaterialColor.WOOD, Block.Properties.create(Material.WOOD).hardnessAndResistance(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("rainbow_log"));
		registry.register(new DoubleLogBlock(MaterialColor.SAND, 1, 250, Block.Properties.create(Material.WOOD, MaterialColor.SAND).hardnessAndResistance(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("end_log"));
		registry.register(new FlammableLogBlock(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("vine_log"));
		registry.register(new FlammableLogBlock(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("flowery_vine_log"));
		registry.register(new FlammableLogBlock(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("dead_log"));
		registry.register(new FlammableLogBlock(MaterialColor.WOOD, 0, 0, Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F).harvestTool(ToolType.AXE).sound(SoundType.STONE)).setRegistryName("petrified_log"));
		registry.register(new FlammableLogBlock(MaterialColor.LIGHT_BLUE, Block.Properties.create(Material.WOOD, MaterialColor.LIGHT_BLUE).hardnessAndResistance(2.0F).harvestTool(ToolType.AXE).lightValue(11).sound(SoundType.WOOD)).setRegistryName("glowing_wood"));
		registry.register(new FlammableLogBlock(MaterialColor.ICE, Block.Properties.create(Material.WOOD, MaterialColor.ICE).hardnessAndResistance(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("frost_wood"));
		registry.register(new FlammableLogBlock(MaterialColor.WOOD, Block.Properties.create(Material.WOOD).hardnessAndResistance(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("rainbow_wood"));
		registry.register(new FlammableLogBlock(MaterialColor.SAND, 1, 250, Block.Properties.create(Material.WOOD, MaterialColor.SAND).hardnessAndResistance(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("end_wood"));
		registry.register(new FlammableLogBlock(MaterialColor.OBSIDIAN, Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("vine_wood"));
		registry.register(new FlammableLogBlock(MaterialColor.OBSIDIAN, Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("flowery_vine_wood"));
		registry.register(new FlammableLogBlock(MaterialColor.OBSIDIAN, Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("dead_wood"));
		registry.register(new FlammableLogBlock(MaterialColor.OBSIDIAN, 0, 0, Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F).harvestTool(ToolType.AXE).sound(SoundType.STONE)).setRegistryName("petrified_wood"));
		registry.register(new FlammableBlock(5, 20, Block.Properties.create(Material.WOOD, MaterialColor.LIGHT_BLUE).hardnessAndResistance(2.0F, 3.0F).harvestTool(ToolType.AXE).lightValue(7).sound(SoundType.WOOD)).setRegistryName("glowing_planks"));
		registry.register(new FlammableBlock(5, 5, Block.Properties.create(Material.WOOD, MaterialColor.ICE).hardnessAndResistance(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("frost_planks"));
		Block rainbowPlanks = register(registry, new FlammableBlock(5, 20, Block.Properties.create(Material.WOOD).hardnessAndResistance(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("rainbow_planks"));
		Block endPlanks = register(registry, new FlammableBlock(1, 250, Block.Properties.create(Material.WOOD, MaterialColor.SAND).hardnessAndResistance(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("end_planks"));
		Block deadPlanks = register(registry, new FlammableBlock(5, 5, Block.Properties.create(Material.WOOD).hardnessAndResistance(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("dead_planks"));
		Block treatedPlanks = register(registry, new FlammableBlock(0, 0, Block.Properties.create(Material.WOOD).hardnessAndResistance(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("treated_planks"));
		registry.register(new FlammableLeavesBlock(Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()).setRegistryName("frost_leaves"));
		registry.register(new FlammableLeavesBlock(Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()).setRegistryName("rainbow_leaves"));
		registry.register(new EndLeavesBlock(Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()).setRegistryName("end_leaves"));
		registry.register(new RainbowSaplingBlock(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0).sound(SoundType.PLANT)).setRegistryName("rainbow_sapling"));
		registry.register(new EndSaplingBlock(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0).sound(SoundType.PLANT)).setRegistryName("end_sapling"));
		
		registry.register(new FlammableLogBlock(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("blood_aspect_log"));
		registry.register(new FlammableLogBlock(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("breath_aspect_log"));
		registry.register(new FlammableLogBlock(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("doom_aspect_log"));
		registry.register(new FlammableLogBlock(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("heart_aspect_log"));
		registry.register(new FlammableLogBlock(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("hope_aspect_log"));
		registry.register(new FlammableLogBlock(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("life_aspect_log"));
		registry.register(new FlammableLogBlock(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("light_aspect_log"));
		registry.register(new FlammableLogBlock(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("mind_aspect_log"));
		registry.register(new FlammableLogBlock(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("rage_aspect_log"));
		registry.register(new FlammableLogBlock(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("space_aspect_log"));
		registry.register(new FlammableLogBlock(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("time_aspect_log"));
		registry.register(new FlammableLogBlock(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("void_aspect_log"));
		registry.register(new FlammableBlock(5, 20, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("blood_aspect_planks"));
		registry.register(new FlammableBlock(5, 20, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("breath_aspect_planks"));
		registry.register(new FlammableBlock(5, 20, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("doom_aspect_planks"));
		registry.register(new FlammableBlock(5, 20, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("heart_aspect_planks"));
		registry.register(new FlammableBlock(5, 20, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("hope_aspect_planks"));
		registry.register(new FlammableBlock(5, 20, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("life_aspect_planks"));
		registry.register(new FlammableBlock(5, 20, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("light_aspect_planks"));
		registry.register(new FlammableBlock(5, 20, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("mind_aspect_planks"));
		registry.register(new FlammableBlock(5, 20, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("rage_aspect_planks"));
		registry.register(new FlammableBlock(5, 20, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("space_aspect_planks"));
		registry.register(new FlammableBlock(5, 20, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("time_aspect_planks"));
		registry.register(new FlammableBlock(5, 20, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("void_aspect_planks"));
		registry.register(new FlammableLeavesBlock(Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()).setRegistryName("blood_aspect_leaves"));
		registry.register(new FlammableLeavesBlock(Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()).setRegistryName("breath_aspect_leaves"));
		registry.register(new FlammableLeavesBlock(Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()).setRegistryName("doom_aspect_leaves"));
		registry.register(new FlammableLeavesBlock(Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()).setRegistryName("heart_aspect_leaves"));
		registry.register(new FlammableLeavesBlock(Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()).setRegistryName("hope_aspect_leaves"));
		registry.register(new FlammableLeavesBlock(Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()).setRegistryName("life_aspect_leaves"));
		registry.register(new FlammableLeavesBlock(Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()).setRegistryName("light_aspect_leaves"));
		registry.register(new FlammableLeavesBlock(Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()).setRegistryName("mind_aspect_leaves"));
		registry.register(new FlammableLeavesBlock(Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()).setRegistryName("rage_aspect_leaves"));
		registry.register(new FlammableLeavesBlock(Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()).setRegistryName("space_aspect_leaves"));
		registry.register(new FlammableLeavesBlock(Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()).setRegistryName("time_aspect_leaves"));
		registry.register(new FlammableLeavesBlock(Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()).setRegistryName("void_aspect_leaves"));
		registry.register(new AspectSaplingBlock(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0).sound(SoundType.PLANT)).setRegistryName("blood_aspect_sapling"));
		registry.register(new AspectSaplingBlock(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0).sound(SoundType.PLANT)).setRegistryName("breath_aspect_sapling"));
		registry.register(new AspectSaplingBlock(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0).sound(SoundType.PLANT)).setRegistryName("doom_aspect_sapling"));
		registry.register(new AspectSaplingBlock(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0).sound(SoundType.PLANT)).setRegistryName("heart_aspect_sapling"));
		registry.register(new AspectSaplingBlock(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0).sound(SoundType.PLANT)).setRegistryName("hope_aspect_sapling"));
		registry.register(new AspectSaplingBlock(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0).sound(SoundType.PLANT)).setRegistryName("life_aspect_sapling"));
		registry.register(new AspectSaplingBlock(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0).sound(SoundType.PLANT)).setRegistryName("light_aspect_sapling"));
		registry.register(new AspectSaplingBlock(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0).sound(SoundType.PLANT)).setRegistryName("mind_aspect_sapling"));
		registry.register(new AspectSaplingBlock(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0).sound(SoundType.PLANT)).setRegistryName("rage_aspect_sapling"));
		registry.register(new AspectSaplingBlock(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0).sound(SoundType.PLANT)).setRegistryName("space_aspect_sapling"));
		registry.register(new AspectSaplingBlock(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0).sound(SoundType.PLANT)).setRegistryName("time_aspect_sapling"));
		registry.register(new AspectSaplingBlock(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0).sound(SoundType.PLANT)).setRegistryName("void_aspect_sapling"));
		
		registry.register(new GlowingMushroomBlock(Block.Properties.create(Material.PLANTS, MaterialColor.DIAMOND).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0).sound(SoundType.PLANT).lightValue(11)).setRegistryName("glowing_mushroom"));
		registry.register(new DesertFloraBlock(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.PLANT)).setRegistryName("desert_bush"));
		registry.register(new DesertFloraBlock(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.PLANT)).setRegistryName("blooming_cactus"));
		registry.register(new PetrifiedFloraBlock(Block.Properties.create(Material.ROCK, DyeColor.GRAY).doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.STONE)).setRegistryName("petrified_grass"));
		registry.register(new PetrifiedFloraBlock(Block.Properties.create(Material.ROCK, DyeColor.GRAY).doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.STONE)).setRegistryName("petrified_poppy"));
		registry.register(new StrawberryBlock(Block.Properties.create(Material.GOURD, MaterialColor.RED).hardnessAndResistance(1.0F).sound(SoundType.WOOD)).setRegistryName("strawberry"));
		registry.register(new StrawberryBlock.AttachedStem((StemGrownBlock) STRAWBERRY, Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0).sound(SoundType.WOOD)).setRegistryName("attached_strawberry_stem"));
		registry.register(new StrawberryBlock.Stem((StemGrownBlock) STRAWBERRY, Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0).sound(SoundType.WOOD)).setRegistryName("strawberry_stem"));
		registry.register(new TallEndGrassBlock(Block.Properties.create(Material.TALL_PLANTS, DyeColor.GREEN).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0.1F).sound(SoundType.NETHER_WART)).setRegistryName("tall_end_grass"));
		registry.register(new FlowerBlock(Effects.GLOWING, 20, Block.Properties.create(Material.PLANTS, DyeColor.YELLOW).doesNotBlockMovement().hardnessAndResistance(0).lightValue(12).sound(SoundType.PLANT)).setRegistryName("glowflower"));
		
		registry.register(new GoopBlock(Block.Properties.create(Material.CLAY).hardnessAndResistance(0.1F).sound(SoundType.SLIME).lightValue(14)).setRegistryName("glowy_goop"));
		registry.register(new GoopBlock(Block.Properties.create(Material.CLAY).hardnessAndResistance(0.1F).sound(SoundType.SLIME)).setRegistryName("coagulated_blood"));
		registry.register(new VeinBlock(Block.Properties.create(Material.WOOD).hardnessAndResistance(0.45F).sound(SoundType.SLIME)).setRegistryName("vein"));
		registry.register(new VeinCornerBlock(Block.Properties.create(Material.WOOD).hardnessAndResistance(0.45F).sound(SoundType.SLIME)).setRegistryName("vein_corner"));
		registry.register(new VeinCornerBlock(Block.Properties.create(Material.WOOD).hardnessAndResistance(0.45F).sound(SoundType.SLIME)).setRegistryName("inverted_vein_corner"));
		registry.register(new PipeBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(4.0F).sound(SoundType.METAL), MSBlockShapes.PIPE).setRegistryName("pipe"));
		registry.register(new Block(Block.Properties.create(Material.IRON).hardnessAndResistance(4.0F).sound(SoundType.METAL)).setRegistryName("pipe_intersection"));
		registry.register(new StoneTabletBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(0.3F)).setRegistryName("stone_slab")); //same thing as stone tablet
		registry.register(new DecorBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(0.5F), MSBlockShapes.NAKAGATOR_STATUE).setRegistryName("nakagator_statue"));
		
		registry.register(new StairsBlock(() -> MSBlocks.BLACK_CASTLE_BRICKS.getDefaultState(), Block.Properties.from(blackCastleBricks)).setRegistryName("black_castle_brick_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.DARK_GRAY_CASTLE_BRICKS.getDefaultState(), Block.Properties.from(darkGrayCastleBricks)).setRegistryName("dark_gray_castle_brick_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.LIGHT_GRAY_CASTLE_BRICKS.getDefaultState(), Block.Properties.from(lightGrayCastleBricks)).setRegistryName("light_gray_castle_brick_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.WHITE_CASTLE_BRICKS.getDefaultState(), Block.Properties.from(whiteCastleBricks)).setRegistryName("white_castle_brick_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.COARSE_STONE.getDefaultState(), Block.Properties.from(coarseStone)).setRegistryName("coarse_stone_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.SHADE_BRICKS.getDefaultState(), Block.Properties.from(shadeBricks)).setRegistryName("shade_brick_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.FROST_BRICKS.getDefaultState(), Block.Properties.from(frostBricks)).setRegistryName("frost_brick_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.CAST_IRON.getDefaultState(), Block.Properties.from(castIron)).setRegistryName("cast_iron_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.MYCELIUM_BRICKS.getDefaultState(), Block.Properties.from(myceliumBricks)).setRegistryName("mycelium_brick_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.CHALK.getDefaultState(), Block.Properties.from(chalk)).setRegistryName("chalk_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.CHALK_BRICKS.getDefaultState(), Block.Properties.from(chalkBricks)).setRegistryName("chalk_brick_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.PINK_STONE_BRICKS.getDefaultState(), Block.Properties.from(pinkStoneBricks)).setRegistryName("pink_stone_brick_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.BROWN_STONE_BRICKS.getDefaultState(), Block.Properties.from(brownStoneBricks)).setRegistryName("brown_stone_brick_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.GREEN_STONE_BRICKS.getDefaultState(), Block.Properties.from(greenStoneBricks)).setRegistryName("green_stone_brick_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.RAINBOW_PLANKS.getDefaultState(), Block.Properties.from(rainbowPlanks)).setRegistryName("rainbow_planks_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.END_PLANKS.getDefaultState(), Block.Properties.from(endPlanks)).setRegistryName("end_planks_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.DEAD_PLANKS.getDefaultState(), Block.Properties.from(deadPlanks)).setRegistryName("dead_planks_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.TREATED_PLANKS.getDefaultState(), Block.Properties.from(treatedPlanks)).setRegistryName("treated_planks_stairs"));
		registry.register(new DecorBlock(Block.Properties.from(greenStoneBricks), MSBlockShapes.STEEP_STAIRS_BASE).setRegistryName("steep_green_stone_brick_stairs_base"));
		registry.register(new DecorBlock(Block.Properties.from(greenStoneBricks), MSBlockShapes.STEEP_STAIRS_TOP).setRegistryName("steep_green_stone_brick_stairs_top"));
		registry.register(new SlabBlock(Block.Properties.from(blackCastleBricks)).setRegistryName("black_castle_brick_slab"));
		registry.register(new SlabBlock(Block.Properties.from(darkGrayCastleBricks)).setRegistryName("dark_gray_castle_brick_slab"));
		registry.register(new SlabBlock(Block.Properties.from(lightGrayCastleBricks)).setRegistryName("light_gray_castle_brick_slab"));
		registry.register(new SlabBlock(Block.Properties.from(whiteCastleBricks)).setRegistryName("white_castle_brick_slab"));
		registry.register(new SlabBlock(Block.Properties.from(chalk)).setRegistryName("chalk_slab"));
		registry.register(new SlabBlock(Block.Properties.from(chalkBricks)).setRegistryName("chalk_brick_slab"));
		registry.register(new SlabBlock(Block.Properties.from(pinkStoneBricks)).setRegistryName("pink_stone_brick_slab"));
		registry.register(new SlabBlock(Block.Properties.from(brownStoneBricks)).setRegistryName("brown_stone_brick_slab"));
		registry.register(new SlabBlock(Block.Properties.from(greenStoneBricks)).setRegistryName("green_stone_brick_slab"));
		registry.register(new SlabBlock(Block.Properties.from(rainbowPlanks)).setRegistryName("rainbow_planks_slab"));
		registry.register(new SlabBlock(Block.Properties.from(endPlanks)).setRegistryName("end_planks_slab"));
		registry.register(new SlabBlock(Block.Properties.from(deadPlanks)).setRegistryName("dead_planks_slab"));
		registry.register(new SlabBlock(Block.Properties.from(treatedPlanks)).setRegistryName("treated_planks_slab"));
		
		registry.register(new TrajectoryBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3).sound(SoundType.METAL)).setRegistryName("trajectory_block"));
		registry.register(new StatStorerBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3).sound(SoundType.METAL)).setRegistryName("stat_storer"));
		registry.register(new RemoteObserverBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3).sound(SoundType.METAL)).setRegistryName("remote_observer"));
		registry.register(new WirelessRedstoneTransmitterBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3).sound(SoundType.METAL)).setRegistryName("wireless_redstone_transmitter"));
		registry.register(new WirelessRedstoneReceiverBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3).tickRandomly().sound(SoundType.METAL)).setRegistryName("wireless_redstone_receiver"));
		registry.register(new SolidSwitchBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3).sound(SoundType.METAL).lightValue(15)).setRegistryName("solid_switch"));
		registry.register(new VariableSolidSwitchBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3).sound(SoundType.METAL).lightValue(15)).setRegistryName("variable_solid_switch"));
		registry.register(new TimedSolidSwitchBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3).sound(SoundType.METAL).lightValue(12), 20).setRegistryName("one_second_interval_timed_solid_switch"));
		registry.register(new TimedSolidSwitchBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3).sound(SoundType.METAL).lightValue(15), 40).setRegistryName("two_second_interval_timed_solid_switch"));
		registry.register(new SummonerBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3).sound(SoundType.METAL)).setRegistryName("summoner"));
		registry.register(new AreaEffectBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(6).sound(SoundType.METAL)).setRegistryName("area_effect_block"));
		registry.register(new PlatformGeneratorBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(6).sound(SoundType.METAL)).setRegistryName("platform_generator"));
		registry.register(new PlatformBlock(Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(0.2F).sound(SoundType.SCAFFOLDING).lightValue(6).notSolid()).setRegistryName("platform_block"));
		registry.register(new ItemMagnetBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3).sound(SoundType.METAL)).setRegistryName("item_magnet"));
		registry.register(new RedstoneClockBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3).sound(SoundType.METAL)).setRegistryName("redstone_clock"));
		registry.register(new LogicGateBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(1).sound(SoundType.STONE), LogicGateBlock.State.AND).setRegistryName("and_gate_block"));
		registry.register(new LogicGateBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(1).sound(SoundType.STONE), LogicGateBlock.State.OR).setRegistryName("or_gate_block"));
		registry.register(new LogicGateBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(1).sound(SoundType.STONE), LogicGateBlock.State.XOR).setRegistryName("xor_gate_block"));
		registry.register(new LogicGateBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(1).sound(SoundType.STONE), LogicGateBlock.State.NAND).setRegistryName("nand_gate_block"));
		registry.register(new LogicGateBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(1).sound(SoundType.STONE), LogicGateBlock.State.NOR).setRegistryName("nor_gate_block"));
		registry.register(new LogicGateBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(1).sound(SoundType.STONE), LogicGateBlock.State.XNOR).setRegistryName("xnor_gate_block"));
		
		registry.register(new GateBlock(Block.Properties.create(Material.PORTAL).doesNotBlockMovement().hardnessAndResistance(-1.0F, 25.0F).sound(SoundType.GLASS).lightValue(11).noDrops()).setRegistryName("gate"));
		registry.register(new ReturnNodeBlock(Block.Properties.create(Material.PORTAL).doesNotBlockMovement().hardnessAndResistance(-1.0F, 10.0F).sound(SoundType.GLASS).lightValue(11).noDrops()).setRegistryName("return_node"));
		
		CRUXTRUDER.registerBlocks(registry);
		registry.register(new CruxtruderLidBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F)).setRegistryName("cruxtruder_lid"));
		TOTEM_LATHE.registerBlocks(registry);
		ALCHEMITER.registerBlocks(registry);
		PUNCH_DESIGNIX.registerBlocks(registry);
		registry.register(new SmallMachineBlock<>(MSBlockShapes.SMALL_CRUXTRUDER.createRotatedShapes(), MSTileEntityTypes.MINI_CRUXTRUDER, Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("mini_cruxtruder"));
		registry.register(new SmallMachineBlock<>(MSBlockShapes.SMALL_TOTEM_LATHE.createRotatedShapes(), MSTileEntityTypes.MINI_TOTEM_LATHE, Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("mini_totem_lathe"));
		registry.register(new MiniAlchemiterBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("mini_alchemiter"));
		registry.register(new SmallMachineBlock<>(MSBlockShapes.SMALL_PUNCH_DESIGNIX.createRotatedShapes(), MSTileEntityTypes.MINI_PUNCH_DESIGNIX, Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("mini_punch_designix"));
		registry.register(new HolopadBlock(Block.Properties.create(Material.IRON, MaterialColor.SNOW).hardnessAndResistance(3.0F)).setRegistryName("holopad"));
		
		registry.register(new ComputerBlock(ComputerBlock.COMPUTER_SHAPE, ComputerBlock.COMPUTER_SHAPE, Block.Properties.create(Material.IRON).hardnessAndResistance(4.0F)).setRegistryName("computer"));
		registry.register(new ComputerBlock(ComputerBlock.LAPTOP_OPEN_SHAPE, ComputerBlock.LAPTOP_CLOSED_SHAPE, Block.Properties.create(Material.IRON).hardnessAndResistance(4.0F)).setRegistryName("laptop"));
		registry.register(new ComputerBlock(ComputerBlock.LAPTOP_OPEN_SHAPE, ComputerBlock.LAPTOP_CLOSED_SHAPE, Block.Properties.create(Material.IRON, MaterialColor.RED).hardnessAndResistance(4.0F)).setRegistryName("crockertop"));
		registry.register(new ComputerBlock(ComputerBlock.LAPTOP_OPEN_SHAPE, ComputerBlock.LAPTOP_CLOSED_SHAPE, Block.Properties.create(Material.IRON, MaterialColor.GREEN).hardnessAndResistance(4.0F)).setRegistryName("hubtop"));
		registry.register(new ComputerBlock(ComputerBlock.LUNCHTOP_OPEN_SHAPE, ComputerBlock.LUNCHTOP_CLOSED_SHAPE, Block.Properties.create(Material.IRON, MaterialColor.RED).hardnessAndResistance(4.0F)).setRegistryName("lunchtop"));
		registry.register(new ComputerBlock(ComputerBlock.OLD_COMPUTER_SHAPE, ComputerBlock.OLD_COMPUTER_SHAPE, Block.Properties.create(Material.IRON).hardnessAndResistance(4.0F)).setRegistryName("old_computer"));
		registry.register(new TransportalizerBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("transportalizer"));
		registry.register(new TransportalizerBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("trans_portalizer"));
		registry.register(new SendificatorBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F), MSBlockShapes.SENDIFICATOR).setRegistryName("sendificator"));
		registry.register(new GristWidgetBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("grist_widget"));
		registry.register(new SmallMachineBlock<>(new CustomVoxelShape(new double[]{4, 0, 4, 12, 6, 12}).createRotatedShapes(), MSTileEntityTypes.URANIUM_COOKER, Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("uranium_cooker"));
		
		registry.register(new CruxiteDowelBlock(Block.Properties.create(Material.GLASS).hardnessAndResistance(0.0F)).setRegistryName("cruxite_dowel"));
		
		registry.register(new GoldSeedsBlock(Block.Properties.create(Material.PLANTS).hardnessAndResistance(0.1F).sound(SoundType.METAL).doesNotBlockMovement()).setRegistryName("gold_seeds"));
		registry.register(new SpecialCactusBlock(Block.Properties.create(Material.WOOD).tickRandomly().hardnessAndResistance(1.0F, 2.5F).sound(SoundType.WOOD).harvestTool(ToolType.AXE)).setRegistryName("wooden_cactus"));
		
		registry.register(new SimpleCakeBlock(Block.Properties.create(Material.CAKE).hardnessAndResistance(0.5F).sound(SoundType.CLOTH), 2, 0.5F, null).setRegistryName("apple_cake"));
		registry.register(new SimpleCakeBlock(Block.Properties.create(Material.CAKE).hardnessAndResistance(0.5F).sound(SoundType.CLOTH), 2, 0.3F, player -> player.addPotionEffect(new EffectInstance(Effects.SPEED, 150, 0))).setRegistryName("blue_cake"));
		registry.register(new SimpleCakeBlock(Block.Properties.create(Material.CAKE).hardnessAndResistance(0.5F).sound(SoundType.CLOTH), 2, 0.3F, player -> {player.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 200, 1));player.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 200, 1));}).setRegistryName("cold_cake"));
		registry.register(new SimpleCakeBlock(Block.Properties.create(Material.CAKE).hardnessAndResistance(0.5F).sound(SoundType.CLOTH), 2, 0.1F, player -> player.heal(1)).setRegistryName("red_cake"));
		registry.register(new SimpleCakeBlock(Block.Properties.create(Material.CAKE).hardnessAndResistance(0.5F).sound(SoundType.CLOTH), 2, 0.1F, player -> player.setFire(4)).setRegistryName("hot_cake"));
		registry.register(new SimpleCakeBlock(Block.Properties.create(Material.CAKE).hardnessAndResistance(0.5F).sound(SoundType.CLOTH), 2, 0.1F, null).setRegistryName("reverse_cake"));
		registry.register(new SimpleCakeBlock(Block.Properties.create(Material.CAKE).hardnessAndResistance(0.5F).sound(SoundType.CLOTH), 3, 0.5F, player -> {player.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 350, 1));player.addPotionEffect(new EffectInstance(Effects.REGENERATION, 200, 0));}).setRegistryName("fuchsia_cake"));
		registry.register(new SimpleCakeBlock(Block.Properties.create(Material.CAKE).hardnessAndResistance(0.5F).sound(SoundType.CLOTH), 2, 0.3F, player -> {player.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 300, 0));player.addPotionEffect(new EffectInstance(Effects.INVISIBILITY, 250, 0));}).setRegistryName("negative_cake"));
		
		registry.register(new SpecialTNTBlock(Block.Properties.create(Material.TNT).hardnessAndResistance(0.0F).sound(SoundType.PLANT), true, false, false).setRegistryName("primed_tnt"));
		registry.register(new SpecialTNTBlock(Block.Properties.create(Material.TNT).hardnessAndResistance(0.0F).sound(SoundType.PLANT).tickRandomly(), false, true, false).setRegistryName("unstable_tnt"));
		registry.register(new SpecialTNTBlock(Block.Properties.create(Material.TNT).hardnessAndResistance(0.0F).sound(SoundType.PLANT), false, false, true).setRegistryName("instant_tnt"));
		registry.register(new SpecialButtonBlock(Block.Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.5F).sound(SoundType.WOOD), true, true).setRegistryName("wooden_explosive_button"));
		registry.register(new SpecialButtonBlock(Block.Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.5F).sound(SoundType.STONE), true, false).setRegistryName("stone_explosive_button"));
		
		registry.register(new DecorBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(0.5F).sound(SoundType.METAL), MSBlockShapes.BLENDER).setRegistryName("blender"));
		registry.register(new DecorBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(0.5F), MSBlockShapes.CHESSBOARD).setRegistryName("chessboard"));
		registry.register(new DecorBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(0.5F), MSBlockShapes.FROG_STATUE).setRegistryName("mini_frog_statue"));
		registry.register(new DecorBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(0.5F), MSBlockShapes.WIZARD_STATUE).setRegistryName("mini_wizard_statue"));
		registry.register(new CassettePlayerBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(0.5F).sound(SoundType.METAL), MSBlockShapes.CASSETTE_PLAYER).setRegistryName("cassette_player"));
		registry.register(new GlowystoneWireBlock(Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(0.0F).lightValue(16).doesNotBlockMovement()).setRegistryName("glowystone_dust"));
		registry.register(new DecorBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(4.0F), MSBlockShapes.PARCEL_PYXIS).setRegistryName("parcel_pyxis"));
		registry.register(new DecorBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F), MSBlockShapes.PYXIS_LID).setRegistryName("pyxis_lid"));
		
		LOTUS_TIME_CAPSULE_BLOCK.registerBlocks(registry);
		
		registry.register(new FlowingModFluidBlock(MSFluids.OIL, new Vec3d(0.0, 0.0, 0.0), 0.75f, Block.Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops()).setRegistryName("oil"));
		registry.register(new FlowingModFluidBlock(MSFluids.BLOOD, new Vec3d(0.8, 0.0, 0.0), 0.25f, Block.Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops()).setRegistryName("blood"));
		registry.register(new FlowingModFluidBlock(MSFluids.BRAIN_JUICE, new Vec3d(0.55, 0.25, 0.7), 0.25f, Block.Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops()).setRegistryName("brain_juice"));
		registry.register(new FlowingWaterColorsBlock(MSFluids.WATER_COLORS, 0.01f, Block.Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops()).setRegistryName("water_colors"));
		registry.register(new FlowingModFluidBlock(MSFluids.ENDER, new Vec3d(0, 0.35, 0.35), (Float.MAX_VALUE), Block.Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops()).setRegistryName("ender"));
		registry.register(new FlowingModFluidBlock(MSFluids.LIGHT_WATER, new Vec3d(0.2, 0.3, 1.0), 0.01f, Block.Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops()).setRegistryName("light_water"));
	}
	
	private static Block register(IForgeRegistry<Block> registry, Block block) //Used because registry.register doesn't return the registered block
	{
		registry.register(block);
		return block;
	}
}
