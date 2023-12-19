package com.mraof.minestuck.util;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ComputerThemeDataManager extends SimpleJsonResourceReloadListener
{
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = new GsonBuilder().registerTypeAdapter(ComputerThemeData.class, new ComputerThemeData.Serializer()).create();
	
	private List<ComputerThemeData> pricings;
	
	public ComputerThemeDataManager()
	{
		super(GSON, "minestuck/computer_themes");
	}
	
	private static ComputerThemeDataManager INSTANCE;
	
	public static ComputerThemeDataManager getInstance()
	{
		return Objects.requireNonNull(INSTANCE);
	}
	
	@Override
	protected void apply(Map<ResourceLocation, JsonElement> jsonEntries, ResourceManager resourceManager, ProfilerFiller profiler)
	{
		ImmutableList.Builder<ComputerThemeData> pricings = ImmutableList.builder();
		for(Map.Entry<ResourceLocation, JsonElement> entry : jsonEntries.entrySet())
		{
			try
			{
				ComputerThemeData pricing = GSON.fromJson(entry.getValue(), ComputerThemeData.class);
				pricings.add(pricing);
			} catch(Exception e)
			{
				LOGGER.error("Couldn't parse computer theme {}", entry.getKey(), e);
			}
		}
		
		this.pricings = pricings.build();
		LOGGER.info("Loaded {} computer themes", this.pricings.size());
	}
	
	public Optional<ResourceLocation> findTexturePath(String name)
	{
		return pricings.stream().filter(pricings -> pricings.appliesTo(pricings.getTexturePath())).findAny().map(ComputerThemeData::getTexturePath);
	}
	
	public Optional<Integer> findTextColor(ResourceLocation stack)
	{
		return pricings.stream().filter(pricings -> pricings.appliesTo(stack)).findAny().map(ComputerThemeData::getTextColor);
	}
	
	public static JsonElement parsePrice(ComputerThemeData pricing)
	{
		return GSON.toJsonTree(pricing);
	}
	
	@SubscribeEvent
	public static void onResourceReload(AddReloadListenerEvent event)
	{
		event.addListener(INSTANCE = new ComputerThemeDataManager());
	}
}