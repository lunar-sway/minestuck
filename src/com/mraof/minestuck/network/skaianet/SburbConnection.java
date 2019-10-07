package com.mraof.minestuck.network.skaianet;

import com.mraof.minestuck.editmode.DeployList;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.lands.LandInfoContainer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.dimension.DimensionType;

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
			handler.markDirty();
		}
	}
	
	/**
	 * @return The land dimension assigned to the client player.
	 */
	public DimensionType getClientDimension()
	{
		return clientHomeLand == null ? null : clientHomeLand.dimensionType;
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
			nbt.put("Client", client.write());
			nbt.put("Server", server.write());
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
		if(nbt.contains("Inventory"))
			c.inventory = nbt.getList("Inventory", 10);
		if(c.isMain)
		{
			c.isActive = nbt.getBoolean("IsActive");
			c.hasEntered = nbt.getBoolean("HasEntered");
			
			if(nbt.contains("CanSplit"))
				c.canSplit = nbt.getBoolean("CanSplit");
			ListNBT list = nbt.getList("GivenItems", 8);
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
			c.client = new ComputerData().read(nbt.getCompound("Client"));
			c.server = new ComputerData().read(nbt.getCompound("Server"));
		}
		else
		{
			c.clientIdentifier = IdentifierHandler.load(nbt, "Client");
			c.serverIdentifier = IdentifierHandler.load(nbt, "Server");
		}
		if(nbt.contains("ClientLand"))
		{
			c.clientHomeLand = LandInfoContainer.read(nbt.getCompound("ClientLand"), handler, c.getClientIdentifier());	//TODO add robustness in the case that the dimension type no longer exists?
			if(!MSDimensions.isLandDimension(c.getClientDimension()))
			{
				Debug.errorf("The connection between %s and %s had a home dimension %d that isn't a land dimension. For safety measures, the connection will be loaded as if the player had not yet entered.", c.getClientIdentifier().getUsername(), c.getServerIdentifier().getUsername(), c.getClientDimension());
				c.clientHomeLand = null;
				c.hasEntered = false;
			}
		}
		c.artifactType = nbt.getInt("Artifact");
		
		return c;
	}
}