package com.mraof.minestuck.advancements;

import com.google.gson.JsonObject;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.entity.consort.EnumConsort;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

public class ConsortItemTrigger extends AbstractCriterionTrigger<ConsortItemTrigger.Instance>
{
	private static final ResourceLocation ID = new ResourceLocation(Minestuck.MOD_ID, "consort_item");
	
	@Override
	public ResourceLocation getId()
	{
		return ID;
	}
	
	@Override
	protected Instance createInstance(JsonObject json, EntityPredicate.AndPredicate predicate, ConditionArrayParser conditionsParser)
	{
		String table = json.has("table") ? JSONUtils.getAsString(json, "table") : null;
		ItemPredicate item = ItemPredicate.fromJson(json.get("item"));
		EnumConsort.MerchantType type = json.has("type") ? EnumConsort.MerchantType.getFromName(JSONUtils.getAsString(json, "type")) : null;
		return new Instance(predicate, table, item, type);
	}
	
	public void trigger(ServerPlayerEntity player, String table, ItemStack item, ConsortEntity consort)
	{
		trigger(player, instance -> instance.test(table, item, consort.merchantType));
	}
	
	public static class Instance extends CriterionInstance
	{
		private final String table;
		private final ItemPredicate item;
		private final EnumConsort.MerchantType type;
		
		public Instance(EntityPredicate.AndPredicate predicate, String table, ItemPredicate item, EnumConsort.MerchantType type)
		{
			super(ID, predicate);
			this.table = table;
			this.item = Objects.requireNonNull(item);
			this.type = type;
		}
		
		public static Instance forType(EnumConsort.MerchantType type)
		{
			return new Instance(EntityPredicate.AndPredicate.ANY, null, ItemPredicate.ANY, type);
		}
		
		public boolean test(String table, ItemStack item, EnumConsort.MerchantType type)
		{
			return (this.table == null || this.table.equals(table)) && this.item.matches(item) && (this.type == null || this.type == type);
		}
		
		@Override
		public JsonObject serializeToJson(ConditionArraySerializer conditions)
		{
			JsonObject json = super.serializeToJson(conditions);
			if(table != null)
				json.addProperty("table", table);
			json.add("item", item.serializeToJson());
			if(type != null)
				json.addProperty("type", type.toString().toLowerCase());
			
			return json;
		}
	}
}