package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

import com.mraof.minestuck.Minestuck;

public class MinestuckInfoPacket extends MinestuckPacket 
{
	
	long worldSeed;

	public MinestuckInfoPacket() {
		super(Type.INFO);
	}

	@Override
	public MinestuckPacket generatePacket(Object... dat) {
		data.writeLong(Minestuck.worldSeed);
		return this;
	}
	
	@Override
	public MinestuckPacket consumePacket(ByteBuf data) {
		worldSeed = data.readLong();
		return this;
	}

	@Override
	public void execute(EntityPlayer player) 
	{
		Minestuck.worldSeed = this.worldSeed;
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.SERVER);
	}

}
