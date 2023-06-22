package com.mraof.minestuck.effects;

import com.mraof.minestuck.Minestuck;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.ZombieEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SuspicionEffect extends MobEffect
{
	protected SuspicionEffect() {
		super(MobEffectCategory.HARMFUL, 0x2C0489);
	}
	
	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return duration % 2 == 0;
	}
	
	@Override
	public void applyEffectTick(@NotNull LivingEntity pLivingEntity, int pAmplifier) {
		super.applyEffectTick(pLivingEntity, pAmplifier);
		
		//Eject riders
		if (pLivingEntity.getVehicle() != null || pLivingEntity.getPassengers().size() > 0){
			pLivingEntity.ejectPassengers();
			pLivingEntity.dismountTo(pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ());
		}
		
		double range = 1 * Math.pow(pAmplifier,0.6);
		//Range is 1 block for level 1, and should diminish how many blocks are increased per level
		//The exponent was chosen so at level 10 the range is 4 blocks
		
		for (LivingEntity otherEntity : pLivingEntity.level.getEntitiesOfClass(
				LivingEntity.class, pLivingEntity.getBoundingBox().inflate(range, 1, range))) {
			if (otherEntity != pLivingEntity && otherEntity.hasEffect(this)) {
				//Push direction vector, from other to self
				double pushX = pLivingEntity.getX() - otherEntity.getX();
				double pushZ = pLivingEntity.getZ() - otherEntity.getZ();
				double pushMagnitude = Math.sqrt((pushX * pushX + pushZ * pushZ));
				pushX /= pushMagnitude;
				pushZ /= pushMagnitude;
				
				//Push is stronger the closest each entity is
				double pushForce = 1 / (Math.max(pushMagnitude,1));
				pLivingEntity.setDeltaMovement(
						pLivingEntity.getDeltaMovement().x + (pushX * pushForce),
						pLivingEntity.getDeltaMovement().y,
						pLivingEntity.getDeltaMovement().z + (pushZ * pushForce));
			}
		}
	}
	
	@SubscribeEvent
	public void onMount(EntityMountEvent event) {
		Entity entityBeingMounted = event.getEntityBeingMounted();
		Entity mountEntity = event.getEntityMounting();
		
		if (((LivingEntity)entityBeingMounted).hasEffect(this) ||
				((LivingEntity)mountEntity).hasEffect(this))
			event.setCanceled(true);
	}
	
	@SubscribeEvent
	public void summonAttempt(ZombieEvent.SummonAidEvent event) {
		if (event.getEntity().hasEffect(this))
			event.setCanceled(true);
	}
	
}
