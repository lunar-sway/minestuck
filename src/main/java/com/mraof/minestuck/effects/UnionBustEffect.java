package com.mraof.minestuck.effects;

import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

//@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class UnionBustEffect extends MobEffect
{
	//private static LivingEntity pLivingEntity;
	protected UnionBustEffect() {
		super(MobEffectCategory.HARMFUL, 0xBC9D00);
	}
	
	/*
	@Override
	public void applyInstantenousEffect(@Nullable Entity pSource, @Nullable Entity pIndirectSource, @NotNull LivingEntity pLivingEntity, int pAmplifier, double pHealth)
	{
		super.applyInstantenousEffect(pSource, pIndirectSource, pLivingEntity, pAmplifier, pHealth);
		UnionBustEffect.pLivingEntity = pLivingEntity;
		pLivingEntity.ejectPassengers();
		pLivingEntity.dismountTo(pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ());
		
		double range = 2;
		List<LivingEntity> entitiesInRange = pLivingEntity.level.getEntitiesOfClass(
				LivingEntity.class, pLivingEntity.getBoundingBox().inflate(range, 0.25D, range));
		entitiesInRange.remove(pLivingEntity);
	}
	*/
	
	
	@Override
	public boolean isDurationEffectTick(int duration, int amplifier)
	{
		return duration % 2 == 0;
	}
	
	
	@Override
	public void applyEffectTick(@NotNull LivingEntity pLivingEntity, int pAmplifier) {
		super.applyEffectTick(pLivingEntity, pAmplifier);
		double range = 5;
		List<LivingEntity> entitiesInRange = pLivingEntity.level.getEntitiesOfClass(
				LivingEntity.class, pLivingEntity.getBoundingBox().inflate(range, 0.25D, range));
		entitiesInRange.remove(pLivingEntity);
		
		for (LivingEntity otherEntity : entitiesInRange) {
			if (otherEntity.hasEffect(this)) {
				//Push direction vector, other to self
				double pushX = pLivingEntity.getX() - otherEntity.getX();
				double pushZ = pLivingEntity.getZ() - otherEntity.getZ();
				double pushMagnitude = Math.sqrt((pushX * pushX + pushZ * pushZ));
				pushX /= pushMagnitude;
				pushZ /= pushMagnitude;
				
				//Push is stronger the closest each entity is
				double pushForce = 1 / Mth.clamp(pushMagnitude,1,pushMagnitude);
				pLivingEntity.setDeltaMovement(pushX * pushForce, 0, pushZ * pushForce);
			}
		}
	}
	
	/*
	@Override
	public boolean isInstantenous()
	{
		return true;
	}
	
	@SubscribeEvent
	public static void onMount(EntityMountEvent event) {
		Entity entityBeingMounted = event.getEntityBeingMounted();
		Entity mountEntity = event.getEntityMounting();
		
		if (entityBeingMounted == pLivingEntity || mountEntity == pLivingEntity)
			event.setCanceled(true);
	}
	
	@SubscribeEvent
	public static void summonAttempt(ZombieEvent.SummonAidEvent event) {
		if (event.getEntity() == pLivingEntity)
			event.setCanceled(true);
	}
	*/
}
