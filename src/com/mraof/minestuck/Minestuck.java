package com.mraof.minestuck;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.client.ClientProxy;
import com.mraof.minestuck.command.*;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.event.ServerEventHandler;
import com.mraof.minestuck.fluid.MSFluids;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyRecipes;
import com.mraof.minestuck.item.crafting.alchemy.GristCostGenerator;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.tracker.PlayerTracker;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.storage.MSExtraData;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.WorldPersistenceHooks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import java.util.Random;

import static com.mraof.minestuck.Minestuck.MOD_ID;

@Mod(MOD_ID)
public class Minestuck
{
	public static final String MOD_NAME = "Minestuck";
	public static final String MOD_ID = "minestuck";
	
	public Minestuck()
	{
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::postSetup);
		
		MinestuckConfig.loadConfig(MinestuckConfig.client_config, FMLPaths.CONFIGDIR.get().resolve("minestuck-client.toml").toString());
		MinestuckConfig.loadConfig(MinestuckConfig.server_config, FMLPaths.CONFIGDIR.get().resolve("minestuck.toml").toString());
		
		WorldPersistenceHooks.addHook(new MSWorldPersistenceHook());
		
		MinecraftForge.EVENT_BUS.register(this);
		
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		
		MSFluids.FLUIDS.register(eventBus);
	}
	
	private void setup(final FMLCommonSetupEvent event)
	{
		//isClientRunning = event.getSide().isClient();
		
		//MinestuckConfig.loadConfigFile(event.getSuggestedConfigurationFile(), event.getSide());
		
		//(new UpdateChecker()).start();
		
		MinestuckConfig.setConfigVariables();
		CommonProxy.init();
	}
	
	private void clientSetup(final FMLClientSetupEvent event)
	{
		ClientProxy.init();
		MinecraftForge.EVENT_BUS.register(ClientProxy.class);
		MinestuckConfig.setClientValues();
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
		event.getServer().getResourceManager().addReloadListener(new GristCostGenerator(event.getServer().getRecipeManager()));
	}
	
	@SubscribeEvent
	public void serverStarting(FMLServerStartingEvent event)
	{
		
		//if(!event.getServer().isDedicatedServer() && Minestuck.class.getAnnotation(Mod.class).version().startsWith("@")) TODO Find an alternative to detect dev environment
			event.getServer().setOnlineMode(false);	//Makes it possible to use LAN in a development environment
		
		if(!event.getServer().isServerInOnlineMode() && MinestuckConfig.useUUID.get())
			Debug.warn("Because uuids might not be consistent in an offline environment, it is not recommended to use uuids for minestuck. You should disable uuidIdentification in the minestuck config.");
		if(event.getServer().isServerInOnlineMode() && !MinestuckConfig.useUUID.get())
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
		
		ServerEventHandler.lastDay = event.getServer().getWorld(DimensionType.OVERWORLD).getGameTime() / 24000L;
		CaptchaDeckHandler.rand = new Random();
	}
	
	@SubscribeEvent
	public void serverStarted(FMLServerStartedEvent event)
	{
		SkaianetHandler skaianet = SkaianetHandler.get(event.getServer());
		MSExtraData.get(event.getServer()).recoverConnections(recovery -> recovery.recover(skaianet.getActiveConnection(recovery.getClientPlayer())));
	}
	
	@SubscribeEvent
	public void serverStopping(FMLServerStoppingEvent event)
	{
		ServerEditHandler.onServerStopping(event.getServer());
	}
	
	@SubscribeEvent
	public void serverStopped(FMLServerStoppedEvent event)
	{
		PlayerTracker.dataCheckerPermission.clear();
		IdentifierHandler.clear();
		SkaianetHandler.clear();
		MSFeatures.LAND_GATE.clearCache();
	}
	
	@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents
	{
		@SubscribeEvent
		public static void onBlockRegistry(final RegistryEvent.Register<Block> event)
		{
			MSBlocks.registerBlocks(event.getRegistry());
		}
		
		@SubscribeEvent
		public static void onItemRegistry(final RegistryEvent.Register<Item> event)
		{
			MSItems.registerItems(event.getRegistry());
		}
	}
}