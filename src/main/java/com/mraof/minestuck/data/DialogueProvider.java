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
	
	public DialogueProvider(PackOutput output)
	{
		this.output = output;
	}
	
	/**
	 * Since a Response can be used to link the current Dialogue with any other Dialogue, its important to make a habit of commenting where a Dialogue is used
	 */
	protected void registerDialogues()
	{
		add(new DialogueBuilder("test1", "test1animation", "generic_extra_large")
				.addResponse("test1response1", List.of(new Dialogue.Condition(Dialogue.Condition.Type.CONSORT_TYPE, "turtle")), List.of(), "test2", true)
				.addResponse("test1response2", List.of(new Dialogue.Condition(Dialogue.Condition.Type.CONSORT_TYPE, "nakagator")), List.of(), "test2", true)
				.addResponse("test1response3", List.of(), List.of(new Dialogue.Trigger(Dialogue.Trigger.Type.COMMAND, "summon minestuck:grist ~ ~ ~ {Value:200}")), "test2", true)
		);
		
		add(new DialogueBuilder("test2", "test2animation", "generic_extra_large")
				.addResponse("test2response1", List.of(new Dialogue.Condition(Dialogue.Condition.Type.CONSORT_TYPE, "salamander", "Was not salamander")), List.of(), "test1", false)
				.addResponse("test2response2", List.of(), List.of(new Dialogue.Trigger(Dialogue.Trigger.Type.COMMAND, "say hi")), "test1", false)
				.addResponse("test2response3", List.of(), List.of(new Dialogue.Trigger(Dialogue.Trigger.Type.COMMAND, "tellraw @a [\"\",{\"text\":\"Welcome\",\"color\":\"aqua\"},{\"text\":\" to \"},{\"text\":\"Minecraft\",\"color\":\"#9B9B17\"},{\"text\":\" Tools \"},{\"text\":\"partner.\",\"obfuscated\":true},{\"text\":\" \"},{\"selector\":\"@s\"},{\"text\":\" fs\"}]")), "test1", false)
		);
		
		//builder can take empty strings for nextDialoguePath
		add(new DialogueBuilder("me_want_cookie")
				.addResponse("im sorry fellow, I have no cookie for you", "")
				.addResponse("why do you want cookie?", "")
				.addResponse("here have a cookie chap", List.of(new Dialogue.Condition(Dialogue.Condition.Type.HAS_ITEM, "minecraft:cookie", "Has no cookie")), List.of(new Dialogue.Trigger(Dialogue.Trigger.Type.TAKE_ITEM, "minecraft:cookie"), new Dialogue.Trigger(Dialogue.Trigger.Type.SET_DIALOGUE, "minestuck:hunger_filled")), "oh_yippee", false)
		);
		add(new DialogueBuilder("oh_yippee"));
		add(new DialogueBuilder("hunger_filled"));
		
		
		
		add(new DialogueBuilder("mycelium.1")
				.addResponse("=>", "mycelium.2"));
		add(new DialogueBuilder("mycelium.2"));
		
		add(new DialogueBuilder("camel")
				.addResponse("dialogue.camel.yes", "camel.no_camel")
				.addResponse("dialogue.camel.no", "camel.dancing_camel")
		);
		add(new DialogueBuilder("camel.no_camel"));
		add(new DialogueBuilder("camel.dancing_camel"));
	}
	
	protected void add(DialogueBuilder builder)
	{
		dialogues.put(builder.path, new Dialogue(builder.path, builder.message, builder.animation, builder.guiPath, builder.responses));
	}
	
	public static class DialogueBuilder
	{
		private final ResourceLocation path;
		private final String message;
		private final String animation;
		private final ResourceLocation guiPath;
		private final List<Dialogue.Response> responses;
		
		DialogueBuilder(String message)
		{
			this(message, "generic_animation", "generic_extra_large");
		}
		
		DialogueBuilder(String message, String animation, String gui)
		{
			this.path = new ResourceLocation(Minestuck.MOD_ID, message);
			this.message = "dialogue." + message;
			this.animation = animation;
			this.guiPath = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/" + gui + ".png");
			this.responses = new ArrayList<>();
		}
		
		public DialogueBuilder addResponse(String response, List<Dialogue.Condition> conditions, List<Dialogue.Trigger> triggers, String nextDialoguePath, boolean hideIfFailed)
		{
			this.responses.add(new Dialogue.Response(response, conditions, triggers, new ResourceLocation(Minestuck.MOD_ID, nextDialoguePath), hideIfFailed));
			return this;
		}
		
		public DialogueBuilder addResponse(String response, String nextDialoguePath)
		{
			this.responses.add(new Dialogue.Response(response, List.of(), List.of(), new ResourceLocation(Minestuck.MOD_ID, nextDialoguePath), true));
			//ResourceLocation testedLocation = (nextDialoguePath == null || nextDialoguePath.isEmpty()) ? null : new ResourceLocation(Minestuck.MOD_ID, nextDialoguePath);
			//this.responses.add(new Dialogue.Response(response, List.of(), List.of(), testedLocation, true));
			return this;
		}
	}
	
	@Override
	public CompletableFuture<?> run(CachedOutput cache)
	{
		registerDialogues();
		
		Path outputPath = output.getOutputFolder();
		List<CompletableFuture<?>> futures = new ArrayList<>(dialogues.size());
		
		for(Map.Entry<ResourceLocation, Dialogue> entry : dialogues.entrySet())
		{
			Path dialoguePath = getPath(outputPath, entry.getKey());
			futures.add(DataProvider.saveStable(cache, DialogueManager.parseDialogue(entry.getValue()), dialoguePath));
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