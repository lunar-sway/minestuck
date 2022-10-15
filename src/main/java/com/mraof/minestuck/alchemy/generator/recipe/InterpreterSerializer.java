package com.mraof.minestuck.alchemy.generator.recipe;

import com.google.gson.JsonElement;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class InterpreterSerializer<T extends RecipeInterpreter> extends ForgeRegistryEntry<InterpreterSerializer<?>>
{
	public abstract T read(JsonElement json);
	
	public abstract JsonElement write(T interpreter);
}