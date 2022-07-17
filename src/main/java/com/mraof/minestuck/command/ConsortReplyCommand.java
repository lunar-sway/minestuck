package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class ConsortReplyCommand
{
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal("consortreply").then(Commands.argument("id", IntegerArgumentType.integer()).then(Commands.argument("path", StringArgumentType.greedyString())
				.executes(context -> execute(context.getSource(), IntegerArgumentType.getInteger(context, "id"), StringArgumentType.getString(context, "path"))))));
	}
	
	public static int execute(CommandSourceStack source, int id, String path) throws CommandSyntaxException
	{
		ServerPlayer player = source.getPlayerOrException();
		Entity entity = player.level.getEntity(id);
		if(entity instanceof ConsortEntity consort && player.position()
				.distanceToSqr(entity.position()) < 100)
		{
			consort.commandReply(player, path);
			return 1;
		}
		return 0;
	}
}