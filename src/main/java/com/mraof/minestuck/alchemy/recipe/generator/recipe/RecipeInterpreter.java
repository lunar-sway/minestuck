package com.mraof.minestuck.alchemy.recipe.generator.recipe;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.recipe.generator.GeneratorCallback;
import com.mraof.minestuck.api.alchemy.recipe.generator.LookupTracker;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;
import java.util.function.Function;

public interface RecipeInterpreter
{
	Codec<RecipeInterpreter> DISPATCH_CODEC = InterpreterTypes.REGISTRY.byNameCodec().dispatch(RecipeInterpreter::codec, Function.identity());
	
	List<Item> getOutputItems(Recipe<?> recipe);
	
	GristSet generateCost(Recipe<?> recipe, Item output, GeneratorCallback callback);
	
	default void reportPreliminaryLookups(Recipe<?> recipe, LookupTracker tracker)
	{}
	
	Codec<? extends RecipeInterpreter> codec();
}