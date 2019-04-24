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

public class EntityImp extends EntityUnderling
{
	public EntityImp(World world) 
	{
		super(world);
		setSize(0.75F, 1.5F);
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
	protected void initEntityAI()
	{
		super.initEntityAI();
		EntityAIAttackOnCollideWithRate aiAttack = new EntityAIAttackOnCollideWithRate(this, .4F, 20, false);
		this.tasks.addTask(3, aiAttack);
	}
	
	protected SoundEvent getAmbientSound()
	{
		return MinestuckSoundHandler.soundImpAmbient;
	}
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return MinestuckSoundHandler.soundImpHurt;
	}
	
	protected SoundEvent getDeathSound()
	{
		return MinestuckSoundHandler.soundImpDeath;
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
			if(entity != null && entity instanceof EntityPlayerMP)
			{
				Echeladder ladder = MinestuckPlayerData.getData((EntityPlayerMP) entity).echeladder;
				ladder.checkBonus((byte) (Echeladder.UNDERLING_BONUS_OFFSET));
			}
		}
	}
}