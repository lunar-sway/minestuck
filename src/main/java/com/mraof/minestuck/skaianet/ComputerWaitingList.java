package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.computer.ComputerReference;
import com.mraof.minestuck.computer.ISburbComputer;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class ComputerWaitingList
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final Map<PlayerIdentifier, ComputerReference> map = new HashMap<>();
	
	private final SkaianetData skaianetData;
	private final Predicate<ISburbComputer> computerValidator;
	private final String name;
	
	ComputerWaitingList(SkaianetData skaianetData, Predicate<ISburbComputer> computerValidator, String name)
	{
		this.skaianetData = skaianetData;
		this.computerValidator = computerValidator;
		this.name = name;
	}
	
	void read(ListTag list)
	{
		for(int i = 0; i < list.size(); i++)
		{
			CompoundTag cmp = list.getCompound(i);
			try
			{
				map.put(IdentifierHandler.loadOrThrow(cmp, "player"), ComputerReference.read(cmp.getCompound("computer")));
			} catch(Exception e)
			{
				LOGGER.error("Got exception when loading entry for the {} waiting list. NBT: {}", name, cmp, e);
			}
		}
	}
	
	ListTag write()
	{
		ListTag list = new ListTag();
		for(Map.Entry<PlayerIdentifier, ComputerReference> entry : map.entrySet())
		{
			CompoundTag nbt = new CompoundTag();
			nbt.put("computer", entry.getValue().write(new CompoundTag()));
			entry.getKey().saveToNBT(nbt, "player");
			list.add(nbt);
		}
		return list;
	}
	
	void useComputerAndRemoveOnSuccess(PlayerIdentifier player, Predicate<ISburbComputer> computerConsumer)
	{
		ComputerReference reference = map.get(player);
		if(reference == null)
			return;
		
		ISburbComputer computer = reference.getComputer(skaianetData.mcServer);
		if(computer == null || reference.isInNether() || !computer.getOwner().equals(player) || !computerValidator.test(computer))
		{
			LOGGER.error("{} had an invalid computer in {} waiting list", player.getUsername(), name);
			remove(player);
			return;
		}
		
		boolean result = computerConsumer.test(computer);
		if(result)
			this.remove(player);
	}
	
	void remove(PlayerIdentifier player)
	{
		if(map.remove(player) != null)
			skaianetData.infoTracker.markDirty(player);
	}
	
	void put(PlayerIdentifier player, ComputerReference reference)
	{
		map.put(player, reference);
		skaianetData.infoTracker.markDirty(player);
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
	
	Set<PlayerIdentifier> getPlayers()
	{
		return map.keySet();
	}
}