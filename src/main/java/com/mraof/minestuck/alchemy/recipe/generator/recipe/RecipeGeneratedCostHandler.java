package com.mraof.minestuck.alchemy.recipe.generator.recipe;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.ImmutableGristSet;
import com.mraof.minestuck.api.alchemy.recipe.JeiGristCost;
import com.mraof.minestuck.api.alchemy.recipe.generator.GeneratedCostProvider;
import com.mraof.minestuck.api.alchemy.recipe.generator.GeneratorCallback;
import com.mraof.minestuck.api.alchemy.recipe.generator.GristCostResult;
import com.mraof.minestuck.api.alchemy.recipe.generator.LookupTracker;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * A portion of the grist cost generation process responsible for generating grist costs from recipes.
 * Loads a configuration file to determine which types of recipes should be processed and in which manner.
 * Any produced grist costs are then made available through a {@link RecipeGeneratedGristCost} instance.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RecipeGeneratedCostHandler extends SimplePreparableReloadListener<List<RecipeGeneratedCostHandler.SourceEntry>> implements GeneratedCostProvider
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final Gson GSON = (new GsonBuilder()).registerTypeAdapter(SourceEntry.class, (JsonDeserializer<SourceEntry>) RecipeGeneratedCostHandler::deserializeSourceEntry).create();
	
	public static final String PATH = "minestuck/grist_cost_generation_recipes.json";
	
	private final RecipeManager recipeManager;
	private Map<Item, ImmutableGristSet> generatedCosts = Collections.emptyMap();
	private RecipeGeneratedCostProcess process = null;
	
	private RecipeGeneratedCostHandler(RecipeManager recipeManager)
	{
		this.recipeManager = recipeManager;
	}
	
	private RecipeGeneratedCostHandler(Map<Item, ImmutableGristSet> generatedCosts)
	{
		recipeManager = null;
		this.generatedCosts = generatedCosts;
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void addListener(AddReloadListenerEvent event)
	{
		event.addListener(new RecipeGeneratedCostHandler(event.getServerResources().getRecipeManager()));
	}
	
	/**
	 * Returns an immutable map of all grist costs generated from recipes.
	 * If called before grist cost generation has finished, an empty map will be returned.
	 */
	public Map<Item, ImmutableGristSet> getMap()
	{
		return generatedCosts;
	}
	
	void write(FriendlyByteBuf buffer)
	{
		buffer.writeInt(generatedCosts.size());
		for(Map.Entry<Item, ImmutableGristSet> entry : generatedCosts.entrySet())
		{
			buffer.writeVarInt(Item.getId(entry.getKey()));
			GristSet.write(entry.getValue(), buffer);
		}
	}
	
	@Nullable
	static RecipeGeneratedCostHandler read(FriendlyByteBuf buffer)
	{
		if(buffer.readableBytes() == 0)
			return null;
		
		int size = buffer.readInt();
		ImmutableMap.Builder<Item, ImmutableGristSet> builder = new ImmutableMap.Builder<>();
		for(int i = 0; i < size; i++)
		{
			Item item = Item.byId(buffer.readVarInt());
			ImmutableGristSet cost = GristSet.read(buffer);
			builder.put(item, cost);
		}
		return new RecipeGeneratedCostHandler(builder.build());
	}
	
	List<JeiGristCost> createJeiCosts()
	{
		List<JeiGristCost> costs = new ArrayList<>();
		for(Map.Entry<Item, ImmutableGristSet> entries : generatedCosts.entrySet())
		{
			if(entries.getValue() != null)
				costs.add(new JeiGristCost.Set(Ingredient.of(entries.getKey()), entries.getValue()));
		}
		return costs;
	}
	
	@Override
	protected List<SourceEntry> prepare(ResourceManager resourceManagerIn, ProfilerFiller profilerIn)
	{
		List<SourceEntry> sources = new ArrayList<>();
		for(String namespace : resourceManagerIn.getNamespaces())
		{
			resourceManagerIn.getResource(new ResourceLocation(namespace, PATH)).ifPresent(resource -> {
				try
				{
					try(
							Reader reader = resource.openAsReader()
					)
					{
						sources.addAll(readEntries(reader));
						
					} catch(RuntimeException runtimeexception)
					{
						LOGGER.warn("Invalid grist_cost_generation.json in data pack: '{}'", resource.sourcePackId(), runtimeexception);
					}
				} catch(IOException ignored)
				{
				}
			});
		}
		
		return sources;
	}
	
	private static List<SourceEntry> readEntries(Reader reader)
	{
		TypeToken<List<SourceEntry>> type = new TypeToken<>()
		{
		};
		try
		{
			JsonReader jsonreader = new JsonReader(reader);
			jsonreader.setLenient(false);
			
			return GSON.getAdapter(type).read(jsonreader);
		} catch(IOException e)
		{
			throw new JsonParseException(e);
		}
	}
	
	private static SourceEntry deserializeSourceEntry(JsonElement json, Type typeOfT, JsonDeserializationContext context)
	{
		JsonObject obj = GsonHelper.convertToJsonObject(json, "source entry");
		
		RecipeSource source = deserializeSource(GsonHelper.getAsJsonObject(obj, "source"));
		
		RecipeInterpreter interpreter = RecipeInterpreter.DISPATCH_CODEC.parse(JsonOps.INSTANCE, obj.get("interpreter"))
				.resultOrPartial(LOGGER::error).orElse(DefaultInterpreter.INSTANCE);
		
		return new SourceEntry(source, interpreter);
	}
	
	private static RecipeSource deserializeSource(JsonObject json)
	{
		String type = GsonHelper.getAsString(json, "type");
		switch(type)
		{
			case "recipe" ->
			{
				ResourceLocation recipe = new ResourceLocation(GsonHelper.getAsString(json, "recipe"));
				return new RecipeSource.SingleRecipe(recipe);
			}
			case "recipe_serializer" ->
			{
				ResourceLocation serializerName = new ResourceLocation(GsonHelper.getAsString(json, "serializer"));
				RecipeSerializer<?> recipeSerializer = ForgeRegistries.RECIPE_SERIALIZERS.getValue(serializerName);
				if(recipeSerializer == null)
					throw new JsonParseException("No recipe type by name " + serializerName);
				return new RecipeSource.BySerializer(recipeSerializer);
			}
			case "recipe_type" ->
			{
				ResourceLocation typeName = new ResourceLocation(GsonHelper.getAsString(json, "recipe_type"));
				RecipeType<?> recipeType = ForgeRegistries.RECIPE_TYPES.getValue(typeName);
				if(recipeType == null)
					throw new JsonParseException("No recipe type by name " + typeName);
				return new RecipeSource.ByType(recipeType);
			}
		}
		throw new JsonParseException("Invalid source type " + type);
	}
	
	@Override
	protected void apply(List<SourceEntry> sources, ResourceManager resourceManagerIn, ProfilerFiller profilerIn)
	{
		Objects.requireNonNull(recipeManager, "Recipe manager was null while generating grist costs");
		
		if(/*!ServerLifecycleHooks.getCurrentServer().isOnExecutionThread() || */(sources.size() != 0 && recipeManager.getRecipes().size() == 0))
		{
			throw new IllegalStateException("Grist cost generator is supposed to be executed on server thread after initializing. The failure of this assertion is not good!");
		}
		
		this.process = new RecipeGeneratedCostProcess(prepareRecipeMap(sources, recipeManager));
		
		for(Recipe<?> recipe : recipeManager.getRecipes())
		{
			if(recipe instanceof RecipeGeneratedGristCost)
			{
				((RecipeGeneratedGristCost) recipe).setHandler(this);
				return;
			}
		}
		
		process = null;
		if(!sources.isEmpty())
			LOGGER.warn("Did not find a recipe for recipe generated grist costs. Grist costs will not be generated!");
	}
	
	void addAsProvider(BiConsumer<Item, GeneratedCostProvider> consumer)
	{
		for(Item item : process.itemSet())
			consumer.accept(item, this);
	}
	
	@Override
	public GristCostResult generate(Item item, GeneratorCallback callback)
	{
		return process.generateCost(item, callback);
	}
	
	@Override
	public void onCostFromOtherRecipe(Item item, GristCostResult lastCost, GeneratorCallback callback)
	{
		this.process.onCostFromOtherRecipe(item, lastCost, callback);
	}
	
	@Override
	public void build()
	{
		if(process != null)
		{
			generatedCosts = process.buildMap();
			process = null;
			LOGGER.info("Generated {} grist conversions from recipes.", generatedCosts.size());
		} else
			throw new IllegalStateException("Tried to build recipe-generated costs, but did not have an ongoing process!");
	}
	
	@Override
	public void reportPreliminaryLookups(LookupTracker tracker)
	{
		this.process.reportPreliminaryLookups(tracker);
	}
	
	/**
	 * Should return a map for fast lookup of item -> recipes + recipe-interpreter
	 * Each recipe should only occur once for each item returned by interpreter.getOutputItems(recipe)
	 * Each item may refer to several recipes if there are recipes for them,
	 * but each recipe should only have one recipe-interpreter depending on which source is the dominant source.
	 * The dominant source should be the most specific source (fewest recipes provided by that source).
	 * If there are equally dominant sources, we won't bother to make a distinction and will instead pick whichever is more convenient implementation-wise.
	 */
	private Map<Item, List<Pair<Recipe<?>, RecipeInterpreter>>> prepareRecipeMap(List<SourceEntry> sources, RecipeManager recipeManager)
	{
		//Step 1: sort recipe interpreters paired with their recipes in the order depending on the number of recipes in the list
		List<Pair<List<Recipe<?>>, RecipeInterpreter>> recipeLists = new ArrayList<>(sources.size());
		for(SourceEntry entry : sources)
		{
			List<Recipe<?>> recipes = entry.source.findRecipes(recipeManager);
			recipeLists.add(Pair.of(recipes, entry.interpreter));
		}
		recipeLists.sort(Comparator.comparingInt(pair -> -pair.getLeft().size()));
		
		//Step 2: Map recipes to interpreters such that each recipe only has one interpreter
		Map<Recipe<?>, RecipeInterpreter> recipeMap = new HashMap<>();
		for(Pair<List<Recipe<?>>, RecipeInterpreter> pair : recipeLists)
		{
			for(Recipe<?> recipe : pair.getLeft())
				recipeMap.put(recipe, pair.getRight());
		}
		
		//Step 3: Take items from interpreter.getOutputItems() and map item -> recipe interpreter pair
		Map<Item, List<Pair<Recipe<?>, RecipeInterpreter>>> itemLookupMap = new HashMap<>();
		for(Map.Entry<Recipe<?>, RecipeInterpreter> entry : recipeMap.entrySet())
		{
			for(Item item : entry.getValue().getOutputItems(entry.getKey()))
			{
				itemLookupMap.computeIfAbsent(item, item1 -> new ArrayList<>()).add(Pair.of(entry.getKey(), entry.getValue()));
			}
		}
		
		return itemLookupMap;
	}
	
	public static class SourceEntry
	{
		private final RecipeSource source;
		private final RecipeInterpreter interpreter;
		
		private SourceEntry(RecipeSource source, RecipeInterpreter interpreter)
		{
			this.source = source;
			this.interpreter = interpreter;
		}
	}
}
