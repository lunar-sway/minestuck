package com.mraof.minestuck;

import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.client.ClientProxy;
import com.mraof.minestuck.computer.ProgramData;
import com.mraof.minestuck.computer.editmode.DeployList;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortDialogue;
import com.mraof.minestuck.entity.consort.ConsortRewardHandler;
import com.mraof.minestuck.entry.ComputerBlockProcess;
import com.mraof.minestuck.entry.EntryProcess;
import com.mraof.minestuck.entry.RSEntryBlockProcess;
import com.mraof.minestuck.fluid.MSFluids;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.ModMusicDiscItem;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.player.KindAbstratusList;
import com.mraof.minestuck.skaianet.SessionHandler;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.MSSurfaceBuilders;
import com.mraof.minestuck.world.gen.feature.MSFillerBlockTypes;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.WorldPersistenceHooks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static com.mraof.minestuck.Minestuck.MOD_ID;
import static com.mraof.minestuck.world.gen.OreGeneration.setupOverworldOreGeneration;

@Mod(MOD_ID)
public class Minestuck
{
	public static final String MOD_NAME = "Minestuck";
	public static final String MOD_ID = "minestuck";
	
	public Minestuck()
	{
		
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
		
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MinestuckConfig.commonConfigSpec);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, MinestuckConfig.CLIENT_CONFIG);
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, MinestuckConfig.SERVER_CONFIG);
		
		WorldPersistenceHooks.addHook(new MSWorldPersistenceHook());
		
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		MSFluids.FLUIDS.register(eventBus);
		MSSurfaceBuilders.REGISTER.register(eventBus);
	}
	
	private void setup(final FMLCommonSetupEvent event)
	{
		MSCriteriaTriggers.register();
		MSEntityTypes.registerPlacements();
		MSFillerBlockTypes.init();
		
		//register ore generation
		setupOverworldOreGeneration();
		//GameRegistry.registerWorldGenerator(oreHandler, 0);
		
		//register channel handler
		MSPacketHandler.setupChannel();
		
		//register consort shop prices
		ConsortRewardHandler.registerMinestuckPrices();
		
		MSBiomes.init();
		ConsortDialogue.init();
		
		KindAbstratusList.registerTypes();
		DeployList.registerItems();
		
		ModMusicDiscItem.setup();
		
		ProgramData.registerProgram(0, new ItemStack(MSItems.CLIENT_DISK), ProgramData::onClientClosed);	//This idea was kind of bad and should be replaced
		ProgramData.registerProgram(1, new ItemStack(MSItems.SERVER_DISK), ProgramData::onServerClosed);
		
		EntryProcess.addBlockProcessing(new ComputerBlockProcess());
		if(ModList.get().isLoaded("refinedstorage"))
			EntryProcess.addBlockProcessing(new RSEntryBlockProcess());
		
		SessionHandler.maxSize = 144;//acceptTitleCollision?(generateSpecialClasses?168:144):12;
	}
	
	private void clientSetup(final FMLClientSetupEvent event)
	{
		ClientProxy.init();
	}
}