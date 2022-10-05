package com.mraof.minestuck.entry;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.util.MSNBTUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents a task for updating blocks copied over into the entry.
 * To reduce time, and still reduce lightning and "floating" liquids,
 * this was created to handle such tasks during the ticks right after entry instead of during entry.
 */
public class PostEntryTask
{
	private static final Logger LOGGER = LogManager.getLogger();
	/**
	 * The maximum amount of time (in milliseconds) to spend updating blocks,
	 * before leaving the rest for the next game tick.
	 */
	private static final long MIN_TIME = 20;
	
	private final ResourceKey<Level> dimension;
	private final int x, y, z;
	private final int entrySize;
	private final byte entryType;	//Used if we add more ways for entry to happen
	private int index;
	
	public PostEntryTask(ResourceKey<Level> dimension, int xCoord, int yCoord, int zCoord, int entrySize, byte entryType)
	{
		this.dimension = dimension;
		this.x = xCoord;
		this.y = yCoord;
		this.z = zCoord;
		this.entrySize = entrySize;
		this.entryType = entryType;
		this.index = 0;
	}
	
	public PostEntryTask(CompoundTag nbt)
	{
		this(MSNBTUtil.tryReadDimensionType(nbt, "dimension"), nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"), nbt.getInt("entrySize"), nbt.getByte("entryType"));
		this.index = nbt.getInt("index");
		if(dimension == null)
			LOGGER.warn("Unable to load dimension type by name {}!", nbt.getString("dimension"));
	}
	
	public CompoundTag write()
	{
		CompoundTag nbt = new CompoundTag();
		MSNBTUtil.tryWriteDimensionType(nbt, "dimension", dimension);
		nbt.putInt("x", x);
		nbt.putInt("y", y);
		nbt.putInt("z", z);
		nbt.putInt("entrySize", entrySize);
		nbt.putByte("entryType", entryType);
		nbt.putInt("index", index);
		
		return nbt;
	}
	
	public boolean onTick(MinecraftServer server)
	{
		if(isDone())
			return false;
		
		ServerLevel world = dimension != null ? server.getLevel(dimension) : null;
		
		if(world == null)
		{
			LOGGER.error("Couldn't find world for dimension {} when performing post entry preparations! Cancelling task.", dimension);
			setDone();
			return true;
		}
		
		int preIndex = index;
		main:
		{
			if(entryType == 0)
			{
				long time = System.currentTimeMillis() + MIN_TIME;
				int i = 0;
				for(int blockX = x - entrySize; blockX <= x + entrySize; blockX++)
				{
					int zWidth = (int) Math.sqrt(entrySize * entrySize - (blockX - x) * (blockX - x));
					for(int blockZ = z - zWidth; blockZ <= z + zWidth; blockZ++)
					{
						int height = (int) Math.sqrt(MinestuckConfig.SERVER.artifactRange.get() * MinestuckConfig.SERVER.artifactRange.get() - (((blockX - x) * (blockX - x) + (blockZ - z) * (blockZ - z)) / 2));
						if(blockX == x - entrySize || blockX == x + entrySize || blockZ == z - zWidth || blockZ == z + zWidth)
							for(int blockY = y - height; blockY <= Math.min(128, y + height); blockY++)
								i = updateBlock(new BlockPos(blockX, blockY, blockZ), world, i, true);
						else
						{
							i = updateBlock(new BlockPos(blockX, y - height, blockZ), world, i, true);
							for(int blockY = y - height + 1; blockY < Math.min(128, y + height); blockY++)
								i = updateBlock(new BlockPos(blockX, blockY, blockZ), world, i, false);
							i = updateBlock(new BlockPos(blockX, Math.min(128, y + height), blockZ), world, i, true);
						}
						if(time <= System.currentTimeMillis())
							break main;
					}
				}
			}
			
			LOGGER.info("Completed entry block updates for dimension {}.", dimension.location());
			setDone();
			return true;
		}
		
		LOGGER.debug("Updated {} blocks this tick.", index - preIndex);
		return index != preIndex;
	}
	
	public boolean isDone()
	{
		return index == -1;
	}
	
	private void setDone()
	{
		index = -1;
	}
	
	private int updateBlock(BlockPos pos, ServerLevel world, int i, boolean blockUpdate)
	{
		if(i >= index)
		{
			if(blockUpdate)
				world.updateNeighborsAt(pos, world.getBlockState(pos).getBlock());
			world.getChunkSource().getLightEngine().checkBlock(pos);
			ChunkAccess chunk = world.getChunk(pos);
			BlockState state = chunk.getBlockState(pos);
			int x = pos.getX() & 15, y = pos.getY(), z = pos.getZ() & 15;
			chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.MOTION_BLOCKING).update(x, y, z, state);
			chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES).update(x, y, z, state);
			chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR).update(x, y, z, state);
			chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE).update(x, y, z, state);
			index++;
		}
		return i + 1;
	}
}