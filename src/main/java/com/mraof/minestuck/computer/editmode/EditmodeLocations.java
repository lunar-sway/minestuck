package com.mraof.minestuck.computer.editmode;

import com.google.common.collect.*;
import com.mojang.datafixers.util.Pair;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.world.MSDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Contains a list of block positions in a radius around which an editmode player can move freely.
 * An instance is stored in SburbConnection for the SBURB client of the connection.
 */
public class EditmodeLocations
{
	//TODO prevent going to non Medium dimensions post Entry, delete maps to the Overworld
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final int VALIDATION_RADIUS = 25;
	public static final int ENTRY_RANGE = 30;
	
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
	
	/**
	 * Takes in a player and looks through each source (such as a computer) providing editmode to see if the player resides within one of the spaces.
	 * Used both server side and client side
	 *
	 * @param editPlayer Player in editmode
	 * @return Whether the player is standing in a supported region
	 */
	public boolean canMoveAtPosition(Player editPlayer, double defaultRange)
	{
		Level editLevel = editPlayer.level();
		double editPosX = editPlayer.getX();
		double editPosY = editPlayer.getY();
		double editPosZ = editPlayer.getZ();
		
		if(!locations.containsKey(editLevel.dimension()))
			return false;
		
		List<Pair<BlockPos, Source>> posSourcePairs = locations.get(editLevel.dimension()).stream().toList();
		boolean xInBounds = false;
		boolean yInBounds = false;
		boolean zInBounds = false;
		boolean allMatch = false;
		boolean noneMatch = true;
		
		for(Pair<BlockPos, Source> posSourcePair : posSourcePairs)
		{
			//if the source is ENTRY, then it should be 30 regardless of what the config says due to the static nature of the location additions when Entry occurred
			double moddedRange = posSourcePair.getSecond() == Source.ENTRY ? ENTRY_RANGE : defaultRange;
			BlockPos iteratePos = posSourcePair.getFirst();
			
			int centerX = iteratePos.getX();
			int centerY = iteratePos.getY();
			int centerZ = iteratePos.getZ();
			
			//if the defaultRange is 1 and the player pos isnt an exact match, dont bother
			//if(defaultRange == 1 && editPos != iteratePos)
			//	continue;
			
			boolean localXInBounds = isWithinRange(editPosX, centerX, moddedRange);
			boolean localYInBounds = isWithinRange(editPosY, centerY, moddedRange);
			boolean localZInBounds = isWithinRange(editPosZ, centerZ, moddedRange);
			
			//TODO does not factor in player offset such as in ServerEditHandler
			if(localXInBounds)
			{
				xInBounds = true;
				noneMatch = false;
			}
			
			if(localYInBounds)
			{
				yInBounds = true;
				noneMatch = false;
			}
			
			if(localZInBounds)
			{
				zInBounds = true;
				noneMatch = false;
			}
			
			if(localXInBounds && localYInBounds && localZInBounds)
			{
				allMatch = true;
				break;
			}
		}
		
		return handleMovement(editPlayer, xInBounds, yInBounds, zInBounds, allMatch, noneMatch);
	}
	
	/**
	 * Checks the editmode players surroundings, then both removes now invalid locations and adds new valid locations.
	 */
	public void validateNearbySources(Player editPlayer, SburbConnection connection)
	{
		Level editLevel = editPlayer.level();
		ResourceKey<Level> editDimension = editLevel.dimension();
		
		if(editLevel.isClientSide)
			return;
		
		//security for pre EditmodeLocations update worlds and for failed SburbConnection copy events
		if(connection.getClientDimension() != null && connection.getClientDimension().equals(editDimension))
		{
			//if the client dimension isnt in locations or no location was of source ENTRY
			if(!locations.containsKey(editDimension) || locations.get(editDimension).stream().noneMatch(blockPosSourcePair -> blockPosSourcePair.getSecond() == Source.ENTRY))
				connection.getClientEditmodeLocations().addEntryLocations(editDimension, connection);
		}
		
		if(!locations.containsKey(editDimension))
			return;
		
		List<Pair<BlockPos, Source>> allLevelPairs = locations.get(editDimension).stream().toList();
		
		for(BlockPos blockIterate : BlockPos.betweenClosed(editPlayer.blockPosition().offset(VALIDATION_RADIUS, VALIDATION_RADIUS, VALIDATION_RADIUS), editPlayer.blockPosition().offset(-VALIDATION_RADIUS, -VALIDATION_RADIUS, -VALIDATION_RADIUS)))
		{
			checkBlockPosValidation(editPlayer, connection, editLevel, editDimension, allLevelPairs, blockIterate);
		}
	}
	
	/**
	 * Gets the closest location in the dimension or returns null if there is none. May require validation.
	 */
	public BlockPos getClosestPosInDimension(Player player)
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
	
	//TODO should this be non-static?
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
	
	private void checkBlockPosValidation(Player editPlayer, SburbConnection connection, Level editLevel, ResourceKey<Level> editDimension, List<Pair<BlockPos, Source>> allLevelPairs, BlockPos blockIterate)
	{
		//TODO locations appear to be added/removed inappropriately
		
		//if locations contains the iterated block pos and the entry is no longer valid, remove it. Else if locations did not contain the iterated pos and its valid, add it
		if(allLevelPairs.stream().anyMatch(blockPosSourcePair -> blockIterate.equals(blockPosSourcePair.getFirst()) && blockPosSourcePair.getSecond() == Source.BLOCK))
		{
			if(!isValidBlockSource(connection, editLevel, blockIterate))
			{
				connection.removeClientEditmodeLocations(editDimension, blockIterate, Source.BLOCK);
				
				int range = MSDimensions.isLandDimension(((ServerPlayer) editPlayer).server, editPlayer.level().dimension()) ? MinestuckConfig.SERVER.landEditRange.get() : MinestuckConfig.SERVER.overworldEditRange.get();
				
				//TODO consider adding message indicating what happened
				if(!canMoveAtPosition(editPlayer, range))
				{
					//teleport player to nearest valid location
					BlockPos nextClosestLocationPos = getClosestPosInDimension(editPlayer);
					if(nextClosestLocationPos != null)
					{
						editPlayer.teleportTo(nextClosestLocationPos.getX() + 0.5D, nextClosestLocationPos.getY() + 1.0D, nextClosestLocationPos.getZ() + 0.5D);
					}
				}
			}
		} else if(isValidBlockSource(connection, editLevel, blockIterate))
		{
			connection.addClientEditmodeLocation(editDimension, blockIterate, Source.BLOCK);
		}
	}
	
	public static boolean isValidBlockSource(SburbConnection connection, Level level, BlockPos pos)
	{
		if(level.getBlockEntity(pos) instanceof ComputerBlockEntity computerBlockEntity)
		{
			return !computerBlockEntity.isBroken() && computerBlockEntity.hasProgram(0) && computerBlockEntity.owner.equals(connection.getClientIdentifier());
		}
		
		return false;
	}
	
	/**
	 * Takes all the flags from canMoveAtPosition() and modifies the players movement/position as needed.
	 *
	 * @return Whether the player is out of one or more coordinate bounds
	 */
	private boolean handleMovement(Player editPlayer, boolean xInBounds, boolean yInBounds, boolean zInBounds, boolean allMatch, boolean noneMatch)
	{
		//TODO with this approach (potentially the use of "old" pos) the player "sticks" to the edge of the zone
		//TODO consider adding message indicating the player is at the edge
		if(allMatch)
		{
			return true;
		} else if(noneMatch)
		{
			if(editPlayer.level().isClientSide)
				editPlayer.setPos(editPlayer.xOld, editPlayer.yOld, editPlayer.zOld);
			else editPlayer.teleportTo(editPlayer.xOld, editPlayer.yOld, editPlayer.zOld);
			
			return false;
		} else
		{
			if(!xInBounds)
				editPlayer.setDeltaMovement(editPlayer.getDeltaMovement().multiply(0, 1, 1));
			
			if(!yInBounds)
				editPlayer.setDeltaMovement(editPlayer.getDeltaMovement().multiply(1, 0, 1));
			
			if(!zInBounds)
				editPlayer.setDeltaMovement(editPlayer.getDeltaMovement().multiply(1, 1, 0));
			
			return false;
		}
	}
	
	/**
	 * Checks if the players position coordinate value in canMoveAtPosition() is within the bounds of a radius surrounding the center of the location
	 */
	private boolean isWithinRange(double playerPos, double locationCenter, double range)
	{
		return playerPos >= locationCenter - range && playerPos <= locationCenter + range;
	}
	
	public void addEntryLocations(ResourceKey<Level> dimension, SburbConnection c)
	{
		c.addClientEditmodeLocation(dimension, new BlockPos(0, 80, 0), EditmodeLocations.Source.ENTRY);
		c.addClientEditmodeLocation(dimension, new BlockPos(0, 120, 0), EditmodeLocations.Source.ENTRY);
		c.addClientEditmodeLocation(dimension, new BlockPos(0, 160, 0), EditmodeLocations.Source.ENTRY);
		c.addClientEditmodeLocation(dimension, new BlockPos(0, 200, 0), EditmodeLocations.Source.ENTRY);
		c.addClientEditmodeLocation(dimension, new BlockPos(0, 240, 0), EditmodeLocations.Source.ENTRY);
		c.addClientEditmodeLocation(dimension, new BlockPos(0, 280, 0), EditmodeLocations.Source.ENTRY);
		c.addClientEditmodeLocation(dimension, new BlockPos(0, 320, 0), EditmodeLocations.Source.ENTRY);
		c.addClientEditmodeLocation(dimension, new BlockPos(0, 360, 0), EditmodeLocations.Source.ENTRY);
		c.addClientEditmodeLocation(dimension, new BlockPos(0, 400, 0), EditmodeLocations.Source.ENTRY);
	}
}