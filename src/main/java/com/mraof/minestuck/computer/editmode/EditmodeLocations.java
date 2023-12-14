package com.mraof.minestuck.computer.editmode;

import com.google.common.collect.*;
import com.mojang.datafixers.util.Pair;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.world.MSDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
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
		
		Pair<BlockPos, Source> locationPairIn = Pair.of(pos.immutable(), source);
		
		//TODO consider validating the pos and source
		if(!locations.containsEntry(level, locationPairIn))
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
	 * Checks the editmode players surroundings, then both removes now invalid locations and adds new valid locations.
	 */
	public void validateNearbySources(Player editPlayer, SburbConnection connection)
	{
		Level editLevel = editPlayer.level();
		ResourceKey<Level> editDimension = editLevel.dimension();
		
		if(editLevel.isClientSide)
			return;
		
		//security for pre EditmodeLocations update worlds
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
		//if locations contains the iterated block pos and the entry is no longer valid, remove it. Else if locations did not contain the iterated pos and its valid, add it
		if(allLevelPairs.contains(Pair.of(blockIterate, Source.BLOCK)))
		{
			if(!isValidBlockSource(connection, editLevel, blockIterate))
			{
				connection.removeClientEditmodeLocations(editDimension, blockIterate, Source.BLOCK);
				
				int range = MSDimensions.isLandDimension(((ServerPlayer) editPlayer).server, editPlayer.level().dimension()) ? MinestuckConfig.SERVER.landEditRange.get() : MinestuckConfig.SERVER.overworldEditRange.get();
				
				//TODO consider adding message indicating what happened
				if(isOutsideBounds(editPlayer, range))
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
	
	public boolean isOutsideBounds(Player editPlayer, double defaultRange)
	{
		@SuppressWarnings("resource") Collection<Pair<BlockPos, Source>> locations = this.locations.get(editPlayer.level().dimension());
		return locations.stream().allMatch(pair -> isOutsideBounds(editPlayer, defaultRange, pair));
	}
	
	public void limitMovement(Player editPlayer, double defaultRange)
	{
		@SuppressWarnings("resource") Collection<Pair<BlockPos, Source>> locations = this.locations.get(editPlayer.level().dimension());
		Optional<Pair<BlockPos, Source>> closestSource = findRelativelyClosest(editPlayer, locations, defaultRange);
		
		if(closestSource.isEmpty())
			return;
		
		if(isOutsideBounds(editPlayer, defaultRange, closestSource.get()))
			limitMovement(editPlayer, closestSource.get(), defaultRange);
	}
	
	private static Optional<Pair<BlockPos, Source>> findRelativelyClosest(Player player, Collection<Pair<BlockPos, Source>> locations, double defaultRange)
	{
		return locations.stream().min(Comparator.comparingDouble(pair -> relativeDistance(player, defaultRange, pair)));
	}
	
	private static boolean isOutsideBounds(Player player, double defaultRange, Pair<BlockPos, Source> pair)
	{
		return relativeDistance(player, defaultRange, pair) > 1;
	}
	
	/**
	 * Calculates a relative distance between the player and the given source, with values less than 1 being inside range, and values larger than 1 being outside range.
	 */
	private static double relativeDistance(Player player, double defaultRange, Pair<BlockPos, Source> pair)
	{
		double range = pair.getSecond() == Source.ENTRY ? ENTRY_RANGE : defaultRange;
		
		Vec3 distance = player.position().subtract(Vec3.atLowerCornerOf(pair.getFirst()));
		return Math.max(
				Math.abs(distance.x()),
				Math.max(Math.abs(distance.y()),
						Math.abs(distance.z()))) / range;
	}
	
	private static void limitMovement(Player player, Pair<BlockPos, Source> pair, double defaultRange)
	{
		double range = pair.getSecond() == Source.ENTRY ? ENTRY_RANGE : defaultRange;
		
		for(Direction direction : Direction.values())
		{
			Vec3 directionNormal = Vec3.atLowerCornerOf(direction.getNormal());
			Vec3 distance = player.position().subtract(Vec3.atLowerCornerOf(pair.getFirst()));
			double distanceOverBorder = distance.dot(directionNormal) - range;
			if(distanceOverBorder >= 0)
				player.addDeltaMovement(directionNormal.scale(-distanceOverBorder));
//				limitMovementInDirection(player, directionNormal);
		}
	}
	
	/**
	 * If the player has a component of their velocity going in the same direction as the provided vec, remove it.
	 */
	private static void limitMovementInDirection(Player player, Vec3 direction)
	{
		direction = direction.normalize();
		double dotProduct = direction.dot(player.getDeltaMovement());
		if(dotProduct > 0)
			player.addDeltaMovement(direction.scale(-dotProduct));
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