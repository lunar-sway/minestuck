package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mraof.minestuck.command.argument.GristSetArgument;
import com.mraof.minestuck.item.crafting.alchemy.GristHelper;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.NonNegativeGristSet;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;

public class GristCommand
{
	public static final String GET = "commands.minestuck.grist.get";
	public static final String ADD = "commands.minestuck.grist.add";
	public static final String SUCCESS = "commands.minestuck.grist.add.success";
	public static final String FAILURE = "commands.minestuck.grist.add.failure";
	public static final String SET = "commands.minestuck.grist.set";
	
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("grist").requires(source -> source.hasPermissionLevel(2)).then(createGet()).then(createAdd()).then(createSet()));
	}
	
	private static ArgumentBuilder<CommandSource, ?> createGet()
	{
		return Commands.literal("get").then(Commands.argument("targets", EntityArgument.players()).executes(context -> get(context.getSource(), EntityArgument.getPlayers(context, "targets"))));
	}
	private static ArgumentBuilder<CommandSource, ?> createAdd()
	{
		return Commands.literal("add").then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("grist", GristSetArgument.gristSet()).executes(context -> add(context.getSource(), EntityArgument.getPlayers(context, "targets"), GristSetArgument.getGristArgument(context, "grist")))));
	}
	private static ArgumentBuilder<CommandSource, ?> createSet()
	{
		return Commands.literal("set").then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("grist", GristSetArgument.nonNegativeSet()).executes(context -> set(context.getSource(), EntityArgument.getPlayers(context, "targets"), GristSetArgument.getNonNegativeGristArgument(context, "grist")))));
	}
	
	private static int get(CommandSource source, Collection<ServerPlayerEntity> players)
	{
		for(ServerPlayerEntity player : players)
		{
			GristSet grist = PlayerSavedData.getData(player).getGristCache();
			source.sendFeedback(new TranslationTextComponent(GET, player.getDisplayName(), grist.asTextComponent()), false);
		}
		return players.size();
	}
	
	private static int add(CommandSource source, Collection<ServerPlayerEntity> players, GristSet grist)
	{
		int i = 0;
		for(ServerPlayerEntity player : players)
		{
			try
			{
				GristHelper.increase(player.world, IdentifierHandler.encode(player), grist);
				i++;
				source.sendFeedback(new TranslationTextComponent(SUCCESS, player.getDisplayName()), true);
			} catch(IllegalArgumentException e)
			{
				e.printStackTrace();
				source.sendErrorMessage(new TranslationTextComponent(FAILURE, player.getDisplayName()));
			}
		}
		source.sendFeedback(new TranslationTextComponent(ADD, i), true);
		return i;
	}
	
	private static int set(CommandSource source, Collection<ServerPlayerEntity> players, NonNegativeGristSet grist)
	{
		for(ServerPlayerEntity player : players)
		{
			PlayerSavedData.getData(player).setGristCache(grist);
		}
		source.sendFeedback(new TranslationTextComponent(SET, players.size(), grist.asTextComponent()), true);
		return players.size();
	}
}