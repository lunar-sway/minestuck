package com.mraof.minestuck.tileentity.redstone;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.redstone.RemoteObserverBlock;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
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
	private int observingRange = 16; //default is 16, but can be set(via gui) between 1 and 64
	
	private EntityType<?> currentEntityType;
	
	public enum ActiveType
	{
		IS_CROUCHING,
		CURRENT_ENTITY_PRESENT,
		IS_LIVING_ENTITY_PRESENT,
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
		activeType = ActiveType.IS_LIVING_ENTITY_PRESENT;
	}
	
	@Override
	public void tick()
	{
		if(level == null || !level.isAreaLoaded(getBlockPos(), 1))
			return;
		
		if(tickCycle >= MinestuckConfig.SERVER.puzzleBlockTickRate.get() * 1.667) //6 * 1.667 ~= 10 ticks or 0.5 sec
		{
			checkRelaventType();
			tickCycle = 0;
		}
		tickCycle++;
	}
	
	public void checkRelaventType()
	{
		boolean shouldBePowered = false;
		
		AxisAlignedBB axisalignedbb = new AxisAlignedBB(getBlockPos()).inflate(observingRange);
		List<Entity> entityList = level.getLoadedEntitiesOfClass(Entity.class, axisalignedbb);
		if(!entityList.isEmpty())
		{
			for(Entity entity : entityList)
			{
				if(activeType == ActiveType.IS_LIVING_ENTITY_PRESENT && entity instanceof LivingEntity)
					shouldBePowered = true;
				else if(activeType == ActiveType.CURRENT_ENTITY_PRESENT && entity.getType() == getCurrentEntityType())
					shouldBePowered = true;
				else if(activeType == ActiveType.IS_UNDERLING_PRESENT && entity instanceof UnderlingEntity)
					shouldBePowered = true;
				else if(activeType == ActiveType.IS_CROUCHING && entity.isCrouching())
					shouldBePowered = true;
				else if(activeType == ActiveType.IS_ENTITY_BURNING && entity.isOnFire())
					shouldBePowered = true;
				else if(activeType == ActiveType.IS_ENTITY_INVISIBLE && entity.isInvisible())
					shouldBePowered = true;
				else if(activeType == ActiveType.IS_ELYTRA_FLYING && (entity instanceof LivingEntity && ((LivingEntity) entity).isFallFlying()))
					shouldBePowered = true;
				else if(activeType == ActiveType.IS_ENTITY_IN_WATER && entity.isInWater())
					shouldBePowered = true;
			}
		}
		
		if(!level.isClientSide)
		{
			if(getBlockState().getValue(RemoteObserverBlock.POWERED) != shouldBePowered)
				level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(RemoteObserverBlock.POWERED, shouldBePowered));
			else level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 2);
		}
	}
	
	public void setCurrentEntityType(EntityType<?> currentEntityType)
	{
		this.currentEntityType = currentEntityType;
	}
	
	public EntityType<?> getCurrentEntityType()
	{
		if(currentEntityType != null)
			return currentEntityType;
		else
			return EntityType.PLAYER;
	}
	
	/**
	 * Checks for entity types that are not intended to be known by survival players or are forbidden from typical use
	 */
	public static boolean entityCanBeObserved(EntityType<?> currentEntityType)
	{
		return !MSTags.EntityTypes.REMOTE_OBSERVER_BLACKLIST.contains(currentEntityType);
	}
	
	public void setObservingRange(int rangeIn)
	{
		this.observingRange = rangeIn;
	}
	
	public int getObservingRange()
	{
		return this.observingRange;
	}
	
	@Override
	public void load(BlockState state, CompoundNBT compound)
	{
		super.load(state, compound);
		
		this.tickCycle = compound.getInt("tickCycle");
		this.activeType = ActiveType.fromInt(compound.getInt("activeTypeOrdinal"));
		observingRange = compound.getInt("observingRange");
		Optional<EntityType<?>> attemptedEntityType = EntityType.byString(compound.getString("currentEntityType"));
		attemptedEntityType.ifPresent(entityType -> this.currentEntityType = entityType);
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound)
	{
		super.save(compound);
		
		compound.putInt("tickCycle", tickCycle);
		compound.putInt("activeTypeOrdinal", getActiveType().ordinal());
		compound.putInt("observingRange", observingRange);
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