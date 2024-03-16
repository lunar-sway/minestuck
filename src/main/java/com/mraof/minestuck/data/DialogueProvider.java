package com.mraof.minestuck.data;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.entity.consort.ConsortEntity;
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
import net.minecraftforge.registries.RegistryObject;

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
	private final Map<ResourceLocation, Dialogue.NodeSelector> dialogues = new HashMap<>();
	
	protected final String modId;
	protected final PackOutput output;
	private final LanguageProvider languageProvider;
	
	public DialogueProvider(String modId, PackOutput output, LanguageProvider languageProvider)
	{
		this.modId = modId;
		this.output = output;
		this.languageProvider = languageProvider;
	}
	
	protected abstract void addDialogue();
	
	protected ResourceLocation dialogueId(String path)
	{
		return new ResourceLocation(this.modId, path);
	}
	
	protected final ResourceLocation add(String path, DialogueBuilder builder)
	{
		return builder.buildDialogue(dialogueId(path), this::checkAndAdd);
	}
	
	protected final void add(ResourceLocation id, SimpleDialogueBuilder builder)
	{
		builder.buildSimple(id, this::checkAndAdd);
	}
	
	private void checkAndAdd(ResourceLocation id, Dialogue.NodeSelector dialogue)
	{
		if(this.dialogues.containsKey(id))
			throw new IllegalArgumentException(id + " was added twice");
		this.dialogues.put(id, dialogue);
	}
	
	public interface DialogueBuilder
	{
		ResourceLocation buildDialogue(ResourceLocation id, BiConsumer<ResourceLocation, Dialogue.NodeSelector> register);
	}
	
	public interface SimpleDialogueBuilder extends DialogueBuilder
	{
		void buildSimple(ResourceLocation id, BiConsumer<ResourceLocation, Dialogue.NodeSelector> register);
		
		@Override
		default ResourceLocation buildDialogue(ResourceLocation id, BiConsumer<ResourceLocation, Dialogue.NodeSelector> register)
		{
			this.buildSimple(id, register);
			return id;
		}
	}
	
	public static class NodeSelectorBuilder implements SimpleDialogueBuilder
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
		public void buildSimple(ResourceLocation id, BiConsumer<ResourceLocation, Dialogue.NodeSelector> register)
		{
			register.accept(id, this.build(id));
		}
	}
	
	public static class NodeBuilder implements SimpleDialogueBuilder
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
		
		public NodeBuilder addResponse(ResponseBuilder responseBuilder)
		{
			this.responses.add(responseBuilder);
			return this;
		}
		
		public Dialogue.Node build(ResourceLocation id)
		{
			DialogueMessage message = this.messageProvider.apply(id);
			Optional<DialogueMessage> description = this.descriptionProvider != null ? Optional.of(this.descriptionProvider.apply(id)) : Optional.empty();
			return new Dialogue.Node(message, description, this.animation, this.guiPath, this.responses.stream().map(builder -> builder.build(id)).toList());
		}
		
		@Override
		public void buildSimple(ResourceLocation id, BiConsumer<ResourceLocation, Dialogue.NodeSelector> register)
		{
			new NodeSelectorBuilder().defaultNode(this)
					.buildSimple(id, register);
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
		public ResourceLocation buildDialogue(ResourceLocation id, BiConsumer<ResourceLocation, Dialogue.NodeSelector> register)
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
				this.nodes.get(index).buildSimple(ids.get(index), register);
			this.nodes.get(0).buildSimple(ids.get(0), register);
			return ids.get(0);
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
	
	public static Condition isInTerrain(RegistryObject<TerrainLandType> landType)
	{
		return new Condition.InTerrainLandType(landType.get());
	}
	
	public static Condition isInTitle(RegistryObject<TitleLandType> landType)
	{
		return new Condition.InTitleLandType(landType.get());
	}
	
	public static Condition isInTerrainLand(TagKey<TerrainLandType> tag)
	{
		return new Condition.InTerrainLandTypeTag(tag);
	}
	
	public static Condition isInTitleLand(TagKey<TitleLandType> tag)
	{
		return new Condition.InTitleLandTypeTag(tag);
	}
	
	@SafeVarargs
	public static Condition isAnyEntityType(RegistryObject<EntityType<ConsortEntity>>... entityType)
	{
		return isAnyEntityType(Arrays.stream(entityType).map(RegistryObject::get).toArray(EntityType<?>[]::new));
	}
	
	public static Condition isAnyEntityType(EntityType<?>... entityTypes)
	{
		if(entityTypes.length == 1)
			return new Condition.IsEntityType(entityTypes[0]);
		else
			return new ListCondition(Arrays.stream(entityTypes).<Condition>map(Condition.IsEntityType::new).toList(), ListCondition.ListType.ANY);
	}
	
	protected final boolean hasAddedDialogue(ResourceLocation dialogueId)
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