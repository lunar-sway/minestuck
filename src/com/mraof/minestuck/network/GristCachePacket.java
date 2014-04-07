package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;

import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.GristType;

import cpw.mods.fml.relauncher.Side;

public class GristCachePacket extends MinestuckPacket 
{
	public int[] values = new int[GristType.allGrists];
	public boolean targetGrist;
	
	public GristCachePacket() 
	{
		super(Type.GRISTCACHE);
	}

	@Override
	public MinestuckPacket generatePacket(Object... dat) 
	{
		for(int i : (int[]) dat[0])
			data.writeInt(i);
		data.writeBoolean((Boolean)dat[1]);
		return this;
	}
	
	@Override
	public MinestuckPacket consumePacket(ByteBuf data) 
	{
		for(int typeInt = 0; typeInt < values.length; typeInt++)
			values[typeInt] = data.readInt();
		targetGrist = data.readBoolean();
		return this;
	}

	@Override
	public void execute(EntityPlayer player) {
		MinestuckPlayerData.onPacketRecived(this);
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.SERVER);
	}
	
}
