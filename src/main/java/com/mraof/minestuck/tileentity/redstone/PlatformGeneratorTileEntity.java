package com.mraof.minestuck.tileentity.redstone;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.redstone.PlatformBlock;
import com.mraof.minestuck.block.redstone.PlatformGeneratorBlock;
import com.mraof.minestuck.block.redstone.PlatformReceptacleBlock;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlatformGeneratorTileEntity extends TileEntity implements ITickableTileEntity
{
	private int tickCycle;
	private int platformLength;
	
	public PlatformGeneratorTileEntity()
	{
		super(MSTileEntityTypes.PLATFORM_GENERATOR.get());
	}
	
	@Override
	public void tick()
	{
		if(level == null || !level.isAreaLoaded(getBlockPos(), 1))
			return;
		
		if(tickCycle >= 10)
		{
			sendUpdate();
			tickCycle = 0;
		}
		
		tickCycle++;
	}
	
	private void sendUpdate()
	{
		if(level != null && !level.isClientSide)
		{
			int powerIn = getBlockState().getValue(PlatformGeneratorBlock.POWER);
			platformLength = powerIn;
			
			if(powerIn > 0)
			{
				for(int blockIterate = 1; blockIterate < platformLength + 1; blockIterate++)
				{
					BlockPos iteratePos = new BlockPos(getBlockPos().relative(getBlockState().getValue(PlatformGeneratorBlock.FACING), blockIterate));
					if(!level.isAreaLoaded(getBlockPos(), blockIterate) || World.isOutsideBuildHeight(iteratePos.getY())) //allows platform blocks to be placed up until it runs out of bounds)
						break;
					
					BlockState iterateBlockState = level.getBlockState(iteratePos);
					
					if(iterateBlockState.getBlock() instanceof PlatformReceptacleBlock)
					{
						if(iterateBlockState.getValue(PlatformReceptacleBlock.ABSORBING))
							break;
					} else if(MSTags.Blocks.PLATFORM_ABSORBING.contains(iterateBlockState.getBlock()))
					{
						break;
					} else if(isReplaceable(iterateBlockState))
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
					
					if(!level.isAreaLoaded(getBlockPos(), blockIterate) || World.isOutsideBuildHeight(iteratePos.getY()))
						break;
					
					BlockState iterateBlockState = level.getBlockState(iteratePos);
					
					if(iterateBlockState.getBlock() instanceof PlatformBlock)
						PlatformBlock.updateSurvival(iterateBlockState, level, iteratePos);
				}
			}
		}
	}
	
	private static boolean isReplaceable(BlockState state)
	{
		Material material = state.getMaterial();
		return state.isAir() || state.is(BlockTags.FIRE) || material.isLiquid() || material.isReplaceable();
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
	
	private void generatePlatform(World world, BlockPos pos, int loopIteration)
	{
		world.setBlockAndUpdate(pos, MSBlocks.PLATFORM_BLOCK.defaultBlockState()
				.setValue(PlatformGeneratorBlock.INVISIBLE_MODE, getBlockState().getValue(PlatformGeneratorBlock.INVISIBLE_MODE))
				.setValue(PlatformBlock.FACING, getBlockState().getValue(PlatformGeneratorBlock.FACING))
				.setValue(PlatformBlock.GENERATOR_DISTANCE, loopIteration));
	}
	
	@Override
	public void load(BlockState state, CompoundNBT compound)
	{
		super.load(state, compound);
		
		tickCycle = compound.getInt("tickCycle");
		platformLength = compound.getInt("platformLength");
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound)
	{
		super.save(compound);
		
		compound.putInt("tickCycle", tickCycle);
		compound.putInt("platformLength", platformLength);
		
		return compound;
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		return this.save(new CompoundNBT());
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(getBlockPos(), 2, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		this.load(getBlockState(), pkt.getTag());
	}
}