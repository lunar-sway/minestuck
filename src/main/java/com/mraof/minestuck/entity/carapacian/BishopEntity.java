package com.mraof.minestuck.entity.carapacian;

import com.mraof.minestuck.entity.ai.AttackByDistanceGoal;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.manager.AnimationData;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class BishopEntity extends CarapacianEntity implements IRangedAttackMob, IMob
{
	int burnTime;

	protected BishopEntity(EntityType<? extends BishopEntity> type, EnumEntityKingdom kingdom, World world)
	{
		super(type, kingdom, world);
		this.xpReward = 3;
	}
	
	public static BishopEntity createProspitian(EntityType<? extends BishopEntity> type, World world)
	{
		return new BishopEntity(type, EnumEntityKingdom.PROSPITIAN, world);
	}
	
	public static BishopEntity createDersite(EntityType<? extends BishopEntity> type, World world)
	{
		return new BishopEntity(type, EnumEntityKingdom.DERSITE, world);
	}
	
	public static AttributeModifierMap.MutableAttribute bishopAttributes()
	{
		return CarapacianEntity.carapacianAttributes().add(Attributes.MAX_HEALTH, 40)
				.add(Attributes.MOVEMENT_SPEED, 0.2);
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		this.goalSelector.addGoal(4, new AttackByDistanceGoal(this, 5/4F, 30, 64.0F));
		this.targetSelector.addGoal(2, new NearestAttackableExtendedGoal(this, LivingEntity.class, 0, true, false, entity -> attackEntitySelector.isEntityApplicable(entity)));
	}
	
	@Override
	public void performRangedAttack(LivingEntity target, float distanceFactor)
	{
		
		double distanceX = target.getX() - this.getX();
		double distanceY = target.getBoundingBox().minY + (double)(target.getBbHeight() / 2.0F) - (this.getY() + (double)(this.getBbHeight() / 2.0F));
		double distanceZ = target.getZ() - this.getZ();
		
		FireballEntity fireball = new FireballEntity(this.level, this, distanceX, distanceY, distanceZ);
		fireball.explosionPower = 1;
		double d8 = this.getBbHeight();
		Vector3d vec3 = this.getViewVector(1.0F);
		double x = (this.getBoundingBox().minX + this.getBoundingBox().maxX) / 2.0F  + vec3.x * d8;
		double y = this.getY() + (double)(this.getBbHeight() / 2.0F);
		double z = (this.getBoundingBox().minZ + this.getBoundingBox().maxZ) / 2.0F + vec3.z * d8;
		fireball.setPos(x, y, z);
		this.level.addFreshEntity(fireball);
	}
	public int getAttackStrength(Entity par1Entity)
	{
		ItemStack var2 = this.getMainHandItem();
		int var3 = 0;
		
		/*if(!var2.isEmpty())
			var3 += var2.getItemDamage();*/
		
		return var3;
	}
	
//	/**
//	 * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
//	 */
//	protected void attackEntity(Entity par1Entity, float par2)
//	{
//
//		if (this.attackTime <= 0 && par2 < 2.0F && par1Entity.getBoundingBox().maxY > this.getBoundingBox().minY && par1Entity.getBoundingBox().minY < this.getBoundingBox().maxY)
//		{
//			this.attackTime = 20;
//			this.attackEntityAsMob(par1Entity);
//		}
//	}
	
	@Override
	public boolean doHurtTarget(Entity par1Entity)
	{
		int damage = this.getAttackStrength(par1Entity);
		int knockback = 6;
		par1Entity.push(-MathHelper.sin(this.yRot * (float)Math.PI / 180.0F) * (float)knockback * 0.5F, 0.1D, (double)(MathHelper.cos(this.yRot * (float)Math.PI / 180.0F) * (float)knockback * 0.5F));
		return par1Entity.hurt(DamageSource.mobAttack(this), damage);
	}
	
	@Override
	public boolean hurt(DamageSource par1DamageSource, float par2)
	{
		if(par1DamageSource.isFire())
		{
			burnTime += par2;
			if(burnTime <= 3)
				return false;
		}
		return super.hurt(par1DamageSource, par2);
	}

	@Override
	public void registerControllers(AnimationData data) {
		//TODO blockbench model + anims
	}

	private static class NearestAttackableExtendedGoal extends NearestAttackableTargetGoal<LivingEntity>
	{
		NearestAttackableExtendedGoal(MobEntity goalOwnerIn, Class<LivingEntity> targetClassIn, int targetChanceIn, boolean checkSight, boolean nearbyOnlyIn, @Nullable Predicate<LivingEntity> targetPredicate)
		{
			super(goalOwnerIn, targetClassIn, targetChanceIn, checkSight, nearbyOnlyIn, targetPredicate);
		}
		
		@Override
		protected AxisAlignedBB getTargetSearchArea(double targetDistance)
		{
			//Bishops use a much higher bounding box for some reason. Probably for their fire ball targeting
			return this.mob.getBoundingBox().inflate(targetDistance, 64.0D, targetDistance);
		}
	}
}