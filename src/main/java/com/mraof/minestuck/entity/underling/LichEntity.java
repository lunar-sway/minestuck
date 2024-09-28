package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.MutableGristSet;
import com.mraof.minestuck.entity.ai.attack.AnimatedAttackWhenInRangeGoal;
import com.mraof.minestuck.entity.ai.attack.MoveToTargetGoal;
import com.mraof.minestuck.entity.animation.MobAnimation;
import com.mraof.minestuck.entity.animation.PhasedMobAnimation;
import com.mraof.minestuck.player.EcheladderBonusType;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class LichEntity extends UnderlingEntity implements GeoEntity
{
	
	public static final PhasedMobAnimation CLAW_PROPERTIES = new PhasedMobAnimation(new MobAnimation(MobAnimation.Action.CLAW, 10, false, false), 11, 13, 16, 22);
	private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("idle");
	private static final RawAnimation CLAW_LEGS_ANIMATION = RawAnimation.begin().then("claw_legs", Animation.LoopType.PLAY_ONCE);
	private static final RawAnimation WALK_ANIMATION = RawAnimation.begin().thenLoop("walk");
	private static final RawAnimation DIE_ANIMATION = RawAnimation.begin().then("die", Animation.LoopType.PLAY_ONCE);
	private static final RawAnimation CLAW_ARMS_ANIMATION = RawAnimation.begin().then("claw_arms", Animation.LoopType.PLAY_ONCE);
	
	public LichEntity(EntityType<? extends LichEntity> type, Level level)
	{
		super(type, level, 7);
	}
	
	public static AttributeSupplier.Builder lichAttributes()
	{
		return UnderlingEntity.underlingAttributes().add(Attributes.MAX_HEALTH, 175)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.3).add(Attributes.MOVEMENT_SPEED, 0.25)
				.add(Attributes.ATTACK_DAMAGE, 8).add(Attributes.ATTACK_SPEED, 2.25);
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		this.goalSelector.addGoal(1, new AttackResistanceGoal());
		this.goalSelector.addGoal(2, new AnimatedAttackWhenInRangeGoal<>(this, CLAW_PROPERTIES, 0, AnimatedAttackWhenInRangeGoal.STANDARD_MELEE_RANGE, AnimatedAttackWhenInRangeGoal.NO_COOLDOWN, 0, 45.0F));
		this.goalSelector.addGoal(3, new MoveToTargetGoal(this, 1F, false));
	}
	
	@Override
	protected SoundEvent getAmbientSound()
	{
		return MSSoundEvents.ENTITY_LICH_AMBIENT.get();
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return MSSoundEvents.ENTITY_LICH_HURT.get();
	}
	
	@Override
	protected SoundEvent getDeathSound()
	{
		return MSSoundEvents.ENTITY_LICH_DEATH.get();
	}
	
	@Override
	public MutableGristSet getGristSpoils()
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
	protected void tickDeath()
	{
		this.deathTime++;
		if (this.deathTime == 90 && !this.level().isClientSide())
		{
			this.level().broadcastEntityEvent(this, (byte) 60);
			this.remove(Entity.RemovalReason.KILLED);
		}
	}
	
	@Override
	public void die(DamageSource cause)
	{
		super.die(cause);
		Entity killer = cause.getEntity();
		if(this.dead && !this.level().isClientSide)
		{
			computePlayerProgress((int) (50 + 2.6 * getGristType().getPower())); //still give xp up to top rung
			firstKillBonus(killer, EcheladderBonusType.LICH);
		}
	}
	
	@Override
	public void initiationPhaseStart(MobAnimation.Action animation)
	{
		if(animation == MobAnimation.Action.CLAW)
			this.playSound(MSSoundEvents.ENTITY_SWOOSH.get(), 0.2F, 1.75F);
	}
	
	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controller)
	{
		controller.add(new AnimationController<>(this, "idleAnimation", LichEntity::idleAnimation));
		controller.add(new AnimationController<>(this, "walkAnimation", LichEntity::walkAnimation)
				.setAnimationSpeedHandler(entity -> MobAnimation.getAttributeAffectedSpeed(entity, Attributes.MOVEMENT_SPEED) * 4));
		controller.add(new AnimationController<>(this, "deathAnimation", LichEntity::deathAnimation));
		controller.add(new AnimationController<>(this, "attackAnimation", LichEntity::attackAnimation));
	}
	
	private static PlayState idleAnimation(AnimationState<LichEntity> state)
	{
		if(state.isMoving() || state.getAnimatable().getCurrentAction() != MobAnimation.Action.IDLE)
		{
			return PlayState.STOP;
		}
		
		state.getController().setAnimation(IDLE_ANIMATION);
		return PlayState.CONTINUE;
	}
	
	private static PlayState walkAnimation(AnimationState<LichEntity> state)
	{
		MobAnimation.Action action = state.getAnimatable().getCurrentAction();
		
		if(action == MobAnimation.Action.CLAW)
		{
			state.getController().setAnimation(CLAW_LEGS_ANIMATION);
			return PlayState.CONTINUE;
		} else if(!MobAnimation.isEntityMovingHorizontally(state.getAnimatable()))
		{
			return PlayState.STOP;
		} else
		{
			state.getController().setAnimation(WALK_ANIMATION);
			return PlayState.CONTINUE;
		}
	}
	
	private static PlayState deathAnimation(AnimationState<LichEntity> state)
	{
		if(state.getAnimatable().dead)
		{
			state.getController().setAnimation(DIE_ANIMATION);
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}
	
	private static PlayState attackAnimation(AnimationState<LichEntity> state)
	{
		if(state.getAnimatable().isActive())
		{
			state.getController().setAnimation(CLAW_ARMS_ANIMATION);
			return PlayState.CONTINUE;
		}
		state.getController().forceAnimationReset();
		state.getController().setAnimationSpeed(MobAnimation.getAttributeAffectedSpeed(state.getAnimatable(), Attributes.ATTACK_SPEED)); //Setting animation speed on stop so it doesn't jump around when attack speed changes mid-attack
		return PlayState.STOP;
	}
	
	private static final UUID RESISTANCE_MODIFIER_ATTACKING_UUID = UUID.fromString("7f03c94c-e287-11ec-8fea-0242ac120002");
	private static final AttributeModifier RESISTANCE_MODIFIER_ATTACKING = new AttributeModifier(RESISTANCE_MODIFIER_ATTACKING_UUID, "Attacking resistance boost", 1, AttributeModifier.Operation.ADDITION);
	
	private class AttackResistanceGoal extends Goal
	{
		@Override
		public boolean canUse()
		{
			return LichEntity.this.isBeforeContact();
		}
		
		@Override
		public void start()
		{
			AttributeInstance instance = getAttributes().getInstance(Attributes.KNOCKBACK_RESISTANCE);
			if(instance != null && !instance.hasModifier(RESISTANCE_MODIFIER_ATTACKING))
				instance.addTransientModifier(RESISTANCE_MODIFIER_ATTACKING);
		}
		
		@Override
		public void stop()
		{
			AttributeInstance instance = getAttributes().getInstance(Attributes.KNOCKBACK_RESISTANCE);
			if(instance != null)
				instance.removeModifier(RESISTANCE_MODIFIER_ATTACKING.getId());
		}
	}
}
