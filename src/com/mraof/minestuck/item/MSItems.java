package com.mraof.minestuck.item;

import com.mraof.minestuck.entity.item.CrewPosterEntity;
import com.mraof.minestuck.entity.item.MetalBoatEntity;
import com.mraof.minestuck.entity.item.SbahjPosterEntity;
import com.mraof.minestuck.fluid.MSFluids;
import com.mraof.minestuck.item.block.*;
import com.mraof.minestuck.item.foods.*;
import com.mraof.minestuck.item.weapon.*;
import com.mraof.minestuck.util.MSSoundEvents;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Items;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.IForgeRegistry;

import static com.mraof.minestuck.block.MSBlocks.*;

import java.util.Arrays;

/**
 * This class contains all non-ItemBlock items that minestuck adds,
 * and is responsible for initializing and registering these.
 */
public class MSItems
{
	
	//hammers
	public static Item CLAW_HAMMER;
	public static Item SLEDGE_HAMMER;
	public static Item BLACKSMITH_HAMMER;
	public static Item POGO_HAMMER;
	public static Item TELESCOPIC_SASSACRUSHER;
	public static Item REGI_HAMMER;
	public static Item FEAR_NO_ANVIL;
	public static Item MELT_MASHER;
	public static Item Q_E_HAMMER_AXE;
	public static Item D_D_E_HAMMER_AXE;
	public static Item ZILLYHOO_HAMMER;
	public static Item POPAMATIC_VRILLYHOO;
	public static Item SCARLET_ZILLYHOO;
	public static Item MWRTHWL;
	//blades
	public static Item SORD;
	public static Item CACTUS_CUTLASS;
	public static Item STEAK_SWORD;
	public static Item BEEF_SWORD;
	public static Item IRRADIATED_STEAK_SWORD;
	public static Item KATANA;
	public static Item UNBREAKABLE_KATANA;
	public static Item FIRE_POKER;
	public static Item HOT_HANDLE;
	public static Item CALEDSCRATCH;
	public static Item CALEDFWLCH;
	public static Item ROYAL_DERINGER;
	public static Item CLAYMORE;
	public static Item ZILLYWAIR_CUTLASS;
	public static Item REGISWORD;
	public static Item SCARLET_RIBBITAR;
	public static Item DOGG_MACHETE;
	public static Item COBALT_SABRE;
	public static Item QUAMTUM_SABRE;
	public static Item SHATTER_BEACON;
	//axes
	public static Item BATLEACKS;
	public static Item COPSE_CRUSHER;
	public static Item BATTLEAXE;
	public static Item BLACKSMITH_BANE;
	public static Item SCRAXE;
	public static Item Q_P_HAMMER_AXE;
	public static Item RUBY_CROAK;
	public static Item HEPHAESTUS_LUMBER;
	public static Item Q_F_HAMMER_AXE;
	//Dice
	public static Item DICE;
	public static Item FLUORITE_OCTET;
	//misc weapons
	public static Item CAT_CLAWS_DRAWN, CAT_CLAWS_SHEATHED;
	//sickles
	public static Item SICKLE;
	public static Item HOMES_SMELL_YA_LATER;
	public static Item FUDGE_SICKLE;
	public static Item REGI_SICKLE;
	public static Item CLAW_SICKLE;
	public static Item CLAW_OF_NRUBYIGLITH;
	public static Item CANDY_SICKLE;
	//clubs
	public static Item DEUCE_CLUB;
	public static Item NIGHT_CLUB;
	public static Item POGO_CLUB;
	public static Item METAL_BAT;
	public static Item SPIKED_CLUB;
	//canes
	public static Item CANE;
	public static Item IRON_CANE;
	public static Item SPEAR_CANE;
	public static Item PARADISES_PORTABELLO;
	public static Item REGI_CANE;
	public static Item DRAGON_CANE;
	public static Item POGO_CANE;
	//Spoons/forks
	public static Item WOODEN_SPOON;
	public static Item SILVER_SPOON;
	public static Item CROCKER_SPOON, CROCKER_FORK;
	public static Item SKAIA_FORK;
	public static Item FORK;
	public static Item SPORK;
	public static Item GOLDEN_SPORK;
	//Material tools
	public static Item EMERALD_SWORD;
	public static Item EMERALD_AXE;
	public static Item EMERALD_PICKAXE;
	public static Item EMERALD_SHOVEL;
	public static Item EMERALD_HOE;
	//Armor
	public static Item PRISMARINE_HELMET;
	public static Item PRISMARINE_CHESTPLATE;
	public static Item PRISMARINE_LEGGINGS;
	public static Item PRISMARINE_BOOTS;
	
	//Core Items
	public static Item BOONDOLLARS;
	public static Item RAW_CRUXITE;
	public static Item RAW_URANIUM;
	public static Item ENERGY_CORE;
	public static Item CRUXITE_APPLE;
	public static Item CRUXITE_POTION;
	public static Item CLIENT_DISK, SERVER_DISK;
	public static Item CAPTCHA_CARD;
	public static Item STACK_MODUS_CARD, QUEUE_MODUS_CARD, QUEUESTACK_MODUS_CARD, TREE_MODUS_CARD, HASHMAP_MODUS_CARD, SET_MODUS_CARD;
	public static Item SHUNT;
	
	//Food
	public static Item BUG_ON_A_STICK;
	public static Item CHOCOLATE_BEETLE;
	public static Item CONE_OF_FLIES;
	public static Item GRASSHOPPER;
	public static Item JAR_OF_BUGS;
	public static Item ONION;
	public static Item SALAD;
	public static Item DESERT_FRUIT;
	public static Item ROCK_COOKIE;
	public static Item FUNGAL_SPORE;
	public static Item SPOREO;
	public static Item MOREL_MUSHROOM;
	public static Item FRENCH_FRY;
	public static Item STRAWBERRY_CHUNK;
	
	public static Item CANDY_CORN;
	public static Item BUILD_GUSHERS;
	public static Item AMBER_GUMMY_WORM, CAULK_PRETZEL, CHALK_CANDY_CIGARETTE, IODINE_LICORICE, SHALE_PEEP, TAR_LICORICE;
	public static Item COBALT_GUM, MARBLE_JAWBREAKER, MERCURY_SIXLETS, QUARTZ_JELLY_BEAN, SULFUR_CANDY_APPLE;
	public static Item AMETHYST_HARD_CANDY, GARNET_TWIX, RUBY_LOLLIPOP, RUST_GUMMY_EYE;
	public static Item DIAMOND_MINT, GOLD_CANDY_RIBBON, URANIUM_GUMMY_BEAR;
	public static Item ARTIFACT_WARHEAD, ZILLIUM_SKITTLES;
	public static Item TAB;
	public static Item FAYGO, FAYGO_CANDY_APPLE, FAYGO_COLA, FAYGO_COTTON_CANDY, FAYGO_CREME, FAYGO_GRAPE;
	public static Item FAYGO_MOON_MIST, FAYGO_PEACH, FAYGO_REDPOP;
	public static Item IRRADIATED_STEAK;
	public static Item SURPRISE_EMBRYO;
	public static Item UNKNOWABLE_EGG;
	
	//Other Land Items
	public static Item GOLDEN_GRASSHOPPER;
	public static Item BUG_NET;
	public static Item FROG;
	public static Item CARVING_TOOL;
	public static Item CRUMPLY_HAT;
	public static Item STONE_EYEBALLS;
	public static Item STONE_SLAB;
	public static Item SHOP_POSTER;
	
	//Other
	public static Item OIL_BUCKET;
	public static Item BLOOD_BUCKET;
	public static Item BRAIN_JUICE_BUCKET;
	public static Item WATER_COLORS_BUCKET;
	public static Item ENDER_BUCKET;
	public static Item LIGHT_WATER_BUCKET;
	public static Item OBSIDIAN_BUCKET;
	public static Item CAPTCHAROID_CAMERA;
	public static Item GRIMOIRE;
	public static Item LONG_FORGOTTEN_WARHORN;
	public static Item RAZOR_BLADE;
	public static Item UP_STICK;
	public static Item IRON_BOAT, GOLD_BOAT;
	public static Item THRESH_DVD;
	public static Item GAMEBRO_MAGAZINE;
	public static Item GAMEGRL_MAGAZINE;
	public static Item CREW_POSTER;
	public static Item SBAHJ_POSTER;
	//Music disks
	public static Item MUSIC_DISC_EMISSARY_OF_DANCE;
	public static Item MUSIC_DISC_DANCE_STAB_DANCE;
	public static Item MUSIC_DISC_RETRO_BATTLE;
	
	public static void registerItems(IForgeRegistry<Item> registry)
	{
		registerItemBlock(registry, BLACK_CHESS_DIRT, MSItemGroup.MAIN);
		registerItemBlock(registry, WHITE_CHESS_DIRT, MSItemGroup.MAIN);
		registerItemBlock(registry, DARK_GRAY_CHESS_DIRT, MSItemGroup.MAIN);
		registerItemBlock(registry, LIGHT_GRAY_CHESS_DIRT, MSItemGroup.MAIN);
		registerItemBlock(registry, SKAIA_PORTAL, MSItemGroup.MAIN);
		
		registerItemBlock(registry, STONE_CRUXITE_ORE, MSItemGroup.LANDS);
		registerItemBlock(registry, NETHERRACK_CRUXITE_ORE, MSItemGroup.LANDS);
		registerItemBlock(registry, COBBLESTONE_CRUXITE_ORE, MSItemGroup.LANDS);
		registerItemBlock(registry, SANDSTONE_CRUXITE_ORE, MSItemGroup.LANDS);
		registerItemBlock(registry, RED_SANDSTONE_CRUXITE_ORE, MSItemGroup.LANDS);
		registerItemBlock(registry, END_STONE_CRUXITE_ORE, MSItemGroup.LANDS);
		registerItemBlock(registry, PINK_STONE_CRUXITE_ORE, MSItemGroup.LANDS);
		registerItemBlock(registry, STONE_URANIUM_ORE, MSItemGroup.LANDS);
		registerItemBlock(registry, NETHERRACK_URANIUM_ORE, MSItemGroup.LANDS);
		registerItemBlock(registry, COBBLESTONE_URANIUM_ORE, MSItemGroup.LANDS);
		registerItemBlock(registry, SANDSTONE_URANIUM_ORE, MSItemGroup.LANDS);
		registerItemBlock(registry, RED_SANDSTONE_URANIUM_ORE, MSItemGroup.LANDS);
		registerItemBlock(registry, END_STONE_URANIUM_ORE, MSItemGroup.LANDS);
		registerItemBlock(registry, PINK_STONE_URANIUM_ORE, MSItemGroup.LANDS);
		registerItemBlock(registry, NETHERRACK_COAL_ORE, MSItemGroup.LANDS);
		registerItemBlock(registry, PINK_STONE_COAL_ORE, MSItemGroup.LANDS);
		registerItemBlock(registry, END_STONE_IRON_ORE, MSItemGroup.LANDS);
		registerItemBlock(registry, SANDSTONE_IRON_ORE, MSItemGroup.LANDS);
		registerItemBlock(registry, RED_SANDSTONE_IRON_ORE, MSItemGroup.LANDS);
		registerItemBlock(registry, SANDSTONE_GOLD_ORE, MSItemGroup.LANDS);
		registerItemBlock(registry, RED_SANDSTONE_GOLD_ORE, MSItemGroup.LANDS);
		registerItemBlock(registry, PINK_STONE_GOLD_ORE, MSItemGroup.LANDS);
		registerItemBlock(registry, END_STONE_REDSTONE_ORE, MSItemGroup.LANDS);
		registerItemBlock(registry, STONE_QUARTZ_ORE, MSItemGroup.LANDS);
		registerItemBlock(registry, PINK_STONE_LAPIS_ORE, MSItemGroup.LANDS);
		registerItemBlock(registry, PINK_STONE_DIAMOND_ORE, MSItemGroup.LANDS);
		
		registerItemBlock(registry, CRUXITE_BLOCK, MSItemGroup.MAIN);
		registerItemBlock(registry, URANIUM_BLOCK, MSItemGroup.MAIN);
		registerItemBlock(registry, GENERIC_OBJECT, MSItemGroup.MAIN);
		
		registerItemBlock(registry, BLUE_DIRT, MSItemGroup.LANDS);
		registerItemBlock(registry, THOUGHT_DIRT, MSItemGroup.LANDS);
		registerItemBlock(registry, COARSE_STONE, MSItemGroup.LANDS);
		registerItemBlock(registry, CHISELED_COARSE_STONE, MSItemGroup.LANDS);
		registerItemBlock(registry, SHADE_BRICKS, MSItemGroup.LANDS);
		registerItemBlock(registry, SMOOTH_SHADE_STONE, MSItemGroup.LANDS);
		registerItemBlock(registry, FROST_BRICKS, MSItemGroup.LANDS);
		registerItemBlock(registry, FROST_TILE, MSItemGroup.LANDS);
		registerItemBlock(registry, CHISELED_FROST_BRICKS, MSItemGroup.LANDS);
		registerItemBlock(registry, CAST_IRON, MSItemGroup.LANDS);
		registerItemBlock(registry, CHISELED_CAST_IRON, MSItemGroup.LANDS);
		registerItemBlock(registry, MYCELIUM_BRICKS, MSItemGroup.LANDS);
		registerItemBlock(registry, BLACK_STONE, MSItemGroup.LANDS);
		registerItemBlock(registry, FLOWERY_MOSSY_COBBLESTONE, MSItemGroup.LANDS);
		registerItemBlock(registry, FLOWERY_MOSSY_STONE_BRICKS, MSItemGroup.LANDS);
		registerItemBlock(registry, COARSE_END_STONE, MSItemGroup.LANDS);
		registerItemBlock(registry, END_GRASS, MSItemGroup.LANDS);
		registerItemBlock(registry, CHALK, MSItemGroup.LANDS);
		registerItemBlock(registry, CHALK_BRICKS, MSItemGroup.LANDS);
		registerItemBlock(registry, CHISELED_CHALK_BRICKS, MSItemGroup.LANDS);
		registerItemBlock(registry, POLISHED_CHALK, MSItemGroup.LANDS);
		registerItemBlock(registry, PINK_STONE, MSItemGroup.LANDS);
		registerItemBlock(registry, PINK_STONE_BRICKS, MSItemGroup.LANDS);
		registerItemBlock(registry, CHISELED_PINK_STONE_BRICKS, MSItemGroup.LANDS);
		registerItemBlock(registry, CRACKED_PINK_STONE_BRICKS, MSItemGroup.LANDS);
		registerItemBlock(registry, MOSSY_PINK_STONE_BRICKS, MSItemGroup.LANDS);
		registerItemBlock(registry, POLISHED_PINK_STONE, MSItemGroup.LANDS);
		registerItemBlock(registry, DENSE_CLOUD, MSItemGroup.LANDS);
		registerItemBlock(registry, BRIGHT_DENSE_CLOUD, MSItemGroup.LANDS);
		registerItemBlock(registry, SUGAR_CUBE, MSItemGroup.LANDS);
		
		registerItemBlock(registry, GLOWING_LOG, MSItemGroup.LANDS);
		registerItemBlock(registry, FROST_LOG, MSItemGroup.LANDS);
		registerItemBlock(registry, RAINBOW_LOG, MSItemGroup.LANDS);
		registerItemBlock(registry, END_LOG, MSItemGroup.LANDS);
		registerItemBlock(registry, VINE_LOG, MSItemGroup.LANDS);
		registerItemBlock(registry, FLOWERY_VINE_LOG, MSItemGroup.LANDS);
		registerItemBlock(registry, DEAD_LOG, MSItemGroup.LANDS);
		registerItemBlock(registry, PETRIFIED_LOG, MSItemGroup.LANDS);
		registerItemBlock(registry, GLOWING_WOOD, MSItemGroup.LANDS);
		registerItemBlock(registry, FROST_WOOD, MSItemGroup.LANDS);
		registerItemBlock(registry, RAINBOW_WOOD, MSItemGroup.LANDS);
		registerItemBlock(registry, END_WOOD, MSItemGroup.LANDS);
		registerItemBlock(registry, VINE_WOOD, MSItemGroup.LANDS);
		registerItemBlock(registry, FLOWERY_VINE_WOOD, MSItemGroup.LANDS);
		registerItemBlock(registry, DEAD_WOOD, MSItemGroup.LANDS);
		registerItemBlock(registry, PETRIFIED_WOOD, MSItemGroup.LANDS);
		registerItemBlock(registry, GLOWING_PLANKS, MSItemGroup.LANDS);
		registerItemBlock(registry, FROST_PLANKS, MSItemGroup.LANDS);
		registerItemBlock(registry, RAINBOW_PLANKS, MSItemGroup.LANDS);
		registerItemBlock(registry, END_PLANKS, MSItemGroup.LANDS);
		registerItemBlock(registry, DEAD_PLANKS, MSItemGroup.LANDS);
		registerItemBlock(registry, TREATED_PLANKS, MSItemGroup.LANDS);
		registerItemBlock(registry, FROST_LEAVES, MSItemGroup.LANDS);
		registerItemBlock(registry, RAINBOW_LEAVES, MSItemGroup.LANDS);
		registerItemBlock(registry, END_LEAVES, MSItemGroup.LANDS);
		registerItemBlock(registry, RAINBOW_SAPLING, MSItemGroup.LANDS);
		registerItemBlock(registry, END_SAPLING, MSItemGroup.LANDS);
		
		registerItemBlock(registry, BLOOD_ASPECT_LOG, MSItemGroup.LANDS);
		registerItemBlock(registry, BREATH_ASPECT_LOG, MSItemGroup.LANDS);
		registerItemBlock(registry, DOOM_ASPECT_LOG, MSItemGroup.LANDS);
		registerItemBlock(registry, HEART_ASPECT_LOG, MSItemGroup.LANDS);
		registerItemBlock(registry, HOPE_ASPECT_LOG, MSItemGroup.LANDS);
		registerItemBlock(registry, LIFE_ASPECT_LOG, MSItemGroup.LANDS);
		registerItemBlock(registry, LIGHT_ASPECT_LOG, MSItemGroup.LANDS);
		registerItemBlock(registry, MIND_ASPECT_LOG, MSItemGroup.LANDS);
		registerItemBlock(registry, RAGE_ASPECT_LOG, MSItemGroup.LANDS);
		registerItemBlock(registry, SPACE_ASPECT_LOG, MSItemGroup.LANDS);
		registerItemBlock(registry, TIME_ASPECT_LOG, MSItemGroup.LANDS);
		registerItemBlock(registry, VOID_ASPECT_LOG, MSItemGroup.LANDS);
		registerItemBlock(registry, BLOOD_ASPECT_PLANKS, MSItemGroup.LANDS);
		registerItemBlock(registry, BREATH_ASPECT_PLANKS, MSItemGroup.LANDS);
		registerItemBlock(registry, DOOM_ASPECT_PLANKS, MSItemGroup.LANDS);
		registerItemBlock(registry, HEART_ASPECT_PLANKS, MSItemGroup.LANDS);
		registerItemBlock(registry, HOPE_ASPECT_PLANKS, MSItemGroup.LANDS);
		registerItemBlock(registry, LIFE_ASPECT_PLANKS, MSItemGroup.LANDS);
		registerItemBlock(registry, LIGHT_ASPECT_PLANKS, MSItemGroup.LANDS);
		registerItemBlock(registry, MIND_ASPECT_PLANKS, MSItemGroup.LANDS);
		registerItemBlock(registry, RAGE_ASPECT_PLANKS, MSItemGroup.LANDS);
		registerItemBlock(registry, SPACE_ASPECT_PLANKS, MSItemGroup.LANDS);
		registerItemBlock(registry, TIME_ASPECT_PLANKS, MSItemGroup.LANDS);
		registerItemBlock(registry, VOID_ASPECT_PLANKS, MSItemGroup.LANDS);
		registerItemBlock(registry, BLOOD_ASPECT_LEAVES, MSItemGroup.LANDS);
		registerItemBlock(registry, BREATH_ASPECT_LEAVES, MSItemGroup.LANDS);
		registerItemBlock(registry, DOOM_ASPECT_LEAVES, MSItemGroup.LANDS);
		registerItemBlock(registry, HEART_ASPECT_LEAVES, MSItemGroup.LANDS);
		registerItemBlock(registry, HOPE_ASPECT_LEAVES, MSItemGroup.LANDS);
		registerItemBlock(registry, LIFE_ASPECT_LEAVES, MSItemGroup.LANDS);
		registerItemBlock(registry, LIGHT_ASPECT_LEAVES, MSItemGroup.LANDS);
		registerItemBlock(registry, MIND_ASPECT_LEAVES, MSItemGroup.LANDS);
		registerItemBlock(registry, RAGE_ASPECT_LEAVES, MSItemGroup.LANDS);
		registerItemBlock(registry, SPACE_ASPECT_LEAVES, MSItemGroup.LANDS);
		registerItemBlock(registry, TIME_ASPECT_LEAVES, MSItemGroup.LANDS);
		registerItemBlock(registry, VOID_ASPECT_LEAVES, MSItemGroup.LANDS);
		registerItemBlock(registry, BLOOD_ASPECT_SAPLING, MSItemGroup.LANDS);
		registerItemBlock(registry, BREATH_ASPECT_SAPLING, MSItemGroup.LANDS);
		registerItemBlock(registry, DOOM_ASPECT_SAPLING, MSItemGroup.LANDS);
		registerItemBlock(registry, HEART_ASPECT_SAPLING, MSItemGroup.LANDS);
		registerItemBlock(registry, HOPE_ASPECT_SAPLING, MSItemGroup.LANDS);
		registerItemBlock(registry, LIFE_ASPECT_SAPLING, MSItemGroup.LANDS);
		registerItemBlock(registry, LIGHT_ASPECT_SAPLING, MSItemGroup.LANDS);
		registerItemBlock(registry, MIND_ASPECT_SAPLING, MSItemGroup.LANDS);
		registerItemBlock(registry, RAGE_ASPECT_SAPLING, MSItemGroup.LANDS);
		registerItemBlock(registry, SPACE_ASPECT_SAPLING, MSItemGroup.LANDS);
		registerItemBlock(registry, TIME_ASPECT_SAPLING, MSItemGroup.LANDS);
		registerItemBlock(registry, VOID_ASPECT_SAPLING, MSItemGroup.LANDS);
		
		registerItemBlock(registry, GLOWING_MUSHROOM, MSItemGroup.LANDS);
		registerItemBlock(registry, DESERT_BUSH, MSItemGroup.LANDS);
		registerItemBlock(registry, BLOOMING_CACTUS, MSItemGroup.LANDS);
		registerItemBlock(registry, PETRIFIED_GRASS, MSItemGroup.LANDS);
		registerItemBlock(registry, PETRIFIED_POPPY, MSItemGroup.LANDS);
		registerItemBlock(registry, STRAWBERRY, MSItemGroup.LANDS);
		
		registerItemBlock(registry, GLOWY_GOOP, MSItemGroup.LANDS);
		registerItemBlock(registry, COAGULATED_BLOOD, MSItemGroup.LANDS);
		registerItemBlock(registry, VEIN, MSItemGroup.LANDS);
		registerItemBlock(registry, VEIN_CORNER, MSItemGroup.LANDS);
		registerItemBlock(registry, INVERTED_VEIN_CORNER, MSItemGroup.LANDS);
		
		registerItemBlock(registry, COARSE_STONE_STAIRS, MSItemGroup.LANDS);
		registerItemBlock(registry, SHADE_BRICK_STAIRS, MSItemGroup.LANDS);
		registerItemBlock(registry, FROST_BRICK_STAIRS, MSItemGroup.LANDS);
		registerItemBlock(registry, CAST_IRON_STAIRS, MSItemGroup.LANDS);
		registerItemBlock(registry, MYCELIUM_BRICK_STAIRS, MSItemGroup.LANDS);
		registerItemBlock(registry, CHALK_STAIRS, MSItemGroup.LANDS);
		registerItemBlock(registry, CHALK_BRICK_STAIRS, MSItemGroup.LANDS);
		registerItemBlock(registry, PINK_STONE_BRICK_STAIRS, MSItemGroup.LANDS);
		registerItemBlock(registry, RAINBOW_PLANKS_STAIRS, MSItemGroup.LANDS);
		registerItemBlock(registry, END_PLANKS_STAIRS, MSItemGroup.LANDS);
		registerItemBlock(registry, DEAD_PLANKS_STAIRS, MSItemGroup.LANDS);
		registerItemBlock(registry, TREATED_PLANKS_STAIRS, MSItemGroup.LANDS);
		registerItemBlock(registry, CHALK_SLAB, MSItemGroup.LANDS);
		registerItemBlock(registry, CHALK_BRICK_SLAB, MSItemGroup.LANDS);
		registerItemBlock(registry, PINK_STONE_BRICK_SLAB, MSItemGroup.LANDS);
		registerItemBlock(registry, RAINBOW_PLANKS_SLAB, MSItemGroup.LANDS);
		registerItemBlock(registry, END_PLANKS_SLAB, MSItemGroup.LANDS);
		registerItemBlock(registry, DEAD_PLANKS_SLAB, MSItemGroup.LANDS);
		registerItemBlock(registry, TREATED_PLANKS_SLAB, MSItemGroup.LANDS);
		
		registry.register(new CruxtruderItem(CRUXTRUDER.getMainBlock(), new Item.Properties().group(MSItemGroup.MAIN)).setRegistryName("cruxtruder"));
		registerItemBlock(registry, CRUXTRUDER_LID, MSItemGroup.MAIN);
		registry.register(new TotemLatheItem(TOTEM_LATHE.getMainBlock(), new Item.Properties().group(MSItemGroup.MAIN)).setRegistryName("totem_lathe"));
		registry.register(new AlchemiterItem(ALCHEMITER.getMainBlock(), new Item.Properties().group(MSItemGroup.MAIN)).setRegistryName("alchemiter"));
		registry.register(new PunchDesignixItem(PUNCH_DESIGNIX.getMainBlock(), new Item.Properties().group(MSItemGroup.MAIN)).setRegistryName("punch_designix"));
		registerItemBlock(registry, new MiniCruxtruderItem(MINI_CRUXTRUDER, new Item.Properties().group(MSItemGroup.MAIN)));
		registerItemBlock(registry, MINI_TOTEM_LATHE, MSItemGroup.MAIN);
		registerItemBlock(registry, MINI_ALCHEMITER, MSItemGroup.MAIN);
		registerItemBlock(registry, MINI_PUNCH_DESIGNIX, MSItemGroup.MAIN);
		/*registerItemBlock(registry, new ItemBlock(holopad));
		registerItemBlock(registry, new ItemJumperBlock(jumperBlockExtension[0]));*/
		
		registerItemBlock(registry, COMPUTER_OFF, MSItemGroup.MAIN);
		registerItemBlock(registry, LAPTOP_OFF, MSItemGroup.MAIN);
		registerItemBlock(registry, CROCKERTOP_OFF, MSItemGroup.MAIN);
		registerItemBlock(registry, HUBTOP_OFF, MSItemGroup.MAIN);
		registerItemBlock(registry, LUNCHTOP_OFF, MSItemGroup.MAIN);
		registerItemBlock(registry, new TransportalizerItem(TRANSPORTALIZER, new Item.Properties().group(MSItemGroup.MAIN)));
		registerItemBlock(registry, GRIST_WIDGET, MSItemGroup.MAIN);
		registerItemBlock(registry, URANIUM_COOKER, MSItemGroup.MAIN);
		
		registerItemBlock(registry, new DowelItem(CRUXITE_DOWEL, new Item.Properties().group(MSItemGroup.MAIN)));
		
		registerItemBlock(registry, GOLD_SEEDS, MSItemGroup.MAIN);
		registerItemBlock(registry, WOODEN_CACTUS, MSItemGroup.MAIN);
		
		registerItemBlock(registry, new BlockItem(APPLE_CAKE, new Item.Properties().group(MSItemGroup.MAIN).maxStackSize(1)));
		registerItemBlock(registry, new BlockItem(BLUE_CAKE, new Item.Properties().group(MSItemGroup.MAIN).maxStackSize(1)));
		registerItemBlock(registry, new BlockItem(COLD_CAKE, new Item.Properties().group(MSItemGroup.MAIN).maxStackSize(1)));
		registerItemBlock(registry, new BlockItem(RED_CAKE, new Item.Properties().group(MSItemGroup.MAIN).maxStackSize(1)));
		registerItemBlock(registry, new BlockItem(HOT_CAKE, new Item.Properties().group(MSItemGroup.MAIN).maxStackSize(1)));
		registerItemBlock(registry, new BlockItem(REVERSE_CAKE, new Item.Properties().group(MSItemGroup.MAIN).maxStackSize(1)));
		registerItemBlock(registry, new BlockItem(FUCHSIA_CAKE, new Item.Properties().group(MSItemGroup.MAIN).maxStackSize(1)));
		
		registerItemBlock(registry, PRIMED_TNT, MSItemGroup.MAIN);
		registerItemBlock(registry, UNSTABLE_TNT, MSItemGroup.MAIN);
		registerItemBlock(registry, INSTANT_TNT, MSItemGroup.MAIN);
		registerItemBlock(registry, WOODEN_EXPLOSIVE_BUTTON, MSItemGroup.MAIN);
		registerItemBlock(registry, STONE_EXPLOSIVE_BUTTON, MSItemGroup.MAIN);
		
		registerItemBlock(registry, BLENDER, MSItemGroup.MAIN);
		registerItemBlock(registry, CHESSBOARD, MSItemGroup.MAIN);
		registerItemBlock(registry, MINI_FROG_STATUE, MSItemGroup.MAIN);
		registerItemBlock(registry, GLOWYSTONE_DUST, MSItemGroup.MAIN);
		
		//hammers
		registry.register(CLAW_HAMMER = new WeaponItem(ItemTier.IRON, 2, -2.4F, 1.0F, new Item.Properties().defaultMaxDamage(131).addToolType(ToolType.PICKAXE, 0).group(MSItemGroup.WEAPONS)).setRegistryName("claw_hammer"));
		registry.register(SLEDGE_HAMMER = new WeaponItem(ItemTier.IRON, 4, -2.8F, 4.0F, new Item.Properties().addToolType(ToolType.PICKAXE, 2).group(MSItemGroup.WEAPONS)).setRegistryName("sledge_hammer"));
		registry.register(BLACKSMITH_HAMMER = new WeaponItem(ItemTier.IRON, 5, -2.8F, 3.5F, new Item.Properties().defaultMaxDamage(450).addToolType(ToolType.PICKAXE, 2).group(MSItemGroup.WEAPONS)).setRegistryName("blacksmith_hammer"));
		registry.register(POGO_HAMMER = new PogoWeaponItem(MSItemTypes.POGO_TIER, 5, -2.8F, 2.0F, 0.7, new Item.Properties().addToolType(ToolType.PICKAXE, 1).group(MSItemGroup.WEAPONS)).setRegistryName("pogo_hammer"));
		registry.register(TELESCOPIC_SASSACRUSHER = new WeaponItem(MSItemTypes.BOOK_TIER, 8, -2.9F, 5.0F, new Item.Properties().defaultMaxDamage(1024).addToolType(ToolType.PICKAXE, 2).group(MSItemGroup.WEAPONS)).setRegistryName("telescopic_sassacrusher"));
		registry.register(REGI_HAMMER = new WeaponItem(MSItemTypes.REGI_TIER, 3, -2.4F, 8.0F, new Item.Properties().addToolType(ToolType.PICKAXE, 2).group(MSItemGroup.WEAPONS)).setRegistryName("regi_hammer"));
		registry.register(FEAR_NO_ANVIL = new PotionWeaponItem(MSItemTypes.RUBY_TIER, 6, -2.8F, 7.0F, new EffectInstance(Effects.SLOWNESS, 100, 3), new Item.Properties().addToolType(ToolType.PICKAXE, 3).group(MSItemGroup.WEAPONS)).setRegistryName("fear_no_anvil"));
		registry.register(MELT_MASHER = new FireWeaponItem(MSItemTypes.RUBY_TIER, 6, -2.8F, 12.0F, 25, new Item.Properties().defaultMaxDamage(1413).addToolType(ToolType.PICKAXE, 4).group(MSItemGroup.WEAPONS)).setRegistryName("melt_masher"));
		registry.register(Q_E_HAMMER_AXE = new PogoFarmineItem(MSItemTypes.RUBY_TIER, 5, -2.8F, 9.0F, Integer.MAX_VALUE, 200, 0.7, new Item.Properties().defaultMaxDamage(6114).addToolType(ToolType.PICKAXE, 3).addToolType(ToolType.SHOVEL, 1).addToolType(ToolType.AXE, 2).group(MSItemGroup.WEAPONS)).setRegistryName("estrogen_empowered_everything_eradicator"));
		registry.register(D_D_E_HAMMER_AXE = new SbahjEEEEItem(MSItemTypes.RUBY_TIER, 5, -2.8F, 9.1F, 0.2, new Item.Properties().defaultMaxDamage(6114).group(MSItemGroup.WEAPONS)).setRegistryName("eeeeeeeeeeee"));
		registry.register(ZILLYHOO_HAMMER = new WeaponItem(MSItemTypes.ZILLYHOO_TIER, 6, -2.8F, 15.0F, new Item.Properties().addToolType(ToolType.PICKAXE, 4).group(MSItemGroup.WEAPONS)).setRegistryName("zillyhoo_hammer"));
		registry.register(POPAMATIC_VRILLYHOO = new RandomWeaponItem(MSItemTypes.ZILLYHOO_TIER, 3, -2.8F, 15.0F, new Item.Properties().addToolType(ToolType.PICKAXE, 4).group(MSItemGroup.WEAPONS)).setRegistryName("popamatic_vrillyhoo"));
		registry.register(SCARLET_ZILLYHOO = new FireWeaponItem(MSItemTypes.RUBY_TIER, 6, -2.8F, 4.0F, 50, new Item.Properties().addToolType(ToolType.PICKAXE, 3)).setRegistryName("scarlet_zillyhoo"));
		registry.register(MWRTHWL = new WeaponItem(MSItemTypes.RUBY_TIER, 6, -2.8F, 4.0F, new Item.Properties().addToolType(ToolType.PICKAXE, 3)).setRegistryName("mwrthwl"));
		
		//blades
		registry.register(SORD = new SordItem(MSItemTypes.SBAHJ_TIER, 3, -2.4F, 1.0F, new Item.Properties().group(MSItemGroup.WEAPONS)).setRegistryName("sord"));
		registry.register(CACTUS_CUTLASS = new WeaponItem(MSItemTypes.CACTUS_TIER, 3, -2.4F, 15.0F, new Item.Properties().group(MSItemGroup.WEAPONS)).setRegistryName("cactaceae_cutlass"));	//The sword harvestTool is only used against webs, hence the high efficiency.
		registry.register(STEAK_SWORD = new ConsumableWeaponItem(MSItemTypes.MEAT_TIER, 4, -2.4F, 5.0F, 8, 1F, new Item.Properties().defaultMaxDamage(250).group(MSItemGroup.WEAPONS)).setRegistryName("steak_sword"));
		registry.register(BEEF_SWORD = new ConsumableWeaponItem(MSItemTypes.MEAT_TIER, 2, -2.4F, 5.0F, 3, 0.8F, 75, new Item.Properties().group(MSItemGroup.WEAPONS)).setRegistryName("beef_sword"));
		registry.register(IRRADIATED_STEAK_SWORD = new ConsumableWeaponItem(MSItemTypes.MEAT_TIER, 2, -2.4F, 5.0F, 4, 0.4F, 25, new Item.Properties().defaultMaxDamage(150).group(MSItemGroup.WEAPONS)).setPotionEffect(new EffectInstance(Effects.WITHER, 100, 1), 0.9F).setRegistryName("irradiated_steak_sword"));
		registry.register(KATANA = new WeaponItem(ItemTier.IRON, 3, -2.4F, 15.0F, new Item.Properties().group(MSItemGroup.WEAPONS)).setRegistryName("katana"));
		registry.register(UNBREAKABLE_KATANA = new WeaponItem(MSItemTypes.RUBY_TIER, 3, -2.4F, 15.0F, new Item.Properties().defaultMaxDamage(-1).group(MSItemGroup.WEAPONS)).setRegistryName("unbreakable_katana"));	//Actually unbreakable
		registry.register(FIRE_POKER = new FireWeaponItem(ItemTier.IRON, 4, -2.4F, 15.0F,  30, new Item.Properties().group(MSItemGroup.WEAPONS)).setRegistryName("fire_poker"));
		registry.register(HOT_HANDLE = new FireWeaponItem(ItemTier.IRON, 3, -2.4F, 15.0F, 10, new Item.Properties().defaultMaxDamage(350).group(MSItemGroup.WEAPONS)).setRegistryName("too_hot_to_handle"));
		registry.register(CALEDSCRATCH = new WeaponItem(MSItemTypes.RUBY_TIER, 2, -2.4F, 15.0F, new Item.Properties().defaultMaxDamage(1561).group(MSItemGroup.WEAPONS)).setRegistryName("caledscratch"));
		registry.register(CALEDFWLCH = new WeaponItem(MSItemTypes.RUBY_TIER, 2, -2.4F, 15.0F, new Item.Properties().defaultMaxDamage(1025).group(MSItemGroup.WEAPONS)).setRegistryName("caledfwlch"));
		registry.register(ROYAL_DERINGER = new WeaponItem(MSItemTypes.RUBY_TIER, 3, -2.4F, 15.0F, new Item.Properties().defaultMaxDamage(1561).group(MSItemGroup.WEAPONS)).setRegistryName("royal_deringer"));
		registry.register(CLAYMORE = new WeaponItem(ItemTier.IRON, 5, -2.6F, 15.0F, new Item.Properties().defaultMaxDamage(600).group(MSItemGroup.WEAPONS)).setRegistryName("claymore"));
		registry.register(ZILLYWAIR_CUTLASS = new WeaponItem(MSItemTypes.ZILLYHOO_TIER, 3, -2.4F, 15.0F, new Item.Properties().group(MSItemGroup.WEAPONS)).setRegistryName("cutlass_of_zillywair"));
		registry.register(REGISWORD = new WeaponItem(MSItemTypes.REGI_TIER, 3, -2.4F, 15.0F, new Item.Properties().group(MSItemGroup.WEAPONS)).setRegistryName("regisword"));
		registry.register(SCARLET_RIBBITAR = new WeaponItem(MSItemTypes.RUBY_TIER, 3, -2.4F, 15.0F, new Item.Properties().group(MSItemGroup.WEAPONS)).setRegistryName("scarlet_ribbitar"));
		registry.register(DOGG_MACHETE = new WeaponItem(MSItemTypes.RUBY_TIER, 1, -2.4F, 15.0F, new Item.Properties().defaultMaxDamage(1000).group(MSItemGroup.WEAPONS)).setRegistryName("dogg_machete"));
		registry.register(COBALT_SABRE = new FireWeaponItem(ItemTier.GOLD, 7, -2.4F, 15.0F, 30, new Item.Properties().defaultMaxDamage(300).group(MSItemGroup.WEAPONS)).setRegistryName("cobalt_sabre"));
		registry.register(QUAMTUM_SABRE = new PotionWeaponItem(MSItemTypes.URANIUM_TIER, 4, -2.4F, 15.0F, new EffectInstance(Effects.WITHER, 100, 1), new Item.Properties().group(MSItemGroup.WEAPONS)).setRegistryName("quantum_sabre"));
		registry.register(SHATTER_BEACON = new WeaponItem(MSItemTypes.RUBY_TIER, 6, -2.4F, 15.0F, new Item.Properties().defaultMaxDamage(1850).group(MSItemGroup.WEAPONS)).setRegistryName("shatter_beacon"));
		
		//axes
		registry.register(BATLEACKS = new SordItem(MSItemTypes.SBAHJ_TIER, 5, -3.5F, 1.0F, new Item.Properties().group(MSItemGroup.WEAPONS)).setRegistryName("batleacks"));
		registry.register(COPSE_CRUSHER = new FarmineItem(ItemTier.STONE, 5, -3.0F, 6.0F, Integer.MAX_VALUE, 20, new Item.Properties().defaultMaxDamage(400).addToolType(ToolType.AXE, 2).group(MSItemGroup.WEAPONS)).setRegistryName("copse_crusher"));
		registry.register(BATTLEAXE = new WeaponItem(ItemTier.IRON, 8, -3.0F, 3.0F, new Item.Properties().defaultMaxDamage(600).group(MSItemGroup.WEAPONS).addToolType(ToolType.AXE, 2)).setRegistryName("battleaxe"));
		registry.register(BLACKSMITH_BANE = new WeaponItem(ItemTier.STONE, 8, -3.0F, 6.0F, new Item.Properties().defaultMaxDamage(413).group(MSItemGroup.WEAPONS).addToolType(ToolType.AXE, 2)).setRegistryName("blacksmith_bane"));
		registry.register(SCRAXE = new WeaponItem(ItemTier.IRON, 8, -3.0F, 7.0F, new Item.Properties().defaultMaxDamage(500).group(MSItemGroup.WEAPONS).addToolType(ToolType.AXE, 2)).setRegistryName("scraxe"));
		registry.register(Q_P_HAMMER_AXE = new PogoFarmineItem(MSItemTypes.POGO_TIER, 6, -3.0F, 2.0F, Integer.MAX_VALUE, 50, 0.6, new Item.Properties().defaultMaxDamage(800).group(MSItemGroup.WEAPONS).addToolType(ToolType.AXE, 2).addToolType(ToolType.PICKAXE, 1)).setRegistryName("piston_powered_pogo_axehammer"));
		registry.register(RUBY_CROAK = new WeaponItem(MSItemTypes.RUBY_TIER, 7, -3.0F, 8.0F, new Item.Properties().group(MSItemGroup.WEAPONS).addToolType(ToolType.AXE, 3)).setRegistryName("ruby_croak"));
		registry.register(HEPHAESTUS_LUMBER = new FireWeaponItem(MSItemTypes.RUBY_TIER, 7, -3.0F, 9.0F, 30, new Item.Properties().defaultMaxDamage(3000).group(MSItemGroup.WEAPONS).addToolType(ToolType.AXE, 3)).setRegistryName("hephaestus_lumberjack"));
		registry.register(Q_F_HAMMER_AXE = new PogoFarmineItem(MSItemTypes.URANIUM_TIER, 7, -3.0F, 5.0F, Integer.MAX_VALUE, 100, 0.7, new Item.Properties().defaultMaxDamage(2048).group(MSItemGroup.WEAPONS).addToolType(ToolType.PICKAXE, 2).addToolType(ToolType.AXE, 3)).setRegistryName("fission_focused_fault_feller"));
		
		//Dice
		registry.register(DICE = new WeaponItem(ItemTier.STONE, 5, -3.0F, 1.0F, new Item.Properties().defaultMaxDamage(51).group(MSItemGroup.WEAPONS)).setRegistryName("dice"));
		registry.register(FLUORITE_OCTET = new WeaponItem(ItemTier.DIAMOND, 12, -3.0F, 1.0F, new Item.Properties().defaultMaxDamage(67).group(MSItemGroup.WEAPONS)).setRegistryName("fluorite_octet"));
		//misc weapons
		registry.register(CAT_CLAWS_DRAWN  = new DualWeaponItem(ItemTier.IRON, 2, -1.5F, 10.0F, new Item.Properties().defaultMaxDamage(500).group(MSItemGroup.WEAPONS)).setRegistryName("cat_claws_drawn"));
		registry.register(CAT_CLAWS_SHEATHED  = new DualWeaponItem(ItemTier.IRON, -1, -1.0F, 10.0F, (DualWeaponItem) CAT_CLAWS_DRAWN, new Item.Properties().defaultMaxDamage(500)).setRegistryName("cat_claws_sheathed"));
		//sickles
		registry.register(SICKLE = new WeaponItem(ItemTier.IRON, 2, -2.4F, 1.5F, new Item.Properties().group(MSItemGroup.WEAPONS).addToolType(MSItemTypes.SICKLE_TOOL, 0)).setRegistryName("sickle"));
		registry.register(HOMES_SMELL_YA_LATER = new WeaponItem(ItemTier.IRON, 3, -2.4F, 3.0F, new Item.Properties().defaultMaxDamage(400).group(MSItemGroup.WEAPONS).addToolType(MSItemTypes.SICKLE_TOOL, 0)).setRegistryName("homes_smell_ya_later"));
		registry.register(FUDGE_SICKLE = new ConsumableWeaponItem(MSItemTypes.CANDY_TIER, 5, -2.4F, 1.0F, 7, 0.6F, new Item.Properties().group(MSItemGroup.WEAPONS).addToolType(MSItemTypes.SICKLE_TOOL, 0)).setRegistryName("fudgesickle"));
		registry.register(REGI_SICKLE = new WeaponItem(MSItemTypes.REGI_TIER, 3, -2.4F, 4.0F, new Item.Properties().group(MSItemGroup.WEAPONS).addToolType(MSItemTypes.SICKLE_TOOL, 0)).setRegistryName("regisickle"));
		registry.register(CLAW_SICKLE = new WeaponItem(MSItemTypes.RUBY_TIER, 3, -2.4F, 4.0F, new Item.Properties().group(MSItemGroup.WEAPONS).addToolType(MSItemTypes.SICKLE_TOOL, 0)).setRegistryName("claw_sickle"));
		registry.register(CLAW_OF_NRUBYIGLITH = new HorrorterrorWeaponItem(MSItemTypes.HORRORTERROR_TIER, 5, -2.4F, 4.0F, new Item.Properties().group(MSItemGroup.WEAPONS).addToolType(MSItemTypes.SICKLE_TOOL, 0)).setRegistryName("claw_of_nrubyiglith"));
		registry.register(CANDY_SICKLE = new CandyWeaponItem(MSItemTypes.CANDY_TIER, 6, -2.4F, 2.5F, new Item.Properties().defaultMaxDamage(96).group(MSItemGroup.WEAPONS).addToolType(MSItemTypes.SICKLE_TOOL, 0)).setRegistryName("candy_sickle"));
		
		//clubs
		registry.register(DEUCE_CLUB = new WeaponItem(ItemTier.WOOD, 3, -2.2F, 2.0F, new Item.Properties().defaultMaxDamage(1024).group(MSItemGroup.WEAPONS)).setRegistryName("deuce_club"));
		registry.register(NIGHT_CLUB = new WeaponItem(MSItemTypes.REGI_TIER, 1, -2.2F, 2.0F, new Item.Properties().defaultMaxDamage(600).group(MSItemGroup.WEAPONS)).setRegistryName("night_club"));
		registry.register(POGO_CLUB = new PogoWeaponItem(MSItemTypes.POGO_TIER, 2, -2.2F, 2.0F, 0.5, new Item.Properties().group(MSItemGroup.WEAPONS)).setRegistryName("pogo_club"));
		registry.register(METAL_BAT = new WeaponItem(ItemTier.IRON, 3, -2.2F, 2.0F, new Item.Properties().defaultMaxDamage(750).group(MSItemGroup.WEAPONS)).setRegistryName("metal_bat"));
		registry.register(SPIKED_CLUB = new WeaponItem(ItemTier.WOOD, 5, -2.2F, 2.0F, new Item.Properties().defaultMaxDamage(500).group(MSItemGroup.WEAPONS)).setRegistryName("spiked_club"));
		
		//canes
		registry.register(CANE = new WeaponItem(ItemTier.WOOD, 2, -2.0F, 1.0F, new Item.Properties().defaultMaxDamage(100).group(MSItemGroup.WEAPONS)).setRegistryName("cane"));
		registry.register(IRON_CANE = new WeaponItem(ItemTier.IRON, 2, -2.0F, 1.0F, new Item.Properties().defaultMaxDamage(450).group(MSItemGroup.WEAPONS)).setRegistryName("iron_cane"));
		registry.register(SPEAR_CANE = new WeaponItem(ItemTier.IRON, 3, -2.0F, 1.0F, new Item.Properties().defaultMaxDamage(300).group(MSItemGroup.WEAPONS)).setRegistryName("spear_cane"));
		registry.register(PARADISES_PORTABELLO = new WeaponItem(MSItemTypes.CANDY_TIER, 3, -2.0F, 1.0F, new Item.Properties().defaultMaxDamage(175).group(MSItemGroup.WEAPONS)).setRegistryName("paradises_portabello"));
		registry.register(REGI_CANE = new WeaponItem(MSItemTypes.REGI_TIER, 3, -2.0F, 1.0F, new Item.Properties().group(MSItemGroup.WEAPONS)).setRegistryName("regi_cane"));
		registry.register(DRAGON_CANE = new WeaponItem(MSItemTypes.RUBY_TIER, 3, -2.0F, 1.0F, new Item.Properties().defaultMaxDamage(300).group(MSItemGroup.WEAPONS)).setRegistryName("dragon_cane"));
		registry.register(POGO_CANE = new PogoWeaponItem(MSItemTypes.POGO_TIER, 2, -2.0F, 1.0F, 0.6, new Item.Properties().group(MSItemGroup.WEAPONS)).setRegistryName("pogo_cane"));
		//Spoons/forks
		registry.register(WOODEN_SPOON = new WeaponItem(ItemTier.WOOD, 2, -2.2F, 1.0F, new Item.Properties().group(MSItemGroup.WEAPONS)).setRegistryName("wooden_spoon"));
		registry.register(SILVER_SPOON = new WeaponItem(ItemTier.IRON, 1, -2.2F, 1.0F, new Item.Properties().group(MSItemGroup.WEAPONS)).setRegistryName("silver_spoon"));
		registry.register(CROCKER_SPOON = new DualWeaponItem(MSItemTypes.RUBY_TIER, 0, -2.2F, 1.0F, new Item.Properties().defaultMaxDamage(512).group(MSItemGroup.WEAPONS)).setRegistryName("crocker_spoon"));
		registry.register(CROCKER_FORK = new DualWeaponItem(MSItemTypes.RUBY_TIER, 2, -2.6F, 1.0F, (DualWeaponItem) CROCKER_SPOON, new Item.Properties().defaultMaxDamage(512)).setRegistryName("crocker_fork"));
		registry.register(SKAIA_FORK = new WeaponItem(MSItemTypes.REGI_TIER, 5, -2.2F, 1.0F, new Item.Properties().defaultMaxDamage(2048).group(MSItemGroup.WEAPONS)).setRegistryName("skaia_fork"));
		registry.register(FORK = new WeaponItem(ItemTier.STONE, 3, -2.2F, 1.0F, new Item.Properties().group(MSItemGroup.WEAPONS)).setRegistryName("fork"));
		registry.register(SPORK = new WeaponItem(ItemTier.STONE, 4, -2.3F, 1.0F, new Item.Properties().group(MSItemGroup.WEAPONS)).setRegistryName("spork"));
		registry.register(GOLDEN_SPORK = new WeaponItem(ItemTier.GOLD, 5, -2.3F, 1.0F, new Item.Properties().group(MSItemGroup.WEAPONS)).setRegistryName("golden_spork"));

		registry.register(EMERALD_SWORD = new SwordItem(MSItemTypes.EMERALD_TIER, 3, -2.4F, new Item.Properties().group(MSItemGroup.WEAPONS)).setRegistryName("emerald_sword"));
		registry.register(EMERALD_AXE = new ModAxeItem(MSItemTypes.EMERALD_TIER, 5, -3.0F, new Item.Properties().group(MSItemGroup.WEAPONS)).setRegistryName("emerald_axe"));
		registry.register(EMERALD_PICKAXE = new ModPickaxeItem(MSItemTypes.EMERALD_TIER, 1 , -2.8F, new Item.Properties().group(MSItemGroup.WEAPONS)).setRegistryName("emerald_pickaxe"));
		registry.register(EMERALD_SHOVEL = new ShovelItem(MSItemTypes.EMERALD_TIER, 1.5F, -3.0F, new Item.Properties().group(MSItemGroup.WEAPONS)).setRegistryName("emerald_shovel"));
		registry.register(EMERALD_HOE = new HoeItem(MSItemTypes.EMERALD_TIER, 0.0F, new Item.Properties().group(MSItemGroup.WEAPONS)).setRegistryName("emerald_hoe"));
		
		//armor
		registry.register(PRISMARINE_HELMET = new ArmorItem(MSItemTypes.PRISMARINE_ARMOR, EquipmentSlotType.HEAD, new Item.Properties().group(MSItemGroup.WEAPONS)).setRegistryName("prismarine_helmet"));
		registry.register(PRISMARINE_CHESTPLATE = new ArmorItem(MSItemTypes.PRISMARINE_ARMOR, EquipmentSlotType.CHEST, new Item.Properties().group(MSItemGroup.WEAPONS)).setRegistryName("prismarine_chestplate"));
		registry.register(PRISMARINE_LEGGINGS = new ArmorItem(MSItemTypes.PRISMARINE_ARMOR, EquipmentSlotType.LEGS, new Item.Properties().group(MSItemGroup.WEAPONS)).setRegistryName("prismarine_leggings"));
		registry.register(PRISMARINE_BOOTS = new ArmorItem(MSItemTypes.PRISMARINE_ARMOR, EquipmentSlotType.FEET, new Item.Properties().group(MSItemGroup.WEAPONS)).setRegistryName("prismarine_boots"));
		
		
		registry.register(BOONDOLLARS = new BoondollarsItem(new Item.Properties().maxStackSize(1).group(MSItemGroup.MAIN)).setRegistryName("boondollars"));
		registry.register(RAW_CRUXITE = new Item(new Item.Properties().group(MSItemGroup.MAIN)).setRegistryName("raw_cruxite"));
		registry.register(RAW_URANIUM = new Item(new Item.Properties().group(MSItemGroup.MAIN)).setRegistryName("raw_uranium"));
		registry.register(ENERGY_CORE = new Item(new Item.Properties().group(MSItemGroup.MAIN)).setRegistryName("energy_core"));
		//have to fix Cruxite artifact classes
		
		registry.register(CRUXITE_APPLE = new CruxiteAppleItem(new Item.Properties().maxStackSize(1).group(MSItemGroup.MAIN)).setRegistryName("cruxite_apple"));
		registry.register(CRUXITE_POTION = new CruxitePotionItem(new Item.Properties().maxStackSize(1).group(MSItemGroup.MAIN)).setRegistryName("cruxite_potion"));
		
		registry.register(CLIENT_DISK = new Item(new Item.Properties().maxStackSize(1).group(MSItemGroup.MAIN)).setRegistryName("client_disk"));
		registry.register(SERVER_DISK = new Item(new Item.Properties().maxStackSize(1).group(MSItemGroup.MAIN)).setRegistryName("server_disk"));
		registry.register(CAPTCHA_CARD = new CaptchaCardItem(new Item.Properties().group(MSItemGroup.MAIN)).setRegistryName("captcha_card"));
		registry.register(STACK_MODUS_CARD = new Item(new Item.Properties().maxStackSize(1).group(MSItemGroup.MAIN)).setRegistryName("stack_modus_card"));
		registry.register(QUEUE_MODUS_CARD = new Item(new Item.Properties().maxStackSize(1).group(MSItemGroup.MAIN)).setRegistryName("queue_modus_card"));
		registry.register(QUEUESTACK_MODUS_CARD = new Item(new Item.Properties().maxStackSize(1).group(MSItemGroup.MAIN)).setRegistryName("queuestack_modus_card"));
		registry.register(TREE_MODUS_CARD = new Item(new Item.Properties().maxStackSize(1).group(MSItemGroup.MAIN)).setRegistryName("tree_modus_card"));
		registry.register(HASHMAP_MODUS_CARD = new Item(new Item.Properties().maxStackSize(1).group(MSItemGroup.MAIN)).setRegistryName("hashmap_modus_card"));
		registry.register(SET_MODUS_CARD = new Item(new Item.Properties().maxStackSize(1).group(MSItemGroup.MAIN)).setRegistryName("set_modus_card"));
		registry.register(SHUNT = new ShuntItem(new Item.Properties().maxStackSize(1).group(MSItemGroup.MAIN)).setRegistryName("shunt"));
		
		//food
		registry.register(BUG_ON_A_STICK = new Item(new Item.Properties().group(MSItemGroup.MAIN).food(MSFoods.BUG_ON_A_STICK)).setRegistryName("bug_on_a_stick"));
		registry.register(CHOCOLATE_BEETLE = new Item(new Item.Properties().group(MSItemGroup.LANDS).food(MSFoods.CHOCOLATE_BEETLE)).setRegistryName("chocolate_beetle"));
		registry.register(CONE_OF_FLIES = new Item(new Item.Properties().group(MSItemGroup.LANDS).food(MSFoods.CONE_OF_FLIES)).setRegistryName("cone_of_flies"));
		registry.register(GRASSHOPPER = new Item(new Item.Properties().group(MSItemGroup.LANDS).food(MSFoods.GRASSHOPPER)).setRegistryName("grasshopper"));
		registry.register(JAR_OF_BUGS = new Item(new Item.Properties().group(MSItemGroup.LANDS).food(MSFoods.JAR_OF_BUGS)).setRegistryName("jar_of_bugs"));
		registry.register(ONION = new Item(new Item.Properties().group(MSItemGroup.LANDS).food(MSFoods.ONION)).setRegistryName("onion"));
		registry.register(SALAD = new SoupItem(new Item.Properties().group(MSItemGroup.LANDS)).setRegistryName("salad"));
		registry.register(DESERT_FRUIT = new Item(new Item.Properties().group(MSItemGroup.LANDS).food(MSFoods.DESERT_FRUIT)).setRegistryName("desert_fruit"));
		registry.register(ROCK_COOKIE = new Item(new Item.Properties().group(MSItemGroup.LANDS)).setRegistryName("rock_cookie"));	//Not actually food, but let's pretend it is
		registry.register(FUNGAL_SPORE = new Item(new Item.Properties().group(MSItemGroup.LANDS).food(MSFoods.FUNGAL_SPORE)).setRegistryName("fungal_spore"));
		registry.register(SPOREO = new Item(new Item.Properties().group(MSItemGroup.LANDS).food(MSFoods.SPOREO)).setRegistryName("sporeo"));
		registry.register(MOREL_MUSHROOM = new Item(new Item.Properties().group(MSItemGroup.LANDS).food(MSFoods.MOREL_MUSHROOM)).setRegistryName("morel_mushroom"));
		registry.register(FRENCH_FRY = new Item(new Item.Properties().group(MSItemGroup.LANDS).food(MSFoods.FRENCH_FRY)).setRegistryName("french_fry"));
		registry.register(STRAWBERRY_CHUNK = new BlockNamedItem(STRAWBERRY_STEM, new Item.Properties().group(MSItemGroup.LANDS).food(MSFoods.STRAWBERRY_CHUNK)).setRegistryName("strawberry_chunk"));
		
		registry.register(CANDY_CORN = new Item(new Item.Properties().group(MSItemGroup.MAIN).food(MSFoods.CANDY_CORN)).setRegistryName("candy_corn"));
		registry.register(BUILD_GUSHERS = new Item(new Item.Properties().group(MSItemGroup.MAIN).food(MSFoods.BUILD_GUSHERS)).setRegistryName("build_gushers"));
		registry.register(AMBER_GUMMY_WORM = new Item(new Item.Properties().group(MSItemGroup.MAIN).food(MSFoods.AMBER_GUMMY_WORM)).setRegistryName("amber_gummy_worm"));
		registry.register(CAULK_PRETZEL = new Item(new Item.Properties().group(MSItemGroup.MAIN).food(MSFoods.CAULK_PRETZEL)).setRegistryName("caulk_pretzel"));
		registry.register(CHALK_CANDY_CIGARETTE = new Item(new Item.Properties().group(MSItemGroup.MAIN).food(MSFoods.CHALK_CANDY_CIGARETTE)).setRegistryName("chalk_candy_cigarette"));
		registry.register(IODINE_LICORICE = new Item(new Item.Properties().group(MSItemGroup.MAIN).food(MSFoods.IODINE_LICORICE)).setRegistryName("iodine_licorice"));
		registry.register(SHALE_PEEP = new Item( new Item.Properties().group(MSItemGroup.MAIN).food(MSFoods.SHALE_PEEP)).setRegistryName("shale_peep"));
		registry.register(TAR_LICORICE = new Item(new Item.Properties().group(MSItemGroup.MAIN).food(MSFoods.TAR_LICORICE)).setRegistryName("tar_licorice"));
		registry.register(COBALT_GUM = new Item(new Item.Properties().group(MSItemGroup.MAIN).food(MSFoods.COBALT_GUM)).setRegistryName("cobalt_gum"));
		registry.register(MARBLE_JAWBREAKER = new Item(new Item.Properties().group(MSItemGroup.MAIN).food(MSFoods.MARBLE_JAWBREAKER)).setRegistryName("marble_jawbreaker"));
		registry.register(MERCURY_SIXLETS = new Item(new Item.Properties().group(MSItemGroup.MAIN).food(MSFoods.MERCURY_SIXLETS)).setRegistryName("mercury_sixlets"));
		registry.register(QUARTZ_JELLY_BEAN = new Item(new Item.Properties().group(MSItemGroup.MAIN).food(MSFoods.QUARTZ_JELLY_BEAN)).setRegistryName("quartz_jelly_bean"));
		registry.register(SULFUR_CANDY_APPLE = new Item(new Item.Properties().group(MSItemGroup.MAIN).food(MSFoods.SULFUR_CANDY_APPLE)).setRegistryName("sulfur_candy_apple"));
		registry.register(AMETHYST_HARD_CANDY = new Item(new Item.Properties().group(MSItemGroup.MAIN).food(MSFoods.AMETHYST_HARD_CANDY)).setRegistryName("amethyst_hard_candy"));
		registry.register(GARNET_TWIX = new Item(new Item.Properties().group(MSItemGroup.MAIN).food(MSFoods.GARNET_TWIX)).setRegistryName("garnet_twix"));
		registry.register(RUBY_LOLLIPOP = new Item(new Item.Properties().group(MSItemGroup.MAIN).food(MSFoods.RUBY_LOLLIPOP)).setRegistryName("ruby_lollipop"));
		registry.register(RUST_GUMMY_EYE = new Item(new Item.Properties().group(MSItemGroup.MAIN).food(MSFoods.RUST_GUMMY_EYE)).setRegistryName("rust_gummy_eye"));
		registry.register(DIAMOND_MINT = new Item(new Item.Properties().group(MSItemGroup.MAIN).food(MSFoods.DIAMOND_MINT)).setRegistryName("diamond_mint"));
		registry.register(GOLD_CANDY_RIBBON = new Item(new Item.Properties().group(MSItemGroup.MAIN).food(MSFoods.GOLD_CANDY_RIBBON)).setRegistryName("gold_candy_ribbon"));
		registry.register(URANIUM_GUMMY_BEAR = new Item(new Item.Properties().group(MSItemGroup.MAIN).food(MSFoods.URANIUM_GUMMY_BEAR)).setRegistryName("uranium_gummy_bear"));
		registry.register(ARTIFACT_WARHEAD = new Item(new Item.Properties().group(MSItemGroup.MAIN).food(MSFoods.ARTIFACT_WARHEAD)).setRegistryName("artifact_warhead"));
		registry.register(ZILLIUM_SKITTLES = new Item(new Item.Properties().group(MSItemGroup.MAIN).food(MSFoods.ZILLIUM_SKITTLES)).setRegistryName("zillium_skittles"));
		registry.register(TAB = new DrinkableItem(new Item.Properties().maxStackSize(16).group(MSItemGroup.MAIN).food(MSFoods.TAB)).setRegistryName("tab"));
		registry.register(FAYGO = new DrinkableItem(new Item.Properties().maxStackSize(16).group(MSItemGroup.MAIN).food(MSFoods.FAYGO)).setRegistryName("orange_faygo"));
		registry.register(FAYGO_CANDY_APPLE = new DrinkableItem(new Item.Properties().maxStackSize(16).group(MSItemGroup.MAIN).food(MSFoods.FAYGO_CANDY_APPLE)).setRegistryName("candy_apple_faygo"));
		registry.register(FAYGO_COLA = new DrinkableItem(new Item.Properties().maxStackSize(16).group(MSItemGroup.MAIN).food(MSFoods.FAYGO_COLA)).setRegistryName("faygo_cola"));
		registry.register(FAYGO_COTTON_CANDY = new DrinkableItem(new Item.Properties().maxStackSize(16).group(MSItemGroup.MAIN).food(MSFoods.FAYGO_COTTON_CANDY)).setRegistryName("cotton_candy_faygo"));
		registry.register(FAYGO_CREME = new DrinkableItem(new Item.Properties().maxStackSize(16).group(MSItemGroup.MAIN).food(MSFoods.FAYGO_CREME)).setRegistryName("creme_soda_faygo"));
		registry.register(FAYGO_GRAPE = new DrinkableItem(new Item.Properties().maxStackSize(16).group(MSItemGroup.MAIN).food(MSFoods.FAYGO_GRAPE)).setRegistryName("grape_faygo"));
		registry.register(FAYGO_MOON_MIST = new DrinkableItem(new Item.Properties().maxStackSize(16).group(MSItemGroup.MAIN).food(MSFoods.FAYGO_MOON_MIST)).setRegistryName("moon_mist_faygo"));
		registry.register(FAYGO_PEACH = new DrinkableItem(new Item.Properties().maxStackSize(16).group(MSItemGroup.MAIN).food(MSFoods.FAYGO_PEACH)).setRegistryName("peach_faygo"));
		registry.register(FAYGO_REDPOP = new DrinkableItem(new Item.Properties().maxStackSize(16).group(MSItemGroup.MAIN).food(MSFoods.FAYGO_REDPOP)).setRegistryName("redpop_faygo"));
		registry.register(IRRADIATED_STEAK = new Item(new Item.Properties().group(MSItemGroup.MAIN).food(MSFoods.IRRADIATED_STEAK)).setRegistryName("irradiated_steak"));
		registry.register(SURPRISE_EMBRYO = new SurpriseEmbryoItem(new Item.Properties().group(MSItemGroup.MAIN).food(MSFoods.SURPRISE_EMBRYO)).setRegistryName("surprise_embryo"));
		registry.register(UNKNOWABLE_EGG = new UnknowableEggItem(new Item.Properties().maxStackSize(16).group(MSItemGroup.MAIN).food(MSFoods.UNKNOWABLE_EGG)).setRegistryName("unknowable_egg"));
		
		registry.register(GOLDEN_GRASSHOPPER = new Item(new Item.Properties().group(MSItemGroup.LANDS)).setRegistryName("golden_grasshopper"));
		registry.register(BUG_NET = new BugNetItem(new Item.Properties().defaultMaxDamage(64).group(MSItemGroup.LANDS)).setRegistryName("bug_net"));
		registry.register(FROG = new FrogItem(new Item.Properties().maxStackSize(1).group(MSItemGroup.LANDS)).setRegistryName("frog"));
		registry.register(CARVING_TOOL = new Item(new Item.Properties().maxStackSize(1).group(MSItemGroup.LANDS)).setRegistryName("carving_tool"));
		registry.register(CRUMPLY_HAT = new Item(new Item.Properties().maxStackSize(1).group(MSItemGroup.LANDS)).setRegistryName("crumply_hat"));
		registry.register(STONE_EYEBALLS = new Item(new Item.Properties().group(MSItemGroup.LANDS)).setRegistryName("stone_eyeballs"));
		registry.register(STONE_SLAB = new Item(new Item.Properties().group(MSItemGroup.LANDS)).setRegistryName("stone_slab"));
		//registry.register(SHOP_POSTER = new HangingItem((world, pos, facing, stack) -> new EntityShopPoster(world, pos, facing, stack, 0), new Item.Properties().maxStackSize(1).group(ModItemGroup.LANDS)).setRegistryName("shop_poster"));
		
		registry.register(OIL_BUCKET = new BucketItem(MSFluids.OIL, new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1).group(MSItemGroup.MAIN)).setRegistryName("oil_bucket"));
		registry.register(BLOOD_BUCKET = new BucketItem(MSFluids.BLOOD, new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1).group(MSItemGroup.MAIN)).setRegistryName("blood_bucket"));
		registry.register(BRAIN_JUICE_BUCKET = new BucketItem(MSFluids.BRAIN_JUICE, new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1).group(MSItemGroup.MAIN)).setRegistryName("brain_juice_bucket"));
		registry.register(WATER_COLORS_BUCKET = new BucketItem(MSFluids.WATER_COLORS, new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1).group(MSItemGroup.MAIN)).setRegistryName("water_colors_bucket"));
		registry.register(ENDER_BUCKET = new BucketItem(MSFluids.ENDER, new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1).group(MSItemGroup.MAIN)).setRegistryName("ender_bucket"));
		registry.register(LIGHT_WATER_BUCKET = new BucketItem(MSFluids.LIGHT_WATER, new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1).group(MSItemGroup.MAIN)).setRegistryName("light_water_bucket"));
		registry.register(OBSIDIAN_BUCKET = new ObsidianBucketItem(new Item.Properties().maxStackSize(1).containerItem(Items.BUCKET).group(MSItemGroup.MAIN)).setRegistryName("obsidian_bucket"));
		registry.register(CAPTCHAROID_CAMERA = new CaptcharoidCameraItem(new Item.Properties().defaultMaxDamage(64).group(MSItemGroup.MAIN)).setRegistryName("captcharoid_camera"));
		registry.register(GRIMOIRE = new GrimoireItem(new Item.Properties().maxStackSize(1).group(MSItemGroup.MAIN)).setRegistryName("grimoire"));
		registry.register(LONG_FORGOTTEN_WARHORN = new LongForgottenWarhornItem(new Item.Properties().defaultMaxDamage(100)).setRegistryName("long_forgotten_warhorn"));
		registry.register(RAZOR_BLADE = new RazorBladeItem(new Item.Properties().group(MSItemGroup.MAIN)).setRegistryName("razor_blade"));
		registry.register(UP_STICK = new Item(new Item.Properties().group(MSItemGroup.MAIN).maxStackSize(1)).setRegistryName("uranium_powered_stick"));
		registry.register(IRON_BOAT = new CustomBoatItem((stack, world, x, y, z) -> new MetalBoatEntity(world, x, y, z, 0), new Item.Properties().group(MSItemGroup.MAIN).maxStackSize(1)).setRegistryName("iron_boat"));
		registry.register(GOLD_BOAT = new CustomBoatItem((stack, world, x, y, z) -> new MetalBoatEntity(world, x, y, z, 1), new Item.Properties().group(MSItemGroup.MAIN).maxStackSize(1)).setRegistryName("gold_boat"));
		registry.register(THRESH_DVD = new Item(new Item.Properties().maxStackSize(1).group(MSItemGroup.MAIN)).setRegistryName("thresh_dvd"));
		registry.register(GAMEBRO_MAGAZINE = new Item(new Item.Properties().maxStackSize(1).group(MSItemGroup.MAIN)).setRegistryName("gamebro_magazine"));
		registry.register(GAMEGRL_MAGAZINE = new Item(new Item.Properties().maxStackSize(1).group(MSItemGroup.MAIN)).setRegistryName("gamegrl_magazine"));
		registry.register(CREW_POSTER = new HangingItem((world, pos, facing, stack) -> new CrewPosterEntity(world, pos, facing), new Item.Properties().maxStackSize(1).group(MSItemGroup.MAIN)).setRegistryName("crew_poster"));
		registry.register(SBAHJ_POSTER = new HangingItem((world, pos, facing, stack) -> new SbahjPosterEntity(world, pos, facing), new Item.Properties().maxStackSize(1).group(MSItemGroup.MAIN)).setRegistryName("sbahj_poster"));
		//registry.register(FAKE_ARMS = new Item(new Item.Properties().maxStackSize(1)).setRegistryName("fake_arms"));
		
		//Music disks
		registry.register(MUSIC_DISC_EMISSARY_OF_DANCE = new ModMusicDiscItem(13, MSSoundEvents.MUSIC_DISC_EMISSARY_OF_DANCE, new Item.Properties().rarity(Rarity.RARE).maxStackSize(1).group(MSItemGroup.MAIN)).setRegistryName("music_disc_emissary_of_dance"));
		registry.register(MUSIC_DISC_DANCE_STAB_DANCE = new ModMusicDiscItem(13, MSSoundEvents.MUSIC_DISC_DANCE_STAB_DANCE, new Item.Properties().rarity(Rarity.RARE).maxStackSize(1).group(MSItemGroup.MAIN)).setRegistryName("music_disc_dance_stab_dance"));
		registry.register(MUSIC_DISC_RETRO_BATTLE = new ModMusicDiscItem(13, MSSoundEvents.MUSIC_DISC_RETRO_BATTLE_THEME, new Item.Properties().rarity(Rarity.RARE).maxStackSize(1).group(MSItemGroup.MAIN)).setRegistryName("music_disc_retro_battle"));
		
		WeaponItem.addToolMaterial(ToolType.PICKAXE, Arrays.asList(Material.IRON, Material.ANVIL, Material.ROCK));
		WeaponItem.addToolMaterial(ToolType.AXE, Arrays.asList(Material.WOOD, Material.PLANTS, Material.TALL_PLANTS));
		WeaponItem.addToolMaterial(ToolType.SHOVEL, Arrays.asList(Material.SNOW, Material.SNOW_BLOCK, Material.CLAY, Material.ORGANIC, Material.EARTH, Material.SAND));
		//WeaponItem.addToolMaterial("sword", Arrays.asList(Material.WEB));
		WeaponItem.addToolMaterial(MSItemTypes.SICKLE_TOOL, Arrays.asList(Material.WEB, Material.LEAVES, Material.PLANTS, Material.TALL_PLANTS));
	}
	
	private static Item registerItemBlock(IForgeRegistry<Item> registry, Block block)
	{
		return registerItemBlock(registry, new BlockItem(block, new Item.Properties()));
	}
	
	private static Item registerItemBlock(IForgeRegistry<Item> registry, Block block, ItemGroup group)
	{
		return registerItemBlock(registry, new BlockItem(block, new Item.Properties().group(group)));
	}
	
	private static Item registerItemBlock(IForgeRegistry<Item> registry, BlockItem item)
	{
		if(item.getBlock().getRegistryName() == null)
			throw new IllegalArgumentException(String.format("The provided itemblock %s has a block without a registry name!", item.getBlock()));
		registry.register(item.setRegistryName(item.getBlock().getRegistryName()));
		return item;
	}
}