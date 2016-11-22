package com.mraof.minestuck.entity.item;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
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

import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.GristAmount;
import com.mraof.minestuck.util.GristHelper;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;
import com.mraof.minestuck.util.IdentifierHandler;

public class EntityGrist extends Entity implements IEntityAdditionalSpawnData
{
	public int cycle;

	public int gristAge = 0;

	private int gristHealth = 5;
	//Type of grist
	private GristType gristType = GristType.Build;
	private int gristValue = 1;

	private EntityPlayer closestPlayer;

	private int targetCycle;

	public EntityGrist(World world, double x, double y, double z, GristAmount gristData)
	{
		super(world);
		this.gristValue = gristData.getAmount();
		this.setSize(this.getSizeByValue(), this.getSizeByValue());
//		this.yOffset = this.height / 2.0F;
		this.setPosition(x, y, z);
		this.rotationYaw = (float)(Math.random() * 360.0D);
		this.motionX = (double)((float)(world.rand.nextGaussian() * 0.20000000298023224D - 0.10000000149011612D));
		this.motionY = (double)((float)(world.rand.nextGaussian() * 0.2D));
		this.motionZ = (double)((float)(world.rand.nextGaussian() * 0.20000000298023224D - 0.10000000149011612D));
		this.isImmuneToFire = true;

		this.gristType = gristData.getType();
	}

	public EntityGrist(World par1World)
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
	protected void entityInit() {}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (this.isEntityInvulnerable(source))
		{
			return false;
		} else
		{
			this.setBeenAttacked();
			this.gristHealth = (int)((float)this.gristHealth - amount);
			
			if (this.gristHealth <= 0)
			{
				this.setDead();
			}
			
			return false;
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
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

		//this.setPosition(this.posX, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0D, this.posZ);
		double d0 = this.getSizeByValue() * 2.0D;

		if (this.targetCycle < this.cycle - 20 + this.getEntityId() % 100) //Why should I care about the entityId
		{
			if (this.closestPlayer == null || this.closestPlayer.getDistanceSqToEntity(this) > d0 * d0)
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

		this.move(this.motionX, this.motionY, this.motionZ);
		float f = 0.98F;
		
		if(this.onGround)
		{
			f = this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ))).getBlock().slipperiness * 0.98F;
		}
		
		this.motionX *= (double)f;
		this.motionY *= 0.9800000190734863D;
		this.motionZ *= (double)f;

		if (this.onGround)
		{
			this.motionY *= -0.8999999761581421D;
		}

		++this.cycle;
		++this.gristAge;

		if (this.gristAge >= 60000)
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
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
	{
		par1NBTTagCompound.setShort("Health", (short)((byte)this.gristHealth));
		par1NBTTagCompound.setShort("Age", (short)this.gristAge);
		par1NBTTagCompound.setShort("Value", (short)this.gristValue);
		par1NBTTagCompound.setString("Type", this.gristType.getName());
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		this.gristHealth = par1NBTTagCompound.getShort("Health") & 255;
		this.gristAge = par1NBTTagCompound.getShort("Age");
		if(par1NBTTagCompound.hasKey("Value", 99))
			this.gristValue = par1NBTTagCompound.getShort("Value");
		if(par1NBTTagCompound.hasKey("Type", 8))
			this.gristType = GristType.getTypeFromString(par1NBTTagCompound.getString("Type"));
	}
	
	/**
	 * Called by a player entity when they collide with an entity
	 */
	@Override
	public void onCollideWithPlayer(EntityPlayer par1EntityPlayer)
	{
		if(this.world.isRemote?ClientEditHandler.isActive():ServerEditHandler.getData(par1EntityPlayer) != null)
			return;
		
		if (!this.world.isRemote)
		{
			this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.1F, 0.5F * ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.8F));
			par1EntityPlayer.onItemPickup(this, 1);
			this.addGrist(par1EntityPlayer);
			this.setDead();
		}
		else  
			this.setDead();
	}
	
	public void addGrist(EntityPlayer entityPlayer)
	{
		GristHelper.increase(IdentifierHandler.encode(entityPlayer), new GristSet(gristType, gristValue));
		MinestuckPlayerTracker.updateGristCache(IdentifierHandler.encode(entityPlayer));
	}
	
	@Override
	public boolean canBeAttackedWithItem()
	{
		return false;
	}
	
	public GristType getType() 
	{
		return gristType;
	}
	
	public static int typeInt(GristType type)
	{
		return type == null ? -1 : type.ordinal();
	
	}
	
	public float getSizeByValue() 
	{
		return (float) (Math.pow(gristValue, .25) / 3.0F);
	}
	
	@Override
	public void writeSpawnData(ByteBuf data) 
	{
		if(typeInt(this.gristType) < 0)
		{
			this.setDead();
		}
		data.writeInt(typeInt(this.gristType));
		data.writeInt(this.gristValue);
	}

	@Override
	public void readSpawnData(ByteBuf data) 
	{
		int typeOffset = data.readInt();
		if(typeOffset < 0)
		{
			this.setDead();
			return;
		}
		this.gristType = GristType.values()[typeOffset];
		this.gristValue = data.readInt();
		this.setSize(this.getSizeByValue(), 0.5F);
//		this.yOffset = this.height / 2.0F;
	}
}