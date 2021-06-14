package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.skaianet.CommandActionHandler;
import com.mraof.minestuck.skaianet.MergeResult;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class SburbConnectionCommand
{
	public static final String SUCCESS = "commands.minestuck.sburbconnection.success";
	public static final String LOCKED = "commands.minestuck.sburbconnection.locked";
	public static final String ALREADY_CONNECTED = "commands.minestuck.sburbconnection.already_connected";
	
	public static final SimpleCommandExceptionType LOCKED_EXCEPTION = new SimpleCommandExceptionType(new TranslationTextComponent(LOCKED));
	public static final SimpleCommandExceptionType CONNECTED_EXCEPTION = new SimpleCommandExceptionType(new TranslationTextComponent(ALREADY_CONNECTED));
	public static final DynamicCommandExceptionType MERGE_EXCEPTION = new DynamicCommandExceptionType(o -> new TranslationTextComponent(((MergeResult) o).translationKey()));
	
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(Commands.literal("sburbconnection").requires(source -> source.hasPermission(2)).then(Commands.argument("client", EntityArgument.player()).then(Commands.argument("server", EntityArgument.player())
				.executes(context -> connect(context.getSource(), EntityArgument.getPlayer(context, "client"), EntityArgument.getPlayer(context, "server"))))));
	}
	
	private static int connect(CommandSource source, ServerPlayerEntity client, ServerPlayerEntity server) throws CommandSyntaxException
	{
		return CommandActionHandler.connectByCommand(source, IdentifierHandler.encode(client), IdentifierHandler.encode(server));
	}
}