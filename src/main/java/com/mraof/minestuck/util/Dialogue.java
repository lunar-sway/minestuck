package com.mraof.minestuck.util;

import com.google.gson.*;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A data driven object that contains everything which determines what shows up on the screen when the dialogue window is opened.
 */
public class Dialogue
{
	private static final Logger LOGGER = LogUtils.getLogger();
	
	private final ResourceLocation path;
	private final String message;
	private final String animation;
	private final ResourceLocation guiPath;
	private final List<Response> responses;
	
	public Dialogue(ResourceLocation path, String message, String animation, ResourceLocation guiPath, List<Response> responses)
	{
		this.path = path;
		this.message = message;
		this.animation = animation;
		this.guiPath = guiPath;
		this.responses = responses;
	}
	
	public ResourceLocation getPath()
	{
		return path;
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
		private final List<Condition> conditions;
		private final ResourceLocation nextDialoguePath;
		private final boolean hideIfFailed;
		
		public Response(String response, List<Condition> conditions, ResourceLocation nextDialoguePath, boolean hideIfFailed)
		{
			this.response = response;
			this.conditions = conditions;
			this.nextDialoguePath = nextDialoguePath;
			this.hideIfFailed = hideIfFailed;
		}
		
		public String getResponse()
		{
			return response;
		}
		
		public List<Condition> getConditions()
		{
			return conditions;
		}
		
		public ResourceLocation getNextDialoguePath()
		{
			return nextDialoguePath;
		}
		
		public boolean isHideIfFailed()
		{
			return hideIfFailed;
		}
		
		public boolean failsWhileImportant(LivingEntity entity)
		{
			return !matchesAllConditions(entity) && isHideIfFailed();
		}
		
		public boolean matchesAllConditions(LivingEntity entity)
		{
			for(Condition condition : conditions)
			{
				if(condition.type.equals(Condition.Type.CONSORT_TYPE))
				{
					boolean consortMatches = entity instanceof ConsortEntity consortEntity && condition.content.equals(consortEntity.getConsortType().getName());
					if(!consortMatches)
						return false;
				}
			}
			
			return true;
		}
	}
	
	/**
	 * A Condition controls whether a piece of dialogue will show up
	 */
	public static class Condition
	{
		public enum Type
		{
			CONSORT_TYPE
		}
		
		private final Type type;
		private final String content;
		@Nullable
		private final String failureTooltip;
		
		public Condition(Type type, String content, @Nullable String failureTooltip)
		{
			this.type = type;
			this.content = content;
			this.failureTooltip = failureTooltip;
		}
		
		public String getContent()
		{
			return content;
		}
		
		@Nullable
		public String getFailureTooltip()
		{
			return failureTooltip;
		}
		
		public static boolean enumExists(String enumName)
		{
			for(Type type : Type.values())
			{
				if(type.name().toLowerCase(Locale.ROOT).equals(enumName.toLowerCase(Locale.ROOT)))
					return true;
			}
			
			return false;
		}
	}
	
	public static class Serializer implements JsonDeserializer<Dialogue>, JsonSerializer<Dialogue>
	{
		@Override
		public Dialogue deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException
		{
			JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "entry");
			
			ResourceLocation pathProvider = ResourceLocation.CODEC.parse(JsonOps.INSTANCE, json.get("path")).getOrThrow(false, LOGGER::error);
			
			String messageProvider = Codec.STRING.parse(JsonOps.INSTANCE, json.get("message")).getOrThrow(false, LOGGER::error);
			String animationProvider = Codec.STRING.parse(JsonOps.INSTANCE, json.get("animation")).getOrThrow(false, LOGGER::error);
			ResourceLocation guiProvider = ResourceLocation.CODEC.parse(JsonOps.INSTANCE, json.get("gui")).getOrThrow(false, LOGGER::error);
			
			List<Response> responses = getResponses(json);
			
			return new Dialogue(pathProvider, messageProvider, animationProvider, guiProvider, responses);
		}
		
		private static List<Response> getResponses(JsonObject json)
		{
			JsonArray responsesObject = json.getAsJsonArray("responses");
			List<Response> responses = new ArrayList<>();
			responsesObject.forEach(element ->
			{
				JsonObject responseObject = GsonHelper.convertToJsonObject(element, "responses");
				
				String responseProvider = Codec.STRING.parse(JsonOps.INSTANCE, responseObject.get("response_message")).getOrThrow(false, LOGGER::error);
				
				JsonArray conditionsObject = responseObject.getAsJsonArray("conditions");
				List<Condition> conditions = new ArrayList<>();
				conditionsObject.forEach(conditionElement ->
				{
					JsonObject conditionObject = conditionElement.getAsJsonObject();
					
					String conditionTypeProvider = Codec.STRING.parse(JsonOps.INSTANCE, conditionObject.get("type")).getOrThrow(false, LOGGER::error);
					String conditionContentProvider = Codec.STRING.parse(JsonOps.INSTANCE, conditionObject.get("content")).getOrThrow(false, LOGGER::error);
					
					String conditionFailureTooltipProvider = GsonHelper.getAsString(conditionObject, "failure_tooltip", null);
					
					//TODO may throw errors when its not filled in correctly
					String conditionTypeName = conditionTypeProvider.toUpperCase(Locale.ROOT);
					if(Condition.enumExists(conditionTypeName))
						conditions.add(new Condition(Condition.Type.valueOf(conditionTypeName), conditionContentProvider, conditionFailureTooltipProvider));
				});
				
				ResourceLocation nextPathProvider = ResourceLocation.CODEC.parse(JsonOps.INSTANCE, responseObject.get("next_path")).getOrThrow(false, LOGGER::error);
				
				boolean hideIfFailedProvider = responseObject.get("hide_if_failed").getAsBoolean();
				
				responses.add(new Response(responseProvider, conditions, nextPathProvider, hideIfFailedProvider));
			});
			return responses;
		}
		
		@Override
		public JsonElement serialize(Dialogue dialogue, Type type, JsonSerializationContext context)
		{
			JsonObject json = new JsonObject();
			
			json.add("path", ResourceLocation.CODEC.encodeStart(JsonOps.INSTANCE, dialogue.path).getOrThrow(false, LOGGER::error));
			
			json.add("message", Codec.STRING.encodeStart(JsonOps.INSTANCE, dialogue.message).getOrThrow(false, LOGGER::error));
			json.add("animation", Codec.STRING.encodeStart(JsonOps.INSTANCE, dialogue.animation).getOrThrow(false, LOGGER::error));
			json.add("gui", ResourceLocation.CODEC.encodeStart(JsonOps.INSTANCE, dialogue.guiPath).getOrThrow(false, LOGGER::error));
			
			JsonArray responses = new JsonArray(dialogue.responses.size());
			for(Response response : dialogue.responses)
			{
				JsonObject responseObject = new JsonObject();
				responseObject.add("response_message", Codec.STRING.encodeStart(JsonOps.INSTANCE, response.response).getOrThrow(false, LOGGER::error));
				
				JsonArray conditions = new JsonArray(response.conditions.size());
				for(Condition condition : response.conditions)
				{
					JsonObject conditionObject = new JsonObject();
					
					String conditionType = condition.type.toString().toLowerCase(Locale.ROOT);
					conditionObject.add("type", Codec.STRING.encodeStart(JsonOps.INSTANCE, conditionType).getOrThrow(false, LOGGER::error));
					conditionObject.add("content", Codec.STRING.encodeStart(JsonOps.INSTANCE, condition.content).getOrThrow(false, LOGGER::error));
					if(condition.failureTooltip != null)
						conditionObject.add("failure_tooltip", Codec.STRING.encodeStart(JsonOps.INSTANCE, condition.failureTooltip).getOrThrow(false, LOGGER::error));
					
					conditions.add(conditionObject);
				}
				responseObject.add("conditions", conditions);
				
				responseObject.add("next_path", ResourceLocation.CODEC.encodeStart(JsonOps.INSTANCE, response.nextDialoguePath).getOrThrow(false, LOGGER::error));
				
				responseObject.addProperty("hide_if_failed", response.hideIfFailed);
				
				responses.add(responseObject);
			}
			json.add("responses", responses);
			
			return json;
		}
	}
}