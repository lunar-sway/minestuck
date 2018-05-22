package com.mraof.minestuck.network;

import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;
import com.mraof.minestuck.util.MinestuckPlayerData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;
import java.util.Map;

public class GristCachePacket extends MinestuckPacket
{
	public GristSet values;
	public boolean targetGrist;

	@Override
	public MinestuckPacket generatePacket(Object... dat)
	{
		GristSet gristSet = (GristSet) dat[0];
		data.writeInt(gristSet.gristTypes.size());
		for (Map.Entry<GristType, Integer> entry : gristSet.getMap().entrySet())
		{
			data.writeInt(entry.getKey().getId());
			data.writeInt(entry.getValue());
		}
		data.writeBoolean((Boolean) dat[1]);
		return this;
	}

	@Override
	public MinestuckPacket consumePacket(ByteBuf data)
	{
		values = new GristSet();
		int length = data.readInt();
		for (int i = 0; i < length; i++)
		{
			values.setGrist(GristType.REGISTRY.getValue(data.readInt()), data.readInt());
		}
		targetGrist = data.readBoolean();
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		MinestuckPlayerData.onPacketRecived(this);
	}

	@Override
	public EnumSet<Side> getSenderSide()
	{
		return EnumSet.of(Side.SERVER);
	}

}
