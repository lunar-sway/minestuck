package com.mraof.minestuck.inventory;

import com.mraof.minestuck.editmode.ServerEditHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerHandler
{
	
	public static Container getPlayerStatsContainer(EntityPlayer player, int id, boolean editmode)
	{
		if(!editmode)
			switch(id)
			{
			case 0: return new ContainerCaptchaDeck();
			default: return null;
			}
		else
			switch(id)
			{
			case 0: return new ContainerEditmode(player);
			default: return null;
			}
	}
	
}
