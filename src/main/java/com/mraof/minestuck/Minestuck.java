package com.mraof.minestuck;

import com.mraof.minestuck.client.ClientProxy;
import com.mraof.minestuck.command.*;
import com.mraof.minestuck.event.ServerEventHandler;
import com.mraof.minestuck.fluid.MSFluids;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.item.crafting.alchemy.GristCostGenerator;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.tracker.PlayerTracker;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.storage.MSExtraData;
import net.minecraft.util.SharedConstants;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.WorldPersistenceHooks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
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
		
		MinestuckConfig.loadConfig(MinestuckConfig.client_config, FMLPaths.CONFIGDIR.get().resolve("minestuck-client.toml").toString());
		MinestuckConfig.loadConfig(MinestuckConfig.server_config, FMLPaths.CONFIGDIR.get().resolve("minestuck.toml").toString());
		
		WorldPersistenceHooks.addHook(new MSWorldPersistenceHook());
		
		MinecraftForge.EVENT_BUS.register(this);
		
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		DistExecutor.runWhenOn(Dist.CLIENT, () -> ClientProxy::registerEarly);
		
		MSFluids.FLUIDS.register(eventBus);
	}
	
	private void setup(final FMLCommonSetupEvent event)
	{
		
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

	@SubscribeEvent
	public void serverAboutToStart(FMLServerAboutToStartEvent event)
	{
		event.getServer().getResourceManager().addReloadListener(new GristCostGenerator(event.getServer()));
	}
	
	@SubscribeEvent
	public void serverStarting(FMLServerStartingEvent event)
	{
		Debug.info(SharedConstants.developmentMode);
		//if(!event.getServer().isDedicatedServer() && Minestuck.class.getAnnotation(Mod.class).version().startsWith("@")) TODO Find an alternative to detect dev environment
			//event.getServer().setOnlineMode(false);	//Makes it possible to use LAN in a development environment
		
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
	public void serverStopped(FMLServerStoppedEvent event)
	{
		PlayerTracker.dataCheckerPermission.clear();
		IdentifierHandler.clear();
		SkaianetHandler.clear();
		MSFeatures.LAND_GATE.clearCache();
	}
}