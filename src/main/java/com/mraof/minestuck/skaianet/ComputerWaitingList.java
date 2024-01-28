package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.computer.ComputerReference;
import com.mraof.minestuck.computer.ISburbComputer;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Iterator;
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
				map.put(IdentifierHandler.load(cmp, "player"), ComputerReference.read(cmp.getCompound("computer")));
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
	
	@Nullable
	ISburbComputer getComputer(PlayerIdentifier player)
	{
		ComputerReference reference = map.get(player);
		if(reference != null)
		{
			ISburbComputer computer = reference.getComputer(skaianetData.mcServer);
			if(computer != null && computer.getOwner().equals(player))
				return computer;
			else
			{
				LOGGER.error("{} had an invalid computer in {} waiting list", player.getUsername(), name);
				remove(player);
			}
		}
		return null;
	}
	
	void useComputerAndRemoveOnSuccess(PlayerIdentifier player, Predicate<ISburbComputer> computerConsumer)
	{
		ISburbComputer clientComputer = this.getComputer(player);
		if(clientComputer == null)
			return;
		boolean result = computerConsumer.test(clientComputer);
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
	
	void validate(MinecraftServer mcServer)
	{
		Iterator<Map.Entry<PlayerIdentifier, ComputerReference>> i = map.entrySet().iterator();
		while(i.hasNext())
		{
			Map.Entry<PlayerIdentifier, ComputerReference> data = i.next();
			ISburbComputer computer = data.getValue().getComputer(mcServer);
			if(computer == null || data.getValue().isInNether() || !computer.getOwner().equals(data.getKey())
					|| !computerValidator.test(computer))
			{
				LOGGER.warn("[SKAIANET] Invalid computer in waiting list!");
				i.remove();
			}
		}
	}
	
	Set<PlayerIdentifier> getPlayers()
	{
		return map.keySet();
	}
}