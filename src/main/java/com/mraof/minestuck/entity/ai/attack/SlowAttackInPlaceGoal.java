package com.mraof.minestuck.entity.ai.attack;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;

import java.util.EnumSet;

/**
 * Like {@link SlowAttackWhenInRangeGoal}, but interrupts any movement and look goals to stand still and look at the target.
 */
public class SlowAttackInPlaceGoal<T extends CreatureEntity & AttackState.Holder> extends SlowAttackWhenInRangeGoal<T>
{
	public SlowAttackInPlaceGoal(T entity, int attackDelay, int attackRecovery)
	{
		super(entity, attackDelay, attackRecovery);
		// Will stop any other goal with movement or looking if this goal activates
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}
	
	@Override
	public void tick()
	{
		LivingEntity target = this.entity.getTarget();
		if(target != null)
			this.entity.getLookControl().setLookAt(target, 30.0F, 30.0F);
		super.tick();
	}
}
