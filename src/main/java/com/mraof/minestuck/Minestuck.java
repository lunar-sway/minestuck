package com.mraof.minestuck;

import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.alchemy.recipe.generator.recipe.InterpreterTypes;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.block.AspectTreeBlocks;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.SkaiaBlocks;
import com.mraof.minestuck.block.plant.MSPottedSaplings;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.command.MSSuggestionProviders;
import com.mraof.minestuck.command.argument.MSArgumentTypes;
import com.mraof.minestuck.computer.ProgramData;
import com.mraof.minestuck.computer.editmode.DeployList;
import com.mraof.minestuck.effects.MSEffects;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.dialogue.Triggers;
import com.mraof.minestuck.entity.dialogue.condition.Conditions;
import com.mraof.minestuck.entry.BlockCopier;
import com.mraof.minestuck.entry.ComputerBlockProcess;
import com.mraof.minestuck.entry.RSEntryBlockProcess;
import com.mraof.minestuck.entry.TransportalizerBlockProcess;
import com.mraof.minestuck.fluid.MSFluids;
import com.mraof.minestuck.inventory.MSMenuTypes;
import com.mraof.minestuck.inventory.captchalogue.ModusTypes;
import com.mraof.minestuck.item.MSCreativeTabs;
import com.mraof.minestuck.item.MSDispenserBehaviours;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.item.loot.MSLootTables;
import com.mraof.minestuck.player.KindAbstratusList;
import com.mraof.minestuck.util.MSAttachments;
import com.mraof.minestuck.util.MSParticleType;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.gen.MSSurfaceRules;
import com.mraof.minestuck.world.gen.MSWorldGenTypes;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.feature.MSStructureProcessorTypes;
import com.mraof.minestuck.world.gen.structure.MSStructures;
import com.mraof.minestuck.world.lands.LandTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import software.bernie.geckolib.GeckoLib;

import static com.mraof.minestuck.Minestuck.MOD_ID;

@Mod(MOD_ID)
public class Minestuck
{
	public static final String MOD_ID = "minestuck";
	
	public static ResourceLocation id(String path)
	{
		return new ResourceLocation(MOD_ID, path);
	}
	
	public Minestuck(IEventBus eventBus)
	{
		
		eventBus.addListener(this::setup);
		
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MinestuckConfig.commonSpec);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, MinestuckConfig.clientSpec);
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, MinestuckConfig.serverSpec);
		
		GeckoLib.initialize(eventBus);
		
		MSBlocks.REGISTER.register(eventBus);
		MSItems.REGISTER.register(eventBus);
		MSFluids.REGISTER.register(eventBus);
		MSFluids.TYPE_REGISTER.register(eventBus);
		MSBlockEntityTypes.REGISTER.register(eventBus);
		MSEntityTypes.REGISTER.register(eventBus);
		MSMenuTypes.REGISTER.register(eventBus);
		GristTypes.register(eventBus);
		MSEffects.REGISTER.register(eventBus);
		MSParticleType.REGISTER.register(eventBus);
		MSSoundEvents.REGISTER.register(eventBus);
		LandTypes.TERRAIN_REGISTER.register(eventBus);
		LandTypes.TITLE_REGISTER.register(eventBus);
		InterpreterTypes.REGISTER.register(eventBus);
		MSRecipeTypes.RECIPE_TYPE_REGISTER.register(eventBus);
		MSRecipeTypes.SERIALIZER_REGISTER.register(eventBus);
		MSCriteriaTriggers.REGISTER.register(eventBus);
		
		MSLootTables.CONDITION_REGISTER.register(eventBus);
		MSLootTables.FUNCTION_REGISTER.register(eventBus);
		MSLootTables.ENTRY_REGISTER.register(eventBus);
		MSLootTables.MODIFIER_REGISTER.register(eventBus);
		
		ModusTypes.REGISTER.register(eventBus);
		Conditions.REGISTER.register(eventBus);
		Triggers.REGISTER.register(eventBus);
		
		MSFeatures.REGISTER.register(eventBus);
		
		MSStructures.PIECE_REGISTER.register(eventBus);
		MSStructures.TYPE_REGISTER.register(eventBus);
		MSStructures.PLACEMENT_REGISTER.register(eventBus);
		
		MSStructureProcessorTypes.REGISTER.register(eventBus);
		MSSurfaceRules.REGISTER.register(eventBus);
		MSWorldGenTypes.REGISTER.register(eventBus);
		
		MSArgumentTypes.REGISTER.register(eventBus);
		
		MSCreativeTabs.REGISTER.register(eventBus);
		
		MSAttachments.REGISTER.register(eventBus);
		
		SkaiaBlocks.init();
		AspectTreeBlocks.init();
	}
	
	/**
	 * Common setup, which happens in parallel with other mods.
	 * Only do thread-safe setup, such as internal setup of thread-safe registering.
	 */
	private void setup(final FMLCommonSetupEvent event)
	{
		event.enqueueWork(this::mainThreadSetup);
	}
	
	/**
	 * Handles any setup that is not thread-safe, and thus need to happen on the main thread.
	 * Typically meant for registering stuff.
	 */
	private void mainThreadSetup()
	{
		MSSuggestionProviders.register();
		
		KindAbstratusList.registerTypes();
		DeployList.registerItems();
		MSDispenserBehaviours.registerBehaviours();
		
		MSPottedSaplings.addSaplingsToPot();
		
		ProgramData.init();
		
		BlockCopier.addStep(new ComputerBlockProcess());
		BlockCopier.addStep(new TransportalizerBlockProcess());
		if(ModList.get().isLoaded("refinedstorage"))
			BlockCopier.addStep(new RSEntryBlockProcess());
	}
}
