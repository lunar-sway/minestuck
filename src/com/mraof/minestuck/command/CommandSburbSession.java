package com.mraof.minestuck.command;

import java.util.Arrays;

import com.mraof.minestuck.network.skaianet.SessionHandler;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.util.Title;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

public class CommandSburbSession extends CommandBase
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
		if(args.length < 2 || args.length < 3 && args[1].equalsIgnoreCase("add") || args.length != 5 && (args[1].equalsIgnoreCase("title") || args[1].equalsIgnoreCase("land")))
			throw new WrongUsageException(this.getCommandUsage(sender));
		String sessionName = args[0];
		String command = args[1];
		
		if(command.equalsIgnoreCase("name"))
		{
			String playerName = args.length < 2 ? getCommandSenderAsPlayer(sender).getCommandSenderName() : args[2];
			SessionHandler.sessionName(sender, this, playerName, sessionName);
			
		} else if(command.equalsIgnoreCase("add")/* || command.equalsIgnoreCase("finish")*/)
		{
			String[] params = Arrays.copyOfRange(args, 2, args.length);
			SessionHandler.managePredefinedSession(sender, this, sessionName, params, false);//command.equalsIgnoreCase("finish"));
		} else if(command.equalsIgnoreCase("title"))
		{
			String playerName = args[2];
			String classStr = args[3], aspectStr = args[4];
			
			EnumClass titleClass = null;
			EnumAspect titleAspect = null;
			
			try	//Parse class
			{
				for(EnumClass c : EnumClass.values())
					if(c.name().equalsIgnoreCase(classStr))
					{
						titleClass = c;
						break;
					}
				
				if(titleClass == null)
				{
					int classIndex = Integer.parseInt(classStr);
					titleClass = EnumClass.getClassFromInt(classIndex);
				}
			} catch(Exception e)
			{
				throw new WrongUsageException("commands.sburbSession.notClass", classStr);
			}
			try	//Parse aspect
			{
				for(EnumAspect aspect : EnumAspect.values())
					if(aspect.name().equalsIgnoreCase(aspectStr))
					{
						titleAspect = aspect;
						break;
					}
				
				if(titleAspect == null)
				{
					int aspectIndex = Integer.parseInt(aspectStr);
					titleAspect = EnumAspect.getAspectFromInt(aspectIndex);
				}
			} catch(Exception e)
			{
				throw new WrongUsageException("commands.sburbSession.notAspect", aspectStr);
			}
			
			SessionHandler.predefineTitle(sender, this, playerName, sessionName, new Title(titleClass, titleAspect));
			
		} else if(command.equalsIgnoreCase("land"))
		{
			
		} else throw new WrongUsageException(this.getCommandUsage(sender));
	}
}