package com.mraof.minestuck.data;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.dialogue.*;
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
import java.util.function.Function;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class DialogueProvider implements DataProvider
{
	private final Map<ResourceLocation, Dialogue> dialogues = new HashMap<>();
	
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
	
	protected final ResourceLocation add(String path, NodeBuilder node)
	{
		return add(path, new NodeSelectorBuilder().defaultNode(node));
	}
	
	protected final ResourceLocation add(String path, NodeSelectorBuilder selector)
	{
		return add(path, new DialogueBuilder(selector));
	}
	
	protected final void addRandomlySelectable(String path, Dialogue.UseContext useContext, NodeBuilder node)
	{
		addRandomlySelectable(path, useContext, new NodeSelectorBuilder().defaultNode(node));
	}
	
	protected final void addRandomlySelectable(String path, Dialogue.UseContext useContext, NodeSelectorBuilder selector)
	{
		add(path, new DialogueBuilder(selector).randomlySelectable(useContext));
	}
	
	private ResourceLocation add(String path, DialogueBuilder builder)
	{
		ResourceLocation id = new ResourceLocation(modId, path);
		dialogues.put(id, builder.build(id));
		return id;
	}
	
	public static class DialogueBuilder
	{
		private final NodeSelectorBuilder selector;
		@Nullable
		private Dialogue.UseContext useContext;
		
		DialogueBuilder(NodeSelectorBuilder selector)
		{
			this.selector = selector;
		}
		
		public DialogueBuilder randomlySelectable(Dialogue.UseContext useContext)
		{
			this.useContext = useContext;
			return this;
		}
		
		private Dialogue build(ResourceLocation id)
		{
			return new Dialogue(this.selector.build(id), Optional.ofNullable(this.useContext));
		}
	}
	
	public static class NodeSelectorBuilder
	{
		private final List<Pair<Conditions, NodeBuilder>> conditionedNodes = new ArrayList<>();
		@Nullable
		private NodeBuilder defaultNode;
		
		public NodeSelectorBuilder node(Condition condition, NodeBuilder node)
		{
			this.conditionedNodes.add(Pair.of(all(condition), node));
			return this;
		}
		
		public NodeSelectorBuilder node(Conditions conditions, NodeBuilder node)
		{
			this.conditionedNodes.add(Pair.of(conditions, node));
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
	}
	
	public static class NodeBuilder
	{
		private final Function<ResourceLocation, DialogueMessage> messageProvider;
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
		
		public NodeBuilder addResponse(ResponseBuilder responseBuilder)
		{
			this.responses.add(responseBuilder);
			return this;
		}
		
		public Dialogue.DialogueNode build(ResourceLocation id)
		{
			DialogueMessage message = this.messageProvider.apply(id);
			return new Dialogue.DialogueNode(message, this.animation, this.guiPath, this.responses.stream().map(builder -> builder.build(id)).toList());
		}
	}
	
	public static class ResponseBuilder
	{
		private final Function<ResourceLocation, DialogueMessage> message;
		private Conditions conditions = Conditions.EMPTY;
		private final List<Trigger> triggers = new ArrayList<>();
		@Nullable
		private ResourceLocation nextDialoguePath = null;
		private boolean loopNextPath = false;
		private boolean hideIfFailed = true;
		
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
			return this.conditions(all(condition));
		}
		
		public ResponseBuilder conditions(Conditions conditions)
		{
			this.hideIfFailed = true;
			this.conditions = conditions;
			return this;
		}
		
		public ResponseBuilder visibleCondition(Condition condition)
		{
			return this.visibleConditions(all(condition));
		}
		
		public ResponseBuilder visibleConditions(Conditions conditions)
		{
			this.hideIfFailed = false;
			this.conditions = conditions;
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
			return new Dialogue.Response(message, this.conditions, this.triggers, Optional.ofNullable(nextPath), this.hideIfFailed);
		}
	}
	
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
	
	public static final DialogueMessage ARROW = new DialogueMessage("minestuck.arrow");
	public static final DialogueMessage DOTS = new DialogueMessage("minestuck.dots");
	
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
	
	private static String languageKeyBase(ResourceLocation id)
	{
		return id.getNamespace() + ".dialogue." + id.getPath().replace("/", ".");
	}
	
	public static Conditions all(Condition... conditions)
	{
		return new Conditions(List.of(conditions), Conditions.Type.ALL);
	}
	
	public static Conditions any(Condition... conditions)
	{
		return new Conditions(List.of(conditions), Conditions.Type.ANY);
	}
	
	public static Conditions one(Condition... conditions)
	{
		return new Conditions(List.of(conditions), Conditions.Type.ONE);
	}
	
	public static Conditions none(Condition... conditions)
	{
		return new Conditions(List.of(conditions), Conditions.Type.NONE);
	}
	
	@SuppressWarnings("unused")
	public static Dialogue.UseContext weighted(int weight, Condition condition)
	{
		return weighted(weight, all(condition));
	}
	
	public static Dialogue.UseContext weighted(int weight, Conditions conditions)
	{
		return new Dialogue.UseContext(conditions, weight);
	}
	
	public static Dialogue.UseContext defaultWeight(Condition condition)
	{
		return defaultWeight(all(condition));
	}
	
	public static Dialogue.UseContext defaultWeight(Conditions conditions)
	{
		return new Dialogue.UseContext(conditions);
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
	
	public static Condition isEntityType(EntityType<?>... entityType)
	{
		if(entityType.length == 1)
			return new Condition.IsEntityType(Arrays.stream(entityType).findFirst().get());
		else
			return new Condition.IsOneOfEntityType(List.of(entityType));
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