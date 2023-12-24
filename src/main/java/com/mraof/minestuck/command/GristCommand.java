package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.NonNegativeGristSet;
import com.mraof.minestuck.command.argument.GristSetArgument;
import com.mraof.minestuck.player.GristCache;
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
	public static final String PARTIAL_SUCCESS = "commands.minestuck.grist.add.partial";
	public static final String NO_CAPACITY = "commands.minestuck.grist.add.no_capacity";
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
			Component gristComponent = GristCache.get(player).getGristSet().asTextComponent();
			source.sendSuccess(() -> Component.translatable(GET, player.getDisplayName(), gristComponent), false);
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
				GristSet remainingGrist = GristCache.get(player).addWithinCapacity(grist, GristHelper.EnumSource.CONSOLE);
				if(remainingGrist.equalContent(grist))
				{
					source.sendFailure(Component.translatable(NO_CAPACITY, player.getDisplayName()));
				} else
				{
					i++;
					if(remainingGrist.isEmpty())
						source.sendSuccess(() -> Component.translatable(SUCCESS, player.getDisplayName()), true);
					else
						source.sendSuccess(() -> Component.translatable(PARTIAL_SUCCESS, player.getDisplayName(), remainingGrist.asTextComponent()), true);
				}
			} catch(IllegalArgumentException e)
			{
				e.printStackTrace();
				source.sendFailure(Component.translatable(FAILURE, player.getDisplayName()));
			}
		}
		if(players.size() > 1)
		{
			int successCount = i;
			source.sendSuccess(() -> Component.translatable(ADD, successCount), true);
		}
		return i;
	}
	
	private static int set(CommandSourceStack source, Collection<ServerPlayer> players, NonNegativeGristSet grist)
	{
		for(ServerPlayer player : players)
		{
			GristCache.get(player).set(grist);
		}
		source.sendSuccess(() -> Component.translatable(SET, players.size(), grist.asTextComponent()), true);
		return players.size();
	}
}