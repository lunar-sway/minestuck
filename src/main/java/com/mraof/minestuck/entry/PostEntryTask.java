package com.mraof.minestuck.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents a task for updating blocks copied over into the entry.
 * To reduce time, and still reduce lightning and "floating" liquids,
 * this was created to handle such tasks during the ticks right after entry instead of during entry.
 */
public final class PostEntryTask
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final Codec<PostEntryTask> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Level.RESOURCE_KEY_CODEC.fieldOf("dimension").forGetter(task -> task.dimension),
			Codec.INT.fieldOf("x").forGetter(task -> task.x),
			Codec.INT.fieldOf("y").forGetter(task -> task.y),
			Codec.INT.fieldOf("z").forGetter(task -> task.z),
			Codec.INT.fieldOf("entrySize").forGetter(task -> task.entrySize),
			Codec.INT.fieldOf("index").forGetter(task -> task.index)
	).apply(instance, (dimension, x, y, z, entrySize, index) -> {
		PostEntryTask task = new PostEntryTask(dimension, x, y, z, entrySize);
		task.index = index;
		return task;
	}));
	
	/**
	 * The minimum amount of time (in milliseconds) to spend each game tick
	 * updating blocks post-entry.
	 * Lower it to spend less time each tick, thus spreading out the work over more ticks.
	 */
	private static final long MIN_TIME = 10;
	
	private final ResourceKey<Level> dimension;
	private final int x, y, z;
	private final int entrySize;
	private int index;
	
	public PostEntryTask(ResourceKey<Level> dimension, int xCoord, int yCoord, int zCoord, int entrySize)
	{
		this.dimension = dimension;
		this.x = xCoord;
		this.y = yCoord;
		this.z = zCoord;
		this.entrySize = entrySize;
		this.index = 0;
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
		
		long time = System.currentTimeMillis() + MIN_TIME;
		int i = 0;
		for(BlockPos pos : EntryBlockIterator.get(x, y, z, entrySize))
		{
			if(i >= index)
			{
				updateBlock(pos.immutable(), world);
				index++;
				if(time <= System.currentTimeMillis())
				{
					LOGGER.debug("Updated {} blocks this tick.", index - preIndex);
					return index != preIndex;
				}
			}
			i++;
		}
		
		LOGGER.info("Completed entry block updates for dimension {}.", dimension.location());
		setDone();
		return true;
	}
	
	public boolean isDone()
	{
		return index == -1;
	}
	
	private void setDone()
	{
		index = -1;
	}
	
	private static void updateBlock(BlockPos pos, ServerLevel level)
	{
		BlockState blockState = level.getBlockState(pos);
		level.getChunkSource().getLightEngine().checkBlock(pos);
		level.sendBlockUpdated(pos, blockState, blockState, 0);
		level.updateNeighborsAt(pos, blockState.getBlock());
	}
}
