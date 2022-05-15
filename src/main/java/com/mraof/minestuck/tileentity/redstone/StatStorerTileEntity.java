package com.mraof.minestuck.tileentity.redstone;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.redstone.StatStorerBlock;
import com.mraof.minestuck.event.AlchemyEvent;
import com.mraof.minestuck.event.GristDropsEvent;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.event.world.SaplingGrowTreeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class StatStorerTileEntity extends TileEntity implements ITickableTileEntity
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
	
	public StatStorerTileEntity()
	{
		super(MSTileEntityTypes.STAT_STORER.get());
	}
	
	@Override
	public void tick()
	{
		if(level == null || !level.isAreaLoaded(worldPosition, 1))
			return;
		
		if(tickCycle % 6 == 1) //6 is wireless constant
		{
			if(!level.isClientSide)
			{
				if(getBlockState().getValue(StatStorerBlock.POWER) != Math.min(15, getActiveStoredStatValue() / getDivideValueBy()))
					level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(StatStorerBlock.POWER, Math.min(15, getActiveStoredStatValue() / getDivideValueBy())));
				else level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 2);
			}
			if(tickCycle >= 5000) //setting arbitrarily high value that the tick cannot go past
				tickCycle = 0;
		}
		tickCycle++;
	}
	
	@Override
	public void load(BlockState state, CompoundNBT compound)
	{
		super.load(state, compound);
		
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
	public CompoundNBT save(CompoundNBT compound)
	{
		super.save(compound);
		
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
	
	public void setActiveTypeAndDivideValue(ActiveType activeTypeIn, int divideValueBy)
	{
		activeType = activeTypeIn;
		if(divideValueBy <= 0)
			divideValueBy = 1;
		this.divideValueBy = divideValueBy;
		this.setChanged();
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 0);
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
		
		if(changeBlockState)
			level.updateNeighborsAt(worldPosition, level.getBlockState(worldPosition).getBlock());
		
	}
	
	public static void attemptStatUpdate(float eventAmount, StatStorerTileEntity.ActiveType activeType, BlockPos eventPos, World world)
	{
		if(world != null && !world.isClientSide())
		{
			for(BlockPos blockPos : BlockPos.betweenClosed(eventPos.offset(16, 16, 16), eventPos.offset(-16, -16, -16)))
			{
				if(!world.isAreaLoaded(blockPos, 0))
					return;
				
				TileEntity tileEntity = world.getBlockEntity(blockPos);
				if(tileEntity instanceof StatStorerTileEntity)
				{
					StatStorerTileEntity storerTileEntity = (StatStorerTileEntity) tileEntity;
					
					if(activeType == storerTileEntity.getActiveType())
						storerTileEntity.setActiveStoredStatValue(storerTileEntity.getActiveStoredStatValue() + eventAmount);
				}
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public static void onEntityHeal(LivingHealEvent event)
	{
		attemptStatUpdate(event.getAmount(), StatStorerTileEntity.ActiveType.HEALTH_RECOVERED, event.getEntity().blockPosition(), event.getEntity().level);
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public static void onSaplingGrow(SaplingGrowTreeEvent event)
	{
		attemptStatUpdate(1, StatStorerTileEntity.ActiveType.SAPLING_GROWN, event.getPos(), (World) event.getWorld());
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public static void onEntityStruck(EntityStruckByLightningEvent event)
	{
		if(event.getLightning().tickCount == 1)
			attemptStatUpdate(1, StatStorerTileEntity.ActiveType.LIGHTNING_STRUCK_ENTITY, event.getEntity().blockPosition(), event.getEntity().level);
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public static void onEntityBred(BabyEntitySpawnEvent event)
	{
		if(!event.isCanceled())
			attemptStatUpdate(1, StatStorerTileEntity.ActiveType.ENTITIES_BRED, event.getParentA().blockPosition(), event.getParentA().level);
	}
	
	@SubscribeEvent
	public static void onExplosion(ExplosionEvent.Detonate event)
	{
		attemptStatUpdate(1, StatStorerTileEntity.ActiveType.EXPLOSIONS, new BlockPos(event.getExplosion().getPosition()), event.getWorld());
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public static void onAlchemy(AlchemyEvent event)
	{
		attemptStatUpdate(1, StatStorerTileEntity.ActiveType.ALCHEMY_ACTIVATED, event.getAlchemiter().getBlockPos(), event.getWorld());
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public static void onGristDrop(GristDropsEvent event)
	{
		attemptStatUpdate(1, StatStorerTileEntity.ActiveType.GRIST_DROPS, event.getUnderling().getEntity().blockPosition(), event.getUnderling().level);
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public static void onEntityDamage(LivingHurtEvent event)
	{
		attemptStatUpdate(event.getAmount(), StatStorerTileEntity.ActiveType.DAMAGE, event.getEntity().blockPosition(), event.getEntity().level);
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public static void onEntityDeath(LivingDeathEvent event)
	{
		attemptStatUpdate(1, StatStorerTileEntity.ActiveType.DEATHS, event.getEntity().blockPosition(), event.getEntity().level);
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		CompoundNBT tagCompound = super.getUpdateTag();
		tagCompound.putInt("activeTypeOrdinal", getActiveType().ordinal());
		tagCompound.putInt("divideValueBy", getDivideValueBy());
		
		return tagCompound;
	}
	
	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(this.worldPosition, 2, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		this.load(getBlockState(), pkt.getTag());
	}
}