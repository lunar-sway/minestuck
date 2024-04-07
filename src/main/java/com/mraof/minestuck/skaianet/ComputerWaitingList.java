package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.computer.ComputerReference;
import com.mraof.minestuck.computer.ISburbComputer;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

class ComputerWaitingList
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
			Optional<PlayerIdentifier> player = IdentifierHandler.load(cmp, "player").resultOrPartial(LOGGER::error);
			Optional<ComputerReference> computerReference = ComputerReference.CODEC.parse(NbtOps.INSTANCE, cmp.get("computer")).resultOrPartial(LOGGER::error);
			if(player.isEmpty() || computerReference.isEmpty())
				continue;
			
			map.put(player.get(), computerReference.get());
		}
	}
	
	ListTag write()
	{
		ListTag list = new ListTag();
		for(Map.Entry<PlayerIdentifier, ComputerReference> entry : map.entrySet())
		{
			Optional<Tag> computerTag = ComputerReference.CODEC.encodeStart(NbtOps.INSTANCE, entry.getValue()).resultOrPartial(LOGGER::error);
			if(computerTag.isEmpty())
				continue;
			
			CompoundTag nbt = new CompoundTag();
			nbt.put("computer", computerTag.get());
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