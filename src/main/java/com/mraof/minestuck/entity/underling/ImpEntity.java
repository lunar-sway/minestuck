package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.entity.ai.attack.MoveToTargetGoal;
import com.mraof.minestuck.entity.ai.attack.SlowAttackWhenInRangeGoal;
import com.mraof.minestuck.entity.ai.attack.ZeroMovementDuringAttack;
import com.mraof.minestuck.entity.animation.MSMobAnimation;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.util.AnimationControllerUtil;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.player.PlayerSavedData;
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
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class ImpEntity extends UnderlingEntity implements IAnimatable
{
	public static final MSMobAnimation CLAW_ANIMATION = new MSMobAnimation(MSMobAnimation.Actions.CLAW, 14, true);
	public static final int CLAW_DELAY = 4;
	public static final int CLAW_RECOVERY = CLAW_ANIMATION.getAnimationLength() - CLAW_DELAY; //10
	
	public ImpEntity(EntityType<? extends ImpEntity> type, Level level)
	{
		super(type, level, 1);
	}
	
	public static AttributeSupplier.Builder impAttributes()
	{
		return UnderlingEntity.underlingAttributes().add(Attributes.MAX_HEALTH, 6)
				.add(Attributes.MOVEMENT_SPEED, 0.28).add(Attributes.ATTACK_DAMAGE, 1);
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		
		this.goalSelector.addGoal(2, new SlowAttackWhenInRangeGoal<>(this, CLAW_DELAY, CLAW_RECOVERY, CLAW_ANIMATION));
		this.goalSelector.addGoal(2, new ZeroMovementDuringAttack<>(this));
		this.goalSelector.addGoal(3, new MoveToTargetGoal(this, 1F, false));
	}
	
	@Override
	public GristSet getGristSpoils()
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
		if(this.dead && !this.level.isClientSide)
		{
			computePlayerProgress((int) (5 + 2 * getGristType().getPower())); //most imps stop giving xp at rung 8
			firstKillBonus(entity, Echeladder.UNDERLING_BONUS_OFFSET);
		}
	}
	
	@Override
	protected boolean isAppropriateTarget(LivingEntity entity)
	{
		if(entity instanceof ServerPlayer)
		{
			//Rung was chosen fairly arbitrary. Feel free to change it if you think a different rung is better
			return PlayerSavedData.getData((ServerPlayer) entity).getEcheladder().getRung() < 19;
		}
		return super.isAppropriateTarget(entity);
	}
	
	@Override
	public void registerControllers(AnimationData data)
	{
		data.addAnimationController(AnimationControllerUtil.createAnimation(this, "idleAnimation", 1, ImpEntity::idleAnimation));
		data.addAnimationController(AnimationControllerUtil.createAnimation(this, "walkArmsAnimation", 1, ImpEntity::walkArmsAnimation));
		data.addAnimationController(AnimationControllerUtil.createAnimation(this, "walkAnimation", 0.5, ImpEntity::walkAnimation));
		data.addAnimationController(AnimationControllerUtil.createAnimation(this, "deathAnimation", 0.7, ImpEntity::deathAnimation));
		data.addAnimationController(AnimationControllerUtil.createAnimation(this, "swingAnimation", 2, ImpEntity::swingAnimation));
	}
	
	private static PlayState idleAnimation(AnimationEvent<ImpEntity> event)
	{
		if(!event.isMoving() && !event.getAnimatable().isAggressive())
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.minestuck.imp.idle", true));
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}
	
	private static PlayState walkAnimation(AnimationEvent<ImpEntity> event)
	{
		if(!event.isMoving())
		{
			return PlayState.STOP;
		}
		
		if(event.getAnimatable().isAggressive())
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.minestuck.imp.run", true));
			return PlayState.CONTINUE;
		} else
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.minestuck.imp.walk", true));
			return PlayState.CONTINUE;
		}
	}
	
	private static PlayState walkArmsAnimation(AnimationEvent<ImpEntity> event)
	{
		if(!event.isMoving() || event.getAnimatable().isActive())
		{
			return PlayState.STOP;
		}
		
		if(event.getAnimatable().isAggressive())
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.minestuck.imp.runarms", true));
			return PlayState.CONTINUE;
		} else
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.minestuck.imp.walkarms", true));
			return PlayState.CONTINUE;
		}
	}
	
	private static PlayState deathAnimation(AnimationEvent<ImpEntity> event)
	{
		if(event.getAnimatable().dead)
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.minestuck.imp.die", false));
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}
	
	private static PlayState swingAnimation(AnimationEvent<ImpEntity> event)
	{
		if(event.getAnimatable().isActive())
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.minestuck.imp.scratch", false));
			return PlayState.CONTINUE;
		}
		event.getController().markNeedsReload();
		return PlayState.STOP;
	}
}