package com.mraof.minestuck.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ConsortTalkTrigger extends SimpleCriterionTrigger<ConsortTalkTrigger.Instance>
{
	@Override
	public Codec<Instance> codec()
	{
		return Instance.CODEC;
	}
	
	public void trigger(ServerPlayer player, String message, ConsortEntity consort)
	{
		trigger(player, instance -> instance.test(message));
	}
	
	public record Instance(Optional<ContextAwarePredicate> player, Optional<String> message) implements SimpleCriterionTrigger.SimpleInstance
	{
		private static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Instance::player),
				Codec.STRING.optionalFieldOf("message").forGetter(Instance::message)
		).apply(instance, Instance::new));
		
		public static Criterion<Instance> any()
		{
			return MSCriteriaTriggers.CONSORT_TALK.get().createCriterion(new Instance(Optional.empty(), Optional.empty()));
		}
		
		public static Criterion<Instance> forMessage(String message)
		{
			return MSCriteriaTriggers.CONSORT_TALK.get().createCriterion(new Instance(Optional.empty(), Optional.of(message)));
		}
		
		public boolean test(String message)
		{
			return this.message.isEmpty() || this.message.get().equals(message);
		}
	}
}
