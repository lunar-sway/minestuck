package com.mraof.minestuck.data.dialogue;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.data.dialogue.DialogueProvider.NodeBuilder;
import com.mraof.minestuck.data.dialogue.DialogueProvider.ResponseBuilder;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public final class IndividualizedDialogue
{
	public static DataProvider create(PackOutput output, LanguageProvider enUsLanguageProvider)
	{
		DialogueProvider provider = new DialogueProvider(Minestuck.MOD_ID, "individual", output);
		DialogueLangHelper l = new DialogueLangHelper(Minestuck.MOD_ID, enUsLanguageProvider);
		
		//Run dialogue creation early so that language stuff gets added before the language provider generates its file
		dialogues(provider, l);
		
		return provider;
	}
	
	private static void dialogues(DialogueProvider provider, DialogueLangHelper l)
	{
		provider.add("kernelsprite", new FolderedDialogue(builder -> {
			ResponseBuilder returnDialogue = new ResponseBuilder(l.msg("individual.kernelsprite.return", "Lets talk about something else.")).nextDialogue(Minestuck.id("individual/kernelsprite/start"));
			
			var helpEntering = builder.add("help_entering", kernelspriteNode(l,
					"Help with Entry requested. Please turn handle to extract a new cruxite dowel.", "It is now gesturing specifically to the handle of the Cruxtruder.")
					.addResponse(returnDialogue)
			);
			
			var whatKernel = builder.add("what_kernel", kernelspriteNode(l,
					"whoami command queried. Unable to answer until Prototyping. Prototyping not available.", "You don't get an answer you can understand.")
					.addResponse(returnDialogue)
			);
			
			var whatSay = builder.add("what_say", kernelspriteNode(l,
					"help command queried. Do you wish to learn about Entry?", "The static continues, but you get the sense that it is gesturing at the Cruxtruder.")
					.addResponse(new ResponseBuilder(l.subMsg("help_entering", "Can you help me use that machine?")).nextDialogue(helpEntering).setNextAsEntrypoint())
					.addResponse(returnDialogue)
			);
			
			builder.addStart(kernelspriteNode(l,
					"Kernelsprite initialized. Please prepare for Entry immediately.", "It sounds like static, but you think it's trying to tell you something.")
					.addResponse(new ResponseBuilder(l.subMsg("what_kernel", "What are you?")).nextDialogue(whatKernel))
					.addResponse(new ResponseBuilder(l.subMsg("what_say", "What are you trying to say?")).nextDialogue(whatSay))
			);
		}));
	}
	
	private static NodeBuilder kernelspriteNode(DialogueLangHelper l, String message, String description)
	{
		return new NodeBuilder(l.defaultKeyMsg("§k" + message)).addDescription(l.subMsg("desc", description));
	}
}
