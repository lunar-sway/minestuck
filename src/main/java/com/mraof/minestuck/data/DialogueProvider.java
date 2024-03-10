package com.mraof.minestuck.data;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.entity.dialogue.*;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.loot.MSLootTables;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.EnumClass;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.LanguageProvider;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DialogueProvider implements DataProvider
{
	public static final String DEFAULT_ANIMATION = "generic_animation";
	public static final ResourceLocation DEFAULT_GUI = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/generic_extra_large.png");
	
	private final Map<ResourceLocation, Dialogue> dialogues = new HashMap<>();
	private final PackOutput output;
	private final LanguageProvider enUsLanguageProvider;
	
	public DialogueProvider(PackOutput output, LanguageProvider enUsLanguageProvider)
	{
		this.output = output;
		this.enUsLanguageProvider = enUsLanguageProvider;
		
		//Call this early so that language stuff gets added before the language provider generates its file regardless of the order that providers are run
		registerDialogues();
	}
	
	/**
	 * Since a Response can be used to link the current Dialogue with any other Dialogue, its important to make a habit of commenting where a Dialogue is used
	 */
	private void registerDialogues()
	{
		testDialogues();
		consortDialogues();
	}
	
	private void consortDialogues()
	{
		//Wind
		addRandomlySelectable("pyre.1", defaultWeight(all(isInLand(LandTypes.WIND.get()), new Condition.IsOneOfEntityType(List.of(MSEntityTypes.SALAMANDER.get(), MSEntityTypes.TURTLE.get())))),
				new NodeBuilder(defaultKeyMsg("If only I was faster than the wind! That would be fun!"))
						.addResponse(new ResponseBuilder(msg("=>")).nextDialogue(
								add("pyre.2", new NodeBuilder(defaultKeyMsg("Actually, nevermind. I would be burned on a pyre for being a witch due to our primal society."))))
						));
		
		//Pulse
		addRandomlySelectable("koolaid", defaultWeight(isInLand(LandTypes.PULSE.get())),
				new NodeBuilder(defaultKeyMsg("Some people say the oceans of blood are actually kool-aid. I'm too scared to taste it for myself.")));
		addRandomlySelectable("murder_rain", defaultWeight(isInLand(LandTypes.PULSE.get())),
				new NodeBuilder(defaultKeyMsg("You don't want to know what it's like to be outside when it rains. You can't tell who's a murderer or who forgot an umbrella!")));
		addRandomlySelectable("swimming", defaultWeight(isInLand(LandTypes.PULSE.get())),
				new NodeBuilder(defaultKeyMsg("If you're looking for a good land to swim in, it's definitely not this one.")));
		addRandomlySelectable("blood_surprise", defaultWeight(isInLand(LandTypes.PULSE.get())),
				new NodeBuilder(defaultKeyMsg("OH GOD IS THAT BLOOD oh wait nevermind.")));
		
		//Thunder
		addRandomlySelectable("skeleton_horse", defaultWeight(isInLand(LandTypes.THUNDER.get())),
				new NodeBuilder(defaultKeyMsg("Some people say at night, skeletons riding skeleton horses come through the town.")));
		addRandomlySelectable("blue_moon", defaultWeight(isInLand(LandTypes.THUNDER.get())),
				new NodeBuilder(defaultKeyMsg("Every once in a blue moon, lightning strikes and burns down the village. We have to rebuild it!")));
		addRandomlySelectable("lightning_strike", defaultWeight(isInLand(LandTypes.THUNDER.get())),
				new NodeBuilder(defaultKeyMsg("You don't want to be struck by lightning. No one does.")));
		addRandomlySelectable("reckoning.1", defaultWeight(isInLand(LandTypes.THUNDER.get())),
				new NodeBuilder(defaultKeyMsg("Those darn doomsayers, preaching about the Apocalypse and The Reckoning and such!"))
				.addResponse(new ResponseBuilder(msg("=>")).nextDialogue("reckoning.2")));
		add("reckoning.2", new NodeBuilder(defaultKeyMsg("What's The Reckoning? It's when meteors from The Veil are sent towards Skaia."))
				.addResponse(new ResponseBuilder(msg("=>")).nextDialogue("reckoning.3")));
		add("reckoning.3", new NodeBuilder(defaultKeyMsg("Like any reasonable %s believes in that!")));
		addRandomlySelectable("thunder_death.1", defaultWeight(all(isInLand(LandTypes.THUNDER.get()), isInLand(LandTypes.WOOD.get()))),
				new NodeBuilder(defaultKeyMsg("We're lucky to have rain with this weather."))
						.addResponse(new ResponseBuilder(msg("=>")).nextDialogue("thunder_death.2")));
		add("thunder_death.2", new NodeBuilder(defaultKeyMsg("Otherwise the thunder would surely have been our death."))
				.addResponse(new ResponseBuilder(msg("=>")).nextDialogue("thunder_death.3")));
		add("thunder_death.3", new NodeBuilder(defaultKeyMsg()));
		addRandomlySelectable("hardcore", defaultWeight(all(isInLand(LandTypes.THUNDER.get()), isInLand(LandTypes.HEAT.get()))),
				new NodeBuilder(defaultKeyMsg("This land is HARDCORE! There's lava and lightning wherever you go!")));
		
		
		addRandomlySelectable("mycelium.1", defaultWeight(isInLand(LandTypes.FUNGI.get())),
				new NodeBuilder(defaultKeyMsg("Frog, don't you love the feeling of mycelium on your toes?"))
						.addResponse(new ResponseBuilder(msg("=>"))
								.nextDialogue(add("mycelium.2", new NodeBuilder(defaultKeyMsg("No? Is that just me?"))))));
		
		//TODO was originally in MSTags.TerrainLandTypes.SAND
		addRandomlySelectable("camel/start", defaultWeight(isInTerrainLand(MSTags.TerrainLandTypes.SAND)), new NodeBuilder(defaultKeyMsg())
				.addResponse(new ResponseBuilder(msg("minestuck.dialogue.camel.yes", "Why not? Seems like a good price for a camel!"))
						.nextDialogue(add("camel/no_camel", new NodeBuilder(defaultKeyMsg("Hahaha! Sucker! I have no camel! Cya later! 8)")))))
				.addResponse(new ResponseBuilder(msg("minestuck.dialogue.camel.no", "Of course not! You know better!"))
						.nextDialogue(add("camel/dancing_camel", new NodeBuilder(defaultKeyMsg("Are you sure? Too bad! The camel knew how to dance, too!"))))));
		
		addRandomlySelectable("food_shop", defaultWeight(new Condition.IsEntityType(MSEntityTypes.SALAMANDER.get())),
				new NodeBuilder(defaultKeyMsg("You hungry? I bet you are! Why else would you be talking to me?"))
						.addResponse("Never mind")
						.addResponse(new ResponseBuilder(msg("What do you have?"))
								.addTrigger(new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_FOOD_STOCK, EnumConsort.MerchantType.FOOD))));
		addRandomlySelectable("fast_food", defaultWeight(new Condition.IsEntityType(MSEntityTypes.NAKAGATOR.get())),
				new NodeBuilder(defaultKeyMsg("Welcome to MacCricket's, what would you like?"))
						.addResponse("I'm good")
						.addResponse(new ResponseBuilder(msg("Show me your menu"))
								.addTrigger(new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_FOOD_STOCK, EnumConsort.MerchantType.FOOD))));
		addRandomlySelectable("grocery_store", defaultWeight(new Condition.IsEntityType(MSEntityTypes.IGUANA.get())),
				new NodeBuilder(defaultKeyMsg("Thank you for choosing Stop and Hop, this village's #1 one grocer!"))
						.addResponse("No thanks")
						.addResponse(new ResponseBuilder(msg("What do you have to sell?"))
								.addTrigger(new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_FOOD_STOCK, EnumConsort.MerchantType.FOOD))));
		addRandomlySelectable("tasty_welcome", defaultWeight(new Condition.IsEntityType(MSEntityTypes.TURTLE.get())),
				new NodeBuilder(defaultKeyMsg("Welcome. I hope you find something tasty among our wares."))
						.addResponse("Goodbye")
						.addResponse(new ResponseBuilder(msg("Let me see your wares"))
								.addTrigger(new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_FOOD_STOCK, EnumConsort.MerchantType.FOOD))));
		
		addRandomlySelectable("immortality_herb.1", defaultWeight(isInLand(LandTypes.FLORA.get())),
				new NodeBuilder(defaultKeyMsg("I have a herb that grants immortality! I'm going to eat it right now!"))
						.addResponse(new ResponseBuilder(msg("=>")).nextDialogue("immortality_herb.2")));
		add("immortality_herb.2", new NodeBuilder(defaultKeyMsg("However, they are easily confused with an explosion-causing herb..."))
				.addResponse(new ResponseBuilder(msg("=>")).nextDialogue("immortality_herb.3")));
		add("immortality_herb.3", new NodeBuilder(defaultKeyMsg("I'm taking the risk."))
				.addResponse(new ResponseBuilder(msg("...")).addTrigger(new Trigger.Explode())));
	}
	
	private void testDialogues()
	{
		addRandomlySelectable("test1", defaultWeight(Conditions.EMPTY), new NodeBuilder(defaultKeyMsg())
				.animation("test1animation")
				.addResponse(new ResponseBuilder(msg("test1response1")).nextDialogue("test2")
						.addCondition(new Condition.IsEntityType(MSEntityTypes.TURTLE.get()))
						.addCondition(new Condition.IsEntityType(MSEntityTypes.IGUANA.get()))
						.conditionType(Conditions.Type.ANY))
				.addResponse(new ResponseBuilder(msg("test1response2")).nextDialogue("test2").addCondition(new Condition.IsEntityType(MSEntityTypes.NAKAGATOR.get())))
				.addResponse(new ResponseBuilder(msg("test1response3")).nextDialogue("test2").addTrigger(new Trigger.Command("summon minestuck:grist ~ ~ ~ {Value:200}")))
				.addResponse(new ResponseBuilder(msg("test1response4"))
						.addCondition(new Condition.HasConditions(new Conditions(List.of(new Condition.IsEntityType(MSEntityTypes.NAKAGATOR.get()), new Condition.IsEntityType(MSEntityTypes.TURTLE.get()), new Condition.IsEntityType(MSEntityTypes.IGUANA.get()), new Condition.IsEntityType(MSEntityTypes.SALAMANDER.get())), Conditions.Type.ONE)))
						.addCondition(new Condition.HasConditions(new Conditions(List.of(new Condition.IsCarapacian(), new Condition.PlayerHasItem(MSItems.ACE_OF_CLUBS.get(), 1)), Conditions.Type.ONE)))
						.conditionType(Conditions.Type.ONE)
						.dontHideFailed()));
		
		addRandomlySelectable("test2", defaultWeight(Conditions.EMPTY), new NodeBuilder(defaultKeyMsg())
				.animation("test2animation")
				.addResponse(new ResponseBuilder(msg("test2response1")).nextDialogue("test1")
						.addCondition(new Condition.IsEntityType(MSEntityTypes.SALAMANDER.get()))
						.dontHideFailed())
				.addResponse(new ResponseBuilder(msg("test2response2")).nextDialogue("test1")
						.addCondition(new Condition.IsCarapacian())
						.addCondition(isInLand(LandTypes.END.get()))
						.addCondition(isInLand(LandTypes.SHADE.get()))
						.addTrigger(new Trigger.Command("say hi"))
						.conditionType(Conditions.Type.NONE)
						.dontHideFailed())
				.addResponse(new ResponseBuilder(msg("test2response3")).nextDialogue("test1")
						.addTrigger(new Trigger.Command("""
								tellraw @a ["",{"text":"Welcome","color":"aqua"},{"text":" to "},{"text":"Minecraft","color":"#9B9B17"},{"text":" Tools "},{"text":"partner.","obfuscated":true},{"text":" "},{"selector":"@s"},{"text":" fs"}]"""))
				)
				.addResponse(new ResponseBuilder(msg("test2response4"))
						.addCondition(new Condition.IsCarapacian())
						.addCondition(new Condition.PlayerIsClass(EnumClass.WITCH))
						.addCondition(new Condition.PlayerIsClass(EnumClass.MAGE))
						.addCondition(new Condition.PlayerIsAspect(EnumAspect.HEART))
						.addCondition(new Condition.PlayerIsAspect(EnumAspect.DOOM))
						.addCondition(isInLand(LandTypes.RAIN.get()))
						.conditionType(Conditions.Type.ONE)
						.dontHideFailed()));
		addRandomlySelectable("turtle_only", defaultWeight(new Condition.IsEntityType(MSEntityTypes.TURTLE.get())), new NodeBuilder(defaultKeyMsg()));
		
		addRandomlySelectable("nakagator_only", defaultWeight(new Condition.IsEntityType(MSEntityTypes.NAKAGATOR.get())), new NodeBuilder(defaultKeyMsg()));
		
		addRandomlySelectable("me_want_cookie", defaultWeight(Conditions.EMPTY), new NodeBuilder(defaultKeyMsg())
				.addResponse("im sorry fellow, I have no cookie for you. Bye")
				.addResponse(new ResponseBuilder(msg("why do you want cookie?")).loop())
				.addResponse(new ResponseBuilder(msg("here have a cookie chap")).nextDialogue("oh_yippee")
						.addCondition(new Condition.PlayerHasItem(Items.COOKIE, 1))
						.addTrigger(new Trigger.TakeItem(Items.COOKIE))
						.addTrigger(new Trigger.SetDialogue(new ResourceLocation(Minestuck.MOD_ID, "hunger_filled")))
						.dontHideFailed()));
		add("oh_yippee", new NodeBuilder(defaultKeyMsg()));
		add("hunger_filled", new NodeBuilder(defaultKeyMsg()));
		
		addRandomlySelectable("me_want_5_cookies", weighted(5, Conditions.EMPTY), new NodeBuilder(defaultKeyMsg())
				.addResponse("im sorry fellow, I have no cookie for you. Bye")
				.addResponse(new ResponseBuilder(msg("here have 5 cookies chap")).nextDialogue("oh_yippee")
						.addCondition(new Condition.PlayerHasItem(Items.COOKIE, 5))
						.addTrigger(new Trigger.TakeItem(Items.COOKIE, 5))
						.dontHideFailed()));
		
		addRandomlySelectable("hi_friend_can_i_help_you", weighted(11, Conditions.EMPTY), new NodeBuilder(defaultKeyMsg())
				.addResponse(new ResponseBuilder(msg("I hate you")).addTrigger(new Trigger.AddConsortReputation(-100)).dontHideFailed())
				.addResponse(new ResponseBuilder(msg("I love you")).addTrigger(new Trigger.AddConsortReputation(100)).dontHideFailed())
				.addResponse(new ResponseBuilder(msg("Rep above 500")).addCondition(new Condition.PlayerHasReputation(500, true)).dontHideFailed())
				.addResponse(new ResponseBuilder(msg("Rep below 200")).addCondition(new Condition.PlayerHasReputation(200, false)).dontHideFailed())
				.addResponse("bye"));
		
		addRandomlySelectable("test_arguments", defaultWeight(Conditions.EMPTY), new NodeBuilder(defaultKeyMsg(DialogueMessage.Argument.PLAYER_NAME_LAND))
				.addResponse(new ResponseBuilder(msg("Player name land: %s", DialogueMessage.Argument.PLAYER_NAME_LAND))));
		
		addRandomlySelectable("look_rich", weighted(100, Conditions.EMPTY), new NodeSelectorBuilder()
				.node(new Condition.PlayerHasBoondollars(10_000, true), new NodeBuilder(msg("Hey, looks like you have a lot of boons!")))
				.node(new Condition.PlayerHasBoondollars(10, false), new NodeBuilder(msg("Wow, you barely have any boons. Poor you.")))
				.defaultNode(new NodeBuilder(msg("Hi! I can sense if someone has a lot of boondollars."))));
		
	}
	
	private ResourceLocation add(String path, NodeBuilder node)
	{
		return add(path, new NodeSelectorBuilder().defaultNode(node));
	}
	
	private ResourceLocation add(String path, NodeSelectorBuilder selector)
	{
		return add(path, new DialogueBuilder(selector));
	}
	
	private void addRandomlySelectable(String path, Dialogue.UseContext useContext, NodeBuilder node)
	{
		addRandomlySelectable(path, useContext, new NodeSelectorBuilder().defaultNode(node));
	}
	
	private void addRandomlySelectable(String path, Dialogue.UseContext useContext, NodeSelectorBuilder selector)
	{
		add(path, new DialogueBuilder(selector).randomlySelectable(useContext));
	}
	
	private ResourceLocation add(String path, DialogueBuilder builder)
	{
		ResourceLocation id = new ResourceLocation(Minestuck.MOD_ID, path);
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
			return new Dialogue(id, this.selector.build(id), Optional.ofNullable(this.useContext));
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
		private final Function<String, DialogueMessage> messageProvider;
		private String animation = DEFAULT_ANIMATION;
		private ResourceLocation guiPath = DEFAULT_GUI;
		private final List<ResponseBuilder> responses = new ArrayList<>();
		
		NodeBuilder(DialogueMessage message)
		{
			this.messageProvider = defaultKey -> message;
		}
		
		NodeBuilder(Function<String, DialogueMessage> messageProvider)
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
		
		public NodeBuilder addResponse(String response)
		{
			return addResponse(new ResponseBuilder(msg(response)));
		}
		
		public NodeBuilder addResponse(ResponseBuilder responseBuilder)
		{
			this.responses.add(responseBuilder);
			return this;
		}
		
		public Dialogue.DialogueNode build(ResourceLocation id)
		{
			DialogueMessage message = this.messageProvider.apply("minestuck.dialogue." + id.getPath().replace("/", "."));
			return new Dialogue.DialogueNode(message, this.animation, this.guiPath, this.responses.stream().map(builder -> builder.build(id)).toList());
		}
	}
	
	public static class ResponseBuilder
	{
		private final DialogueMessage message;
		private final List<Condition> conditions = new ArrayList<>();
		private Conditions.Type conditionsType = Conditions.Type.ALL;
		private final List<Trigger> triggers = new ArrayList<>();
		@Nullable
		private ResourceLocation nextDialoguePath = null;
		private boolean loopNextPath = false;
		private boolean hideIfFailed = true;
		
		ResponseBuilder(DialogueMessage message)
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
		
		public ResponseBuilder addCondition(Condition condition)
		{
			this.conditions.add(condition);
			return this;
		}
		
		public ResponseBuilder conditionType(Conditions.Type type)
		{
			this.conditionsType = type;
			return this;
		}
		
		public ResponseBuilder addTrigger(Trigger trigger)
		{
			this.triggers.add(trigger);
			return this;
		}
		
		public ResponseBuilder dontHideFailed()
		{
			this.hideIfFailed = false;
			return this;
		}
		
		public Dialogue.Response build(ResourceLocation selfPath)
		{
			ResourceLocation nextPath = this.loopNextPath ? selfPath : this.nextDialoguePath;
			return new Dialogue.Response(this.message, new Conditions(this.conditions, this.conditionsType), this.triggers, Optional.ofNullable(nextPath), this.hideIfFailed);
		}
	}
	
	@Deprecated
	public static Function<String, DialogueMessage> defaultKeyMsg(DialogueMessage.Argument... arguments)
	{
		return key -> msg(key, arguments);
	}
	
	public Function<String, DialogueMessage> defaultKeyMsg(String text, DialogueMessage.Argument... arguments)
	{
		return key -> msg(key, text, arguments);
	}
	
	@Deprecated
	public static DialogueMessage msg(String key, DialogueMessage.Argument... arguments)
	{
		return new DialogueMessage(key, List.of(arguments));
	}
	
	public DialogueMessage msg(String key, String text, DialogueMessage.Argument... arguments)
	{
		this.enUsLanguageProvider.add(key, text);
		return new DialogueMessage(key, List.of(arguments));
	}
	
	public static Conditions all(Condition... conditions)
	{
		return new Conditions(List.of(conditions), Conditions.Type.ALL);
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