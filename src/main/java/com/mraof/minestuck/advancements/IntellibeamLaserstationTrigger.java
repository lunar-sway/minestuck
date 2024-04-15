package com.mraof.minestuck.advancements;

import com.google.gson.JsonObject;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class IntellibeamLaserstationTrigger extends SimpleCriterionTrigger<IntellibeamLaserstationTrigger.Instance>
{
	@Override
	protected Instance createInstance(JsonObject json, Optional<ContextAwarePredicate> predicate, DeserializationContext context)
	{
		return new Instance(predicate, ItemPredicate.fromJson(json.get("item")));
	}
	
	public void trigger(ServerPlayer player, ItemStack item)
	{
		trigger(player, instance -> instance.test(item));
	}
	
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public static class Instance extends AbstractCriterionTriggerInstance
	{
		private final Optional<ItemPredicate> item;
		
		public Instance(Optional<ContextAwarePredicate> predicate, Optional<ItemPredicate> item)
		{
			super(predicate);
			this.item = item;
		}
		
		public static Criterion<Instance> any()
		{
			return create(Optional.empty());
		}
		
		public static Criterion<Instance> create(Optional<ItemPredicate> item)
		{
			return MSCriteriaTriggers.INTELLIBEAM_LASERSTATION.createCriterion(new Instance(Optional.empty(), item));
		}
		
		public boolean test(ItemStack item)
		{
			return this.item.isEmpty() || this.item.get().matches(item);
		}
		
		@Override
		public JsonObject serializeToJson()
		{
			JsonObject json = super.serializeToJson();
			this.item.ifPresent(item -> json.add("item", item.serializeToJson()));
			return json;
		}
	}
}
