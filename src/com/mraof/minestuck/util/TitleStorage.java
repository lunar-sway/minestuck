package com.mraof.minestuck.util;

import com.mraof.minestuck.network.TitlePacket;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TitleStorage
{
	@SideOnly(Side.CLIENT)
	public static Title title;

	public static void onPacketRecieved(TitlePacket packet)
	{
		title = new Title(TitleHelper.getClassFromInt(packet.heroClass), TitleHelper.getAspectFromInt(packet.heroAspect));
	}
}
