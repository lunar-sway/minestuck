package com.mraof.minestuck.entity.ai.attack;

import com.mraof.minestuck.entity.AttackingAnimatedEntity;
import com.mraof.minestuck.entity.animation.PhasedMobAnimation;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

/**
 * Cooldown sensitive animated attack which flings the target and nearby entities into the air when they meet certain criteria.
 * The criteria is if they were on the ground and not crouching, unless the entity is within a few blocks in which the crouching doesn't matter.
 */
public class GroundSlamGoal<T extends AttackingAnimatedEntity> extends AnimatedAttackWhenInRangeGoal<T>
{
	public static final int GROUP_SLAM_COOLDOWN = 20; //how long it should take before another nearby mob can ground slam
	
	public GroundSlamGoal(T entity, PhasedMobAnimation animation, float minRange, float maxRange, int actionCooldown)
	{
		super(entity, animation, minRange, maxRange, actionCooldown, 0, 180.0F);
	}
	
	@Override
	public boolean canUse()
	{
		return super.canUse() && entity.hasFinishedCooldown(); //cooldown sensitive
	}
	
	@Override
	public void start()
	{
		super.start();
		
		applyGroupCooldown();
	}
	
	private void applyGroupCooldown()
	{
		Level level = this.entity.level();
		AABB aabb = new AABB(this.entity.blockPosition()).inflate(8);
		List<AttackingAnimatedEntity> entityList = level.getEntitiesOfClass(AttackingAnimatedEntity.class, aabb);
		if(!entityList.isEmpty())
		{
			for(AttackingAnimatedEntity iteratedEntity : entityList)
			{
				if(iteratedEntity != this.entity && iteratedEntity.getType() == this.entity.getType() && !iteratedEntity.existingCooldownIsLonger(GROUP_SLAM_COOLDOWN))
				{
					iteratedEntity.setCooldown(GROUP_SLAM_COOLDOWN + phasedAnimation.getTotalAnimationLength(speed)); //plays at beginning to prevent simultaneous goal use by other mobs of the same type
				}
			}
		}
	}
	
	@Override
	public void attemptToLandAttack(PathfinderMob attacker, LivingEntity target)
	{
		Level level = entity.level();
		level.playSound(null, this.entity.blockPosition(), MSSoundEvents.ENTITY_SLAM.get(), SoundSource.HOSTILE, 2.5F, 1);
		
		//flinging any nearby smaller mobs on the ground indiscriminately
		AABB aabb = new AABB(attacker.blockPosition()).inflate(4); //TODO make the size of this bounding box better match that determined by the inMeleeRange boolean
		List<LivingEntity> entityList = level.getEntitiesOfClass(LivingEntity.class, aabb);
		if(!entityList.isEmpty())
		{
			for(LivingEntity iteratedEntity : entityList)
			{
				boolean smallerThanAttacker = iteratedEntity.getBoundingBox().getSize() < attacker.getBoundingBox().getSize();
				
				if(iteratedEntity != attacker && iteratedEntity != target && iteratedEntity.onGround() && !iteratedEntity.noPhysics && smallerThanAttacker)
				{
					fling(attacker, iteratedEntity, false);
				}
			}
		}
		
		//flinging the target mob
		if(target != null && targetCanBeHit(target) && target.onGround())
		{
			//similar range check to indiscriminate mob fling check
			boolean inMeleeRange = getStandardAttackReachSqr(target) >= entity.distanceToSqr(target);
			
			//slam will connect if the target is close or if the target is farther away but not crouching, always requires target be on the ground
			if(inMeleeRange || !target.isCrouching())
			{
				fling(attacker, target, true);
			}
		}
	}
	
	private void fling(PathfinderMob attacker, LivingEntity target, boolean doHurt)
	{
		//TODO figure out if handleEntityEvent/broadcastEntityEvent or another method can be used as an alternative to hurting the player in order to update. setDeltaMovement/push does not work on their own
		if(doHurt)
		{
			//after the clamp is applied: 10 damage at 1-2 blocks away, 3.4 damage at 5 blocks away, 1 damage at 10 blocks away, 0.2 damage at 15 blocks away
			double distanceModified = (1.2 / (Math.sqrt(attacker.distanceToSqr(target)) * 0.05)) - 1.4;
			float damage = Math.min(Math.max((float) distanceModified, 0.2F), 10F);
			target.hurt(attacker.damageSources().mobAttack(attacker), damage);
		}
		
		//non-target mobs don't need to be damaged, if they did then underlings would aggro on each other
		target.push(target.getRandom().nextFloat() - 0.5, 0.75, target.getRandom().nextFloat() - 0.5);
	}
}