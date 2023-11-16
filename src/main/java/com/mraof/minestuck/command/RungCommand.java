package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.player.PlayerSavedData;
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
					.then(Commands.literal("get").then(subCommandGet()))
					.then(Commands.literal("set").then(subCommandSet()))
		);
	}
	
	private static ArgumentBuilder<CommandSourceStack, ?> subCommandGet()
	{
		return Commands.argument("target", EntityArgument.player()).executes(RungCommand::getRung);
	}
	
	public static int getRung(CommandContext<CommandSourceStack> context)
	{
		ServerPlayer player;
		try {
			player = EntityArgument.getPlayer(context, "target");
		} catch (CommandSyntaxException ignored) {
			return 0;
		}
		int rung = PlayerSavedData.getData(player).getEcheladder().getRung();
		
		context.getSource().sendSuccess(() -> Component.translatable(GET_SUCCESS, player.getScoreboardName(), rung), true);
		return rung;
	}
	
	private static ArgumentBuilder<CommandSourceStack, ?> subCommandSet()
	{
		return Commands.argument("target", EntityArgument.players())
				.then(Commands.argument("rung", IntegerArgumentType.integer(0, Echeladder.RUNG_COUNT - 1)).executes(RungCommand::setRung)
						.then(Commands.argument("progress", DoubleArgumentType.doubleArg(0, 1)).executes(RungCommand::setRungProgress)));
	}
	
	private static int setRung(CommandContext<CommandSourceStack> context)
	{
		Collection<ServerPlayer> players;
		try {
			players = EntityArgument.getPlayers(context, "target");
		} catch(CommandSyntaxException ignored) {
			return 0;
		}
		int rung = IntegerArgumentType.getInteger(context, "rung");
		
		players.forEach(p -> PlayerSavedData.getData(p).getEcheladder().setByCommand(rung, 0));
		
		context.getSource().sendSuccess(() -> Component.translatable(SET_SUCCESS, players.size(), rung, 0), true);
		return players.size();
	}
	
	private static int setRungProgress(CommandContext<CommandSourceStack> context)
	{
		Collection<ServerPlayer> players;
		try {
			players = EntityArgument.getPlayers(context, "target");
		} catch(CommandSyntaxException ignored) {
			return 0;
		}
		int rung = IntegerArgumentType.getInteger(context, "rung");
		double progress = DoubleArgumentType.getDouble(context, "progress");
		
		players.forEach(p -> PlayerSavedData.getData(p).getEcheladder().setByCommand(rung, progress));
		
		context.getSource().sendSuccess(() -> Component.translatable(SET_SUCCESS, players.size(), rung, progress), true);
		return players.size();
	}
}
