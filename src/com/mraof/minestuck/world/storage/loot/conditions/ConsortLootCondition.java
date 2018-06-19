package com.mraof.minestuck.world.storage.loot.conditions;

import com.google.gson.*;
import com.mraof.minestuck.entity.consort.EnumConsort;
import net.minecraft.entity.Entity;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import java.util.Random;

public class ConsortLootCondition implements LootCondition
{
	private final EnumConsort[] consorts;
	
	public ConsortLootCondition(EnumConsort[] consorts)
	{
		this.consorts = consorts;
	}
	
	@Override
	public boolean testCondition(Random rand, LootContext context)
	{
		Entity entity = context.getLootedEntity();
		if(entity != null)
			for(EnumConsort type : consorts)
				if(type.isConsort(entity))
					return true;
		return false;
	}
	
	public static class Serializer extends LootCondition.Serializer<ConsortLootCondition>
	{
		public Serializer()
		{
			super(new ResourceLocation("minestuck", "consort"), ConsortLootCondition.class);
		}
		
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
					consorts[i] = getType(JsonUtils.getString(list.get(i), "consort"));
				
			} else consorts = new EnumConsort[] {getType(JsonUtils.getString(json, "consort"))};
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
