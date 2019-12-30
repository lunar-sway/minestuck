package com.mraof.minestuck.item.crafting.alchemy;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GristCostGenerator extends ReloadListener<List<GristCostGenerator.Source>>
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final Gson GSON = (new GsonBuilder()).registerTypeAdapter(Source.class, (JsonDeserializer<Source>) GristCostGenerator::deserializeSource).create();
	
	private final RecipeManager recipeManager;
	
	public GristCostGenerator(RecipeManager recipeManager)
	{
		this.recipeManager = recipeManager;
	}
	
	@Override
	protected List<Source> prepare(IResourceManager resourceManagerIn, IProfiler profilerIn)
	{
		List<Source> sources = new ArrayList<>();
		for(String namespace : resourceManagerIn.getResourceNamespaces())
		{
			try
			{
				if(resourceManagerIn.hasResource(new ResourceLocation(namespace, "minestuck/grist_cost_generation.json")))
				{
					IResource resource = resourceManagerIn.getResource(new ResourceLocation(namespace, "minestuck/grist_cost_generation.json"));
					try
					{
						List<Source> namespaceEntries = readEntries(resource.getInputStream());
						
						sources.addAll(namespaceEntries);
						
					} catch(RuntimeException runtimeexception)
					{
						LOGGER.warn("Invalid grist_cost_generation.json in data pack: '{}'", resource.getPackName(), runtimeexception);
					}
				}
			} catch(IOException ignored)
			{}
		}
		
		return sources;
	}
	
	@Override
	protected void apply(List<Source> sources, IResourceManager resourceManagerIn, IProfiler profilerIn)
	{
		int recipeCount = recipeManager.getRecipes().size();
		LOGGER.info("Recipe sources loaded: {}", sources);
		if(recipeCount > 0)
			LOGGER.info("Recipes have loaded at this time.");
		else LOGGER.warn("Recipes have not loaded at this time!");
		
		//TODO Is it actually safe to generate costs here, or should it be done elsewhere/lazily?
	}
	
	private static List<Source> readEntries(InputStream input)
	{
		List<Source> sources;
		try
		{
			Type type = new TypeToken<List<Source>>(){}.getType();
			sources = JSONUtils.fromJson(GSON, new InputStreamReader(input), type);
		} finally
		{
			IOUtils.closeQuietly(input);
		}
		return sources;
	}
	
	private static Source deserializeSource(JsonElement json, Type typeOfT, JsonDeserializationContext context)
	{
		JsonObject jsonObj = JSONUtils.getJsonObject(json, "source");
		
		String type = JSONUtils.getString(jsonObj, "type");
		if(type.equals("recipe"))
		{
			ResourceLocation recipe = new ResourceLocation(JSONUtils.getString(jsonObj, "value"));
			return new RecipeSource(recipe);
		} else if(type.equals("recipe_serializer"))
		{
			ResourceLocation serializerName = new ResourceLocation(JSONUtils.getString(jsonObj, "value"));
			IRecipeSerializer<?> recipeSerializer = ForgeRegistries.RECIPE_SERIALIZERS.getValue(serializerName);
			if(recipeSerializer == null)
				throw new JsonParseException("No recipe type by name " + serializerName);
			return new RecipeSerializerSource(recipeSerializer);
		} else if(type.equals("recipe_type"))
		{
			ResourceLocation typeName = new ResourceLocation(JSONUtils.getString(jsonObj, "value"));
			IRecipeType<?> recipeType = Registry.RECIPE_TYPE.getValue(typeName).orElseThrow(() -> new JsonParseException("No recipe type by name " + typeName));
			return new RecipeTypeSource(recipeType);
		}
		throw new JsonParseException("Invalid source type " + type);
	}
	
	static abstract class Source
	{
	
	}
	
	private static class RecipeSource extends Source
	{
		private final ResourceLocation recipe;
		
		private RecipeSource(ResourceLocation recipe)
		{
			this.recipe = Objects.requireNonNull(recipe);
		}
		
		@Override
		public String toString()
		{
			return "recipe_source[recipe="+recipe+"]";
		}
	}
	
	private static class RecipeSerializerSource extends Source
	{
		private final IRecipeSerializer<?> serializer;
		
		private RecipeSerializerSource(IRecipeSerializer<?> serializer)
		{
			this.serializer = Objects.requireNonNull(serializer);
		}
		
		@Override
		public String toString()
		{
			return "recipe_source[serializer="+serializer.getRegistryName()+"]";
		}
	}
	
	private static class RecipeTypeSource extends Source
	{
		private final IRecipeType<?> recipeType;
		
		private RecipeTypeSource(IRecipeType<?> recipeType)
		{
			this.recipeType = Objects.requireNonNull(recipeType);
		}
		
		@Override
		public String toString()
		{
			return "recipe_source[type="+recipeType+"]";
		}
	}
}