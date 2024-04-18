package com.mraof.minestuck.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TreeModusRootTrigger extends SimpleCriterionTrigger<TreeModusRootTrigger.Instance>
{
	@Override
	public Codec<Instance> codec()
	{
		return Instance.CODEC;
	}
	
	public void trigger(ServerPlayer player, int count)
	{
		trigger(player, instance -> instance.test(count));
	}
	
	public record Instance(Optional<ContextAwarePredicate> player, MinMaxBounds.Ints count) implements SimpleCriterionTrigger.SimpleInstance
	{
		private static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player").forGetter(Instance::player),
				MinMaxBounds.Ints.CODEC.fieldOf("count").forGetter(Instance::count)
		).apply(instance, Instance::new));
		
		public static Criterion<Instance> count(MinMaxBounds.Ints count)
		{
			return MSCriteriaTriggers.TREE_MODUS_ROOT.get().createCriterion(new Instance(Optional.empty(), count));
		}
		
		public boolean test(int count)
		{
			return this.count.matches(count);
		}
	}
}
