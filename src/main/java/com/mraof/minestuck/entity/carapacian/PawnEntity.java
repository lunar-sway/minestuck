package com.mraof.minestuck.entity.carapacian;

import com.mraof.minestuck.item.MSItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class PawnEntity extends CarapacianEntity implements IRangedAttackMob, IMob
{
	private static Random randStatic = new Random();
	private final RangedAttackGoal aiArrowAttack = new RangedAttackGoal(this, 0.25F, 20, 10.0F);
	private final MeleeAttackGoal aiMeleeAttack = new MeleeAttackGoal(this, .4F, false);
	
	protected PawnEntity(EntityType<? extends PawnEntity> type, EnumEntityKingdom kingdom, World world)
	{
		super(type, kingdom, world);
		this.experienceValue = 1;
		setCombatTask();
	}
	
	public static PawnEntity createProspitian(EntityType<? extends PawnEntity> type, World world)
	{
		return new PawnEntity(type, EnumEntityKingdom.PROSPITIAN, world);
	}
	
	public static PawnEntity createDersite(EntityType<? extends PawnEntity> type, World world)
	{
		return new PawnEntity(type, EnumEntityKingdom.DERSITE, world);
	}
	
	@Override
	protected void registerAttributes()
	{
		super.registerAttributes();
		getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		
		getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, true, false, entity -> attackEntitySelector.isEntityApplicable(entity)));
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
	{
		super.setEquipmentBasedOnDifficulty(difficulty);
		this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(randStatic.nextDouble() < .25 ? Items.BOW : rand.nextDouble() < .2 ? MSItems.REGISWORD : rand.nextDouble() < .02 ? MSItems.SORD : Items.STONE_SWORD));
	}
	
	@Override
	public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor)
	{
		ArrowEntity arrow = new ArrowEntity(this.world, this);
		double d0 = target.getPosX() - this.getPosX();
		double d1 = target.getBoundingBox().minY + (double)(target.getHeight() / 3.0F) - arrow.getPosY();
		double d2 = target.getPosZ() - this.getPosZ();
		double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
		arrow.shoot(d0, d1 + d3 * 0.2D, d2, 1.6F, 12.0F);
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
		this.world.addEntity(arrow);
	}

	/**
	 * Returns the amount of damage a mob should deal.
	 */
	public float getAttackStrength(Entity par1Entity)
	{
		ItemStack weapon = this.getHeldItemMainhand();
		float damage = 2;

		if (!weapon.isEmpty())
			damage += (float)this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue();
		
		damage += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((LivingEntity) par1Entity).getCreatureAttribute());
		
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
			par1Entity.addVelocity(-MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F) * (float)knockback * 0.5F, 0.1D, (double)(MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F) * (float)knockback * 0.5F));

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
	
	private void setCombatTask()
	{
		if(this.world != null && !this.world.isRemote)
		{
			this.goalSelector.removeGoal(this.aiArrowAttack);
			this.goalSelector.removeGoal(this.aiMeleeAttack);
			ItemStack weapon = this.getHeldItemMainhand();
			
			if(!weapon.isEmpty() && weapon.getItem() == Items.BOW)
			{
				this.goalSelector.addGoal(4, this.aiArrowAttack);
			} else
				this.goalSelector.addGoal(4, this.aiMeleeAttack);
		}
	}
	
	@Override
	public void readAdditional(CompoundNBT compound)
	{
		super.readAdditional(compound);
		setCombatTask();
	}
	
	@Override
	public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack)
	{
		super.setItemStackToSlot(slotIn, stack);
		
		if (!this.world.isRemote)
		{
			this.setCombatTask();
		}
	}
	
	@Nullable
	@Override
	public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag)
	{
		spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
		
		setEquipmentBasedOnDifficulty(difficultyIn);
		this.setEnchantmentBasedOnDifficulty(difficultyIn);
		
		setCombatTask();
		return spawnDataIn;
	}
}