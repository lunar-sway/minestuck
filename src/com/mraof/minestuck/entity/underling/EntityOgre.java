package com.mraof.minestuck.entity.underling;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import com.mraof.minestuck.entity.ai.EntityAIAttackOnCollideWithRate;
import com.mraof.minestuck.util.Echeladder;
import com.mraof.minestuck.util.GristHelper;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;
import com.mraof.minestuck.util.MinestuckAchievementHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;

//Makes non-stop ogre puns
public class EntityOgre extends EntityUnderling 
{
	private EntityAIAttackOnCollideWithRate entityAIAttackOnCollideWithRate;
	public EntityOgre(World world)
	{
		super(world, "Ogre");
		setSize(3.0F, 4.5F);
		this.stepHeight = 1.0F;
	}

	@Override
	public GristSet getGristSpoils()
	{
		return GristHelper.getRandomDrop(type, 4);
	}
	
	@Override
	protected void setCombatTask() 
	{
		if(entityAIAttackOnCollideWithRate == null)
			entityAIAttackOnCollideWithRate = new EntityAIAttackOnCollideWithRate(this, .3F, 40, false);
		entityAIAttackOnCollideWithRate.setDistanceMultiplier(1.2F);
		this.tasks.removeTask(this.entityAIAttackOnCollideWithRate);
		this.tasks.addTask(4, entityAIAttackOnCollideWithRate);
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
	protected void applyGristType(GristType type, boolean fullHeal)
	{
		super.applyGristType(type, fullHeal);
		this.experienceValue = (int) (5 * type.getPower() + 4);
	}
	
	@Override
	public void onDeath(DamageSource cause)
	{
		super.onDeath(cause);
		Entity entity = cause.getEntity();
		if(this.dead && !this.worldObj.isRemote && type != null)
		{
			computePlayerProgress((int) (40*type.getPower() + 50));
			if(entity != null && entity instanceof EntityPlayerMP)
			{
				((EntityPlayerMP) entity).triggerAchievement(MinestuckAchievementHandler.killOgre);
				Echeladder ladder = MinestuckPlayerData.getData((EntityPlayerMP) entity).echeladder;
				ladder.checkBonus((byte) (Echeladder.UNDERLING_BONUS_OFFSET + 1));
			}
		}
	}
}