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
import net.minecraft.entity.EntityType;
import net.minecraft.item.DyeColor;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;
import java.util.function.Function;

@ObjectHolder(Minestuck.MOD_ID)
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class MSBlocks
{
	//TODO: rename all instances of "CASTLE_BRICKS" to "CHESS_BRICKS" (Black, DGrey, LGrey, White)
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
	public static final Block COARSE_STONE = getNull(), CHISELED_COARSE_STONE = getNull(), COARSE_STONE_BRICKS = getNull(), COARSE_STONE_COLUMN = getNull(), CHISELED_COARSE_STONE_BRICKS = getNull(), CRACKED_COARSE_STONE_BRICKS = getNull(), MOSSY_COARSE_STONE = getNull();
	public static final Block SHADE_STONE = getNull(), SMOOTH_SHADE_STONE = getNull(), SHADE_BRICKS = getNull(), SHADE_COLUMN = getNull(), CHISELED_SHADE_BRICKS = getNull(), CRACKED_SHADE_BRICKS = getNull(), MOSSY_SHADE_BRICKS = getNull(), BLOOD_SHADE_BRICKS = getNull(), TAR_SHADE_BRICKS = getNull();
	public static final Block FROST_BRICKS = getNull(), FROST_TILE = getNull(), CHISELED_FROST_TILE = getNull(), FROST_COLUMN = getNull(),  CHISELED_FROST_BRICKS = getNull(), CRACKED_FROST_BRICKS = getNull(), FLOWERY_FROST_BRICKS = getNull();
	public static final Block CAST_IRON = getNull(), CHISELED_CAST_IRON = getNull();
	public static final Block MYCELIUM_COBBLESTONE = getNull(), MYCELIUM_STONE = getNull(), POLISHED_MYCELIUM_STONE = getNull(), MYCELIUM_BRICKS  = getNull(), MYCELIUM_COLUMN = getNull();
	public static final Block CHISELED_MYCELIUM_BRICKS = getNull(), CRACKED_MYCELIUM_BRICKS = getNull(), MOSSY_MYCELIUM_BRICKS = getNull(), FLOWERY_MYCELIUM_BRICKS = getNull();
	public static final Block STEEL_BEAM = getNull();
	public static final Block BLACK_STONE = getNull(), BLACK_COBBLESTONE = getNull(), POLISHED_BLACK_STONE = getNull(), BLACK_STONE_BRICKS = getNull(), BLACK_STONE_COLUMN = getNull(), CHISELED_BLACK_STONE_BRICKS = getNull(), CRACKED_BLACK_STONE_BRICKS = getNull(), BLACK_SAND = getNull();
	public static final Block DECREPIT_STONE_BRICKS = getNull(), FLOWERY_MOSSY_COBBLESTONE = getNull(), MOSSY_DECREPIT_STONE_BRICKS = getNull(), FLOWERY_MOSSY_STONE_BRICKS = getNull();
	public static final Block COARSE_END_STONE = getNull(), END_GRASS = getNull();
	public static final Block CHALK = getNull(), POLISHED_CHALK = getNull(), CHALK_BRICKS = getNull(), CHALK_COLUMN = getNull(), CHISELED_CHALK_BRICKS = getNull(), MOSSY_CHALK_BRICKS = getNull(), FLOWERY_CHALK_BRICKS = getNull();
	public static final Block PINK_STONE = getNull(), POLISHED_PINK_STONE = getNull(), PINK_STONE_BRICKS = getNull(), CHISELED_PINK_STONE_BRICKS = getNull(), PINK_STONE_COLUMN = getNull();
	public static final Block CRACKED_PINK_STONE_BRICKS = getNull(), MOSSY_PINK_STONE_BRICKS = getNull();
	public static final Block BROWN_STONE = getNull(), POLISHED_BROWN_STONE = getNull(), BROWN_STONE_BRICKS = getNull(), CRACKED_BROWN_STONE_BRICKS = getNull(), BROWN_STONE_COLUMN = getNull();
	public static final Block GREEN_STONE = getNull(), POLISHED_GREEN_STONE = getNull(), GREEN_STONE_BRICKS = getNull(), GREEN_STONE_COLUMN = getNull(), CHISELED_GREEN_STONE_BRICKS = getNull(), HORIZONTAL_GREEN_STONE_BRICKS = getNull(), VERTICAL_GREEN_STONE_BRICKS = getNull()
			, GREEN_STONE_BRICK_TRIM = getNull(), GREEN_STONE_BRICK_FROG = getNull(), GREEN_STONE_BRICK_IGUANA_LEFT = getNull(), GREEN_STONE_BRICK_IGUANA_RIGHT = getNull(), GREEN_STONE_BRICK_LOTUS = getNull(), GREEN_STONE_BRICK_NAK_LEFT = getNull(), GREEN_STONE_BRICK_NAK_RIGHT = getNull()
			, GREEN_STONE_BRICK_SALAMANDER_LEFT = getNull(), GREEN_STONE_BRICK_SALAMANDER_RIGHT = getNull(), GREEN_STONE_BRICK_SKAIA = getNull(), GREEN_STONE_BRICK_TURTLE = getNull();
	public static final Block SANDSTONE_COLUMN = getNull(), CHISELED_SANDSTONE_COLUMN = getNull(), RED_SANDSTONE_COLUMN = getNull(), CHISELED_RED_SANDSTONE_COLUMN = getNull();
	public static final Block UNCARVED_WOOD = getNull(), CHIPBOARD = getNull(), WOOD_SHAVINGS = getNull();
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
	public static final Block COARSE_STONE_STAIRS = getNull(), COARSE_STONE_BRICK_STAIRS = getNull(), SHADE_STAIRS = getNull(), SHADE_BRICK_STAIRS = getNull(), FROST_TILE_STAIRS = getNull(), FROST_BRICK_STAIRS = getNull(), CAST_IRON_STAIRS = getNull(), BLACK_STONE_STAIRS = getNull(), BLACK_STONE_BRICK_STAIRS = getNull();
	public static final Block FLOWERY_MOSSY_STONE_BRICK_STAIRS = getNull(), MYCELIUM_STAIRS = getNull(), MYCELIUM_BRICK_STAIRS = getNull(), CHALK_STAIRS = getNull(), CHALK_BRICK_STAIRS = getNull(), PINK_STONE_STAIRS = getNull(), PINK_STONE_BRICK_STAIRS = getNull(), BROWN_STONE_STAIRS = getNull(), BROWN_STONE_BRICK_STAIRS = getNull(), GREEN_STONE_STAIRS = getNull(), GREEN_STONE_BRICK_STAIRS = getNull();
	public static final Block RAINBOW_PLANKS_STAIRS = getNull(), END_PLANKS_STAIRS = getNull(), DEAD_PLANKS_STAIRS = getNull(), TREATED_PLANKS_STAIRS = getNull();
	public static final Block STEEP_GREEN_STONE_BRICK_STAIRS_BASE = getNull(), STEEP_GREEN_STONE_BRICK_STAIRS_TOP = getNull();
	public static final Block BLACK_CASTLE_BRICK_SLAB = getNull(), DARK_GRAY_CASTLE_BRICK_SLAB = getNull(), LIGHT_GRAY_CASTLE_BRICK_SLAB = getNull(), WHITE_CASTLE_BRICK_SLAB = getNull();
	public static final Block FLOWERY_MOSSY_STONE_BRICK_SLAB = getNull(), COARSE_STONE_SLAB = getNull(), COARSE_STONE_BRICK_SLAB = getNull(), SHADE_SLAB = getNull(), SHADE_BRICK_SLAB = getNull(), FROST_TILE_SLAB = getNull(), FROST_BRICK_SLAB = getNull(), BLACK_STONE_SLAB = getNull(), BLACK_STONE_BRICK_SLAB = getNull(), MYCELIUM_SLAB = getNull(), MYCELIUM_BRICK_SLAB = getNull(), CHALK_SLAB = getNull(), CHALK_BRICK_SLAB = getNull(), PINK_STONE_SLAB = getNull(), PINK_STONE_BRICK_SLAB = getNull(), BROWN_STONE_SLAB = getNull(), BROWN_STONE_BRICK_SLAB = getNull(), GREEN_STONE_SLAB = getNull(), GREEN_STONE_BRICK_SLAB = getNull();
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
	public static final Block PLATFORM_RECEPTACLE = getNull();
	public static final Block ITEM_MAGNET = getNull();
	public static final Block REDSTONE_CLOCK = getNull();
	public static final Block ROTATOR = getNull();
	public static final Block FALL_PAD = getNull();
	public static final Block FRAGILE_STONE = getNull();
	public static final Block RETRACTABLE_SPIKES = getNull();
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
	public static final Block CARROT_CAKE = getNull();
	
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
	public static final Block MINI_TYPHEUS_STATUE = getNull();
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
		
		//Skaia Blocks
		registry.register(new Block(AbstractBlock.Properties.of(Material.DIRT, MaterialColor.COLOR_BLACK).strength(0.5F).harvestTool(ToolType.SHOVEL).sound(SoundType.GRAVEL)).setRegistryName("black_chess_dirt"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.DIRT, MaterialColor.SNOW).strength(0.5F).harvestTool(ToolType.SHOVEL).sound(SoundType.GRAVEL)).setRegistryName("white_chess_dirt"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.DIRT, MaterialColor.COLOR_GRAY).strength(0.5F).harvestTool(ToolType.SHOVEL).sound(SoundType.GRAVEL)).setRegistryName("dark_gray_chess_dirt"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.DIRT, MaterialColor.COLOR_LIGHT_GRAY).strength(0.5F).harvestTool(ToolType.SHOVEL).sound(SoundType.GRAVEL)).setRegistryName("light_gray_chess_dirt"));
		registry.register(new SkaiaPortalBlock(AbstractBlock.Properties.of(Material.PORTAL, MaterialColor.COLOR_CYAN).noCollission().lightLevel(state -> 11).strength(-1.0F, 3600000.0F).noDrops()).setRegistryName("skaia_portal"));
		Block blackChessBricks = register(registry, new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_BLACK).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("black_castle_bricks"));
		Block darkGrayChessBricks = register(registry, new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_GRAY).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("dark_gray_castle_bricks"));
		Block lightGrayChessBricks = register(registry, new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("light_gray_castle_bricks"));
		Block whiteChessBricks = register(registry, new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_WHITE).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("white_castle_bricks"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_BLACK).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("black_castle_brick_smooth"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_GRAY).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("dark_gray_castle_brick_smooth"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("light_gray_castle_brick_smooth"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_WHITE).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("white_castle_brick_smooth"));
		registry.register(new MSDirectionalBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_BLACK).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("black_castle_brick_trim"));
		registry.register(new MSDirectionalBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_GRAY).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("dark_gray_castle_brick_trim"));
		registry.register(new MSDirectionalBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_LIGHT_GRAY).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("light_gray_castle_brick_trim"));
		registry.register(new MSDirectionalBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_WHITE).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("white_castle_brick_trim"));
		registry.register(new StainedGlassBlock(DyeColor.BLUE, AbstractBlock.Properties.of(Material.GLASS, DyeColor.BLUE).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn(MSBlocks::never).isRedstoneConductor(MSBlocks::never).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never)).setRegistryName("checkered_stained_glass"));
		registry.register(new StainedGlassBlock(DyeColor.BLUE, AbstractBlock.Properties.of(Material.GLASS, DyeColor.BLUE).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn(MSBlocks::never).isRedstoneConductor(MSBlocks::never).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never)).setRegistryName("black_crown_stained_glass"));
		registry.register(new StainedGlassBlock(DyeColor.BLUE, AbstractBlock.Properties.of(Material.GLASS, DyeColor.BLUE).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn(MSBlocks::never).isRedstoneConductor(MSBlocks::never).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never)).setRegistryName("black_pawn_stained_glass"));
		registry.register(new StainedGlassBlock(DyeColor.BLUE, AbstractBlock.Properties.of(Material.GLASS, DyeColor.BLUE).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn(MSBlocks::never).isRedstoneConductor(MSBlocks::never).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never)).setRegistryName("white_crown_stained_glass"));
		registry.register(new StainedGlassBlock(DyeColor.BLUE, AbstractBlock.Properties.of(Material.GLASS, DyeColor.BLUE).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn(MSBlocks::never).isRedstoneConductor(MSBlocks::never).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never)).setRegistryName("white_pawn_stained_glass"));
		
		//Ores
		registry.register(new CustomOreBlock(2, 5, AbstractBlock.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("stone_cruxite_ore"));
		registry.register(new CustomOreBlock(2, 5, AbstractBlock.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("netherrack_cruxite_ore"));
		registry.register(new CustomOreBlock(2, 5, AbstractBlock.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("cobblestone_cruxite_ore"));
		registry.register(new CustomOreBlock(2, 5, AbstractBlock.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("sandstone_cruxite_ore"));
		registry.register(new CustomOreBlock(2, 5, AbstractBlock.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("red_sandstone_cruxite_ore"));
		registry.register(new CustomOreBlock(2, 5, AbstractBlock.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("end_stone_cruxite_ore"));
		registry.register(new CustomOreBlock(2, 5, AbstractBlock.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("shade_stone_cruxite_ore"));
		registry.register(new CustomOreBlock(2, 5, AbstractBlock.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("pink_stone_cruxite_ore"));
		registry.register(new CustomOreBlock(2, 5, AbstractBlock.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(1).lightLevel(state -> 3)).setRegistryName("stone_uranium_ore"));
		registry.register(new CustomOreBlock(2, 5, AbstractBlock.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(1).lightLevel(state -> 3)).setRegistryName("netherrack_uranium_ore"));
		registry.register(new CustomOreBlock(2, 5, AbstractBlock.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(1).lightLevel(state -> 3)).setRegistryName("cobblestone_uranium_ore"));
		registry.register(new CustomOreBlock(2, 5, AbstractBlock.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(1).lightLevel(state -> 3)).setRegistryName("sandstone_uranium_ore"));
		registry.register(new CustomOreBlock(2, 5, AbstractBlock.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(1).lightLevel(state -> 3)).setRegistryName("red_sandstone_uranium_ore"));
		registry.register(new CustomOreBlock(2, 5, AbstractBlock.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(1).lightLevel(state -> 3)).setRegistryName("end_stone_uranium_ore"));
		registry.register(new CustomOreBlock(2, 5, AbstractBlock.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(1).lightLevel(state -> 3)).setRegistryName("shade_stone_uranium_ore"));
		registry.register(new CustomOreBlock(2, 5, AbstractBlock.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(1).lightLevel(state -> 3)).setRegistryName("pink_stone_uranium_ore"));
		registry.register(new CustomOreBlock(0, 2, AbstractBlock.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("netherrack_coal_ore"));
		registry.register(new CustomOreBlock(0, 2, AbstractBlock.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("shade_stone_coal_ore"));
		registry.register(new CustomOreBlock(0, 2, AbstractBlock.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("pink_stone_coal_ore"));
		registry.register(new CustomOreBlock(AbstractBlock.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("end_stone_iron_ore"));
		registry.register(new CustomOreBlock(AbstractBlock.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("sandstone_iron_ore"));
		registry.register(new CustomOreBlock(AbstractBlock.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("red_sandstone_iron_ore"));
		registry.register(new CustomOreBlock(AbstractBlock.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(2)).setRegistryName("sandstone_gold_ore"));
		registry.register(new CustomOreBlock(AbstractBlock.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(2)).setRegistryName("red_sandstone_gold_ore"));
		registry.register(new CustomOreBlock(AbstractBlock.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(2)).setRegistryName("shade_stone_gold_ore"));
		registry.register(new CustomOreBlock(AbstractBlock.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(2)).setRegistryName("pink_stone_gold_ore"));
		registry.register(new CustomOreBlock(1, 5, AbstractBlock.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(2)).setRegistryName("end_stone_redstone_ore"));
		registry.register(new CustomOreBlock(2, 5, AbstractBlock.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("stone_quartz_ore"));
		registry.register(new CustomOreBlock(2, 5, AbstractBlock.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("pink_stone_lapis_ore"));
		registry.register(new CustomOreBlock(3, 7, AbstractBlock.Properties.of(Material.STONE).strength(3.0F, 3.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(2)).setRegistryName("pink_stone_diamond_ore"));
		
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, DyeColor.LIGHT_BLUE).strength(3.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("cruxite_block"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, DyeColor.LIME).strength(3.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(0).lightLevel(state -> 7)).setRegistryName("uranium_block"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.VEGETABLE, DyeColor.LIME).strength(1.0F).sound(SoundType.WOOD)).setRegistryName("generic_object"));
		
		registry.register(new Block(AbstractBlock.Properties.of(Material.DIRT, MaterialColor.COLOR_BLUE).strength(0.5F).sound(SoundType.GRAVEL)).setRegistryName("blue_dirt"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.DIRT, MaterialColor.COLOR_LIGHT_GREEN).strength(0.5F).sound(SoundType.GRAVEL)).setRegistryName("thought_dirt"));
		Block coarseStone = register(registry, new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE).requiresCorrectToolForDrops().strength(2.0F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("coarse_stone"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE).requiresCorrectToolForDrops().strength(2.0F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("chiseled_coarse_stone"));
		Block coarseStoneBricks = register(registry, new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE).requiresCorrectToolForDrops().strength(2.0F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("coarse_stone_bricks"));
		registry.register(new MSDirectionalBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE).requiresCorrectToolForDrops().strength(2.0F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("coarse_stone_column"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE).requiresCorrectToolForDrops().strength(2.0F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("chiseled_coarse_stone_bricks"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE).requiresCorrectToolForDrops().strength(2.0F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("cracked_coarse_stone_bricks"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE).requiresCorrectToolForDrops().strength(2.0F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("mossy_coarse_stone"));
		Block shadeStone = register(registry, new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("shade_stone"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("smooth_shade_stone"));
		Block shadeBricks = register(registry, new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("shade_bricks"));
		registry.register(new MSDirectionalBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("shade_column"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("chiseled_shade_bricks"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("cracked_shade_bricks"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("mossy_shade_bricks"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("blood_shade_bricks"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("tar_shade_bricks"));
		Block frostTile = register(registry, new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.ICE).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("frost_tile"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.ICE).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("chiseled_frost_tile"));
		Block frostBricks = register(registry, new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.ICE).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("frost_bricks"));
		registry.register(new MSDirectionalBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.ICE).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("frost_column"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.ICE).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("chiseled_frost_bricks"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.ICE).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("cracked_frost_bricks"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.ICE).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("flowery_frost_bricks"));
		Block castIron = register(registry, new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.METAL).requiresCorrectToolForDrops().strength(3.0F, 9.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("cast_iron"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.METAL).requiresCorrectToolForDrops().strength(3.0F, 9.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("chiseled_cast_iron"));
		registry.register(new MSDirectionalBlock(AbstractBlock.Properties.of(Material.METAL, MaterialColor.METAL).requiresCorrectToolForDrops().strength(3.0F, 9.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("steel_beam"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_MAGENTA).requiresCorrectToolForDrops().strength(1.5f, 6.0f).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("mycelium_cobblestone"));
		Block myceliumStone = register(registry, new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_MAGENTA).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("mycelium_stone"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_MAGENTA).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("polished_mycelium_stone"));
		Block myceliumBricks = register(registry, new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_MAGENTA).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("mycelium_bricks"));
		registry.register(new MSDirectionalBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_MAGENTA).requiresCorrectToolForDrops().strength(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("mycelium_column"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_MAGENTA).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("chiseled_mycelium_bricks"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_MAGENTA).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("cracked_mycelium_bricks"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_MAGENTA).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("mossy_mycelium_bricks"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_MAGENTA).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("flowery_mycelium_bricks"));
		Block blackStone = register(registry, new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(2.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("black_stone"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(2.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("polished_black_stone"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(2.5f,6.0f).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("black_cobblestone"));
		Block blackStoneBricks = register(registry, new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(2.5f,6.0f).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("black_stone_bricks"));
		registry.register(new MSDirectionalBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(2.5f,6.0f).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("black_stone_column"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(2.5f,6.0f).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("cracked_black_stone_bricks"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(2.5f,6.0f).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("chiseled_black_stone_bricks"));
		registry.register(new SandBlock(0x181915, AbstractBlock.Properties.of(Material.SAND, MaterialColor.COLOR_BLACK).strength(0.5F).sound(SoundType.SAND)).setRegistryName("black_sand"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("decrepit_stone_bricks"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("flowery_mossy_cobblestone"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("mossy_decrepit_stone_bricks"));
		Block floweryMossyStoneBricks = register(registry, new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(1.5f, 6.0f).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("flowery_mossy_stone_bricks"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.SAND).requiresCorrectToolForDrops().strength(3.0F, 9.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("coarse_end_stone"));
		registry.register(new EndGrassBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(3.0F, 9.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("end_grass"));
		Block chalk = register(registry, new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.SNOW).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("chalk"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.SNOW).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("polished_chalk"));
		Block chalkBricks = register(registry, new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.SNOW).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("chalk_bricks"));
		registry.register(new MSDirectionalBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.SNOW).requiresCorrectToolForDrops().strength(1.5f, 6.0f).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("chalk_column"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.SNOW).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("chiseled_chalk_bricks"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.SNOW).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("mossy_chalk_bricks"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.SNOW).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("flowery_chalk_bricks"));
		Block pinkStone = register(registry, new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_PINK).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("pink_stone"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_PINK).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("polished_pink_stone"));
		Block pinkStoneBricks = register(registry, new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_PINK).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("pink_stone_bricks"));
		registry.register(new MSDirectionalBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_PINK).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("pink_stone_column"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_PINK).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("chiseled_pink_stone_bricks"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_PINK).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("cracked_pink_stone_bricks"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_PINK).requiresCorrectToolForDrops().strength(1.5F, 6.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("mossy_pink_stone_bricks"));
		Block brownStone = register(registry, new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BROWN).requiresCorrectToolForDrops().strength(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("brown_stone"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BROWN).requiresCorrectToolForDrops().strength(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("polished_brown_stone"));
		Block brownStoneBricks = register(registry, new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BROWN).requiresCorrectToolForDrops().strength(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("brown_stone_bricks"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BROWN).requiresCorrectToolForDrops().strength(1.0F, 5.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("cracked_brown_stone_bricks"));
		registry.register(new MSDirectionalBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BROWN).requiresCorrectToolForDrops().strength(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("brown_stone_column"));
		Block greenStone = register(registry, new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).strength(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("green_stone"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).strength(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("polished_green_stone"));
		Block greenStoneBricks = register(registry, new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).strength(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("green_stone_bricks"));
		registry.register(new MSDirectionalBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).strength(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("green_stone_column"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).strength(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("chiseled_green_stone_bricks"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).strength(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("horizontal_green_stone_bricks"));
		registry.register(new Block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).strength(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("vertical_green_stone_bricks"));
		registry.register(new MSDirectionalBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).strength(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("green_stone_brick_trim"));
		registry.register(new HieroglyphBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).strength(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("green_stone_brick_frog"));
		registry.register(new HieroglyphBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).strength(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("green_stone_brick_iguana_left"));
		registry.register(new HieroglyphBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).strength(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("green_stone_brick_iguana_right"));
		registry.register(new HieroglyphBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).strength(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("green_stone_brick_lotus"));
		registry.register(new HieroglyphBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).strength(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("green_stone_brick_nak_left"));
		registry.register(new HieroglyphBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).strength(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("green_stone_brick_nak_right"));
		registry.register(new HieroglyphBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).strength(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("green_stone_brick_salamander_left"));
		registry.register(new HieroglyphBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).strength(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("green_stone_brick_salamander_right"));
		registry.register(new HieroglyphBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).strength(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("green_stone_brick_skaia"));
		registry.register(new HieroglyphBlock(Block.Properties.of(Material.STONE, MaterialColor.COLOR_GREEN).strength(2.5F, 7.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("green_stone_brick_turtle"));
		registry.register(new MSDirectionalBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.SAND).requiresCorrectToolForDrops().strength(0.8F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("sandstone_column"));
		registry.register(new MSDirectionalBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.SAND).requiresCorrectToolForDrops().strength(0.8F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("chiseled_sandstone_column"));
		registry.register(new MSDirectionalBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(0.8F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("red_sandstone_column"));
		registry.register(new MSDirectionalBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(0.8F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("chiseled_red_sandstone_column"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("uncarved_wood"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(1.0F).harvestTool(ToolType.AXE).sound(SoundType.SCAFFOLDING)).setRegistryName("chipboard"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(0.4F).harvestTool(ToolType.SHOVEL).sound(SoundType.SAND)).setRegistryName("wood_shavings"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.GLASS, MaterialColor.COLOR_YELLOW).strength(0.5F).sound(SoundType.SNOW)).setRegistryName("dense_cloud"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.GLASS, MaterialColor.COLOR_LIGHT_GRAY).strength(0.5F).sound(SoundType.SNOW)).setRegistryName("bright_dense_cloud"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.SAND, MaterialColor.SNOW).strength(0.4F).sound(SoundType.SAND)).setRegistryName("sugar_cube"));
		registry.register(new SpikeBlock(AbstractBlock.Properties.of(Material.METAL, MaterialColor.METAL).strength(2).sound(SoundType.METAL), MSBlockShapes.SPIKES).setRegistryName("spikes"));
		
		registry.register(new FlammableLogBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.COLOR_LIGHT_BLUE).strength(2.0F).harvestTool(ToolType.AXE).lightLevel(state -> 11).sound(SoundType.WOOD)).setRegistryName("glowing_log"));
		registry.register(new FlammableLogBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.ICE).strength(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("frost_log"));
		registry.register(new FlammableLogBlock(AbstractBlock.Properties.of(Material.WOOD).strength(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("rainbow_log"));
		registry.register(new DoubleLogBlock(1, 250, AbstractBlock.Properties.of(Material.WOOD, MaterialColor.SAND).strength(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("end_log"));
		registry.register(new FlammableLogBlock(AbstractBlock.Properties.of(Material.WOOD, logColors(MaterialColor.WOOD, MaterialColor.PODZOL)).strength(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("vine_log"));
		registry.register(new FlammableLogBlock(AbstractBlock.Properties.of(Material.WOOD, logColors(MaterialColor.WOOD, MaterialColor.PODZOL)).strength(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("flowery_vine_log"));
		registry.register(new FlammableLogBlock(AbstractBlock.Properties.of(Material.WOOD, logColors(MaterialColor.WOOD, MaterialColor.PODZOL)).strength(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("dead_log"));
		registry.register(new RotatedPillarBlock(AbstractBlock.Properties.of(Material.WOOD, logColors(MaterialColor.WOOD, MaterialColor.PODZOL)).strength(2.0F).harvestTool(ToolType.AXE).sound(SoundType.STONE)).setRegistryName("petrified_log"));
		registry.register(new FlammableLogBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.COLOR_LIGHT_BLUE).strength(2.0F).harvestTool(ToolType.AXE).lightLevel(state -> 11).sound(SoundType.WOOD)).setRegistryName("glowing_wood"));
		registry.register(new FlammableLogBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.ICE).strength(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("frost_wood"));
		registry.register(new FlammableLogBlock(AbstractBlock.Properties.of(Material.WOOD).strength(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("rainbow_wood"));
		registry.register(new FlammableLogBlock(1, 250, AbstractBlock.Properties.of(Material.WOOD, MaterialColor.SAND).strength(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("end_wood"));
		registry.register(new FlammableLogBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("vine_wood"));
		registry.register(new FlammableLogBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("flowery_vine_wood"));
		registry.register(new FlammableLogBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("dead_wood"));
		registry.register(new RotatedPillarBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2.0F).harvestTool(ToolType.AXE).sound(SoundType.STONE)).setRegistryName("petrified_wood"));
		registry.register(new FlammableBlock(5, 20, AbstractBlock.Properties.of(Material.WOOD, MaterialColor.COLOR_LIGHT_BLUE).strength(2.0F, 3.0F).harvestTool(ToolType.AXE).lightLevel(state -> 7).sound(SoundType.WOOD)).setRegistryName("glowing_planks"));
		registry.register(new FlammableBlock(5, 5, AbstractBlock.Properties.of(Material.WOOD, MaterialColor.ICE).strength(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("frost_planks"));
		Block rainbowPlanks = register(registry, new FlammableBlock(5, 20, AbstractBlock.Properties.of(Material.WOOD).strength(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("rainbow_planks"));
		Block endPlanks = register(registry, new FlammableBlock(1, 250, AbstractBlock.Properties.of(Material.WOOD, MaterialColor.SAND).strength(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("end_planks"));
		Block deadPlanks = register(registry, new FlammableBlock(5, 5, AbstractBlock.Properties.of(Material.WOOD).strength(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("dead_planks"));
		Block treatedPlanks = register(registry, new FlammableBlock(0, 0, AbstractBlock.Properties.of(Material.WOOD).strength(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("treated_planks"));
		registry.register(new FlammableLeavesBlock(AbstractBlock.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(MSBlocks::leafSpawns).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never)).setRegistryName("frost_leaves"));
		registry.register(new FlammableLeavesBlock(AbstractBlock.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(MSBlocks::leafSpawns).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never)).setRegistryName("rainbow_leaves"));
		registry.register(new EndLeavesBlock(AbstractBlock.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(MSBlocks::leafSpawns).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never)).setRegistryName("end_leaves"));
		registry.register(new RainbowSaplingBlock(AbstractBlock.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)).setRegistryName("rainbow_sapling"));
		registry.register(new EndSaplingBlock(AbstractBlock.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)).setRegistryName("end_sapling"));
		
		registry.register(new FlammableLogBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("blood_aspect_log"));
		registry.register(new FlammableLogBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("breath_aspect_log"));
		registry.register(new FlammableLogBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("doom_aspect_log"));
		registry.register(new FlammableLogBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("heart_aspect_log"));
		registry.register(new FlammableLogBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("hope_aspect_log"));
		registry.register(new FlammableLogBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("life_aspect_log"));
		registry.register(new FlammableLogBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("light_aspect_log"));
		registry.register(new FlammableLogBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("mind_aspect_log"));
		registry.register(new FlammableLogBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("rage_aspect_log"));
		registry.register(new FlammableLogBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("space_aspect_log"));
		registry.register(new FlammableLogBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("time_aspect_log"));
		registry.register(new FlammableLogBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("void_aspect_log"));
		registry.register(new FlammableBlock(5, 20, AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("blood_aspect_planks"));
		registry.register(new FlammableBlock(5, 20, AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("breath_aspect_planks"));
		registry.register(new FlammableBlock(5, 20, AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("doom_aspect_planks"));
		registry.register(new FlammableBlock(5, 20, AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("heart_aspect_planks"));
		registry.register(new FlammableBlock(5, 20, AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("hope_aspect_planks"));
		registry.register(new FlammableBlock(5, 20, AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("life_aspect_planks"));
		registry.register(new FlammableBlock(5, 20, AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("light_aspect_planks"));
		registry.register(new FlammableBlock(5, 20, AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("mind_aspect_planks"));
		registry.register(new FlammableBlock(5, 20, AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("rage_aspect_planks"));
		registry.register(new FlammableBlock(5, 20, AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("space_aspect_planks"));
		registry.register(new FlammableBlock(5, 20, AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("time_aspect_planks"));
		registry.register(new FlammableBlock(5, 20, AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).harvestTool(ToolType.AXE).sound(SoundType.WOOD)).setRegistryName("void_aspect_planks"));
		registry.register(new FlammableLeavesBlock(AbstractBlock.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()).setRegistryName("blood_aspect_leaves"));
		registry.register(new FlammableLeavesBlock(AbstractBlock.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()).setRegistryName("breath_aspect_leaves"));
		registry.register(new FlammableLeavesBlock(AbstractBlock.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()).setRegistryName("doom_aspect_leaves"));
		registry.register(new FlammableLeavesBlock(AbstractBlock.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()).setRegistryName("heart_aspect_leaves"));
		registry.register(new FlammableLeavesBlock(AbstractBlock.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()).setRegistryName("hope_aspect_leaves"));
		registry.register(new FlammableLeavesBlock(AbstractBlock.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()).setRegistryName("life_aspect_leaves"));
		registry.register(new FlammableLeavesBlock(AbstractBlock.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()).setRegistryName("light_aspect_leaves"));
		registry.register(new FlammableLeavesBlock(AbstractBlock.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()).setRegistryName("mind_aspect_leaves"));
		registry.register(new FlammableLeavesBlock(AbstractBlock.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()).setRegistryName("rage_aspect_leaves"));
		registry.register(new FlammableLeavesBlock(AbstractBlock.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()).setRegistryName("space_aspect_leaves"));
		registry.register(new FlammableLeavesBlock(AbstractBlock.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()).setRegistryName("time_aspect_leaves"));
		registry.register(new FlammableLeavesBlock(AbstractBlock.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()).setRegistryName("void_aspect_leaves"));
		registry.register(new AspectSaplingBlock(AbstractBlock.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)).setRegistryName("blood_aspect_sapling"));
		registry.register(new AspectSaplingBlock(AbstractBlock.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)).setRegistryName("breath_aspect_sapling"));
		registry.register(new AspectSaplingBlock(AbstractBlock.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)).setRegistryName("doom_aspect_sapling"));
		registry.register(new AspectSaplingBlock(AbstractBlock.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)).setRegistryName("heart_aspect_sapling"));
		registry.register(new AspectSaplingBlock(AbstractBlock.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)).setRegistryName("hope_aspect_sapling"));
		registry.register(new AspectSaplingBlock(AbstractBlock.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)).setRegistryName("life_aspect_sapling"));
		registry.register(new AspectSaplingBlock(AbstractBlock.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)).setRegistryName("light_aspect_sapling"));
		registry.register(new AspectSaplingBlock(AbstractBlock.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)).setRegistryName("mind_aspect_sapling"));
		registry.register(new AspectSaplingBlock(AbstractBlock.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)).setRegistryName("rage_aspect_sapling"));
		registry.register(new AspectSaplingBlock(AbstractBlock.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)).setRegistryName("space_aspect_sapling"));
		registry.register(new AspectSaplingBlock(AbstractBlock.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)).setRegistryName("time_aspect_sapling"));
		registry.register(new AspectSaplingBlock(AbstractBlock.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)).setRegistryName("void_aspect_sapling"));
		
		registry.register(new GlowingMushroomBlock(AbstractBlock.Properties.of(Material.PLANT, MaterialColor.DIAMOND).noCollission().randomTicks().strength(0).sound(SoundType.GRASS).lightLevel(state -> 11)).setRegistryName("glowing_mushroom"));
		registry.register(new DesertFloraBlock(AbstractBlock.Properties.of(Material.PLANT).noCollission().strength(0).sound(SoundType.GRASS)).setRegistryName("desert_bush"));
		registry.register(new DesertFloraBlock(AbstractBlock.Properties.of(Material.PLANT).noCollission().strength(0).sound(SoundType.GRASS)).setRegistryName("blooming_cactus"));
		registry.register(new PetrifiedFloraBlock(AbstractBlock.Properties.of(Material.STONE, DyeColor.GRAY).requiresCorrectToolForDrops().noCollission().strength(0).harvestTool(ToolType.PICKAXE).harvestLevel(0).sound(SoundType.STONE)).setRegistryName("petrified_grass"));
		registry.register(new PetrifiedFloraBlock(AbstractBlock.Properties.of(Material.STONE, DyeColor.GRAY).requiresCorrectToolForDrops().noCollission().strength(0).harvestTool(ToolType.PICKAXE).harvestLevel(0).sound(SoundType.STONE)).setRegistryName("petrified_poppy"));
		registry.register(new StrawberryBlock(AbstractBlock.Properties.of(Material.VEGETABLE, MaterialColor.COLOR_RED).strength(1.0F).sound(SoundType.WOOD)).setRegistryName("strawberry"));
		registry.register(new StrawberryBlock.AttachedStem((StemGrownBlock) STRAWBERRY, AbstractBlock.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.WOOD)).setRegistryName("attached_strawberry_stem"));
		registry.register(new StrawberryBlock.Stem((StemGrownBlock) STRAWBERRY, AbstractBlock.Properties.of(Material.PLANT).noCollission().randomTicks().strength(0).sound(SoundType.WOOD)).setRegistryName("strawberry_stem"));
		registry.register(new TallEndGrassBlock(AbstractBlock.Properties.of(Material.REPLACEABLE_PLANT, DyeColor.GREEN).noCollission().randomTicks().strength(0.1F).sound(SoundType.NETHER_WART)).setRegistryName("tall_end_grass"));
		registry.register(new FlowerBlock(Effects.GLOWING, 20, AbstractBlock.Properties.of(Material.PLANT, DyeColor.YELLOW).noCollission().strength(0).lightLevel(state -> 12).sound(SoundType.GRASS)).setRegistryName("glowflower"));
		
		registry.register(new GoopBlock(AbstractBlock.Properties.of(Material.CLAY).strength(0.1F).sound(SoundType.SLIME_BLOCK).lightLevel(state -> 14)).setRegistryName("glowy_goop"));
		registry.register(new GoopBlock(AbstractBlock.Properties.of(Material.CLAY).strength(0.1F).sound(SoundType.SLIME_BLOCK)).setRegistryName("coagulated_blood"));
		registry.register(new VeinBlock(AbstractBlock.Properties.of(Material.WOOD).strength(0.45F).sound(SoundType.SLIME_BLOCK)).setRegistryName("vein"));
		registry.register(new VeinCornerBlock(AbstractBlock.Properties.of(Material.WOOD).strength(0.45F).sound(SoundType.SLIME_BLOCK)).setRegistryName("vein_corner"));
		registry.register(new VeinCornerBlock(AbstractBlock.Properties.of(Material.WOOD).strength(0.45F).sound(SoundType.SLIME_BLOCK)).setRegistryName("inverted_vein_corner"));
		registry.register(new DirectionalCustomShapeBlock(AbstractBlock.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(4.0F).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(0), MSBlockShapes.PIPE).setRegistryName("pipe"));
		registry.register(new Block(AbstractBlock.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(4.0F).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("pipe_intersection"));
		registry.register(new StoneTabletBlock(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(0.3F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("stone_slab")); //same thing as stone tablet
		registry.register(new CustomShapeBlock(AbstractBlock.Properties.of(Material.STONE).strength(0.5F), MSBlockShapes.NAKAGATOR_STATUE).setRegistryName("nakagator_statue"));
		
		registry.register(new StairsBlock(() -> MSBlocks.BLACK_CASTLE_BRICKS.defaultBlockState(), AbstractBlock.Properties.copy(blackChessBricks)).setRegistryName("black_castle_brick_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.DARK_GRAY_CASTLE_BRICKS.defaultBlockState(), AbstractBlock.Properties.copy(darkGrayChessBricks)).setRegistryName("dark_gray_castle_brick_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.LIGHT_GRAY_CASTLE_BRICKS.defaultBlockState(), AbstractBlock.Properties.copy(lightGrayChessBricks)).setRegistryName("light_gray_castle_brick_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.WHITE_CASTLE_BRICKS.defaultBlockState(), AbstractBlock.Properties.copy(whiteChessBricks)).setRegistryName("white_castle_brick_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.COARSE_STONE.defaultBlockState(), AbstractBlock.Properties.copy(coarseStone)).setRegistryName("coarse_stone_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.COARSE_STONE_BRICKS.defaultBlockState(), AbstractBlock.Properties.copy(coarseStoneBricks)).setRegistryName("coarse_stone_brick_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.SHADE_STONE.defaultBlockState(), AbstractBlock.Properties.copy(shadeStone)).setRegistryName("shade_stairs"));;
		registry.register(new StairsBlock(() -> MSBlocks.SHADE_BRICKS.defaultBlockState(), AbstractBlock.Properties.copy(shadeBricks)).setRegistryName("shade_brick_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.FROST_TILE.defaultBlockState(), AbstractBlock.Properties.copy(frostTile)).setRegistryName("frost_tile_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.FROST_BRICKS.defaultBlockState(), AbstractBlock.Properties.copy(frostBricks)).setRegistryName("frost_brick_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.CAST_IRON.defaultBlockState(), AbstractBlock.Properties.copy(castIron)).setRegistryName("cast_iron_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.BLACK_STONE.defaultBlockState(), AbstractBlock.Properties.copy(blackStone)).setRegistryName("black_stone_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.BLACK_STONE_BRICKS.defaultBlockState(), AbstractBlock.Properties.copy(blackStoneBricks)).setRegistryName("black_stone_brick_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.MYCELIUM_STONE.defaultBlockState(), AbstractBlock.Properties.copy(myceliumStone)).setRegistryName("mycelium_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.MYCELIUM_BRICKS.defaultBlockState(), AbstractBlock.Properties.copy(myceliumBricks)).setRegistryName("mycelium_brick_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.FLOWERY_MOSSY_STONE_BRICKS.defaultBlockState(), AbstractBlock.Properties.copy(floweryMossyStoneBricks)).setRegistryName("flowery_mossy_stone_brick_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.CHALK.defaultBlockState(), AbstractBlock.Properties.copy(chalk)).setRegistryName("chalk_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.CHALK_BRICKS.defaultBlockState(), AbstractBlock.Properties.copy(chalkBricks)).setRegistryName("chalk_brick_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.PINK_STONE.defaultBlockState(), AbstractBlock.Properties.copy(pinkStone)).setRegistryName("pink_stone_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.PINK_STONE_BRICKS.defaultBlockState(), AbstractBlock.Properties.copy(pinkStoneBricks)).setRegistryName("pink_stone_brick_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.BROWN_STONE.defaultBlockState(), AbstractBlock.Properties.copy(brownStone)).setRegistryName("brown_stone_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.BROWN_STONE_BRICKS.defaultBlockState(), AbstractBlock.Properties.copy(brownStoneBricks)).setRegistryName("brown_stone_brick_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.GREEN_STONE.defaultBlockState(), Block.Properties.copy(greenStone)).setRegistryName("green_stone_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.GREEN_STONE_BRICKS.defaultBlockState(), Block.Properties.copy(greenStoneBricks)).setRegistryName("green_stone_brick_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.RAINBOW_PLANKS.defaultBlockState(), AbstractBlock.Properties.copy(rainbowPlanks)).setRegistryName("rainbow_planks_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.END_PLANKS.defaultBlockState(), AbstractBlock.Properties.copy(endPlanks)).setRegistryName("end_planks_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.DEAD_PLANKS.defaultBlockState(), AbstractBlock.Properties.copy(deadPlanks)).setRegistryName("dead_planks_stairs"));
		registry.register(new StairsBlock(() -> MSBlocks.TREATED_PLANKS.defaultBlockState(), AbstractBlock.Properties.copy(treatedPlanks)).setRegistryName("treated_planks_stairs"));
		registry.register(new CustomShapeBlock(Block.Properties.copy(greenStoneBricks), MSBlockShapes.STEEP_STAIRS_BASE).setRegistryName("steep_green_stone_brick_stairs_base"));
		registry.register(new CustomShapeBlock(Block.Properties.copy(greenStoneBricks), MSBlockShapes.STEEP_STAIRS_TOP).setRegistryName("steep_green_stone_brick_stairs_top"));
		registry.register(new SlabBlock(AbstractBlock.Properties.copy(blackChessBricks)).setRegistryName("black_castle_brick_slab"));
		registry.register(new SlabBlock(AbstractBlock.Properties.copy(darkGrayChessBricks)).setRegistryName("dark_gray_castle_brick_slab"));
		registry.register(new SlabBlock(AbstractBlock.Properties.copy(lightGrayChessBricks)).setRegistryName("light_gray_castle_brick_slab"));
		registry.register(new SlabBlock(AbstractBlock.Properties.copy(whiteChessBricks)).setRegistryName("white_castle_brick_slab"));
		registry.register(new SlabBlock(AbstractBlock.Properties.copy(chalk)).setRegistryName("chalk_slab"));
		registry.register(new SlabBlock(AbstractBlock.Properties.copy(chalkBricks)).setRegistryName("chalk_brick_slab"));
		registry.register(new SlabBlock(AbstractBlock.Properties.copy(pinkStone)).setRegistryName("pink_stone_slab"));
		registry.register(new SlabBlock(AbstractBlock.Properties.copy(pinkStoneBricks)).setRegistryName("pink_stone_brick_slab"));
		registry.register(new SlabBlock(AbstractBlock.Properties.copy(blackStone)).setRegistryName("black_stone_slab"));
		registry.register(new SlabBlock(AbstractBlock.Properties.copy(blackStoneBricks)).setRegistryName("black_stone_brick_slab"));
		registry.register(new SlabBlock(AbstractBlock.Properties.copy(myceliumStone)).setRegistryName("mycelium_slab"));
		registry.register(new SlabBlock(AbstractBlock.Properties.copy(myceliumBricks)).setRegistryName("mycelium_brick_slab"));
		registry.register(new SlabBlock(AbstractBlock.Properties.copy(floweryMossyStoneBricks)).setRegistryName("flowery_mossy_stone_brick_slab"));
		registry.register(new SlabBlock(AbstractBlock.Properties.copy(frostTile)).setRegistryName("frost_tile_slab"));
		registry.register(new SlabBlock(AbstractBlock.Properties.copy(frostBricks)).setRegistryName("frost_brick_slab"));
		registry.register(new SlabBlock(AbstractBlock.Properties.copy(shadeStone)).setRegistryName("shade_slab"));
		registry.register(new SlabBlock(AbstractBlock.Properties.copy(shadeBricks)).setRegistryName("shade_brick_slab"));
		registry.register(new SlabBlock(AbstractBlock.Properties.copy(coarseStone)).setRegistryName("coarse_stone_slab"));
		registry.register(new SlabBlock(AbstractBlock.Properties.copy(coarseStoneBricks)).setRegistryName("coarse_stone_brick_slab"));
		registry.register(new SlabBlock(AbstractBlock.Properties.copy(brownStone)).setRegistryName("brown_stone_slab"));
		registry.register(new SlabBlock(AbstractBlock.Properties.copy(brownStoneBricks)).setRegistryName("brown_stone_brick_slab"));
		registry.register(new SlabBlock(Block.Properties.copy(greenStone)).setRegistryName("green_stone_slab"));
		registry.register(new SlabBlock(Block.Properties.copy(greenStoneBricks)).setRegistryName("green_stone_brick_slab"));
		registry.register(new SlabBlock(AbstractBlock.Properties.copy(rainbowPlanks)).setRegistryName("rainbow_planks_slab"));
		registry.register(new SlabBlock(AbstractBlock.Properties.copy(endPlanks)).setRegistryName("end_planks_slab"));
		registry.register(new SlabBlock(AbstractBlock.Properties.copy(deadPlanks)).setRegistryName("dead_planks_slab"));
		registry.register(new SlabBlock(AbstractBlock.Properties.copy(treatedPlanks)).setRegistryName("treated_planks_slab"));
		
		registry.register(new TrajectoryBlock(AbstractBlock.Properties.of(Material.METAL).strength(3).sound(SoundType.METAL)).setRegistryName("trajectory_block"));
		registry.register(new StatStorerBlock(AbstractBlock.Properties.of(Material.METAL).strength(3).sound(SoundType.METAL)).setRegistryName("stat_storer"));
		registry.register(new RemoteObserverBlock(AbstractBlock.Properties.of(Material.METAL).strength(3).sound(SoundType.METAL)).setRegistryName("remote_observer"));
		registry.register(new WirelessRedstoneTransmitterBlock(AbstractBlock.Properties.of(Material.METAL).strength(3).sound(SoundType.METAL)).setRegistryName("wireless_redstone_transmitter"));
		registry.register(new WirelessRedstoneReceiverBlock(AbstractBlock.Properties.of(Material.METAL).strength(3).randomTicks().sound(SoundType.METAL)).setRegistryName("wireless_redstone_receiver"));
		registry.register(new SolidSwitchBlock(AbstractBlock.Properties.of(Material.METAL).strength(3).sound(SoundType.METAL).lightLevel(state -> state.getValue(SolidSwitchBlock.POWERED) ? 15 : 0)).setRegistryName("solid_switch"));
		registry.register(new VariableSolidSwitchBlock(AbstractBlock.Properties.of(Material.METAL).strength(3).sound(SoundType.METAL).lightLevel(state -> state.getValue(VariableSolidSwitchBlock.POWER))).setRegistryName("variable_solid_switch"));
		registry.register(new TimedSolidSwitchBlock(AbstractBlock.Properties.of(Material.METAL).strength(3).sound(SoundType.METAL).lightLevel(state -> state.getValue(TimedSolidSwitchBlock.POWER)), 20).setRegistryName("one_second_interval_timed_solid_switch"));
		registry.register(new TimedSolidSwitchBlock(AbstractBlock.Properties.of(Material.METAL).strength(3).sound(SoundType.METAL).lightLevel(state -> state.getValue(TimedSolidSwitchBlock.POWER)), 40).setRegistryName("two_second_interval_timed_solid_switch"));
		registry.register(new SummonerBlock(AbstractBlock.Properties.of(Material.METAL).strength(3).sound(SoundType.METAL)).setRegistryName("summoner"));
		registry.register(new AreaEffectBlock(AbstractBlock.Properties.of(Material.METAL).strength(6).sound(SoundType.METAL)).setRegistryName("area_effect_block"));
		registry.register(new PlatformGeneratorBlock(AbstractBlock.Properties.of(Material.METAL).strength(6).sound(SoundType.METAL)).setRegistryName("platform_generator"));
		registry.register(new PlatformBlock(AbstractBlock.Properties.of(Material.BARRIER).strength(0.2F).sound(SoundType.SCAFFOLDING).lightLevel(state -> 6).noOcclusion().isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never)).setRegistryName("platform_block"));
		registry.register(new PlatformReceptacleBlock(AbstractBlock.Properties.of(Material.METAL).strength(3).sound(SoundType.METAL)).setRegistryName("platform_receptacle"));
		registry.register(new ItemMagnetBlock(AbstractBlock.Properties.of(Material.METAL).strength(3).sound(SoundType.METAL), new CustomVoxelShape(new double[]{0, 0, 0, 16, 1, 16}, new double[]{1, 1, 1, 15, 15, 15}, new double[]{0, 15, 0, 16, 16, 16})).setRegistryName("item_magnet"));
		registry.register(new RedstoneClockBlock(AbstractBlock.Properties.of(Material.METAL).strength(3).sound(SoundType.METAL)).setRegistryName("redstone_clock"));
		registry.register(new RotatorBlock(AbstractBlock.Properties.of(Material.METAL).strength(3).sound(SoundType.METAL)).setRegistryName("rotator"));
		registry.register(new FallPadBlock(AbstractBlock.Properties.of(Material.CLOTH_DECORATION).strength(1).sound(SoundType.WOOL)).setRegistryName("fall_pad"));
		registry.register(new FragileBlock(AbstractBlock.Properties.of(Material.STONE).strength(1).sound(SoundType.STONE)).setRegistryName("fragile_stone"));
		registry.register(new RetractableSpikesBlock(AbstractBlock.Properties.of(Material.METAL).strength(1).sound(SoundType.METAL)).setRegistryName("retractable_spikes"));
		registry.register(new LogicGateBlock(AbstractBlock.Properties.of(Material.STONE).strength(1).sound(SoundType.STONE), LogicGateBlock.State.AND).setRegistryName("and_gate_block"));
		registry.register(new LogicGateBlock(AbstractBlock.Properties.of(Material.STONE).strength(1).sound(SoundType.STONE), LogicGateBlock.State.OR).setRegistryName("or_gate_block"));
		registry.register(new LogicGateBlock(AbstractBlock.Properties.of(Material.STONE).strength(1).sound(SoundType.STONE), LogicGateBlock.State.XOR).setRegistryName("xor_gate_block"));
		registry.register(new LogicGateBlock(AbstractBlock.Properties.of(Material.STONE).strength(1).sound(SoundType.STONE), LogicGateBlock.State.NAND).setRegistryName("nand_gate_block"));
		registry.register(new LogicGateBlock(AbstractBlock.Properties.of(Material.STONE).strength(1).sound(SoundType.STONE), LogicGateBlock.State.NOR).setRegistryName("nor_gate_block"));
		registry.register(new LogicGateBlock(AbstractBlock.Properties.of(Material.STONE).strength(1).sound(SoundType.STONE), LogicGateBlock.State.XNOR).setRegistryName("xnor_gate_block"));
		
		registry.register(new GateBlock(AbstractBlock.Properties.of(Material.PORTAL).noCollission().strength(-1.0F, 25.0F).sound(SoundType.GLASS).lightLevel(state -> 11).noDrops()).setRegistryName("gate"));
		registry.register(new ReturnNodeBlock(AbstractBlock.Properties.of(Material.PORTAL).noCollission().strength(-1.0F, 10.0F).sound(SoundType.GLASS).lightLevel(state -> 11).noDrops()).setRegistryName("return_node"));
		
		CRUXTRUDER.registerBlocks(registry);
		registry.register(new CruxtruderLidBlock(AbstractBlock.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(1.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("cruxtruder_lid"));
		TOTEM_LATHE.registerBlocks(registry);
		ALCHEMITER.registerBlocks(registry);
		PUNCH_DESIGNIX.registerBlocks(registry);
		registry.register(new SmallMachineBlock<>(MSBlockShapes.SMALL_CRUXTRUDER.createRotatedShapes(), MSTileEntityTypes.MINI_CRUXTRUDER, AbstractBlock.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("mini_cruxtruder"));
		registry.register(new SmallMachineBlock<>(MSBlockShapes.SMALL_TOTEM_LATHE.createRotatedShapes(), MSTileEntityTypes.MINI_TOTEM_LATHE, AbstractBlock.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("mini_totem_lathe"));
		registry.register(new MiniAlchemiterBlock(AbstractBlock.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("mini_alchemiter"));
		registry.register(new SmallMachineBlock<>(MSBlockShapes.SMALL_PUNCH_DESIGNIX.createRotatedShapes(), MSTileEntityTypes.MINI_PUNCH_DESIGNIX, AbstractBlock.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("mini_punch_designix"));
		registry.register(new HolopadBlock(AbstractBlock.Properties.of(Material.METAL, MaterialColor.SNOW).requiresCorrectToolForDrops().strength(3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("holopad"));
		
		registry.register(new ComputerBlock(ComputerBlock.COMPUTER_SHAPE, ComputerBlock.COMPUTER_SHAPE, AbstractBlock.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(4.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("computer"));
		registry.register(new ComputerBlock(ComputerBlock.LAPTOP_OPEN_SHAPE, ComputerBlock.LAPTOP_CLOSED_SHAPE, AbstractBlock.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(4.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("laptop"));
		registry.register(new ComputerBlock(ComputerBlock.LAPTOP_OPEN_SHAPE, ComputerBlock.LAPTOP_CLOSED_SHAPE, AbstractBlock.Properties.of(Material.METAL, MaterialColor.COLOR_RED).requiresCorrectToolForDrops().strength(4.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("crockertop"));
		registry.register(new ComputerBlock(ComputerBlock.LAPTOP_OPEN_SHAPE, ComputerBlock.LAPTOP_CLOSED_SHAPE, AbstractBlock.Properties.of(Material.METAL, MaterialColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(4.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("hubtop"));
		registry.register(new ComputerBlock(ComputerBlock.LUNCHTOP_OPEN_SHAPE, ComputerBlock.LUNCHTOP_CLOSED_SHAPE, AbstractBlock.Properties.of(Material.METAL, MaterialColor.COLOR_RED).requiresCorrectToolForDrops().strength(4.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("lunchtop"));
		registry.register(new ComputerBlock(ComputerBlock.OLD_COMPUTER_SHAPE, ComputerBlock.OLD_COMPUTER_SHAPE, AbstractBlock.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(4.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("old_computer"));
		registry.register(new TransportalizerBlock(AbstractBlock.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("transportalizer"));
		registry.register(new TransportalizerBlock(AbstractBlock.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("trans_portalizer"));
		registry.register(new SendificatorBlock(AbstractBlock.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0), MSBlockShapes.SENDIFICATOR).setRegistryName("sendificator"));
		registry.register(new GristWidgetBlock(AbstractBlock.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("grist_widget"));
		registry.register(new SmallMachineBlock<>(new CustomVoxelShape(new double[]{4, 0, 4, 12, 6, 12}).createRotatedShapes(), MSTileEntityTypes.URANIUM_COOKER, AbstractBlock.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0)).setRegistryName("uranium_cooker"));
		
		registry.register(new CruxiteDowelBlock(AbstractBlock.Properties.of(Material.GLASS).strength(0.0F)).setRegistryName("cruxite_dowel"));
		
		registry.register(new GoldSeedsBlock(AbstractBlock.Properties.of(Material.PLANT).strength(0.1F).sound(SoundType.METAL).noCollission()).setRegistryName("gold_seeds"));
		registry.register(new SpecialCactusBlock(AbstractBlock.Properties.of(Material.WOOD).randomTicks().strength(1.0F, 2.5F).sound(SoundType.WOOD).harvestTool(ToolType.AXE)).setRegistryName("wooden_cactus"));
		
		registry.register(new SimpleCakeBlock(AbstractBlock.Properties.of(Material.CAKE).strength(0.5F).sound(SoundType.WOOL), 2, 0.5F, null).setRegistryName("apple_cake"));
		registry.register(new SimpleCakeBlock(AbstractBlock.Properties.of(Material.CAKE).strength(0.5F).sound(SoundType.WOOL), 2, 0.3F, player -> player.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 150, 0))).setRegistryName("blue_cake"));
		registry.register(new SimpleCakeBlock(AbstractBlock.Properties.of(Material.CAKE).strength(0.5F).sound(SoundType.WOOL), 2, 0.3F, player -> {player.addEffect(new EffectInstance(Effects.WEAKNESS, 200, 1));player.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 200, 1));}).setRegistryName("cold_cake"));
		registry.register(new SimpleCakeBlock(AbstractBlock.Properties.of(Material.CAKE).strength(0.5F).sound(SoundType.WOOL), 2, 0.1F, player -> player.heal(1)).setRegistryName("red_cake"));
		registry.register(new SimpleCakeBlock(AbstractBlock.Properties.of(Material.CAKE).strength(0.5F).sound(SoundType.WOOL), 2, 0.1F, player -> player.setSecondsOnFire(4)).setRegistryName("hot_cake"));
		registry.register(new SimpleCakeBlock(AbstractBlock.Properties.of(Material.CAKE).strength(0.5F).sound(SoundType.WOOL), 2, 0.1F, null).setRegistryName("reverse_cake"));
		registry.register(new SimpleCakeBlock(AbstractBlock.Properties.of(Material.CAKE).strength(0.5F).sound(SoundType.WOOL), 3, 0.5F, player -> {player.addEffect(new EffectInstance(Effects.ABSORPTION, 350, 1));player.addEffect(new EffectInstance(Effects.REGENERATION, 200, 0));}).setRegistryName("fuchsia_cake"));
		registry.register(new SimpleCakeBlock(AbstractBlock.Properties.of(Material.CAKE).strength(0.5F).sound(SoundType.WOOL), 2, 0.3F, player -> {player.addEffect(new EffectInstance(Effects.BLINDNESS, 300, 0));player.addEffect(new EffectInstance(Effects.INVISIBILITY, 250, 0));}).setRegistryName("negative_cake"));
		registry.register(new SimpleCakeBlock(AbstractBlock.Properties.of(Material.CAKE).strength(0.5F).sound(SoundType.WOOL), 2, 0.3F, player -> player.addEffect(new EffectInstance(Effects.NIGHT_VISION, 200, 0))).setRegistryName("carrot_cake"));
		
		registry.register(new SpecialTNTBlock(AbstractBlock.Properties.of(Material.EXPLOSIVE).strength(0.0F).sound(SoundType.GRASS), true, false, false).setRegistryName("primed_tnt"));
		registry.register(new SpecialTNTBlock(AbstractBlock.Properties.of(Material.EXPLOSIVE).strength(0.0F).sound(SoundType.GRASS).randomTicks(), false, true, false).setRegistryName("unstable_tnt"));
		registry.register(new SpecialTNTBlock(AbstractBlock.Properties.of(Material.EXPLOSIVE).strength(0.0F).sound(SoundType.GRASS), false, false, true).setRegistryName("instant_tnt"));
		registry.register(new SpecialButtonBlock(AbstractBlock.Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.WOOD), true, true).setRegistryName("wooden_explosive_button"));
		registry.register(new SpecialButtonBlock(AbstractBlock.Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.STONE), true, false).setRegistryName("stone_explosive_button"));
		
		registry.register(new CustomShapeBlock(AbstractBlock.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(0.5F).harvestTool(ToolType.PICKAXE).harvestLevel(0).sound(SoundType.METAL), MSBlockShapes.BLENDER).setRegistryName("blender"));
		registry.register(new CustomShapeBlock(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(0.5F).harvestTool(ToolType.PICKAXE).harvestLevel(0), MSBlockShapes.CHESSBOARD).setRegistryName("chessboard"));
		registry.register(new CustomShapeBlock(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(0.5F).harvestTool(ToolType.PICKAXE).harvestLevel(0), MSBlockShapes.FROG_STATUE).setRegistryName("mini_frog_statue"));
		registry.register(new CustomShapeBlock(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(0.5F).harvestTool(ToolType.PICKAXE).harvestLevel(0), MSBlockShapes.WIZARD_STATUE).setRegistryName("mini_wizard_statue"));
		registry.register(new CustomShapeBlock(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(0.5F).harvestTool(ToolType.PICKAXE).harvestLevel(0), MSBlockShapes.DENIZEN_STATUE).setRegistryName("mini_typheus_statue"));
		registry.register(new CassettePlayerBlock(AbstractBlock.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(0.5F).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(0), MSBlockShapes.CASSETTE_PLAYER).setRegistryName("cassette_player"));
		registry.register(new GlowystoneWireBlock(AbstractBlock.Properties.of(Material.DECORATION).strength(0.0F).lightLevel(state -> 16).noCollission()).setRegistryName("glowystone_dust"));
		registry.register(new CustomShapeBlock(AbstractBlock.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(4.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0), MSBlockShapes.PARCEL_PYXIS).setRegistryName("parcel_pyxis"));
		registry.register(new CustomShapeBlock(AbstractBlock.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(1.0F).harvestTool(ToolType.PICKAXE).harvestLevel(0), MSBlockShapes.PYXIS_LID).setRegistryName("pyxis_lid"));
		
		LOTUS_TIME_CAPSULE_BLOCK.registerBlocks(registry);
		
		registry.register(new FlowingModFluidBlock(MSFluids.OIL, new Vector3d(0.0, 0.0, 0.0), 0.75f, AbstractBlock.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()).setRegistryName("oil"));
		registry.register(new FlowingModFluidBlock(MSFluids.BLOOD, new Vector3d(0.8, 0.0, 0.0), 0.25f, AbstractBlock.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()).setRegistryName("blood"));
		registry.register(new FlowingModFluidBlock(MSFluids.BRAIN_JUICE, new Vector3d(0.55, 0.25, 0.7), 0.25f, AbstractBlock.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()).setRegistryName("brain_juice"));
		registry.register(new FlowingWaterColorsBlock(MSFluids.WATER_COLORS, 0.01f, AbstractBlock.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()).setRegistryName("water_colors"));
		registry.register(new FlowingModFluidBlock(MSFluids.ENDER, new Vector3d(0, 0.35, 0.35), (Float.MAX_VALUE), AbstractBlock.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()).setRegistryName("ender"));
		registry.register(new FlowingModFluidBlock(MSFluids.LIGHT_WATER, new Vector3d(0.2, 0.3, 1.0), 0.01f, AbstractBlock.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()).setRegistryName("light_water"));
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
	
	private static Boolean leafSpawns(BlockState state, IBlockReader world, BlockPos pos, EntityType<?> type)
	{
		return type == EntityType.OCELOT || type == EntityType.PARROT;
	}
	
	private static boolean never(BlockState state, IBlockReader world, BlockPos pos)
	{
		return false;
	}
	
	private static Boolean never(BlockState state, IBlockReader world, BlockPos pos, EntityType<?> type)
	{
		return false;
	}
}