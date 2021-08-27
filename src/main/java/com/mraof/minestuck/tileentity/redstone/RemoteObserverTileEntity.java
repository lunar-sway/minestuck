package com.mraof.minestuck.tileentity.redstone;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.RemoteObserverBlock;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import com.mraof.minestuck.util.Debug;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class RemoteObserverTileEntity extends TileEntity implements ITickableTileEntity
{
	private int tickCycle;
	private ActiveType activeType;
	
	public enum ActiveType
	{
		IS_CROUCHING,
		IS_PLAYER_PRESENT,
		IS_ENTITY_PRESENT,
		IS_UNDERLING_PRESENT,
		IS_ENTITY_BURNING,
		IS_ENTITY_INVISIBLE;
		
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
	
	public RemoteObserverTileEntity()
	{
		super(MSTileEntityTypes.REMOTE_OBSERVER.get());
	}
	
	@Override
	public void tick()
	{
		if(this.getWorld() == null || !this.getWorld().isAreaLoaded(pos, 1))
			return; // Forge: prevent loading unloaded chunks
		
		if(tickCycle % MinestuckConfig.SERVER.wirelessBlocksTickRate.get() == 1)
		{
			checkRelaventType();
			if(tickCycle >= 5000) //setting arbitrarily high value that the tick cannot go past
				tickCycle = 0;
		}
		tickCycle++;
	}
	
	public void checkRelaventType()
	{
		world.setBlockState(pos, getBlockState().with(RemoteObserverBlock.POWERED, false));
		
		AxisAlignedBB axisalignedbb = getRenderBoundingBox().grow(15D, 15D, 15D);
		List<LivingEntity> livingEntityList = world.getEntitiesWithinAABB(LivingEntity.class, axisalignedbb);
		if(!livingEntityList.isEmpty())
		{
			if(activeType == RemoteObserverTileEntity.ActiveType.IS_ENTITY_PRESENT)
				world.setBlockState(pos, getBlockState().with(RemoteObserverBlock.POWERED, true));
			else
			{
				for(LivingEntity livingEntity : livingEntityList)
				{
					if(activeType == RemoteObserverTileEntity.ActiveType.IS_PLAYER_PRESENT && livingEntity instanceof PlayerEntity)
						world.setBlockState(pos, getBlockState().with(RemoteObserverBlock.POWERED, true));
					else if(activeType == RemoteObserverTileEntity.ActiveType.IS_UNDERLING_PRESENT && livingEntity instanceof UnderlingEntity)
						world.setBlockState(pos, getBlockState().with(RemoteObserverBlock.POWERED, true));
					else if(activeType == RemoteObserverTileEntity.ActiveType.IS_CROUCHING && livingEntity.isCrouching())
						world.setBlockState(pos, getBlockState().with(RemoteObserverBlock.POWERED, true));
					else if(activeType == RemoteObserverTileEntity.ActiveType.IS_ENTITY_BURNING && livingEntity.isBurning())
						world.setBlockState(pos, getBlockState().with(RemoteObserverBlock.POWERED, true));
					else if(activeType == RemoteObserverTileEntity.ActiveType.IS_ENTITY_INVISIBLE && livingEntity.isInvisible())
						world.setBlockState(pos, getBlockState().with(RemoteObserverBlock.POWERED, true));
				}
			}
		}
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		
		this.tickCycle = compound.getInt("tickCycle");
		this.activeType = ActiveType.fromInt(compound.getInt("activeTypeOrdinal"));
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		
		compound.putInt("tickCycle", tickCycle);
		compound.putInt("activeTypeOrdinal", getActiveType().ordinal());
		
		return compound;
	}
	
	public ActiveType getActiveType()
	{
		Debug.debugf("getActiveType. activeType = %s", activeType);
		
		if(this.activeType == null)
		{
			activeType = ActiveType.IS_ENTITY_PRESENT;
		}
		
		return this.activeType;
	}
	
	public void setActiveType(ActiveType activeTypeIn)
	{
		activeType = activeTypeIn;
	}
}
