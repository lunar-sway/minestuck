package com.mraof.minestuck.effects;

import com.mraof.minestuck.Minestuck;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityMountEvent;
import net.neoforged.neoforge.event.entity.living.ZombieEvent;
import org.jetbrains.annotations.NotNull;

/**
 * The Suspicion effect causes mobs to forcibly move away from other entities.
 *
 * @see #onMount(net.neoforged.neoforge.event.entity.EntityMountEvent) Cancels out attempts to ride affected entities
 * @see #summonAttempt(ZombieEvent.SummonAidEvent) Cancels out attempts to spawn reinforcements
 */
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SuspicionEffect extends MobEffect
{
	/**
	 * Rescales the curve of the range's increase. It is also the value of the minimum range (range at level 1).
	 */
	private static final float RANGE_SCALE = 1.0f;
	
	protected SuspicionEffect()
	{
		super(MobEffectCategory.HARMFUL, 0x2C0489);
	}
	
	/**
	 * Used to enable the effect at all, blocking the {@link #applyEffectTick(LivingEntity, int)} and {@link #onMount(EntityMountEvent)} functions if false.
	 * It is true if the afflicted entity is not an {@link ArmorStand}, doesn't have the "NoAI" NBT ({@link Mob#isNoAi()}), or isn't a {@link Player}.
	 */
	private static boolean isPushable(Entity entity)
	{
		return !((entity instanceof Player)
				|| (entity instanceof ArmorStand)
				|| (entity instanceof Mob mob && mob.isNoAi()));
	}
	
	/**
	 * Used to enable the pushing effect on flying entities, as an alternative for the {@link LivingEntity#onGround()} check.
	 */
	private static boolean isFlying(Entity entity)
	{
		return ((entity instanceof FlyingMob) || (entity instanceof FlyingAnimal));
	}
	
	/**
	 * Used to check if an entity has the effect AND is pushable, to inflict code on the checked entity
	 */
	private static boolean hasEffect(Entity entity)
	{
		return (isPushable(entity)
				&& (entity instanceof LivingEntity livingEntity
				&& livingEntity.hasEffect(MSEffects.SUSPICION.get())));
	}
	
	/**
	 * Cancels out attempts to ride (or be ridden by) an affected entity
	 */
	@SubscribeEvent
	public static void onMount(EntityMountEvent event)
	{
		Entity entityMounting = event.getEntityMounting();
		if(hasEffect(entityMounting))
			event.setCanceled(true);
		
		Entity entityBeingMounted = event.getEntityBeingMounted();
		if(hasEffect(entityBeingMounted))
			event.setCanceled(true);
	}
	
	/**
	 * Cancels out attempts to call for reinforcements.
	 * Examples: {@link net.minecraft.world.entity.monster.Zombie#hurt(DamageSource, float)}, {@link net.minecraft.world.entity.monster.ZombifiedPiglin#hurt(DamageSource, float)})
	 */
	@SubscribeEvent
	public static void summonAttempt(ZombieEvent.SummonAidEvent event)
	{
		if(event.getEntity().hasEffect(MSEffects.SUSPICION.get()))
			event.setResult(Event.Result.DENY);
	}
	
	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier)
	{
		return duration % 2 == 0;
	}
	
	/**
	 * Every tick, eject the affected entity's vehicle/passengers and apply a repulsion force from all mobs with this effect within a calculated range.
	 *
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
		if(!(pLivingEntity.onGround() || isFlying(pLivingEntity)))
			return;
		
		double range = RANGE_SCALE * Math.pow(pAmplifier, 0.6);
		
		for(LivingEntity otherEntity : pLivingEntity.level().getEntitiesOfClass(LivingEntity.class, pLivingEntity.getBoundingBox().inflate(range, 1, range)))
		{
			if(otherEntity != pLivingEntity && otherEntity.hasEffect(this))
			{
				//Push direction vector (this entity <- other entity)
				Vec3 push = otherEntity.position().vectorTo(pLivingEntity.position()).multiply(1, 0, 1);
				
				//Push is stronger the closest each entity is
				double pushForce = 1 / (Math.max(push.length(), 1));
				
				//Apply directional force
				push = push.normalize().scale(pushForce);
				pLivingEntity.push(push.x, 0, push.z);
			}
		}
	}
	
}
