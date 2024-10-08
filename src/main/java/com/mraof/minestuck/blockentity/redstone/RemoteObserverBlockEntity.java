package com.mraof.minestuck.blockentity.redstone;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.redstone.RemoteObserverBlock;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.network.block.RemoteObserverSettingsPacket;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;

public class RemoteObserverBlockEntity extends BlockEntity
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
		IS_ENTITY_ON_GROUND((entity, observer) -> entity.onGround()),
		IS_SPRINTING((entity, observer) -> entity.isSprinting()); //TODO IS_BOSS_PRESENT
		
		private final BiPredicate<Entity, RemoteObserverBlockEntity> typeConditions;
		
		ActiveType(BiPredicate<Entity, RemoteObserverBlockEntity> typeConditions)
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
	
	public RemoteObserverBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.REMOTE_OBSERVER.get(), pos, state);
		activeType = ActiveType.IS_LIVING_ENTITY_PRESENT;
	}
	
	public static void serverTick(Level level, BlockPos pos, BlockState state, RemoteObserverBlockEntity blockEntity)
	{
		if(!level.isAreaLoaded(pos, 1))
			return;
		
		if(blockEntity.tickCycle >= MinestuckConfig.SERVER.puzzleBlockTickRate.get() * 1.667) //6 * 1.667 ~= 10 ticks or 0.5 sec by default
		{
			blockEntity.checkRelaventType();
			blockEntity.tickCycle = 0;
		}
		blockEntity.tickCycle++;
	}
	
	private void checkRelaventType()
	{
		boolean shouldBePowered = false;
		
		AABB axisalignedbb = new AABB(getBlockPos()).inflate(observingRange);
		List<Entity> entityList = level.getEntities(null, axisalignedbb);
		if(!entityList.isEmpty())
		{
			for(Entity entity : entityList)
			{
				if(activeType.typeConditions.test(entity, this))
					shouldBePowered = true; //as long as a single entity from the list matches the conditions of the current active type, it should be powered
			}
		}
		
		if(getBlockState().getValue(RemoteObserverBlock.POWERED) != shouldBePowered)
			level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(RemoteObserverBlock.POWERED, shouldBePowered));
		else level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 2);
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
		return !currentEntityType.is(MSTags.EntityTypes.REMOTE_OBSERVER_BLACKLIST);
	}
	
	public int getObservingRange()
	{
		return this.observingRange;
	}
	
	public void handleSettingsPacket(RemoteObserverSettingsPacket packet)
	{
		activeType = Objects.requireNonNull(packet.activeType());
		if(packet.entityType() != null)
			this.currentEntityType = packet.entityType();
		this.observingRange = packet.observingRange();
		
		setChanged();
		if(getLevel() instanceof ServerLevel serverLevel)
			serverLevel.getChunkSource().blockChanged(getBlockPos());
	}
	
	@Override
	public void load(CompoundTag compound)
	{
		super.load(compound);
		
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
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		
		compound.putInt("tickCycle", tickCycle);
		compound.putInt("activeTypeOrdinal", getActiveType().ordinal());
		compound.putInt("observingRange", observingRange);
		compound.putString("currentEntityType", EntityType.getKey(getCurrentEntityType()).toString());
	}
	
	public ActiveType getActiveType()
	{
		return this.activeType;
	}
	
	@Override
	public CompoundTag getUpdateTag()
	{
		return this.saveWithoutMetadata();
	}
	
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}
}
