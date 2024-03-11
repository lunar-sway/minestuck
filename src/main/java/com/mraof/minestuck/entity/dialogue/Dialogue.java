package com.mraof.minestuck.entity.dialogue;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.dialogue.condition.Condition;
import com.mraof.minestuck.network.DialogueScreenPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.util.DialogueManager;
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
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * A data driven object that contains everything which determines what shows up on the screen when the dialogue window is opened.
 */
//TODO animation is unused?
public record Dialogue(NodeSelector nodes, Optional<RandomlySelectable> selectable)
{
	public static final String DEFAULT_ANIMATION = "generic_animation";
	public static final ResourceLocation DEFAULT_GUI = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/generic_extra_large.png");
	
	public static Codec<Dialogue> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			NodeSelector.EITHER_MAP_CODEC.forGetter(Dialogue::nodes),
			new PreservingOptionalFieldCodec<>(RandomlySelectable.CODEC, "selectable").forGetter(Dialogue::selectable)
	).apply(instance, Dialogue::new));
	
	public ResourceLocation lookupId()
	{
		return DialogueManager.getInstance().getId(this);
	}
	
	/**
	 * Opens up the dialogue screen and includes a nbt object containing whether all the conditions are matched
	 */
	public static void openScreen(LivingEntity entity, ServerPlayer serverPlayer, Dialogue dialogue)
	{
		Pair<DialogueNode, Integer> node = dialogue.nodes().pickNode(entity, serverPlayer);
		DialogueData data = node.getFirst().evaluateData(entity, serverPlayer);
		NodeReference nodeReference = new NodeReference(dialogue.lookupId(), node.getSecond());
		
		DialogueScreenPacket packet = new DialogueScreenPacket(entity.getId(), nodeReference, data);
		MSPacketHandler.sendToPlayer(packet, serverPlayer);
	}
	
	public record NodeSelector(List<Pair<Condition, DialogueNode>> conditionedNodes, DialogueNode defaultNode)
	{
		public static final Codec<Pair<Condition, DialogueNode>> ENTRY_CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Condition.CODEC.fieldOf("condition").forGetter(Pair::getFirst),
				DialogueNode.CODEC.fieldOf("node").forGetter(Pair::getSecond)
		).apply(instance, Pair::of));
		public static final Codec<NodeSelector> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ENTRY_CODEC.listOf().fieldOf("conditioned_nodes").forGetter(NodeSelector::conditionedNodes),
				DialogueNode.CODEC.fieldOf("default_node").forGetter(NodeSelector::defaultNode)
		).apply(instance, NodeSelector::new));
		public static final MapCodec<NodeSelector> EITHER_MAP_CODEC = Codec.mapEither(NodeSelector.CODEC.fieldOf("nodes"), DialogueNode.CODEC.fieldOf("node"))
				.xmap(either -> either.map(Function.identity(), node -> new NodeSelector(List.of(), node)),
						nodeSelector -> nodeSelector.conditionedNodes.isEmpty() ? Either.right(nodeSelector.defaultNode) : Either.left(nodeSelector));
		
		public Pair<DialogueNode, Integer> pickNode(LivingEntity entity, ServerPlayer player)
		{
			for(int i = 0; i < this.conditionedNodes.size(); i++)
			{
				var pair = this.conditionedNodes.get(i);
				if(pair.getFirst().test(entity, player))
				{
					DialogueNode node = pair.getSecond();
					return Pair.of(node, i);
				}
			}
			return Pair.of(this.defaultNode, -1);
		}
		
		public Optional<DialogueNode> getNodeIfValid(int index, LivingEntity entity, ServerPlayer player)
		{
			var pair = this.pickNode(entity, player);
			if(pair.getSecond() != index)
				return Optional.empty();
			
			return Optional.of(pair.getFirst());
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
	
	public record DialogueNode(DialogueMessage message, String animation, ResourceLocation guiPath, List<Response> responses)
	{
		public static Codec<DialogueNode> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				DialogueMessage.CODEC.fieldOf("message").forGetter(DialogueNode::message),
				PreservingOptionalFieldCodec.withDefault(Codec.STRING, "animation", DEFAULT_ANIMATION).forGetter(DialogueNode::animation),
				PreservingOptionalFieldCodec.withDefault(ResourceLocation.CODEC, "gui", DEFAULT_GUI).forGetter(DialogueNode::guiPath),
				PreservingOptionalFieldCodec.forList(Response.LIST_CODEC, "responses").forGetter(DialogueNode::responses)
		).apply(instance, DialogueNode::new));
		
		private DialogueData evaluateData(LivingEntity entity, ServerPlayer serverPlayer)
		{
			List<ResponseData> responses = IntStream.range(0, responses().size())
					.mapToObj(responseIndex -> responses().get(responseIndex).evaluateData(responseIndex, entity, serverPlayer))
					.flatMap(Optional::stream).toList();
			
			return new DialogueData(this.message().evaluateComponent(entity, serverPlayer), this.guiPath(), responses);
		}
		
		public Optional<Response> getResponseIfValid(int responseIndex)
		{
			if(responseIndex < 0 || this.responses().size() <= responseIndex)
				return Optional.empty();
			
			return Optional.of(this.responses().get(responseIndex));
		}
	}
	
	/**
	 * A Response contains all possible Dialogues that can be reached from the present Dialogue.
	 * It contains the message that represents the Response, any Conditions/Triggers, the location of the next Dialogue, and a boolean determining whether the Response should be visible when it fails to meet the Conditions
	 */
	public record Response(DialogueMessage message, Condition condition, List<Trigger> triggers,
						   Optional<ResourceLocation> nextDialoguePath, boolean hideIfFailed)
	{
		public static Codec<Response> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				DialogueMessage.CODEC.fieldOf("message").forGetter(Response::message),
				PreservingOptionalFieldCodec.withDefault(Condition.CODEC, "condition", Condition.AlwaysTrue.INSTANCE).forGetter(Response::condition),
				PreservingOptionalFieldCodec.withDefault(Trigger.LIST_CODEC, "triggers", List.of()).forGetter(Response::triggers),
				new PreservingOptionalFieldCodec<>(ResourceLocation.CODEC, "next_dialogue_path").forGetter(Response::nextDialoguePath),
				PreservingOptionalFieldCodec.withDefault(Codec.BOOL, "hide_if_failed", true).forGetter(Response::hideIfFailed)
		).apply(instance, Response::new));
		
		static Codec<List<Response>> LIST_CODEC = Response.CODEC.listOf();
		
		public Optional<ResponseData> evaluateData(int responseIndex, LivingEntity entity, ServerPlayer serverPlayer)
		{
			Optional<ConditionFailure> conditionFailure;
			if(this.condition().test(entity, serverPlayer))
				conditionFailure = Optional.empty();
			else
			{
				if(this.hideIfFailed())
					return Optional.empty();
				
				Component failureMessages = this.condition().getFailureTooltip();
				
				conditionFailure = Optional.of(new ConditionFailure(failureMessages));
			}
			return Optional.of(new ResponseData(this.message().evaluateComponent(entity, serverPlayer), responseIndex, conditionFailure));
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
			
			Dialogue nextDialogue = DialogueManager.getInstance().getDialogue(nextPath.get());
			if(nextDialogue != null)
				Dialogue.openScreen(entity, player, nextDialogue);
		}
	}
	
	public record RandomlySelectable(Condition condition, int weight)
	{
		static Codec<RandomlySelectable> CODEC = RecordCodecBuilder.create(instance ->
				instance.group(Condition.CODEC.fieldOf("condition").forGetter(RandomlySelectable::condition),
								PreservingOptionalFieldCodec.withDefault(Codec.INT, "dialogue_weight", 10).forGetter(RandomlySelectable::weight))
						.apply(instance, RandomlySelectable::new));
		
		public RandomlySelectable(Condition condition)
		{
			this(condition, 10);
		}
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
