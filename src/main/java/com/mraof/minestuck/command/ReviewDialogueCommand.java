package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mraof.minestuck.command.argument.DialogueCategoryArgument;
import com.mraof.minestuck.entity.dialogue.Dialogue;
import com.mraof.minestuck.entity.dialogue.DialogueEntity;
import com.mraof.minestuck.entity.dialogue.RandomlySelectableDialogue;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;

import java.util.Collection;

public final class ReviewDialogueCommand
{
	public static final String INVALID_TYPE_KEY = "commands.minestuck.review_dialouge.invalid_type";
	public static final String SUCCESS_KEY = "commands.minestuck.review_dialouge.success";
	private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.summon.failed"));
	private static final SimpleCommandExceptionType INVALID_TYPE = new SimpleCommandExceptionType(Component.translatable(INVALID_TYPE_KEY));
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext)
	{
		dispatcher.register(Commands.literal("review_dialogue").requires(source -> source.hasPermission(Commands.LEVEL_GAMEMASTERS))
				.then(Commands.argument("entity", ResourceArgument.resource(buildContext, Registries.ENTITY_TYPE))
						.suggests(MSSuggestionProviders.instance().DIALOGUE_ENTITY_TYPE)
						.then(Commands.argument("category", new DialogueCategoryArgument())
								.executes(context -> spawnDialogueEntities(context.getSource(),
										ResourceArgument.getSummonableEntityType(context, "entity").value(),
										DialogueCategoryArgument.getCategory(context, "category"))))));
	}
	
	private static int spawnDialogueEntities(CommandSourceStack source, EntityType<?> entityType, RandomlySelectableDialogue.DialogueCategory category) throws CommandSyntaxException
	{
		Collection<Dialogue.SelectableDialogue> dialogueCollection = RandomlySelectableDialogue.instance(category).getAll();
		
		ServerLevel level = source.getLevel();
		BlockPos pos = BlockPos.containing(source.getPosition());
		
		for(Dialogue.SelectableDialogue dialogue : dialogueCollection)
		{
			pos = pos.east(2);
			level.removeBlock(pos, false);
			level.setBlock(pos.below(), Blocks.BRICKS.defaultBlockState(), Block.UPDATE_ALL);
			
			DialogueEntity dialogueEntity = spawnEntity(entityType, level, pos);
			
			dialogueEntity.getDialogueComponent().setDialogue(dialogue.dialogueId(), true);
			
			placeSign(dialogue, pos, level);
		}
		
		source.sendSuccess(() -> Component.translatable(SUCCESS_KEY, dialogueCollection.size()), true);
		
		return dialogueCollection.size();
	}
	
	private static DialogueEntity spawnEntity(EntityType<?> entityType, ServerLevel level, BlockPos pos) throws CommandSyntaxException
	{
		Entity entity = entityType.spawn(level, pos, MobSpawnType.COMMAND);
		if(entity == null)
			throw ERROR_FAILED.create();
		
		entity.setNoGravity(true);
		if(entity instanceof Mob mob)
			mob.setNoAi(true);
		
		if(entity instanceof DialogueEntity dialogueEntity)
			return dialogueEntity;
		else
			throw INVALID_TYPE.create();
	}
	
	private static void placeSign(Dialogue.SelectableDialogue dialogue, BlockPos pos, ServerLevel level)
	{
		BlockPos signPos = pos.north().below();
		level.setBlock(signPos, Blocks.OAK_WALL_SIGN.defaultBlockState().setValue(WallSignBlock.FACING, Direction.NORTH), Block.UPDATE_ALL);
		if(level.getBlockEntity(signPos) instanceof SignBlockEntity sign)
		{
			SignText signText = new SignText().setHasGlowingText(true).setColor(DyeColor.WHITE)
					.setMessage(0, Component.literal(dialogue.dialogueId().getNamespace()));
			String[] lines = dialogue.dialogueId().getPath().split("/", 3);
			for(int i = 0; i < lines.length; i++)
				signText = signText.setMessage(i + 1, Component.literal(lines[i]));
			sign.setText(signText, true);
		}
	}
}
