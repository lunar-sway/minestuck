package com.mraof.minestuck;

import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.client.ClientProxy;
import com.mraof.minestuck.command.*;
import com.mraof.minestuck.editmode.DeployList;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.event.ServerEventHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.modSupport.crafttweaker.CraftTweakerSupport;
import com.mraof.minestuck.tileentity.*;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.*;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;

import java.util.Random;

import static com.mraof.minestuck.Minestuck.MOD_ID;
import static com.mraof.minestuck.Minestuck.MOD_NAME;

@Mod(modid = MOD_ID, name = MOD_NAME, version = "@VERSION@", guiFactory = "com.mraof.minestuck.client.gui.MinestuckGuiFactory", acceptedMinecraftVersions = "[1.12,1.12.2]")
public class Minestuck
{
	public static final String MOD_NAME = "Minestuck";
	public static final String MOD_ID = "minestuck";
	
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
	
	@SidedProxy(clientSide = "com.mraof.minestuck.client.ClientProxy", serverSide = "com.mraof.minestuck.CommonProxy")
	public static CommonProxy proxy;

	public static long worldSeed = 0;	//TODO proper usage of seed when generating titles, land aspects, and land dimension data
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) 
	{
		isClientRunning = event.getSide().isClient();
		
		Debug.logger = event.getModLog();
		
		MinestuckConfig.loadConfigFile(event.getSuggestedConfigurationFile(), event.getSide());
		
		//(new UpdateChecker()).start();
		
		proxy.preInit();
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event) 
	{
		//Register textures and renders
		if(isClientRunning)
		{
			ClientProxy.registerRenderers();
		}
		
		proxy.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) 
	{
		if(Loader.isModLoaded("crafttweaker"))
			CraftTweakerSupport.applyRecipes();
		
		AlchemyRecipes.registerAutomaticRecipes();
	}

	@EventHandler 
	public void serverAboutToStart(FMLServerAboutToStartEvent event)
	{
		isServerRunning = true;
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
		event.registerServerCommand(new CommandPorkhollow());
		event.registerServerCommand(new CommandLandDebug());
		
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
