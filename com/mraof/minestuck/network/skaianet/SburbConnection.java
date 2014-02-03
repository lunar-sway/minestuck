package com.mraof.minestuck.network.skaianet;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.editmode.DeployList;

public class SburbConnection {
	
	ComputerData client;
	String clientName = "";
	ComputerData server;
	String serverName = "";
	boolean isActive;
	boolean isMain;
	boolean enteredGame;
	boolean canSplit;
	int clientHomeLand;
	/**
	 * 0 = card
	 * 1+ = items in deploy list
	 * If the client will have frog breeding as quest, the array will be extended and the new positions will hold the gear.
	 */
	boolean[] givenItemList = new boolean[DeployList.getItemList().size()+1];
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
	
	public byte[] getBytes() {
		ByteArrayDataOutput data = ByteStreams.newDataOutput();
		
		data.writeBoolean(isMain);
		if(isMain){
			data.writeBoolean(isActive);
			data.writeBoolean(enteredGame);
		}
		data.write((getClientName()+"\n").getBytes());
		data.write((getServerName()+"\n").getBytes());
		
		return data.toByteArray();
	}

	NBTTagCompound write() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("isMain", isMain);
		if(inventory != null)
			nbt.setTag("inventory", inventory);
		if(isMain){
			nbt.setBoolean("isActive", isActive);
			nbt.setBoolean("enteredGame", enteredGame);
			nbt.setBoolean("canSplit", canSplit);
			nbt.setBoolean("givenCard", givenItemList[0]);
			NBTTagList list = (NBTTagList) unregisteredItems.copy();
			for(ItemStack stack : DeployList.getItemList()) {
				NBTTagCompound itemData = stack.writeToNBT(new NBTTagCompound());
				itemData.setBoolean("given", givenItemList[DeployList.getOrdinal(stack)+1]);
				list.appendTag(itemData);
			}
			
			nbt.setTag("givenItems", list);
			if(enteredGame){
				nbt.setInteger("clientLand", clientHomeLand);
				nbt.setInteger("centerX", centerX);
				nbt.setInteger("centerZ", centerZ);
			}
		}
		if(isActive){
			nbt.setCompoundTag("client", client.write());
			nbt.setCompoundTag("server", server.write());
		} else {
			nbt.setString("client", getClientName());
			nbt.setString("server", getServerName());
		}
		return nbt;
	}
	
	SburbConnection read(NBTTagCompound nbt) {
		isMain = nbt.getBoolean("isMain");
		if(nbt.hasKey("inventory"))
			inventory = nbt.getTagList("inventory");
		if(isMain){
			isActive = nbt.getBoolean("isActive");
			enteredGame = nbt.getBoolean("enteredGame");
			if(enteredGame){
				clientHomeLand = nbt.getInteger("clientLand");
				centerX = nbt.getInteger("centerX");
				centerZ = nbt.getInteger("centerZ");
			}
			if(nbt.hasKey("canSplit"))
				canSplit = nbt.getBoolean("canSplit");
			givenItemList[0] = nbt.getBoolean("givenCard");
			if(nbt.hasKey("givenItems")) {
				if(nbt.getTag("givenItems") instanceof NBTTagList) {
					NBTTagList list = nbt.getTagList("givenItems");
					for(int i = 0; i < list.tagCount(); i++) {
						NBTTagCompound itemTag = (NBTTagCompound) list.tagAt(i);
						int ordinal = DeployList.getOrdinal(ItemStack.loadItemStackFromNBT(itemTag));
						if(ordinal == -1)
							unregisteredItems.appendTag(itemTag);
						else givenItemList[ordinal+1] = itemTag.getBoolean("given");
					}
				} else {
					byte[] array = nbt.getByteArray("givenItems");
					givenItemList[0] = array[4] != 0;
					for(int i = 0; i < 4; i++)
						givenItemList[DeployList.getOrdinal(new ItemStack(Minestuck.blockMachine,1,i))+1] = array[i] != 0;
				}
			} else {
				givenItemList[0] = true;
				for(int i = 0; i < 4; i++)
					givenItemList[DeployList.getOrdinal(new ItemStack(Minestuck.blockMachine,1,i))+1] = true;
			}
		}
		if(isActive){
			client = new ComputerData().read(nbt.getCompoundTag("client"));
			server = new ComputerData().read(nbt.getCompoundTag("server"));
		} else {
			clientName = nbt.getString("client");
			serverName = nbt.getString("server");
		}
		return this;
	}
	
}
