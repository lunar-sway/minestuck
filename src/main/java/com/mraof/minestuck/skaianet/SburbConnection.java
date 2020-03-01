package com.mraof.minestuck.skaianet;

import com.mojang.datafixers.Dynamic;
import com.mraof.minestuck.computer.editmode.DeployEntry;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.lands.LandInfo;
import com.mraof.minestuck.world.lands.LandTypePair;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.util.Constants;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public final class SburbConnection
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
	boolean canSplit;
	private LandInfo clientLandInfo;
	int artifactType;
	
	private final Set<String> givenItemList = new HashSet<>();
	
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
			
			if(nbt.contains("CanSplit", Constants.NBT.TAG_ANY_NUMERIC))
				canSplit = nbt.getBoolean("CanSplit");
			ListNBT list = nbt.getList("GivenItems", Constants.NBT.TAG_STRING);
			for(int i = 0; i < list.size(); i++)
			{
				givenItemList.add(list.getString(i));
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
			clientLandInfo = LandInfo.read(nbt.getCompound("ClientLand"), handler, getClientIdentifier());
			handler.updateLandMaps(this);
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
			nbt.putBoolean("CanSplit", canSplit);
			ListNBT list = new ListNBT();
			for(String name : givenItemList)
				list.add(new StringNBT(name));
			
			nbt.put("GivenItems", list);
			if(clientLandInfo != null)
			{
				nbt.put("ClientLand", clientLandInfo.write(new CompoundNBT()));
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
	
	public boolean hasServerPlayer()
	{
		return getServerIdentifier() != IdentifierHandler.NULL_IDENTIFIER;
	}
	
	void removeServerPlayer()
	{
		serverIdentifier = IdentifierHandler.NULL_IDENTIFIER;
	}
	
	public GlobalPos getClientComputer()
	{
		return clientComputer;
	}
	public GlobalPos getServerComputer()
	{
		return serverComputer;
	}
	
	public boolean isClient(ComputerTileEntity computer)
	{
		return isActive && getClientIdentifier().equals(computer.owner) && clientComputer.getDimension() == computer.getWorld().getDimension().getType() && clientComputer.getPos().equals(computer.getPos());
	}
	
	public boolean isServer(ComputerTileEntity computer)
	{
		return isActive && getServerIdentifier().equals(computer.owner) && serverComputer.getDimension() == computer.getWorld().getDimension().getType() && serverComputer.getPos().equals(computer.getPos());
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
	
	public boolean hasEntered()
	{
		return clientLandInfo != null;
	}
	/**
	 * @return The land dimension assigned to the client player.
	 */
	public DimensionType getClientDimension()
	{
		return getLandInfo() == null ? null : getLandInfo().getDimensionType();
	}
	LandInfo getLandInfo()
	{
		return clientLandInfo;
	}
	void setLand(LandTypePair landTypes, DimensionType dimension)
	{
		if(clientLandInfo != null)
			throw new IllegalStateException("Can't set land twice");
		else
		{
			clientLandInfo = new LandInfo(clientIdentifier, landTypes, dimension, new Random());	//TODO handle random better
			handler.updateLandMaps(this);
		}
	}
	@Deprecated
	public boolean hasGivenItem(String item) { return givenItemList.contains(item); }
	public boolean hasGivenItem(DeployEntry item) { return givenItemList.contains(item.getName()); }
	public void setHasGivenItem(DeployEntry item)
	{
		if(givenItemList.add(item.getName()))
		{
			EditData data = ServerEditHandler.getData(handler.mcServer, this);
			if(data != null)
				data.sendGivenItemsToEditor();
		}
	}
	void resetGivenItems() { givenItemList.clear(); }
	
	void copyFrom(SburbConnection other)
	{
		canSplit = other.canSplit;
		centerX = other.centerX;
		centerZ = other.centerZ;
		clientLandInfo = other.clientLandInfo;
		artifactType = other.artifactType;
		if(other.inventory != null)
			inventory = other.inventory.copy();
	}
	/**
	 * Writes the connection info needed client-side to a network buffer. Must match with {@link ReducedConnection#read}.
	 */
	public void toBuffer(PacketBuffer buffer)
	{
		buffer.writeBoolean(isMain);
		if(isMain){
			buffer.writeBoolean(isActive);
			buffer.writeBoolean(hasEntered());
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
		connectionTag.putString("clientId", getClientIdentifier().getCommandString());
		if(hasServerPlayer())
			connectionTag.putString("server", getServerIdentifier().getUsername());
		connectionTag.putBoolean("isMain", isMain());
		connectionTag.putBoolean("isActive", isActive());
		if(isMain())
		{
			if(clientLandInfo != null)
			{
				connectionTag.putString("clientDim", getClientDimension().getRegistryName().toString());
				connectionTag.putString("aspect1", clientLandInfo.landName1());
				connectionTag.putString("aspect2", clientLandInfo.landName2());
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
		connectionTag.putString("clientId", identifier.getCommandString());
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