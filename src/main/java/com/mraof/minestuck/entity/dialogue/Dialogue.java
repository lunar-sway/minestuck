package com.mraof.minestuck.entity.dialogue;

import com.google.gson.*;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.data.DialogueProvider;
import com.mraof.minestuck.network.DialogueScreenPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.util.PreservingOptionalFieldCodec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A data driven object that contains everything which determines what shows up on the screen when the dialogue window is opened.
 */
public record Dialogue(ResourceLocation path, DialogueMessage message, String animation, ResourceLocation guiPath,
					   List<Response> responses, UseContext useContext)
{
	private static final Logger LOGGER = LogUtils.getLogger();
	
	//TODO animation and gui_path do not currently benefit from PreservingOptionalFieldCodec
	public static Codec<Dialogue> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(ResourceLocation.CODEC.fieldOf("path").forGetter(Dialogue::path),
							DialogueMessage.CODEC.fieldOf("dialogue_message").forGetter(Dialogue::message),
							PreservingOptionalFieldCodec.withDefault(Codec.STRING, "animation", DialogueProvider.DEFAULT_ANIMATION).forGetter(Dialogue::animation),
							PreservingOptionalFieldCodec.withDefault(ResourceLocation.CODEC, "gui_path", DialogueProvider.DEFAULT_GUI).forGetter(Dialogue::guiPath),
							Response.LIST_CODEC.fieldOf("responses").forGetter(Dialogue::responses),
							UseContext.CODEC.fieldOf("use_context").forGetter(Dialogue::useContext))
					.apply(instance, Dialogue::new));
	
	public Dialogue(ResourceLocation path, DialogueMessage message, String animation, ResourceLocation guiPath, List<Response> responses, @Nullable UseContext useContext)
	{
		this.path = path;
		this.message = message;
		this.animation = animation;
		this.guiPath = guiPath;
		this.responses = responses;
		this.useContext = useContext;
	}
	
	/**
	 * Opens up the dialogue screen and includes a nbt object containing whether all the conditions are matched
	 */
	public static void openScreen(LivingEntity entity, ServerPlayer serverPlayer, Dialogue dialogue)
	{
		CompoundTag dialogueData = writeAllMessagesToCompound(entity, serverPlayer, dialogue);
		
		DialogueScreenPacket packet = DialogueScreenPacket.createPacket(entity, dialogue, dialogueData);
		MSPacketHandler.sendToPlayer(packet, serverPlayer);
	}
	
	/**
	 * A Response contains all possible Dialogues that can be reached from the present Dialogue.
	 * It contains the message that represents the Response, any Conditions/Triggers, the location of the next Dialogue, and a boolean determining whether the Response should be visible when it fails to meet the Conditions
	 */
	public record Response(DialogueMessage response, Conditions conditions, List<Trigger> triggers,
						   ResourceLocation nextDialoguePath, boolean hideIfFailed)
	{
		public static Codec<Response> CODEC = RecordCodecBuilder.create(instance ->
				instance.group(DialogueMessage.CODEC.fieldOf("response_message").forGetter(Response::response),
								Conditions.CODEC.fieldOf("conditions").forGetter(Response::conditions),
								PreservingOptionalFieldCodec.withDefault(Trigger.LIST_CODEC, "triggers", List.of()).forGetter(Response::triggers),
								PreservingOptionalFieldCodec.withDefault(ResourceLocation.CODEC, "next_dialogue_path", DialogueProvider.EMPTY_NEXT_PATH).forGetter(Response::nextDialoguePath),
								PreservingOptionalFieldCodec.withDefault(Codec.BOOL, "hide_if_failed", true).forGetter(Response::hideIfFailed)
						)
						.apply(instance, Response::new));
		
		static Codec<List<Response>> LIST_CODEC = Response.CODEC.listOf();
	}
	
	public static class UseContext
	{
		static Codec<UseContext> CODEC = RecordCodecBuilder.create(instance ->
				instance.group(Conditions.CODEC.fieldOf("conditions").forGetter(UseContext::getConditions),
								PreservingOptionalFieldCodec.withDefault(Codec.INT, "dialogue_weight", 10).forGetter(UseContext::getWeight))
						.apply(instance, UseContext::new));
		
		private final Conditions conditions;
		private final int weight;
		
		public UseContext(Condition condition)
		{
			this(new Conditions(List.of(condition), Conditions.Type.ALL), 10);
		}
		
		public UseContext(Condition condition, int weight)
		{
			this(new Conditions(List.of(condition), Conditions.Type.ALL), weight);
		}
		
		public UseContext(Conditions conditions)
		{
			this(conditions, 10);
		}
		
		public UseContext(Conditions conditions, int weight)
		{
			this.conditions = conditions;
			this.weight = weight;
		}
		
		public Conditions getConditions()
		{
			return conditions;
		}
		
		public int getWeight()
		{
			return weight;
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
			DialogueMessage messageProvider = DialogueMessage.CODEC.parse(JsonOps.INSTANCE, json.get("dialogue_message")).getOrThrow(false, LOGGER::error);
			String animationProvider = Codec.STRING.parse(JsonOps.INSTANCE, json.get("animation")).getOrThrow(false, LOGGER::error);
			ResourceLocation guiProvider = ResourceLocation.CODEC.parse(JsonOps.INSTANCE, json.get("gui")).getOrThrow(false, LOGGER::error);
			
			UseContext useContext = null;
			JsonObject useContextObject = json.getAsJsonObject("use_context");
			if(useContextObject != null)
			{
				useContext = UseContext.CODEC.parse(JsonOps.INSTANCE, useContextObject).getOrThrow(true, LOGGER::error);
			}
			
			List<Response> responsesProvider = Response.LIST_CODEC.parse(JsonOps.INSTANCE, json.getAsJsonArray("responses")).getOrThrow(true, LOGGER::error);
			
			return new Dialogue(pathProvider, messageProvider, animationProvider, guiProvider, responsesProvider, useContext);
		}
		
		@Override
		public JsonElement serialize(Dialogue dialogue, Type type, JsonSerializationContext context)
		{
			JsonObject json = new JsonObject();
			
			json.add("path", ResourceLocation.CODEC.encodeStart(JsonOps.INSTANCE, dialogue.path).getOrThrow(false, LOGGER::error));
			json.add("dialogue_message", DialogueMessage.CODEC.encodeStart(JsonOps.INSTANCE, dialogue.message).getOrThrow(false, LOGGER::error));
			json.add("animation", Codec.STRING.encodeStart(JsonOps.INSTANCE, dialogue.animation).getOrThrow(false, LOGGER::error));
			json.add("gui", ResourceLocation.CODEC.encodeStart(JsonOps.INSTANCE, dialogue.guiPath).getOrThrow(false, LOGGER::error));
			
			json.add("responses", Response.LIST_CODEC.encodeStart(JsonOps.INSTANCE, dialogue.responses).getOrThrow(false, LOGGER::error));
			
			if(dialogue.useContext != null)
			{
				json.add("use_context", UseContext.CODEC.encodeStart(JsonOps.INSTANCE, dialogue.useContext).getOrThrow(false, LOGGER::error));
			}
			
			return json;
		}
	}
	
	public static CompoundTag writeAllMessagesToCompound(LivingEntity entity, ServerPlayer serverPlayer, Dialogue dialogue)
	{
		CompoundTag messageArgs = new CompoundTag();
		
		ListTag dialogueArgs = new ListTag();
		dialogue.message().processArguments(entity, serverPlayer).forEach(argument -> dialogueArgs.add(StringTag.valueOf(argument)));
		messageArgs.put("dialogue_message", dialogueArgs);
		
		CompoundTag responsesArgs = new CompoundTag();
		dialogue.responses().forEach(response -> {
			Optional<ConditionFailure> conditionFailure;
			if(response.conditions().testWithContext(entity, serverPlayer))
				conditionFailure = Optional.empty();
			else
			{
				List<String> failureMessages = response.conditions().conditionList().stream()
						.map(Condition::getFailureTooltip).filter(message -> !message.isEmpty()).toList();
				
				conditionFailure = Optional.of(new ConditionFailure(failureMessages));
			}
			ResponseData responseData = new ResponseData(response.response().processArguments(entity, serverPlayer),
					dialogue.responses.indexOf(response), conditionFailure);
			
			responsesArgs.put(response.response().message(), responseData.write());
		});
		messageArgs.put("responses", responsesArgs);
		
		return messageArgs;
	}
	
	public record DialogueData(List<String> messageArguments, Map<String, ResponseData> responsesMap)
	{
		public static DialogueData read(CompoundTag messageArgs)
		{
			return new DialogueData(readDialogueArguments(messageArgs), readResponsesMap(messageArgs));
		}
		
		private static Map<String, ResponseData> readResponsesMap(CompoundTag messageArgs)
		{
			CompoundTag responses = messageArgs.getCompound("responses");
			Map<String, ResponseData> responseMap = new HashMap<>();
			responses.getAllKeys().forEach(key -> responseMap.put(key, ResponseData.read(responses.getCompound(key))));
			return responseMap;
		}
		
		private static List<String> readDialogueArguments(CompoundTag messageArgs)
		{
			return messageArgs.getList("dialogue_message", Tag.TAG_STRING)
					.stream().map(StringTag.class::cast).map(StringTag::getAsString).toList();
		}
	}
	
	public record ResponseData(List<String> arguments, int index, Optional<ConditionFailure> conditionFailure)
	{
		private static ResponseData read(CompoundTag tag)
		{
			List<String> arguments = tag.getList("arguments", Tag.TAG_STRING)
					.stream().map(StringTag.class::cast).map(StringTag::getAsString).toList();
			int index = tag.getInt("index");
			Optional<ConditionFailure> conditionFailure = tag.contains("failure", Tag.TAG_COMPOUND)
					? Optional.of(ConditionFailure.read(tag.getCompound("failure"))) : Optional.empty();
			
			return new ResponseData(arguments, index, conditionFailure);
		}
		
		private CompoundTag write()
		{
			CompoundTag tag = new CompoundTag();
			ListTag argumentsTag = new ListTag();
			arguments.forEach(argument -> argumentsTag.add(StringTag.valueOf(argument)));
			tag.put("arguments", argumentsTag);
			tag.putInt("index", this.index);
			this.conditionFailure.ifPresent(failure -> tag.put("failure", failure.write()));
			return tag;
		}
	}
	
	public record ConditionFailure(List<String> causes)
	{
		private static ConditionFailure read(CompoundTag tag)
		{
			List<String> causes = tag.getList("causes", Tag.TAG_STRING)
					.stream().map(StringTag.class::cast).map(StringTag::getAsString).toList();
			
			return new ConditionFailure(causes);
		}
		
		private CompoundTag write()
		{
			CompoundTag tag = new CompoundTag();
			ListTag causesTag = new ListTag();
			this.causes.forEach(cause -> causesTag.add(StringTag.valueOf(cause)));
			tag.put("causes", causesTag);
			return tag;
		}
	}
}