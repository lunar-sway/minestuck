package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.item.loot.MSLootTables;
import com.mraof.minestuck.entity.dialogue.Dialogue;
import com.mraof.minestuck.util.DialogueManager;
import com.mraof.minestuck.entity.dialogue.Trigger;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DialogueProvider implements DataProvider
{
	/**
	 * Can be used as a dead end to close the screen
	 */
	public static final ResourceLocation EMPTY_NEXT_PATH = new ResourceLocation("close_menu");
	/**
	 * Can be used to activate triggers but return to the same screen
	 */
	public static final ResourceLocation LOOP_NEXT_PATH = new ResourceLocation("loop_menu");
	
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
		add(new DialogueBuilder("mycelium.1", Dialogue.Condition.Type.CONDITIONLESS)
				.addResponse(new ResponseBuilder("=>", "mycelium.2"))
		);
		add(new DialogueBuilder("mycelium.2"));
		
		add(new DialogueBuilder("camel/start", Dialogue.Condition.Type.CONDITIONLESS)
				.addResponse(new ResponseBuilder("minestuck.dialogue.camel.yes", "camel/no_camel"))
				.addResponse(new ResponseBuilder("minestuck.dialogue.camel.no", "camel/dancing_camel"))
		);
		add(new DialogueBuilder("camel/no_camel"));
		add(new DialogueBuilder("camel/dancing_camel"));
		
		add(new DialogueBuilder("food_shop", new Dialogue.Condition(Dialogue.Condition.Type.CONSORT_TYPE, "salamander"))
				.addResponse("Never mind")
				.addResponse(new ResponseBuilder("What do you have?").addTrigger(new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_FOOD_STOCK, EnumConsort.MerchantType.FOOD.getName())))
		);
		add(new DialogueBuilder("fast_food", new Dialogue.Condition(Dialogue.Condition.Type.CONSORT_TYPE, "nakagator"))
				.addResponse("I'm good")
				.addResponse(new ResponseBuilder("Show me your menu").addTrigger(new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_FOOD_STOCK, EnumConsort.MerchantType.FOOD.getName())))
		);
		add(new DialogueBuilder("grocery_store", new Dialogue.Condition(Dialogue.Condition.Type.CONSORT_TYPE, "iguana"))
				.addResponse("No thanks")
				.addResponse(new ResponseBuilder("What do you have to sell?").addTrigger(new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_FOOD_STOCK, EnumConsort.MerchantType.FOOD.getName())))
		);
		add(new DialogueBuilder("tasty_welcome", new Dialogue.Condition(Dialogue.Condition.Type.CONSORT_TYPE, "turtle"))
				.addResponse("Goodbye")
				.addResponse(new ResponseBuilder("Let me see your wares").addTrigger(new Trigger.OpenConsortMerchantGui(MSLootTables.CONSORT_FOOD_STOCK, EnumConsort.MerchantType.FOOD.getName())))
		);
	}
	
	private void testDialogues()
	{
		add(new DialogueBuilder("test1", "test1animation", "generic_extra_large", new Dialogue.UseContext(List.of(new Dialogue.Condition(Dialogue.Condition.Type.CONDITIONLESS))))
				.addResponse(new ResponseBuilder("test1response1", "test2").addCondition(Dialogue.Condition.Type.CONSORT_TYPE, "turtle"))
				.addResponse(new ResponseBuilder("test1response2", "test2").addCondition(Dialogue.Condition.Type.CONSORT_TYPE, "nakagator"))
				.addResponse(new ResponseBuilder("test1response3", "test2").addTrigger(new Trigger.Command("summon minestuck:grist ~ ~ ~ {Value:200}")))
		);
		
		add(new DialogueBuilder("test2", "test2animation", "generic_extra_large", new Dialogue.UseContext(List.of(new Dialogue.Condition(Dialogue.Condition.Type.CONDITIONLESS))))
				.addResponse(new ResponseBuilder("test2response1", "test1")
						.addCondition(Dialogue.Condition.Type.CONSORT_TYPE, "salamander", null, "Was not salamander")
						.dontHideFailed())
				.addResponse(new ResponseBuilder("test2response2", "test1").addTrigger(new Trigger.Command("say hi")))
				.addResponse(new ResponseBuilder("test2response3", "test1")
						.addTrigger(new Trigger.Command("""
								tellraw @a ["",{"text":"Welcome","color":"aqua"},{"text":" to "},{"text":"Minecraft","color":"#9B9B17"},{"text":" Tools "},{"text":"partner.","obfuscated":true},{"text":" "},{"selector":"@s"},{"text":" fs"}]""")))
		);
		
		add(new DialogueBuilder("turtle_only", "test2animation", "generic_extra_large", new Dialogue.UseContext(List.of(new Dialogue.Condition(Dialogue.Condition.Type.CONSORT_TYPE, "turtle")))));
		
		add(new DialogueBuilder("nakagator_only", "test2animation", "generic_extra_large", new Dialogue.UseContext(List.of(new Dialogue.Condition(Dialogue.Condition.Type.IS_ENTITY_TYPE, "minestuck:nakagator")))));
		
		add(new DialogueBuilder("me_want_cookie", Dialogue.Condition.Type.CONDITIONLESS)
				.addResponse("im sorry fellow, I have no cookie for you. Bye")
				.addResponse(new ResponseBuilder("why do you want cookie?", LOOP_NEXT_PATH))
				.addResponse(new ResponseBuilder("here have a cookie chap", "oh_yippee")
						.addCondition(Dialogue.Condition.Type.HAS_ITEM, "minecraft:cookie", null, "Has no cookie")
						.addTrigger(new Trigger.TakeItem(Items.COOKIE))
						.addTrigger(new Trigger.SetDialogue(new ResourceLocation(Minestuck.MOD_ID, "hunger_filled")))
						.dontHideFailed())
		);
		add(new DialogueBuilder("oh_yippee"));
		add(new DialogueBuilder("hunger_filled"));
		
		add(new DialogueBuilder("me_want_5_cookies", Dialogue.Condition.Type.CONDITIONLESS)
				.addResponse("im sorry fellow, I have no cookie for you. Bye")
				.addResponse(new ResponseBuilder("here have 5 cookies chap", "oh_yippee")
						.addCondition(Dialogue.Condition.Type.HAS_ITEM, "minecraft:cookie", "5", "Has no cookie")
						.addTrigger(new Trigger.TakeItem(Items.COOKIE, 5))
						.dontHideFailed())
		);
		
		add(new DialogueBuilder("hi_friend_can_i_help_you", Dialogue.Condition.Type.CONDITIONLESS)
				.addResponse(new ResponseBuilder("I hate you").addTrigger(new Trigger.AddConsortReputation(-100)).dontHideFailed())
				.addResponse(new ResponseBuilder("I love you").addTrigger(new Trigger.AddConsortReputation(100)).dontHideFailed())
				.addResponse(new ResponseBuilder("Rep above 500").addCondition(Dialogue.Condition.Type.HAS_MINIMUM_REPUTATION, "500", null, "Rep too low").dontHideFailed())
				.addResponse(new ResponseBuilder("Rep below 200").addCondition(Dialogue.Condition.Type.HAS_MAXIMUM_REPUTATION, "200", null, "Rep too high").dontHideFailed())
				.addResponse("bye")
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
		
		DialogueBuilder(String message, Dialogue.Condition.Type useConditionType)
		{
			this(message, "generic_animation", "generic_extra_large", new Dialogue.UseContext(List.of(new Dialogue.Condition(useConditionType))));
		}
		
		DialogueBuilder(String message, Dialogue.Condition useCondition)
		{
			this(message, "generic_animation", "generic_extra_large", new Dialogue.UseContext(List.of(useCondition)));
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
		
		public DialogueBuilder addResponse(String response)
		{
			return addResponse(new ResponseBuilder(response));
		}
		
		public DialogueBuilder addResponse(ResponseBuilder responseBuilder)
		{
			this.responses.add(new Dialogue.Response(responseBuilder.response, responseBuilder.conditions, responseBuilder.triggers, responseBuilder.nextDialoguePath, responseBuilder.hideIfFailed));
			return this;
		}
	}
	
	public static class ResponseBuilder
	{
		private final String response;
		private final List<Dialogue.Condition> conditions;
		private final List<Trigger> triggers;
		private final ResourceLocation nextDialoguePath;
		private boolean hideIfFailed;
		
		ResponseBuilder(String response)
		{
			this(response, EMPTY_NEXT_PATH);
		}
		
		ResponseBuilder(String response, ResourceLocation nextDialoguePath)
		{
			this.response = response;
			this.conditions = new ArrayList<>();
			this.triggers = new ArrayList<>();
			this.nextDialoguePath = nextDialoguePath;
			this.hideIfFailed = true;
		}
		
		ResponseBuilder(String response, String nextDialoguePath)
		{
			this.response = response;
			this.conditions = new ArrayList<>();
			this.triggers = new ArrayList<>();
			this.nextDialoguePath = new ResourceLocation(Minestuck.MOD_ID, nextDialoguePath);
			this.hideIfFailed = true;
		}
		
		public ResponseBuilder addCondition(Dialogue.Condition.Type type)
		{
			this.conditions.add(new Dialogue.Condition(type));
			return this;
		}
		
		public ResponseBuilder addCondition(Dialogue.Condition.Type type, String content)
		{
			this.conditions.add(new Dialogue.Condition(type, content));
			return this;
		}
		
		public ResponseBuilder addCondition(Dialogue.Condition.Type type, String content, String contentExtra, String failureTooltip)
		{
			this.conditions.add(new Dialogue.Condition(type, content, contentExtra, failureTooltip));
			return this;
		}
		
		public ResponseBuilder addTrigger(Trigger trigger)
		{
			this.triggers.add(trigger);
			return this;
		}
		
		public ResponseBuilder dontHideFailed()
		{
			this.hideIfFailed = false;
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