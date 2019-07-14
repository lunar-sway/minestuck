package com.mraof.minestuck.item;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.material.Material;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import java.util.List;

public class CustomBoatItem extends Item
{
	public static final IDispenseItemBehavior DISPENSER_BEHAIVOR = new BehaivorDispenseCustomBoat();
	protected final BoatProvider provider;
	
	public CustomBoatItem(BoatProvider provider, Properties properties)
	{
		super(properties);
		this.provider = provider;
		DispenserBlock.registerDispenseBehavior(this, DISPENSER_BEHAIVOR);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
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
		Vec3d vec31 = vec3.add((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
		RayTraceResult rayTrace = worldIn.rayTraceBlocks(vec3, vec31, RayTraceFluidMode.ALWAYS);

		if (rayTrace == null)
			return new ActionResult<>(ActionResultType.PASS, itemstack);
		else
		{
			Vec3d vec32 = playerIn.getLook(f);
			boolean flag = false;
			List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(playerIn, playerIn.getBoundingBox().expand(vec32.x * d3, vec32.y * d3, vec32.z * d3).grow(1.0D));
			
			for(Entity entity : list)
			{
				if (entity.canBeCollidedWith())
				{
					float f10 = entity.getCollisionBorderSize();
					AxisAlignedBB axisalignedbb = entity.getBoundingBox().expand((double)f10, (double)f10, (double)f10);
					
					if (axisalignedbb.contains(vec3))
					{
						flag = true;
					}
				}
			}
			
			if (flag)
			{
				return new ActionResult<>(ActionResultType.PASS, itemstack);
			}
			else
			{
				if (rayTrace.type == RayTraceResult.Type.BLOCK)
				{
					BlockPos blockpos = rayTrace.getBlockPos();
					
					Entity entityboat = provider.createBoat(itemstack, worldIn, (double)((float)blockpos.getX() + 0.5F), (double)((float)blockpos.getY() + 1.0F), (double)((float)blockpos.getZ() + 0.5F));
					entityboat.rotationYaw = playerIn.rotationYaw;
					
					if (!worldIn.isCollisionBoxesEmpty(entityboat, entityboat.getBoundingBox().grow(-0.1D)))
					{
						return new ActionResult<>(ActionResultType.FAIL, itemstack);
					}
					
					if (!worldIn.isRemote)
					{
						worldIn.spawnEntity(entityboat);
					}
					
					if (!playerIn.abilities.isCreativeMode)
					{
						itemstack.shrink(1);
					}
					return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
				}
				
				return new ActionResult<>(ActionResultType.PASS, itemstack);
			}
		}
	}
	
	protected static class BehaivorDispenseCustomBoat extends DefaultDispenseItemBehavior
	{
		@Override
		public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
		{
			if(!(stack.getItem() instanceof CustomBoatItem))
				throw new IllegalStateException("Can't use custom boat dispenser behaivor on non-custom boat item!");
			
			CustomBoatItem boatItem = (CustomBoatItem) stack.getItem();
			Direction enumfacing = source.getBlockState().get(DispenserBlock.FACING);
			World world = source.getWorld();
			double d0 = source.getX() + (double)((float)enumfacing.getXOffset() * 1.125F);
			double d1 = source.getY() + (double)((float)enumfacing.getYOffset() * 1.125F);
			double d2 = source.getZ() + (double)((float)enumfacing.getZOffset() * 1.125F);
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
			Entity entityBoat = boatItem.provider.createBoat(stack, world, d0, d1 + d3, d2);
			world.spawnEntity(entityBoat);
			stack.shrink(1);
			return stack;
		}
		@Override
		protected void playDispenseSound(IBlockSource source)
		{
			source.getWorld().playEvent(1000, source.getBlockPos(), 0);
		}
	}
	
	public interface BoatProvider
	{
		Entity createBoat(ItemStack stack, World world, double x, double y, double z);
	}
}