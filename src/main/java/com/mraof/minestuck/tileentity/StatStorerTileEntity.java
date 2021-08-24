package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.block.EnumKeyType;
import com.mraof.minestuck.block.StatStorerBlock;
import com.mraof.minestuck.util.Debug;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class StatStorerTileEntity extends TileEntity
{
	private float damageStored;
	//private boolean alreadyActivated;
	
	public StatStorerTileEntity()
	{
		super(MSTileEntityTypes.STAT_STORER.get());
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		
		if(compound.contains("damageStored"))
			this.damageStored = compound.getFloat("damageStored");
		else
			this.damageStored = 0F;
		
		//this.alreadyActivated = compound.getBoolean("alreadyActivated");
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		compound.putFloat("damageStored", damageStored);
		
		//compound.putBoolean("alreadyActivated", alreadyActivated);
		
		return compound;
	}
	
	public float getStoredStatValue()
	{
		return this.damageStored;
	}
	
	public void setStoredStatValue(float damageStoredIn, BlockPos blockPos, boolean playAnimation)
	{
		if(playAnimation)
		{
			for(int i = 0; i < 10; i++)
			{
				this.world.addParticle(ParticleTypes.HEART, true, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 0.01, 0.01, 0.01);
			}
		}
		//this.world.getBlockState(blockPos)
		
		Debug.debugf("setStoredStatValues");
		
		this.damageStored = damageStoredIn;
		((StatStorerBlock) world.getBlockState(pos).getBlock()).updateNeighbors(world.getBlockState(pos), world, pos, 3);
		//BlockState blockState = world.getBlockState(pos);
		//world.notifyBlockUpdate(pos, blockState, blockState, 3);
	}
	
	/*public boolean getAlreadyActivated()
	{
		return this.alreadyActivated;
	}*/
	
	/*public void setAlreadyActivated(boolean alreadyActivatedIn)
	{
		this.alreadyActivated = alreadyActivatedIn;
	}*/
	
	
}
