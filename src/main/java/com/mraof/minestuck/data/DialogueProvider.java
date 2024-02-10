package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.Dialogue;
import com.mraof.minestuck.util.DialogueManager;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DialogueProvider implements DataProvider
{
	private final Map<ResourceLocation, Dialogue> dialogues = new HashMap<>();
	private final PackOutput output;
	private final String modid;
	
	public DialogueProvider(PackOutput output, String modid)
	{
		this.output = output;
		this.modid = modid;
	}
	
	protected void registerDialogues()
	{
		String mycelium1 = "mycelium.1";
		String mycelium2 = "mycelium.2";
		List<Dialogue.Response> mycelium1Response = new ArrayList<>();
		mycelium1Response = addResponse(mycelium1Response, "=>", List.of(), mycelium2);
		addSimple(mycelium1, mycelium1Response);
		addSimpleEnd(mycelium2);
		
		List<Dialogue.Response> camelResponses = new ArrayList<>();
		camelResponses = addResponse(camelResponses, "consort.camel.yes", List.of(), "camel.no_camel");
		camelResponses = addResponse(camelResponses, "consort.camel.no", List.of(), "camel.dancing_camel");
		addSimple("camel", camelResponses);
		addSimpleEnd("camel.no_camel");
		addSimpleEnd("camel.dancing_camel");
		
		
		
		String test1Name = "test1name";
		List<Dialogue.Response> test1Responses = new ArrayList<>();
		test1Responses = addResponse(test1Responses, "test1response1", List.of(new Dialogue.Condition(Dialogue.Condition.Type.CONSORT_TYPE, "turtle")), "test2name");
		test1Responses = addResponse(test1Responses, "test1response2", List.of(new Dialogue.Condition(Dialogue.Condition.Type.CONSORT_TYPE, "nakagator")), "test2name");
		test1Responses = addResponse(test1Responses, "test1response3", List.of(), "test2name");
		add(test1Name, "minestuck.test1message", "test1animation", "generic_extra_large", test1Responses);
		
		String test2Name = "test2name";
		List<Dialogue.Response> test2Responses = new ArrayList<>();
		test2Responses = addResponse(test2Responses, "test2response1", List.of(new Dialogue.Condition(Dialogue.Condition.Type.CONSORT_TYPE, "salamander")), test1Name);
		test2Responses = addResponse(test2Responses,"test2response2", List.of(), test1Name);
		test2Responses = addResponse(test2Responses,"test2response3", List.of(), test1Name);
		add(test2Name, "test2message", "test2animation", "generic_extra_large", test2Responses);
	}
	
	/*private void addResponse(String... response)
	{
		List<DialogueJson.Response> responses = new ArrayList<>();
		response.forEach
		responses.add(new DialogueJson.Response());
	}*/
	
	protected List<Dialogue.Response> addResponse(List<Dialogue.Response> responses, String response, List<Dialogue.Condition> conditions, String nextDialoguePath)
	{
		//responses.add(new Dialogue.Response(response, conditions, new ResourceLocation(Minestuck.MOD_ID, "minestuck/dialogue/" + nextDialoguePath + ".json")));
		responses.add(new Dialogue.Response(response, conditions, new ResourceLocation(Minestuck.MOD_ID, nextDialoguePath)));
		return responses;
	}
	
	protected void addSimple(String name, List<Dialogue.Response> responses)
	{
		add(name, "consort." + name, "generic_animation", "generic_extra_large", responses);
	}
	
	protected void addSimpleEnd(String name)
	{
		addSimple(name, new ArrayList<>());
	}
	
	protected void add(String name, String message, String animation, String gui, List<Dialogue.Response> responses)
	{
		ResourceLocation location = new ResourceLocation(modid, name);
		dialogues.put(location, new Dialogue(location, message, animation, new ResourceLocation(Minestuck.MOD_ID, "textures/gui/" + gui + ".png"), responses));
	}
	
	@Override
	public CompletableFuture<?> run(CachedOutput cache)
	{
		registerDialogues();
		
		Path outputPath = output.getOutputFolder();
		List<CompletableFuture<?>> futures = new ArrayList<>(dialogues.size());
		
		for(Map.Entry<ResourceLocation, Dialogue> entry : dialogues.entrySet())
		{
			Path pricingPath = getPath(outputPath, entry.getKey());
			futures.add(DataProvider.saveStable(cache, DialogueManager.parseDialogue(entry.getValue()), pricingPath));
		}
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
	}
	
	private static Path getPath(Path outputPath, ResourceLocation id)
	{
		return outputPath.resolve("data/" + id.getNamespace() + "/minestuck/dialogue/" + id.getPath() + ".json");
	}
	
	@Override
	public String getName()
	{
		return "Dialogues";
	}
}