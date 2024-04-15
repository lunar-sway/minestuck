package com.mraof.minestuck.data.dialogue;

import com.mraof.minestuck.Minestuck;
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
import static com.mraof.minestuck.entity.dialogue.condition.Conditions.*;
import static com.mraof.minestuck.world.lands.LandTypes.*;

public final class ConsortGeneralMerchantDialogue
{
	private static final Trigger.OpenConsortMerchantGui SHOP_TRIGGER = new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_GENERAL_STOCK);
	
	public static DataProvider create(PackOutput output, LanguageProvider enUsLanguageProvider)
	{
		SelectableDialogueProvider provider = new SelectableDialogueProvider(Minestuck.MOD_ID, RandomlySelectableDialogue.DialogueCategory.CONSORT_GENERAL_MERCHANT, output);
		
		dialogue(provider, new DialogueLangHelper(Minestuck.MOD_ID, enUsLanguageProvider));
		
		return provider;
	}
	
	private static void dialogue(SelectableDialogueProvider provider, DialogueLangHelper l)
	{
		provider.addRandomlySelectable("generic", defaultWeight(alwaysTrue()).keepOnReset(),
				new DialogueProvider.NodeBuilder(l.defaultKeyMsg("We have generic goods for generic people!"))
						.addResponse(new DialogueProvider.ResponseBuilder(msg(ARROW)).addTrigger(SHOP_TRIGGER)));
		provider.addRandomlySelectable("got_the_goods", defaultWeight(alwaysTrue()).keepOnReset(),
				new DialogueProvider.NodeBuilder(l.defaultKeyMsg("You want the goods? We got the goods."))
						.addResponse(new DialogueProvider.ResponseBuilder(msg(ARROW)).addTrigger(SHOP_TRIGGER)));
		provider.addRandomlySelectable("top_tier", defaultWeight(alwaysTrue()).keepOnReset(),
				new DialogueProvider.NodeBuilder(l.defaultKeyMsg("We have top tier goods for a top tier god!"))
						.addResponse(new DialogueProvider.ResponseBuilder(msg(ARROW)).addTrigger(SHOP_TRIGGER)));
		
		provider.addRandomlySelectable("breeze", defaultWeight(isInTitleLand(WIND)).keepOnReset(),
				new DialogueProvider.NodeBuilder(l.defaultKeyMsg("The breeze has brought me many fine wares."))
						.addResponse(new DialogueProvider.ResponseBuilder(msg(ARROW)).addTrigger(SHOP_TRIGGER)));
		provider.addRandomlySelectable("blood", defaultWeight(isInTitleLand(PULSE)).keepOnReset(),
				new DialogueProvider.NodeBuilder(l.defaultKeyMsg("I accept payment in blood. And boondollars. I also take checks."))
						.addResponse(new DialogueProvider.ResponseBuilder(msg(ARROW)).addTrigger(SHOP_TRIGGER)));
		provider.addRandomlySelectable("life", defaultWeight(isInTitleLand(RABBITS)).keepOnReset(),
				new DialogueProvider.NodeBuilder(l.defaultKeyMsg("Twenty years selling losers hats and shit really takes the life out of you."))
						.addResponse(new DialogueProvider.ResponseBuilder(msg(ARROW)).addTrigger(SHOP_TRIGGER)));
		provider.addRandomlySelectable("doom", defaultWeight(isInTitleLand(THUNDER)).keepOnReset(),
				new DialogueProvider.NodeBuilder(l.defaultKeyMsg("With my prices, my competitors are doomed!"))
						.addResponse(new DialogueProvider.ResponseBuilder(msg(ARROW)).addTrigger(SHOP_TRIGGER)));
		provider.addRandomlySelectable("frog", defaultWeight(isInTitleLand(FROGS)).keepOnReset(),
				new DialogueProvider.NodeBuilder(l.defaultKeyMsg("Get your frog merchandise here! Limited time only! ....Just kidding, I'm always here."))
						.addResponse(new DialogueProvider.ResponseBuilder(msg(ARROW)).addTrigger(SHOP_TRIGGER)));
		provider.addRandomlySelectable("time", defaultWeight(isInTitleLand(CLOCKWORK)).keepOnReset(),
				new DialogueProvider.NodeBuilder(l.defaultKeyMsg("Future me has told me that some sucker would buy tons of \"goods\" from me... will that sucker be you?"))
						.addResponse(new DialogueProvider.ResponseBuilder(msg(ARROW)).addTrigger(SHOP_TRIGGER)));
		provider.addRandomlySelectable("book", defaultWeight(isInTitleLand(THOUGHT)).keepOnReset(),
				new DialogueProvider.NodeBuilder(l.defaultKeyMsg("Books have taught us how to sell as much useless crap as possible, want some?"))
						.addResponse(new DialogueProvider.ResponseBuilder(msg(ARROW)).addTrigger(SHOP_TRIGGER)));
		provider.addRandomlySelectable("cake", defaultWeight(isInTitleLand(CAKE)).keepOnReset(),
				new DialogueProvider.NodeBuilder(l.defaultKeyMsg("Don't buy goods here. Cake is our specialty, not general goods. Very low quality."))
						.addResponse(new DialogueProvider.ResponseBuilder(msg(ARROW)).addTrigger(SHOP_TRIGGER)));
		provider.addRandomlySelectable("light", defaultWeight(isInTitleLand(LIGHT)).keepOnReset(),
				new DialogueProvider.NodeBuilder(l.defaultKeyMsg("You may be blind but I'm sure you can see our prices are fantastic!"))
						.addResponse(new DialogueProvider.ResponseBuilder(msg(ARROW)).addTrigger(SHOP_TRIGGER)));
		provider.addRandomlySelectable("silence", defaultWeight(isInTitleLand(SILENCE)).keepOnReset(),
				new DialogueProvider.NodeBuilder(l.defaultKeyMsg("This land may be silent but our prices will make you SCREAM!... with joy, of course!"))
						.addResponse(new DialogueProvider.ResponseBuilder(msg(ARROW)).addTrigger(SHOP_TRIGGER)));
		provider.addRandomlySelectable("rage", defaultWeight(isInTitleLand(MSTags.TitleLandTypes.MONSTERS)).keepOnReset(),
				new DialogueProvider.NodeBuilder(l.defaultKeyMsg("Other store's prices are INFURIATING! Our prices, however, will soothe that frustration!"))
						.addResponse(new DialogueProvider.ResponseBuilder(msg(ARROW)).addTrigger(SHOP_TRIGGER)));
		provider.addRandomlySelectable("tower", defaultWeight(isInTitleLand(TOWERS)).keepOnReset(),
				new DialogueProvider.NodeBuilder(l.defaultKeyMsg("I got the goods; they're all recently raided from a nearby tower!"))
						.addResponse(new DialogueProvider.ResponseBuilder(msg(ARROW)).addTrigger(SHOP_TRIGGER)));
		provider.addRandomlySelectable("buckets", defaultWeight(isInTitleLand(BUCKETS)).keepOnReset(),
				new DialogueProvider.NodeBuilder(l.defaultKeyMsg("Here's a witty one-liner tying in buckets and general stores!"))
						.addResponse(new DialogueProvider.ResponseBuilder(msg(ARROW)).addTrigger(SHOP_TRIGGER)));
		
		provider.addRandomlySelectable("eye_catching", defaultWeight(isInTerrainLand(RAINBOW)).keepOnReset(),
				new DialogueProvider.NodeBuilder(l.defaultKeyMsg("Looking for something that won't catch your eye? You've come to the right place!"))
						.addResponse(new DialogueProvider.ResponseBuilder(msg(ARROW)).addTrigger(SHOP_TRIGGER)));
	}
}
