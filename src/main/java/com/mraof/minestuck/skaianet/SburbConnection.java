package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.computer.ComputerReference;
import com.mraof.minestuck.computer.ISburbComputer;
import com.mraof.minestuck.computer.editmode.DeployEntry;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.skaianet.client.ReducedConnection;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.lands.LandInfo;
import com.mraof.minestuck.world.lands.LandTypePair;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public final class SburbConnection
{
	final SkaianetHandler skaianet;
	
	@Nonnull
	private final PlayerIdentifier clientIdentifier;
	@Nonnull
	private PlayerIdentifier serverIdentifier;
	private ComputerReference clientComputer;
	private ComputerReference serverComputer;
	private Session session;
	
	private boolean isActive;
	private boolean isMain;
	boolean lockedToSession;
	private boolean hasEntered = false;	//If the player has entered. Is set to true after entry has finished
	private LandInfo clientLandInfo;	//The land info for this client player. This is initialized in preparation for entry
	int artifactType;
	
	private final Set<String> givenItemList = new HashSet<>();
	
	//Only used by the edit handler
	private ListNBT inventory;
	
	SburbConnection(PlayerIdentifier client, SkaianetHandler skaianet)
	{
		this(client, IdentifierHandler.NULL_IDENTIFIER, skaianet);
	}
	
	SburbConnection(PlayerIdentifier client, PlayerIdentifier server, SkaianetHandler skaianet)
	{
		clientIdentifier = Objects.requireNonNull(client);
		serverIdentifier = Objects.requireNonNull(server);
		this.skaianet = skaianet;
		this.lockedToSession = false;
	}
	
	SburbConnection(CompoundNBT nbt, SkaianetHandler skaianet)
	{
		this.skaianet = skaianet;
		isMain = nbt.getBoolean("IsMain");
		boolean active = true;
		if(nbt.contains("Inventory", Constants.NBT.TAG_LIST))
			inventory = nbt.getList("Inventory", Constants.NBT.TAG_COMPOUND);
		if(isMain)
		{
			active = nbt.getBoolean("IsActive");
			
			lockedToSession = nbt.getBoolean("locked");
			ListNBT list = nbt.getList("GivenItems", Constants.NBT.TAG_STRING);
			for(int i = 0; i < list.size(); i++)
			{
				givenItemList.add(list.getString(i));
			}
		}
		clientIdentifier = IdentifierHandler.load(nbt, "client");
		serverIdentifier = IdentifierHandler.load(nbt, "server");
		if(active)
		{
			try
			{
				clientComputer = ComputerReference.read(nbt.getCompound("client_computer"));
				serverComputer = ComputerReference.read(nbt.getCompound("server_computer"));
				isActive = true;
			} catch(Exception e)
			{
				Debug.logger.error("Unable to read computer position for sburb connection between "+ clientIdentifier.getUsername()+" and "+serverIdentifier.getUsername()+", setting connection to be inactive. Cause: ", e);
			}
		}
		if(nbt.contains("ClientLand", Constants.NBT.TAG_COMPOUND))
		{
			clientLandInfo = LandInfo.read(nbt.getCompound("ClientLand"), skaianet, getClientIdentifier());
			MSDimensions.updateLandMaps(this);
			hasEntered = nbt.contains("has_entered") ? nbt.getBoolean("has_entered") : true;
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
			nbt.putBoolean("locked", lockedToSession);
			ListNBT list = new ListNBT();
			for(String name : givenItemList)
				list.add(StringNBT.valueOf(name));
			
			nbt.put("GivenItems", list);
			if(clientLandInfo != null)
			{
				nbt.put("ClientLand", clientLandInfo.write(new CompoundNBT()));
				nbt.putBoolean("has_entered", hasEntered);
			}
		}
		
		getClientIdentifier().saveToNBT(nbt, "client");
		getServerIdentifier().saveToNBT(nbt, "server");
		
		if(isActive)
		{
			nbt.put("client_computer", clientComputer.write(new CompoundNBT()));
			nbt.put("server_computer", serverComputer.write(new CompoundNBT()));
		}
		
		nbt.putInt("artifact", artifactType);
		return nbt;
	}
	
	void setSession(Session session)
	{
		this.session = session;
	}
	
	void copyComputerReferences(SburbConnection connection)
	{
		if(!connection.isActive() || !connection.getClientIdentifier().equals(clientIdentifier)
				|| !connection.getServerIdentifier().equals(serverIdentifier))
			throw new IllegalArgumentException();
		clientComputer = connection.getClientComputer();
		serverComputer = connection.getServerComputer();
		isActive = true;
	}
	
	void setActive(ISburbComputer client, ISburbComputer server)
	{
		if(isActive())
			throw new IllegalStateException("Should not activate sburb connection when already active");
		Objects.requireNonNull(client);
		Objects.requireNonNull(server);
		
		clientComputer = client.createReference();
		serverComputer = server.createReference();
		isActive = true;
		skaianet.infoTracker.markDirty(this);
		
		client.connected(serverIdentifier, true);
		server.connected(clientIdentifier, false);
		
	}
	
	void close()
	{
		clientComputer = null;
		serverComputer = null;
		isActive = false;
		skaianet.infoTracker.markDirty(this);
	}
	
	@Nonnull
	public PlayerIdentifier getClientIdentifier()
	{
		return clientIdentifier;
	}
	
	@Nonnull
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
		skaianet.infoTracker.markDirty(this);
		serverIdentifier = IdentifierHandler.NULL_IDENTIFIER;
	}
	
	void setNewServerPlayer(PlayerIdentifier identifier)
	{
		if(hasServerPlayer())
			throw new IllegalStateException("Connection already has server player");
		else serverIdentifier = Objects.requireNonNull(identifier);
		skaianet.infoTracker.markDirty(this);
	}
	
	boolean hasPlayer(PlayerIdentifier player)
	{
		return clientIdentifier.equals(player) || serverIdentifier.equals(player);
	}
	
	public ComputerReference getClientComputer()
	{
		return clientComputer;
	}
	public ComputerReference getServerComputer()
	{
		return serverComputer;
	}
	
	public boolean isClient(ISburbComputer computer)
	{
		return isActive && getClientIdentifier().equals(computer.getOwner()) && clientComputer.matches(computer);
	}
	
	public boolean isServer(ISburbComputer computer)
	{
		return isActive && getServerIdentifier().equals(computer.getOwner()) && serverComputer.matches(computer);
	}
	void updateComputer(ISburbComputer oldComputer, ComputerReference newComputer)
	{
		if(isActive())
		{
			if(clientComputer.matches(oldComputer))
				clientComputer = Objects.requireNonNull(newComputer);
			if(serverComputer.matches(oldComputer))
				serverComputer = Objects.requireNonNull(newComputer);
		}
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
			skaianet.infoTracker.markDirty(this);
		}
	}
	
	public boolean hasEntered()
	{
		return hasEntered;
	}
	public Title getClientTitle()
	{
		if(hasEntered())
		{
			Title title = PlayerSavedData.getData(getClientIdentifier(), skaianet.mcServer).getTitle();
			if(title == null)
				Debug.warnf("Found player %s that has entered, but did not have a title!", getClientIdentifier().getUsername());
			return title;
		}
		return null;
	}
	/**
	 * @return The land dimension assigned to the client player.
	 */
	public DimensionType getClientDimension()
	{
		return getLandInfo() == null ? null : getLandInfo().getDimensionType();
	}
	public LandInfo getLandInfo()
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
			MSDimensions.updateLandMaps(this);
		}
	}
	void setHasEntered()
	{
		if(clientLandInfo == null)
			throw new IllegalStateException("Land has not been initiated, can't have entered now!");
		if(hasEntered)
			throw new IllegalStateException("Can't have entered twice");
		hasEntered = true;
		skaianet.infoTracker.markDirty(this);
	}
	public boolean hasGivenItem(DeployEntry item) { return givenItemList.contains(item.getName()); }
	public void setHasGivenItem(DeployEntry item)
	{
		if(givenItemList.add(item.getName()))
		{
			EditData data = ServerEditHandler.getData(skaianet.mcServer, this);
			if(data != null)
				data.sendGivenItemsToEditor();
		}
	}
	void resetGivenItems()
	{
		if(!givenItemList.isEmpty())
		{
			givenItemList.clear();
			EditData data = ServerEditHandler.getData(skaianet.mcServer, this);
			if(data != null)
				data.sendGivenItemsToEditor();
		}
	}
	
	public ListNBT getEditmodeInventory()
	{
		return inventory == null ? null : inventory.copy();
	}
	
	public void putEditmodeInventory(ListNBT nbt)
	{
		inventory = nbt;
	}
	
	void copyFrom(SburbConnection other)
	{
		lockedToSession = other.lockedToSession;
		clientLandInfo = other.clientLandInfo;
		hasEntered = other.hasEntered;
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
}