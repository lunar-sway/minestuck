package com.mraof.minestuck.advancements;

import com.google.gson.JsonObject;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TreeModusRootTrigger extends SimpleCriterionTrigger<TreeModusRootTrigger.Instance>
{
	@Override
	protected Instance createInstance(JsonObject json, Optional<ContextAwarePredicate> predicate, DeserializationContext context)
	{
		return new Instance(predicate, MinMaxBounds.Ints.fromJson(json.get("count")));
	}
	
	public void trigger(ServerPlayer player, int count)
	{
		trigger(player, instance -> instance.test(count));
	}
	
	public static class Instance extends AbstractCriterionTriggerInstance
	{
		private final MinMaxBounds.Ints count;
		
		@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
		public Instance(Optional<ContextAwarePredicate> predicate, MinMaxBounds.Ints count)
		{
			super(predicate);
			this.count = Objects.requireNonNull(count);
		}
		
		public static Criterion<Instance> count(MinMaxBounds.Ints count)
		{
			return MSCriteriaTriggers.TREE_MODUS_ROOT.createCriterion(new Instance(Optional.empty(), count));
		}
		
		public boolean test(int count)
		{
			return this.count.matches(count);
		}
		
		@Override
		public JsonObject serializeToJson()
		{
			JsonObject json = super.serializeToJson();
			json.add("count", count.serializeToJson());
			
			return json;
		}
	}
}