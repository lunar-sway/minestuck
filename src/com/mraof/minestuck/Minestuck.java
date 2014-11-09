package com.mraof.minestuck;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Iterator;

import org.lwjgl.opengl.GLContext;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import codechicken.nei.NEIModContainer;

import com.mraof.minestuck.block.BlockChessTile;
import com.mraof.minestuck.block.BlockColoredDirt;
import com.mraof.minestuck.block.BlockComputerOff;
import com.mraof.minestuck.block.BlockComputerOn;
import com.mraof.minestuck.block.BlockFluidBlood;
import com.mraof.minestuck.block.BlockFluidBrainJuice;
import com.mraof.minestuck.block.BlockFluidOil;
import com.mraof.minestuck.block.BlockGatePortal;
import com.mraof.minestuck.block.BlockLayered;
import com.mraof.minestuck.block.BlockMachine;
import com.mraof.minestuck.block.BlockStorage;
import com.mraof.minestuck.block.BlockTransportalizer;
import com.mraof.minestuck.block.OreCruxite;
import com.mraof.minestuck.client.ClientProxy;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.editmode.DeployList;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.entity.EntityDecoy;
import com.mraof.minestuck.entity.carapacian.EntityBlackBishop;
import com.mraof.minestuck.entity.carapacian.EntityBlackPawn;
import com.mraof.minestuck.entity.carapacian.EntityBlackRook;
import com.mraof.minestuck.entity.carapacian.EntityWhiteBishop;
import com.mraof.minestuck.entity.carapacian.EntityWhitePawn;
import com.mraof.minestuck.entity.carapacian.EntityWhiteRook;
import com.mraof.minestuck.entity.consort.EntityIguana;
import com.mraof.minestuck.entity.consort.EntityNakagator;
import com.mraof.minestuck.entity.consort.EntitySalamander;
import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.entity.underling.EntityBasilisk;
import com.mraof.minestuck.entity.underling.EntityGiclops;
import com.mraof.minestuck.entity.underling.EntityImp;
import com.mraof.minestuck.entity.underling.EntityOgre;
import com.mraof.minestuck.event.MinestuckFluidHandler;
import com.mraof.minestuck.item.ItemCaptchaCard;
import com.mraof.minestuck.item.ItemComponent;
import com.mraof.minestuck.item.ItemCruxiteArtifact;
import com.mraof.minestuck.item.ItemCruxiteRaw;
import com.mraof.minestuck.item.ItemDisk;
import com.mraof.minestuck.item.ItemDowel;
import com.mraof.minestuck.item.ItemMinestuckBucket;
import com.mraof.minestuck.item.block.ItemBlockLayered;
import com.mraof.minestuck.item.block.ItemChessTile;
import com.mraof.minestuck.item.block.ItemColoredDirt;
import com.mraof.minestuck.item.block.ItemComputerOff;
import com.mraof.minestuck.item.block.ItemMachine;
import com.mraof.minestuck.item.block.ItemStorageBlock;
import com.mraof.minestuck.item.weapon.EnumBladeType;
import com.mraof.minestuck.item.weapon.EnumCaneType;
import com.mraof.minestuck.item.weapon.EnumClubType;
import com.mraof.minestuck.item.weapon.EnumHammerType;
import com.mraof.minestuck.item.weapon.EnumSickleType;
import com.mraof.minestuck.item.weapon.EnumSporkType;
import com.mraof.minestuck.item.weapon.ItemBlade;
import com.mraof.minestuck.item.weapon.ItemCane;
import com.mraof.minestuck.item.weapon.ItemClub;
import com.mraof.minestuck.item.weapon.ItemHammer;
import com.mraof.minestuck.item.weapon.ItemSickle;
import com.mraof.minestuck.item.weapon.ItemSpork;
import com.mraof.minestuck.nei.NEIMinestuckConfig;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.skaianet.SessionHandler;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.tileentity.TileEntityGatePortal;
import com.mraof.minestuck.tileentity.TileEntityMachine;
import com.mraof.minestuck.tileentity.TileEntityTransportalizer;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.ComputerProgram;
import com.mraof.minestuck.util.MinestuckAchievementHandler;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.ComputerProgram;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.MinestuckPlayerData;
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

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
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
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
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
	public static Item captchaCard;
	public static Item cruxiteArtifact;
	public static Item disk;
	public static Item component;
	public static ItemMinestuckBucket minestuckBucket;


	
	//Blocks
	public static Block chessTile;
	public static Block coloredDirt;
	public static Block gatePortal;
	public static Block oreCruxite;
	public static Block blockStorage;
	public static Block blockMachine;
	public static Block blockComputerOn;
	public static Block blockComputerOff;
	public static Block transportalizer;
	
	public static Block blockOil;
	public static Block blockBlood;
	public static Block blockBrainJuice;
	public static Block layeredSand;

	public static Fluid fluidOil;
	public static Fluid fluidBlood;
	public static Fluid fluidBrainJuice;

	
	
	//Client config
	public static int clientOverworldEditRange;	//Edit range used by the client side.
	public static int clientLandEditRange;		//changed by a MinestuckConfigPacket sent by the server on login.
	public static boolean clientHardMode;
	public static boolean clientGiveItems;
	public static boolean clientEasyDesignix;
	
	//General
	public static boolean hardMode = false;	//Future config option. Currently alters how easy the entry items are accessible after the first time. The machines cost 100 build and there will only be one card if this is true.
	public static boolean generateCruxiteOre; //If set to false, Cruxite Ore will not generate
	public static boolean privateComputers;	//If a player should be able to use other players computers or not.
	public static boolean acceptTitleCollision;	//Allows combinations like "Heir of Hope" and "Seer of Hope" to exist in the same session. Will try to avoid duplicates.
	public static boolean generateSpecialClasses;	//Allow generation of the "Lord" and "Muse" classes.
	public static boolean globalSession;	//Makes only one session possible. Recommended to be true on small servers. Will be ignored when loading a world that already got 2+ sessions.
	public static boolean easyDesignix; //Makes it so you don't need to encode individual cards before combining them.
	public static boolean toolTipEnabled;
	public static boolean forceMaxSize;	//If it should prevent players from joining a session if there is no possible combinations left.
	public static boolean giveItems;
	public static boolean specialCardRenderer;
	public static String privateMessage;
	public static int artifactRange; //The range of the Cruxite Artifact in teleporting zones over to the new land
	public static int overworldEditRange;
	public static int landEditRange;
	public static int cardResolution;
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
	public static EnumMap<Side, FMLEmbeddedChannel> channels;

	public int currentEntityIdOffset = 0;
	public static long worldSeed = 0;

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
		easyDesignix  = config.get("General", "easyDesignix", true).getBoolean(true);
		overworldEditRange = config.get("General", "overWorldEditRange", 15).getInt();
		landEditRange = config.get("General", "landEditRange", 30).getInt();	//Now radius
		artifactRange = config.get("General", "artifactRange", 30).getInt();
		MinestuckAchievementHandler.idOffset = config.get("General", "statisticIdOffset", 413).getInt();
		toolTipEnabled = config.get("General", "editmodeToolTip", false).getBoolean(false);
		hardMode = config.get("General", "hardMode", false).getBoolean(false);
		forceMaxSize = config.get("General", "forceMaxSize", false).getBoolean(false);
		escapeFailureMode = config.get("General", "escapeFailureMode", 0).getInt();
		giveItems = config.get("General", "giveItems", false, "Setting this to true replaces editmode with the old Give Items.").getBoolean(false);
		
		if(escapeFailureMode > 2 || escapeFailureMode < 0)
			escapeFailureMode = 0;
		if(event.getSide().isClient()) {	//Client sided config values
			toolTipEnabled = config.get("General", "editmodeToolTip", false).getBoolean(false);
			specialCardRenderer = config.get("General", "specialCardRenderer", false).getBoolean(false);
			if(Minestuck.specialCardRenderer && !GLContext.getCapabilities().GL_EXT_framebuffer_object)
			{
				specialCardRenderer = false;
				FMLLog.warning("[Minestuck] The FBO extension is not available and is required for the advanced rendering of captchalouge cards.");
			}
			cardResolution = config.get("General", "cardResolution", 1).getInt(1);
			if(cardResolution < 0)
				cardResolution = 0;
		}
		config.save();
		
		(new UpdateChecker()).start();
		
		//Register the Minestuck creative tab
		tabMinestuck = new CreativeTabs("tabMinestuck")
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
		layeredSand = GameRegistry.registerBlock(new BlockLayered(Blocks.sand), ItemBlockLayered.class, "layeredSand").setBlockName("layeredSand");
		coloredDirt = GameRegistry.registerBlock(new BlockColoredDirt(new String[] {"BlueDirt", "ThoughtDirt"}), ItemColoredDirt.class, "coloredDirt").setBlockName("coloredDirt").setHardness(0.5F);
		//machines
		blockStorage = GameRegistry.registerBlock(new BlockStorage(),ItemStorageBlock.class,"blockStorage");
		blockMachine = GameRegistry.registerBlock(new BlockMachine(), ItemMachine.class,"blockMachine");
		blockComputerOff = GameRegistry.registerBlock(new BlockComputerOff(), ItemComputerOff.class,"blockComputer");
		blockComputerOn = GameRegistry.registerBlock(new BlockComputerOn(),"blockComputerOn");
		transportalizer = GameRegistry.registerBlock(new BlockTransportalizer(), "transportalizer");
		//fluids
		fluidOil = new Fluid("Oil");
		FluidRegistry.registerFluid(fluidOil);
		fluidBlood = new Fluid("Blood");
		FluidRegistry.registerFluid(fluidBlood);
		fluidBrainJuice = new Fluid("BrainJuice");
		FluidRegistry.registerFluid(fluidBrainJuice);
		blockOil = GameRegistry.registerBlock(new BlockFluidOil(fluidOil, Material.water), "blockOil");
		blockBlood = GameRegistry.registerBlock(new BlockFluidBlood(fluidBlood, Material.water), "blockBlood");
		blockBrainJuice = GameRegistry.registerBlock(new BlockFluidBrainJuice(fluidBrainJuice, Material.water), "blockBrainJuice");

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
		cruxiteDowel = new ItemDowel();
		captchaCard = new ItemCaptchaCard();
		cruxiteArtifact = new ItemCruxiteArtifact(1, false);
		disk = new ItemDisk();
		component = new ItemComponent();
		minestuckBucket = new ItemMinestuckBucket();
		
		minestuckBucket.addBlock(blockBlood, "BucketBlood");
		minestuckBucket.addBlock(blockOil, "BucketOil");
		minestuckBucket.addBlock(blockBrainJuice, "BucketBrainJuice");
		
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
		GameRegistry.registerItem(captchaCard, "captchaCard");
		GameRegistry.registerItem(cruxiteArtifact, "cruxiteArtifact");
		GameRegistry.registerItem(disk, "computerDisk");
		GameRegistry.registerItem(component, "component");
		GameRegistry.registerItem(minestuckBucket, "minestuckBucket");
		
		
		MinestuckAchievementHandler.prepareAchievementPage();
		
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event) 
	{
		
		//set harvest information for blocks
//		MinecraftForge.setBlockHarvestLevel(chessTile, "shovel", 0);
//		MinecraftForge.setBlockHarvestLevel(oreCruxite, "pickaxe", 1);
//		MinecraftForge.setBlockHarvestLevel(blockStorage, "pickaxe", 1);
//		MinecraftForge.setBlockHarvestLevel(blockMachine, "pickaxe", 1);


		fluidOil.setUnlocalizedName(blockOil.getUnlocalizedName());
		fluidBlood.setUnlocalizedName(blockBlood.getUnlocalizedName());
		fluidBrainJuice.setUnlocalizedName(blockBrainJuice.getUnlocalizedName());
		
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
		this.registerAndMapEntity(EntityBlackRook.class, "dersiteRook", 0x000000, 0xc121d9);
		this.registerAndMapEntity(EntityWhiteRook.class, "prospitianRook", 0xffffff, 0xfde500);
			//To not register this entity as spawnable using a mob egg.
//		EntityList.addMapping(EntityDecoy.class, "playerDecoy", entityIdStart + currentEntityIdOffset);
		EntityRegistry.registerModEntity(EntityDecoy.class, "playerDecoy", currentEntityIdOffset, this, 80, 3, true);
		currentEntityIdOffset++;
		
		//register entities with fml
		EntityRegistry.registerModEntity(EntityGrist.class, "grist", currentEntityIdOffset, this, 512, 1, true);
		
		//register Tile Entities
		GameRegistry.registerTileEntity(TileEntityGatePortal.class, "gatePortal");
		GameRegistry.registerTileEntity(TileEntityMachine.class, "containerMachine");
		GameRegistry.registerTileEntity(TileEntityComputer.class, "computerSburb");
		GameRegistry.registerTileEntity(TileEntityTransportalizer.class, "transportalizer");
		//register world generators
		DimensionManager.registerProviderType(skaiaProviderTypeId, WorldProviderSkaia.class, true);
		DimensionManager.registerDimension(skaiaDimensionId, skaiaProviderTypeId);
		DimensionManager.registerProviderType(landProviderTypeId, WorldProviderLands.class, true);
		
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
		MinecraftForge.EVENT_BUS.register(ServerEditHandler.instance);
		MinecraftForge.EVENT_BUS.register(MinestuckAchievementHandler.instance);
		
		FMLCommonHandler.instance().bus().register(MinestuckPlayerTracker.instance);
		FMLCommonHandler.instance().bus().register(ServerEditHandler.instance);
		
		if(event.getSide().isClient())
		{
			MinecraftForge.EVENT_BUS.register(ClientEditHandler.instance);
			FMLCommonHandler.instance().bus().register(ClientEditHandler.instance);
		}
		
		//register channel handler
		channels = NetworkRegistry.INSTANCE.newChannel("Minestuck", MinestuckChannelHandler.instance);
		
		//Register structures
		MapGenStructureIO.registerStructure(StructureCastleStart.class, "SkaiaCastle");
		StructureCastlePieces.func_143048_a();

		//register recipes
		AlchemyRecipeHandler.registerVanillaRecipes();
		AlchemyRecipeHandler.registerMinestuckRecipes();
		AlchemyRecipeHandler.registerModRecipes();
		
		KindAbstratusList.registerTypes();
		DeployList.registerItems();
		
		ComputerProgram.registerProgram(0, SburbClient.class, new ItemStack(disk, 1, 0));
		ComputerProgram.registerProgram(1, SburbServer.class, new ItemStack(disk, 1, 1));
		
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
	public void registerAndMapEntity(Class<? extends Entity> entityClass, String name, int eggColor, int eggSpotColor)
	{
		this.registerAndMapEntity(entityClass, name, eggColor, eggSpotColor, 80, 3, true);
	}

	public void registerAndMapEntity(Class<? extends Entity> entityClass, String name, int eggColor, int eggSpotColor, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates)
	{
		EntityList.addMapping(entityClass, name, entityIdStart + currentEntityIdOffset, eggColor, eggSpotColor);
		EntityRegistry.registerModEntity(entityClass, name, currentEntityIdOffset, this, trackingRange, updateFrequency, sendsVelocityUpdates);
		currentEntityIdOffset++;
	}

	@EventHandler 
	public void serverAboutToStart(FMLServerAboutToStartEvent event)
	{
		//unregister lands that may not be in this save
		for (Iterator<Byte> iterator = MinestuckSaveHandler.lands.iterator(); iterator.hasNext(); )
		{
			int dim = ((Number)iterator.next()).intValue();
			if(DimensionManager.isDimensionRegistered(dim))
			{
				DimensionManager.unregisterDimension(dim);
				//Debug.print("Server about to start, Unregistering " + dim);
			}
			iterator.remove();
		}
		TileEntityTransportalizer.transportalizers.clear();
	}
	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		worldSeed = event.getServer().worldServers[0].getSeed();

		MinestuckSaveHandler.lands.clear();
		MinestuckPlayerData.onServerStarting();
		
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
				
				MinestuckPlayerData.readFromNBT(nbt);

				TileEntityTransportalizer.loadTransportalizers(nbt.getCompoundTag("transportalizers"));
				
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
			}
		
		File gristcache = event.getServer().worldServers[0].getSaveHandler().getMapFileFromName("gristCache");
		if(gristcache != null && gristcache.exists()) {
			NBTTagCompound nbt = null;
			try {
				nbt = CompressedStreamTools.readCompressed(new FileInputStream(gristcache));
			} catch(IOException e){
				e.printStackTrace();
			}
			
			MinestuckPlayerData.readFromNBT(nbt);
			
		}
	}
	
	@EventHandler
	public void serverStopping(FMLServerStoppingEvent event) {
		ServerEditHandler.onServerStopping();
	}
}
