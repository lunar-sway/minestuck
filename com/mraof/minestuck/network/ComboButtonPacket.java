package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import com.mraof.minestuck.inventory.ContainerMachine;
import com.mraof.minestuck.tileentity.TileEntityMachine;
import com.mraof.minestuck.util.Debug;

import cpw.mods.fml.relauncher.Side;

public class ComboButtonPacket extends MinestuckPacket {

	public boolean newMode;
	public int xCoord;
	public int yCoord;
	public int zCoord;
	public int gristTotal;
	public ComboButtonPacket() 
	{
		super(Type.COMBOBUTTON);
	}

	@Override
	public MinestuckPacket generatePacket(Object... dat) 
	{
		data.writeBoolean((Boolean) dat[0]);
		
		return this;
	}

	@Override
	public MinestuckPacket consumePacket(ByteBuf data) 
	{
		
		newMode = data.readBoolean();
		
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
				TileEntityMachine te = ((ContainerMachine) ((EntityPlayerMP)player).openContainer).tileEntity;
		
		if (te == null) {
			Debug.print("Invalid TE!");
		} else {
			Debug.print("Button pressed. AND mode is " + newMode);
			te.mode = newMode;
		}
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.CLIENT);
	}

}
