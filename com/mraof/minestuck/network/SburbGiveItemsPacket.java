package com.mraof.minestuck.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.util.Debug;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class SburbGiveItemsPacket extends MinestuckPacket {

	public String client;
	public int gristTotal;
	public SburbGiveItemsPacket() 
	{
		super(Type.SBURB_GIVE);
	}

	@Override
	public byte[] generatePacket(Object... data) {
		return data[0].toString().getBytes();
	}

	@Override
	public MinestuckPacket consumePacket(byte[] data) 
	{
		ByteArrayDataInput dat = ByteStreams.newDataInput(data);
		
		client = dat.readLine();
		
		return this;
	}

	@Override
	public void execute(INetworkManager network, MinestuckPacketHandler handler, Player player, String userName) {
		if(!((EntityPlayer)player).worldObj.isRemote){
			EntityPlayer entityPlayer = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(client);
			InventoryPlayer items = entityPlayer != null?entityPlayer.inventory:null;
			boolean canGive = items != null?SkaianetHandler.giveItems(client):false;
			if (canGive){
				
				for (int i = 0;i < 4;i++) {
					items.addItemStackToInventory(new ItemStack(Minestuck.blockMachine.blockID,1,i));
				}
				ItemStack card = new ItemStack(Minestuck.punchedCard.itemID,1,0);
				card.setTagCompound(new NBTTagCompound());
				card.getTagCompound().setInteger("contentID",Minestuck.cruxiteArtifact.itemID);
				card.getTagCompound().setInteger("contentMeta",0);
				items.addItemStackToInventory(card);
				
				Debug.print("Packet recieved. Items given!");
			}
		}
	}

}
