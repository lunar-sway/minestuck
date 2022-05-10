package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.entity.ai.CustomMeleeAttackGoal;
import com.mraof.minestuck.item.crafting.alchemy.GristHelper;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class BasiliskEntity extends UnderlingEntity implements IAnimatable
{
	public BasiliskEntity(EntityType<? extends BasiliskEntity> type, World world)
	{
		super(type, world, 5);
		this.setAttackDelay(4);
		this.setAttackRecovery(10);
	}
	
	public static AttributeModifierMap.MutableAttribute basiliskAttributes()
	{
		return UnderlingEntity.underlingAttributes().add(Attributes.MAX_HEALTH, 85)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.6).add(Attributes.MOVEMENT_SPEED, 0.25)
				.add(Attributes.ATTACK_DAMAGE, 6);
	}
	
	protected SoundEvent getAmbientSound()
	{
		return MSSoundEvents.ENTITY_BASILISK_AMBIENT;
	}
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return MSSoundEvents.ENTITY_BASILISK_HURT;
	}
	
	protected SoundEvent getDeathSound()
	{
		return MSSoundEvents.ENTITY_BASILISK_DEATH;
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
			firstKillBonus(entity, (byte) (Echeladder.UNDERLING_BONUS_OFFSET + 2));
		}
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(createAnimation("walkAnimation", 1, this::walkAnimation));
		data.addAnimationController(createAnimation("deathAnimation", 1, this::deathAnimation));
		data.addAnimationController(createAnimation("swingAnimation", 1, this::swingAnimation));
	}

	private <E extends IAnimatable> PlayState walkAnimation(AnimationEvent<E> event) {
		if (!event.isMoving()) {
			return PlayState.STOP;
		}

		if (this.isAggressive()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("run", true));
			return PlayState.CONTINUE;
		} else {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", true));
			return PlayState.CONTINUE;
		}
	}

	private <E extends IAnimatable> PlayState deathAnimation(AnimationEvent<E> event) {
		if (dead) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("die", false));
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}

	private <E extends IAnimatable> PlayState swingAnimation(AnimationEvent<E> event) {
		if (isAttacking()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("bite", false));
			return PlayState.CONTINUE;
		}
		event.getController().markNeedsReload();
		return PlayState.STOP;
	}
}