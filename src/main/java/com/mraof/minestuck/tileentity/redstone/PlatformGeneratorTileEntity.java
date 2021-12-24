package com.mraof.minestuck.tileentity.redstone;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.redstone.PlatformGeneratorBlock;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
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
			return; // Forge: prevent loading unloaded chunks
		
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
			int powerIn = level.getBestNeighborSignal(getBlockPos());
			platformLength = powerIn;
			
			if(powerIn > 0)
			{
				for(int blockIterate = 1; blockIterate < platformLength + 1; blockIterate++)
				{
					BlockPos iteratePos = new BlockPos(getBlockPos().relative(getBlockState().getValue(PlatformGeneratorBlock.FACING), blockIterate));
					if(!level.isAreaLoaded(getBlockPos(), blockIterate) || World.isOutsideBuildHeight(iteratePos.getY())) //allows platform blocks to be placed up until it runs out of bounds
						break;
					
					if(level.getBlockState(iteratePos).getMaterial().isLiquid() || level.getBlockState(iteratePos).isAir()/* || (level.getBlockState(iteratePos).getBlock() == MSBlocks.PLATFORM_BLOCK && level.getPendingBlockTicks().isTickScheduled(iteratePos, MSBlocks.PLATFORM_BLOCK))*/)
					{
						level.setBlock(iteratePos, MSBlocks.PLATFORM_BLOCK.defaultBlockState().setValue(PlatformGeneratorBlock.INVISIBLE_MODE, getBlockState().getValue(PlatformGeneratorBlock.INVISIBLE_MODE)), 2);
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