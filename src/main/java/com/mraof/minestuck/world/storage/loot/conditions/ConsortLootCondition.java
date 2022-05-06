package com.mraof.minestuck.world.storage.loot.conditions;

import com.google.gson.*;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.world.storage.loot.MSLootTables;
import net.minecraft.entity.Entity;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;

public class ConsortLootCondition implements ILootCondition
{
	private final EnumConsort[] consorts;
	
	public ConsortLootCondition(EnumConsort[] consorts)
	{
		this.consorts = consorts;
	}
	
	@Override
	public LootConditionType getType()
	{
		return MSLootTables.consortConditionType();
	}
	
	@Override
	public boolean test(LootContext context)
	{
		Entity entity = context.getParamOrNull(LootParameters.THIS_ENTITY);
		if(entity != null)
			for(EnumConsort type : consorts)
				if(type.isConsort(entity))
					return true;
		return false;
	}
	
	public static IBuilder builder(EnumConsort... consorts)
	{
		return () -> new ConsortLootCondition(consorts);
	}
	
	public static class Serializer implements ILootSerializer<ConsortLootCondition>
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
					consorts[i] = getType(JSONUtils.convertToString(list.get(i), "consort"));
				
			} else consorts = new EnumConsort[] {getType(JSONUtils.getAsString(json, "consort"))};
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
