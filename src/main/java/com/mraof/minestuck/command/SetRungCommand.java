package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.player.PlayerSavedData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class SetRungCommand
{
	public static final String SUCCESS = "commands.minestuck.set_rung";
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal("setrung").requires(source -> source.hasPermission(2)).then(Commands.argument("target", EntityArgument.players())
				.then(Commands.argument("rung", IntegerArgumentType.integer(0, Echeladder.RUNG_COUNT - 1)).executes(context -> setRung(context.getSource(), EntityArgument.getPlayers(context, "target"), IntegerArgumentType.getInteger(context, "rung"), 0))
				.then(Commands.argument("progress", DoubleArgumentType.doubleArg(0, 1)).executes(context -> setRung(context.getSource(), EntityArgument.getPlayers(context, "target"), IntegerArgumentType.getInteger(context, "rung"), DoubleArgumentType.getDouble(context, "progress")))))));
	}
	
	private static int setRung(CommandSourceStack source, Collection<ServerPlayer> players, int rung, double progress)
	{
		for(ServerPlayer player : players)
		{
			PlayerSavedData.getData(player).getEcheladder().setByCommand(rung, progress);
		}
		
		source.sendSuccess(() -> Component.translatable(SUCCESS, players.size(), rung, progress), true);
		return players.size();
	}
}