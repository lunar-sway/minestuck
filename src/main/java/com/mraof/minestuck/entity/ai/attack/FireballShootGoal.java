package com.mraof.minestuck.entity.ai.attack;

import com.mraof.minestuck.entity.AttackingAnimatedEntity;
import com.mraof.minestuck.entity.animation.PhasedMobAnimation;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Objects;

/**
 * Alternative attack for when the attack target is out of standard range, a small fireball is shot at the targets initial position.
 */
public class FireballShootGoal<T extends AttackingAnimatedEntity> extends AnimatedAttackWhenInRangeGoal<T>
{
	public static final int GROUP_SHOOT_COOLDOWN = 20; //how long it should take before another nearby mob can shoot fireballs
	
	/**
	 * Block position that the target was standing at when start() was ran
	 */
	private BlockPos initialTargetPos;
	
	public FireballShootGoal(T entity, PhasedMobAnimation animation, float minRange, float maxRange, int actionCooldown)
	{
		super(entity, animation, minRange, maxRange, actionCooldown, 0, 180.0F);
	}
	
	@Override
	public boolean canUse()
	{
		return super.canUse() && entity.hasFinishedCooldown() && noEntitiesObstructing(this.entity.getTarget()); //the super implies the target is not null
	}
	
	@Override
	public void start()
	{
		super.start();
		
		initialTargetPos = Objects.requireNonNull(this.entity.getTarget()).blockPosition();
		
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
				if(iteratedEntity != this.entity && iteratedEntity.getType() == this.entity.getType() && !iteratedEntity.existingCooldownIsLonger(GROUP_SHOOT_COOLDOWN))
				{
					iteratedEntity.setCooldown(GROUP_SHOOT_COOLDOWN + phasedAnimation.getTotalAnimationLength(speed)); //plays at beginning to prevent overlapping
				}
			}
		}
	}
	
	@Override
	public void attemptToLandAttack(PathfinderMob attacker, LivingEntity target)
	{
		Level level = entity.level();
		level.playSound(null, this.entity.blockPosition(), SoundEvents.FIRECHARGE_USE, SoundSource.HOSTILE, 1, 1);
		
		//fireball is shot towards the initial target position
		if(initialTargetPos != null)
		{
			double distanceX = initialTargetPos.getX() - attacker.getX();
			double distanceY = initialTargetPos.getY() - (attacker.getY() + (double) (attacker.getBbHeight() / 2.0F));
			double distanceZ = initialTargetPos.getZ() - attacker.getZ();
			
			SmallFireball fireball = new SmallFireball(level, attacker, distanceX, distanceY, distanceZ);
			double bbHeight = attacker.getBbHeight();
			Vec3 viewVec = attacker.getViewVector(1.0F);
			double x = (attacker.getBoundingBox().minX + attacker.getBoundingBox().maxX) / 2.0F + viewVec.x * bbHeight;
			double y = attacker.getY() + (double) (attacker.getBbHeight() / 2.0F);
			double z = (attacker.getBoundingBox().minZ + attacker.getBoundingBox().maxZ) / 2.0F + viewVec.z * bbHeight;
			fireball.setPos(x, y, z);
			level.addFreshEntity(fireball);
		}
	}
	
	private boolean noEntitiesObstructing(LivingEntity target)
	{
		//simulates the basilisk looking at the target, before setting its head rotation back to the way it was
		float initialYHeadRotO = this.entity.yHeadRotO;
		float initialYBodyRot = this.entity.yBodyRot;
		float initialYBodyRotO = this.entity.yBodyRotO;
		this.entity.lookAt(target.createCommandSourceStack().getAnchor(), target.position());
		Vec3 targetFacingVec = this.entity.getLookAngle();
		this.entity.yHeadRotO = initialYHeadRotO;
		this.entity.yBodyRot = initialYBodyRot;
		this.entity.yBodyRotO = initialYBodyRotO;
		
		int distanceBetweenPos = (int) Math.sqrt(this.entity.blockPosition().distSqr(target.blockPosition()));
		
		for(int step = 0; step < distanceBetweenPos; step++)
		{
			Vec3 eyePos = this.entity.getEyePosition(1F);
			Vec3 vecPos = eyePos.add(targetFacingVec.scale(step));
			
			List<LivingEntity> livingEntityList = entityWithinAABB(this.entity.level(), vecPos, step + 1 == distanceBetweenPos);
			
			for(LivingEntity livingEntityIterate : livingEntityList)
			{
				if(livingEntityIterate != null && livingEntityIterate != this.entity && livingEntityIterate != target)
					return false; //if there is a non-null entity that is not either the attacker or the target, that means there is an obstructing entity
			}
		}
		
		return true; //if no entities are found between the position of the attacker and raytraced steps towards the target, the attacker is good to proceed
	}
	
	/**
	 * @param level    level of entity
	 * @param vecPos   the current position along the raytrace
	 * @param lastStep if this is the last iteration of the function, expands the AABB so as not to catch other combatants
	 */
	private List<LivingEntity> entityWithinAABB(Level level, Vec3 vecPos, boolean lastStep)
	{
		AABB axisAlignedBB = new AABB(BlockPos.containing(vecPos)).inflate(lastStep ? 4 : 0.75);
		return level.getEntitiesOfClass(LivingEntity.class, axisAlignedBB);
	}
}