package com.mraof.minestuck.advancements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mraof.minestuck.Minestuck;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class PunchDesignixTrigger implements ICriterionTrigger<PunchDesignixTrigger.Instance>
{
	private static final ResourceLocation ID = new ResourceLocation(Minestuck.MOD_ID, "punch_designix");
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
		ItemPredicate input = ItemPredicate.deserialize(json.get("input"));
		ItemPredicate target = ItemPredicate.deserialize(json.get("target"));
		ItemPredicate output = ItemPredicate.deserialize(json.get("output"));
		return new Instance(input, target, output);
	}
	
	public void trigger(ServerPlayerEntity player, ItemStack input, ItemStack target, ItemStack result)
	{
		Listeners listeners = listenersMap.get(player.getAdvancements());
		if(listeners != null)
			listeners.trigger(player, input, target, result);
	}
	
	public static class Instance extends CriterionInstance
	{
		private final ItemPredicate input;
		private final ItemPredicate target;
		private final ItemPredicate output;
		
		public Instance(ItemPredicate input, ItemPredicate target, ItemPredicate output)
		{
			super(ID);
			this.input = Objects.requireNonNull(input);
			this.target = Objects.requireNonNull(target);
			this.output = Objects.requireNonNull(output);
		}
		
		public static Instance any()
		{
			return create(ItemPredicate.ANY, ItemPredicate.ANY, ItemPredicate.ANY);
		}
		
		public static Instance create(ItemPredicate input, ItemPredicate target, ItemPredicate output)
		{
			return new Instance(input, target, output);
		}
		
		public boolean test(ItemStack input, ItemStack target, ItemStack output)
		{
			return this.input.test(input) && this.target.test(target) && this.output.test(output);
		}
		
		@Override
		public JsonElement serialize()
		{
			JsonObject json = new JsonObject();
			json.add("input", input.serialize());
			json.add("target", target.serialize());
			json.add("output", output.serialize());
			
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
		
		public void trigger(ServerPlayerEntity player, ItemStack input, ItemStack target, ItemStack output)
		{
			List<Listener<Instance>> list = Lists.newArrayList();
			for(Listener<Instance> listener : listeners)
				if(listener.getCriterionInstance().test(input, target, output))
					list.add(listener);
			
			list.forEach((listener) -> listener.grantCriterion(playerAdvancements));
		}
	}
}