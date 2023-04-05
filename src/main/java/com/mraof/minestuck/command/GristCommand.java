package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.NonNegativeGristSet;
import com.mraof.minestuck.command.argument.GristSetArgument;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerSavedData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class GristCommand
{
	public static final String GET = "commands.minestuck.grist.get";
	public static final String ADD = "commands.minestuck.grist.add";
	public static final String SUCCESS = "commands.minestuck.grist.add.success";
	public static final String FAILURE = "commands.minestuck.grist.add.failure";
	public static final String SET = "commands.minestuck.grist.set";
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal("grist").requires(source -> source.hasPermission(2)).then(createGet()).then(createAdd()).then(createSet()));
	}
	
	private static ArgumentBuilder<CommandSourceStack, ?> createGet()
	{
		return Commands.literal("get").then(Commands.argument("targets", EntityArgument.players()).executes(context -> get(context.getSource(), EntityArgument.getPlayers(context, "targets"))));
	}
	private static ArgumentBuilder<CommandSourceStack, ?> createAdd()
	{
		return Commands.literal("add").then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("grist", GristSetArgument.gristSet()).executes(context -> add(context.getSource(), EntityArgument.getPlayers(context, "targets"), GristSetArgument.getGristArgument(context, "grist")))));
	}
	private static ArgumentBuilder<CommandSourceStack, ?> createSet()
	{
		return Commands.literal("set").then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("grist", GristSetArgument.nonNegativeSet()).executes(context -> set(context.getSource(), EntityArgument.getPlayers(context, "targets"), GristSetArgument.getNonNegativeGristArgument(context, "grist")))));
	}
	
	private static int get(CommandSourceStack source, Collection<ServerPlayer> players)
	{
		for(ServerPlayer player : players)
		{
			GristSet grist = PlayerSavedData.getData(player).getGristCache().getGristSet();
			source.sendSuccess(Component.translatable(GET, player.getDisplayName(), grist.asTextComponent()), false);
		}
		return players.size();
	}
	
	private static int add(CommandSourceStack source, Collection<ServerPlayer> players, GristSet grist)
	{
		int i = 0;
		for(ServerPlayer player : players)
		{
			try
			{
				GristHelper.increaseAndNotify(player.level, IdentifierHandler.encode(player), grist, GristHelper.EnumSource.CONSOLE);
				i++;
				source.sendSuccess(Component.translatable(SUCCESS, player.getDisplayName()), true);
			} catch(IllegalArgumentException e)
			{
				e.printStackTrace();
				source.sendFailure(Component.translatable(FAILURE, player.getDisplayName()));
			}
		}
		source.sendSuccess(Component.translatable(ADD, i), true);
		return i;
	}
	
	private static int set(CommandSourceStack source, Collection<ServerPlayer> players, NonNegativeGristSet grist)
	{
		for(ServerPlayer player : players)
		{
			PlayerSavedData.getData(player).getGristCache().setGristSet(grist);
		}
		source.sendSuccess(Component.translatable(SET, players.size(), grist.asTextComponent()), true);
		return players.size();
	}
}