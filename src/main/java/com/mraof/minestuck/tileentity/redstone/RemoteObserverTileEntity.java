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
import java.util.function.BiPredicate;

public class RemoteObserverTileEntity extends TileEntity implements ITickableTileEntity
{
	private int tickCycle;
	@Nonnull
	private ActiveType activeType;
	private int observingRange = 16; //default is 16, but can be set(via gui) between 1 and 64
	
	private EntityType<?> currentEntityType;
	
	/**
	 * Each enum value contains an inlined lambda expression/anonymous function that tests whether a given entity passes
	 */
	public enum ActiveType
	{
		//when adding new enums, make sure to insert it at the end. Otherwise the ordinal stored in any already generated observers will be messed up
		IS_CROUCHING((entity, observer) -> entity.isCrouching()),
		CURRENT_ENTITY_PRESENT((entity, observer) -> entity.getType() == observer.getCurrentEntityType()),
		IS_LIVING_ENTITY_PRESENT((entity, observer) -> entity instanceof LivingEntity),
		IS_UNDERLING_PRESENT((entity, observer) -> entity instanceof UnderlingEntity),
		IS_ENTITY_BURNING((entity, observer) -> entity.isOnFire()),
		IS_ENTITY_INVISIBLE((entity, observer) -> entity.isInvisible()),
		IS_ELYTRA_FLYING((entity, observer) -> entity instanceof LivingEntity && ((LivingEntity) entity).isFallFlying()),
		IS_ENTITY_UNDER_WATER((entity, observer) -> entity.isUnderWater()),
		IS_ENTITY_WET((entity, observer) -> entity.isInWaterRainOrBubble()),
		IS_ENTITY_ON_GROUND((entity, observer) -> entity.isOnGround()),
		IS_SPRINTING((entity, observer) -> entity.isSprinting()); //TODO IS_BOSS_PRESENT
		
		BiPredicate<Entity, RemoteObserverTileEntity> typeConditions;
		
		ActiveType(BiPredicate<Entity, RemoteObserverTileEntity> typeConditions)
		{
			this.typeConditions = typeConditions;
		}
		
		public static ActiveType fromInt(int ordinal) //converts int back into enum
		{
			if(0 <= ordinal && ordinal < ActiveType.values().length)
				return ActiveType.values()[ordinal];
			else
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
		
		if(tickCycle >= MinestuckConfig.SERVER.puzzleBlockTickRate.get() * 1.667) //6 * 1.667 ~= 10 ticks or 0.5 sec by default
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
				if(activeType.typeConditions.test(entity, this))
					shouldBePowered = true; //as long as a single entity from the list matches the conditions of the current active type, it should be powered
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
		if(compound.contains("observingRange"))
			observingRange = compound.getInt("observingRange");
		else
			observingRange = 15; //before the range was introduced, it was defaulted to 15
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