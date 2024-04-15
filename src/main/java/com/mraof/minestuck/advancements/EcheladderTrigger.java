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
public class EcheladderTrigger extends SimpleCriterionTrigger<EcheladderTrigger.Instance>
{
	@Override
	protected Instance createInstance(JsonObject json, Optional<ContextAwarePredicate> predicate, DeserializationContext deserializationContext)
	{
		MinMaxBounds.Ints rung = MinMaxBounds.Ints.fromJson(json.get("rung"));
		return new EcheladderTrigger.Instance(predicate, rung);
	}
	
	public void trigger(ServerPlayer player, int rung)
	{
		trigger(player, instance -> instance.test(rung));
	}
	
	public static class Instance extends AbstractCriterionTriggerInstance
	{
		private final MinMaxBounds.Ints rung;
		
		@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
		public Instance(Optional<ContextAwarePredicate> predicate, MinMaxBounds.Ints rung)
		{
			super(predicate);
			this.rung = Objects.requireNonNull(rung);
		}
		
		public static Criterion<Instance> rung(MinMaxBounds.Ints rung)
		{
			return MSCriteriaTriggers.ECHELADDER.createCriterion(new Instance(Optional.empty(), rung));
		}
		
		public boolean test(int count)
		{
			return this.rung.matches(count);
		}

		@Override
		public JsonObject serializeToJson()
		{
			JsonObject json = super.serializeToJson();
			json.add("rung", rung.serializeToJson());

			return json;
		}
	}
}
