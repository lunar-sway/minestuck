package com.mraof.minestuck;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
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
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.editmode.DeployList;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.entity.EntityDecoy;
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
import com.mraof.minestuck.item.ItemBlockLayered;
import com.mraof.minestuck.item.ItemMachine;
import com.mraof.minestuck.item.ItemMinestuckBucket;
import com.mraof.minestuck.item.ItemSickle;
import com.mraof.minestuck.item.ItemSpork;
import com.mraof.minestuck.item.ItemStorageBlock;
import com.mraof.minestuck.nei.NEIMinestuckConfig;
import com.mraof.minestuck.network.MinestuckConnectionHandler;
import com.mraof.minestuck.network.MinestuckPacketHandler;
import com.mraof.minestuck.network.skaianet.SessionHandler;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.tileentity.TileEntityGatePortal;
import com.mraof.minestuck.tileentity.TileEntityMachine;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.ComputerProgram;
import com.mraof.minestuck.util.MinestuckStatsHandler;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.GristStorage;
import com.mraof.minestuck.util.GristType;
import com.mraof.minestuck.util.KindAbstratusList;
import com.mraof.minestuck.util.SburbClient;
import com.mraof.minestuck.util.SburbServer;
import com.mraof.minestuck.util.UpdateChecker;
import com.mraof.minestuck.world.WorldProviderLands;
import com.mraof.minestuck.world.WorldProviderSkaia;
import com.mraof.minestuck.world.gen.OreHandler;
import com.mraof.minestuck.world.gen.structure.StructureCastlePieces;
import com.mraof.minestuck.world.gen.structure.StructureCastleStart;
import com.mraof.minestuck.world.storage.MinestuckSaveHandler;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "Minestuck", name = "Minestuck", version = "@VERSION@")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, packetHandler = MinestuckPacketHandler.class, channels = {"Minestuck"})
public class Minestuck
{
	//these ids are in case I need to raise or lower ids for whatever reason
	public static int toolIdStart = 5001;
	public static int entityIdStart = 5050;
	public static int blockIdStart = 500;
	public static int itemIdStart = 6001;
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

	//hammers
	public static int clawHammerId;
	public static int sledgeHammerId;
	public static int pogoHammerId;
	public static int telescopicSassacrusherId;
	public static int fearNoAnvilId;
	public static int zillyhooHammerId;
	public static int popamaticVrillyhooId;
	public static int scarletZillyhooId;
	//blades
	public static int sordId;
	public static int ninjaSwordId;
	public static int katanaId;
	public static int caledscratchId;
	public static int royalDeringerId;
	public static int regiswordId;
	public static int scarletRibbitarId;
	public static int doggMacheteId;
	//sickles
	public static int sickleId;
	public static int homesSmellYaLaterId;
	public static int regiSickleId;
	public static int clawSickleId;
	//clubs
	public static int deuceClubId;
	//canes
	public static int caneId;
	public static int spearCaneId;
	public static int dragonCaneId;
	//Spoons/forks
	public static int crockerSporkId;
	public static int skaiaForkId;
	//Other
	public static int rawCruxiteId;
	public static int cruxiteDowelId;
	public static int cruxiteDowelCarvedId;
	public static int blankCardId;
	public static int punchedCardId;
	public static int cruxiteArtifactId;
	public static int diskId;
	public static int componentId;
	public static int minestuckBucketId;
	
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

	//Block IDs
	public static int chessTileId;
	public static int gatePortalId;
	public static int oreCruxiteId;
	public static int blockStorageId;
	public static int blockMachineId;
	public static int blockComputerOnId;
	public static int blockComputerOffId;
	public static int blockOilId;
	public static int blockBloodId;
	public static int layeredSandId;
	
	
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
	/**
	 * 0: Make the player's new server player his/her old server player's server player
	 * 1: The player that lost his/her server player will have an idle main connection until someone without a client player connects to him/her.
	 * (Will try to put a better explanation somewhere else later)
	 */
	public static int escapeFailureMode;	//What will happen if someone's server player fails to escape the overworld in time,

	// The instance of your mod that Forge uses.
	@Instance("Minestuck")
	public static Minestuck instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide="com.mraof.minestuck.client.ClientProxy", serverSide="com.mraof.minestuck.CommonProxy")

	//The proxy to be used by client and server
	public static CommonProxy proxy;
	public static CreativeTabs tabMinestuck;

	public int currentEntityIdOffset = 0;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) 
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();

		blockIdStart = config.get("Block Ids", "blockIdStart", 500).getInt();
		chessTileId = config.get("Block Ids", "chessTileId", blockIdStart).getInt();
		gatePortalId = config.get("Block Ids", "gatePortalId", blockIdStart + 1).getInt();
		oreCruxiteId = config.get("Block Ids", "oreCruxiteId", blockIdStart + 2).getInt();
		blockStorageId = config.get("Block Ids", "blockStorageId", blockIdStart + 3).getInt();
		blockMachineId = config.get("Block Ids", "blockMachineId", blockIdStart + 4).getInt();
		blockComputerOffId = config.get("Block Ids", "blockComputerOffId", blockIdStart + 5).getInt();
		blockComputerOnId = config.get("Block Ids", "blockComputerOnId", blockIdStart + 6).getInt();
		blockOilId = config.get("Block Ids", "blockOilId", blockIdStart + 7).getInt();
		blockBloodId = config.get("Block Ids", "blockBloodId", blockIdStart + 8).getInt();
		layeredSandId = config.get("Block Ids", "layeredSandId", blockIdStart + 9).getInt();
		if(config.get("Block Ids", "useBlockIdStart", true).getBoolean(true))
		{
			chessTileId = blockIdStart;
			gatePortalId = blockIdStart + 1;
			oreCruxiteId = blockIdStart + 2;
			blockStorageId = blockIdStart + 3;
			blockMachineId = blockIdStart + 4;
			blockComputerOffId = blockIdStart + 5;
			blockComputerOnId = blockIdStart + 6;
			blockOilId = blockIdStart + 7;
			blockBloodId = blockIdStart + 8;
			layeredSandId = blockIdStart + 9;
		}
		//Debug.printf("Fluid Block Ids loaded, Blood id: %d, Oil id: %d", blockBloodId, blockOilId);

		toolIdStart = config.get("Item Ids", "toolIdStart", 5001).getInt();
		clawHammerId = config.get("Item Ids", "clawHammerId", toolIdStart).getInt();
		sledgeHammerId = config.get("Item Ids", "sledgeHammerId", toolIdStart + 1).getInt();
		pogoHammerId = config.get("Item Ids", "pogoHammerId", toolIdStart + 2).getInt();
		telescopicSassacrusherId = config.get("Item Ids", "telescopicSassacrusherId", toolIdStart + 3).getInt();
		fearNoAnvilId = config.get("Item Ids", "fearNoAnvilId", toolIdStart + 4).getInt();
		zillyhooHammerId = config.get("Item Ids", "zillyhooHammerId", toolIdStart + 5).getInt();
		popamaticVrillyhooId = config.get("Item Ids", "popamaticVrillyhooId", toolIdStart + 6).getInt();
		scarletZillyhooId = config.get("Item Ids", "scarletZillyhooId", toolIdStart + 7).getInt();
		sordId = config.get("Item Ids", "sordId", toolIdStart + 8).getInt();
		ninjaSwordId = config.get("Item Ids", "ninjaSwordId", toolIdStart + 9).getInt();
		katanaId = config.get("Item Ids", "katanaId", toolIdStart + 10).getInt();
		caledscratchId = config.get("Item Ids", "caledscratchId", toolIdStart + 11).getInt();
		royalDeringerId = config.get("Item Ids", "royalDeringerId", toolIdStart + 12).getInt();
		regiswordId = config.get("Item Ids", "regiswordId", toolIdStart + 13).getInt();
		scarletRibbitarId = config.get("Item Ids", "scarletRibbitarId", toolIdStart + 14).getInt();
		doggMacheteId = config.get("Item Ids", "doggMacheteId", toolIdStart + 15).getInt();
		sickleId = config.get("Item Ids", "sickleId", toolIdStart + 16).getInt();
		homesSmellYaLaterId = config.get("Item Ids", "homesSmellYaLaterId", toolIdStart + 17).getInt();
		regiSickleId = config.get("Item Ids", "regiSickleId", toolIdStart + 18).getInt();
		clawSickleId = config.get("Item Ids", "clawSickleId", toolIdStart + 19).getInt();
		deuceClubId = config.get("Item Ids", "deuceClubId", toolIdStart + 20).getInt();
		caneId = config.get("Item Ids", "caneId", toolIdStart + 21).getInt();
		spearCaneId = config.get("Item Ids", "spearCaneId", toolIdStart + 22).getInt();
		dragonCaneId = config.get("Item Ids", "dragonCaneId", toolIdStart + 23).getInt();
		crockerSporkId = config.get("Item Ids", "crockerSporkId", toolIdStart + 24).getInt();
		skaiaForkId = config.get("Item Ids", "skaiaForkId", toolIdStart + 25).getInt();

		if(config.get("Item Ids", "useToolIdStart", true).getBoolean(true))
		{
			clawHammerId = toolIdStart;
			sledgeHammerId = toolIdStart + 1;
			pogoHammerId = toolIdStart + 2;
			telescopicSassacrusherId = toolIdStart + 3;
			fearNoAnvilId = toolIdStart + 4;
			zillyhooHammerId = toolIdStart + 5;
			popamaticVrillyhooId = toolIdStart + 6;
			scarletZillyhooId = toolIdStart + 7;
			sordId = toolIdStart + 8;
			ninjaSwordId = toolIdStart + 9;
			katanaId = toolIdStart + 10;
			caledscratchId = toolIdStart + 11;
			royalDeringerId = toolIdStart + 12;
			regiswordId = toolIdStart + 13;
			scarletRibbitarId = toolIdStart + 14;
			doggMacheteId = toolIdStart + 15;
			sickleId = toolIdStart + 16;
			homesSmellYaLaterId = toolIdStart + 17;
			regiSickleId = toolIdStart + 18;
			clawSickleId = toolIdStart + 19;
			deuceClubId = toolIdStart + 20;
			caneId = toolIdStart + 21;
			spearCaneId = toolIdStart + 22;
			dragonCaneId = toolIdStart + 23;
			crockerSporkId = toolIdStart + 24;
			skaiaForkId = toolIdStart + 25;
		}

		itemIdStart = config.get("Item Ids", "itemIdStart", 6001).getInt();
		rawCruxiteId = config.get("Item Ids", "rawCruxiteId", itemIdStart).getInt();
		cruxiteDowelId = config.get("Item Ids", "cruxiteDowelId", itemIdStart + 1).getInt();
		cruxiteDowelCarvedId = config.get("Item Ids", "cruxiteDowelCarvedId", itemIdStart + 2).getInt();
		blankCardId = config.get("Item Ids", "blankCardId", itemIdStart + 3).getInt();
		punchedCardId = config.get("Item Ids", "punchedCardId", itemIdStart + 4).getInt();
		cruxiteArtifactId = config.get("Item Ids", "cruxiteArtifactId", itemIdStart + 5).getInt();
		diskId = config.get("Item Ids", "diskId", itemIdStart + 6).getInt();
		componentId = config.get("Item Ids", "componentId", itemIdStart + 7).getInt();
		minestuckBucketId = config.get("Item Ids", "minestuckBucketId", itemIdStart + 8).getInt();
		if(config.get("Item Ids", "useItemIdStart", true).getBoolean(true));
		{
			rawCruxiteId = itemIdStart;
			cruxiteDowelId = itemIdStart + 1;
			cruxiteDowelCarvedId = itemIdStart + 2;
			blankCardId = itemIdStart + 3;
			punchedCardId = itemIdStart + 4;
			cruxiteArtifactId = itemIdStart + 5;
			diskId = itemIdStart + 6;
			componentId = itemIdStart + 7;	
			minestuckBucketId = itemIdStart + 8;			
		}

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
		overworldEditRange = config.get("General", "overWorldEditRange", 15).getInt();
		landEditRange = config.get("General", "landEditRange", 30).getInt();	//Now radius
		artifactRange = config.get("General", "artifactRange", 30).getInt();
		MinestuckStatsHandler.idOffset = config.get("General", "statisticIdOffset", 413).getInt();
		hardMode = config.get("General", "hardMode", false).getBoolean(false);
		forceMaxSize = config.get("General", "forceMaxSize", false).getBoolean(false);
		escapeFailureMode = config.get("General", "escapeFailureMode", 0).getInt();
		if(escapeFailureMode > 2 || escapeFailureMode < 0)
			escapeFailureMode = 0;
		if(event.getSide().isClient()) {	//Client sided config values
			toolTipEnabled = config.get("General", "editModeToolTip", false).getBoolean(false);
		}
		config.save();
		
		MinestuckStatsHandler.prepareAchievementPage();
		
		(new UpdateChecker()).start();
	}

	@EventHandler
	public void load(FMLInitializationEvent event) 
	{
		//Register the Minestuck creative tab
		this.tabMinestuck = new CreativeTabs("tabMinestuck")
		{
			public ItemStack getIconItemStack() 
			{
				return new ItemStack(zillyhooHammer,1);
			}
		};

		//blocks
		chessTile = new BlockChessTile(chessTileId);
		gatePortal = new BlockGatePortal(gatePortalId, Material.portal);
		oreCruxite = new OreCruxite(oreCruxiteId);
		layeredSand = new BlockLayered(layeredSandId, Block.sand).setUnlocalizedName("layeredSand");
		//machines
		blockStorage = new BlockStorage(blockStorageId);
		blockMachine = new BlockMachine(blockMachineId);
		blockComputerOff = new BlockComputerOff(blockComputerOffId);
		blockComputerOn = new BlockComputerOn(blockComputerOnId);
		//fluids
		fluidOil = new Fluid("Oil").setBlockID(blockOilId);
		FluidRegistry.registerFluid(fluidOil);
		fluidBlood = new Fluid("Blood").setBlockID(blockBloodId);
		FluidRegistry.registerFluid(fluidBlood);
		blockOil = new BlockFluidOil(blockOilId, fluidOil, Material.water);
		blockBlood = new BlockFluidBlood(blockBloodId, fluidBlood, Material.water);
		
		//items
		//hammers
		clawHammer = new ItemHammer(clawHammerId, EnumHammerType.CLAW);
		sledgeHammer = new ItemHammer(sledgeHammerId, EnumHammerType.SLEDGE);
		pogoHammer = new ItemHammer(pogoHammerId, EnumHammerType.POGO);
		telescopicSassacrusher = new ItemHammer(telescopicSassacrusherId, EnumHammerType.TELESCOPIC);
		fearNoAnvil = new ItemHammer(fearNoAnvilId, EnumHammerType.FEARNOANVIL);
		zillyhooHammer = new ItemHammer(zillyhooHammerId, EnumHammerType.ZILLYHOO);
		popamaticVrillyhoo = new ItemHammer(popamaticVrillyhooId, EnumHammerType.POPAMATIC);
		scarletZillyhoo = new ItemHammer(scarletZillyhooId, EnumHammerType.SCARLET);
		//blades
		sord = new ItemBlade(sordId, EnumBladeType.SORD);
		ninjaSword = new ItemBlade(ninjaSwordId, EnumBladeType.NINJA);
		katana = new ItemBlade(katanaId, EnumBladeType.KATANA);
		caledscratch = new ItemBlade(caledscratchId, EnumBladeType.CALEDSCRATCH);
		royalDeringer = new ItemBlade(royalDeringerId, EnumBladeType.DERINGER);
		regisword = new ItemBlade(regiswordId, EnumBladeType.REGISWORD);
		scarletRibbitar = new ItemBlade(scarletRibbitarId, EnumBladeType.SCARLET);
		doggMachete = new ItemBlade(doggMacheteId, EnumBladeType.DOGG);
		//sickles
		sickle = new ItemSickle(sickleId, EnumSickleType.SICKLE);
		homesSmellYaLater = new ItemSickle(homesSmellYaLaterId, EnumSickleType.HOMES);
		regiSickle = new ItemSickle(regiSickleId, EnumSickleType.REGISICKLE);
		clawSickle = new ItemSickle(clawSickleId, EnumSickleType.CLAW);
		//clubs
		deuceClub = new ItemClub(deuceClubId, EnumClubType.DEUCE);
		//canes
		cane = new ItemCane(caneId, EnumCaneType.CANE);
		spearCane = new ItemCane(spearCaneId, EnumCaneType.SPEAR);
		dragonCane = new ItemCane(dragonCaneId, EnumCaneType.DRAGON);
		//Spoons/forks
		crockerSpork = new ItemSpork(crockerSporkId, EnumSporkType.CROCKER);
		skaiaFork = new ItemSpork(skaiaForkId, EnumSporkType.SKAIA);
		//items
		rawCruxite = new ItemCruxiteRaw(rawCruxiteId);
		cruxiteDowel = new ItemDowelUncarved(cruxiteDowelId);
		cruxiteDowelCarved = new ItemDowelCarved(cruxiteDowelCarvedId);
		blankCard = new ItemCardBlank(blankCardId);
		punchedCard = new ItemCardPunched(punchedCardId);
		cruxiteArtifact = new ItemCruxiteArtifact(cruxiteArtifactId, 1, false);
		disk = new ItemDisk(diskId);
		component = new ItemComponent(componentId);
		minestuckBucket = new ItemMinestuckBucket(minestuckBucketId);
		
		//registers things for the client
		ClientProxy.registerSided();
		//server doesn't actually register any renderers for obvious reasons
		proxy.registerRenderers();
		//the client does, however
		ClientProxy.registerRenderers();
		//register blocks
		GameRegistry.registerBlock(chessTile, ItemChessTile.class, "chessTile");
		GameRegistry.registerBlock(gatePortal, "gatePortal");
		GameRegistry.registerBlock(oreCruxite,"oreCruxite");
		GameRegistry.registerBlock(blockStorage,ItemStorageBlock.class,"blockStorage");
		GameRegistry.registerBlock(blockMachine,ItemMachine.class,"blockMachine");
		GameRegistry.registerBlock(blockComputerOff,ItemComputerOff.class,"blockComputer");
		GameRegistry.registerBlock(blockComputerOn,"blockComputerOn");
		GameRegistry.registerBlock(layeredSand, ItemBlockLayered.class, "layeredSand");
		//fluids
		GameRegistry.registerBlock(blockOil, "blockOil");
		GameRegistry.registerBlock(blockBlood, "blockBlood");
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
		ItemStack bloodBucket = new ItemStack(minestuckBucket, 1, blockBloodId);
		ItemStack oilBucket = new ItemStack(minestuckBucket, 1, blockOilId);
		ItemStack layeredSandStack = new ItemStack(layeredSand);
		//set harvest information for blocks
		MinecraftForge.setBlockHarvestLevel(chessTile, "shovel", 0);
		MinecraftForge.setBlockHarvestLevel(oreCruxite, "pickaxe", 1);
		MinecraftForge.setBlockHarvestLevel(blockStorage, "pickaxe", 1);
		MinecraftForge.setBlockHarvestLevel(blockMachine, "pickaxe", 1);

		minestuckBucket.fillFluids.add(blockBloodId);
		minestuckBucket.textureFiles.put(blockBloodId, "BucketBlood");
		minestuckBucket.fillFluids.add(blockOilId);
		minestuckBucket.textureFiles.put(blockOilId, "BucketOil");

		fluidOil.setUnlocalizedName(blockOil.getUnlocalizedName());
		fluidBlood.setUnlocalizedName(blockBlood.getUnlocalizedName());
		
		//Debug.printf("Blood id: %d, Oil id: %d", blockBloodId, blockOilId);
		
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
		EntityList.addMapping(EntityDecoy.class, "playerDecoy", entityIdStart + currentEntityIdOffset);
		EntityRegistry.registerModEntity(EntityDecoy.class, "playerDecoy", currentEntityIdOffset, this, 80, 3, true);
		currentEntityIdOffset++;
		
		//register entities with fml
		EntityRegistry.registerModEntity(EntityGrist.class, "grist", currentEntityIdOffset, this, 512, 1, true);

		EntityRegistry.addSpawn(EntityImp.class, 3, 3, 20, EnumCreatureType.monster, WorldType.base12Biomes);
		EntityRegistry.addSpawn(EntityOgre.class, 2, 1, 1, EnumCreatureType.monster, WorldType.base12Biomes);
		EntityRegistry.addSpawn(EntityBasilisk.class, 1, 1, 1, EnumCreatureType.monster, WorldType.base12Biomes);
		EntityRegistry.addSpawn(EntityGiclops.class, 1, 1, 1, EnumCreatureType.monster, WorldType.base12Biomes);

		//register Tile Entities
		GameRegistry.registerTileEntity(TileEntityGatePortal.class, "gatePortal");
		GameRegistry.registerTileEntity(TileEntityMachine.class, "containerMachine");
		GameRegistry.registerTileEntity(TileEntityComputer.class, "computerSburb");
		//register world generators
		DimensionManager.registerProviderType(skaiaProviderTypeId, WorldProviderSkaia.class, true);
		DimensionManager.registerDimension(skaiaDimensionId, skaiaProviderTypeId);
		DimensionManager.registerProviderType(landProviderTypeId, WorldProviderLands.class, true);
		//Register the player tracker
		GameRegistry.registerPlayerTracker(new MinestuckPlayerTracker());

		//register ore generation
		if (generateCruxiteOre) {
			OreHandler oreHandler = new OreHandler();
			GameRegistry.registerWorldGenerator(oreHandler);
		}

		//register machine GUIs
		NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());

		//register connection handler
		NetworkRegistry.instance().registerConnectionHandler(new MinestuckConnectionHandler());

		//Register structures
		MapGenStructureIO.func_143034_b(StructureCastleStart.class, "SkaiaCastle");
		StructureCastlePieces.func_143048_a();

		//register recipes
		AlchemyRecipeHandler.registerVanillaRecipes();
		AlchemyRecipeHandler.registerMinestuckRecipes();
		AlchemyRecipeHandler.registerModRecipes();
		
		if(event.getSide().isClient())
			TickRegistry.registerTickHandler(ClientEditHandler.instance, Side.CLIENT);
		TickRegistry.registerTickHandler(ServerEditHandler.instance, Side.SERVER);
		
		KindAbstratusList.registerTypes();
		DeployList.registerItems();
		
		ComputerProgram.registerProgram(0, SburbClient.class, clientDiskStack);
		ComputerProgram.registerProgram(1, SburbServer.class, serverDiskStack);
		
		SessionHandler.maxSize = acceptTitleCollision?(generateSpecialClasses?168:144):12;
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) 
	{
		MinecraftForge.EVENT_BUS.register(new MinestuckSaveHandler());
		MinecraftForge.EVENT_BUS.register(new MinestuckFluidHandler());
		if(event.getSide().isClient())
			MinecraftForge.EVENT_BUS.register(ClientEditHandler.instance);
		MinecraftForge.EVENT_BUS.register(ServerEditHandler.instance);
		MinecraftForge.EVENT_BUS.register(MinestuckStatsHandler.instance);
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
				//Debug.print("Server about to start, Unregistering " + dim);
			}
			iterator.remove();
		}
	}
	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		MinestuckSaveHandler.lands.clear();
		File dataFile = event.getServer().worldServers[0].getSaveHandler().getMapFileFromName("MinestuckData");
		if(dataFile != null && dataFile.exists()) {
			NBTTagCompound nbt = null;
			try {
				nbt = CompressedStreamTools.readCompressed(new FileInputStream(dataFile));
			} catch(IOException e) {
				e.printStackTrace();
			}
			if(nbt != null) {
				for(byte landId : nbt.getByteArray("landList")) {
					if(MinestuckSaveHandler.lands.contains((Byte)landId))
						continue;
					MinestuckSaveHandler.lands.add(landId);
					
					if(!DimensionManager.isDimensionRegistered(landId))
						DimensionManager.registerDimension(landId, Minestuck.landProviderTypeId);
				}
				
				SkaianetHandler.loadData(nbt.getCompoundTag("skaianet"));
				
				GristStorage.readFromNBT(nbt);
				
			}
			return;
		}
		
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
					//Debug.printf("Found land dimension id of: %d", currentByte);
					
					if(!DimensionManager.isDimensionRegistered(currentByte))
						DimensionManager.registerDimension(currentByte, Minestuck.landProviderTypeId);
				}
				dataInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		File connectionData = event.getServer().worldServers[0].getSaveHandler().getMapFileFromName("connectionList");
		if(connectionData != null && connectionData.exists())
			try {
				SkaianetHandler.loadData(CompressedStreamTools.readCompressed(new FileInputStream(connectionData)));
			} catch(IOException e) {
				e.printStackTrace();
				SkaianetHandler.loadOld(connectionData);
			}
		
		File gristcache = event.getServer().worldServers[0].getSaveHandler().getMapFileFromName("gristCache");
		if(gristcache != null && gristcache.exists()) {
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
