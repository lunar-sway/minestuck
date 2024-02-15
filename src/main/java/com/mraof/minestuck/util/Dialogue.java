package com.mraof.minestuck.util;

import com.google.gson.*;
import com.ibm.icu.impl.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.entity.DialogueEntity;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.player.ClientPlayerData;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.player.PlayerSavedData;
import com.mraof.minestuck.world.MSDimensions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.TriPredicate;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.util.TriConsumer;
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
			PLACEHOLDER((entity, player, stringStringPair) -> true),
			CONSORT_TYPE((entity, player, content) ->
					entity instanceof ConsortEntity consortEntity && content.first.equals(consortEntity.getConsortType().getName())
			),
			HAS_ITEM((entity, player, content) ->
			{
				if(player == null)
					return false;
				
				ItemStack stack = findPlayerItem(content.first, player, parseIntFromString(content.second, 1));
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
						return ClientPlayerData.getConsortReputation() > parseIntFromString(content.first, -9999);
					} else
					{
						PlayerData data = PlayerSavedData.getData((ServerPlayer) player);
						if(data != null)
							return data.getConsortReputation(level.dimension()) > parseIntFromString(content.first, -9999);
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
						return ClientPlayerData.getConsortReputation() < parseIntFromString(content.first, 9999);
					} else
					{
						PlayerData data = PlayerSavedData.getData((ServerPlayer) player);
						if(data != null)
							return data.getConsortReputation(level.dimension()) < parseIntFromString(content.first, 9999);
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
	
	public static ItemStack findPlayerItem(String registryName, Player player, int minAmount)
	{
		Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(registryName));
		
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
	
	private static int parseIntFromString(String member, int amount)
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
	
	/**
	 * A Trigger allows for new code to be called when a dialogue option is picked
	 */
	public static class Trigger
	{
		public enum Type
		{
			COMMAND((entity, player, s) ->
			{
				if(player != null)
				{
					try(Level level = player.level())
					{
						if(!level.isClientSide)
						{
							//TODO using the entity for this instead of the player failed
							level.getServer().getCommands().performPrefixedCommand(player.createCommandSourceStack(), s.first);
						}
					} catch(IOException e)
					{
						LOGGER.debug("Trigger in Dialogue tried to get null level from player");
					}
				}
			}),
			TAKE_ITEM((entity, player, s) ->
			{
				if(player != null)
				{
					int amount = parseIntFromString(s.second, 1);
					ItemStack stack = findPlayerItem(s.first, player, amount);
					if(stack != null)
					{
						stack.shrink(amount);
					}
				}
			}),
			SET_DIALOGUE((entity, player, s) ->
			{
				if(entity instanceof DialogueEntity dialogueEntity)
				{
					dialogueEntity.setDialogue(s.first);
				}
			}),
			ADD_CONSORT_REPUTATION((entity, player, s) ->
			{
				//TODO freezes server-side
				
				if(entity instanceof ConsortEntity consortEntity && player instanceof ServerPlayer serverPlayer)
				{
					PlayerData data = PlayerSavedData.getData(serverPlayer);
					if(data != null)
					{
						try
						{
							data.addConsortReputation(Integer.parseInt(s.first), consortEntity.getHomeDimension());
						} catch(NumberFormatException ignored)
						{
							LOGGER.debug("Failed to parse string from a Dialogue into an integer");
						}
					}
				}
			});
			
			private final TriConsumer<LivingEntity, Player, Pair<String, String>> conditions;
			
			Type(TriConsumer<LivingEntity, Player, Pair<String, String>> conditions)
			{
				this.conditions = conditions;
			}
			
			public static Type fromInt(int ordinal) //converts int back into enum
			{
				if(0 <= ordinal && ordinal < Type.values().length)
					return Type.values()[ordinal];
				else
					throw new IllegalArgumentException("Invalid ordinal of " + ordinal + " for Trigger type!");
			}
		}
		
		private final Type type;
		private final String content;
		@Nullable
		private final String contentExtra;
		
		public Trigger(Type type, String content)
		{
			this(type, content, null);
		}
		
		public Trigger(Type type, String content, @Nullable String contentExtra)
		{
			this.type = type;
			this.content = content;
			this.contentExtra = contentExtra;
		}
		
		public Type getType()
		{
			return type;
		}
		
		public String getContent()
		{
			return content;
		}
		
		public String getContentExtra()
		{
			return contentExtra == null ? "" : contentExtra;
		}
		
		//TODO since activation of these conditions occurs from a client packet to the server, we may want to check validity
		public void testConditions(LivingEntity entity, Player player)
		{
			type.conditions.accept(entity, player, Pair.of(content, getContentExtra()));
		}
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
				List<Trigger> triggers = deserializeTriggers(responseObject);
				
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
		
		private static List<Trigger> deserializeTriggers(JsonObject responseObject)
		{
			JsonArray triggersObject = responseObject.getAsJsonArray("triggers");
			List<Trigger> triggers = new ArrayList<>();
			triggersObject.forEach(triggerElement ->
			{
				JsonObject triggerObject = triggerElement.getAsJsonObject();
				
				String triggerTypeProvider = Codec.STRING.parse(JsonOps.INSTANCE, triggerObject.get("type")).getOrThrow(false, LOGGER::error);
				String triggerContentProvider = Codec.STRING.parse(JsonOps.INSTANCE, triggerObject.get("content")).getOrThrow(false, LOGGER::error);
				String triggerContentExtraProvider = GsonHelper.getAsString(triggerObject, "content_extra", null);
				
				//TODO may throw errors when its not filled in correctly
				String triggerTypeName = triggerTypeProvider.toUpperCase(Locale.ROOT);
				if(enumExists(Trigger.Type.class, triggerTypeName))
					triggers.add(new Trigger(Trigger.Type.valueOf(triggerTypeName), triggerContentProvider, triggerContentExtraProvider));
			});
			return triggers;
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
				
				JsonArray triggers = serializeTriggers(response);
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
		
		private static JsonArray serializeTriggers(Response response)
		{
			JsonArray triggers = new JsonArray(response.triggers.size());
			for(Trigger trigger : response.triggers)
			{
				JsonObject triggerObject = new JsonObject();
				
				String triggerType = trigger.type.toString().toLowerCase(Locale.ROOT);
				triggerObject.add("type", Codec.STRING.encodeStart(JsonOps.INSTANCE, triggerType).getOrThrow(false, LOGGER::error));
				triggerObject.add("content", Codec.STRING.encodeStart(JsonOps.INSTANCE, trigger.content).getOrThrow(false, LOGGER::error));
				triggerObject.addProperty("content_extra", trigger.contentExtra);
				
				triggers.add(triggerObject);
			}
			return triggers;
		}
	}
}