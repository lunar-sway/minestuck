package com.mraof.minestuck;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.client.ClientProxy;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.client.util.MinestuckModelManager;
import com.mraof.minestuck.command.*;
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.editmode.DeployList;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.entity.MinestuckEntities;
import com.mraof.minestuck.entity.consort.ConsortDialogue;
import com.mraof.minestuck.event.MinestuckFluidHandler;
import com.mraof.minestuck.event.ServerEventHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.skaianet.SessionHandler;
import com.mraof.minestuck.tileentity.*;
import com.mraof.minestuck.tracker.ConnectionListener;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.*;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.gen.OreHandler;
import com.mraof.minestuck.world.gen.structure.StructureCastlePieces;
import com.mraof.minestuck.world.gen.structure.StructureCastleStart;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.structure.MapGenLandStructure;
import com.mraof.minestuck.world.lands.structure.village.ConsortVillageComponents;
import com.mraof.minestuck.world.storage.MinestuckSaveHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Random;

//import codechicken.nei.NEIModContainer;
//import com.mraof.minestuck.nei.NEIMinestuckConfig;

@Mod(modid = "minestuck", name = "Minestuck", version = "@VERSION@", guiFactory = "com.mraof.minestuck.client.gui.MinestuckGuiFactory", acceptedMinecraftVersions = "[1.11]")
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
	
	// The instance of your mod that Forge uses.
	@Instance("minestuck")
	public static Minestuck instance;
	
	public static CreativeTabs tabMinestuck;

	public static long worldSeed = 0;	//TODO proper usage of seed when generating titles, land aspects, and land dimension data
	
	public static SoundEvent soundEmissaryOfDance;
	public static SoundEvent soundDanceStabDance;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) 
	{
		isClientRunning = event.getSide().isClient();
		
		Debug.logger = event.getModLog();
		
		MinestuckConfig.loadConfigFile(event.getSuggestedConfigurationFile(), event.getSide());
		
		//(new UpdateChecker()).start();
		
		//Register the Minestuck creative tab
		tabMinestuck = new CreativeTabs("tabMinestuck")
		{
			@Override
			public ItemStack getTabIconItem()
			{
				return new ItemStack(MinestuckItems.zillyhooHammer);
			}
		};
		
		ResourceLocation soundLocation = new ResourceLocation("minestuck", "record.emissary");
		soundEmissaryOfDance = GameRegistry.register(new SoundEvent(soundLocation), soundLocation);
		soundLocation = new ResourceLocation("minestuck", "record.danceStab");
		soundDanceStabDance = GameRegistry.register(new SoundEvent(soundLocation), soundLocation);
		
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
		GameRegistry.registerTileEntity(TileEntitySkaiaPortal.class, "Minstuck.GatePortal");
		GameRegistry.registerTileEntity(TileEntitySburbMachine.class, "Minestuck.SburbMachine");
		GameRegistry.registerTileEntity(TileEntityCrockerMachine.class, "Minestuck.CrockerMachine");
		GameRegistry.registerTileEntity(TileEntityComputer.class, "Minestuck.ComputerSburb");
		GameRegistry.registerTileEntity(TileEntityTransportalizer.class, "Minestuck.Transportalizer");
		GameRegistry.registerTileEntity(TileEntityGate.class, "Minestuck.Gate");
		
		MinestuckDimensionHandler.register();
		
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
		MinecraftForge.EVENT_BUS.register(MinestuckChannelHandler.instance);
		MinecraftForge.EVENT_BUS.register(new ConnectionListener());
		
		if(event.getSide().isClient())
		{
			MinecraftForge.EVENT_BUS.register(ClientEditHandler.instance);
			MinecraftForge.EVENT_BUS.register(new MinestuckConfig());
		}
		
		//register channel handler
		MinestuckChannelHandler.setupChannel();
		
		//Register structures
		MapGenStructureIO.registerStructure(StructureCastleStart.class, "SkaiaCastle");
		StructureCastlePieces.registerComponents();
		MapGenLandStructure.registerStructures();
		ConsortVillageComponents.registerComponents();
		
		//register recipes
		AlchemyRecipeHandler.registerVanillaRecipes();
		AlchemyRecipeHandler.registerMinestuckRecipes();
		AlchemyRecipeHandler.registerModRecipes();
		
		LandAspectRegistry.registerLandAspects();
		ConsortDialogue.init();
		
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
//			NEIModContainer.plugins.add(new NEIMinestuckConfig());
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
		IdentifierHandler.clear();
	}
	
	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		if(!event.getServer().isDedicatedServer() && Minestuck.class.getAnnotation(Mod.class).version().startsWith("@"))
			event.getServer().setOnlineMode(false);	//Makes it possible to use LAN in a development environment
		
		if(!event.getServer().isServerInOnlineMode() && MinestuckConfig.useUUID)
			Debug.warn("Because uuids might not be consistent in an offline environment, it is not recommended to use uuids for minestuck. You should disable uuidIdentification in the minestuck config.");
		if(event.getServer().isServerInOnlineMode() && !MinestuckConfig.useUUID)
			Debug.warn("Because users may change their usernames, it is normally recommended to use uuids for minestuck. You should enable uuidIdentification in the minestuck config.");
		
		event.registerServerCommand(new CommandCheckLand());
		event.registerServerCommand(new CommandGrist());
		event.registerServerCommand(new CommandGristSend());
		event.registerServerCommand(new CommandTransportalizer());
		event.registerServerCommand(new CommandSburbSession());
		event.registerServerCommand(new CommandSburbServer());
		event.registerServerCommand(new CommandSetRung());
		event.registerServerCommand(new CommandConsortReply());
		event.registerServerCommand(new CommandToStructure());
		
		worldSeed = event.getServer().worlds[0].getSeed();
		ServerEventHandler.lastDay = event.getServer().worlds[0].getWorldTime() / 24000L;
		CaptchaDeckHandler.rand = new Random();
	}
	
	@EventHandler
	public void serverStopping(FMLServerStoppingEvent event)
	{
		ServerEditHandler.onServerStopping();
	}
}
