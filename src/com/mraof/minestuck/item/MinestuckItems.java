package com.mraof.minestuck.item;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.BlockColoredDirt;
import com.mraof.minestuck.block.BlockCrockerMachine;
import com.mraof.minestuck.block.BlockMinestuckLog;
import com.mraof.minestuck.block.BlockMinestuckStone;
import com.mraof.minestuck.entity.item.EntityCrewPoster;
import com.mraof.minestuck.entity.item.EntitySbahjPoster;
import com.mraof.minestuck.item.block.ItemBlockLayered;
import com.mraof.minestuck.item.block.ItemSburbMachine;
import com.mraof.minestuck.item.weapon.*;
import com.mraof.minestuck.util.MinestuckSoundHandler;
import net.minecraft.entity.EntityHanging;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
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
	//hammers
	public static Item clawHammer;
	public static Item sledgeHammer;
	public static Item blacksmithHammer;
	public static Item pogoHammer;
	public static Item telescopicSassacrusher;
	public static Item fearNoAnvil;
	public static Item zillyhooHammer;
	public static Item popamaticVrillyhoo;
	public static Item scarletZillyhoo;
	public static Item mwrthwl;
	//blades
	public static Item sord;
	public static Item cactusCutlass;
	public static Item katana;
	public static Item unbreakableKatana;	//Not actually unbreakable
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
	//axes
	public static Item copseCrusher;
	public static Item blacksmithBane;
	public static Item scraxe;
	public static Item qPHammerAxe;
	public static Item rubyCroak;
	public static Item hephaestusLumber;
	//Dice
	public static Item dice;
	public static Item fluoriteOctet;
	//mic weapons
	public static Item CatClaws;
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
	public static Item spearCane;
	public static Item dragonCane;
	//Spoons/forks
	public static Item woodenSpoon;
	public static Item silverSpoon;
	public static ItemSpork crockerSpork;
	public static Item skaiaFork;
	public static Item fork;
	public static Item spork;
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
	//Other
	public static Item rawCruxite;
	public static Item cruxiteDowel;
	public static Item captchaCard;
	public static ItemCruxiteArtifact cruxiteApple;
	public static Item cruxitePotion;
	public static Item disk;
	public static Item chessboard;
	public static ItemModus modusCard;
	public static ItemMinestuckBucket minestuckBucket;
	public static ItemGoldSeeds goldSeeds;	//This item is pretty much only a joke
	public static ItemMetalBoat metalBoat;
	public static Item obsidianBucket;
	public static Item emeraldSword;
	public static Item emeraldAxe;
	public static Item emeraldPickaxe;
	public static Item emeraldShovel;
	public static Item emeraldHoe;
	public static Item threshDvd;
	public static Item crewPoster;
	public static Item sbahjPoster;
	public static Item recordEmissaryOfDance;
	public static Item recordDanceStab;
	public static Item carvingTool;
	public static Item crumplyHat;
	public static Item frogStatueReplica;
	public static Item stoneSlab;
	public static Item glowystoneDust;
	
	public static Item.ToolMaterial toolEmerald;
	public static ItemArmor.ArmorMaterial armorPrismarine;
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		IForgeRegistry<Item> registry = event.getRegistry();
		registerItemBlock(registry, new ItemMultiTexture(chessTile, chessTile, new String[] {"black", "white", "darkgrey", "lightgrey"}));
		registerItemBlock(registry, new ItemBlock(skaiaPortal));
		
		registerItemBlock(registry, new ItemMultiTexture(oreCruxite, oreCruxite, new String[0])
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
		
		registerItemBlock(registry, new ItemBlock(cruxiteBlock));
		registerItemBlock(registry, new ItemBlock(genericObject));
		registerItemBlock(registry, new ItemSburbMachine(sburbMachine));
		registerItemBlock(registry, new ItemMultiTexture(crockerMachine, crockerMachine, new ItemMultiTexture.Mapper()
				{
					@Override
					public String apply(ItemStack input)
					{
						return BlockCrockerMachine.MachineType.values()[input.getItemDamage()].getUnlocalizedName();
					}
				}));
		registerItemBlock(registry, new ItemBlock(blockComputerOff));
		registerItemBlock(registry, new ItemBlock(transportalizer));
		
		registerItemBlock(registry, new ItemBlockLayered(layeredSand));
		registerItemBlock(registry, new ItemMultiTexture(coloredDirt, coloredDirt, new ItemMultiTexture.Mapper()
				{
					@Override
					public String apply(ItemStack input)
					{
						return BlockColoredDirt.BlockType.values()[input.getItemDamage()].getName();
					}
				}));
		registerItemBlock(registry, new ItemBlock(glowingMushroom));
		registerItemBlock(registry, new ItemBlock(glowingLog));
		registerItemBlock(registry, new ItemBlock(glowingPlanks));
		registerItemBlock(registry, new ItemMultiTexture(stone, stone, new ItemMultiTexture.Mapper()
		{
			@Override
			public String apply(ItemStack input)
			{
				return BlockMinestuckStone.BlockType.getFromMeta(input.getMetadata()).getUnlocalizedName();
			}
		}));
		registerItemBlock(registry, new ItemBlock(coarseStoneStairs));
		registerItemBlock(registry, new ItemBlock(shadeBrickStairs));
		registerItemBlock(registry, new ItemBlock(frostBrickStairs));
		registerItemBlock(registry, new ItemBlock(castIronStairs));
		registerItemBlock(registry, new ItemMultiTexture(log, log, new ItemMultiTexture.Mapper()
		{
			@Override
			public String apply(ItemStack input)
			{
				return BlockMinestuckLog.BlockType.values()[input.getItemDamage()].getUnlocalizedName();
			}
		}));
		registerItemBlock(registry, new ItemBlock(woodenCactus));
		
		registerItemBlock(registry, new ItemBlock(primedTnt));
		registerItemBlock(registry, new ItemBlock(unstableTnt));
		registerItemBlock(registry, new ItemBlock(instantTnt));
		registerItemBlock(registry, new ItemBlock(woodenExplosiveButton));
		registerItemBlock(registry, new ItemBlock(stoneExplosiveButton));
		
		//hammers
		clawHammer = register(registry, new ItemWeapon(131, 4.0D, -2.4D, 10, "clawHammer").setTool("pickaxe", 0, 1.0F).setRegistryName("claw_hammer"));
		sledgeHammer = register(registry, new ItemWeapon(250, 6.0D, -2.8D, 8, "sledgeHammer").setTool("pickaxe", 2, 4.0F).setRegistryName("sledge_hammer"));
		blacksmithHammer = register(registry, new ItemWeapon(450, 7.0D, -2.8D, 10, "blacksmithHammer").setTool("pickaxe", 2, 3.5F).setRegistryName("blacksmith_hammer"));
		pogoHammer = register(registry, new ItemPogoWeapon(400, 7.0D, -2.8D, 8, "pogoHammer", 0.7).setTool("pickaxe", 1, 2.0F).setRegistryName("pogo_hammer"));
		telescopicSassacrusher = register(registry, new ItemWeapon(1024, 9.0D, -2.9D, 15, "telescopicSassacrusher").setTool("pickaxe", 2, 5.0F).setRegistryName("telescopic_sassacrusher"));
		fearNoAnvil = register(registry, new ItemPotionWeapon(2048, 10.0D, -2.8D, 12, "fearNoAnvil", new PotionEffect(MobEffects.SLOWNESS,100,3)).setTool("pickaxe", 3, 7.0F).setRegistryName("fear_no_anvil"));
		zillyhooHammer = register(registry, new ItemWeapon(3000, 11.0D, -2.8D, 30, "zillyhooHammer").setTool("pickaxe", 4, 15.0F).setRegistryName("zillyhoo_hammer"));
		popamaticVrillyhoo = register(registry, new ItemWeapon(3000, 0.0D, -2.8D, 30, "popamaticVrillyhoo").setTool("pickaxe", 4, 15.0F).setRegistryName("popamatic_vrillyhoo"));
		scarletZillyhoo = register(registry, new ItemFireWeapon(2000, 11.0D, -2.8D, 16, "scarletZillyhoo", 50).setTool("pickaxe", 3, 4.0F).setRegistryName("scarlet_zillyhoo"));
		mwrthwl = register(registry, new ItemWeapon(2000, 10.5D, -2.8D, 16, "mwrthwl").setTool("pickaxe", 3, 4.0F).setRegistryName("mwrthwl"));
		
		//blades
		sord = register(registry, new ItemSord(59, 2, -2.4D, 5, "sord").setRegistryName("sord"));
		cactusCutlass = register(registry, new ItemWeapon(104, 4, -2.4D, 10, "cactaceaeCutlass").setRegistryName("cactaceae_cutlass"));
		katana = register(registry, new ItemWeapon(250, 5, -2.4D, 15, "ninjaSword").setRegistryName("katana"));
		unbreakableKatana = register(registry, new ItemWeapon(2200, 7, -2.4D, 20, "katana").setRegistryName("unbreakable_katana"));
		firePoker = register(registry, new ItemFireWeapon(250, 6, -2.4D, 15, "firePoker", 30).setRegistryName("fire_poker"));
		hotHandle = register(registry, new ItemFireWeapon(350, 5, -2.4D, 15, "hotHandle", 10).setRegistryName("too_hot_to_handle"));
		caledscratch = register(registry, new ItemWeapon(1561, 6, -2.4D, 30, "caledscratch").setRegistryName("caledscratch"));
		caledfwlch = register(registry, new ItemWeapon(1025, 6, -2.4D, 30, "caledfwlch").setRegistryName("caledfwlch"));
		royalDeringer = register(registry, new ItemWeapon(1561, 7, -2.4D, 30, "royalDeringer").setRegistryName("royal_deringer"));
		zillywairCutlass = register(registry, new ItemWeapon(2500, 8, -2.4D, 30, "zillywairCutlass").setRegistryName("cutlass_of_zillywair"));
		regisword = register(registry, new ItemWeapon(812, 6, -2.4D, 10, "regisword").setRegistryName("regisword"));
		scarletRibbitar = register(registry, new ItemWeapon(2000, 7, -2.4D, 30, "scarletRibbitar").setRegistryName("scarlet_ribbitar"));
		doggMachete = register(registry, new ItemWeapon(1000, 5, -2.4D, 30, "doggMachete").setRegistryName("dogg_machete"));
		cobaltSabre = register(registry, new ItemFireWeapon(300, 7, -2.4D, 10, "cobaltSabre", 30).setRegistryName("cobalt_sabre"));
		
		//axes
		copseCrusher = register(registry, new ItemWeapon(400, 6.0D, -3.0D, 20, "copseCrusher", -1, 20).setTool("axe", 2, 7.0F).setRegistryName("copse_crusher"));
		blacksmithBane = register(registry, new ItemWeapon(413, 9.0D, -3.0D, 15, "blacksmithBane").setTool("axe", 2, 6.0F).setRegistryName("blacksmith_bane"));
		scraxe = register(registry, new ItemWeapon(500, 10.0D, -3.0D, 20, "scraxe").setTool("axe", 2, 7.0F).setRegistryName("scraxe"));
		qPHammerAxe = register(registry, new ItemPogoWeapon(800, 8.0D, -3.0D, 30, "qPHammerAxe", 0.6).setTool("axe", 2, 7.0F).setTool("pickaxe", 1, 2.0F).setTerminus(-1, 50).setRegistryName("piston_powered_pogo_axehammer"));
		rubyCroak = register(registry, new ItemWeapon(2000, 11.0D, -2.9D, 30, "rubyCroak").setTool("axe", 3, 8.0F).setRegistryName("ruby_croak"));
		hephaestusLumber = register(registry, new ItemFireWeapon(3000, 11.0D, -3.0D, 30, "hephaestusLumber", 30).setTool("axe", 3, 9.0F).setRegistryName("hephaestus_lumberjack"));
		
		//Dice
		dice = register(registry, new ItemWeapon(51, 6, 3, 6, "dice").setRegistryName("dice"));
		fluoriteOctet = register(registry, new ItemWeapon(67, 15, 6, 8, "fluoriteOctet").setRegistryName("fluorite_octet"));
		//misc weapons
		CatClaws = register(registry, new ItemDualWeapon(500, 4.0D, 1.0D, -1.5D,-1.0D, 6, "catclaws").setRegistryName("catclaws"));
		//sickles
		sickle = register(registry, new ItemWeapon(220, 4.0D, -2.4D, 8, "sickle").setRegistryName("sickle"));
		homesSmellYaLater = register(registry, new ItemWeapon(400, 5.5D, -2.4D, 10, "homesSmellYaLater").setRegistryName("homes_smell_ya_later"));
		fudgeSickle = register(registry, new ItemWeapon(450, 5.5D, -2.4D, 10, "fudgeSickle").setRegistryName("fudgesickle"));
		regiSickle = register(registry, new ItemWeapon(812, 6.0D, -2.4D, 5, "regiSickle").setRegistryName("regisickle"));
		clawSickle = register(registry, new ItemWeapon(2048, 7.0D, -2.4D, 15, "clawSickle").setRegistryName("claw_sickle"));
		candySickle = register(registry, new ItemCandyWeapon(96, 6.0D, -2.4D, 15, "candySickle").setRegistryName("candy_sickle"));

		//clubs
		deuceClub = register(registry, new ItemWeapon(1024, 2.5D, -2.2D, 15, "deuceClub").setRegistryName("deuce_club"));
		nightClub = register(registry, new ItemWeapon(600, 4.0D, -2.2D, 20, "nightClub").setRegistryName("nightclub"));
		pogoClub = register(registry, new ItemPogoWeapon(600, 3.5D, -2.2D, 15, "pogoClub", 0.5).setRegistryName("pogo_club"));
		metalBat = register(registry, new ItemWeapon(750, 5.0D, -2.2D, 5, "metalBat").setRegistryName("metal_bat"));
		spikedClub = register(registry, new ItemWeapon(500, 5.5D, -2.2D, 5, "spikedClub").setRegistryName("spiked_club"));

		//canes
		cane = register(registry, new ItemWeapon(100, 2.0D, -2.0D, 15, "cane").setRegistryName("cane"));
		spearCane = register(registry, new ItemWeapon(300, 4.0D, -2.0D, 13, "spearCane").setRegistryName("spear_cane"));
		dragonCane = register(registry, new ItemWeapon(300, 6.5D, -2.0D, 20, "dragonCane").setRegistryName("dragon_cane"));
		
		//Spoons/forks
		woodenSpoon = register(registry, new ItemWeapon(59, 2.0D, -2.2D, 5, "woodenSpoon").setRegistryName("wooden_spoon"));
		silverSpoon = register(registry, new ItemWeapon(250, 2.5D, -2.2D, 12, "silverSpoon").setRegistryName("silver_spoon"));
		crockerSpork = (ItemSpork) register(registry, new ItemSpork(512, 4.0D, -2.2D, 15, "crocker").setRegistryName("crocker_spork"));
		skaiaFork = register(registry, new ItemWeapon(2048, 8.5D, -2.2D, 10, "skaiaFork").setRegistryName("skaia_fork"));
		fork = register(registry, new ItemWeapon(100, 4.0D, -2.2D, 3, "fork").setRegistryName("fork"));
		spork = register(registry, new ItemWeapon(120, 4.5D, -2.3D, 5, "spork").setRegistryName("spork"));
		
		toolEmerald = EnumHelper.addToolMaterial("EMERALD", 3, 1220, 12.0F, 4.0F, 12).setRepairItem(new ItemStack(Items.EMERALD));
		emeraldSword = register(registry, new ItemSword(toolEmerald).setRegistryName("emerald_sword")).setUnlocalizedName("swordEmerald").setCreativeTab(Minestuck.tabMinestuck);
		emeraldAxe = register(registry, new ItemMinestuckAxe(toolEmerald, 9.0F, -3.0F).setRegistryName("emerald_axe")).setUnlocalizedName("hatchetEmerald").setCreativeTab(Minestuck.tabMinestuck);
		emeraldPickaxe = register(registry, new ItemMinestuckPickaxe(toolEmerald).setRegistryName("emerald_pickaxe")).setUnlocalizedName("pickaxeEmerald").setCreativeTab(Minestuck.tabMinestuck);
		emeraldShovel = register(registry, new ItemSpade(toolEmerald).setRegistryName("emerald_shovel")).setUnlocalizedName("shovelEmerald").setCreativeTab(Minestuck.tabMinestuck);
		emeraldHoe = register(registry, new ItemHoe(toolEmerald).setRegistryName("emerald_hoe")).setUnlocalizedName("hoeEmerald").setCreativeTab(Minestuck.tabMinestuck);
		
		//armor
		armorPrismarine = EnumHelper.addArmorMaterial("PRISMARINE", "minestuck:prismarine", 20, new int[]{3, 7, 6, 2}, 15, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
		armorPrismarine.repairMaterial = new ItemStack(Items.PRISMARINE_SHARD);
		prismarineHelmet = register(registry, new ItemArmor(armorPrismarine, 0, EntityEquipmentSlot.HEAD).setRegistryName("prismarine_helmet")).setUnlocalizedName("helmetPrismarine").setCreativeTab(Minestuck.tabMinestuck);
		prismarineChestplate = register(registry, new ItemArmor(armorPrismarine, 0, EntityEquipmentSlot.CHEST).setRegistryName("prismarine_chestplate")).setUnlocalizedName("chestplatePrismarine").setCreativeTab(Minestuck.tabMinestuck);
		prismarineLeggings = register(registry, new ItemArmor(armorPrismarine, 0, EntityEquipmentSlot.LEGS).setRegistryName("prismarine_leggings")).setUnlocalizedName("leggingsPrismarine").setCreativeTab(Minestuck.tabMinestuck);
		prismarineBoots = register(registry, new ItemArmor(armorPrismarine, 0, EntityEquipmentSlot.FEET).setRegistryName("prismarine_boots")).setUnlocalizedName("bootsPrismarine").setCreativeTab(Minestuck.tabMinestuck);
		
		//food
		candy = register(registry, new ItemMinestuckCandy().setRegistryName("candy"));
		beverage = register(registry, new ItemMinestuckBeverage().setRegistryName("beverage"));
		bugOnAStick = register(registry, new ItemFood(1, 0.1F, false).setRegistryName("bug_on_stick")).setUnlocalizedName("bugOnAStick").setCreativeTab(Minestuck.tabMinestuck);
		chocolateBeetle = register(registry, new ItemFood(3, 0.4F, false).setRegistryName("chocolate_beetle")).setUnlocalizedName("chocolateBeetle").setCreativeTab(Minestuck.tabMinestuck);
		coneOfFlies = register(registry, new ItemFood(2, 0.1F, false).setRegistryName("cone_of_flies")).setUnlocalizedName("coneOfFlies").setCreativeTab(Minestuck.tabMinestuck);
		grasshopper = register(registry, new ItemFood(4, 0.5F, false).setRegistryName("grasshopper")).setUnlocalizedName("grasshopper").setCreativeTab(Minestuck.tabMinestuck);
		jarOfBugs = register(registry, new ItemFood(3, 0.2F, false).setRegistryName("jar_of_bugs")).setUnlocalizedName("jarOfBugs").setCreativeTab(Minestuck.tabMinestuck);
		onion = register(registry, new ItemFood(2, 0.2F, false).setRegistryName("onion")).setUnlocalizedName("onion").setCreativeTab(Minestuck.tabMinestuck);
		salad = register(registry, new ItemSoup(1).setRegistryName("salad").setUnlocalizedName("salad").setCreativeTab(Minestuck.tabMinestuck));
		
		//misc
		rawCruxite = register(registry, new Item().setRegistryName("raw_cruxite")).setUnlocalizedName("rawCruxite").setCreativeTab(Minestuck.tabMinestuck);
		cruxiteDowel = register(registry, new ItemDowel().setRegistryName("cruxite_dowel"));
		captchaCard = register(registry, new ItemCaptchaCard().setRegistryName("captcha_card"));
		cruxiteApple = (ItemCruxiteArtifact) register(registry, new ItemCruxiteApple().setRegistryName("cruxite_apple"));
		cruxitePotion = register(registry, new ItemCruxitePotion().setRegistryName("cruxite_potion"));
		disk = register(registry, new ItemDisk().setRegistryName("computer_disk"));
		chessboard = register(registry, new Item().setRegistryName("chessboard")).setUnlocalizedName("chessboard").setMaxStackSize(1).setCreativeTab(Minestuck.tabMinestuck);
		minestuckBucket = (ItemMinestuckBucket) register(registry, new ItemMinestuckBucket().setRegistryName("minestuck_bucket"));
		obsidianBucket = register(registry, new ItemObsidianBucket().setRegistryName("bucket_obsidian"));
		modusCard = (ItemModus) register(registry, new ItemModus().setRegistryName("modus_card"));
		goldSeeds = (ItemGoldSeeds) register(registry, new ItemGoldSeeds().setRegistryName("gold_seeds"));
		metalBoat = (ItemMetalBoat) register(registry, new ItemMetalBoat().setRegistryName("metal_boat"));
		threshDvd = register(registry, new Item().setRegistryName("thresh_dvd")).setUnlocalizedName("threshDvd").setMaxStackSize(1).setCreativeTab(Minestuck.tabMinestuck);
		crewPoster = register(registry, new ItemHanging(){
			@Override
			public EntityHanging createEntity(World worldIn, BlockPos pos, EnumFacing facing, ItemStack stack)
			{
				return new EntityCrewPoster(worldIn, pos, facing);
			}
		}.setRegistryName("crew_poster")).setUnlocalizedName("crewPoster").setMaxStackSize(1).setCreativeTab(Minestuck.tabMinestuck);
		sbahjPoster = register(registry, new ItemHanging(){
			@Override
			public EntityHanging createEntity(World worldIn, BlockPos pos, EnumFacing facing, ItemStack stack)
			{
				return new EntitySbahjPoster(worldIn, pos, facing);
			}
		}.setRegistryName("sbahj_poster")).setUnlocalizedName("sbahjPoster").setMaxStackSize(1).setCreativeTab(Minestuck.tabMinestuck);
		carvingTool = register(registry, new Item().setRegistryName("carving_tool")).setUnlocalizedName("carvingTool").setMaxStackSize(1).setCreativeTab(Minestuck.tabMinestuck);
		crumplyHat = register(registry, new Item().setRegistryName("crumply_hat")).setUnlocalizedName("crumplyHat").setMaxStackSize(1).setCreativeTab(Minestuck.tabMinestuck);
		frogStatueReplica = register(registry, new Item().setRegistryName("frog_statue_replica")).setUnlocalizedName("frogStatueReplica").setCreativeTab(Minestuck.tabMinestuck);
		stoneSlab = register(registry, new Item().setRegistryName("stone_slab")).setUnlocalizedName("stoneSlab").setCreativeTab(Minestuck.tabMinestuck);
		glowystoneDust = register(registry, new ItemGlowystoneDust().setRegistryName("glowystone_dust")).setUnlocalizedName("glowystoneDust").setCreativeTab(Minestuck.tabMinestuck);
		
		//Music disks
		recordEmissaryOfDance = register(registry, new ItemMinestuckRecord("emissary", MinestuckSoundHandler.soundEmissaryOfDance).setRegistryName("record_emissary").setUnlocalizedName("record"));
		recordDanceStab = register(registry, new ItemMinestuckRecord("danceStab", MinestuckSoundHandler.soundDanceStabDance).setRegistryName("record_dance_stab").setUnlocalizedName("record"));
		
		minestuckBucket.addBlock(blockOil.getDefaultState());
		minestuckBucket.addBlock(blockBlood.getDefaultState());
		minestuckBucket.addBlock(blockBrainJuice.getDefaultState());
		/*for(Block block : liquidGrists)
		{
			minestuckBucket.addBlock(block.getDefaultState());
		}*/
	}
	
	private static Item register(IForgeRegistry<Item> registry, Item item)
	{
		registry.register(item);
		return item;
	}
	
	private static Item registerItemBlock(IForgeRegistry<Item> registry, ItemBlock item)
	{
		registry.register(item.setRegistryName(item.getBlock().getRegistryName()));
		return item;
	}
}
