package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.entity.ai.AttackOnCollideWithRateGoal;
import com.mraof.minestuck.item.crafting.alchemy.GristHelper;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

//Makes non-stop ogre puns
public class OgreEntity extends UnderlingEntity
{
	public OgreEntity(EntityType<? extends OgreEntity> type, World world)
	{
		super(type, world);
		this.stepHeight = 1.0F;
	}
	
	@Override
	protected void registerAttributes()
	{
		super.registerAttributes();
		getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50.0D);
		getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.4F);
		getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.65D);
		getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		AttackOnCollideWithRateGoal aiAttack = new AttackOnCollideWithRateGoal(this, .3F, 40, false);
		aiAttack.setDistanceMultiplier(1.2F);
		this.goalSelector.addGoal(3, aiAttack);
	}
	
	protected SoundEvent getAmbientSound()
	{
		return MSSoundEvents.ENTITY_OGRE_AMBIENT;
	}
	
	protected SoundEvent getDeathSound()
	{
		return MSSoundEvents.ENTITY_OGRE_DEATH;
	}	
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return MSSoundEvents.ENTITY_OGRE_HURT;
	}
	
	@Override
	public GristSet getGristSpoils()
	{
		return GristHelper.generateUnderlingGristDrops(this, damageMap, 4);
	}
	
	@Override
	protected int getVitalityGel()
	{
		return rand.nextInt(3) + 3;
	}
	
	@Override
	protected void onGristTypeUpdated(GristType type)
	{
		super.onGristTypeUpdated(type);
		applyGristModifier(SharedMonsterAttributes.MAX_HEALTH, 13 * type.getPower(), AttributeModifier.Operation.ADDITION);
		applyGristModifier(SharedMonsterAttributes.ATTACK_DAMAGE, 2.1 * type.getPower(), AttributeModifier.Operation.ADDITION);
		this.experienceValue = (int) (5 * type.getPower() + 4);
	}
	
	@Override
	public void onDeath(DamageSource cause)
	{
		super.onDeath(cause);
		Entity entity = cause.getTrueSource();
		if(this.dead && !this.world.isRemote)
		{
			computePlayerProgress((int) (40* getGristType().getPower() + 50));
			if(entity instanceof ServerPlayerEntity)
			{
				Echeladder ladder = PlayerSavedData.getData((ServerPlayerEntity) entity).getEcheladder();
				ladder.checkBonus((byte) (Echeladder.UNDERLING_BONUS_OFFSET + 1));
			}
		}
	}
}