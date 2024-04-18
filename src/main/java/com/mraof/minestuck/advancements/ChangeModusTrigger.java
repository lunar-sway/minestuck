package com.mraof.minestuck.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.inventory.captchalogue.ModusTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ChangeModusTrigger extends SimpleCriterionTrigger<ChangeModusTrigger.Instance>
{
	@Override
	public Codec<Instance> codec()
	{
		return Instance.CODEC;
	}
	
	public void trigger(ServerPlayer player, Modus modus)
	{
		trigger(player, instance -> instance.test(modus.getType()));
	}
	
	public record Instance(Optional<ContextAwarePredicate> player, Optional<ModusType<?>> modusType) implements SimpleCriterionTrigger.SimpleInstance
	{
		private static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player").forGetter(Instance::player),
				ExtraCodecs.strictOptionalField(ModusTypes.REGISTRY.byNameCodec(), "modus").forGetter(Instance::modusType)
		).apply(instance, Instance::new));
		
		public static Criterion<Instance> any()
		{
			return MSCriteriaTriggers.CHANGE_MODUS.get().createCriterion(new Instance(Optional.empty(), Optional.empty()));
		}
		
		public static Criterion<Instance> to(ModusType<?> type)
		{
			return MSCriteriaTriggers.CHANGE_MODUS.get().createCriterion(new Instance(Optional.empty(), Optional.of(type)));
		}
		
		public boolean test(ModusType<?> modusType)
		{
			return this.modusType.isEmpty() || this.modusType.get().equals(modusType);
		}
	}
}
