package com.mraof.minestuck.entity.carapacian;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.mraof.minestuck.entity.EntityListFilter;
import com.mraof.minestuck.entity.EntityMinestuck;
import com.mraof.minestuck.entity.ai.EntityAIHurtByTargetAllied;
import com.mraof.minestuck.entity.ai.EntityAIMoveToBattle;
import com.mraof.minestuck.entity.ai.EntityAINearestAttackableTargetWithHeight;

public abstract class EntityCarapacian extends EntityMinestuck
{
	protected List<Class<? extends EntityLivingBase>> enemyClasses;
	protected List<Class<? extends EntityLivingBase>> allyClasses;
	protected static List<Class<? extends EntityLivingBase>> prospitianClasses = new ArrayList<Class<? extends EntityLivingBase>>(); 
	protected static List<Class<? extends EntityLivingBase>> dersiteClasses = new ArrayList<Class<? extends EntityLivingBase>>(); 
	protected static EntityListFilter prospitianSelector = new EntityListFilter(prospitianClasses);
	protected static EntityListFilter dersiteSelector = new EntityListFilter(dersiteClasses);
	static
	{
		dersiteClasses.add(EntityBlackPawn.class);
		dersiteClasses.add(EntityBlackBishop.class);
		dersiteClasses.add(EntityBlackRook.class);

		prospitianClasses.add(EntityWhitePawn.class);
		prospitianClasses.add(EntityWhiteBishop.class);
		prospitianClasses.add(EntityWhiteRook.class);
	}
	protected EntityListFilter attackEntitySelector;

	public EntityCarapacian(EntityType<?> type, World par1World)
	{
		super(type, par1World);
		enemyClasses = new ArrayList<Class<? extends EntityLivingBase>>();
		allyClasses = new ArrayList<Class<? extends EntityLivingBase>>();
		setEnemies();
		setAllies();
		
		this.tasks.addTask(1, new EntityAISwimming(this));
		//this.tasks.addTask(4, new EntityAIMoveToBattle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTargetAllied(this, this.getKingdom() == EnumEntityKingdom.PROSPITIAN ? prospitianSelector : dersiteSelector));
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

	public abstract float getWanderSpeed();

	public void setEnemies()
	{
		attackEntitySelector = new EntityListFilter(enemyClasses);
		switch(this.getKingdom())
		{
			case PROSPITIAN:
				enemyClasses.addAll(dersiteClasses);
				break;
			case DERSITE:
				enemyClasses.addAll(prospitianClasses);
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
	public void setAllies() 
	{
		switch(this.getKingdom())
		{
			case PROSPITIAN:
				allyClasses.addAll(prospitianClasses);
				break;
			case DERSITE:
				allyClasses.addAll(dersiteClasses);
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
	
	@Override
	public boolean canAttackClass(Class par1Class)
	{
		return !this.allyClasses.contains(par1Class);
	}
	
	EntityAINearestAttackableTargetWithHeight entityAINearestAttackableTargetWithHeight()
	{
		return new EntityAINearestAttackableTargetWithHeight(this, EntityLivingBase.class, 256.0F, 0, true, false, attackEntitySelector);
	}
	public abstract EnumEntityKingdom getKingdom();

}
