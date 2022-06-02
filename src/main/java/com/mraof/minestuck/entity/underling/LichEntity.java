package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.item.crafting.alchemy.GristHelper;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class LichEntity extends UnderlingEntity implements IAnimatable
{
	private final AttributeModifier knockback;
	
	public LichEntity(EntityType<? extends LichEntity> type, World world)
	{
		super(type, world, 7);
		this.attackDelay = 14;
		this.attackRecovery = 16;
		this.canMoveWhileAttacking = true;
		knockback = new AttributeModifier("attack.knockback", 1, AttributeModifier.Operation.ADDITION);
	}
	
	public static AttributeModifierMap.MutableAttribute lichAttributes()
	{
		return UnderlingEntity.underlingAttributes().add(Attributes.MAX_HEALTH, 175)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.3).add(Attributes.MOVEMENT_SPEED, 0.25)
				.add(Attributes.ATTACK_DAMAGE, 8);
	}
	
	protected SoundEvent getAmbientSound()
	{
		return MSSoundEvents.ENTITY_LICH_AMBIENT;
	}
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return MSSoundEvents.ENTITY_LICH_HURT;
	}
	
	protected SoundEvent getDeathSound()
	{
		return MSSoundEvents.ENTITY_LICH_DEATH;
	}
	
	@Override
	public GristSet getGristSpoils()
	{
		return GristHelper.generateUnderlingGristDrops(this, damageMap, 8);
	}
	
	@Override
	protected int getVitalityGel()
	{
		return random.nextInt(3) + 6;
	}
	
	@Override
	protected void onGristTypeUpdated(GristType type)
	{
		super.onGristTypeUpdated(type);
		applyGristModifier(Attributes.MAX_HEALTH, 30 * type.getPower(), AttributeModifier.Operation.ADDITION);
		applyGristModifier(Attributes.ATTACK_DAMAGE, 3.4 * type.getPower(), AttributeModifier.Operation.ADDITION);
		this.xpReward = (int) (6.5 * type.getPower() + 4);
	}
	
	@Override
	public void die(DamageSource cause)
	{
		super.die(cause);
		Entity killer = cause.getEntity();
		if(this.dead && !this.level.isClientSide)
		{
			computePlayerProgress((int) (50 + 2.6 * getGristType().getPower())); //still give xp up to top rung
			firstKillBonus(killer, (byte) (Echeladder.UNDERLING_BONUS_OFFSET + 3));
		}
	}
	
	@Override
	protected void onAttackStart()
	{
		ModifiableAttributeInstance instance = getAttributes().getInstance(Attributes.KNOCKBACK_RESISTANCE);
		if(instance != null && !instance.hasModifier(knockback))
			instance.addTransientModifier(knockback);
	}
	
	@Override
	protected void onAttackEnd()
	{
		ModifiableAttributeInstance instance = getAttributes().getInstance(Attributes.KNOCKBACK_RESISTANCE);
		if(instance != null)
			instance.removeModifier(knockback);
	}
	
	@Override
	public void registerControllers(AnimationData data)
	{
		data.addAnimationController(createAnimation("walkAnimation", 1, this::walkAnimation));
		data.addAnimationController(createAnimation("deathAnimation", 1, this::deathAnimation));
		data.addAnimationController(createAnimation("swingAnimation", 0.8, this::swingAnimation));
	}
	
	private <E extends IAnimatable> PlayState walkAnimation(AnimationEvent<E> event)
	{
		if(!event.isMoving())
		{
			return PlayState.STOP;
		}
		event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", true));
		return PlayState.CONTINUE;
	}
	
	private <E extends IAnimatable> PlayState deathAnimation(AnimationEvent<E> event)
	{
		if(dead)
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("die", false));
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}
	
	private <E extends IAnimatable> PlayState swingAnimation(AnimationEvent<E> event)
	{
		if(isAttacking())
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("attack", false));
			return PlayState.CONTINUE;
		}
		event.getController().markNeedsReload();
		return PlayState.STOP;
	}
}