package com.mraof.minestuck;

import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.client.ClientProxy;
import com.mraof.minestuck.client.gui.GuiHandler;
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
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Random;

import static com.mraof.minestuck.Minestuck.MOD_ID;
import static com.mraof.minestuck.Minestuck.MOD_NAME;

@Mod(MOD_ID)
public class Minestuck
{
	public static final String MOD_NAME = "Minestuck";
	public static final String MOD_ID = "minestuck";
	
	/**
	 * True only if the minecraft application is client-sided 
	 */
	//public static boolean isClientRunning;
	/**
	 * True if the minecraft application is server-sided, or if there is an integrated server running
	 */
	//public static volatile boolean isServerRunning;
	
	// The instance of your mod that Forge uses.
	/*@Instance("minestuck")
	public static Minestuck instance;
	
	@SidedProxy(clientSide = "com.mraof.minestuck.client.ClientProxy", serverSide = "com.mraof.minestuck.CommonProxy")
	public static CommonProxy proxy;*/

	public static long worldSeed = 0;	//TODO proper usage of seed when generating titles, land aspects, and land dimension data
	
	public Minestuck()
	{
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::postSetup);
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.GUIFACTORY, () -> GuiHandler::provideGuiContainer);
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	private void setup(final FMLCommonSetupEvent event)
	{
		//isClientRunning = event.getSide().isClient();
		
		//Debug.logger = event.getModLog();
		
		//MinestuckConfig.loadConfigFile(event.getSuggestedConfigurationFile(), event.getSide());
		
		//(new UpdateChecker()).start();
		
		CommonProxy.init();
	}
	
	private void clientSetup(final FMLClientSetupEvent event)
	{
		ClientProxy.init();
	}
	
	public void postSetup(FMLLoadCompleteEvent event)
	{
		/*if(Loader.isModLoaded("crafttweaker"))
			CraftTweakerSupport.applyRecipes();*/
		
		AlchemyRecipes.registerAutomaticRecipes();
	}

	@SubscribeEvent
	public void serverAboutToStart(FMLServerAboutToStartEvent event)
	{
		//isServerRunning = true;
		TileEntityTransportalizer.transportalizers.clear();
		DeployList.applyConfigValues(MinestuckConfig.deployConfigurations);
	}
	
	@SubscribeEvent
	public void serverClosed(FMLServerStoppedEvent event)
	{
		MinestuckDimensionHandler.unregisterDimensions();
		//isServerRunning = !isClientRunning;
		MinestuckPlayerTracker.dataCheckerPermission.clear();
		IdentifierHandler.clear();
	}
	
	@SubscribeEvent
	public void serverStarting(FMLServerStartingEvent event)
	{
		//if(!event.getServer().isDedicatedServer() && Minestuck.class.getAnnotation(Mod.class).version().startsWith("@")) TODO Find an alternative to detect dev environment
			event.getServer().setOnlineMode(false);	//Makes it possible to use LAN in a development environment
		
		if(!event.getServer().isServerInOnlineMode() && MinestuckConfig.useUUID)
			Debug.warn("Because uuids might not be consistent in an offline environment, it is not recommended to use uuids for minestuck. You should disable uuidIdentification in the minestuck config.");
		if(event.getServer().isServerInOnlineMode() && !MinestuckConfig.useUUID)
			Debug.warn("Because users may change their usernames, it is normally recommended to use uuids for minestuck. You should enable uuidIdentification in the minestuck config.");
		
		CommandCheckLand.register(event.getCommandDispatcher());
		CommandGrist.register(event.getCommandDispatcher());
		CommandGristSend.register(event.getCommandDispatcher());
		CommandTransportalizer.register(event.getCommandDispatcher());
		CommandSburbSession.register(event.getCommandDispatcher());
		CommandSburbServer.register(event.getCommandDispatcher());
		CommandSetRung.register(event.getCommandDispatcher());
		CommandConsortReply.register(event.getCommandDispatcher());
		CommandToStructure.register(event.getCommandDispatcher());
		CommandPorkhollow.register(event.getCommandDispatcher());
		CommandLandDebug.register(event.getCommandDispatcher());
		
		worldSeed = event.getServer().getWorld(DimensionType.OVERWORLD).getSeed();
		ServerEventHandler.lastDay = event.getServer().getWorld(DimensionType.OVERWORLD).getGameTime() / 24000L;
		CaptchaDeckHandler.rand = new Random();
		
		MinestuckDimensionHandler.register();
	}
	
	@SubscribeEvent
	public void serverStopping(FMLServerStoppingEvent event)
	{
		ServerEditHandler.onServerStopping();
	}
}
