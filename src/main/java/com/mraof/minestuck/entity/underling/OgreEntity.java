package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.entity.ai.CustomMeleeAttackGoal;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

//Makes non-stop ogre puns
public class OgreEntity extends UnderlingEntity
{
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
		this.goalSelector.addGoal(3, new CustomMeleeAttackGoal(this, 1.0F, false, 40, 1.2F));
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
}