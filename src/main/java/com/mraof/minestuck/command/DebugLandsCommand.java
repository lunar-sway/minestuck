package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mraof.minestuck.command.argument.LandTypePairArgument;
import com.mraof.minestuck.command.argument.ListArgument;
import com.mraof.minestuck.skaianet.CommandActionHandler;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

/**
 * A command that is useful for testing the land skybox as it creates debug lands that is connected to the user
 */
public class DebugLandsCommand
{
	public static final String MUST_ENTER = "commands.minestuck.debuglands.must_enter";
	public static final SimpleCommandExceptionType MUST_ENTER_EXCEPTION = new SimpleCommandExceptionType(Component.translatable(MUST_ENTER));
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal("debuglands").then(Commands.argument("lands", ListArgument.list(LandTypePairArgument.nullablePairs()))
				.executes(context -> createDebugLands(context.getSource(), ListArgument.getListArgument(context, "lands")))));
	}
	
	private static int createDebugLands(CommandSourceStack source, List<LandTypePair> landTypes) throws CommandSyntaxException
	{
		ServerPlayer player = source.getPlayerOrException();
		CommandActionHandler.createDebugLandsChain(player, landTypes, source);
		return 1;
	}
}