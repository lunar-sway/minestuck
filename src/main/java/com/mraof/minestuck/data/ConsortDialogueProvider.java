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
		addRandomlySelectable("dad_wind", defaultWeight(isInLand(WIND.get())),
				new NodeBuilder(defaultKeyMsg("My dad was blown away in one of the recent wind storms.")));
		
		addRandomlySelectable("pyre.1", defaultWeight(all(isInLand(WIND.get()), isAnyEntityType(SALAMANDER.get(), TURTLE.get()))),
				new NodeBuilder(defaultKeyMsg("If only I was faster than the wind! That would be fun!"))
						.next(add("pyre.2", new NodeBuilder(defaultKeyMsg("Actually, nevermind. I would be burned on a pyre for being a witch due to our primal society.")))));
		
		//Pulse
		addRandomlySelectable("koolaid", defaultWeight(all(isInLand(PULSE.get()), isAnyEntityType(SALAMANDER.get(), TURTLE.get()))),
				new NodeBuilder(defaultKeyMsg("Some people say the oceans of blood are actually kool-aid. I'm too scared to taste it for myself.")));
		addRandomlySelectable("murder_rain", defaultWeight(isInLand(PULSE.get())),
				new NodeBuilder(defaultKeyMsg("You don't want to know what it's like to be outside when it rains. You can't tell who's a murderer or who forgot an umbrella!")));
		addRandomlySelectable("swimming", defaultWeight(all(isInLand(PULSE.get()), isAnyEntityType(IGUANA.get(), TURTLE.get()))),
				new NodeBuilder(defaultKeyMsg("If you're looking for a good land to swim in, it's definitely not this one.")));
		addRandomlySelectable("blood_surprise", defaultWeight(all(isInLand(PULSE.get()), isAnyEntityType(IGUANA.get(), NAKAGATOR.get()))),
				new NodeBuilder(defaultKeyMsg("OH GOD IS THAT BLOOD oh wait nevermind.")));
		
		//Thunder
		addRandomlySelectable("skeleton_horse", defaultWeight(isInLand(THUNDER.get())),
				new NodeBuilder(defaultKeyMsg("Some people say at night, skeletons riding skeleton horses come through the town.")));
		addRandomlySelectable("blue_moon", defaultWeight(all(isInLand(THUNDER.get()), isAnyEntityType(SALAMANDER.get(), IGUANA.get()))),
				new NodeBuilder(defaultKeyMsg("Every once in a blue moon, lightning strikes and burns down the village. We have to rebuild it!")));
		addRandomlySelectable("lightning_strike", defaultWeight(all(isInLand(THUNDER.get()), isAnyEntityType(TURTLE.get()))),
				new NodeBuilder(defaultKeyMsg("You don't want to be struck by lightning. No one does.")));
		
		addRandomlySelectable("reckoning.1", defaultWeight(isInLand(THUNDER.get())),
				new NodeBuilder(defaultKeyMsg("Those darn doomsayers, preaching about the Apocalypse and The Reckoning and such!"))
						.next(add("reckoning.2", new NodeBuilder(defaultKeyMsg("What's The Reckoning? It's when meteors from The Veil are sent towards Skaia."))
								.next(add("reckoning.3", new NodeBuilder(defaultKeyMsg("Like any reasonable %s believes in that!", DialogueMessage.Argument.ENTITY_TYPE)))))));
		
		addRandomlySelectable("thunder_death.1", defaultWeight(all(isInLand(THUNDER.get()), isInLand(WOOD.get()))),
				new NodeBuilder(defaultKeyMsg("We're lucky to have rain with this weather."))
						.next(add("thunder_death.2", new NodeBuilder(defaultKeyMsg("Otherwise the thunder would surely have been our death."))
								.addClosingResponse(DOTS))));
		
		addRandomlySelectable("hardcore", defaultWeight(all(isInLand(THUNDER.get()), isInLand(HEAT.get()))),
				new NodeBuilder(defaultKeyMsg("This land is HARDCORE! There's lava and lightning wherever you go!")));
		
		addRandomlySelectable("thunder_throw.1", defaultWeight(all(isInLand(THUNDER.get()), isAnyEntityType(TURTLE.get(), SALAMANDER.get()))),
				new NodeBuilder(defaultKeyMsg("Nemesis has been throwing thunder for generations, not stopping for even a moment."))
						.next(add("thunder_throw.2", new NodeBuilder(defaultKeyMsg("They are even doing it in their sleep. Can you believe that?"))
								.addClosingResponse(DOTS))));
		
		//Rabbits
		addRandomlySelectable("bunny_birthday", defaultWeight(all(isInLand(RABBITS.get()), isAnyEntityType(NAKAGATOR.get(), SALAMANDER.get()))),
				new NodeBuilder(defaultKeyMsg("Our daughter wants a bunny for her birthday, even though she caught six in the past three hours.")));
		addRandomlySelectable("rabbit_eating", defaultWeight(all(isInLand(RABBITS.get()), isAnyEntityType(TURTLE.get(), SALAMANDER.get()))),
				new NodeBuilder(defaultKeyMsg("One time our village ran out of food and we tried eating rabbits. It was a dark period in our village history.")));
		addRandomlySelectable("edgy_life_hatred", defaultWeight(all(isInLand(RABBITS.get()), isAnyEntityType(IGUANA.get(), NAKAGATOR.get()))),
				new NodeBuilder(defaultKeyMsg("This place is just so full of life! I despise it.")));
		
		addRandomlySelectable("rabbit.food_shortage.1", defaultWeight(all(isInLand(RABBITS.get()), isInTerrainLand(MSTags.TerrainLandTypes.IS_DESOLATE))),
				new NodeBuilder(defaultKeyMsg("This land is already pretty desolate. There being lots of rabbits eating everything they find doesn't help!"))
						.addResponse(new ResponseBuilder(ARROW).condition(isInTerrainLand(MSTags.TerrainLandTypes.ROCK))
								.nextDialogue(add("rabbit.food_shortage.2", new NodeBuilder(defaultKeyMsg("But with that many rabbits around, there sure are other ways of getting food..."))
										.addClosingResponse(DOTS)))));
		
		addRandomlySelectable("rabbit.food.1", weighted(100, all(isInLand(RABBITS.get()),
						any(isInTerrainLand(MSTags.TerrainLandTypes.IS_DESOLATE), isInLand(FUNGI.get()), isInLand(SHADE.get())))),
				new NodeBuilder(defaultKeyMsg("I sure wonder where the rabbits are getting their food from."))
						.next("rabbit.food.2"));
		add("rabbit.food.2", new NodeSelectorBuilder()
				.node(any(isInLand(FUNGI.get()), isInLand(SHADE.get())),
						new NodeBuilder(subMsg("b", "I mean, there's not really much else than mushrooms around here.")))
				.defaultNode(new NodeBuilder(subMsg("a", "There's not really much food to be found in this desolate place."))
						.addResponse(new ResponseBuilder(ARROW).condition(isInTerrainLand(MSTags.TerrainLandTypes.SAND))
								.nextDialogue(add("rabbit.food.3", new NodeBuilder(defaultKeyMsg("Except maybe cacti, but would rabbits eat something that prickly?")))))));
		
		//Monsters
		addRandomlySelectable("pet_zombie", defaultWeight(all(isInTitleLand(MSTags.TitleLandTypes.MONSTERS), isAnyEntityType(NAKAGATOR.get(), SALAMANDER.get()))),
				new NodeBuilder(defaultKeyMsg("I've heard moaning coming from our son's bedroom. I found out he's keeping a pet zombie in there! Tamed it and everything!")));
		addRandomlySelectable("spider_raid", defaultWeight(isInLand(MONSTERS.get())),
				new NodeBuilder(defaultKeyMsg("A few giant spiders raided our village last night, taking all of our bugs! Those monsters...")));
		addRandomlySelectable("monstersona", defaultWeight(all(isInTitleLand(MSTags.TitleLandTypes.MONSTERS), isAnyEntityType(IGUANA.get(), NAKAGATOR.get()))),
				new NodeBuilder(defaultKeyMsg("What's your monster-sona? Mine is a zombie.")));
		
		//Towers
		addRandomlySelectable("bug_treasure", defaultWeight(all(isInLand(TOWERS.get()), isAnyEntityType(SALAMANDER.get(), IGUANA.get()))),
				new NodeBuilder(defaultKeyMsg("Legends say underneath the tower to the north is a Captain Lizardtail's buried treasure! Literal tons of bugs, they say!")));
		addRandomlySelectable("tower_gone", defaultWeight(all(isInLand(TOWERS.get()), isAnyEntityType(TURTLE.get(), SALAMANDER.get()))),
				new NodeBuilder(defaultKeyMsg("That tower over there was built by my great grandpa Fjorgenheimer! You can tell by how its about to fall apa- oh it fell apart.")));
		addRandomlySelectable("no_tower_treasure", defaultWeight(all(isInLand(TOWERS.get()), isAnyEntityType(IGUANA.get(), NAKAGATOR.get()))),
				new NodeBuilder(defaultKeyMsg("I feel ripped off. I was born in a land full of magical towers but none of them have treasure!")));
		
		//Thought
		addRandomlySelectable("glass_books", defaultWeight(all(isInLand(THOUGHT.get()), isAnyEntityType(TURTLE.get(), IGUANA.get()))),
				new NodeBuilder(defaultKeyMsg("Our smartest villager read all the books in the library and now knows how to make glass jars! He's a gift from the big frog above!")));
		addRandomlySelectable("book_food", defaultWeight(all(isInLand(THOUGHT.get()), isAnyEntityType(SALAMANDER.get(), NAKAGATOR.get()))),
				new NodeBuilder(defaultKeyMsg("We ate all the books in the nearby college ruins. It turns out thousand-year-old leather doesn't make the best dinner.")));
		addRandomlySelectable("to_eat", defaultWeight(all(isInLand(THOUGHT.get()), isAnyEntityType(IGUANA.get(), NAKAGATOR.get()))),
				new NodeBuilder(defaultKeyMsg("To eat or not to eat, that is the question.")));
		
		//Cake
		addRandomlySelectable("mystery_recipe", defaultWeight(all(isInLand(CAKE.get()), isAnyEntityType(TURTLE.get(), NAKAGATOR.get()))),
				new NodeBuilder(defaultKeyMsg("All of the villagers here are trying to crack the mystery of how to make the frosted bread we see all day on our walks.")));
		addRandomlySelectable("cake_regen", defaultWeight(all(isInLand(CAKE.get()), isAnyEntityType(TURTLE.get(), SALAMANDER.get()))),
				new NodeBuilder(defaultKeyMsg("I heard all the cakes magically regenerate if you don't completely eat them! That's completely stupid!")));
		addRandomlySelectable("cake_recipe", defaultWeight(all(isInLand(CAKE.get()), isAnyEntityType(IGUANA.get(), SALAMANDER.get()))),
				new NodeBuilder(defaultKeyMsg("Let's see, the recipe calls for 5 tbsp. of sugar, 2 tbsp. vanilla, 1 large grasshopper... what are you looking at?")));
		addRandomlySelectable("fire_cakes", defaultWeight(all(isInLand(CAKE.get()), isInLand(HEAT.get()))),
				new NodeBuilder(defaultKeyMsg("If you're not careful, anything can set you on fire here, even the cakes!")));
		addRandomlySelectable("frosting", defaultWeight(all(isInLand(CAKE.get()), isInLand(FROST.get()))),
				new NodeBuilder(defaultKeyMsg("When we start talking about cakes, the others start mentioning frosting. I'm not sure I get what they're talking about!")));
		
		addRandomlySelectable("gear_technology", defaultWeight(all(isInLand(CLOCKWORK.get()), isAnyEntityType(SALAMANDER.get(), IGUANA.get()))),
				new NodeBuilder(defaultKeyMsg("Legends say the giant gears were used for technology no consort has ever seen before. That's absurd! It's obviously food!")));
		addRandomlySelectable("evil_gears", defaultWeight(all(isInLand(CLOCKWORK.get()), isAnyEntityType(NAKAGATOR.get(), IGUANA.get()))),
				new NodeBuilder(defaultKeyMsg("My neighbor says the gears are evil! He also said that swords are used for combat, so he's probably insane.")));
		addRandomlySelectable("ticking", defaultWeight(all(isInLand(CLOCKWORK.get()), isAnyEntityType(TURTLE.get(), SALAMANDER.get()))),
				new NodeBuilder(defaultKeyMsg("The ticking keeps me up all night. It keeps us all up all night. Save us.")));
		
		//Frogs
		addRandomlySelectable("frog_creation", defaultWeight(isInLand(FROGS.get())),
				new NodeBuilder(defaultKeyMsg("We are thankful for all the frogs that They gave to us when the universe was created. They, of course, is the genesis frog. I feel bad for the fool who has to make another!")));
		addRandomlySelectable("frog_location", defaultWeight(isInLand(FROGS.get())),
				new NodeBuilder(defaultKeyMsg("You won't find many frogs where you find villages. Most of them live where the terrain is rougher.")));
		addRandomlySelectable("frog_imitation", defaultWeight(isInLand(FROGS.get())),
				new NodeBuilder(defaultKeyMsg("Ribbit, ribbit! I'm a frog! I don't care what you say!")));
		addRandomlySelectable("frog_variants.1", defaultWeight(isInLand(FROGS.get())),
				new NodeBuilder(defaultKeyMsg("Most people believe there aren't that many types of frogs. 4740, maybe? Anything beyond that would be proposterous."))
						.next(add("frog_variants.2", new NodeBuilder(defaultKeyMsg("Here in %s, however, we know that there are 9.444731276889531e+22 types of frogs.", DialogueMessage.Argument.LAND_NAME)))));
		addRandomlySelectable("frog_hatred", defaultWeight(isInLand(FROGS.get())),
				new NodeBuilder(defaultKeyMsg("For whatever reason, residents of Derse HATE frogs! Why would someone hate frogs?")));
		addRandomlySelectable("grasshopper_fishing.1", defaultWeight(all(isInLand(FROGS.get()), isAnyEntityType(SALAMANDER.get(), IGUANA.get()))),
				new NodeBuilder(defaultKeyMsg("My brother found a magic grasshopper while fishing recently!"))
						.next(add("grasshopper_fishing.2", new NodeBuilder(defaultKeyMsg("Usually all we find are rings!")))));
		addRandomlySelectable("gay_frogs", defaultWeight(all(isInLand(FROGS.get()), isInLand(RAINBOW.get()))),
				new NodeBuilder(defaultKeyMsg("The frogs around here are all so gay! Look at them happily hopping about!")));
		addRandomlySelectable("non_teleporting_frogs", defaultWeight(all(isInLand(FROGS.get()), isInLand(END.get()))),
				new NodeBuilder(defaultKeyMsg("While the rest of us are getting dizzy, teleporting at random in the tall grass, the frogs seem immune! Makes it harder to catch them, that's for sure.")));
		
		//Buckets
		addRandomlySelectable("lewd_buckets", defaultWeight(isInLand(BUCKETS.get())),
				new NodeBuilder(defaultKeyMsg("Some may call our land lewd, but the buckets are just so fun to swim in!")));
		addRandomlySelectable("water_buckets", defaultWeight(all(isInLand(BUCKETS.get()), isInTerrainLand(MSTags.TerrainLandTypes.SAND))),
				new NodeBuilder(defaultKeyMsg("The buckets are a great source of water, as long as you pick the ones with water...")));
		addRandomlySelectable("warm_buckets", defaultWeight(all(isInLand(BUCKETS.get()), isInLand(FROST.get()))),
				new NodeBuilder(defaultKeyMsg("Did you know that some buckets provide warmth? I tend to curl up next to one from time to time.")));
		addRandomlySelectable("oil_buckets.1", defaultWeight(all(isInLand(BUCKETS.get()), isInLand(SHADE.get()))),
				new NodeBuilder(defaultKeyMsg("Did you know that the buckets sometimes hold something other than oil?"))
						.next(add("oil_buckets.2", new NodeBuilder(defaultKeyMsg("In some cases, they even contain something drinkable!")))));
		
		//Light
		addRandomlySelectable("blindness", defaultWeight(isInLand(LIGHT.get())),
				new NodeBuilder(defaultKeyMsg("God, it's bright. Half of our village is blind. It's beginning to become a serious problem.")));
		addRandomlySelectable("doctors_inside", defaultWeight(all(isInLand(LIGHT.get()), isAnyEntityType(TURTLE.get()))),
				new NodeBuilder(defaultKeyMsg("Our best village doctors found that staying outside in the blinding light for too long is not good for us. Most of us stay inside all our lives. It's sad.")));
		addRandomlySelectable("staring", defaultWeight(isInLand(LIGHT.get())),
				new NodeBuilder(defaultKeyMsg("Are you staring at me? No, really! I can't see because I'm blind.")));
		addRandomlySelectable("sunglasses.1", defaultWeight(all(isInLand(LIGHT.get()), isInLand(HEAT.get()))),
				new NodeBuilder(defaultKeyMsg("You'd better wear sunglasses, else you might not see where you're going."))
						.next(add("sunglasses.2", new NodeBuilder(defaultKeyMsg("This is not the best place to wander blindly in.")))));
		addRandomlySelectable("bright_snow.1", defaultWeight(all(isInLand(LIGHT.get()), isInLand(FROST.get()))),
				new NodeBuilder(defaultKeyMsg("You would think that the light would melt more snow."))
						.next(add("bright_snow.2", new NodeBuilder(defaultKeyMsg("But nope, the snow stays as frozen as ever!")))));
		addRandomlySelectable("glimmering_snow", defaultWeight(all(isInLand(LIGHT.get()), isInLand(FROST.get()))),
				new NodeBuilder(defaultKeyMsg("Isn't it wonderful how much the snow is glimmering in the light?")));
		addRandomlySelectable("glimmering_sand", defaultWeight(all(isInLand(LIGHT.get()), isInTerrainLand(MSTags.TerrainLandTypes.SAND))),
				new NodeBuilder(defaultKeyMsg("Isn't it wonderful how much the sand is glimmering in the light?")));
		addRandomlySelectable("light_pillars", defaultWeight(all(isInLand(LIGHT.get()), isAnyEntityType(IGUANA.get(), TURTLE.get()))),
				new NodeBuilder(defaultKeyMsg("Those light pillars... they somehow make me think of the legend of the wyrm.")));
		
		//Silence
		addRandomlySelectable("murder_silence", defaultWeight(all(isInLand(SILENCE.get()), isAnyEntityType(NAKAGATOR.get(), SALAMANDER.get()))),
				new NodeBuilder(defaultKeyMsg("This is a great place for murder. No one will hear you scream.")));
		addRandomlySelectable("silent_underlings", defaultWeight(isInLand(SILENCE.get())),
				new NodeBuilder(defaultKeyMsg("This place is so quiet and peaceful. Too bad we can't hear underlings about to kill us.")));
		addRandomlySelectable("listening.1", defaultWeight(all(isInLand(SILENCE.get()), isAnyEntityType(IGUANA.get(), SALAMANDER.get()))),
				new NodeBuilder(defaultKeyMsg("Shhh, they can hear you..."))
						.next(add("listening.2", new NodeBuilder(defaultKeyMsg("Just kidding, no one can hear you! The land itself muffles your words!")))));
		addRandomlySelectable("calmness", defaultWeight(all(isInLand(SILENCE.get()), isAnyEntityType(TURTLE.get(), IGUANA.get()))),
				new NodeBuilder(defaultKeyMsg("The sense of calmness in the air, it's kind of unnerving!")));
		
		//Mixed
		addRandomlySelectable("climb_high", defaultWeight(all(any(isInLand(TOWERS.get()), isInLand(WIND.get())), isAnyEntityType(IGUANA.get()))),
				new NodeBuilder(defaultKeyMsg("Climb up high and you'll be up for a great view!")));
		
		
		
		
		addRandomlySelectable("mycelium.1", defaultWeight(isInLand(FUNGI.get())),
				new NodeBuilder(defaultKeyMsg("Frog, don't you love the feeling of mycelium on your toes?"))
						.next(add("mycelium.2", new NodeBuilder(defaultKeyMsg("No? Is that just me?")))));
		
		addRandomlySelectable("camel/start", defaultWeight(isInTerrainLand(MSTags.TerrainLandTypes.SAND)), new NodeBuilder(defaultKeyMsg())
				.addResponse(new ResponseBuilder(msg("minestuck.dialogue.camel.yes", "Why not? Seems like a good price for a camel!"))
						.nextDialogue(add("camel/no_camel", new NodeBuilder(defaultKeyMsg("Hahaha! Sucker! I have no camel! Cya later! 8)")))))
				.addResponse(new ResponseBuilder(msg("minestuck.dialogue.camel.no", "Of course not! You know better!"))
						.nextDialogue(add("camel/dancing_camel", new NodeBuilder(defaultKeyMsg("Are you sure? Too bad! The camel knew how to dance, too!"))))));
		
		//TODO stop the shops from being selectable at end of testing
		addRandomlySelectable("food_shop", defaultWeight(isAnyEntityType(SALAMANDER.get())),
				new NodeBuilder(defaultKeyMsg("You hungry? I bet you are! Why else would you be talking to me?"))
						.addClosingResponse(subMsg("bye", "Never mind"))
						.addResponse(new ResponseBuilder(subMsg("next", "What do you have?"))
								.addTrigger(new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_FOOD_STOCK, EnumConsort.MerchantType.FOOD))));
		addRandomlySelectable("fast_food", defaultWeight(isAnyEntityType(NAKAGATOR.get())),
				new NodeBuilder(defaultKeyMsg("Welcome to MacCricket's, what would you like?"))
						.addClosingResponse(subMsg("bye", "I'm good"))
						.addResponse(new ResponseBuilder(subMsg("next", "Show me your menu"))
								.addTrigger(new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_FOOD_STOCK, EnumConsort.MerchantType.FOOD))));
		addRandomlySelectable("grocery_store", defaultWeight(isAnyEntityType(IGUANA.get())),
				new NodeBuilder(defaultKeyMsg("Thank you for choosing Stop and Hop, this village's #1 one grocer!"))
						.addClosingResponse(subMsg("bye", "No thanks"))
						.addResponse(new ResponseBuilder(subMsg("next", "What do you have to sell?"))
								.addTrigger(new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_FOOD_STOCK, EnumConsort.MerchantType.FOOD))));
		addRandomlySelectable("tasty_welcome", defaultWeight(isAnyEntityType(TURTLE.get())),
				new NodeBuilder(defaultKeyMsg("Welcome. I hope you find something tasty among our wares."))
						.addClosingResponse(subMsg("bye", "Goodbye"))
						.addResponse(new ResponseBuilder(subMsg("next", "Let me see your wares"))
								.addTrigger(new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_FOOD_STOCK, EnumConsort.MerchantType.FOOD))));
		
		addRandomlySelectable("immortality_herb.1", defaultWeight(isInLand(FLORA.get())),
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
						.condition(any(isAnyEntityType(TURTLE.get()), isAnyEntityType(IGUANA.get()))))
				.addResponse(new ResponseBuilder(msg("test1response2")).nextDialogue("test2").condition(isAnyEntityType(NAKAGATOR.get())))
				.addResponse(new ResponseBuilder(msg("test1response3")).nextDialogue("test2").addTrigger(new Trigger.Command("summon minestuck:grist ~ ~ ~ {Value:200}")))
				.addResponse(new ResponseBuilder(msg("test1response4"))
						.visibleCondition(subText("fail", "This very custom condition was not met."), one(
								one(isAnyEntityType(NAKAGATOR.get()), isAnyEntityType(TURTLE.get()), isAnyEntityType(IGUANA.get()), isAnyEntityType(SALAMANDER.get())),
								one(new Condition.IsCarapacian(), new Condition.PlayerHasItem(MSItems.ACE_OF_CLUBS.get(), 1))
						))));
		
		addRandomlySelectable("test2", defaultWeight(Condition.AlwaysTrue.INSTANCE), new NodeBuilder(defaultKeyMsg())
				.animation("test2animation")
				.addResponse(new ResponseBuilder(msg("test2response1")).nextDialogue("test1")
						.visibleCondition(isAnyEntityType(SALAMANDER.get())))
				.addResponse(new ResponseBuilder(msg("test2response2")).nextDialogue("test1")
						.visibleCondition(none(new Condition.IsCarapacian(), isInLand(END.get()), isInLand(SHADE.get())))
						.addTrigger(new Trigger.Command("say hi")))
				.addResponse(new ResponseBuilder(msg("test2response3")).nextDialogue("test1")
						.addTrigger(new Trigger.Command("""
								tellraw @a ["",{"text":"Welcome","color":"aqua"},{"text":" to "},{"text":"Minecraft","color":"#9B9B17"},{"text":" Tools "},{"text":"partner.","obfuscated":true},{"text":" "},{"selector":"@s"},{"text":" fs"}]"""))
				)
				.addResponse(new ResponseBuilder(msg("test2response4"))
						.visibleCondition(one(new Condition.IsCarapacian(), new Condition.PlayerIsClass(EnumClass.WITCH), new Condition.PlayerIsClass(EnumClass.MAGE),
								new Condition.PlayerIsAspect(EnumAspect.HEART), new Condition.PlayerIsAspect(EnumAspect.DOOM), isInLand(RAIN.get())))));
		addRandomlySelectable("turtle_only", defaultWeight(isAnyEntityType(TURTLE.get())), new NodeBuilder(defaultKeyMsg()));
		
		addRandomlySelectable("nakagator_only", defaultWeight(isAnyEntityType(NAKAGATOR.get())), new NodeBuilder(defaultKeyMsg()));
		
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
		
		addRandomlySelectable("look_rich", weighted(100, Condition.AlwaysTrue.INSTANCE), new NodeSelectorBuilder()
				.node(new Condition.PlayerHasBoondollars(10_000, true), new NodeBuilder(subMsg("rich", "Hey, looks like you have a lot of boons!")))
				.node(new Condition.PlayerHasBoondollars(10, false), new NodeBuilder(subMsg("poor", "Wow, you barely have any boons. Poor you.")))
				.defaultNode(new NodeBuilder(defaultKeyMsg("Hi! I can sense if someone has a lot of boondollars."))));
	}
}
