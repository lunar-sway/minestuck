package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mraof.minestuck.alchemy.GristGutter;
import com.mraof.minestuck.alchemy.ImmutableGristSet;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerSavedData;
import com.mraof.minestuck.skaianet.Session;
import com.mraof.minestuck.skaianet.SessionHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import static net.minecraft.commands.Commands.*;

public class GutterCommand
{
	public static final SimpleCommandExceptionType NO_SESSION_EXCEPTION = new SimpleCommandExceptionType(Component.literal("Cannot find gutter because you are not in a session."));
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(literal("grist_gutter").requires(source -> source.hasPermission(LEVEL_GAMEMASTERS)).then(subcommandShow()));
	}
	
	private static ArgumentBuilder<CommandSourceStack, ?> subcommandShow()
	{
		return literal("show").executes(context -> show(context.getSource()));
	}
	
	private static int show(CommandSourceStack source) throws CommandSyntaxException
	{
		ServerPlayer player = source.getPlayerOrException();
		
		Session session = SessionHandler.get(player.server).getPlayerSession(IdentifierHandler.encode(player));
		if(session == null)
			throw NO_SESSION_EXCEPTION.create();
		
		double multiplier = GristGutter.gutterMultiplierForSession(session, PlayerSavedData.get(player.server));
		long capacity = session.getGristGutter().getRemainingCapacity(session, PlayerSavedData.get(player.server));
		ImmutableGristSet gristSet = session.getGristGutter().getCache();
		source.sendSuccess(Component.translatable("Gutter modifier: %s, remaining capacity: %s, grist contained: %s", multiplier, capacity, gristSet.asTextComponent()), false);
		
		return 1;
	}
}
