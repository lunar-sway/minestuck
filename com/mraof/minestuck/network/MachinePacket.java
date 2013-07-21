package com.mraof.minestuck.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.alchemy.CombinationMode;
import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.tileentity.TileEntityMachine;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import cpw.mods.fml.common.network.Player;

public class MachinePacket extends MinestuckPacket {

	public boolean newMode;
	public int xCoord;
	public int yCoord;
	public int zCoord;
	public int gristTotal;
	public MachinePacket() 
	{
		super(Type.MACHINE);
	}

	@Override
	public byte[] generatePacket(Object... data) 
	{
		ByteArrayDataOutput dat = ByteStreams.newDataOutput();
		dat.writeInt((Integer)data[0]);
		dat.writeInt((Integer)data[1]);
		dat.writeInt((Integer)data[2]);
		dat.writeBoolean((Boolean) data[3]);
		return dat.toByteArray();
	}

	@Override
	public MinestuckPacket consumePacket(byte[] data) 
	{
		ByteArrayDataInput dat = ByteStreams.newDataInput(data);
		
		newMode = dat.readBoolean();
		xCoord = dat.readInt();
		yCoord = dat.readInt();
		zCoord = dat.readInt();
		
		TileEntityMachine te = (TileEntityMachine) Minecraft.getMinecraft().theWorld.getBlockTileEntity(xCoord, yCoord, zCoord);
		
		if (te == null) {
			System.out.println("Invalid TE!");
		} else {
			te.mode = newMode ? CombinationMode.AND : CombinationMode.OR;
		}
		
		return this;
	}

	@Override
	public void execute(INetworkManager network, MinestuckPacketHandler handler, Player player, String userName)
	{
		
	}

}
