package com.mraof.minestuck.data.dialogue;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.data.dialogue.DialogueProvider.NodeBuilder;
import com.mraof.minestuck.data.dialogue.DialogueProvider.ResponseBuilder;
import com.mraof.minestuck.entity.dialogue.DialogueMessage.Argument;
import com.mraof.minestuck.entity.dialogue.RandomlySelectableDialogue;
import com.mraof.minestuck.entity.dialogue.Trigger;
import com.mraof.minestuck.entity.dialogue.condition.Condition;
import com.mraof.minestuck.item.loot.MSLootTables;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

import static com.mraof.minestuck.data.dialogue.SelectableDialogueProvider.defaultWeight;
import static com.mraof.minestuck.entity.MSEntityTypes.*;
import static com.mraof.minestuck.entity.dialogue.condition.Conditions.isAnyEntityType;

public final class ShadyConsortDialogue
{
	public static DataProvider create(PackOutput output, LanguageProvider enUsLanguageProvider)
	{
		SelectableDialogueProvider provider = new SelectableDialogueProvider(Minestuck.MOD_ID, RandomlySelectableDialogue.DialogueCategory.SHADY_CONSORT, output);
		
		dialogue(provider, new DialogueLangHelper(Minestuck.MOD_ID, enUsLanguageProvider));
		
		return provider;
	}
	
	private static void dialogue(SelectableDialogueProvider provider, DialogueLangHelper l)
	{
		provider.addRandomlySelectable("shady_offer", defaultWeight(isAnyEntityType(SALAMANDER, TURTLE)), new FolderedDialogue(builder -> {
			var purchase = builder.add("purchase", new NodeBuilder(l.defaultKeyMsg("Thanks for your cash.")));
			
			var shadyOffer2 = builder.add("next", new NodeBuilder(l.defaultKeyMsg("You're missin' out kiddo. %s it. I'll sell you this thing for 500 boondollars.", Argument.ENTITY_SOUND))	//todo the entity sound for turtles doesn't work well here
					.addResponse(new ResponseBuilder(l.subMsg("buy", "buy it already! [Pay 500 boondollars]"))
							.visibleCondition(new Condition.PlayerHasBoondollars(500))
							.addTrigger(new Trigger.AddBoondollars(-500))
							.addTrigger(new Trigger.AddConsortReputation(-35))
							.addTrigger(new Trigger.GiveFromLootTable(MSLootTables.CONSORT_JUNK_REWARD))
							.addPlayerMessage(l.subMsg("buy.reply", " Sure! Fine! Ok! Jeez..."))
							.nextDialogue(purchase)
							.setNextAsEntrypoint())
					.addClosingResponse(l.subMsg("deny", "It may be a deception, do not buy the \"thing\"!")));
			
			builder.addStart(new NodeBuilder(l.defaultKeyMsg("Hey kid... I'll give you something special for 1000 boondollars..."))
					.addDescription(l.subMsg("desc", "This %s seems pretty shady. You're not sure whether or not to trust them...", Argument.ENTITY_TYPE))
					.addResponse(new ResponseBuilder(l.subMsg("buy", "Buy \"Something special\" [Pay 1000 boondollars]"))
							.visibleCondition(new Condition.PlayerHasBoondollars(1000))
							.addTrigger(new Trigger.AddBoondollars(-1000))
							.addTrigger(new Trigger.AddConsortReputation(-15))
							.addTrigger(new Trigger.GiveFromLootTable(MSLootTables.CONSORT_JUNK_REWARD))
							.addTrigger(new Trigger.SetPlayerDialogue(purchase))
							.addPlayerMessage(l.subMsg("buy.reply", "Ok... Sure??"))
							.nextDialogue(builder.add("item", new NodeBuilder(l.defaultKeyMsg("Here, kid.")))))
					.addResponse(new ResponseBuilder(l.subMsg("deny", "Do not buy from the shadowy dealer."))
							.addPlayerMessage(l.subMsg("deny.reply", "Uh, no thanks..."))
							.nextDialogue(shadyOffer2)));
		}));
		
		provider.addRandomlySelectable("peppy_offer", defaultWeight(isAnyEntityType(NAKAGATOR, IGUANA)), new FolderedDialogue(builder -> {
			var purchase = builder.add("purchase", new NodeBuilder(l.defaultKeyMsg("Thank you for your money!")));
			
			var peppyOffer2 = builder.add("next", new NodeBuilder(l.defaultKeyMsg("Oh! No worries! How about I sell it to you for just 500 boondollars instead??"))
					.addResponse(new ResponseBuilder(l.subMsg("buy", "Buy that cheap item! [pay 500 boondollars]"))
							.visibleCondition(new Condition.PlayerHasBoondollars(500))
							.addTrigger(new Trigger.AddBoondollars(-500))
							.addTrigger(new Trigger.AddConsortReputation(-35))
							.addTrigger(new Trigger.GiveFromLootTable(MSLootTables.CONSORT_JUNK_REWARD))
							.addPlayerMessage(l.subMsg("buy.reply", "Sure! I'd be glad to!"))
							.nextDialogue(purchase)
							.setNextAsEntrypoint())
					.addClosingResponse(l.subMsg("deny", "Still do not buy from cheery salesman.")));
			
			builder.addStart(new NodeBuilder(l.defaultKeyMsg("Hey there! I've got a wonderful item here for just 1000 boondollars? How about it kid?"))
					.addDescription(l.subMsg("desc", "This %s is way too nice to ever scam you! Surely you can trust them?", Argument.ENTITY_TYPE))
					.addResponse(new ResponseBuilder(l.subMsg("buy", "Buy \"Wonderful item\" [Pay 1000 boondollars]"))
							.visibleCondition(new Condition.PlayerHasBoondollars(1000))
							.addTrigger(new Trigger.AddBoondollars(-1000))
							.addTrigger(new Trigger.AddConsortReputation(-15))
							.addTrigger(new Trigger.GiveFromLootTable(MSLootTables.CONSORT_JUNK_REWARD))
							.addTrigger(new Trigger.SetPlayerDialogue(purchase))
							.addPlayerMessage(l.subMsg("buy.reply", "Sure! Why not?"))
							.nextDialogue(builder.add("item", new NodeBuilder(l.defaultKeyMsg("Here you are!")))))
					.addResponse(new ResponseBuilder(l.subMsg("deny", "Do not buy from cheery salesman."))
							.addPlayerMessage(l.subMsg("deny.reply", "No thanks! I'm short on cash."))
							.nextDialogue(peppyOffer2)));
		}));
	}
}
