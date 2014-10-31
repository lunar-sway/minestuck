package com.mraof.minestuck.client.event;

import net.minecraftforge.client.event.TextureStitchEvent;

import com.mraof.minestuck.Minestuck;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MinestuckClientEventHandler 
{
	
	public static float renderTickTime;
	
	@SubscribeEvent
	public void postStitch(TextureStitchEvent.Post event)
	{
		Minestuck.fluidOil.setIcons(Minestuck.blockOil.getBlockTextureFromSide(0), Minestuck.blockOil.getBlockTextureFromSide(1));
		Minestuck.fluidBlood.setIcons(Minestuck.blockBlood.getBlockTextureFromSide(0), Minestuck.blockBlood.getBlockTextureFromSide(1));
	}
	
}
