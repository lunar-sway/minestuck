package com.mraof.minestuck.network;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.util.TitleHelper;

import cpw.mods.fml.common.network.Player;
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
	public byte[] generatePacket(Object... data) 
	{
		ByteArrayDataOutput dat = ByteStreams.newDataOutput();
		dat.writeInt(TitleHelper.getIntFromClass((EnumClass) data[0]));
		dat.writeInt(TitleHelper.getIntFromAspect((EnumAspect) data[1]));
		return dat.toByteArray();
	}

	@Override
	public MinestuckPacket consumePacket(byte[] data, Side side) 
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

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.SERVER);
	}
	
}
