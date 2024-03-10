package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.entity.dialogue.Condition;
import com.mraof.minestuck.entity.dialogue.Conditions;
import com.mraof.minestuck.entity.dialogue.DialogueMessage;
import com.mraof.minestuck.entity.dialogue.Trigger;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.loot.MSLootTables;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.EnumClass;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.world.lands.LandTypes;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.LanguageProvider;

import java.util.List;

public final class ConsortDialogueProvider extends DialogueProvider
{
	public ConsortDialogueProvider(PackOutput output, LanguageProvider enUsLanguageProvider)
	{
		super(Minestuck.MOD_ID, output, enUsLanguageProvider);
	}
	
	@Override
	protected void addDialogue()
	{
		consortDialogues();
		testDialogues();
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
						.addClosingResponse(responseMsg("bye", "Never mind"))
						.addResponse(new ResponseBuilder(responseMsg("next", "What do you have?"))
								.addTrigger(new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_FOOD_STOCK, EnumConsort.MerchantType.FOOD))));
		addRandomlySelectable("fast_food", defaultWeight(new Condition.IsEntityType(MSEntityTypes.NAKAGATOR.get())),
				new NodeBuilder(defaultKeyMsg("Welcome to MacCricket's, what would you like?"))
						.addClosingResponse(responseMsg("bye", "I'm good"))
						.addResponse(new ResponseBuilder(responseMsg("next", "Show me your menu"))
								.addTrigger(new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_FOOD_STOCK, EnumConsort.MerchantType.FOOD))));
		addRandomlySelectable("grocery_store", defaultWeight(new Condition.IsEntityType(MSEntityTypes.IGUANA.get())),
				new NodeBuilder(defaultKeyMsg("Thank you for choosing Stop and Hop, this village's #1 one grocer!"))
						.addClosingResponse(responseMsg("bye", "No thanks"))
						.addResponse(new ResponseBuilder(responseMsg("next", "What do you have to sell?"))
								.addTrigger(new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_FOOD_STOCK, EnumConsort.MerchantType.FOOD))));
		addRandomlySelectable("tasty_welcome", defaultWeight(new Condition.IsEntityType(MSEntityTypes.TURTLE.get())),
				new NodeBuilder(defaultKeyMsg("Welcome. I hope you find something tasty among our wares."))
						.addClosingResponse(responseMsg("bye", "Goodbye"))
						.addResponse(new ResponseBuilder(responseMsg("next", "Let me see your wares"))
								.addTrigger(new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_FOOD_STOCK, EnumConsort.MerchantType.FOOD))));
		
		addRandomlySelectable("immortality_herb.1", defaultWeight(isInLand(LandTypes.FLORA.get())),
				new NodeBuilder(defaultKeyMsg("I have a herb that grants immortality! I'm going to eat it right now!"))
						.addResponse(new ResponseBuilder(msg("=>")).nextDialogue("immortality_herb.2")));
		add("immortality_herb.2", new NodeBuilder(defaultKeyMsg("However, they are easily confused with an explosion-causing herb..."))
				.addResponse(new ResponseBuilder(msg("=>")).nextDialogue("immortality_herb.3")));
		add("immortality_herb.3", new NodeBuilder(defaultKeyMsg("I'm taking the risk."))
				.addResponse(new ResponseBuilder(responseMsg("end", "...")).addTrigger(new Trigger.Explode())));
	}
	
	private void testDialogues()
	{
		addRandomlySelectable("test1", defaultWeight(Conditions.EMPTY), new NodeBuilder(defaultKeyMsg("Press §eSHIFT§r for more info"))
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
				.addClosingResponse(responseMsg("no", "im sorry fellow, I have no cookie for you. Bye"))
				.addResponse(new ResponseBuilder(responseMsg("why", "why do you want cookie?")).loop())
				.addResponse(new ResponseBuilder(responseMsg("give", "here have a cookie chap")).nextDialogue("oh_yippee")
						.addCondition(new Condition.PlayerHasItem(Items.COOKIE, 1))
						.addTrigger(new Trigger.TakeItem(Items.COOKIE))
						.addTrigger(new Trigger.SetDialogue(new ResourceLocation(Minestuck.MOD_ID, "hunger_filled")))
						.dontHideFailed()));
		add("oh_yippee", new NodeBuilder(defaultKeyMsg()));
		add("hunger_filled", new NodeBuilder(defaultKeyMsg()));
		
		addRandomlySelectable("me_want_5_cookies", weighted(5, Conditions.EMPTY), new NodeBuilder(defaultKeyMsg())
				.addClosingResponse(responseMsg("no", "im sorry fellow, I have no cookie for you. Bye"))
				.addResponse(new ResponseBuilder(responseMsg("give", "here have 5 cookies chap")).nextDialogue("oh_yippee")
						.addCondition(new Condition.PlayerHasItem(Items.COOKIE, 5))
						.addTrigger(new Trigger.TakeItem(Items.COOKIE, 5))
						.dontHideFailed()));
		
		addRandomlySelectable("hi_friend_can_i_help_you", weighted(11, Conditions.EMPTY), new NodeBuilder(defaultKeyMsg())
				.addResponse(new ResponseBuilder(responseMsg("hate", "I hate you")).addTrigger(new Trigger.AddConsortReputation(-100)).dontHideFailed())
				.addResponse(new ResponseBuilder(responseMsg("love", "I love you")).addTrigger(new Trigger.AddConsortReputation(100)).dontHideFailed())
				.addResponse(new ResponseBuilder(responseMsg("high_rep", "Rep above 500")).addCondition(new Condition.PlayerHasReputation(500, true)).dontHideFailed())
				.addResponse(new ResponseBuilder(responseMsg("low_rep", "Rep below 200")).addCondition(new Condition.PlayerHasReputation(200, false)).dontHideFailed())
				.addClosingResponse(responseMsg("bye", "bye")));
		
		addRandomlySelectable("test_arguments", defaultWeight(Conditions.EMPTY), new NodeBuilder(defaultKeyMsg("Player name land: %s", DialogueMessage.Argument.PLAYER_NAME_LAND))
				.addResponse(new ResponseBuilder(responseMsg("name", "Player name land: %s", DialogueMessage.Argument.PLAYER_NAME_LAND))));
		
		addRandomlySelectable("look_rich", weighted(100, Conditions.EMPTY), new NodeSelectorBuilder()
				.node(new Condition.PlayerHasBoondollars(10_000, true), new NodeBuilder(msg("Hey, looks like you have a lot of boons!")))
				.node(new Condition.PlayerHasBoondollars(10, false), new NodeBuilder(msg("Wow, you barely have any boons. Poor you.")))
				.defaultNode(new NodeBuilder(msg("Hi! I can sense if someone has a lot of boondollars."))));
	}
}
