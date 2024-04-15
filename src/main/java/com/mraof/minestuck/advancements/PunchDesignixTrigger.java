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
public class PunchDesignixTrigger extends SimpleCriterionTrigger<PunchDesignixTrigger.Instance>
{
	@Override
	protected Instance createInstance(JsonObject json, Optional<ContextAwarePredicate> predicate, DeserializationContext context)
	{
		Optional<ItemPredicate> input = ItemPredicate.fromJson(json.get("input"));
		Optional<ItemPredicate> target = ItemPredicate.fromJson(json.get("target"));
		Optional<ItemPredicate> output = ItemPredicate.fromJson(json.get("output"));
		return new Instance(predicate, input, target, output);
	}
	
	public void trigger(ServerPlayer player, ItemStack input, ItemStack target, ItemStack result)
	{
		trigger(player, instance -> instance.test(input, target, result));
	}
	
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public static class Instance extends AbstractCriterionTriggerInstance
	{
		private final Optional<ItemPredicate> input;
		private final Optional<ItemPredicate> target;
		private final Optional<ItemPredicate> output;
		
		public Instance(Optional<ContextAwarePredicate> predicate, Optional<ItemPredicate> input, Optional<ItemPredicate> target, Optional<ItemPredicate> output)
		{
			super(predicate);
			this.input = input;
			this.target = target;
			this.output = output;
		}
		
		public static Criterion<Instance> any()
		{
			return create(Optional.empty(), Optional.empty(), Optional.empty());
		}
		
		public static Criterion<Instance> create(Optional<ItemPredicate> input, Optional<ItemPredicate> target, Optional<ItemPredicate> output)
		{
			return MSCriteriaTriggers.PUNCH_DESIGNIX.createCriterion(new Instance(Optional.empty(), input, target, output));
		}
		
		public boolean test(ItemStack input, ItemStack target, ItemStack output)
		{
			return (this.input.isEmpty() || this.input.get().matches(input))
					&& (this.target.isEmpty() || this.target.get().matches(target))
					&& (this.output.isEmpty() || this.output.get().matches(output));
		}
		
		@Override
		public JsonObject serializeToJson()
		{
			JsonObject json = super.serializeToJson();
			this.input.ifPresent(input -> json.add("input", input.serializeToJson()));
			this.target.ifPresent(target -> json.add("target", target.serializeToJson()));
			this.output.ifPresent(output -> json.add("output", output.serializeToJson()));
			
			return json;
		}
	}
}