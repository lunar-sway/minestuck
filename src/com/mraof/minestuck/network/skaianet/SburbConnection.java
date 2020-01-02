package com.mraof.minestuck.network.skaianet;

import com.mojang.datafixers.Dynamic;
import com.mraof.minestuck.editmode.DeployList;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;
import com.mraof.minestuck.util.Title;
import com.mraof.minestuck.world.lands.LandInfo;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.util.Constants;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class SburbConnection
{
	final SkaianetHandler handler;
	
	/**
	 * Identifier for the client player. Beware that this might be null if connection.client isn't null
	 * It is recommended to use connection.getClientName() instead if possible
	 */
	final PlayerIdentifier clientIdentifier;
	/**
	 * Identifier for the server player. Beware that this might be null if connection.server isn't null
	 * It is recommended to use connection.getServerName() instead if possible
	 */
	PlayerIdentifier serverIdentifier;
	GlobalPos clientComputer;	//TODO Abstraction that works with multiple representations of computers
	GlobalPos serverComputer;
	
	private boolean isActive;
	private boolean isMain;
	boolean hasEntered;
	boolean canSplit;
	LandInfo clientHomeLand;
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
	
	SburbConnection(PlayerIdentifier client, PlayerIdentifier server, SkaianetHandler handler)
	{
		clientIdentifier = client;
		serverIdentifier = server;
		this.handler = handler;
		this.canSplit = true;
	}
	
	SburbConnection(CompoundNBT nbt, SkaianetHandler handler)
	{
		this.handler = handler;
		isMain = nbt.getBoolean("IsMain");
		if(nbt.contains("Inventory", Constants.NBT.TAG_LIST))
			inventory = nbt.getList("Inventory", Constants.NBT.TAG_COMPOUND);
		if(isMain)
		{
			isActive = nbt.getBoolean("IsActive");
			hasEntered = nbt.getBoolean("HasEntered");
			
			if(nbt.contains("CanSplit", Constants.NBT.TAG_ANY_NUMERIC))
				canSplit = nbt.getBoolean("CanSplit");
			ListNBT list = nbt.getList("GivenItems", Constants.NBT.TAG_STRING);
			for(int i = 0; i < list.size(); i++)
			{
				String name = list.getString(i);
				int ordinal = DeployList.getOrdinal(name);
				if(ordinal == -1)
					unregisteredItems.add(new StringNBT(name));
				else givenItemList[ordinal] = true;
			}
		}
		clientIdentifier = IdentifierHandler.load(nbt, "client");
		serverIdentifier = IdentifierHandler.load(nbt, "server");
		if(isActive)
		{
			try
			{
				clientComputer = GlobalPos.deserialize(new Dynamic<>(NBTDynamicOps.INSTANCE, nbt.getCompound("client_pos")));
				serverComputer = GlobalPos.deserialize(new Dynamic<>(NBTDynamicOps.INSTANCE, nbt.getCompound("server_pos")));
			} catch(Exception e)
			{
				Debug.logger.error("Unable to read computer position for sburb connection between "+ clientIdentifier.getUsername()+" and "+serverIdentifier.getUsername()+", setting connection to be inactive. Cause: ", e);
				isActive = false;
			}
		}
		if(nbt.contains("ClientLand", Constants.NBT.TAG_COMPOUND))
		{
			clientHomeLand = LandInfo.read(nbt.getCompound("ClientLand"), handler, getClientIdentifier());	//TODO add robustness in the case that the dimension type no longer exists?
		}
		artifactType = nbt.getInt("artifact");
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
		
		getClientIdentifier().saveToNBT(nbt, "client");
		getServerIdentifier().saveToNBT(nbt, "server");
		
		if(isActive)
		{
			nbt.put("client_pos", clientComputer.serialize(NBTDynamicOps.INSTANCE));
			nbt.put("server_pos", serverComputer.serialize(NBTDynamicOps.INSTANCE));
		}
		
		nbt.putInt("artifact", artifactType);
		return nbt;
	}
	
	void setActive(GlobalPos client, GlobalPos server)
	{
		clientComputer = client;
		serverComputer = server;
		isActive = true;
	}
	
	void close()
	{
		clientComputer = null;
		serverComputer = null;
		isActive = false;
	}
	
	public PlayerIdentifier getClientIdentifier()
	{
		return clientIdentifier;
	}
	
	public PlayerIdentifier getServerIdentifier()
	{
		return serverIdentifier;
	}
	
	public GlobalPos getClientComputer()
	{
		return clientComputer;
	}
	
	public boolean isClient(ComputerTileEntity computer)
	{
		return isActive && getClientIdentifier().equals(computer.owner) && clientComputer.getDimension() == computer.getWorld().getDimension().getType() && clientComputer.getPos().equals(computer.getPos());
	}
	
	public boolean isServer(ComputerTileEntity computer)
	{
		return isActive && getServerIdentifier().equals(computer.owner) && serverComputer.getDimension() == computer.getWorld().getDimension().getType() && serverComputer.getPos().equals(computer.getPos());
	}
	
	public boolean hasEntered()
	{
		return hasEntered;
	}
	public boolean isMain(){return isMain;}
	public boolean isActive()
	{
		return isActive;
	}
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
	public boolean[] givenItems(){return Arrays.copyOf(givenItemList, givenItemList.length);}
	
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
	
	CompoundNBT createDataTag(Set<PlayerIdentifier> playerSet, Map<PlayerIdentifier, PredefineData> predefinedPlayers)
	{
		if(isMain())
			playerSet.add(getClientIdentifier());
		CompoundNBT connectionTag = new CompoundNBT();
		connectionTag.putString("client", getClientIdentifier().getUsername());
		connectionTag.putString("clientId", getClientIdentifier().getString());
		if(!getServerIdentifier().equals(IdentifierHandler.nullIdentifier))
			connectionTag.putString("server", getServerIdentifier().getUsername());
		connectionTag.putBoolean("isMain", isMain());
		connectionTag.putBoolean("isActive", isActive());
		if(isMain())
		{
			if(clientHomeLand != null)
			{
				connectionTag.putString("clientDim", getClientDimension().getRegistryName().toString());
				connectionTag.putString("aspect1", clientHomeLand.landName1());
				connectionTag.putString("aspect2", clientHomeLand.landName2());
				Title title = PlayerSavedData.getData(getClientIdentifier(), handler.mcServer).getTitle();
				connectionTag.putByte("class", title == null ? -1 : (byte) title.getHeroClass().ordinal());
				connectionTag.putByte("aspect", title == null ? -1 : (byte) title.getHeroAspect().ordinal());
			} else if(predefinedPlayers.containsKey(getClientIdentifier()))
			{
				PredefineData data = predefinedPlayers.get(getClientIdentifier());
				
				if(data.title != null)
				{
					connectionTag.putByte("class", (byte) data.title.getHeroClass().ordinal());
					connectionTag.putByte("aspect", (byte) data.title.getHeroAspect().ordinal());
				}
				
				if(data.landTerrain != null)
					connectionTag.putString("aspectTerrain", data.landTerrain.getRegistryName().toString());
				if(data.landTitle != null)
					connectionTag.putString("aspectTitle", data.landTitle.getRegistryName().toString());
			}
		}
		return connectionTag;
	}
	
	static CompoundNBT cratePredefineDataTag(PlayerIdentifier identifier, PredefineData data)
	{
		CompoundNBT connectionTag = new CompoundNBT();
		
		connectionTag.putString("client", identifier.getUsername());
		connectionTag.putString("clientId", identifier.getString());
		connectionTag.putBoolean("isMain", true);
		connectionTag.putBoolean("isActive", false);
		connectionTag.putInt("clientDim", 0);
		
		if(data.title != null)
		{
			connectionTag.putByte("class", (byte) data.title.getHeroClass().ordinal());
			connectionTag.putByte("aspect", (byte) data.title.getHeroAspect().ordinal());
		}
		
		if(data.landTerrain != null)
			connectionTag.putString("aspectTerrain", data.landTerrain.getRegistryName().toString());
		if(data.landTitle != null)
			connectionTag.putString("aspectTitle", data.landTitle.getRegistryName().toString());
		
		return connectionTag;
	}
}