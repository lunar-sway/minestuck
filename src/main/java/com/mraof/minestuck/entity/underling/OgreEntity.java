package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.MutableGristSet;
import com.mraof.minestuck.entity.ai.attack.AnimatedAttackWhenInRangeGoal;
import com.mraof.minestuck.entity.ai.attack.GroundSlamGoal;
import com.mraof.minestuck.entity.ai.attack.MoveToTargetGoal;
import com.mraof.minestuck.entity.animation.MobAnimation;
import com.mraof.minestuck.entity.animation.PhasedMobAnimation;
import com.mraof.minestuck.player.EcheladderBonusType;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.ParametersAreNonnullByDefault;

//Makes non-stop ogre puns
@ParametersAreNonnullByDefault
public class OgreEntity extends UnderlingEntity
{
	
	public static final PhasedMobAnimation RIGHT_PUNCH_PROPERTIES = new PhasedMobAnimation(new MobAnimation(MobAnimation.Action.RIGHT_PUNCH, 22, true, true), 3, 5, 6, 11);
	public static final PhasedMobAnimation LEFT_PUNCH_PROPERTIES = new PhasedMobAnimation(new MobAnimation(MobAnimation.Action.LEFT_PUNCH, 22, true, true), 3, 5, 6, 11);
	public static final PhasedMobAnimation SLAM_PROPERTIES = new PhasedMobAnimation(new MobAnimation(MobAnimation.Action.SLAM, 30, true, true), 6, 7, 9, 15);
	public static final RawAnimation WALK_ANIMATION = RawAnimation.begin().thenLoop("walk");
	public static final RawAnimation WALKARMS_ANIMATION = RawAnimation.begin().thenLoop("walkarms");
	public static final RawAnimation RIGHT_PUNCH_ANIMATION = RawAnimation.begin().then("right_punch", Animation.LoopType.PLAY_ONCE);
	public static final RawAnimation LEFT_PUNCH_ANIMATION = RawAnimation.begin().then("left_punch", Animation.LoopType.PLAY_ONCE);
	public static final RawAnimation SMASH_ANIMATION = RawAnimation.begin().then("smash", Animation.LoopType.PLAY_ONCE);
	public static final RawAnimation DIE_ANIMATION = RawAnimation.begin().then("die", Animation.LoopType.PLAY_ONCE);
	
	public OgreEntity(EntityType<? extends OgreEntity> type, Level level)
	{
		super(type, level, 3);
		this.setMaxUpStep(1.0F);
	}
	
	public static AttributeSupplier.Builder ogreAttributes()
	{
		return UnderlingEntity.underlingAttributes().add(Attributes.MAX_HEALTH, 50)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.4).add(Attributes.MOVEMENT_SPEED, 0.22)
				.add(Attributes.ATTACK_DAMAGE, 6).add(Attributes.ATTACK_KNOCKBACK, 12)
				.add(Attributes.ATTACK_SPEED, 0.5);
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		//no overlap in attack goal ranges
		this.goalSelector.addGoal(2, new AnimatedAttackWhenInRangeGoal<>(this, RIGHT_PUNCH_PROPERTIES, 0, AnimatedAttackWhenInRangeGoal.STANDARD_MELEE_RANGE, 40, 35.0F, 55.0F));
		this.goalSelector.addGoal(2, new AnimatedAttackWhenInRangeGoal<>(this, LEFT_PUNCH_PROPERTIES, 0, AnimatedAttackWhenInRangeGoal.STANDARD_MELEE_RANGE, 40, -35.0F, 55.0F));
		this.goalSelector.addGoal(3, new GroundSlamGoal<>(this, SLAM_PROPERTIES, AnimatedAttackWhenInRangeGoal.STANDARD_MELEE_RANGE, 15, 160));
		this.goalSelector.addGoal(3, new MoveToTargetGoal(this, 1F, false));
	}
	
	@Override
	protected SoundEvent getAmbientSound()
	{
		return MSSoundEvents.ENTITY_OGRE_AMBIENT.get();
	}
	
	@Override
	protected SoundEvent getDeathSound()
	{
		return MSSoundEvents.ENTITY_OGRE_DEATH.get();
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return MSSoundEvents.ENTITY_OGRE_HURT.get();
	}
	
	@Override
	public MutableGristSet getGristSpoils()
	{
		return GristHelper.generateUnderlingGristDrops(this, damageMap, 4);
	}
	
	@Override
	protected int getVitalityGel()
	{
		return random.nextInt(3) + 3;
	}
	
	@Override
	protected void onGristTypeUpdated(GristType type)
	{
		super.onGristTypeUpdated(type);
		applyGristModifier(Attributes.MAX_HEALTH, 13 * type.getPower(), AttributeModifier.Operation.ADDITION);
		applyGristModifier(Attributes.ATTACK_DAMAGE, 2.1 * type.getPower(), AttributeModifier.Operation.ADDITION);
		this.xpReward = (int) (5 * type.getPower() + 4);
	}
	
	@Override
	public void die(DamageSource cause)
	{
		super.die(cause);
		Entity entity = cause.getEntity();
		if(this.dead && !this.level().isClientSide)
		{
			computePlayerProgress((int) (15 + 2.2 * getGristType().getPower())); //most ogres stop giving xp at rung 18
			firstKillBonus(entity, EcheladderBonusType.OGRE);
		}
	}
	
	@Override
	public void initiationPhaseStart(MobAnimation.Action animation)
	{
		if(animation == MobAnimation.Action.RIGHT_PUNCH || animation == MobAnimation.Action.LEFT_PUNCH || animation == MobAnimation.Action.SLAM)
			this.playSound(MSSoundEvents.ENTITY_SWOOSH.get(), 0.5F, 0);
	}
	
	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers)
	{
		controllers.add(new AnimationController<>(this, "walkArmsAnimation", OgreEntity::walkArmsAnimation)
				.setAnimationSpeedHandler(entity -> MobAnimation.getAttributeAffectedSpeed(entity, Attributes.MOVEMENT_SPEED) * 2.27));
		controllers.add(new AnimationController<>(this, "walkAnimation", OgreEntity::walkAnimation)
				.setAnimationSpeedHandler(entity -> MobAnimation.getAttributeAffectedSpeed(entity, Attributes.MOVEMENT_SPEED) * 2.27));
		controllers.add(new AnimationController<>(this, "attackAnimation", OgreEntity::attackAnimation));
		controllers.add(new AnimationController<>(this, "deathAnimation", OgreEntity::deathAnimation).setAnimationSpeed(0.85));
	}
	
	private static PlayState walkAnimation(AnimationState<OgreEntity> state)
	{
		if(MobAnimation.isEntityMovingHorizontally(state.getAnimatable()))
		{
			state.getController().setAnimation(WALK_ANIMATION);
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}
	
	private static PlayState walkArmsAnimation(AnimationState<OgreEntity> state)
	{
		if(MobAnimation.isEntityMovingHorizontally(state.getAnimatable()) && !state.getAnimatable().isActive())
		{
			state.getController().setAnimation(WALKARMS_ANIMATION);
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}
	
	private static PlayState attackAnimation(AnimationState<OgreEntity> state)
	{
		MobAnimation.Action action = state.getAnimatable().getCurrentAction();
		
		if(action == MobAnimation.Action.RIGHT_PUNCH)
		{
			state.getController().setAnimation(RIGHT_PUNCH_ANIMATION);
			return PlayState.CONTINUE;
		} else if(action == MobAnimation.Action.LEFT_PUNCH)
		{
			state.getController().setAnimation(LEFT_PUNCH_ANIMATION);
			return PlayState.CONTINUE;
		} else if(action == MobAnimation.Action.SLAM)
		{
			state.getController().setAnimation(SMASH_ANIMATION);
			return PlayState.CONTINUE;
		}
		
		state.getController().forceAnimationReset();
		state.getController().setAnimationSpeed(MobAnimation.getAttributeAffectedSpeed(state.getAnimatable(), Attributes.ATTACK_SPEED)); //Setting animation speed on stop so it doesn't jump around when attack speed changes mid-attack
		return PlayState.STOP;
	}
	
	private static PlayState deathAnimation(AnimationState<OgreEntity> state)
	{
		if(state.getAnimatable().dead)
		{
			state.getController().setAnimation(DIE_ANIMATION);
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}
}
