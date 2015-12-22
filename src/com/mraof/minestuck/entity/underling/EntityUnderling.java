package com.mraof.minestuck.entity.underling;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import com.mraof.minestuck.entity.EntityListFilter;
import com.mraof.minestuck.entity.EntityMinestuck;
import com.mraof.minestuck.entity.ai.EntityAIHurtByTargetAllied;
import com.mraof.minestuck.entity.ai.EntityAINearestAttackableTargetWithHeight;
import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.util.GristAmount;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;

public abstract class EntityUnderling extends EntityMinestuck implements IEntityAdditionalSpawnData, IMob
{

	protected List<Class<? extends EntityLivingBase>> enemyClasses;
	@SuppressWarnings("unchecked")
	protected static EntityListFilter underlingSelector = new EntityListFilter(Arrays.<Class<? extends EntityLivingBase>>asList(EntityImp.class, EntityOgre.class, EntityBasilisk.class, EntityGiclops.class));
	protected EntityListFilter attackEntitySelector;
	//The type of the underling
	protected GristType type;
	//Name of underling, used in getting the texture and actually naming it
	public String underlingName;
	//random used in randomly choosing a type of creature
	protected static Random randStatic = new Random();
	
	public EntityUnderling(World par1World, String underlingName) 
	{
		super(par1World);
		
		this.underlingName = underlingName;
		enemyClasses = new ArrayList<Class<? extends EntityLivingBase>>();
		setEnemies();

		this.tasks.addTask(1, new EntityAISwimming(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTargetAllied(this, underlingSelector));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTargetWithHeight(this, EntityPlayer.class, 128.0F, 2, true, false));
		this.targetTasks.addTask(2, this.entityAINearestAttackableTargetWithHeight());
		this.tasks.addTask(5, new EntityAIWander(this, this.getWanderSpeed()));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));

		if (par1World != null && !par1World.isRemote)
		{
			this.setCombatTask();
		}

	}
	
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
		
		this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue((double)(this.getKnockbackResistance()));
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(this.getWanderSpeed());
	}
	
	protected void applyGristType(GristType type, boolean fullHeal)
	{
		this.type = type;
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(this.getMaximumHealth());
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(this.getAttackDamage());
		if(fullHeal)
			this.setHealth(this.getMaxHealth());
	}
	
	//used when getting how much grist should be dropped on death
	public abstract GristSet getGristSpoils();

	protected abstract void setCombatTask();
	
	protected abstract float getMaximumHealth();
	
	protected abstract float getKnockbackResistance();
	
	protected abstract double getWanderSpeed();
	
	protected abstract double getAttackDamage();
	
	@Override
	public boolean attackEntityAsMob(Entity entityIn)
	{
		boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue());
		return flag;
	}
	
	@Override
	protected void onDeathUpdate() 
	{
		super.onDeathUpdate();
		if(this.deathTime == 20 && !this.worldObj.isRemote)
		{
			for(Object gristType : this.getGristSpoils().getArray())
				this.worldObj.spawnEntityInWorld(new EntityGrist(worldObj, this.posX + this.rand.nextDouble() * this.width - this.width / 2, this.posY, this.posZ + this.rand.nextDouble() * this.width - this.width / 2, (GristAmount) gristType));
		}
	}
	@Override
	public String getTexture() 
	{
		if(type == null)
			return "textures/mobs/" + GristType.Shale.getName() + underlingName + ".png";
		return "textures/mobs/" + type.getName() + underlingName + ".png";
	}
	
	@Override
	public String getCommandSenderName() 
	{
		if(type != null)
			return StatCollector.translateToLocalFormatted("entity.minestuck." + underlingName + ".type", type.getDisplayName());
		else return StatCollector.translateToFallback("entity.minestuck." + underlingName + ".name");
	}
	
	@Override
	public void setAttackTarget(EntityLivingBase par1EntityLivingBase) 
	{
		super.setAttackTarget(par1EntityLivingBase);
		if(par1EntityLivingBase != null)
		{
			this.addEnemy(par1EntityLivingBase.getClass());
		}
	}
	public void setEnemies()
	{
		attackEntitySelector = new EntityListFilter(enemyClasses);
	}

	public void addEnemy(Class<? extends EntityLivingBase> enemyClass)
	{
		if(!enemyClasses.contains(enemyClass))
		{
			enemyClasses.add(enemyClass);
			this.setEnemies();
			this.setCombatTask();
		}
	}
	EntityAINearestAttackableTargetWithHeight entityAINearestAttackableTargetWithHeight()
	{
		return new EntityAINearestAttackableTargetWithHeight(this, EntityLivingBase.class, 128.0F, 2, true, false, attackEntitySelector);
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound) 
	{
		super.writeEntityToNBT(tagCompound);
		tagCompound.setString("Type", this.type.getName());
		if(hasHome())
		{
			NBTTagCompound nbt = new NBTTagCompound();
			BlockPos home = getHomePosition();
			nbt.setInteger("homeX", home.getX());
			nbt.setInteger("homeY", home.getY());
			nbt.setInteger("homeZ", home.getZ());
			nbt.setInteger("maxHomeDistance", (int) getMaximumHomeDistance());
			tagCompound.setTag("homePos", nbt);
		}
	}
	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompound) 
	{
		if(tagCompound.hasKey("Type", 8))
			applyGristType(GristType.getTypeFromString(tagCompound.getString("Type")), false);
		else applyGristType(SburbHandler.getUnderlingType(this), false);
		super.readEntityFromNBT(tagCompound);
		
		if(tagCompound.hasKey("homePos", 10))
		{
			NBTTagCompound nbt = tagCompound.getCompoundTag("homePos");
			BlockPos pos = new BlockPos(nbt.getInteger("homeX"), nbt.getInteger("homeY"), nbt.getInteger("homeZ"));
			setHomePosAndDistance(pos, nbt.getInteger("maxHomeDistance"));
		}
	}
	
	@Override
	public boolean getCanSpawnHere()
	{
		return this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL && super.getCanSpawnHere();
	}
	
	@Override
	public void writeSpawnData(ByteBuf buffer)
	{
		buffer.writeInt(type.ordinal());
	}
	
	@Override
	public void readSpawnData(ByteBuf additionalData)
	{
		applyGristType(GristType.values()[additionalData.readInt()], false);
		this.textureResource = new ResourceLocation("minestuck", this.getTexture());
	}
	
	@Override
	public IEntityLivingData onSpawnFirstTime(DifficultyInstance difficulty, IEntityLivingData livingData)
	{
		
		if(!(livingData instanceof UnderlingData))
		{
			applyGristType(SburbHandler.getUnderlingType(this), true);
			livingData = new UnderlingData(this.type);
		} else
		{
			applyGristType(((UnderlingData)livingData).type, true);
		}
		
		return super.onSpawnFirstTime(difficulty, livingData);
	}
	
	@Override
	protected boolean canDespawn()
	{
		return !this.hasHome();
	}
	
	protected class UnderlingData implements IEntityLivingData
	{
		public final GristType type;
		public UnderlingData(GristType type)
		{
			this.type = type;
		}
	}
	
}
