package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;

public class ConsortReplyCommand
{
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("consortreply").then(Commands.argument("id", IntegerArgumentType.integer()).then(Commands.argument("path", StringArgumentType.greedyString())
				.executes(context -> execute(context.getSource(), IntegerArgumentType.getInteger(context, "id"), StringArgumentType.getString(context, "path"))))));
	}
	
	public static int execute(CommandSource source, int id, String path) throws CommandSyntaxException
	{
		ServerPlayerEntity player = source.asPlayer();
		Entity entity = player.world.getEntityByID(id);
		if(entity instanceof ConsortEntity && player.getPositionVec()
				.squareDistanceTo(entity.getPositionVec()) < 100)
		{
			ConsortEntity consort = (ConsortEntity) entity;
			consort.commandReply(player, path);
			return 1;
		}
		return 0;
	}
}