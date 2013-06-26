package com.mraof.minestuck.entity;

import java.util.ArrayList;
import java.util.List;

import com.mraof.minestuck.entity.ai.EntityAINearestAttackableTargetWithHeight;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract class EntityCarapacian extends EntityCreature
{
	protected List<Class<? extends EntityLiving>> enemyClasses;
	protected List<Class<? extends EntityLiving>> allyClasses;
	protected EntityListAttackFilter attackEntitySelector;

	public EntityCarapacian(World par1World)
	{
		super(par1World);
		enemyClasses = new ArrayList<Class<? extends EntityLiving>>();
		allyClasses = new ArrayList<Class<? extends EntityLiving>>();
		setEnemies();
		setAllies();

		this.tasks.addTask(1, new EntityAISwimming(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, this.entityAINearestAttackableTargetWithHeight());
		this.tasks.addTask(5, new EntityAIWander(this, this.moveSpeed));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));

		if (par1World != null && !par1World.isRemote)
		{
			this.setCombatTask();
		}
	}

	protected abstract void setCombatTask();

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
		return new EntityAINearestAttackableTargetWithHeight(this, EntityLiving.class, 256.0F, 0, true, false, attackEntitySelector);
	}

}
