package com.mraof.minestuck.advancements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.inventory.captchalogue.ModusTypes;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.Objects;
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
		ModusType<?> modus = json.has("modus") ? ModusTypes.REGISTRY.getValue(new ResourceLocation(JSONUtils.getString(json, "modus"))) : null;
		ItemPredicate item = json.has("item") ? ItemPredicate.deserialize(json.get("item")) : null;
		MinMaxBounds.IntBound count = MinMaxBounds.IntBound.fromJson(json.get("count"));
		return new Instance(modus, item, count);
	}
	
	public void trigger(ServerPlayerEntity player, Modus modus, ItemStack item)
	{
		Listeners listeners = listenersMap.get(player.getAdvancements());
		if(listeners != null)
			listeners.trigger(modus.getType(), item, modus.getNonEmptyCards());
	}
	
	public static class Instance extends CriterionInstance
	{
		private final ModusType<?> modus;
		private final ItemPredicate item;
		private final MinMaxBounds.IntBound count;
		public Instance(ModusType<?> modus, ItemPredicate item, MinMaxBounds.IntBound count)
		{
			super(ID);
			this.modus = modus;
			this.item = Objects.requireNonNull(item);
			this.count = Objects.requireNonNull(count);
		}
		
		public boolean test(ModusType<?> modus, ItemStack item, int count)
		{
			return (this.modus == null || this.modus.equals(modus)) && this.item.test(item) && this.count.test(count);
		}
		
		@Override
		public JsonElement serialize()
		{
			JsonObject json = new JsonObject();
			if(modus != null)
				json.addProperty("modus", modus.getRegistryName().toString());
			json.add("item", item.serialize());
			json.add("count", count.serialize());
			
			return json;
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
		
		public void trigger(ModusType<?> modus, ItemStack item, int count)
		{
			List<Listener<Instance>> list = Lists.newArrayList();
			for(Listener<Instance> listener : listeners)
				if(listener.getCriterionInstance().test(modus, item, count))
					list.add(listener);
			
			list.forEach((listener) -> listener.grantCriterion(playerAdvancements));
		}
	}
}