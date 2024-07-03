package com.mraof.minestuck.blockentity.redstone;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.redstone.StatStorerBlock;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.event.AlchemyEvent;
import com.mraof.minestuck.event.GristDropsEvent;
import com.mraof.minestuck.network.block.StatStorerSettingsPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent;
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.neoforged.neoforge.event.level.SaplingGrowTreeEvent;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class StatStorerBlockEntity extends BlockEntity
{
	private float damageStored;
	private int deathsStored;
	private int saplingsGrownStored;
	private float healthRecoveredStored;
	private int lightningStruckStored;
	private int entitiesBredStored;
	private int explosionsStored;
	private int alchemyActivatedStored;
	private int gristDropsStored;
	
	private ActiveType activeType;
	private int divideValueBy;
	private int tickCycle;
	
	public enum ActiveType
	{
		DAMAGE,
		DEATHS,
		SAPLING_GROWN,
		HEALTH_RECOVERED,
		LIGHTNING_STRUCK_ENTITY,
		ENTITIES_BRED,
		EXPLOSIONS,
		ALCHEMY_ACTIVATED,
		GRIST_DROPS;
		
		public static ActiveType fromInt(int ordinal) //converts int back into enum
		{
			for(ActiveType type : ActiveType.values())
			{
				if(type.ordinal() == ordinal)
					return type;
			}
			throw new IllegalArgumentException("Invalid ordinal of " + ordinal + " for stat storer active type!");
		}
		
		public String getNameNoSpaces()
		{
			return name().replace('_', ' ');
		}
	}
	
	public StatStorerBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.STAT_STORER.get(), pos, state);
	}
	
	
	public static void serverTick(Level level, BlockPos pos, BlockState state, StatStorerBlockEntity blockEntity)
	{
		if(!level.isAreaLoaded(pos, 1))
			return;
		
		if(blockEntity.tickCycle % MinestuckConfig.SERVER.puzzleBlockTickRate.get() == 1)
		{
			if(!level.isClientSide)
			{
				int moddedStoredValue = Math.min(15, blockEntity.getActiveStoredStatValue() / blockEntity.getDivideValueBy());
				if(state.getValue(StatStorerBlock.POWER) != moddedStoredValue)
					level.setBlock(pos, state.setValue(StatStorerBlock.POWER, moddedStoredValue), Block.UPDATE_ALL);
			}
			if(blockEntity.tickCycle >= 5000) //setting arbitrarily high value that the tick cannot go past
				blockEntity.tickCycle = 0;
		}
		blockEntity.tickCycle++;
	}
	
	public void handleSettingsPacket(StatStorerSettingsPacket packet)
	{
		activeType = packet.activeType();
		this.divideValueBy = Math.max(1, packet.divideValueBy());
		
		this.setChanged();
		if(getLevel() instanceof ServerLevel serverLevel)
			serverLevel.getChunkSource().blockChanged(getBlockPos());
	}
	
	@Override
	public void load(CompoundTag compound)
	{
		super.load(compound);
		
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
		
		if(compound.contains("entitiesBredStored"))
			this.entitiesBredStored = compound.getInt("entitiesBredStored");
		else
			this.entitiesBredStored = 0;
		
		if(compound.contains("explosionsStored"))
			this.explosionsStored = compound.getInt("explosionsStored");
		else
			this.explosionsStored = 0;
		
		if(compound.contains("alchemyActivatedStored"))
			this.alchemyActivatedStored = compound.getInt("alchemyActivatedStored");
		else
			this.alchemyActivatedStored = 0;
		
		if(compound.contains("gristDropsStored"))
			this.gristDropsStored = compound.getInt("gristDropsStored");
		else
			this.gristDropsStored = 0;
		
		
		this.tickCycle = compound.getInt("tickCycle");
		this.activeType = ActiveType.fromInt(compound.getInt("activeTypeOrdinal"));
		if(compound.contains("divideValueBy"))
			this.divideValueBy = compound.getInt("divideValueBy");
		else
			this.divideValueBy = 1;
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		
		compound.putFloat("damageStored", damageStored);
		compound.putInt("deathsStored", deathsStored);
		compound.putInt("saplingsGrownStored", saplingsGrownStored);
		compound.putFloat("healthRecoveredStored", healthRecoveredStored);
		compound.putInt("lightningStruckStored", lightningStruckStored);
		compound.putInt("entitiesBredStored", entitiesBredStored);
		compound.putInt("explosionsStored", explosionsStored);
		compound.putInt("alchemyActivatedStored", alchemyActivatedStored);
		compound.putInt("gristDropsStored", gristDropsStored);
		
		compound.putInt("tickCycle", tickCycle);
		compound.putInt("activeTypeOrdinal", getActiveType().ordinal());
		compound.putInt("divideValueBy", divideValueBy);
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
		else if(this.activeType == ActiveType.LIGHTNING_STRUCK_ENTITY)
			return this.lightningStruckStored;
		else if(this.activeType == ActiveType.ENTITIES_BRED)
			return this.entitiesBredStored;
		else if(this.activeType == ActiveType.EXPLOSIONS)
			return this.explosionsStored;
		else if(this.activeType == ActiveType.ALCHEMY_ACTIVATED)
			return this.alchemyActivatedStored;
		else if(this.activeType == ActiveType.GRIST_DROPS)
			return this.gristDropsStored;
		
		return 0;
	}
	
	public ActiveType getActiveType()
	{
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
	
	public void setActiveStoredStatValue(float storedStatIn)
	{
		activeType = getActiveType();
		
		boolean changeBlockState = false;
		
		if(this.activeType == ActiveType.DAMAGE)
		{
			this.damageStored = storedStatIn;
			changeBlockState = true;
			
		} else if(this.activeType == ActiveType.DEATHS)
		{
			this.deathsStored = (int) storedStatIn;
			changeBlockState = true;
		} else if(this.activeType == ActiveType.SAPLING_GROWN)
		{
			this.saplingsGrownStored = (int) storedStatIn;
			changeBlockState = true;
		} else if(this.activeType == ActiveType.HEALTH_RECOVERED)
		{
			this.healthRecoveredStored = storedStatIn;
			changeBlockState = true;
		} else if(this.activeType == ActiveType.LIGHTNING_STRUCK_ENTITY)
		{
			this.lightningStruckStored = (int) storedStatIn;
			changeBlockState = true;
		} else if(this.activeType == ActiveType.ENTITIES_BRED)
		{
			this.entitiesBredStored = (int) storedStatIn;
			changeBlockState = true;
		} else if(this.activeType == ActiveType.EXPLOSIONS)
		{
			this.explosionsStored = (int) storedStatIn;
			changeBlockState = true;
		} else if(this.activeType == ActiveType.ALCHEMY_ACTIVATED)
		{
			this.alchemyActivatedStored = (int) storedStatIn;
			changeBlockState = true;
		} else if(this.activeType == ActiveType.GRIST_DROPS)
		{
			this.gristDropsStored = (int) storedStatIn;
			changeBlockState = true;
		}
		
		if(changeBlockState && level != null)
			level.updateNeighborsAt(worldPosition, level.getBlockState(worldPosition).getBlock());
		
	}
	
	public static void attemptStatUpdate(float eventAmount, StatStorerBlockEntity.ActiveType activeType, BlockPos eventPos, Level level)
	{
		if(level != null && !level.isClientSide())
		{
			for(BlockPos blockPos : BlockPos.betweenClosed(eventPos.offset(16, 16, 16), eventPos.offset(-16, -16, -16)))
			{
				if(!level.isAreaLoaded(blockPos, 0))
					return;
				
				if(level.getBlockEntity(blockPos) instanceof StatStorerBlockEntity statStorer)
				{
					
					if(activeType == statStorer.getActiveType())
						statStorer.setActiveStoredStatValue(statStorer.getActiveStoredStatValue() + eventAmount);
				}
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public static void onEntityHeal(LivingHealEvent event)
	{
		attemptStatUpdate(event.getAmount(), StatStorerBlockEntity.ActiveType.HEALTH_RECOVERED, event.getEntity().blockPosition(), event.getEntity().level());
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public static void onSaplingGrow(SaplingGrowTreeEvent event)
	{
		attemptStatUpdate(1, StatStorerBlockEntity.ActiveType.SAPLING_GROWN, event.getPos(), (Level) event.getLevel());
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public static void onEntityStruck(EntityStruckByLightningEvent event)
	{
		if(event.getLightning().tickCount == 1)
			attemptStatUpdate(1, StatStorerBlockEntity.ActiveType.LIGHTNING_STRUCK_ENTITY, event.getEntity().blockPosition(), event.getEntity().level());
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public static void onEntityBred(BabyEntitySpawnEvent event)
	{
		if(!event.isCanceled())
			attemptStatUpdate(1, StatStorerBlockEntity.ActiveType.ENTITIES_BRED, event.getParentA().blockPosition(), event.getParentA().level());
	}
	
	@SubscribeEvent
	public static void onExplosion(ExplosionEvent.Detonate event)
	{
		attemptStatUpdate(1, StatStorerBlockEntity.ActiveType.EXPLOSIONS, BlockPos.containing(event.getExplosion().center()), event.getLevel());
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public static void onAlchemy(AlchemyEvent event)
	{
		attemptStatUpdate(1, StatStorerBlockEntity.ActiveType.ALCHEMY_ACTIVATED, event.getAlchemiter().getBlockPos(), event.getLevel());
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public static void onGristDrop(GristDropsEvent event)
	{
		attemptStatUpdate(1, StatStorerBlockEntity.ActiveType.GRIST_DROPS, event.getUnderling().blockPosition(), event.getUnderling().level());
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public static void onEntityDamage(LivingHurtEvent event)
	{
		attemptStatUpdate(event.getAmount(), StatStorerBlockEntity.ActiveType.DAMAGE, event.getEntity().blockPosition(), event.getEntity().level());
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public static void onEntityDeath(LivingDeathEvent event)
	{
		attemptStatUpdate(1, StatStorerBlockEntity.ActiveType.DEATHS, event.getEntity().blockPosition(), event.getEntity().level());
	}
	
	@Override
	public CompoundTag getUpdateTag()
	{
		CompoundTag tagCompound = super.getUpdateTag();
		tagCompound.putInt("activeTypeOrdinal", getActiveType().ordinal());
		tagCompound.putInt("divideValueBy", getDivideValueBy());
		
		return tagCompound;
	}
	
	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}
}
