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
		//add(new DialogueBuilder("dad_wind", isInLand(LandTypes.WIND.get())));
		add(new DialogueBuilder("pyre.1")
				.randomlySelectable(all(
						isInLand(LandTypes.WIND.get()),
						new Condition.IsOneOfEntityType(List.of(MSEntityTypes.SALAMANDER.get(), MSEntityTypes.TURTLE.get()))))
				.addResponse(new ResponseBuilder("=>").nextDialogue("pyre.2")));
		add(new DialogueBuilder("pyre.2"));
		
		//Pulse
		add(new DialogueBuilder("koolaid").randomlySelectable(isInLand(LandTypes.PULSE.get())));
		add(new DialogueBuilder("murder_rain").randomlySelectable(isInLand(LandTypes.PULSE.get())));
		add(new DialogueBuilder("swimming").randomlySelectable(isInLand(LandTypes.PULSE.get())));
		add(new DialogueBuilder("blood_surprise").randomlySelectable(isInLand(LandTypes.PULSE.get())));
		
		//Thunder
		add(new DialogueBuilder("skeleton_horse").randomlySelectable(isInLand(LandTypes.THUNDER.get())));
		add(new DialogueBuilder("blue_moon").randomlySelectable(isInLand(LandTypes.THUNDER.get())));
		add(new DialogueBuilder("lightning_strike").randomlySelectable(isInLand(LandTypes.THUNDER.get())));
		add(new DialogueBuilder("reckoning.1")
				.randomlySelectable(isInLand(LandTypes.THUNDER.get()))
				.addResponse(new ResponseBuilder("=>").nextDialogue("reckoning.2")));
		add(new DialogueBuilder("reckoning.2")
				.addResponse(new ResponseBuilder("=>").nextDialogue("reckoning.3")));
		add(new DialogueBuilder("reckoning.3"));
		add(new DialogueBuilder("thunder_death.1")
				.randomlySelectable(all(
						isInLand(LandTypes.THUNDER.get()),
						isInLand(LandTypes.WOOD.get())))
				.addResponse(new ResponseBuilder("=>").nextDialogue("thunder_death.2")));
		add(new DialogueBuilder("thunder_death.2")
				.addResponse(new ResponseBuilder("=>").nextDialogue("thunder_death.3")));
		add(new DialogueBuilder("thunder_death.3"));
		add(new DialogueBuilder("hardcore")
				.randomlySelectable(all(
						isInLand(LandTypes.THUNDER.get()),
						isInLand(LandTypes.HEAT.get()))));
		
		
		add(new DialogueBuilder("mycelium.1")
				.randomlySelectable(isInLand(LandTypes.FUNGI.get()))
				.addResponse(new ResponseBuilder("=>").nextDialogue("mycelium.2")));
		add(new DialogueBuilder("mycelium.2"));
		
		//TODO was originally in MSTags.TerrainLandTypes.SAND
		add(new DialogueBuilder("camel/start")
				.randomlySelectable(isInTerrainLand(MSTags.TerrainLandTypes.SAND))
				.addResponse(new ResponseBuilder("minestuck.dialogue.camel.yes").nextDialogue("camel/no_camel"))
				.addResponse(new ResponseBuilder("minestuck.dialogue.camel.no").nextDialogue("camel/dancing_camel")));
		add(new DialogueBuilder("camel/no_camel"));
		add(new DialogueBuilder("camel/dancing_camel"));
		
		add(new DialogueBuilder("food_shop")
				.randomlySelectable(new Condition.IsEntityType(MSEntityTypes.SALAMANDER.get()))
				.addResponse("Never mind")
				.addResponse(new ResponseBuilder("What do you have?").addTrigger(new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_FOOD_STOCK, EnumConsort.MerchantType.FOOD))));
		add(new DialogueBuilder("fast_food")
				.randomlySelectable(new Condition.IsEntityType(MSEntityTypes.NAKAGATOR.get()))
				.addResponse("I'm good")
				.addResponse(new ResponseBuilder("Show me your menu").addTrigger(new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_FOOD_STOCK, EnumConsort.MerchantType.FOOD))));
		add(new DialogueBuilder("grocery_store")
				.randomlySelectable(new Condition.IsEntityType(MSEntityTypes.IGUANA.get()))
				.addResponse("No thanks")
				.addResponse(new ResponseBuilder("What do you have to sell?").addTrigger(new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_FOOD_STOCK, EnumConsort.MerchantType.FOOD))));
		add(new DialogueBuilder("tasty_welcome")
				.randomlySelectable(new Condition.IsEntityType(MSEntityTypes.TURTLE.get()))
				.addResponse("Goodbye")
				.addResponse(new ResponseBuilder("Let me see your wares").addTrigger(new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_FOOD_STOCK, EnumConsort.MerchantType.FOOD))));
		
		add(new DialogueBuilder("immortality_herb.1")
				.randomlySelectable(isInLand(LandTypes.FLORA.get()))
				.addResponse(new ResponseBuilder("=>").nextDialogue("immortality_herb.2")));
		add(new DialogueBuilder("immortality_herb.2")
				.addResponse(new ResponseBuilder("=>").nextDialogue("immortality_herb.3")));
		add(new DialogueBuilder("immortality_herb.3")
				.addResponse(new ResponseBuilder("...").addTrigger(new Trigger.Explode())));
	}
	
	private void testDialogues()
	{
		add(new DialogueBuilder("test1")
				.randomlySelectable()
				.animation("test1animation")
				.addResponse(new ResponseBuilder("test1response1").nextDialogue("test2")
						.addCondition(new Condition.IsEntityType(MSEntityTypes.TURTLE.get()))
						.addCondition(new Condition.IsEntityType(MSEntityTypes.IGUANA.get()))
						.conditionType(Conditions.Type.ANY))
				.addResponse(new ResponseBuilder("test1response2").nextDialogue("test2").addCondition(new Condition.IsEntityType(MSEntityTypes.NAKAGATOR.get())))
				.addResponse(new ResponseBuilder("test1response3").nextDialogue("test2").addTrigger(new Trigger.Command("summon minestuck:grist ~ ~ ~ {Value:200}")))
				.addResponse(new ResponseBuilder("test1response4")
						.addCondition(new Condition.HasConditions(new Conditions(List.of(new Condition.IsEntityType(MSEntityTypes.NAKAGATOR.get()), new Condition.IsEntityType(MSEntityTypes.TURTLE.get()), new Condition.IsEntityType(MSEntityTypes.IGUANA.get()), new Condition.IsEntityType(MSEntityTypes.SALAMANDER.get())), Conditions.Type.ONE)))
						.addCondition(new Condition.HasConditions(new Conditions(List.of(new Condition.IsCarapacian(), new Condition.PlayerHasItem(MSItems.ACE_OF_CLUBS.get(), 1)), Conditions.Type.ONE)))
						.conditionType(Conditions.Type.ONE)
						.dontHideFailed()));
		
		add(new DialogueBuilder("test2")
				.randomlySelectable()
				.animation("test2animation")
				.addResponse(new ResponseBuilder("test2response1").nextDialogue("test1")
						.addCondition(new Condition.IsEntityType(MSEntityTypes.SALAMANDER.get()))
						.dontHideFailed())
				.addResponse(new ResponseBuilder("test2response2").nextDialogue("test1")
						.addCondition(new Condition.IsCarapacian())
						.addCondition(isInLand(LandTypes.END.get()))
						.addCondition(isInLand(LandTypes.SHADE.get()))
						.addTrigger(new Trigger.Command("say hi"))
						.conditionType(Conditions.Type.NONE)
						.dontHideFailed())
				.addResponse(new ResponseBuilder("test2response3").nextDialogue("test1")
						.addTrigger(new Trigger.Command("""
								tellraw @a ["",{"text":"Welcome","color":"aqua"},{"text":" to "},{"text":"Minecraft","color":"#9B9B17"},{"text":" Tools "},{"text":"partner.","obfuscated":true},{"text":" "},{"selector":"@s"},{"text":" fs"}]"""))
				)
				.addResponse(new ResponseBuilder("test2response4")
						.addCondition(new Condition.IsCarapacian())
						.addCondition(new Condition.PlayerIsClass(EnumClass.WITCH))
						.addCondition(new Condition.PlayerIsClass(EnumClass.MAGE))
						.addCondition(new Condition.PlayerIsAspect(EnumAspect.HEART))
						.addCondition(new Condition.PlayerIsAspect(EnumAspect.DOOM))
						.addCondition(isInLand(LandTypes.RAIN.get()))
						.conditionType(Conditions.Type.ONE)
						.dontHideFailed()));
		add(new DialogueBuilder("turtle_only").randomlySelectable(new Condition.IsEntityType(MSEntityTypes.TURTLE.get())));
		
		add(new DialogueBuilder("nakagator_only").randomlySelectable(new Condition.IsEntityType(MSEntityTypes.NAKAGATOR.get())));
		
		add(new DialogueBuilder("me_want_cookie")
				.randomlySelectable()
				.addResponse("im sorry fellow, I have no cookie for you. Bye")
				.addResponse(new ResponseBuilder("why do you want cookie?").loop())
				.addResponse(new ResponseBuilder("here have a cookie chap").nextDialogue("oh_yippee")
						.addCondition(new Condition.PlayerHasItem(Items.COOKIE, 1))
						.addTrigger(new Trigger.TakeItem(Items.COOKIE))
						.addTrigger(new Trigger.SetDialogue(new ResourceLocation(Minestuck.MOD_ID, "hunger_filled")))
						.dontHideFailed()));
		add(new DialogueBuilder("oh_yippee"));
		add(new DialogueBuilder("hunger_filled"));
		
		add(new DialogueBuilder("me_want_5_cookies")
				.randomlySelectable(5)
				.addResponse("im sorry fellow, I have no cookie for you. Bye")
				.addResponse(new ResponseBuilder("here have 5 cookies chap").nextDialogue("oh_yippee")
						.addCondition(new Condition.PlayerHasItem(Items.COOKIE, 5))
						.addTrigger(new Trigger.TakeItem(Items.COOKIE, 5))
						.dontHideFailed()));
		
		add(new DialogueBuilder("hi_friend_can_i_help_you")
				.randomlySelectable(11)
				.addResponse(new ResponseBuilder("I hate you").addTrigger(new Trigger.AddConsortReputation(-100)).dontHideFailed())
				.addResponse(new ResponseBuilder("I love you").addTrigger(new Trigger.AddConsortReputation(100)).dontHideFailed())
				.addResponse(new ResponseBuilder("Rep above 500").addCondition(new Condition.PlayerHasReputation(500, true)).dontHideFailed())
				.addResponse(new ResponseBuilder("Rep below 200").addCondition(new Condition.PlayerHasReputation(200, false)).dontHideFailed())
				.addResponse("bye"));
		
		add(new DialogueBuilder("test_arguments", DialogueMessage.Argument.PLAYER_NAME_LAND)
				.randomlySelectable()
				.addResponse(new ResponseBuilder("Player name land: %s", DialogueMessage.Argument.PLAYER_NAME_LAND)));
	}
	
	private void add(DialogueBuilder builder)
	{
		Dialogue.DialogueNode node = new Dialogue.DialogueNode(builder.message, builder.animation, builder.guiPath, builder.responses);
		Dialogue dialogue = new Dialogue(builder.path, new Dialogue.NodeSelector(List.of(), node), Optional.ofNullable(builder.useContext));
		dialogues.put(builder.path, dialogue);
	}
	
	public static class DialogueBuilder
	{
		private final ResourceLocation path;
		private final DialogueMessage message;
		private String animation = DEFAULT_ANIMATION;
		private ResourceLocation guiPath = DEFAULT_GUI;
		private final List<Dialogue.Response> responses = new ArrayList<>();
		@Nullable
		private Dialogue.UseContext useContext;
		
		DialogueBuilder(String path, DialogueMessage.Argument... arguments)
		{
			this.path = new ResourceLocation(Minestuck.MOD_ID, path);
			this.message = new DialogueMessage("minestuck.dialogue." + path.replace("/", "."), List.of(arguments));
		}
		
		public DialogueBuilder animation(String animation)
		{
			this.animation = animation;
			return this;
		}
		
		public DialogueBuilder gui(ResourceLocation guiPath)
		{
			this.guiPath = guiPath;
			return this;
		}
		
		public DialogueBuilder addResponse(String response)
		{
			return addResponse(new ResponseBuilder(response));
		}
		
		public DialogueBuilder addResponse(ResponseBuilder responseBuilder)
		{
			ResourceLocation nextPath = responseBuilder.loopNextPath ? this.path : responseBuilder.nextDialoguePath;
			this.responses.add(new Dialogue.Response(responseBuilder.message, new Conditions(responseBuilder.conditions, responseBuilder.conditionsType), responseBuilder.triggers, Optional.ofNullable(nextPath), responseBuilder.hideIfFailed));
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
		
		ResponseBuilder(String key, DialogueMessage.Argument... arguments)
		{
			this.message = new DialogueMessage(key, List.of(arguments));
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