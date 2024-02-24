package com.mraof.minestuck.entity.dialogue;

import com.google.gson.*;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.network.DialogueScreenPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A data driven object that contains everything which determines what shows up on the screen when the dialogue window is opened.
 */
public record Dialogue(ResourceLocation path, String message, String animation, ResourceLocation guiPath, List<Response> responses, UseContext useContext)
{
	private static final Logger LOGGER = LogUtils.getLogger();
	
	public static Codec<Dialogue> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(ResourceLocation.CODEC.fieldOf("path").forGetter(Dialogue::path),
							Codec.STRING.fieldOf("message").forGetter(Dialogue::message),
							Codec.STRING.fieldOf("animation").forGetter(Dialogue::animation),
							ResourceLocation.CODEC.fieldOf("gui_path").forGetter(Dialogue::guiPath),
							Codec.list(Response.CODEC).fieldOf("responses").forGetter(Dialogue::responses),
							UseContext.CODEC.fieldOf("use_context").forGetter(Dialogue::useContext))
					.apply(instance, Dialogue::new));
	
	public Dialogue(ResourceLocation path, String message, String animation, ResourceLocation guiPath, List<Response> responses, @Nullable UseContext useContext)
	{
		this.path = path;
		this.message = message;
		this.animation = animation;
		this.guiPath = guiPath;
		this.responses = responses;
		this.useContext = useContext;
	}
	
	/**
	 * Opens up the dialogue screen and includes an nbt object containing whether all the conditions are matched
	 */
	public static void openScreen(LivingEntity entity, ServerPlayer serverPlayer, Dialogue dialogue)
	{
		CompoundTag nbt = new CompoundTag();
		dialogue.responses().forEach(response -> nbt.putBoolean(response.response(), Condition.matchesAllConditions(entity, serverPlayer, response.conditions())));
		DialogueScreenPacket packet = DialogueScreenPacket.createPacket(entity, dialogue, nbt);
		MSPacketHandler.sendToPlayer(packet, serverPlayer);
	}
	
	/**
	 * A Response contains all possible Dialogues that can be reached from the present Dialogue.
	 * It contains the message that represents the Response, any Conditions/Triggers, the location of the next Dialogue, and a boolean determining whether the Response should be visible when it fails to meet the Conditions
	 */
	public record Response(String response, List<Condition> conditions, List<Trigger> triggers,
						   ResourceLocation nextDialoguePath, boolean hideIfFailed)
	{
		public static Codec<Response> CODEC = RecordCodecBuilder.create(instance ->
				instance.group(Codec.STRING.fieldOf("response").forGetter(Response::response),
								Codec.list(Condition.CODEC).fieldOf("conditions").forGetter(Response::conditions),
								Codec.list(Trigger.CODEC).fieldOf("triggers").forGetter(Response::triggers),
								ResourceLocation.CODEC.fieldOf("next_dialogue_path").forGetter(Response::nextDialoguePath),
								Codec.BOOL.fieldOf("hide_if_failed").forGetter(Response::hideIfFailed)
						)
						.apply(instance, Response::new));
	}
	
	public static class UseContext
	{
		static Codec<UseContext> CODEC = RecordCodecBuilder.create(instance ->
				instance.group(Codec.list(Condition.CODEC).fieldOf("conditions").forGetter(UseContext::getConditions))
						.apply(instance, UseContext::new));
		
		private final List<Condition> conditions;
		
		public UseContext(Condition condition)
		{
			this.conditions = List.of(condition);
		}
		
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
		
		private static List<Trigger> deserializeTriggers(JsonObject responseObject)
		{
			JsonElement triggersElement = responseObject.get("triggers");
			JsonArray triggersArray;
			if(triggersElement.isJsonArray())
			{
				triggersArray = (JsonArray) triggersElement;
			} else
			{
				//empty single-element array to keep it standardized. Dialogue fails to deserialize if the element is not an array
				triggersArray = new JsonArray();
				triggersArray.add(triggersElement);
			}
			return Trigger.LIST_CODEC.parse(JsonOps.INSTANCE, triggersArray).getOrThrow(true, LOGGER::error);
		}
		
		private static List<Condition> deserializeConditions(JsonObject responseObject)
		{
			JsonElement conditionsElement = responseObject.get("conditions");
			JsonArray conditionsArray;
			if(conditionsElement.isJsonArray())
			{
				conditionsArray = (JsonArray) conditionsElement;
			} else
			{
				//empty single-element array to keep it standardized. Dialogue fails to deserialize if the element is not an array
				conditionsArray = new JsonArray();
				conditionsArray.add(conditionsElement);
			}
			return Condition.LIST_CODEC.parse(JsonOps.INSTANCE, conditionsArray).getOrThrow(true, LOGGER::error);
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
				useContextObject.add("conditions", Condition.LIST_CODEC.encodeStart(JsonOps.INSTANCE, dialogue.useContext.conditions).getOrThrow(false, LOGGER::error));
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
				
				responseObject.add("conditions", Condition.LIST_CODEC.encodeStart(JsonOps.INSTANCE, response.conditions).getOrThrow(false, LOGGER::error));
				
				responseObject.add("triggers", Trigger.LIST_CODEC.encodeStart(JsonOps.INSTANCE, response.triggers).getOrThrow(false, LOGGER::error));
				
				responseObject.add("next_path", ResourceLocation.CODEC.encodeStart(JsonOps.INSTANCE, response.nextDialoguePath).getOrThrow(false, LOGGER::error));
				responseObject.addProperty("hide_if_failed", response.hideIfFailed);
				
				responses.add(responseObject);
			}
			return responses;
		}
	}
}