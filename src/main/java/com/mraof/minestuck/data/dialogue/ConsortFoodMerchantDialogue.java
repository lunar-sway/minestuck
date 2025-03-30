package com.mraof.minestuck.data.dialogue;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.data.dialogue.DialogueProvider.NodeBuilder;
import com.mraof.minestuck.data.dialogue.DialogueProvider.ResponseBuilder;
import com.mraof.minestuck.entity.dialogue.DialogueMessage.Argument;
import com.mraof.minestuck.entity.dialogue.RandomlySelectableDialogue;
import com.mraof.minestuck.entity.dialogue.Trigger;
import com.mraof.minestuck.item.loot.MSLootTables;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

import static com.mraof.minestuck.data.dialogue.DialogueLangHelper.msg;
import static com.mraof.minestuck.data.dialogue.DialogueProvider.ARROW;
import static com.mraof.minestuck.data.dialogue.SelectableDialogueProvider.defaultWeight;
import static com.mraof.minestuck.entity.MSEntityTypes.*;
import static com.mraof.minestuck.entity.dialogue.condition.Conditions.*;
import static com.mraof.minestuck.world.lands.LandTypes.*;

public final class ConsortFoodMerchantDialogue
{
	private static final Trigger.OpenConsortMerchantGui SHOP_TRIGGER = new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_FOOD_STOCK);
	
	public static DataProvider create(PackOutput output, LanguageProvider enUsLanguageProvider)
	{
		SelectableDialogueProvider provider = new SelectableDialogueProvider(Minestuck.MOD_ID, RandomlySelectableDialogue.DialogueCategory.CONSORT_FOOD_MERCHANT, output);
		
		dialogue(provider, new DialogueLangHelper(Minestuck.MOD_ID, enUsLanguageProvider));
		
		return provider;
	}
	
	private static void dialogue(SelectableDialogueProvider provider, DialogueLangHelper l)
	{
		provider.addRandomlySelectable("food_shop", defaultWeight(isAnyEntityType(SALAMANDER)).keepOnReset(),
				new NodeBuilder(l.defaultKeyMsg("You hungry? I bet you are! Why else would you be talking to me?"))
						.addClosingResponse(l.subMsg("bye", "Never mind"))
						.addResponse(new ResponseBuilder(l.subMsg("next", "What do you have?")).addTrigger(SHOP_TRIGGER)));
		provider.addRandomlySelectable("fast_food", defaultWeight(isAnyEntityType(NAKAGATOR)).keepOnReset(),
				new NodeBuilder(l.defaultKeyMsg("Welcome to MacCricket's, what would you like?"))
						.addClosingResponse(l.subMsg("bye", "I'm good"))
						.addResponse(new ResponseBuilder(l.subMsg("next", "Show me your menu")).addTrigger(SHOP_TRIGGER)));
		provider.addRandomlySelectable("grocery_store", defaultWeight(isAnyEntityType(IGUANA)).keepOnReset(),
				new NodeBuilder(l.defaultKeyMsg("Thank you for choosing Stop and Hop, this village's #1 one grocer!"))
						.addClosingResponse(l.subMsg("bye", "No thanks"))
						.addResponse(new ResponseBuilder(l.subMsg("next", "What do you have to sell?")).addTrigger(SHOP_TRIGGER)));
		provider.addRandomlySelectable("tasty_welcome", defaultWeight(isAnyEntityType(TURTLE)).keepOnReset(),
				new NodeBuilder(l.defaultKeyMsg("Welcome. I hope you find something tasty among our wares."))
						.addClosingResponse(l.subMsg("bye", "Goodbye"))
						.addResponse(new ResponseBuilder(l.subMsg("next", "Let me see your wares")).addTrigger(SHOP_TRIGGER)));
		
		//todo these have not been made as fancy with responses as the ones above. Do we want to work on that?
		provider.addRandomlySelectable("breeze", defaultWeight(isInTitleLand(WIND)).keepOnReset(),
				new NodeBuilder(l.defaultKeyMsg("It's hard to catch our food with the Breeze carrying them away and whatnot!"))
						.addResponse(new ResponseBuilder(msg(ARROW)).addTrigger(SHOP_TRIGGER)));
		provider.addRandomlySelectable("blood", defaultWeight(isInTitleLand(PULSE)).keepOnReset(),
				new NodeBuilder(l.defaultKeyMsg("Hope you like red meat!"))
						.addResponse(new ResponseBuilder(msg(ARROW)).addTrigger(SHOP_TRIGGER)));
		provider.addRandomlySelectable("rabbit", defaultWeight(isInTitleLand(RABBITS)).keepOnReset(),
				new NodeBuilder(l.defaultKeyMsg("Rabbit stew! Mmmm!"))
						.addResponse(new ResponseBuilder(msg(ARROW)).addTrigger(SHOP_TRIGGER)));
		provider.addRandomlySelectable("lightning", defaultWeight(isInTitleLand(THUNDER)).keepOnReset(),
				new NodeBuilder(l.defaultKeyMsg("Lightning-smoked meat really adds to the flavor!"))
						.addResponse(new ResponseBuilder(msg(ARROW)).addTrigger(SHOP_TRIGGER)));
		provider.addRandomlySelectable("frog_leg", defaultWeight(isInTitleLand(FROGS)).keepOnReset(),
				new NodeBuilder(l.defaultKeyMsg("Frog legs are good but eating them is heresy! Buy some of our food instead!"))
						.addResponse(new ResponseBuilder(msg(ARROW)).addTrigger(SHOP_TRIGGER)));
		provider.addRandomlySelectable("frog", defaultWeight(isInTitleLand(FROGS)).keepOnReset(),
				new NodeBuilder(l.defaultKeyMsg("We would never eat frogs here in the %s! Grasshoppers, though...", Argument.LAND_NAME))
						.addResponse(new ResponseBuilder(msg(ARROW)).addTrigger(SHOP_TRIGGER)));
		provider.addRandomlySelectable("time", defaultWeight(isInTitleLand(CLOCKWORK)).keepOnReset(),
				new NodeBuilder(l.defaultKeyMsg("Tick Tock, time's a-wasting! Eat something or leave!"))
						.addResponse(new ResponseBuilder(msg(ARROW)).addTrigger(SHOP_TRIGGER)));
		provider.addRandomlySelectable("thyme", defaultWeight(any(isInTitleLand(CLOCKWORK), isInTitleLand(THOUGHT))).keepOnReset(),
				new NodeBuilder(l.defaultKeyMsg("Take your mind off of the limited time we have left alive with food!"))
						.addResponse(new ResponseBuilder(msg(ARROW)).addTrigger(SHOP_TRIGGER)));
		provider.addRandomlySelectable("library", defaultWeight(isInTitleLand(THOUGHT)).keepOnReset(),
				new NodeBuilder(l.defaultKeyMsg("I'm selling recipes I made from a nearby library's cookbook. Buy some!"))
						.addResponse(new ResponseBuilder(msg(ARROW)).addTrigger(SHOP_TRIGGER)));
		provider.addRandomlySelectable("cake", defaultWeight(isInTitleLand(CAKE)).keepOnReset(),
				new NodeBuilder(l.defaultKeyMsg("Who needs cake when you have crickets!"))
						.addResponse(new ResponseBuilder(msg(ARROW)).addTrigger(SHOP_TRIGGER)));
		provider.addRandomlySelectable("light", defaultWeight(isInTitleLand(LIGHT)).keepOnReset(),
				new NodeBuilder(l.defaultKeyMsg("Crickets are nocturnal so our species' main food source is hard to find here. Oh well!"))
						.addResponse(new ResponseBuilder(msg(ARROW)).addTrigger(SHOP_TRIGGER)));
		provider.addRandomlySelectable("silence", defaultWeight(isInTitleLand(SILENCE)).keepOnReset(),
				new NodeBuilder(l.defaultKeyMsg("This place used to be filled with the sounds of crickets until we captured them all for food!"))
						.addResponse(new ResponseBuilder(msg(ARROW)).addTrigger(SHOP_TRIGGER)));
		provider.addRandomlySelectable("rage", defaultWeight(isInTitleLand(MSTags.TitleLandTypes.MONSTERS)).keepOnReset(),
				new NodeBuilder(l.defaultKeyMsg("You're not you when you're hungry. Buy a snickers!"))
						.addResponse(new ResponseBuilder(msg(ARROW)).addTrigger(SHOP_TRIGGER)));
		provider.addRandomlySelectable("hope", defaultWeight(isInTitleLand(TOWERS)).keepOnReset(),
				new NodeBuilder(l.defaultKeyMsg("I HOPE you're hungry!"))
						.addDescription(l.subMsg("desc", "You hear a faint ba-dum tss in the distance."))
						.addResponse(new ResponseBuilder(msg(ARROW)).addTrigger(SHOP_TRIGGER)));
		provider.addRandomlySelectable("buckets", defaultWeight(isInTitleLand(BUCKETS)).keepOnReset(),
				new NodeBuilder(l.defaultKeyMsg("Ever soaked your grist candies in the liquids the buckets contain? It really enhances the taste!"))
						.addResponse(new ResponseBuilder(msg(ARROW)).addTrigger(SHOP_TRIGGER)));
		
	}
}
