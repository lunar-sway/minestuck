package com.mraof.minestuck.advancements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mraof.minestuck.Minestuck;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class EventTrigger implements ICriterionTrigger<EventTrigger.Instance>
{
	public static final ResourceLocation SBURB_CONNECTION_ID = new ResourceLocation(Minestuck.MOD_ID, "sburb_connection");
	public static final ResourceLocation CRUXITE_ARTIFACT_ID = new ResourceLocation(Minestuck.MOD_ID, "cruxite_artifact");
	public static final ResourceLocation MELON_OVERLOAD_ID = new ResourceLocation(Minestuck.MOD_ID, "melon_overload");
	public static final ResourceLocation SURF_N_TURF_ID = new ResourceLocation(Minestuck.MOD_ID, "surf_n_turf");
	private final Map<PlayerAdvancements, Listeners> listenersMap = Maps.newHashMap();
	private final ResourceLocation id;
	
	public EventTrigger(ResourceLocation id)
	{
		this.id = id;
	}
	
	@Override
	public ResourceLocation getId()
	{
		return id;
	}
	
	@Override
	public void addListener(PlayerAdvancements playerAdvancementsIn, Listener<Instance> listener)
	{
		Listeners listeners = listenersMap.get(playerAdvancementsIn);
		if(listeners == null)
		{
			listeners = new Listeners(playerAdvancementsIn);
			listenersMap.put(playerAdvancementsIn, listeners);
		}
		listeners.add(listener);
	}
	
	@Override
	public void removeListener(PlayerAdvancements playerAdvancementsIn, Listener<Instance> listener)
	{
		Listeners listeners = listenersMap.get(playerAdvancementsIn);
		if(listeners != null)
		{
			listeners.remove(listener);
			if(listeners.isEmpty())
				listenersMap.remove(playerAdvancementsIn);
		}
	}
	
	@Override
	public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
	{
		listenersMap.remove(playerAdvancementsIn);
	}
	
	@Override
	public Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
	{
		return new Instance(id);
	}
	
	public void trigger(ServerPlayerEntity player)
	{
		Listeners listeners = listenersMap.get(player.getAdvancements());
		if(listeners != null)
			listeners.trigger();
	}
	
	public static class Instance extends CriterionInstance
	{
		public static Instance sburbConnection()
		{
			return new Instance(SBURB_CONNECTION_ID);
		}
		
		public static Instance cruxiteArtifact()
		{
			return new Instance(CRUXITE_ARTIFACT_ID);
		}
		
		public static Instance melonOverload()
		{
			return new Instance(MELON_OVERLOAD_ID);
		}
		
		public static Instance surfNTurf()
		{
			return new Instance(SURF_N_TURF_ID);
		}
		
		public Instance(ResourceLocation id)
		{
			super(id);
		}
		
		public boolean test()
		{
			return true;
		}
	}
	
	static class Listeners
	{
		private final PlayerAdvancements playerAdvancements;
		private final Set<Listener<Instance>> listeners = Sets.newHashSet();
		
		public Listeners(PlayerAdvancements playerAdvancementsIn)
		{
			this.playerAdvancements = playerAdvancementsIn;
		}
		
		public boolean isEmpty()
		{
			return listeners.isEmpty();
		}
		
		public void add(ICriterionTrigger.Listener<Instance> listener)
		{
			this.listeners.add(listener);
		}
		
		public void remove(ICriterionTrigger.Listener<Instance> listener)
		{
			this.listeners.remove(listener);
		}
		
		public void trigger()
		{
			List<Listener<Instance>> list = Lists.newArrayList();
			for(Listener<Instance> listener : listeners)
				if(listener.getCriterionInstance().test())
					list.add(listener);
			
			list.forEach((listener) -> listener.grantCriterion(playerAdvancements));
		}
	}
}