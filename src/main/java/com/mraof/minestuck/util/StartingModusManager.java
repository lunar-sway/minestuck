package com.mraof.minestuck.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.data.MinestuckStartingModusProvider;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.inventory.captchalogue.ModusTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class StartingModusManager extends SimplePreparableReloadListener<List<String>>
{
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = new GsonBuilder().registerTypeAdapter(BoondollarPricing.class, new BoondollarPricing.Serializer()).create();
	
	private static StartingModusManager INSTANCE;
	private List<ModusType<?>> startingModusTypes;
	
	@Override
	protected List<String> prepare(ResourceManager resourceManager, ProfilerFiller profiler)
	{
		ResourceLocation location = new ResourceLocation(Minestuck.MOD_ID, MinestuckStartingModusProvider.PATH);
		if(!resourceManager.hasResource(location))
			return null;
		
		try(Resource resource = resourceManager.getResource(location);
			InputStream stream = resource.getInputStream();
			Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
			JsonReader jsonreader = new JsonReader(reader))
		{
			return GSON.getAdapter(new TypeToken<ArrayList<String>>()
			{}).read(jsonreader);
		} catch(IOException ignored)
		{
		} catch(RuntimeException runtimeexception)
		{
			LOGGER.warn("Invalid json in data pack: '{}'", location.toString(), runtimeexception);
		}
		
		return null;
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
		
		return ModusTypes.REGISTRY.getValue(name);
	}
	
	@SubscribeEvent
	public static void onResourceReload(AddReloadListenerEvent event)
	{
		event.addListener(INSTANCE = new StartingModusManager());
	}
	
	public static StartingModusManager getInstance()
	{
		return Objects.requireNonNull(INSTANCE);
	}
	
	public static List<ModusType<?>> getStartingModusTypes()
	{
		return INSTANCE.startingModusTypes;
	}
}