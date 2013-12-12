package com.mraof.minestuck.network;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.inventory.ContainerMachine;
import com.mraof.minestuck.tileentity.TileEntityMachine;
import com.mraof.minestuck.util.Debug;

import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class GoButtonPacket extends MinestuckPacket {

	public boolean newMode;
	public boolean overrideStop;
	public GoButtonPacket() 
	{
		super(Type.GOBUTTON);
	}

	@Override
	public byte[] generatePacket(Object... data) 
	{
		ByteArrayDataOutput dat = ByteStreams.newDataOutput();
		dat.writeBoolean((Boolean) data[0]);
		dat.writeBoolean((Boolean) data[1]);
		return dat.toByteArray();
	}

	@Override
	public MinestuckPacket consumePacket(byte[] data, Side side) 
	{
		ByteArrayDataInput dat = ByteStreams.newDataInput(data);
		
		newMode = dat.readBoolean();
		overrideStop = dat.readBoolean();
		
		return this;
	}

	@Override
	public void execute(INetworkManager network, MinestuckPacketHandler handler, Player player, String userName)
	{
				TileEntityMachine te = ((ContainerMachine) ((EntityPlayerMP)player).openContainer).tileEntity;
		
		if (te == null) {
			Debug.print("Invalid TE!");
		} else {
			Debug.print("Button pressed. Alchemiter going!");
			te.ready = newMode;
			te.overrideStop = overrideStop;
		}
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.CLIENT);
	}

}
