package com.mraof.minestuck.data.dialogue;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.data.dialogue.DialogueProvider.NodeBuilder;
import com.mraof.minestuck.data.dialogue.DialogueProvider.NodeSelectorBuilder;
import com.mraof.minestuck.data.dialogue.DialogueProvider.ResponseBuilder;
import com.mraof.minestuck.entity.dialogue.DialogueMessage.Argument;
import com.mraof.minestuck.entity.dialogue.RandomlySelectableDialogue;
import com.mraof.minestuck.entity.dialogue.Trigger;
import com.mraof.minestuck.entity.dialogue.condition.Condition;
import com.mraof.minestuck.item.loot.MSLootTables;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

import java.util.List;

import static com.mraof.minestuck.data.dialogue.DialogueLangHelper.msg;
import static com.mraof.minestuck.data.dialogue.DialogueProvider.ARROW;
import static com.mraof.minestuck.data.dialogue.DialogueProvider.DOTS;
import static com.mraof.minestuck.data.dialogue.SelectableDialogueProvider.defaultWeight;
import static com.mraof.minestuck.entity.MSEntityTypes.*;
import static com.mraof.minestuck.entity.dialogue.condition.Conditions.*;
import static com.mraof.minestuck.world.lands.LandTypes.*;

public final class ConsortDialogue
{
	public static DataProvider create(PackOutput output, LanguageProvider enUsLanguageProvider)
	{
		SelectableDialogueProvider provider = new SelectableDialogueProvider(Minestuck.MOD_ID, RandomlySelectableDialogue.DialogueCategory.CONSORT, output);
		DialogueLangHelper l = new DialogueLangHelper(Minestuck.MOD_ID, enUsLanguageProvider);
		
		//Run dialogue creation early so that language stuff gets added before the language provider generates its file
		consortDialogues(provider, l);
		
		return provider;
	}
	
	//todo look over response texts.
	// In the old system, there was one text showing off the option, and another text being what the player actually "replied" with. When moving to this system, only one of the two was used at each case.
	
	//todo some nodes have closing responses and others doesn't. Do we want to work in more closing responses?
	
	//todo we should check if any dialogue would do well to use Trigger.SetPlayerDialogue or responseBuilder.setNextAsEntrypoint() to set a new and player-specific entrypoint
	// (for when dialogue progresses in some way that it shouldn't start from the beginning)
	
	//todo if we want, we can shorten or replace some chain dialogue by showing multiple messages on the same screen (with "nodeBuilder.addMessage()").
	// Check if we want to do that kind of change with any of these!
	
	//todo tweak dialogue weights? perhaps dialogue with more specific conditions should have a higher weight than those with less specific conditions to compensate?
	private static void consortDialogues(SelectableDialogueProvider provider, DialogueLangHelper l)
	{
		//Generic
		var genericThanks = provider.dialogue().add("generic_thanks", new NodeBuilder(l.defaultKeyMsg("Thank you for helping me!")));
		var thanks = provider.dialogue().add("thanks", new NodeBuilder(l.defaultKeyMsg("Thanks!")));
		final DialogueProvider.MessageProducer sadFaceMsg = l.msg("sad_face", ":(");
		var yesMsg = l.msg("yes", "Yes");
		var noMsg = l.msg("no", "No");
		
		//Wind
		provider.addRandomlySelectable("dad_wind", defaultWeight(isInTitleLand(WIND)), //todo review this
				new NodeBuilder(l.defaultKeyMsg("My dad was blown away in one of the recent wind storms.")));
		
		provider.addRandomlySelectable("pyre", defaultWeight(all(isInTitleLand(WIND), isAnyEntityType(SALAMANDER, TURTLE))), new ChainBuilder()
				.node(new NodeBuilder(l.defaultKeyMsg("If only I was faster than the wind! That would be fun!")))
				.node(new NodeBuilder(l.defaultKeyMsg("Actually, nevermind. I would be burned on a pyre for being a witch due to our primal society."))));
		
		
		//Pulse
		provider.addRandomlySelectable("koolaid", defaultWeight(all(isInTitleLand(PULSE), isAnyEntityType(SALAMANDER, TURTLE))),
				new NodeBuilder(l.defaultKeyMsg("Some people say the oceans of blood are actually kool-aid. I'm too scared to taste it for myself.")));
		provider.addRandomlySelectable("murder_rain", defaultWeight(isInTitleLand(PULSE)),
				new NodeBuilder(l.defaultKeyMsg("You don't want to know what it's like to be outside when it rains. You can't tell who's a murderer or who forgot an umbrella!")));
		provider.addRandomlySelectable("swimming", defaultWeight(all(isInTitleLand(PULSE), isAnyEntityType(IGUANA, TURTLE))),
				new NodeBuilder(l.defaultKeyMsg("If you're looking for a good land to swim in, it's definitely not this one.")));
		provider.addRandomlySelectable("blood_surprise", defaultWeight(all(isInTitleLand(PULSE), isAnyEntityType(IGUANA, NAKAGATOR))),
				new NodeBuilder(l.defaultKeyMsg("OH GOD IS THAT BLOOD oh wait nevermind.")));
		
		
		//Thunder
		provider.addRandomlySelectable("skeleton_horse", defaultWeight(isInTitleLand(THUNDER)),
				new NodeBuilder(l.defaultKeyMsg("Some people say at night, skeletons riding skeleton horses come through the town.")));
		provider.addRandomlySelectable("blue_moon", defaultWeight(all(isInTitleLand(THUNDER), isAnyEntityType(SALAMANDER, IGUANA))),
				new NodeBuilder(l.defaultKeyMsg("Every once in a blue moon, lightning strikes and burns down the village. We have to rebuild it!")));
		provider.addRandomlySelectable("lightning_strike", defaultWeight(all(isInTitleLand(THUNDER), isAnyEntityType(TURTLE))),
				new NodeBuilder(l.defaultKeyMsg("You don't want to be struck by lightning. No one does.")));
		
		provider.addRandomlySelectable("reckoning", defaultWeight(isInTitleLand(THUNDER)), new ChainBuilder().withFolders()
				.node(new NodeBuilder(l.defaultKeyMsg("Those darn doomsayers, preaching about the Apocalypse and The Reckoning and such!")))
				.node(new NodeBuilder(l.defaultKeyMsg("What's The Reckoning? It's when meteors from The Veil are sent towards Skaia.")))
				.node(new NodeBuilder(l.defaultKeyMsg("Like any reasonable %s believes in that!", Argument.ENTITY_TYPE))));
		provider.addRandomlySelectable("thunder_death", defaultWeight(all(isInTitleLand(THUNDER), isInTerrainLand(WOOD))), new ChainBuilder()
				.node(new NodeBuilder(l.defaultKeyMsg("We're lucky to have rain with this weather.")))
				.node(new NodeBuilder(l.defaultKeyMsg("Otherwise the thunder would surely have been our death.")).addClosingResponse()));
		provider.addRandomlySelectable("hardcore", defaultWeight(all(isInTitleLand(THUNDER), isInTerrainLand(HEAT))),
				new NodeBuilder(l.defaultKeyMsg("This land is HARDCORE! There's lava and lightning wherever you go!")));
		provider.addRandomlySelectable("thunder_throw", defaultWeight(all(isInTitleLand(THUNDER), isAnyEntityType(TURTLE, SALAMANDER))), new ChainBuilder()
				.node(new NodeBuilder(l.defaultKeyMsg("Nemesis has been throwing thunder for generations, not stopping for even a moment.")))
				.node(new NodeBuilder(l.defaultKeyMsg("They are even doing it in their sleep. Can you believe that?")).addClosingResponse()));
		
		
		//Rabbits
		provider.addRandomlySelectable("bunny_birthday", defaultWeight(all(isInTitleLand(RABBITS), isAnyEntityType(NAKAGATOR, SALAMANDER))), //todo review this
				new NodeBuilder(l.defaultKeyMsg("Our daughter wants a bunny for her birthday, even though she caught six in the past three hours.")));
		provider.addRandomlySelectable("rabbit_eating", defaultWeight(all(isInTitleLand(RABBITS), isAnyEntityType(TURTLE, SALAMANDER))),
				new NodeBuilder(l.defaultKeyMsg("One time our village ran out of food and we tried eating rabbits. It was a dark period in our village history.")));
		provider.addRandomlySelectable("edgy_life_hatred", defaultWeight(all(isInTitleLand(RABBITS), isAnyEntityType(IGUANA, NAKAGATOR))),
				new NodeBuilder(l.defaultKeyMsg("This place is just so full of life! I despise it.")));
		provider.addRandomlySelectable("rabbit.food_shortage", defaultWeight(all(isInTitleLand(RABBITS), isInTerrainLand(MSTags.TerrainLandTypes.IS_DESOLATE))),
				new NodeBuilder(l.defaultKeyMsg("This land is already pretty desolate. There being lots of rabbits eating everything they find doesn't help!"))
						.addResponse(new ResponseBuilder(msg(ARROW)).condition(isAnyEntityType(NAKAGATOR))
								.nextDialogue("other_way", new NodeBuilder(l.defaultKeyMsg("But with that many rabbits around, there sure are other ways of getting food..."))
										.addClosingResponse())));
		provider.addRandomlySelectable("rabbit_food", defaultWeight(all(isInTitleLand(RABBITS), any(isInTerrainLand(MSTags.TerrainLandTypes.IS_DESOLATE), isInTerrainLand(FUNGI), isInTerrainLand(SHADE)))), new FolderedDialogue(builder -> {
			var cacti = builder.add("cacti", new NodeBuilder(l.defaultKeyMsg("Except maybe cacti, but would rabbits eat something that prickly?")));
			var next = builder.add("next", new NodeSelectorBuilder()
					.node(any(isInTerrainLand(FUNGI), isInTerrainLand(SHADE)), new NodeBuilder(l.subMsg("mushrooms", "I mean, there's not really much else than mushrooms around here.")))
					.defaultNode(new NodeBuilder(l.subMsg("desolate", "There's not really much food to be found in this desolate place."))
							.addResponse(new ResponseBuilder(msg(ARROW)).condition(isInTerrainLand(MSTags.TerrainLandTypes.SAND)).nextDialogue(cacti))));
			builder.addStart(new NodeBuilder(l.defaultKeyMsg("I sure wonder where the rabbits are getting their food from.")).next(next));
		}));
		
		
		//Monsters
		provider.addRandomlySelectable("pet_zombie", defaultWeight(all(isInTitleLand(MSTags.TitleLandTypes.MONSTERS), isAnyEntityType(NAKAGATOR, SALAMANDER))), //todo review this
				new NodeBuilder(l.defaultKeyMsg("I've heard moaning coming from our son's bedroom. I found out he's keeping a pet zombie in there! Tamed it and everything!")));
		provider.addRandomlySelectable("spider_raid", defaultWeight(isInTitleLand(MONSTERS)),
				new NodeBuilder(l.defaultKeyMsg("A few giant spiders raided our village last night, taking all of our bugs! Those monsters...")));
		provider.addRandomlySelectable("monstersona", defaultWeight(all(isInTitleLand(MSTags.TitleLandTypes.MONSTERS), isAnyEntityType(IGUANA, NAKAGATOR))),
				new NodeBuilder(l.defaultKeyMsg("What's your monster-sona? Mine is a zombie.")));
		
		
		//Towers
		provider.addRandomlySelectable("bug_treasure", defaultWeight(all(isInTitleLand(TOWERS), isAnyEntityType(SALAMANDER, IGUANA))),
				new NodeBuilder(l.defaultKeyMsg("Legends say underneath the tower to the north is a Captain Lizardtail's buried treasure! Literal tons of bugs, they say!")));
		provider.addRandomlySelectable("tower_gone", defaultWeight(all(isInTitleLand(TOWERS), isAnyEntityType(TURTLE, SALAMANDER))), //todo review this
				new NodeBuilder(l.defaultKeyMsg("That tower over there was built by my great grandpa Fjorgenheimer! You can tell by how its about to fall apa- oh it fell apart.")));
		provider.addRandomlySelectable("no_tower_treasure", defaultWeight(all(isInTitleLand(TOWERS), isAnyEntityType(IGUANA, NAKAGATOR))),
				new NodeBuilder(l.defaultKeyMsg("I feel ripped off. I was born in a land full of magical towers but none of them have treasure!")));
		
		
		//Thought
		provider.addRandomlySelectable("glass_books", defaultWeight(all(isInTitleLand(THOUGHT), isAnyEntityType(TURTLE, IGUANA))),
				new NodeBuilder(l.defaultKeyMsg("Our smartest villager read all the books in the library and now knows how to make glass jars! He's a gift from the big frog above!")));
		provider.addRandomlySelectable("book_food", defaultWeight(all(isInTitleLand(THOUGHT), isAnyEntityType(SALAMANDER, NAKAGATOR))),
				new NodeBuilder(l.defaultKeyMsg("We ate all the books in the nearby college ruins. It turns out thousand-year-old leather doesn't make the best dinner.")));
		provider.addRandomlySelectable("to_eat", defaultWeight(all(isInTitleLand(THOUGHT), isAnyEntityType(IGUANA, NAKAGATOR))),
				new NodeBuilder(l.defaultKeyMsg("To eat or not to eat, that is the question.")));
		
		
		//Cake
		provider.addRandomlySelectable("mystery_recipe", defaultWeight(all(isInTitleLand(CAKE), isAnyEntityType(TURTLE, NAKAGATOR))),
				new NodeBuilder(l.defaultKeyMsg("All of the villagers here are trying to crack the mystery of how to make the frosted bread we see all day on our walks.")));
		provider.addRandomlySelectable("cake_regen", defaultWeight(all(isInTitleLand(CAKE), isAnyEntityType(TURTLE, SALAMANDER))),
				new NodeBuilder(l.defaultKeyMsg("I heard all the cakes magically regenerate if you don't completely eat them! That's completely stupid!")));
		provider.addRandomlySelectable("cake_recipe", defaultWeight(all(isInTitleLand(CAKE), isAnyEntityType(IGUANA, SALAMANDER))),
				new NodeBuilder(l.defaultKeyMsg("Let's see, the recipe calls for 5 tbsp. of sugar, 2 tbsp. vanilla, 1 large grasshopper... what are you looking at?")));
		provider.addRandomlySelectable("fire_cakes", defaultWeight(all(isInTitleLand(CAKE), isInTerrainLand(HEAT))),
				new NodeBuilder(l.defaultKeyMsg("If you're not careful, anything can set you on fire here, even the cakes!")));
		provider.addRandomlySelectable("frosting", defaultWeight(all(isInTitleLand(CAKE), isInTerrainLand(FROST))),
				new NodeBuilder(l.defaultKeyMsg("When we start talking about cakes, the others start mentioning frosting. I'm not sure I get what they're talking about!")));
		
		
		//Clockwork
		provider.addRandomlySelectable("gear_technology", defaultWeight(all(isInTitleLand(CLOCKWORK), isAnyEntityType(SALAMANDER, IGUANA))),
				new NodeBuilder(l.defaultKeyMsg("Legends say the giant gears were used for technology no consort has ever seen before. That's absurd! It's obviously food!")));
		provider.addRandomlySelectable("evil_gears", defaultWeight(all(isInTitleLand(CLOCKWORK), isAnyEntityType(NAKAGATOR, IGUANA))),
				new NodeBuilder(l.defaultKeyMsg("My neighbor says the gears are evil! He also said that swords are used for combat, so he's probably insane.")));
		provider.addRandomlySelectable("ticking", defaultWeight(all(isInTitleLand(CLOCKWORK), isAnyEntityType(TURTLE, SALAMANDER))),
				new NodeBuilder(l.defaultKeyMsg("The ticking keeps me up all night. It keeps us all up all night. Save us.")));
		
		
		//Frogs
		provider.addRandomlySelectable("frog_creation", defaultWeight(isInTitleLand(FROGS)),
				new NodeBuilder(l.defaultKeyMsg("We are thankful for all the frogs that They gave to us when the universe was created. They, of course, is the genesis frog. I feel bad for the fool who has to make another!")));
		provider.addRandomlySelectable("frog_location", defaultWeight(isInTitleLand(FROGS)),
				new NodeBuilder(l.defaultKeyMsg("You won't find many frogs where you find villages. Most of them live where the terrain is rougher.")));
		provider.addRandomlySelectable("frog_imitation", defaultWeight(isInTitleLand(FROGS)),
				new NodeBuilder(l.defaultKeyMsg("Ribbit, ribbit! I'm a frog! I don't care what you say!")));
		provider.addRandomlySelectable("frog_variants", defaultWeight(isInTitleLand(FROGS)), new ChainBuilder()
				.node(new NodeBuilder(l.defaultKeyMsg("Most people believe there aren't that many types of frogs. 4740, maybe? Anything beyond that would be preposterous.")))
				.node(new NodeBuilder(l.defaultKeyMsg("Here in %s, however, we know that there are 9.444731276889531e+22 types of frogs.", Argument.LAND_NAME))
						.addClosingResponse()));
		provider.addRandomlySelectable("frog_hatred", defaultWeight(isInTitleLand(FROGS)),
				new NodeBuilder(l.defaultKeyMsg("For whatever reason, residents of Derse HATE frogs! Why would someone hate frogs?")));
		provider.addRandomlySelectable("grasshopper_fishing", defaultWeight(all(isInTitleLand(FROGS), isAnyEntityType(SALAMANDER, IGUANA))), new ChainBuilder()
				.node(new NodeBuilder(l.defaultKeyMsg("My brother found a magic grasshopper while fishing recently!")))
				.node(new NodeBuilder(l.defaultKeyMsg("Usually all we find are rings!"))));
		provider.addRandomlySelectable("gay_frogs", defaultWeight(all(isInTitleLand(FROGS), isInTerrainLand(RAINBOW))),
				new NodeBuilder(l.defaultKeyMsg("The frogs around here are all so gay! Look at them happily hopping about!")));
		provider.addRandomlySelectable("non_teleporting_frogs", defaultWeight(all(isInTitleLand(FROGS), isInTerrainLand(END))),
				new NodeBuilder(l.defaultKeyMsg("While the rest of us are getting dizzy, teleporting at random in the tall grass, the frogs seem immune! Makes it harder to catch them, that's for sure.")));
		
		
		//Buckets
		provider.addRandomlySelectable("lewd_buckets", defaultWeight(isInTitleLand(BUCKETS)),
				new NodeBuilder(l.defaultKeyMsg("Some may call our land lewd, but the buckets are just so fun to swim in!")));
		provider.addRandomlySelectable("water_buckets", defaultWeight(all(isInTitleLand(BUCKETS), isInTerrainLand(MSTags.TerrainLandTypes.SAND))),
				new NodeBuilder(l.defaultKeyMsg("The buckets are a great source of water, as long as you pick the ones with water...")));
		provider.addRandomlySelectable("warm_buckets", defaultWeight(all(isInTitleLand(BUCKETS), isInTerrainLand(FROST))),
				new NodeBuilder(l.defaultKeyMsg("Did you know that some buckets provide warmth? I tend to curl up next to one from time to time.")));
		provider.addRandomlySelectable("oil_buckets", defaultWeight(all(isInTitleLand(BUCKETS), isInTerrainLand(SHADE))), new ChainBuilder()
				.node(new NodeBuilder(l.defaultKeyMsg("Did you know that the buckets sometimes hold something other than oil?")))
				.node(new NodeBuilder(l.defaultKeyMsg("In some cases, they even contain something drinkable!"))));
		
		
		//Light
		provider.addRandomlySelectable("blindness", defaultWeight(isInTitleLand(LIGHT)),
				new NodeBuilder(l.defaultKeyMsg("God, it's bright. Half of our village is blind. It's beginning to become a serious problem.")));
		provider.addRandomlySelectable("doctors_inside", defaultWeight(all(isInTitleLand(LIGHT), isAnyEntityType(TURTLE))),
				new NodeBuilder(l.defaultKeyMsg("Our best village doctors found that staying outside in the blinding light for too long is not good for us. Most of us stay inside all our lives. It's sad.")));
		provider.addRandomlySelectable("staring", defaultWeight(isInTitleLand(LIGHT)),
				new NodeBuilder(l.defaultKeyMsg("Are you staring at me? No, really! I can't see because I'm blind.")));
		provider.addRandomlySelectable("sunglasses", defaultWeight(all(isInTitleLand(LIGHT), isInTerrainLand(HEAT))), new ChainBuilder()
				.node(new NodeBuilder(l.defaultKeyMsg("You'd better wear sunglasses, else you might not see where you're going.")))
				.node(new NodeBuilder(l.defaultKeyMsg("This is not the best place to wander blindly in."))));
		provider.addRandomlySelectable("bright_snow", defaultWeight(all(isInTitleLand(LIGHT), isInTerrainLand(FROST))), new ChainBuilder()
				.node(new NodeBuilder(l.defaultKeyMsg("You would think that the light would melt more snow.")))
				.node(new NodeBuilder(l.defaultKeyMsg("But nope, the snow stays as frozen as ever!"))));
		provider.addRandomlySelectable("glimmering_snow", defaultWeight(all(isInTitleLand(LIGHT), isInTerrainLand(FROST))),
				new NodeBuilder(l.defaultKeyMsg("Isn't it wonderful how much the snow is glimmering in the light?")));
		provider.addRandomlySelectable("glimmering_sand", defaultWeight(all(isInTitleLand(LIGHT), isInTerrainLand(MSTags.TerrainLandTypes.SAND))),
				new NodeBuilder(l.defaultKeyMsg("Isn't it wonderful how much the sand is glimmering in the light?")));
		provider.addRandomlySelectable("light_pillars", defaultWeight(all(isInTitleLand(LIGHT), isAnyEntityType(IGUANA, TURTLE))),
				new NodeBuilder(l.defaultKeyMsg("Those light pillars... they somehow make me think of the legend of the wyrm.")));
		
		
		//Silence
		provider.addRandomlySelectable("murder_silence", defaultWeight(all(isInTitleLand(SILENCE), isAnyEntityType(NAKAGATOR, SALAMANDER))),
				new NodeBuilder(l.defaultKeyMsg("This is a great place for murder. No one will hear you scream.")));
		provider.addRandomlySelectable("silent_underlings", defaultWeight(isInTitleLand(SILENCE)),
				new NodeBuilder(l.defaultKeyMsg("This place is so quiet and peaceful. Too bad we can't hear underlings about to kill us.")));
		provider.addRandomlySelectable("listening", defaultWeight(all(isInTitleLand(SILENCE), isAnyEntityType(IGUANA, SALAMANDER))), new ChainBuilder()
				.node(new NodeBuilder(l.defaultKeyMsg("Shhh, they can hear you...")))
				.node(new NodeBuilder(l.defaultKeyMsg("Just kidding, no one can hear you! The land itself muffles your words!"))));
		provider.addRandomlySelectable("calmness", defaultWeight(all(isInTitleLand(SILENCE), isAnyEntityType(TURTLE, IGUANA))),
				new NodeBuilder(l.defaultKeyMsg("The sense of calmness in the air, it's kind of unnerving!")));
		
		
		//Mixed
		provider.addRandomlySelectable("climb_high", defaultWeight(all(any(isInTitleLand(TOWERS), isInTitleLand(WIND)), isAnyEntityType(IGUANA))),
				new NodeBuilder(l.defaultKeyMsg("Climb up high and you'll be up for a great view!")));
		provider.addRandomlySelectable("height_fear", defaultWeight(all(any(isInTitleLand(TOWERS), isInTitleLand(WIND)), isAnyEntityType(TURTLE))), new NodeSelectorBuilder()
				.node(new Condition.AtOrAboveY(78), new NodeBuilder(l.subMsg("panic", "AAH, I am scared of heights!")))
				.node(isInTitleLand(TOWERS), "towers", new ChainBuilder()
						.node(new NodeBuilder(l.defaultKeyMsg("I'd climb up one of those towers and look at the view, but I am scared of heights.")))
						.node(new NodeBuilder(l.defaultKeyMsg("I mean, what if I slipped and fell off the stairs?"))))
				.defaultNode("rock", new ChainBuilder()
						.node(new NodeBuilder(l.defaultKeyMsg("I'd climb up one of those rocks and look at the view, but I am scared of heights.")))
						.node(new NodeBuilder(l.defaultKeyMsg("I mean what if I fell down and landed on my back?")))));
		
		
		//Shade
		provider.addRandomlySelectable("mush_farm", defaultWeight(isInTerrainLand(SHADE)), new ChainBuilder().withFolders()
				.node(new NodeBuilder(l.defaultKeyMsg("Someone's gotta be farmin' all these goddamn fuckin' mushrooms, pain in the ass through truly it be.")))
				.node(new NodeBuilder(l.defaultKeyMsg("So that's what I'm doing.")))
				.node(new NodeBuilder(l.defaultKeyMsg("Standing around here.")))
				.node(new NodeBuilder(l.defaultKeyMsg("farmin' all these")))
				.node(new NodeBuilder(l.defaultKeyMsg("goddamn")))
				.node(new NodeBuilder(l.defaultKeyMsg("fuckin")))
				.node(new NodeBuilder(l.defaultKeyMsg("mushrooms"))));
		provider.addRandomlySelectable("mushroom_pizza", defaultWeight(isInTerrainLand(SHADE)), new FolderedDialogue(builder ->
				builder.addStart(new NodeBuilder(l.defaultKeyMsg("Do you put glow mushrooms on your pizza or leave them off?"))
						.addResponse(new ResponseBuilder(l.subMsg("on", "Put them on"))
								.addPlayerMessage(l.subMsg("on.reply", "I put them on!"))
								.nextDialogue(builder.add("on", new NodeBuilder(l.defaultKeyMsg("Good! I was afraid I'd have to kill you!")))))
						.addResponse(new ResponseBuilder(l.subMsg("off", "Leave them off"))
								.addPlayerMessage(l.subMsg("off.reply", "I leave them off!"))
								.nextDialogue(builder.add("off", new NodeBuilder(l.defaultKeyMsg("You are a despicable person."))))))
		));
		provider.addRandomlySelectable("fire_hazard", defaultWeight(all(isInTerrainLand(SHADE), none(isInTitleLand(THUNDER)))),
				new NodeBuilder(l.defaultKeyMsg("Our land is a fire waiting to happen! Hopefully there isn't any lightning!")));
		provider.addRandomlySelectable("that_boy_needs_therapy", defaultWeight(isInTerrainLand(SHADE)),
				new NodeBuilder(l.defaultKeyMsg("Sometimes I wonder whether a purely mushroom diet is the cause of my dwindling mental capacity. In those moments, I think 'Ooh! mushroom!'... speaking of mushrooms, Sometimes I wonder...")));
		provider.addRandomlySelectable("lazy_king", defaultWeight(isInTerrainLand(SHADE)),
				new NodeBuilder(l.defaultKeyMsg("I feel like our king just sits around doing nothing but eating weird glowing mushrooms! So lazy!")));
		
		//Heat
		provider.addRandomlySelectable("getting_hot", defaultWeight(isInTerrainLand(HEAT)),
				new NodeBuilder(l.defaultKeyMsg("Is it getting hot in here or is it just me?")));
		provider.addRandomlySelectable("step_into_fire", defaultWeight(isInTerrainLand(HEAT)),
				new NodeBuilder(l.defaultKeyMsg("You'd better watch where you're going. Wouldn't want you to step right into some fire.")));
		provider.addRandomlySelectable("lava_crickets", defaultWeight(isInTerrainLand(HEAT)),
				new NodeBuilder(l.defaultKeyMsg("Have you ever had a lava-roasted cricket? The lava really brings out the cricket juices.")));
		provider.addRandomlySelectable("tummy_tunnel", defaultWeight(isInTerrainLand(HEAT)),    //todo review this. This dialogue was set up as regular dialogue in the old system, but its location in the language provider suggested that it might have meant to be food merchant dialogue.
				new NodeBuilder(l.defaultKeyMsg("Man this shop is packed tighter then my tummy tunnel when I gotta make brown on the john after eating one too many of them incandescent pies what be popping around.")));
		provider.addRandomlySelectable("the_water_is_lava", defaultWeight(isInTerrainLand(HEAT)),
				new NodeBuilder(l.defaultKeyMsg("You know the water is fucking lava? Who thought it would be a good idea to make water out of lava? How do we even stay hydrated in this place dude?")));
		
		
		//Wood
		provider.addRandomlySelectable("wood_treatments", defaultWeight(isInTerrainLand(WOOD)),
				new NodeBuilder(l.defaultKeyMsg("We figured out how to treat the wood to make it less flammable. I hope we didn't miss a spot.")));
		provider.addRandomlySelectable("splinters", defaultWeight(isInTerrainLand(WOOD)), new ChainBuilder()
				.node(new NodeBuilder(l.defaultKeyMsg("Be careful not to walk barefoot here, you could get a splinter!")))
				.node(new NodeBuilder(l.defaultKeyMsg("Some of our kind have died due to the amount of splinters they received while on a walk."))));
		
		
		//Sand
		provider.addRandomlySelectable("sand_surfing", defaultWeight(isInTerrainLand(MSTags.TerrainLandTypes.SAND)),
				new NodeBuilder(l.defaultKeyMsg("Sand-surfing is my new favorite sport! Too bad you can't really move, though.")));
		provider.addRandomlySelectable("camel", defaultWeight(isInTerrainLand(MSTags.TerrainLandTypes.SAND)), new FolderedDialogue(builder ->
				builder.addStart(new NodeBuilder(l.defaultKeyMsg("Want to buy a used camel? Only 2000 boondollars."))
						.addResponse(new ResponseBuilder(l.subMsg("yes", "Why not? Seems like a good price for a camel!"))
								.addPlayerMessage(l.subMsg("yes.reply", "Sure!"))
								.nextDialogue(builder.add("no_camel", new NodeBuilder(l.defaultKeyMsg("Hahaha! Sucker! I have no camel! Cya later! 8)"))))    //todo a text mention (maybe in description?) saying they went off before actually getting any boondollars?
								.setNextAsEntrypoint())
						.addResponse(new ResponseBuilder(l.subMsg("no", "Of course not! You know better!"))
								.addPlayerMessage(l.subMsg("no.reply", "Not at all!"))
								.nextDialogue(builder.add("dancing_camel", new NodeBuilder(l.defaultKeyMsg("Are you sure? Too bad! The camel knew how to dance, too!"))))))
		));
		
		
		//Sandstone
		provider.addRandomlySelectable("knockoff", defaultWeight(isInTerrainLand(MSTags.TerrainLandTypes.SANDSTONE)),
				new NodeBuilder(l.defaultKeyMsg("I kind of feel like we're a stale, knockoff sand land.")));
		provider.addRandomlySelectable("sandless", defaultWeight(isInTerrainLand(MSTags.TerrainLandTypes.SANDSTONE)), new ChainBuilder()
				.node(new NodeBuilder(l.defaultKeyMsg("According to legend, %s ate all the sand here leaving nothing but sandstone!", Argument.LAND_DENIZEN)))
				.node(new NodeBuilder(l.defaultKeyMsg("I'm kidding, I made that up on the spot. I had no other dialogue."))));
		
		
		//Frost
		provider.addRandomlySelectable("frozen", defaultWeight(isInTerrainLand(FROST)), new ChainBuilder()
				.node(new NodeBuilder(l.defaultKeyMsg("My neighbors were complaining the other night about the snow.")))
				.node(new NodeBuilder(l.defaultKeyMsg("Personally, the cold never really bothered me anyways."))
						.addDescription(l.subMsg("desc", "You hear a faint \"ba-dum tss\" in the distance."))));
		provider.addRandomlySelectable("fur_coat", defaultWeight(isInTerrainLand(FROST)), new FolderedDialogue(builder ->
				builder.addStart(new NodeBuilder(l.defaultKeyMsg("Darn! I only need 100 more boondollars for a nice, fur coat! I'm going to freeze!"))
						.addResponse(new ResponseBuilder(l.subMsg("pay", "Give them the boondollars [Pay 100 boondollars]"))
								.condition(new Condition.PlayerHasBoondollars(100))
								.addTrigger(new Trigger.AddBoondollars(-100))
								.addTrigger(new Trigger.GiveFromLootTable(MSLootTables.CONSORT_JUNK_REWARD))
								.addTrigger(new Trigger.AddConsortReputation(50))
								.addTrigger(new Trigger.SetDialogue(genericThanks))    //todo they'll be thanking any player like this. Maybe work with player flags to thank the specific player and say something else to others?
								.addPlayerMessage(l.subMsg("pay.reply", "Here you go!"))
								.nextDialogue(builder.add("gratitude", new NodeBuilder(l.defaultKeyMsg("Oh, thank you! Now I won't freeze to death out here! Take this as a token of gratitude!")))))
						.addResponse(new ResponseBuilder(l.subMsg("ignore", "Don't give them any of your hard-earned boondollars!"))
								.addPlayerMessage(l.subMsg("ignore.reply", "Sorry, but I can't help you."))
								.nextDialogue(builder.add("death", new NodeBuilder(l.defaultKeyMsg("I guess I'll just die then..."))))))));
		provider.addRandomlySelectable("tent_protection", defaultWeight(all(isInTerrainLand(FROST), Condition.HasMoveRestriction.INSTANCE)),
				new NodeBuilder(l.defaultKeyMsg("These tents doesn't protect against the cold very well, but they are good enough.")));
		
		
		//Rock
		provider.addRandomlySelectable("all_ores", defaultWeight(isInTerrainLand(MSTags.TerrainLandTypes.ROCK)),
				new NodeBuilder(l.defaultKeyMsg("Jokes on the losers in other lands, we have ALL the resources! All of them!")));
		provider.addRandomlySelectable("rockfu", defaultWeight(isInTerrainLand(MSTags.TerrainLandTypes.ROCK)),
				new NodeBuilder(l.defaultKeyMsg("Here in %s, we practice rock-fu! Learn the way of the rock to CRUSH your enemies into a fine rock powder!", Argument.LAND_NAME)));
		
		
		//Forest
		provider.addRandomlySelectable("all_trees", defaultWeight(isInTerrainLand(MSTags.TerrainLandTypes.FOREST)),
				new NodeBuilder(l.defaultKeyMsg("Jokes on the losers in other lands, we have ALL the trees! All of them!")));
		provider.addRandomlySelectable("really_likes_trees", defaultWeight(isInTerrainLand(MSTags.TerrainLandTypes.FOREST)),
				new NodeBuilder(l.defaultKeyMsg("Do you like trees? I really like trees. I am one with the tree. Trees. TREES. TREEEES!")));
		
		
		//Fungi
		provider.addRandomlySelectable("mycelium", defaultWeight(isInTerrainLand(FUNGI)), new ChainBuilder()
				.node(new NodeBuilder(l.defaultKeyMsg("Frog, don't you love the feeling of mycelium on your toes?")))
				.node(new NodeBuilder(l.defaultKeyMsg("No? Is that just me?"))));
		provider.addRandomlySelectable("adaptation", defaultWeight(isInTerrainLand(FUNGI)), new ChainBuilder()
				.node(new NodeBuilder(l.defaultKeyMsg("At first, no one liked the mushrooms when our planet was cursed with the Dank.")))
				.node(new NodeBuilder(l.defaultKeyMsg("Those who refused to adapt to the new food source Perished, obviously."))));
		provider.addRandomlySelectable("mushroom_curse", defaultWeight(isInTerrainLand(FUNGI)),
				new NodeBuilder(l.defaultKeyMsg("Curse %s! And curse all their mushrooms, too! I miss eating crickets instead of all these mushrooms!", Argument.LAND_DENIZEN)));
		provider.addRandomlySelectable("jacket", defaultWeight(isInTerrainLand(FUNGI)),
				new NodeBuilder(l.defaultKeyMsg("It's so damp and cold. I wish I had a jacket!")));
		provider.addRandomlySelectable("mildew", defaultWeight(isInTerrainLand(FUNGI)),
				new NodeBuilder(l.defaultKeyMsg("Ah, the mildew on the grass in the morning makes the landscape so pretty!")));
		provider.addRandomlySelectable("fungus_destroyer", defaultWeight(isInTerrainLand(FUNGI)),
				new NodeBuilder(l.defaultKeyMsg("According to legends of old, the %s will come one day and get the evil %s to clear up all this fungus!", Argument.LAND_TITLE, Argument.LAND_DENIZEN)));
		
		
		//Rainbow
		provider.addRandomlySelectable("generic_green", defaultWeight(isInTerrainLand(RAINBOW)),
				new NodeBuilder(l.defaultKeyMsg("Have you ever noticed rainbow wood looks green from a distance? I wonder if green is somehow more generic than other colors.")));
		provider.addRandomlySelectable("overwhelming_colors", defaultWeight(isInTerrainLand(RAINBOW)),
				new NodeBuilder(l.defaultKeyMsg("Even for us turtles, this place is too bright. All the light and colors around here can be really overwhelming!")));
		provider.addRandomlySelectable("saw_rainbow", defaultWeight(isInTerrainLand(RAINBOW)),
				new NodeBuilder(l.defaultKeyMsg("I saw a rainbow yesterday! Normally I see way more than that.")));
		provider.addRandomlySelectable("sunglasses", defaultWeight(isInTerrainLand(RAINBOW)),
				new NodeBuilder(l.defaultKeyMsg("Some sunglasses would be really great in a Land like this. Too bad I don't have ears!")));
		provider.addRandomlySelectable("what_is_wool", defaultWeight(isInTerrainLand(RAINBOW)),
				new NodeBuilder(l.defaultKeyMsg("I have no clue what the ground here is made of. I've never seen anything like it anywhere else!")));
		provider.addRandomlySelectable("love_colors", defaultWeight(isInTerrainLand(RAINBOW)),
				new NodeBuilder(l.defaultKeyMsg("People ask me, \"What's your favorite color?\" I can't pick! I love them all! They're all special in their own way! Well, except green.")));
		provider.addRandomlySelectable("types_of_colors", defaultWeight(isInTerrainLand(RAINBOW)), new ChainBuilder().withFolders()
				.node(new NodeBuilder(l.defaultKeyMsg("In the additive color system, there are three primary colors: red, green, and blue.")))
				.node(new NodeBuilder(l.defaultKeyMsg("In the subtractive color system, there are also three primary colors, but those are magenta, yellow, and cyan.")))
				.node(new NodeBuilder(l.defaultKeyMsg("In the additive system, mixing red and green makes yellow, mixing green and blue makes cyan, and mixing blue and red makes magenta.")))
				.node(new NodeBuilder(l.defaultKeyMsg("In the subtractive system, mixing magenta and yellow makes red, mixing yellow and cyan makes green, and mixing cyan and magenta makes blue!")))
				.node(new NodeBuilder(l.defaultKeyMsg("These six colors make up the color wheel: red, yellow, green, cyan, blue, magenta, and then back to red.")))
				.node(new NodeBuilder(l.defaultKeyMsg("When you look at a rainbow, you don't see magenta, because the blue on one end doesn't mix with the red on the other end.")))
				.node(new NodeBuilder(l.defaultKeyMsg("You do, however, see purple, which is between magenta and blue. Short answer for why that is, your eyes are lying to you.")))
				.node(new NodeBuilder(l.defaultKeyMsg("Beyond the six main colors, however, there are also six other colors: pink, brown, orange, lime, light blue, and purple.")))
				.node(new NodeBuilder(l.defaultKeyMsg("In addition, there are also the tones of white, light gray, gray, and black.")))
				.node(new NodeBuilder(l.defaultKeyMsg("In the additive system, mixing all the colors together makes white.")))
				.node(new NodeBuilder(l.defaultKeyMsg("In the subtractive system, mixing all the colors together makes black.")))
				.node(new NodeBuilder(l.defaultKeyMsg("When dealing with dye, however, you can find some unusual combinations, or lack of combinations.")))
				.node(new NodeBuilder(l.defaultKeyMsg("Dyes work largely on the subtractive color system, but they don't always mix the way they should.")))
				.node(new NodeBuilder(l.defaultKeyMsg("This is because dyes, while vibrant, are imperfect representations of their respective colors.")))
				.node(new NodeBuilder(l.defaultKeyMsg("Lime, for example, should be a mix of yellow and green. To get lime dye, though, you need to mix cactus green with white dye instead.")))
				.node(new NodeBuilder(l.defaultKeyMsg("When mixing red and blue to get magenta, the blue overpowers the red and you get purple. You have to mix the purple with not just red, but pink to get magenta.")))
				.node(new NodeBuilder(l.defaultKeyMsg("Dye is weird like that.")))
				.node(new NodeBuilder(l.defaultKeyMsg("...what, you're still listening to me? Wow. No one's ever listened to the whole thing before. Would you like to hear it again?")))
				.loop());
		
		
		//End
		provider.addRandomlySelectable("at_the_end", defaultWeight(isInTerrainLand(END)),
				new NodeBuilder(l.defaultKeyMsg("This may be the start of our conversation, but now we're at the end.")));
		provider.addRandomlySelectable("chorus_fruit", defaultWeight(isInTerrainLand(END)),
				new NodeBuilder(l.defaultKeyMsg("Never eat fruit. Last time I tried it, I blacked out and came to somewhere else! Stick to bugs like a normal person!")));
		provider.addRandomlySelectable("end_grass", defaultWeight(isInTerrainLand(END)),
				new NodeBuilder(l.defaultKeyMsg("The grass in this place just keeps growing everywhere! You can bet that any patches of grass you find weren't there before. I don't even know how it takes root in the stone like that.")));
		provider.addRandomlySelectable("grass_curse", defaultWeight(isInTerrainLand(END)),
				new NodeBuilder(l.defaultKeyMsg("Rumors say that %s got mad one day and cursed the world with all this grass everywhere. It gets into our homes!", Argument.LAND_DENIZEN)));
		provider.addRandomlySelectable("tall_grass", defaultWeight(isInTerrainLand(END)),
				new NodeBuilder(l.defaultKeyMsg("The taller grass is so disorienting to walk through! Unless you are careful it will just move you around.")));
		provider.addRandomlySelectable("useless_elytra", defaultWeight(isInTerrainLand(END)),
				new NodeBuilder(l.defaultKeyMsg("One time, I saw a guy with some weird wing-looking things on his back. He could glide with them, but without being able to stay in the air, what's the point?")));
		
		
		//Rain
		provider.addRandomlySelectable("empty_ocean", defaultWeight(isInTerrainLand(RAIN)),
				new NodeBuilder(l.defaultKeyMsg("Our oceans used to be filled with life! Now they're all barren, thanks to %s.", Argument.LAND_DENIZEN)));
		provider.addRandomlySelectable("forbidden_snack", defaultWeight(isInTerrainLand(RAIN)),
				new NodeBuilder(l.defaultKeyMsg("Contrary to popular belief, chalk is not safe for consumption... but how can I resist its allure?")));
		provider.addRandomlySelectable("cotton_candy", defaultWeight(isInTerrainLand(RAIN)),
				new NodeBuilder(l.defaultKeyMsg("Have you ever considered eating a rain cloud? Yum! Maybe it tastes like cotton candy...")));
		provider.addRandomlySelectable("monsters_below", defaultWeight(isInTerrainLand(RAIN)),
				new NodeBuilder(l.defaultKeyMsg("Do you know what lies deep beneath the ocean waters? Scary to think about!")));
		provider.addRandomlySelectable("keep_swimming", defaultWeight(isInTerrainLand(RAIN)),
				new NodeBuilder(l.defaultKeyMsg("Just keep swimming, just keep swimming! Yay, swimming!")));
		
		
		//Flora
		provider.addRandomlySelectable("battle_site", defaultWeight(isInTerrainLand(FLORA)),
				new NodeBuilder(l.defaultKeyMsg("This land was the site of a battle ages and ages and ages ago.")));
		provider.addRandomlySelectable("blood_oceans", defaultWeight(isInTerrainLand(FLORA)),
				new NodeBuilder(l.defaultKeyMsg("The giant creatures who warred here long ago shed so much blood that, even now, the oceans are red with it.")));
		provider.addRandomlySelectable("giant_swords", defaultWeight(isInTerrainLand(FLORA)),
				new NodeBuilder(l.defaultKeyMsg("My grandpa told me that the giant swords everywhere were dropped by giants locked in combat ages ago.")));
		provider.addRandomlySelectable("bloodberries", defaultWeight(isInTerrainLand(FLORA)), new ChainBuilder()
				.node(new NodeBuilder(l.defaultKeyMsg("The strawberries here grow big and red thanks to all the blood in the water supply! The flowers thrive, too!")))
				.node(new NodeBuilder(l.defaultKeyMsg("Strawberry juice is the only thing safe to drink here. If I have any more, I'll scream. Please save us."))));
		provider.addRandomlySelectable("sharp_slide", defaultWeight(isInTerrainLand(FLORA)),
				new NodeBuilder(l.defaultKeyMsg("Don't use the sharp sides of giant swords as slides. May her beautiful soul rest in pieces.")));
		provider.addRandomlySelectable("immortality_herb", defaultWeight(all(isInTerrainLand(FLORA), Condition.FirstTimeGenerating.INSTANCE)).keepOnReset(), new ChainBuilder().withFolders()
				.node(new NodeBuilder(l.defaultKeyMsg("I have a herb that grants immortality! I'm going to eat it right now!")))
				.node(new NodeBuilder(l.defaultKeyMsg("However, they are easily confused with an explosion-causing herb...")))
				.node(new NodeBuilder(l.defaultKeyMsg("I'm taking the risk.")).addResponse(new ResponseBuilder(msg(DOTS)).addTrigger(new Trigger.Explode()))));
		provider.addRandomlySelectable("spices", defaultWeight(isInTerrainLand(FLORA)), new ChainBuilder()
				.node(new NodeBuilder(l.defaultKeyMsg("A good chef cooks with the spices found throughout the land.")))
				.node(new NodeBuilder(l.defaultKeyMsg("Other chefs are envious that they don't live in %s.", Argument.LAND_NAME))));
		
		
		//Mixed
		provider.addRandomlySelectable("red_better", defaultWeight(any(isInTerrainLand(RED_SAND), isInTerrainLand(RED_SANDSTONE))),
				new NodeBuilder(l.defaultKeyMsg("Red is much better than yellow, don't you think?")));
		provider.addRandomlySelectable("yellow_better", defaultWeight(any(isInTerrainLand(SAND), isInTerrainLand(SANDSTONE))),
				new NodeBuilder(l.defaultKeyMsg("In our village, we have tales of monsters that are attracted to red. That's why everything is yellow!")));
		
		
		//Misc
		provider.addRandomlySelectable("denizen_mention", defaultWeight(isInLand()),
				new NodeBuilder(l.defaultKeyMsg("It's a wonderful day. Hopefully some monster underneath the planet's surface doesn't eat us all!")));
		provider.addRandomlySelectable("ring_fishing", defaultWeight(isAnyEntityType(SALAMANDER, IGUANA)),
				new NodeBuilder(l.defaultKeyMsg("My brother found a magic ring while fishing recently!")));
		provider.addRandomlySelectable("frog_walk", defaultWeight(isAnyEntityType(TURTLE)),
				new NodeBuilder(l.defaultKeyMsg("Frog, it's such a wonderful day to just walk around a village.")));
		provider.addRandomlySelectable("delicious_hair", defaultWeight(isAnyEntityType(IGUANA)),
				new NodeBuilder(l.defaultKeyMsg("Holy leapin' god, you have such wonderful hair! Can I eat some?")));
		provider.addRandomlySelectable("music_invention", defaultWeight(isAnyEntityType(NAKAGATOR, SALAMANDER)),
				new NodeBuilder(l.defaultKeyMsg("I invented music, y'kno! My favorite song goes like ba ba dum, dum ba dum.")));
		provider.addRandomlySelectable("wyrm", defaultWeight(isAnyEntityType(TURTLE, IGUANA)),
				new NodeBuilder(l.defaultKeyMsg("Legends speak of the Wyrm, a giant ivory pillar that radiates joy and happiness and uselessness.")));
		provider.addRandomlySelectable("useless_pogo", defaultWeight(alwaysTrue()),
				new NodeBuilder(l.defaultKeyMsg("I once found this piece of junk that launched me upward when I hit the ground with it. It really hurt when I came back down, and I didn't get anywhere!")));
		provider.addRandomlySelectable("await_hero", defaultWeight(isInHomeLand()),
				new NodeBuilder(l.defaultKeyMsg("Here, in the %s, we %s worship the %s. We wait and hope for the day that they awaken.", Argument.LAND_NAME, Argument.ENTITY_TYPES, Argument.LAND_TITLE)));
		provider.addRandomlySelectable("zazzerpan", defaultWeight(isAnyEntityType(TURTLE)),
				new NodeBuilder(l.defaultKeyMsg("Old wizard Zazzerpan would be turning in his grave if he saw the horrors that walk these lands. Those giclopes sure are terrifying!")));
		provider.addRandomlySelectable("texas_history", defaultWeight(all(isAnyEntityType(TURTLE), isFromLand())),
				new NodeBuilder(l.defaultKeyMsg("The place was %s, the year, was 20XX.", Argument.LAND_NAME)));
		provider.addRandomlySelectable("disks", defaultWeight(isAnyEntityType(IGUANA)),
				new NodeBuilder(l.defaultKeyMsg("I used to be an adventurer like you, then I never got the disks.")));
		provider.addRandomlySelectable("whoops", defaultWeight(isAnyEntityType(IGUANA)),
				new NodeBuilder(l.defaultKeyMsg("Beware the man who speaks in hands, wait...wrong game.")));
		provider.addRandomlySelectable("fourth_wall", defaultWeight(isAnyEntityType(IGUANA)),
				new NodeBuilder(l.defaultKeyMsg("Maybe you should do something more productive than talking to NPCs.")));
		provider.addRandomlySelectable("consort_scoliosis", defaultWeight(isAnyEntityType(TURTLE)),
				new NodeBuilder(l.defaultKeyMsg("I'm not actually a child, I simply have an incredibly advanced case of consort scoliosisis that has gone untreated for years.")));
		provider.addRandomlySelectable("oh_to_be_ugly", defaultWeight(isAnyEntityType(NAKAGATOR)),
				new NodeBuilder(l.defaultKeyMsg("Inspite of the fact that I'm training to become an evil wizard, I'm simply not ugly enough to fit the bill. Any good wizardling knows that one must be as ugly as their desires to truly obtain power.")));
		provider.addRandomlySelectable("no_to_podcasting", defaultWeight(isAnyEntityType(IGUANA)),
				new NodeBuilder(l.defaultKeyMsg("My buddy wanted to do some podcasting... I'm no longer friends with him I'm not gonna lie")));
		provider.addRandomlySelectable("bats", defaultWeight(isAnyEntityType(TURTLE)),
				new NodeBuilder(l.defaultKeyMsg("I don't like the idea of a bat. Never seen one, and I don't plan on it. It's just like, why the fuck are they built like that!?")));
		provider.addRandomlySelectable("so_what", defaultWeight(isAnyEntityType(SALAMANDER)),
				new NodeBuilder(l.defaultKeyMsg("Uhuh, so what you think you and your little hairy non-reptilian disposition impresses me? It doesn't impress me you just look like a fool. A damned hairy fool.")));
		provider.addRandomlySelectable("trolly_problem", defaultWeight(isAnyEntityType(NAKAGATOR)),
				new NodeBuilder(l.defaultKeyMsg("The trolly problem isn't really all that complicated for me all things considered. I mean, I'll probably die of old age before the trolly ever hits me considering my incredibly short and quickly dwindling lifespan")));
		provider.addRandomlySelectable("a_little_lampshading", defaultWeight(isAnyEntityType(IGUANA)),
				new NodeBuilder(l.defaultKeyMsg("Look I know you aren't a reptile, I just got one question. You ever hear some of these people talk? I swear, they must be putting mercury in the water with how nonsensical conversation can be with these guys.")));
		provider.addRandomlySelectable("hats", defaultWeight(isAnyEntityType(SALAMANDER)),
				new NodeBuilder(l.defaultKeyMsg("I like crumpled hats, they're comfy and easy to wear!")));
		provider.addRandomlySelectable("wwizard", defaultWeight(isAnyEntityType(TURTLE)),
				new NodeBuilder(l.defaultKeyMsg("Secret wizards? Th-there are no secret wizards! Wh-what're you speaking of, o-outlandish traveller?")));
		provider.addRandomlySelectable("stock_market", defaultWeight(isAnyEntityType(NAKAGATOR)),
				new NodeBuilder(l.defaultKeyMsg("I bought a bunch of stocks on the market... Now I'm broke...")));
		provider.addRandomlySelectable("identity", defaultWeight(all(isAnyEntityType(SALAMANDER), isFromLand())),
				new NodeBuilder(l.defaultKeyMsg("I heard that the true name of the %s is %s. Isn't that cool?", Argument.LAND_TITLE, Argument.LAND_PLAYER_NAME)));
		provider.addRandomlySelectable("college", defaultWeight(alwaysTrue()), new ChainBuilder()
				.node(new NodeBuilder(l.defaultKeyMsg("Please, I need to pay for my children to attend college...")))
				.node(new NodeBuilder(l.defaultKeyMsg("Wow, you have so many boondollars! I'll never make that much in my short, amphibious lifetime."))));    //todo this doesn't actually depend on how many boondollars the player has. There's also a lack of interaction options from what the text would warrant.
		provider.addRandomlySelectable("unknown", defaultWeight(isAnyEntityType(TURTLE)), new ChainBuilder()
				.node(new NodeBuilder(l.defaultKeyMsg("They are coming...")))    //todo custom response message?
				.node(new NodeBuilder(l.defaultKeyMsg("Huh? 'Who the fuck is They'? What kind of question is that?! I don't know! Who the fuck are you?"))));
		provider.addRandomlySelectable("cult", defaultWeight(isAnyEntityType(TURTLE, SALAMANDER)), new ChainBuilder()
				.node(new NodeBuilder(l.defaultKeyMsg("We would love to invite you, %s, to our secret wizards cult.", Argument.PLAYER_TITLE)))
				.node(new NodeBuilder(l.defaultKeyMsg("Meet me by dawn with mercury, salt, and sulfur to begin the initiation."))));    //todo review this. Suggests interactivity that is simply not there.
		provider.addRandomlySelectable("title_presence", defaultWeight(all(isAnyEntityType(IGUANA, SALAMANDER), isFromLand())), new FolderedDialogue(builder ->
				builder.addStart(new NodeBuilder(l.defaultKeyMsg("I sense the presence of the %s. Tell me if you see them, ok?", Argument.PLAYER_TITLE))
						.addResponse(new ResponseBuilder(l.subMsg("i_am", "Present yourself as the %s.", Argument.PLAYER_TITLE))
								.addPlayerMessage(l.subMsg("i_am.reply", "I am the %s.", Argument.PLAYER_TITLE))
								.nextDialogue(builder.add("i_am", new NodeBuilder(l.defaultKeyMsg("OH MY %s", Argument.ENTITY_SOUND_2)))))
						.addResponse(new ResponseBuilder(l.subMsg("agree", "\"Agree\" to do that."))
								.addPlayerMessage(l.subMsg("agree.reply", "Hehe, ok I will.")).nextDialogue(thanks)))
		));
		provider.addRandomlySelectable("denizen", defaultWeight(all(isAnyEntityType(SALAMANDER, IGUANA, TURTLE), isInHomeLand())), new FolderedDialogue(builder ->
				builder.addStart(new NodeBuilder(l.defaultKeyMsg("%s has been sleeping for a thousand years. I shudder at the thought of their return.", Argument.LAND_DENIZEN))
						.addResponse(new ResponseBuilder(l.subMsg("what", "What?"))
								.addPlayerMessage(l.subMsg("what.reply", "The... what?"))
								.nextDialogue(builder.add("explain", new NodeBuilder(l.defaultKeyMsg("The Denizen is the One that Slumbers in our very soil. It is eternally waiting for the %s to awaken it. Then they will be given The Choice, and their victory will be determined by what they choose.", Argument.LAND_CLASS)))))
						.addResponse(new ResponseBuilder(l.subMsg("alignment", "Ask if the denizens are bad or not."))
								.addPlayerMessage(l.subMsg("alignment.reply", "Were these 'denizens' bad?"))
								.nextDialogue(builder.add("alignment", new NodeBuilder(l.defaultKeyMsg("How am I supposed to know if they were good or bad? There's more to a living being than just black and white!"))))))
		));
		provider.addRandomlySelectable("floating_island", defaultWeight(all(isInHomeLand(), new Condition.NearSpawn(256))),
				new NodeBuilder(l.defaultKeyMsg("I heard a floating island just appeared somewhere near here recently and falling chunks destroyed a village underneath it!")));
		provider.addRandomlySelectable("heroic_stench", defaultWeight(isInLand()), new NodeSelectorBuilder()
				.node(Condition.HasPlayerEntered.INSTANCE, new NodeBuilder(l.defaultKeyMsg("You smell kind of... heroic... like a hero, perhaps? It makes me kinda nervous to be around you!")))
				.defaultNode(new NodeBuilder(l.subMsg("leech", "You smell like you're leeching from the success from another hero... is this true?"))));
		provider.addRandomlySelectable("watch_skaia", defaultWeight(isFromLand()), new NodeSelectorBuilder()
				.node(all(Condition.IsInSkaia.INSTANCE, isAnyEntityType(TURTLE)), new NodeBuilder(l.subMsg("at_skaia.turtle", "Oh my...! I'm actually on Skaia!")))
				.node(Condition.IsInSkaia.INSTANCE, new NodeBuilder(l.subMsg("at_skaia", "OH MY %s! I'M ACTUALLY ON SKAIA!", Argument.ENTITY_SOUND_2)))
				.node(Condition.ConsortVisitedSkaia.INSTANCE, new NodeBuilder(l.subMsg("has_visited", "You know, I have actually visited Skaia at one point!")))
				.defaultNode(new NodeBuilder(l.defaultKeyMsg("Sometimes, I look up in the sky to see Skaia and wish I could visit there some day..."))));
		
		provider.addRandomlySelectable("hungry", defaultWeight(isAnyEntityType(SALAMANDER, IGUANA, NAKAGATOR)), new FolderedDialogue(builder ->
		{
			String barterFlag = "barter", noBarterFlag = "no_barter";
			
			var barterNode = new NodeBuilder(l.defaultKeyMsg("But I am starving here! What if I paid you 10 boondollars for it?"))
					.addResponse(new ResponseBuilder(l.subMsg("yes", "Sure"))
							.condition(Condition.HasMatchedItem.INSTANCE)
							.addTrigger(Trigger.TakeMatchedItem.INSTANCE)
							.addTrigger(new Trigger.AddBoondollars(10))
							.addPlayerMessage(l.subMsg("yes.reply", "Sure, I can agree to that."))
							.addDescription(l.subMsg("yes.desc", "You are given 10 boondollars for the %s.", Argument.MATCHED_ITEM))
							.setNextAsEntrypoint()
							.nextDialogue(builder.add("finally", new NodeBuilder(l.defaultKeyMsg("Finally!")))))
					.addResponse(new ResponseBuilder(l.subMsg("no", "Too Cheap"))
							.addPlayerMessage(l.subMsg("no.reply", "I won't let it go that cheap."))
							.nextDialogue(builder.add("end", new NodeBuilder(l.defaultKeyMsg("Fine. I will just go and find a real food store."))))
							.setNextAsEntrypoint());
			
			var firstNo = builder.add("first_no", new NodeSelectorBuilder()
					.node(new Condition.Flag(barterFlag), barterNode)
					.defaultNode(new NodeBuilder(sadFaceMsg)));
			
			var noReplyMsg = l.msg(builder.startId(), "first_no_reply", "I don't really want to give this away.");
			
			builder.addStart(new NodeSelectorBuilder()    //todo create a "not hungry" dialogue node for SetDialogue trigger to be used for any other player after the consort gets its snack.
					.node(new Condition.ItemTagMatch(MSTags.Items.CONSORT_SNACKS), new NodeBuilder(l.subMsg("ask", "A %s! Could I have some?", Argument.MATCHED_ITEM))
							.addResponse(new ResponseBuilder(yesMsg)
									.condition(Condition.HasMatchedItem.INSTANCE)
									.addTrigger(Trigger.TakeMatchedItem.INSTANCE)
									.addTrigger(new Trigger.AddConsortReputation(15))
									.addPlayerMessage(l.subMsg("yes_reply", "Sure, here."))
									.nextDialogue(builder.add("thanks", new NodeBuilder(l.defaultKeyMsg("Thank you! I will remember your kindness for the rest of my short life."))))
									.setNextAsEntrypoint())
							.addResponse(new ResponseBuilder(noMsg)
									.condition(isAnyEntityType(NAKAGATOR))
									.addTrigger(new Trigger.SetFlag(barterFlag, false))
									.addPlayerMessage(noReplyMsg)
									.nextDialogue(firstNo)
									.setNextAsEntrypoint())
							.addResponse(new ResponseBuilder(noMsg)
									.condition(isAnyEntityType(IGUANA))
									.addTrigger(new Trigger.SetFlag(noBarterFlag, false))
									.addPlayerMessage(noReplyMsg)
									.nextDialogue(firstNo)
									.setNextAsEntrypoint())
							.addResponse(new ResponseBuilder(noMsg)
									.condition(isAnyEntityType(SALAMANDER))
									.addTrigger(new Trigger.SetRandomFlag(List.of(barterFlag, noBarterFlag), false))
									.addPlayerMessage(noReplyMsg)
									.nextDialogue(firstNo)
									.setNextAsEntrypoint()))
					.defaultNode(new NodeBuilder(l.defaultKeyMsg("I'm hungry. Have any bugs? Maybe a chocolate chip cookie? Mmm."))));
		}));
		
		provider.addRandomlySelectable("rap_battle", defaultWeight(isAnyEntityType(NAKAGATOR, IGUANA)), new FolderedDialogue(builder ->
		{
			String rapA = "rap_a", rapB = "rap_b", rapC = "rap_c", rapD = "rap_d", rapE = "rap_e", rapF = "rap_f";
			
			var afterRap = builder.add("after_rap", new NodeBuilder()
					.addDescription(l.defaultKeyMsg("... that rap was really awful."))
					.addResponse(new ResponseBuilder(l.subMsg("school", "Schoo' that foo'!"))
							.addPlayerMessage(l.subMsg("school.reply", "All right, now it's my turn."))
							.addDescription(l.subMsg("school.desc","The %s proceeded to drop sick fire unlike any the %s had ever seen before.", Argument.PLAYER_TITLE, Argument.LAND_NAME))
							.setNextAsEntrypoint()
							.nextDialogue(builder.add("final", new NodeBuilder().addMessage(l.defaultKeyMsg("%s. You are the greatest rapper ever.", Argument.ENTITY_SOUND)))))
					.addResponse(new ResponseBuilder(l.subMsg("concede", "Let the poor guy think they won."))
							.addPlayerMessage(l.subMsg("concede.reply", "... wow. I'm just... not going to try to go against something like that."))
							.setNextAsEntrypoint()
							.nextDialogue(builder.add("concede", new NodeBuilder(l.defaultKeyMsg("%s, yes! I am the greatest rapper ever!", Argument.ENTITY_SOUND))))));
			
			var raps = builder.add("raps", new NodeSelectorBuilder()
					.node(new Condition.Flag(rapA), new NodeBuilder()
							.addMessage(l.subMsg("a1", "I see you carryin' a pick"))
							.addMessage(l.subMsg("a2", "You think you minin'? Sick"))
							.addMessage(l.subMsg("a3", "But uh..."))
							.addMessage(l.subMsg("a4", "you ain't. Word."))
							.next(afterRap))
					.node(new Condition.Flag(rapB), new NodeBuilder()
							.addMessage(l.subMsg("b1", "You're green and square, kinda beveled on the sides"))
							.addMessage(l.subMsg("b2", "And the corners I guess. Or are they called vertexes?"))
							.addMessage(l.subMsg("b3", "But I'm sayin' you're generic. Like, so generic"))
							.addMessage(l.subMsg("b4", "it doesn't make sense. ... uh, sorry for being a jerk."))
							.next(afterRap))
					.node(new Condition.Flag(rapC), new NodeBuilder()
							.addMessage(l.subMsg("c1", "Yeah. Yeah. Yeah. Yeah."))
							.addMessage(l.subMsg("c2", "Ooh. Ooh. Ooh. Ooh."))
							.addMessage(l.subMsg("c3", "%1$s. %1$s. %1$s. %1$s.", Argument.ENTITY_SOUND))
							.addMessage(l.subMsg("c4", "Yeah. Yeah. Yeah. Yeah."))
							.next(afterRap))
					.node(new Condition.Flag(rapD), new NodeBuilder()
							.addMessage(l.subMsg("d1", "I'm the Knight of Time, the god of sick beats"))
							.addMessage(l.subMsg("d2", "Settle down and lemme... why are you giving me that look?"))
							.addMessage(l.subMsg("d3", "This is my own original rap!"))
							.addMessage(l.subMsg("d4", "Really!"))
							.next(afterRap))
					.node(new Condition.Flag(rapE), new NodeBuilder()
							.addMessage(l.subMsg("e1", "Incaseyoucouldn'ttell,"))
							.addMessage(l.subMsg("e2", "you'reuglyandyousmell!"))
							.addMessage(l.subMsg("e3", "OOOHHHHHHHHH!"))
							.addMessage(l.subMsg("e4", "...That's how rap works, right?"))
							.next(afterRap))
					.defaultNode(new NodeBuilder()
							.addMessage(l.subMsg("f1", "Have a nice trip and I'll see you next fall!"))
							.addMessage(l.subMsg("f2", "I hope you don't mind that my house isn't tall,"))
							.addMessage(l.subMsg("f3", "'Cuz them things is dang'rous and although no one cares,"))
							.addMessage(l.subMsg("f4", "I'm tellin' ya, dawg, I WARNED YOU 'BOUT STAIRS!"))
							.next(afterRap)));
			
			builder.addStart(new NodeBuilder(l.defaultKeyMsg("I challenge you to a rap battle! Accept challenge? Y/N"))
					.addResponse(new ResponseBuilder(l.subMsg("accept", "Accept this consort's challenge!"))
							.addTrigger(new Trigger.SetRandomFlag(List.of(rapA, rapB, rapC, rapD, rapE, rapF), false))
							.addPlayerMessage(l.subMsg("accept.reply", "Y! I'll take you on! You can even go first."))
							.nextDialogue(raps)
							.setNextAsEntrypoint())
					.addResponse(new ResponseBuilder(l.subMsg("deny", "Don't bother with this guy."))
							.addPlayerMessage(l.subMsg("deny.reply", "N. Maybe later."))
							.nextDialogue(builder.add("deny", new NodeBuilder(l.defaultKeyMsg("Maybe one day I will find a challenger worthy of my greatness...."))))));
		}));
		
	}
}
