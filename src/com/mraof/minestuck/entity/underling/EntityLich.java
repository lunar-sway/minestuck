package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.entity.ai.EntityAIAttackOnCollideWithRate;
import com.mraof.minestuck.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityLich extends EntityUnderling
{
	
	public EntityLich(World world) 
	{
		super(world);
		setSize(1.0F, 2.0F);
	}
	
	@Override
	protected String getUnderlingName()
	{
		return "lich";
	}
	
	@Override
	protected void initEntityAI()
	{
		super.initEntityAI();
		EntityAIAttackOnCollideWithRate aiAttack = new EntityAIAttackOnCollideWithRate(this, .4F, 20, false);
		this.tasks.addTask(3, aiAttack);
	}
	
	protected SoundEvent getAmbientSound()
	{
		return MinestuckSoundHandler.soundLichAmbient;
	}
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return MinestuckSoundHandler.soundLichHurt;
	}
	
	protected SoundEvent getDeathSound()
	{
		return MinestuckSoundHandler.soundLichDeath;
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
			if(entity != null && entity instanceof EntityPlayerMP)
			{
				Echeladder ladder = MinestuckPlayerData.getData((EntityPlayerMP) entity).echeladder;
				ladder.checkBonus((byte) (Echeladder.UNDERLING_BONUS_OFFSET + 3));
			}
		}
	}
}