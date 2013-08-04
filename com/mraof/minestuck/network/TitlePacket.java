package com.mraof.minestuck.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.entity.item.EntityGrist;

import cpw.mods.fml.common.network.Player;

public class TitlePacket extends MinestuckPacket 
{
	public int heroClass;
	public int heroAspect;
	public TitlePacket() 
	{
		super(Type.TITLE);
	}

	@Override
	public byte[] generatePacket(Object... data) 
	{
		ByteArrayDataOutput dat = ByteStreams.newDataOutput();
		dat.writeInt((Integer) data[0]);
		dat.writeInt((Integer) data[1]);
		return dat.toByteArray();
	}

	@Override
	public MinestuckPacket consumePacket(byte[] data) 
	{
		ByteArrayDataInput dat = ByteStreams.newDataInput(data);
		heroClass = dat.readInt();
		heroAspect = dat.readInt();
		return this;
	}

	@Override
	public void execute(INetworkManager network, MinestuckPacketHandler handler, Player player, String userName)
	{
		EntityPlayer entityPlayer = (EntityPlayer)player;
		
		if(entityPlayer.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getTags().size() == 0)
			entityPlayer.getEntityData().setCompoundTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
		entityPlayer.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).setInteger("Class", this.heroClass);
		entityPlayer.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).setInteger("Aspect", this.heroAspect);
	}
	
}
