package com.mraof.minestuck.util;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BoondollarPriceManager extends SimpleJsonResourceReloadListener
{
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = new GsonBuilder().registerTypeAdapter(BoondollarPricing.class, new BoondollarPricing.Serializer()).create();
	
	private List<BoondollarPricing> pricings;
	
	public BoondollarPriceManager()
	{
		super(GSON, "minestuck/boondollar_prices");
	}
	
	private static BoondollarPriceManager INSTANCE;
	
	public static BoondollarPriceManager getInstance()
	{
		return Objects.requireNonNull(INSTANCE);
	}
	
	@Override
	protected void apply(Map<ResourceLocation, JsonElement> jsonEntries, ResourceManager resourceManager, ProfilerFiller profiler)
	{
		ImmutableList.Builder<BoondollarPricing> pricings = ImmutableList.builder();
		for(Map.Entry<ResourceLocation, JsonElement> entry : jsonEntries.entrySet())
		{
			try
			{
				BoondollarPricing pricing = GSON.fromJson(entry.getValue(), BoondollarPricing.class);
				pricings.add(pricing);
			} catch(Exception e)
			{
				LOGGER.error("Couldn't parse boondollar pricing {}", entry.getKey(), e);
			}
		}
		
		this.pricings = pricings.build();
		LOGGER.info("Loaded {} boondollar prices", this.pricings.size());
	}
	
	public Optional<Integer> findPrice(ItemStack stack, RandomSource rand)
	{
		return pricings.stream().filter(pricing -> pricing.appliesTo(stack)).findAny().map(pricing -> pricing.generatePrice(rand));
	}
	
	public static JsonElement parsePrice(BoondollarPricing pricing)
	{
		return GSON.toJsonTree(pricing);
	}
	
	@SubscribeEvent
	public static void onResourceReload(AddReloadListenerEvent event)
	{
		event.addListener(INSTANCE = new BoondollarPriceManager());
	}
}