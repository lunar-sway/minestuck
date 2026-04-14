package com.mraof.minestuck.data.dialogue;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.data.dialogue.DialogueProvider.NodeBuilder;
import com.mraof.minestuck.data.dialogue.DialogueProvider.NodeSelectorBuilder;
import com.mraof.minestuck.data.dialogue.DialogueProvider.ResponseBuilder;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.mraof.minestuck.entity.dialogue.Trigger.GoToBlock;
import static com.mraof.minestuck.entity.dialogue.condition.Conditions.*;

public final class IndividualizedDialogue
{
	public static DataProvider create(PackOutput output, LanguageProvider enUsLanguageProvider, CompletableFuture<HolderLookup.Provider> lookup)
	{
		DialogueProvider provider = new DialogueProvider(Minestuck.MOD_ID, "individual", lookup, output);
		DialogueLangHelper l = new DialogueLangHelper(Minestuck.MOD_ID, enUsLanguageProvider);
		
		//Run dialogue creation early so that language stuff gets added before the language provider generates its file
		dialogues(provider, l);
		
		return provider;
	}
	
	private static void dialogues(DialogueProvider provider, DialogueLangHelper l)
	{
		provider.getLookupProvider().thenCompose(providerIt ->
		{
			provider.add("kernelsprite", new FolderedDialogue(builder -> {
				List<Block> cruxtruderBlocks = new ArrayList<>();
				MSBlocks.CRUXTRUDER.forEachBlock(cruxtruderBlocks::add);
				List<Block> latheBlocks = new ArrayList<>();
				MSBlocks.TOTEM_LATHE.forEachBlock(latheBlocks::add);
				List<Block> alchemiterBlocks = new ArrayList<>();
				MSBlocks.ALCHEMITER.forEachBlock(alchemiterBlocks::add);
				List<Block> gateBlocks = new ArrayList<>(List.of(MSBlocks.GATE.get(), MSBlocks.GATE_MAIN.get()));
				
				ResponseBuilder returnDialogue = new ResponseBuilder(l.msg("individual.kernelsprite.return", "Lets talk about something else."))
						.setNextAsEntrypoint()
						.addTrigger(new GoToBlock(BlockPredicate.Builder.block().build(), 1, 1, 1, 1, 1, false))
						.nextDialogue(Minestuck.id("individual/kernelsprite/start"));
				
				var blank = builder.add("blank", kernelspriteNode(l,
						"Unknown command.", "It doesn't give a response you can understand.. instead it just floats back over to the Cruxtruder")
						.addResponse(returnDialogue));
				
				var helpEntering7 = builder.add("help_entering.7", new NodeSelectorBuilder()
						.node(playerHasItemTag(MSTags.Items.CRUXITE_ARTIFACTS, 1), kernelspriteNode(l,
								"Please consume Cruxite Artifact to complete Entry process.", "It seems to be waiting for you to consume the Cruxite Artifact. This feels like it's going to be big step.")
								.addResponse(new ResponseBuilder(l.subMsg("thanks_for_help", "Thank you for helping me"))
										.addTrigger(new GoToBlock(BlockPredicate.Builder.block().of(cruxtruderBlocks).build(), 20, 1, 240, 1, 6, true))
										.setNextAsEntrypoint()
										.nextDialogue(blank)
								)
								.addResponse(new ResponseBuilder(l.subMsg("anything_else", "Is there anything else?"))
										.addTrigger(new GoToBlock(BlockPredicate.Builder.block().of(cruxtruderBlocks).build(), 20, 1, 240, 1, 6, true))
										.setNextAsEntrypoint()
										.nextDialogue(blank)
								))
						.defaultNode(kernelspriteNode(l, "default",
								"Cruxite Artifact still present. Please confirm Entry has initiated.", "It expects you to have something in your inventory, perhaps from the Alchemiter?")
								.addResponse(returnDialogue)
						)
				);
				
				var helpEntering6 = builder.add("help_entering.6", kernelspriteNode(l,
						"Please alchemize artifact.", "It moves towards the middle section of the Alchemiter.")
						.addResponse(new ResponseBuilder(l.subMsg("next", "What's next?"))
								.addPlayerMessage(l.subMsg("next_player", "What's next?"))
								.addTrigger(new GoToBlock(BlockPredicate.Builder.block().of(MSBlocks.ALCHEMITER.CENTER.get()).build(), 20, 1, 240, 1, 6, true))
								.nextDialogue(helpEntering7)
								.setNextAsEntrypoint()
						)
						.addResponse(returnDialogue)
				);
				
				var needAlchemiter = builder.add("need_alchemiter", kernelspriteNode(l,
						"Affirmative. Alchemiter required to continue Entry process", "That seems like a yes..")
						.addResponse(returnDialogue)
				);
				
				var helpEntering5 = builder.add("help_entering.5", new NodeSelectorBuilder()
						.node(isNearBlock(MSBlocks.ALCHEMITER.TOTEM_PAD.get(), 20, 1), kernelspriteNode(l,
								"Identified Alchemiter. Please insert carved dowel.", "It is now gesturing specifically to the portion of the Alchemiter with a small pedestal.")
								.addResponse(new ResponseBuilder(l.subMsg("next", "What's next?"))
										.addPlayerMessage(l.subMsg("next_player", "What's next?"))
										.addTrigger(new GoToBlock(BlockPredicate.Builder.block().of(MSBlocks.ALCHEMITER.CENTER.get()).build(), 20, 1, 240, 1, 6, true))
										.nextDialogue(helpEntering6)
										.setNextAsEntrypoint())
								.addResponse(returnDialogue))
						.defaultNode(kernelspriteNode(l, "default",
								"Alchemiter not found. Please make a request from your Server to place a Alchemiter.", "It gestures for something new, perhaps a machine to place the carved dowel into?")
								.addResponse(new ResponseBuilder(l.subMsg("need_alchemiter", "Do I need another machine?")).addPlayerMessage(l.subMsg("need_alchemiter_player", "Do I need another machine?")).nextDialogue(needAlchemiter))
								.addResponse(returnDialogue)
						)
				);
				
				var helpEntering4 = builder.add("help_entering.4", kernelspriteNode(l,
						"Please pull handle.", "It moves towards the section of the Totem Lathe with a handle at the top.")
						.addResponse(new ResponseBuilder(l.subMsg("next", "What's next?"))
								.addPlayerMessage(l.subMsg("next_player", "What's next?"))
								.addTrigger(new GoToBlock(BlockPredicate.Builder.block().of(alchemiterBlocks).build(), 20, 1, 240, 1, 6, true))
								.nextDialogue(helpEntering5)
								.setNextAsEntrypoint())
						.addResponse(returnDialogue)
				);
				
				var helpEntering3 = builder.add("help_entering.3", kernelspriteNode(l,
						"Please insert punched card.", "It moves towards the section of the Totem Lathe with a slot for captchalogue cards. Maybe it needs a specific captchalogue card?")
						.addResponse(new ResponseBuilder(l.subMsg("next", "What's next?"))
								.addPlayerMessage(l.subMsg("next_player", "What's next?"))
								.addTrigger(new GoToBlock(BlockPredicate.Builder.block().of(MSBlocks.TOTEM_LATHE.TOP.get()).build(), 20, 1, 240, 1, 6, true))
								.nextDialogue(helpEntering4)
								.setNextAsEntrypoint())
						.addResponse(returnDialogue)
				);
				
				var needLathe = builder.add("need_lathe", kernelspriteNode(l,
						"Affirmative. Totem Lathe required to continue Entry process", "That seems like a yes..")
						.addResponse(returnDialogue)
				);
				
				var helpEntering2 = builder.add("help_entering.2", new NodeSelectorBuilder()
						.node(isNearBlock(MSBlocks.TOTEM_LATHE.WHEEL.get(), 20, 1), kernelspriteNode(l,
								"Identified Totem Lathe. Please insert uncarved dowel.", "It is now gesturing specifically to the middle section of the Totem Lathe.")
								.addResponse(new ResponseBuilder(l.subMsg("next", "What's next?"))
										.addPlayerMessage(l.subMsg("next_player", "What's next?"))
										.addTrigger(new GoToBlock(BlockPredicate.Builder.block().of(MSBlocks.TOTEM_LATHE.TOP_CORNER.get()).build(), 20, 1, 240, 1, 6, true))
										.nextDialogue(helpEntering3)
										.setNextAsEntrypoint())
								.addResponse(returnDialogue))
						.defaultNode(kernelspriteNode(l, "default",
								"Totem Lathe not found. Please make a request from your Server to place a Totem Lathe.", "It gestures for something new, perhaps a machine to place the uncarved dowel into?")
								.addResponse(new ResponseBuilder(l.subMsg("need_lathe", "Do I need another machine?")).addPlayerMessage(l.subMsg("need_lathe_player", "Do I need another machine?")).nextDialogue(needLathe))
								.addResponse(returnDialogue)
						)
				);
				
				var helpEntering = builder.add("help_entering.1", kernelspriteNode(l,
						"Help with Entry requested. Please turn handle to extract a new cruxite dowel.", "It is now gesturing specifically to the handle of the Cruxtruder.")
						.addResponse(new ResponseBuilder(l.subMsg("next", "What's next?"))
								.addPlayerMessage(l.subMsg("next_player", "What's next?"))
								.addTrigger(new GoToBlock(BlockPredicate.Builder.block().of(latheBlocks).build(), 13, 1, 240, 1, 6, true))
								.nextDialogue(helpEntering2)
								.setNextAsEntrypoint())
						.addResponse(returnDialogue)
				);
				
				var needCruxtruder = builder.add("need_cruxtruder", kernelspriteNode(l,
						"Affirmative. Cruxtruder required to continue Entry process", "That seems like a yes..")
						.addResponse(returnDialogue)
				);
				
				var whatKernel = builder.add("what_kernel", kernelspriteNode(l,
						"whoami command queried. Unable to answer until Prototyping. Prototyping not available.", "You don't get an answer you can understand.")
						.addResponse(returnDialogue)
				);
				
				var whatSay = builder.add("what_say", new NodeSelectorBuilder()
						.node(isNearBlock(MSBlocks.CRUXTRUDER.TUBE.get(), 20, 1), kernelspriteNode(l, "near_block",
								"help command queried. Do you wish to learn about Entry?", "The static continues, but you get the sense that it is gesturing at the Cruxtruder.")
								.addResponse(new ResponseBuilder(l.subMsg("help_entering", "Can you help me use that machine?"))
										.addPlayerMessage(l.subMsg("help_entering_player", "Can you help me use that machine?"))
										.addTrigger(new GoToBlock(BlockPredicate.Builder.block().of(cruxtruderBlocks).build(), 20, 1, 240, 1, 6, true))
										.nextDialogue(helpEntering)
										.setNextAsEntrypoint())
								.addResponse(returnDialogue))
						.defaultNode(kernelspriteNode(l, "default",
								"help command queried. Do you wish to learn about Entry? Please provide a Cruxtruder for further assistance.", "The static continues, but you get the sense that it is gesturing for something. Maybe the Cruxtruder it came out of?")
								.addResponse(new ResponseBuilder(l.subMsg("need_cruxtruder", "Do I need the Cruxtuder for something?")).addPlayerMessage(l.subMsg("need_cruxtruder_player", "Do I need the Cruxtuder for something?")).nextDialogue(needCruxtruder))
								.addResponse(returnDialogue)
						)
				);
				
				builder.addStart(
						new NodeSelectorBuilder()
								.node(none(hasEntered(), isInLand()), kernelspriteNode(l,
										"Kernelsprite initialized. Please prepare for Entry immediately.", "All you hear is static, but you think it's trying to tell you something.")
										.addResponse(new ResponseBuilder(l.subMsg("what_kernel_alt", "What are you?")).nextDialogue(whatKernel))
										.addResponse(new ResponseBuilder(l.subMsg("what_say", "What are you trying to say?")).nextDialogue(whatSay))
										.addResponse(new ResponseBuilder(l.subMsg("prototype_alt", "[Prototype]"))
												.visibleCondition(l.subText("prototype_implementation_alt", "Prototyping is not implemented yet!"), none(alwaysTrue()))))
								.defaultNode(kernelspriteNode(l, "default",
										"Kernelsprite initialized. Please input command.", "All you hear is static, but you think it is talking.")
										.addResponse(new ResponseBuilder(l.subMsg("what_kernel", "What are you?")).nextDialogue(whatKernel))
										.addResponse(new ResponseBuilder(l.subMsg("go_home", "Can you please go somewhere else? [Stay near Gate]"))
												.visibleCondition(l.subText("go_home_cond", "Is not close enough to a Gate or is already there."), all(isNearBlocks(gateBlocks, 20, 1), none(isNearBlocks(gateBlocks, 4, 1))))
												.addTrigger(new GoToBlock(BlockPredicate.Builder.block().of(gateBlocks).build(), 20, 0.5, 360, 1, 4, true)))
										.addResponse(new ResponseBuilder(l.subMsg("go_free", "You don't have to stay here anymore [Roam free]"))
												.condition(isNearBlocks(gateBlocks, 4, 1))
												.addTrigger(new GoToBlock(BlockPredicate.Builder.block().of(gateBlocks).build(), 6, 0.05, 1, 1, 6, false)))
										.addResponse(new ResponseBuilder(l.subMsg("prototype", "[Prototype]"))
												.visibleCondition(l.subText("prototype_implementation", "Prototyping is not implemented yet!"), none(alwaysTrue())))
								)
				);
			}));
			
			return CompletableFuture.allOf();
		});
	}
	
	private static NodeBuilder kernelspriteNode(DialogueLangHelper l, String message, String description)
	{
		return new NodeBuilder(l.defaultKeyMsg("§k" + message)).addDescription(l.subMsg("desc", description));
	}
	
	private static NodeBuilder kernelspriteNode(DialogueLangHelper l, String key, String message, String description)
	{
		return new NodeBuilder(l.subMsg(key, "§k" + message)).addDescription(l.subMsg(key + ".desc", description));
	}
}
