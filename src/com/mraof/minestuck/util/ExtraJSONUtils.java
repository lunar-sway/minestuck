package com.mraof.minestuck.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.JSONUtils;

/**
 * For any json utils not found in {@link net.minecraft.util.JSONUtils}
 */
public class ExtraJSONUtils
{
	public static long getLong(JsonElement json, String key)
	{
		if(json.isJsonPrimitive() && json.getAsJsonPrimitive().isNumber())
		{
			return json.getAsLong();
		} else
		{
			throw new JsonSyntaxException("Expected " + key + " to be a Long, was " + JSONUtils.toString(json));
		}
	}
	
	public static long getLong(JsonObject json, String key)
	{
		if(json.has(key))
		{
			return getLong(json.get(key), key);
		} else
		{
			throw new JsonSyntaxException("Missing " + key + ", expected to find a Long");
		}
	}
}