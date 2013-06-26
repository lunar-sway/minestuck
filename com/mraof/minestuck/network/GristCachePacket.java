package com.mraof.minestuck.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.entity.item.EntityGrist;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import cpw.mods.fml.common.network.Player;

public class GristCachePacket extends MinestuckPacket 
{
	int[] values = new int[EntityGrist.gristTypes.length];

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
		System.out.println(dat.toByteArray().length);
		return dat.toByteArray();
	}
	
	@Override
	public MinestuckPacket consumePacket(byte[] data) 
	{
		System.out.println(data.length);
		ByteArrayDataInput dat = ByteStreams.newDataInput(data);
		for(int typeInt = 0; typeInt < values.length; typeInt++)
			values[typeInt] = dat.readInt();
		return this;
	}

	@Override
	public void execute(INetworkManager network, MinestuckPacketHandler minestuckPacketHandler, Player player, String userName) 
	{
		EntityPlayer entityPlayer = (EntityPlayer)player;
		if(entityPlayer.getEntityData().getCompoundTag("Grist").getTags().size() == 0)
			entityPlayer.getEntityData().setCompoundTag("Grist", new NBTTagCompound("Grist"));
		for(int typeInt = 0; typeInt < values.length; typeInt++)
			entityPlayer.getEntityData().getCompoundTag("Grist").setInteger(EntityGrist.gristTypes[typeInt], values[typeInt]);
	}


}
