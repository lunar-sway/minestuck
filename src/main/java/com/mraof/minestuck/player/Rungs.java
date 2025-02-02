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
	
	private static final List<Rung> RUNGS = new ArrayList<>(); //sorted upon creation
	
	public static long getGristCapacity(int rung)
	{
		return getRung(rung).gristCapacity();
	}
	
	public static long getBoondollarsGained(int rung)
	{
		return getRung(rung).boondollars();
	}
	
	public static long getProgressReq(int rung)
	{
		return getRung(rung).expRequirement();
	}
	
	public static int getBackgroundColor(int rung)
	{
		return getRung(rung).backgroundColor();
	}
	
	public static int getTextColor(int rung)
	{
		return getRung(rung).textColor();
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
	
	public static List<Rung.EcheladderAttribute> getRelevantAttributes(int rung)
	{
		List<Rung.EcheladderAttribute> attributes = new ArrayList<>();
		
		for(Rung rungIterate : RUNGS.subList(0, rung))
			attributes.addAll(rungIterate.attributes());
		
		return attributes;
	}
	
	public static int finalRung()
	{
		return RUNGS.size() - 1;
	}
	
	public List<Rung> getRungs()
	{
		return RUNGS;
	}
	
	public static Rung getRung(int rung)
	{
		if(RUNGS.size() > rung)
			return RUNGS.get(rung);
		else
			return RUNGS.getLast();
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
