package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;

import com.mraof.minestuck.inventory.ContainerCrockerMachine;
import com.mraof.minestuck.inventory.ContainerSburbMachine;
import com.mraof.minestuck.tileentity.TileEntityAlchemiter;
import com.mraof.minestuck.tileentity.TileEntityMachine;
import com.mraof.minestuck.util.Debug;

public class AlchemiterPacket extends MinestuckPacket
{
	
	public BlockPos tePos;
	public int quantity;
	
	@Override
	public MinestuckPacket generatePacket(Object... dat) 
	{
		TileEntity te=((TileEntity)dat[0]);
		data.writeInt(te.getPos().getX());
		data.writeInt(te.getPos().getY());
		data.writeInt(te.getPos().getZ());
		data.writeInt((int)dat[1]);
		
		return this;
	}

	@Override
	public MinestuckPacket consumePacket(ByteBuf data) 
	{
		tePos=new BlockPos(data.readInt(),data.readInt(), data.readInt());
		
		quantity=data.readInt();
		
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		TileEntity te;
		te = player.getEntityWorld().getTileEntity(tePos);
		if(te instanceof TileEntityAlchemiter) {
			((TileEntityAlchemiter)te).processContents(quantity);
		}

	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.CLIENT);
	}

}
