package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.item.crafting.alchemy.GristHelper;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.entity.ai.AttackOnCollideWithRateGoal;
import com.mraof.minestuck.util.*;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
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
	protected String getUnderlingName()
	{
		return "ogre";
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
		return ModSoundEvents.ENTITY_OGRE_AMBIENT;
	}
	
	protected SoundEvent getDeathSound()
	{
		return ModSoundEvents.ENTITY_OGRE_DEATH;
	}	
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return ModSoundEvents.ENTITY_OGRE_HURT;
	}
	
	@Override
	public GristSet getGristSpoils()
	{
		return GristHelper.getRandomDrop(type, 4);
	}
	
	@Override
	protected double getWanderSpeed() 
	{
		return 0.65;
	}
	
	@Override
	protected float getMaximumHealth() 
	{
		return type != null ? 13F * type.getPower() + 50 : 1;
	}
	
	@Override
	protected float getKnockbackResistance()
	{
		return 0.4F;
	}
	
	@Override
	protected double getAttackDamage()
	{
		return this.type.getPower() * 2.1 + 6;
	}
	
	@Override
	protected int getVitalityGel()
	{
		return rand.nextInt(3) + 3;
	}
	
	@Override
	protected void applyGristType(GristType type, boolean fullHeal)
	{
		super.applyGristType(type, fullHeal);
		this.experienceValue = (int) (5 * type.getPower() + 4);
	}
	
	@Override
	public void onDeath(DamageSource cause)
	{
		super.onDeath(cause);
		Entity entity = cause.getTrueSource();
		if(this.dead && !this.world.isRemote && type != null)
		{
			computePlayerProgress((int) (40*type.getPower() + 50));
			if(entity instanceof ServerPlayerEntity)
			{
				//((EntityPlayerMP) entity).addStat(MinestuckAchievementHandler.killOgre);
				Echeladder ladder = PlayerSavedData.getData((ServerPlayerEntity) entity).echeladder;
				ladder.checkBonus((byte) (Echeladder.UNDERLING_BONUS_OFFSET + 1));
			}
		}
	}
}