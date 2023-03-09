package com.mraof.minestuck.alchemy.generator.recipe;

import com.google.gson.JsonElement;

public abstract class InterpreterSerializer<T extends RecipeInterpreter>
{
	public abstract T read(JsonElement json);
	
	public abstract JsonElement write(T interpreter);
}