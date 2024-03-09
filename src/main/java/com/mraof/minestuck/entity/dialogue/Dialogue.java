package com.mraof.minestuck.entity.dialogue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.data.DialogueProvider;
import com.mraof.minestuck.network.DialogueScreenPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.util.PreservingOptionalFieldCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * A data driven object that contains everything which determines what shows up on the screen when the dialogue window is opened.
 */
//TODO animation is unused?
public record Dialogue(ResourceLocation path, DialogueNode node, Optional<UseContext> useContext)
{
	public static Codec<Dialogue> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ResourceLocation.CODEC.fieldOf("path").forGetter(Dialogue::path),
			DialogueNode.CODEC.fieldOf("node").forGetter(Dialogue::node),
			new PreservingOptionalFieldCodec<>(UseContext.CODEC, "use_context").forGetter(Dialogue::useContext)
	).apply(instance, Dialogue::new));
	
	/**
	 * Opens up the dialogue screen and includes a nbt object containing whether all the conditions are matched
	 */
	public static void openScreen(LivingEntity entity, ServerPlayer serverPlayer, Dialogue dialogue)
	{
		DialogueData data = dialogue.node().evaluateData(entity, serverPlayer);
		
		DialogueScreenPacket packet = DialogueScreenPacket.createPacket(entity, dialogue, data);
		MSPacketHandler.sendToPlayer(packet, serverPlayer);
	}
	
	public record DialogueNode(DialogueMessage message, String animation, ResourceLocation guiPath, List<Response> responses)
	{
		public static Codec<DialogueNode> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				DialogueMessage.CODEC.fieldOf("message").forGetter(DialogueNode::message),
				PreservingOptionalFieldCodec.withDefault(Codec.STRING, "animation", DialogueProvider.DEFAULT_ANIMATION).forGetter(DialogueNode::animation),
				PreservingOptionalFieldCodec.withDefault(ResourceLocation.CODEC, "gui", DialogueProvider.DEFAULT_GUI).forGetter(DialogueNode::guiPath),
				PreservingOptionalFieldCodec.forList(Response.LIST_CODEC, "responses").forGetter(DialogueNode::responses)
		).apply(instance, DialogueNode::new));
		
		private DialogueData evaluateData(LivingEntity entity, ServerPlayer serverPlayer)
		{
			List<ResponseData> responses = IntStream.range(0, responses().size())
					.mapToObj(responseIndex -> responses().get(responseIndex).evaluateData(responseIndex, entity, serverPlayer))
					.flatMap(Optional::stream).toList();
			
			return new DialogueData(this.message().evaluateComponent(entity, serverPlayer), this.guiPath(), responses);
		}
	}
	
	/**
	 * A Response contains all possible Dialogues that can be reached from the present Dialogue.
	 * It contains the message that represents the Response, any Conditions/Triggers, the location of the next Dialogue, and a boolean determining whether the Response should be visible when it fails to meet the Conditions
	 */
	public record Response(DialogueMessage message, Conditions conditions, List<Trigger> triggers,
						   ResourceLocation nextDialoguePath, boolean hideIfFailed)
	{
		public static Codec<Response> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				DialogueMessage.CODEC.fieldOf("message").forGetter(Response::message),
				Conditions.CODEC.fieldOf("conditions").forGetter(Response::conditions),
				PreservingOptionalFieldCodec.withDefault(Trigger.LIST_CODEC, "triggers", List.of()).forGetter(Response::triggers),
				PreservingOptionalFieldCodec.withDefault(ResourceLocation.CODEC, "next_dialogue_path", DialogueProvider.EMPTY_NEXT_PATH).forGetter(Response::nextDialoguePath),
				PreservingOptionalFieldCodec.withDefault(Codec.BOOL, "hide_if_failed", true).forGetter(Response::hideIfFailed)
		).apply(instance, Response::new));
		
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
			return Optional.of(new ResponseData(this.message().evaluateComponent(entity, serverPlayer), responseIndex, conditionFailure));
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
