package com.mraof.minestuck.util;

import com.google.gson.*;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.entity.carapacian.CarapacianEntity;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.player.ClientPlayerData;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.player.PlayerSavedData;
import com.mraof.minestuck.world.MSDimensions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.TriPredicate;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
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
	private final UseContext useContext;
	
	public Dialogue(ResourceLocation path, String message, String animation, ResourceLocation guiPath, List<Response> responses, @Nullable UseContext useContext)
	{
		this.path = path;
		this.message = message;
		this.animation = animation;
		this.guiPath = guiPath;
		this.responses = responses;
		this.useContext = useContext;
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
	
	public UseContext getUseContext()
	{
		return useContext;
	}
	
	/**
	 * A Response contains all possible Dialogues that can be reached from the present Dialogue.
	 * It contains the message that represents the Response, any Conditions/Triggers, the location of the next Dialogue, and a boolean determining whether the Response should be visible when it fails to meet the Conditions
	 */
	public static class Response
	{
		private final String response;
		private final List<Condition> conditions;
		private final List<Trigger> triggers;
		private final ResourceLocation nextDialoguePath;
		private final boolean hideIfFailed;
		
		public Response(String response, List<Condition> conditions, List<Trigger> triggers, ResourceLocation nextDialoguePath, boolean hideIfFailed)
		{
			this.response = response;
			this.conditions = conditions;
			this.triggers = triggers;
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
		
		public List<Trigger> getTriggers()
		{
			return triggers;
		}
		
		public ResourceLocation getNextDialoguePath()
		{
			return nextDialoguePath;
		}
		
		public boolean isHideIfFailed()
		{
			return hideIfFailed;
		}
		
		public boolean failsWhileImportant(LivingEntity entity, Player player)
		{
			return !Dialogue.Condition.matchesAllConditions(entity, player, conditions) && isHideIfFailed();
		}
	}
	
	/**
	 * A Condition controls whether a piece of dialogue will show up
	 */
	public static class Condition
	{
		//TODO conditions are used in both server and client side context. When they are used to help pick dialogue, players are inherently null
		public enum Type
		{
			CONDITIONLESS((entity, player, stringStringPair) -> true),
			IS_CONSORT((entity, player, content) -> entity instanceof ConsortEntity),
			IS_CARAPACIAN((entity, player, content) -> entity instanceof CarapacianEntity),
			IS_ENTITY_TYPE((entity, player, content) ->
			{
				EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(content.getFirst()));
				return entityType != null && entity.getType().equals(entityType);
			}),
			CONSORT_TYPE((entity, player, content) ->
					//distinct in that it uses consort type, not the entity type
					entity instanceof ConsortEntity consortEntity && content.getFirst().equals(consortEntity.getConsortType().getName())
			),
			HAS_ITEM((entity, player, content) ->
			{
				if(player == null)
					return false;
				
				ItemStack stack = findPlayerItem(content.getFirst(), player, parseIntFromString(content.getSecond(), 1));
				return stack != null;
			}),
			IN_LAND((entity, player, content) ->
			{
				try(Level level = player.level())
				{
					if(!level.isClientSide)
					{
						return MSDimensions.isLandDimension(player.getServer(), level.dimension());
					}
				} catch(IOException e)
				{
					LOGGER.debug("Condition in Dialogue tried to get null level from entity");
				}
				
				return false;
			}),
			HAS_MINIMUM_REPUTATION((entity, player, content) ->
			{
				if(player == null)
					return false;
				
				try(Level level = player.level())
				{
					if(level.isClientSide)
					{
						return ClientPlayerData.getConsortReputation() > parseIntFromString(content.getFirst(), -9999);
					} else
					{
						PlayerData data = PlayerSavedData.getData((ServerPlayer) player);
						if(data != null)
							return data.getConsortReputation(level.dimension()) > parseIntFromString(content.getFirst(), -9999);
					}
				} catch(IOException e)
				{
					LOGGER.debug("Condition in Dialogue tried to get null level from player");
				}
				
				return false;
			}),
			HAS_MAXIMUM_REPUTATION((entity, player, content) ->
			{
				if(player == null)
					return false;
				
				try(Level level = player.level())
				{
					if(level.isClientSide)
					{
						return ClientPlayerData.getConsortReputation() < parseIntFromString(content.getFirst(), 9999);
					} else
					{
						PlayerData data = PlayerSavedData.getData((ServerPlayer) player);
						if(data != null)
							return data.getConsortReputation(level.dimension()) < parseIntFromString(content.getFirst(), 9999);
					}
				} catch(IOException e)
				{
					LOGGER.debug("Condition in Dialogue tried to get null level from player");
				}
				
				return false;
			});
			
			private final TriPredicate<LivingEntity, Player, Pair<String, String>> conditions;
			
			Type(TriPredicate<LivingEntity, Player, Pair<String, String>> conditions)
			{
				this.conditions = conditions;
			}
		}
		
		private final Type type;
		private final String content;
		@Nullable
		private final String contentExtra;
		@Nullable
		private final String failureTooltip;
		
		public Condition(Type type)
		{
			this(type, "", null, null);
		}
		
		public Condition(Type type, String content)
		{
			this(type, content, null, null);
		}
		
		public Condition(Type type, String content, @Nullable String contentExtra, @Nullable String failureTooltip)
		{
			this.type = type;
			this.content = content;
			this.contentExtra = contentExtra;
			this.failureTooltip = failureTooltip;
		}
		
		public String getContent()
		{
			return content;
		}
		
		public String getContentExtra()
		{
			return contentExtra == null ? "" : contentExtra;
		}
		
		@Nullable
		public String getFailureTooltip()
		{
			return failureTooltip;
		}
		
		public static boolean matchesAllConditions(LivingEntity entity, Player player, List<Condition> conditions)
		{
			if(conditions == null)
				return false;
			
			for(Condition condition : conditions)
			{
				if(!condition.type.conditions.test(entity, player, Pair.of(condition.getContent(), condition.getContentExtra())))
				{
					return false;
				}
			}
			
			return true;
		}
	}
	
	public static class UseContext
	{
		private final List<Condition> conditions;
		
		public UseContext(List<Condition> conditions)
		{
			this.conditions = conditions;
		}
		
		public List<Condition> getConditions()
		{
			return conditions;
		}
	}
	
	@Nullable
	public static ItemStack findPlayerItem(String registryName, Player player, int minAmount)
	{
		return findPlayerItem(ForgeRegistries.ITEMS.getValue(new ResourceLocation(registryName)), player, minAmount);
	}
	
	@Nullable
	public static ItemStack findPlayerItem(Item item, Player player, int minAmount)
	{
		for(ItemStack invItem : player.getInventory().items)
		{
			if(invItem.is(item))
			{
				if(invItem.getCount() >= minAmount)
					return invItem;
			}
		}
		
		return null;
	}
	
	static int parseIntFromString(String member, int amount)
	{
		if(member != null && !member.isEmpty())
		{
			try
			{
				amount = Integer.parseInt(member);
			} catch(NumberFormatException ignored)
			{
				LOGGER.debug("Failed to parse string from a Dialogue into an integer");
			}
		}
		
		return amount;
	}
	
	public static <T extends Enum<T>> boolean enumExists(Class<T> enumClass, String enumName)
	{
		for(T enumConstant : enumClass.getEnumConstants())
		{
			if(enumConstant.name().equalsIgnoreCase(enumName))
				return true;
		}
		
		return false;
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
			
			UseContext useContext = null;
			JsonObject useContextObject = json.getAsJsonObject("use_context");
			List<Condition> conditions;
			if(useContextObject != null)
			{
				conditions = deserializeConditions(useContextObject);
				useContext = new UseContext(conditions);
			}
			
			List<Response> responsesProvider = deserializeResponses(json);
			
			return new Dialogue(pathProvider, messageProvider, animationProvider, guiProvider, responsesProvider, useContext);
		}
		
		private static List<Response> deserializeResponses(JsonObject json)
		{
			JsonArray responsesObject = json.getAsJsonArray("responses");
			List<Response> responses = new ArrayList<>();
			responsesObject.forEach(element ->
			{
				JsonObject responseObject = GsonHelper.convertToJsonObject(element, "responses");
				
				String responseProvider = Codec.STRING.parse(JsonOps.INSTANCE, responseObject.get("response_message")).getOrThrow(false, LOGGER::error);
				
				List<Condition> conditions = deserializeConditions(responseObject);
				List<Trigger> triggers = Trigger.deserializeTriggers(responseObject);
				
				ResourceLocation nextPathProvider = ResourceLocation.CODEC.parse(JsonOps.INSTANCE, responseObject.get("next_path")).getOrThrow(false, LOGGER::error);
				boolean hideIfFailedProvider = responseObject.get("hide_if_failed").getAsBoolean();
				
				responses.add(new Response(responseProvider, conditions, triggers, nextPathProvider, hideIfFailedProvider));
			});
			return responses;
		}
		
		private static List<Condition> deserializeConditions(JsonObject responseObject)
		{
			JsonArray conditionsObject = responseObject.getAsJsonArray("conditions");
			List<Condition> conditions = new ArrayList<>();
			conditionsObject.forEach(conditionElement ->
			{
				JsonObject conditionObject = conditionElement.getAsJsonObject();
				
				String conditionTypeProvider = Codec.STRING.parse(JsonOps.INSTANCE, conditionObject.get("type")).getOrThrow(false, LOGGER::error);
				String conditionContentProvider = Codec.STRING.parse(JsonOps.INSTANCE, conditionObject.get("content")).getOrThrow(false, LOGGER::error);
				String conditionContentExtraProvider = GsonHelper.getAsString(conditionObject, "content_extra", null);
				String conditionFailureTooltipProvider = GsonHelper.getAsString(conditionObject, "failure_tooltip", null);
				
				//TODO may throw errors when its not filled in correctly
				String conditionTypeName = conditionTypeProvider.toUpperCase(Locale.ROOT);
				if(enumExists(Condition.Type.class, conditionTypeName))
					conditions.add(new Condition(Condition.Type.valueOf(conditionTypeName), conditionContentProvider, conditionContentExtraProvider, conditionFailureTooltipProvider));
			});
			return conditions;
		}
		
		@Override
		public JsonElement serialize(Dialogue dialogue, Type type, JsonSerializationContext context)
		{
			JsonObject json = new JsonObject();
			
			json.add("path", ResourceLocation.CODEC.encodeStart(JsonOps.INSTANCE, dialogue.path).getOrThrow(false, LOGGER::error));
			json.add("message", Codec.STRING.encodeStart(JsonOps.INSTANCE, dialogue.message).getOrThrow(false, LOGGER::error));
			json.add("animation", Codec.STRING.encodeStart(JsonOps.INSTANCE, dialogue.animation).getOrThrow(false, LOGGER::error));
			json.add("gui", ResourceLocation.CODEC.encodeStart(JsonOps.INSTANCE, dialogue.guiPath).getOrThrow(false, LOGGER::error));
			
			JsonArray responses = serializeResponses(dialogue);
			json.add("responses", responses);
			
			if(dialogue.useContext != null)
			{
				JsonObject useContextObject = new JsonObject();
				JsonArray conditions = serializeConditions(dialogue.useContext.conditions);
				useContextObject.add("conditions", conditions);
				json.add("use_context", useContextObject);
			}
			
			return json;
		}
		
		private static JsonArray serializeResponses(Dialogue dialogue)
		{
			JsonArray responses = new JsonArray(dialogue.responses.size());
			for(Response response : dialogue.responses)
			{
				JsonObject responseObject = new JsonObject();
				responseObject.add("response_message", Codec.STRING.encodeStart(JsonOps.INSTANCE, response.response).getOrThrow(false, LOGGER::error));
				
				JsonArray conditions = serializeConditions(response.conditions);
				responseObject.add("conditions", conditions);
				
				JsonArray triggers = Trigger.serializeTriggers(response.triggers);
				responseObject.add("triggers", triggers);
				
				responseObject.add("next_path", ResourceLocation.CODEC.encodeStart(JsonOps.INSTANCE, response.nextDialoguePath).getOrThrow(false, LOGGER::error));
				responseObject.addProperty("hide_if_failed", response.hideIfFailed);
				
				responses.add(responseObject);
			}
			return responses;
		}
		
		private static JsonArray serializeConditions(List<Condition> conditions)
		{
			JsonArray conditionsArray = new JsonArray(conditions.size());
			for(Condition condition : conditions)
			{
				JsonObject conditionObject = new JsonObject();
				
				String conditionType = condition.type.toString().toLowerCase(Locale.ROOT);
				conditionObject.add("type", Codec.STRING.encodeStart(JsonOps.INSTANCE, conditionType).getOrThrow(false, LOGGER::error));
				conditionObject.add("content", Codec.STRING.encodeStart(JsonOps.INSTANCE, condition.content).getOrThrow(false, LOGGER::error));
				conditionObject.addProperty("content_extra", condition.contentExtra);
				conditionObject.addProperty("failure_tooltip", condition.failureTooltip);
				
				conditionsArray.add(conditionObject);
			}
			return conditionsArray;
		}
	}
}