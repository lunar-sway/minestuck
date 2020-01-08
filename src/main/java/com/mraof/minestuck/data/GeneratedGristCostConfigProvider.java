package com.mraof.minestuck.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.item.crafting.alchemy.DefaultInterpreter;
import com.mraof.minestuck.item.crafting.alchemy.GristCostGenerator;
import com.mraof.minestuck.item.crafting.alchemy.InterpreterSerializer;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GeneratedGristCostConfigProvider implements IDataProvider
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
		serializer(IRecipeSerializer.CRAFTING_SHAPED);
		serializer(IRecipeSerializer.CRAFTING_SHAPELESS);
		serializer(MSRecipeTypes.NON_MIRRORED);
		type(IRecipeType.STONECUTTING);
	}
	
	@Override
	public final void act(DirectoryCache cache) throws IOException
	{
		addEntries();
		
		Path outputFolder = generator.getOutputFolder();
		
		Path jsonPath = outputFolder.resolve("data/" + modid + "/minestuck/grist_cost_generation.json");
		
		JsonElement json = serialize();
		IDataProvider.save(GSON, cache, json, jsonPath);
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
	
	protected void serializer(IRecipeSerializer<?> serializer)
	{
		serializer(serializer, DefaultInterpreter.INSTANCE);
	}
	
	protected void type(IRecipeType<?> type)
	{
		type(type, DefaultInterpreter.INSTANCE);
	}
	
	protected void recipe(ResourceLocation location, GristCostGenerator.RecipeInterpreter interpreter)
	{
		JsonObject source = new JsonObject();
		source.addProperty("type", "recipe");
		source.addProperty("value", location.toString());
		addEntry(source, interpreter);
	}
	
	protected void serializer(IRecipeSerializer<?> serializer, GristCostGenerator.RecipeInterpreter interpreter)
	{
		JsonObject source = new JsonObject();
		source.addProperty("type", "recipe_serializer");
		source.addProperty("value", serializer.getRegistryName().toString());
		addEntry(source, interpreter);
	}
	
	protected void type(IRecipeType<?> type, GristCostGenerator.RecipeInterpreter interpreter)
	{
		JsonObject source = new JsonObject();
		source.addProperty("type", "recipe_type");
		source.addProperty("value", type.toString());
		addEntry(source, interpreter);
	}
	
	@SuppressWarnings("unchecked")
	private <T extends GristCostGenerator.RecipeInterpreter> void addEntry(JsonObject source, T interpreter)
	{
		JsonObject entry = new JsonObject();
		entry.add("source", source);
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
