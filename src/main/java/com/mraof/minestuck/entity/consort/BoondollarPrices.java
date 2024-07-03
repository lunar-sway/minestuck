package com.mraof.minestuck.entity.consort;

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
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class BoondollarPrices
{
	private final List<BoondollarPriceRecipe> prices;
	
	private BoondollarPrices(List<BoondollarPriceRecipe> prices)
	{
		this.prices = prices;
	}
	
	public Optional<Integer> findPrice(ItemStack stack, RandomSource rand)
	{
		return prices.stream().filter(pricing -> pricing.appliesTo(stack)).findAny().map(pricing -> pricing.generatePrice(rand));
	}
	
	private static BoondollarPrices INSTANCE;
	
	public static BoondollarPrices getInstance()
	{
		return Objects.requireNonNull(INSTANCE);
	}
	
	@SubscribeEvent
	private static void onResourceReload(AddReloadListenerEvent event)
	{
		event.addListener(new BoondollarPrices.Loader());
	}
	
	@SubscribeEvent
	private static void onServerStopped(ServerStoppedEvent event)
	{
		INSTANCE = null;
	}
	
	private static final class Loader extends SimpleJsonResourceReloadListener
	{
		private static final Logger LOGGER = LogManager.getLogger();
		
		Loader()
		{
			super(new GsonBuilder().create(), "minestuck/boondollar_prices");
		}
		
		@Override
		protected void apply(Map<ResourceLocation, JsonElement> jsonEntries, ResourceManager resourceManager, ProfilerFiller profiler)
		{
			ImmutableList.Builder<BoondollarPriceRecipe> prices = ImmutableList.builder();
			for(Map.Entry<ResourceLocation, JsonElement> entry : jsonEntries.entrySet())
			{
				BoondollarPriceRecipe.CODEC.parse(JsonOps.INSTANCE, entry.getValue())
						.resultOrPartial(message -> LOGGER.error("Couldn't parse boondollar pricing {}: {}", entry.getKey(), message))
						.ifPresent(prices::add);
			}
			
			INSTANCE = new BoondollarPrices(prices.build());
			LOGGER.info("Loaded {} boondollar prices", INSTANCE.prices.size());
		}
	}
}
