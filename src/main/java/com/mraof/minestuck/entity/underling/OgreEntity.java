package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.entity.ai.attack.AnimatedAttackWhenInRangeGoal;
import com.mraof.minestuck.entity.ai.attack.GroundSlamGoal;
import com.mraof.minestuck.entity.ai.attack.MoveToTargetGoal;
import com.mraof.minestuck.entity.animation.MobAnimation;
import com.mraof.minestuck.entity.animation.PhasedMobAnimation;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.util.AnimationControllerUtil;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

//Makes non-stop ogre puns
public class OgreEntity extends UnderlingEntity
{
	public static final PhasedMobAnimation PUNCH_ANIMATION = new PhasedMobAnimation(new MobAnimation(MobAnimation.Action.PUNCH, 22, true, true), 8, 10, 13);
	public static final PhasedMobAnimation SLAM_ANIMATION = new PhasedMobAnimation(new MobAnimation(MobAnimation.Action.SLAM, 26, true, true), 12, 14, 17);
	
	public OgreEntity(EntityType<? extends OgreEntity> type, Level level)
	{
		super(type, level, 3);
		this.maxUpStep = 1.0F;
	}
	
	public static AttributeSupplier.Builder ogreAttributes()
	{
		return UnderlingEntity.underlingAttributes().add(Attributes.MAX_HEALTH, 50)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.4).add(Attributes.MOVEMENT_SPEED, 0.22)
				.add(Attributes.ATTACK_DAMAGE, 6);
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		this.goalSelector.addGoal(2, new AnimatedAttackWhenInRangeGoal<>(this, PUNCH_ANIMATION));
		this.goalSelector.addGoal(3, new GroundSlamGoal<>(this, SLAM_ANIMATION, AnimatedAttackWhenInRangeGoal.STANDARD_MELEE_RANGE, 15));
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
	public GristSet getGristSpoils()
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
		if(this.dead && !this.level.isClientSide)
		{
			computePlayerProgress((int) (15 + 2.2 * getGristType().getPower())); //most ogres stop giving xp at rung 18
			firstKillBonus(entity, (byte) (Echeladder.UNDERLING_BONUS_OFFSET + 1));
		}
	}
	
	@Override
	public void registerControllers(AnimationData data)
	{
		data.addAnimationController(AnimationControllerUtil.createAnimation(this, "walkArmsAnimation", 0.3, OgreEntity::walkArmsAnimation));
		data.addAnimationController(AnimationControllerUtil.createAnimation(this, "walkAnimation", 0.3, OgreEntity::walkAnimation));
		data.addAnimationController(AnimationControllerUtil.createAnimation(this, "swingAnimation", 0.5, OgreEntity::swingAnimation));
		data.addAnimationController(AnimationControllerUtil.createAnimation(this, "deathAnimation", 0.85, OgreEntity::deathAnimation));
	}
	
	private static PlayState walkAnimation(AnimationEvent<OgreEntity> event)
	{
		if(event.isMoving())
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.ogre.walk", true));
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}
	
	private static PlayState walkArmsAnimation(AnimationEvent<OgreEntity> event)
	{
		if(event.isMoving() && !event.getAnimatable().isActive())
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.ogre.walkarms", true));
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}
	
	private static PlayState swingAnimation(AnimationEvent<OgreEntity> event)
	{
		MobAnimation.Action action = event.getAnimatable().getCurrentAction();
		
		if(action == MobAnimation.Action.PUNCH)
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.ogre.punch", false));
			return PlayState.CONTINUE;
		} else if(action == MobAnimation.Action.SLAM)
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.ogre.smash", false));
			return PlayState.CONTINUE;
		}
		
		event.getController().markNeedsReload();
		return PlayState.STOP;
	}
	
	private static PlayState deathAnimation(AnimationEvent<OgreEntity> event)
	{
		if(event.getAnimatable().dead)
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.ogre.die", false));
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}
}