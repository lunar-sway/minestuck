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
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * A data driven object that contains everything which determines what shows up on the screen when the dialogue window is opened.
 */
public record Dialogue(ResourceLocation path, DialogueMessage message, String animation, ResourceLocation guiPath,
					   List<Response> responses, @Nullable UseContext useContext)
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
	
	/**
	 * Opens up the dialogue screen and includes a nbt object containing whether all the conditions are matched
	 */
	public static void openScreen(LivingEntity entity, ServerPlayer serverPlayer, Dialogue dialogue)
	{
		DialogueData data = dialogue.evaluateData(entity, serverPlayer);
		
		DialogueScreenPacket packet = DialogueScreenPacket.createPacket(entity, dialogue, data);
		MSPacketHandler.sendToPlayer(packet, serverPlayer);
	}
	
	private DialogueData evaluateData(LivingEntity entity, ServerPlayer serverPlayer)
	{
		List<ResponseData> responses = IntStream.range(0, responses().size())
				.mapToObj(responseIndex -> responses().get(responseIndex).evaluateData(responseIndex, entity, serverPlayer))
				.flatMap(Optional::stream).toList();
		
		return new DialogueData(this.message().evaluateComponent(entity, serverPlayer), this.guiPath(), responses);
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
		
		public Optional<ResponseData> evaluateData(int responseIndex, LivingEntity entity, ServerPlayer serverPlayer)
		{
			Optional<ConditionFailure> conditionFailure;
			if(this.conditions().testWithContext(entity, serverPlayer))
				conditionFailure = Optional.empty();
			else
			{
				if(this.hideIfFailed())
					return Optional.empty();
				
				List<String> failureMessages = this.conditions().conditionList().stream()
						.map(Condition::getFailureTooltip).filter(message -> !message.isEmpty()).toList();
				
				conditionFailure = Optional.of(new ConditionFailure(failureMessages));
			}
			return Optional.of(new ResponseData(this.response().evaluateComponent(entity, serverPlayer), responseIndex, conditionFailure));
		}
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
	
	public record DialogueData(Component message, ResourceLocation guiBackground, List<ResponseData> responses)
	{
		public static DialogueData read(FriendlyByteBuf buffer)
		{
			Component message = buffer.readComponent();
			ResourceLocation guiBackground = buffer.readResourceLocation();
			List<ResponseData> responses = buffer.readList(ResponseData::read);
			
			return new DialogueData(message, guiBackground, responses);
		}
		
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeComponent(this.message);
			buffer.writeResourceLocation(this.guiBackground);
			buffer.writeCollection(this.responses, (byteBuf, responseData) -> responseData.write(byteBuf));
		}
	}
	
	public record ResponseData(Component message, int index, Optional<ConditionFailure> conditionFailure)
	{
		private static ResponseData read(FriendlyByteBuf buffer)
		{
			Component message = buffer.readComponent();
			int index = buffer.readInt();
			Optional<ConditionFailure> conditionFailure = buffer.readOptional(ConditionFailure::read);
			
			return new ResponseData(message, index, conditionFailure);
		}
		
		private void write(FriendlyByteBuf buffer)
		{
			buffer.writeComponent(this.message);
			buffer.writeInt(this.index);
			buffer.writeOptional(this.conditionFailure, (byteBuf, failure) -> failure.write(byteBuf));
		}
	}
	
	public record ConditionFailure(List<String> causes)
	{
		private static ConditionFailure read(FriendlyByteBuf buffer)
		{
			List<String> causes = buffer.readList(FriendlyByteBuf::readUtf);
			
			return new ConditionFailure(causes);
		}
		
		private void write(FriendlyByteBuf buffer)
		{
			buffer.writeCollection(this.causes, FriendlyByteBuf::writeUtf);
		}
	}
}
