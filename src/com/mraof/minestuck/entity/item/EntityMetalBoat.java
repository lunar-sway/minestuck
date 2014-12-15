package com.mraof.minestuck.entity.item;

import java.util.Iterator;
import java.util.List;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.Debug;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.world.World;

public class EntityMetalBoat extends EntityBoat
{
	
	public EntityMetalBoat(World world)
	{
		super(world);
	}
	
	public EntityMetalBoat(World world, double x, double y, double z, int type)
	{
		super(world, x, y, z);
	}
	
	@Override
	public float getDamageTaken()
	{
		return super.getDamageTaken();
	}
	
	@Override
	public void setDamageTaken(float damage)
	{
		super.setDamageTaken(damage/1.5F);
	}
	
	@Override
	public void onUpdate()
	{
		
		super.onUpdate();
		
		double y = posY - prevPosY;
		setPosition(posX, prevPosY, posZ);
		List list1 = this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().addCoord(0, y, 0));
		AxisAlignedBB axisalignedbb1;
		
		for (Iterator iterator = list1.iterator(); iterator.hasNext(); y = axisalignedbb1.calculateYOffset(this.getEntityBoundingBox(), y))
		{
			axisalignedbb1 = (AxisAlignedBB)iterator.next();
		}
		setPosition(posX, posY + y, posZ);
		
		double d0 = 0.0D;
		int b0 = 5;
		
		for (int i = 0; i < b0; ++i)
		{
			double d1 = this.getEntityBoundingBox().minY + (this.getEntityBoundingBox().maxY - this.getEntityBoundingBox().minY) * (double)(i + 0) / (double)b0 - 0.125D;
			double d3 = this.getEntityBoundingBox().minY + (this.getEntityBoundingBox().maxY - this.getEntityBoundingBox().minY) * (double)(i + 1) / (double)b0 - 0.125D;
			AxisAlignedBB axisalignedbb = new AxisAlignedBB(this.getEntityBoundingBox().minX, d1, this.getEntityBoundingBox().minZ, this.getEntityBoundingBox().maxX, d3, this.getEntityBoundingBox().maxZ);
			
			if (this.worldObj.isAABBInMaterial(axisalignedbb, Material.water))
			{
				d0 += 1.0D / (double)b0;
			}
		}
		
		this.motionY -= d0/8;
		
		if(onGround && motionY < 0)
			motionY = 0;
		if(motionY > 0)
			motionY = -motionY;
		
		if(posY > prevPosY)
			setPosition(posX, prevPosY, posZ);
		
	}
	
	protected void func_180433_a(double par1, boolean par2, Block par3, BlockPos par4)
	{
		if (par2)
		{
			if (this.fallDistance > 3.0F)
			{
				this.fall(this.fallDistance, 1.0F);
				
				if (!this.worldObj.isRemote && !this.isDead)
				{
					this.setDead();
					int i;
					
					for (i = 0; i < 3; ++i)
					{
						this.dropItemWithOffset(Items.iron_ingot, 1, 0.0F);
					}
				}
				
				this.fallDistance = 0.0F;
			}
		}
		else if (this.worldObj.getBlockState((new BlockPos(this)).down()).getBlock().getMaterial() != Material.water && par1 < 0.0D)
		{
			this.fallDistance = (float)((double)this.fallDistance - par1);
		}
	}
	
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (this.isEntityInvulnerable(source))
		{
			return false;
		}
		else if (!this.worldObj.isRemote && !this.isDead)
		{
			if (this.riddenByEntity != null && this.riddenByEntity == source.getEntity() && source instanceof EntityDamageSourceIndirect)
			{
				return false;
			}
			else
			{
				this.setForwardDirection(-this.getForwardDirection());
				this.setTimeSinceHit(10);
				this.setDamageTaken(this.getDamageTaken() + amount * 10.0F);
				this.setBeenAttacked();
				boolean flag = source.getEntity() instanceof EntityPlayer && ((EntityPlayer)source.getEntity()).capabilities.isCreativeMode;
				
				if (flag || this.getDamageTaken() > 40.0F)
				{
					if (this.riddenByEntity != null)
					{
						this.riddenByEntity.mountEntity(this);
					}
					
					if (!flag)
					{
						this.dropItemWithOffset(Minestuck.metalBoat, 1, 0.0F);
					}
					
					this.setDead();
				}
				
				return true;
			}
		}
		else
		{
			return true;
		}
	}
}