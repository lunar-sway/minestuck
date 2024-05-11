package com.mraof.minestuck.util;

import com.google.common.collect.ImmutableList;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class BoondollarPriceManager extends SimpleJsonResourceReloadListener
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private List<BoondollarPricing> pricings;
	
	public BoondollarPriceManager()
	{
		super(new GsonBuilder().create(), "minestuck/boondollar_prices");
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
			BoondollarPricing.CODEC.parse(JsonOps.INSTANCE, entry.getValue())
					.resultOrPartial(message -> LOGGER.error("Couldn't parse boondollar pricing {}: {}", entry.getKey(), message))
					.ifPresent(pricings::add);
		}
		
		this.pricings = pricings.build();
		LOGGER.info("Loaded {} boondollar prices", this.pricings.size());
	}
	
	public Optional<Integer> findPrice(ItemStack stack, RandomSource rand)
	{
		return pricings.stream().filter(pricing -> pricing.appliesTo(stack)).findAny().map(pricing -> pricing.generatePrice(rand));
	}
	
	@SubscribeEvent
	public static void onResourceReload(AddReloadListenerEvent event)
	{
		event.addListener(INSTANCE = new BoondollarPriceManager());
	}
}
