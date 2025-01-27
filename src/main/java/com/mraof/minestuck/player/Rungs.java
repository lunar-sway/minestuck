package com.mraof.minestuck.player;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.Minestuck;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@ParametersAreNonnullByDefault
@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME)
public class Rungs
{
	private static final Logger LOGGER = LogManager.getLogger();
	public static final String PATH = "minestuck/config/rungs.json";
	
	public static final int RUNG_COUNT = 50;
	private static final List<Rung> RUNGS = new ArrayList<>(); //sorted upon creation
	
	public static long getGristCapacity(int rung)
	{
		return RUNGS.get(rung).gristCapacity();
	}
	
	public static long getBoondollarsGained(int rung)
	{
		return RUNGS.get(rung).boondollars();
	}
	
	public static long getProgressReq(int rung)
	{
		return RUNGS.get(rung).expRequirement();
	}
	
	public static List<MobEffectInstance> getRelevantEffects(EnumAspect aspect, int rung)
	{
		List<MobEffectInstance> instances = new ArrayList<>();
		
		for(Rung rungIterate : RUNGS.subList(0, rung))
		{
			for(Rung.AspectEffect aspectEffect : rungIterate.aspectEffects().stream().filter(aspectEffect -> aspectEffect.aspect().equals(aspect)).toList())
			{
				//TODO remove effects of the same type but of a lower amplifier
				/*for(int i = 0; i < instances.size(); i++)
				{
					if(instances.get(i).getEffect().equals(aspectEffect.effect()))
						instances.remove(i); //hmm
				}*/
				instances.add(new MobEffectInstance(aspectEffect.effect(), 600, aspectEffect.amplifier()));
			}
		}
		
		return instances;
	}
	
	public static int finalRung()
	{
		return RUNG_COUNT - 1;
	}
	
	public List<Rung> getRungs()
	{
		return RUNGS;
	}
	
	public Rung getRung(int rung)
	{
		return RUNGS.get(rung);
	}
	
	@SubscribeEvent
	public static void onServerStopped(ServerStoppedEvent event)
	{
		RUNGS.clear();
	}
	
	@SubscribeEvent
	public static void onResourceReload(AddReloadListenerEvent event)
	{
		event.addListener(new Loader());
	}
	
	private final static class Loader extends SimplePreparableReloadListener<List<Rung>>
	{
		@Override
		protected List<Rung> prepare(ResourceManager resourceManager, ProfilerFiller profiler)
		{
			return getParsedRungs(resourceManager).orElse(new ArrayList<>());
		}
		
		private static Optional<List<Rung>> getParsedRungs(ResourceManager resourceManager)
		{
			ResourceLocation location = Minestuck.id(PATH);
			Optional<Resource> potentialResource = resourceManager.getResource(location);
			
			return potentialResource.flatMap(resource ->
			{
				try(Reader reader = resource.openAsReader())
				{
					JsonElement json = JsonParser.parseReader(reader);
					return Rung.LIST_CODEC.parse(JsonOps.INSTANCE, json)
							.resultOrPartial(message -> LOGGER.error("Problem parsing rungs json at: {}. This means there is no data for rungs. Reason: {}", location, message));
					
				} catch(IOException ignored)
				{
					return Optional.empty();
				}
			});
		}
		
		@Override
		protected void apply(List<Rung> rungs, ResourceManager resourceManager, ProfilerFiller profiler)
		{
			Rungs.RUNGS.addAll(rungs);
			Rungs.RUNGS.sort(Comparator.comparingInt(Rung::rung));
		}
	}
}
