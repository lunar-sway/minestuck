package com.mraof.minestuck.item.crafting.alchemy;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.server.MinecraftServer;
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
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GristCostGenerator extends ReloadListener<List<GristCostGenerator.SourceEntry>>
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final Gson GSON = (new GsonBuilder()).registerTypeAdapter(SourceEntry.class, (JsonDeserializer<SourceEntry>) GristCostGenerator::deserializeSourceEntry).registerTypeAdapter(Source.class, (JsonDeserializer<Source>) GristCostGenerator::deserializeSource).create();
	
	private final MinecraftServer server;
	private Map<Item, GristSet> generatedCosts = Collections.emptyMap();	//TODO grist cost recipe that queries the results here
	
	public GristCostGenerator(MinecraftServer server)
	{
		this.server = server;
	}
	
	@Override
	protected List<SourceEntry> prepare(IResourceManager resourceManagerIn, IProfiler profilerIn)
	{
		List<SourceEntry> sources = new ArrayList<>();
		for(String namespace : resourceManagerIn.getResourceNamespaces())
		{
			try
			{
				if(resourceManagerIn.hasResource(new ResourceLocation(namespace, "minestuck/grist_cost_generation.json")))
				{
					IResource resource = resourceManagerIn.getResource(new ResourceLocation(namespace, "minestuck/grist_cost_generation.json"));
					try
					{
						List<SourceEntry> namespaceEntries = readEntries(resource.getInputStream());
						
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
	
	private static List<SourceEntry> readEntries(InputStream input)
	{
		List<SourceEntry> sources;
		try
		{
			Type type = new TypeToken<List<SourceEntry>>(){}.getType();
			sources = JSONUtils.fromJson(GSON, new InputStreamReader(input), type);
		} finally
		{
			IOUtils.closeQuietly(input);
		}
		return sources;
	}
	
	private static SourceEntry deserializeSourceEntry(JsonElement json, Type typeOfT, JsonDeserializationContext context)
	{
		Source source = GSON.getAdapter(Source.class).fromJsonTree(json);
		//TODO Implement recipe interpreter serializer registry (similar to recipe serializers)
		return new SourceEntry(source, DefaultInterpreter.INSTANCE);
	}
	
	private static Source deserializeSource(JsonElement json, Type typeOfT, JsonDeserializationContext context)
	{
		JsonObject jsonObj = JSONUtils.getJsonObject(json, "source");
		
		String type = JSONUtils.getString(jsonObj, "type");
		switch(type)
		{
			case "recipe":
				ResourceLocation recipe = new ResourceLocation(JSONUtils.getString(jsonObj, "value"));
				return new RecipeSource(recipe);
			case "recipe_serializer":
				ResourceLocation serializerName = new ResourceLocation(JSONUtils.getString(jsonObj, "value"));
				IRecipeSerializer<?> recipeSerializer = ForgeRegistries.RECIPE_SERIALIZERS.getValue(serializerName);
				if(recipeSerializer == null)
					throw new JsonParseException("No recipe type by name " + serializerName);
				return new RecipeSerializerSource(recipeSerializer);
			case "recipe_type":
				ResourceLocation typeName = new ResourceLocation(JSONUtils.getString(jsonObj, "value"));
				IRecipeType<?> recipeType = Registry.RECIPE_TYPE.getValue(typeName).orElseThrow(() -> new JsonParseException("No recipe type by name " + typeName));
				return new RecipeTypeSource(recipeType);
		}
		throw new JsonParseException("Invalid source type " + type);
	}
	
	@Override
	protected void apply(List<SourceEntry> sources, IResourceManager resourceManagerIn, IProfiler profilerIn)
	{
		RecipeManager recipeManager = server.getRecipeManager();
		if(!server.isOnExecutionThread() || (sources.size() != 0 &&  recipeManager.getRecipes().size() == 0))
		{
			throw new IllegalStateException("Grist cost generator is supposed to be executed on server thread after initializing. The failure of this assertion is not good!");
		}
		
		GeneratorProcess process = new GeneratorProcess(recipeManager, prepareRecipeMap(sources, recipeManager));
		
		List<Item> items = new ArrayList<>(process.lookupMap.keySet());
		for(Item item : items)
		{
			GristSet cost = costForItem(process, item);
			if(!process.gristCostLookup.containsKey(item))
				process.generatedCosts.put(item, cost);
		}
		
		this.generatedCosts = ImmutableMap.copyOf(process.generatedCosts);
	}
	
	private Map<Item, List<Pair<IRecipe<?>, RecipeInterpreter>>> prepareRecipeMap(List<SourceEntry> sources, RecipeManager recipeManager)
	{
		//TODO
		// Should return a mutable map for fast lookup of item -> recipes + recipe-interpreter
		// Each recipe should only occur once
		// Each item may refer to several recipes if there are recipes for them,
		// but each recipe should only have one recipe-interpreter depending on which source is the dominant source.
		// The dominant source should be the most specific source (fewest recipes provided by that source).
		// If there are equally dominant sources, we won't bother to make a distinction and will instead pick whichever is more convenient implementation-wise.
		
		return Collections.emptyMap();
	}
	
	private GristSet costFromRecipes(GeneratorProcess process, Item item)
	{
		GristSet minCost = null;
		for(Pair<IRecipe<?>, RecipeInterpreter> recipePair : process.lookupMap.getOrDefault(item, Collections.emptyList()))
		{
			GristSet cost = costForRecipe(process, recipePair);
			if(cost != null && (minCost == null || cost.getValue() < minCost.getValue()))
				minCost = cost;
		}
		return minCost;
	}
	
	private GristSet costForRecipe(GeneratorProcess process, Pair<IRecipe<?>, RecipeInterpreter> recipePair)
	{
		return recipePair.getSecond().generateCost(recipePair.getFirst(), ingredient -> costForIngredient(process, ingredient));
	}
	
	private GristSet costForIngredient(GeneratorProcess process, Ingredient ingredient)
	{
		GristSet minCost = null;
		for(ItemStack stack : ingredient.getMatchingStacks())
		{
			if(ingredient.test(new ItemStack(stack.getItem())))
			{
				GristSet cost = costForItem(process, stack.getItem());
				if(cost != null && (minCost == null || cost.getValue() < minCost.getValue()))
					minCost = cost;
			}
		}
		return minCost;
	}
	
	private GristSet costForItem(GeneratorProcess process, Item item)
	{
		if(!process.itemsInProcess.contains(item))
		{
			//Look up previous results
			if(process.generatedCosts.containsKey(item))
				return process.generatedCosts.get(item);
			
			//Look up regular grist cost recipe
			if(!process.hasDoneGristCostLookup.contains(item))
			{
				Optional<GristCostRecipe> recipe = GristCostRecipe.findRecipeForItem(new ItemStack(item), null, process.recipeManager);
				process.hasDoneGristCostLookup.add(item);
				if(recipe.isPresent())
				{
					GristSet cost = recipe.get().getGristCost(new ItemStack(item), GristTypes.BUILD, false);
					process.gristCostLookup.put(item, cost);
					return cost;
				}
			} else
			{
				if(process.hasDoneGristCostLookup.contains(item))
					return process.gristCostLookup.get(item);
			}
			
			//If it doesn't already have a cost from elsewhere, find cost from its recipes
			process.itemsInProcess.add(item);
			GristSet cost = costFromRecipes(process, item);
			process.itemsInProcess.remove(item);
			return cost;
		}
		return null;
	}
	
	static class GeneratorProcess
	{
		private final RecipeManager recipeManager;
		private final Map<Item, List<Pair<IRecipe<?>, RecipeInterpreter>>> lookupMap;
		private final Map<Item, GristSet> generatedCosts = new HashMap<>();
		private final Set<Item> hasDoneGristCostLookup = new HashSet<>();
		private final Map<Item, GristSet> gristCostLookup = new HashMap<>();
		private final Set<Item> itemsInProcess = new HashSet<>();
		
		public GeneratorProcess(RecipeManager recipeManager, Map<Item, List<Pair<IRecipe<?>, RecipeInterpreter>>> lookupMap)
		{
			this.recipeManager = recipeManager;
			this.lookupMap = lookupMap;
		}
	}
	
	static class SourceEntry
	{
		private Source source;
		private RecipeInterpreter interpreter;
		
		private SourceEntry(Source source, RecipeInterpreter interpreter)
		{
			this.source = source;
			this.interpreter = interpreter;
		}
	}
	
	public interface Source
	{
		List<IRecipe<?>> findRecipes(RecipeManager recipeManager);
	}
	
	private static class RecipeSource implements Source
	{
		private final ResourceLocation recipe;
		
		private RecipeSource(ResourceLocation recipe)
		{
			this.recipe = Objects.requireNonNull(recipe);
		}
		
		@Override
		public List<IRecipe<?>> findRecipes(RecipeManager recipeManager)
		{
			Optional<? extends IRecipe<?>> recipe = recipeManager.getRecipe(this.recipe).filter(iRecipe -> !iRecipe.isDynamic());
			return recipe.<List<IRecipe<?>>>map(Collections::singletonList).orElse(Collections.emptyList());
		}
		
		@Override
		public String toString()
		{
			return "recipe_source[recipe="+recipe+"]";
		}
	}
	
	private static class RecipeSerializerSource implements Source
	{
		private final IRecipeSerializer<?> serializer;
		
		private RecipeSerializerSource(IRecipeSerializer<?> serializer)
		{
			this.serializer = Objects.requireNonNull(serializer);
		}
		
		@Override
		public List<IRecipe<?>> findRecipes(RecipeManager recipeManager)
		{
			return recipeManager.getRecipes().stream().filter(iRecipe -> iRecipe.getSerializer() == serializer).filter(iRecipe -> !iRecipe.isDynamic()).collect(Collectors.toList());
		}
		
		@Override
		public String toString()
		{
			return "recipe_source[serializer="+serializer.getRegistryName()+"]";
		}
	}
	
	private static class RecipeTypeSource implements Source
	{
		private final IRecipeType<?> recipeType;
		
		private RecipeTypeSource(IRecipeType<?> recipeType)
		{
			this.recipeType = Objects.requireNonNull(recipeType);
		}
		
		@Override
		public List<IRecipe<?>> findRecipes(RecipeManager recipeManager)
		{
			return recipeManager.getRecipes().stream().filter(iRecipe -> iRecipe.getType() == recipeType).filter(iRecipe -> !iRecipe.isDynamic()).collect(Collectors.toList());
		}
		
		@Override
		public String toString()
		{
			return "recipe_source[type="+recipeType+"]";
		}
	}
	
	public interface RecipeInterpreter
	{
		GristSet generateCost(IRecipe<?> recipes, Function<Ingredient, GristSet> ingredientInterpreter);
	}
}