package com.mraof.minestuck.computer.theme;

import com.google.gson.*;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.Minestuck;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.valueproviders.ConstantInt;
import org.slf4j.Logger;

import java.lang.reflect.Type;

/**
 * This class is used to define json files that will be used to determine what wallpaper and text color appears on in-game computers.
 */
public class ComputerTheme
{
	private static final Logger LOGGER = LogUtils.getLogger();
	
	public static final int DEFAULT_TEXT_COLOR = 0x404040;
	public static final ResourceLocation DEFAULT_TEXTURE_PATH = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/theme/default.png");
	
	private final ResourceLocation texturePath;
	private final ConstantInt textColor;
	private final String themeName;
	
	public ComputerTheme(ResourceLocation texturePath, ConstantInt textColor, String themeName)
	{
		this.texturePath = texturePath;
		this.textColor = textColor;
		this.themeName = themeName;
	}
	
	public int getTextColor()
	{
		return textColor.getValue();
	}
	
	public ResourceLocation getTexturePath()
	{
		return texturePath;
	}
	
	public String getThemeName()
	{
		return themeName;
	}
	
	public static class Serializer implements JsonDeserializer<ComputerTheme>, JsonSerializer<ComputerTheme>
	{
		@Override
		public ComputerTheme deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException
		{
			JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "entry");
			ResourceLocation texturePathProvider = ResourceLocation.CODEC.parse(JsonOps.INSTANCE, json.get("texture_location")).getOrThrow(false, LOGGER::error);
			ConstantInt textColorProvider = ConstantInt.CODEC.parse(JsonOps.INSTANCE, json.get("text_color")).getOrThrow(false, LOGGER::error);
			String themeNameProvider = Codec.STRING.parse(JsonOps.INSTANCE, json.get("theme_name")).getOrThrow(false, LOGGER::error);
			return new ComputerTheme(texturePathProvider, textColorProvider, themeNameProvider);
		}
		
		@Override
		public JsonElement serialize(ComputerTheme themeData, Type type, JsonSerializationContext context)
		{
			JsonObject json = new JsonObject();
			json.add("texture_location", ResourceLocation.CODEC.encodeStart(JsonOps.INSTANCE, themeData.texturePath).getOrThrow(false, LOGGER::error));
			json.add("text_color", ConstantInt.CODEC.encodeStart(JsonOps.INSTANCE, themeData.textColor).getOrThrow(false, LOGGER::error));
			json.add("theme_name", Codec.STRING.encodeStart(JsonOps.INSTANCE, themeData.themeName).getOrThrow(false, LOGGER::error));
			return json;
		}
	}
}