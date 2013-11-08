package com.mraof.minestuck.network.skaianet;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class SburbConnection {
	
	
//	@SideOnly(Side.SERVER)
	ComputerData client;
	String clientName = "";
//	@SideOnly(Side.SERVER)
	ComputerData server;
	String serverName = "";
	boolean isActive;
	boolean isMain;
	boolean enteredGame;
	boolean canSplit;
	int clientHomeLand;
	/**
	 * 0-3 = the machines
	 * 4 = the card
	 */
	boolean[] givenItemList = new boolean[5];
	
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
	
	/**
	 * Beware of changed use!
	 * @return Now returns an boolean array containing if it have given certain items or not.
	 */
	public boolean[] givenItems(){
		return givenItemList;
	}
	
	public byte[] getBytes() {
		ByteArrayDataOutput data = ByteStreams.newDataOutput();
		
		data.writeBoolean(isMain);
		if(isMain){
			data.writeBoolean(isActive);
			data.writeBoolean(enteredGame);
			
			for(boolean b : givenItemList)
				data.writeBoolean(b);
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
			byte[] array = new byte[givenItemList.length];
			for(int i = 0; i < givenItemList.length; i++)
				array[i] = (byte) (givenItemList[i]?1:0);
			nbt.setByteArray("givenItems", array);
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
			if(nbt.hasKey("givenItems")) {
				byte[] array = nbt.getByteArray("givenItems");
				for(int i = 0; i < array.length; i++)
					givenItemList[i] = array[i] != 0;
			} else for(int i = 0; i < 4; i++)
					givenItemList[i] = true;
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
	
	@Override
		public boolean equals(Object obj) {
		if(obj instanceof SburbConnection){
			SburbConnection c = (SburbConnection)obj;
			return this.getClientName() == c.getClientName() && this.getServerName() == c.getServerName();
		}
		return false;
	}
	
}
