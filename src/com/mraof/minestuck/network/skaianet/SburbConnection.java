package com.mraof.minestuck.network.skaianet;

import com.mraof.minestuck.editmode.DeployList;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;
import com.mraof.minestuck.world.lands.LandInfoContainer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.util.Constants;

import java.util.Arrays;

public class SburbConnection
{
	final SkaianetHandler handler;
	
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
	
	boolean isActive;
	private boolean isMain;
	boolean hasEntered;
	boolean canSplit;
	LandInfoContainer clientHomeLand;
	int artifactType;
	/**
	 * If the client will have frog breeding as quest, the array will be extended and the new positions will hold the gear.
	 */
	boolean[] givenItemList = new boolean[DeployList.getEntryCount()];
	ListNBT unregisteredItems = new ListNBT();
	
	//Only used by the edit handler
	public int centerX, centerZ;	//TODO No longer needed as it is either computer pos or the land dim spawn location. Should be functions instead
	public ListNBT inventory;	//TODO Should not be public
	
	//Non-saved variables used by the edit handler
	public double posX, posZ;
	public boolean useCoordinates;
	
	SburbConnection(SkaianetHandler handler)
	{
		this.handler = handler;
		this.canSplit = true;
		this.isActive = true;
	}
	
	public PlayerIdentifier getClientIdentifier()
	{
		if(clientIdentifier == null)
			return client.getOwner();
		else return clientIdentifier;
	}
	
	public PlayerIdentifier getServerIdentifier()
	{
		if(serverIdentifier == null)
			return server.getOwner();
		else return serverIdentifier;
	}
	
	public ComputerData getClientData() {return client;}
	public ComputerData getServerData() {return server;}
	
	public boolean hasEntered()
	{
		return hasEntered;
	}
	public boolean isMain(){return isMain;}
	void setIsMain()
	{
		if(!isMain)
		{
			isMain = true;
		}
	}
	
	/**
	 * @return The land dimension assigned to the client player.
	 */
	public DimensionType getClientDimension()
	{
		return clientHomeLand == null ? null : clientHomeLand.getDimensionType();
	}
	public boolean[] givenItems(){return Arrays.copyOf(givenItemList, givenItemList.length);}	//TODO Add way of setting given items that also calls skaianetHandler.markDirty()
	
	/**
	 * Writes the connection info needed client-side to a network buffer. Must match with {@link ReducedConnection#read}.
	 */
	public void toBuffer(PacketBuffer buffer)
	{
		buffer.writeBoolean(isMain);
		if(isMain){
			buffer.writeBoolean(isActive);
			buffer.writeBoolean(hasEntered);
		}
		buffer.writeInt(getClientIdentifier().getId());
		buffer.writeString(getClientIdentifier().getUsername(), 16);
		buffer.writeInt(getServerIdentifier().getId());
		buffer.writeString(getServerIdentifier().getUsername(), 16);
	}

	CompoundNBT write()
	{
		CompoundNBT nbt = new CompoundNBT();
		nbt.putBoolean("IsMain", isMain);
		if(inventory != null)
			nbt.put("Inventory", inventory);
		if(isMain)
		{
			nbt.putBoolean("IsActive", isActive);
			nbt.putBoolean("HasEntered", hasEntered);
			nbt.putBoolean("CanSplit", canSplit);
			ListNBT list = unregisteredItems.copy();
			String[] deployNames = DeployList.getNameList();
			for(int i = 0; i < givenItemList.length; i++)
			{
				if(givenItemList[i])
					list.add(new StringNBT(deployNames[i]));
			}
			
			nbt.put("GivenItems", list);
			if(clientHomeLand != null)
			{
				nbt.put("ClientLand", clientHomeLand.write(new CompoundNBT()));
			}
		}
		if(isActive)
		{
			nbt.put("Client", client.write(new CompoundNBT()));
			nbt.put("Server", server.write(new CompoundNBT()));
		}
		else
		{
			getClientIdentifier().saveToNBT(nbt, "Client");
			getServerIdentifier().saveToNBT(nbt, "Server");
		}
		nbt.putInt("Artifact", artifactType);
		return nbt;
	}
	
	static SburbConnection read(CompoundNBT nbt, SkaianetHandler handler)
	{
		SburbConnection c = new SburbConnection(handler);
		c.isMain = nbt.getBoolean("IsMain");
		if(nbt.contains("Inventory", Constants.NBT.TAG_LIST))
			c.inventory = nbt.getList("Inventory", Constants.NBT.TAG_COMPOUND);
		if(c.isMain)
		{
			c.isActive = nbt.getBoolean("IsActive");
			c.hasEntered = nbt.getBoolean("HasEntered");
			
			if(nbt.contains("CanSplit", Constants.NBT.TAG_ANY_NUMERIC))
				c.canSplit = nbt.getBoolean("CanSplit");
			ListNBT list = nbt.getList("GivenItems", Constants.NBT.TAG_STRING);
			for(int i = 0; i < list.size(); i++)
			{
				String name = list.getString(i);
				int ordinal = DeployList.getOrdinal(name);
				if(ordinal == -1)
					c.unregisteredItems.add(new StringNBT(name));
				else c.givenItemList[ordinal] = true;
			}
		}
		if(c.isActive)
		{
			c.client = new ComputerData(nbt.getCompound("Client"));
			c.server = new ComputerData(nbt.getCompound("Server"));
		}
		else
		{
			c.clientIdentifier = IdentifierHandler.load(nbt, "Client");
			c.serverIdentifier = IdentifierHandler.load(nbt, "Server");
		}
		if(nbt.contains("ClientLand", Constants.NBT.TAG_COMPOUND))
		{
			c.clientHomeLand = LandInfoContainer.read(nbt.getCompound("ClientLand"), handler, c.getClientIdentifier());	//TODO add robustness in the case that the dimension type no longer exists?
		}
		c.artifactType = nbt.getInt("Artifact");
		
		return c;
	}
}