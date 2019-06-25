package com.mraof.minestuck.network.skaianet;

import com.mraof.minestuck.editmode.DeployList;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SburbConnection
{
	
	ComputerData client;
	/**
	 * Identifier for the client player. Beware that this might be null if connection.client isn't null
	 * It is recommended to use connection.getClientName() instead if possible
	 */
	PlayerIdentifier clientIdentifier;
	ComputerData server;
	/**
	 * Identifier for the server player. Beware that this might be null if connection.server isn't null
	 * It is recommended to use connection.getServerName() instead if possible
	 */
	PlayerIdentifier serverIdentifier;
	
	/**
	 * Display name used by computer guis
	 */
	@OnlyIn(Dist.CLIENT)
	String clientName, serverName;
	/**
	 * Id for identifying players clientside
	 */
	@OnlyIn(Dist.CLIENT)
	int clientId, serverId;
	
	boolean isActive;
	boolean isMain;
	boolean enteredGame;
	boolean canSplit;
	DimensionType clientHomeLand;
	int artifactType;
	/**
	 * If the client will have frog breeding as quest, the array will be extended and the new positions will hold the gear.
	 */
	boolean[] givenItemList = new boolean[DeployList.getEntryCount()];
	NBTTagList unregisteredItems = new NBTTagList();
	
	//Only used by the edit handler
	public int centerX, centerZ;
	public NBTTagList inventory;
	
	//Non-saved variables used by the edit handler
	public double posX, posZ;
	public boolean useCoordinates;
	
	SburbConnection()
	{
		this.canSplit = true;
		this.isActive = true;
	}
	
	public PlayerIdentifier getClientIdentifier()
	{
		if(clientIdentifier == null)
			return client.owner;
		else return clientIdentifier;
	}
	
	public PlayerIdentifier getServerIdentifier()
	{
		if(serverIdentifier == null)
			return server.owner;
		else return serverIdentifier;
	}
	
	public ComputerData getClientData() {return client;}
	public ComputerData getServerData() {return server;}
	public boolean enteredGame(){return enteredGame;}
	public boolean isMain(){return isMain;}
	public DimensionType getClientDimension() {return clientHomeLand;}
	public boolean[] givenItems(){return givenItemList;}
	@OnlyIn(Dist.CLIENT)
	public String getClientDisplayName() {return clientName;}
	@OnlyIn(Dist.CLIENT)
	public String getServerDisplayName() {return serverName;}
	@OnlyIn(Dist.CLIENT)
	public int getClientId() {return clientId;}
	@OnlyIn(Dist.CLIENT)
	public int getServerId() {return serverId;}
	
	public void toBuffer(PacketBuffer buffer)
	{
		buffer.writeBoolean(isMain);
		if(isMain){
			buffer.writeBoolean(isActive);
			buffer.writeBoolean(enteredGame);
		}
		buffer.writeInt(getClientIdentifier().getId());
		buffer.writeString(getClientIdentifier().getUsername(), 16);
		buffer.writeInt(getServerIdentifier().getId());
		buffer.writeString(getServerIdentifier().getUsername(), 16);
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
			NBTTagList list = unregisteredItems.copy();
			String[] deployNames = DeployList.getNameList();
			for(int i = 0; i < givenItemList.length; i++)
			{
				if(givenItemList[i])
					list.add(new NBTTagString(deployNames[i]));
			}
			
			nbt.setTag("givenItems", list);
			if(enteredGame)
			{
				nbt.setString("clientLand", clientHomeLand.getRegistryName().toString());
			}
		}
		if(isActive)
		{
			nbt.setTag("client", client.write());
			nbt.setTag("server", server.write());
		}
		else
		{
			getClientIdentifier().saveToNBT(nbt, "client");
			getServerIdentifier().saveToNBT(nbt, "server");
		}
		nbt.setInt("artifact", artifactType);
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
			
			if(nbt.hasKey("canSplit"))
				canSplit = nbt.getBoolean("canSplit");
			NBTTagList list = nbt.getList("givenItems", 8);
			for(int i = 0; i < list.size(); i++)
			{
				String name = list.getString(i);
				int ordinal = DeployList.getOrdinal(name);
				if(ordinal == -1)
					unregisteredItems.add(new NBTTagString(name));
				else givenItemList[ordinal] = true;
			}
		}
		if(isActive)
		{
			client = new ComputerData().read(nbt.getCompound("client"));
			server = new ComputerData().read(nbt.getCompound("server"));
		}
		else
		{
			clientIdentifier = IdentifierHandler.load(nbt, "client");
			serverIdentifier = IdentifierHandler.load(nbt, "server");
		}
		if(enteredGame)
		{
			clientHomeLand = DimensionType.byName(new ResourceLocation(nbt.getString("clientLand")));	//TODO add robustness in the case that the dimension type no longer exists?
			if(!MinestuckDimensionHandler.isLandDimension(clientHomeLand))
			{
				Debug.errorf("The connection between %s and %s had a home dimension %d that isn't a land dimension. For safety measures, the connection will be loaded as if the player had not yet entered.", getClientIdentifier().getUsername(), getServerIdentifier().getUsername(), clientHomeLand);
				enteredGame = false;
			}
		}
		artifactType = nbt.getInt("artifact");
		
		return this;
	}
	
}