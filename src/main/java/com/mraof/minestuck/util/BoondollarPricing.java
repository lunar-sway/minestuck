package com.mraof.minestuck.util;

import com.google.gson.*;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.slf4j.Logger;

import java.lang.reflect.Type;

public record BoondollarPricing(Ingredient ingredient, IntProvider priceRange)
{
	private static final Logger LOGGER = LogUtils.getLogger();
	
	public int generatePrice(RandomSource random)
	{
		return priceRange.sample(random);
	}
	
	public boolean appliesTo(ItemStack stack)
	{
		return ingredient.test(stack);
	}
	
	//todo replace serializer with codec
	public static class Serializer implements JsonDeserializer<BoondollarPricing>, JsonSerializer<BoondollarPricing>
	{
		@Override
		public BoondollarPricing deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException
		{
			JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "boolean pricing");
			IntProvider priceProvider = IntProvider.CODEC.parse(JsonOps.INSTANCE, json.get("range")).getOrThrow(false, LOGGER::error);
			return new BoondollarPricing(Ingredient.fromJson(json.get("ingredient"), false), priceProvider);
		}
		
		@Override
		public JsonElement serialize(BoondollarPricing pricing, Type type, JsonSerializationContext context)
		{
			JsonObject json = new JsonObject();
			json.add("ingredient", Ingredient.CODEC_NONEMPTY.encodeStart(JsonOps.INSTANCE, pricing.ingredient).getOrThrow(false, LOGGER::error));
			json.add("range", IntProvider.CODEC.encodeStart(JsonOps.INSTANCE, pricing.priceRange).getOrThrow(false, LOGGER::error));
			return json;
		}
	}
}
