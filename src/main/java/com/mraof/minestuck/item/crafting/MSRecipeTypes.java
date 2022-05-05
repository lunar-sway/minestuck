package com.mraof.minestuck.item.crafting;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.crafting.alchemy.*;
import com.mraof.minestuck.item.crafting.alchemy.generator.ContainerGristCost;
import com.mraof.minestuck.item.crafting.alchemy.generator.RecipeGeneratedGristCost;
import com.mraof.minestuck.item.crafting.alchemy.generator.SourceGristCost;
import com.mraof.minestuck.world.storage.loot.MSLootTables;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;

@ObjectHolder(Minestuck.MOD_ID)
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class MSRecipeTypes
{
	public static RecipeType<IrradiatingRecipe> IRRADIATING_TYPE;
	public static RecipeType<GristCostRecipe> GRIST_COST_TYPE;
	public static RecipeType<AbstractCombinationRecipe> COMBINATION_TYPE;
	
	public static final RecipeSerializer<NonMirroredRecipe> NON_MIRRORED = getNull();
	public static final SimpleCookingSerializer<IrradiatingRecipe> IRRADIATING = getNull();
	public static final RecipeSerializer<IrradiatingFallbackRecipe> IRRADIATING_FALLBACK = getNull();
	public static final RecipeSerializer<GristCost> GRIST_COST = getNull();
	public static final RecipeSerializer<GristCost> CONTAINER_GRIST_COST = getNull();
	public static final RecipeSerializer<GristCostRecipe> WILDCARD_GRIST_COST = getNull();
	public static final RecipeSerializer<UnavailableGristCost> UNAVAILABLE_GRIST_COST = getNull();
	public static final RecipeSerializer<RecipeGeneratedGristCost> RECIPE_GRIST_COST = getNull();
	public static final RecipeSerializer<SourceGristCost> SOURCE_GRIST_COST = getNull();
	public static final RecipeSerializer<CombinationRecipe> COMBINATION = getNull();
	
	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T getNull()
	{
		return null;
	}
	
	@SubscribeEvent
	public static void registerSerializers(final RegistryEvent.Register<RecipeSerializer<?>> event)
	{
		IRRADIATING_TYPE = RecipeType.register(Minestuck.MOD_ID+":irradiating");
		GRIST_COST_TYPE = RecipeType.register(Minestuck.MOD_ID+":grist_cost");
		COMBINATION_TYPE = RecipeType.register(Minestuck.MOD_ID+":combination");
		
		IForgeRegistry<RecipeSerializer<?>> registry = event.getRegistry();
		registry.register(new NonMirroredRecipe.Serializer().setRegistryName("non_mirrored"));
		registry.register(new SimpleCookingSerializer<>(IrradiatingRecipe::new, 20).setRegistryName("irradiating"));
		registry.register(new IrradiatingFallbackRecipe.Serializer().setRegistryName("irradiating_fallback"));
		
		registry.register(new GristCost.Serializer().setRegistryName("grist_cost"));
		registry.register(new ContainerGristCost.Serializer().setRegistryName("container_grist_cost"));
		registry.register(new WildcardGristCost.Serializer().setRegistryName("wildcard_grist_cost"));
		registry.register(new UnavailableGristCost.Serializer().setRegistryName("unavailable_grist_cost"));
		registry.register(new RecipeGeneratedGristCost.Serializer().setRegistryName("recipe_grist_cost"));
		registry.register(new SourceGristCost.Serializer().setRegistryName("source_grist_cost"));
		registry.register(new CombinationRecipe.Serializer().setRegistryName("combination"));
		
		MSLootTables.registerLootSerializers();	//Needs to be called somewhere, preferably during a registry event, and this is close enough
	}
}