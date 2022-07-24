package com.mraof.minestuck.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.mraof.minestuck.Minestuck;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class StartingModusManager extends SimpleJsonResourceReloadListener
{
	private static final Gson GSON = new GsonBuilder().registerTypeAdapter(BoondollarPricing.class, new BoondollarPricing.Serializer()).create();
	
	private static StartingModusManager INSTANCE;
	private List<String> startingModusTypes;
	
	public StartingModusManager()
	{
		super(GSON, "minestuck/config");
	}
	
	@Override
	protected void apply(Map<ResourceLocation, JsonElement> jsonEntries, ResourceManager resourceManager, ProfilerFiller profiler)
	{
		JsonElement element = jsonEntries.get(new ResourceLocation(Minestuck.MOD_ID, "starting_modus"));
		this.startingModusTypes = GSON.fromJson(element, new TypeToken<ArrayList<String>>(){}.getType());
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
	
	public static List<String> getStartingModusTypes()
	{
		return INSTANCE.startingModusTypes;
	}
}