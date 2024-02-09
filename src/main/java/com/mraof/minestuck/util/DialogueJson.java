package com.mraof.minestuck.util;

import com.google.gson.*;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.slf4j.Logger;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DialogueJson
{
	private static final Logger LOGGER = LogUtils.getLogger();
	
	private final String message;
	private final String animation;
	private final ResourceLocation guiPath;
	private final List<Response> responses;
	
	public DialogueJson(String message, String animation, ResourceLocation guiPath, List<Response> responses)
	{
		this.message = message;
		this.animation = animation;
		this.guiPath = guiPath;
		this.responses = responses;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public String getAnimation()
	{
		return animation;
	}
	
	public ResourceLocation getGuiPath()
	{
		return guiPath;
	}
	
	public List<Response> getResponses()
	{
		return responses;
	}
	
	public static class Response
	{
		private final String response;
		private final List<String> conditions;
		private final ResourceLocation nextDialoguePath;
		
		public Response(String response, List<String> conditions, ResourceLocation nextDialoguePath)
		{
			this.response = response;
			this.conditions = conditions;
			this.nextDialoguePath = nextDialoguePath;
		}
		
		public String getResponse()
		{
			return response;
		}
		
		public List<String> getConditions()
		{
			return conditions;
		}
		
		public ResourceLocation getNextDialoguePath()
		{
			return nextDialoguePath;
		}
	}
	
	public static class Serializer implements JsonDeserializer<DialogueJson>, JsonSerializer<DialogueJson>
	{
		@Override
		public DialogueJson deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException
		{
			JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "entry");
			
			String messageProvider = Codec.STRING.parse(JsonOps.INSTANCE, json.get("message")).getOrThrow(false, LOGGER::error);
			String animationProvider = Codec.STRING.parse(JsonOps.INSTANCE, json.get("animation")).getOrThrow(false, LOGGER::error);
			ResourceLocation guiProvider = ResourceLocation.CODEC.parse(JsonOps.INSTANCE, json.get("gui")).getOrThrow(false, LOGGER::error);
			
			JsonArray responsesObject = json.getAsJsonArray("responses");
			List<Response> responses = new ArrayList<>();
			responsesObject.forEach(element ->
			{
				JsonObject responseObject = GsonHelper.convertToJsonObject(element, "responses");
				
				String responseProvider = Codec.STRING.parse(JsonOps.INSTANCE, responseObject.get("response_message")).getOrThrow(false, LOGGER::error);
				
				JsonArray conditionsObject = responseObject.getAsJsonArray("conditions");
				List<String> conditions = new ArrayList<>();
				conditionsObject.forEach(conditionElement ->
				{
					conditions.add(conditionElement.getAsString());
				});
				
				ResourceLocation nextPathProvider = ResourceLocation.CODEC.parse(JsonOps.INSTANCE, responseObject.get("next_path")).getOrThrow(false, LOGGER::error);
				
				responses.add(new Response(responseProvider, conditions, nextPathProvider));
			});
			
			return new DialogueJson(messageProvider, animationProvider, guiProvider, responses);
		}
		
		@Override
		public JsonElement serialize(DialogueJson dialogueJson, Type type, JsonSerializationContext context)
		{
			JsonObject json = new JsonObject();
			
			json.add("message", Codec.STRING.encodeStart(JsonOps.INSTANCE, dialogueJson.message).getOrThrow(false, LOGGER::error));
			json.add("animation", Codec.STRING.encodeStart(JsonOps.INSTANCE, dialogueJson.animation).getOrThrow(false, LOGGER::error));
			json.add("gui", ResourceLocation.CODEC.encodeStart(JsonOps.INSTANCE, dialogueJson.guiPath).getOrThrow(false, LOGGER::error));
			
			JsonArray responses = new JsonArray(dialogueJson.responses.size());
			for(Response response : dialogueJson.responses)
			{
				JsonObject responseObject = new JsonObject();
				responseObject.add("response_message", Codec.STRING.encodeStart(JsonOps.INSTANCE, response.response).getOrThrow(false, LOGGER::error));
				
				JsonArray conditions = new JsonArray(response.conditions.size());
				for(String condition : response.conditions)
				{
					conditions.add(condition);
				}
				responseObject.add("conditions", conditions);
				
				responseObject.add("next_path", ResourceLocation.CODEC.encodeStart(JsonOps.INSTANCE, response.nextDialoguePath).getOrThrow(false, LOGGER::error));
				responses.add(responseObject);
			}
			json.add("responses", responses);
			
			return json;
		}
	}
}