package com.mraof.minestuck.world.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mraof.minestuck.item.KeyItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.storage.loot.IRandomRange;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootFunction;
import net.minecraft.world.storage.loot.RandomRanges;
import net.minecraft.world.storage.loot.conditions.ILootCondition;

public class SetKeyValues extends LootFunction
{
	private final IRandomRange countRange;
	//private final Dimension dimension;
	
	public SetKeyValues(ILootCondition[] conditionsIn, IRandomRange countRangeIn/*, Dimension dimensionIn*/)
	{
		super(conditionsIn);
		this.countRange = countRangeIn;
		//this.dimension = dimensionIn;
	}
	
	@Override
	protected ItemStack doApply(ItemStack stack, LootContext context)
	{
		//KeyItem.setDimension(stack, dimension);
		return KeyItem.setKeyType(stack, countRange.generateInt(context.getRandom()));
	}
	
	public static Builder<?> builder(IRandomRange range/*, Dimension dimension*/)
	{
		return builder((conditions) -> new SetKeyValues(conditions, range/*, dimension*/));
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
			//object.addProperty("dimension", functionClazz.dimension.getType().getRegistryName().toString());
		}
		
		public SetKeyValues deserialize(JsonObject object, JsonDeserializationContext deserializationContext, ILootCondition[] conditionsIn)
		{
			return new SetKeyValues(conditionsIn, RandomRanges.deserialize(object.get("key"), deserializationContext));
		}
	}
	
	/*
	public static Builder<?> builder(IRandomRange range, Dimension dimension)
	{
		return builder((conditions) -> new SetKeyValues(conditions, range, dimension));
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
			object.addProperty("dimension", functionClazz.dimension.getType().getRegistryName().toString());
		}
		
		public SetKeyValues deserialize(JsonObject object, JsonDeserializationContext deserializationContext, ILootCondition[] conditionsIn)
		{
			return new SetKeyValues(conditionsIn, RandomRanges.deserialize(object.get("key"), deserializationContext));
		}
	}
	 */
}