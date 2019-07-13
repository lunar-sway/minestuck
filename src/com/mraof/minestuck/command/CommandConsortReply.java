package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mraof.minestuck.entity.consort.ConsortEntity;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3d;

public class CommandConsortReply
{
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("consortreply").then(Commands.argument("id", IntegerArgumentType.integer())).then(Commands.argument("path", StringArgumentType.word()))
				.executes(context -> execute(context.getSource(), IntegerArgumentType.getInteger(context, "id"), StringArgumentType.getString(context, "path"))));
	}
	
	public static int execute(CommandSource source, int id, String path) throws CommandSyntaxException
	{
		EntityPlayerMP player = source.asPlayer();
		Entity entity = player.world.getEntityByID(id);
		if(entity instanceof ConsortEntity && new Vec3d(player.posX, player.posY, player.posZ)
				.squareDistanceTo(entity.posX, entity.posY, entity.posZ) < 100)
		{
			ConsortEntity consort = (ConsortEntity) entity;
			consort.commandReply(player, path);
		}
		return 1;
	}
}