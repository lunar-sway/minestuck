package com.mraof.minestuck.advancements;

import com.google.gson.JsonObject;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.entity.consort.EnumConsort;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ConsortItemTrigger extends SimpleCriterionTrigger<ConsortItemTrigger.Instance>
{
	@Override
	protected Instance createInstance(JsonObject json, Optional<ContextAwarePredicate> predicate, DeserializationContext context)
	{
		String table = json.has("table") ? GsonHelper.getAsString(json, "table") : null;
		Optional<ItemPredicate> item = ItemPredicate.fromJson(json.get("item"));
		EnumConsort.MerchantType type = json.has("type") ? EnumConsort.MerchantType.getFromName(GsonHelper.getAsString(json, "type")) : null;
		return new Instance(predicate, table, item, type);
	}
	
	public void trigger(ServerPlayer player, String table, ItemStack item, ConsortEntity consort)
	{
		trigger(player, instance -> instance.test(table, item, consort.merchantType));
	}
	
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public static class Instance extends AbstractCriterionTriggerInstance
	{
		@Nullable
		private final String table;
		private final Optional<ItemPredicate> item;
		@Nullable
		private final EnumConsort.MerchantType type;
		
		public Instance(Optional<ContextAwarePredicate> predicate, @Nullable String table, Optional<ItemPredicate> item, @Nullable EnumConsort.MerchantType type)
		{
			super(predicate);
			this.table = table;
			this.item = item;
			this.type = type;
		}
		
		public static Criterion<Instance> forType(EnumConsort.MerchantType type)
		{
			return MSCriteriaTriggers.CONSORT_ITEM.createCriterion(new Instance(Optional.empty(), null, Optional.empty(), type));
		}
		
		public boolean test(String table, ItemStack item, EnumConsort.MerchantType type)
		{
			return (this.table == null || this.table.equals(table))
					&& (this.item.isEmpty() || this.item.get().matches(item))
					&& (this.type == null || this.type == type);
		}
		
		@Override
		public JsonObject serializeToJson()
		{
			JsonObject json = super.serializeToJson();
			if(table != null)
				json.addProperty("table", table);
			this.item.ifPresent(item -> json.add("item", item.serializeToJson()));
			if(type != null)
				json.addProperty("type", type.toString().toLowerCase());
			
			return json;
		}
	}
}