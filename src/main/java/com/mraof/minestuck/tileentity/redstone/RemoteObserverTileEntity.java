package com.mraof.minestuck.tileentity.redstone;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.redstone.RemoteObserverBlock;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

public class RemoteObserverTileEntity extends TileEntity implements ITickableTileEntity
{
	private int tickCycle;
	@Nonnull
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
			throw new IllegalArgumentException("Invalid ordinal of " + ordinal + " for remote observer active type!");
		}
		
		public String getNameNoSpaces()
		{
			return name().replace('_', ' ');
		}
	}
	
	public RemoteObserverTileEntity()
	{
		super(MSTileEntityTypes.REMOTE_OBSERVER.get());
		activeType = ActiveType.IS_ENTITY_PRESENT;
	}
	
	@Override
	public void tick()
	{
		if(this.getWorld() == null || !this.getWorld().isAreaLoaded(pos, 1))
			return; // Forge: prevent loading unloaded chunks
		
		if(tickCycle >= MinestuckConfig.SERVER.wirelessBlocksTickRate.get())
		{
			checkRelaventType();
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
		return this.activeType;
	}
	
	public void setActiveType(ActiveType activeTypeIn)
	{
		activeType = Objects.requireNonNull(activeTypeIn);
	}
}
