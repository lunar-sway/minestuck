package com.mraof.minestuck.item.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mraof.minestuck.item.BoondollarsItem;
import com.mraof.minestuck.item.loot.MSLootTables;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class SetBoondollarCount extends LootItemConditionalFunction
{
	private final NumberProvider countRange;
	
	public SetBoondollarCount(LootItemCondition[] conditionsIn, NumberProvider countRangeIn)
	{
		super(conditionsIn);
		this.countRange = countRangeIn;
	}
	
	@Override
	public LootItemFunctionType getType()
	{
		return MSLootTables.SET_BOONDOLLAR_FUNCTION.get();
	}
	
	@Override
	protected ItemStack run(ItemStack stack, LootContext context)
	{
		return BoondollarsItem.setCount(stack, countRange.getInt(context));
	}
	
	public static LootItemConditionalFunction.Builder<?> builder(NumberProvider range)
	{
		return simpleBuilder((conditions) -> new SetBoondollarCount(conditions, range));
	}
	
	public static class Serializer extends LootItemConditionalFunction.Serializer<SetBoondollarCount>
	{
		public void serialize(JsonObject object, SetBoondollarCount function, JsonSerializationContext context)
		{
			object.add("count", context.serialize(function.countRange));
		}
		
		public SetBoondollarCount deserialize(JsonObject object, JsonDeserializationContext context, LootItemCondition[] conditionsIn)
		{
			return new SetBoondollarCount(conditionsIn, GsonHelper.getAsObject(object, "count", context, NumberProvider.class));
		}
	}
}