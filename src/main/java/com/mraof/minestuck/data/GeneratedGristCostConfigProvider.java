package com.mraof.minestuck.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.GristTypes;
import com.mraof.minestuck.item.crafting.alchemy.generator.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GeneratedGristCostConfigProvider implements DataProvider
{
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
	private final DataGenerator generator;
	private final String modid;
	private final List<JsonObject> entries = new ArrayList<>();
	
	public GeneratedGristCostConfigProvider(DataGenerator generator, String modid)
	{
		this.generator = generator;
		this.modid = modid;
	}
	
	protected void addEntries()
	{
		serializer(RecipeSerializer.SHAPED_RECIPE);
		serializer(RecipeSerializer.SHAPELESS_RECIPE);
		serializer(MSRecipeTypes.NON_MIRRORED);
		type(RecipeType.STONECUTTING);
		type(RecipeType.SMITHING, SmithingInterpreter.INSTANCE);
		type(RecipeType.SMELTING, new CookingCostInterpreter(new GristSet(GristTypes.TAR, 1)));
		type(MSRecipeTypes.IRRADIATING_TYPE, new CookingCostInterpreter(new GristSet(GristTypes.URANIUM, 1)));
	}
	
	@Override
	public final void run(HashCache cache) throws IOException
	{
		addEntries();
		
		Path outputFolder = generator.getOutputFolder();
		
		Path jsonPath = outputFolder.resolve("data/" + modid + "/" + RecipeGeneratedCostHandler.PATH);
		
		JsonElement json = serialize();
		DataProvider.save(GSON, cache, json, jsonPath);
	}
	
	private JsonElement serialize()
	{
		Type type = new TypeToken<List<JsonObject>>(){}.getType();
		return GSON.toJsonTree(entries, type);
	}
	
	protected void recipe(ResourceLocation location)
	{
		recipe(location, DefaultInterpreter.INSTANCE);
	}
	
	protected void serializer(RecipeSerializer<?> serializer)
	{
		serializer(serializer, DefaultInterpreter.INSTANCE);
	}
	
	protected void type(RecipeType<?> type)
	{
		type(type, DefaultInterpreter.INSTANCE);
	}
	
	protected void recipe(ResourceLocation location, RecipeInterpreter interpreter)
	{
		JsonObject entry = new JsonObject();
		entry.addProperty("source_type", "recipe");
		entry.addProperty("source", location.toString());
		addEntry(entry, interpreter);
	}
	
	protected void serializer(RecipeSerializer<?> serializer, RecipeInterpreter interpreter)
	{
		JsonObject entry = new JsonObject();
		entry.addProperty("source_type", "recipe_serializer");
		entry.addProperty("source", serializer.getRegistryName().toString());
		addEntry(entry, interpreter);
	}
	
	protected void type(RecipeType<?> type, RecipeInterpreter interpreter)
	{
		JsonObject entry = new JsonObject();
		entry.addProperty("source_type", "recipe_type");
		entry.addProperty("source", type.toString());
		addEntry(entry, interpreter);
	}
	
	@SuppressWarnings("unchecked")
	private <T extends RecipeInterpreter> void addEntry(JsonObject entry, T interpreter)
	{
		ResourceLocation type = Objects.requireNonNull(interpreter.getSerializer().getRegistryName());
		entry.addProperty("interpreter_type", type.toString());
		entry.add("interpreter", ((InterpreterSerializer<T>) interpreter.getSerializer()).write(interpreter));
		
		entries.add(entry);
	}
	
	@Override
	public String getName()
	{
		return "Minestuck generated grist costs config";
	}
}
