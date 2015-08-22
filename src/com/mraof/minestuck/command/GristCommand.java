package com.mraof.minestuck.command;

import com.mraof.minestuck.util.GristAmount;
import com.mraof.minestuck.util.GristHelper;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.UsernameHandler;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;

public class GristCommand extends CommandBase
{
	@Override
	public String getName()
	{
		return "grist";
	}

	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		return "commands.grist.usage";
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException
	{
		if(args.length < 1)
		{
			sender.addChatMessage(new ChatComponentTranslation(this.getCommandUsage(sender)));
			return;
		}
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
			name = UsernameHandler.encode(player.getName());
		}

		if(command.equalsIgnoreCase("set"))
		{
			GristSet grist = parseGrist(args, offset);
			for(GristAmount amount : grist.getArray())
			{
				GristHelper.setGrist(name, amount.getType(), amount.getAmount());
			}
		}
		else if(command.equalsIgnoreCase("add"))
		{
			GristHelper.increase(name, parseGrist(args, offset));
		}
		else if(command.equalsIgnoreCase("get"))
		{
			String displayName = name.equals(".client") ? "Client Player" : name;
			StringBuilder grist = new StringBuilder();
			System.out.println(MinestuckPlayerData.getGristSet(name));
			for(GristAmount amount : MinestuckPlayerData.getGristSet(name).getArray())
			{
				System.out.println(amount);
				grist.append("\n" + amount.getAmount() + " " + amount.getType().getDisplayName());	//TODO properly translate display name for client side
			}
			sender.addChatMessage(new ChatComponentTranslation("grist.get", displayName, grist.toString()));
		}
	}

	public GristSet parseGrist(String[] args, int startOffset)
	{
		GristSet grist = new GristSet();
		for(int i = startOffset; i < args.length - 1; i += 2)
		{
			grist.addGrist(GristType.getTypeFromString(args[i].substring(0,1).toUpperCase() + args[i].substring(1).toLowerCase()), Integer.parseInt(args[i + 1]));
		}
		return grist;
	}
}
