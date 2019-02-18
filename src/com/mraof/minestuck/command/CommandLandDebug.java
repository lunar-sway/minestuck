package com.mraof.minestuck.command;

import com.mraof.minestuck.network.skaianet.SessionHandler;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import com.mraof.minestuck.world.lands.title.TitleLandAspect;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;
import java.util.List;

public class CommandLandDebug extends CommandBase
{
	@Override
	public String getName()
	{
		return "landDebug";
	}
	
	@Override
	public String getUsage(ICommandSender sender)
	{
		return null;
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		EntityPlayer player = getCommandSenderAsPlayer(sender);
		
		List<LandAspectRegistry.AspectCombination> landspects = new ArrayList<>();
		for(int i = 0; i < args.length; i++)
		{
			if(args[i].equals("~"))
			{
				landspects.add(null);
				continue;
			}
			
			TerrainLandAspect landTerrain = LandAspectRegistry.fromNameTerrain(args[i].toLowerCase());
			if(landTerrain == null)
				throw new CommandException("Can't find terrain land aspect by the name %s", args[i]);
			if(i + 1 >= args.length)
				throw new CommandException("Not enough arguments provided!");
			i++;
			TitleLandAspect landTitle = LandAspectRegistry.fromNameTitle(args[i].toLowerCase());
			if(landTitle == null)
				throw new CommandException("Can't find title land aspect by the name %s", args[i]);
			landspects.add(new LandAspectRegistry.AspectCombination(landTerrain, landTitle));
		}
		
		SessionHandler.createDebugLandsChain(landspects, player);
		sender.sendMessage(new TextComponentString("The chain has now been created."));
	}
}
