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
    private static final DataParameter<Integer> FROG_SIZE = EntityDataManager.<Integer>createKey(EntityFrog.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> SKIN_COLOR = EntityDataManager.<Integer>createKey(EntityFrog.class, DataSerializers.VARINT);
    
	public EntityFrog(World world)
	{
		super(world);
	}
	
	protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(FROG_SIZE, Integer.valueOf(1));
        this.dataManager.register(SKIN_COLOR, 0);
    }
	
	@Override
	public String getTexture()
	{
		return "textures/mobs/frog/base.png";
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
	
	protected void setFrogSize(int size, boolean p_70799_2_)
    {
        this.dataManager.set(FROG_SIZE, Integer.valueOf(size));
        this.setSize(0.51000005F * (float)size, 0.51000005F * (float)size);
        this.setPosition(this.posX, this.posY, this.posZ);
        //this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((double)(size * size));
        //this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double)(0.2F + 0.1F * (float)size));

        if (p_70799_2_)
        {
            this.setHealth(this.getMaxHealth());
        }

        this.experienceValue = size;
    }
	
	public int getFrogSize()
    {
        return ((Integer)this.dataManager.get(FROG_SIZE)).intValue();
    }

    public static void registerFixesFrog(DataFixer fixer)
    {
        EntityLiving.registerFixesMob(fixer, EntityFrog.class);
    }
	
	public void writeEntityToNBT(NBTTagCompound compound)
    {
		System.out.println("write");
        super.writeEntityToNBT(compound);
        compound.setInteger("Size", this.getFrogSize() - 1);
        compound.setInteger("skinColor", this.getSkinColor());
       // compound.setBoolean("wasOnGround", this.wasOnGround);
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
    	System.out.println("read");
        super.readEntityFromNBT(compound);
        int i = compound.getInteger("Size");

        if (i < 0)
            i = 0;

        if (compound.hasKey("skinColor")) {
        	System.out.println("found skin color");
			if (compound.getInteger("skinColor") == 0) {
				System.out.println("skin color == 0");
				this.setSkinColor(0);
			} else {
				System.out.println("skin color != 0");
				this.setSkinColor(compound.getInteger("skinColor"));
			}
		}
		else {
			System.out.println("couldn't find skin color");
			this.setSkinColor(0);
		}
        
        this.setFrogSize(i + 1, false);
        //this.wasOnGround = compound.getBoolean("wasOnGround");
    }
	
	public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (FROG_SIZE.equals(key))
        {
            int i = this.getFrogSize();
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
    	System.out.println("setting skin color to " + i);
    	this.dataManager.set(SKIN_COLOR, i);
    	System.out.println("skin color set to " + i);
	}

	public int getSkinColor() {
		// TODO Auto-generated method stub
		return 0;
	}
}