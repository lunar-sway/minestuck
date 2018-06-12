package com.mraof.minestuck.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SessionHandler;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.GristAmount;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandGristSend extends CommandBase
{
	
	@Override
	public String getName()
	{
		return "gristSend";
	}
	
	@Override
	public List getAliases()
	{
		ArrayList<String> list = new ArrayList<String>();
		list.add("gristSend");
		list.add("gristGive");
		list.add("sendGrist");
		list.add("giveGrist");
		return list;
	}
	
	@Override
	public String getUsage(ICommandSender sender)
	{
		return (sender instanceof EntityPlayerMP) ? "commands.gristSend.usage" : "commands.playerOnly.redirectGrist";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(!(sender instanceof EntityPlayerMP))
			throw new PlayerNotFoundException("commands.playerOnly.redirectGrist");
		EntityPlayerMP player = (EntityPlayerMP) sender;
		
		if(args.length < 3 || args.length % 2 == 0)
			throw new WrongUsageException(this.getUsage(sender));
		
		String receiver = args[0];
		EntityPlayerMP receivingPlayer = server.getPlayerList().getPlayerByUsername(receiver);
		if(receivingPlayer != null)
		{
			if(!isPermittedFor(player, receivingPlayer))
				throw new CommandException("commands.gristSend.notPermitted", receivingPlayer.getName());
			
			GristSet set = new GristSet();
			GristAmount[] parsedAmounts = CommandGrist.parseGrist(args, 1);
			for(GristAmount amount : parsedAmounts)
				set.addGrist(amount);
			
			GristSet playerGrist = MinestuckPlayerData.getGristSet(player);
			GristSet receiverGrist = MinestuckPlayerData.getGristSet(receivingPlayer);
			
			StringBuilder costStr = new StringBuilder();
			boolean first = true;
			for(GristType type : GristType.values())
			{
				int i = Math.max(0, Math.min(playerGrist.getGrist(type), set.getGrist(type)));
				
				if(i > 0)
				{
					playerGrist.addGrist(type, -i);
					receiverGrist.addGrist(type, i);
					
					if(!first)
						costStr.append(", ");
					costStr.append(i + " " + type.getDisplayName());
					first = false;
				}
			}
			
			PlayerIdentifier senderIdentifier;
			PlayerIdentifier receiverIdentifier;
			
			senderIdentifier = IdentifierHandler.encode(player);
			receiverIdentifier = IdentifierHandler.encode(receivingPlayer);
			
			MinestuckPlayerTracker.updateGristCache(senderIdentifier);
			MinestuckPlayerTracker.updateGristCache(receiverIdentifier);

			notifyCommandListener(sender, this, "commands.gristSend.success", receiver, costStr.toString());
			
		} else throw new PlayerNotFoundException("Couldn't find player \"%s\".", receiver);
	}
	
	private static boolean isPermittedFor(EntityPlayerMP player, EntityPlayerMP player2)
	{
		PlayerIdentifier name1 = IdentifierHandler.encode(player), name2 = IdentifierHandler.encode(player2);
		SburbConnection c1 = SkaianetHandler.getMainConnection(name1, true);
		SburbConnection c2 = SkaianetHandler.getMainConnection(name2, true);
		if(c1 == null || c2 == null || !c1.enteredGame() || !c2.enteredGame())
			return false;
		else return SessionHandler.getPlayerSession(name1) == SessionHandler.getPlayerSession(name2);
	}
	
	@Override
	public int getRequiredPermissionLevel()
	{
		return 0;
	}
	
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender)
	{
		return true;
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
	{
		return args.length == 1 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) :
			(args.length > 1 && (args.length%2 == 1) ? getListOfStringsMatchingLastWord(args, GristType.getNames())
					: Collections.<String>emptyList());
	}
}