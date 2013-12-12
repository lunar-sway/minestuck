package com.mraof.minestuck.network;

import java.util.EnumSet;

import net.minecraft.network.INetworkManager;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.util.GristStorage;
import com.mraof.minestuck.util.GristType;

import cpw.mods.fml.common.network.Player;
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
	public byte[] generatePacket(Object... data) 
	{
		ByteArrayDataOutput dat = ByteStreams.newDataOutput();
		values = (int[]) data[0];
		for(int currentValue : values)
			dat.writeInt(currentValue);
		dat.writeBoolean((Boolean)data[1]);
		return dat.toByteArray();
	}
	
	@Override
	public MinestuckPacket consumePacket(byte[] data, Side side) 
	{
		ByteArrayDataInput dat = ByteStreams.newDataInput(data);
		for(int typeInt = 0; typeInt < values.length; typeInt++)
			values[typeInt] = dat.readInt();
		targetGrist = dat.readBoolean();
		return this;
	}

	@Override
	public void execute(INetworkManager network, MinestuckPacketHandler minestuckPacketHandler, Player player, String userName) {
		GristStorage.onPacketRecived(this);
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.SERVER);
	}
	
}
