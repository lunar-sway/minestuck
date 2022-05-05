package com.mraof.minestuck.item.crafting.alchemy.generator;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.jei.JeiGristCost;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RecipeGeneratedCostHandler extends SimplePreparableReloadListener<List<RecipeGeneratedCostHandler.SourceEntry>> implements GeneratedCostProvider
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final Gson GSON = (new GsonBuilder()).registerTypeAdapter(SourceEntry.class, (JsonDeserializer<SourceEntry>) RecipeGeneratedCostHandler::deserializeSourceEntry).create();
	
	public static final String PATH = "minestuck/grist_cost_generation_recipes.json";
	
	private final RecipeManager recipeManager;
	private Map<Item, GristSet> generatedCosts = Collections.emptyMap();
	private RecipeGeneratedCostProcess process = null;
	
	private RecipeGeneratedCostHandler(RecipeManager recipeManager)
	{
		this.recipeManager = recipeManager;
	}
	
	private RecipeGeneratedCostHandler(Map<Item, GristSet> generatedCosts)
	{
		recipeManager = null;
		this.generatedCosts = generatedCosts;
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void addListener(AddReloadListenerEvent event)
	{
		event.addListener(new RecipeGeneratedCostHandler(event.getServerResources().getRecipeManager()));
	}
	
	GristSet getGristCost(Item item)
	{
		return generatedCosts.get(item);
	}
	
	void write(FriendlyByteBuf buffer)
	{
		buffer.writeInt(generatedCosts.size());
		for(Map.Entry<Item, GristSet> entry : generatedCosts.entrySet())
		{
			buffer.writeVarInt(Item.getId(entry.getKey()));
			entry.getValue().write(buffer);
		}
	}
	
	static RecipeGeneratedCostHandler read(FriendlyByteBuf buffer)
	{
		if(buffer.readableBytes() == 0)
			return null;
		
		int size = buffer.readInt();
		ImmutableMap.Builder<Item, GristSet> builder = new ImmutableMap.Builder<>();
		for(int i = 0; i < size; i++)
		{
			Item item = Item.byId(buffer.readVarInt());
			GristSet cost = GristSet.read(buffer);
			builder.put(item, cost);
		}
		return new RecipeGeneratedCostHandler(builder.build());
	}
	
	List<JeiGristCost> createJeiCosts()
	{
		List<JeiGristCost> costs = new ArrayList<>();
		for(Map.Entry<Item, GristSet> entries : generatedCosts.entrySet())
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
			try
			{
				if(resourceManagerIn.hasResource(new ResourceLocation(namespace, PATH)))
				{
					Resource resource = resourceManagerIn.getResource(new ResourceLocation(namespace, PATH));
					try(
							InputStream stream = resource.getInputStream();
							Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)
					)
					{
						sources.addAll(readEntries(reader));
						
					} catch(RuntimeException runtimeexception)
					{
						LOGGER.warn("Invalid grist_cost_generation.json in data pack: '{}'", resource.getSourceName(), runtimeexception);
					}
				}
			} catch(IOException ignored)
			{}
		}
		
		return sources;
	}
	
	private static List<SourceEntry> readEntries(Reader reader)
	{
		TypeToken<List<SourceEntry>> type = new TypeToken<List<SourceEntry>>(){};
		try
		{
			JsonReader jsonreader = new JsonReader(reader);
			jsonreader.setLenient(false);
			
			return GSON.getAdapter(type).read(jsonreader);
		} catch (IOException e)
		{
			throw new JsonParseException(e);
		}
	}
	
	private static SourceEntry deserializeSourceEntry(JsonElement json, Type typeOfT, JsonDeserializationContext context)
	{
		JsonObject obj = GsonHelper.convertToJsonObject(json, "source entry");
		
		Source source = deserializeSource(obj);
		
		ResourceLocation name = new ResourceLocation(GsonHelper.getAsString(obj, "interpreter_type"));
		InterpreterSerializer<?> serializer = InterpreterSerializer.REGISTRY.getValue(name);
		
		if(serializer == null)
		{
			LOGGER.error("No interpreter serializer by name {}. Using default interpreter instead.", name);
			return new SourceEntry(source, DefaultInterpreter.INSTANCE);
		} else return new SourceEntry(source, serializer.read(obj.get("interpreter")));
	}
	
	private static Source deserializeSource(JsonObject json)
	{
		String type = GsonHelper.getAsString(json, "source_type");
		switch(type)
		{
			case "recipe":
				ResourceLocation recipe = new ResourceLocation(GsonHelper.getAsString(json, "source"));
				return new RecipeSource(recipe);
			case "recipe_serializer":
				ResourceLocation serializerName = new ResourceLocation(GsonHelper.getAsString(json, "source"));
				RecipeSerializer<?> recipeSerializer = ForgeRegistries.RECIPE_SERIALIZERS.getValue(serializerName);
				if(recipeSerializer == null)
					throw new JsonParseException("No recipe type by name " + serializerName);
				return new RecipeSerializerSource(recipeSerializer);
			case "recipe_type":
				ResourceLocation typeName = new ResourceLocation(GsonHelper.getAsString(json, "source"));
				RecipeType<?> recipeType = Registry.RECIPE_TYPE.getOptional(typeName).orElseThrow(() -> new JsonParseException("No recipe type by name " + typeName));
				return new RecipeTypeSource(recipeType);
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
	public GristCostResult generate(Item item, GristCostResult lastCost, GenerationContext context)
	{
		return process.generateCost(item, lastCost, context);
	}
	
	@Override
	public void build()
	{
		if(process != null)
		{
			generatedCosts = process.buildMap();
			process = null;
			LOGGER.info("Generated {} grist conversions from recipes.", generatedCosts.size());
		} else throw new IllegalStateException("Tried to build recipe-generated costs, but did not have an ongoing process!");
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
	
	static class SourceEntry
	{
		private final Source source;
		private final RecipeInterpreter interpreter;
		
		private SourceEntry(Source source, RecipeInterpreter interpreter)
		{
			this.source = source;
			this.interpreter = interpreter;
		}
	}
	
	public interface Source
	{
		List<Recipe<?>> findRecipes(RecipeManager recipeManager);
	}
	
	private static class RecipeSource implements Source
	{
		private final ResourceLocation recipe;
		
		private RecipeSource(ResourceLocation recipe)
		{
			this.recipe = Objects.requireNonNull(recipe);
		}
		
		@Override
		public List<Recipe<?>> findRecipes(RecipeManager recipeManager)
		{
			Optional<? extends Recipe<?>> recipe = recipeManager.byKey(this.recipe);
			return recipe.<List<Recipe<?>>>map(Collections::singletonList).orElse(Collections.emptyList());
		}
		
		@Override
		public String toString()
		{
			return "recipe_source[recipe="+recipe+"]";
		}
	}
	
	private static class RecipeSerializerSource implements Source
	{
		private final RecipeSerializer<?> serializer;
		
		private RecipeSerializerSource(RecipeSerializer<?> serializer)
		{
			this.serializer = Objects.requireNonNull(serializer);
		}
		
		@Override
		public List<Recipe<?>> findRecipes(RecipeManager recipeManager)
		{
			return recipeManager.getRecipes().stream().filter(iRecipe -> iRecipe.getSerializer() == serializer).collect(Collectors.toList());
		}
		
		@Override
		public String toString()
		{
			return "recipe_source[serializer="+serializer.getRegistryName()+"]";
		}
	}
	
	private static class RecipeTypeSource implements Source
	{
		private final RecipeType<?> recipeType;
		
		private RecipeTypeSource(RecipeType<?> recipeType)
		{
			this.recipeType = Objects.requireNonNull(recipeType);
		}
		
		@Override
		public List<Recipe<?>> findRecipes(RecipeManager recipeManager)
		{
			return recipeManager.getRecipes().stream().filter(iRecipe -> iRecipe.getType() == recipeType).collect(Collectors.toList());
		}
		
		@Override
		public String toString()
		{
			return "recipe_source[type="+recipeType+"]";
		}
	}
}