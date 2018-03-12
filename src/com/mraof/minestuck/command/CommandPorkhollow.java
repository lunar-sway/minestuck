package com.mraof.minestuck.command;

import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.PlayerDataPacket;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandPorkhollow extends CommandBase    //Much like /gristSend and /land, is a temporary command until a proper feature is in place
{
	@Override
	public String getName()
	{
		return "porkhollow";
	}
	
	@Override
	public String getUsage(ICommandSender sender)
	{
		return "commands.porkhollow.usage";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(!(sender instanceof EntityPlayerMP))
			throw new PlayerNotFoundException("commands.playerOnly");
		EntityPlayerMP player = (EntityPlayerMP) sender;
		MinestuckPlayerData.PlayerData data = MinestuckPlayerData.getData(player);
		if(args.length < 2)
			throw new WrongUsageException(this.getUsage(sender));
		
		String subCommand = args[0];
		
		if(subCommand.equalsIgnoreCase("send"))
		{
			if(args.length < 3)
				throw new WrongUsageException(this.getUsage(sender));
			EntityPlayerMP otherPlayer = getPlayer(server, sender, args[1]);
			int amount = parseInt(args[2], 0, data.boondollars);
			data.boondollars -= amount;
			MinestuckPlayerData.getData(otherPlayer).boondollars += amount;
			
			player.sendMessage(new TextComponentTranslation("commands.porkhollow.sendSuccess", amount, otherPlayer.getDisplayName()));
			
			MinestuckChannelHandler.sendToPlayer(MinestuckPacket.makePacket(MinestuckPacket.Type.PLAYER_DATA, PlayerDataPacket.BOONDOLLAR, data.boondollars), player);
			MinestuckChannelHandler.sendToPlayer(MinestuckPacket.makePacket(MinestuckPacket.Type.PLAYER_DATA, PlayerDataPacket.BOONDOLLAR, MinestuckPlayerData.getData(otherPlayer).boondollars), otherPlayer);
		} else if(subCommand.equalsIgnoreCase("take"))
		{
			//Extract boondollars as physical items
		}
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
}
