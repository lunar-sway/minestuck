package com.mraof.minestuck.command;

import java.util.Arrays;

import com.mraof.minestuck.network.skaianet.SessionHandler;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

public class CommandCreateSession extends CommandBase
{
	
	@Override
	public String getCommandName()
	{
		return "sburbSession";
	}
	
	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		return "commands.sburbSession.usage";
	}
	
	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException
	{
		if(args.length < 2 || args.length < 3 && !args[1].equalsIgnoreCase("finish"))
			throw new WrongUsageException(this.getCommandUsage(sender));
		String sessionName = args[0];
		String command = args[1];
		
		if(command.equalsIgnoreCase("name"))
		{
			String playerName = args[2];
			
		} else if(command.equalsIgnoreCase("add") || command.equalsIgnoreCase("finish"))
		{
			String[] params = Arrays.copyOfRange(args, 2, args.length);
			SessionHandler.managePredefinedSession(sender, this, sessionName, params, command.equalsIgnoreCase("finish"));
		}
	}
}