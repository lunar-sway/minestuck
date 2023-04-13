package com.mraof.minestuck;

import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.alchemy.GristTypes;
import com.mraof.minestuck.alchemy.generator.recipe.InterpreterSerializers;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.command.argument.MSArgumentTypes;
import com.mraof.minestuck.computer.ProgramData;
import com.mraof.minestuck.computer.editmode.DeployList;
import com.mraof.minestuck.effects.MSEffects;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortDialogue;
import com.mraof.minestuck.entry.*;
import com.mraof.minestuck.fluid.MSFluids;
import com.mraof.minestuck.inventory.MSMenuTypes;
import com.mraof.minestuck.inventory.captchalogue.ModusTypes;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.item.loot.MSLootTables;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.player.KindAbstratusList;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.gen.MSDensityFunctions;
import com.mraof.minestuck.world.gen.MSNoiseParameters;
import com.mraof.minestuck.world.gen.MSSurfaceRules;
import com.mraof.minestuck.world.gen.MSWorldGenTypes;
import com.mraof.minestuck.world.gen.feature.MSCFeatures;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.feature.MSStructureProcessorTypes;
import com.mraof.minestuck.world.gen.structure.*;
import com.mraof.minestuck.world.lands.LandTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

import static com.mraof.minestuck.Minestuck.MOD_ID;

@Mod(MOD_ID)
public class Minestuck
{
	public static final String MOD_NAME = "Minestuck";
	public static final String MOD_ID = "minestuck";
	
	public Minestuck()
	{
		
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MinestuckConfig.commonSpec);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, MinestuckConfig.clientSpec);
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, MinestuckConfig.serverSpec);
		
		GeckoLib.initialize();
		
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		MSBlocks.REGISTER.register(eventBus);
		MSItems.REGISTER.register(eventBus);
		MSFluids.REGISTER.register(eventBus);
		MSFluids.TYPE_REGISTER.register(eventBus);
		MSBlockEntityTypes.REGISTER.register(eventBus);
		MSEntityTypes.REGISTER.register(eventBus);
		MSMenuTypes.REGISTER.register(eventBus);
		GristTypes.GRIST_TYPES.register(eventBus);
		MSEffects.REGISTER.register(eventBus);
		MSSoundEvents.REGISTER.register(eventBus);
		LandTypes.TERRAIN_REGISTER.register(eventBus);
		LandTypes.TITLE_REGISTER.register(eventBus);
		InterpreterSerializers.REGISTER.register(eventBus);
		MSRecipeTypes.RECIPE_TYPE_REGISTER.register(eventBus);
		MSRecipeTypes.SERIALIZER_REGISTER.register(eventBus);
		MSLootTables.CONDITION_REGISTER.register(eventBus);
		MSLootTables.FUNCTION_REGISTER.register(eventBus);
		MSLootTables.ENTRY_REGISTER.register(eventBus);
		MSLootTables.MODIFIER_REGISTER.register(eventBus);
		ModusTypes.REGISTER.register(eventBus);
		
		MSNoiseParameters.REGISTER.register(eventBus);
		MSDensityFunctions.REGISTER.register(eventBus);
		MSFeatures.REGISTER.register(eventBus);
		MSCFeatures.REGISTER.register(eventBus);
		MSPlacedFeatures.REGISTER.register(eventBus);
		
		MSStructurePieces.REGISTER.register(eventBus);
		MSStructures.REGISTER.register(eventBus);
		MSConfiguredStructures.REGISTER.register(eventBus);
		MSStructurePlacements.REGISTER.register(eventBus);
		MSStructureSets.REGISTER.register(eventBus);
		
		MSStructureProcessorTypes.REGISTER.register(eventBus);
		MSSurfaceRules.REGISTER.register(eventBus);
		MSWorldGenTypes.REGISTER.register(eventBus);
		
		MSArgumentTypes.REGISTER.register(eventBus);
	}
	
	/**
	 * Common setup, which happens in parallel with other mods.
	 * Only do thread-safe setup, such as internal setup of thread-safe registering.
	 */
	private void setup(final FMLCommonSetupEvent event)
	{
		event.enqueueWork(this::mainThreadSetup);
		
		//register channel handler
		MSPacketHandler.setupChannel();
	}
	
	/**
	 * Handles any setup that is not thread-safe, and thus need to happen on the main thread.
	 * Typically meant for registering stuff.
	 */
	private void mainThreadSetup()
	{
		MSCriteriaTriggers.register();
		MSEntityTypes.registerPlacements();
		
		ConsortDialogue.init();
		
		KindAbstratusList.registerTypes();
		DeployList.registerItems();
		
		ProgramData.registerProgram(0, new ItemStack(MSItems.CLIENT_DISK.get()), ProgramData::onClientClosed);
		ProgramData.registerProgram(1, new ItemStack(MSItems.SERVER_DISK.get()), ProgramData::onServerClosed);
		
		BlockCopier.addStep(new ComputerBlockProcess());
		BlockCopier.addStep(new TransportalizerBlockProcess());
		if(ModList.get().isLoaded("refinedstorage"))
			BlockCopier.addStep(new RSEntryBlockProcess());
	}
}