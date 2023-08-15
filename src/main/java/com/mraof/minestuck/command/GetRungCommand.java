package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mraof.minestuck.player.PlayerSavedData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import static org.stringtemplate.v4.compiler.STLexer.str;

public class GetRungCommand
{
	public static final String SUCCESS = "commands.minestuck.get_rung";
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal("rung").then(Commands.argument("target", EntityArgument.player())
				.executes(GetRungCommand::executeGetRung)));
	}
	
	public static int executeGetRung(CommandContext<CommandSourceStack> context)
	{
		ServerPlayer player = context.getSource().getPlayer();
		String playerName = player.getScoreboardName();
		Integer rungInt = PlayerSavedData.getData(player).getEcheladder().getRung();
		
		context.getSource().sendSuccess(() -> Component.translatable(SUCCESS, playerName, rungInt.toString()), false);
		return rungInt;
	}
}
