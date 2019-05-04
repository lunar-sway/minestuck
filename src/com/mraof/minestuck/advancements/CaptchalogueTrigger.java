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
import net.minecraft.advancements.criterion.AbstractCriterionInstance;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CaptchalogueTrigger implements ICriterionTrigger<CaptchalogueTrigger.Instance>
{
	private static final ResourceLocation ID = new ResourceLocation(Minestuck.MOD_ID, "captchalogue");
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
		ItemPredicate item = null;
		if(json.has("item"))
			item = ItemPredicate.deserialize(json.get("item"));
		MinMaxBounds.IntBound count = MinMaxBounds.IntBound.fromJson(json.get("count"));
		return new Instance(modus, item, count);
	}
	
	public void trigger(EntityPlayerMP player, Modus modus, ItemStack item)
	{
		Listeners listeners = listenersMap.get(player.getAdvancements());
		if(listeners != null)
			listeners.trigger(CaptchaDeckHandler.getType(modus.getClass()).toString(), item, modus.getNonEmptyCards());
	}
	
	public static class Instance extends AbstractCriterionInstance
	{
		private final String modus;
		private final ItemPredicate item;
		private final MinMaxBounds.IntBound count;
		public Instance(String modus, ItemPredicate item, MinMaxBounds.IntBound count)
		{
			super(ID);
			this.modus = modus;
			this.item = item;
			this.count = count;
		}
		
		public boolean test(String modus, ItemStack item, int count)
		{
			return (this.modus == null || this.modus.equals(modus)) && (this.item == null || this.item.test(item)) && this.count.test(count);
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
		
		public void trigger(String modus, ItemStack item, int count)
		{
			List<Listener<Instance>> list = Lists.newArrayList();
			for(Listener<Instance> listener : listeners)
				if(listener.getCriterionInstance().test(modus, item, count))
					list.add(listener);
			
			list.forEach((listener) -> listener.grantCriterion(playerAdvancements));
		}
	}
}