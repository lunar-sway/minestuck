package com.mraof.minestuck.command;

import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.GristAmount;
import com.mraof.minestuck.util.GristHelper;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.UsernameHandler;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;

public class CommandGrist extends CommandBase
{
	@Override
	public String getCommandName()
	{
		return "grist";
	}

	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		return "commands.grist.usage";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException
	{
		if(args.length < 1 || (!args[0].equalsIgnoreCase("get") && args.length < 2))
			throw new WrongUsageException(this.getCommandUsage(sender));
		
		String command = args[0];
		String name;
		int offset = 1;
		if(!(command.equalsIgnoreCase("set") || command.equalsIgnoreCase("add") || command.equalsIgnoreCase("get")))
		{
			command = args[1];
			name = args[0];
			offset = 2;
		} else
		{
			EntityPlayerMP player = this.getCommandSenderAsPlayer(sender);
			name = UsernameHandler.encode(player.getCommandSenderName());
		}
		
		if((args.length - offset) % 2  != 0)
			throw new WrongUsageException(this.getCommandUsage(sender));
		
		String displayName = name.equals(".client") ? "Client Player" : name;
		
		if(command.equalsIgnoreCase("set"))
		{
			GristAmount[] grist = parseGrist(args, offset);	//Using a GristAmount array instead of a GristSet gives support for setting grist to 0
			for(GristAmount amount : grist)
				GristHelper.setGrist(name, amount.getType(), amount.getAmount());
			MinestuckPlayerTracker.updateGristCache(name);
			notifyOperators(sender, this, "commands.grist.setSuccess", name);
		}
		else if(command.equalsIgnoreCase("add"))
		{
			GristSet grist = new GristSet(parseGrist(args, offset));
			GristHelper.increase(name, grist);
			MinestuckPlayerTracker.updateGristCache(name);
			notifyOperators(sender, this, "commands.grist.addSuccess", name);
		}
		else if(command.equalsIgnoreCase("get"))
		{
			StringBuilder grist = new StringBuilder();
			System.out.println(MinestuckPlayerData.getGristSet(name));
			for(GristAmount amount : MinestuckPlayerData.getGristSet(name).getArray())
				grist.append("\n" + amount.getAmount() + " " + amount.getType().getDisplayName());	//TODO properly translate display name for client side
			
			sender.addChatMessage(new ChatComponentTranslation("commands.grist.get", displayName, grist.toString()));
		}
	}

	public GristAmount[] parseGrist(String[] args, int startOffset) throws CommandException
	{
		GristAmount[] grist = new GristAmount[(args.length - startOffset)/2];
		for(int i = startOffset; i < args.length - 1; i += 2)
		{
			GristType type = GristType.getTypeFromString(args[i].substring(0,1).toUpperCase() + args[i].substring(1).toLowerCase());
			if(type == null)
				throw new SyntaxErrorException("commands.grist.invalidSyntax", args[i]);
			grist[(i - startOffset)/2] = new GristAmount(type, parseInt(args[i + 1]));
		}
		
		return grist;
	}
}
