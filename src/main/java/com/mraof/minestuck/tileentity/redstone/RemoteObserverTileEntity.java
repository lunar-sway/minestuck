package com.mraof.minestuck.tileentity.redstone;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.redstone.RemoteObserverBlock;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class RemoteObserverTileEntity extends TileEntity implements ITickableTileEntity
{
	private int tickCycle;
	@Nonnull
	private ActiveType activeType;
	
	private EntityType<?> currentEntityType;
	
	public enum ActiveType
	{
		IS_CROUCHING,
		CURRENT_ENTITY_PRESENT,
		IS_ENTITY_PRESENT,
		IS_UNDERLING_PRESENT,
		IS_ENTITY_BURNING,
		IS_ENTITY_INVISIBLE,
		IS_ELYTRA_FLYING,
		IS_ENTITY_IN_WATER;
		
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
		if(level == null || !level.isAreaLoaded(getBlockPos(), 1))
			return; // Forge: prevent loading unloaded chunks
		
		if(tickCycle >= MinestuckConfig.SERVER.wirelessBlocksTickRate.get() * 1.667) //with the config value of 6 ticks, 6 * 1.667 ~= 10 ticks or 0.5 sec
		{
			checkRelaventType();
			tickCycle = 0;
		}
		tickCycle++;
	}
	
	public void checkRelaventType()
	{
		boolean shouldBePowered = false;
		
		AxisAlignedBB axisalignedbb = getRenderBoundingBox().inflate(15D, 15D, 15D);
		List<LivingEntity> livingEntityList = level.getLoadedEntitiesOfClass(LivingEntity.class, axisalignedbb);
		if(!livingEntityList.isEmpty())
		{
			if(activeType == ActiveType.IS_ENTITY_PRESENT)
				shouldBePowered = true;
			else
			{
				for(LivingEntity livingEntity : livingEntityList)
				{
					if(activeType == ActiveType.CURRENT_ENTITY_PRESENT && livingEntity.getType() == getCurrentEntityType())
						shouldBePowered = true;
					else if(activeType == ActiveType.IS_UNDERLING_PRESENT && livingEntity instanceof UnderlingEntity)
						shouldBePowered = true;
					else if(activeType == ActiveType.IS_CROUCHING && livingEntity.isCrouching())
						shouldBePowered = true;
					else if(activeType == ActiveType.IS_ENTITY_BURNING && livingEntity.isOnFire())
						shouldBePowered = true;
					else if(activeType == ActiveType.IS_ENTITY_INVISIBLE && livingEntity.isInvisible())
						shouldBePowered = true;
					else if(activeType == ActiveType.IS_ELYTRA_FLYING && livingEntity.isFallFlying())
						shouldBePowered = true;
					else if(activeType == ActiveType.IS_ENTITY_IN_WATER && livingEntity.isInWater())
						shouldBePowered = true;
				}
			}
		}
		
		level.setBlock(getBlockPos(), getBlockState().setValue(RemoteObserverBlock.POWERED, shouldBePowered), 2);
	}
	
	public EntityType<?> getCurrentEntityType()
	{
		if(currentEntityType != null)
			return currentEntityType;
		else
			return EntityType.PLAYER;
	}
	
	public void setCurrentEntityType(EntityType<?> currentEntityType)
	{
		this.currentEntityType = currentEntityType;
	}
	
	@Override
	public void load(BlockState state, CompoundNBT compound)
	{
		super.load(state, compound);
		
		this.tickCycle = compound.getInt("tickCycle");
		this.activeType = ActiveType.fromInt(compound.getInt("activeTypeOrdinal"));
		Optional<EntityType<?>> attemptedEntityType = EntityType.byString(compound.getString("currentEntityType"));
		attemptedEntityType.ifPresent(entityType -> this.currentEntityType = entityType);
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound)
	{
		super.save(compound);
		
		compound.putInt("tickCycle", tickCycle);
		compound.putInt("activeTypeOrdinal", getActiveType().ordinal());
		compound.putString("currentEntityType", EntityType.getKey(getCurrentEntityType()).toString());
		
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