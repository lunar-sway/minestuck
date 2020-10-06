package com.mraof.minestuck.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mraof.minestuck.item.BoondollarsItem;
import com.mraof.minestuck.world.storage.loot.MSLootTables;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;

public class SetBoondollarCount extends LootFunction
{
	private final IRandomRange countRange;
	
	public SetBoondollarCount(ILootCondition[] conditionsIn, IRandomRange countRangeIn)
	{
		super(conditionsIn);
		this.countRange = countRangeIn;
	}
	
	@Override
	public LootFunctionType getFunctionType()
	{
		return MSLootTables.setBoondollarFunctionType();
	}
	
	@Override
	protected ItemStack doApply(ItemStack stack, LootContext context)
	{
		return BoondollarsItem.setCount(stack, countRange.generateInt(context.getRandom()));
	}
	
	public static LootFunction.Builder<?> builder(IRandomRange range)
	{
		return builder((conditions) -> new SetBoondollarCount(conditions, range));
	}
	
	public static class Serializer extends LootFunction.Serializer<SetBoondollarCount>
	{
		public void serialize(JsonObject object, SetBoondollarCount functionClazz, JsonSerializationContext serializationContext)
		{
			object.add("count", RandomRanges.serialize(functionClazz.countRange, serializationContext));
		}
		
		public SetBoondollarCount deserialize(JsonObject object, JsonDeserializationContext deserializationContext, ILootCondition[] conditionsIn)
		{
			return new SetBoondollarCount(conditionsIn, RandomRanges.deserialize(object.get("count"), deserializationContext));
		}
	}
}