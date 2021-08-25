package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.block.StatStorerBlock;
import com.mraof.minestuck.util.Debug;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class StatStorerTileEntity extends TileEntity
{
	private float damageStored;
	private int deathsStored;
	private ActiveType activeType;
	//private boolean alreadyActivated;
	
	public enum ActiveType{
		DAMAGE,
		DEATHS;
		
		public static ActiveType fromInt(int ordinal) //converts int back into enum
		{
			for(ActiveType type : ActiveType.values())
			{
				if(type.ordinal() == ordinal)
					return type;
			}
			return null;
		}
	}
	
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
		
		if(compound.contains("deathsStored"))
			this.deathsStored = compound.getInt("deathsStored");
		else
			this.deathsStored = 0;
		
		//this.alreadyActivated = compound.getBoolean("alreadyActivated");
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		compound.putFloat("damageStored", damageStored);
		compound.putFloat("deathsStored", deathsStored);
		
		//compound.putBoolean("alreadyActivated", alreadyActivated);
		
		return compound;
	}
	
	public int getActiveStoredStatValue()
	{
		getActiveType();
		if(this.activeType == ActiveType.DAMAGE)
			return (int) this.damageStored;
		else if(this.activeType == ActiveType.DEATHS)
			return this.deathsStored;
		
		return 0;
	}
	
	public ActiveType getActiveType()
	{
		if(this.activeType == null)
			activeType = ActiveType.DAMAGE;
		return this.activeType;
	}
	
	public void setActiveType(ActiveType activeTypeIn)
	{
		activeType = activeTypeIn;
	}
	
	public float getDamageStored()
	{
		return this.damageStored;
	}
	
	public int getDeathsStored()
	{
		return this.deathsStored;
	}
	
	public void setStoredDamageValue(float damageStoredIn, BlockPos blockPos, boolean playAnimation)
	{
		if(playAnimation)
			playAnimation(blockPos);
		
		//this.world.getBlockState(blockPos)
		
		Debug.debugf("setStoredDamageValue");
		
		this.damageStored = damageStoredIn;
		((StatStorerBlock) world.getBlockState(pos).getBlock()).updateNeighbors(world.getBlockState(pos), world, pos, 3);
		//BlockState blockState = world.getBlockState(pos);
		//world.notifyBlockUpdate(pos, blockState, blockState, 3);
	}
	
	public void setStoredDeathValue(int deathsStoredIn, BlockPos blockPos, boolean playAnimation)
	{
		if(playAnimation)
			playAnimation(blockPos);
		
		Debug.debugf("setStoredDeathValue");
		
		this.damageStored = deathsStoredIn;
		((StatStorerBlock) world.getBlockState(pos).getBlock()).updateNeighbors(world.getBlockState(pos), world, pos, 3);
		//BlockState blockState = world.getBlockState(pos);
		//world.notifyBlockUpdate(pos, blockState, blockState, 3);
	}
	
	public void playAnimation(BlockPos blockPosIn)
	{
		for(int i = 0; i < 10; i++)
		{
			this.world.addParticle(ParticleTypes.HEART, true, blockPosIn.getX(), blockPosIn.getY(), blockPosIn.getZ(), 0.01, 0.01, 0.01);
		}
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
