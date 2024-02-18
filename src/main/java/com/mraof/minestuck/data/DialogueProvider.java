package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.Dialogue;
import com.mraof.minestuck.util.DialogueManager;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DialogueProvider implements DataProvider
{
	public static final ResourceLocation EMPTY_NEXT_PATH = new ResourceLocation("");
	
	private final Map<ResourceLocation, Dialogue> dialogues = new HashMap<>();
	private final PackOutput output;
	
	public DialogueProvider(PackOutput output)
	{
		this.output = output;
	}
	
	/**
	 * Since a Response can be used to link the current Dialogue with any other Dialogue, its important to make a habit of commenting where a Dialogue is used
	 */
	private void registerDialogues()
	{
		testDialogues();
		consortDialogues();
	}
	
	private void consortDialogues()
	{
		add(new DialogueBuilder("mycelium.1", new Dialogue.UseContext(List.of(new Dialogue.Condition(Dialogue.Condition.Type.CONDITIONLESS))))
				.addResponse("=>", "mycelium.2")
		);
		add(new DialogueBuilder("mycelium.2"));
		
		add(new DialogueBuilder("camel/start", new Dialogue.UseContext(List.of(new Dialogue.Condition(Dialogue.Condition.Type.CONDITIONLESS))))
				.addResponse("dialogue.camel.yes", "camel/no_camel")
				.addResponse("dialogue.camel.no", "camel/dancing_camel")
		);
		add(new DialogueBuilder("camel/no_camel"));
		add(new DialogueBuilder("camel/dancing_camel"));
	}
	
	private void testDialogues()
	{
		add(new DialogueBuilder("test1", "test1animation", "generic_extra_large", new Dialogue.UseContext(List.of(new Dialogue.Condition(Dialogue.Condition.Type.CONDITIONLESS))))
				.addResponse("test1response1", List.of(new Dialogue.Condition(Dialogue.Condition.Type.CONSORT_TYPE, "turtle")), List.of(), "test2", true)
				.addResponse("test1response2", List.of(new Dialogue.Condition(Dialogue.Condition.Type.CONSORT_TYPE, "nakagator")), List.of(), "test2", true)
				.addResponse("test1response3", List.of(), List.of(new Dialogue.Trigger(Dialogue.Trigger.Type.COMMAND, "summon minestuck:grist ~ ~ ~ {Value:200}")), "test2", true)
		);
		
		add(new DialogueBuilder("test2", "test2animation", "generic_extra_large", new Dialogue.UseContext(List.of(new Dialogue.Condition(Dialogue.Condition.Type.CONDITIONLESS))))
				.addResponse("test2response1", List.of(new Dialogue.Condition(Dialogue.Condition.Type.CONSORT_TYPE, "salamander", null, "Was not salamander")), List.of(), "test1", false)
				.addResponse("test2response2", List.of(), List.of(new Dialogue.Trigger(Dialogue.Trigger.Type.COMMAND, "say hi")), "test1", false)
				.addResponse("test2response3", List.of(), List.of(new Dialogue.Trigger(Dialogue.Trigger.Type.COMMAND, """
						tellraw @a ["",{"text":"Welcome","color":"aqua"},{"text":" to "},{"text":"Minecraft","color":"#9B9B17"},{"text":" Tools "},{"text":"partner.","obfuscated":true},{"text":" "},{"selector":"@s"},{"text":" fs"}]""")), "test1", false)
		);
		
		add(new DialogueBuilder("turtle_only", "test2animation", "generic_extra_large", new Dialogue.UseContext(List.of(new Dialogue.Condition(Dialogue.Condition.Type.CONSORT_TYPE, "turtle")))));
		
		add(new DialogueBuilder("nakagator_only", "test2animation", "generic_extra_large", new Dialogue.UseContext(List.of(new Dialogue.Condition(Dialogue.Condition.Type.IS_ENTITY_TYPE, "minestuck:nakagator")))));
		
		//builder can take empty strings for nextDialoguePath
		add(new DialogueBuilder("me_want_cookie", new Dialogue.UseContext(List.of(new Dialogue.Condition(Dialogue.Condition.Type.CONDITIONLESS))))
				.addResponse("im sorry fellow, I have no cookie for you. Bye", "")
				.addResponse("why do you want cookie?", null)
				.addResponse("here have a cookie chap", List.of(new Dialogue.Condition(Dialogue.Condition.Type.HAS_ITEM, "minecraft:cookie", null, "Has no cookie")), List.of(new Dialogue.Trigger(Dialogue.Trigger.Type.TAKE_ITEM, "minecraft:cookie"), new Dialogue.Trigger(Dialogue.Trigger.Type.SET_DIALOGUE, "minestuck:hunger_filled")), "oh_yippee", false)
		);
		add(new DialogueBuilder("oh_yippee"));
		add(new DialogueBuilder("hunger_filled"));
		
		add(new DialogueBuilder("me_want_5_cookies", new Dialogue.UseContext(List.of(new Dialogue.Condition(Dialogue.Condition.Type.CONDITIONLESS))))
				.addResponse("im sorry fellow, I have no cookie for you. Bye", "")
				.addResponse("here have 5 cookies chap", List.of(new Dialogue.Condition(Dialogue.Condition.Type.HAS_ITEM, "minecraft:cookie", "5", "Has no cookie")), List.of(new Dialogue.Trigger(Dialogue.Trigger.Type.TAKE_ITEM, "minecraft:cookie", "5")), "oh_yippee", false)
		);
		
		add(new DialogueBuilder("hi_friend_can_i_help_you", new Dialogue.UseContext(List.of(new Dialogue.Condition(Dialogue.Condition.Type.CONDITIONLESS))))
				.addResponse("I hate you", List.of(), List.of(new Dialogue.Trigger(Dialogue.Trigger.Type.ADD_CONSORT_REPUTATION, "-100")), null, false)
				.addResponse("I love you", List.of(), List.of(new Dialogue.Trigger(Dialogue.Trigger.Type.ADD_CONSORT_REPUTATION, "100")), null, false)
				.addResponse("Rep above 500", List.of(new Dialogue.Condition(Dialogue.Condition.Type.HAS_MINIMUM_REPUTATION, "500", null, "Rep too low")), List.of(), null, false)
				.addResponse("Rep below 200", List.of(new Dialogue.Condition(Dialogue.Condition.Type.HAS_MAXIMUM_REPUTATION, "200", null, "Rep too high")), List.of(), null, false)
				.addResponse("bye", "")
		);
	}
	
	private void add(DialogueBuilder builder)
	{
		Dialogue dialogue = new Dialogue(builder.path, builder.message, builder.animation, builder.guiPath, builder.responses, builder.useContext);
		dialogues.put(builder.path, dialogue);
	}
	
	public static class DialogueBuilder
	{
		private final ResourceLocation path;
		private final String message;
		private final String animation;
		private final ResourceLocation guiPath;
		private final List<Dialogue.Response> responses;
		private final Dialogue.UseContext useContext;
		
		DialogueBuilder(String message)
		{
			this(message, "generic_animation", "generic_extra_large", null);
		}
		
		DialogueBuilder(String message, Dialogue.UseContext useContext)
		{
			this(message, "generic_animation", "generic_extra_large", useContext);
		}
		
		DialogueBuilder(String message, String animation, String gui, @Nullable Dialogue.UseContext useContext)
		{
			this.path = new ResourceLocation(Minestuck.MOD_ID, message);
			this.message = "minestuck.dialogue." + message.replace("/", ".");
			this.animation = animation;
			this.guiPath = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/" + gui + ".png");
			this.responses = new ArrayList<>();
			this.useContext = useContext;
		}
		
		public DialogueBuilder addResponse(String response, List<Dialogue.Condition> conditions, List<Dialogue.Trigger> triggers, @Nullable String nextDialoguePath, boolean hideIfFailed)
		{
			ResourceLocation nextPath = getNextPath(nextDialoguePath);
			this.responses.add(new Dialogue.Response(response, conditions, triggers, nextPath, hideIfFailed));
			return this;
		}
		
		public DialogueBuilder addResponse(String response, @Nullable String nextDialoguePath)
		{
			ResourceLocation nextPath = getNextPath(nextDialoguePath);
			this.responses.add(new Dialogue.Response(response, List.of(), List.of(), nextPath, true));
			return this;
		}
		
		/**
		 * Tries and creates the next path resource location. If its null it will be the empty form which will close the screen, if its empty then it will be replaced with itself, else it will make it like normal
		 */
		private ResourceLocation getNextPath(@Nullable String nextDialoguePath)
		{
			ResourceLocation nextPath;
			if(nextDialoguePath == null)
				nextPath = this.path;
			else if(nextDialoguePath.isEmpty())
				nextPath = EMPTY_NEXT_PATH;
			else
				nextPath = new ResourceLocation(Minestuck.MOD_ID, nextDialoguePath);
			return nextPath;
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