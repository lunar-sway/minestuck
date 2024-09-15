package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.MutableGristSet;
import com.mraof.minestuck.entity.ai.attack.AnimatedAttackWhenInRangeGoal;
import com.mraof.minestuck.entity.ai.attack.FireballShootGoal;
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
import net.neoforged.neoforge.entity.PartEntity;
import org.joml.Vector3d;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BasiliskEntity extends UnderlingEntity implements GeoEntity
{
	public static final PhasedMobAnimation TAIL_SWING_PROPERTIES = new PhasedMobAnimation(new MobAnimation(MobAnimation.Action.SWING, 12, true, false), 1, 2, 3, 6);
	public static final PhasedMobAnimation BITE_PROPERTIES = new PhasedMobAnimation(new MobAnimation(MobAnimation.Action.BITE, 10, true, false), 1, 2, 3, 5);
	public static final PhasedMobAnimation SHOOT_PROPERTIES = new PhasedMobAnimation(new MobAnimation(MobAnimation.Action.SHOOT, 14, true, true), 1, 4, 6, 7);
	private static final RawAnimation RUN_ANIMATION = RawAnimation.begin().thenLoop("run");
	private static final RawAnimation WALK_ANIMATION = RawAnimation.begin().thenLoop("walk");
	private static final RawAnimation DIE_ANIMATION = RawAnimation.begin().then("die", Animation.LoopType.PLAY_ONCE);
	private static final RawAnimation BITE_ANIMATION = RawAnimation.begin().then("bite", Animation.LoopType.PLAY_ONCE);
	private static final RawAnimation TAIL_WHIP_ANIMATION = RawAnimation.begin().then("tail_whip", Animation.LoopType.PLAY_ONCE);
	private static final RawAnimation SHOOT_ANIMATION = RawAnimation.begin().then("shoot", Animation.LoopType.PLAY_ONCE);
	
	private final BasiliskPartEntity[] parts;
	private final BasiliskPartEntity head;
	private final BasiliskPartEntity body;
	private final BasiliskPartEntity tail;
	private final BasiliskPartEntity tailEnd;
	
	public BasiliskEntity(EntityType<? extends BasiliskEntity> type, Level level)
	{
		super(type, level, 5);
		
		this.head = new BasiliskPartEntity(this, 2.1F, 2.1F);
		this.body = new BasiliskPartEntity(this, 2.5F, 1.9F);
		this.tail = new BasiliskPartEntity(this, 1.8F, 1.8F);
		this.tailEnd = new BasiliskPartEntity(this, 1.4F, 1.3F);
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
	
	@Override
	public void restoreFrom(Entity entity)
	{
		if(entity instanceof BasiliskPartEntity)
			throw new UnsupportedOperationException("Tried to restore a basilisk from a basilisk part. Might be trying to copy a part and accidentally creating a new basilisk!");
		super.restoreFrom(entity);
	}
	
	public static AttributeSupplier.Builder basiliskAttributes()
	{
		return UnderlingEntity.underlingAttributes().add(Attributes.MAX_HEALTH, 85)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.6).add(Attributes.MOVEMENT_SPEED, 0.25)
				.add(Attributes.ATTACK_DAMAGE, 6).add(Attributes.ATTACK_SPEED, 0.5);
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		this.goalSelector.addGoal(2, new AnimatedAttackWhenInRangeGoal<>(this, TAIL_SWING_PROPERTIES, 0, 3.5F, 40, 180.0F, 90.0F));
		this.goalSelector.addGoal(2, new AnimatedAttackWhenInRangeGoal<>(this, BITE_PROPERTIES, 1.0F, AnimatedAttackWhenInRangeGoal.STANDARD_MELEE_RANGE, 40, 0, 40.0F));
		this.goalSelector.addGoal(3, new FireballShootGoal<>(this, SHOOT_PROPERTIES, AnimatedAttackWhenInRangeGoal.STANDARD_MELEE_RANGE, 25, 180));
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
	public MutableGristSet getGristSpoils()
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
		if(this.dead && !this.level().isClientSide)
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
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers)
	{
		controllers.add(new AnimationController<>(this, "idleAnimation", BasiliskEntity::idleAnimation));
		controllers.add(new AnimationController<>(this, "walkAnimation", BasiliskEntity::walkAnimation)
				.setAnimationSpeedHandler(entity -> MobAnimation.getAttributeAffectedSpeed(entity, Attributes.MOVEMENT_SPEED) * 3.5));
		controllers.add(new AnimationController<>(this, "deathAnimation", BasiliskEntity::deathAnimation));
		controllers.add(new AnimationController<>(this, "attackAnimation", BasiliskEntity::attackAnimation));
	}
	
	private static PlayState idleAnimation(AnimationState<BasiliskEntity> state)
	{
		return PlayState.CONTINUE; //TODO make tounge & tail flick
	}
	
	private static PlayState walkAnimation(AnimationState<BasiliskEntity> state)
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
	
	private static PlayState deathAnimation(AnimationState<BasiliskEntity> state)
	{
		if(state.getAnimatable().dead)
		{
			state.getController().setAnimation(DIE_ANIMATION);
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}
	
	private static PlayState attackAnimation(AnimationState<BasiliskEntity> state)
	{
		MobAnimation.Action action = state.getAnimatable().getCurrentAction();
		
		if(action == MobAnimation.Action.BITE)
		{
			state.getController().setAnimation(BITE_ANIMATION);
			return PlayState.CONTINUE;
		} else if(action == MobAnimation.Action.SWING)
		{
			state.getController().setAnimation(TAIL_WHIP_ANIMATION);
			return PlayState.CONTINUE;
		} else if(action == MobAnimation.Action.SHOOT)
		{
			state.getController().setAnimation(SHOOT_ANIMATION);
			return PlayState.CONTINUE;
		}
		
		state.getController().forceAnimationReset();
		state.getController().setAnimationSpeed(MobAnimation.getAttributeAffectedSpeed(state.getAnimatable(), Attributes.ATTACK_SPEED)); //Setting animation speed on stop so it doesn't jump around when attack speed changes mid-attack
		return PlayState.STOP;
	}
}
