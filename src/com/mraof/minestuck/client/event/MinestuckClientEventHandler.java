package com.mraof.minestuck.client.event;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

import com.mraof.minestuck.Minestuck;

@SideOnly(Side.CLIENT)
public class MinestuckClientEventHandler 
{
	
	public static float renderTickTime;
	
	@SubscribeEvent
	public void postStitch(TextureStitchEvent.Post event)
	{
//		Minestuck.fluidOil.setIcons(Minestuck.blockOil.getBlockTextureFromSide(0), Minestuck.blockOil.getBlockTextureFromSide(1));
//		Minestuck.fluidBlood.setIcons(Minestuck.blockBlood.getBlockTextureFromSide(0), Minestuck.blockBlood.getBlockTextureFromSide(1));
	}
	
}
