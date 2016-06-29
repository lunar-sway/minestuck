package com.mraof.minestuck.command;

import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.GristAmount;
import com.mraof.minestuck.util.GristHelper;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

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
	public int getRequiredPermissionLevel()
	{
		return 2;	//Same as /give
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(args.length < 1 || (!args[0].equalsIgnoreCase("get") && args.length < 2))
			throw new WrongUsageException(this.getCommandUsage(sender));
		
		String command = args[0];
		PlayerIdentifier identifier;
		int offset = 1;
		if(!(command.equalsIgnoreCase("set") || command.equalsIgnoreCase("add") || command.equalsIgnoreCase("get")))
		{
			command = args[1];
			identifier = IdentifierHandler.getForCommand(server, sender, args[0]);
			offset = 2;
		} else
		{
			EntityPlayerMP player = this.getCommandSenderAsPlayer(sender);
			identifier = IdentifierHandler.encode(player);
		}
		
		if((args.length - offset) % 2  != 0)
			throw new WrongUsageException(this.getCommandUsage(sender));
		
		String displayName = identifier.getUsername();
		
		if(command.equalsIgnoreCase("set"))
		{
			GristAmount[] grist = parseGrist(args, offset);	//Using a GristAmount array instead of a GristSet gives support for setting grist to 0
			for(GristAmount amount : grist)
				GristHelper.setGrist(identifier, amount.getType(), amount.getAmount());
			MinestuckPlayerTracker.updateGristCache(identifier);
			notifyCommandListener(sender, this, "commands.grist.setSuccess", displayName);
		}
		else if(command.equalsIgnoreCase("add"))
		{
			GristSet grist = new GristSet(parseGrist(args, offset));
			GristHelper.increase(identifier, grist);
			MinestuckPlayerTracker.updateGristCache(identifier);
			notifyCommandListener(sender, this, "commands.grist.addSuccess", displayName);
		}
		else if(command.equalsIgnoreCase("get"))
		{
			StringBuilder grist = new StringBuilder();
			for(GristAmount amount : MinestuckPlayerData.getGristSet(identifier).getArray())
				grist.append("\n" + amount.getAmount() + " " + amount.getType().getDisplayName());	//TODO properly translate display name for client side
			
			sender.addChatMessage(new TextComponentTranslation("commands.grist.get", displayName, grist.toString()));
		} else throw new WrongUsageException("commands.invalidSubCommand", command);
	}

	public static GristAmount[] parseGrist(String[] args, int startOffset) throws CommandException
	{
		GristAmount[] grist = new GristAmount[(args.length - startOffset)/2];
		for(int i = startOffset; i < args.length - 1; i += 2)
		{
			GristType type = GristType.getTypeFromString(args[i].toLowerCase());
			int numIndex = 1;
			if(type == null)
			{	//Support both orders
				type = GristType.getTypeFromString(args[i + 1].toLowerCase());
				numIndex = 0;
				
				if(type == null)
					throw new SyntaxErrorException("commands.grist.invalidSyntax", args[i]);
			}
			grist[(i - startOffset)/2] = new GristAmount(type, parseInt(args[i + numIndex]));
		}
		
		return grist;
	}
}
