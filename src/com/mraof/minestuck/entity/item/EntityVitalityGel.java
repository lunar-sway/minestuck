package com.mraof.minestuck.entity.item;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.editmode.ServerEditHandler;

public class EntityVitalityGel extends Entity implements IEntityAdditionalSpawnData
{
	public int cycle;

	public int age = 0;
	private int healAmount;
	private int health = 5;
	
	private EntityPlayer closestPlayer;

	private int targetCycle;

	public EntityVitalityGel(World world, double x, double y, double z, int healAmount)
	{
		super(world);
		this.setSize(this.getSizeByValue(), 0.5F);
		this.setPosition(x, y, z);
		this.rotationYaw = (float)(Math.random() * 360.0D);
		this.motionX = (double)((float)(world.rand.nextGaussian() * 0.2D - 0.1D));
		this.motionY = (double)((float)(world.rand.nextGaussian() * 0.2D));
		this.motionZ = (double)((float)(world.rand.nextGaussian() * 0.2D - 0.1D));
		this.isImmuneToFire = true;
		
		this.healAmount = healAmount;
	}

	public EntityVitalityGel(World par1World)
	{
		super(par1World);
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
	 * prevent them from trampling crops
	 */
	protected boolean canTriggerWalking()
	{
		return false;
	}
	
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (this.isEntityInvulnerable(source))
		{
			return false;
		} else
		{
			this.setBeenAttacked();
			this.health = (int)((float)this.health - amount);
			
			if (this.health <= 0)
			{
				this.setDead();
			}
			
			return false;
		}
	}
	
	protected void entityInit() {}
	
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float par1)
	{
		float f1 = 0.5F;

		int i = super.getBrightnessForRender(par1);
		int j = i & 255;
		int k = i >> 16 & 255;
		j += (int)(f1 * 15.0F * 16.0F);

		if (j > 240)
		{
			j = 240;
		}

		return j | k << 16;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate()
	{
		super.onUpdate();
		
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY -= 0.03D;
		
		if (this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ))).getBlock().getMaterial() == Material.lava)
		{
			this.motionY = 0.2D;
			this.motionX = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
			this.motionZ = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
			this.playSound("random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
		}
		
		double d0 = this.getSizeByValue() * 2.0D;
		
		if (this.targetCycle < this.cycle - 20 + this.getEntityId() % 100)
		{
			if (this.closestPlayer == null || this.closestPlayer.getDistanceSqToEntity(this) > d0 * d0)
			{
				this.closestPlayer = this.worldObj.getClosestPlayerToEntity(this, d0);
			}
			
			this.targetCycle = this.cycle;
		}

		if (this.closestPlayer != null)
		{
			double d1 = (this.closestPlayer.posX - this.posX) / d0;
			double d2 = (this.closestPlayer.posY + (double)this.closestPlayer.getEyeHeight() - this.posY) / d0;
			double d3 = (this.closestPlayer.posZ - this.posZ) / d0;
			double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
			double d5 = this.getSizeByValue() * 2.0D - d4;

			if (d5 > 0.0D)
			{
				this.motionX += d1 / d4 * d5 * 0.1D;
				this.motionY += d2 / d4 * d5 * 0.1D;
				this.motionZ += d3 / d4 * d5 * 0.1D;
			}
		}

		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		float f = 0.98F;
		
		if(this.onGround)
		{
			f = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.98F;
		}
		
		this.motionX *= (double)f;
		this.motionY *= 0.98D;
		this.motionZ *= (double)f;

		if (this.onGround)
		{
			this.motionY *= -0.9D;
		}

		++this.cycle;
		++this.age;

		if (this.age >= 60000)
		{
			this.setDead();
		}
		
	}

	/**
	 * Returns if this entity is in water and will end up adding the waters velocity to the entity
	 */
	@Override
	public boolean handleWaterMovement()
	{
		return this.worldObj.handleMaterialAcceleration(this.getEntityBoundingBox(), Material.water, this);
	}
	
	public boolean attackEntityFrom(DamageSource source, int par2)
	{
		return false;
	}
	
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		nbt.setShort("health", (short)((byte)this.health));
		nbt.setShort("age", (short)this.age);
		nbt.setShort("amount", (short)this.healAmount);
	}
	
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		this.health = nbt.getShort("health") & 255;
		this.age = nbt.getShort("age");
		this.healAmount = nbt.getShort("amount");
	}

	/**
	 * Called by a player entity when they collide with an entity
	 */
	public void onCollideWithPlayer(EntityPlayer player)
	{
		if(this.worldObj.isRemote?ClientEditHandler.isActive():ServerEditHandler.getData(player) != null)
			return;
		
		if (!this.worldObj.isRemote)
		{
			this.playSound("random.pop", 0.1F, 0.5F * ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.8F));
			player.heal(healAmount);
			this.setDead();
		}
		else  
			this.setDead();
	}
	
	public boolean canAttackWithItem()
	{
		return false;
	}
	
	public float getSizeByValue() 
	{
		return (float) healAmount/4.0F;
	}
	
	@Override
	public void writeSpawnData(ByteBuf data) 
	{
		data.writeInt(this.healAmount);
	}
	
	@Override
	public void readSpawnData(ByteBuf data) 
	{
		this.healAmount = data.readInt();
		this.setSize(this.getSizeByValue(), 0.5F);
	}
}