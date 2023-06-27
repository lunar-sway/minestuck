package com.mraof.minestuck.effects;

import com.mraof.minestuck.Minestuck;
import net.minecraft.commands.arguments.CompoundTagArgument;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.ZombieEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

/**
 * The Suspicion effect causes mobs to forcibly move away from other entities.
 * @see #onMount(net.minecraftforge.event.entity.EntityMountEvent) Cancels out attempts to ride affected entities
 * @see #summonAttempt(ZombieEvent.SummonAidEvent) Cancels out attempts to spawn reinforcements
 */
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SuspicionEffect extends MobEffect
{
	/**
	 * Used to fire some functionality in {@link #applyEffectTick(LivingEntity, int)} only once.
	 */
	private boolean hasInitialized = false;
	/**
	 * Used to enable the effect at all, blocking the {@link #applyEffectTick(LivingEntity, int)} and {@link #onMount(EntityMountEvent)} functions if false.
	 * It is true if the afflicted entity is not an {@link net.minecraft.world.entity.decoration.ArmorStand} instance and doesn't have the "NoAI" NBT.
	 */
	private boolean isPushable = true;
	/**
	 * Used to enable the pushing effect on flying entities, as an alternative for the {@link LivingEntity#isOnGround()} check.
	 */
	private boolean isFlying = false;
	
	/**
	 * Rescales the curve of the range's increase. It is also the value of the minimum range (range at level 1).
	 */
	private static final float rangeScale = 1.0f;
	
	protected SuspicionEffect()
	{
		super(MobEffectCategory.HARMFUL, 0x2C0489);
	}
	
	@Override
	public boolean isDurationEffectTick(int duration, int amplifier)
	{
		return duration % 2 == 0;
	}
	
	/**
	 * Every tick, eject the affected entity's vehicle/passengers and apply a repulsion force from all mobs with this effect within a calculated range.
	 * @param pLivingEntity the afflicted entity. It will be pushed away from any afflicted entity, and from its ride(r).
	 * @param pAmplifier    the effect's strength. Used to calculate the repulsion range, is applied an exponentiation smaller than 1 to create a diminishing return.
	 */
	@Override
	public void applyEffectTick(@NotNull LivingEntity pLivingEntity, int pAmplifier)
	{
		super.applyEffectTick(pLivingEntity, pAmplifier);
		
		if(!hasInitialized)
		{
			isFlying = ((pLivingEntity instanceof FlyingMob) || (pLivingEntity instanceof FlyingAnimal));
			isPushable = !((pLivingEntity instanceof ArmorStand) || (pLivingEntity.getPersistentData().contains("NoAI", 99)));
			
			hasInitialized = true;
		}
		
		//If the entity can't be pushed (armor stand/NoAI), there's no point in continuing
		if(!isPushable)
		{
			return;
		}
		
		if(pLivingEntity.getVehicle() != null || pLivingEntity.getPassengers().size() > 0)
		{
			pLivingEntity.ejectPassengers();
			pLivingEntity.dismountTo(pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ());
		}
		
		double range = rangeScale * Math.pow(pAmplifier, 0.6);
		
		if(pLivingEntity.isOnGround() || isFlying)
		{
			for(LivingEntity otherEntity : pLivingEntity.level.getEntitiesOfClass(LivingEntity.class, pLivingEntity.getBoundingBox().inflate(range, 1, range)))
			{
				if(otherEntity != pLivingEntity && otherEntity.hasEffect(this))
				{
					//Push direction vector, from other to self
					double pushX = pLivingEntity.getX() - otherEntity.getX();
					double pushZ = pLivingEntity.getZ() - otherEntity.getZ();
					double pushMagnitude = Math.sqrt((pushX * pushX + pushZ * pushZ));
					pushX /= pushMagnitude;
					pushZ /= pushMagnitude;
					
					//Push is stronger the closest each entity is
					double pushForce = 1 / (Math.max(pushMagnitude, 1));
					pLivingEntity.push((pushX * pushForce), 0, (pushZ * pushForce));
				}
			}
		}
	}
	
	/**
	 * Cancels out attempts to ride (or be ridden by) an affected entity
	 */
	@SubscribeEvent
	public void onMount(EntityMountEvent event)
	{
		if(isPushable)
		{
			Entity entityBeingMounted = event.getEntityBeingMounted();
			Entity mountEntity = event.getEntityMounting();
			
			if(((LivingEntity) entityBeingMounted).hasEffect(this) || ((LivingEntity) mountEntity).hasEffect(this))
				event.setCanceled(true);
		}
	}
	
	/**
	 * Cancels out attempts to call for reinforcements.
	 * Examples: {@link net.minecraft.world.entity.monster.Zombie#hurt(DamageSource, float)}, {@link net.minecraft.world.entity.monster.ZombifiedPiglin#hurt(DamageSource, float)})
	 */
	@SubscribeEvent
	public void summonAttempt(ZombieEvent.SummonAidEvent event)
	{
		if(event.getEntity().hasEffect(this)) event.setCanceled(true);
	}
	
}
