package com.mraof.minestuck.entity.underling;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import com.mraof.minestuck.entity.ai.EntityAIAttackOnCollideWithRate;
import com.mraof.minestuck.util.GristHelper;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;
import com.mraof.minestuck.util.MinestuckAchievementHandler;

//Makes non-stop ogre puns
public class EntityOgre extends EntityUnderling 
{
	private EntityAIAttackOnCollideWithRate entityAIAttackOnCollideWithRate;
	public EntityOgre(World world)
	{
		super(world, "Ogre");
		setSize(3.0F, 4.5F);
		this.stepHeight = 1.0F;
	}

	@Override
	public GristSet getGristSpoils()
	{
		return GristHelper.getRandomDrop(type,3);
	}
	@Override
	public boolean attackEntityAsMob(Entity par1Entity) 
	{
		return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), (this.type.getPower() + 1) * 2);
	}
	@Override
	protected void setCombatTask() 
	{
		if(entityAIAttackOnCollideWithRate == null)
			entityAIAttackOnCollideWithRate = new EntityAIAttackOnCollideWithRate(this, .3F, 40, false);
		entityAIAttackOnCollideWithRate.setDistanceMultiplier(1.2F);
		this.tasks.removeTask(this.entityAIAttackOnCollideWithRate);
		this.tasks.addTask(4, entityAIAttackOnCollideWithRate);
	}
	@Override
	protected double getWanderSpeed() 
	{
		return 0.1;
	}
	@Override
	protected float getMaximumHealth() 
	{
		return type != null ? 16 * (type.getPower() + 1) + 8 : 0;
	}
	
	@Override
	public void onDeath(DamageSource cause)
	{
		super.onDeath(cause);
		Entity entity = cause.getEntity();
		if(this.dead && entity != null && entity instanceof EntityPlayerMP)
		{
			((EntityPlayerMP) entity).triggerAchievement(MinestuckAchievementHandler.killOgre);
		}
	}
	
	@Override
	protected boolean useAltName() 
	{
		if(type.equals(GristType.Tar))
			return true;
		return super.useAltName();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompund)
	{
		super.readFromNBT(tagCompund);
		this.experienceValue = (int) (5 * type.getPower() + 4);
	}
	
	@Override
	public void readSpawnData(ByteBuf additionalData)
	{
		super.readSpawnData(additionalData);
		this.experienceValue = (int) (5 * type.getPower() + 4);
	}
	
	@Override
	public IEntityLivingData func_180482_a(DifficultyInstance difficulty, IEntityLivingData livingData)
	{
		livingData = super.func_180482_a(difficulty, livingData);
		this.experienceValue = (int) (5 * type.getPower() + 4);
		return livingData;
	}
}