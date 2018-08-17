package com.mraof.minestuck.command;

import java.util.Collections;
import java.util.List;

import com.mraof.minestuck.util.Echeladder;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;
import com.mraof.minestuck.util.MinestuckPlayerData;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandSetRung extends CommandBase
{
	
	@Override
	public String getName()
	{
		return "setRung";
	}
	
	@Override
	public String getUsage(ICommandSender sender)
	{
		return "commands.setRung.usage";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(args.length < 2)
			throw new WrongUsageException(this.getUsage(sender));
		
		PlayerIdentifier target = IdentifierHandler.getForCommand(server, sender, args[0]);
		
		int rung = parseInt(args[1], 0, Echeladder.RUNG_COUNT - 1);
		double progress = 0;
		if(args.length >= 3)
			progress = parseDouble(args[2], 0, 1);
		
		Echeladder echeladder = MinestuckPlayerData.getData(target).echeladder;
		echeladder.setByCommand(rung, progress);
		
		notifyCommandListener(sender, this, "commands.setRung.success", target.getUsername(), rung, progress*100);
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
	{
		return args.length == 1 ? IdentifierHandler.getCommandAutocomplete(server, args) : Collections.<String>emptyList();
	}
	
	@Override
	public int getRequiredPermissionLevel()
	{
		return 2;
	}
	
}