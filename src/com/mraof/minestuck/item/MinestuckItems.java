package com.mraof.minestuck.item;

import com.mraof.minestuck.block.BlockColoredDirt;
import com.mraof.minestuck.block.BlockCrockerMachine;
import com.mraof.minestuck.block.BlockMinestuckLog;
import com.mraof.minestuck.block.BlockMinestuckStone;
import com.mraof.minestuck.entity.item.EntityCrewPoster;
import com.mraof.minestuck.entity.item.EntitySbahjPoster;
import com.mraof.minestuck.item.block.ItemBlockCraftingTab;
import com.mraof.minestuck.item.block.ItemBlockLayered;
import com.mraof.minestuck.item.block.ItemSburbMachine;
import com.mraof.minestuck.item.weapon.*;
import com.mraof.minestuck.util.MinestuckSoundHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityHanging;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import static com.mraof.minestuck.block.MinestuckBlocks.*;

public class MinestuckItems
{

	public static Item.ToolMaterial toolEmerald = EnumHelper.addToolMaterial("EMERALD", 3, 1220, 12.0F, 4.0F, 12).setRepairItem(new ItemStack(Items.EMERALD));
	public static ItemArmor.ArmorMaterial armorPrismarine = EnumHelper.addArmorMaterial("PRISMARINE", "minestuck:prismarine", 20, new int[]{3, 7, 6, 2}, 15, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
	;
	public static Item.ToolMaterial toolUranium = EnumHelper.addToolMaterial("URANIUM", 3, 1220, 12.0F, 6.0F, 15);
	//hammers
	public static Item clawHammer;
	public static Item sledgeHammer;
	public static Item blacksmithHammer;
	public static Item pogoHammer;
	public static Item telescopicSassacrusher;
	public static Item regiHammer;
	public static Item fearNoAnvil;
	public static Item zillyhooHammer;
	public static final CreativeTabs tabMinestuck = new CreativeTabs("tabMinestuck")
	{
		@Override
		public ItemStack getTabIconItem()
		{
			return new ItemStack(MinestuckItems.zillyhooHammer);
		}
	};
	public static Item popamaticVrillyhoo;
	public static Item scarletZillyhoo;
	public static Item mwrthwl;
	//blades
	public static Item sord;
	public static Item cactusCutlass;
	public static Item katana;
	public static Item unbreakableKatana;
	public static Item firePoker;
	public static Item hotHandle;
	public static Item caledscratch;
	public static Item caledfwlch;
	public static Item royalDeringer;
	public static Item zillywairCutlass;
	public static Item regisword;
	public static Item scarletRibbitar;
	public static Item doggMachete;
	public static Item cobaltSabre;
	public static Item quantumSabre;
	//axes
	public static Item copseCrusher;
	public static Item blacksmithBane;
	public static Item scraxe;
	public static Item qPHammerAxe;
	public static Item rubyCroak;
	public static Item hephaestusLumber;
	public static Item qFHammerAxe;
	//Dice
	public static Item dice;
	public static Item fluoriteOctet;
	//mic weapons
	public static Item catClaws;
	//sickles
	public static Item sickle;
	public static Item homesSmellYaLater;
	public static Item fudgeSickle;
	public static Item regiSickle;
	public static Item clawSickle;
	public static Item candySickle;
	//clubs
	public static Item deuceClub;
	public static Item nightClub;
	public static Item pogoClub;
	public static Item metalBat;
	public static Item spikedClub;
	//canes
	public static Item cane;
	public static Item ironCane;
	public static Item spearCane;
	public static Item regiCane;
	public static Item dragonCane;
	public static Item pogoCane;
	public static Item upStick;
	//Spoons/forks
	public static Item woodenSpoon;
	public static Item silverSpoon;
	public static ItemSpork crockerSpork = (ItemSpork) new ItemSpork(512, 4.0D, -2.2D, 15, "crocker").setRegistryName("crocker_spork");
	public static Item skaiaFork;
	public static Item fork;
	public static Item spork;
	//Material tools
	public static Item emeraldSword;
	public static Item emeraldAxe;
	public static Item emeraldPickaxe;
	public static Item emeraldShovel;
	public static Item emeraldHoe;
	//Armor
	public static Item prismarineHelmet;
	public static Item prismarineChestplate;
	public static Item prismarineLeggings;
	public static Item prismarineBoots;
	//Food
	public static Item candy;
	public static Item beverage;
	public static Item bugOnAStick;
	public static Item chocolateBeetle;
	public static Item coneOfFlies;
	public static Item grasshopper;
	public static Item jarOfBugs;
	public static Item onion;
	public static Item salad;
	public static Item irradiatedSteak;
	public static Item rockCookie;
	//Other
	public static Item rawCruxite;
	public static Item rawUranium;
	public static Item energyCore;
	public static Item cruxiteDowel;
	public static Item captchaCard;
	public static ItemCruxiteArtifact cruxiteApple;
	public static Item cruxitePotion;
	public static Item disk;
	public static Item chessboard;
	public static ItemMinestuckBucket minestuckBucket;
	public static Item obsidianBucket;
	public static Item modusCard;
	public static Item goldSeeds;
	public static Item metalBoat;
	public static Item threshDvd;
	public static Item crewPoster;
	public static Item sbahjPoster;
	public static Item carvingTool;
	public static Item crumplyHat;
	public static Item frogStatueReplica;
	public static Item stoneSlab;
	public static Item glowystoneDust;
	public static Item fakeArms;
	//Music disks
	public static Item recordEmissaryOfDance;
	public static Item recordDanceStab;

	//Creating items has to be after blocks are created because some items rely on blocks not being null, like gold seeds
	public static void createItems()
	{
		//hammers
		clawHammer = new ItemWeapon(131, 4.0D, -2.4D, 10, "clawHammer").setTool("pickaxe", 0, 1.0F).setRegistryName("claw_hammer");
		sledgeHammer = new ItemWeapon(250, 6.0D, -2.8D, 8, "sledgeHammer").setTool("pickaxe", 2, 4.0F).setRegistryName("sledge_hammer");
		blacksmithHammer = new ItemWeapon(450, 7.0D, -2.8D, 10, "blacksmithHammer").setTool("pickaxe", 2, 3.5F).setRegistryName("blacksmith_hammer");
		pogoHammer = new ItemPogoWeapon(400, 7.0D, -2.8D, 8, "pogoHammer", 0.7).setTool("pickaxe", 1, 2.0F).setRegistryName("pogo_hammer");
		telescopicSassacrusher = new ItemWeapon(1024, 9.0D, -2.9D, 15, "telescopicSassacrusher").setTool("pickaxe", 2, 5.0F).setRegistryName("telescopic_sassacrusher");
		regiHammer = new ItemWeapon(812, 6.0D, -2.4D, 5, "regiHammer").setRegistryName("regi_hammer");
		fearNoAnvil = new ItemPotionWeapon(2048, 10.0D, -2.8D, 12, "fearNoAnvil", new PotionEffect(MobEffects.SLOWNESS, 100, 3)).setTool("pickaxe", 3, 7.0F).setRegistryName("fear_no_anvil");
		zillyhooHammer = new ItemWeapon(3000, 11.0D, -2.8D, 30, "zillyhooHammer").setTool("pickaxe", 4, 15.0F).setRegistryName("zillyhoo_hammer");
		popamaticVrillyhoo = new ItemWeapon(3000, 0.0D, -2.8D, 30, "popamaticVrillyhoo").setTool("pickaxe", 4, 15.0F).setRegistryName("popamatic_vrillyhoo");
		scarletZillyhoo = new ItemFireWeapon(2000, 11.0D, -2.8D, 16, "scarletZillyhoo", 50).setTool("pickaxe", 3, 4.0F).setRegistryName("scarlet_zillyhoo");
		mwrthwl = new ItemWeapon(2000, 10.5D, -2.8D, 16, "mwrthwl").setTool("pickaxe", 3, 4.0F).setRegistryName("mwrthwl");
		//blades
		sord = new ItemSord(59, 2, -2.4D, 5, "sord").setRegistryName("sord");
		cactusCutlass = new ItemWeapon(104, 4, -2.4D, 10, "cactaceaeCutlass").setRegistryName("cactaceae_cutlass");
		katana = new ItemWeapon(250, 5, -2.4D, 15, "ninjaSword").setRegistryName("katana");
		unbreakableKatana = new ItemWeapon(2200, 7, -2.4D, 20, "katana").setRegistryName("unbreakable_katana");    //Not actually unbreakable
		firePoker = new ItemFireWeapon(250, 6, -2.4D, 15, "firePoker", 30).setRegistryName("fire_poker");
		hotHandle = new ItemFireWeapon(350, 5, -2.4D, 15, "hotHandle", 10).setRegistryName("too_hot_to_handle");
		caledscratch = new ItemWeapon(1561, 6, -2.4D, 30, "caledscratch").setRegistryName("caledscratch");
		caledfwlch = new ItemWeapon(1025, 6, -2.4D, 30, "caledfwlch").setRegistryName("caledfwlch");
		royalDeringer = new ItemWeapon(1561, 7, -2.4D, 30, "royalDeringer").setRegistryName("royal_deringer");
		zillywairCutlass = new ItemWeapon(2500, 8, -2.4D, 30, "zillywairCutlass").setRegistryName("cutlass_of_zillywair");
		regisword = new ItemWeapon(812, 6, -2.4D, 10, "regisword").setRegistryName("regisword");
		scarletRibbitar = new ItemWeapon(2000, 7, -2.4D, 30, "scarletRibbitar").setRegistryName("scarlet_ribbitar");
		doggMachete = new ItemWeapon(1000, 5, -2.4D, 30, "doggMachete").setRegistryName("dogg_machete");
		cobaltSabre = new ItemFireWeapon(300, 7, -2.4D, 10, "cobaltSabre", 30).setRegistryName("cobalt_sabre");
		quantumSabre = new ItemPotionWeapon(toolUranium, 600, 8, -2.4D, 5, "quantumSabre", new PotionEffect(MobEffects.WITHER, 100, 1)).setRegistryName("quantum_sabre");
		//axes
		copseCrusher = new ItemWeapon(400, 6.0D, -3.0D, 20, "copseCrusher", -1, 20).setTool("axe", 2, 7.0F).setRegistryName("copse_crusher");
		blacksmithBane = new ItemWeapon(413, 9.0D, -3.0D, 15, "blacksmithBane").setTool("axe", 2, 6.0F).setRegistryName("blacksmith_bane");
		scraxe = new ItemWeapon(500, 10.0D, -3.0D, 20, "scraxe").setTool("axe", 2, 7.0F).setRegistryName("scraxe");
		qPHammerAxe = new ItemPogoWeapon(800, 8.0D, -3.0D, 30, "qPHammerAxe", 0.6).setTool("axe", 2, 7.0F).setTool("pickaxe", 1, 2.0F).setTerminus(-1, 50).setRegistryName("piston_powered_pogo_axehammer");
		rubyCroak = new ItemWeapon(2000, 11.0D, -2.9D, 30, "rubyCroak").setTool("axe", 3, 8.0F).setRegistryName("ruby_croak");
		hephaestusLumber = new ItemFireWeapon(3000, 11.0D, -3.0D, 30, "hephaestusLumber", 30).setTool("axe", 3, 9.0F).setRegistryName("hephaestus_lumberjack");
		qFHammerAxe = new ItemPogoWeapon(2048, 11.0D, -3.0D, 0, "qFHammerAxe", 0.7).setTool("axe", 3, 9.0F).setTool("pickaxe", 2, 4.0F).setTerminus(-1, 100).setRegistryName("fission_focused_fault_feller");
		//Dice
		dice = new ItemWeapon(51, 6, 3, 6, "dice").setRegistryName("dice");
		fluoriteOctet = new ItemWeapon(67, 15, 6, 8, "fluoriteOctet").setRegistryName("fluorite_octet");
		//mic weapons
		catClaws = new ItemDualWeapon(500, 4.0D, 1.0D, -1.5D, -1.0D, 6, "catclaws").setRegistryName("catclaws");
		//sickles
		sickle = new ItemWeapon(220, 4.0D, -2.4D, 8, "sickle").setRegistryName("sickle");
		homesSmellYaLater = new ItemWeapon(400, 5.5D, -2.4D, 10, "homesSmellYaLater").setRegistryName("homes_smell_ya_later");
		fudgeSickle = new ItemWeapon(450, 5.5D, -2.4D, 10, "fudgeSickle").setRegistryName("fudgesickle");
		regiSickle = new ItemWeapon(812, 6.0D, -2.4D, 5, "regiSickle").setRegistryName("regisickle");
		clawSickle = new ItemWeapon(2048, 7.0D, -2.4D, 15, "clawSickle").setRegistryName("claw_sickle");
		candySickle = new ItemCandyWeapon(96, 6.0D, -2.4D, 15, "candySickle").setRegistryName("candy_sickle");
		//clubs
		deuceClub = new ItemWeapon(1024, 2.5D, -2.2D, 15, "deuceClub").setRegistryName("deuce_club");
		nightClub = new ItemWeapon(600, 4.0D, -2.2D, 20, "nightClub").setRegistryName("nightclub");
		pogoClub = new ItemPogoWeapon(600, 3.5D, -2.2D, 15, "pogoClub", 0.5).setRegistryName("pogo_club");
		metalBat = new ItemWeapon(750, 5.0D, -2.2D, 5, "metalBat").setRegistryName("metal_bat");
		spikedClub = new ItemWeapon(500, 5.5D, -2.2D, 5, "spikedClub").setRegistryName("spiked_club");
		//canes
		cane = new ItemWeapon(100, 2.0D, -2.0D, 15, "cane").setRegistryName("cane");
		ironCane = new ItemWeapon(450, 3.5D, -2.0D, 10, "ironCane").setRegistryName("iron_cane");
		spearCane = new ItemWeapon(300, 5.0D, -2.0D, 13, "spearCane").setRegistryName("spear_cane");
		regiCane = new ItemWeapon(812, 6.0D, -2.0D, 7, "regiCane").setRegistryName("regi_cane");
		dragonCane = new ItemWeapon(300, 6.5D, -2.0D, 20, "dragonCane").setRegistryName("dragon_cane");
		pogoCane = new ItemPogoWeapon(500, 3.0D, -2.0D, 15, "pogoCane", 0.6).setRegistryName("pogo_cane");
		upStick = new ItemWeapon(ToolMaterial.WOOD, 1, 0.0D, 0.0D, 0, "upStick").setUnbreakable().setRegistryName("uranium_powered_stick");
		//Spoons/forks
		woodenSpoon = new ItemWeapon(59, 2.0D, -2.2D, 5, "woodenSpoon").setRegistryName("wooden_spoon");
		silverSpoon = new ItemWeapon(250, 2.5D, -2.2D, 12, "silverSpoon").setRegistryName("silver_spoon");
		crockerSpork = (ItemSpork) new ItemSpork(512, 4.0D, -2.2D, 15, "crocker").setRegistryName("crocker_spork");
		skaiaFork = new ItemWeapon(2048, 8.5D, -2.2D, 10, "skaiaFork").setRegistryName("skaia_fork");
		fork = new ItemWeapon(100, 4.0D, -2.2D, 3, "fork").setRegistryName("fork");
		spork = new ItemWeapon(120, 4.5D, -2.3D, 5, "spork").setRegistryName("spork");
		//Material tools
		emeraldSword = new ItemSword(toolEmerald).setRegistryName("emerald_sword").setUnlocalizedName("swordEmerald").setCreativeTab(tabMinestuck);
		emeraldAxe = new ItemMinestuckAxe(toolEmerald, 9.0F, -3.0F).setRegistryName("emerald_axe").setUnlocalizedName("hatchetEmerald").setCreativeTab(tabMinestuck);
		emeraldPickaxe = new ItemMinestuckPickaxe(toolEmerald).setRegistryName("emerald_pickaxe").setUnlocalizedName("pickaxeEmerald").setCreativeTab(tabMinestuck);
		emeraldShovel = new ItemSpade(toolEmerald).setRegistryName("emerald_shovel").setUnlocalizedName("shovelEmerald").setCreativeTab(tabMinestuck);
		emeraldHoe = new ItemHoe(toolEmerald).setRegistryName("emerald_hoe").setUnlocalizedName("hoeEmerald").setCreativeTab(tabMinestuck);
		//Armor
		prismarineHelmet = new ItemArmor(armorPrismarine, 0, EntityEquipmentSlot.HEAD).setRegistryName("prismarine_helmet").setUnlocalizedName("helmetPrismarine").setCreativeTab(tabMinestuck);
		prismarineChestplate = new ItemArmor(armorPrismarine, 0, EntityEquipmentSlot.CHEST).setRegistryName("prismarine_chestplate").setUnlocalizedName("chestplatePrismarine").setCreativeTab(tabMinestuck);
		prismarineLeggings = new ItemArmor(armorPrismarine, 0, EntityEquipmentSlot.LEGS).setRegistryName("prismarine_leggings").setUnlocalizedName("leggingsPrismarine").setCreativeTab(tabMinestuck);
		prismarineBoots = new ItemArmor(armorPrismarine, 0, EntityEquipmentSlot.FEET).setRegistryName("prismarine_boots").setUnlocalizedName("bootsPrismarine").setCreativeTab(tabMinestuck);
		//Food
		candy = new ItemMinestuckCandy().setRegistryName("candy");
		beverage = new ItemMinestuckBeverage().setRegistryName("beverage");
		bugOnAStick = new ItemFood(1, 0.1F, false).setRegistryName("bug_on_stick").setUnlocalizedName("bugOnAStick").setCreativeTab(tabMinestuck);
		chocolateBeetle = new ItemFood(3, 0.4F, false).setRegistryName("chocolate_beetle").setUnlocalizedName("chocolateBeetle").setCreativeTab(tabMinestuck);
		coneOfFlies = new ItemFood(1, 0.1F, false).setRegistryName("cone_of_flies").setUnlocalizedName("coneOfFlies").setCreativeTab(tabMinestuck);
		grasshopper = new ItemFood(4, 0.5F, false).setRegistryName("grasshopper").setUnlocalizedName("grasshopper").setCreativeTab(tabMinestuck);
		jarOfBugs = new ItemFood(3, 0.2F, false).setRegistryName("jar_of_bugs").setUnlocalizedName("jarOfBugs").setCreativeTab(tabMinestuck);
		onion = new ItemFood(2, 0.2F, false).setRegistryName("onion").setUnlocalizedName("onion").setCreativeTab(tabMinestuck);
		salad = new ItemSoup(1).setRegistryName("salad").setUnlocalizedName("salad").setCreativeTab(tabMinestuck);
		irradiatedSteak = new ItemFood(4, 0.4F, true).setPotionEffect(new PotionEffect(MobEffects.WITHER, 100, 1), 0.9F).setRegistryName("irradiated_steak").setUnlocalizedName("irradiatedSteak").setCreativeTab(tabMinestuck);
		rockCookie = new Item().setRegistryName("rock_cookie").setUnlocalizedName("rockCookie").setCreativeTab(tabMinestuck);
		//Other
		rawCruxite = new Item().setRegistryName("raw_cruxite").setUnlocalizedName("rawCruxite").setCreativeTab(tabMinestuck);
		rawUranium = new Item().setRegistryName("raw_uranium").setUnlocalizedName("rawUranium").setCreativeTab(tabMinestuck);
		energyCore = new Item().setRegistryName("energy_core").setUnlocalizedName("energyCore").setCreativeTab(tabMinestuck);
		cruxiteDowel = new ItemDowel().setRegistryName("cruxite_dowel");
		captchaCard = new ItemCaptchaCard().setRegistryName("captcha_card");
		cruxiteApple = (ItemCruxiteArtifact) new ItemCruxiteApple().setRegistryName("cruxite_apple");
		cruxitePotion = new ItemCruxitePotion().setRegistryName("cruxite_potion");
		disk = new ItemDisk().setRegistryName("computer_disk");
		chessboard = new Item().setRegistryName("chessboard").setUnlocalizedName("chessboard").setMaxStackSize(1).setCreativeTab(tabMinestuck);
		minestuckBucket = (ItemMinestuckBucket) new ItemMinestuckBucket().setRegistryName("minestuck_bucket");
		obsidianBucket = new ItemObsidianBucket().setRegistryName("bucket_obsidian");
		modusCard = new ItemModus().setRegistryName("modus_card");
		goldSeeds = new ItemGoldSeeds().setRegistryName("gold_seeds");
		metalBoat = new ItemMetalBoat().setRegistryName("metal_boat");
		threshDvd = new Item().setRegistryName("thresh_dvd").setUnlocalizedName("threshDvd").setMaxStackSize(1).setCreativeTab(tabMinestuck);
		crewPoster = new ItemHanging()
		{
			@Override
			public EntityHanging createEntity(World worldIn, BlockPos pos, EnumFacing facing, ItemStack stack)
			{
				return new EntityCrewPoster(worldIn, pos, facing);
			}
		}.setRegistryName("crew_poster").setUnlocalizedName("crewPoster").setMaxStackSize(1).setCreativeTab(tabMinestuck);
		sbahjPoster = new ItemHanging()
		{
			@Override
			public EntityHanging createEntity(World worldIn, BlockPos pos, EnumFacing facing, ItemStack stack)
			{
				return new EntitySbahjPoster(worldIn, pos, facing);
			}
		}.setRegistryName("sbahj_poster").setUnlocalizedName("sbahjPoster").setMaxStackSize(1).setCreativeTab(tabMinestuck);
		carvingTool = new Item().setRegistryName("carving_tool").setUnlocalizedName("carvingTool").setMaxStackSize(1).setCreativeTab(tabMinestuck);
		crumplyHat = new Item().setRegistryName("crumply_hat").setUnlocalizedName("crumplyHat").setMaxStackSize(1).setCreativeTab(tabMinestuck);
		frogStatueReplica = new Item().setRegistryName("frog_statue_replica").setUnlocalizedName("frogStatueReplica").setCreativeTab(tabMinestuck);
		stoneSlab = new Item().setRegistryName("stone_slab").setUnlocalizedName("stoneSlab").setCreativeTab(tabMinestuck);
		glowystoneDust = new ItemGlowystoneDust().setRegistryName("glowystone_dust").setUnlocalizedName("glowystoneDust").setCreativeTab(tabMinestuck);
		fakeArms = new Item().setRegistryName("fake_arms").setUnlocalizedName("fakeArms").setCreativeTab(null);
		//Music disks
		recordEmissaryOfDance = new ItemMinestuckRecord("emissary", MinestuckSoundHandler.soundEmissaryOfDance).setRegistryName("record_emissary").setUnlocalizedName("record");
		recordDanceStab = new ItemMinestuckRecord("danceStab", MinestuckSoundHandler.soundDanceStabDance).setRegistryName("record_dance_stab").setUnlocalizedName("record");
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		armorPrismarine.repairMaterial = new ItemStack(Items.PRISMARINE_SHARD);

		IForgeRegistry<Item> registry = event.getRegistry();
		registerItemBlock(registry, new ItemMultiTexture(chessTile, chessTile, new String[]{"black", "white", "darkgrey", "lightgrey"}));
		registerItemBlock(registry, new ItemBlock(skaiaPortal));

		registerItemBlock(registry, new ItemMultiTexture(oreCruxite, oreCruxite, new String[0])
		{
			@Override
			public String getUnlocalizedName(ItemStack stack)
			{
				return block.getUnlocalizedName();
			}
		});
		registerItemBlock(registry, new ItemMultiTexture(oreUranium, oreUranium, new String[0])
		{
			@Override
			public String getUnlocalizedName(ItemStack stack)
			{
				return block.getUnlocalizedName();
			}
		});
		registerItemBlock(registry, new ItemBlock(coalOreNetherrack));
		registerItemBlock(registry, new ItemBlock(ironOreSandstone));
		registerItemBlock(registry, new ItemBlock(ironOreSandstoneRed));
		registerItemBlock(registry, new ItemBlock(goldOreSandstone));
		registerItemBlock(registry, new ItemBlock(goldOreSandstoneRed));

		registerItemBlock(registry, new ItemBlockCraftingTab(cruxiteBlock, CreativeTabs.BUILDING_BLOCKS));
		registerItemBlock(registry, new ItemBlock(genericObject));
		registerItemBlock(registry, new ItemSburbMachine(sburbMachine));
		registerItemBlock(registry, new ItemMultiTexture(crockerMachine, crockerMachine,
				(ItemStack input) -> BlockCrockerMachine.MachineType.values()[input.getItemDamage() % BlockCrockerMachine.MachineType.values().length].getUnlocalizedName()));
		registerItemBlock(registry, new ItemBlock(blockComputerOff));
		registerItemBlock(registry, new ItemBlock(transportalizer));

		registerItemBlock(registry, new ItemBlockLayered(layeredSand));
		registerItemBlock(registry, new ItemMultiTexture(coloredDirt, coloredDirt,
				(ItemStack input) -> BlockColoredDirt.BlockType.values()[input.getItemDamage() % BlockColoredDirt.BlockType.values().length].getName()));
		registerItemBlock(registry, new ItemBlock(glowingMushroom));
		registerItemBlock(registry, new ItemBlock(glowingLog));
		registerItemBlock(registry, new ItemBlockCraftingTab(glowingPlanks, CreativeTabs.BUILDING_BLOCKS));
		registerItemBlock(registry, new ItemMultiTexture(stone, stone,
				(ItemStack input) -> BlockMinestuckStone.BlockType.getFromMeta(input.getMetadata()).getUnlocalizedName()));
		registerItemBlock(registry, new ItemBlockCraftingTab(coarseStoneStairs, CreativeTabs.BUILDING_BLOCKS));
		registerItemBlock(registry, new ItemBlockCraftingTab(shadeBrickStairs, CreativeTabs.BUILDING_BLOCKS));
		registerItemBlock(registry, new ItemBlockCraftingTab(frostBrickStairs, CreativeTabs.BUILDING_BLOCKS));
		registerItemBlock(registry, new ItemBlockCraftingTab(castIronStairs, CreativeTabs.BUILDING_BLOCKS));
		registerItemBlock(registry, new ItemMultiTexture(log, log,
				(ItemStack input) -> BlockMinestuckLog.BlockType.values()[input.getItemDamage() % BlockMinestuckLog.BlockType.values().length].getUnlocalizedName()));
		registerItemBlock(registry, new ItemBlock(woodenCactus));
		registerItemBlock(registry, new ItemBlock(sugarCube));
		registerItemBlock(registry, new ItemBlock(appleCake)).setMaxStackSize(1);
		registerItemBlock(registry, new ItemBlock(blueCake)).setMaxStackSize(1);
		registerItemBlock(registry, new ItemBlock(coldCake)).setMaxStackSize(1);
		registerItemBlock(registry, new ItemBlock(redCake)).setMaxStackSize(1);
		registerItemBlock(registry, new ItemBlock(hotCake)).setMaxStackSize(1);

		registerItemBlock(registry, new ItemBlock(primedTnt));
		registerItemBlock(registry, new ItemBlock(unstableTnt));
		registerItemBlock(registry, new ItemBlock(instantTnt));
		registerItemBlock(registry, new ItemBlock(woodenExplosiveButton));
		registerItemBlock(registry, new ItemBlock(stoneExplosiveButton));

		registerItemBlock(registry, new ItemBlock(uraniumCooker));

		//hammers
		registry.register(clawHammer);
		registry.register(sledgeHammer);
		registry.register(blacksmithHammer);
		registry.register(pogoHammer);
		registry.register(telescopicSassacrusher);
		registry.register(regiHammer);
		registry.register(fearNoAnvil);
		registry.register(zillyhooHammer);
		registry.register(popamaticVrillyhoo);
		registry.register(scarletZillyhoo);
		registry.register(mwrthwl);

		//blades
		registry.register(sord);
		registry.register(cactusCutlass);
		registry.register(katana);
		registry.register(unbreakableKatana);
		registry.register(firePoker);
		registry.register(hotHandle);
		registry.register(caledscratch);
		registry.register(caledfwlch);
		registry.register(royalDeringer);
		registry.register(zillywairCutlass);
		registry.register(regisword);
		registry.register(scarletRibbitar);
		registry.register(doggMachete);
		registry.register(cobaltSabre);
		registry.register(quantumSabre);

		//axes
		registry.register(copseCrusher);
		registry.register(blacksmithBane);
		registry.register(scraxe);
		registry.register(qPHammerAxe);
		registry.register(rubyCroak);
		registry.register(hephaestusLumber);
		registry.register(qFHammerAxe);

		//Dice
		registry.register(dice);
		registry.register(fluoriteOctet);
		//misc weapons
		registry.register(catClaws);
		//sickles
		registry.register(sickle);
		registry.register(homesSmellYaLater);
		registry.register(fudgeSickle);
		registry.register(regiSickle);
		registry.register(clawSickle);
		registry.register(candySickle);

		//clubs
		registry.register(deuceClub);
		registry.register(nightClub);
		registry.register(pogoClub);
		registry.register(metalBat);
		registry.register(spikedClub);

		//canes
		registry.register(cane);
		registry.register(ironCane);
		registry.register(spearCane);
		registry.register(regiCane);
		registry.register(dragonCane);
		registry.register(pogoCane);
		registry.register(upStick);
		//Spoons/forks
		registry.register(woodenSpoon);
		registry.register(silverSpoon);
		registry.register(crockerSpork);
		registry.register(skaiaFork);
		registry.register(fork);
		registry.register(spork);

		registry.register(emeraldSword);
		registry.register(emeraldAxe);
		registry.register(emeraldPickaxe);
		registry.register(emeraldShovel);
		registry.register(emeraldHoe);

		//armor
		registry.register(prismarineHelmet);
		registry.register(prismarineChestplate);
		registry.register(prismarineLeggings);
		registry.register(prismarineBoots);

		//food
		registry.register(candy);
		registry.register(beverage);
		registry.register(bugOnAStick);
		registry.register(chocolateBeetle);
		registry.register(coneOfFlies);
		registry.register(grasshopper);
		registry.register(jarOfBugs);
		registry.register(onion);
		registry.register(salad);
		registry.register(irradiatedSteak);
		registry.register(rockCookie);

		//misc
		registry.register(rawCruxite);
		registry.register(rawUranium);
		registry.register(energyCore);
		registry.register(cruxiteDowel);
		registry.register(captchaCard);
		registry.register(cruxiteApple);
		registry.register(cruxitePotion);
		registry.register(disk);
		registry.register(chessboard);
		registry.register(minestuckBucket);
		registry.register(obsidianBucket);
		registry.register(modusCard);
		registry.register(goldSeeds);
		registry.register(metalBoat);
		registry.register(threshDvd);
		registry.register(crewPoster);
		registry.register(sbahjPoster);
		registry.register(carvingTool);
		registry.register(crumplyHat);
		registry.register(frogStatueReplica);
		registry.register(stoneSlab);
		registry.register(glowystoneDust);
		registry.register(fakeArms);

		//Music disks
		registry.register(recordEmissaryOfDance);
		registry.register(recordDanceStab);

		minestuckBucket.addBlock(blockOil.getDefaultState());
		minestuckBucket.addBlock(blockBlood.getDefaultState());
		minestuckBucket.addBlock(blockBrainJuice.getDefaultState());
		/*for(Block block : liquidGrists)
		{
			minestuckBucket.addBlock(block.getDefaultState());
		}*/

		toolUranium.setRepairItem(new ItemStack(rawUranium));
	}

	private static Item registerItemBlock(IForgeRegistry<Item> registry, ItemBlock item)
	{
		registry.register(item.setRegistryName(item.getBlock().getRegistryName()));
		return item;
	}
}
