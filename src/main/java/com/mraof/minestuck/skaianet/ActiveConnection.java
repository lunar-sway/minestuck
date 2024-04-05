package com.mraof.minestuck.skaianet;

import com.mojang.serialization.DataResult;
import com.mraof.minestuck.computer.ComputerReference;
import com.mraof.minestuck.computer.ISburbComputer;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Represents an active sburb connection between two computers, which:
 * - Is a condition for using editmode
 * - Plays a role in determining the primary connection between players
 * @author kirderf1
 */
public final class ActiveConnection
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final PlayerIdentifier client;
	private ComputerReference clientComputer;
	private final PlayerIdentifier server;
	private ComputerReference serverComputer;
	
	ActiveConnection(PlayerIdentifier client, ComputerReference clientComputer,
							PlayerIdentifier server, ComputerReference serverComputer)
	{
		this.client = client;
		this.clientComputer = clientComputer;
		this.server = server;
		this.serverComputer = serverComputer;
	}
	
	ActiveConnection(ISburbComputer client, ISburbComputer server)
	{
		this(client.getOwner(), client.createReference(), server.getOwner(), server.createReference());
	}
	
	public boolean isClient(ISburbComputer computer)
	{
		return this.clientComputer().matches(computer) && this.client().equals(computer.getOwner());
	}
	
	public boolean isServer(ISburbComputer computer)
	{
		return this.serverComputer().matches(computer) && this.server().equals(computer.getOwner());
	}
	
	public boolean hasPlayer(PlayerIdentifier player)
	{
		return this.client().equals(player) || this.server().equals(player);
	}
	
	public PlayerIdentifier client()
	{
		return client;
	}
	
	public ComputerReference clientComputer()
	{
		return clientComputer;
	}
	
	public PlayerIdentifier server()
	{
		return server;
	}
	
	public ComputerReference serverComputer()
	{
		return serverComputer;
	}
	
	void updateComputer(ISburbComputer oldComputer, ComputerReference newComputer)
	{
		Objects.requireNonNull(newComputer);
		if(this.clientComputer.matches(oldComputer))
			this.clientComputer = newComputer;
		if(this.serverComputer.matches(oldComputer))
			this.serverComputer = newComputer;
	}
	
	@Nullable
	public Vec3 lastEditmodePosition;
	
	CompoundTag write()
	{
		CompoundTag tag = new CompoundTag();
		client.saveToNBT(tag, "client");
		server.saveToNBT(tag, "server");
		ComputerReference.CODEC.encodeStart(NbtOps.INSTANCE, clientComputer).resultOrPartial(LOGGER::error)
				.ifPresent(computerTag -> tag.put("client_computer", computerTag));
		ComputerReference.CODEC.encodeStart(NbtOps.INSTANCE, serverComputer).resultOrPartial(LOGGER::error)
				.ifPresent(computerTag -> tag.put("server_computer", computerTag));
		
		return tag;
	}
	
	static DataResult<ActiveConnection> read(CompoundTag tag)
	{
		return DataResult.unbox(DataResult.instance().apply4(
				ActiveConnection::new,
				IdentifierHandler.load(tag, "client"),
				ComputerReference.CODEC.parse(NbtOps.INSTANCE, tag.getCompound("client_computer")),
				IdentifierHandler.load(tag, "server"),
				ComputerReference.CODEC.parse(NbtOps.INSTANCE, tag.getCompound("server_computer"))
		));
	}
}
