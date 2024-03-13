package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.entity.dialogue.DialogueMessage;
import com.mraof.minestuck.entity.dialogue.Trigger;
import com.mraof.minestuck.entity.dialogue.condition.Condition;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.loot.MSLootTables;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.EnumClass;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.LanguageProvider;

import static com.mraof.minestuck.entity.MSEntityTypes.*;
import static com.mraof.minestuck.world.lands.LandTypes.*;

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
		addRandomlySelectable("dad_wind", defaultWeight(isInTitle(WIND)),
				new NodeBuilder(defaultKeyMsg("My dad was blown away in one of the recent wind storms.")));
		
		addRandomlySelectable("pyre.1", defaultWeight(all(isInTitle(WIND), isEntityType(SALAMANDER, TURTLE))),
				new NodeBuilder(defaultKeyMsg("If only I was faster than the wind! That would be fun!"))
						.next(add("pyre.2", new NodeBuilder(defaultKeyMsg("Actually, nevermind. I would be burned on a pyre for being a witch due to our primal society.")))));
		
		//Pulse
		addRandomlySelectable("koolaid", defaultWeight(all(isInTitle(PULSE), isEntityType(SALAMANDER, TURTLE))),
				new NodeBuilder(defaultKeyMsg("Some people say the oceans of blood are actually kool-aid. I'm too scared to taste it for myself.")));
		addRandomlySelectable("murder_rain", defaultWeight(isInTitle(PULSE)),
				new NodeBuilder(defaultKeyMsg("You don't want to know what it's like to be outside when it rains. You can't tell who's a murderer or who forgot an umbrella!")));
		addRandomlySelectable("swimming", defaultWeight(all(isInTitle(PULSE), isEntityType(IGUANA, TURTLE))),
				new NodeBuilder(defaultKeyMsg("If you're looking for a good land to swim in, it's definitely not this one.")));
		addRandomlySelectable("blood_surprise", defaultWeight(all(isInTitle(PULSE), isEntityType(IGUANA, NAKAGATOR))),
				new NodeBuilder(defaultKeyMsg("OH GOD IS THAT BLOOD oh wait nevermind.")));
		
		
		//Thunder
		addRandomlySelectable("skeleton_horse", defaultWeight(isInTitle(THUNDER)),
				new NodeBuilder(defaultKeyMsg("Some people say at night, skeletons riding skeleton horses come through the town.")));
		addRandomlySelectable("blue_moon", defaultWeight(all(isInTitle(THUNDER), isEntityType(SALAMANDER, IGUANA))),
				new NodeBuilder(defaultKeyMsg("Every once in a blue moon, lightning strikes and burns down the village. We have to rebuild it!")));
		addRandomlySelectable("lightning_strike", defaultWeight(all(isInTitle(THUNDER), isEntityType(TURTLE))),
				new NodeBuilder(defaultKeyMsg("You don't want to be struck by lightning. No one does.")));
		
		addRandomlySelectable("reckoning.1", defaultWeight(isInTitle(THUNDER)),
				new NodeBuilder(defaultKeyMsg("Those darn doomsayers, preaching about the Apocalypse and The Reckoning and such!"))
						.next("reckoning.2"));
		add("reckoning.2", new NodeBuilder(defaultKeyMsg("What's The Reckoning? It's when meteors from The Veil are sent towards Skaia."))
				.next("reckoning.3"));
		add("reckoning.3", new NodeBuilder(defaultKeyMsg("Like any reasonable %s believes in that!", DialogueMessage.Argument.ENTITY_TYPE)));
		addRandomlySelectable("thunder_death.1", defaultWeight(all(isInTitle(THUNDER), isInTerrain(WOOD))),
				new NodeBuilder(defaultKeyMsg("We're lucky to have rain with this weather."))
						.next(add("thunder_death.2", new NodeBuilder(defaultKeyMsg("Otherwise the thunder would surely have been our death."))
								.addClosingResponse(DOTS))));
		addRandomlySelectable("hardcore", defaultWeight(all(isInTitle(THUNDER), isInTerrain(HEAT))),
				new NodeBuilder(defaultKeyMsg("This land is HARDCORE! There's lava and lightning wherever you go!")));
		addRandomlySelectable("thunder_throw.1", defaultWeight(all(isInTitle(THUNDER), isEntityType(TURTLE, SALAMANDER))),
				new NodeBuilder(defaultKeyMsg("Nemesis has been throwing thunder for generations, not stopping for even a moment."))
						.next(add("thunder_throw.2", new NodeBuilder(defaultKeyMsg("They are even doing it in their sleep. Can you believe that?"))
								.addClosingResponse(DOTS))));
		
		
		//Rabbits
		addRandomlySelectable("bunny_birthday", defaultWeight(all(isInTitle(RABBITS), isEntityType(NAKAGATOR, SALAMANDER))),
				new NodeBuilder(defaultKeyMsg("Our daughter wants a bunny for her birthday, even though she caught six in the past three hours.")));
		addRandomlySelectable("rabbit_eating", defaultWeight(all(isInTitle(RABBITS), isEntityType(TURTLE, SALAMANDER))),
				new NodeBuilder(defaultKeyMsg("One time our village ran out of food and we tried eating rabbits. It was a dark period in our village history.")));
		addRandomlySelectable("edgy_life_hatred", defaultWeight(all(isInTitle(RABBITS), isEntityType(IGUANA, NAKAGATOR))),
				new NodeBuilder(defaultKeyMsg("This place is just so full of life! I despise it.")));
		addRandomlySelectable("rabbit.food_shortage.1", defaultWeight(all(isInTitle(RABBITS), isInTerrainLand(MSTags.TerrainLandTypes.IS_DESOLATE))),
				new NodeBuilder(defaultKeyMsg("This land is already pretty desolate. There being lots of rabbits eating everything they find doesn't help!"))
						.addResponse(new ResponseBuilder(ARROW).condition(isInTerrainLand(MSTags.TerrainLandTypes.ROCK)).nextDialogue("rabbit.food_shortage.2")));
		add("rabbit.food_shortage.2", new NodeBuilder(defaultKeyMsg("But with that many rabbits around, there sure are other ways of getting food..."))
				.addClosingResponse(DOTS));
		addRandomlySelectable("rabbit.food.1", defaultWeight(all(isInTitle(RABBITS),
						any(isInTerrainLand(MSTags.TerrainLandTypes.IS_DESOLATE), isInTerrain(FUNGI), isInTerrain(SHADE)))),
				new NodeBuilder(defaultKeyMsg("I sure wonder where the rabbits are getting their food from."))
						.next("rabbit.food.2"));
		add("rabbit.food.2", new NodeSelectorBuilder()
				.node(any(isInTerrain(FUNGI), isInTerrain(SHADE)),
						new NodeBuilder(subMsg("b", "I mean, there's not really much else than mushrooms around here.")))
				.defaultNode(new NodeBuilder(subMsg("a", "There's not really much food to be found in this desolate place."))
						.addResponse(new ResponseBuilder(ARROW).condition(isInTerrainLand(MSTags.TerrainLandTypes.SAND))
								.nextDialogue(add("rabbit.food.3", new NodeBuilder(defaultKeyMsg("Except maybe cacti, but would rabbits eat something that prickly?")))))));
		
		
		//Monsters
		addRandomlySelectable("spider_raid", defaultWeight(isInTitle(MONSTERS)),
				new NodeBuilder(defaultKeyMsg("A few giant spiders raided our village last night, taking all of our bugs! Those monsters...")));
		addRandomlySelectable("monstersona", defaultWeight(all(isInTitle(MONSTERS), isEntityType(IGUANA, NAKAGATOR))),
				new NodeBuilder(defaultKeyMsg("What's your monster-sona? Mine is a zombie.")));
		
		
		//Towers
		addRandomlySelectable("bug_treasure", defaultWeight(all(isInTitle(TOWERS), isEntityType(SALAMANDER, IGUANA))),
				new NodeBuilder(defaultKeyMsg("Legends say underneath the tower to the north is a Captain Lizardtail's buried treasure! Literal tons of bugs, they say!")));
		addRandomlySelectable("no_tower_treasure", defaultWeight(all(isInTitle(TOWERS), isEntityType(IGUANA, NAKAGATOR))),
				new NodeBuilder(defaultKeyMsg("I feel ripped off. I was born in a land full of magical towers but none of them have treasure!")));
		
		
		//Thought
		addRandomlySelectable("glass_books", defaultWeight(all(isInTitle(THOUGHT), isEntityType(TURTLE, IGUANA))),
				new NodeBuilder(defaultKeyMsg("Our smartest villager read all the books in the library and now knows how to make glass jars! He's a gift from the big frog above!")));
		addRandomlySelectable("book_food", defaultWeight(all(isInTitle(THOUGHT), isEntityType(SALAMANDER, NAKAGATOR))),
				new NodeBuilder(defaultKeyMsg("We ate all the books in the nearby college ruins. It turns out thousand-year-old leather doesn't make the best dinner.")));
		addRandomlySelectable("to_eat", defaultWeight(all(isInTitle(THOUGHT), isEntityType(IGUANA, NAKAGATOR))),
				new NodeBuilder(defaultKeyMsg("To eat or not to eat, that is the question.")));
		
		
		//Cake
		addRandomlySelectable("mystery_recipe", defaultWeight(all(isInTitle(CAKE), isEntityType(TURTLE, NAKAGATOR))),
				new NodeBuilder(defaultKeyMsg("All of the villagers here are trying to crack the mystery of how to make the frosted bread we see all day on our walks.")));
		addRandomlySelectable("cake_regen", defaultWeight(all(isInTitle(CAKE), isEntityType(TURTLE, SALAMANDER))),
				new NodeBuilder(defaultKeyMsg("I heard all the cakes magically regenerate if you don't completely eat them! That's completely stupid!")));
		addRandomlySelectable("cake_recipe", defaultWeight(all(isInTitle(CAKE), isEntityType(IGUANA, SALAMANDER))),
				new NodeBuilder(defaultKeyMsg("Let's see, the recipe calls for 5 tbsp. of sugar, 2 tbsp. vanilla, 1 large grasshopper... what are you looking at?")));
		addRandomlySelectable("fire_cakes", defaultWeight(all(isInTitle(CAKE), isInTerrain(HEAT))),
				new NodeBuilder(defaultKeyMsg("If you're not careful, anything can set you on fire here, even the cakes!")));
		addRandomlySelectable("frosting", defaultWeight(all(isInTitle(CAKE), isInTerrain(FROST))),
				new NodeBuilder(defaultKeyMsg("When we start talking about cakes, the others start mentioning frosting. I'm not sure I get what they're talking about!")));
		
		
		//Clockwork
		addRandomlySelectable("gear_technology", defaultWeight(all(isInTitle(CLOCKWORK), isEntityType(SALAMANDER, IGUANA))),
				new NodeBuilder(defaultKeyMsg("Legends say the giant gears were used for technology no consort has ever seen before. That's absurd! It's obviously food!")));
		addRandomlySelectable("evil_gears", defaultWeight(all(isInTitle(CLOCKWORK), isEntityType(NAKAGATOR, IGUANA))),
				new NodeBuilder(defaultKeyMsg("My neighbor says the gears are evil! He also said that swords are used for combat, so he's probably insane.")));
		addRandomlySelectable("ticking", defaultWeight(all(isInTitle(CLOCKWORK), isEntityType(TURTLE, SALAMANDER))),
				new NodeBuilder(defaultKeyMsg("The ticking keeps me up all night. It keeps us all up all night. Save us.")));
		
		
		//Frogs
		addRandomlySelectable("frog_creation", defaultWeight(all(isInTitle(FROGS))),
				new NodeBuilder(defaultKeyMsg("We are thankful for all the frogs that They gave to us when the universe was created. They, of course, is the genesis frog. I feel bad for the fool who has to make another!")));
		addRandomlySelectable("frog_location", defaultWeight(all(isInTitle(FROGS))),
				new NodeBuilder(defaultKeyMsg("You won't find many frogs where you find villages. Most of them live where the terrain is rougher.")));
		addRandomlySelectable("frog_imitation", defaultWeight(all(isInTitle(FROGS))),
				new NodeBuilder(defaultKeyMsg("Ribbit, ribbit! I'm a frog! I don't care what you say!")));
		addRandomlySelectable("frog_variants.1", defaultWeight(isInTitle(FROGS)),
				new NodeBuilder(defaultKeyMsg("Most people believe there aren't that many types of frogs. 4740, maybe? Anything beyond that would be preposterous.", DialogueMessage.Argument.LAND_NAME))
						.addResponse(new ResponseBuilder(ARROW).nextDialogue("frog_variants.2")));
		add("frog_variants.2", new NodeBuilder(defaultKeyMsg("Here in %s, however, we know that there are 9.444731276889531e+22 types of frogs."))
				.addClosingResponse(DOTS));
		
		
		addRandomlySelectable("mycelium.1", defaultWeight(isInTerrain(FUNGI)),
				new NodeBuilder(defaultKeyMsg("Frog, don't you love the feeling of mycelium on your toes?"))
						.next(add("mycelium.2", new NodeBuilder(defaultKeyMsg("No? Is that just me?")))));
		
		addRandomlySelectable("camel/start", defaultWeight(isInTerrainLand(MSTags.TerrainLandTypes.SAND)), new NodeBuilder(defaultKeyMsg())
				.addResponse(new ResponseBuilder(msg("minestuck.dialogue.camel.yes", "Why not? Seems like a good price for a camel!"))
						.nextDialogue(add("camel/no_camel", new NodeBuilder(defaultKeyMsg("Hahaha! Sucker! I have no camel! Cya later! 8)")))))
				.addResponse(new ResponseBuilder(msg("minestuck.dialogue.camel.no", "Of course not! You know better!"))
						.nextDialogue(add("camel/dancing_camel", new NodeBuilder(defaultKeyMsg("Are you sure? Too bad! The camel knew how to dance, too!"))))));
		
		//TODO stop the shops from being selectable at end of testing
		addRandomlySelectable("food_shop", defaultWeight(isEntityType(SALAMANDER)),
				new NodeBuilder(defaultKeyMsg("You hungry? I bet you are! Why else would you be talking to me?"))
						.addClosingResponse(subMsg("bye", "Never mind"))
						.addResponse(new ResponseBuilder(subMsg("next", "What do you have?"))
								.addTrigger(new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_FOOD_STOCK, EnumConsort.MerchantType.FOOD))));
		addRandomlySelectable("fast_food", defaultWeight(isEntityType(NAKAGATOR)),
				new NodeBuilder(defaultKeyMsg("Welcome to MacCricket's, what would you like?"))
						.addClosingResponse(subMsg("bye", "I'm good"))
						.addResponse(new ResponseBuilder(subMsg("next", "Show me your menu"))
								.addTrigger(new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_FOOD_STOCK, EnumConsort.MerchantType.FOOD))));
		addRandomlySelectable("grocery_store", defaultWeight(isEntityType(IGUANA)),
				new NodeBuilder(defaultKeyMsg("Thank you for choosing Stop and Hop, this village's #1 one grocer!"))
						.addClosingResponse(subMsg("bye", "No thanks"))
						.addResponse(new ResponseBuilder(subMsg("next", "What do you have to sell?"))
								.addTrigger(new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_FOOD_STOCK, EnumConsort.MerchantType.FOOD))));
		addRandomlySelectable("tasty_welcome", defaultWeight(isEntityType(TURTLE)),
				new NodeBuilder(defaultKeyMsg("Welcome. I hope you find something tasty among our wares."))
						.addClosingResponse(subMsg("bye", "Goodbye"))
						.addResponse(new ResponseBuilder(subMsg("next", "Let me see your wares"))
								.addTrigger(new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_FOOD_STOCK, EnumConsort.MerchantType.FOOD))));
		
		addRandomlySelectable("immortality_herb.1", defaultWeight(isInTerrain(FLORA)),
				new NodeBuilder(defaultKeyMsg("I have a herb that grants immortality! I'm going to eat it right now!"))
						.next("immortality_herb.2"));
		add("immortality_herb.2", new NodeBuilder(defaultKeyMsg("However, they are easily confused with an explosion-causing herb..."))
				.next(add("immortality_herb.3", new NodeBuilder(defaultKeyMsg("I'm taking the risk."))
						.addResponse(new ResponseBuilder(DOTS).addTrigger(new Trigger.Explode())))));
	}
	
	private void testDialogues()
	{
		addRandomlySelectable("test1", defaultWeight(Condition.AlwaysTrue.INSTANCE), new NodeBuilder(defaultKeyMsg("Press §eSHIFT§r for more info"))
				.animation("test1animation")
				.addResponse(new ResponseBuilder(msg("test1response1")).nextDialogue("test2")
						.condition(any(isEntityType(TURTLE), isEntityType(IGUANA))))
				.addResponse(new ResponseBuilder(msg("test1response2")).nextDialogue("test2").condition(isEntityType(NAKAGATOR)))
				.addResponse(new ResponseBuilder(msg("test1response3")).nextDialogue("test2").addTrigger(new Trigger.Command("summon minestuck:grist ~ ~ ~ {Value:200}")))
				.addResponse(new ResponseBuilder(msg("test1response4"))
						.visibleCondition(subText("fail", "This very custom condition was not met."), one(
								one(isEntityType(NAKAGATOR), isEntityType(TURTLE), isEntityType(IGUANA), isEntityType(SALAMANDER)),
								one(new Condition.IsCarapacian(), new Condition.PlayerHasItem(MSItems.ACE_OF_CLUBS.get(), 1))
						))));
		
		addRandomlySelectable("test2", defaultWeight(Condition.AlwaysTrue.INSTANCE), new NodeBuilder(defaultKeyMsg())
				.animation("test2animation")
				.addResponse(new ResponseBuilder(msg("test2response1")).nextDialogue("test1")
						.visibleCondition(isEntityType(SALAMANDER)))
				.addResponse(new ResponseBuilder(msg("test2response2")).nextDialogue("test1")
						.visibleCondition(none(new Condition.IsCarapacian(), isInTerrain(END), isInTerrain(SHADE)))
						.addTrigger(new Trigger.Command("say hi")))
				.addResponse(new ResponseBuilder(msg("test2response3")).nextDialogue("test1")
						.addTrigger(new Trigger.Command("""
								tellraw @a ["",{"text":"Welcome","color":"aqua"},{"text":" to "},{"text":"Minecraft","color":"#9B9B17"},{"text":" Tools "},{"text":"partner.","obfuscated":true},{"text":" "},{"selector":"@s"},{"text":" fs"}]"""))
				)
				.addResponse(new ResponseBuilder(msg("test2response4"))
						.visibleCondition(one(new Condition.IsCarapacian(), new Condition.PlayerIsClass(EnumClass.WITCH), new Condition.PlayerIsClass(EnumClass.MAGE),
								new Condition.PlayerIsAspect(EnumAspect.HEART), new Condition.PlayerIsAspect(EnumAspect.DOOM), isInTerrain(RAIN)))));
		addRandomlySelectable("turtle_only", defaultWeight(isEntityType(TURTLE)), new NodeBuilder(defaultKeyMsg()));
		
		addRandomlySelectable("nakagator_only", defaultWeight(isEntityType(NAKAGATOR)), new NodeBuilder(defaultKeyMsg()));
		
		addRandomlySelectable("me_want_cookie", defaultWeight(Condition.AlwaysTrue.INSTANCE), new NodeBuilder(defaultKeyMsg())
				.addClosingResponse(subMsg("no", "im sorry fellow, I have no cookie for you. Bye"))
				.addResponse(new ResponseBuilder(subMsg("why", "why do you want cookie?")).loop())
				.addResponse(new ResponseBuilder(subMsg("give", "here have a cookie chap")).nextDialogue("oh_yippee")
						.visibleCondition(new Condition.PlayerHasItem(Items.COOKIE, 1))
						.addTrigger(new Trigger.TakeItem(Items.COOKIE))
						.addTrigger(new Trigger.SetDialogue(new ResourceLocation(Minestuck.MOD_ID, "hunger_filled")))));
		add("oh_yippee", new NodeBuilder(defaultKeyMsg()));
		add("hunger_filled", new NodeBuilder(defaultKeyMsg()));
		
		addRandomlySelectable("me_want_5_cookies", weighted(5, Condition.AlwaysTrue.INSTANCE), new NodeBuilder(defaultKeyMsg())
				.addClosingResponse(subMsg("no", "im sorry fellow, I have no cookie for you. Bye"))
				.addResponse(new ResponseBuilder(subMsg("give", "here have 5 cookies chap")).nextDialogue("oh_yippee")
						.visibleCondition(new Condition.PlayerHasItem(Items.COOKIE, 5))
						.addTrigger(new Trigger.TakeItem(Items.COOKIE, 5))));
		
		addRandomlySelectable("hi_friend_can_i_help_you", weighted(11, Condition.AlwaysTrue.INSTANCE), new NodeBuilder(defaultKeyMsg())
				.addResponse(new ResponseBuilder(subMsg("hate", "I hate you")).addTrigger(new Trigger.AddConsortReputation(-100)))
				.addResponse(new ResponseBuilder(subMsg("love", "I love you")).addTrigger(new Trigger.AddConsortReputation(100)))
				.addResponse(new ResponseBuilder(subMsg("high_rep", "Rep above 500")).visibleCondition(new Condition.PlayerHasReputation(500, true)))
				.addResponse(new ResponseBuilder(subMsg("low_rep", "Rep below 200")).visibleCondition(new Condition.PlayerHasReputation(200, false)))
				.addClosingResponse(subMsg("bye", "bye")));
		
		addRandomlySelectable("test_arguments", defaultWeight(Condition.AlwaysTrue.INSTANCE), new NodeBuilder(defaultKeyMsg("Player name land: %s", DialogueMessage.Argument.PLAYER_NAME_LAND))
				.addResponse(new ResponseBuilder(subMsg("name", "Player name land: %s", DialogueMessage.Argument.PLAYER_NAME_LAND))));
		
		addRandomlySelectable("look_rich", defaultWeight(Condition.AlwaysTrue.INSTANCE), new NodeSelectorBuilder()
				.node(new Condition.PlayerHasBoondollars(10_000, true), new NodeBuilder(subMsg("rich", "Hey, looks like you have a lot of boons!")))
				.node(new Condition.PlayerHasBoondollars(10, false), new NodeBuilder(subMsg("poor", "Wow, you barely have any boons. Poor you.")))
				.defaultNode(new NodeBuilder(defaultKeyMsg("Hi! I can sense if someone has a lot of boondollars."))));
	}
}
