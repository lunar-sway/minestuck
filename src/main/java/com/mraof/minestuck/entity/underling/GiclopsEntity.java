package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.entity.ai.attack.CustomMeleeAttackGoal;
import com.mraof.minestuck.item.crafting.alchemy.GristHelper;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.fluid.Fluid;
import net.minecraft.tags.ITag;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;

public class GiclopsEntity extends UnderlingEntity implements IAnimatable
{
	public GiclopsEntity(EntityType<? extends GiclopsEntity> type, World world)
	{
		super(type, world, 7);
		this.maxUpStep = 2;
	}
	
	public static AttributeModifierMap.MutableAttribute giclopsAttributes()
	{
		return UnderlingEntity.underlingAttributes().add(Attributes.MAX_HEALTH, 210)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.9).add(Attributes.MOVEMENT_SPEED, 0.23)
				.add(Attributes.ATTACK_DAMAGE, 10);
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		this.goalSelector.addGoal(3, new CustomMeleeAttackGoal(this, 1.0F, false, 50, 1.1F));
	}
	
	protected SoundEvent getAmbientSound()
	{
		return MSSoundEvents.ENTITY_GICLOPS_AMBIENT;
	}
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return MSSoundEvents.ENTITY_GICLOPS_HURT;
	}
	
	protected SoundEvent getDeathSound()
	{
		return MSSoundEvents.ENTITY_GICLOPS_DEATH;
	}
	
	@Override
	public GristSet getGristSpoils()
	{
		return GristHelper.generateUnderlingGristDrops(this, damageMap, 10);
	}
	
	@Override
	protected int getVitalityGel()
	{
		return random.nextInt(4) + 5;
	}
	
	@Override
	protected void onGristTypeUpdated(GristType type)
	{
		super.onGristTypeUpdated(type);
		applyGristModifier(Attributes.MAX_HEALTH, 46 * type.getPower(), AttributeModifier.Operation.ADDITION);
		applyGristModifier(Attributes.ATTACK_DAMAGE, 4.5 * type.getPower(), AttributeModifier.Operation.ADDITION);
		this.xpReward = (int) (7 * type.getPower() + 5);
	}
	
	@Override
	public void die(DamageSource cause)
	{
		super.die(cause);
		Entity entity = cause.getEntity();
		if(this.dead && !this.level.isClientSide)
		{
			computePlayerProgress((int) (200 + 3 * getGristType().getPower())); //still give xp up to top rung
			firstKillBonus(entity, (byte) (Echeladder.UNDERLING_BONUS_OFFSET + 4));
		}
	}
	
	//Reduced lag is worth not taking damage for being inside a wall
	@Override
	public boolean isInWall()
	{
		return false;
	}
	
	//Only pay attention to the top for water
	
	@Override
	public boolean updateFluidHeightAndDoFluidPushing(ITag<Fluid> fluidTag, double fluidFactor)
	{
		AxisAlignedBB realBox = this.getBoundingBox();
		this.setBoundingBox(new AxisAlignedBB(realBox.minX, realBox.maxY - 1, realBox.minZ, realBox.maxX, realBox.maxY, realBox.maxZ));
		boolean result = super.updateFluidHeightAndDoFluidPushing(fluidTag, fluidFactor);
		this.setBoundingBox(realBox);
		return result;
	}
	
	@Override
	public void move(MoverType typeIn, Vector3d pos)
	{
		AxisAlignedBB realBox = this.getBoundingBox();
		double minX = pos.x > 0 ? realBox.maxX - pos.x : realBox.minX;
		/*				y > 0 ? realBox.maxY - y : realBox.minY,*/
		double minY = realBox.minY;
		double minZ = pos.z > 0 ? realBox.maxZ - pos.z : realBox.minZ;
		double maxX = pos.x < 0 ? realBox.minX - pos.x : realBox.maxX;
		double maxY = pos.y < 0 ? realBox.minY - pos.y : realBox.maxY;
		double maxZ = pos.z < 0 ? realBox.minZ - pos.z : realBox.maxZ;
		this.setBoundingBox(new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ));
		super.move(typeIn, pos);
		AxisAlignedBB changedBox = this.getBoundingBox();
		this.setBoundingBox(realBox.move(changedBox.minX - minX, changedBox.minY - minY, changedBox.minZ - minZ));
		this.setLocationFromBoundingbox();
	}
	
	@Override
	public boolean isPickable()
	{
		return true;
	}
	
	@Override
	public void registerControllers(AnimationData data)
	{
	
	}
}