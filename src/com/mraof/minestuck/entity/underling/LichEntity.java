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

public class LichEntity extends UnderlingEntity
{
	
	public LichEntity(EntityType<? extends LichEntity> type, World world)
	{
		super(type, world);
	}
	
	@Override
	protected String getUnderlingName()
	{
		return "lich";
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
		return ModSoundEvents.ENTITY_LICH_AMBIENT;
	}
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return ModSoundEvents.ENTITY_LICH_HURT;
	}
	
	protected SoundEvent getDeathSound()
	{
		return ModSoundEvents.ENTITY_LICH_DEATH;
	}
	
	@Override
	public GristSet getGristSpoils()
	{
		return GristHelper.getRandomDrop(type, 8);
	}
	
	@Override
	protected double getWanderSpeed() 
	{
		return 0.4;
	}
	
	@Override
	protected float getMaximumHealth() 
	{
		return type != null ? 30*type.getPower() + 175 : 1;
	}
	
	@Override
	protected float getKnockbackResistance()
	{
		return 0.3F;
	}
	
	@Override
	protected double getAttackDamage()
	{
		return Math.ceil(this.type.getPower()*3.4 + 8);
	}
	
	@Override
	protected int getVitalityGel()
	{
		return rand.nextInt(3)+6;
	}
	
	@Override
	protected void applyGristType(GristType type, boolean fullHeal)
	{
		super.applyGristType(type, fullHeal);
		this.experienceValue = (int) (6.5 * type.getPower() + 4);
	}
	
	@Override
	public void onDeath(DamageSource cause)
	{
		super.onDeath(cause);
		Entity entity = cause.getTrueSource();
		if(this.dead && !this.world.isRemote && type != null)
		{
			computePlayerProgress((int) (300*type.getPower() + 650));
			if(entity instanceof ServerPlayerEntity)
			{
				Echeladder ladder = PlayerSavedData.getData((ServerPlayerEntity) entity).echeladder;
				ladder.checkBonus((byte) (Echeladder.UNDERLING_BONUS_OFFSET + 3));
			}
		}
	}
}