package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mraof.minestuck.command.argument.LandTypePairArgument;
import com.mraof.minestuck.command.argument.ListArgument;
import com.mraof.minestuck.skaianet.SessionHandler;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

/**
 * A command that is useful for testing the land skybox as it creates debug lands that is connected to the user
 */
public class DebugLandsCommand
{
	public static final String MUST_ENTER = "commands.minestuck.debuglands.must_enter";
	public static final SimpleCommandExceptionType MUST_ENTER_EXCEPTION = new SimpleCommandExceptionType(new TranslationTextComponent(MUST_ENTER));
	
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("debuglands").then(Commands.argument("lands", ListArgument.list(LandTypePairArgument.nullablePairs()))
				.executes(context -> createDebugLands(context.getSource(), ListArgument.getListArgument(context, "lands")))));
	}
	
	private static int createDebugLands(CommandSource source, List<LandTypePair> landTypes) throws CommandSyntaxException
	{
		ServerPlayerEntity player = source.asPlayer();
		SessionHandler.get(player.server).createDebugLandsChain(player, landTypes, source);
		return 1;
	}
}