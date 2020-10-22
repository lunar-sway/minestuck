package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.computer.ComputerReference;
import com.mraof.minestuck.computer.ISburbComputer;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ComputerWaitingList
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final Map<PlayerIdentifier, ComputerReference> map = new HashMap<>();
	
	private final InfoTracker infoTracker;
	private final boolean forClients;
	private final String name;
	
	ComputerWaitingList(InfoTracker infoTracker, boolean forClients, String name)
	{
		this.infoTracker = infoTracker;
		this.forClients = forClients;
		this.name = name;
	}
	
	void read(ListNBT list)
	{
		for(int i = 0; i < list.size(); i++)
		{
			CompoundNBT cmp = list.getCompound(i);
			try
			{
				map.put(IdentifierHandler.load(cmp, "player"), ComputerReference.read(cmp.getCompound("computer")));
			} catch(Exception e)
			{
				LOGGER.error("Got exception when loading entry for the {} waiting list. NBT: {}", name, cmp, e);
			}
		}
	}
	
	ListNBT write()
	{
		ListNBT list = new ListNBT();
		for(Map.Entry<PlayerIdentifier, ComputerReference> entry : map.entrySet())
		{
			CompoundNBT nbt = new CompoundNBT();
			nbt.put("computer", entry.getValue().write(new CompoundNBT()));
			entry.getKey().saveToNBT(nbt, "player");
			list.add(nbt);
		}
		return list;
	}
	
	ISburbComputer getComputer(MinecraftServer mcServer, PlayerIdentifier player)
	{
		ComputerReference reference = map.get(player);
		if(reference != null)
		{
			ISburbComputer computer = reference.getComputer(mcServer);
			if(computer != null)
				return computer;
			else
			{
				LOGGER.error("{} had an invalid computer in {} waiting list", player.getUsername(), name);
				remove(player);
			}
		}
		return null;
	}
	
	void remove(PlayerIdentifier player)
	{
		if(map.remove(player) != null)
			infoTracker.markDirty(player);
	}
	
	void put(PlayerIdentifier player, ISburbComputer computer)
	{
		computer.setIsResuming(forClients);
		map.put(player, computer.createReference());
		infoTracker.markDirty(player);
	}
	
	boolean contains(PlayerIdentifier player)
	{
		return map.containsKey(player);
	}
	
	boolean contains(ISburbComputer computer)
	{
		ComputerReference reference = map.get(computer.getOwner());
		return reference != null && reference.matches(computer);
	}
	
	void replace(PlayerIdentifier player, ComputerReference oldRef, ComputerReference newRef)
	{
		map.replace(player, oldRef, newRef);
		//No need to mark dirty as long as we only update the player on IF they're on the list
	}
	
	void validate(MinecraftServer mcServer)
	{
		Iterator<Map.Entry<PlayerIdentifier, ComputerReference>> i = map.entrySet().iterator();
		while(i.hasNext())
		{
			Map.Entry<PlayerIdentifier, ComputerReference> data = i.next();
			ISburbComputer computer = data.getValue().getComputer(mcServer);
			if(computer == null || data.getValue().isInNether() || !computer.getOwner().equals(data.getKey())
					|| !(forClients && computer.getClientBoolean("isResuming")
					|| !forClients && computer.getServerBoolean("isOpen")))
			{
				LOGGER.warn("[SKAIANET] Invalid computer in waiting list!");
				i.remove();
			}
		}
	}
}