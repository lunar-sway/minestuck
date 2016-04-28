package com.mraof.minestuck.command;

import java.util.Arrays;

import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.util.Title;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import com.mraof.minestuck.world.lands.title.TitleLandAspect;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

public class CommandSburbSession extends CommandBase	//TODO properly localize all messages related to this command
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
	public int getRequiredPermissionLevel()
	{
		return 2;
	}
	
	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException
	{
		if(args.length < 2 || args.length < 3 && args[1].equalsIgnoreCase("add")
				|| args.length != 4 && (args[1].equalsIgnoreCase("landTerrain") || args[1].equalsIgnoreCase("landTitle"))
				|| args.length != 5 && args[1].equalsIgnoreCase("title"))
			throw new WrongUsageException(this.getCommandUsage(sender));
		String sessionName = args[0];
		String command = args[1];
		
		if(command.equalsIgnoreCase("name"))
		{
			String playerName = args.length < 2 ? getCommandSenderAsPlayer(sender).getCommandSenderName() : args[2];
			SburbHandler.sessionName(sender, this, playerName, sessionName);
			
		} else if(command.equalsIgnoreCase("add")/* || command.equalsIgnoreCase("finish")*/)
		{
			String[] params = Arrays.copyOfRange(args, 2, args.length);
			SburbHandler.managePredefinedSession(sender, this, sessionName, params, false);//command.equalsIgnoreCase("finish"));
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
			
			SburbHandler.predefineTitle(sender, this, playerName, sessionName, new Title(titleClass, titleAspect));
			
		} else if(command.equalsIgnoreCase("landTerrain"))
		{
			String playerName = args[2];
			
			TerrainLandAspect landAspect = LandAspectRegistry.fromNameTerrain(args[3]);
			if(landAspect == null)
				throw new CommandException("Can't find terrain land aspect by the name %s", args[3]);
			
			SburbHandler.predefineTerrainLandAspect(sender, this, playerName, sessionName, landAspect);
			
		} else if(command.equalsIgnoreCase("landTitle"))
		{
			String playerName = args[2];
			
			TitleLandAspect landAspect = LandAspectRegistry.fromNameTitle(args[3]);
			if(landAspect == null)
				throw new CommandException("Can't find title land aspect by the name %s", args[3]);
			
			SburbHandler.predefineTitleLandAspect(sender, this, playerName, sessionName, landAspect);
			
		} else throw new WrongUsageException(this.getCommandUsage(sender));
	}
}