package com.mraof.minestuck.computer.editmode;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.network.editmode.EditmodeLocationsPacket;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.SburbPlayerData;
import com.mraof.minestuck.util.MSAttachments;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Contains a list of block positions in a radius around which an editmode player can move freely.
 */
public final class EditmodeLocations implements INBTSerializable<CompoundTag>
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final String REMOVED_LOCATION_MESSAGE = "minestuck.editmode.removed_location";
	
	private static final int ENTRY_RANGE = 30;
	
	private final Multimap<ResourceKey<Level>, BlockPos> computers = ArrayListMultimap.create();
	
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
	
	@Override
	public CompoundTag serializeNBT()
	{
		CompoundTag compoundTag = new CompoundTag();
		
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
	
	@Override
	public void deserializeNBT(CompoundTag compoundTag)
	{
		ListTag locationsTag = compoundTag.getList("computers", Tag.TAG_COMPOUND);
		
		for(int i = 0; i < locationsTag.size(); i++)
		{
			CompoundTag nbt = locationsTag.getCompound(i);
			
			ResourceKey<Level> dimension = Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, nbt.get("dim")).resultOrPartial(LOGGER::error).orElse(null);
			
			int posX = nbt.getInt("x");
			int posY = nbt.getInt("y");
			int posZ = nbt.getInt("z");
			
			this.computers.put(dimension, new BlockPos(posX, posY, posZ));
		}
	}
	
	public static EditmodeLocations read(CompoundTag compoundTag)
	{
		EditmodeLocations locations = new EditmodeLocations();
		locations.deserializeNBT(compoundTag);
		return locations;
	}
	
	public List<BlockPos> getSortedPositions(@Nonnull ResourceKey<Level> level, @Nullable ResourceKey<Level> land)
	{
		Stream<BlockPos> entryLocations = level == land ? ENTRY_POSITIONS.stream() : Stream.empty();
		
		return Stream.concat(computers.get(level).stream(), entryLocations).toList();
	}
	
	public static boolean checkIsValidSourcePos(EditData data, ResourceKey<Level> level, BlockPos pos)
	{
		PlayerIdentifier owner = data.getTarget();
		ResourceKey<Level> land = data.sburbData().getLandDimensionIfEntered();
		EditmodeLocations locations = PlayerData.get(owner, data.getEditor().server).getData(MSAttachments.EDITMODE_LOCATIONS);
		
		if(level == land && ENTRY_POSITIONS.contains(pos))
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
	
	public void validateClosestSource(ServerPlayer editPlayer, SburbPlayerData targetData)
	{
		Level editLevel = editPlayer.level();
		ResourceKey<Level> editDimension = editLevel.dimension();
		ResourceKey<Level> land = targetData.getLandDimensionIfEntered();
		
		this.findRelativelyClosestArea(editPlayer, land).map(Area::center).ifPresent(pos -> {
			if(isComputerSourceInvalidFor(editLevel, pos, targetData.playerId()))
				removeBlockSource(editPlayer.server, targetData.playerId(), editDimension, pos);
		});
	}
	
	public static void onEntry(MinecraftServer mcServer, PlayerIdentifier owner)
	{
		sendLocationsToEditor(mcServer, owner);
	}
	
	public static void addBlockSourceIfValid(ComputerBlockEntity computer)
	{
		if(!(computer.getLevel() instanceof ServerLevel level))
			return;
		if(computer.getOwner() == null)
			return;
		
		var locations = PlayerData.get(computer.getOwner(), level).getData(MSAttachments.EDITMODE_LOCATIONS);
		
		if(locations.computers.containsEntry(level.dimension(), computer.getBlockPos()))
			return;
		if(isComputerSourceInvalid(computer))
			return;
		
		locations.computers.put(level.dimension(), computer.getBlockPos());
		sendLocationsToEditor(level.getServer(), computer.getOwner());
	}
	
	public static void removeBlockSource(MinecraftServer mcServer, PlayerIdentifier owner, ResourceKey<Level> level, BlockPos pos)
	{
		var locations = PlayerData.get(owner, mcServer).getData(MSAttachments.EDITMODE_LOCATIONS);
		
		boolean wasRemoved = locations.computers.remove(level, pos);
		if(wasRemoved)
		{
			sendLocationsToEditor(mcServer, owner);
			locations.updatePlayerNearRemovedComputerSource(mcServer, owner, level, pos);
		}
	}
	
	public void limitMovement(Player editPlayer, @Nullable ResourceKey<Level> land)
	{
		Optional<Area> closestSource = this.findRelativelyClosestArea(editPlayer, land);
		
		if(closestSource.isEmpty())
			return;
		
		if(isOutsideBounds(editPlayer, closestSource.get()))
			limitMovement(editPlayer, closestSource.get());
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
	
	/**
	 * Calculates a relative distance between the player and the given source,
	 * with values less than 1 being inside range, and values larger than 1 being outside range.
	 */
	private static double relativeDistance(Player player, Area area)
	{
		Vec3 distance = player.position().subtract(Vec3.atLowerCornerOf(area.center()));
		return Math.max(
				Math.abs(distance.x()),
				Math.max(Math.abs(distance.y()),
						Math.abs(distance.z()))) / area.range();
	}
	
	private Stream<Area> getAreasFor(@Nonnull ResourceKey<Level> level, @Nullable ResourceKey<Level> land)
	{
		int computerRange = getComputerRange(level == land);
		Stream<BlockPos> entryLocations = level == land ? ENTRY_POSITIONS.stream() : Stream.empty();
		
		return Stream.concat(
				this.computers.get(level).stream().map(pos -> new Area(pos, computerRange)),
				entryLocations.map(pos -> new Area(pos, ENTRY_RANGE)));
	}
	
	private static int getComputerRange(boolean isInLand)
	{
		return isInLand ? MinestuckConfig.SERVER.landEditRange.get() : MinestuckConfig.SERVER.overworldEditRange.get();
	}
	
	@SuppressWarnings("resource")
	private boolean isInsideBounds(Player editPlayer, @Nullable ResourceKey<Level> land)
	{
		return !getAreasFor(editPlayer.level().dimension(), land)
				.allMatch(area -> isOutsideBounds(editPlayer, area));
	}
	
	@SuppressWarnings("resource")
	private Optional<Area> findRelativelyClosestArea(Player player, @Nullable ResourceKey<Level> land)
	{
		return getAreasFor(player.level().dimension(), land).min(Comparator.comparingDouble(area -> relativeDistance(player, area)));
	}
	
	private static boolean isOutsideBounds(Player player, Area area)
	{
		return relativeDistance(player, area) > 1;
	}
	
	@SuppressWarnings("resource")
	private void updatePlayerNearRemovedComputerSource(MinecraftServer mcServer, PlayerIdentifier owner, ResourceKey<Level> level, BlockPos computerPos)
	{
		EditData data = ServerEditHandler.getData(mcServer, owner);
		if(data == null)
			return;
		Player editPlayer = data.getEditor();
		ResourceKey<Level> land = data.sburbData().getLandDimensionIfEntered();
		
		if(editPlayer.level().dimension() != level || isInsideBounds(editPlayer, land))
			return;
		
		Optional<Area> newAreaOptional = this.findRelativelyClosestArea(editPlayer, land);
		if(newAreaOptional.isEmpty())
			return;
		Area newClosestArea = newAreaOptional.get();
		Area removedArea = new Area(computerPos, getComputerRange(editPlayer.level().dimension() == land));
		
		if(relativeDistance(editPlayer, removedArea) > relativeDistance(editPlayer, newClosestArea))
			return;
		
		editPlayer.sendSystemMessage(Component.translatable(REMOVED_LOCATION_MESSAGE));
		BlockPos nextClosestLocationPos = newClosestArea.center();
		editPlayer.teleportTo(nextClosestLocationPos.getX() + 0.5D, nextClosestLocationPos.getY() + 1.0D, nextClosestLocationPos.getZ() + 0.5D);
	}
	
	private static void sendLocationsToEditor(MinecraftServer mcServer, PlayerIdentifier owner)
	{
		EditData editData = ServerEditHandler.getData(mcServer, owner);
		if(editData != null)
			EditmodeLocationsPacket.send(editData);
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
}
