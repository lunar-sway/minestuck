package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import com.mraof.minestuck.Minestuck;

import cpw.mods.fml.relauncher.Side;

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
		if(MinecraftServer.getServer() != null && MinecraftServer.getServer().isServerRunning())
			return;
		Minestuck.worldSeed = this.worldSeed;
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.SERVER);
	}

}
