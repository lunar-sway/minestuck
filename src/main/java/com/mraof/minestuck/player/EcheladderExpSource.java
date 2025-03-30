package com.mraof.minestuck.player;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@ParametersAreNonnullByDefault
@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME)
public interface EcheladderExpSource
{
	Codec<EcheladderExpSource> CODEC = EcheladderExpSources.REGISTRY.byNameCodec().dispatch(EcheladderExpSource::codec, Function.identity());
	
	List<EcheladderExpSource> INSTANCE_LIST = new ArrayList<>();
	
	static List<EcheladderExpSource> instance()
	{
		return Objects.requireNonNull(INSTANCE_LIST);
	}
	
	@SubscribeEvent
	static void onResourceReload(AddReloadListenerEvent event)
	{
		event.addListener(new Loader());
	}
	
	@SubscribeEvent
	static void onServerStopped(ServerStoppedEvent event)
	{
		INSTANCE_LIST.clear();
	}
	
	MapCodec<? extends EcheladderExpSource> codec();
	
	void process(ServerPlayer player);
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	static void onEntityDeath(LivingDeathEvent event)
	{
		if(event.getSource().getEntity() instanceof ServerPlayer player && !(player instanceof FakePlayer))
		{
			for(EcheladderExpSource source : INSTANCE_LIST)
			{
				if(source instanceof KillEntity killEntitySource && killEntitySource.matches(event.getEntity().getType()))
				{
					killEntitySource.process(player);
					return;
				}
				
				if(source instanceof KillEntityTag killEntityTagSource && killEntityTagSource.matches(event.getEntity().getType()))
				{
					killEntityTagSource.process(player);
					return;
				}
			}
		}
	}
	
	record KillEntity(EntityType<?> entityType, int amount) implements EcheladderExpSource
	{
		static final MapCodec<KillEntity> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_type").forGetter(KillEntity::entityType),
				Codec.INT.fieldOf("amount").forGetter(KillEntity::amount)
		).apply(instance, KillEntity::new));
		
		@Override
		public MapCodec<? extends EcheladderExpSource> codec()
		{
			return CODEC;
		}
		
		public boolean matches(EntityType<?> entityType)
		{
			return this.entityType.equals(entityType);
		}
		
		@Override
		public void process(ServerPlayer player)
		{
			EcheladderExpSource.advance(player, amount);
		}
	}
	
	record KillEntityTag(TagKey<EntityType<?>> entityTypeTag, int amount) implements EcheladderExpSource
	{
		static final MapCodec<KillEntityTag> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				TagKey.codec(BuiltInRegistries.ENTITY_TYPE.key()).fieldOf("entity_type_tag").forGetter(KillEntityTag::entityTypeTag),
				Codec.INT.fieldOf("amount").forGetter(KillEntityTag::amount)
		).apply(instance, KillEntityTag::new));
		
		@Override
		public MapCodec<? extends EcheladderExpSource> codec()
		{
			return CODEC;
		}
		
		public boolean matches(EntityType<?> entityType)
		{
			return entityType.is(this.entityTypeTag);
		}
		
		@Override
		public void process(ServerPlayer player)
		{
			EcheladderExpSource.advance(player, amount);
		}
	}
	
	@SubscribeEvent
	static void onAdvancementGained(AdvancementEvent.AdvancementEarnEvent event)
	{
		for(EcheladderExpSource source : INSTANCE_LIST)
		{
			if(source instanceof AdvancementEarned advancementSource && advancementSource.matches(event.getAdvancement().id()))
			{
				advancementSource.process((ServerPlayer) event.getEntity());
				return;
			}
		}
	}
	
	record AdvancementEarned(ResourceLocation advancementId, int amount) implements EcheladderExpSource
	{
		static final MapCodec<AdvancementEarned> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				ResourceLocation.CODEC.fieldOf("advancement_id").forGetter(AdvancementEarned::advancementId),
				Codec.INT.fieldOf("amount").forGetter(AdvancementEarned::amount)
		).apply(instance, AdvancementEarned::new));
		
		@Override
		public MapCodec<? extends EcheladderExpSource> codec()
		{
			return CODEC;
		}
		
		public boolean matches(ResourceLocation advancementId)
		{
			return this.advancementId.equals(advancementId);
		}
		
		@Override
		public void process(ServerPlayer player)
		{
			EcheladderExpSource.advance(player, amount);
		}
	}
	
	private static void advance(ServerPlayer player, int exp)
	{
		Echeladder.get(player).increaseProgress(exp);
	}
	
	class Loader extends SimpleJsonResourceReloadListener
	{
		private static final Logger LOGGER = LogManager.getLogger();
		private static final Gson GSON = new GsonBuilder().create();
		
		public Loader()
		{
			super(GSON, "minestuck/exp_source");
		}
		
		@Override
		protected void apply(Map<ResourceLocation, JsonElement> jsonElements, ResourceManager resourceManager, ProfilerFiller profiler)
		{
			ImmutableList.Builder<EcheladderExpSource> listBuilder = ImmutableList.builder();
			for(Map.Entry<ResourceLocation, JsonElement> entry : jsonElements.entrySet())
			{
				EcheladderExpSource.CODEC.parse(JsonOps.INSTANCE, entry.getValue())
						.resultOrPartial(message -> LOGGER.error("Couldn't parse echeladder exp source {}: {}", entry.getKey(), message))
						.ifPresent(listBuilder::add);
			}
			
			INSTANCE_LIST.clear();
			INSTANCE_LIST.addAll(listBuilder.build());
			LOGGER.info("Loaded {} echeladder exp sources", INSTANCE_LIST.size());
		}
	}
}
