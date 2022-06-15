package com.mraof.minestuck.entity.carapacian;

import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.AnimationUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
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
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

public class PawnEntity extends CarapacianEntity implements IRangedAttackMob, IMob, IAnimatable
{
	private final AnimationFactory factory = new AnimationFactory(this);
	private final RangedAttackGoal aiArrowAttack = new RangedAttackGoal(this, 5 / 4F, 20, 10.0F);
	private final MeleeAttackGoal aiMeleeAttack = new MeleeAttackGoal(this, 2F, false);
	
	protected PawnEntity(EntityType<? extends PawnEntity> type, EnumEntityKingdom kingdom, World world)
	{
		super(type, kingdom, world);
		this.xpReward = 1;
		setCombatTask();
	}
	
	@Override
	public AnimationFactory getFactory()
	{
		return this.factory;
	}
	
	public static PawnEntity createProspitian(EntityType<? extends PawnEntity> type, World world)
	{
		return new PawnEntity(type, EnumEntityKingdom.PROSPITIAN, world);
	}
	
	public static PawnEntity createDersite(EntityType<? extends PawnEntity> type, World world)
	{
		return new PawnEntity(type, EnumEntityKingdom.DERSITE, world);
	}
	
	public static AttributeModifierMap.MutableAttribute pawnAttributes()
	{
		return CarapacianEntity.carapacianAttributes().add(Attributes.ATTACK_DAMAGE)
				.add(Attributes.MOVEMENT_SPEED, 0.2);
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		this.goalSelector.addGoal(2, new SlowAttackInPlaceGoal(this, 6, 12));
		this.goalSelector.addGoal(3, new MoveToTargetGoal(this, 1F, false));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, true, false, entity -> attackEntitySelector.isEntityApplicable(entity)));
	}
	
	@Override
	protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty)
	{
		super.populateDefaultEquipmentSlots(difficulty);
		this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(random.nextDouble() < .25 ? Items.BOW : random.nextDouble() < .2 ? MSItems.REGISWORD : random.nextDouble() < .02 ? MSItems.SORD : Items.STONE_SWORD));
	}
	
	@Override
	public void performRangedAttack(LivingEntity target, float distanceFactor)
	{
		ArrowEntity arrow = new ArrowEntity(this.level, this);
		double d0 = target.getX() - this.getX();
		double d1 = target.getBoundingBox().minY + (double) (target.getBbHeight() / 3.0F) - arrow.getY();
		double d2 = target.getZ() - this.getZ();
		double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
		arrow.shoot(d0, d1 + d3 * 0.2D, d2, 1.6F, 12.0F);
		int power = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER_ARROWS, this);
		int punch = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH_ARROWS, this);
		
		if(power > 0)
		{
			arrow.setBaseDamage(arrow.getBaseDamage() + (double) power * 0.5D + 0.5D);
		}
		
		if(punch > 0)
		{
			arrow.setKnockback(punch);
		}
		
		if(EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAMING_ARROWS, this) > 0)
		{
			arrow.setSecondsOnFire(100);
		}
		
		playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
		//		EntityPawn pawn = this.getClass() == EntityWhitePawn.class ? new EntityWhitePawn(this.worldObj, 0) : new EntityBlackPawn(this.worldObj, 0);
		//		pawn.setLocationAndAngles(this.posX, this.posY, this.posZ, 0, 0);
		//		pawn.initCreature();
		//		this.worldObj.spawnEntityInWorld(pawn);	
		//I was just messing around to see if I could make an EntityLiving spawn more EntityLiving, it can
		this.level.addFreshEntity(arrow);
	}
	
	/**
	 * Returns the amount of damage a mob should deal.
	 */
	public float getAttackStrength(Entity par1Entity)
	{
		ItemStack weapon = this.getMainHandItem();
		float damage = 2;
		
		if(!weapon.isEmpty())
			damage += (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
		
		damage += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity) par1Entity).getMobType());
		
		return damage;
	}
	
	@Override
	public boolean doHurtTarget(Entity par1Entity)
	{
		float damage = this.getAttackStrength(par1Entity);
		int fireAspectLevel = EnchantmentHelper.getFireAspect(this);
		int knockback = EnchantmentHelper.getKnockbackBonus(this);
		
		if(fireAspectLevel > 0 && !par1Entity.isOnFire())
			par1Entity.setSecondsOnFire(1);
		
		if(knockback > 0)
			par1Entity.push(-MathHelper.sin(this.yRot * (float) Math.PI / 180.0F) * (float) knockback * 0.5F, 0.1D, (double) (MathHelper.cos(this.yRot * (float) Math.PI / 180.0F) * (float) knockback * 0.5F));
		
		return par1Entity.hurt(DamageSource.mobAttack(this), damage);
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
		if(this.level != null && !this.level.isClientSide)
		{
			this.goalSelector.removeGoal(this.aiArrowAttack);
			this.goalSelector.removeGoal(this.aiMeleeAttack);
			ItemStack weapon = this.getMainHandItem();
			
			if(!weapon.isEmpty() && weapon.getItem() == Items.BOW)
			{
				this.goalSelector.addGoal(4, this.aiArrowAttack);
			} else
				this.goalSelector.addGoal(4, this.aiMeleeAttack);
		}
	}
	
	@Override
	public void readAdditionalSaveData(CompoundNBT compound)
	{
		super.readAdditionalSaveData(compound);
		setCombatTask();
	}
	
	@Override
	public void setItemSlot(EquipmentSlotType slotIn, ItemStack stack)
	{
		super.setItemSlot(slotIn, stack);
		
		if(!this.level.isClientSide)
		{
			this.setCombatTask();
		}
	}
	
	@Nullable
	@Override
	public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag)
	{
		spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
		
		populateDefaultEquipmentSlots(difficultyIn);
		this.populateDefaultEquipmentEnchantments(difficultyIn);
		
		setCombatTask();
		return spawnDataIn;
	}
	
	@Override
	public void registerControllers(AnimationData data)
	{
		data.addAnimationController(AnimationUtil.createAnimation(this, "walkArmsAnimation", 1, PawnEntity::walkArmsAnimation));
		data.addAnimationController(AnimationUtil.createAnimation(this, "walkAnimation", 1, PawnEntity::walkAnimation));
		data.addAnimationController(AnimationUtil.createAnimation(this, "deathAnimation", 1, PawnEntity::deathAnimation));
		data.addAnimationController(AnimationUtil.createAnimation(this, "swingAnimation", 2, PawnEntity::swingAnimation));
	}
	
	private static PlayState walkAnimation(AnimationEvent<PawnEntity> event)
	{
		if(event.isMoving())
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", true));
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}
	
	private static PlayState walkArmsAnimation(AnimationEvent<PawnEntity> event)
	{
		if(event.isMoving() && !event.getAnimatable().isAttacking())
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("walkarms", true));
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}
	
	private static PlayState deathAnimation(AnimationEvent<PawnEntity> event)
	{
		if(event.getAnimatable().dead)
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("die", false));
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}
	
	private static PlayState swingAnimation(AnimationEvent<PawnEntity> event)
	{
		if(event.getAnimatable().isAttacking())
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("punch1", false));
			return PlayState.CONTINUE;
		}
		event.getController().markNeedsReload();
		return PlayState.STOP;
	}
}