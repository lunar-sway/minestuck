package com.mraof.minestuck.network;

import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.NetHandler;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.entity.item.EntityGrist;

import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.common.network.Player;

public class GristPacket extends MinestuckPacket 
{
	public int typeInt;
	public int gristTotal;
	public GristPacket() 
	{
		super(Type.GRIST);
	}

	@Override
	public byte[] generatePacket(Object... data) 
	{
		ByteArrayDataOutput dat = ByteStreams.newDataOutput();
		dat.writeInt((int) data[0]);
		dat.writeInt((int) data[1]);
		return dat.toByteArray();
	}

	@Override
	public MinestuckPacket consumePacket(byte[] data) 
	{
		ByteArrayDataInput dat = ByteStreams.newDataInput(data);
		typeInt = dat.readInt();
		gristTotal = dat.readInt();
		return this;
	}

	@Override
	public void execute(INetworkManager network, MinestuckPacketHandler handler, Player player, String userName)
	{
		if(this.typeInt == -1)return;
		EntityPlayer entityPlayer = (EntityPlayer)player;
		if(entityPlayer.getEntityData().getCompoundTag("Grist").getTags().size() == 0)
			entityPlayer.getEntityData().setCompoundTag("Grist", new NBTTagCompound("Grist"));
		entityPlayer.getEntityData().getCompoundTag("Grist").setInteger(EntityGrist.gristTypes[this.typeInt], this.gristTotal);
//		((WorldClient) entityPlayer.worldObj).removeEntityFromWorld(this.gristEntityId);
	}
	
}
