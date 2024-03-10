package com.mraof.minestuck.data;

import com.google.gson.JsonElement;
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
	
	public DialogueProvider(PackOutput output)
	{
		this.output = output;
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
		add("pyre.1", new DialogueBuilder(defaultKeyMsg())
				.randomlySelectable(all(
						isInLand(LandTypes.WIND.get()),
						new Condition.IsOneOfEntityType(List.of(MSEntityTypes.SALAMANDER.get(), MSEntityTypes.TURTLE.get()))))
				.addResponse(new ResponseBuilder(msg("=>")).nextDialogue("pyre.2")));
		add("pyre.2", new DialogueBuilder(defaultKeyMsg()));
		
		//Pulse
		add("koolaid", new DialogueBuilder(defaultKeyMsg()).randomlySelectable(isInLand(LandTypes.PULSE.get())));
		add("murder_rain", new DialogueBuilder(defaultKeyMsg()).randomlySelectable(isInLand(LandTypes.PULSE.get())));
		add("swimming", new DialogueBuilder(defaultKeyMsg()).randomlySelectable(isInLand(LandTypes.PULSE.get())));
		add("blood_surprise", new DialogueBuilder(defaultKeyMsg()).randomlySelectable(isInLand(LandTypes.PULSE.get())));
		
		//Thunder
		add("skeleton_horse", new DialogueBuilder(defaultKeyMsg()).randomlySelectable(isInLand(LandTypes.THUNDER.get())));
		add("blue_moon", new DialogueBuilder(defaultKeyMsg()).randomlySelectable(isInLand(LandTypes.THUNDER.get())));
		add("lightning_strike", new DialogueBuilder(defaultKeyMsg()).randomlySelectable(isInLand(LandTypes.THUNDER.get())));
		add("reckoning.1", new DialogueBuilder(defaultKeyMsg())
				.randomlySelectable(isInLand(LandTypes.THUNDER.get()))
				.addResponse(new ResponseBuilder(msg("=>")).nextDialogue("reckoning.2")));
		add("reckoning.2", new DialogueBuilder(defaultKeyMsg())
				.addResponse(new ResponseBuilder(msg("=>")).nextDialogue("reckoning.3")));
		add("reckoning.3", new DialogueBuilder(defaultKeyMsg()));
		add("thunder_death.1", new DialogueBuilder(defaultKeyMsg())
				.randomlySelectable(all(
						isInLand(LandTypes.THUNDER.get()),
						isInLand(LandTypes.WOOD.get())))
				.addResponse(new ResponseBuilder(msg("=>")).nextDialogue("thunder_death.2")));
		add("thunder_death.2", new DialogueBuilder(defaultKeyMsg())
				.addResponse(new ResponseBuilder(msg("=>")).nextDialogue("thunder_death.3")));
		add("thunder_death.3", new DialogueBuilder(defaultKeyMsg()));
		add("hardcore", new DialogueBuilder(defaultKeyMsg())
				.randomlySelectable(all(
						isInLand(LandTypes.THUNDER.get()),
						isInLand(LandTypes.HEAT.get()))));
		
		
		add("mycelium.1", new DialogueBuilder(defaultKeyMsg())
				.randomlySelectable(isInLand(LandTypes.FUNGI.get()))
				.addResponse(new ResponseBuilder(msg("=>")).nextDialogue("mycelium.2")));
		add("mycelium.2", new DialogueBuilder(defaultKeyMsg()));
		
		//TODO was originally in MSTags.TerrainLandTypes.SAND
		add("camel/start", new DialogueBuilder(defaultKeyMsg())
				.randomlySelectable(isInTerrainLand(MSTags.TerrainLandTypes.SAND))
				.addResponse(new ResponseBuilder(msg("minestuck.dialogue.camel.yes")).nextDialogue("camel/no_camel"))
				.addResponse(new ResponseBuilder(msg("minestuck.dialogue.camel.no")).nextDialogue("camel/dancing_camel")));
		add("camel/no_camel", new DialogueBuilder(defaultKeyMsg()));
		add("camel/dancing_camel", new DialogueBuilder(defaultKeyMsg()));
		
		add("food_shop", new DialogueBuilder(defaultKeyMsg())
				.randomlySelectable(new Condition.IsEntityType(MSEntityTypes.SALAMANDER.get()))
				.addResponse("Never mind")
				.addResponse(new ResponseBuilder(msg("What do you have?")).addTrigger(new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_FOOD_STOCK, EnumConsort.MerchantType.FOOD))));
		add("fast_food", new DialogueBuilder(defaultKeyMsg())
				.randomlySelectable(new Condition.IsEntityType(MSEntityTypes.NAKAGATOR.get()))
				.addResponse("I'm good")
				.addResponse(new ResponseBuilder(msg("Show me your menu")).addTrigger(new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_FOOD_STOCK, EnumConsort.MerchantType.FOOD))));
		add("grocery_store", new DialogueBuilder(defaultKeyMsg())
				.randomlySelectable(new Condition.IsEntityType(MSEntityTypes.IGUANA.get()))
				.addResponse("No thanks")
				.addResponse(new ResponseBuilder(msg("What do you have to sell?")).addTrigger(new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_FOOD_STOCK, EnumConsort.MerchantType.FOOD))));
		add("tasty_welcome", new DialogueBuilder(defaultKeyMsg())
				.randomlySelectable(new Condition.IsEntityType(MSEntityTypes.TURTLE.get()))
				.addResponse("Goodbye")
				.addResponse(new ResponseBuilder(msg("Let me see your wares")).addTrigger(new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_FOOD_STOCK, EnumConsort.MerchantType.FOOD))));
		
		add("immortality_herb.1", new DialogueBuilder(defaultKeyMsg())
				.randomlySelectable(isInLand(LandTypes.FLORA.get()))
				.addResponse(new ResponseBuilder(msg("=>")).nextDialogue("immortality_herb.2")));
		add("immortality_herb.2", new DialogueBuilder(defaultKeyMsg())
				.addResponse(new ResponseBuilder(msg("=>")).nextDialogue("immortality_herb.3")));
		add("immortality_herb.3", new DialogueBuilder(defaultKeyMsg())
				.addResponse(new ResponseBuilder(msg("...")).addTrigger(new Trigger.Explode())));
	}
	
	private void testDialogues()
	{
		add("test1", new DialogueBuilder(defaultKeyMsg())
				.randomlySelectable()
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
		
		add("test2", new DialogueBuilder(defaultKeyMsg())
				.randomlySelectable()
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
		add("turtle_only", new DialogueBuilder(defaultKeyMsg()).randomlySelectable(new Condition.IsEntityType(MSEntityTypes.TURTLE.get())));
		
		add("nakagator_only", new DialogueBuilder(defaultKeyMsg()).randomlySelectable(new Condition.IsEntityType(MSEntityTypes.NAKAGATOR.get())));
		
		add("me_want_cookie", new DialogueBuilder(defaultKeyMsg())
				.randomlySelectable()
				.addResponse("im sorry fellow, I have no cookie for you. Bye")
				.addResponse(new ResponseBuilder(msg("why do you want cookie?")).loop())
				.addResponse(new ResponseBuilder(msg("here have a cookie chap")).nextDialogue("oh_yippee")
						.addCondition(new Condition.PlayerHasItem(Items.COOKIE, 1))
						.addTrigger(new Trigger.TakeItem(Items.COOKIE))
						.addTrigger(new Trigger.SetDialogue(new ResourceLocation(Minestuck.MOD_ID, "hunger_filled")))
						.dontHideFailed()));
		add("oh_yippee", new DialogueBuilder(defaultKeyMsg()));
		add("hunger_filled", new DialogueBuilder(defaultKeyMsg()));
		
		add("me_want_5_cookies", new DialogueBuilder(defaultKeyMsg())
				.randomlySelectable(5)
				.addResponse("im sorry fellow, I have no cookie for you. Bye")
				.addResponse(new ResponseBuilder(msg("here have 5 cookies chap")).nextDialogue("oh_yippee")
						.addCondition(new Condition.PlayerHasItem(Items.COOKIE, 5))
						.addTrigger(new Trigger.TakeItem(Items.COOKIE, 5))
						.dontHideFailed()));
		
		add("hi_friend_can_i_help_you", new DialogueBuilder(defaultKeyMsg())
				.randomlySelectable(11)
				.addResponse(new ResponseBuilder(msg("I hate you")).addTrigger(new Trigger.AddConsortReputation(-100)).dontHideFailed())
				.addResponse(new ResponseBuilder(msg("I love you")).addTrigger(new Trigger.AddConsortReputation(100)).dontHideFailed())
				.addResponse(new ResponseBuilder(msg("Rep above 500")).addCondition(new Condition.PlayerHasReputation(500, true)).dontHideFailed())
				.addResponse(new ResponseBuilder(msg("Rep below 200")).addCondition(new Condition.PlayerHasReputation(200, false)).dontHideFailed())
				.addResponse("bye"));
		
		add("test_arguments", new DialogueBuilder(defaultKeyMsg(DialogueMessage.Argument.PLAYER_NAME_LAND))
				.randomlySelectable()
				.addResponse(new ResponseBuilder(msg("Player name land: %s", DialogueMessage.Argument.PLAYER_NAME_LAND))));
	}
	
	private void add(String path, DialogueBuilder builder)
	{
		ResourceLocation id = new ResourceLocation(Minestuck.MOD_ID, path);
		dialogues.put(id, builder.build(id));
	}
	
	public static class DialogueBuilder
	{
		private final NodeBuilder nodeBuilder;
		@Nullable
		private Dialogue.UseContext useContext;
		
		DialogueBuilder(Function<String, DialogueMessage> messageProvider)
		{
			this(new NodeBuilder(messageProvider));
		}
		
		DialogueBuilder(DialogueMessage message)
		{
			this(new NodeBuilder(message));
		}
		
		DialogueBuilder(NodeBuilder nodeBuilder)
		{
			this.nodeBuilder = nodeBuilder;
		}
		
		@Deprecated
		public DialogueBuilder animation(String animation)
		{
			this.nodeBuilder.animation(animation);
			return this;
		}
		
		@Deprecated
		public DialogueBuilder addResponse(String response)
		{
			this.nodeBuilder.addResponse(response);
			return this;
		}
		
		@Deprecated
		public DialogueBuilder addResponse(ResponseBuilder responseBuilder)
		{
			this.nodeBuilder.addResponse(responseBuilder);
			return this;
		}
		
		public DialogueBuilder randomlySelectable()
		{
			return this.randomlySelectable(Conditions.EMPTY);
		}
		
		public DialogueBuilder randomlySelectable(int weight)
		{
			return this.randomlySelectable(Conditions.EMPTY, weight);
		}
		
		public DialogueBuilder randomlySelectable(Condition useCondition)
		{
			return this.randomlySelectable(new Conditions(List.of(useCondition), Conditions.Type.ALL));
		}
		
		@SuppressWarnings("unused")
		public DialogueBuilder randomlySelectable(Condition useCondition, int weight)
		{
			return this.randomlySelectable(new Conditions(List.of(useCondition), Conditions.Type.ALL), weight);
		}
		
		public DialogueBuilder randomlySelectable(Conditions useConditions)
		{
			this.useContext = new Dialogue.UseContext(useConditions);
			return this;
		}
		
		public DialogueBuilder randomlySelectable(Conditions useConditions, int weight)
		{
			this.useContext = new Dialogue.UseContext(useConditions, weight);
			return this;
		}
		
		private Dialogue build(ResourceLocation id)
		{
			Dialogue.DialogueNode node = this.nodeBuilder.build(id);
			return new Dialogue(id, new Dialogue.NodeSelector(List.of(), node), Optional.ofNullable(this.useContext));
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
	
	public static Function<String, DialogueMessage> defaultKeyMsg(DialogueMessage.Argument... arguments)
	{
		return key -> msg(key, arguments);
	}
	
	public static DialogueMessage msg(String key, DialogueMessage.Argument... arguments)
	{
		return new DialogueMessage(key, List.of(arguments));
	}
	
	public static Conditions all(Condition... conditions)
	{
		return new Conditions(List.of(conditions), Conditions.Type.ALL);
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
		registerDialogues();
		
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