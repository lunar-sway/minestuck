package com.mraof.minestuck.entity.carapacian;

import com.mraof.minestuck.entity.ai.attack.AnimatedAttackWhenInRangeGoal;
import com.mraof.minestuck.entity.ai.attack.MoveToTargetGoal;
import com.mraof.minestuck.entity.animation.MobAnimation;
import com.mraof.minestuck.entity.animation.PhasedMobAnimation;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.AnimationControllerUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PawnEntity extends CarapacianEntity implements RangedAttackMob, Enemy, GeoEntity, PhasedMobAnimation.Phases.Holder
{
	private static final EntityDataAccessor<Integer> CURRENT_ACTION = SynchedEntityData.defineId(PawnEntity.class, EntityDataSerializers.INT);
	
	public static final PhasedMobAnimation MELEE_ANIMATION = new PhasedMobAnimation(new MobAnimation(MobAnimation.Action.MELEE, 18, true, false), 3, 6, 7);
	private static final RawAnimation WALK_ANIMATION = RawAnimation.begin().thenLoop("walk");
	private static final RawAnimation ARMS_WALKING_ANIMATION = RawAnimation.begin().thenLoop("walkarms");
	private static final RawAnimation PUNCH_ANIMATION_1 = RawAnimation.begin().then("punch1", Animation.LoopType.PLAY_ONCE);
	private static final RawAnimation DIE_ANIMATION = RawAnimation.begin().then("die", Animation.LoopType.PLAY_ONCE);
	
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	private final RangedAttackGoal aiArrowAttack = new RangedAttackGoal(this, 5 / 4F, 20, 10.0F);
	private final MeleeAttackGoal aiMeleeAttack = new MeleeAttackGoal(this, 2F, false);
	
	protected PawnEntity(EntityType<? extends PawnEntity> type, EnumEntityKingdom kingdom, Level level)
	{
		super(type, kingdom, level);
		this.xpReward = 1;
		setCombatTask();
	}
	
	public static PawnEntity createProspitian(EntityType<? extends PawnEntity> type, Level level)
	{
		return new PawnEntity(type, EnumEntityKingdom.PROSPITIAN, level);
	}
	
	public static PawnEntity createDersite(EntityType<? extends PawnEntity> type, Level level)
	{
		return new PawnEntity(type, EnumEntityKingdom.DERSITE, level);
	}
	
	public static AttributeSupplier.Builder pawnAttributes()
	{
		return CarapacianEntity.carapacianAttributes().add(Attributes.ATTACK_DAMAGE)
				.add(Attributes.MOVEMENT_SPEED, 0.2);
	}
	
	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache()
	{
		return this.cache;
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		this.goalSelector.addGoal(2, new AnimatedAttackWhenInRangeGoal<>(this, MELEE_ANIMATION));
		this.goalSelector.addGoal(3, new MoveToTargetGoal(this, 1F, false));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, true, false, entity -> attackEntitySelector.isEntityApplicable(entity)));
	}
	
	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		entityData.define(CURRENT_ACTION, MobAnimation.IDLE_ACTION.ordinal());
	}
	
	@Override
	public PhasedMobAnimation.Phases getPhase()
	{
		return PhasedMobAnimation.Phases.values()[this.entityData.get(CURRENT_ACTION)];
	}
	
	@Override
	public void setAnimationPhase(PhasedMobAnimation.Phases phase, MobAnimation.Action action)
	{
		this.entityData.set(CURRENT_ACTION, phase.ordinal());
	}
	
	@Override
	protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty)
	{
		super.populateDefaultEquipmentSlots(random, difficulty);
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(random.nextDouble() < .25 ? Items.BOW : random.nextDouble() < .2 ? MSItems.REGISWORD.get() : random.nextDouble() < .02 ? MSItems.SORD.get() : Items.STONE_SWORD));
	}
	
	@Override
	public void performRangedAttack(LivingEntity target, float distanceFactor)
	{
		Arrow arrow = new Arrow(this.level(), this);
		double d0 = target.getX() - this.getX();
		double d1 = target.getBoundingBox().minY + (double) (target.getBbHeight() / 3.0F) - arrow.getY();
		double d2 = target.getZ() - this.getZ();
		double d3 = Math.sqrt(d0 * d0 + d2 * d2);
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
		this.level().addFreshEntity(arrow);
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
			par1Entity.push(-Mth.sin(this.getYRot() * (float) Math.PI / 180.0F) * (float) knockback * 0.5F, 0.1D, (double) (Mth.cos(this.getYRot() * (float) Math.PI / 180.0F) * (float) knockback * 0.5F));
		
		return par1Entity.hurt(this.damageSources().mobAttack(this), damage);
	}
	
	private void setCombatTask()
	{
		if(this.level() != null && !this.level().isClientSide)
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
	public void readAdditionalSaveData(CompoundTag compound)
	{
		super.readAdditionalSaveData(compound);
		setCombatTask();
	}
	
	@Override
	public void setItemSlot(EquipmentSlot slotIn, ItemStack stack)
	{
		super.setItemSlot(slotIn, stack);
		
		if(!this.level().isClientSide)
		{
			this.setCombatTask();
		}
	}
	
	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag)
	{
		spawnDataIn = super.finalizeSpawn(level, difficultyIn, reason, spawnDataIn, dataTag);
		
		populateDefaultEquipmentSlots(level.getRandom(), difficultyIn);
		this.populateDefaultEquipmentEnchantments(level.getRandom(), difficultyIn);
		
		setCombatTask();
		return spawnDataIn;
	}
	
	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers)
	{
		controllers.add(AnimationControllerUtil.createAnimation(this, "walkArmsAnimation", 1, PawnEntity::walkArmsAnimation));
		controllers.add(AnimationControllerUtil.createAnimation(this, "walkAnimation", 1, PawnEntity::walkAnimation));
		controllers.add(AnimationControllerUtil.createAnimation(this, "deathAnimation", 1, PawnEntity::deathAnimation));
		controllers.add(AnimationControllerUtil.createAnimation(this, "swingAnimation", 2, PawnEntity::swingAnimation));
	}
	
	private static PlayState walkAnimation(AnimationState<PawnEntity> state)
	{
		if(state.isMoving())
		{
			state.getController().setAnimation(WALK_ANIMATION);
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}
	
	private static PlayState walkArmsAnimation(AnimationState<PawnEntity> state)
	{
		if(state.isMoving() && !state.getAnimatable().isActive())
		{
			state.getController().setAnimation(ARMS_WALKING_ANIMATION);
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}
	
	private static PlayState deathAnimation(AnimationState<PawnEntity> event)
	{
		if(event.getAnimatable().dead)
		{
			event.getController().setAnimation(DIE_ANIMATION);
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}
	
	private static PlayState swingAnimation(AnimationState<PawnEntity> event)
	{
		if(event.getAnimatable().isActive())
		{
			event.getController().setAnimation(PUNCH_ANIMATION_1);
			return PlayState.CONTINUE;
		}
		event.getController().forceAnimationReset();
		return PlayState.STOP;
	}
}