package com.mraof.minestuck.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.ai.EntityPawnAINearestAttackableTarget;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
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
import net.minecraft.entity.ai.EntityAITaskEntry;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract class EntityPawn extends EntityCreature implements IRangedAttackMob, IMob
{
	private static Random randStatic = new Random();
	List<Class<? extends EntityLiving>> enemyClasses;
	private EntityAIArrowAttack entityAIArrowAttack = new EntityAIArrowAttack(this, 0.25F, 20, 10.0F);
	private EntityAIAttackOnCollide entityAIAttackOnCollide = new EntityAIAttackOnCollide(this, .4F, false);

	private EntityListAttackFilter attackEntitySelector;
	private int pawnType;

	public EntityPawn(World world)
	{
			this(world, randStatic.nextDouble() < .25 ? 1 : 0);
	}
	public EntityPawn(World world, int pawnType) 
	{
		super(world);
		moveSpeed = 0.3F;
		setSize(0.6F, 1.5F);
		this.experienceValue = 1;
		enemyClasses = new ArrayList<Class<? extends EntityLiving>>();
		setEnemies();
		
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(5, new EntityAIWander(this, this.moveSpeed));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLiving.class, 256.0F, 0, true, false, attackEntitySelector));
		this.pawnType = pawnType;

		
		if (world != null && !world.isRemote)
		{
			this.setCombatTask();
		}
	}
	
	@Override
	public int getMaxHealth() 
	{
		return 20;
	}
	
	@Override
	protected boolean isAIEnabled() 
	{
		return true;
	}
	
	public void setEnemies()
	{
		attackEntitySelector = new EntityListAttackFilter(enemyClasses);
	}
	
	@Override
	public void initCreature() 
	{
		this.addRandomArmor();
			
		if(this.pawnType == 1)
		{
			this.targetTasks.addTask(4, entityAIArrowAttack);
		}
		else
			this.tasks.addTask(4, this.entityAIAttackOnCollide);
		this.setCurrentItemOrArmor(0, new ItemStack(this.pawnType == 1 ? Item.bow : rand.nextDouble() < .2 ? Minestuck.regisword : rand.nextDouble() < .02 ? Minestuck.sord : Item.swordStone));
		this.func_82162_bC();
	}
	
	@Override
	public void attackEntityWithRangedAttack(EntityLiving var1, float f1) 
	{
		EntityArrow var2 = new EntityArrow(this.worldObj, this, var1, 1.6F, 12.0F);
		int var3 = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.getHeldItem());
		int var4 = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.getHeldItem());

		if (var3 > 0)
		{
			var2.setDamage(var2.getDamage() + (double)var3 * 0.5D + 0.5D);
		}

		if (var4 > 0)
		{
			var2.setKnockbackStrength(var4);
		}

		if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, this.getHeldItem()) > 0)
		{
			var2.setFire(100);
		}

		this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
//		EntityPawn pawn = this.getClass() == EntityWhitePawn.class ? new EntityWhitePawn(this.worldObj, 0) : new EntityBlackPawn(this.worldObj, 0);
//		pawn.setLocationAndAngles(this.posX, this.posY, this.posZ, 0, 0);
//		pawn.initCreature();
//		this.worldObj.spawnEntityInWorld(pawn);	
		//I was just messing around to see if I could make an EntityLiving spawn more EntityLiving, it can
		this.worldObj.spawnEntityInWorld(var2);
	}
	
	/**
	 * Returns the amount of damage a mob should deal.
	 */
	public int getAttackStrength(Entity par1Entity)
	{
		ItemStack var2 = this.getHeldItem();
		int var3 = 2;

		if (var2 != null)
		{
			var3 += var2.getDamageVsEntity(this);
		}

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
	
	public void setCombatTask()
	{
		this.tasks.removeTask(this.entityAIArrowAttack);
		this.tasks.removeTask(this.entityAIAttackOnCollide);
		ItemStack var1 = this.getHeldItem();

		if (var1 != null && var1.itemID == Item.bow.itemID)
		{
			this.tasks.addTask(4, this.entityAIArrowAttack);
		}
		else
			this.tasks.addTask(4, this.entityAIAttackOnCollide);
	}
	public void setCurrentItemOrArmor(int par1, ItemStack par2ItemStack)
	{
		super.setCurrentItemOrArmor(par1, par2ItemStack);

		if (!this.worldObj.isRemote && par1 == 0)
		{
			this.setCombatTask();
		}
	}


}
