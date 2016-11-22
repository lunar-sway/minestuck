package com.mraof.minestuck.entity.carapacian;

import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import com.mraof.minestuck.item.MinestuckItems;

public abstract class EntityPawn extends EntityCarapacian implements IRangedAttackMob, IMob
{
	private static Random randStatic = new Random();
	private EntityAIAttackRanged entityAIArrowAttack = new EntityAIAttackRanged(this, 0.25F, 20, 10.0F);
	private EntityAIAttackMelee entityAIAttackOnCollide = new EntityAIAttackMelee(this, .4F, false);
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
	public float getWanderSpeed() 
	{
		return .3F;
	}
	
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData entityLivingData)	//was called "onSpawnWithEgg"
	{
		entityLivingData = super.onInitialSpawn(difficulty, entityLivingData);
		setEquipmentBasedOnDifficulty(difficulty);
		
		if(this.pawnType == 1)
		{
			this.targetTasks.addTask(4, entityAIArrowAttack);
		}
		else
			this.tasks.addTask(4, this.entityAIAttackOnCollide);
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(this.pawnType == 1 ? Items.BOW : rand.nextDouble() < .2 ? MinestuckItems.regisword : rand.nextDouble() < .02 ? MinestuckItems.sord : Items.STONE_SWORD));
		this.setEnchantmentBasedOnDifficulty(difficulty);	//was called "enchantEquipment"
		return entityLivingData;
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float f1) 
	{
		EntityArrow arrow = new EntityTippedArrow(this.world, this);
		double d0 = target.posX - this.posX;
		double d1 = target.getEntityBoundingBox().minY + (double)(target.height / 3.0F) - arrow.posY;
		double d2 = target.posZ - this.posZ;
		double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
		arrow.setThrowableHeading(d0, d1 + d3 * 0.2D, d2, 1.6F, 12.0F);
		int power = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.POWER, this);
		int punch = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.PUNCH, this);

		if(power > 0)
		{
			arrow.setDamage(arrow.getDamage() + (double)power * 0.5D + 0.5D);
		}

		if(punch > 0)
		{
			arrow.setKnockbackStrength(punch);
		}

		if(EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FLAME, this) > 0)
		{
			arrow.setFire(100);
		}
		
		playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		//		EntityPawn pawn = this.getClass() == EntityWhitePawn.class ? new EntityWhitePawn(this.worldObj, 0) : new EntityBlackPawn(this.worldObj, 0);
		//		pawn.setLocationAndAngles(this.posX, this.posY, this.posZ, 0, 0);
		//		pawn.initCreature();
		//		this.worldObj.spawnEntityInWorld(pawn);	
		//I was just messing around to see if I could make an EntityLiving spawn more EntityLiving, it can
		this.world.spawnEntity(arrow);
	}

	/**
	 * Returns the amount of damage a mob should deal.
	 */
	public float getAttackStrength(Entity par1Entity)
	{
		ItemStack weapon = this.getHeldItemMainhand();
		float damage = 2;

		if (weapon != null)
			damage += 
				(float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
		
		damage += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase) par1Entity).getCreatureAttribute());
		
		return damage;
	}

	@Override
	public boolean attackEntityAsMob(Entity par1Entity)
	{
		float damage = this.getAttackStrength(par1Entity);
		int fireAspectLevel = EnchantmentHelper.getFireAspectModifier(this);
		int knockback = EnchantmentHelper.getKnockbackModifier(this);

		if (fireAspectLevel > 0 && !par1Entity.isBurning())
			par1Entity.setFire(1);

		if (knockback > 0)
			par1Entity.addVelocity((double)(-MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F) * (float)knockback * 0.5F), 0.1D, (double)(MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F) * (float)knockback * 0.5F));

		return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
	}
	
//	/**
//	 * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
//	 */
//	@Override
//	protected void attackEntity(Entity par1Entity, float par2)
//	{
//		if (this.attackTime <= 0 && par2 < 2.0F && par1Entity.getEntityBoundingBox().maxY > this.getEntityBoundingBox().minY && par1Entity.getEntityBoundingBox().minY < this.getEntityBoundingBox().maxY)
//		{
//			this.attackTime = 20;
//			this.attackEntityAsMob(par1Entity);
//		}
//	}
	
	@Override
	public void setCombatTask()
	{
		if(this.entityAIArrowAttack == null || this.entityAIAttackOnCollide == null)
		{
			entityAIArrowAttack = new EntityAIAttackRanged(this, 0.25F, 20, 10.0F);
			entityAIAttackOnCollide = new EntityAIAttackMelee(this, .4F, false);
		}
		this.tasks.removeTask(this.entityAIArrowAttack);
		this.tasks.removeTask(this.entityAIAttackOnCollide);
		ItemStack weapon = this.getHeldItemMainhand();

		if (weapon != null && weapon.getItem() == Items.BOW)
		{
			this.tasks.addTask(4, this.entityAIArrowAttack);
		}
		else
			this.tasks.addTask(4, this.entityAIAttackOnCollide);
	}
	
	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack)
	{
		super.setItemStackToSlot(slotIn, stack);

		if (!this.world.isRemote && slotIn == EntityEquipmentSlot.MAINHAND)
		{
			this.setCombatTask();
		}
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
	}
	
}
