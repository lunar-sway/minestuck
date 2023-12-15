package com.mraof.minestuck.computer.editmode;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.data.EditmodeLocationsPacket;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.player.PlayerSavedData;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.world.MSDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Stream;

/**
 * Contains a list of block positions in a radius around which an editmode player can move freely.
 * An instance is stored in SburbConnection for the SBURB client of the connection.
 */
public final class EditmodeLocations
{
	//TODO prevent going to non Medium dimensions post Entry, delete maps to the Overworld
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final int VALIDATION_RADIUS = 25;
	public static final int ENTRY_RANGE = 30;
	
	private final Multimap<ResourceKey<Level>, BlockPos> computers = ArrayListMultimap.create();
	@Nullable
	private ResourceKey<Level> land;
	
	private static final List<BlockPos> ENTRY_POSITIONS = List.of(
			new BlockPos(0, 80, 0),
			new BlockPos(0, 120, 0),
			new BlockPos(0, 160, 0),
			new BlockPos(0, 200, 0),
			new BlockPos(0, 240, 0),
			new BlockPos(0, 280, 0),
			new BlockPos(0, 320, 0),
			new BlockPos(0, 360, 0),
			new BlockPos(0, 400, 0));
	
	public record Area(BlockPos center, int range)
	{}
	
	public List<BlockPos> getSortedPositions(@Nonnull ResourceKey<Level> level)
	{
		Stream<BlockPos> entryLocations = level == this.land ? ENTRY_POSITIONS.stream() : Stream.empty();
		
		return Stream.concat(computers.get(level).stream(), entryLocations).toList();
	}
	
	public boolean isSource(GlobalPos pos)
	{
		return computers.get(pos.dimension()).contains(pos.pos())
				|| pos.dimension() == this.land && ENTRY_POSITIONS.contains(pos.pos());
	}
	
	public Stream<Area> getAreasFor(@Nullable ResourceKey<Level> level, int defaultRange)
	{
		Stream<BlockPos> entryLocations = level == this.land ? ENTRY_POSITIONS.stream() : Stream.empty();
		
		return Stream.concat(
				this.computers.get(level).stream().map(pos -> new Area(pos, defaultRange)),
				entryLocations.map(pos -> new Area(pos, ENTRY_RANGE)));
	}
	
	/**
	 * Checks the editmode players surroundings, then both removes now invalid locations and adds new valid locations.
	 */
	public void validateNearbySources(ServerPlayer editPlayer, SburbConnection connection)
	{
		Level editLevel = editPlayer.level();
		ResourceKey<Level> editDimension = editLevel.dimension();
		
		if(editLevel.isClientSide)
			return;
		
		//security for pre EditmodeLocations update worlds
		if(connection.getClientDimension() != null && this.land == null)
			this.addEntryLocations(editPlayer.server, connection.getClientIdentifier(), connection.getClientDimension());
		
		if(!computers.containsKey(editDimension))
			return;
		
		Collection<BlockPos> allLevelPairs = computers.get(editDimension);
		
		for(BlockPos blockIterate : BlockPos.betweenClosed(editPlayer.blockPosition().offset(VALIDATION_RADIUS, VALIDATION_RADIUS, VALIDATION_RADIUS), editPlayer.blockPosition().offset(-VALIDATION_RADIUS, -VALIDATION_RADIUS, -VALIDATION_RADIUS)))
		{
			checkBlockPosValidation(editPlayer, connection.getClientIdentifier(), editLevel, editDimension, allLevelPairs, blockIterate);
		}
	}
	
	/**
	 * Gets the closest location in the dimension or returns null if there is none. May require validation.
	 */
	@SuppressWarnings("resource")
	public BlockPos getClosestPosInDimension(Player player)
	{
		BlockPos playerPos = player.blockPosition();
		ResourceKey<Level> editLevel = player.level().dimension();
		BlockPos pickedPos = null;
		double pickedPosDistance = Integer.MAX_VALUE;
		
		for(BlockPos iteratedPos : getSortedPositions(editLevel))
		{
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
	
	public CompoundTag write()
	{
		CompoundTag compoundTag = new CompoundTag();
		if(this.land != null)
			Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, this.land).resultOrPartial(LOGGER::error)
					.ifPresent(tag -> compoundTag.put("land", tag));
		
		ListTag listTag = new ListTag();
		
		for(Map.Entry<ResourceKey<Level>, BlockPos> entry : computers.entries())
		{
			CompoundTag nbt = new CompoundTag();
			
			Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, entry.getKey()).resultOrPartial(LOGGER::error)
					.ifPresent(tag -> nbt.put("dim", tag));
			
			BlockPos pos = entry.getValue();
			
			nbt.putInt("x", pos.getX());
			nbt.putInt("y", pos.getY());
			nbt.putInt("z", pos.getZ());
			
			listTag.add(nbt);
		}
		
		compoundTag.put("computers", listTag);
		return compoundTag;
	}
	
	public static EditmodeLocations read(CompoundTag compoundTag)
	{
		EditmodeLocations locations = new EditmodeLocations();
		if(compoundTag.contains("land"))
			locations.land = Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, compoundTag.get("land")).resultOrPartial(LOGGER::error).orElse(null);
		
		ListTag locationsTag = compoundTag.getList("computers", Tag.TAG_COMPOUND);
		
		for(int i = 0; i < locationsTag.size(); i++)
		{
			CompoundTag nbt = locationsTag.getCompound(i);
			
			ResourceKey<Level> dimension = Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, nbt.get("dim")).resultOrPartial(LOGGER::error).orElse(null);
			
			int posX = nbt.getInt("x");
			int posY = nbt.getInt("y");
			int posZ = nbt.getInt("z");
			
			locations.computers.put(dimension, new BlockPos(posX, posY, posZ));
		}
		
		return locations;
	}
	
	private void checkBlockPosValidation(ServerPlayer editPlayer, PlayerIdentifier owner, Level editLevel, ResourceKey<Level> editDimension, Collection<BlockPos> allLevelPairs, BlockPos blockIterate)
	{
		//if locations contains the iterated block pos and the entry is no longer valid, remove it.
		if(allLevelPairs.contains(blockIterate))
		{
			if(!isValidBlockSource(editLevel, owner, blockIterate))
			{
				removeBlockSource(editPlayer.server, owner, editDimension, blockIterate);
				
				int range = MSDimensions.isLandDimension(editPlayer.server, editPlayer.level().dimension()) ? MinestuckConfig.SERVER.landEditRange.get() : MinestuckConfig.SERVER.overworldEditRange.get();
				
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
		}
	}
	
	private static boolean isValidBlockSource(Level level, PlayerIdentifier owner, BlockPos pos)
	{
		if(level.getBlockEntity(pos) instanceof ComputerBlockEntity computerBlockEntity)
		{
			return !computerBlockEntity.isBroken() && computerBlockEntity.hasProgram(0) && computerBlockEntity.owner.equals(owner);
		}
		
		return false;
	}
	
	@SuppressWarnings("resource")
	public boolean isOutsideBounds(Player editPlayer, int defaultRange)
	{
		return getAreasFor(editPlayer.level().dimension(), defaultRange)
				.allMatch(area -> isOutsideBounds(editPlayer, area));
	}
	
	@SuppressWarnings("resource")
	public void limitMovement(Player editPlayer, int defaultRange)
	{
		Optional<Area> closestSource = findRelativelyClosest(editPlayer, getAreasFor(editPlayer.level().dimension(), defaultRange));
		
		if(closestSource.isEmpty())
			return;
		
		if(isOutsideBounds(editPlayer, closestSource.get()))
			limitMovement(editPlayer, closestSource.get());
	}
	
	private static Optional<Area> findRelativelyClosest(Player player, Stream<Area> areas)
	{
		return areas.min(Comparator.comparingDouble(area -> relativeDistance(player, area)));
	}
	
	private static boolean isOutsideBounds(Player player, Area area)
	{
		return relativeDistance(player, area) > 1;
	}
	
	/**
	 * Calculates a relative distance between the player and the given source, with values less than 1 being inside range, and values larger than 1 being outside range.
	 */
	private static double relativeDistance(Player player, Area area)
	{
		Vec3 distance = player.position().subtract(Vec3.atLowerCornerOf(area.center()));
		return Math.max(
				Math.abs(distance.x()),
				Math.max(Math.abs(distance.y()),
						Math.abs(distance.z()))) / area.range();
	}
	
	private static void limitMovement(Player player, Area area)
	{
		for(Direction direction : Direction.values())
		{
			Vec3 directionNormal = Vec3.atLowerCornerOf(direction.getNormal());
			Vec3 distance = player.position().subtract(Vec3.atLowerCornerOf(area.center()));
			double distanceOverBorder = distance.dot(directionNormal) - area.range();
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
	
	public void addEntryLocations(MinecraftServer mcServer, PlayerIdentifier owner, ResourceKey<Level> dimension)
	{
		this.land = dimension;
		sendLocationsToEditor(mcServer, owner, this);
	}
	
	public static void addBlockSource(MinecraftServer mcServer, PlayerIdentifier owner, ResourceKey<Level> level, BlockPos pos)
	{
		var locations = PlayerSavedData.getData(owner, mcServer).editmodeLocations;
		
		if(locations.computers.containsEntry(level, pos))
			return;
		
		//TODO consider validating the pos and source
		locations.computers.put(level, pos.immutable());
		
		sendLocationsToEditor(mcServer, owner, locations);
	}
	
	public static void removeBlockSource(MinecraftServer mcServer, PlayerIdentifier owner, ResourceKey<Level> level, BlockPos pos)
	{
		var locations = PlayerSavedData.getData(owner, mcServer).editmodeLocations;
		locations.computers.remove(level, pos);
		
		sendLocationsToEditor(mcServer, owner, locations);
	}
	
	private static void sendLocationsToEditor(MinecraftServer mcServer, PlayerIdentifier owner, EditmodeLocations locations)
	{
		SkaianetHandler.get(mcServer).getPrimaryConnection(owner, true).ifPresent(connection -> {
			EditData editData = ServerEditHandler.getData(mcServer, connection);
			if(editData != null)
				MSPacketHandler.sendToPlayer(new EditmodeLocationsPacket(locations), editData.getEditor());
		});
	}
}