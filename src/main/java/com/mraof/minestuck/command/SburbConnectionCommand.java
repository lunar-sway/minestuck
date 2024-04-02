package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.SburbConnections;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class SburbConnectionCommand
{
	public static final String SUCCESS = "commands.minestuck.sburbconnection.success";
	public static final String ALREADY_CONNECTED = "commands.minestuck.sburbconnection.already_connected";
	
	public static final SimpleCommandExceptionType CONNECTED_EXCEPTION = new SimpleCommandExceptionType(Component.translatable(ALREADY_CONNECTED));
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal("sburbconnection").requires(source -> source.hasPermission(2)).then(Commands.argument("client", EntityArgument.player()).then(Commands.argument("server", EntityArgument.player())
				.executes(context -> connect(context.getSource(), EntityArgument.getPlayer(context, "client"), EntityArgument.getPlayer(context, "server"))))));
	}
	
	private static int connect(CommandSourceStack source, ServerPlayer client, ServerPlayer server) throws CommandSyntaxException
	{
		PlayerIdentifier clientId = IdentifierHandler.encode(client);
		PlayerIdentifier serverId = IdentifierHandler.encode(server);
		if(forceConnection(clientId, serverId, source.getServer()))
		{
			source.sendSuccess(() -> Component.translatable(SUCCESS, clientId.getUsername(), serverId.getUsername()), true);
			return 1;
		} else
		{
			throw CONNECTED_EXCEPTION.create();
		}
	}
	
	public static boolean forceConnection(PlayerIdentifier client, PlayerIdentifier server, MinecraftServer mcServer)
	{
		var connections = SburbConnections.get(mcServer);
		if(connections.isPrimaryPair(client, server))
			return false;
		
		if(!connections.hasPrimaryConnectionForClient(client)
				&& connections.getActiveConnection(client).filter(connection -> connection.server().equals(server)).isPresent())
		{
			connections.setPrimaryConnection(client, server);
			return true;
		}
		
		connections.unlinkClientPlayer(client);
		connections.unlinkServerPlayer(server);
		
		connections.setPrimaryConnection(client, server);
		
		return true;
	}
}