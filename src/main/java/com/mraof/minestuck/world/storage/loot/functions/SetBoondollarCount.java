package com.mraof.minestuck.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mraof.minestuck.item.BoondollarsItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootFunction;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.ILootCondition;

public class SetBoondollarCount extends LootFunction
{
	private final RandomValueRange countRange;
	
	public SetBoondollarCount(ILootCondition[] conditionsIn, RandomValueRange countRangeIn)
	{
		super(conditionsIn);
		this.countRange = countRangeIn;
	}
	
	@Override
	protected ItemStack doApply(ItemStack stack, LootContext context)
	{
		return BoondollarsItem.setCount(stack, countRange.generateInt(context.getRandom()));
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
		
		public SetBoondollarCount deserialize(JsonObject object, JsonDeserializationContext deserializationContext, ILootCondition[] conditionsIn)
		{
			return new SetBoondollarCount(conditionsIn, JSONUtils.deserializeClass(object, "count", deserializationContext, RandomValueRange.class));
		}
	}
}