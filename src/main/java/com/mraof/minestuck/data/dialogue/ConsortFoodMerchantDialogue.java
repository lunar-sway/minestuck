package com.mraof.minestuck.data.dialogue;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.dialogue.RandomlySelectableDialogue;
import com.mraof.minestuck.entity.dialogue.Trigger;
import com.mraof.minestuck.item.loot.MSLootTables;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

import static com.mraof.minestuck.data.dialogue.SelectableDialogueProvider.defaultWeight;
import static com.mraof.minestuck.entity.MSEntityTypes.*;
import static com.mraof.minestuck.entity.dialogue.condition.Conditions.isAnyEntityType;

public final class ConsortFoodMerchantDialogue
{
	public static DataProvider create(PackOutput output, LanguageProvider enUsLanguageProvider)
	{
		SelectableDialogueProvider provider = new SelectableDialogueProvider(Minestuck.MOD_ID, RandomlySelectableDialogue.DialogueCategory.CONSORT_FOOD_MERCHANT, output);
		
		dialogue(provider, new DialogueLangHelper(enUsLanguageProvider));
		
		return provider;
	}
	
	private static void dialogue(SelectableDialogueProvider provider, DialogueLangHelper l)
	{
		provider.addRandomlySelectable("food_shop", defaultWeight(isAnyEntityType(SALAMANDER)),
				new DialogueProvider.NodeBuilder(l.defaultKeyMsg("You hungry? I bet you are! Why else would you be talking to me?"))
						.addClosingResponse(l.subMsg("bye", "Never mind"))
						.addResponse(new DialogueProvider.ResponseBuilder(l.subMsg("next", "What do you have?"))
								.addTrigger(new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_FOOD_STOCK))));
		provider.addRandomlySelectable("fast_food", defaultWeight(isAnyEntityType(NAKAGATOR)),
				new DialogueProvider.NodeBuilder(l.defaultKeyMsg("Welcome to MacCricket's, what would you like?"))
						.addClosingResponse(l.subMsg("bye", "I'm good"))
						.addResponse(new DialogueProvider.ResponseBuilder(l.subMsg("next", "Show me your menu"))
								.addTrigger(new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_FOOD_STOCK))));
		provider.addRandomlySelectable("grocery_store", defaultWeight(isAnyEntityType(IGUANA)),
				new DialogueProvider.NodeBuilder(l.defaultKeyMsg("Thank you for choosing Stop and Hop, this village's #1 one grocer!"))
						.addClosingResponse(l.subMsg("bye", "No thanks"))
						.addResponse(new DialogueProvider.ResponseBuilder(l.subMsg("next", "What do you have to sell?"))
								.addTrigger(new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_FOOD_STOCK))));
		provider.addRandomlySelectable("tasty_welcome", defaultWeight(isAnyEntityType(TURTLE)),
				new DialogueProvider.NodeBuilder(l.defaultKeyMsg("Welcome. I hope you find something tasty among our wares."))
						.addClosingResponse(l.subMsg("bye", "Goodbye"))
						.addResponse(new DialogueProvider.ResponseBuilder(l.subMsg("next", "Let me see your wares"))
								.addTrigger(new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_FOOD_STOCK))));
		
	}
}
