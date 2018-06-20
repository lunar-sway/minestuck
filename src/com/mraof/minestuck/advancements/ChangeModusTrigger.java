package com.mraof.minestuck.advancements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalouge.Modus;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChangeModusTrigger implements ICriterionTrigger<ChangeModusTrigger.Instance>
{
	private static final ResourceLocation ID = new ResourceLocation(Minestuck.MOD_ID, "change_modus");
	private final Map<PlayerAdvancements, Listeners> listenersMap = Maps.newHashMap();
	
	@Override
	public ResourceLocation getId()
	{
		return ID;
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
		String modus = null;
		if(json.has("modus"))
		{
			modus = json.get("modus").getAsString();
			if(!CaptchaDeckHandler.isInRegistry(new ResourceLocation(modus)))
				throw new IllegalArgumentException("Invalid modus "+modus);
		}
		return new Instance(modus);
	}
	
	public void trigger(EntityPlayerMP player, Modus modus)
	{
		Listeners listeners = listenersMap.get(player.getAdvancements());
		if(listeners != null)
			listeners.trigger(CaptchaDeckHandler.getType(modus.getClass()).toString());
	}
	
	public static class Instance extends AbstractCriterionInstance
	{
		private final String modus;
		public Instance(String modus)
		{
			super(ID);
			this.modus = modus;
		}
		
		public boolean test(String modus)
		{
			return this.modus == null || this.modus.equals(modus);
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
		
		public void add(Listener<Instance> listener)
		{
			this.listeners.add(listener);
		}
		
		public void remove(Listener<Instance> listener)
		{
			this.listeners.remove(listener);
		}
		
		public void trigger(String modus)
		{
			List<Listener<Instance>> list = Lists.newArrayList();
			for(Listener<Instance> listener : listeners)
				if(listener.getCriterionInstance().test(modus))
					list.add(listener);
			
			list.forEach((listener) -> listener.grantCriterion(playerAdvancements));
		}
	}
}