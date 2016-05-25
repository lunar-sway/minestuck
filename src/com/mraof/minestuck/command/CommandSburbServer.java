package com.mraof.minestuck.command;

import com.mraof.minestuck.network.skaianet.SessionHandler;
import com.mraof.minestuck.util.UsernameHandler;
import com.mraof.minestuck.util.UsernameHandler.PlayerIdentifier;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;

public class CommandSburbServer extends CommandBase
{
	
	@Override
	public String getCommandName()
	{
		return "sburbServer";
	}
	
	@Override
	public String getCommandUsage(ICommandSender sender)
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
		
		PlayerIdentifier client = UsernameHandler.getForCommand(mcServer, sender, args[0]);
		PlayerIdentifier server = UsernameHandler.getForCommand(mcServer, sender, args[1]);
		
		SessionHandler.connectByCommand(sender, this, client, server);
	}
	
}