package com.mraof.minestuck.entity.ai.attack;

import com.mojang.math.Vector3d;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;

import java.util.EnumSet;
import java.util.Objects;

/**
 * Like {@link SlowAttackWhenInRangeGoal}, but interrupts any movement and look goals to stand still and look at the target.
 */
public class SlowAttackInPlaceGoal<T extends PathfinderMob & AttackState.Holder> extends SlowAttackWhenInRangeGoal<T>
{
	private Vector3d lookTarget;
	
	public SlowAttackInPlaceGoal(T entity, int attackDelay, int attackRecovery)
	{
		super(entity, attackDelay, attackRecovery);
		// Will stop any other goal with movement or looking if this goal activates
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}
	
	@Override
	public void start()
	{
		//the target should be guaranteed to be non-null because canUse() requires it to be non-null.
		LivingEntity target = Objects.requireNonNull(this.entity.getTarget());
		this.lookTarget = new Vector3d(target.getX(), target.getEyeY(), target.getZ());
		super.start();
	}
	
	@Override
	public void tick()
	{
		this.entity.getLookControl().setLookAt(lookTarget.x, lookTarget.y, lookTarget.z, 30.0F, 30.0F);
		super.tick();
	}
}
