package com.mraof.minestuck.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.network.MinestuckPacket.Type;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import cpw.mods.fml.common.network.Player;

public class MachinePacket extends MinestuckPacket {

	public boolean newMode;
	public int xCoord;
	public int yCoord;
	public int zCoord;
	public int gristTotal;
	public MachinePacket() 
	{
		super(Type.MACHINE);
	}

	@Override
	public byte[] generatePacket(Object... data) 
	{
		ByteArrayDataOutput dat = ByteStreams.newDataOutput();
		dat.writeInt((Integer)data[0]);
		dat.writeInt((Integer)data[1]);
		dat.writeInt((Integer)data[2]);
		dat.writeBoolean((Boolean) data[3]);
		return dat.toByteArray();
	}

	@Override
	public MinestuckPacket consumePacket(byte[] data) 
	{
		ByteArrayDataInput dat = ByteStreams.newDataInput(data);
		
		newMode = dat.readBoolean();
		xCoord = dat.readInt();
		yCoord = dat.readInt();
		zCoord = dat.readInt();
		return this;
	}

	@Override
	public void execute(INetworkManager network, MinestuckPacketHandler handler, Player player, String userName)
	{
		
	}

}
