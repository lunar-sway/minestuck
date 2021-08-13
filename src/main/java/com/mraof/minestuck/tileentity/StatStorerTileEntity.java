package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.block.EnumKeyType;
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
			{//TODO plays on hurt entity
				this.world.addParticle(ParticleTypes.HEART,true, blockPos.getX(), blockPos.getY() + 1, blockPos.getZ(), 0.01, 0.01,0.01);
			}
		}
		
		this.damageStored = damageStoredIn;
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
