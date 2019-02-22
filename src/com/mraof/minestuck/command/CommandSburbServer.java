package com.mraof.minestuck.command;

import java.util.Collections;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mraof.minestuck.network.skaianet.SessionHandler;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandSburbServer extends CommandBase
{
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
	
	}
	
	@Override
	public String getName()
	{
		return "sburbServer";
	}
	
	@Override
	public String getUsage(ICommandSender sender)
	{
		return "commands.sburbServer.usage";
	}
	
	@Override
	public int getRequiredPermissionLevel()
	{
		return 2;
	}
	
	@Override
	public void execute(MinecraftServer mcServer, ICommandSender sender, String[] args) throws CommandException
	{
		if(args.length != 2)
			throw new WrongUsageException("commands.sburbServer.usage");
		
		PlayerIdentifier client = IdentifierHandler.getForCommand(mcServer, sender, args[0]);
		PlayerIdentifier server = IdentifierHandler.getForCommand(mcServer, sender, args[1]);
		
		SessionHandler.connectByCommand(sender, this, client, server);
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
	{
		return args.length == 1 || args.length == 2 ? IdentifierHandler.getCommandAutocomplete(server, args)
				: Collections.<String>emptyList();
	}
}