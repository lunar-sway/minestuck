package com.mraof.minestuck.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

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
		
	}
}