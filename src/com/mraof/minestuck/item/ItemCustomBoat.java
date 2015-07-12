package com.mraof.minestuck.item;

import java.util.List;

import net.minecraft.block.BlockDispenser;
import net.minecraft.block.material.Material;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.mraof.minestuck.Minestuck;

public abstract class ItemCustomBoat extends Item
{
	
	public ItemCustomBoat()
	{
		this.maxStackSize = 1;
		setCreativeTab(Minestuck.tabMinestuck);
		BlockDispenser.dispenseBehaviorRegistry.putObject(this, new BehaivorDispenseCustomBoat());
	}
	
	//This method is copied straight from the normal boat item.
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
	{
		float f = 1.0F;
		float f1 = playerIn.prevRotationPitch + (playerIn.rotationPitch - playerIn.prevRotationPitch) * f;
		float f2 = playerIn.prevRotationYaw + (playerIn.rotationYaw - playerIn.prevRotationYaw) * f;
		double d0 = playerIn.prevPosX + (playerIn.posX - playerIn.prevPosX) * (double)f;
		double d1 = playerIn.prevPosY + (playerIn.posY - playerIn.prevPosY) * (double)f + (double)playerIn.getEyeHeight();
		double d2 = playerIn.prevPosZ + (playerIn.posZ - playerIn.prevPosZ) * (double)f;
		Vec3 vec3 = new Vec3(d0, d1, d2);
		float f3 = MathHelper.cos(-f2 * 0.017453292F - (float)Math.PI);
		float f4 = MathHelper.sin(-f2 * 0.017453292F - (float)Math.PI);
		float f5 = -MathHelper.cos(-f1 * 0.017453292F);
		float f6 = MathHelper.sin(-f1 * 0.017453292F);
		float f7 = f4 * f5;
		float f8 = f3 * f5;
		double d3 = 5.0D;
		Vec3 vec31 = vec3.addVector((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
		MovingObjectPosition movingobjectposition = worldIn.rayTraceBlocks(vec3, vec31, true);
		
		if (movingobjectposition == null)
		{
			return itemStackIn;
		}
		else
		{
			Vec3 vec32 = playerIn.getLook(f);
			boolean flag = false;
			float f9 = 1.0F;
			List list = worldIn.getEntitiesWithinAABBExcludingEntity(playerIn, playerIn.getEntityBoundingBox().addCoord(vec32.xCoord * d3, vec32.yCoord * d3, vec32.zCoord * d3).expand((double)f9, (double)f9, (double)f9));
			
			for (int i = 0; i < list.size(); ++i)
			{
				Entity entity = (Entity)list.get(i);
				
				if (entity.canBeCollidedWith())
				{
					float f10 = entity.getCollisionBorderSize();
					AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expand((double)f10, (double)f10, (double)f10);
					
					if (axisalignedbb.isVecInside(vec3))
					{
						flag = true;
					}
				}
			}
			
			if (flag)
			{
				return itemStackIn;
			}
			else
			{
				if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
				{
					BlockPos blockpos = movingobjectposition.getBlockPos();
					
					if (worldIn.getBlockState(blockpos).getBlock() == Blocks.snow_layer)
					{
						blockpos = blockpos.down();
					}
					
					Entity entityboat = createBoat(itemStackIn, worldIn, (double)((float)blockpos.getX() + 0.5F), (double)((float)blockpos.getY() + 1.0F), (double)((float)blockpos.getZ() + 0.5F));
					entityboat.rotationYaw = (float)(((MathHelper.floor_double((double)(playerIn.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) - 1) * 90);
					
					if (!worldIn.getCollidingBoundingBoxes(entityboat, entityboat.getEntityBoundingBox().expand(-0.1D, -0.1D, -0.1D)).isEmpty())
					{
						return itemStackIn;
					}
					
					if (!worldIn.isRemote)
					{
						worldIn.spawnEntityInWorld(entityboat);
					}
					
					if (!playerIn.capabilities.isCreativeMode)
					{
						--itemStackIn.stackSize;
					}
				}
				
				return itemStackIn;
			}
		}
	}
	
	protected abstract Entity createBoat(ItemStack stack, World world, double x, double y, double z);
	
	protected class BehaivorDispenseCustomBoat extends BehaviorDefaultDispenseItem
	{
		public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
		{
			EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockMetadata());
			World world = source.getWorld();
			double d0 = source.getX() + (double)((float)enumfacing.getFrontOffsetX() * 1.125F);
			double d1 = source.getY() + (double)((float)enumfacing.getFrontOffsetY() * 1.125F);
			double d2 = source.getZ() + (double)((float)enumfacing.getFrontOffsetZ() * 1.125F);
			BlockPos blockpos = source.getBlockPos().offset(enumfacing);
			Material material = world.getBlockState(blockpos).getBlock().getMaterial();
			double d3;
			
			if (Material.water.equals(material))
			{
				d3 = 1.0D;
			}
			else
			{
				if (!Material.air.equals(material) || !Material.water.equals(world.getBlockState(blockpos.down()).getBlock().getMaterial()))
				{
					return this.dispense(source, stack);
				}
				
				d3 = 0.0D;
			}
			
			Entity entityboat = createBoat(stack, world, d0, d1 + d3, d2);
			world.spawnEntityInWorld(entityboat);
			stack.splitStack(1);
			return stack;
		}
		protected void playDispenseSound(IBlockSource source)
		{
			source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
		}
	}
}