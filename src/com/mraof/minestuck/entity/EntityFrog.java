package com.mraof.minestuck.entity;

import com.mraof.minestuck.entity.ai.frog.EntityAIPanicHop;
import com.mraof.minestuck.entity.ai.frog.EntityAIStopHopping;
import com.mraof.minestuck.entity.ai.frog.EntityAIWanderHop;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

public class EntityFrog extends EntityMinestuck
{
	
	private int jumpTicks;
    private int jumpDuration;
    private boolean shouldJump = false;
    private static final DataParameter<Float> FROG_SIZE = EntityDataManager.<Float>createKey(EntityFrog.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> SKIN_COLOR = EntityDataManager.<Integer>createKey(EntityFrog.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> EYE_COLOR = EntityDataManager.<Integer>createKey(EntityFrog.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> BELLY_COLOR = EntityDataManager.<Integer>createKey(EntityFrog.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> EYE_TYPE = EntityDataManager.<Integer>createKey(EntityFrog.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> BELLY_TYPE = EntityDataManager.<Integer>createKey(EntityFrog.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> TYPE = EntityDataManager.<Integer>createKey(EntityFrog.class, DataSerializers.VARINT);
    
	
	public EntityFrog(World world)
	{
		super(world);
	}
	
	protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(FROG_SIZE, 1f);
        this.dataManager.register(SKIN_COLOR, 0);
        this.dataManager.register(EYE_COLOR, 0);
        this.dataManager.register(BELLY_COLOR, 0);
        this.dataManager.register(EYE_TYPE, 0);
        this.dataManager.register(BELLY_TYPE, 0);
        this.dataManager.register(TYPE, 0);
        
    }
	
	@Override
	public String getTexture()
	{
		String path;
		switch(getType())
		{
			default: case 0: path = "textures/mobs/frog/base.png";
			break;
			case 1: path = "textures/mobs/frog/totally_normal_frog.png";
			break;
			case 2: path = "textures/mobs/frog/ruby_contraband.png";
			break;
		}
		return path;
	}
	
	@Override
	protected float getMaximumHealth() 
	{
		return 5;
	}
	
	@Override
	protected void initEntityAI()
	{
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIPanicHop(this, 1.0D));
		tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 0.6F));
		tasks.addTask(5, new EntityAIWanderHop(this, 0.6D));
		tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		tasks.addTask(7, new EntityAILookIdle(this));
		tasks.addTask(8, new EntityAIStopHopping(this));
		
	}
	
	public void handleStatusUpdate(byte id)
    {
        if (id == 1)
        {
            //this.createRunningParticles();
            this.jumpDuration = 10;
            this.jumpTicks = 0;
        }
        else
        {
            super.handleStatusUpdate(id);
        }
    }
	

    public static void registerFixesFrog(DataFixer fixer)
    {
        EntityLiving.registerFixesMob(fixer, EntityFrog.class);
    }
	
	public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("Type", this.getType());
        compound.setFloat("Size", this.getFrogSize()+0.4f);
        compound.setInteger("skinColor", this.getSkinColor());
        compound.setInteger("eyeColor", this.getEyeColor());
        compound.setInteger("bellyColor", this.getBellyColor());
        compound.setInteger("eyeType", this.getEyeType());
        compound.setInteger("bellyType", this.getBellyType());
        //compound.setBoolean("wasOnGround", this.wasOnGround);
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        
        if(compound.hasKey("Type")) setType(compound.getInteger("Type"));
        else setType(1);
        
        if(compound.hasKey("Size"))
        {
	        float i = compound.getFloat("Size");
	        if (i <= 0) i = 0;
	        this.setFrogSize(i-0.4f, false);
        }
        else this.setFrogSize(0.6f, false);
        if (compound.hasKey("skinColor")) 
        {
			if (compound.getInteger("skinColor") == 0) 
				this.setSkinColor(0);
			
			else this.setSkinColor(compound.getInteger("skinColor"));
		}
		else this.setSkinColor(0);

        if (compound.hasKey("eyeColor")) 
        {
        	if (compound.getInteger("eyeColor") == 0) 
				this.setEyeColor(0);
			
			else this.setEyeColor(compound.getInteger("eyeColor"));
		}
		else this.setEyeColor(0);
        
        if (compound.hasKey("eyeType")) 
        {
        	if (compound.getInteger("eyeType") == 0) 
				this.setEyeType(0);
			
			else this.setEyeType(compound.getInteger("eyeType"));
		}
		else this.setEyeType(0);
        

        if (compound.hasKey("bellyColor")) 
        {
        	if (compound.getInteger("bellyColor") == 0) 
				this.setBellyColor(0);
			
			else this.setBellyColor(compound.getInteger("bellyColor"));
		}
		else this.setBellyColor(0);
        
        if (compound.hasKey("bellyType")) 
        {
        	if (compound.getInteger("bellyType") == 0) 
				this.setBellyType(0);
			
			else this.setBellyType(compound.getInteger("bellyType"));
		}
		else this.setBellyType(0);
        
        
        //this.wasOnGround = compound.getBoolean("wasOnGround");
    }
	
	public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (FROG_SIZE.equals(key))
        {
            float i = this.getFrogSize();
            this.setSize(0.51000005F * (float)i, 0.51000005F * (float)i);
            this.rotationYaw = this.rotationYawHead;
            this.renderYawOffset = this.rotationYawHead;

            if (this.isInWater() && this.rand.nextInt(20) == 0)
            {
                this.doWaterSplashEffect();
            }
        }

        super.notifyDataManagerChange(key);
    }
    
	public void startJumping()
    {
        //this.setJumping(true);
        this.getJumpHelper().setJumping();
        this.jumpDuration = 10;
        this.jumpTicks = 0;
        //System.out.println("startJumping");
        //System.out.println(jumpDuration);
    }
	
	public void setJump(boolean jump)
	{
		shouldJump = jump;
	}
	
	public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (this.jumpTicks != this.jumpDuration)
        {
            ++this.jumpTicks;
        }
        else if (this.jumpDuration != 0)
        {
            this.jumpTicks = 0;
            this.jumpDuration = 0;
            this.setJumping(false);
        }
        //System.out.println(jumpTicks + " / " + jumpDuration);
        if(shouldJump)
		{
			startJumping();
			setJump(false);
		}
    }
	
	public float setJumpCompletion(float f)
    {
        //System.out.println(jumpDuration != 0);
        return this.jumpDuration == 0 ? 0.0F : ((float)this.jumpTicks + f) / (float)this.jumpDuration;
    }

    private void setSkinColor(int i) 
    {
    	this.dataManager.set(SKIN_COLOR, i);
	}

	public int getSkinColor() 
	{
		return this.dataManager.get(SKIN_COLOR);
	}
	
	private void setEyeColor(int i)
	{
		this.dataManager.set(EYE_COLOR, i);
	}
	
	public int getEyeColor() 
	{
		return this.dataManager.get(EYE_COLOR);
	}

	private void setBellyColor(int i)
	{
		this.dataManager.set(BELLY_COLOR, i);
	}
	
	public int getBellyColor() 
	{
		return this.dataManager.get(BELLY_COLOR);
	}
	

	private void setEyeType(int i)
	{
		this.dataManager.set(EYE_TYPE, i);
	}
	
	public int getEyeType() 
	{
		return this.dataManager.get(EYE_TYPE);
	}

	private void setBellyType(int i)
	{
		this.dataManager.set(BELLY_TYPE, i);
	}
	
	public int getBellyType() 
	{
		return this.dataManager.get(BELLY_TYPE);
	}
	

	protected void setFrogSize(float size, boolean p_70799_2_)
    {
        this.dataManager.set(FROG_SIZE, Float.valueOf(size));
        this.setSize(0.51000005F * (float)size, 0.51000005F * (float)size);
        this.setPosition(this.posX, this.posY, this.posZ);
        //this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((double)(size * size));
        //this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double)(0.2F + 0.1F * (float)size));

        if (p_70799_2_)
        {
            this.setHealth(this.getMaxHealth());
        }

        this.experienceValue = (int)size;
    }
	
	public float getFrogSize()
    {
        return this.dataManager.get(FROG_SIZE);
    }
	
	private void setType(int i)
	{
		this.dataManager.set(TYPE, i);
	}
	
	public int getType()
	{
		return this.dataManager.get(TYPE);
	}
	
}