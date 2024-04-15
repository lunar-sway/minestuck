package com.mraof.minestuck.advancements;

import com.google.gson.JsonObject;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.inventory.captchalogue.ModusTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CaptchalogueTrigger extends SimpleCriterionTrigger<CaptchalogueTrigger.Instance>
{
	@Override
	protected Instance createInstance(JsonObject json, Optional<ContextAwarePredicate> predicate, DeserializationContext context)
	{
		ModusType<?> modus = json.has("modus") ? ModusTypes.REGISTRY.get(new ResourceLocation(GsonHelper.getAsString(json, "modus"))) : null;
		Optional<ItemPredicate> item = ItemPredicate.fromJson(json.get("item"));
		MinMaxBounds.Ints count = MinMaxBounds.Ints.fromJson(json.get("count"));
		return new Instance(predicate, modus, item, count);
	}
	
	public void trigger(ServerPlayer player, Modus modus, ItemStack item)
	{
		trigger(player, instance -> instance.test(modus.getType(), item, modus.getNonEmptyCards()));
	}
	
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public static class Instance extends AbstractCriterionTriggerInstance
	{
		@Nullable
		private final ModusType<?> modus;
		private final Optional<ItemPredicate> item;
		private final MinMaxBounds.Ints count;
		
		public Instance(Optional<ContextAwarePredicate> predicate, @Nullable ModusType<?> modus,Optional<ItemPredicate> item, MinMaxBounds.Ints count)
		{
			super(predicate);
			this.modus = modus;
			this.item = item;
			this.count = Objects.requireNonNull(count);
		}
		
		public boolean test(ModusType<?> modus, ItemStack item, int count)
		{
			return (this.modus == null || this.modus.equals(modus)) && (this.item.isEmpty() || this.item.get().matches(item)) && this.count.matches(count);
		}
		
		@Override
		public JsonObject serializeToJson()
		{
			JsonObject json = super.serializeToJson();
			if(modus != null)
				json.addProperty("modus", String.valueOf(ModusTypes.REGISTRY.getKey(modus)));
			this.item.ifPresent(item -> json.add("item", item.serializeToJson()));
			json.add("count", count.serializeToJson());
			
			return json;
		}
	}
	
}