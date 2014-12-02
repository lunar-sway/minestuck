package com.mraof.minestuck.entity.underling;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import com.mraof.minestuck.entity.EntityListFilter;
import com.mraof.minestuck.entity.EntityMinestuck;
import com.mraof.minestuck.entity.ai.EntityAIHurtByTargetAllied;
import com.mraof.minestuck.entity.ai.EntityAINearestAttackableTargetWithHeight;
import com.mraof.minestuck.entity.item.EntityGrist;
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
	
	public EntityUnderling(World par1World, GristType type, String underlingName) 
	{
		super(par1World, type, underlingName);
		
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
	protected void setCustomStartingVariables(Object[] objects) 
	{
		this.type = (GristType)objects[0];
		this.underlingName = (String)objects[1];
	}
	//used when getting how much grist should be dropped on death
	public abstract GristSet getGristSpoils();

	protected abstract void setCombatTask();
	
	protected abstract float getMaximumHealth();

	protected abstract float getWanderSpeed();
	protected boolean useAltName()
	{
		return false;
	};
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
		return "textures/mobs/" + type.getName() + underlingName + ".png";
	}
	
	@Override
	public String getName() 
	{
		return StatCollector.translateToLocalFormatted("entity." + underlingName + ".type", type.getDisplayName());
	}
//	@Override
//	protected boolean isAIEnabled()
//	{
//		return true;
//	}
	
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
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) 
	{
		super.writeEntityToNBT(par1nbtTagCompound);
		par1nbtTagCompound.setString("Type", this.type.getName());
	}
	@Override
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) 
	{
		super.readEntityFromNBT(par1nbtTagCompound);
		this.type = GristType.getTypeFromString(par1nbtTagCompound.getString("Type"));
	}
//	@Override
//	public void writeSpawnData(ByteBuf data) 
//	{
//		data.writeInt(((Enum<GristType>) type).ordinal());
//	}
//	@Override
//	public void readSpawnData(ByteBuf data) 
//	{
//		this.type = type.getClass().getEnumConstants()[data.readInt()];
//		this.textureResource = new ResourceLocation("minestuck", this.getTexture());
//	}

	@Override
	   public boolean getCanSpawnHere()
	{
		return this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL && this.isValidLightLevel() && super.getCanSpawnHere();
	}
	
	protected boolean isValidLightLevel()
	{
		
		int i = MathHelper.floor_double(this.posX);
		int j = MathHelper.floor_double(this.getBoundingBox().minY);
		int k = MathHelper.floor_double(this.posZ);
		
	   //	if (this.worldObj.getBlockLightOpacity(i, j, k) == 0) { //Prevents spawning IN blocks
	   //		return false;
	   //	}
	   // Debug.print("Spawning an entity...");

		//Debug.print("Sunlight level is "+this.worldObj.getSavedLightValue(EnumSkyBlock.Sky, i, j, k));
//		if (this.worldObj.getSavedLightValue(EnumSkyBlock.SKY, i, j, k) > this.rand.nextInt(32))
//		{	Don't know what to replace here
//			//Debug.print("Too much sun! Failed.");
//			return false;
//		}
//		else
		{
			int l = this.worldObj.getBlockLightOpacity(new BlockPos(i, j, k));	//Might be wrong method

			if (this.worldObj.isThundering())
			{
				int i1 = this.worldObj.getSkylightSubtracted();
				this.worldObj.setSkylightSubtracted(10);
				l = this.worldObj.getBlockLightOpacity(new BlockPos(i, j, k));	//Might be wrong method
				this.worldObj.setSkylightSubtracted(i1);
			}

			//Debug.print("Light level calculated as " + l);
			
			return l <= this.rand.nextInt(8);
        }
    }
	
	@Override
	public void writeSpawnData(ByteBuf buffer) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void readSpawnData(ByteBuf additionalData) {
		// TODO Auto-generated method stub
		
	}
	
}
