package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.MinestuckConfig;
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
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class GiclopsEntity extends UnderlingEntity implements GeoEntity
{
	public static final PhasedMobAnimation KICK_PROPERTIES = new PhasedMobAnimation(new MobAnimation(MobAnimation.Action.KICK, 40, true, true), 18, 20, 22, 1);
	private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("idle");
	private static final RawAnimation WALK_ANIMATION = RawAnimation.begin().thenLoop("walk");
	private static final RawAnimation KICK_ANIMATION = RawAnimation.begin().then("kick", Animation.LoopType.PLAY_ONCE);
	private static final RawAnimation DEATH_ANIMATION = RawAnimation.begin().then("death", Animation.LoopType.PLAY_ONCE);
	
	public GiclopsEntity(EntityType<? extends GiclopsEntity> type, Level level)
	{
		super(type, level, 7);
		this.setMaxUpStep(2);
	}
	
	public static AttributeSupplier.Builder giclopsAttributes()
	{
		return UnderlingEntity.underlingAttributes().add(Attributes.MAX_HEALTH, 210)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.9).add(Attributes.MOVEMENT_SPEED, 0.20)
				.add(Attributes.ATTACK_DAMAGE, 18).add(Attributes.ATTACK_SPEED, 1);
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		this.goalSelector.addGoal(2, new AnimatedAttackWhenInRangeGoal<>(this, KICK_PROPERTIES));
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
	public MutableGristSet getGristSpoils()
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
	public void baseTick()
	{
		super.baseTick();
		if(!level().isClientSide && MinestuckConfig.SERVER.disableGiclops.get())
			this.discard();
	}
	
	@Override
	public void die(DamageSource cause)
	{
		super.die(cause);
		Entity entity = cause.getEntity();
		if(this.dead && !this.level().isClientSide)
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
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers)
	{
		controllers.add(new AnimationController<>(this, "idleAnimation", GiclopsEntity::idleAnimation));
		controllers.add(new AnimationController<>(this, "walkAnimation", GiclopsEntity::walkAnimation)
				.setAnimationSpeedHandler(entity -> MobAnimation.getAttributeAffectedSpeed(entity, Attributes.MOVEMENT_SPEED) * 5));
		controllers.add(new AnimationController<>(this, "attackAnimation", GiclopsEntity::attackAnimation));
		controllers.add(new AnimationController<>(this, "deathAnimation", GiclopsEntity::deathAnimation));
	}
	
	private static PlayState idleAnimation(AnimationState<GiclopsEntity> state)
	{
		if(state.isMoving() || state.getAnimatable().getCurrentAction() != MobAnimation.Action.IDLE)
		{
			return PlayState.STOP;
		}
		
		state.getController().setAnimation(IDLE_ANIMATION);
		return PlayState.CONTINUE;
	}
	
	private static PlayState walkAnimation(AnimationState<GiclopsEntity> state)
	{
		if(MobAnimation.isEntityMovingHorizontally(state.getAnimatable()))
		{
			state.getController().setAnimation(WALK_ANIMATION);
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}
	
	private static PlayState attackAnimation(AnimationState<GiclopsEntity> state)
	{
		MobAnimation.Action action = state.getAnimatable().getCurrentAction();
		
		if(action == MobAnimation.Action.KICK)
		{
			state.getController().setAnimation(KICK_ANIMATION);
			return PlayState.CONTINUE;
		}
		
		state.getController().forceAnimationReset();
		state.getController().setAnimationSpeed(MobAnimation.getAttributeAffectedSpeed(state.getAnimatable(), Attributes.ATTACK_SPEED)); //Setting animation speed on stop so it doesn't jump around when attack speed changes mid-attack
		return PlayState.STOP;
	}
	
	private static PlayState deathAnimation(AnimationState<GiclopsEntity> state)
	{
		if(state.getAnimatable().dead)
		{
			state.getController().setAnimation(DEATH_ANIMATION);
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}
}
