package com.mraof.minestuck.data;

import com.mraof.minestuck.util.BoondollarPriceManager;
import com.mraof.minestuck.util.BoondollarPricing;
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
		test1Responses.add(new DialogueJson.Response("test1response1", List.of("test1response1condition1"), "test2name"));
		test1Responses.add(new DialogueJson.Response("test1response2", List.of("test1response2condition1","test1response2condition2"), "test2name"));
		test1Responses.add(new DialogueJson.Response("test1response3", List.of(), "test2name"));
		add(test1Name, new DialogueJson("test1message", "test1animation", "test1GuiPath", test1Responses));
		
		String test2Name = "test2name";
		List<DialogueJson.Response> test2Responses = new ArrayList<>();
		test2Responses.add(new DialogueJson.Response("test2response1", List.of("test2response1condition1"), test1Name));
		test2Responses.add(new DialogueJson.Response("test2response2", List.of("test2response2condition1","test2response2condition2"), test1Name));
		test2Responses.add(new DialogueJson.Response("test2response3", List.of("test2response3condition1"), test1Name));
		add(test2Name, new DialogueJson("test2message", "test2animation", "test2GuiPath", test2Responses));
	}
	
	/*private void addResponse(String... response)
	{
		List<DialogueJson.Response> responses = new ArrayList<>();
		response.forEach
		responses.add(new DialogueJson.Response());
	}*/
	
	protected void add(String name, DialogueJson dialogue)
	{
		dialogues.put(new ResourceLocation(modid, name), dialogue);
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