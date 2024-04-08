package com.mraof.minestuck.data.dialogue;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.data.dialogue.DialogueProvider.NodeBuilder;
import com.mraof.minestuck.data.dialogue.DialogueProvider.MessageProducer;
import com.mraof.minestuck.data.dialogue.DialogueProvider.ResponseBuilder;
import com.mraof.minestuck.entity.dialogue.DialogueMessage;
import com.mraof.minestuck.entity.dialogue.RandomlySelectableDialogue;
import com.mraof.minestuck.entity.dialogue.Trigger;
import com.mraof.minestuck.entity.dialogue.condition.Condition;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.common.data.LanguageProvider;

import static com.mraof.minestuck.data.dialogue.SelectableDialogueProvider.defaultWeight;
import static com.mraof.minestuck.data.dialogue.SelectableDialogueProvider.weighted;
import static com.mraof.minestuck.entity.dialogue.condition.Conditions.*;

public final class CarapacianSoldierDialogue
{
	public static DataProvider create(PackOutput output, LanguageProvider enUsLanguageProvider)
	{
		SelectableDialogueProvider provider = new SelectableDialogueProvider(Minestuck.MOD_ID, RandomlySelectableDialogue.DialogueCategory.CARAPACIAN_SOLDIER, output);
		DialogueLangHelper l = new DialogueLangHelper(Minestuck.MOD_ID, enUsLanguageProvider);
		
		//Run dialogue creation early so that language stuff gets added before the language provider generates its file
		carapacianDialogues(provider, l);
		
		return provider;
	}
	
	private static void carapacianDialogues(SelectableDialogueProvider provider, DialogueLangHelper l)
	{
		final MessageProducer dots = l.msg(DialogueProvider.DOTS);
		
		provider.addRandomlySelectable("dread_waiting", defaultWeight(isInLand()),
				new NodeBuilder(dots).addDescription(l.defaultKeyMsg("They explain that sometimes it's more exhausting to be posted on a Land since there isn't any fighting. If you aren't in combat you have time to think. That's when the dread sets in.")));
		
		provider.addRandomlySelectable("enemy", defaultWeight(all(isInSkaia(), isProspitian())),
				new NodeBuilder(dots).addDescription(l.defaultKeyMsg("They are panicking about whether any Dersites are nearby.")));
		
		provider.addRandomlySelectable("sword_barter", weighted(40, isHolding(MSItems.REGISWORD.get())), new FolderedDialogue(builder ->
		{
			var goodbye = l.subMsg("goodbye", "That's really strange. Good luck with that!");
			
			var thanks = builder.add("thanks", new NodeBuilder(dots).addDescription(l.defaultKeyMsg("They thank you for helping them.")));
			
			var barter = builder.add("barter", new NodeBuilder(dots).addDescription(l.defaultKeyMsg("They ask if you have a new sword for them."))
					.addResponse(new ResponseBuilder(l.subMsg("give_item", "[Give them the %s]", DialogueMessage.Argument.MATCHED_ITEM))
							.addPlayerMessage(l.subMsg("give_item.reply", "Here you go!"))
							.visibleCondition(resource -> "Must have a sword and must not be carrying a regisword", all(new Condition.ItemTagMatch(ItemTags.SWORDS), none(new Condition.PlayerHasItem(MSItems.REGISWORD.get(), 1))))
							.addTrigger(new Trigger.SetNPCMatchedItem(EquipmentSlot.MAINHAND))
							.addTrigger(new Trigger.GiveItem(MSItems.REGISWORD.get(), 1))
							.addTrigger(new Trigger.SetDialogue(thanks))
							.nextDialogue(thanks))
					.addClosingResponse(l.subMsg("not_yet", "Not yet.")));
			
			var badMood = builder.add("bad_mood", new NodeBuilder(dots).addDescription(l.defaultKeyMsg("They explain that the arch agent does not always get along with the Black Queen and when he gets in a bad mood he allegedly will order his minions to go and distribute a bunch of these swords around. Apparently the queen is even aware of it and finds it amusing.")));
			
			var frequently = builder.add("frequently", new NodeBuilder(dots).addDescription(l.defaultKeyMsg("They mention that the last few times they were outside the palace, there was a bin labelled \"Regicide weapon disposal\". It was always full.")));
			
			builder.addStart(new ChainBuilder()
					.node(new NodeBuilder(dots).addDescription(l.defaultKeyMsg("They mention that the weirdest thing happened earlier.")))
					.node(new NodeBuilder(dots).addDescription(l.defaultKeyMsg("A Dersite agent wandered up to them and shoved this sword into their hands. He loudly exclaimed that it would be such a shame if a member of royalty was \"forcibly retired\" because anyone who would do such a thing \"might just become the wealthiest carapacian to ever live\".")))
					.node(new NodeBuilder(dots).addDescription(l.defaultKeyMsg("Not having another sword they just kind of brought it into battle, but now they wish it would be gone."))
							.addResponse(new ResponseBuilder(l.subMsg("suggestion", "Maybe I could find you something else to use?"))
									.nextDialogue(barter)
									.setNextAsEntrypoint())
							.addResponse(new ResponseBuilder(l.subMsg("derse_loyalty", "Why would an agent of Derse want the Dersite royalty dead?"))
									.condition(isDersite())
									.nextDialogue(badMood))
							.addResponse(new ResponseBuilder(l.subMsg("prospitian_loyalty", "Have the agents tried anything like this before?"))
									.condition(isProspitian())
									.nextDialogue(frequently))
							.addClosingResponse(goodbye)
					)
			);
		}));
	}
}
