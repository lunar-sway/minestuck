package com.mraof.minestuck.util;

import com.google.gson.*;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.slf4j.Logger;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DialogueJson
{
	private static final Logger LOGGER = LogUtils.getLogger();
	
	private final String message;
	private final List<Response> responses;
	
	public DialogueJson(String message, /*String animation, ResourceLocation guiPath, */List<Response> responses)
	{
		this.message = message;
		this.responses = responses;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public List<Response> getResponses()
	{
		return responses;
	}
	
	public static class Response
	{
		private final String response;
		
		public Response(String response/*, List<String> conditions, ResourceLocation nextDialoguePath*/)
		{
			this.response = response;
		}
		
		public String getResponse()
		{
			return response;
		}
	}
	
	public static class Serializer implements JsonDeserializer<DialogueJson>, JsonSerializer<DialogueJson>
	{
		@Override
		public DialogueJson deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException
		{
			JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "entry");
			String messageProvider = Codec.STRING.parse(JsonOps.INSTANCE, json.get("message")).getOrThrow(false, LOGGER::error);
			
			JsonArray list = json.getAsJsonArray("responses");
			List<Response> responses = new ArrayList<>();
			list.forEach(element -> responses.add(new Response(GsonHelper.convertToString(element, "responses"))));
			
			return new DialogueJson(messageProvider, responses);
		}
		
		@Override
		public JsonElement serialize(DialogueJson dialogueJson, Type type, JsonSerializationContext context)
		{
			JsonObject json = new JsonObject();
			
			json.add("message", Codec.STRING.encodeStart(JsonOps.INSTANCE, dialogueJson.message).getOrThrow(false, LOGGER::error));
			
			JsonArray responses = new JsonArray(dialogueJson.getResponses().size());
			for(Response response : dialogueJson.getResponses())
			{
				responses.add(new JsonPrimitive(response.getResponse()));
			}
			json.add("responses", responses);
			
			return json;
		}
	}
}