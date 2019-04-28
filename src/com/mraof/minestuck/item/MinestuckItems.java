package com.mraof.minestuck.item;

import com.mraof.minestuck.entity.item.EntityCrewPoster;
import com.mraof.minestuck.entity.item.EntitySbahjPoster;
import com.mraof.minestuck.entity.item.EntityShopPoster;
import com.mraof.minestuck.item.block.*;
import com.mraof.minestuck.item.weapon.*;
import com.mraof.minestuck.util.MinestuckSoundHandler;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityHanging;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

import static com.mraof.minestuck.block.MinestuckBlocks.*;

import java.util.Arrays;

/**
 * This class contains all non-ItemBlock items that minestuck adds,
 * and is responsible for initializing and registering these.
 */
public class MinestuckItems
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
	//Food
	public static Item candy = new ItemMinestuckCandy();
	public static Item beverage = new ItemMinestuckBeverage();
	public static Item bugOnAStick = new ItemFood(1, 0.1F, false).setUnlocalizedName("bugOnAStick").setCreativeTab(TabMinestuck.instance);
	public static Item chocolateBeetle = new ItemFood(3, 0.4F, false).setUnlocalizedName("chocolateBeetle").setCreativeTab(TabMinestuck.instance);
	public static Item coneOfFlies = new ItemFood(2, 0.1F, false).setUnlocalizedName("coneOfFlies").setCreativeTab(TabMinestuck.instance);
	public static Item grasshopper = new ItemFood(4, 0.5F, false).setUnlocalizedName("grasshopper").setCreativeTab(TabMinestuck.instance);
	public static Item jarOfBugs = new ItemFood(3, 0.2F, false).setUnlocalizedName("jarOfBugs").setCreativeTab(TabMinestuck.instance);
	public static Item onion = new ItemFood(2, 0.2F, false).setUnlocalizedName("onion").setCreativeTab(TabMinestuck.instance);
	public static Item salad = new ItemSoup(1).setUnlocalizedName("salad").setCreativeTab(TabMinestuck.instance);
	public static Item desertFruit = new ItemFood(1, 0.1F, false).setUnlocalizedName("desertFruit").setCreativeTab(TabMinestuck.instance);
	public static Item irradiatedSteak = new ItemFood(4, 0.4F, true).setPotionEffect(new PotionEffect(MobEffects.WITHER, 100, 1), 0.9F).setUnlocalizedName("irradiatedSteak").setCreativeTab(TabMinestuck.instance);
	public static Item rockCookie = new Item().setUnlocalizedName("rockCookie").setCreativeTab(TabMinestuck.instance);
	public static Item fungalSpore = new ItemFood(1, 0.2F, false).setPotionEffect(new PotionEffect(MobEffects.POISON, 60, 3), 0.7F).setUnlocalizedName("fungalSpore").setCreativeTab(TabMinestuck.instance);
	public static Item sporeo = new ItemFood(3, 0.4F, false).setUnlocalizedName("sporeo").setCreativeTab(TabMinestuck.instance);
	public static Item morelMushroom = new ItemFood(3, 0.9F, false).setUnlocalizedName("morelMushroom").setCreativeTab(TabMinestuck.instance);
	public static Item frenchFry = new ItemFood(1, 0.1F, false).setUnlocalizedName("frenchFry").setCreativeTab(TabMinestuck.instance);
	public static Item strawberryChunk = new ItemMinestuckSeedFood(4, 0.5F).setUnlocalizedName("strawberryChunk").setCreativeTab(TabMinestuck.instance);
	public static Item surpriseEmbryo = new ItemSurpriseEmbryo(3, 0.2F, false);
	public static Item unknowableEgg = new ItemUnknowableEgg(3, 0.3F, false).setUnlocalizedName("unknowableEgg");
	//Other
	public static Item UP_STICK;
	public static Item goldenGrasshopper = new Item().setUnlocalizedName("goldenGrasshopper").setCreativeTab(TabMinestuck.instance);
	public static Item bugNet = new ItemNet().setUnlocalizedName("net");
	public static Item itemFrog = new ItemFrog().setUnlocalizedName("frog");
	public static Item boondollars = new ItemBoondollars();
	public static Item rawCruxite = new Item().setUnlocalizedName("rawCruxite").setCreativeTab(TabMinestuck.instance);
	public static Item rawUranium = new Item().setUnlocalizedName("rawUranium").setCreativeTab(TabMinestuck.instance);
	public static Item energyCore = new Item().setUnlocalizedName("energyCore").setCreativeTab(TabMinestuck.instance);
	//public static Item chessboard = new Item().setUnlocalizedName("chessboard").setCreativeTab(TabMinestuck.instance);
	public static Item captchaCard = new ItemCaptchaCard();
	public static Item cruxiteApple = new ItemCruxiteApple();
	public static Item cruxitePotion = new ItemCruxitePotion();
	public static Item disk = new ItemDisk();
	public static Item grimoire = new ItemGrimoire().setUnlocalizedName("grimoire");
	public static Item longForgottenWarhorn = new ItemLongForgottenWarhorn().setUnlocalizedName("longForgottenWarhorn");
	//public static Item chessboard = new Item().setUnlocalizedName("chessboard").setMaxStackSize(1).setCreativeTab(TabMinestuck.instance);
	public static Item minestuckBucket = new ItemMinestuckBucket();
	public static Item obsidianBucket = new ItemObsidianBucket();
	public static Item modusCard = new ItemModus();
	public static Item goldSeeds = new ItemGoldSeeds();
	public static Item razorBlade = new ItemRazorBlade();
	public static Item metalBoat = new ItemMetalBoat();
	public static Item shunt = new ItemShunt();
	public static Item captcharoidCamera = new ItemCaptcharoidCamera();
	public static Item threshDvd = new Item().setUnlocalizedName("threshDvd").setMaxStackSize(1).setCreativeTab(TabMinestuck.instance);
	public static Item gamebroMagazine = new Item().setUnlocalizedName("gamebroMagazine").setMaxStackSize(1).setCreativeTab(TabMinestuck.instance);
	public static Item gamegrlMagazine = new Item().setUnlocalizedName("gamegrlMagazine").setMaxStackSize(1).setCreativeTab(TabMinestuck.instance);
	public static Item crewPoster = new ItemHanging()
	{
		@Override
		public EntityHanging createEntity(World worldIn, BlockPos pos, EnumFacing facing, ItemStack stack, int meta)
		{
			return new EntityCrewPoster(worldIn, pos, facing);
		}
	}.setUnlocalizedName("crewPoster").setMaxStackSize(1).setCreativeTab(TabMinestuck.instance);
	public static Item sbahjPoster = new ItemHanging()
	{
		@Override
		public EntityHanging createEntity(World worldIn, BlockPos pos, EnumFacing facing, ItemStack stack, int meta)
		{
			return new EntitySbahjPoster(worldIn, pos, facing);
		}
	}.setUnlocalizedName("sbahjPoster").setMaxStackSize(1).setCreativeTab(TabMinestuck.instance);
	
	public static Item shopPoster = new ItemShopPoster()
	{
		@Override
		public EntityHanging createEntity(World worldIn, BlockPos pos, EnumFacing facing, ItemStack stack, int meta)
		{
			return new EntityShopPoster(worldIn, pos, facing, stack, meta);
		}
	}.setUnlocalizedName("shopPoster").setMaxStackSize(1).setCreativeTab(TabMinestuck.instance);

	public static Item carvingTool = new Item().setUnlocalizedName("carvingTool").setMaxStackSize(1).setCreativeTab(TabMinestuck.instance);
	public static Item crumplyHat = new Item().setUnlocalizedName("crumplyHat").setMaxStackSize(1).setCreativeTab(TabMinestuck.instance);
    public static Item stoneEyeballs = new Item().setUnlocalizedName("stoneEyeballs").setCreativeTab(TabMinestuck.instance);
	public static Item stoneSlab = new Item().setUnlocalizedName("stoneSlab").setCreativeTab(TabMinestuck.instance);
	public static Item glowystoneDust = new ItemGlowystoneDust().setUnlocalizedName("glowystoneDust").setCreativeTab(TabMinestuck.instance);
	public static Item fakeArms = new Item().setUnlocalizedName("fakeArms").setCreativeTab(null);
	//Music disks
	public static Item recordEmissaryOfDance = new ItemMinestuckRecord("emissary", MinestuckSoundHandler.soundEmissaryOfDance).setUnlocalizedName("record");
	public static Item recordDanceStab = new ItemMinestuckRecord("danceStab", MinestuckSoundHandler.soundDanceStabDance).setUnlocalizedName("record");
	public static Item recordRetroBattle = new ItemMinestuckRecord("retroBattle",MinestuckSoundHandler.soundRetroBattleTheme).setUnlocalizedName("record");
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		IForgeRegistry<Item> registry = event.getRegistry();
		registerItemBlock(registry, BLACK_CHESS_DIRT, ModItemGroup.MAIN);
		registerItemBlock(registry, WHITE_CHESS_DIRT, ModItemGroup.MAIN);
		registerItemBlock(registry, DARK_GRAY_CHESS_DIRT, ModItemGroup.MAIN);
		registerItemBlock(registry, LIGHT_GRAY_CHESS_DIRT, ModItemGroup.MAIN);
		registerItemBlock(registry, SKAIA_PORTAL, ModItemGroup.MAIN);
		
		registerItemBlock(registry, CRUXITE_ORE_STONE, ModItemGroup.LANDS);
		registerItemBlock(registry, CRUXITE_ORE_NETHERRACK, ModItemGroup.LANDS);
		registerItemBlock(registry, CRUXITE_ORE_COBBLESTONE, ModItemGroup.LANDS);
		registerItemBlock(registry, CRUXITE_ORE_SANDSTONE, ModItemGroup.LANDS);
		registerItemBlock(registry, CRUXITE_ORE_RED_SANDSTONE, ModItemGroup.LANDS);
		registerItemBlock(registry, CRUXITE_ORE_END_STONE, ModItemGroup.LANDS);
		registerItemBlock(registry, CRUXITE_ORE_PINK_STONE, ModItemGroup.LANDS);
		registerItemBlock(registry, URANIUM_ORE_STONE, ModItemGroup.LANDS);
		registerItemBlock(registry, URANIUM_ORE_NETHERRACK, ModItemGroup.LANDS);
		registerItemBlock(registry, URANIUM_ORE_COBBLESTONE, ModItemGroup.LANDS);
		registerItemBlock(registry, URANIUM_ORE_SANDSTONE, ModItemGroup.LANDS);
		registerItemBlock(registry, URANIUM_ORE_RED_SANDSTONE, ModItemGroup.LANDS);
		registerItemBlock(registry, URANIUM_ORE_END_STONE, ModItemGroup.LANDS);
		registerItemBlock(registry, COAL_ORE_NETHERRACK, ModItemGroup.LANDS);
		registerItemBlock(registry, COAL_ORE_PINK_STONE, ModItemGroup.LANDS);
		registerItemBlock(registry, IRON_ORE_END_STONE, ModItemGroup.LANDS);
		registerItemBlock(registry, IRON_ORE_SANDSTONE, ModItemGroup.LANDS);
		registerItemBlock(registry, IRON_ORE_SANDSTONE_RED, ModItemGroup.LANDS);
		registerItemBlock(registry, GOLD_ORE_SANDSTONE, ModItemGroup.LANDS);
		registerItemBlock(registry, GOLD_ORE_SANDSTONE_RED, ModItemGroup.LANDS);
		registerItemBlock(registry, GOLD_ORE_PINK_STONE, ModItemGroup.LANDS);
		registerItemBlock(registry, REDSTONE_ORE_END_STONE, ModItemGroup.LANDS);
		registerItemBlock(registry, QUARTZ_ORE_STONE, ModItemGroup.LANDS);
		registerItemBlock(registry, LAPIS_ORE_PINK_STONE, ModItemGroup.LANDS);
		registerItemBlock(registry, DIAMOND_ORE_PINK_STONE, ModItemGroup.LANDS);
		
		registerItemBlock(registry, CRUXITE_BLOCK, ModItemGroup.MAIN);
		registerItemBlock(registry, URANIUM_BLOCK, ModItemGroup.MAIN);
		registerItemBlock(registry, GENERIC_OBJECT, ModItemGroup.MAIN);
		
		registerItemBlock(registry, BLUE_DIRT, ModItemGroup.LANDS);
		registerItemBlock(registry, THOUGHT_DIRT, ModItemGroup.LANDS);
		registerItemBlock(registry, COARSE_STONE, ModItemGroup.LANDS);
		registerItemBlock(registry, COARSE_CHISELED, ModItemGroup.LANDS);
		registerItemBlock(registry, SHADE_BRICKS, ModItemGroup.LANDS);
		registerItemBlock(registry, SHADE_SMOOTH, ModItemGroup.LANDS);
		registerItemBlock(registry, FROST_BRICKS, ModItemGroup.LANDS);
		registerItemBlock(registry, FROST_TILE, ModItemGroup.LANDS);
		registerItemBlock(registry, FROST_BRICKS_CHISELED, ModItemGroup.LANDS);
		registerItemBlock(registry, CAST_IRON, ModItemGroup.LANDS);
		registerItemBlock(registry, CAST_IRON_CHISELED, ModItemGroup.LANDS);
		registerItemBlock(registry, MYCELIUM_BRICKS, ModItemGroup.LANDS);
		registerItemBlock(registry, BLACK_STONE, ModItemGroup.LANDS);
		registerItemBlock(registry, FLOWERY_MOSS_STONE, ModItemGroup.LANDS);
		registerItemBlock(registry, FLOWERY_MOSS_BRICK, ModItemGroup.LANDS);
		registerItemBlock(registry, COARSE_END_STONE, ModItemGroup.LANDS);
		registerItemBlock(registry, END_GRASS, ModItemGroup.LANDS);
		registerItemBlock(registry, CHALK, ModItemGroup.LANDS);
		registerItemBlock(registry, CHALK_BRICKS, ModItemGroup.LANDS);
		registerItemBlock(registry, CHALK_CHISELED, ModItemGroup.LANDS);
		registerItemBlock(registry, CHALK_POLISHED, ModItemGroup.LANDS);
		registerItemBlock(registry, PINK_STONE, ModItemGroup.LANDS);
		registerItemBlock(registry, PINK_STONE_BRICKS, ModItemGroup.LANDS);
		registerItemBlock(registry, PINK_STONE_CHISELED, ModItemGroup.LANDS);
		registerItemBlock(registry, PINK_STONE_CRACKED, ModItemGroup.LANDS);
		registerItemBlock(registry, PINK_STONE_MOSSY, ModItemGroup.LANDS);
		registerItemBlock(registry, PINK_STONE_POLISHED, ModItemGroup.LANDS);
		registerItemBlock(registry, DENSE_CLOUD, ModItemGroup.LANDS);
		registerItemBlock(registry, DENSE_CLOUD_BRIGHT, ModItemGroup.LANDS);
		registerItemBlock(registry, SUGAR_CUBE, ModItemGroup.LANDS);
		
		registerItemBlock(registry, GLOWING_LOG, ModItemGroup.LANDS);
		registerItemBlock(registry, FROST_LOG, ModItemGroup.LANDS);
		registerItemBlock(registry, RAINBOW_LOG, ModItemGroup.LANDS);
		registerItemBlock(registry, END_LOG, ModItemGroup.LANDS);
		registerItemBlock(registry, VINE_LOG, ModItemGroup.LANDS);
		registerItemBlock(registry, FLOWERY_VINE_LOG, ModItemGroup.LANDS);
		registerItemBlock(registry, DEAD_LOG, ModItemGroup.LANDS);
		registerItemBlock(registry, PETRIFIED_LOG, ModItemGroup.LANDS);
		registerItemBlock(registry, GLOWING_PLANKS, ModItemGroup.LANDS);
		registerItemBlock(registry, FROST_PLANKS, ModItemGroup.LANDS);
		registerItemBlock(registry, RAINBOW_PLANKS, ModItemGroup.LANDS);
		registerItemBlock(registry, END_PLANKS, ModItemGroup.LANDS);
		registerItemBlock(registry, DEAD_PLANKS, ModItemGroup.LANDS);
		registerItemBlock(registry, TREATED_PLANKS, ModItemGroup.LANDS);
		registerItemBlock(registry, FROST_LEAVES, ModItemGroup.LANDS);
		registerItemBlock(registry, RAINBOW_LEAVES, ModItemGroup.LANDS);
		registerItemBlock(registry, END_LEAVES, ModItemGroup.LANDS);
		registerItemBlock(registry, RAINBOW_SAPLING, ModItemGroup.LANDS);
		registerItemBlock(registry, END_SAPLING, ModItemGroup.LANDS);
		
		registerItemBlock(registry, BLOOD_ASPECT_LOG, ModItemGroup.LANDS);
		registerItemBlock(registry, BREATH_ASPECT_LOG, ModItemGroup.LANDS);
		registerItemBlock(registry, DOOM_ASPECT_LOG, ModItemGroup.LANDS);
		registerItemBlock(registry, HEART_ASPECT_LOG, ModItemGroup.LANDS);
		registerItemBlock(registry, HOPE_ASPECT_LOG, ModItemGroup.LANDS);
		registerItemBlock(registry, LIFE_ASPECT_LOG, ModItemGroup.LANDS);
		registerItemBlock(registry, LIGHT_ASPECT_LOG, ModItemGroup.LANDS);
		registerItemBlock(registry, MIND_ASPECT_LOG, ModItemGroup.LANDS);
		registerItemBlock(registry, RAGE_ASPECT_LOG, ModItemGroup.LANDS);
		registerItemBlock(registry, SPACE_ASPECT_LOG, ModItemGroup.LANDS);
		registerItemBlock(registry, TIME_ASPECT_LOG, ModItemGroup.LANDS);
		registerItemBlock(registry, VOID_ASPECT_LOG, ModItemGroup.LANDS);
		registerItemBlock(registry, BLOOD_ASPECT_PLANKS, ModItemGroup.LANDS);
		registerItemBlock(registry, BREATH_ASPECT_PLANKS, ModItemGroup.LANDS);
		registerItemBlock(registry, DOOM_ASPECT_PLANKS, ModItemGroup.LANDS);
		registerItemBlock(registry, HEART_ASPECT_PLANKS, ModItemGroup.LANDS);
		registerItemBlock(registry, HOPE_ASPECT_PLANKS, ModItemGroup.LANDS);
		registerItemBlock(registry, LIFE_ASPECT_PLANKS, ModItemGroup.LANDS);
		registerItemBlock(registry, LIGHT_ASPECT_PLANKS, ModItemGroup.LANDS);
		registerItemBlock(registry, MIND_ASPECT_PLANKS, ModItemGroup.LANDS);
		registerItemBlock(registry, RAGE_ASPECT_PLANKS, ModItemGroup.LANDS);
		registerItemBlock(registry, SPACE_ASPECT_PLANKS, ModItemGroup.LANDS);
		registerItemBlock(registry, TIME_ASPECT_PLANKS, ModItemGroup.LANDS);
		registerItemBlock(registry, VOID_ASPECT_PLANKS, ModItemGroup.LANDS);
		registerItemBlock(registry, BLOOD_ASPECT_LEAVES, ModItemGroup.LANDS);
		registerItemBlock(registry, BREATH_ASPECT_LEAVES, ModItemGroup.LANDS);
		registerItemBlock(registry, DOOM_ASPECT_LEAVES, ModItemGroup.LANDS);
		registerItemBlock(registry, HEART_ASPECT_LEAVES, ModItemGroup.LANDS);
		registerItemBlock(registry, HOPE_ASPECT_LEAVES, ModItemGroup.LANDS);
		registerItemBlock(registry, LIFE_ASPECT_LEAVES, ModItemGroup.LANDS);
		registerItemBlock(registry, LIGHT_ASPECT_LEAVES, ModItemGroup.LANDS);
		registerItemBlock(registry, MIND_ASPECT_LEAVES, ModItemGroup.LANDS);
		registerItemBlock(registry, RAGE_ASPECT_LEAVES, ModItemGroup.LANDS);
		registerItemBlock(registry, SPACE_ASPECT_LEAVES, ModItemGroup.LANDS);
		registerItemBlock(registry, TIME_ASPECT_LEAVES, ModItemGroup.LANDS);
		registerItemBlock(registry, VOID_ASPECT_LEAVES, ModItemGroup.LANDS);
		registerItemBlock(registry, BLOOD_ASPECT_SAPLING, ModItemGroup.LANDS);
		registerItemBlock(registry, BREATH_ASPECT_SAPLING, ModItemGroup.LANDS);
		registerItemBlock(registry, DOOM_ASPECT_SAPLING, ModItemGroup.LANDS);
		registerItemBlock(registry, HEART_ASPECT_SAPLING, ModItemGroup.LANDS);
		registerItemBlock(registry, HOPE_ASPECT_SAPLING, ModItemGroup.LANDS);
		registerItemBlock(registry, LIFE_ASPECT_SAPLING, ModItemGroup.LANDS);
		registerItemBlock(registry, LIGHT_ASPECT_SAPLING, ModItemGroup.LANDS);
		registerItemBlock(registry, MIND_ASPECT_SAPLING, ModItemGroup.LANDS);
		registerItemBlock(registry, RAGE_ASPECT_SAPLING, ModItemGroup.LANDS);
		registerItemBlock(registry, SPACE_ASPECT_SAPLING, ModItemGroup.LANDS);
		registerItemBlock(registry, TIME_ASPECT_SAPLING, ModItemGroup.LANDS);
		registerItemBlock(registry, VOID_ASPECT_SAPLING, ModItemGroup.LANDS);
		
		registerItemBlock(registry, GLOWING_MUSHROOM, ModItemGroup.LANDS);
		registerItemBlock(registry, DESERT_BUSH, ModItemGroup.LANDS);
		registerItemBlock(registry, BLOOMING_CACTUS, ModItemGroup.LANDS);
		registerItemBlock(registry, PETRIFIED_GRASS, ModItemGroup.LANDS);
		registerItemBlock(registry, PETRIFIED_POPPY, ModItemGroup.LANDS);
		registerItemBlock(registry, STRAWBERRY, ModItemGroup.LANDS);
		
		registerItemBlock(registry, LAYERED_SAND, ModItemGroup.LANDS);
		registerItemBlock(registry, LAYERED_RED_SAND, ModItemGroup.LANDS);
		registerItemBlock(registry, GLOWY_GOOP, ModItemGroup.LANDS);
		registerItemBlock(registry, COAGULATED_BLOOD, ModItemGroup.LANDS);
		registerItemBlock(registry, VEIN, ModItemGroup.LANDS);
		registerItemBlock(registry, VEIN_CORNER, ModItemGroup.LANDS);
		registerItemBlock(registry, VEIN_CORNER_INVERTED, ModItemGroup.LANDS);
		
		registerItemBlock(registry, COARSE_STONE_STAIRS, ModItemGroup.LANDS);
		registerItemBlock(registry, SHADE_BRICK_STAIRS, ModItemGroup.LANDS);
		registerItemBlock(registry, FROST_BRICK_STAIRS, ModItemGroup.LANDS);
		registerItemBlock(registry, CAST_IRON_STAIRS, ModItemGroup.LANDS);
		registerItemBlock(registry, MYCELIUM_BRICK_STAIRS, ModItemGroup.LANDS);
		registerItemBlock(registry, CHALK_STAIRS, ModItemGroup.LANDS);
		registerItemBlock(registry, CHALK_BRICK_STAIRS, ModItemGroup.LANDS);
		registerItemBlock(registry, PINK_STONE_BRICK_STAIRS, ModItemGroup.LANDS);
		registerItemBlock(registry, RAINBOW_PLANKS_STAIRS, ModItemGroup.LANDS);
		registerItemBlock(registry, END_PLANKS_STAIRS, ModItemGroup.LANDS);
		registerItemBlock(registry, DEAD_PLANKS_STAIRS, ModItemGroup.LANDS);
		registerItemBlock(registry, TREATED_PLANKS_STAIRS, ModItemGroup.LANDS);
		registerItemBlock(registry, CHALK_SLAB, ModItemGroup.LANDS);
		registerItemBlock(registry, CHALK_BRICK_SLAB, ModItemGroup.LANDS);
		registerItemBlock(registry, PINK_STONE_BRICK_SLAB, ModItemGroup.LANDS);
		registerItemBlock(registry, RAINBOW_PLANKS_SLAB, ModItemGroup.LANDS);
		registerItemBlock(registry, END_PLANKS_SLAB, ModItemGroup.LANDS);
		registerItemBlock(registry, DEAD_PLANKS_SLAB, ModItemGroup.LANDS);
		registerItemBlock(registry, TREATED_PLANKS_SLAB, ModItemGroup.LANDS);
		
		registerItemBlock(registry, new ItemCruxtruder(CRUXTRUDER_TUBE, new Item.Properties().group(ModItemGroup.MAIN)));
		registerItemBlock(registry, CRUXTRUDER_LID, ModItemGroup.MAIN);
		registerItemBlock(registry, new ItemTotemLathe(TOTEM_LATHE_CARD_SLOT, new Item.Properties().group(ModItemGroup.MAIN)));
		registerItemBlock(registry, new ItemAlchemiter(ALCHEMITER_TOTEM_PAD, new Item.Properties().group(ModItemGroup.MAIN)));
		registerItemBlock(registry, new ItemPunchDesignix(PUNCH_DESIGNIX_SLOT, new Item.Properties().group(ModItemGroup.MAIN)));
		registerItemBlock(registry, new ItemMiniCruxtruder(MINI_CRUXTRUDER, new Item.Properties().group(ModItemGroup.MAIN)));
		registerItemBlock(registry, MINI_TOTEM_LATHE, ModItemGroup.MAIN);
		registerItemBlock(registry, MINI_ALCHEMITER, ModItemGroup.MAIN);
		registerItemBlock(registry, MINI_PUNCH_DESIGNIX, ModItemGroup.MAIN);
		/*registerItemBlock(registry, new ItemBlock(holopad));
		registerItemBlock(registry, new ItemJumperBlock(jumperBlockExtension[0]));*/
		
		registerItemBlock(registry, COMPUTER_OFF, ModItemGroup.MAIN);
		registerItemBlock(registry, LAPTOP_OFF, ModItemGroup.MAIN);
		registerItemBlock(registry, CROCKERTOP_OFF, ModItemGroup.MAIN);
		registerItemBlock(registry, HUBTOP_OFF, ModItemGroup.MAIN);
		registerItemBlock(registry, LUNCHTOP_OFF, ModItemGroup.MAIN);
		registerItemBlock(registry, new ItemTransportalizer(TRANSPORTALIZER, new Item.Properties().group(ModItemGroup.MAIN)));
		registerItemBlock(registry, GRIST_WIDGET, ModItemGroup.MAIN);
		registerItemBlock(registry, URANIUM_COOKER, ModItemGroup.MAIN);
		
		registerItemBlock(registry, new ItemDowel(CRUXITE_DOWEL, new Item.Properties().group(ModItemGroup.MAIN)));
		
		registerItemBlock(registry, GOLD_SEEDS, ModItemGroup.MAIN);	//TODO Gold seeds item
		registerItemBlock(registry, WOODEN_CACTUS, ModItemGroup.MAIN);
		
		registerItemBlock(registry, new ItemBlock(APPLE_CAKE, new Item.Properties().group(ModItemGroup.MAIN).maxStackSize(1)));
		registerItemBlock(registry, new ItemBlock(BLUE_CAKE, new Item.Properties().group(ModItemGroup.MAIN).maxStackSize(1)));
		registerItemBlock(registry, new ItemBlock(COLD_CAKE, new Item.Properties().group(ModItemGroup.MAIN).maxStackSize(1)));
		registerItemBlock(registry, new ItemBlock(RED_CAKE, new Item.Properties().group(ModItemGroup.MAIN).maxStackSize(1)));
		registerItemBlock(registry, new ItemBlock(HOT_CAKE, new Item.Properties().group(ModItemGroup.MAIN).maxStackSize(1)));
		registerItemBlock(registry, new ItemBlock(REVERSE_CAKE, new Item.Properties().group(ModItemGroup.MAIN).maxStackSize(1)));
		registerItemBlock(registry, new ItemBlock(FUCHSIA_CAKE, new Item.Properties().group(ModItemGroup.MAIN).maxStackSize(1)));
		
		registerItemBlock(registry, PRIMED_TNT, ModItemGroup.MAIN);
		registerItemBlock(registry, UNSTABLE_TNT, ModItemGroup.MAIN);
		registerItemBlock(registry, INSTANT_TNT, ModItemGroup.MAIN);
		registerItemBlock(registry, WOODEN_EXPLOSIVE_BUTTON, ModItemGroup.MAIN);
		registerItemBlock(registry, STONE_EXPLOSIVE_BUTTON, ModItemGroup.MAIN);
		
		registerItemBlock(registry, BLENDER, ModItemGroup.MAIN);
		registerItemBlock(registry, CHESSBOARD, ModItemGroup.MAIN);
		registerItemBlock(registry, MINI_FROG_STATUE, ModItemGroup.MAIN);
		registerItemBlock(registry, GLOWYSTONE_WIRE, ModItemGroup.MAIN);	//TODO Glowystone wire item
		
		//hammers
		registry.register(CLAW_HAMMER = new ItemWeapon(ItemTier.IRON, 2, -2.4F, 1.0F, new Item.Properties().defaultMaxDamage(131).addToolType(ToolType.PICKAXE, 0).group(ModItemGroup.WEAPONS)).setRegistryName("claw_hammer"));
		registry.register(SLEDGE_HAMMER = new ItemWeapon(ItemTier.IRON, 4, -2.8F, 4.0F, new Item.Properties().addToolType(ToolType.PICKAXE, 2).group(ModItemGroup.WEAPONS)).setRegistryName("sledge_hammer"));
		registry.register(BLACKSMITH_HAMMER = new ItemWeapon(ItemTier.IRON, 5, -2.8F, 3.5F, new Item.Properties().defaultMaxDamage(450).addToolType(ToolType.PICKAXE, 2).group(ModItemGroup.WEAPONS)).setRegistryName("blacksmith_hammer"));
		registry.register(POGO_HAMMER = new ItemPogoWeapon(ModItemTypes.POGO_TIER, 5, -2.8F, 2.0F, 0.7, new Item.Properties().addToolType(ToolType.PICKAXE, 1).group(ModItemGroup.WEAPONS)).setRegistryName("pogo_hammer"));
		registry.register(TELESCOPIC_SASSACRUSHER = new ItemWeapon(ModItemTypes.BOOK_TIER, 8, -2.9F, 5.0F, new Item.Properties().defaultMaxDamage(1024).addToolType(ToolType.PICKAXE, 2).group(ModItemGroup.WEAPONS)).setRegistryName("telescopic_sassacrusher"));
		registry.register(REGI_HAMMER = new ItemWeapon(ModItemTypes.REGI_TIER, 3, -2.4F, 8.0F, new Item.Properties().addToolType(ToolType.PICKAXE, 2).group(ModItemGroup.WEAPONS)).setRegistryName("regi_hammer"));
		registry.register(FEAR_NO_ANVIL = new ItemPotionWeapon(ModItemTypes.RUBY_TIER, 6, -2.8F, 7.0F, new PotionEffect(MobEffects.SLOWNESS, 100, 3), new Item.Properties().addToolType(ToolType.PICKAXE, 3).group(ModItemGroup.WEAPONS)).setRegistryName("fear_no_anvil"));
		registry.register(MELT_MASHER = new ItemFireWeapon(ModItemTypes.RUBY_TIER, 6, -2.8F, 12.0F, 25, new Item.Properties().defaultMaxDamage(1413).addToolType(ToolType.PICKAXE, 4).group(ModItemGroup.WEAPONS)).setRegistryName("melt_masher"));
		registry.register(Q_E_HAMMER_AXE = new ItemPogoFarmine(ModItemTypes.RUBY_TIER, 5, -2.8F, 9.0F, Integer.MAX_VALUE, 200, 0.7, new Item.Properties().defaultMaxDamage(6114).addToolType(ToolType.PICKAXE, 3).addToolType(ToolType.SHOVEL, 1).addToolType(ToolType.AXE, 2).group(ModItemGroup.WEAPONS)).setRegistryName("estrogen_empowered_everything_eradicator"));
		registry.register(D_D_E_HAMMER_AXE = new ItemSbahjEEEE(ModItemTypes.RUBY_TIER, 5, -2.8F, 9.1F, 0.2, new Item.Properties().defaultMaxDamage(6114).group(ModItemGroup.WEAPONS)).setRegistryName("eeeeeeeeeeee"));
		registry.register(ZILLYHOO_HAMMER = new ItemWeapon(ModItemTypes.ZILLYHOO_TIER, 6, -2.8F, 15.0F, new Item.Properties().addToolType(ToolType.PICKAXE, 4).group(ModItemGroup.WEAPONS)).setRegistryName("zillyhoo_hammer"));
		registry.register(POPAMATIC_VRILLYHOO = new ItemRandomWeapon(ModItemTypes.ZILLYHOO_TIER, 3, -2.8F, 15.0F, new Item.Properties().addToolType(ToolType.PICKAXE, 4).group(ModItemGroup.WEAPONS)).setRegistryName("popamatic_vrillyhoo"));
		registry.register(SCARLET_ZILLYHOO = new ItemFireWeapon(ModItemTypes.RUBY_TIER, 6, -2.8F, 4.0F, 50, new Item.Properties().addToolType(ToolType.PICKAXE, 3)).setRegistryName("scarlet_zillyhoo"));
		registry.register(MWRTHWL = new ItemWeapon(ModItemTypes.RUBY_TIER, 6, -2.8F, 4.0F, new Item.Properties().addToolType(ToolType.PICKAXE, 3)).setRegistryName("mwrthwl"));
		
		//blades
		registry.register(SORD = new ItemSord(ModItemTypes.SBAHJ_TIER, 3, -2.4F, 1.0F, new Item.Properties().group(ModItemGroup.WEAPONS)).setRegistryName("sord"));
		registry.register(CACTUS_CUTLASS = new ItemWeapon(ModItemTypes.CACTUS_TIER, 3, -2.4F, 15.0F, new Item.Properties().group(ModItemGroup.WEAPONS)).setRegistryName("cactaceae_cutlass"));	//The sword harvestTool is only used against webs, hence the high efficiency.
		registry.register(STEAK_SWORD = new ItemConsumableWeapon(ModItemTypes.MEAT_TIER, 4, -2.4F, 5.0F, 8, 1F, new Item.Properties().defaultMaxDamage(250).group(ModItemGroup.WEAPONS)).setRegistryName("steak_sword"));
		registry.register(BEEF_SWORD = new ItemConsumableWeapon(ModItemTypes.MEAT_TIER, 2, -2.4F, 5.0F, 3, 0.8F, 75, new Item.Properties().group(ModItemGroup.WEAPONS)).setRegistryName("beef_sword"));
		registry.register(IRRADIATED_STEAK_SWORD = new ItemConsumableWeapon(ModItemTypes.MEAT_TIER, 2, -2.4F, 5.0F, 4, 0.4F, 25, new Item.Properties().defaultMaxDamage(150).group(ModItemGroup.WEAPONS)).setPotionEffect(new PotionEffect(MobEffects.WITHER, 100, 1), 0.9F).setRegistryName("irradiated_steak_sword"));
		registry.register(KATANA = new ItemWeapon(ItemTier.IRON, 3, -2.4F, 15.0F, new Item.Properties().group(ModItemGroup.WEAPONS)).setRegistryName("katana"));
		registry.register(UNBREAKABLE_KATANA = new ItemWeapon(ModItemTypes.RUBY_TIER, 3, -2.4F, 15.0F, new Item.Properties().group(ModItemGroup.WEAPONS)).setRegistryName("unbreakable_katana"));	//Not actually unbreakable
		registry.register(FIRE_POKER = new ItemFireWeapon(ItemTier.IRON, 4, -2.4F, 15.0F,  30, new Item.Properties().group(ModItemGroup.WEAPONS)).setRegistryName("fire_poker"));
		registry.register(HOT_HANDLE = new ItemFireWeapon(ItemTier.IRON, 3, -2.4F, 15.0F, 10, new Item.Properties().defaultMaxDamage(350).group(ModItemGroup.WEAPONS)).setRegistryName("too_hot_to_handle"));
		registry.register(CALEDSCRATCH = new ItemWeapon(ModItemTypes.RUBY_TIER, 2, -2.4F, 15.0F, new Item.Properties().defaultMaxDamage(1561).group(ModItemGroup.WEAPONS)).setRegistryName("caledscratch"));
		registry.register(CALEDFWLCH = new ItemWeapon(ModItemTypes.RUBY_TIER, 2, -2.4F, 15.0F, new Item.Properties().defaultMaxDamage(1025).group(ModItemGroup.WEAPONS)).setRegistryName("caledfwlch"));
		registry.register(ROYAL_DERINGER = new ItemWeapon(ModItemTypes.RUBY_TIER, 3, -2.4F, 15.0F, new Item.Properties().defaultMaxDamage(1561).group(ModItemGroup.WEAPONS)).setRegistryName("royal_deringer"));
		registry.register(CLAYMORE = new ItemWeapon(ItemTier.IRON, 5, -2.6F, 15.0F, new Item.Properties().defaultMaxDamage(600).group(ModItemGroup.WEAPONS)).setRegistryName("claymore"));
		registry.register(ZILLYWAIR_CUTLASS = new ItemWeapon(ModItemTypes.ZILLYHOO_TIER, 3, -2.4F, 15.0F, new Item.Properties().group(ModItemGroup.WEAPONS)).setRegistryName("cutlass_of_zillywair"));
		registry.register(REGISWORD = new ItemWeapon(ModItemTypes.REGI_TIER, 3, -2.4F, 15.0F, new Item.Properties().group(ModItemGroup.WEAPONS)).setRegistryName("regisword"));
		registry.register(SCARLET_RIBBITAR = new ItemWeapon(ModItemTypes.RUBY_TIER, 3, -2.4F, 15.0F, new Item.Properties().group(ModItemGroup.WEAPONS)).setRegistryName("scarlet_ribbitar"));
		registry.register(DOGG_MACHETE = new ItemWeapon(ModItemTypes.RUBY_TIER, 1, -2.4F, 15.0F, new Item.Properties().defaultMaxDamage(1000).group(ModItemGroup.WEAPONS)).setRegistryName("dogg_machete"));
		registry.register(COBALT_SABRE = new ItemFireWeapon(ItemTier.GOLD, 7, -2.4F, 15.0F, 30, new Item.Properties().defaultMaxDamage(300).group(ModItemGroup.WEAPONS)).setRegistryName("cobalt_sabre"));
		registry.register(QUAMTUM_SABRE = new ItemPotionWeapon(ModItemTypes.URANIUM_TIER, 4, -2.4F, 15.0F, new PotionEffect(MobEffects.WITHER, 100, 1), new Item.Properties().group(ModItemGroup.WEAPONS)).setRegistryName("quantum_sabre"));
		registry.register(SHATTER_BEACON = new ItemWeapon(ModItemTypes.RUBY_TIER, 6, -2.4F, 15.0F, new Item.Properties().defaultMaxDamage(1850).group(ModItemGroup.WEAPONS)).setRegistryName("shatter_beacon"));
		
		//axes
		registry.register(BATLEACKS = new ItemSord(ModItemTypes.SBAHJ_TIER, 5, -3.5F, 1.0F, new Item.Properties().group(ModItemGroup.WEAPONS)).setRegistryName("batleacks"));
		registry.register(COPSE_CRUSHER = new ItemFarmine(ItemTier.STONE, 5, -3.0F, 6.0F, Integer.MAX_VALUE, 20, new Item.Properties().defaultMaxDamage(400).addToolType(ToolType.AXE, 2).group(ModItemGroup.WEAPONS)).setRegistryName("copse_crusher"));
		registry.register(BATTLEAXE = new ItemWeapon(ItemTier.IRON, 8, -3.0F, 3.0F, new Item.Properties().defaultMaxDamage(600).group(ModItemGroup.WEAPONS).addToolType(ToolType.AXE, 2)).setRegistryName("battleaxe"));
		registry.register(BLACKSMITH_BANE = new ItemWeapon(ItemTier.STONE, 8, -3.0F, 6.0F, new Item.Properties().defaultMaxDamage(413).group(ModItemGroup.WEAPONS).addToolType(ToolType.AXE, 2)).setRegistryName("blacksmith_bane"));
		registry.register(SCRAXE = new ItemWeapon(ItemTier.IRON, 8, -3.0F, 7.0F, new Item.Properties().defaultMaxDamage(500).group(ModItemGroup.WEAPONS).addToolType(ToolType.AXE, 2)).setRegistryName("scraxe"));
		registry.register(Q_P_HAMMER_AXE = new ItemPogoFarmine(ModItemTypes.POGO_TIER, 6, -3.0F, 2.0F, Integer.MAX_VALUE, 50, 0.6, new Item.Properties().defaultMaxDamage(800).group(ModItemGroup.WEAPONS).addToolType(ToolType.AXE, 2).addToolType(ToolType.PICKAXE, 1)).setRegistryName("piston_powered_pogo_axehammer"));
		registry.register(RUBY_CROAK = new ItemWeapon(ModItemTypes.RUBY_TIER, 7, -3.0F, 8.0F, new Item.Properties().group(ModItemGroup.WEAPONS).addToolType(ToolType.AXE, 3)).setRegistryName("ruby_croak"));
		registry.register(HEPHAESTUS_LUMBER = new ItemFireWeapon(ModItemTypes.RUBY_TIER, 7, -3.0F, 9.0F, 30, new Item.Properties().defaultMaxDamage(3000).group(ModItemGroup.WEAPONS).addToolType(ToolType.AXE, 3)).setRegistryName("hephaestus_lumberjack"));
		registry.register(Q_F_HAMMER_AXE = new ItemPogoFarmine(ModItemTypes.URANIUM_TIER, 7, -3.0F, 5.0F, Integer.MAX_VALUE, 100, 0.7, new Item.Properties().defaultMaxDamage(2048).group(ModItemGroup.WEAPONS).addToolType(ToolType.PICKAXE, 2).addToolType(ToolType.AXE, 3)).setRegistryName("fission_focused_fault_feller"));
		
		//Dice
		registry.register(DICE = new ItemWeapon(ItemTier.STONE, 5, -3.0F, 1.0F, new Item.Properties().defaultMaxDamage(51).group(ModItemGroup.WEAPONS)).setRegistryName("dice"));
		registry.register(FLUORITE_OCTET = new ItemWeapon(ItemTier.DIAMOND, 12, -3.0F, 1.0F, new Item.Properties().defaultMaxDamage(67).group(ModItemGroup.WEAPONS)).setRegistryName("fluorite_octet"));
		//misc weapons
		registry.register(CAT_CLAWS_DRAWN  = new ItemDualWeapon(ItemTier.IRON, 2, -1.5F, 10.0F, new Item.Properties().defaultMaxDamage(500).group(ModItemGroup.WEAPONS)).setRegistryName("cat_claws_drawn"));
		registry.register(CAT_CLAWS_SHEATHED  = new ItemDualWeapon(ItemTier.IRON, -1, -1.0F, 10.0F, (ItemDualWeapon) CAT_CLAWS_DRAWN, new Item.Properties().defaultMaxDamage(500)).setRegistryName("cat_claws_sheathed"));
		//sickles
		registry.register(SICKLE = new ItemWeapon(ItemTier.IRON, 2, -2.4F, 1.5F, new Item.Properties().group(ModItemGroup.WEAPONS).addToolType(ModItemTypes.SICKLE_TOOL, 0)).setRegistryName("sickle"));
		registry.register(HOMES_SMELL_YA_LATER = new ItemWeapon(ItemTier.IRON, 3, -2.4F, 3.0F, new Item.Properties().defaultMaxDamage(400).group(ModItemGroup.WEAPONS).addToolType(ModItemTypes.SICKLE_TOOL, 0)).setRegistryName("homes_smell_ya_later"));
		registry.register(FUDGE_SICKLE = new ItemConsumableWeapon(ModItemTypes.CANDY_TIER, 5, -2.4F, 1.0F, 7, 0.6F, new Item.Properties().group(ModItemGroup.WEAPONS).addToolType(ModItemTypes.SICKLE_TOOL, 0)).setRegistryName("fudgesickle"));
		registry.register(REGI_SICKLE = new ItemWeapon(ModItemTypes.REGI_TIER, 3, -2.4F, 4.0F, new Item.Properties().group(ModItemGroup.WEAPONS).addToolType(ModItemTypes.SICKLE_TOOL, 0)).setRegistryName("regisickle"));
		registry.register(CLAW_SICKLE = new ItemWeapon(ModItemTypes.RUBY_TIER, 3, -2.4F, 4.0F, new Item.Properties().group(ModItemGroup.WEAPONS).addToolType(ModItemTypes.SICKLE_TOOL, 0)).setRegistryName("claw_sickle"));
		registry.register(CLAW_OF_NRUBYIGLITH = new ItemHorrorterrorWeapon(ModItemTypes.HORRORTERROR_TIER, 5, -2.4F, 4.0F, new Item.Properties().group(ModItemGroup.WEAPONS).addToolType(ModItemTypes.SICKLE_TOOL, 0)).setRegistryName("claw_of_nrubyiglith"));
		registry.register(CANDY_SICKLE = new ItemCandyWeapon(ModItemTypes.CANDY_TIER, 6, -2.4F, 2.5F, new Item.Properties().defaultMaxDamage(96).group(ModItemGroup.WEAPONS).addToolType(ModItemTypes.SICKLE_TOOL, 0)).setRegistryName("candy_sickle"));
		
		//clubs
		registry.register(DEUCE_CLUB = new ItemWeapon(ItemTier.WOOD, 3, -2.2F, 2.0F, new Item.Properties().defaultMaxDamage(1024).group(ModItemGroup.WEAPONS)).setRegistryName("deuce_club"));
		registry.register(NIGHT_CLUB = new ItemWeapon(ModItemTypes.REGI_TIER, 1, -2.2F, 2.0F, new Item.Properties().defaultMaxDamage(600).group(ModItemGroup.WEAPONS)).setRegistryName("night_club"));
		registry.register(POGO_CLUB = new ItemPogoWeapon(ModItemTypes.POGO_TIER, 2, -2.2F, 2.0F, 0.5, new Item.Properties().group(ModItemGroup.WEAPONS)).setRegistryName("pogo_club"));
		registry.register(METAL_BAT = new ItemWeapon(ItemTier.IRON, 3, -2.2F, 2.0F, new Item.Properties().defaultMaxDamage(750).group(ModItemGroup.WEAPONS)).setRegistryName("metal_bat"));
		registry.register(SPIKED_CLUB = new ItemWeapon(ItemTier.WOOD, 5, -2.2F, 2.0F, new Item.Properties().defaultMaxDamage(500).group(ModItemGroup.WEAPONS)).setRegistryName("spiked_club"));
		
		//canes
		registry.register(CANE = new ItemWeapon(ItemTier.WOOD, 2, -2.0F, 1.0F, new Item.Properties().defaultMaxDamage(100).group(ModItemGroup.WEAPONS)).setRegistryName("cane"));
		registry.register(IRON_CANE = new ItemWeapon(ItemTier.IRON, 2, -2.0F, 1.0F, new Item.Properties().defaultMaxDamage(450).group(ModItemGroup.WEAPONS)).setRegistryName("iron_cane"));
		registry.register(SPEAR_CANE = new ItemWeapon(ItemTier.IRON, 3, -2.0F, 1.0F, new Item.Properties().defaultMaxDamage(300).group(ModItemGroup.WEAPONS)).setRegistryName("spear_cane"));
		registry.register(PARADISES_PORTABELLO = new ItemWeapon(ModItemTypes.CANDY_TIER, 3, -2.0F, 1.0F, new Item.Properties().defaultMaxDamage(175).group(ModItemGroup.WEAPONS)).setRegistryName("paradises_portabello"));
		registry.register(REGI_CANE = new ItemWeapon(ModItemTypes.REGI_TIER, 3, -2.0F, 1.0F, new Item.Properties().group(ModItemGroup.WEAPONS)).setRegistryName("regi_cane"));
		registry.register(DRAGON_CANE = new ItemWeapon(ModItemTypes.RUBY_TIER, 3, -2.0F, 1.0F, new Item.Properties().defaultMaxDamage(300).group(ModItemGroup.WEAPONS)).setRegistryName("dragon_cane"));
		registry.register(POGO_CANE = new ItemPogoWeapon(ModItemTypes.POGO_TIER, 2, -2.0F, 1.0F, 0.6, new Item.Properties().group(ModItemGroup.WEAPONS)).setRegistryName("pogo_cane"));
		//Spoons/forks
		registry.register(WOODEN_SPOON = new ItemWeapon(ItemTier.WOOD, 2, -2.2F, 1.0F, new Item.Properties().group(ModItemGroup.WEAPONS)).setRegistryName("wooden_spoon"));
		registry.register(SILVER_SPOON = new ItemWeapon(ItemTier.IRON, 1, -2.2F, 1.0F, new Item.Properties().group(ModItemGroup.WEAPONS)).setRegistryName("silver_spoon"));
		registry.register(CROCKER_SPOON = new ItemDualWeapon(ModItemTypes.RUBY_TIER, 0, -2.2F, 1.0F, new Item.Properties().defaultMaxDamage(512).group(ModItemGroup.WEAPONS)).setRegistryName("crocker_spoon"));
		registry.register(CROCKER_FORK = new ItemDualWeapon(ModItemTypes.RUBY_TIER, 2, -2.6F, 1.0F, (ItemDualWeapon) CROCKER_SPOON, new Item.Properties().defaultMaxDamage(512)).setRegistryName("crocker_fork"));
		registry.register(SKAIA_FORK = new ItemWeapon(ModItemTypes.REGI_TIER, 5, -2.2F, 1.0F, new Item.Properties().defaultMaxDamage(2048).group(ModItemGroup.WEAPONS)).setRegistryName("skaia_fork"));
		registry.register(FORK = new ItemWeapon(ItemTier.STONE, 3, -2.2F, 1.0F, new Item.Properties().group(ModItemGroup.WEAPONS)).setRegistryName("fork"));
		registry.register(SPORK = new ItemWeapon(ItemTier.STONE, 4, -2.3F, 1.0F, new Item.Properties().group(ModItemGroup.WEAPONS)).setRegistryName("spork"));
		registry.register(GOLDEN_SPORK = new ItemWeapon(ItemTier.GOLD, 5, -2.3F, 1.0F, new Item.Properties().group(ModItemGroup.WEAPONS)).setRegistryName("golden_spork"));

		registry.register(EMERALD_SWORD = new ItemSword(ModItemTypes.EMERALD_TIER, 3, -2.4F, new Item.Properties().group(ModItemGroup.WEAPONS)).setRegistryName("emerald_sword"));
		registry.register(EMERALD_AXE = new ItemModAxe(ModItemTypes.EMERALD_TIER, 5, -3.0F, new Item.Properties().group(ModItemGroup.WEAPONS)).setRegistryName("emerald_axe"));
		registry.register(EMERALD_PICKAXE = new ItemModPickaxe(ModItemTypes.EMERALD_TIER, 1 , -2.8F, new Item.Properties().group(ModItemGroup.WEAPONS)).setRegistryName("emerald_pickaxe"));
		registry.register(EMERALD_SHOVEL = new ItemSpade(ModItemTypes.EMERALD_TIER, 1.5F, -3.0F, new Item.Properties().group(ModItemGroup.WEAPONS)).setRegistryName("emerald_shovel"));
		registry.register(EMERALD_HOE = new ItemHoe(ModItemTypes.EMERALD_TIER, 0.0F, new Item.Properties().group(ModItemGroup.WEAPONS)).setRegistryName("emerald_hoe"));
		
		//armor
		registry.register(PRISMARINE_HELMET = new ItemArmor(ModItemTypes.PRISMARINE_ARMOR, EntityEquipmentSlot.HEAD, new Item.Properties().group(ModItemGroup.WEAPONS)).setRegistryName("prismarine_helmet"));
		registry.register(PRISMARINE_CHESTPLATE = new ItemArmor(ModItemTypes.PRISMARINE_ARMOR, EntityEquipmentSlot.CHEST, new Item.Properties().group(ModItemGroup.WEAPONS)).setRegistryName("prismarine_chestplate"));
		registry.register(PRISMARINE_LEGGINGS = new ItemArmor(ModItemTypes.PRISMARINE_ARMOR, EntityEquipmentSlot.LEGS, new Item.Properties().group(ModItemGroup.WEAPONS)).setRegistryName("prismarine_leggings"));
		registry.register(PRISMARINE_BOOTS = new ItemArmor(ModItemTypes.PRISMARINE_ARMOR, EntityEquipmentSlot.FEET, new Item.Properties().group(ModItemGroup.WEAPONS)).setRegistryName("prismarine_boots"));
		
		registry.register(UP_STICK = new Item(new Item.Properties().group(ModItemGroup.WEAPONS).maxStackSize(1)).setRegistryName("uranium_powered_stick"));
		
		//food
		registry.register(candy.setRegistryName("candy"));
		registry.register(beverage.setRegistryName("beverage"));
		registry.register(bugOnAStick.setRegistryName("bug_on_stick"));
		registry.register(chocolateBeetle.setRegistryName("chocolate_beetle"));
		registry.register(coneOfFlies.setRegistryName("cone_of_flies"));
		registry.register(grasshopper.setRegistryName("grasshopper"));
		registry.register(jarOfBugs.setRegistryName("jar_of_bugs"));
		registry.register(onion.setRegistryName("onion"));
		registry.register(salad.setRegistryName("salad"));
		registry.register(desertFruit.setRegistryName("desert_fruit"));
		registry.register(irradiatedSteak.setRegistryName("irradiated_steak"));
		registry.register(rockCookie.setRegistryName("rock_cookie"));
		registry.register(fungalSpore.setRegistryName("fungal_spore"));
		registry.register(sporeo.setRegistryName("sporeo"));
		registry.register(morelMushroom.setRegistryName("morel_mushroom"));
		registry.register(frenchFry.setRegistryName("french_fry"));
		registry.register(strawberryChunk.setRegistryName("strawberry_chunk"));
		registry.register(surpriseEmbryo.setRegistryName("surprise_embryo"));
		registry.register(unknowableEgg.setRegistryName("unknowable_egg"));

		//misc
		registry.register(goldenGrasshopper.setRegistryName("golden_grasshopper"));
		registry.register(bugNet.setRegistryName("net"));
		registry.register(itemFrog.setRegistryName("frog"));
		registry.register(boondollars.setRegistryName("boondollars"));
		registry.register(rawCruxite.setRegistryName("raw_cruxite"));
		registry.register(rawUranium.setRegistryName("raw_uranium"));
		registry.register(energyCore.setRegistryName("energy_core"));
		registry.register(captchaCard.setRegistryName("captcha_card"));
		registry.register(cruxiteApple.setRegistryName("cruxite_apple"));
		registry.register(cruxitePotion.setRegistryName("cruxite_potion"));
		registry.register(disk.setRegistryName("computer_disk"));
        //registry.register(chessboard.setRegistryName("chessboard"));
		registry.register(grimoire.setRegistryName("grimoire"));
		registry.register(longForgottenWarhorn.setRegistryName("long_forgotten_warhorn"));
		registry.register(minestuckBucket.setRegistryName("minestuck_bucket"));
		registry.register(obsidianBucket.setRegistryName("bucket_obsidian"));
		registry.register(modusCard.setRegistryName("modus_card"));
		registry.register(goldSeeds.setRegistryName("gold_seeds"));
		registry.register(razorBlade.setRegistryName("razor_blade"));
		registry.register(metalBoat.setRegistryName("metal_boat"));
		registry.register(threshDvd.setRegistryName("thresh_dvd"));
		registry.register(gamebroMagazine.setRegistryName("gamebro_magazine"));
		registry.register(gamegrlMagazine.setRegistryName("gamegrl_magazine"));
		registry.register(crewPoster.setRegistryName("crew_poster"));
		registry.register(sbahjPoster.setRegistryName("sbahj_poster"));
		//registry.register(shopPoster.setRegistryName("shop_poster"));
		registry.register(carvingTool.setRegistryName("carving_tool"));
		registry.register(crumplyHat.setRegistryName("crumply_hat"));
		registry.register(stoneEyeballs.setRegistryName("stone_eyeballs"));
		registry.register(stoneSlab.setRegistryName("stone_slab"));
		registry.register(glowystoneDust.setRegistryName("glowystone_dust"));
		registry.register(fakeArms.setRegistryName("fake_arms"));
		//registry.register(shunt.setRegistryName("shunt"));
		registry.register(captcharoidCamera.setRegistryName("captcharoid_camera"));

		//Music disks
		registry.register(recordEmissaryOfDance.setRegistryName("record_emissary"));
		registry.register(recordDanceStab.setRegistryName("record_dance_stab"));
		registry.register(recordRetroBattle.setRegistryName("record_retro_battle"));

		/*((ItemMinestuckBucket) minestuckBucket).addBlock(blockOil.getDefaultState());
		((ItemMinestuckBucket) minestuckBucket).addBlock(blockBlood.getDefaultState());
		((ItemMinestuckBucket) minestuckBucket).addBlock(blockBrainJuice.getDefaultState());
		((ItemMinestuckBucket) minestuckBucket).addBlock(blockWatercolors.getDefaultState());
		((ItemMinestuckBucket) minestuckBucket).addBlock(blockEnder.getDefaultState());
		((ItemMinestuckBucket) minestuckBucket).addBlock(blockLightWater.getDefaultState());*/

		/*for(Block block : liquidGrists)
		{
			minestuckBucket.addBlock(block.getDefaultState());
		}*/
		
		((ItemMinestuckSeedFood) strawberryChunk).setPlant(STRAWBERRY_STEM.getDefaultState());
		
		ItemWeapon.addToolMaterial(ToolType.PICKAXE, Arrays.asList(Material.IRON, Material.ANVIL, Material.ROCK));
		ItemWeapon.addToolMaterial(ToolType.AXE, Arrays.asList(Material.WOOD, Material.PLANTS, Material.VINE));
		ItemWeapon.addToolMaterial(ToolType.SHOVEL, Arrays.asList(Material.SNOW, Material.CRAFTED_SNOW, Material.CLAY, Material.GRASS, Material.GROUND, Material.SAND));
		//ItemWeapon.addToolMaterial("sword", Arrays.asList(Material.WEB));
		ItemWeapon.addToolMaterial(ModItemTypes.SICKLE_TOOL, Arrays.asList(Material.WEB, Material.LEAVES, Material.PLANTS, Material.VINE));
	}
	
	private static Item registerItemBlock(IForgeRegistry<Item> registry, Block block)
	{
		return registerItemBlock(registry, new ItemBlock(block, new Item.Properties()));
	}
	
	private static Item registerItemBlock(IForgeRegistry<Item> registry, Block block, ItemGroup group)
	{
		return registerItemBlock(registry, new ItemBlock(block, new Item.Properties().group(group)));
	}
	
	private static Item registerItemBlock(IForgeRegistry<Item> registry, ItemBlock item)
	{
		if(item.getBlock().getRegistryName() == null)
			throw new IllegalArgumentException(String.format("The provided itemblock %s has a block without a registry name!", item.getBlock()));
		registry.register(item.setRegistryName(item.getBlock().getRegistryName()));
		return item;
	}
}