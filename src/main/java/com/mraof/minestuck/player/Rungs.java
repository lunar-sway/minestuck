package com.mraof.minestuck.player;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.data.RungsProvider;
import com.mraof.minestuck.network.RungDisplayDataPacket;
import com.mraof.minestuck.util.MSAttachments;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

@ParametersAreNonnullByDefault
@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME)
public class Rungs
{
	private static final Logger LOGGER = LogManager.getLogger();
	public static final String PATH = "minestuck/config/rungs.json";
	
	private static final List<Rung> RUNGS = new ArrayList<>(); //sorted upon creation
	private static Collection<RungExtension> RUNG_EXTENSION_LIST = Collections.emptySet();
	
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
	
	@SubscribeEvent
	private static void onPlayerTickEvent(PlayerTickEvent.Pre event)
	{
		if(event.getEntity() instanceof ServerPlayer player)
		{
			if(!player.getData(MSAttachments.EFFECT_TOGGLE))
				return;
			if(player.getCommandSenderWorld().getGameTime() % 380 != 0)
				return;
			Optional<PlayerData> data = PlayerData.get(player);
			if(data.isEmpty())
				return;
			
			int rung = Echeladder.get(data.get()).getRung();
			EnumAspect aspect = Title.getTitle(data.get()).map(Title::heroAspect).orElse(null);
			List<MobEffectInstance> effects = getRelevantEffects(aspect, rung);
			
			if(!effects.isEmpty())
			{
				effects.forEach(player::addEffect);
				LOGGER.debug("Applied echeladder potion effect(s) to {}", player.getDisplayName().getString());
			}
		}
	}
	
	public static List<MobEffectInstance> getRelevantEffects(@Nullable EnumAspect aspect, int rung)
	{
		List<MobEffectInstance> instances = new ArrayList<>();
		
		for(RungExtension extension : RUNG_EXTENSION_LIST)
		{
			if(extension.requiredAspect().isEmpty() || extension.requiredAspect().get() == aspect)
			{
				for(RungReq<Rung.Effect> rungEffect : extension.effectList())
				{
					if(rungEffect.rung() <= rung)
						instances.add(new MobEffectInstance(rungEffect.value().mobEffect(), 600, rungEffect.value().amplifier()));
				}
			}
		}
		
		return instances;
	}
	
	public static List<Rung.EcheladderAttribute> getRelevantAttributes(@Nullable EnumAspect aspect, int rung)
	{
		List<Rung.EcheladderAttribute> attributes = new ArrayList<>();
		
		for(RungExtension extension : RUNG_EXTENSION_LIST)
		{
			if(extension.requiredAspect().isEmpty() || extension.requiredAspect().get() == aspect)
			{
				for(RungReq<Rung.EcheladderAttribute> rungAttribute : extension.attributeList())
				{
					if(rungAttribute.rung() <= rung)
						attributes.add(rungAttribute.value());
				}
			}
		}
		
		return attributes;
	}
	
	public static int finalRung()
	{
		return RUNGS.size() - 1;
	}
	
	public static int getMaxAttainableRung(ServerPlayer serverPlayer)
	{
		int maxRung = 0;
		
		for(Rung rung : RUNGS)
		{
			Optional<Rung.RungCondition> rungCondition = rung.rungCondition();
			
			if(rungCondition.isPresent() && !rungCondition.get().canInitiateRung(serverPlayer))
				break; //stop iterating maxRung when obstruction is met
			
			maxRung++;
		}
		
		return maxRung;
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
		event.addListener(new ExtensionsLoader());
	}
	
	private final static class Loader extends SimplePreparableReloadListener<List<Rung>>
	{
		@Override
		protected List<Rung> prepare(ResourceManager resourceManager, ProfilerFiller profiler)
		{
			return loadRungList(resourceManager);
		}
		
		private static List<Rung> loadRungList(ResourceManager resourceManager)
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
			}).orElse(Collections.emptyList());
		}
		
		@Override
		protected void apply(List<Rung> rungs, ResourceManager resourceManager, ProfilerFiller profiler)
		{
			Rungs.RUNGS.clear();
			Rungs.RUNGS.addAll(rungs);
			Rungs.RUNGS.sort(Comparator.comparingInt(Rung::rung));
		}
	}
	
	private final static class ExtensionsLoader extends SimpleJsonResourceReloadListener
	{
		private static final Gson GSON = new GsonBuilder().create();
		
		public ExtensionsLoader()
		{
			super(GSON, "minestuck/rung_extension");
		}
		
		@Override
		protected void apply(Map<ResourceLocation, JsonElement> jsonElements, ResourceManager resourceManager, ProfilerFiller profiler)
		{
			ImmutableList.Builder<RungExtension> listBuilder = ImmutableList.builder();
			for(Map.Entry<ResourceLocation, JsonElement> entry : jsonElements.entrySet())
			{
				RungExtension.CODEC.parse(JsonOps.INSTANCE, entry.getValue())
						.resultOrPartial(message -> LOGGER.error("Couldn't parse rung extension {}: {}", entry.getKey(), message))
						.ifPresent(listBuilder::add);
			}
			
			Rungs.RUNG_EXTENSION_LIST = listBuilder.build();
		}
	}
	
	private static record RungExtension(Optional<EnumAspect> requiredAspect, List<RungReq<Rung.EcheladderAttribute>> attributeList, List<RungReq<Rung.Effect>> effectList)
	{
		static Codec<RungExtension> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				EnumAspect.CODEC.optionalFieldOf("aspect").forGetter(RungExtension::requiredAspect),
				RungReq.codec(Rung.EcheladderAttribute.CODEC).listOf().optionalFieldOf("attributes", Collections.emptyList()).forGetter(RungExtension::attributeList),
				RungReq.codec(Rung.Effect.CODEC).listOf().optionalFieldOf("effects", Collections.emptyList()).forGetter(RungExtension::effectList)
		).apply(instance, RungExtension::new));
	}
	
	private static record RungReq<T>(int rung, T value)
	{
		static <T> Codec<RungReq<T>> codec(MapCodec<T> innerCodec)
		{
			return RecordCodecBuilder.create(instance -> instance.group(
					Codec.INT.optionalFieldOf("rung", 0).forGetter(RungReq::rung),
					innerCodec.forGetter(RungReq::value)
			).apply(instance, RungReq::new));
		}
	}
	
	@SubscribeEvent
	private static void onDatapackSync(OnDatapackSyncEvent event)
	{
		if (event.getPlayer() != null)
			PacketDistributor.sendToPlayer(event.getPlayer(), createDisplayDataPacket());
		else
			PacketDistributor.sendToAllPlayers(createDisplayDataPacket());
	}
	
	private static RungDisplayDataPacket createDisplayDataPacket()
	{
		List<Rung.DisplayData> rungList = RUNGS.stream().map(rung -> {
			Rung.DisplayAttributes attributes = new Rung.DisplayAttributes(getAttributeAmount(rung.rung(), RungsProvider.DAMAGE_BOOST_ID), getAttributeAmount(rung.rung(), RungsProvider.HEALTH_BOOST_ID),
					1D + getAttributeAmount(rung.rung(), RungsProvider.UNDERLING_DAMAGE_ID), 1D + getAttributeAmount(rung.rung(), RungsProvider.UNDERLING_PROTECTION_ID));
					return new Rung.DisplayData(rung.backgroundColor(), rung.textColor(), rung.gristCapacity(), attributes);
				}
		).toList();
		return new RungDisplayDataPacket(rungList);
	}
	
	private static double getAttributeAmount(int rung, ResourceLocation id)
	{
		for(Rung.EcheladderAttribute echeladderAttribute : getRelevantAttributes(null, rung))
		{
			if(echeladderAttribute.id().equals(id))
				return echeladderAttribute.getAmount(rung);
		}
		
		return 0;
	}
}
