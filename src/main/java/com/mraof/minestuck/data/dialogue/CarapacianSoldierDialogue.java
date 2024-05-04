package com.mraof.minestuck.data.dialogue;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.data.dialogue.DialogueProvider.MessageProducer;
import com.mraof.minestuck.data.dialogue.DialogueProvider.NodeBuilder;
import com.mraof.minestuck.data.dialogue.DialogueProvider.NodeSelectorBuilder;
import com.mraof.minestuck.data.dialogue.DialogueProvider.ResponseBuilder;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.entity.dialogue.DialogueMessage;
import com.mraof.minestuck.entity.dialogue.RandomlySelectableDialogue;
import com.mraof.minestuck.entity.dialogue.Trigger;
import com.mraof.minestuck.entity.dialogue.condition.Condition;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.neoforged.neoforge.common.data.LanguageProvider;

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
		provider.addRandomlySelectable("dread_waiting", defaultWeight(isInLand()),
				descriptionNode(l.defaultKeyMsg("They explain that sometimes it's more exhausting to be posted on a Land since there isn't any fighting. If you aren't in combat you have time to think. That's when the dread sets in.")));
		
		provider.addRandomlySelectable("forlorn", defaultWeight(isInSkaia()),
				descriptionNode(l.defaultKeyMsg("Sometimes they wish they could just all set their weapons down and be done with this war.")));
		
		provider.addRandomlySelectable("constant_nakking", defaultWeight(isInConsortLand(EnumConsort.NAKAGATOR)),
				descriptionNode(l.defaultKeyMsg("They describe how they have a huge headache from the constant nakking.")));
		
		provider.addRandomlySelectable("too_quiet", defaultWeight(isInConsortLand(EnumConsort.TURTLE)),
				descriptionNode(l.defaultKeyMsg("They look around paranoid, saying that while the turtles are a nice change of pace from the other consorts they've come across, they are so quiet its unnerving.")));
		
		provider.addRandomlySelectable("constant_thipping", defaultWeight(isInConsortLand(EnumConsort.IGUANA)),
				descriptionNode(l.defaultKeyMsg("One of the nearby iguanas asked for some food and when they gave it to them the iguana complained for an hour about the \"lack of a comprehensive flavor profile\".")));
		
		provider.addRandomlySelectable("glub_glub", defaultWeight(isInConsortLand(EnumConsort.SALAMANDER)),
				descriptionNode(l.defaultKeyMsg("They hear the local salamanders glub so often that it's started to become part of their own vocabulary. Glub glub.")));
		
		provider.addRandomlySelectable("rain_sleet_or_snow", defaultWeight(alwaysTrue()),
				descriptionNode(l.defaultKeyMsg("They sigh and share that they would love to deliver mail one day. That has to be one of the most honorable professions out there.")));
		
		provider.addRandomlySelectable("dreamer", defaultWeight(alwaysTrue()), new FolderedDialogue(builder ->
		{
			var explainDreamers = builder.add("explain_dreamers", new ChainBuilder()
					.node(descriptionNode(l.defaultKeyMsg("They explain that Dreamers are heroes who reside in towers on the moons of Prospit and Derse.")))
					.node(descriptionNode(l.defaultKeyMsg("There is one for each Land that exists, although they don't know what the connection is there.")))
			);
			
			builder.addStart(new NodeSelectorBuilder()
					.node(hasEntered(), descriptionNode(l.subMsg("familiar", "They look at you with surprise, saying you remind them of the Dreamers."))
							.addResponse(new ResponseBuilder(l.subMsg("who", "Who are the Dreamers?"))
									.nextDialogue(explainDreamers)))
					.defaultNode(descriptionNode(l.subMsg("unfamiliar", "They are curious as to who you are and what you are doing here."))));
		}));
		
		provider.addRandomlySelectable("not_so_bad", defaultWeight(isInSkaia()), new ChainBuilder()
				.node(descriptionNode(l.defaultKeyMsg("They recount one time where they went out to an isolated part of the Battlefield. Unexpectedly, a member of enemy scouting party came across them while sat on a hill side.")))
				.node(descriptionNode(l.defaultKeyMsg("They went to draw their weapon only to realize they forgot it back at camp. The two soldiers stared at each other for what felt like ages.")))
				.node(descriptionNode(l.defaultKeyMsg("To make things worse, the rest of the scouting party called out to see what was going on. But just when they thought their life was coming to an end, the other soldier pretended like they weren't there and announced that the area was clear.")))
				.node(descriptionNode(l.defaultKeyMsg("They never ran so fast, trying to get back somewhere safe. And they certainly never leave anywhere without a weapon now.")))
		);
		
		provider.addRandomlySelectable("enemy", defaultWeight(all(isInSkaia(), isProspitian())),
				descriptionNode(l.defaultKeyMsg("They are panicking about whether any Dersites are nearby.")));
		
		provider.addRandomlySelectable("propaganda", defaultWeight(all(isInSkaia(), isDersite())),
				descriptionNode(l.defaultKeyMsg("They already knew that the propaganda surrounding Prospitians was fake but it was still surprising to see that they didn't have horns or cool fangs or blood red eyes. Kind of a bummer honestly.")));
		
		provider.addRandomlySelectable("sword_barter", weighted(40, isHolding(MSItems.REGISWORD.get())), new FolderedDialogue(builder ->
		{
			var goodbye = l.subMsg("goodbye", "That's really strange. Good luck with that!");
			
			var thanks = builder.add("thanks", descriptionNode(l.defaultKeyMsg("They thank you for helping them.")));
			
			var barter = builder.add("barter", descriptionNode(l.defaultKeyMsg("They ask if you have a new sword for them."))
					.addResponse(new ResponseBuilder(l.subMsg("give_item", "[Give them the %s]", DialogueMessage.Argument.MATCHED_ITEM))
							.addPlayerMessage(l.subMsg("give_item.reply", "Here you go!"))
							.visibleCondition(l.subText("give_item.condition", "Must have a sword, excluding a regisword"), new Condition.ItemTagMatchExclude(ItemTags.SWORDS, MSItems.REGISWORD.get()))
							.addTrigger(new Trigger.SetNPCMatchedItem(EquipmentSlot.MAINHAND))
							.addTrigger(new Trigger.GiveItem(MSItems.REGISWORD.get(), 1))
							.addTrigger(new Trigger.SetDialogue(thanks))
							.nextDialogue(thanks))
					.addClosingResponse(l.subMsg("not_yet", "Not yet.")));
			
			var badMood = builder.add("bad_mood", descriptionNode(l.defaultKeyMsg("They explain that the arch agent does not always get along with the Black Queen and when he gets in a bad mood he allegedly will order his minions to go and distribute a bunch of these swords around. Apparently the queen is even aware of it and finds it amusing.")));
			
			var frequently = builder.add("frequently", descriptionNode(l.defaultKeyMsg("They mention that the last few times they were outside the palace, there was a bin labelled \"Regicide weapon disposal\". It was always full.")));
			
			builder.addStart(new ChainBuilder()
					.node(descriptionNode(l.defaultKeyMsg("They mention that the weirdest thing happened earlier.")))
					.node(descriptionNode(l.defaultKeyMsg("A Dersite agent wandered up to them and shoved this sword into their hands. He loudly exclaimed that it would be such a shame if a member of royalty was \"forcibly retired\" because anyone who would do such a thing \"might just become the wealthiest carapacian to ever live\".")))
					.node(descriptionNode(l.defaultKeyMsg("Not having another sword they just kind of brought it into battle, but now they wish it would be gone."))
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
	
	private static NodeBuilder descriptionNode(MessageProducer message)
	{
		return new NodeBuilder(DialogueLangHelper.msg(DialogueProvider.DOTS)).addDescription(message);
	}
}