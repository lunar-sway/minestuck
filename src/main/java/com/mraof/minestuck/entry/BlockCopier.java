package com.mraof.minestuck.entry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
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
	
	static void copyBlock(LevelChunk sourceChunk, BlockPos source, BlockState block, LevelChunk targetChunk, BlockPos targetPos)
	{
		targetChunk.setBlockState(targetPos, block, true);
		
		BlockEntity blockEntity = sourceChunk.getBlockEntity(source, LevelChunk.EntityCreationType.CHECK);
		BlockEntity newBE = null;
		if(blockEntity != null)
			newBE = cloneBEToPos(block, targetChunk, targetPos, blockEntity);
		
		for(CopyStep processing : copySteps)
		{
			processing.copyOver((ServerLevel) sourceChunk.getLevel(), source, (ServerLevel) targetChunk.getLevel(), targetPos, block, blockEntity, newBE);
		}
	}
	
	@Nullable
	private static BlockEntity cloneBEToPos(BlockState block, LevelChunk targetChunk, BlockPos targetPos, BlockEntity blockEntity)
	{
		CompoundTag nbt = blockEntity.saveWithId();
		nbt.putInt("x", targetPos.getX());
		nbt.putInt("y", targetPos.getY());
		nbt.putInt("z", targetPos.getZ());
		BlockEntity newBE = BlockEntity.loadStatic(targetPos, block, nbt);
		if(newBE != null)
			targetChunk.addAndRegisterBlockEntity(newBE);
		else
			LOGGER.warn("Unable to create a new block entity {} when teleporting blocks to the medium!", BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(blockEntity.getType()));
		return newBE;
	}
}
