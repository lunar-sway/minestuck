package com.mraof.minestuck.network.skaianet;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import com.mraof.minestuck.editmode.DeployList;
import com.mraof.minestuck.network.MinestuckPacket;

public class SburbConnection {
	
	ComputerData client;
	/**
	 * Name for the client player. Beware that this might be an empty string if connection.client isn't null
	 * It is recommended to use connection.getClientName() instead if possible
	 */
	String clientName = "";
	ComputerData server;
	/**
	 * Name for the server player. Beware that this might be an empty string if connection.server isn't null
	 * It is recommended to use connection.getServerName() instead if possible
	 */
	String serverName = "";
	boolean isActive;
	boolean isMain;
	boolean enteredGame;
	boolean canSplit;
	int clientHomeLand;
	int artifactType;
	/**
	 * If the client will have frog breeding as quest, the array will be extended and the new positions will hold the gear.
	 */
	boolean[] givenItemList = new boolean[DeployList.getItemList().size()];
	NBTTagList unregisteredItems = new NBTTagList();
	
	//Only used by the edit handler
	public int centerX, centerZ;
	public NBTTagList inventory;
	
	//Non-saved variables used by the edit handler
	public double posX, posZ;
	public boolean useCoordinates;
	
	SburbConnection(){
		this.canSplit = true;
		this.isActive = true;
	}
	
	public String getClientName(){
		if(clientName.isEmpty())
			return client.owner;
		else return clientName;
	}
	public String getServerName(){
		if(serverName.isEmpty())
			return server.owner;
		else return serverName;
	}
	
	public ComputerData getClientData() {return client;}
	public ComputerData getServerData() {return server;}
	public boolean enteredGame(){return enteredGame;}
	public boolean isMain(){return isMain;}
	public int getClientDimension() {return clientHomeLand;}
	public boolean[] givenItems(){return givenItemList;}
	
	public void writeBytes(ByteBuf data)
	{
		data.writeBoolean(isMain);
		if(isMain){
			data.writeBoolean(isActive);
			data.writeBoolean(enteredGame);
		}
		MinestuckPacket.writeString(data, getClientName()+"\n");
		MinestuckPacket.writeString(data, getServerName()+"\n");
	}

	NBTTagCompound write()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("isMain", isMain);
		if(inventory != null)
			nbt.setTag("inventory", inventory);
		if(isMain)
		{
			nbt.setBoolean("isActive", isActive);
			nbt.setBoolean("enteredGame", enteredGame);
			nbt.setBoolean("canSplit", canSplit);
			NBTTagList list = (NBTTagList) unregisteredItems.copy();
			for(ItemStack stack : DeployList.getItemList())
			{
				NBTTagCompound itemData = stack.writeToNBT(new NBTTagCompound());
				itemData.setBoolean("given", givenItemList[DeployList.getOrdinal(stack)]);
				list.appendTag(itemData);
			}
			
			nbt.setTag("givenItems", list);
			if(enteredGame)
			{
				nbt.setInteger("clientLand", clientHomeLand);
				nbt.setInteger("centerX", centerX);
				nbt.setInteger("centerZ", centerZ);
			}
		}
		if(isActive)
		{
			nbt.setTag("client", client.write());
			nbt.setTag("server", server.write());
		}
		else
		{
			nbt.setString("client", getClientName());
			nbt.setString("server", getServerName());
		}
		nbt.setInteger("artifact", artifactType);
		return nbt;
	}
	
	SburbConnection read(NBTTagCompound nbt)
	{
		isMain = nbt.getBoolean("isMain");
		if(nbt.hasKey("inventory"))
			inventory = (NBTTagList) nbt.getTag("inventory");
		if(isMain)
		{
			isActive = nbt.getBoolean("isActive");
			enteredGame = nbt.getBoolean("enteredGame");
			if(enteredGame)
			{
				clientHomeLand = nbt.getInteger("clientLand");
				centerX = nbt.getInteger("centerX");
				centerZ = nbt.getInteger("centerZ");
			}
			if(nbt.hasKey("canSplit"))
				canSplit = nbt.getBoolean("canSplit");
			NBTTagList list = (NBTTagList) nbt.getTagList("givenItems", 10);
			for(int i = 0; i < list.tagCount(); i++)
			{
				NBTTagCompound itemTag = list.getCompoundTagAt(i);
				int ordinal = DeployList.getOrdinal(ItemStack.loadItemStackFromNBT(itemTag));
				if(ordinal == -1)
					unregisteredItems.appendTag(itemTag);
				else givenItemList[ordinal] = itemTag.getBoolean("given");
			}
		}
		if(isActive)
		{
			client = new ComputerData().read(nbt.getCompoundTag("client"));
			server = new ComputerData().read(nbt.getCompoundTag("server"));
		}
		else
		{
			clientName = nbt.getString("client");
			serverName = nbt.getString("server");
		}
		artifactType = nbt.getInteger("artifact");
		
		return this;
	}
	
}
