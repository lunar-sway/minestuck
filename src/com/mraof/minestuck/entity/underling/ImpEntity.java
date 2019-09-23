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

public class ImpEntity extends UnderlingEntity
{
	public ImpEntity(EntityType<? extends ImpEntity> type, World world)
	{
		super(type, world);
	}
	
	@Override
	protected String getUnderlingName()
	{
		return "imp";
	}
	
	@Override
	public GristSet getGristSpoils()
	{
		return GristHelper.getRandomDrop(type, 1);
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		AttackOnCollideWithRateGoal aiAttack = new AttackOnCollideWithRateGoal(this, .4F, 20, false);
		this.goalSelector.addGoal(3, aiAttack);
	}
	
	protected SoundEvent getAmbientSound()
	{
		return ModSoundEvents.ENTITY_IMP_AMBIENT;
	}
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return ModSoundEvents.ENTITY_IMP_HURT;
	}
	
	protected SoundEvent getDeathSound()
	{
		return ModSoundEvents.ENTITY_IMP_DEATH;
	}
	
	@Override
	protected double getWanderSpeed() 
	{
		return 0.6;
	}
	@Override
	protected float getMaximumHealth() 
	{
		return type != null ? 8*type.getPower() + 6 : 1;
	}
	
	@Override
	protected float getKnockbackResistance()
	{
		return 0;
	}
	
	@Override
	protected double getAttackDamage()
	{
		return Math.ceil(this.type.getPower() + 1);
	}
	
	@Override
	protected int getVitalityGel()
	{
		return rand.nextInt(3)+1;
	}
	
	@Override
	protected void applyGristType(GristType type, boolean fullHeal)
	{
		super.applyGristType(type, fullHeal);
		this.experienceValue = (int) (3 * type.getPower() + 1);
	}
	
	@Override
	public void onDeath(DamageSource cause)
	{
		super.onDeath(cause);
		Entity entity = cause.getTrueSource();
		if(this.dead && !this.world.isRemote && type != null)
		{
			computePlayerProgress((int) (2 + 3*type.getPower()));
			if(entity instanceof ServerPlayerEntity)
			{
				Echeladder ladder = PlayerSavedData.getData((ServerPlayerEntity) entity).echeladder;
				ladder.checkBonus(Echeladder.UNDERLING_BONUS_OFFSET);
			}
		}
	}
}