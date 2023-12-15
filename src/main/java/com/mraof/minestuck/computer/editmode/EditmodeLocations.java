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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
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
 */
public final class EditmodeLocations
{
	//TODO prevent going to non Medium dimensions post Entry, delete maps to the Overworld
	
	private static final Logger LOGGER = LogManager.getLogger();
	
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
	
	public static boolean checkIsValidSourcePos(EditData data, ResourceKey<Level> level, BlockPos pos)
	{
		PlayerIdentifier owner = data.getConnection().getClientIdentifier();
		EditmodeLocations locations = PlayerSavedData.getData(owner, data.getEditor().server).editmodeLocations;
		
		if(level == locations.land && ENTRY_POSITIONS.contains(pos))
			return true;
		
		if(!locations.computers.get(level).contains(pos))
			return false;
		
		if(isComputerSourceInvalidFor(data.getEditor().level(), pos, owner))
		{
			removeBlockSource(data.getEditor().server, owner, level, pos);
			return false;
		}
		
		return true;
	}
	
	public Stream<Area> getAreasFor(@Nonnull ResourceKey<Level> level)
	{
		int computerRange = level == this.land ? MinestuckConfig.SERVER.landEditRange.get() : MinestuckConfig.SERVER.overworldEditRange.get();
		Stream<BlockPos> entryLocations = level == this.land ? ENTRY_POSITIONS.stream() : Stream.empty();
		
		return Stream.concat(
				this.computers.get(level).stream().map(pos -> new Area(pos, computerRange)),
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
		
		this.findRelativelyClosestArea(editPlayer).map(Area::center).ifPresent(pos -> {
			if(isComputerSourceInvalidFor(editLevel, pos, connection.getClientIdentifier()))
			{
				removeBlockSource(editPlayer.server, connection.getClientIdentifier(), editDimension, pos);
				
				//TODO consider adding message indicating what happened
				if(isOutsideBounds(editPlayer))
				{
					//teleport player to nearest valid location
					this.findRelativelyClosestArea(editPlayer).map(Area::center).ifPresent(nextClosestLocationPos -> {
						editPlayer.teleportTo(nextClosestLocationPos.getX() + 0.5D, nextClosestLocationPos.getY() + 1.0D, nextClosestLocationPos.getZ() + 0.5D);
					});
				}
			}
		});
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
	
	private static boolean isComputerSourceInvalidFor(Level level, BlockPos pos, PlayerIdentifier owner)
	{
		if(level.getBlockEntity(pos) instanceof ComputerBlockEntity computerBlockEntity)
			return isComputerSourceInvalid(computerBlockEntity) || !computerBlockEntity.owner.equals(owner);
		
		return true;
	}
	
	private static boolean isComputerSourceInvalid(ComputerBlockEntity computer)
	{
		return computer.isBroken() || !computer.hasProgram(0);
	}
	
	@SuppressWarnings("resource")
	public boolean isOutsideBounds(Player editPlayer)
	{
		return getAreasFor(editPlayer.level().dimension())
				.allMatch(area -> isOutsideBounds(editPlayer, area));
	}
	
	public void limitMovement(Player editPlayer)
	{
		Optional<Area> closestSource = this.findRelativelyClosestArea(editPlayer);
		
		if(closestSource.isEmpty())
			return;
		
		if(isOutsideBounds(editPlayer, closestSource.get()))
			limitMovement(editPlayer, closestSource.get());
	}
	
	@SuppressWarnings("resource")
	private Optional<Area> findRelativelyClosestArea(Player player)
	{
		return getAreasFor(player.level().dimension()).min(Comparator.comparingDouble(area -> relativeDistance(player, area)));
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
		}
	}
	
	public void addEntryLocations(MinecraftServer mcServer, PlayerIdentifier owner, ResourceKey<Level> dimension)
	{
		this.land = dimension;
		sendLocationsToEditor(mcServer, owner, this);
	}
	
	public static void addBlockSourceIfValid(ComputerBlockEntity computer)
	{
		if(!(computer.getLevel() instanceof ServerLevel level))
			return;
		if(computer.getOwner() == null)
			return;
		
		var locations = PlayerSavedData.getData(computer.getOwner(), level).editmodeLocations;
		
		if(locations.computers.containsEntry(level.dimension(), computer.getBlockPos()))
			return;
		if(isComputerSourceInvalid(computer))
			return;
		
		locations.computers.put(level.dimension(), computer.getBlockPos());
		sendLocationsToEditor(level.getServer(), computer.getOwner(), locations);
	}
	
	public static void removeBlockSource(MinecraftServer mcServer, PlayerIdentifier owner, ResourceKey<Level> level, BlockPos pos)
	{
		var locations = PlayerSavedData.getData(owner, mcServer).editmodeLocations;
		
		boolean wasRemoved = locations.computers.remove(level, pos);
		if(wasRemoved)
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