package com.mraof.minestuck.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.tileentity.TileEntityComputer;

import cpw.mods.fml.common.network.Player;

public class SburbGiveItemsPacket extends MinestuckPacket {

	public String connectedTo;
	public int xCoord;
	public int yCoord;
	public int zCoord;
	public int gristTotal;
	public SburbGiveItemsPacket() 
	{
		super(Type.SBURB_GIVE);
	}

	@Override
	public byte[] generatePacket(Object... data) 
	{
		ByteArrayDataOutput dat = ByteStreams.newDataOutput();
		dat.writeInt((Integer)data[0]);
		dat.writeInt((Integer)data[1]);
		dat.writeInt((Integer)data[2]);
		dat.writeChars((String) data[3]);
		return dat.toByteArray();
	}

	@Override
	public MinestuckPacket consumePacket(byte[] data) 
	{
		ByteArrayDataInput dat = ByteStreams.newDataInput(data);
		
		xCoord = dat.readInt();
		yCoord = dat.readInt();
		zCoord = dat.readInt();
		connectedTo = dat.readLine();
		
		return this;
	}

	@Override
	public void execute(INetworkManager network, MinestuckPacketHandler handler, Player player, String userName)
	{
		TileEntityComputer te = (TileEntityComputer)Minecraft.getMinecraft().theWorld.getBlockTileEntity(xCoord,yCoord,zCoord);
				
		if (te == null) {
			System.out.println("[MINESTUCK] Packet recieved, but TE isn't there!");
			return;
		}
		
		Container items = ((EntityPlayer)player).inventoryContainer;
		ItemStack[] newItems = new ItemStack[5];
		for (int i = 0;i < 4;i++) {
			newItems[i] = new ItemStack(Minestuck.blockMachine.blockID,1,i);
		}
		ItemStack card = new ItemStack(Minestuck.punchedCard.itemID,1,0);
		card.setTagCompound(new NBTTagCompound());
		card.getTagCompound().setInteger("contentID",Minestuck.cruxiteArtifact.itemID);
		card.getTagCompound().setInteger("contentMeta",0);
		newItems[4] = card;
		items.putStacksInSlots(newItems);
		te.givenItems = true;
		
		System.out.println("[MINESTUCK] Packet recieved. Items given!");
	}

}
