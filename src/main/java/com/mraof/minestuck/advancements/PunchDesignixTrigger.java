package com.mraof.minestuck.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PunchDesignixTrigger extends SimpleCriterionTrigger<PunchDesignixTrigger.Instance>
{
	@Override
	public Codec<Instance> codec()
	{
		return Instance.CODEC;
	}
	
	public void trigger(ServerPlayer player, ItemStack input, ItemStack target, ItemStack result)
	{
		trigger(player, instance -> instance.test(input, target, result));
	}
	
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public record Instance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> input,
						   Optional<ItemPredicate> target, Optional<ItemPredicate> output) implements SimpleCriterionTrigger.SimpleInstance
	{
		private static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Instance::player),
				ItemPredicate.CODEC.optionalFieldOf("input").forGetter(Instance::input),
				ItemPredicate.CODEC.optionalFieldOf("target").forGetter(Instance::target),
				ItemPredicate.CODEC.optionalFieldOf("output").forGetter(Instance::output)
		).apply(instance, Instance::new));
		
		public static Criterion<Instance> any()
		{
			return create(Optional.empty(), Optional.empty(), Optional.empty());
		}
		
		public static Criterion<Instance> create(Optional<ItemPredicate> input, Optional<ItemPredicate> target, Optional<ItemPredicate> output)
		{
			return MSCriteriaTriggers.PUNCH_DESIGNIX.get().createCriterion(new Instance(Optional.empty(), input, target, output));
		}
		
		public boolean test(ItemStack input, ItemStack target, ItemStack output)
		{
			return (this.input.isEmpty() || this.input.get().test(input))
					&& (this.target.isEmpty() || this.target.get().test(target))
					&& (this.output.isEmpty() || this.output.get().test(output));
		}
	}
}
