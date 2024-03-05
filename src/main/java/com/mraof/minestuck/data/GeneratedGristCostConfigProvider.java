package com.mraof.minestuck.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.alchemy.recipe.generator.recipe.*;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GeneratedGristCostConfigProvider implements DataProvider
{
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
	private final PackOutput output;
	private final String modid;
	private final List<JsonObject> entries = new ArrayList<>();
	
	public GeneratedGristCostConfigProvider(PackOutput output, String modid)
	{
		this.output = output;
		this.modid = modid;
	}
	
	protected void addEntries()
	{
		serializer(RecipeSerializer.SHAPED_RECIPE);
		serializer(RecipeSerializer.SHAPELESS_RECIPE);
		serializer(MSRecipeTypes.NON_MIRRORED.get());
		type(RecipeType.STONECUTTING);
		serializer(RecipeSerializer.SMITHING_TRANSFORM, SmithingInterpreter.INSTANCE);
		type(RecipeType.SMELTING, new CookingCostInterpreter(GristTypes.TAR.get().amount(1)));
		type(MSRecipeTypes.IRRADIATING_TYPE.get(), new CookingCostInterpreter(GristTypes.URANIUM.get().amount(1)));
	}
	
	@Override
	public final CompletableFuture<?> run(CachedOutput cache)
	{
		addEntries();
		
		Path jsonPath = this.output.getOutputFolder(PackOutput.Target.DATA_PACK)
				.resolve(modid).resolve(RecipeGeneratedCostHandler.PATH);
		
		JsonElement json = serialize();
		return DataProvider.saveStable(cache, json, jsonPath);
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
		entry.addProperty("type", "recipe");
		entry.addProperty("recipe", location.toString());
		addEntry(entry, interpreter);
	}
	
	protected void serializer(RecipeSerializer<?> serializer, RecipeInterpreter interpreter)
	{
		JsonObject entry = new JsonObject();
		entry.addProperty("type", "recipe_serializer");
		ResourceLocation serializerId = Objects.requireNonNull(ForgeRegistries.RECIPE_SERIALIZERS.getKey(serializer));
		entry.addProperty("serializer", serializerId.toString());
		addEntry(entry, interpreter);
	}
	
	protected void type(RecipeType<?> type, RecipeInterpreter interpreter)
	{
		JsonObject entry = new JsonObject();
		entry.addProperty("type", "recipe_type");
		entry.addProperty("recipe_type", type.toString());
		addEntry(entry, interpreter);
	}
	
	@SuppressWarnings("unchecked")
	private <T extends RecipeInterpreter> void addEntry(JsonObject source, T interpreter)
	{
		JsonObject entry = new JsonObject();
		entry.add("source", source);
		
		ResourceLocation type = Objects.requireNonNull(InterpreterTypes.REGISTRY.get().getKey(interpreter.codec()));
		JsonObject interpreterObj = new JsonObject();
		interpreterObj.addProperty("type", type.toString());
		interpreterObj.add("value", ((Codec<T>) interpreter.codec()).encodeStart(JsonOps.INSTANCE, interpreter).getOrThrow(false, LOGGER::error));
		entry.add("interpreter", interpreterObj);
		
		entries.add(entry);
	}
	
	@Override
	public String getName()
	{
		return "Minestuck generated grist costs config";
	}
}
