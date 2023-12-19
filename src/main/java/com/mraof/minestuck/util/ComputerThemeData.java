package com.mraof.minestuck.util;

import com.google.gson.*;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.slf4j.Logger;

import java.lang.reflect.Type;

public class ComputerThemeData
{
	private static final Logger LOGGER = LogUtils.getLogger();
	
	private final ResourceLocation texturePath;
	private final ConstantInt textColor;
	
	public ComputerThemeData(ResourceLocation texturePath, ConstantInt textColor)
	{
		this.texturePath = texturePath;
		this.textColor = textColor;
	}
	
	public int getTextColor()
	{
		return textColor.getValue();
	}
	
	public ResourceLocation getTexturePath()
	{
		return texturePath;
	}
	
	public boolean appliesTo(ResourceLocation stack)
	{
		return stack == texturePath;
	}
	
	public static class Serializer implements JsonDeserializer<ComputerThemeData>, JsonSerializer<ComputerThemeData>
	{
		@Override
		public ComputerThemeData deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException
		{
			JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "boolean pricing");
			ResourceLocation texturePathProvider = ResourceLocation.CODEC.parse(JsonOps.INSTANCE, json.get("texture_location")).getOrThrow(false, LOGGER::error);
			ConstantInt textColorProvider = ConstantInt.CODEC.parse(JsonOps.INSTANCE, json.get("text_color")).getOrThrow(false, LOGGER::error);
			return new ComputerThemeData(texturePathProvider, textColorProvider);
		}
		
		@Override
		public JsonElement serialize(ComputerThemeData themeData, Type type, JsonSerializationContext context)
		{
			JsonObject json = new JsonObject();
			json.add("texture_location", ResourceLocation.CODEC.encodeStart(JsonOps.INSTANCE, themeData.texturePath).getOrThrow(false, LOGGER::error));
			json.add("text_color", ConstantInt.CODEC.encodeStart(JsonOps.INSTANCE, themeData.textColor).getOrThrow(false, LOGGER::error));
			return json;
		}
	}
}