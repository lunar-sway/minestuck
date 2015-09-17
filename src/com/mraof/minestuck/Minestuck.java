package com.mraof.minestuck;

import java.util.Random;

import com.mraof.minestuck.block.BlockChessTile;
import com.mraof.minestuck.block.BlockColoredDirt;
import com.mraof.minestuck.block.BlockComputerOff;
import com.mraof.minestuck.block.BlockComputerOn;
import com.mraof.minestuck.block.BlockFluid;
import com.mraof.minestuck.block.BlockGlowingMushroom;
import com.mraof.minestuck.block.BlockGoldSeeds;
import com.mraof.minestuck.block.BlockLayered;
import com.mraof.minestuck.block.BlockMachine;
import com.mraof.minestuck.block.BlockReturnNode;
import com.mraof.minestuck.block.BlockSkaiaPortal;
import com.mraof.minestuck.block.BlockStorage;
import com.mraof.minestuck.block.BlockTransportalizer;
import com.mraof.minestuck.block.OreCruxite;
import com.mraof.minestuck.client.ClientProxy;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.client.util.MinestuckModelManager;
import com.mraof.minestuck.command.CommandCheckLand;
import com.mraof.minestuck.command.GristCommand;
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.editmode.DeployList;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.entity.MinestuckEntities;
import com.mraof.minestuck.event.MinestuckFluidHandler;
import com.mraof.minestuck.event.ServerEventHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.item.ItemCaptchaCard;
import com.mraof.minestuck.item.ItemComponent;
import com.mraof.minestuck.item.ItemCruxiteApple;
import com.mraof.minestuck.item.ItemCruxiteRaw;
import com.mraof.minestuck.item.ItemDisk;
import com.mraof.minestuck.item.ItemDowel;
import com.mraof.minestuck.item.ItemGoldSeeds;
import com.mraof.minestuck.item.ItemMetalBoat;
import com.mraof.minestuck.item.ItemMinestuckAxe;
import com.mraof.minestuck.item.ItemMinestuckBucket;
import com.mraof.minestuck.item.ItemMinestuckPickaxe;
import com.mraof.minestuck.item.ItemModus;
import com.mraof.minestuck.item.ItemObsidianBucket;
import com.mraof.minestuck.item.block.ItemBlockLayered;
import com.mraof.minestuck.item.block.ItemChessTile;
import com.mraof.minestuck.item.block.ItemColoredDirt;
import com.mraof.minestuck.item.block.ItemMachine;
import com.mraof.minestuck.item.block.ItemOreCruxite;
import com.mraof.minestuck.item.block.ItemStorageBlock;
import com.mraof.minestuck.item.weapon.EnumBladeType;
import com.mraof.minestuck.item.weapon.EnumCaneType;
import com.mraof.minestuck.item.weapon.EnumClubType;
import com.mraof.minestuck.item.weapon.EnumHammerType;
import com.mraof.minestuck.item.weapon.EnumSickleType;
import com.mraof.minestuck.item.weapon.EnumSporkType;
import com.mraof.minestuck.item.weapon.EnumDiceType;
import com.mraof.minestuck.item.weapon.ItemBlade;
import com.mraof.minestuck.item.weapon.ItemCane;
import com.mraof.minestuck.item.weapon.ItemClub;
import com.mraof.minestuck.item.weapon.ItemHammer;
import com.mraof.minestuck.item.weapon.ItemSickle;
import com.mraof.minestuck.item.weapon.ItemSpork;
import com.mraof.minestuck.item.weapon.ItemDice;
import com.mraof.minestuck.nei.NEIMinestuckConfig;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.skaianet.SessionHandler;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.tileentity.TileEntityGate;
import com.mraof.minestuck.tileentity.TileEntityMachine;
import com.mraof.minestuck.tileentity.TileEntitySkaiaPortal;
import com.mraof.minestuck.tileentity.TileEntityTransportalizer;
import com.mraof.minestuck.tracker.ConnectionListener;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.ComputerProgram;
import com.mraof.minestuck.util.KindAbstratusList;
import com.mraof.minestuck.util.MinestuckAchievementHandler;
import com.mraof.minestuck.util.SburbClient;
import com.mraof.minestuck.util.SburbServer;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.WorldProviderLands;
import com.mraof.minestuck.world.WorldProviderSkaia;
import com.mraof.minestuck.world.biome.BiomeGenMinestuck;
import com.mraof.minestuck.world.gen.OreHandler;
import com.mraof.minestuck.world.gen.structure.StructureCastlePieces;
import com.mraof.minestuck.world.gen.structure.StructureCastleStart;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.structure.LandStructureHandler;
import com.mraof.minestuck.world.storage.MinestuckSaveHandler;

import codechicken.nei.NEIModContainer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = "Minestuck", name = "Minestuck", version = "@VERSION@", guiFactory = "com.mraof.minestuck.client.gui.MinestuckGuiFactory")
public class Minestuck
{
	
	/**
	 * True only if the minecraft application is client-sided
	 */
	public static boolean isClientRunning;
	/**
	 * True if the minecraft application is server-sided, or if there is an integrated server running
	 */
	public static volatile boolean isServerRunning;
	
	public static int skaiaProviderTypeId;
	public static int skaiaDimensionId;
	public static int landProviderTypeId;
	public static int landDimensionIdStart;
	public static int biomeIdStart;
	
	//hammers
	public static Item clawHammer;
	public static Item sledgeHammer;
	public static Item blacksmith_hammer;
	public static Item pogoHammer;
	public static Item telescopicSassacrusher;
	public static Item fearNoAnvil;
	public static Item zillyhooHammer;
	public static Item popamaticVrillyhoo;
	public static Item scarletZillyhoo;
	//blades
	public static Item sord;
	public static Item ninjaSword;
	public static Item katana;
	public static Item caledscratch;
	public static Item royalDeringer;
	public static Item regisword;
	public static Item scarletRibbitar;
	public static Item doggMachete;
	//Dice
	public static Item dice;
	public static Item fluorite_octet;
	//sickles
	public static Item sickle;
	public static Item homesSmellYaLater;
	public static Item regiSickle;
	public static Item clawSickle;
	//clubs
	public static Item deuceClub;
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
	//Other
	public static Item rawCruxite;
	public static Item cruxiteDowel;
	public static Item captchaCard;
	public static Item cruxiteApple;
	public static Item disk;
	public static Item component;
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
	public static Item prismarine_helmet;
	public static Item prismarine_chestplate;
	public static Item prismarine_leggings;
	public static Item prismarine_boots;
	
	//Blocks
	public static Block chessTile;
	public static BlockColoredDirt coloredDirt;
	public static Block gatePortal;
	public static OreCruxite oreCruxite;
	public static Block blockStorage;
	public static Block blockMachine;
	public static Block blockComputerOn;
	public static Block blockComputerOff;
	public static Block transportalizer;
	public static BlockGoldSeeds blockGoldSeeds;
	public static Block returnNode;
	public static BlockGlowingMushroom glowingMushroom;
	
	public static Block blockOil;
	public static Block blockBlood;
	public static Block blockBrainJuice;
	public static Block layeredSand;
	
	public static Fluid fluidOil;
	public static Fluid fluidBlood;
	public static Fluid fluidBrainJuice;
	
	public static Item.ToolMaterial toolEmerald;
	public static ItemArmor.ArmorMaterial armor_prismarine;
	
	// The instance of your mod that Forge uses.
	@Instance("Minestuck")
	public static Minestuck instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide="com.mraof.minestuck.client.ClientProxy", serverSide="com.mraof.minestuck.CommonProxy")

	//The proxy to be used by client and server
	public static CommonProxy proxy;
	public static CreativeTabs tabMinestuck;

	public static long worldSeed = 0;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) 
	{
		isClientRunning = event.getSide().isClient();
		
		MinestuckConfig.loadConfigFile(event.getSuggestedConfigurationFile(), event.getSide());
		
		//(new UpdateChecker()).start();
		
		//Register the Minestuck creative tab
		tabMinestuck = new CreativeTabs("tabMinestuck")
		{
			@Override
			public Item getTabIconItem() {
				return zillyhooHammer;
			}
		};
		
		//blocks
		chessTile = GameRegistry.registerBlock(new BlockChessTile(), ItemChessTile.class, "chess_tile");
		gatePortal = GameRegistry.registerBlock(new BlockSkaiaPortal(Material.portal), "gate_portal");
		oreCruxite = (OreCruxite) GameRegistry.registerBlock(new OreCruxite(), ItemOreCruxite.class, "ore_cruxite");
		layeredSand = GameRegistry.registerBlock(new BlockLayered(Blocks.sand), ItemBlockLayered.class, "layered_sand").setUnlocalizedName("layeredSand");
		coloredDirt = (BlockColoredDirt) GameRegistry.registerBlock(new BlockColoredDirt(), ItemColoredDirt.class, "colored_dirt").setUnlocalizedName("coloredDirt").setHardness(0.5F);
		blockStorage = GameRegistry.registerBlock(new BlockStorage(),ItemStorageBlock.class,"storage_block");
		blockMachine = GameRegistry.registerBlock(new BlockMachine(), ItemMachine.class,"machine_block");
		blockComputerOff = GameRegistry.registerBlock(new BlockComputerOff(),"computer_standard");
		blockComputerOn = GameRegistry.registerBlock(new BlockComputerOn(), null, "computer_standard_on");
		transportalizer = GameRegistry.registerBlock(new BlockTransportalizer(), "transportalizer");
		blockGoldSeeds = (BlockGoldSeeds) GameRegistry.registerBlock(new BlockGoldSeeds(), null, "gold_seeds");
		returnNode = GameRegistry.registerBlock(new BlockReturnNode(), null, "return_node");
		glowingMushroom = (BlockGlowingMushroom) GameRegistry.registerBlock(new BlockGlowingMushroom(), "glowing_mushroom");
		//fluids
		fluidOil = new Fluid("Oil", new ResourceLocation("minestuck", "blocks/OilStill"), new ResourceLocation("minestuck", "blocks/OilFlowing"));
		FluidRegistry.registerFluid(fluidOil);
		fluidBlood = new Fluid("Blood", new ResourceLocation("minestuck", "blocks/BloodStill"), new ResourceLocation("minestuck", "blocks/BloodFlowing"));
		FluidRegistry.registerFluid(fluidBlood);
		fluidBrainJuice = new Fluid("BrainJuice", new ResourceLocation("minestuck", "blocks/BrainJuiceStill"), new ResourceLocation("minestuck", "blocks/BrainJuiceFlowing"));
		FluidRegistry.registerFluid(fluidBrainJuice);
		blockOil = GameRegistry.registerBlock(new BlockFluid(fluidOil, Material.water).setUnlocalizedName("oil"), null, "block_oil");
		blockBlood = GameRegistry.registerBlock(new BlockFluid(fluidBlood, Material.water).setUnlocalizedName("blood"), null, "block_blood");
		blockBrainJuice = GameRegistry.registerBlock(new BlockFluid(fluidBrainJuice, Material.water).setUnlocalizedName("brainJuice"), null, "block_brain_juice");
		
		//items
		//hammers
		clawHammer = new ItemHammer(EnumHammerType.CLAW);
		sledgeHammer = new ItemHammer(EnumHammerType.SLEDGE);
		blacksmith_hammer = new ItemHammer(EnumHammerType.BLACKSMITH);
		pogoHammer = new ItemHammer(EnumHammerType.POGO);
		telescopicSassacrusher = new ItemHammer(EnumHammerType.TELESCOPIC);
		fearNoAnvil = new ItemHammer(EnumHammerType.FEARNOANVIL);
		zillyhooHammer = new ItemHammer(EnumHammerType.ZILLYHOO);
		popamaticVrillyhoo = new ItemHammer(EnumHammerType.POPAMATIC);
		scarletZillyhoo = new ItemHammer(EnumHammerType.SCARLET);
		//blades
		sord = new ItemBlade(EnumBladeType.SORD);
		ninjaSword = new ItemBlade(EnumBladeType.NINJA);
		katana = new ItemBlade(EnumBladeType.KATANA);
		caledscratch = new ItemBlade(EnumBladeType.CALEDSCRATCH);
		royalDeringer = new ItemBlade(EnumBladeType.DERINGER);
		regisword = new ItemBlade(EnumBladeType.REGISWORD);
		scarletRibbitar = new ItemBlade(EnumBladeType.SCARLET);
		doggMachete = new ItemBlade(EnumBladeType.DOGG);
		//Dice
		dice = new ItemDice(EnumDiceType.DICE);
		fluorite_octet = new ItemDice(EnumDiceType.FLUORITE_OCTET);
		
		//sickles
		sickle = new ItemSickle(EnumSickleType.SICKLE);
		homesSmellYaLater = new ItemSickle(EnumSickleType.HOMES);
		regiSickle = new ItemSickle(EnumSickleType.REGISICKLE);
		clawSickle = new ItemSickle(EnumSickleType.CLAW);
		//clubs
		deuceClub = new ItemClub(EnumClubType.DEUCE);
		//canes
		cane = new ItemCane(EnumCaneType.CANE);
		spearCane = new ItemCane(EnumCaneType.SPEAR);
		dragonCane = new ItemCane(EnumCaneType.DRAGON);
		//Spoons/forks
		woodenSpoon = new ItemSpork(EnumSporkType.SPOON_WOOD);
		silverSpoon = new ItemSpork(EnumSporkType.SPOON_SILVER);
		crockerSpork = new ItemSpork(EnumSporkType.CROCKER);
		skaiaFork = new ItemSpork(EnumSporkType.SKAIA);
		fork = new ItemSpork(EnumSporkType.FORK);
		
		toolEmerald = EnumHelper.addToolMaterial("EMERALD", 3, 1220, 12.0F, 4.0F, 12).setRepairItem(new ItemStack(Items.emerald));
		emeraldSword = new ItemSword(toolEmerald).setUnlocalizedName("swordEmerald").setCreativeTab(tabMinestuck);
		emeraldAxe = new ItemMinestuckAxe(toolEmerald).setUnlocalizedName("hatchetEmerald").setCreativeTab(tabMinestuck);
		emeraldPickaxe = new ItemMinestuckPickaxe(toolEmerald).setUnlocalizedName("pickaxeEmerald").setCreativeTab(tabMinestuck);
		emeraldShovel = new ItemSpade(toolEmerald).setUnlocalizedName("shovelEmerald").setCreativeTab(tabMinestuck);
		emeraldHoe = new ItemHoe(toolEmerald).setUnlocalizedName("hoeEmerald").setCreativeTab(tabMinestuck);
		//armor
		armor_prismarine = EnumHelper.addArmorMaterial("PRISMARINE", "minestuck:prismarine", 20, new int[]{3, 7, 6, 2}, 15);
		armor_prismarine.customCraftingMaterial = Items.prismarine_shard;
		prismarine_helmet = new ItemArmor(armor_prismarine, 0, 0).setUnlocalizedName("helmetPrismarine").setCreativeTab(tabMinestuck);
		prismarine_chestplate = new ItemArmor(armor_prismarine, 0, 1).setUnlocalizedName("chestplatePrismarine").setCreativeTab(tabMinestuck);
		prismarine_leggings = new ItemArmor(armor_prismarine, 0, 2).setUnlocalizedName("leggingsPrismarine").setCreativeTab(tabMinestuck);
		prismarine_boots = new ItemArmor(armor_prismarine, 0, 3).setUnlocalizedName("bootsPrismarine").setCreativeTab(tabMinestuck);
		//misc
		rawCruxite = new ItemCruxiteRaw();
		cruxiteDowel = new ItemDowel();
		captchaCard = new ItemCaptchaCard();
		cruxiteApple = new ItemCruxiteApple();
		disk = new ItemDisk();
		component = new ItemComponent();
		minestuckBucket = new ItemMinestuckBucket();
		obsidianBucket = new ItemObsidianBucket();
		modusCard = new ItemModus();
		goldSeeds = new ItemGoldSeeds();
		metalBoat = new ItemMetalBoat();
		
		minestuckBucket.addBlock(blockOil);
		minestuckBucket.addBlock(blockBlood);
		minestuckBucket.addBlock(blockBrainJuice);
		
		GameRegistry.registerItem(clawHammer, "claw_hammer");
		GameRegistry.registerItem(sledgeHammer, "sledge_hammer");
		GameRegistry.registerItem(blacksmith_hammer, "blacksmith_hammer");
		GameRegistry.registerItem(pogoHammer, "pogo_hammer");
		GameRegistry.registerItem(telescopicSassacrusher, "telescopic_sassacrusher");
		GameRegistry.registerItem(fearNoAnvil, "fear_no_anvil");
		GameRegistry.registerItem(zillyhooHammer, "zillyhoo_hammer");
		GameRegistry.registerItem(popamaticVrillyhoo, "popamatic_vrillyhoo");
		GameRegistry.registerItem(scarletZillyhoo, "scarlet_zillyhoo");
		
		GameRegistry.registerItem(sord, "sord");
		GameRegistry.registerItem(ninjaSword, "ninja_sword");
		GameRegistry.registerItem(katana, "katana");
		GameRegistry.registerItem(caledscratch, "caledscratch");
		GameRegistry.registerItem(royalDeringer, "royal_deringer");
		GameRegistry.registerItem(regisword, "regisword");
		GameRegistry.registerItem(scarletRibbitar, "scarlet_ribbitar");
		GameRegistry.registerItem(doggMachete, "dogg_machete");
		
		GameRegistry.registerItem(dice,"dice");
		GameRegistry.registerItem(fluorite_octet, "fluorite_octet");
		
		GameRegistry.registerItem(sickle, "sickle");
		GameRegistry.registerItem(homesSmellYaLater, "homes_smell_ya_later");
		GameRegistry.registerItem(regiSickle, "regi_sickle");
		GameRegistry.registerItem(clawSickle, "claw_sickle");
		
		GameRegistry.registerItem(deuceClub, "deuce_club");
		
		GameRegistry.registerItem(cane, "cane");
		GameRegistry.registerItem(spearCane, "spear_cane");
		GameRegistry.registerItem(dragonCane, "dragon_cane");
		
		GameRegistry.registerItem(woodenSpoon, "spoon_wood");
		GameRegistry.registerItem(silverSpoon, "spoon_silver");
		GameRegistry.registerItem(crockerSpork, "crocker_spork");
		GameRegistry.registerItem(skaiaFork, "skaia_fork");
		GameRegistry.registerItem(fork, "fork");
		
		GameRegistry.registerItem(emeraldSword, "emerald_sword");
		GameRegistry.registerItem(emeraldAxe, "emerald_axe");
		GameRegistry.registerItem(emeraldPickaxe, "emerald_pickaxe");
		GameRegistry.registerItem(emeraldShovel, "emerald_shovel");
		GameRegistry.registerItem(emeraldHoe, "emerald_hoe");
		
		GameRegistry.registerItem(prismarine_helmet, "prismarine_helmet");
		GameRegistry.registerItem(prismarine_chestplate, "prismarine_chestplate");
		GameRegistry.registerItem(prismarine_leggings, "prismarine_leggings");
		GameRegistry.registerItem(prismarine_boots, "prismarine_boots");
		
		GameRegistry.registerItem(rawCruxite, "cruxite_raw");
		GameRegistry.registerItem(cruxiteDowel, "cruxite_dowel");
		GameRegistry.registerItem(captchaCard, "captcha_card");
		GameRegistry.registerItem(cruxiteApple, "cruxite_artifact");	//TODO change to "cruxite_apple" when there's no risk of messing with existing save files
		GameRegistry.registerItem(disk, "computer_disk");
		GameRegistry.registerItem(component, "component");
		GameRegistry.registerItem(minestuckBucket, "minestuck_bucket");
		GameRegistry.registerItem(obsidianBucket, "bucket_obsidian");
		GameRegistry.registerItem(modusCard, "modus_card");
		GameRegistry.registerItem(goldSeeds, "gold_seeds");
		GameRegistry.registerItem(metalBoat, "metal_boat");
		
		if(isClientRunning)
		{
			ClientProxy.registerSided();
			MinestuckModelManager.registerVariants();
		}
		
		MinestuckAchievementHandler.prepareAchievementPage();
		
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event) 
	{
		
		fluidOil.setUnlocalizedName(blockOil.getUnlocalizedName());
		fluidBlood.setUnlocalizedName(blockBlood.getUnlocalizedName());
		fluidBrainJuice.setUnlocalizedName(blockBrainJuice.getUnlocalizedName());
		MinestuckEntities.registerEntities();
		//register Tile Entities
		GameRegistry.registerTileEntity(TileEntitySkaiaPortal.class, "minstuck.gatePortal");
		GameRegistry.registerTileEntity(TileEntityMachine.class, "minestuck.containerMachine");
		GameRegistry.registerTileEntity(TileEntityComputer.class, "minestuck.computerSburb");
		GameRegistry.registerTileEntity(TileEntityTransportalizer.class, "minestuck.transportalizer");
		GameRegistry.registerTileEntity(TileEntityGate.class, "minestuck.gate");
		//register world generators
		DimensionManager.registerProviderType(skaiaProviderTypeId, WorldProviderSkaia.class, false);
		DimensionManager.registerDimension(skaiaDimensionId, skaiaProviderTypeId);
		DimensionManager.registerProviderType(landProviderTypeId, WorldProviderLands.class, MinestuckConfig.keepDimensionsLoaded);
		
		BiomeGenMinestuck.mediumNormal = new BiomeGenMinestuck(biomeIdStart).setBiomeName("The Medium");
		BiomeGenMinestuck.mediumCold = new BiomeGenMinestuck(biomeIdStart+1).setBiomeName("The Medium (Cold)").setEnableSnow().setTemperatureRainfall(0.0F, 0.5F);
		
		//register ore generation
		OreHandler oreHandler = new OreHandler();
		GameRegistry.registerWorldGenerator(oreHandler, 0);
		
		//register GUI handler
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		
		//Register textures and renders
		if(isClientRunning)
		{
			MinestuckModelManager.registerTextures();
			ClientProxy.registerRenderers();
		}
		
		//Register event handlers
		MinecraftForge.EVENT_BUS.register(new MinestuckSaveHandler());
		MinecraftForge.EVENT_BUS.register(new MinestuckFluidHandler());
		MinecraftForge.EVENT_BUS.register(ServerEditHandler.instance);
		MinecraftForge.EVENT_BUS.register(MinestuckAchievementHandler.instance);
		MinecraftForge.EVENT_BUS.register(MinestuckPlayerTracker.instance);
		
		FMLCommonHandler.instance().bus().register(MinestuckPlayerTracker.instance);
		FMLCommonHandler.instance().bus().register(ServerEditHandler.instance);
		FMLCommonHandler.instance().bus().register(MinestuckChannelHandler.instance);
		FMLCommonHandler.instance().bus().register(new ConnectionListener());
		FMLCommonHandler.instance().bus().register(new ServerEventHandler());
		
		if(event.getSide().isClient())
		{
		//register channel handler
			MinecraftForge.EVENT_BUS.register(ClientEditHandler.instance);
			FMLCommonHandler.instance().bus().register(ClientEditHandler.instance);
			FMLCommonHandler.instance().bus().register(new MinestuckConfig());
		}
		
		//register channel handler
		MinestuckChannelHandler.setupChannel();
		
		//Register structures
		MapGenStructureIO.registerStructure(StructureCastleStart.class, "SkaiaCastle");
		StructureCastlePieces.registerComponents();
		LandStructureHandler.registerStructures();
		
		//register recipes
		AlchemyRecipeHandler.registerVanillaRecipes();
		AlchemyRecipeHandler.registerMinestuckRecipes();
		AlchemyRecipeHandler.registerModRecipes();
		
		LandAspectRegistry.registerLandAspects();
		
		KindAbstratusList.registerTypes();
		DeployList.registerItems();
		
		ComputerProgram.registerProgram(0, SburbClient.class, new ItemStack(disk, 1, 0));	//This idea was kind of bad and should be replaced
		ComputerProgram.registerProgram(1, SburbServer.class, new ItemStack(disk, 1, 1));
		
		SessionHandler.maxSize = 144;//acceptTitleCollision?(generateSpecialClasses?168:144):12;
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) 
	{
		AlchemyRecipeHandler.registerDynamicRecipes();

		//register NEI stuff
		if (Loader.isModLoaded("NotEnoughItems")) {
			NEIModContainer.plugins.add(new NEIMinestuckConfig());
		}
	}

	@EventHandler 
	public void serverAboutToStart(FMLServerAboutToStartEvent event)
	{
		isServerRunning = true;
		AlchemyRecipeHandler.addOrRemoveRecipes(MinestuckConfig.cardRecipe);
		TileEntityTransportalizer.transportalizers.clear();
		DeployList.applyConfigValues(MinestuckConfig.deployConfigurations);
	}
	
	@EventHandler
	public void serverClosed(FMLServerStoppedEvent event)
	{
		MinestuckDimensionHandler.unregisterDimensions();
		isServerRunning = !isClientRunning;
	}
	
	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		
		event.registerServerCommand(new CommandCheckLand());
		event.registerServerCommand(new GristCommand());
		
		worldSeed = event.getServer().worldServers[0].getSeed();
		CaptchaDeckHandler.rand = new Random();
	}
	
	@EventHandler
	public void serverStopping(FMLServerStoppingEvent event)
	{
		ServerEditHandler.onServerStopping();
	}
}
