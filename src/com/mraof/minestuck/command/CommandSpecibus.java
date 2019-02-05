package com.mraof.minestuck.command;

import java.util.ArrayList;
import java.util.List;

import com.mraof.minestuck.inventory.specibus.StrifePortfolioHandler;
import com.mraof.minestuck.inventory.specibus.StrifeSpecibus;
import com.mraof.minestuck.util.KindAbstratusList;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandSpecibus extends CommandBase
{

	@Override
	public String getName() 
	{
		return "strifePortfolio";
	}

	@Override
	public String getUsage(ICommandSender sender) 
	{
		return "commands.specibus.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException 
	{
		if ((!args[1].equals("clear") && args.length < 3) || args.length < 2)
            throw new WrongUsageException(getUsage(sender), new Object[0]);
		
		EntityPlayer player = getEntity(server, sender, args[0], EntityPlayer.class);
		
		if(args[1].equals("add"))
		{
			StrifePortfolioHandler.addSpecibus(player, new StrifeSpecibus(KindAbstratusList.getTypeFromName(args[2])));
		}
		else if(args[1].equals("remove"))
		{
			//TODO
		}
		else if(args[1].equals("clear"))
		{
			StrifePortfolioHandler.setPortfolio(player, new ArrayList<StrifeSpecibus>());
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) 
	{
		switch(args.length)
		{
		case 1: return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
		case 2: return getListOfStringsMatchingLastWord(args, "add", "remove", "clear");
		case 3:
			if(args[2] != "clear") return getListOfStringsMatchingLastWord(args, KindAbstratusList.getNamesList());
		default: return super.getTabCompletions(server, sender, args, targetPos);
		}
	}
}
