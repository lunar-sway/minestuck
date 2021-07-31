package com.mraof.minestuck.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mraof.minestuck.item.KeyItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.IRandomRange;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootFunction;
import net.minecraft.world.storage.loot.RandomRanges;
import net.minecraft.world.storage.loot.conditions.ILootCondition;

public class SetKeyValues extends LootFunction
{
	private final IRandomRange countRange;
	
	public SetKeyValues(ILootCondition[] conditionsIn, IRandomRange countRangeIn)
	{
		super(conditionsIn);
		this.countRange = countRangeIn;
	}
	
	@Override
	protected ItemStack doApply(ItemStack stack, LootContext context)
	{
		return KeyItem.setKeyType(stack, countRange.generateInt(context.getRandom()));
	}
	
	public static Builder<?> builder(IRandomRange range)
	{
		return builder((conditions) -> new SetKeyValues(conditions, range));
	}
	
	public static class Serializer extends LootFunction.Serializer<SetKeyValues>
	{
		public Serializer()
		{
			super(new ResourceLocation("minestuck:set_key_values"), SetKeyValues.class);
		}
		
		public void serialize(JsonObject object, SetKeyValues functionClazz, JsonSerializationContext serializationContext)
		{
			object.add("key", RandomRanges.serialize(functionClazz.countRange, serializationContext));
		}
		
		public SetKeyValues deserialize(JsonObject object, JsonDeserializationContext deserializationContext, ILootCondition[] conditionsIn)
		{
			return new SetKeyValues(conditionsIn, RandomRanges.deserialize(object.get("key"), deserializationContext));
		}
	}
}