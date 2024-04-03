package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mraof.minestuck.entity.dialogue.DialogueEntity;
import com.mraof.minestuck.entity.dialogue.DialogueNodes;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public final class SetDialogueCommand
{
	public static final String INVALID_ENTITY_KEY = "commands.minestuck.set_dialouge.invalid_entity";
	public static final String INVALID_ID_KEY = "commands.minestuck.set_dialouge.invalid_id";
	public static final String SUCCESS_KEY = "commands.minestuck.set_dialouge.success";
	private static final DynamicCommandExceptionType INVALID_ENTITY = new DynamicCommandExceptionType(entity -> Component.translatable(INVALID_ENTITY_KEY, entity));
	private static final DynamicCommandExceptionType INVALID_ID = new DynamicCommandExceptionType(id -> Component.translatable(INVALID_ID_KEY, id));
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal("set_dialogue").requires(source -> source.hasPermission(Commands.LEVEL_GAMEMASTERS))
				.then(Commands.argument("entity", EntityArgument.entity())
						.then(Commands.argument("dialogue", ResourceLocationArgument.id())
								.suggests(MSSuggestionProviders.ALL_DIALOGUE_NODES)
								.executes(context -> setDialogue(context.getSource(),
										EntityArgument.getEntity(context, "entity"), ResourceLocationArgument.getId(context, "dialogue"))))));
	}
	
	private static int setDialogue(CommandSourceStack source, Entity entity, ResourceLocation id) throws CommandSyntaxException
	{
		if(!(entity instanceof DialogueEntity dialogueEntity))
			throw INVALID_ENTITY.create(entity.getDisplayName());
		
		if(DialogueNodes.getInstance().getDialogue(id) == null)
			throw INVALID_ID.create(id);
		
		dialogueEntity.getDialogueComponent().setDialogue(id, true);
		source.sendSuccess(() -> Component.translatable(SUCCESS_KEY, entity.getDisplayName(), id), true);
		return 1;
	}
}
