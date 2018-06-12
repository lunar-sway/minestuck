package com.mraof.minestuck.item;

import net.minecraft.block.BlockDispenser;
import net.minecraft.block.material.Material;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import java.util.List;

public abstract class ItemCustomBoat extends Item
{
	
	public ItemCustomBoat()
	{
		this.maxStackSize = 1;
		setCreativeTab(MinestuckItems.tabMinestuck);
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, new BehaivorDispenseCustomBoat());
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		float f = 1.0F;
		float f1 = playerIn.prevRotationPitch + (playerIn.rotationPitch - playerIn.prevRotationPitch) * f;
		float f2 = playerIn.prevRotationYaw + (playerIn.rotationYaw - playerIn.prevRotationYaw) * f;
		double d0 = playerIn.prevPosX + (playerIn.posX - playerIn.prevPosX) * (double)f;
		double d1 = playerIn.prevPosY + (playerIn.posY - playerIn.prevPosY) * (double)f + (double)playerIn.getEyeHeight();
		double d2 = playerIn.prevPosZ + (playerIn.posZ - playerIn.prevPosZ) * (double)f;
		Vec3d vec3 = new Vec3d(d0, d1, d2);
		float f3 = MathHelper.cos(-f2 * 0.017453292F - (float)Math.PI);
		float f4 = MathHelper.sin(-f2 * 0.017453292F - (float)Math.PI);
		float f5 = -MathHelper.cos(-f1 * 0.017453292F);
		float f6 = MathHelper.sin(-f1 * 0.017453292F);
		float f7 = f4 * f5;
		float f8 = f3 * f5;
		double d3 = 5.0D;
		Vec3d vec31 = vec3.addVector((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
		RayTraceResult rayTrace = worldIn.rayTraceBlocks(vec3, vec31, true);
		
		if (rayTrace == null)
		{
			return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
		}
		else
		{
			Vec3d vec32 = playerIn.getLook(f);
			boolean flag = false;
			float f9 = 1.0F;
			List list = worldIn.getEntitiesWithinAABBExcludingEntity(playerIn, playerIn.getEntityBoundingBox().expand(vec32.x * d3, vec32.y * d3, vec32.z * d3).grow((double)f9));
			
			for (int i = 0; i < list.size(); ++i)
			{
				Entity entity = (Entity)list.get(i);
				
				if (entity.canBeCollidedWith())
				{
					float f10 = entity.getCollisionBorderSize();
					AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expand((double)f10, (double)f10, (double)f10);
					
					if (axisalignedbb.contains(vec3))
					{
						flag = true;
					}
				}
			}
			
			if (flag)
			{
				return new ActionResult(EnumActionResult.PASS, itemstack);
			}
			else
			{
				if (rayTrace.typeOfHit == RayTraceResult.Type.BLOCK)
				{
					BlockPos blockpos = rayTrace.getBlockPos();
					
					if (worldIn.getBlockState(blockpos).getBlock() == Blocks.SNOW_LAYER)
					{
						blockpos = blockpos.down();
					}
					
					Entity entityboat = createBoat(itemstack, worldIn, (double)((float)blockpos.getX() + 0.5F), (double)((float)blockpos.getY() + 1.0F), (double)((float)blockpos.getZ() + 0.5F));
					entityboat.rotationYaw = (float)(((MathHelper.floor((double)(playerIn.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) - 1) * 90);
					
					if (!worldIn.getCollisionBoxes(entityboat, entityboat.getEntityBoundingBox().grow(-0.1D)).isEmpty())
					{
						return new ActionResult(EnumActionResult.FAIL, itemstack);
					}
					
					if (!worldIn.isRemote)
					{
						worldIn.spawnEntity(entityboat);
					}
					
					if (!playerIn.capabilities.isCreativeMode)
					{
						itemstack.shrink(1);
					}
					return new ActionResult(EnumActionResult.SUCCESS, itemstack);
				}
				
				return new ActionResult(EnumActionResult.PASS, itemstack);
			}
		}
	}
	
	protected abstract Entity createBoat(ItemStack stack, World world, double x, double y, double z);
	
	protected class BehaivorDispenseCustomBoat extends BehaviorDefaultDispenseItem
	{
		@Override
		public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
		{
			EnumFacing enumfacing = (EnumFacing)source.getBlockState().getValue(BlockDispenser.FACING);
			World world = source.getWorld();
			double d0 = source.getX() + (double)((float)enumfacing.getFrontOffsetX() * 1.125F);
			double d1 = source.getY() + (double)((float)enumfacing.getFrontOffsetY() * 1.125F);
			double d2 = source.getZ() + (double)((float)enumfacing.getFrontOffsetZ() * 1.125F);
			BlockPos blockpos = source.getBlockPos().offset(enumfacing);
			Material material = world.getBlockState(blockpos).getMaterial();
			double d3;
			
			if (Material.WATER.equals(material))
			{
				d3 = 1.0D;
			}
			else
			{
				if (!Material.AIR.equals(material) || !Material.WATER.equals(world.getBlockState(blockpos.down()).getMaterial()))
				{
					return this.dispense(source, stack);
				}
				
				d3 = 0.0D;
			}
			
			Entity entityboat = createBoat(stack, world, d0, d1 + d3, d2);
			world.spawnEntity(entityboat);
			stack.splitStack(1);
			return stack;
		}
		@Override
		protected void playDispenseSound(IBlockSource source)
		{
			source.getWorld().playEvent(1000, source.getBlockPos(), 0);
		}
	}
}