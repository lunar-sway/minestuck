package com.mraof.minestuck.entry;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public final class BlockCopier
{
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Set<CopyStep> copySteps = new HashSet<>();
	
	public interface CopyStep
	{
		void copyOver(ServerLevel oldWorld, BlockPos oldPos, ServerLevel newWorld, BlockPos newPos, BlockState state, @Nullable BlockEntity oldBE, @Nullable BlockEntity newBE);
	}
	
	/**
	 * Not thread-safe. Make sure to only call this on the main thread
	 */
	public static void addStep(CopyStep processing)
	{
		copySteps.add(processing);
	}
	
	static void copyBlock(ServerLevel level, LevelChunk chunkFrom, BlockPos source, BlockState block, ChunkAccess chunkTo, BlockPos dest)
	{
		if(chunkTo.getBlockState(dest).is(Blocks.BEDROCK))
			return;
		
		if(block.isAir())
			level.setBlock(dest, block, 0);
		else
			copyBlockDirect(level, chunkFrom, chunkTo, source.getX(), source.getY(), source.getZ(), dest.getX(), dest.getY(), dest.getZ());
		
		BlockEntity blockEntity = chunkFrom.getBlockEntity(source, LevelChunk.EntityCreationType.CHECK);
		BlockEntity newBE = null;
		if(blockEntity != null)
		{
			CompoundTag nbt = blockEntity.saveWithId();
			nbt.putInt("x", dest.getX());
			nbt.putInt("y", dest.getY());
			nbt.putInt("z", dest.getZ());
			newBE = BlockEntity.loadStatic(dest, block, nbt);
			if(newBE != null)
				level.setBlockEntity(newBE);
			else
				LOGGER.warn("Unable to create a new block entity {} when teleporting blocks to the medium!", ForgeRegistries.BLOCK_ENTITY_TYPES.getKey(blockEntity.getType()));
			
		}
		
		for(CopyStep processing : copySteps)
		{
			processing.copyOver((ServerLevel) chunkFrom.getLevel(), source, level, dest, block, blockEntity, newBE);
		}
	}
	
	private static void copyBlockDirect(LevelAccessor levelAccessor, ChunkAccess cSrc, ChunkAccess cDst, int xSrc, int ySrc, int zSrc, int xDst, int yDst, int zDst)
	{
		BlockPos dest = new BlockPos(xDst, yDst, zDst);
		LevelChunkSection blockStorageSrc = getBlockStorage(cSrc, ySrc);
		LevelChunkSection blockStorageDst = getBlockStorage(cDst, yDst);
		int y = yDst;
		xSrc &= 15; ySrc &= 15; zSrc &= 15; xDst &= 15; yDst &= 15; zDst &= 15;
		
		boolean isEmpty = blockStorageDst.hasOnlyAir();
		BlockState state = blockStorageSrc.getBlockState(xSrc, ySrc, zSrc);
		blockStorageDst.setBlockState(xDst, yDst, zDst, state);
		if(isEmpty != blockStorageDst.hasOnlyAir())
			levelAccessor.getChunkSource().getLightEngine().updateSectionStatus(dest, blockStorageDst.hasOnlyAir());    //I assume this adds or removes a light storage section here depending on if it is needed (because a section with just air doesn't have to be regarded)
		
		cDst.getOrCreateHeightmapUnprimed(Heightmap.Types.MOTION_BLOCKING).update(xDst, y, zDst, state);
		cDst.getOrCreateHeightmapUnprimed(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES).update(xDst, y, zDst, state);
		cDst.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR).update(xDst, y, zDst, state);
		cDst.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE).update(xDst, y, zDst, state);
	}
	
	private static LevelChunkSection getBlockStorage(ChunkAccess c, int y)
	{
		return c.getSection(c.getSectionIndex(y));
	}
}
