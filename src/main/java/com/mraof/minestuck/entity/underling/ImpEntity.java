package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.item.crafting.alchemy.GristHelper;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.util.AnimationUtil;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class ImpEntity extends UnderlingEntity implements IAnimatable
{
	
	public ImpEntity(EntityType<? extends ImpEntity> type, World world)
	{
		super(type, world, 1);
	}
	
	public static AttributeModifierMap.MutableAttribute impAttributes()
	{
		return UnderlingEntity.underlingAttributes().add(Attributes.MAX_HEALTH, 6)
				.add(Attributes.MOVEMENT_SPEED, 0.28).add(Attributes.ATTACK_DAMAGE, 1);
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		
		this.goalSelector.addGoal(2, new SlowAttackInPlaceGoal(this, 4, 10));
		this.goalSelector.addGoal(3, new MoveToTargetGoal(this, 1F, false));
	}
	
	@Override
	public GristSet getGristSpoils()
	{
		return GristHelper.generateUnderlingGristDrops(this, damageMap, 1);
	}
	
	protected SoundEvent getAmbientSound()
	{
		return MSSoundEvents.ENTITY_IMP_AMBIENT;
	}
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return MSSoundEvents.ENTITY_IMP_HURT;
	}
	
	protected SoundEvent getDeathSound()
	{
		return MSSoundEvents.ENTITY_IMP_DEATH;
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
		if(entity instanceof ServerPlayerEntity)
		{
			//Rung was chosen fairly arbitrary. Feel free to change it if you think a different rung is better
			return PlayerSavedData.getData((ServerPlayerEntity) entity).getEcheladder().getRung() < 19;
		}
		return super.isAppropriateTarget(entity);
	}
	
	@Override
	public void registerControllers(AnimationData data)
	{
		data.addAnimationController(AnimationUtil.createAnimation(this, "idleAnimation", 1, ImpEntity::idleAnimation));
		data.addAnimationController(AnimationUtil.createAnimation(this, "walkArmsAnimation", 1, ImpEntity::walkArmsAnimation));
		data.addAnimationController(AnimationUtil.createAnimation(this, "walkAnimation", 0.5, ImpEntity::walkAnimation));
		data.addAnimationController(AnimationUtil.createAnimation(this, "deathAnimation", 0.7, ImpEntity::deathAnimation));
		data.addAnimationController(AnimationUtil.createAnimation(this, "swingAnimation", 2, ImpEntity::swingAnimation));
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
		if(!event.isMoving() || event.getAnimatable().isAttacking())
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
		if(event.getAnimatable().isAttacking())
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.minestuck.imp.scratch", false));
			return PlayState.CONTINUE;
		}
		event.getController().markNeedsReload();
		return PlayState.STOP;
	}
}