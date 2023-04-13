package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.entity.ai.attack.AnimatedAttackWhenInRangeGoal;
import com.mraof.minestuck.entity.ai.attack.MoveToTargetGoal;
import com.mraof.minestuck.entity.animation.MobAnimation;
import com.mraof.minestuck.entity.animation.PhasedMobAnimation;
import com.mraof.minestuck.player.EcheladderBonusType;
import com.mraof.minestuck.util.AnimationControllerUtil;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;

import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

//TODO check back and make sure removal of disableGiclops config was intentional
public class GiclopsEntity extends UnderlingEntity implements IAnimatable
{
	public static final PhasedMobAnimation KICK_ANIMATION = new PhasedMobAnimation(new MobAnimation(MobAnimation.Action.KICK, 40, true, true), 18, 20, 22);
	
	public GiclopsEntity(EntityType<? extends GiclopsEntity> type, Level level)
	{
		super(type, level, 7);
		this.maxUpStep = 2;
	}
	
	public static AttributeSupplier.Builder giclopsAttributes()
	{
		return UnderlingEntity.underlingAttributes().add(Attributes.MAX_HEALTH, 210)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.9).add(Attributes.MOVEMENT_SPEED, 0.20)
				.add(Attributes.ATTACK_DAMAGE, 18);
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		this.goalSelector.addGoal(2, new AnimatedAttackWhenInRangeGoal<>(this, KICK_ANIMATION));
		this.goalSelector.addGoal(3, new MoveToTargetGoal(this, 1F, false));
	}
	
	@Override
	protected SoundEvent getAmbientSound()
	{
		return MSSoundEvents.ENTITY_GICLOPS_AMBIENT.get();
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return MSSoundEvents.ENTITY_GICLOPS_HURT.get();
	}
	
	@Override
	protected SoundEvent getDeathSound()
	{
		return MSSoundEvents.ENTITY_GICLOPS_DEATH.get();
	}
	
	@Override
	public GristSet getGristSpoils()
	{
		return GristHelper.generateUnderlingGristDrops(this, damageMap, 10);
	}
	
	@Override
	protected int getVitalityGel()
	{
		return random.nextInt(4) + 5;
	}
	
	@Override
	protected void onGristTypeUpdated(GristType type)
	{
		super.onGristTypeUpdated(type);
		applyGristModifier(Attributes.MAX_HEALTH, 46 * type.getPower(), AttributeModifier.Operation.ADDITION);
		applyGristModifier(Attributes.ATTACK_DAMAGE, 4.5 * type.getPower(), AttributeModifier.Operation.ADDITION);
		this.xpReward = (int) (7 * type.getPower() + 5);
	}
	
	@Override
	public void die(DamageSource cause)
	{
		super.die(cause);
		Entity entity = cause.getEntity();
		if(this.dead && !this.level.isClientSide)
		{
			computePlayerProgress((int) (200 + 3 * getGristType().getPower())); //still give xp up to top rung
			firstKillBonus(entity, EcheladderBonusType.GICLOPS);
		}
	}
	
	//Reduced lag is worth not taking damage for being inside a wall
	@Override
	public boolean isInWall()
	{
		return false;
	}
	
	//Only pay attention to the top for water
	
	@Override
	public boolean updateFluidHeightAndDoFluidPushing(TagKey<Fluid> fluidTag, double fluidFactor)
	{
		AABB realBox = this.getBoundingBox();
		this.setBoundingBox(new AABB(realBox.minX, realBox.maxY - 1, realBox.minZ, realBox.maxX, realBox.maxY, realBox.maxZ));
		boolean result = super.updateFluidHeightAndDoFluidPushing(fluidTag, fluidFactor);
		this.setBoundingBox(realBox);
		return result;
	}
	
	/*@Override
	public void move(MoverType typeIn, Vec3 pos)    //TODO probably doesn't work as originally intended anymore. What was this meant to do?
	{
		AABB realBox = this.getBoundingBox();
		double minX = pos.x > 0 ? realBox.maxX - pos.x : realBox.minX;
		/*				y > 0 ? realBox.maxY - y : realBox.minY,*/
		/*double minY = realBox.minY;
		double minZ = pos.z > 0 ? realBox.maxZ - pos.z : realBox.minZ;
		double maxX = pos.x < 0 ? realBox.minX - pos.x : realBox.maxX;
		double maxY = pos.y < 0 ? realBox.minY - pos.y : realBox.maxY;
		double maxZ = pos.z < 0 ? realBox.minZ - pos.z : realBox.maxZ;
		this.setBoundingBox(new AABB(minX, minY, minZ, maxX, maxY, maxZ));
		super.move(typeIn, pos);
		AABB changedBox = this.getBoundingBox();
		this.setPos(this.getX() + changedBox.minX - minX, this.getY() + changedBox.minY - minY, this.getZ() + changedBox.minZ - minZ);
	}*/
	
	@Override
	public boolean isPickable()
	{
		return true;
	}
	
	@Override
	public void initiationPhaseStart(MobAnimation.Action animation)
	{
		if(animation == MobAnimation.Action.KICK)
			this.playSound(MSSoundEvents.ENTITY_SWOOSH.get(), 0.75F, 0);
	}
	
	@Override
	public void registerControllers(AnimationData data)
	{
		data.addAnimationController(AnimationControllerUtil.createAnimation(this, "idleAnimation", 1, GiclopsEntity::idleAnimation));
		data.addAnimationController(AnimationControllerUtil.createAnimation(this, "walkAnimation", 1, GiclopsEntity::walkAnimation));
		data.addAnimationController(AnimationControllerUtil.createAnimation(this, "attackAnimation", 1, GiclopsEntity::attackAnimation));
		data.addAnimationController(AnimationControllerUtil.createAnimation(this, "deathAnimation", 1, GiclopsEntity::deathAnimation));
	}
	
	private static PlayState idleAnimation(AnimationEvent<GiclopsEntity> event)
	{
		if(event.isMoving() || event.getAnimatable().getCurrentAction() != MobAnimation.Action.IDLE)
		{
			return PlayState.STOP;
		}
		
		event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
		return PlayState.CONTINUE;
	}
	
	private static PlayState walkAnimation(AnimationEvent<GiclopsEntity> event)
	{
		if(event.isMoving())
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", true));
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}
	
	private static PlayState attackAnimation(AnimationEvent<GiclopsEntity> event)
	{
		MobAnimation.Action action = event.getAnimatable().getCurrentAction();
		
		if(action == MobAnimation.Action.KICK)
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("kick", false));
			return PlayState.CONTINUE;
		}
		
		event.getController().markNeedsReload();
		return PlayState.STOP;
	}
	
	private static PlayState deathAnimation(AnimationEvent<GiclopsEntity> event)
	{
		if(event.getAnimatable().dead)
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("death", false));
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}
}