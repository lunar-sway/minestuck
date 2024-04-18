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
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class IntellibeamLaserstationTrigger extends SimpleCriterionTrigger<IntellibeamLaserstationTrigger.Instance>
{
	@Override
	public Codec<Instance> codec()
	{
		return Instance.CODEC;
	}
	
	public void trigger(ServerPlayer player, ItemStack item)
	{
		trigger(player, instance -> instance.test(item));
	}
	
	public record Instance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> item) implements SimpleCriterionTrigger.SimpleInstance
	{
		private static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player").forGetter(Instance::player),
				ExtraCodecs.strictOptionalField(ItemPredicate.CODEC, "item").forGetter(Instance::item)
		).apply(instance, Instance::new));
		
		public static Criterion<Instance> any()
		{
			return MSCriteriaTriggers.INTELLIBEAM_LASERSTATION.get().createCriterion(new Instance(Optional.empty(), Optional.empty()));
		}
		
		public static Criterion<Instance> create(ItemPredicate item)
		{
			return MSCriteriaTriggers.INTELLIBEAM_LASERSTATION.get().createCriterion(new Instance(Optional.empty(), Optional.of(item)));
		}
		
		public boolean test(ItemStack item)
		{
			return this.item.isEmpty() || this.item.get().matches(item);
		}
	}
}
