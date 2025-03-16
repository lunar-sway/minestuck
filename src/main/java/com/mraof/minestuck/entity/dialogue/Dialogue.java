package com.mraof.minestuck.entity.dialogue;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.dialogue.condition.Condition;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A data driven object that contains everything which determines what shows up on the screen when the dialogue window is opened.
 */
public final class Dialogue
{
	public static final String DIALOGUE_FORMAT = "minestuck.dialogue.format";
	public static final ResourceLocation DEFAULT_GUI = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "textures/gui/dialogue/dialogue.png");
	
	public record NodeSelector(List<Pair<Condition, Node>> conditionedNodes, Node defaultNode)
	{
		private static final Codec<Pair<Condition, Node>> ENTRY_CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Condition.CODEC.fieldOf("condition").forGetter(Pair::getFirst),
				Node.CODEC.fieldOf("node").forGetter(Pair::getSecond)
		).apply(instance, Pair::of));
		private static final Codec<NodeSelector> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ENTRY_CODEC.listOf().fieldOf("conditioned_nodes").forGetter(NodeSelector::conditionedNodes),
				Node.CODEC.fieldOf("default_node").forGetter(NodeSelector::defaultNode)
		).apply(instance, NodeSelector::new));
		public static final Codec<NodeSelector> CODEC = Codec.either(NodeSelector.DIRECT_CODEC, Node.CODEC.fieldOf("node").codec())
				.xmap(either -> either.map(Function.identity(), node -> new NodeSelector(List.of(), node)),
						nodeSelector -> nodeSelector.conditionedNodes.isEmpty() ? Either.right(nodeSelector.defaultNode) : Either.left(nodeSelector));
		
		
		public Pair<Node, Integer> pickNode(LivingEntity entity, ServerPlayer player)
		{
			for(int i = 0; i < this.conditionedNodes.size(); i++)
			{
				var pair = this.conditionedNodes.get(i);
				if(pair.getFirst().test(entity, player))
				{
					Node node = pair.getSecond();
					return Pair.of(node, i);
				}
			}
			return Pair.of(this.defaultNode, -1);
		}
		
		public Optional<Node> getNodeIfValid(int index, LivingEntity entity, ServerPlayer player)
		{
			var pair = this.pickNode(entity, player);
			if(pair.getSecond() != index)
				return Optional.empty();
			
			return Optional.of(pair.getFirst());
		}
		
		public void visitConnectedDialogue(Consumer<ResourceLocation> idConsumer)
		{
			this.conditionedNodes.forEach(pair -> pair.getSecond().visitConnectedDialogue(idConsumer));
			this.defaultNode.visitConnectedDialogue(idConsumer);
		}
	}
	
	public record NodeReference(ResourceLocation dialoguePath, int nodeIndex)
	{
	}
	
	public record Node(List<Pair<MessageType, DialogueMessage>> messages, DialogueAnimationData animation, ResourceLocation guiPath, List<Response> responses)
	{
		private static final Codec<Pair<MessageType, DialogueMessage>> MESSAGE_CODEC = Codec.mapPair(MessageType.CODEC.fieldOf("type"), DialogueMessage.CODEC.fieldOf("message")).codec();
		private static final MapCodec<List<Pair<MessageType, DialogueMessage>>> MESSAGES_MAP_CODEC = Codec.mapEither(DialogueMessage.CODEC.fieldOf("message"), MESSAGE_CODEC.listOf().fieldOf("messages"))
				.xmap(either -> either.map(message -> List.of(Pair.of(MessageType.ENTITY, message)), Function.identity()),
						messages -> messages.size() == 1 && messages.get(0).getFirst() == MessageType.ENTITY ? Either.left(messages.get(0).getSecond()): Either.right(messages));
		public static final Codec<Node> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				MESSAGES_MAP_CODEC.forGetter(Node::messages),
				DialogueAnimationData.CODEC.fieldOf("animation").orElse(DialogueAnimationData.DEFAULT_ANIMATION).forGetter(Node::animation),
				ResourceLocation.CODEC.fieldOf("gui").orElse(DEFAULT_GUI).forGetter(Node::guiPath),
				Response.LIST_CODEC.fieldOf("responses").orElse(Collections.emptyList()).forGetter(Node::responses)
		).apply(instance, Node::new));
		
		DialogueData evaluateData(LivingEntity entity, ServerPlayer player, @Nullable Dialogue.NextDialogue source)
		{
			List<ResponseData> responses = IntStream.range(0, responses().size())
					.mapToObj(responseIndex -> responses().get(responseIndex).evaluateData(responseIndex, entity, player))
					.flatMap(Optional::stream).toList();
			
			List<Component> lines = Stream.concat(
					source != null ? source.replyMessages.stream() : Stream.empty(),
					this.messages.stream()
			).map(messagePair -> this.buildMessage(messagePair, entity, player)).toList();
			
			return new DialogueData(lines, this.guiPath(), responses, this.animation, ((DialogueEntity) entity).getSpriteType());
		}
		
		private Component buildMessage(Pair<MessageType, DialogueMessage> messagePair, LivingEntity entity, ServerPlayer player)
		{
			MutableComponent messageComponent = messagePair.getSecond().evaluateComponent(entity, player);
			return switch(messagePair.getFirst())
			{
				case ENTITY ->
				{
					MutableComponent component = Component.translatable(DIALOGUE_FORMAT,
							entity.getDisplayName(), messageComponent);
					
					if(entity instanceof DialogueEntity dialogueEntity)
						component.withStyle(dialogueEntity.getChatColor());
					
					yield component;
				}
				case PLAYER -> Component.translatable(DIALOGUE_FORMAT, player.getDisplayName(), messageComponent);
				case DESCRIPTION ->
						messageComponent.withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
			};
		}
		
		public Optional<Response> getResponseIfValid(int responseIndex)
		{
			if(responseIndex < 0 || this.responses().size() <= responseIndex)
				return Optional.empty();
			
			return Optional.of(this.responses().get(responseIndex));
		}
		
		public void visitConnectedDialogue(Consumer<ResourceLocation> idConsumer)
		{
			responses.forEach(response -> response.nextDialogue.ifPresent(nextDialogue -> idConsumer.accept(nextDialogue.id)));
		}
	}
	
	@MethodsReturnNonnullByDefault
	public enum MessageType implements StringRepresentable
	{
		ENTITY,
		PLAYER,
		DESCRIPTION;
		
		static final Codec<MessageType> CODEC = StringRepresentable.fromEnum(MessageType::values);
		
		@Override
		public String getSerializedName()
		{
			return this.name().toLowerCase(Locale.ROOT);
		}
	}
	
	/**
	 * A Response contains all possible Dialogues that can be reached from the present Dialogue.
	 * It contains the message that represents the Response, any Conditions/Triggers, the location of the next Dialogue, and a boolean determining whether the Response should be visible when it fails to meet the Conditions
	 */
	public record Response(DialogueMessage message, List<Trigger> triggers, Optional<NextDialogue> nextDialogue,
						   Condition condition, boolean hideIfFailed, Optional<String> failTooltipKey)
	{
		public static Codec<Response> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				DialogueMessage.CODEC.fieldOf("message").forGetter(Response::message),
				Trigger.LIST_CODEC.fieldOf("triggers").orElse(List.of()).forGetter(Response::triggers),
				NextDialogue.EITHER_CODEC.optionalFieldOf("next_dialogue").forGetter(Response::nextDialogue),
				Condition.CODEC.fieldOf("condition").orElse(Condition.AlwaysTrue.INSTANCE).forGetter(Response::condition),
				Codec.BOOL.fieldOf("hide_if_failed").orElse(true).forGetter(Response::hideIfFailed),
				Codec.STRING.optionalFieldOf("fail_tooltip").forGetter(Response::failTooltipKey)
		).apply(instance, Response::new));
		
		static Codec<List<Response>> LIST_CODEC = Response.CODEC.listOf();
		
		public Optional<ResponseData> evaluateData(int responseIndex, LivingEntity entity, ServerPlayer serverPlayer)
		{
			Optional<ConditionFailure> conditionFailure;
			if(this.condition.test(entity, serverPlayer))
				conditionFailure = Optional.empty();
			else
			{
				if(this.hideIfFailed)
					return Optional.empty();
				
				Component failureMessages = this.failTooltipKey.<Component>map(Component::translatable)
						.orElseGet(this.condition::getFailureTooltip);
				
				conditionFailure = Optional.of(new ConditionFailure(failureMessages));
			}
			return Optional.of(new ResponseData(this.message.evaluateComponent(entity, serverPlayer), this.nextDialogue.isEmpty(), responseIndex, conditionFailure));
		}
		
		public void trigger(DialogueComponent component, ServerPlayer player)
		{
			if(!this.condition().test(component.entity(), player))
				return;
			
			for(Trigger trigger : this.triggers())
				trigger.triggerEffect(component.entity(), player);
			
			this.nextDialogue().ifPresent(nextDialogue -> nextDialogue.apply(component, player));
		}
	}
	
	public record NextDialogue(ResourceLocation id, boolean setAsEntrypoint, List<Pair<MessageType, DialogueMessage>> replyMessages)
	{
		private static final MapCodec<List<Pair<MessageType, DialogueMessage>>> MESSAGES_MAP_CODEC = Codec.mapEither(DialogueMessage.CODEC.fieldOf("player_message"), Node.MESSAGE_CODEC.listOf().fieldOf("reply_messages"))
				.xmap(either -> either.map(message -> List.of(Pair.of(MessageType.PLAYER, message)), Function.identity()),
						messages -> messages.size() == 1 && messages.get(0).getFirst() == MessageType.PLAYER ? Either.left(messages.get(0).getSecond()): Either.right(messages));
		public static final Codec<NextDialogue> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ResourceLocation.CODEC.fieldOf("id").forGetter(NextDialogue::id),
				Codec.BOOL.fieldOf("set_as_entrypoint").forGetter(NextDialogue::setAsEntrypoint),
				MESSAGES_MAP_CODEC.forGetter(NextDialogue::replyMessages)
		).apply(instance, NextDialogue::new));
		public static final Codec<NextDialogue> EITHER_CODEC = Codec.either(ResourceLocation.CODEC, DIRECT_CODEC)
				.xmap(either -> either.map(id -> new NextDialogue(id, false, List.of()), Function.identity()),
						nextDialogue -> !nextDialogue.setAsEntrypoint && nextDialogue.replyMessages.isEmpty() ? Either.left(nextDialogue.id) : Either.right(nextDialogue));
		
		public void apply(DialogueComponent component, ServerPlayer player)
		{
			if(this.setAsEntrypoint)
				component.setDialogueForPlayer(player, this.id);
			component.tryOpenScreenForDialogue(player, this.id, this);
		}
	}
	
	public record SelectableDialogue(ResourceLocation dialogueId, Condition condition, int weight, boolean keepOnReset)
	{
		public static final int DEFAULT_WEIGHT = 10;
		public static Codec<SelectableDialogue> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ResourceLocation.CODEC.fieldOf("dialogue").forGetter(SelectableDialogue::dialogueId),
				Condition.NPC_ONLY_CODEC.fieldOf("condition").forGetter(SelectableDialogue::condition),
				Codec.INT.fieldOf("dialogue_weight").orElse(DEFAULT_WEIGHT).forGetter(SelectableDialogue::weight),
				Codec.BOOL.fieldOf("keep_on_reset").orElse(false).forGetter(SelectableDialogue::keepOnReset)
		).apply(instance, SelectableDialogue::new));
	}
	
	public record DialogueData(List<Component> messages, ResourceLocation guiBackground, List<ResponseData> responses, DialogueAnimationData animationData, String spriteType)
	{
		public static StreamCodec<RegistryFriendlyByteBuf, DialogueData> STREAM_CODEC = StreamCodec.composite(
				ComponentSerialization.STREAM_CODEC.apply(ByteBufCodecs.list()),
				DialogueData::messages,
				ResourceLocation.STREAM_CODEC,
				DialogueData::guiBackground,
				ResponseData.STREAM_CODEC.apply(ByteBufCodecs.list()),
				DialogueData::responses,
				DialogueAnimationData.STREAM_CODEC,
				DialogueData::animationData,
				ByteBufCodecs.STRING_UTF8,
				DialogueData::spriteType,
				DialogueData::new
		);
	}
	
	public record ResponseData(Component message, boolean shouldClose, int index, Optional<ConditionFailure> conditionFailure)
	{
		public static final StreamCodec<RegistryFriendlyByteBuf, ResponseData> STREAM_CODEC = StreamCodec.composite(
			ComponentSerialization.STREAM_CODEC,
			ResponseData::message,
			ByteBufCodecs.BOOL,
			ResponseData::shouldClose,
			ByteBufCodecs.INT,
			ResponseData::index,
			ByteBufCodecs.optional(ConditionFailure.STREAM_CODEC),
			ResponseData::conditionFailure,
			ResponseData::new
		);
		
	}
	
	public record ConditionFailure(Component causes)
	{
		public static final StreamCodec<RegistryFriendlyByteBuf, ConditionFailure> STREAM_CODEC = StreamCodec.composite(
			ComponentSerialization.STREAM_CODEC,
			ConditionFailure::causes,
			ConditionFailure::new
		);
	}
}
