package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.player.Echeladder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class RungCommand
{
	public static final String GET_SUCCESS = "commands.minestuck.get_rung";
	public static final String SET_SUCCESS = "commands.minestuck.set_rung";
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(
			Commands.literal("rung").requires(s -> s.hasPermission(Commands.LEVEL_GAMEMASTERS))
					.then(subCommandGet())
					.then(subCommandSet())
		);
	}
	
	private static ArgumentBuilder<CommandSourceStack, ?> subCommandGet()
	{
		return Commands.literal("get")
				.then(Commands.argument("target", EntityArgument.player())
						.executes(context -> getRung(context, EntityArgument.getPlayer(context, "target"))));
	}
	
	private static int getRung(CommandContext<CommandSourceStack> context, ServerPlayer player)
	{
		int rung = Echeladder.get(player).getRung();
		
		context.getSource().sendSuccess(() -> Component.translatable(GET_SUCCESS, player.getScoreboardName(), rung), false);
		return rung;
	}
	
	private static ArgumentBuilder<CommandSourceStack, ?> subCommandSet()
	{
		return Commands.literal("set")
				.then(Commands.argument("target", EntityArgument.players())
						.then(Commands.argument("rung", IntegerArgumentType.integer(0, Echeladder.RUNG_COUNT - 1))
								.executes(context ->
										setRung(context, EntityArgument.getPlayers(context, "target"),
												IntegerArgumentType.getInteger(context, "rung"), 0)
								)
								.then(Commands.argument("progress", DoubleArgumentType.doubleArg(0, 1))
										.executes(context ->
												setRung(context, EntityArgument.getPlayers(context, "target"),
														IntegerArgumentType.getInteger(context, "rung"),
														DoubleArgumentType.getDouble(context, "progress"))
										))));
	}
	
	private static int setRung(CommandContext<CommandSourceStack> context, Collection<ServerPlayer> players, int rung, double progress)
	{
		for (ServerPlayer player : players) {
			Echeladder.get(player).setByCommand(rung, progress);
			MSCriteriaTriggers.ECHELADDER.get().trigger(player, rung);
		}
		context.getSource().sendSuccess(() -> Component.translatable(SET_SUCCESS, players.size(), rung, progress), true);
		return players.size();
	}
}
