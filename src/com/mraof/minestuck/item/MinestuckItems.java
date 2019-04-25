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
import net.minecraft.init.Items;
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
	public static Item batleacks = new ItemSord(64, 4, -3.5D, 5, "batleacks");
	public static Item copseCrusher = new ItemFarmine(400, 6.0D, -3.0D, 20, "copseCrusher", Integer.MAX_VALUE, 20).setTool("axe", 2, 6.0F);
	public static Item battleaxe = new ItemWeapon(600, 10D, -3.0D, 15, "battleaxe").setTool("axe", 2, 3.0F);
	public static Item blacksmithBane = new ItemWeapon(413, 9.0D, -3.0D, 15, "blacksmithBane").setTool("axe", 2, 6.0F);
	public static Item scraxe = new ItemWeapon(500, 10.0D, -3.0D, 20, "scraxe").setTool("axe", 2, 7.0F);
	public static Item qPHammerAxe = new ItemPogoFarmine(800, 8.0D, -3.0D, 30, "qPHammerAxe", Integer.MAX_VALUE, 50, 0.6).setTool("pickaxe", 1, 2.0F).setTool("axe", 2, 7.0F);
	public static Item rubyCroak = new ItemWeapon(2000, 11.0D, -3.0D, 30, "rubyCroak").setTool("axe", 3, 8.0F);
	public static Item hephaestusLumber = new ItemFireWeapon(3000, 11.0D, -3.0D, 30, "hephaestusLumber", 30).setTool("axe", 3, 9.0F);
	public static Item qFHammerAxe = new ItemPogoFarmine(toolUranium, 2048, 11.0D, -3.0D, 0, "qFHammerAxe", Integer.MAX_VALUE, 100, 0.7).setTool("pickaxe", 2, 5.0F).setTool("axe", 3, 9.0F);
	//Dice
	public static Item dice = new ItemWeapon(51, 6, 3, 6, "dice");
	public static Item fluoriteOctet = new ItemWeapon(67, 15, 6, 8, "fluoriteOctet");
	//misc weapons
	public static Item catClaws = new ItemDualWeapon(500, 4.0D, 1.0D, -1.5D, -1.0D, 6, "catclaws");
	//sickles
	public static Item sickle = new ItemWeapon(220, 4.0D, -2.4D, 8, "sickle").setTool("sickle", 0, 1.5F);
	public static Item homesSmellYaLater = new ItemWeapon(400, 5.5D, -2.4D, 10, "homesSmellYaLater").setTool("sickle", 0, 3.0F);
	public static Item fudgeSickle = new ItemConsumableWeapon(450, 5.5D, -2.4D, 10, "fudgeSickle", 7, 0.6F).setTool("sickle", 0, 1.0F);
	public static Item regiSickle = new ItemWeapon(812, 6.0D, -2.4D, 5, "regiSickle").setTool("sickle", 0, 4.0F);
	public static Item clawSickle = new ItemWeapon(2048, 7.0D, -2.4D, 15, "clawSickle").setTool("sickle", 0, 4.0F);
	public static Item clawOfNrubyiglith = new ItemHorrorterrorWeapon(1600, 9.5D, -2.4D, 15, "clawOfNrubyiglith").setTool("sickle", 0, 4.0F);
	public static Item candySickle = new ItemCandyWeapon(96, 6.0D, -2.4D, 15, "candySickle").setTool("sickle", 0, 2.5F);
	//clubs
	public static Item deuceClub = new ItemWeapon(1024, 2.5D, -2.2D, 15, "deuceClub");
	public static Item nightClub = new ItemWeapon(600, 4.0D, -2.2D, 20, "nightClub");
	public static Item pogoClub = new ItemPogoWeapon(600, 3.5D, -2.2D, 15, "pogoClub", 0.5);
	public static Item metalBat = new ItemWeapon(750, 5.0D, -2.2D, 5, "metalBat");
	public static Item spikedClub = new ItemWeapon(500, 5.5D, -2.2D, 5, "spikedClub");
	//canes
	public static Item cane = new ItemWeapon(100, 2.0D, -2.0D, 15, "cane");
	public static Item ironCane = new ItemWeapon(450, 3.5D, -2.0D, 10, "ironCane");
	public static Item spearCane = new ItemWeapon(300, 5.0D, -2.0D, 13, "spearCane");
	public static Item paradisesPortabello = new ItemWeapon(175, 3.0D, -2.0D, 10, "paradisesPortabello");
	public static Item regiCane = new ItemWeapon(812, 6.0D, -2.0D, 7, "regiCane");
	public static Item dragonCane = new ItemWeapon(300, 6.5D, -2.0D, 20, "dragonCane");
	public static Item pogoCane = new ItemPogoWeapon(500, 3.0D, -2.0D, 15, "pogoCane", 0.6);
	public static Item upStick = new ItemWeapon(toolUranium, 1, 0.0D, 0.0D, 0, "upStick").setUnbreakable();	//Never runs out of uranium!
	//Spoons/forks
	public static Item woodenSpoon = new ItemWeapon(59, 2.0D, -2.2D, 5, "woodenSpoon");
	public static Item silverSpoon = new ItemWeapon(250, 2.5D, -2.2D, 12, "silverSpoon");
	public static ItemSpork crockerSpork = (ItemSpork) new ItemSpork(512, 4.0D, -2.2D, 15, "crocker");
	public static Item skaiaFork = new ItemWeapon(2048, 8.5D, -2.2D, 10, "skaiaFork");
	public static Item fork = new ItemWeapon(100, 4.0D, -2.2D, 3, "fork");
	public static Item spork = new ItemWeapon(120, 4.5D, -2.3D, 5, "spork");
	public static Item goldenSpork = new ItemWeapon(45, 5D, -2.3D, 22, "goldenSpork");
	//Material tools
	public static Item emeraldSword = new ItemSword(toolEmerald).setUnlocalizedName("swordEmerald").setCreativeTab(TabMinestuck.instance);
	public static Item emeraldAxe = new ItemMinestuckAxe(toolEmerald, 9.0F, -3.0F).setUnlocalizedName("hatchetEmerald").setCreativeTab(TabMinestuck.instance);
	public static Item emeraldPickaxe = new ItemMinestuckPickaxe(toolEmerald).setUnlocalizedName("pickaxeEmerald").setCreativeTab(TabMinestuck.instance);
	public static Item emeraldShovel = new ItemSpade(toolEmerald).setUnlocalizedName("shovelEmerald").setCreativeTab(TabMinestuck.instance);
	public static Item emeraldHoe = new ItemHoe(toolEmerald).setUnlocalizedName("hoeEmerald").setCreativeTab(TabMinestuck.instance);
	//Armor
	public static Item prismarineHelmet = new ItemArmor(armorPrismarine, 0, EntityEquipmentSlot.HEAD).setUnlocalizedName("helmetPrismarine").setCreativeTab(TabMinestuck.instance);
	public static Item prismarineChestplate = new ItemArmor(armorPrismarine, 0, EntityEquipmentSlot.CHEST).setUnlocalizedName("chestplatePrismarine").setCreativeTab(TabMinestuck.instance);
	public static Item prismarineLeggings = new ItemArmor(armorPrismarine, 0, EntityEquipmentSlot.LEGS).setUnlocalizedName("leggingsPrismarine").setCreativeTab(TabMinestuck.instance);
	public static Item prismarineBoots = new ItemArmor(armorPrismarine, 0, EntityEquipmentSlot.FEET).setUnlocalizedName("bootsPrismarine").setCreativeTab(TabMinestuck.instance);
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
		registry.register(batleacks.setRegistryName("batleacks"));
		registry.register(copseCrusher.setRegistryName("copse_crusher"));
		registry.register(battleaxe.setRegistryName("battleaxe"));
		registry.register(blacksmithBane.setRegistryName("blacksmith_bane"));
		registry.register(scraxe.setRegistryName("scraxe"));
		registry.register(qPHammerAxe.setRegistryName("piston_powered_pogo_axehammer"));
		registry.register(rubyCroak.setRegistryName("ruby_croak"));
		registry.register(hephaestusLumber.setRegistryName("hephaestus_lumberjack"));
		registry.register(qFHammerAxe.setRegistryName("fission_focused_fault_feller"));
		
		//Dice
		registry.register(dice.setRegistryName("dice"));
		registry.register(fluoriteOctet.setRegistryName("fluorite_octet"));
		//misc weapons
		registry.register(catClaws.setRegistryName("catclaws"));
		//sickles
		registry.register(sickle.setRegistryName("sickle"));
		registry.register(homesSmellYaLater.setRegistryName("homes_smell_ya_later"));
		registry.register(fudgeSickle.setRegistryName("fudgesickle"));
		registry.register(regiSickle.setRegistryName("regisickle"));
		registry.register(clawSickle.setRegistryName("claw_sickle"));
		registry.register(clawOfNrubyiglith.setRegistryName("claw_of_nrubyiglith"));
		registry.register(candySickle.setRegistryName("candy_sickle"));
		
		//clubs
		registry.register(deuceClub.setRegistryName("deuce_club"));
		registry.register(nightClub.setRegistryName("nightclub"));
		registry.register(pogoClub.setRegistryName("pogo_club"));
		registry.register(metalBat.setRegistryName("metal_bat"));
		registry.register(spikedClub.setRegistryName("spiked_club"));
		
		//canes
		registry.register(cane.setRegistryName("cane"));
		registry.register(ironCane.setRegistryName("iron_cane"));
		registry.register(spearCane.setRegistryName("spear_cane"));
		registry.register(paradisesPortabello.setRegistryName("paradises_portabello"));
		registry.register(regiCane.setRegistryName("regi_cane"));
		registry.register(dragonCane.setRegistryName("dragon_cane"));
		registry.register(pogoCane.setRegistryName("pogo_cane"));
		registry.register(upStick.setRegistryName("uranium_powered_stick"));
		//Spoons/forks
		registry.register(woodenSpoon.setRegistryName("wooden_spoon"));
		registry.register(silverSpoon.setRegistryName("silver_spoon"));
		registry.register(crockerSpork.setRegistryName("crocker_spork"));
		registry.register(skaiaFork.setRegistryName("skaia_fork"));
		registry.register(fork.setRegistryName("fork"));
		registry.register(spork.setRegistryName("spork"));
		registry.register(goldenSpork.setRegistryName("golden_spork"));

		registry.register(emeraldSword.setRegistryName("emerald_sword"));
		registry.register(emeraldAxe.setRegistryName("emerald_axe"));
		registry.register(emeraldPickaxe.setRegistryName("emerald_pickaxe"));
		registry.register(emeraldShovel.setRegistryName("emerald_shovel"));
		registry.register(emeraldHoe.setRegistryName("emerald_hoe"));
		
		//armor
		registry.register(prismarineHelmet.setRegistryName("prismarine_helmet"));
		registry.register(prismarineChestplate.setRegistryName("prismarine_chestplate"));
		registry.register(prismarineLeggings.setRegistryName("prismarine_leggings"));
		registry.register(prismarineBoots.setRegistryName("prismarine_boots"));
		
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

		((ItemMinestuckBucket) minestuckBucket).addBlock(blockOil.getDefaultState());
		((ItemMinestuckBucket) minestuckBucket).addBlock(blockBlood.getDefaultState());
		((ItemMinestuckBucket) minestuckBucket).addBlock(blockBrainJuice.getDefaultState());
		((ItemMinestuckBucket) minestuckBucket).addBlock(blockWatercolors.getDefaultState());
		((ItemMinestuckBucket) minestuckBucket).addBlock(blockEnder.getDefaultState());
		((ItemMinestuckBucket) minestuckBucket).addBlock(blockLightWater.getDefaultState());

		/*for(Block block : liquidGrists)
		{
			minestuckBucket.addBlock(block.getDefaultState());
		}*/
		
		((ItemMinestuckSeedFood) strawberryChunk).setPlant(strawberryStem.getDefaultState());
		
		ItemWeapon.addToolMaterial("pickaxe", Arrays.asList(Material.IRON, Material.ANVIL, Material.ROCK));
		ItemWeapon.addToolMaterial("axe", Arrays.asList(Material.WOOD, Material.PLANTS, Material.VINE));
		ItemWeapon.addToolMaterial("shovel", Arrays.asList(Material.SNOW, Material.CRAFTED_SNOW, Material.CLAY, Material.GRASS, Material.GROUND, Material.SAND));
		ItemWeapon.addToolMaterial("sword", Arrays.asList(Material.WEB));
		ItemWeapon.addToolMaterial("sickle", Arrays.asList(Material.WEB, Material.LEAVES, Material.PLANTS, Material.VINE));
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