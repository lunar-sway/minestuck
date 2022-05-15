package com.mraof.minestuck.tileentity.redstone;

import com.mraof.minestuck.block.redstone.RemoteObserverBlock;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
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
		IS_ENTITY_IN_WATER; //TODO IS_BOSS_PRESENT
		
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
			return;
		
		if(tickCycle >= 6 * 1.667) //6 * 1.667 ~= 10 ticks or 0.5 sec, 6 is wireless constant
		{
			checkRelaventType();
			tickCycle = 0;
		}
		tickCycle++;
	}
	
	public void checkRelaventType()
	{
		boolean shouldBePowered = false;
		
		//TODO configurable radius
		//TODO allow for the center of the radius to be moved to other coordinates as is seen with command blocks
		AxisAlignedBB axisalignedbb = new AxisAlignedBB(getBlockPos()).inflate(15D, 15D, 15D);
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
		
		if(!level.isClientSide)
		{
			if(getBlockState().getValue(RemoteObserverBlock.POWERED) != shouldBePowered)
				level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(RemoteObserverBlock.POWERED, shouldBePowered));
			else level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 2);
		}
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