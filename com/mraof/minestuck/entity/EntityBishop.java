package com.mraof.minestuck.entity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityBishop extends EntityCreature implements IRangedAttackMob, IMob
{
	List<Class<? extends EntityLiving>> enemyClasses;
	private EntityAIArrowAttack entityAIArrowAttack = new EntityAIArrowAttack(this, 0.25F, 30, 10.0F);
	private EntityAIAttackOnCollide entityAIAttackOnCollide = new EntityAIAttackOnCollide(this, .4F, false);
	private EntityListAttackFilter attackEntitySelector;

	public EntityBishop(World par1World) 
	{
		super(par1World);
		enemyClasses = new ArrayList<Class<? extends EntityLiving>>();
		attackEntitySelector = new EntityListAttackFilter(enemyClasses);
		//TODO make an Enum or something like that with all the enemies and friends of dersites and enemies and friends of prospitians
	
		this.setEntityHealth(this.getMaxHealth());
		this.texture = "/mods/Minestuck/textures/mobs/Bishop.png";
		this.setSize(1.9F, 3.9F);
		this.moveSpeed = 0.6F;
//		this.getNavigator().setCanSwim(true);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(5, new EntityAIWander(this, this.moveSpeed));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(7, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.experienceValue = 3;
	}
	public void setEnemies()
	{
		attackEntitySelector = new EntityListAttackFilter(enemyClasses);
	}

	@Override
	public int getMaxHealth() 
	{
		return 40;
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLiving entityliving, float f) 
	{
		
	}
	public int getAttackStrength(Entity par1Entity)
	{
		ItemStack var2 = this.getHeldItem();
		int var3 = 2;
		
		if (var2 != null)
			var3 += var2.getDamageVsEntity(this);
		
		return var3;
	}
	
	public boolean attackEntityAsMob(Entity par1Entity)
	{
		int damage = this.getAttackStrength(par1Entity);
		return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
	}
	/**
	 * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
	 */
	protected void attackEntity(Entity par1Entity, float par2)
	{
		if (this.attackTime <= 0 && par2 < 2.0F && par1Entity.boundingBox.maxY > this.boundingBox.minY && par1Entity.boundingBox.minY < this.boundingBox.maxY)
		{
			this.attackTime = 20;
			this.attackEntityAsMob(par1Entity);
			System.out.println(this.getAttackTarget());
		}
	}
	@Override
	public void initCreature() 
	{
		attackEntitySelector = new EntityListAttackFilter(enemyClasses);
		this.addRandomArmor();
		this.func_82162_bC();
		
		this.targetTasks.addTask(4, entityAIArrowAttack);
		this.tasks.addTask(5, this.entityAIAttackOnCollide);
		this.setCurrentItemOrArmor(0, new ItemStack(Item.blazeRod));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLiving.class, 32.0F, 0, true, false, attackEntitySelector));
	}

}
