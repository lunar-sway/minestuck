package com.mraof.minestuck.data.dialogue;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.entity.dialogue.Dialogue;
import com.mraof.minestuck.entity.dialogue.DialogueAnimationData;
import com.mraof.minestuck.entity.dialogue.DialogueMessage;
import com.mraof.minestuck.entity.dialogue.Trigger;
import com.mraof.minestuck.entity.dialogue.condition.Condition;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * A provider specifically for building dialogue nodes and generating json files for said dialogue nodes.
 * @see SelectableDialogueProvider
 * @see DialogueLangHelper
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class DialogueProvider implements DataProvider
{
	private final Map<ResourceLocation, Dialogue.NodeSelector> dialogues = new HashMap<>();
	
	private final String modId;
	private final String subFolder;
	private final PackOutput output;
	
	public DialogueProvider(String modId, String subFolder, PackOutput output)
	{
		this.modId = modId;
		this.subFolder = subFolder;
		this.output = output;
	}
	
	public ResourceLocation dialogueId(String path)
	{
		return new ResourceLocation(this.modId, this.subFolder + "/" + path);
	}
	
	public ResourceLocation add(String path, DialogueProducer builder)
	{
		return builder.buildAndRegister(dialogueId(path), this::checkAndAdd);
	}
	
	public void add(ResourceLocation id, SimpleDialogueProducer builder)
	{
		builder.buildAndRegister(id, this::checkAndAdd);
	}
	
	private void checkAndAdd(ResourceLocation id, Dialogue.NodeSelector dialogue)
	{
		if(this.dialogues.containsKey(id))
			throw new IllegalArgumentException(id + " was added twice");
		this.dialogues.put(id, dialogue);
	}
	
	/**
	 * A builder that can would create one or more node selectors, with one being considered a starting node.
	 */
	public interface DialogueProducer
	{
		ResourceLocation buildAndRegister(ResourceLocation id, BiConsumer<ResourceLocation, Dialogue.NodeSelector> register);
	}
	
	/**
	 * A version of {@link DialogueProducer} which is expected to register the starting node selector under the given id.
	 */
	public interface SimpleDialogueProducer extends DialogueProducer
	{
		Dialogue.NodeSelector buildSelector(ResourceLocation id, BiConsumer<ResourceLocation, Dialogue.NodeSelector> register);
		
		@Override
		default ResourceLocation buildAndRegister(ResourceLocation id, BiConsumer<ResourceLocation, Dialogue.NodeSelector> register)
		{
			register.accept(id, this.buildSelector(id, register));
			return id;
		}
	}
	
	public interface NodeProducer
	{
		Dialogue.Node buildNode(ResourceLocation id, BiConsumer<ResourceLocation, Dialogue.NodeSelector> register);
	}
	
	public interface MessageProducer
	{
		DialogueMessage build(ResourceLocation baseId);
	}
	
	public static class NodeSelectorBuilder implements SimpleDialogueProducer
	{
		private final List<Pair<Condition, NodeProducer>> conditionedNodes = new ArrayList<>();
		@Nullable
		private NodeProducer defaultNode;
		
		public NodeSelectorBuilder node(Condition condition, String key, NodeProducer node)
		{
			return node(condition, (id, register) -> node.buildNode(id.withSuffix("." + key), register));
		}
		
		public NodeSelectorBuilder node(Condition condition, NodeProducer node)
		{
			this.conditionedNodes.add(Pair.of(condition, node));
			return this;
		}
		
		public NodeSelectorBuilder defaultNode(String key, NodeProducer node)
		{
			return defaultNode((id, register) -> node.buildNode(id.withSuffix("." + key), register));
		}
		
		public NodeSelectorBuilder defaultNode(NodeProducer node)
		{
			this.defaultNode = node;
			return this;
		}
		
		@Override
		public Dialogue.NodeSelector buildSelector(ResourceLocation id, BiConsumer<ResourceLocation, Dialogue.NodeSelector> register)
		{
			Objects.requireNonNull(this.defaultNode, "Default node must be set");
			return new Dialogue.NodeSelector(this.conditionedNodes.stream().map(pair -> pair.mapSecond(builder -> builder.buildNode(id, register))).toList(),
					this.defaultNode.buildNode(id, register));
		}
	}
	
	public static class NodeBuilder implements SimpleDialogueProducer, NodeProducer
	{
		private final List<Pair<Dialogue.MessageType, MessageProducer>> messages = new ArrayList<>();
		private DialogueAnimationData animation = DialogueAnimationData.DEFAULT_ANIMATION;
		private ResourceLocation guiPath = Dialogue.DEFAULT_GUI;
		private final List<ResponseBuilder> responses = new ArrayList<>();
		
		public NodeBuilder(MessageProducer message)
		{
			this.addMessage(message);
		}
		
		public NodeBuilder()
		{
		}
		
		public NodeBuilder addMessage(MessageProducer message)
		{
			this.messages.add(Pair.of(Dialogue.MessageType.ENTITY, message));
			return this;
		}
		
		public NodeBuilder addDescription(MessageProducer message)
		{
			this.messages.add(Pair.of(Dialogue.MessageType.DESCRIPTION, message));
			return this;
		}
		
		public NodeBuilder animation(String emotion)
		{
			return animation(new DialogueAnimationData(emotion, DialogueAnimationData.DEFAULT_SPRITE_HEIGHT, DialogueAnimationData.DEFAULT_SPRITE_WIDTH, 0, 0, 1.0F));
		}
		
		public NodeBuilder animation(DialogueAnimationData animation)
		{
			this.animation = animation;
			return this;
		}
		
		public NodeBuilder gui(ResourceLocation guiPath)
		{
			this.guiPath = guiPath;
			return this;
		}
		
		public NodeBuilder addClosingResponse()
		{
			return addClosingResponse(DialogueLangHelper.msg(DOTS));
		}
		
		public NodeBuilder addClosingResponse(MessageProducer message)
		{
			return addResponse(new ResponseBuilder(message));
		}
		
		public NodeBuilder next(String key, DialogueProducer dialogueProducer)
		{
			return this.addResponse(new ResponseBuilder(DialogueLangHelper.msg(ARROW)).nextDialogue(key, dialogueProducer));
		}
		
		public NodeBuilder next(ResourceLocation dialogueId)
		{
			return this.addResponse(new ResponseBuilder(DialogueLangHelper.msg(ARROW)).nextDialogue(dialogueId));
		}
		
		public NodeBuilder addResponse(ResponseBuilder responseBuilder)
		{
			this.responses.add(responseBuilder);
			return this;
		}
		
		@Override
		public Dialogue.Node buildNode(ResourceLocation id, BiConsumer<ResourceLocation, Dialogue.NodeSelector> register)
		{
			List<Pair<Dialogue.MessageType, DialogueMessage>> messages = this.messages.stream()
					.map(pair -> pair.mapSecond(producer -> producer.build(id))).toList();
			
			List<Dialogue.Response> responses = this.responses.stream().map(builder -> builder.build(id, register)).toList();
			
			return new Dialogue.Node(messages, this.animation, this.guiPath, responses);
		}
		
		@Override
		public Dialogue.NodeSelector buildSelector(ResourceLocation id, BiConsumer<ResourceLocation, Dialogue.NodeSelector> register)
		{
			return new NodeSelectorBuilder().defaultNode(this)
					.buildSelector(id, register);
		}
	}
	
	public static class ResponseBuilder
	{
		private final MessageProducer message;
		private final List<Trigger> triggers = new ArrayList<>();
		@Nullable
		private DialogueProducer nextDialogue = null;
		private boolean setEntrypoint = false;
		private final List<Pair<Dialogue.MessageType, MessageProducer>> replyMessages = new ArrayList<>();
		private Condition condition = Condition.AlwaysTrue.INSTANCE;
		private boolean hideIfFailed = true;
		private Function<ResourceLocation, String> failTooltip = id -> null;
		
		ResponseBuilder(MessageProducer message)
		{
			this.message = message;
		}
		
		public ResponseBuilder nextDialogue(String key, DialogueProducer dialogueProducer)
		{
			this.nextDialogue = (id, register) -> dialogueProducer.buildAndRegister(id.withSuffix("." + key), register);
			return this;
		}
		
		public ResponseBuilder nextDialogue(ResourceLocation nextDialogueId)
		{
			this.nextDialogue = (id, register) -> nextDialogueId;
			return this;
		}
		
		public ResponseBuilder addPlayerMessage(MessageProducer producer)
		{
			return this.addReplyMessage(Dialogue.MessageType.PLAYER, producer);
		}
		
		public ResponseBuilder addDescription(MessageProducer producer)
		{
			return this.addReplyMessage(Dialogue.MessageType.DESCRIPTION, producer);
		}
		
		public ResponseBuilder addReplyMessage(Dialogue.MessageType type, MessageProducer producer)
		{
			this.replyMessages.add(Pair.of(type, producer));
			return this;
		}
		
		public ResponseBuilder loop()
		{
			this.nextDialogue = (id, register) -> id;
			return this;
		}
		
		public ResponseBuilder setNextAsEntrypoint()
		{
			this.setEntrypoint = true;
			return this;
		}
		
		public ResponseBuilder condition(Condition condition)
		{
			this.hideIfFailed = true;
			this.condition = condition;
			return this;
		}
		
		public ResponseBuilder visibleCondition(Condition condition)
		{
			this.hideIfFailed = false;
			this.condition = condition;
			return this;
		}
		
		public ResponseBuilder visibleCondition(Function<ResourceLocation, String> failTooltip, Condition condition)
		{
			this.hideIfFailed = false;
			this.condition = condition;
			this.failTooltip = failTooltip;
			return this;
		}
		
		public ResponseBuilder addTrigger(Trigger trigger)
		{
			this.triggers.add(trigger);
			return this;
		}
		
		public Dialogue.Response build(ResourceLocation id, BiConsumer<ResourceLocation, Dialogue.NodeSelector> register)
		{
			Optional<Dialogue.NextDialogue> nextDialogue = Optional.ofNullable(this.nextDialogue).map(producer -> producer.buildAndRegister(id, register))
					.map(nextId -> new Dialogue.NextDialogue(nextId, this.setEntrypoint, this.replyMessages.stream().map(pair -> pair.mapSecond(producer -> producer.build(id))).toList()));
			DialogueMessage message = this.message.build(id);
			return new Dialogue.Response(message, this.triggers, nextDialogue, this.condition, this.hideIfFailed, Optional.ofNullable(this.failTooltip.apply(id)));
		}
	}
	
	public static final DialogueMessage ARROW = new DialogueMessage("minestuck.arrow");
	public static final DialogueMessage DOTS = new DialogueMessage("minestuck.dots");
	
	boolean hasAddedDialogue(ResourceLocation dialogueId)
	{
		return this.dialogues.containsKey(dialogueId);
	}
	
	@Override
	public CompletableFuture<?> run(CachedOutput cache)
	{
		Set<ResourceLocation> missingDialogue = new HashSet<>();
		dialogues.values().forEach(dialogue -> dialogue.visitConnectedDialogue(dialogueId -> {
			if(!hasAddedDialogue(dialogueId))
				missingDialogue.add(dialogueId);
		}));
		if(!missingDialogue.isEmpty())
			throw new IllegalStateException("Some referenced dialogue is missing: " + missingDialogue);
		
		Path outputPath = output.getOutputFolder();
		List<CompletableFuture<?>> futures = new ArrayList<>(dialogues.size());
		
		for(Map.Entry<ResourceLocation, Dialogue.NodeSelector> entry : dialogues.entrySet())
		{
			Path dialoguePath = getPath(outputPath, entry.getKey());
			JsonElement dialogueJson = Dialogue.NodeSelector.CODEC.encodeStart(JsonOps.INSTANCE, entry.getValue()).getOrThrow(false, LOGGER::error);
			futures.add(DataProvider.saveStable(cache, dialogueJson, dialoguePath));
		}
		
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
	}
	
	private static Path getPath(Path outputPath, ResourceLocation id)
	{
		return outputPath.resolve("data/" + id.getNamespace() + "/minestuck/dialogue/" + id.getPath() + ".json");
	}
	
	@Override
	public String getName()
	{
		return "Dialogues";
	}
}