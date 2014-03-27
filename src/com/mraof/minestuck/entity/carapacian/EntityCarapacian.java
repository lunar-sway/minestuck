package com.mraof.minestuck.entity.carapacian;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.mraof.minestuck.entity.EntityListAttackFilter;
import com.mraof.minestuck.entity.EntityMinestuck;
import com.mraof.minestuck.entity.ai.EntityAINearestAttackableTargetWithHeight;

public abstract class EntityCarapacian extends EntityMinestuck
{
	protected List<Class<? extends EntityLivingBase>> enemyClasses;
	protected List<Class<? extends EntityLivingBase>> allyClasses;
	protected EntityListAttackFilter attackEntitySelector;

	public EntityCarapacian(World par1World)
	{
		super(par1World);
		enemyClasses = new ArrayList<Class<? extends EntityLivingBase>>();
		allyClasses = new ArrayList<Class<? extends EntityLivingBase>>();
		setEnemies();
		setAllies();

		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue((double)(this.getMaximumHealth()));
		this.setHealth(this.getMaximumHealth());

		this.tasks.addTask(1, new EntityAISwimming(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		this.targetTasks.addTask(2, this.entityAINearestAttackableTargetWithHeight());
		this.tasks.addTask(5, new EntityAIWander(this, this.getWanderSpeed()));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));

		if (par1World != null && !par1World.isRemote)
		{
			this.setCombatTask();
		}
	}


	protected abstract void setCombatTask();

	protected abstract float getWanderSpeed();

	public void setEnemies()
	{
		attackEntitySelector = new EntityListAttackFilter(enemyClasses);
	}
	public void setEnemies(EnumEntityKingdom side)
	{
		switch(side)
		{
		case PROSPITIAN:
			enemyClasses.add(EntityBlackPawn.class);
			enemyClasses.add(EntityBlackBishop.class);
			break;
		case DERSITE:
			enemyClasses.add(EntityWhitePawn.class);
			enemyClasses.add(EntityWhiteBishop.class);
		}
	}
	public void addEnemy(Class<? extends EntityLivingBase> enemyClass)
	{
		if(canAttackClass(enemyClass) && !enemyClasses.contains(enemyClass))
		{
			enemyClasses.add(enemyClass);
			this.setEnemies();
			this.setCombatTask();
		}
	}
	public void setAllies() {}
	public void setAllies(EnumEntityKingdom side)
	{
		switch(side)
		{
		case PROSPITIAN:
			allyClasses.add(EntityWhitePawn.class);
			allyClasses.add(EntityWhiteBishop.class);
			break;
		case DERSITE:
			allyClasses.add(EntityBlackPawn.class);
			allyClasses.add(EntityBlackBishop.class);
		}
	}
	@Override
	public void setAttackTarget(EntityLivingBase par1EntityLivingBase) 
	{
		super.setAttackTarget(par1EntityLivingBase);
		if(par1EntityLivingBase != null)
		{
			this.addEnemy(par1EntityLivingBase.getClass());
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canAttackClass(Class par1Class)
	{

		return !this.allyClasses.contains(par1Class);
	}

	@Override
	protected boolean isAIEnabled()
	{
		return true;
	}
	EntityAINearestAttackableTargetWithHeight entityAINearestAttackableTargetWithHeight()
	{
		return new EntityAINearestAttackableTargetWithHeight(this, EntityLivingBase.class, 256.0F, 0, true, false, attackEntitySelector);
	}

}
