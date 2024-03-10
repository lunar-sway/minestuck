package com.mraof.minestuck.alchemy.recipe.generator.recipe;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public final class InterpreterTypes
{
	public static final DeferredRegister<Codec<? extends RecipeInterpreter>> REGISTER = DeferredRegister.create(Minestuck.id("recipe_interpreter_codec"), Minestuck.MOD_ID);
	
	public static final Supplier<IForgeRegistry<Codec<? extends RecipeInterpreter>>> REGISTRY = REGISTER.makeRegistry(() -> new RegistryBuilder<Codec<? extends RecipeInterpreter>>().disableSaving().disableSync());
	
	static {
		REGISTER.register("default", () -> DefaultInterpreter.CODEC);
		REGISTER.register("cooking", () -> CookingCostInterpreter.CODEC);
		REGISTER.register("smithing", () -> SmithingInterpreter.CODEC);
	}
}
