package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.computer.ComputerReference;
import com.mraof.minestuck.computer.ISburbComputer;
import com.mraof.minestuck.computer.editmode.DeployEntry;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.event.ConnectionCreatedEvent;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.skaianet.client.ReducedConnection;
import com.mraof.minestuck.player.PlayerSavedData;
import net.minecraft.nbt.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public final class SburbConnection
{
	private static final Logger LOGGER = LogManager.getLogger();
	
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
	private ResourceKey<Level> clientLandKey;	//The land info for this client player. This is initialized in preparation for entry
	int artifactType;
	private GristType baseGrist;
	
	private final Set<String> givenItemList = new HashSet<>();
	
	//Only used by the edit handler
	private ListTag inventory;
	
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
	
	SburbConnection(CompoundTag nbt, SkaianetHandler skaianet)
	{
		this.skaianet = skaianet;
		isMain = nbt.getBoolean("IsMain");
		boolean active = true;
		if(nbt.contains("Inventory", Tag.TAG_LIST))
			inventory = nbt.getList("Inventory", Tag.TAG_COMPOUND);
		if(isMain)
		{
			active = nbt.getBoolean("IsActive");
			
			lockedToSession = nbt.getBoolean("locked");
			ListTag list = nbt.getList("GivenItems", Tag.TAG_STRING);
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
				LOGGER.error("Unable to read computer position for sburb connection between {} and {}, setting connection to be inactive. Cause: ", clientIdentifier.getUsername(), serverIdentifier.getUsername(), e);
			}
		}
		if(nbt.contains("ClientLand"))
		{
			clientLandKey = Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, nbt.get("ClientLand")).resultOrPartial(LOGGER::error).orElse(null);
			hasEntered = nbt.contains("has_entered") ? nbt.getBoolean("has_entered") : true;
		}
		artifactType = nbt.getInt("artifact");
		baseGrist = GristType.read(nbt, "base_grist", () -> SburbHandler.generateGristType(new Random()));
	}
	
	CompoundTag write()
	{
		CompoundTag nbt = new CompoundTag();
		nbt.putBoolean("IsMain", isMain);
		if(inventory != null)
			nbt.put("Inventory", inventory);
		if(isMain)
		{
			nbt.putBoolean("IsActive", isActive);
			nbt.putBoolean("locked", lockedToSession);
			ListTag list = new ListTag();
			for(String name : givenItemList)
				list.add(StringTag.valueOf(name));
			
			nbt.put("GivenItems", list);
			if(clientLandKey != null)
			{
				Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, clientLandKey).resultOrPartial(LOGGER::error)
						.ifPresent(tag -> nbt.put("ClientLand", tag));
				nbt.putBoolean("has_entered", hasEntered);
			}
		}
		
		getClientIdentifier().saveToNBT(nbt, "client");
		getServerIdentifier().saveToNBT(nbt, "server");
		
		if(isActive)
		{
			nbt.put("client_computer", clientComputer.write(new CompoundTag()));
			nbt.put("server_computer", serverComputer.write(new CompoundTag()));
		}
		
		nbt.putInt("artifact", artifactType);
		baseGrist.write(nbt, "base_grist");
		return nbt;
	}
	
	public Session getSession()
	{
		return session;
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
	
	void setActive(ISburbComputer client, ISburbComputer server, ConnectionCreatedEvent.ConnectionType type)
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
		
		MinecraftForge.EVENT_BUS.post(new ConnectionCreatedEvent(skaianet.mcServer, this, session, type));
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
		skaianet.sessionHandler.onConnectionChainBroken(session);
	}
	
	void setNewServerPlayer(PlayerIdentifier server) throws MergeResult.SessionMergeException
	{
		if(hasServerPlayer())
			throw new IllegalStateException("Connection already has server player");
		if(skaianet.getPrimaryConnection(server, false).isPresent())
			throw MergeResult.GENERIC_FAIL.exception();
		skaianet.sessionHandler.prepareSessionFor(clientIdentifier, server);	//Make sure that it is fine to add the server here session-wise
		
		serverIdentifier = Objects.requireNonNull(server);
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
				LOGGER.warn("Found player {} that has entered, but did not have a title!", getClientIdentifier().getUsername());
			return title;
		}
		return null;
	}
	/**
	 * @return The land dimension assigned to the client player.
	 */
	public ResourceKey<Level> getClientDimension()
	{
		return this.clientLandKey;
	}
	void setLand(ResourceKey<Level> dimension)
	{
		if(clientLandKey != null)
			throw new IllegalStateException("Can't set land twice");
		else
		{
			clientLandKey = dimension;
		}
	}
	void setHasEntered()
	{
		if(clientLandKey == null)
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
	
	public ListTag getEditmodeInventory()
	{
		return inventory == null ? null : inventory.copy();
	}
	
	public void putEditmodeInventory(ListTag nbt)
	{
		inventory = nbt;
	}
	
	public GristType getBaseGrist()
	{
		return baseGrist;
	}
	
	void setBaseGrist(GristType type)
	{
		if(baseGrist != null)
			throw new IllegalStateException("base grist type has already been set!");
		baseGrist = type;
	}
	
	void copyFrom(SburbConnection other)
	{
		lockedToSession = other.lockedToSession;
		clientLandKey = other.clientLandKey;
		hasEntered = other.hasEntered;
		artifactType = other.artifactType;
		baseGrist = other.baseGrist;
		if(other.inventory != null)
			inventory = other.inventory.copy();
	}
	/**
	 * Writes the connection info needed client-side to a network buffer. Must match with {@link ReducedConnection#read}.
	 */
	public void toBuffer(FriendlyByteBuf buffer)
	{
		buffer.writeBoolean(isMain);
		if(isMain){
			buffer.writeBoolean(isActive);
			buffer.writeBoolean(hasEntered());
		}
		buffer.writeInt(getClientIdentifier().getId());
		buffer.writeUtf(getClientIdentifier().getUsername(), 16);
		buffer.writeInt(getServerIdentifier().getId());
		buffer.writeUtf(getServerIdentifier().getUsername(), 16);
	}
}