package com.mraof.minestuck.item.crafting;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.alchemy.*;
import com.mraof.minestuck.alchemy.generator.ContainerGristCost;
import com.mraof.minestuck.alchemy.generator.SourceGristCost;
import com.mraof.minestuck.alchemy.generator.recipe.RecipeGeneratedGristCost;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MSRecipeTypes
{
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPE_REGISTER = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, Minestuck.MOD_ID);
	
	public static final RegistryObject<RecipeType<IrradiatingRecipe>> IRRADIATING_TYPE = recipeType("irradiating");
	public static final RegistryObject<RecipeType<GristCostRecipe>> GRIST_COST_TYPE = recipeType("grist_cost");
	public static final RegistryObject<RecipeType<AbstractCombinationRecipe>> COMBINATION_TYPE = recipeType("combination");
	
	private static <T extends Recipe<?>> RegistryObject<RecipeType<T>> recipeType(String name)
	{
		return RECIPE_TYPE_REGISTER.register(name, () -> new RecipeType<>()
		{
			@Override
			public String toString()
			{
				return "minestuck:" + name;
			}
		});
	}
	
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
}