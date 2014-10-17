package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;

import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.Title;
import com.mraof.minestuck.util.TitleHelper;

import cpw.mods.fml.relauncher.Side;

public class TitlePacket extends MinestuckPacket 
{
	public int heroClass;
	public int heroAspect;
	public TitlePacket() 
	{
		super(Type.TITLE);
	}

	@Override
	public MinestuckPacket generatePacket(Object... dat) 
	{
		data.writeInt(TitleHelper.getIntFromClass((EnumClass) dat[0]));
		data.writeInt(TitleHelper.getIntFromAspect((EnumAspect) dat[1]));
		
		return this;
	}

	@Override
	public MinestuckPacket consumePacket(ByteBuf data) 
	{
		heroClass = data.readInt();
		heroAspect = data.readInt();
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		MinestuckPlayerData.title = new Title(TitleHelper.getClassFromInt(heroClass), TitleHelper.getAspectFromInt(heroAspect));
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.SERVER);
	}
	
}
