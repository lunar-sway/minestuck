package com.mraof.minestuck;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import codechicken.nei.asm.NEIModContainer;

import com.mraof.minestuck.block.BlockChessTile;
import com.mraof.minestuck.block.BlockComputerOff;
import com.mraof.minestuck.block.BlockComputerOn;
import com.mraof.minestuck.block.BlockFluidBlood;
import com.mraof.minestuck.block.BlockFluidOil;
import com.mraof.minestuck.block.BlockGatePortal;
import com.mraof.minestuck.block.BlockLayered;
import com.mraof.minestuck.block.BlockMachine;
import com.mraof.minestuck.block.BlockStorage;
import com.mraof.minestuck.block.OreCruxite;
import com.mraof.minestuck.client.ClientProxy;
import com.mraof.minestuck.client.gui.GuiHandler;
//import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.editmode.DeployList;
//import com.mraof.minestuck.editmode.ServerEditHandler;
//import com.mraof.minestuck.entity.EntityDecoy;
import com.mraof.minestuck.entity.carapacian.EntityBlackBishop;
import com.mraof.minestuck.entity.carapacian.EntityBlackPawn;
import com.mraof.minestuck.entity.carapacian.EntityWhiteBishop;
import com.mraof.minestuck.entity.carapacian.EntityWhitePawn;
import com.mraof.minestuck.entity.consort.EntityIguana;
import com.mraof.minestuck.entity.consort.EntityNakagator;
import com.mraof.minestuck.entity.consort.EntitySalamander;
import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.entity.underling.EntityBasilisk;
import com.mraof.minestuck.entity.underling.EntityGiclops;
import com.mraof.minestuck.entity.underling.EntityImp;
import com.mraof.minestuck.entity.underling.EntityOgre;
import com.mraof.minestuck.event.MinestuckFluidHandler;
import com.mraof.minestuck.item.EnumBladeType;
import com.mraof.minestuck.item.EnumCaneType;
import com.mraof.minestuck.item.EnumClubType;
import com.mraof.minestuck.item.EnumHammerType;
import com.mraof.minestuck.item.EnumSickleType;
import com.mraof.minestuck.item.EnumSporkType;
import com.mraof.minestuck.item.ItemBlade;
import com.mraof.minestuck.item.ItemBlockLayered;
import com.mraof.minestuck.item.ItemCane;
import com.mraof.minestuck.item.ItemCardBlank;
import com.mraof.minestuck.item.ItemCardPunched;
import com.mraof.minestuck.item.ItemChessTile;
import com.mraof.minestuck.item.ItemClub;
import com.mraof.minestuck.item.ItemComponent;
import com.mraof.minestuck.item.ItemComputerOff;
import com.mraof.minestuck.item.ItemCruxiteArtifact;
import com.mraof.minestuck.item.ItemCruxiteRaw;
import com.mraof.minestuck.item.ItemDisk;
import com.mraof.minestuck.item.ItemDowelCarved;
import com.mraof.minestuck.item.ItemDowelUncarved;
import com.mraof.minestuck.item.ItemHammer;
import com.mraof.minestuck.item.ItemMachine;
import com.mraof.minestuck.item.ItemMinestuckBucket;
import com.mraof.minestuck.item.ItemSickle;
import com.mraof.minestuck.item.ItemSpork;
import com.mraof.minestuck.item.ItemStorageBlock;
import com.mraof.minestuck.nei.NEIMinestuckConfig;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.skaianet.SessionHandler;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.tileentity.TileEntityGatePortal;
import com.mraof.minestuck.tileentity.TileEntityMachine;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.GristStorage;
import com.mraof.minestuck.util.KindAbstratusList;
import com.mraof.minestuck.util.MinestuckStatsHandler;
import com.mraof.minestuck.util.UpdateChecker;
import com.mraof.minestuck.world.WorldProviderLands;
import com.mraof.minestuck.world.WorldProviderSkaia;
import com.mraof.minestuck.world.gen.OreHandler;
import com.mraof.minestuck.world.gen.structure.StructureCastlePieces;
import com.mraof.minestuck.world.gen.structure.StructureCastleStart;
import com.mraof.minestuck.world.storage.MinestuckSaveHandler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;


@Mod(modid = "Minestuck", name = "Minestuck", version = "@VERSION@")
public class Minestuck
{
	//these ids are in case I need to raise or lower ids for whatever reason
	public static int entityIdStart = 5050;
	public static int skaiaProviderTypeId = 2;
	public static int skaiaDimensionId = 2;
	public static int landProviderTypeId = 3;
	public static int landDimensionIdStart = 3;

	//hammers
	public static Item clawHammer;
	public static Item sledgeHammer;
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
	public static Item crockerSpork;
	public static Item skaiaFork;
	//Other
	public static Item rawCruxite;
	public static Item cruxiteDowel;
	public static Item cruxiteDowelCarved;
	public static Item blankCard;
	public static Item punchedCard;
	public static Item cruxiteArtifact;
	public static Item disk;
	public static Item component;
	public static ItemMinestuckBucket minestuckBucket;


	
	//Blocks
	public static Block chessTile;
	public static Block gatePortal;
	public static Block oreCruxite;
	public static Block blockStorage;
	public static Block blockMachine;
	public static Block blockComputerOn;
	public static Block blockComputerOff;
	
	public static Block blockOil;
	public static Block blockBlood;
	public static Block layeredSand;

	public static Fluid fluidOil;
	public static Fluid fluidBlood;

	
	
	//Client config
	public static int clientOverworldEditRange;	//Edit range used by the client side.
	public static int clientLandEditRange;		//changed by a MinestuckConfigPacket sent by the server on login.
	
	//General
	public static boolean clientHardMode;
	public static boolean hardMode = false;	//Future config option. Currently alters how easy the entry items are accessible after the first time. The machines cost 100 build and there will only be one card if this is true.
	public static boolean generateCruxiteOre; //If set to false, Cruxite Ore will not generate
	public static boolean privateComputers;	//If a player should be able to use other players computers or not.
	public static boolean acceptTitleCollision;	//Allows combinations like "Heir of Hope" and "Seer of Hope" to exist in the same session. Will try to avoid duplicates.
	public static boolean generateSpecialClasses;	//Allow generation of the "Lord" and "Muse" classes.
	public static boolean globalSession;	//Makes only one session possible. Recommended to be true on small servers. Will be ignored when loading a world that already got 2+ sessions.
	public static boolean easyDesignex; //Makes it so you don't need to encode individual cards before combining them.
	public static boolean toolTipEnabled;
	public static boolean forceMaxSize;	//If it should prevent players from joining a session if there is no possible combinations left.
	public static String privateMessage;
	public static int artifactRange; //The range of the Cruxite Artifact in teleporting zones over to the new land
	public static int overworldEditRange;
	public static int landEditRange;

	// The instance of your mod that Forge uses.
	@Instance("Minestuck")
	public static Minestuck instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide="com.mraof.minestuck.client.ClientProxy", serverSide="com.mraof.minestuck.CommonProxy")

	//The proxy to be used by client and server
	public static CommonProxy proxy;
	public static CreativeTabs tabMinestuck;
	public static EnumMap<Side, FMLEmbeddedChannel> channels;

	public int currentEntityIdOffset = 0;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) 
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();

		entityIdStart = config.get("Entity Ids", "entitydIdStart", 5050).getInt(); //The number 5050 might make it seem like this is meant to match up with item/block IDs, but it is not
		skaiaProviderTypeId = config.get("Provider Type Ids", "skaiaProviderTypeId", 2).getInt();
		skaiaDimensionId = config.get("Dimension Ids", "skaiaDimensionId", 2).getInt();
		landProviderTypeId = config.get("Provider Type Ids", "landProviderTypeIdStart", 3).getInt();
		landDimensionIdStart = config.get("Dimension Ids", "landDimensionIdStart", 3).getInt();
		Debug.isDebugMode = config.get("General","printDebugMessages",true).getBoolean(true);
		generateCruxiteOre = config.get("General","generateCruxiteOre",true).getBoolean(true);
		acceptTitleCollision = config.get("General", "acceptTitleCollision", false).getBoolean(false);
		generateSpecialClasses = config.get("General", "generateSpecialClasses", false).getBoolean(false);
		globalSession = config.get("General", "globalSession", true).getBoolean(true);
		privateComputers = config.get("General", "privateComputers", false).getBoolean(false);
		privateMessage = config.get("General", "privateMessage", "You are not allowed to access other players computers.").getString();
		easyDesignex  = config.get("General", "easyDesignex", true).getBoolean(true);
		overworldEditRange = config.get("General", "overWorldEditRange", 26).getInt();
		landEditRange = config.get("General", "landEditRange", 52).getInt();
		artifactRange = config.get("General", "artifcatRange", 30).getInt();
		MinestuckStatsHandler.idOffset = config.get("General", "statisticIdOffset", 413).getInt();
		toolTipEnabled = config.get("General", "toolTipEnabled", false).getBoolean(false);
		hardMode = config.get("General", "hardMode", false).getBoolean(false);
		forceMaxSize = config.get("General", "forceMaxSize", false).getBoolean(false);
		config.save();
		
		(new UpdateChecker()).start();
		
		//Register the Minestuck creative tab
		this.tabMinestuck = new CreativeTabs("tabMinestuck")
		{
			@Override
			public Item getTabIconItem() {
				return zillyhooHammer;
			}
		};
		
		//blocks
		chessTile = GameRegistry.registerBlock(new BlockChessTile(), ItemChessTile.class, "chessTile");
		gatePortal = GameRegistry.registerBlock(new BlockGatePortal(Material.portal), "gatePortal");
		oreCruxite = GameRegistry.registerBlock(new OreCruxite(),"oreCruxite");
		layeredSand = GameRegistry.registerBlock(new BlockLayered(Blocks.sand), "layeredSand").setBlockName("layeredSand");
		//machines
		blockStorage = GameRegistry.registerBlock(new BlockStorage(),ItemStorageBlock.class,"blockStorage");
		blockMachine = GameRegistry.registerBlock(new BlockMachine(), ItemMachine.class,"blockMachine");
		blockComputerOff = GameRegistry.registerBlock(new BlockComputerOff(),"blockComputer");
		blockComputerOn = GameRegistry.registerBlock(new BlockComputerOn(),"blockComputerOn");
		//fluids
		fluidOil = new Fluid("Oil");
		FluidRegistry.registerFluid(fluidOil);
		fluidBlood = new Fluid("Blood");
		FluidRegistry.registerFluid(fluidBlood);
		blockOil = new BlockFluidOil(fluidOil, Material.water);
		blockBlood = new BlockFluidBlood(fluidBlood, Material.water);
		
		//items
		//hammers
		clawHammer = new ItemHammer(EnumHammerType.CLAW);
		sledgeHammer = new ItemHammer(EnumHammerType.SLEDGE);
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
		crockerSpork = new ItemSpork(EnumSporkType.CROCKER);
		skaiaFork = new ItemSpork(EnumSporkType.SKAIA);
		//items
		rawCruxite = new ItemCruxiteRaw();
		cruxiteDowel = new ItemDowelUncarved();
		cruxiteDowelCarved = new ItemDowelCarved();
		blankCard = new ItemCardBlank();
		punchedCard = new ItemCardPunched();
		cruxiteArtifact = new ItemCruxiteArtifact(1, false);
		disk = new ItemDisk();
		component = new ItemComponent();
		minestuckBucket = new ItemMinestuckBucket();
		
		minestuckBucket.addBlock(blockBlood, "BucketBlood");
		minestuckBucket.addBlock(blockOil, "BucketOil");
		
		//registers things for the client
		if(event.getSide().isClient()) {
			ClientProxy.registerSided();
			ClientProxy.registerRenderers();
		}
		
		GameRegistry.registerItem(clawHammer, "clawHammer");
		GameRegistry.registerItem(sledgeHammer, "sledgeHammer");
		GameRegistry.registerItem(pogoHammer, "pogoHammer");
		GameRegistry.registerItem(telescopicSassacrusher, "telescopicSassacrusher");
		GameRegistry.registerItem(fearNoAnvil, "fearNoAnvil");
		GameRegistry.registerItem(zillyhooHammer, "zillyhooHammer");
		GameRegistry.registerItem(popamaticVrillyhoo, "popamaticVrillyhoo");
		GameRegistry.registerItem(scarletZillyhoo, "scarletZillyhoo");
		
		GameRegistry.registerItem(sord, "sord");
		GameRegistry.registerItem(ninjaSword, "ninjaSword");
		GameRegistry.registerItem(katana, "katana");
		GameRegistry.registerItem(caledscratch, "caledscratch");
		GameRegistry.registerItem(royalDeringer, "royalDeringer");
		GameRegistry.registerItem(regisword, "regisword");
		GameRegistry.registerItem(scarletRibbitar, "scarletRibbitar");
		GameRegistry.registerItem(doggMachete, "doggMachete");
		
		GameRegistry.registerItem(sickle, "sickle");
		GameRegistry.registerItem(homesSmellYaLater, "homesSmellYaLater");
		GameRegistry.registerItem(regiSickle, "regiSickle");
		GameRegistry.registerItem(clawSickle, "clawSickle");
		
		GameRegistry.registerItem(deuceClub, "deuceClub");
		
		GameRegistry.registerItem(cane, "cane");
		GameRegistry.registerItem(spearCane, "spearCane");
		GameRegistry.registerItem(dragonCane, "dragonCane");
		
		GameRegistry.registerItem(crockerSpork, "crockerSpork");
		GameRegistry.registerItem(skaiaFork, "skaiaFork");
		
		GameRegistry.registerItem(rawCruxite, "cruxiteRaw");
		GameRegistry.registerItem(cruxiteDowel, "cruxiteDowel");
		GameRegistry.registerItem(cruxiteDowelCarved, "cruxiteDowelCarved");
		GameRegistry.registerItem(blankCard, "blankCard");
		GameRegistry.registerItem(punchedCard, "punchedCard");
		GameRegistry.registerItem(cruxiteArtifact, "cruxiteArtifact");
		GameRegistry.registerItem(disk, "computerDisk");
		GameRegistry.registerItem(component, "component");
		GameRegistry.registerItem(minestuckBucket, "minestuckBucket");
		
		//fluids
		GameRegistry.registerBlock(blockOil, "blockOil");
		GameRegistry.registerBlock(blockBlood, "blockBlood");
		
		MinestuckStatsHandler.prepareAchievementPage();
		
	}

	@EventHandler
	public void load(FMLInitializationEvent event) 
	{
		
		//metadata nonsense to conserve ids
		ItemStack blackChessTileStack = new ItemStack(chessTile, 1, 0);
		ItemStack whiteChessTileStack = new ItemStack(chessTile, 1, 1);
		ItemStack darkGreyChessTileStack = new ItemStack(chessTile, 1, 2);
		ItemStack lightGreyChessTileStack = new ItemStack(chessTile, 1, 3);
		ItemStack cruxiteBlockStack = new ItemStack(blockStorage,1,0);
		ItemStack genericObjectStack = new ItemStack(blockStorage,1,1);
		ItemStack cruxtruderStack = new ItemStack(blockMachine,1,0);
		ItemStack punchDesignexStack = new ItemStack(blockMachine,1,1);
		ItemStack totemLatheStack = new ItemStack(blockMachine,1,2);
		ItemStack alchemiterStack = new ItemStack(blockMachine,1,3);
		ItemStack widgetStack = new ItemStack(blockMachine,1,4);
		ItemStack clientDiskStack = new ItemStack(disk,1,0);
		ItemStack serverDiskStack = new ItemStack(disk,1,1);
		ItemStack woodenSpoonStack = new ItemStack(component,1,0);
		ItemStack silverSpoonStack = new ItemStack(component,1,1);
		ItemStack chessboardStack = new ItemStack(component,1,2);
//		ItemStack bloodBucket = new ItemStack(minestuckBucket, 1, minestuckBucket.FillFluidIds.get(blockBlood.getUnlocalizedName()));
//		ItemStack oilBucket = new ItemStack(minestuckBucket, 1, minestuckBucket.FillFluidIds.get(blockOil.getUnlocalizedName()));
		ItemStack layeredSandStack = new ItemStack(layeredSand);
		//set harvest information for blocks
//		MinecraftForge.setBlockHarvestLevel(chessTile, "shovel", 0);
//		MinecraftForge.setBlockHarvestLevel(oreCruxite, "pickaxe", 1);
//		MinecraftForge.setBlockHarvestLevel(blockStorage, "pickaxe", 1);
//		MinecraftForge.setBlockHarvestLevel(blockMachine, "pickaxe", 1);


		fluidOil.setUnlocalizedName(blockOil.getUnlocalizedName());
		fluidBlood.setUnlocalizedName(blockBlood.getUnlocalizedName());
		
//		Debug.printf("Blood id: %d, Oil id: %d", blockBloodId, blockOilId);
		
		//register entities
		this.registerAndMapEntity(EntitySalamander.class, "Salamander", 0xffe62e, 0xfffb53);
		this.registerAndMapEntity(EntityNakagator.class, "Nakagator", 0xff0000, 0xff6a00);
		this.registerAndMapEntity(EntityIguana.class, "Iguana", 0x0026ff, 0x0094ff);
		this.registerAndMapEntity(EntityImp.class, "Imp", 0x000000, 0xffffff);
		this.registerAndMapEntity(EntityOgre.class, "Ogre", 0x000000, 0xffffff);
		this.registerAndMapEntity(EntityBasilisk.class, "Basilisk", 0x400040, 0xffffff);
		this.registerAndMapEntity(EntityGiclops.class, "Giclops", 0x000000, 0xffffff);
		this.registerAndMapEntity(EntityBlackPawn.class, "dersitePawn", 0x0f0f0f, 0xf0f0f0);
		this.registerAndMapEntity(EntityWhitePawn.class, "prospitianPawn", 0xf0f0f0, 0x0f0f0f);
		this.registerAndMapEntity(EntityBlackBishop.class, "dersiteBishop", 0x000000, 0xc121d9);
		this.registerAndMapEntity(EntityWhiteBishop.class, "prospitianBishop", 0xffffff, 0xfde500);
			//To not register this entity as spawnable using a mob egg.
//		EntityList.addMapping(EntityDecoy.class, "playerDecoy", entityIdStart + currentEntityIdOffset);
//		EntityRegistry.registerModEntity(EntityDecoy.class, "playerDecoy", currentEntityIdOffset, this, 80, 3, true);
		currentEntityIdOffset++;
		
		//register entities with fml
		EntityRegistry.registerModEntity(EntityGrist.class, "grist", currentEntityIdOffset, this, 512, 1, true);

//		EntityRegistry.addSpawn(EntityImp.class, 3, 3, 20, EnumCreatureType.monster, BiomeGenBase.getBiomeGenArray());
//		EntityRegistry.addSpawn(EntityOgre.class, 2, 1, 1, EnumCreatureType.monster, BiomeGenBase.getBiomeGenArray());
//		EntityRegistry.addSpawn(EntityBasilisk.class, 1, 1, 1, EnumCreatureType.monster, BiomeGenBase.getBiomeGenArray());
//		EntityRegistry.addSpawn(EntityGiclops.class, 1, 1, 1, EnumCreatureType.monster, BiomeGenBase.getBiomeGenArray());

		//register Tile Entities
		GameRegistry.registerTileEntity(TileEntityGatePortal.class, "gatePortal");
		GameRegistry.registerTileEntity(TileEntityMachine.class, "containerMachine");
		GameRegistry.registerTileEntity(TileEntityComputer.class, "computerSburb");
		//register world generators
		DimensionManager.registerProviderType(skaiaProviderTypeId, WorldProviderSkaia.class, true);
		DimensionManager.registerDimension(skaiaDimensionId, skaiaProviderTypeId);
		DimensionManager.registerProviderType(landProviderTypeId, WorldProviderLands.class, true);
		//Register the player tracker
//		GameRegistry.registerPlayerTracker(new MinestuckPlayerTracker());

		//register ore generation
		if (generateCruxiteOre) {
			OreHandler oreHandler = new OreHandler();
			GameRegistry.registerWorldGenerator(oreHandler, 0);
		}

		//register machine GUIs
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		
		//Register event handlers
		MinecraftForge.EVENT_BUS.register(new MinestuckSaveHandler());
		MinecraftForge.EVENT_BUS.register(new MinestuckFluidHandler());
		FMLCommonHandler.instance().bus().register(MinestuckPlayerTracker.instance);
		if(event.getSide().isClient())
		{
//			MinecraftForge.EVENT_BUS.register(ClientEditHandler.instance);
		}
//		MinecraftForge.EVENT_BUS.register(ServerEditHandler.instance);
		MinecraftForge.EVENT_BUS.register(MinestuckStatsHandler.instance);
		//register channel handler
		channels = 
				NetworkRegistry.
				INSTANCE
				.newChannel(
						"Minestuck",
						MinestuckChannelHandler.
						instance);
		
		//Register structures
		MapGenStructureIO.registerStructure(StructureCastleStart.class, "SkaiaCastle");
		StructureCastlePieces.func_143048_a();

		//register recipes
		AlchemyRecipeHandler.registerVanillaRecipes();
		AlchemyRecipeHandler.registerMinestuckRecipes();
		AlchemyRecipeHandler.registerModRecipes();
		
//		if(event.getSide().isServer())
		
		KindAbstratusList.registerTypes();
		DeployList.registerItems();
		
		SessionHandler.maxSize = acceptTitleCollision?(generateSpecialClasses?168:144):12;
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
	//registers entity with forge and minecraft, and increases currentEntityIdOffset by one in order to prevent id collision
	public void registerAndMapEntity(Class entityClass, String name, int eggColor, int eggSpotColor)
	{
		this.registerAndMapEntity(entityClass, name, eggColor, eggSpotColor, 80, 3, true);
	}
	public void registerAndMapEntity(Class entityClass, String name, int eggColor, int eggSpotColor, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates)
	{
		EntityList.addMapping(entityClass, name, entityIdStart + currentEntityIdOffset, eggColor, eggSpotColor);
		EntityRegistry.registerModEntity(entityClass, name, currentEntityIdOffset, this, trackingRange, updateFrequency, sendsVelocityUpdates);
		currentEntityIdOffset++;
	}

	@EventHandler 
	public void serverAboutToStart(FMLServerAboutToStartEvent event)
	{
		//unregister lands that may not be in this save
		for(Iterator iterator = MinestuckSaveHandler.lands.iterator(); iterator.hasNext(); )
		{
			int dim = ((Number)iterator.next()).intValue();
			if(DimensionManager.isDimensionRegistered(dim))
			{
				DimensionManager.unregisterDimension(dim);
				Debug.print("Server about to start, Unregistering " + dim);
			}
			iterator.remove();
		}
	}
	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		MinestuckSaveHandler.lands.clear();
		File landList = event.getServer().worldServers[0].getSaveHandler().getMapFileFromName("minestuckLandList");
		if (landList != null && landList.exists())
		{
			try {
				DataInputStream dataInputStream = new DataInputStream(new FileInputStream(landList));
				int currentByte;
				while((currentByte = dataInputStream.read()) != -1)
				{
					if(MinestuckSaveHandler.lands.contains((byte)currentByte))
							continue;
					MinestuckSaveHandler.lands.add((byte)currentByte);
					Debug.printf("Found land dimension id of: %d", currentByte);
					
					if(!DimensionManager.isDimensionRegistered(currentByte))
						DimensionManager.registerDimension(currentByte, Minestuck.landProviderTypeId);
				}
				dataInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		SkaianetHandler.loadData(event.getServer().worldServers[0].getSaveHandler().getMapFileFromName("connectionList"));
		
		File gristcache = event.getServer().worldServers[0].getSaveHandler().getMapFileFromName("gristCache");
		if(gristcache.exists()) {
			NBTTagCompound nbt = null;
			try{
				nbt = CompressedStreamTools.readCompressed(new FileInputStream(gristcache));
			} catch(IOException e){
				e.printStackTrace();
			}
			
			GristStorage.readFromNBT(nbt);
			
		}
	}
	
}