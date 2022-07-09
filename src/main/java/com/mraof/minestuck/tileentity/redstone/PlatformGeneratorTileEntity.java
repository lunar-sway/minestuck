package com.mraof.minestuck.tileentity.redstone;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.redstone.PlatformBlock;
import com.mraof.minestuck.block.redstone.PlatformGeneratorBlock;
import com.mraof.minestuck.block.redstone.PlatformReceptacleBlock;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

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
					{
						break;
					}
					
					BlockState iterateBlockState = level.getBlockState(iteratePos);
					
					if(iterateBlockState.getBlock() instanceof PlatformReceptacleBlock)
					{
						if(iterateBlockState.getValue(PlatformReceptacleBlock.ABSORBING))
						{
							break;
						}
					} else if(iterateBlockState.getMaterial().isLiquid() || iterateBlockState.isAir()/* || iterateBlockState.getBlock() == MSBlocks.PLATFORM_BLOCK && level.getPendingBlockTicks().isTickScheduled(iteratePos, MSBlocks.PLATFORM_BLOCK))*/)
					{
						BlockState newState = MSBlocks.PLATFORM_BLOCK.defaultBlockState()
								.setValue(PlatformBlock.INVISIBLE, getBlockState().getValue(PlatformGeneratorBlock.INVISIBLE_MODE))
								.setValue(PlatformBlock.FACING, getBlockState().getValue(PlatformGeneratorBlock.FACING));
						level.setBlock(iteratePos, newState, Constants.BlockFlags.DEFAULT);
					}
				}
			}
		}
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