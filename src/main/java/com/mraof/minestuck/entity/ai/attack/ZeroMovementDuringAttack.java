package com.mraof.minestuck.entity.ai.attack;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.Goal;

import java.util.UUID;

/**
 * An alternative to {@link SlowAttackInPlaceGoal}.
 * While {@link SlowAttackInPlaceGoal} stops movement by interrupting the movement goal,
 * this goal avoids that by instead applying a movement modifier that sets movement speed to 0.
 * Also, while {@link SlowAttackInPlaceGoal} is used instead of {@link SlowAttackWhenInRangeGoal},
 * this goal is used on top of {@link SlowAttackWhenInRangeGoal}.
 */
public class ZeroMovementDuringAttack<T extends CreatureEntity & AttackState.Holder> extends Goal
{
	private static final UUID MOVEMENT_MODIFIER_ATTACKING_UUID = UUID.fromString("a2793876-ee17-11ec-8ea0-0242ac120002");
	private static final AttributeModifier MOVEMENT_MODIFIER_ATTACKING = new AttributeModifier(MOVEMENT_MODIFIER_ATTACKING_UUID, "Attacking movement reduction", -1, AttributeModifier.Operation.MULTIPLY_TOTAL);
	
	private final T entity;
	
	public ZeroMovementDuringAttack(T entity)
	{
		this.entity = entity;
	}
	
	@Override
	public boolean canUse()
	{
		return this.entity.isAttacking();
	}
	
	@Override
	public void start()
	{
		ModifiableAttributeInstance instance = this.entity.getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
		if(instance != null && !instance.hasModifier(MOVEMENT_MODIFIER_ATTACKING))
			instance.addTransientModifier(MOVEMENT_MODIFIER_ATTACKING);
	}
	
	@Override
	public void stop()
	{
		ModifiableAttributeInstance instance = this.entity.getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
		if(instance != null)
			instance.removeModifier(MOVEMENT_MODIFIER_ATTACKING);
	}
}
