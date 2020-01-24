package com.mraof.minestuck;

import com.mraof.minestuck.client.ClientProxy;
import com.mraof.minestuck.fluid.MSFluids;
import com.mraof.minestuck.world.gen.MSSurfaceBuilders;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.WorldPersistenceHooks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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
		
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, MinestuckConfig.CLIENT_CONFIG);
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, MinestuckConfig.SERVER_CONFIG);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MinestuckConfig.COMMON_CONFIG);
		
		WorldPersistenceHooks.addHook(new MSWorldPersistenceHook());
		
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		MSFluids.FLUIDS.register(eventBus);
		MSSurfaceBuilders.REGISTER.register(eventBus);
	}
	
	private void setup(final FMLCommonSetupEvent event)
	{
		CommonProxy.init();
	}
	
	private void clientSetup(final FMLClientSetupEvent event)
	{
		ClientProxy.init();
	}
}