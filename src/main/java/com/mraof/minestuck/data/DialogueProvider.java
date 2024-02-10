package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.DialogueJson;
import com.mraof.minestuck.util.DialogueJsonManager;
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
	private final Map<ResourceLocation, DialogueJson> dialogues = new HashMap<>();
	private final PackOutput output;
	private final String modid;
	
	public DialogueProvider(PackOutput output, String modid)
	{
		this.output = output;
		this.modid = modid;
	}
	
	protected void registerDialogues()
	{
		String test1Name = "test1name";
		List<DialogueJson.Response> test1Responses = new ArrayList<>();
		test1Responses = addResponse(test1Responses, "test1response1", List.of("test1response1condition1"), "test2name");
		test1Responses = addResponse(test1Responses, "test1response2", List.of("test1response2condition1","test1response2condition2"), "test2name");
		test1Responses = addResponse(test1Responses, "test1response3", List.of(), "test2name");
		add(test1Name, "minestuck.test1message", "test1animation", "generic_extra_large", test1Responses);
		
		String test2Name = "test2name";
		List<DialogueJson.Response> test2Responses = new ArrayList<>();
		test2Responses = addResponse(test2Responses, "test2response1", List.of("test2response1condition1"), test1Name);
		test2Responses = addResponse(test2Responses,"test2response2", List.of("test2response2condition1","test2response2condition2"), test1Name);
		test2Responses = addResponse(test2Responses,"test2response3", List.of("test2response3condition1"), test1Name);
		add(test2Name, "test2message", "test2animation", "generic_extra_large", test2Responses);
	}
	
	/*private void addResponse(String... response)
	{
		List<DialogueJson.Response> responses = new ArrayList<>();
		response.forEach
		responses.add(new DialogueJson.Response());
	}*/
	
	protected List<DialogueJson.Response> addResponse(List<DialogueJson.Response> responses, String response, List<String> conditions, String nextDialoguePath)
	{
		responses.add(new DialogueJson.Response(response, conditions, new ResourceLocation(Minestuck.MOD_ID, "minestuck/dialogue/" + nextDialoguePath + ".json")));
		return responses;
	}
	
	protected void add(String name, String message, String animation, String gui, List<DialogueJson.Response> responses)
	{
		dialogues.put(new ResourceLocation(modid, name), new DialogueJson(message, animation, new ResourceLocation(Minestuck.MOD_ID, "textures/gui/" + gui + ".png"), responses));
	}
	
	@Override
	public CompletableFuture<?> run(CachedOutput cache)
	{
		registerDialogues();
		
		Path outputPath = output.getOutputFolder();
		List<CompletableFuture<?>> futures = new ArrayList<>(dialogues.size());
		
		for(Map.Entry<ResourceLocation, DialogueJson> entry : dialogues.entrySet())
		{
			Path pricingPath = getPath(outputPath, entry.getKey());
			futures.add(DataProvider.saveStable(cache, DialogueJsonManager.parsePrice(entry.getValue()), pricingPath));
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