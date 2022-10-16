package com.mraof.minestuck.item.loot.conditions;

import com.google.gson.*;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.item.loot.MSLootTables;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class ConsortLootCondition implements LootItemCondition
{
	private final EnumConsort[] consorts;
	
	public ConsortLootCondition(EnumConsort[] consorts)
	{
		this.consorts = consorts;
	}
	
	@Override
	public LootItemConditionType getType()
	{
		return MSLootTables.CONSORT_CONDITION.get();
	}
	
	@Override
	public boolean test(LootContext context)
	{
		Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
		if(entity != null)
			for(EnumConsort type : consorts)
				if(type.isConsort(entity))
					return true;
		return false;
	}
	
	public static Builder builder(EnumConsort... consorts)
	{
		return () -> new ConsortLootCondition(consorts);
	}
	
	public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<ConsortLootCondition>
	{
		@Override
		public void serialize(JsonObject json, ConsortLootCondition value, JsonSerializationContext context)
		{
			if(value.consorts.length == 1)
				json.addProperty("consort", value.consorts[0].name().toLowerCase());
			else
			{
				JsonArray list = new JsonArray();
				for(EnumConsort type : value.consorts)
					list.add(type.toString().toLowerCase());
				
				json.add("consort", list);
			}
		}
		
		@Override
		public ConsortLootCondition deserialize(JsonObject json, JsonDeserializationContext context)
		{
			EnumConsort[] consorts;
			if(json.has("consort") && json.get("consort").isJsonArray())
			{
				JsonArray list = json.getAsJsonArray("consort");
				consorts = new EnumConsort[list.size()];
				for(int i = 0; i < list.size(); i++)
					consorts[i] = getType(GsonHelper.convertToString(list.get(i), "consort"));
				
			} else consorts = new EnumConsort[] {getType(GsonHelper.getAsString(json, "consort"))};
			return new ConsortLootCondition(consorts);
		}
		
		private static EnumConsort getType(String name)
		{
			for(EnumConsort type : EnumConsort.values())
				if(type.name().toLowerCase().equals(name))
					return type;
			throw new JsonSyntaxException("\"" + name + "\" is not a valid consort type.");
		}
	}
}
