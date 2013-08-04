package com.mraof.minestuck.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.inventory.ContainerMachine;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.tileentity.TileEntityMachine;
import com.mraof.minestuck.util.CombinationMode;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import cpw.mods.fml.common.network.Player;

public class GoButtonPacket extends MinestuckPacket {

	public boolean newMode;
	public int xCoord;
	public int yCoord;
	public int zCoord;
	public int gristTotal;
	public GoButtonPacket() 
	{
		super(Type.GOBUTTON);
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
			System.out.println("[MINESTUCK] Button pressed. Alchemiter going!");
			te.alcReady = newMode;
		}
	}

}
