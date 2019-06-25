package com.mraof.minestuck.inventory;

import com.mraof.minestuck.inventory.captchalogue.ContainerCaptchaDeck;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ContainerHandler
{
	
	public static int windowIdStart = 200;
	@OnlyIn(Dist.CLIENT)
	public static int clientWindowIdStart;
	
	public static Container getPlayerStatsContainer(EntityPlayer player, int id, boolean editmode)
	{
		if(!editmode)
			switch(id)
			{
			case 0: return new ContainerCaptchaDeck(player);
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
