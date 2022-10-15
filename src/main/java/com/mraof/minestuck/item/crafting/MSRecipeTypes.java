package com.mraof.minestuck.item.crafting;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.alchemy.*;
import com.mraof.minestuck.alchemy.generator.ContainerGristCost;
import com.mraof.minestuck.alchemy.generator.recipe.RecipeGeneratedGristCost;
import com.mraof.minestuck.alchemy.generator.SourceGristCost;
import com.mraof.minestuck.item.loot.MSLootTables;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class MSRecipeTypes
{
	public static RecipeType<IrradiatingRecipe> IRRADIATING_TYPE;
	public static RecipeType<GristCostRecipe> GRIST_COST_TYPE;
	public static RecipeType<AbstractCombinationRecipe> COMBINATION_TYPE;
	
	public static final DeferredRegister<RecipeSerializer<?>> SERIALIZER_REGISTER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Minestuck.MOD_ID);
	
	public static final RegistryObject<RecipeSerializer<NonMirroredRecipe>> NON_MIRRORED = SERIALIZER_REGISTER.register("non_mirrored", NonMirroredRecipe.Serializer::new);
	
	public static final RegistryObject<SimpleCookingSerializer<IrradiatingRecipe>> IRRADIATING = SERIALIZER_REGISTER.register("irradiating", () -> new SimpleCookingSerializer<>(IrradiatingRecipe::new, 20));
	public static final RegistryObject<RecipeSerializer<IrradiatingFallbackRecipe>> IRRADIATING_FALLBACK = SERIALIZER_REGISTER.register("irradiating_fallback", IrradiatingFallbackRecipe.Serializer::new);
	
	public static final RegistryObject<RecipeSerializer<GristCost>> GRIST_COST = SERIALIZER_REGISTER.register("grist_cost", GristCost.Serializer::new);
	public static final RegistryObject<RecipeSerializer<ContainerGristCost>> CONTAINER_GRIST_COST = SERIALIZER_REGISTER.register("container_grist_cost", ContainerGristCost.Serializer::new);
	public static final RegistryObject<RecipeSerializer<WildcardGristCost>> WILDCARD_GRIST_COST = SERIALIZER_REGISTER.register("wildcard_grist_cost", WildcardGristCost.Serializer::new);
	public static final RegistryObject<RecipeSerializer<UnavailableGristCost>> UNAVAILABLE_GRIST_COST = SERIALIZER_REGISTER.register("unavailable_grist_cost", UnavailableGristCost.Serializer::new);
	public static final RegistryObject<RecipeSerializer<RecipeGeneratedGristCost>> RECIPE_GRIST_COST = SERIALIZER_REGISTER.register("recipe_grist_cost", RecipeGeneratedGristCost.Serializer::new);
	public static final RegistryObject<RecipeSerializer<SourceGristCost>> SOURCE_GRIST_COST = SERIALIZER_REGISTER.register("source_grist_cost", SourceGristCost.Serializer::new);
	
	public static final RegistryObject<RecipeSerializer<CombinationRecipe>> COMBINATION = SERIALIZER_REGISTER.register("combination", CombinationRecipe.Serializer::new);
	
	@SubscribeEvent
	public static void registerSerializers(final RegistryEvent.Register<RecipeSerializer<?>> event)
	{
		IRRADIATING_TYPE = RecipeType.register(Minestuck.MOD_ID+":irradiating");
		GRIST_COST_TYPE = RecipeType.register(Minestuck.MOD_ID+":grist_cost");
		COMBINATION_TYPE = RecipeType.register(Minestuck.MOD_ID+":combination");
		
		MSLootTables.registerLootSerializers();	//Needs to be called somewhere, preferably during a registry event, and this is close enough
	}
}