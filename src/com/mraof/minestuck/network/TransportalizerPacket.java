package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.relauncher.Side;

import com.mraof.minestuck.tileentity.TileEntityTransportalizer;
import com.mraof.minestuck.util.Debug;

public class TransportalizerPacket extends MinestuckPacket
{
	int x;
	int y;
	int z;
	int dim;
	String destId;

	public TransportalizerPacket()
	{
		super(Type.TRANSPORTALIZER);
	}

	@Override
	public MinestuckPacket generatePacket(Object... dat)
	{
		data.writeInt((Integer) dat[0]);
		data.writeInt((Integer) dat[1]);
		data.writeInt((Integer) dat[2]);
		if(dat.length > 0)
			data.writeBytes(((String) dat[3]).getBytes());
		return this;
	}

	@Override
	public MinestuckPacket consumePacket(ByteBuf data)
	{
		x = data.readInt();
		y = data.readInt();
		z = data.readInt();
		byte[] destBytes = new byte[4];
		//data.getBytes(0, destBytes, 0, 4);
		for(int i = 0; i < 4; i++)
			destBytes[i] = data.readByte();
		Debug.printf("%d, %d, %d, %d", destBytes[0], destBytes[1], destBytes[2], destBytes[3]);
		destId = new String(destBytes);
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		TileEntityTransportalizer te = (TileEntityTransportalizer) player.worldObj.getTileEntity(new BlockPos(x, y, z));
		if(te != null)
		{
			te.setDestId(destId);
		}
	}

	@Override
	public EnumSet<Side> getSenderSide()
	{
		return EnumSet.of(Side.CLIENT);
	}
}
