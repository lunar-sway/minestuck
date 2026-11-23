package com.mraof.minestuck.advancements;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class BladeKindBreakTrigger extends SimpleCriterionTrigger<BladeKindBreakTrigger.TriggerInstance>
{
	@Override
	public Codec<TriggerInstance> codec()
	{
		return TriggerInstance.CODEC;
	}
	
	public void trigger(ServerPlayer player)
	{
		this.trigger(player, instance -> true);
	}
	
	public record TriggerInstance(
			Optional<ContextAwarePredicate> player
	) implements SimpleInstance
	{
		public static final Codec<TriggerInstance> CODEC = Codec.unit(
				new TriggerInstance(Optional.empty())
		);
		
		public static Criterion<TriggerInstance> any()
		{
			return MSCriteriaTriggers.BLADEKIND_BREAK.get()
					.createCriterion(new TriggerInstance(Optional.empty()));
		}
	}
}