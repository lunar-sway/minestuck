package com.mraof.minestuck;

import java.util.Random;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
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
import codechicken.nei.NEIModContainer;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.client.ClientProxy;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.client.util.MinestuckModelManager;
import com.mraof.minestuck.command.CommandCheckLand;
import com.mraof.minestuck.command.CommandSburbSession;
import com.mraof.minestuck.command.CommandGrist;
import com.mraof.minestuck.command.CommandTransportalizer;
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.editmode.DeployList;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.entity.MinestuckEntities;
import com.mraof.minestuck.event.MinestuckFluidHandler;
import com.mraof.minestuck.event.ServerEventHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.item.MinestuckItems;
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
	
	// The instance of your mod that Forge uses.
	@Instance("Minestuck")
	public static Minestuck instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide="com.mraof.minestuck.client.ClientProxy", serverSide="com.mraof.minestuck.CommonProxy")

	//The proxy to be used by client and server
	public static CommonProxy proxy;
	public static CreativeTabs tabMinestuck;

	public static long worldSeed = 0;	//TODO proper usage of seed when generating titles, land aspects, and land dimension data

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
				return MinestuckItems.zillyhooHammer;
			}
		};
		
		MinestuckBlocks.registerBlocks();
		MinestuckItems.registerItems();
		
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
		
		BiomeGenMinestuck.mediumNormal = new BiomeGenMinestuck(biomeIdStart, true).setBiomeName("The Medium");
		BiomeGenMinestuck.mediumOcean = new BiomeGenMinestuck(biomeIdStart+1, true).setBiomeName("The Medium (Ocean)");
		
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
		MinecraftForge.EVENT_BUS.register(ServerEventHandler.instance);
		
		FMLCommonHandler.instance().bus().register(MinestuckPlayerTracker.instance);
		FMLCommonHandler.instance().bus().register(ServerEditHandler.instance);
		FMLCommonHandler.instance().bus().register(MinestuckChannelHandler.instance);
		FMLCommonHandler.instance().bus().register(new ConnectionListener());
		FMLCommonHandler.instance().bus().register(ServerEventHandler.instance);
		
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
		
		ComputerProgram.registerProgram(0, SburbClient.class, new ItemStack(MinestuckItems.disk, 1, 0));	//This idea was kind of bad and should be replaced
		ComputerProgram.registerProgram(1, SburbServer.class, new ItemStack(MinestuckItems.disk, 1, 1));
		
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
		MinestuckPlayerTracker.dataCheckerPermission.clear();
	}
	
	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		if(!event.getServer().isDedicatedServer() && Minestuck.class.getAnnotation(Mod.class).version().startsWith("@"))
			event.getServer().setOnlineMode(false);	//Makes it possible to use LAN in a development environment
		
		event.registerServerCommand(new CommandCheckLand());
		event.registerServerCommand(new CommandGrist());
		event.registerServerCommand(new CommandTransportalizer());
		event.registerServerCommand(new CommandSburbSession());
		
		worldSeed = event.getServer().worldServers[0].getSeed();
		ServerEventHandler.lastDay = event.getServer().worldServers[0].getWorldTime() / 24000L;
		CaptchaDeckHandler.rand = new Random();
	}
	
	@EventHandler
	public void serverStopping(FMLServerStoppingEvent event)
	{
		ServerEditHandler.onServerStopping();
	}
}
