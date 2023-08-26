package com.mraof.minestuck.computer.editmode;

import com.google.common.collect.*;
import com.mojang.datafixers.util.Pair;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Describes a list of locations in which a player in editmode can move around. Should be stored for each Sburb Client
 */
public class EditmodeLocations
{
	//TODO prevent going to non Medium dimensions post Entry, delete maps to the Overworld
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final Multimap<ResourceKey<Level>, Pair<BlockPos, Source>> locations = ArrayListMultimap.create();
	
	public enum Source
	{
		BLOCK,
		ENTITY,
		ENTRY;
		
		public static Source fromInt(int ordinal) //converts int back into enum
		{
			if(0 <= ordinal && ordinal < Source.values().length)
				return Source.values()[ordinal];
			else
				throw new IllegalArgumentException("Invalid ordinal of " + ordinal + " for EditmodeLocations Source!");
		}
	}
	
	public EditmodeLocations(ResourceKey<Level> level, BlockPos pos, Source source)
	{
		addEntry(level, pos, source);
	}
	
	public EditmodeLocations()
	{
	}
	
	public Multimap<ResourceKey<Level>, Pair<BlockPos, Source>> getLocations()
	{
		return locations;
	}
	
	public void addEntry(ResourceKey<Level> level, BlockPos pos, Source source)
	{
		if(level == null || pos == null)
			return;
		
		Pair<BlockPos, Source> locationPairIn = Pair.of(pos, source);
		
		//TODO consider validating the pos and source
		if(locations.containsKey(level))
		{
			for(Pair<BlockPos, Source> locationPair : locations.get(level))
			{
				if(locationPair.getFirst().equals(pos) && locationPair.getSecond().equals(source))
					return; //prevent duplicates
			}
		}
		
		locations.put(level, locationPairIn);
	}
	
	public void removeEntry(ResourceKey<Level> level, BlockPos pos, Source source)
	{
		if(level == null || pos == null)
			return;
		
		//dont try to remove ENTRY Sources as they are only added once
		if(source != Source.ENTRY)
			locations.remove(level, Pair.of(pos, source));
	}
	
	//TODO switch to non-static
	public static ListTag write(Multimap<ResourceKey<Level>, Pair<BlockPos, Source>> locations)
	{
		ListTag listTag = new ListTag();
		
		for(Map.Entry<ResourceKey<Level>, Pair<BlockPos, Source>> entry : locations.entries())
		{
			CompoundTag nbt = new CompoundTag();
			
			ResourceLocation.CODEC.encodeStart(NbtOps.INSTANCE, entry.getKey().location()).resultOrPartial(LOGGER::error)
					.ifPresent(tag -> nbt.put("dim", tag));
			
			BlockPos pos = entry.getValue().getFirst();
			
			nbt.putInt("x", pos.getX());
			nbt.putInt("y", pos.getY());
			nbt.putInt("z", pos.getZ());
			
			nbt.putInt("source", entry.getValue().getSecond().ordinal());
			
			listTag.add(nbt);
		}
		
		return listTag;
	}
	
	public static EditmodeLocations read(ListTag locationsTag)
	{
		EditmodeLocations locations = new EditmodeLocations();
		
		for(int i = 0; i < locationsTag.size(); i++)
		{
			CompoundTag nbt = locationsTag.getCompound(i);
			
			ResourceKey<Level> dimension = Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, nbt.get("dim")).resultOrPartial(LOGGER::error).orElse(null);
			
			int posX = nbt.getInt("x");
			int posY = nbt.getInt("y");
			int posZ = nbt.getInt("z");
			
			int sourceOrdinal = nbt.getInt("source");
			
			locations.addEntry(dimension, new BlockPos(posX, posY, posZ), Source.fromInt(sourceOrdinal));
		}
		
		return locations;
	}
	
	private boolean isValidSource(Level level, BlockPos pos, Source source)
	{
		if(source == Source.BLOCK)
		{
			if(level.getBlockEntity(pos) instanceof ComputerBlockEntity computerBlockEntity)
			{
				return !computerBlockEntity.isBroken() && computerBlockEntity.hasProgram(0);
			}
		} else if(source == Source.ENTITY)
		{
			return false; //There is no conditions for a valid Source of type ENTITY yet
		} else
			return true; //Sources of type ENTRY should always be considered valid, unless we need to determine whether the dimension was set correctly
		
		
		return false;
	}
	
	/**
	 * Takes in a player and looks through each source (such as a computer) providing editmode to see if the player resides within one of the spaces.
	 * Used both server side and client side
	 *
	 * @param editPlayer Player in editmode
	 * @return Whether the player is standing in a supported region
	 */
	public boolean isValidLocation(Player editPlayer, double range)
	{
		Level editLevel = editPlayer.level();
		double editPosX = editPlayer.getX();
		double editPosY = editPlayer.getY();
		double editPosZ = editPlayer.getZ();
		
		if(!locations.containsKey(editLevel.dimension()))
			return false;
		
		List<BlockPos> allLevelPos = new ArrayList<>();
		for(Pair<BlockPos, Source> valuePair : locations.get(editLevel.dimension()).stream().toList())
		{
			allLevelPos.add(valuePair.getFirst());
		}
		
		boolean xMatches = false;
		boolean yMatches = false;
		boolean zMatches = false;
		boolean allMatch = false;
		
		for(BlockPos iteratePos : allLevelPos)
		{
			int centerX = iteratePos.getX();
			int centerY = iteratePos.getY();
			int centerZ = iteratePos.getZ();
			
			//if the range is 1 and the player pos isnt an exact match, dont bother
			//if(range == 1 && editPos != iteratePos)
			//	continue;
			
			boolean localXMatches = editPosX >= centerX - range && editPosX <= centerX + range;
			boolean localYMatches = editPosY >= centerY - range && editPosY <= centerY + range;
			boolean localZMatches = editPosZ >= centerZ - range && editPosZ <= centerZ + range;
			
			//TODO does not factor in player offset such as in ServerEditHandler
			if(localXMatches)
				xMatches = true;
			
			if(localYMatches)
				yMatches = true;
			
			if(localZMatches)
				zMatches = true;
			
			if(localXMatches && localYMatches && localZMatches)
				allMatch = true;
		}
		
		if(!xMatches && !allMatch)
			editPlayer.setDeltaMovement(editPlayer.getDeltaMovement().multiply(0, 1, 1));
		
		if(!yMatches && !allMatch)
			editPlayer.setDeltaMovement(editPlayer.getDeltaMovement().multiply(1, 0, 1));
		
		if(!zMatches && !allMatch)
			editPlayer.setDeltaMovement(editPlayer.getDeltaMovement().multiply(1, 1, 0));
		
		if(allMatch)
		{
			return true;
		} else
		{
			//TODO do not use "old" pos, player "sticks" to the edge of the zone
			if(editPlayer.level().isClientSide)
				editPlayer.setPos(editPlayer.xOld, editPlayer.yOld, editPlayer.zOld);
			else editPlayer.teleportTo(editPlayer.xOld, editPlayer.yOld, editPlayer.zOld);
			
			return false;
		}
	}
	
	//TODO only works in one dimension
	public static BlockPos getClosestPosInDimension(Multimap<ResourceKey<Level>, Pair<BlockPos, Source>> locations, Player player)
	{
		BlockPos playerPos = player.blockPosition();
		ResourceKey<Level> editLevel = player.level().dimension();
		BlockPos pickedPos = null;
		double pickedPosDistance = Integer.MAX_VALUE;
		
		for(Pair<BlockPos, Source> valuePair : locations.get(editLevel).stream().toList())
		{
			BlockPos iteratedPos = valuePair.getFirst();
			
			if(pickedPos == null)
				pickedPos = iteratedPos;
			else
			{
				double iteratedPosDistance = Math.sqrt(iteratedPos.distSqr(playerPos));
				
				if(iteratedPosDistance < pickedPosDistance)
				{
					pickedPos = iteratedPos;
					pickedPosDistance = iteratedPosDistance;
				}
			}
		}
		
		return pickedPos;
	}
	
	public static Multimap<ResourceKey<Level>, Pair<BlockPos, Source>> addTestingLocations(ResourceKey<Level> editLevel, Multimap<ResourceKey<Level>, Pair<BlockPos, Source>> locations)
	{
		//temp for testing
		List<BlockPos> allBlockPos = new ArrayList<>();
		for(Map.Entry<ResourceKey<Level>, Pair<BlockPos, Source>> location : locations.entries())
		{
			allBlockPos.add(location.getValue().getFirst());
		}
		
		if(!allBlockPos.contains(new BlockPos(18, 71, -36)))
			locations.put(editLevel, Pair.of(new BlockPos(18, 71, -36), Source.BLOCK));
		if(!allBlockPos.contains(new BlockPos(10, 71, -36)))
			locations.put(editLevel, Pair.of(new BlockPos(10, 71, -36), Source.BLOCK));
		if(!allBlockPos.contains(new BlockPos(10, 80, -32)))
			locations.put(editLevel, Pair.of(new BlockPos(10, 80, -32), Source.BLOCK));
		
		if(!allBlockPos.contains(new BlockPos(0, 100, 0)))
			locations.put(editLevel, Pair.of(new BlockPos(0, 100, 0), Source.ENTRY));
		if(!allBlockPos.contains(new BlockPos(0, 120, 0)))
			locations.put(editLevel, Pair.of(new BlockPos(0, 120, 0), Source.ENTRY));
		if(!allBlockPos.contains(new BlockPos(0, 140, 0)))
			locations.put(editLevel, Pair.of(new BlockPos(0, 140, 0), Source.ENTRY));
		
		return locations;
	}
}