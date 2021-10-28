package com.mraof.minestuck.tileentity.redstone;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.redstone.PlatformGeneratorBlock;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
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
		if(world == null || !world.isAreaLoaded(pos, 1))
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
		if(world != null && !world.isRemote)
		{
			int powerIn = world.getRedstonePowerFromNeighbors(pos);
			platformLength = powerIn;
			
			if(powerIn > 0)
			{
				for(int blockIterate = 1; blockIterate < platformLength + 1; blockIterate++)
				{
					BlockPos iteratePos = new BlockPos(pos.offset(getBlockState().get(PlatformGeneratorBlock.FACING), blockIterate));
					if(!world.isAreaLoaded(pos, blockIterate) || World.isYOutOfBounds(iteratePos.getY())) //allows platform blocks to be placed up until it runs out of bounds
						break;
					
					if(world.getBlockState(iteratePos).getMaterial().isLiquid() || world.getBlockState(iteratePos).isAir())
					{
						world.setBlockState(iteratePos, MSBlocks.PLATFORM_BLOCK.getDefaultState().with(PlatformGeneratorBlock.INVISIBLE_MODE, getBlockState().get(PlatformGeneratorBlock.INVISIBLE_MODE)));
					}
				}
			}
		}
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		tickCycle = compound.getInt("tickCycle");
		platformLength = compound.getInt("platformLength");
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		
		compound.putInt("tickCycle", tickCycle);
		compound.putInt("platformLength", platformLength);
		
		return compound;
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		return this.write(new CompoundNBT());
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(this.pos, 2, this.write(new CompoundNBT()));
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		this.read(pkt.getNbtCompound());
	}
	
}