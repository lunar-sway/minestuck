package com.mraof.minestuck.entity.underling;

import com.mojang.math.Vector3d;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.entity.ai.attack.FireballShootGoal;
import com.mraof.minestuck.entity.ai.attack.MoveToTargetGoal;
import com.mraof.minestuck.entity.ai.attack.AnimatedAttackWhenInRangeGoal;
import com.mraof.minestuck.entity.animation.MobAnimation;
import com.mraof.minestuck.entity.animation.PhasedMobAnimation;
import com.mraof.minestuck.player.EcheladderBonusType;
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
import net.minecraftforge.entity.PartEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class BasiliskEntity extends UnderlingEntity implements IAnimatable
{
	public static final PhasedMobAnimation TAIL_SWING_ANIMATION = new PhasedMobAnimation(new MobAnimation(MobAnimation.Action.SWING, 12, true, false), 2, 4, 6);
	public static final PhasedMobAnimation BITE_ANIMATION = new PhasedMobAnimation(new MobAnimation(MobAnimation.Action.BITE, 10, true, false), 2, 4, 5);
	public static final PhasedMobAnimation SHOOT_ANIMATION = new PhasedMobAnimation(new MobAnimation(MobAnimation.Action.SHOOT, 14, true, true), 1, 4, 6);
	
	private final BasiliskPartEntity[] parts;
	private final BasiliskPartEntity head;
	private final BasiliskPartEntity body;
	private final BasiliskPartEntity tail;
	private final BasiliskPartEntity tailEnd;
	
	public BasiliskEntity(EntityType<? extends BasiliskEntity> type, Level level)
	{
		super(type, level, 5);
		
		this.head = new BasiliskPartEntity(this, "head", 2.1F, 2.1F);
		this.body = new BasiliskPartEntity(this, "body", 2.5F, 1.9F);
		this.tail = new BasiliskPartEntity(this, "tail", 1.8F, 1.8F);
		this.tailEnd = new BasiliskPartEntity(this, "tailEnd", 1.4F, 1.3F);
		parts = new BasiliskPartEntity[]{this.head, this.body, this.tail, this.tailEnd};
		this.noCulling = true;
		this.setId(ENTITY_COUNTER.getAndAdd(this.parts.length + 1) + 1); // Imitate forges fix to MC-158205
	}
	
	@Override
	public void setId(int id)
	{
		super.setId(id);
		// Imitate forges fix to MC-158205
		for(int i = 0; i < this.parts.length; i++)
			this.parts[i].setId(id + i + 1);
	}
	
	public static AttributeSupplier.Builder basiliskAttributes()
	{
		return UnderlingEntity.underlingAttributes().add(Attributes.MAX_HEALTH, 85)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.6).add(Attributes.MOVEMENT_SPEED, 0.25)
				.add(Attributes.ATTACK_DAMAGE, 6);
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		this.goalSelector.addGoal(2, new AnimatedAttackWhenInRangeGoal<>(this, TAIL_SWING_ANIMATION, 0, 3.5F, 40, 180.0F, 90.0F));
		this.goalSelector.addGoal(2, new AnimatedAttackWhenInRangeGoal<>(this, BITE_ANIMATION, 1.0F, AnimatedAttackWhenInRangeGoal.STANDARD_MELEE_RANGE, 40, 0, 40.0F));
		this.goalSelector.addGoal(3, new FireballShootGoal<>(this, SHOOT_ANIMATION, AnimatedAttackWhenInRangeGoal.STANDARD_MELEE_RANGE, 25, 180));
		this.goalSelector.addGoal(3, new MoveToTargetGoal(this, 1F, false));
	}
	
	@Override
	protected SoundEvent getAmbientSound()
	{
		return MSSoundEvents.ENTITY_BASILISK_AMBIENT.get();
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return MSSoundEvents.ENTITY_BASILISK_HURT.get();
	}
	
	@Override
	protected SoundEvent getDeathSound()
	{
		return MSSoundEvents.ENTITY_BASILISK_DEATH.get();
	}
	
	@Override
	public GristSet getGristSpoils()
	{
		return GristHelper.generateUnderlingGristDrops(this, damageMap, 6);
	}
	
	@Override
	protected int getVitalityGel()
	{
		return random.nextInt(3) + 4;
	}
	
	@Override
	protected void onGristTypeUpdated(GristType type)
	{
		super.onGristTypeUpdated(type);
		applyGristModifier(Attributes.MAX_HEALTH, 20 * type.getPower(), AttributeModifier.Operation.ADDITION);
		applyGristModifier(Attributes.ATTACK_DAMAGE, 2.7 * type.getPower(), AttributeModifier.Operation.ADDITION);
		this.xpReward = (int) (6 * type.getPower() + 4);
	}
	
	@Override
	public void die(DamageSource cause)
	{
		super.die(cause);
		Entity entity = cause.getEntity();
		if(this.dead && !this.level.isClientSide)
		{
			computePlayerProgress((int) (30 + 2.4 * getGristType().getPower())); //most basilisks stop giving xp at rung 32
			firstKillBonus(entity, EcheladderBonusType.BASILISK);
		}
	}
	
	@Override
	public boolean canBeCollidedWith()
	{
		return true;
	}
	
	public void checkDespawn()
	{
	}
	
	@Override
	public boolean isPickable()
	{
		return true;
	}
	
	@Override
	public void aiStep()
	{
		super.aiStep();
		// save the current part positions
		Vector3d[] positions = new Vector3d[this.parts.length];
		for(int j = 0; j < this.parts.length; ++j)
		{
			positions[j] = new Vector3d(this.parts[j].getX(), this.parts[j].getY(), this.parts[j].getZ());
		}
		
		float bodyAngle = this.yBodyRot * ((float) Math.PI / 180F);
		double xOffset = Math.sin(bodyAngle);
		double zOffset = -Math.cos(bodyAngle);
		
		// update the body parts based on the body rotation + apply natural offsets
		this.updatePart(this.body, 0, 0, 0);
		this.updatePart(this.head, xOffset * -2.5, 0.3, zOffset * -2.5);
		this.updatePart(this.tail, xOffset * 2.5, 0, zOffset * 2.5);
		this.updatePart(this.tailEnd, xOffset * 4.5, 1, zOffset * 4.5);
		
		// sets various mysterious params - used to sync client server stuff
		for(int l = 0; l < this.parts.length; ++l)
		{
			this.parts[l].xo = positions[l].x;
			this.parts[l].yo = positions[l].y;
			this.parts[l].zo = positions[l].z;
			this.parts[l].xOld = positions[l].x;
			this.parts[l].yOld = positions[l].y;
			this.parts[l].zOld = positions[l].z;
		}
	}
	
	private void updatePart(BasiliskPartEntity part, double xOffset, double yOffset, double zOffset)
	{
		part.setPos(this.getX() + xOffset, this.getY() + yOffset, this.getZ() + zOffset);
	}
	
	@Override
	public boolean isMultipartEntity()
	{
		return true;
	}
	
	@Override
	public PartEntity<?>[] getParts()
	{
		return this.parts;
	}
	
	@Override
	public void initiationPhaseStart(MobAnimation.Action animation)
	{
		if(animation == MobAnimation.Action.SWING)
			this.playSound(MSSoundEvents.ENTITY_SWOOSH.get(), 1, 2);
	}
	
	@Override
	public void contactPhaseStart(MobAnimation.Action animation)
	{
		if(animation == MobAnimation.Action.BITE)
			this.playSound(MSSoundEvents.ENTITY_BITE.get(), 1, 1);
	}
	
	@Override
	public void registerControllers(AnimationData data)
	{
		data.addAnimationController(AnimationControllerUtil.createAnimation(this, "idleAnimation", 1, BasiliskEntity::idleAnimation));
		data.addAnimationController(AnimationControllerUtil.createAnimation(this, "walkAnimation", 0.5, BasiliskEntity::walkAnimation));
		data.addAnimationController(AnimationControllerUtil.createAnimation(this, "deathAnimation", 1, BasiliskEntity::deathAnimation));
		data.addAnimationController(AnimationControllerUtil.createAnimation(this, "attackAnimation", 1, BasiliskEntity::attackAnimation));
	}
	
	private static PlayState idleAnimation(AnimationEvent<BasiliskEntity> event)
	{
		return PlayState.CONTINUE; //TODO make tounge & tail flick
	}
	
	private static PlayState walkAnimation(AnimationEvent<BasiliskEntity> event)
	{
		if(!event.isMoving())
		{
			return PlayState.STOP;
		}
		
		if(event.getAnimatable().isAggressive())
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("run", true));
			return PlayState.CONTINUE;
		} else
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", true));
			return PlayState.CONTINUE;
		}
	}
	
	private static PlayState deathAnimation(AnimationEvent<BasiliskEntity> event)
	{
		if(event.getAnimatable().dead)
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("die", false));
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}
	
	private static PlayState attackAnimation(AnimationEvent<BasiliskEntity> event)
	{
		MobAnimation.Action action = event.getAnimatable().getCurrentAction();
		
		if(action == MobAnimation.Action.BITE)
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("bite", false));
			return PlayState.CONTINUE;
		} else if(action == MobAnimation.Action.SWING)
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("tail_whip", false));
			return PlayState.CONTINUE;
		} else if(action == MobAnimation.Action.SHOOT)
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("shoot", false));
			return PlayState.CONTINUE;
		}
		
		event.getController().markNeedsReload();
		return PlayState.STOP;
	}
}