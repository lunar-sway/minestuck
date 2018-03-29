package com.mraof.minestuck.command;

import com.mraof.minestuck.item.ItemBoondollars;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.command.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
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
		if(args.length < 2)
			throw new WrongUsageException(this.getUsage(sender));
		
		String subCommand = args[0];
		
		if(subCommand.equalsIgnoreCase("send"))
		{
			if(args.length < 3)
				throw new WrongUsageException(this.getUsage(sender));
			EntityPlayerMP otherPlayer = getPlayer(server, sender, args[1]);
			int amount = parseInt(args[2], 0);
			if(MinestuckPlayerData.addBoondollars(player, -amount))
			{
				MinestuckPlayerData.addBoondollars(otherPlayer, amount);
				player.sendMessage(new TextComponentTranslation("commands.porkhollow.sendSuccess", amount, otherPlayer.getDisplayName()));
			} else throw new CommandException("commands.porkhollow.notEnough");
		} else if(subCommand.equalsIgnoreCase("take"))
		{
			int amount = parseInt(args[1], 1);
			if(MinestuckPlayerData.addBoondollars(player, -amount))
			{
				ItemStack stack = ItemBoondollars.setCount(new ItemStack(MinestuckItems.boondollars), amount);
				if (!player.addItemStackToInventory(stack))
				{
					EntityItem entity = player.dropItem(stack, false);
					if (entity != null)
						entity.setNoPickupDelay();
				} else player.inventoryContainer.detectAndSendChanges();
				
				player.sendMessage(new TextComponentTranslation("commands.porkhollow.takeSuccess", amount));
			} else throw new CommandException("commands.porkhollow.notEnough");
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
