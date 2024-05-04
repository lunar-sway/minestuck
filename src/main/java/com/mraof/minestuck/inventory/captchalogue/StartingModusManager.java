package com.mraof.minestuck.inventory.captchalogue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class StartingModusManager extends SimplePreparableReloadListener<List<String>>
{
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = new GsonBuilder().create();
	
	public static final String PATH = "minestuck/config/starting_modus.json";
	
	private static StartingModusManager INSTANCE;
	private List<ModusType<?>> startingModusTypes;
	
	@Override
	protected List<String> prepare(ResourceManager resourceManager, ProfilerFiller profiler)
	{
		List<String> modusTypes = new ArrayList<>();
		
		for(String namespace : resourceManager.getNamespaces())
		{
			ResourceLocation location = new ResourceLocation(namespace, PATH);
			resourceManager.getResource(location).ifPresent(resource -> {
				try(Reader reader = resource.openAsReader();
					JsonReader jsonreader = new JsonReader(reader))
				{
					modusTypes.addAll(GSON.getAdapter(new TypeToken<ArrayList<String>>()
					{}).read(jsonreader));
				} catch(IOException ignored)
				{
				} catch(RuntimeException runtimeexception)
				{
					LOGGER.warn("Invalid json in data pack: '{}'", location.toString(), runtimeexception);
				}
			});
		}
		
		return modusTypes;
	}
	
	@Override
	protected void apply(List<String> modusTypes, ResourceManager resourceManager, ProfilerFiller profiler)
	{
		startingModusTypes = modusTypes.stream().map(this::createModus).collect(Collectors.toList());
	}
	
	private ModusType<?> createModus(String key)
	{
		ResourceLocation name = ResourceLocation.tryParse(key);
		
		if(name == null)
			LOGGER.error("Unable to parse starting modus type {} as a resource location!", key);
		
		ModusType<?> modusType = ModusTypes.REGISTRY.get(name);
		
		if(modusType == null)
			LOGGER.error("Unable to get the modus type '{}' from the registry", name);
		
		return modusType;
	}
	
	@SubscribeEvent
	public static void onResourceReload(AddReloadListenerEvent event)
	{
		event.addListener(INSTANCE = new StartingModusManager());
	}
	
	public static List<ModusType<?>> getStartingModusTypes()
	{
		return INSTANCE.startingModusTypes;
	}
}