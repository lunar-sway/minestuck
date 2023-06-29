package com.mraof.minestuck.effects;

import com.mraof.minestuck.Minestuck;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
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
	 * Used to enable the effect at all, blocking the {@link #applyEffectTick(LivingEntity, int)} and {@link #onMount(EntityMountEvent)} functions if false.
	 * It is true if the afflicted entity is not an {@link net.minecraft.world.entity.decoration.ArmorStand}, doesn't have the "NoAI" NBT, or isn't a {@link net.minecraft.world.entity.player.Player}.
	 */
	private static boolean isPushable(Entity entity) {
		return !((entity instanceof ArmorStand) ||
				(entity.getPersistentData().contains("NoAI", 99)) ||
				(entity instanceof Player)
		);
	}
	/**
	 * Used to enable the pushing effect on flying entities, as an alternative for the {@link LivingEntity#isOnGround()} check.
	 */
	private static boolean isFlying(Entity entity) {
		return ((entity instanceof FlyingMob) || (entity instanceof FlyingAnimal));
	}
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
		
		//If the entity can't be pushed (armor stand/NoAI), there's no point in continuing
		if(!isPushable(pLivingEntity))
			return;
		
		//Dismount everyone affected
		if(pLivingEntity.getVehicle() != null)
			pLivingEntity.dismountTo(pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ());
		
		if(pLivingEntity.getPassengers().size() > 0)
			pLivingEntity.ejectPassengers();
		
		//The entity won't be pushed if not a flying entity and off the ground
		if(!(pLivingEntity.isOnGround() || isFlying(pLivingEntity)))
			return;
		
		double range = rangeScale * Math.pow(pAmplifier, 0.6);
		
		for(LivingEntity otherEntity : pLivingEntity.level.getEntitiesOfClass(LivingEntity.class, pLivingEntity.getBoundingBox().inflate(range, 1, range)))
		{
			if(otherEntity != pLivingEntity && otherEntity.hasEffect(this))
			{
				//Push direction vector, from other to self
				Vec2 push = new Vec2(
						(float) (pLivingEntity.getX() - otherEntity.getX()),
						(float) (pLivingEntity.getZ() - otherEntity.getZ()));
				Vec2 pushN = push.normalized();
				
				//Push is stronger the closest each entity is
				double pushForce = 1 / (Math.max(push.length(), 1));
				pLivingEntity.push((pushN.x * pushForce), 0, (pushN.y * pushForce));
			}
		}
	}
	
	/**
	 * Cancels out attempts to ride (or be ridden by) an affected entity
	 */
	@SubscribeEvent
	public static void onMount(EntityMountEvent event)
	{
		Entity mountEntity = event.getEntityMounting();
		if(isPushable(mountEntity) && ((LivingEntity) mountEntity).hasEffect(MSEffects.SUSPICION.get()))
			event.setCanceled(true);
		
		Entity entityBeingMounted = event.getEntityBeingMounted();
		if(isPushable(entityBeingMounted) && ((LivingEntity) entityBeingMounted).hasEffect(MSEffects.SUSPICION.get()))
			event.setCanceled(true);
	}
	
	/**
	 * Cancels out attempts to call for reinforcements.
	 * Examples: {@link net.minecraft.world.entity.monster.Zombie#hurt(DamageSource, float)}, {@link net.minecraft.world.entity.monster.ZombifiedPiglin#hurt(DamageSource, float)})
	 */
	@SubscribeEvent
	public static void summonAttempt(ZombieEvent.SummonAidEvent event)
	{
		if(event.getEntity().hasEffect(MSEffects.SUSPICION.get())) event.setCanceled(true);
	}
	
}
