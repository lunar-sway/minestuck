package com.mraof.minestuck.entity.underling;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.mraof.minestuck.entity.EntityListAttackFilter;
import com.mraof.minestuck.entity.EntityMinestuck;
import com.mraof.minestuck.entity.ai.EntityAINearestAttackableTargetWithHeight;
import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.GristAmount;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public abstract class EntityUnderling extends EntityMinestuck implements IEntityAdditionalSpawnData, IMob
{

	protected List<Class<? extends EntityLivingBase>> enemyClasses;
	protected EntityListAttackFilter attackEntitySelector;
	//The type of the imp
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
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
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
	
	protected abstract float getMaxHealth();

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
		return "textures/mobs/" + (useAltName() ? type.getAltName() : type.getName() )+ underlingName + ".png";
	}
	//Gives each type of underling a unique name, so instead of all types being called entity.underlingName.name they are called entity.typeString.underlingName.name
	@Override
	public String getEntityName() 
	{
		String s = type.getAltName() + "." + underlingName;
		return StatCollector.translateToLocal("entity." + s + ".name");
	}
	@Override
	protected boolean isAIEnabled()
	{
		return true;
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
		attackEntitySelector = new EntityListAttackFilter(enemyClasses);
	}

	public void addEnemy(Class enemyClass)
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
		this.type = type.getTypeFromString(par1nbtTagCompound.getString("Type"));
	}
	@Override
	public void writeSpawnData(ByteArrayDataOutput data) 
	{
		data.writeInt(((Enum) type).ordinal());
	}
	@Override
	public void readSpawnData(ByteArrayDataInput data) 
	{
		this.type = type.getClass().getEnumConstants()[data.readInt()];
		this.textureResource = new ResourceLocation("minestuck", this.getTexture());
	}

	@Override
	   public boolean getCanSpawnHere()
    {
        return this.worldObj.difficultySetting > 0 && this.isValidLightLevel() && super.getCanSpawnHere();
    }
	
    protected boolean isValidLightLevel()
    {
    	
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.boundingBox.minY);
        int k = MathHelper.floor_double(this.posZ);
        
       //	if (this.worldObj.getBlockLightOpacity(i, j, k) == 0) { //Prevents spawning IN blocks
       //		return false;
       //	}
       // Debug.print("Spawning an entity...");

        //Debug.print("Sunlight level is "+this.worldObj.getSavedLightValue(EnumSkyBlock.Sky, i, j, k));
        if (this.worldObj.getSavedLightValue(EnumSkyBlock.Sky, i, j, k) > this.rand.nextInt(32))
        {
        	//Debug.print("Too much sun! Failed.");
            return false;
        }
        else
        {
            int l = this.worldObj.getBlockLightValue(i, j, k);

            if (this.worldObj.isThundering())
            {
                int i1 = this.worldObj.skylightSubtracted;
                this.worldObj.skylightSubtracted = 10;
                l = this.worldObj.getBlockLightValue(i, j, k);
                this.worldObj.skylightSubtracted = i1;
            }

            //Debug.print("Light level calculated as " + l);
            
            return l <= this.rand.nextInt(8);
        }
    }
}
