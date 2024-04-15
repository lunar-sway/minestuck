package com.mraof.minestuck.item.crafting;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.alchemy.recipe.GristCost;
import com.mraof.minestuck.alchemy.recipe.RegularCombinationRecipe;
import com.mraof.minestuck.alchemy.recipe.UnavailableGristCost;
import com.mraof.minestuck.alchemy.recipe.WildcardGristCost;
import com.mraof.minestuck.alchemy.recipe.generator.ContainerGristCost;
import com.mraof.minestuck.alchemy.recipe.generator.SourceGristCost;
import com.mraof.minestuck.alchemy.recipe.generator.recipe.RecipeGeneratedGristCost;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.api.alchemy.recipe.combination.CombinationRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MSRecipeTypes
{
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPE_REGISTER = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, Minestuck.MOD_ID);
	
	public static final Supplier<RecipeType<IrradiatingRecipe>> IRRADIATING_TYPE = recipeType("irradiating");
	public static final Supplier<RecipeType<GristCostRecipe>> GRIST_COST_TYPE = recipeType("grist_cost");
	public static final Supplier<RecipeType<CombinationRecipe>> COMBINATION_TYPE = recipeType("combination");
	
	private static <T extends Recipe<?>> Supplier<RecipeType<T>> recipeType(String name)
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
	
	public static final DeferredRegister<RecipeSerializer<?>> SERIALIZER_REGISTER = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Minestuck.MOD_ID);
	
	public static final Supplier<RecipeSerializer<NonMirroredRecipe>> NON_MIRRORED = SERIALIZER_REGISTER.register("non_mirrored", NonMirroredRecipe.Serializer::new);
	
	public static final Supplier<SimpleCookingSerializer<IrradiatingRecipe>> IRRADIATING = SERIALIZER_REGISTER.register("irradiating", () -> new SimpleCookingSerializer<>(IrradiatingRecipe::new, 20));
	public static final Supplier<RecipeSerializer<IrradiatingFallbackRecipe>> IRRADIATING_FALLBACK = SERIALIZER_REGISTER.register("irradiating_fallback", IrradiatingFallbackRecipe.Serializer::new);
	
	public static final Supplier<RecipeSerializer<GristCost>> GRIST_COST = SERIALIZER_REGISTER.register("grist_cost", GristCost.Serializer::new);
	public static final Supplier<RecipeSerializer<ContainerGristCost>> CONTAINER_GRIST_COST = SERIALIZER_REGISTER.register("container_grist_cost", ContainerGristCost.Serializer::new);
	public static final Supplier<RecipeSerializer<WildcardGristCost>> WILDCARD_GRIST_COST = SERIALIZER_REGISTER.register("wildcard_grist_cost", WildcardGristCost.Serializer::new);
	public static final Supplier<RecipeSerializer<UnavailableGristCost>> UNAVAILABLE_GRIST_COST = SERIALIZER_REGISTER.register("unavailable_grist_cost", UnavailableGristCost.Serializer::new);
	public static final Supplier<RecipeSerializer<RecipeGeneratedGristCost>> RECIPE_GRIST_COST = SERIALIZER_REGISTER.register("recipe_grist_cost", RecipeGeneratedGristCost.Serializer::new);
	public static final Supplier<RecipeSerializer<SourceGristCost>> SOURCE_GRIST_COST = SERIALIZER_REGISTER.register("source_grist_cost", SourceGristCost.Serializer::new);
	
	public static final Supplier<RecipeSerializer<RegularCombinationRecipe>> COMBINATION = SERIALIZER_REGISTER.register("combination", RegularCombinationRecipe.Serializer::new);
}