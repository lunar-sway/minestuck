package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import com.mraof.minestuck.inventory.ContainerMachine;
import com.mraof.minestuck.tileentity.TileEntityMachine;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.GristType;

import cpw.mods.fml.relauncher.Side;

public class MachineStatePacket extends MinestuckPacket {

	public boolean newMode;
	public int xCoord;
	public int yCoord;
	public int zCoord;
	public int gristType = -1;
	public MachineStatePacket() 
	{
		super(Type.MACHINE_STATE);
	}

	@Override
	public MinestuckPacket generatePacket(Object... dat) 
	{
		if(dat[0] instanceof Boolean)
			data.writeBoolean((Boolean) dat[0]);
		else data.writeInt((Integer) dat[0]);
		
		return this;
	}

	@Override
	public MinestuckPacket consumePacket(ByteBuf data) 
	{
		if(data.readableBytes() == 1)
			newMode = data.readBoolean();
		else gristType = data.readInt();
		
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		TileEntityMachine te = ((ContainerMachine) ((EntityPlayerMP)player).openContainer).tileEntity;
		
		if (te == null) {
			Debug.print("Invalid TE!");
		}
		else
		{
			if(gristType == -1)
			{
				Debug.print("Button pressed. AND mode is " + newMode);
				te.mode = newMode;
			}
			else
			{
				te.selectedGrist = GristType.values()[gristType];
			}
		}
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.CLIENT);
	}

}
