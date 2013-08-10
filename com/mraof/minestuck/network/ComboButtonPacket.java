package com.mraof.minestuck.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.inventory.ContainerMachine;
import com.mraof.minestuck.tileentity.TileEntityMachine;

import cpw.mods.fml.common.network.Player;

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
	public byte[] generatePacket(Object... data) 
	{
		ByteArrayDataOutput dat = ByteStreams.newDataOutput();
		dat.writeBoolean((Boolean) data[0]);
		return dat.toByteArray();
	}

	@Override
	public MinestuckPacket consumePacket(byte[] data) 
	{
		ByteArrayDataInput dat = ByteStreams.newDataInput(data);
		
		newMode = dat.readBoolean();
		
		return this;
	}

	@Override
	public void execute(INetworkManager network, MinestuckPacketHandler handler, Player player, String userName)
	{
				TileEntityMachine te = ((ContainerMachine) ((EntityPlayerMP)player).openContainer).tileEntity;
		
		if (te == null) {
			System.out.println("[MINESTUCK] Invalid TE!");
		} else {
			System.out.println("[MINESTUCK] Button pressed. AND mode is " + newMode);
			te.mode = newMode;
		}
	}

}
