package com.mraof.minestuck.entity.dialogue;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.dialogue.condition.Condition;
import com.mraof.minestuck.util.PreservingOptionalFieldCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * A data driven object that contains everything which determines what shows up on the screen when the dialogue window is opened.
 */
public final class Dialogue
{
	public static final String DEFAULT_ANIMATION = "generic_animation";
	public static final ResourceLocation DEFAULT_GUI = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/generic_extra_large.png");
	
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
		public static NodeReference read(FriendlyByteBuf buffer)
		{
			ResourceLocation dialoguePath = buffer.readResourceLocation();
			int nodeIndex = buffer.readInt();
			
			return new NodeReference(dialoguePath, nodeIndex);
		}
		
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeResourceLocation(this.dialoguePath);
			buffer.writeInt(this.nodeIndex);
		}
	}
	
	//TODO animation is unused?
	public record Node(DialogueMessage message, Optional<DialogueMessage> description, String animation, ResourceLocation guiPath, List<Response> responses)
	{
		public static Codec<Node> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				DialogueMessage.CODEC.fieldOf("message").forGetter(Node::message),
				new PreservingOptionalFieldCodec<>(DialogueMessage.CODEC, "description").forGetter(Node::description),
				PreservingOptionalFieldCodec.withDefault(Codec.STRING, "animation", DEFAULT_ANIMATION).forGetter(Node::animation),
				PreservingOptionalFieldCodec.withDefault(ResourceLocation.CODEC, "gui", DEFAULT_GUI).forGetter(Node::guiPath),
				PreservingOptionalFieldCodec.forList(Response.LIST_CODEC, "responses").forGetter(Node::responses)
		).apply(instance, Node::new));
		
		DialogueData evaluateData(LivingEntity entity, ServerPlayer player)
		{
			List<ResponseData> responses = IntStream.range(0, responses().size())
					.mapToObj(responseIndex -> responses().get(responseIndex).evaluateData(responseIndex, entity, player))
					.flatMap(Optional::stream).toList();
			
			MutableComponent message = this.message().evaluateComponent(entity, player);
			this.description().ifPresent(descriptionMessage ->
					message.append("\n")
							.append(descriptionMessage.evaluateComponent(entity, player)
									.withStyle(style -> style.withItalic(true).applyFormat(ChatFormatting.GRAY))));
			
			return new DialogueData(message, this.guiPath(), responses);
		}
		
		public Optional<Response> getResponseIfValid(int responseIndex)
		{
			if(responseIndex < 0 || this.responses().size() <= responseIndex)
				return Optional.empty();
			
			return Optional.of(this.responses().get(responseIndex));
		}
		
		public void visitConnectedDialogue(Consumer<ResourceLocation> idConsumer)
		{
			responses.forEach(response -> response.nextDialoguePath().ifPresent(idConsumer));
		}
	}
	
	/**
	 * A Response contains all possible Dialogues that can be reached from the present Dialogue.
	 * It contains the message that represents the Response, any Conditions/Triggers, the location of the next Dialogue, and a boolean determining whether the Response should be visible when it fails to meet the Conditions
	 */
	public record Response(DialogueMessage message, List<Trigger> triggers, Optional<ResourceLocation> nextDialoguePath,
						   Condition condition, boolean hideIfFailed, Optional<String> failTooltipKey)
	{
		public static Codec<Response> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				DialogueMessage.CODEC.fieldOf("message").forGetter(Response::message),
				PreservingOptionalFieldCodec.withDefault(Trigger.LIST_CODEC, "triggers", List.of()).forGetter(Response::triggers),
				new PreservingOptionalFieldCodec<>(ResourceLocation.CODEC, "next_dialogue_path").forGetter(Response::nextDialoguePath),
				PreservingOptionalFieldCodec.withDefault(Condition.CODEC, "condition", Condition.AlwaysTrue.INSTANCE).forGetter(Response::condition),
				PreservingOptionalFieldCodec.withDefault(Codec.BOOL, "hide_if_failed", true).forGetter(Response::hideIfFailed),
				new PreservingOptionalFieldCodec<>(Codec.STRING, "fail_tooltip").forGetter(Response::failTooltipKey)
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
			return Optional.of(new ResponseData(this.message.evaluateComponent(entity, serverPlayer), this.nextDialoguePath.isEmpty(), responseIndex, conditionFailure));
		}
		
		public void trigger(LivingEntity entity, ServerPlayer player)
		{
			if(!this.condition().test(entity, player))
				return;
			
			for(Trigger trigger : this.triggers())
				trigger.triggerEffect(entity, player);
			
			Optional<ResourceLocation> nextPath = this.nextDialoguePath();
			
			if(nextPath.isEmpty())
				return;
			
			((DialogueEntity) entity).getDialogueComponent().tryOpenScreenForDialogue(player, nextPath.get());
		}
	}
	
	public record SelectableDialogue(ResourceLocation dialogueId, Condition condition, int weight, boolean keepOnReset)
	{
		public static final int DEFAULT_WEIGHT = 10;
		public static Codec<SelectableDialogue> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ResourceLocation.CODEC.fieldOf("dialogue").forGetter(SelectableDialogue::dialogueId),
				Condition.NPC_ONLY_CODEC.fieldOf("condition").forGetter(SelectableDialogue::condition),
				PreservingOptionalFieldCodec.withDefault(Codec.INT, "dialogue_weight", DEFAULT_WEIGHT).forGetter(SelectableDialogue::weight),
				PreservingOptionalFieldCodec.withDefault(Codec.BOOL, "keep_on_reset", false).forGetter(SelectableDialogue::keepOnReset)
		).apply(instance, SelectableDialogue::new));
	}
	
	//TODO this helper function does not belong here
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
	
	public record ResponseData(Component message, boolean shouldClose, int index, Optional<ConditionFailure> conditionFailure)
	{
		private static ResponseData read(FriendlyByteBuf buffer)
		{
			Component message = buffer.readComponent();
			boolean shouldClose = buffer.readBoolean();
			int index = buffer.readInt();
			Optional<ConditionFailure> conditionFailure = buffer.readOptional(ConditionFailure::read);
			
			return new ResponseData(message, shouldClose, index, conditionFailure);
		}
		
		private void write(FriendlyByteBuf buffer)
		{
			buffer.writeComponent(this.message);
			buffer.writeBoolean(this.shouldClose);
			buffer.writeInt(this.index);
			buffer.writeOptional(this.conditionFailure, (byteBuf, failure) -> failure.write(byteBuf));
		}
	}
	
	public record ConditionFailure(Component causes)
	{
		private static ConditionFailure read(FriendlyByteBuf buffer)
		{
			return new ConditionFailure(buffer.readComponent());
		}
		
		private void write(FriendlyByteBuf buffer)
		{
			buffer.writeComponent(this.causes);
		}
	}
}
