package com.mraof.minestuck.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.inventory.captchalogue.ModusTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CaptchalogueTrigger extends SimpleCriterionTrigger<CaptchalogueTrigger.Instance>
{
	@Override
	public Codec<Instance> codec()
	{
		return Instance.CODEC;
	}
	
	public void trigger(ServerPlayer player, Modus modus, ItemStack item)
	{
		trigger(player, instance -> instance.test(modus.getType(), item, modus.getNonEmptyCards()));
	}
	
	public record Instance(Optional<ContextAwarePredicate> player, Optional<ModusType<?>> modus,
						   Optional<ItemPredicate> item, MinMaxBounds.Ints count) implements SimpleCriterionTrigger.SimpleInstance
	{
		private static final Codec<Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player").forGetter(Instance::player),
				ExtraCodecs.strictOptionalField(ModusTypes.REGISTRY.byNameCodec(), "modus").forGetter(Instance::modus),
				ExtraCodecs.strictOptionalField(ItemPredicate.CODEC, "item").forGetter(Instance::item),
				MinMaxBounds.Ints.CODEC.fieldOf("count").forGetter(Instance::count)
		).apply(instance, Instance::new));
		
		public boolean test(ModusType<?> modus, ItemStack item, int count)
		{
			return (this.modus.isEmpty() || this.modus.get().equals(modus))
					&& (this.item.isEmpty() || this.item.get().matches(item))
					&& this.count.matches(count);
		}
	}
}
