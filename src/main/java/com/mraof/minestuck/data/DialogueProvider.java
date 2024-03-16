package com.mraof.minestuck.data;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.dialogue.Dialogue;
import com.mraof.minestuck.entity.dialogue.DialogueMessage;
import com.mraof.minestuck.entity.dialogue.Trigger;
import com.mraof.minestuck.entity.dialogue.condition.Condition;
import com.mraof.minestuck.entity.dialogue.condition.ListCondition;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.LanguageProvider;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.IntStream;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class DialogueProvider implements DataProvider
{
	private final Map<ResourceLocation, Dialogue> dialogues = new HashMap<>();
	private final Map<ResourceLocation, Dialogue.SelectableDialogue> selectableDialogueMap = new HashMap<>();
	
	private final String modId;
	private final PackOutput output;
	private final LanguageProvider languageProvider;
	
	public DialogueProvider(String modId, PackOutput output, LanguageProvider languageProvider)
	{
		this.modId = modId;
		this.output = output;
		this.languageProvider = languageProvider;
		
		//Call this early so that language stuff gets added before the language provider generates its file regardless of the order that providers are run
		addDialogue();
	}
	
	protected abstract void addDialogue();
	
	protected final ResourceLocation add(String path, DialogueBuilder builder)
	{
		return builder.buildDialogue(new ResourceLocation(modId, path), this::checkAndAdd);
	}
	
	protected final void addRandomlySelectable(String path, SelectableBuilder selectable, DialogueBuilder builder)
	{
		ResourceLocation id = new ResourceLocation(modId, path);
		ResourceLocation dialogueId = builder.buildDialogue(id, this::checkAndAdd);
		this.selectableDialogueMap.put(id, selectable.build(dialogueId));
	}
	
	private void checkAndAdd(ResourceLocation id, Dialogue dialogue)
	{
		if(this.dialogues.containsKey(id))
			throw new IllegalArgumentException(id + " was added twice");
		this.dialogues.put(id, dialogue);
	}
	
	public interface DialogueBuilder
	{
		ResourceLocation buildDialogue(ResourceLocation id, BiConsumer<ResourceLocation, Dialogue> register);
	}
	
	public static class NodeSelectorBuilder implements DialogueBuilder
	{
		private final List<Pair<Condition, NodeBuilder>> conditionedNodes = new ArrayList<>();
		@Nullable
		private NodeBuilder defaultNode;
		
		public NodeSelectorBuilder node(Condition condition, NodeBuilder node)
		{
			this.conditionedNodes.add(Pair.of(condition, node));
			return this;
		}
		
		public NodeSelectorBuilder defaultNode(NodeBuilder node)
		{
			this.defaultNode = node;
			return this;
		}
		
		public Dialogue.NodeSelector build(ResourceLocation id)
		{
			Objects.requireNonNull(this.defaultNode, "Default node must be set");
			return new Dialogue.NodeSelector(this.conditionedNodes.stream().map(pair -> pair.mapSecond(builder -> builder.build(id))).toList(),
					this.defaultNode.build(id));
		}
		
		@Override
		public ResourceLocation buildDialogue(ResourceLocation id, BiConsumer<ResourceLocation, Dialogue> register)
		{
			register.accept(id, new Dialogue(this.build(id)));
			return id;
		}
	}
	
	public static class NodeBuilder implements DialogueBuilder
	{
		private final Function<ResourceLocation, DialogueMessage> messageProvider;
		@Nullable
		private Function<ResourceLocation, DialogueMessage> descriptionProvider;
		private String animation = Dialogue.DEFAULT_ANIMATION;
		private ResourceLocation guiPath = Dialogue.DEFAULT_GUI;
		private final List<ResponseBuilder> responses = new ArrayList<>();
		
		NodeBuilder(DialogueMessage message)
		{
			this.messageProvider = id -> message;
		}
		
		NodeBuilder(Function<ResourceLocation, DialogueMessage> messageProvider)
		{
			this.messageProvider = messageProvider;
		}
		
		public NodeBuilder description(Function<ResourceLocation, DialogueMessage> provider)
		{
			this.descriptionProvider = provider;
			return this;
		}
		
		public NodeBuilder animation(String animation)
		{
			this.animation = animation;
			return this;
		}
		
		public NodeBuilder gui(ResourceLocation guiPath)
		{
			this.guiPath = guiPath;
			return this;
		}
		
		public NodeBuilder addClosingResponse(DialogueMessage message)
		{
			return addResponse(new ResponseBuilder(message));
		}
		
		public NodeBuilder addClosingResponse(Function<ResourceLocation, DialogueMessage> message)
		{
			return addResponse(new ResponseBuilder(message));
		}
		
		public NodeBuilder next(ResourceLocation dialogueId)
		{
			return this.addResponse(new ResponseBuilder(ARROW).nextDialogue(dialogueId));
		}
		
		@Deprecated
		public NodeBuilder next(String dialoguePath)
		{
			return this.addResponse(new ResponseBuilder(ARROW).nextDialogue(dialoguePath));
		}
		
		public NodeBuilder addResponse(ResponseBuilder responseBuilder)
		{
			this.responses.add(responseBuilder);
			return this;
		}
		
		public Dialogue.DialogueNode build(ResourceLocation id)
		{
			DialogueMessage message = this.messageProvider.apply(id);
			Optional<DialogueMessage> description = this.descriptionProvider != null ? Optional.of(this.descriptionProvider.apply(id)) : Optional.empty();
			return new Dialogue.DialogueNode(message, description, this.animation, this.guiPath, this.responses.stream().map(builder -> builder.build(id)).toList());
		}
		
		@Override
		public ResourceLocation buildDialogue(ResourceLocation id, BiConsumer<ResourceLocation, Dialogue> register)
		{
			return new NodeSelectorBuilder().defaultNode(this)
					.buildDialogue(id, register);
		}
	}
	
	public static class ResponseBuilder
	{
		private final Function<ResourceLocation, DialogueMessage> message;
		private final List<Trigger> triggers = new ArrayList<>();
		@Nullable
		private ResourceLocation nextDialoguePath = null;
		private boolean loopNextPath = false;
		private Condition condition = Condition.AlwaysTrue.INSTANCE;
		private boolean hideIfFailed = true;
		private Function<ResourceLocation, String> failTooltip = id -> null;
		
		ResponseBuilder(DialogueMessage message)
		{
			this.message = id -> message;
		}
		
		ResponseBuilder(Function<ResourceLocation, DialogueMessage> message)
		{
			this.message = message;
		}
		
		public ResponseBuilder nextDialogue(ResourceLocation nextDialoguePath)
		{
			this.loopNextPath = false;
			this.nextDialoguePath = nextDialoguePath;
			return this;
		}
		
		@Deprecated
		public ResponseBuilder nextDialogue(String nextDialoguePath)
		{
			return this.nextDialogue(new ResourceLocation(Minestuck.MOD_ID, nextDialoguePath));
		}
		
		public ResponseBuilder loop()
		{
			this.loopNextPath = true;
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
		
		public Dialogue.Response build(ResourceLocation id)
		{
			ResourceLocation nextPath = this.loopNextPath ? id : this.nextDialoguePath;
			DialogueMessage message = this.message.apply(id);
			return new Dialogue.Response(message, this.triggers, Optional.ofNullable(nextPath), this.condition, this.hideIfFailed, Optional.ofNullable(this.failTooltip.apply(id)));
		}
	}
	
	public static class ChainBuilder implements DialogueBuilder
	{
		private boolean withFolders = false;
		private final List<NodeBuilder> nodes = new ArrayList<>();
		private boolean loop = false;
		
		public ChainBuilder withFolders()
		{
			this.withFolders = true;
			return this;
		}
		
		public ChainBuilder node(NodeBuilder nodeBuilder)
		{
			this.nodes.add(nodeBuilder);
			return this;
		}
		
		public ChainBuilder loop()
		{
			this.loop = true;
			return this;
		}
		
		@Override
		public ResourceLocation buildDialogue(ResourceLocation id, BiConsumer<ResourceLocation, Dialogue> register)
		{
			if(this.nodes.isEmpty())
				throw new IllegalStateException("Nodes must be added to this chain builder");
			
			List<ResourceLocation> ids = IntStream.range(0, this.nodes.size())
					.mapToObj(index -> id.withSuffix((withFolders ? "/" : ".") + (index + 1))).toList();
			
			for(int index = 1; index < this.nodes.size(); index++)
				this.nodes.get(index - 1).next(ids.get(index));
			if(this.loop)
				this.nodes.get(this.nodes.size() - 1).next(ids.get(0));
			
			for(int index = 1; index < this.nodes.size(); index++)
				this.nodes.get(index).buildDialogue(ids.get(index), register);
			return this.nodes.get(0).buildDialogue(ids.get(0), register);
		}
	}
	
	public static final DialogueMessage ARROW = new DialogueMessage("minestuck.arrow");
	public static final DialogueMessage DOTS = new DialogueMessage("minestuck.dots");
	
	@Deprecated
	public static Function<ResourceLocation, DialogueMessage> defaultKeyMsg(DialogueMessage.Argument... arguments)
	{
		return id -> msg(languageKeyBase(id), arguments);
	}
	
	public Function<ResourceLocation, DialogueMessage> defaultKeyMsg(String text, DialogueMessage.Argument... arguments)
	{
		return id -> msg(languageKeyBase(id), text, arguments);
	}
	
	public Function<ResourceLocation, DialogueMessage> subMsg(String key, String text, DialogueMessage.Argument... arguments)
	{
		return id -> msg(languageKeyBase(id) + "." + key, text, arguments);
	}
	
	@Deprecated
	public static DialogueMessage msg(String key, DialogueMessage.Argument... arguments)
	{
		return new DialogueMessage(key, List.of(arguments));
	}
	
	public DialogueMessage msg(String key, String text, DialogueMessage.Argument... arguments)
	{
		this.languageProvider.add(key, text);
		return new DialogueMessage(key, List.of(arguments));
	}
	
	public Function<ResourceLocation, String> subText(String subKey, String text)
	{
		return id -> {
			String key = languageKeyBase(id) + "." + subKey;
			this.languageProvider.add(key, text);
			return key;
		};
	}
	
	private static String languageKeyBase(ResourceLocation id)
	{
		return id.getNamespace() + ".dialogue." + id.getPath().replace("/", ".");
	}
	
	public static final class SelectableBuilder {
		private final Condition condition;
		private final int weight;
		private boolean keepOnReset = false;
		
		public SelectableBuilder(Condition condition, int weight)
		{
			this.condition = condition;
			this.weight = weight;
		}
		
		public SelectableBuilder keepOnReset()
		{
			this.keepOnReset = true;
			return this;
		}
		
		public Dialogue.SelectableDialogue build(ResourceLocation dialogueId)
		{
			return new Dialogue.SelectableDialogue(dialogueId, this.condition, this.weight, this.keepOnReset);
		}
	}
	
	public static Condition all(Condition... conditions)
	{
		return new ListCondition(List.of(conditions), ListCondition.ListType.ALL);
	}
	
	public static Condition any(Condition... conditions)
	{
		return new ListCondition(List.of(conditions), ListCondition.ListType.ANY);
	}
	
	public static Condition one(Condition... conditions)
	{
		return new ListCondition(List.of(conditions), ListCondition.ListType.ONE);
	}
	
	public static Condition none(Condition... conditions)
	{
		return new ListCondition(List.of(conditions), ListCondition.ListType.NONE);
	}
	
	@SuppressWarnings("unused")
	public static SelectableBuilder weighted(int weight, Condition condition)
	{
		return new SelectableBuilder(condition, weight);
	}
	
	public static SelectableBuilder defaultWeight(Condition condition)
	{
		return new SelectableBuilder(condition, Dialogue.SelectableDialogue.DEFAULT_WEIGHT);
	}
	
	public static Condition isInLand(TerrainLandType landType)
	{
		return new Condition.InTerrainLandType(landType);
	}
	
	public static Condition isInLand(TitleLandType landType)
	{
		return new Condition.InTitleLandType(landType);
	}
	
	public static Condition isInTerrainLand(TagKey<TerrainLandType> tag)
	{
		return new Condition.InTerrainLandTypeTag(tag);
	}
	
	public static Condition isInTitleLand(TagKey<TitleLandType> tag)
	{
		return new Condition.InTitleLandTypeTag(tag);
	}
	
	public static Condition isAnyEntityType(EntityType<?>... entityTypes)
	{
		if(entityTypes.length == 1)
			return new Condition.IsEntityType(entityTypes[0]);
		else
			return new ListCondition(Arrays.stream(entityTypes).<Condition>map(Condition.IsEntityType::new).toList(), ListCondition.ListType.ANY);
	}
	
	@Override
	public CompletableFuture<?> run(CachedOutput cache)
	{
		Path outputPath = output.getOutputFolder();
		List<CompletableFuture<?>> futures = new ArrayList<>(dialogues.size());
		
		for(Map.Entry<ResourceLocation, Dialogue> entry : dialogues.entrySet())
		{
			Path dialoguePath = getPath(outputPath, entry.getKey());
			JsonElement dialogueJson = Dialogue.CODEC.encodeStart(JsonOps.INSTANCE, entry.getValue()).getOrThrow(false, LOGGER::error);
			futures.add(DataProvider.saveStable(cache, dialogueJson, dialoguePath));
		}
		
		for(Map.Entry<ResourceLocation, Dialogue.SelectableDialogue> entry : this.selectableDialogueMap.entrySet())
		{
			Path selectablePath = outputPath.resolve("data/" + entry.getKey().getNamespace() + "/minestuck/selectable_dialogue/" + entry.getKey().getPath() + ".json");
			JsonElement selectableJson = Dialogue.SelectableDialogue.CODEC.encodeStart(JsonOps.INSTANCE, entry.getValue()).getOrThrow(false, LOGGER::error);
			futures.add(DataProvider.saveStable(cache, selectableJson, selectablePath));
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