package com.mraof.minestuck.alchemy.recipe.generator.recipe;

import com.mojang.serialization.MapCodec;
import com.mraof.minestuck.Minestuck;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class InterpreterTypes
{
	public static final DeferredRegister<MapCodec<? extends RecipeInterpreter>> REGISTER = DeferredRegister.create(Minestuck.id("recipe_interpreter_codec"), Minestuck.MOD_ID);
	
	public static final Registry<MapCodec<? extends RecipeInterpreter>> REGISTRY = REGISTER.makeRegistry(builder -> {});
	
	static {
		REGISTER.register("default", () -> DefaultInterpreter.CODEC);
		REGISTER.register("cooking", () -> CookingCostInterpreter.CODEC);
		REGISTER.register("smithing", () -> SmithingInterpreter.CODEC);
	}
}
