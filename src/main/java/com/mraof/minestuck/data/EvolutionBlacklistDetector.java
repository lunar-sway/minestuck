package com.mraof.minestuck.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Decides which swords can't get a half version
 */
public final class EvolutionBlacklistDetector
{
	private static final String MODEL_FOLDER = "models/item";
	private static final String TEXTURE_FOLDER = "textures";
	
	/**
	 * Weapons whose texture is not named like the item, when they don't have a hand-written model that says which texture is used.
	 */
	public static final Map<String, String> BASE_TEXTURE_OVERRIDES = Map.of("beef_sword", "raw_beef_sword", "dogg_machete", "snoop_dogg_machete");
	
	private EvolutionBlacklistDetector()
	{
	}
	
	/**
	 * @param item the full (not half!!) weapon
	 */
	public static boolean isUnsupported(ExistingFileHelper fileHelper, ResourceLocation item)
	{
		List<ResourceLocation> textures = new ArrayList<>();
		
		if(fileHelper.exists(item, PackType.CLIENT_RESOURCES, ".json", MODEL_FOLDER))
		{
			JsonObject model = readJson(fileHelper, item);
			if(model.has("elements")) return true; // 3d model
			
			if(model.has("textures"))
				for(Map.Entry<String, JsonElement> entry : model.getAsJsonObject("textures").entrySet())
					if(entry.getValue().isJsonPrimitive() && !entry.getValue().getAsString().startsWith("#"))
						textures.add(ResourceLocation.parse(entry.getValue().getAsString()));
		} else
		{
			textures.add(item.withPath("item/" + BASE_TEXTURE_OVERRIDES.getOrDefault(item.getPath(), item.getPath())));
		}
		
		for(ResourceLocation texture : textures)
		{
			if(fileHelper.exists(texture, PackType.CLIENT_RESOURCES, ".png.mcmeta", TEXTURE_FOLDER))
				return true; // animated
			
			if(fileHelper.exists(texture, PackType.CLIENT_RESOURCES, ".png", TEXTURE_FOLDER))
			{
				BufferedImage image = readImage(fileHelper, texture);
				if(!HalfBladeCutter.isSupportedSize(image.getWidth(), image.getHeight())) return true;
			}
		}
		return false;
	}
	
	private static JsonObject readJson(ExistingFileHelper fileHelper, ResourceLocation model)
	{
		try(InputStream input = fileHelper.getResource(model, PackType.CLIENT_RESOURCES, ".json", MODEL_FOLDER).open(); InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8))
		{
			return JsonParser.parseReader(reader).getAsJsonObject();
		} catch(IOException e)
		{
			throw new UncheckedIOException("Unable to read the model " + model, e);
		}
	}
	
	private static BufferedImage readImage(ExistingFileHelper fileHelper, ResourceLocation texture)
	{
		try(InputStream input = fileHelper.getResource(texture, PackType.CLIENT_RESOURCES, ".png", TEXTURE_FOLDER).open())
		{
			return ImageIO.read(input);
		} catch(IOException e)
		{
			throw new UncheckedIOException("Unable to read the texture " + texture, e);
		}
	}
}
