package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;

import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.util.TitleHelper;
import com.mraof.minestuck.util.TitleStorage;

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
		/*if(player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).hasNoTags())
			player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
		player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).setInteger("Class", this.heroClass);
		player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).setInteger("Aspect", this.heroAspect);*/
		TitleStorage.onPacketRecieved(this);
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.SERVER);
	}
	
}
