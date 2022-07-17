package com.mraof.minestuck.advancements;

import com.google.gson.JsonObject;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.entity.consort.EnumConsort;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class ConsortItemTrigger extends SimpleCriterionTrigger<ConsortItemTrigger.Instance>
{
	private static final ResourceLocation ID = new ResourceLocation(Minestuck.MOD_ID, "consort_item");
	
	@Override
	public ResourceLocation getId()
	{
		return ID;
	}
	
	@Override
	protected Instance createInstance(JsonObject json, EntityPredicate.Composite predicate, DeserializationContext context)
	{
		String table = json.has("table") ? GsonHelper.getAsString(json, "table") : null;
		ItemPredicate item = ItemPredicate.fromJson(json.get("item"));
		EnumConsort.MerchantType type = json.has("type") ? EnumConsort.MerchantType.getFromName(GsonHelper.getAsString(json, "type")) : null;
		return new Instance(predicate, table, item, type);
	}
	
	public void trigger(ServerPlayer player, String table, ItemStack item, ConsortEntity consort)
	{
		trigger(player, instance -> instance.test(table, item, consort.merchantType));
	}
	
	public static class Instance extends AbstractCriterionTriggerInstance
	{
		private final String table;
		private final ItemPredicate item;
		private final EnumConsort.MerchantType type;
		
		public Instance(EntityPredicate.Composite predicate, String table, ItemPredicate item, EnumConsort.MerchantType type)
		{
			super(ID, predicate);
			this.table = table;
			this.item = Objects.requireNonNull(item);
			this.type = type;
		}
		
		public static Instance forType(EnumConsort.MerchantType type)
		{
			return new Instance(EntityPredicate.Composite.ANY, null, ItemPredicate.ANY, type);
		}
		
		public boolean test(String table, ItemStack item, EnumConsort.MerchantType type)
		{
			return (this.table == null || this.table.equals(table)) && this.item.matches(item) && (this.type == null || this.type == type);
		}
		
		@Override
		public JsonObject serializeToJson(SerializationContext context)
		{
			JsonObject json = super.serializeToJson(context);
			if(table != null)
				json.addProperty("table", table);
			json.add("item", item.serializeToJson());
			if(type != null)
				json.addProperty("type", type.toString().toLowerCase());
			
			return json;
		}
	}
}