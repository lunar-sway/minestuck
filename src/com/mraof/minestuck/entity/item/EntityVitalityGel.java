package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.editmode.ServerEditHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityVitalityGel extends Entity implements IEntityAdditionalSpawnData
{
	public int cycle;

	public int age = 0;
	private int healAmount = 1;
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
	@Override
	protected boolean canTriggerWalking()
	{
		return false;
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (this.isEntityInvulnerable(source))
		{
			return false;
		} else
		{
			this.markVelocityChanged();
			this.health = (int)((float)this.health - amount);
			
			if (this.health <= 0)
			{
				this.setDead();
			}
			
			return false;
		}
	}
	
	@Override
	protected void entityInit() {}
	
	
	@SideOnly(Side.CLIENT)
	@Override
	public int getBrightnessForRender()
	{
		float f1 = 0.5F;

		int i = super.getBrightnessForRender();
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
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY -= 0.03D;
		
		if (this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ))).getMaterial() == Material.LAVA)
		{
			this.motionY = 0.2D;
			this.motionX = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
			this.motionZ = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
			this.playSound(SoundEvents.ENTITY_GENERIC_BURN, 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
		}
		
		double d0 = this.getSizeByValue() * 2.0D;
		
		if (this.targetCycle < this.cycle - 20 + this.getEntityId() % 100)
		{
			if (this.closestPlayer == null || this.closestPlayer.getDistanceSq(this) > d0 * d0)
			{
				this.closestPlayer = this.world.getClosestPlayerToEntity(this, d0);
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

		this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
		float f = 0.98F;
		
		if(this.onGround)
		{
			f = this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ))).getBlock().slipperiness * 0.98F;
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
		return this.world.handleMaterialAcceleration(this.getEntityBoundingBox(), Material.WATER, this);
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		nbt.setShort("health", (short)((byte)this.health));
		nbt.setShort("age", (short)this.age);
		nbt.setShort("amount", (short)this.healAmount);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		this.health = nbt.getShort("health") & 255;
		this.age = nbt.getShort("age");
		if(nbt.hasKey("amount", 99))
			this.healAmount = nbt.getShort("amount");
	}

	/**
	 * Called by a player entity when they collide with an entity
	 */
	@Override
	public void onCollideWithPlayer(EntityPlayer player)
	{
		if(this.world.isRemote?ClientEditHandler.isActive():ServerEditHandler.getData(player) != null)
			return;
		
		if (!this.world.isRemote)
		{
			this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.1F, 0.5F * ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.8F));
			player.heal(healAmount);
			this.setDead();
		}
		else  
			this.setDead();
	}
	
	@Override
	public boolean canBeAttackedWithItem()
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