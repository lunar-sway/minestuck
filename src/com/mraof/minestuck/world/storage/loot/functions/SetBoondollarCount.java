package com.mraof.minestuck.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mraof.minestuck.item.ItemBoondollars;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

import java.util.Random;

public class SetBoondollarCount extends LootFunction
{
	private final RandomValueRange countRange;
	
	public SetBoondollarCount(LootCondition[] conditionsIn, RandomValueRange countRangeIn)
	{
		super(conditionsIn);
		this.countRange = countRangeIn;
	}
	
	public ItemStack apply(ItemStack stack, Random rand, LootContext context)
	{
		return ItemBoondollars.setCount(stack, countRange.generateInt(rand));
	}
	
	public static class Serializer extends LootFunction.Serializer<SetBoondollarCount>
	{
		public Serializer()
		{
			super(new ResourceLocation("minestuck:set_boondollar_count"), SetBoondollarCount.class);
		}
		
		public void serialize(JsonObject object, SetBoondollarCount functionClazz, JsonSerializationContext serializationContext)
		{
			object.add("count", serializationContext.serialize(functionClazz.countRange));
		}
		
		public SetBoondollarCount deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootCondition[] conditionsIn)
		{
			return new SetBoondollarCount(conditionsIn, JsonUtils.deserializeClass(object, "count", deserializationContext, RandomValueRange.class));
		}
	}
}
