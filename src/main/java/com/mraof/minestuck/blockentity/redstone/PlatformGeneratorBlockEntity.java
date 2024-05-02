package com.mraof.minestuck.blockentity.redstone;

import com.mraof.minestuck.block.BlockUtil;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.redstone.PlatformBlock;
import com.mraof.minestuck.block.redstone.PlatformGeneratorBlock;
import com.mraof.minestuck.block.redstone.PlatformReceptacleBlock;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PlatformGeneratorBlockEntity extends BlockEntity
{
	private int tickCycle;
	private int platformLength;
	
	public PlatformGeneratorBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.PLATFORM_GENERATOR.get(), pos, state);
	}
	
	public static void serverTick(Level level, BlockPos pos, BlockState state, PlatformGeneratorBlockEntity blockEntity)
	{
		if(!level.isAreaLoaded(pos, 1))
			return;
		
		if(blockEntity.tickCycle >= 10)
		{
			blockEntity.sendUpdate();
			blockEntity.tickCycle = 0;
		}
		
		blockEntity.tickCycle++;
	}
	
	private void sendUpdate()
	{
		if(level != null)
		{
			int powerIn = getBlockState().getValue(PlatformGeneratorBlock.POWER);
			platformLength = powerIn;
			
			if(powerIn > 0)
			{
				for(int blockIterate = 1; blockIterate < platformLength + 1; blockIterate++)
				{
					BlockPos iteratePos = new BlockPos(getBlockPos().relative(getBlockState().getValue(PlatformGeneratorBlock.FACING), blockIterate));
					if(!level.isAreaLoaded(getBlockPos(), blockIterate) || level.isOutsideBuildHeight(iteratePos)) //allows platform blocks to be placed up until it runs out of bounds)
					{
						break;
					}
					
					BlockState iterateBlockState = level.getBlockState(iteratePos);
					
					if(iterateBlockState.getBlock() instanceof PlatformReceptacleBlock)
					{
						if(iterateBlockState.getValue(PlatformReceptacleBlock.ABSORBING))
							break;
					} else if(iterateBlockState.is(MSTags.Blocks.PLATFORM_ABSORBING))
					{
						break;
					} else if(BlockUtil.isReplaceable(iterateBlockState))
					{
						if(!iterateBlockState.isAir())
							level.destroyBlock(iteratePos, true);
						generatePlatform(level, iteratePos, blockIterate);
					} else if(shouldReplaceExistingPlatformBlock(iteratePos, blockIterate))
					{
						generatePlatform(level, iteratePos, blockIterate);
					}
				}
			} else
			{
				for(int blockIterate = 1; blockIterate < 16; blockIterate++)
				{
					BlockPos iteratePos = new BlockPos(getBlockPos().relative(getBlockState().getValue(PlatformGeneratorBlock.FACING), blockIterate));
					
					if(!level.isAreaLoaded(getBlockPos(), blockIterate) || level.isOutsideBuildHeight(iteratePos.getY()))
						break;
					
					BlockState iterateBlockState = level.getBlockState(iteratePos);
					
					if(iterateBlockState.getBlock() instanceof PlatformBlock)
						PlatformBlock.updateSurvival(iterateBlockState, level, iteratePos);
				}
			}
		}
	}
	
	private boolean shouldReplaceExistingPlatformBlock(BlockPos pos, int loopIteration)
	{
		if(level != null)
		{
			BlockState state = level.getBlockState(pos);
			if(state.getBlock() instanceof PlatformBlock)
			{
				PlatformBlock.updateSurvival(state, level, pos);
				state = level.getBlockState(pos);
				return !(state.getBlock() instanceof PlatformBlock);
				
				//if the platform generator is now generating invisible platform blocks, it will replace if it also has the same facing and generation distance values. If not generation invisible platform blocks it will replace only invisible platform blocks
			}
		}
		
		return false;
	}
	
	private void generatePlatform(Level level, BlockPos pos, int loopIteration)
	{
		level.setBlockAndUpdate(pos, MSBlocks.PLATFORM_BLOCK.get().defaultBlockState()
				.setValue(PlatformBlock.INVISIBLE, getBlockState().getValue(PlatformGeneratorBlock.INVISIBLE_MODE))
				.setValue(PlatformBlock.FACING, getBlockState().getValue(PlatformGeneratorBlock.FACING))
				.setValue(PlatformBlock.GENERATOR_DISTANCE, loopIteration));
	}
	
	@Override
	public void load(CompoundTag compound)
	{
		super.load(compound);
		
		tickCycle = compound.getInt("tickCycle");
		platformLength = compound.getInt("platformLength");
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		
		compound.putInt("tickCycle", tickCycle);
		compound.putInt("platformLength", platformLength);
	}
	
	@Override
	public CompoundTag getUpdateTag()
	{
		return this.saveWithoutMetadata();
	}
	
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}
}