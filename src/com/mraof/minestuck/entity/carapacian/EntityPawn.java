package com.mraof.minestuck.entity.carapacian;

import java.util.Random;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.mraof.minestuck.Minestuck;

public abstract class EntityPawn extends EntityCarapacian implements IRangedAttackMob, IMob
{
	private static Random randStatic = new Random();
	private EntityAIArrowAttack entityAIArrowAttack = new EntityAIArrowAttack(this, 0.25F, 20, 10.0F);
	private EntityAIAttackOnCollide entityAIAttackOnCollide = new EntityAIAttackOnCollide(this, .4F, false);
	private int pawnType;
	protected float moveSpeed = 0.3F;

	public EntityPawn(World world)
	{
		this(world, randStatic.nextDouble() < .25 ? 1 : 0);
	}
	public EntityPawn(World world, int pawnType) 
	{
		super(world);
		setSize(0.6F, 1.5F);
		this.experienceValue = 1;

		this.pawnType = pawnType;
	}

	@Override
	public float getMaximumHealth() 
	{
		return 20;
	}

	@Override
	protected float getWanderSpeed() 
	{
		return .3F;
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData par1EntityLivingData)
	{
		par1EntityLivingData = super.onSpawnWithEgg(par1EntityLivingData);
		this.addRandomArmor();

		if(this.pawnType == 1)
		{
			this.targetTasks.addTask(4, entityAIArrowAttack);
		}
		else
			this.tasks.addTask(4, this.entityAIAttackOnCollide);
		this.setCurrentItemOrArmor(0, new ItemStack(this.pawnType == 1 ? Items.bow : rand.nextDouble() < .2 ? Minestuck.regisword : rand.nextDouble() < .02 ? Minestuck.sord : Items.stone_sword));
		this.enchantEquipment();
		return par1EntityLivingData;
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase entityLiving, float f1) 
	{
		EntityArrow arrow = new EntityArrow(this.worldObj, this, entityLiving, 1.6F, 12.0F);
		int power = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.getHeldItem());
		int punch = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.getHeldItem());

		if (power > 0)
		{
			arrow.setDamage(arrow.getDamage() + (double)power * 0.5D + 0.5D);
		}

		if (punch > 0)
		{
			arrow.setKnockbackStrength(punch);
		}

		if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, this.getHeldItem()) > 0)
		{
			arrow.setFire(100);
		}

		this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		//		EntityPawn pawn = this.getClass() == EntityWhitePawn.class ? new EntityWhitePawn(this.worldObj, 0) : new EntityBlackPawn(this.worldObj, 0);
		//		pawn.setLocationAndAngles(this.posX, this.posY, this.posZ, 0, 0);
		//		pawn.initCreature();
		//		this.worldObj.spawnEntityInWorld(pawn);	
		//I was just messing around to see if I could make an EntityLiving spawn more EntityLiving, it can
		this.worldObj.spawnEntityInWorld(arrow);
	}

	/**
	 * Returns the amount of damage a mob should deal.
	 */
	public float getAttackStrength(Entity par1Entity)
	{
		ItemStack weapon = this.getHeldItem();
		float damage = 2;

		if (weapon != null)
			damage += 
				(float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();

		damage += EnchantmentHelper.getEnchantmentModifierLiving(this, (EntityLivingBase)par1Entity);

		return damage;
	}

	@Override
	public boolean attackEntityAsMob(Entity par1Entity)
	{
		float damage = this.getAttackStrength(par1Entity);
		int fireAspectLevel = EnchantmentHelper.getFireAspectModifier(this);
		int knockback = EnchantmentHelper.getKnockbackModifier(this, (EntityLivingBase)par1Entity);

		if (fireAspectLevel > 0 && !par1Entity.isBurning())
			par1Entity.setFire(1);

		if (knockback > 0)
			par1Entity.addVelocity((double)(-MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F) * (float)knockback * 0.5F), 0.1D, (double)(MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F) * (float)knockback * 0.5F));

		return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
	}
	/**
	 * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
	 */

	@Override
	protected void attackEntity(Entity par1Entity, float par2)
	{
		if (this.attackTime <= 0 && par2 < 2.0F && par1Entity.boundingBox.maxY > this.boundingBox.minY && par1Entity.boundingBox.minY < this.boundingBox.maxY)
		{
			this.attackTime = 20;
			this.attackEntityAsMob(par1Entity);
		}
	}

	@Override
	public void setCombatTask()
	{
		if(this.entityAIArrowAttack == null || this.entityAIAttackOnCollide == null)
		{
			entityAIArrowAttack = new EntityAIArrowAttack(this, 0.25F, 20, 10.0F);
			entityAIAttackOnCollide = new EntityAIAttackOnCollide(this, .4F, false);
		}
		this.tasks.removeTask(this.entityAIArrowAttack);
		this.tasks.removeTask(this.entityAIAttackOnCollide);
		ItemStack weapon = this.getHeldItem();

		if (weapon != null && weapon.getItem() == Items.bow)
		{
			this.tasks.addTask(4, this.entityAIArrowAttack);
		}
		else
			this.tasks.addTask(4, this.entityAIAttackOnCollide);
	}

	@Override
	public void setCurrentItemOrArmor(int slot, ItemStack par2ItemStack)
	{
		super.setCurrentItemOrArmor(slot, par2ItemStack);

		if (!this.worldObj.isRemote && slot == 0)
		{
			this.setCombatTask();
		}
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
	}


}
