package com.mraof.minestuck.entity.ai.attack;

import com.mraof.minestuck.entity.AttackingAnimatedEntity;
import com.mraof.minestuck.entity.animation.PhasedMobAnimation;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

/**
 * Alternative attack for when the target is out of standard range, results in players being flung into the air if they were on the ground and not crouching.
 */
public class GroundSlamGoal<T extends AttackingAnimatedEntity> extends AnimatedAttackWhenInRangeGoal<T>
{
	public static final int GROUP_SLAM_COOLDOWN = 20; //how long it should take before another nearby mob can ground slam
	
	public GroundSlamGoal(T entity, PhasedMobAnimation animation, float minRange, float maxRange, int actionCooldown)
	{
		super(entity, animation, minRange, maxRange, actionCooldown);
	}
	
	@Override
	public boolean canUse()
	{
		return super.canUse() && entity.hasFinishedCooldown();
	}
	
	@Override
	public void start()
	{
		super.start();
		
		Level level = this.entity.getLevel();
		AABB aabb = new AABB(this.entity.blockPosition()).inflate(4);
		List<AttackingAnimatedEntity> entityList = level.getEntitiesOfClass(AttackingAnimatedEntity.class, aabb);
		if(!entityList.isEmpty())
		{
			for(AttackingAnimatedEntity iteratedEntity : entityList)
			{
				if(iteratedEntity != this.entity && !iteratedEntity.existingCooldownIsLonger(GROUP_SLAM_COOLDOWN))
				{
					//TODO slamGoal bool does not seem to trigger
					for(Goal iteratedGoal : iteratedEntity.goalSelector.getAvailableGoals())
					{
						boolean slamGoal = iteratedGoal.equals(this);
						if(slamGoal)
							iteratedEntity.setCooldown(GROUP_SLAM_COOLDOWN + phasedAnimation.getTotalAnimationLength()); //plays at beginning to prevent overlapping
					}
				}
			}
		}
	}
	
	@Override
	public void attemptToLandAttack(PathfinderMob attacker, LivingEntity target)
	{
		Level level = entity.getLevel();
		level.playSound(null, this.entity.blockPosition(), MSSoundEvents.ENTITY_SLAM.get(), SoundSource.HOSTILE, 1, 1);
		
		//flinging any nearby smaller mobs on the ground indiscriminately
		AABB aabb = new AABB(attacker.blockPosition()).inflate(4);
		List<LivingEntity> entityList = level.getEntitiesOfClass(LivingEntity.class, aabb);
		if(!entityList.isEmpty())
		{
			for(LivingEntity iteratedEntity : entityList)
			{
				boolean smallerThanAttacker = iteratedEntity.getBoundingBox().getSize() < attacker.getBoundingBox().getSize();
				
				if(iteratedEntity != attacker && iteratedEntity != target && iteratedEntity.isOnGround() && !iteratedEntity.noPhysics && smallerThanAttacker)
				{
					fling(attacker, iteratedEntity, false);
				}
			}
		}
		
		//flinging the target mob
		if(target != null && targetCanBeHit(target) && target instanceof Player playerTarget && playerTarget.isOnGround())
		{
			boolean inMeleeRange = getStandardAttackReachSqr(target) >= entity.distanceToSqr(target);
			
			//slam will connect if the player is close or if the player is farther away but not crouching, always requires player be on the ground
			if(inMeleeRange || !playerTarget.isCrouching())
			{
				fling(attacker, playerTarget, true);
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
			target.hurt(DamageSource.mobAttack(attacker), damage);
		}
		
		//non-player characters don't need to be damaged
		target.push(target.getRandom().nextFloat() - 0.5, 0.75, target.getRandom().nextFloat() - 0.5);
	}
}