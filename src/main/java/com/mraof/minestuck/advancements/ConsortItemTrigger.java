package com.mraof.minestuck.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.entity.consort.EnumConsort;
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
public class ConsortItemTrigger extends SimpleCriterionTrigger<ConsortItemTrigger.Instance>
{
	@Override
	public Codec<Instance> codec()
	{
		return Instance.CODEC;
	}
	
	public void trigger(ServerPlayer player, String table, ItemStack item, ConsortEntity consort)
	{
		trigger(player, instance -> instance.test(table, item, consort.merchantType));
	}
	
	public record Instance(Optional<ContextAwarePredicate> player, Optional<String> table,
						   Optional<ItemPredicate> item, Optional<EnumConsort.MerchantType> type) implements SimpleCriterionTrigger.SimpleInstance
	{
		private static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player").forGetter(Instance::player),
				ExtraCodecs.strictOptionalField(Codec.STRING, "table").forGetter(Instance::table),
				ExtraCodecs.strictOptionalField(ItemPredicate.CODEC, "item").forGetter(Instance::item),
				ExtraCodecs.strictOptionalField(EnumConsort.MerchantType.CODEC, "type").forGetter(Instance::type)
		).apply(instance, Instance::new));
		
		public static Criterion<Instance> forType(EnumConsort.MerchantType type)
		{
			return MSCriteriaTriggers.CONSORT_ITEM.get().createCriterion(new Instance(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(type)));
		}
		
		public boolean test(String table, ItemStack item, EnumConsort.MerchantType type)
		{
			return (this.table.isEmpty() || this.table.get().equals(table))
					&& (this.item.isEmpty() || this.item.get().matches(item))
					&& (this.type.isEmpty() || this.type.get() == type);
		}
	}
}
