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
	private int saplingsGrownStored;
	private float healthRecoveredStored;
	private int lightningStruckStored;
	private int blockRightClickStored;
	private int entitySetTargetStored;
	private int alcehemyActivatedStored;
	
	private ActiveType activeType;
	private int divideValueBy;
	
	public enum ActiveType
	{
		DAMAGE,
		DEATHS,
		SAPLING_GROWN,
		HEALTH_RECOVERED,
		LIGHTNING_STRUCK,
		BLOCK_RIGHT_CLICK,
		ENTITY_SET_TARGET,
		ALCHEMY_ACTIVATED;
		
		public static ActiveType fromInt(int ordinal) //converts int back into enum
		{
			for(ActiveType type : ActiveType.values())
			{
				if(type.ordinal() == ordinal)
					return type;
			}
			return null;
		}
		
		public String getNameNoSpaces()
		{
			return name().replace('_', ' ');
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
		
		if(compound.contains("saplingsGrownStored"))
			this.saplingsGrownStored = compound.getInt("saplingsGrownStored");
		else
			this.saplingsGrownStored = 0;
		
		if(compound.contains("healthRecoveredStored"))
			this.healthRecoveredStored = compound.getFloat("healthRecoveredStored");
		else
			this.healthRecoveredStored = 0F;
		
		if(compound.contains("lightningStruckStored"))
			this.lightningStruckStored = compound.getInt("lightningStruckStored");
		else
			this.lightningStruckStored = 0;
		
		if(compound.contains("blockRightClickStored"))
			this.blockRightClickStored = compound.getInt("blockRightClickStored");
		else
			this.blockRightClickStored = 0;
		
		if(compound.contains("entitySetTargetStored"))
			this.entitySetTargetStored = compound.getInt("entitySetTargetStored");
		else
			this.entitySetTargetStored = 0;
		
		if(compound.contains("alcehemyActivatedStored"))
			this.alcehemyActivatedStored = compound.getInt("alcehemyActivatedStored");
		else
			this.alcehemyActivatedStored = 0;
		
		
		this.activeType = ActiveType.fromInt(compound.getInt("activeTypeOrdinal"));
		if(compound.contains("divideValueBy"))
			this.divideValueBy = compound.getInt("divideValueBy");
		else
			this.divideValueBy = 1;
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		compound.putFloat("damageStored", damageStored);
		compound.putInt("deathsStored", deathsStored);
		compound.putInt("saplingsGrownStored", saplingsGrownStored);
		compound.putFloat("healthRecoveredStored", healthRecoveredStored);
		compound.putInt("lightningStruckStored", lightningStruckStored);
		compound.putInt("blockRightClickStored", blockRightClickStored);
		compound.putInt("entitySetTargetStored", entitySetTargetStored);
		compound.putInt("alcehemyActivatedStored", alcehemyActivatedStored);
		
		compound.putInt("activeTypeOrdinal", activeType.ordinal());
		compound.putInt("divideValueBy", divideValueBy);
		
		return compound;
	}
	
	public int getActiveStoredStatValue()
	{
		activeType = getActiveType();
		if(this.activeType == ActiveType.DAMAGE)
			return (int) this.damageStored;
		else if(this.activeType == ActiveType.DEATHS)
			return this.deathsStored;
		else if(this.activeType == ActiveType.SAPLING_GROWN)
			return this.saplingsGrownStored;
		else if(this.activeType == ActiveType.HEALTH_RECOVERED)
			return (int) this.healthRecoveredStored;
		else if(this.activeType == ActiveType.LIGHTNING_STRUCK)
			return this.lightningStruckStored;
		else if(this.activeType == ActiveType.BLOCK_RIGHT_CLICK)
			return this.blockRightClickStored;
		else if(this.activeType == ActiveType.ENTITY_SET_TARGET)
			return this.entitySetTargetStored;
		else if(this.activeType == ActiveType.ALCHEMY_ACTIVATED)
			return this.alcehemyActivatedStored;
		
		return 0;
	}
	
	public ActiveType getActiveType()
	{
		Debug.debugf("getActiveType. activeType = %s", activeType);
		
		if(this.activeType == null)
		{
			activeType = ActiveType.DAMAGE;
		}
		
		return this.activeType;
	}
	
	public int getDivideValueBy()
	{
		if(this.divideValueBy <= 0)
			divideValueBy = 1;
		return this.divideValueBy;
	}
	
	public void setActiveType(ActiveType activeTypeIn)
	{
		activeType = activeTypeIn;
	}
	
	public void setActiveStoredStatValue(float storedStatIn, BlockPos blockPos, boolean playAnimation)
	{
		if(playAnimation)
			playAnimation(blockPos);
		activeType = getActiveType();
		
		if(this.activeType == ActiveType.DAMAGE)
		{
			this.damageStored = storedStatIn;
			((StatStorerBlock) world.getBlockState(pos).getBlock()).updateNeighbors(world.getBlockState(pos), world, pos, 3);
		} else if(this.activeType == ActiveType.DEATHS)
		{
			this.deathsStored = (int) storedStatIn;
			((StatStorerBlock) world.getBlockState(pos).getBlock()).updateNeighbors(world.getBlockState(pos), world, pos, 3);
		} else if(this.activeType == ActiveType.SAPLING_GROWN)
		{
			this.saplingsGrownStored = (int) storedStatIn;
			((StatStorerBlock) world.getBlockState(pos).getBlock()).updateNeighbors(world.getBlockState(pos), world, pos, 3);
		} else if(this.activeType == ActiveType.HEALTH_RECOVERED)
		{
			this.healthRecoveredStored = storedStatIn;
			((StatStorerBlock) world.getBlockState(pos).getBlock()).updateNeighbors(world.getBlockState(pos), world, pos, 3);
		} else if(this.activeType == ActiveType.LIGHTNING_STRUCK)
		{
			this.lightningStruckStored = (int) storedStatIn;
			((StatStorerBlock) world.getBlockState(pos).getBlock()).updateNeighbors(world.getBlockState(pos), world, pos, 3);
		} else if(this.activeType == ActiveType.BLOCK_RIGHT_CLICK)
		{
			this.blockRightClickStored = (int) storedStatIn;
			((StatStorerBlock) world.getBlockState(pos).getBlock()).updateNeighbors(world.getBlockState(pos), world, pos, 3);
		} else if(this.activeType == ActiveType.ENTITY_SET_TARGET)
		{
			this.entitySetTargetStored = (int) storedStatIn;
			((StatStorerBlock) world.getBlockState(pos).getBlock()).updateNeighbors(world.getBlockState(pos), world, pos, 3);
		} else if(this.activeType == ActiveType.ALCHEMY_ACTIVATED)
		{
			this.alcehemyActivatedStored = (int) storedStatIn;
			((StatStorerBlock) world.getBlockState(pos).getBlock()).updateNeighbors(world.getBlockState(pos), world, pos, 3);
		}
	}
	
	public void setDivideValue(int divideValueBy)
	{
		this.divideValueBy = divideValueBy;
	}
	
	public void playAnimation(BlockPos blockPosIn)
	{
		for(int i = 0; i < 10; i++)
		{
			this.world.addParticle(ParticleTypes.HEART, true, blockPosIn.getX(), blockPosIn.getY(), blockPosIn.getZ(), 0.01, 0.01, 0.01);
		}
	}
}
