package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.MutableGristSet;
import com.mraof.minestuck.entity.ai.attack.AnimatedAttackWhenInRangeGoal;
import com.mraof.minestuck.entity.ai.attack.MoveToTargetGoal;
import com.mraof.minestuck.entity.animation.MobAnimation;
import com.mraof.minestuck.entity.animation.PhasedMobAnimation;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.player.EcheladderBonusType;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.FakePlayer;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ImpEntity extends UnderlingEntity implements GeoEntity
{
	public static final PhasedMobAnimation CLAW_PROPERTIES = new PhasedMobAnimation(new MobAnimation(MobAnimation.Action.CLAW, 8, true, false), 2, 5, 10, 16);
	private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("animation.minestuck.imp.idle");
	private static final RawAnimation RUN_ANIMATION = RawAnimation.begin().thenLoop("animation.minestuck.imp.run");
	private static final RawAnimation WALK_ANIMATION = RawAnimation.begin().thenLoop("animation.minestuck.imp.walk");
	private static final RawAnimation RUNARMS_ANIMATION = RawAnimation.begin().thenLoop("animation.minestuck.imp.runarms");
	private static final RawAnimation WALKARMS_ANIMATION = RawAnimation.begin().thenLoop("animation.minestuck.imp.walkarms");
	private static final RawAnimation DIE_ANIMATION = RawAnimation.begin().then("animation.minestuck.imp.die", Animation.LoopType.PLAY_ONCE);
	private static final RawAnimation SCRATCH_ANIMATION = RawAnimation.begin().then("animation.minestuck.imp.scratch", Animation.LoopType.PLAY_ONCE);
	
	public ImpEntity(EntityType<? extends ImpEntity> type, Level level)
	{
		super(type, level, 1);
	}
	
	public static AttributeSupplier.Builder impAttributes()
	{
		return UnderlingEntity.underlingAttributes().add(Attributes.MAX_HEALTH, 6)
				.add(Attributes.MOVEMENT_SPEED, 0.28).add(Attributes.ATTACK_DAMAGE, 2).add(Attributes.ATTACK_SPEED, 2);
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		
		this.goalSelector.addGoal(2, new AnimatedAttackWhenInRangeGoal<>(this, CLAW_PROPERTIES));
		this.goalSelector.addGoal(3, new MoveToTargetGoal(this, 1F, false));
	}
	
	@Override
	public MutableGristSet getGristSpoils()
	{
		return GristHelper.generateUnderlingGristDrops(this, damageMap, 1);
	}
	
	protected SoundEvent getAmbientSound()
	{
		return MSSoundEvents.ENTITY_IMP_AMBIENT.get();
	}
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return MSSoundEvents.ENTITY_IMP_HURT.get();
	}
	
	protected SoundEvent getDeathSound()
	{
		return MSSoundEvents.ENTITY_IMP_DEATH.get();
	}
	
	@Override
	protected int getVitalityGel()
	{
		return random.nextInt(3) + 1;
	}
	
	@Override
	protected void onGristTypeUpdated(GristType type)
	{
		super.onGristTypeUpdated(type);
		applyGristModifier(Attributes.MAX_HEALTH, 8 * type.getPower(), AttributeModifier.Operation.ADDITION);
		applyGristModifier(Attributes.ATTACK_DAMAGE, Math.ceil(type.getPower()), AttributeModifier.Operation.ADDITION);
		this.xpReward = (int) (3 * type.getPower() + 1);
	}
	
	@Override
	public void die(DamageSource cause)
	{
		super.die(cause);
		Entity entity = cause.getEntity();
		if(this.dead && !this.level().isClientSide)
		{
			computePlayerProgress((int) (5 + 2 * getGristType().getPower())); //most imps stop giving xp at rung 8
			firstKillBonus(entity, EcheladderBonusType.IMP);
		}
	}
	
	@Override
	protected boolean isAppropriateTarget(LivingEntity entity)
	{
		//imps will not attack players above rung 15 unless an underling is attacked in their presence
		if(entity instanceof ServerPlayer player && !(entity instanceof FakePlayer))
		{
			return Echeladder.get(player).getRung() < 16;
		}
		return super.isAppropriateTarget(entity);
	}
	
	@Override
	public void initiationPhaseStart(MobAnimation.Action animation)
	{
		if(animation == MobAnimation.Action.CLAW)
			this.playSound(MSSoundEvents.ENTITY_SWOOSH.get(), 0.2F, 1.75F);
	}
	
	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers)
	{
		controllers.add(new AnimationController<>(this, "idleAnimation", ImpEntity::idleAnimation));
		controllers.add(new AnimationController<>(this, "walkArmsAnimation", ImpEntity::walkArmsAnimation)
				.setAnimationSpeedHandler(entity -> MobAnimation.getAttributeAffectedSpeed(entity, Attributes.MOVEMENT_SPEED) * 1.785));
		controllers.add(new AnimationController<>(this, "walkAnimation", ImpEntity::walkAnimation)
				.setAnimationSpeedHandler(entity -> MobAnimation.getAttributeAffectedSpeed(entity, Attributes.MOVEMENT_SPEED) * 1.785));
		controllers.add(new AnimationController<>(this, "deathAnimation", ImpEntity::deathAnimation).setAnimationSpeed(0.7));
		controllers.add(new AnimationController<>(this, "attackAnimation", ImpEntity::attackAnimation));
	}
	
	private static PlayState idleAnimation(AnimationState<ImpEntity> state)
	{
		if(!state.isMoving() && !state.getAnimatable().isAggressive())
		{
			state.getController().setAnimation(IDLE_ANIMATION);
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}
	
	private static PlayState walkAnimation(AnimationState<ImpEntity> state)
	{
		if(!MobAnimation.isEntityMovingHorizontally(state.getAnimatable()))
		{
			return PlayState.STOP;
		}
		
		if(state.getAnimatable().isAggressive())
		{
			state.getController().setAnimation(RUN_ANIMATION);
		} else
		{
			state.getController().setAnimation(WALK_ANIMATION);
		}
		return PlayState.CONTINUE;
	}
	
	private static PlayState walkArmsAnimation(AnimationState<ImpEntity> state)
	{
		if(!MobAnimation.isEntityMovingHorizontally(state.getAnimatable()) || state.getAnimatable().isActive())
		{
			return PlayState.STOP;
		}
		
		if(state.getAnimatable().isAggressive())
		{
			state.getController().setAnimation(RUNARMS_ANIMATION);
		} else
		{
			state.getController().setAnimation(WALKARMS_ANIMATION);
		}
		return PlayState.CONTINUE;
	}
	
	private static PlayState deathAnimation(AnimationState<ImpEntity> state)
	{
		if(state.getAnimatable().dead)
		{
			state.getController().setAnimation(DIE_ANIMATION);
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}
	
	private static PlayState attackAnimation(AnimationState<ImpEntity> state)
	{
		if(state.getAnimatable().isActive())
		{
			state.getController().setAnimation(SCRATCH_ANIMATION);
			return PlayState.CONTINUE;
		}
		state.getController().forceAnimationReset();
		state.getController().setAnimationSpeed(MobAnimation.getAttributeAffectedSpeed(state.getAnimatable(), Attributes.ATTACK_SPEED)); //Setting animation speed on stop so it doesn't jump around when attack speed changes mid-attack
		return PlayState.STOP;
	}
}

