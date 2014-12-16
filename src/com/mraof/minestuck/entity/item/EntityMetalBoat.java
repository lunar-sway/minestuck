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
		double pos = posY;
		double motion = motionY;
		captureDrops = true;
		
		super.onUpdate();
		
		captureDrops = false;
		if(!capturedDrops.isEmpty())
		{
			double prevMotionX = posX - prevPosX, prevMotionZ = posZ - prevPosZ;
			if(Math.sqrt(prevMotionX * prevMotionX + prevMotionZ * prevMotionZ) > 0.3)
				for(int i = 0; i < 3; i++)
					dropItem(Items.iron_ingot, 1);
			else
			{
				isDead = false;
				this.motionX *= 0.9900000095367432D;
				this.motionY *= 0.949999988079071D;
				this.motionZ *= 0.9900000095367432D;
			}
		}
		
		capturedDrops.clear();
		
		if(!this.worldObj.isAABBInMaterial(this.getEntityBoundingBox(), Material.water))
			return;
		
		if(!onGround)
			motion -= 0.1;
		
		this.motionY = motion;
		setPosition(posX, pos, posZ);
		this.motionY -= 0.04D;
			motionY /= 2;
		
		moveEntity(0, motionY, 0);
		
	}
	
	protected void func_180433_a(double par1, boolean par2, Block par3, BlockPos par4)
	{
		if (par2)
		{
			if (this.fallDistance > 5.0F)
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