package com.mraof.minestuck.entity.ai.attack;

import com.mraof.minestuck.entity.AnimatedPathfinderMob;
import com.mraof.minestuck.entity.animation.PhasedMobAnimation;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;

/**
 * Alternative attack for when the target is out of standard range, results in players being flung into the air if they were on the ground and not crouching.
 */
public class GroundSlamGoal<T extends AnimatedPathfinderMob & PhasedMobAnimation.Phases.Holder> extends AnimatedAttackWhenInRangeGoal<T>
{
	public GroundSlamGoal(T entity, PhasedMobAnimation animation, float minRange, float maxRange)
	{
		super(entity, animation, minRange, maxRange);
	}
	
	@Override
	public void attemptToLandAttack(PathfinderMob attacker, LivingEntity target)
	{
		this.entity.level.playSound(null, this.entity.blockPosition(), MSSoundEvents.ENTITY_SLAM.get(), SoundSource.HOSTILE, 1, 1);
		
		if(target != null && targetCanBeHit(target) && target instanceof Player playerTarget && playerTarget.isOnGround())
		{
			boolean inMeleeRange = getStandardAttackReachSqr(target) >= entity.distanceToSqr(target);
			
			//slam will connect if the player is close or if the player is farther away but not crouching, always requires player be on the ground
			if(inMeleeRange || !playerTarget.isCrouching())
			{
				//TODO figure out if handleEntityEvent/broadcastEntityEvent or another method can be used as an alternative to hurting the player in order to update. setDeltaMovement/push does not work on their own
				//after the clamp is applied: 10 damage at 1-2 blocks away, 3.4 damage at 5 blocks away, 1 damage at 10 blocks away, 0.2 damage at 15 blocks away
				double distanceModified = (1.2 / (Math.sqrt(this.entity.distanceToSqr(target)) * 0.05)) - 1.4;
				float damage = Math.min(Math.max((float) distanceModified, 0.2F), 10F);
				playerTarget.hurt(DamageSource.mobAttack(attacker), damage);
				playerTarget.push(playerTarget.getRandom().nextFloat() - 0.5, 0.75, playerTarget.getRandom().nextFloat() - 0.5);
			}
		}
	}
}