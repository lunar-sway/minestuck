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
public class EcheladderTrigger extends SimpleCriterionTrigger<EcheladderTrigger.Instance>
{
	@Override
	public Codec<Instance> codec()
	{
		return Instance.CODEC;
	}
	
	public void trigger(ServerPlayer player, int rung)
	{
		trigger(player, instance -> instance.test(rung));
	}
	
	public record Instance(Optional<ContextAwarePredicate> player, MinMaxBounds.Ints rung) implements SimpleCriterionTrigger.SimpleInstance
	{
		private static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player").forGetter(Instance::player),
				MinMaxBounds.Ints.CODEC.fieldOf("rung").forGetter(Instance::rung)
		).apply(instance, Instance::new));
		
		public static Criterion<Instance> rung(MinMaxBounds.Ints rung)
		{
			return MSCriteriaTriggers.ECHELADDER.get().createCriterion(new Instance(Optional.empty(), rung));
		}
		
		public boolean test(int count)
		{
			return this.rung.matches(count);
		}
	}
}
