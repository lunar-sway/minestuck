package com.mraof.minestuck.item;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.List;
import java.util.function.Predicate;

public class CustomBoatItem extends Item
{
	private static final Predicate<Entity> CAN_COLLIDE_PREDICATE = EntityPredicates.NOT_SPECTATING.and(Entity::canBeCollidedWith);
	protected final BoatProvider provider;
	
	public CustomBoatItem(BoatProvider provider, Properties properties)
	{
		super(properties);
		this.provider = provider;
		DispenserBlock.registerDispenseBehavior(this, new BehaviorDispenseCustomBoat());
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		float partialTicks = 1.0F;
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		RayTraceResult rayTrace = rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.ANY);
		
		if(rayTrace.getType() == RayTraceResult.Type.MISS)
			return ActionResult.resultPass(itemstack);
		else
		{
			Vec3d lookDirection = playerIn.getLook(partialTicks);
			List<Entity> list = worldIn.getEntitiesInAABBexcluding(playerIn, playerIn.getBoundingBox().expand(lookDirection.scale(5.0D)).grow(1.0D), CAN_COLLIDE_PREDICATE);
			
			if(!list.isEmpty())
			{
				Vec3d eyePos = playerIn.getEyePosition(partialTicks);
				for(Entity entity : list)
				{
					AxisAlignedBB axisalignedbb = entity.getBoundingBox().grow(entity.getCollisionBorderSize());
					
					if(axisalignedbb.contains(eyePos))
						return ActionResult.resultPass(itemstack);
				}
			}
			
			if(rayTrace.getType() == RayTraceResult.Type.BLOCK)
			{
				Entity boat = provider.createBoat(itemstack, worldIn, rayTrace.getHitVec().x, rayTrace.getHitVec().y, rayTrace.getHitVec().z);
				boat.rotationYaw = playerIn.rotationYaw;
				
				if(!worldIn.hasNoCollisions(boat, boat.getBoundingBox().grow(-0.1D)))
					return ActionResult.resultFail(itemstack);
				
				if(!worldIn.isRemote)
					worldIn.addEntity(boat);
				
				if(!playerIn.abilities.isCreativeMode)
					itemstack.shrink(1);
				
				playerIn.addStat(Stats.ITEM_USED.get(this));
				return ActionResult.resultSuccess(itemstack);
			}
			
			return ActionResult.resultPass(itemstack);
		}
	}
	
	protected class BehaviorDispenseCustomBoat extends DefaultDispenseItemBehavior
	{
		@Override
		public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
		{
			Direction direction = source.getBlockState().get(DispenserBlock.FACING);
			World world = source.getWorld();
			double d0 = source.getX() + (double)((float)direction.getXOffset() * 1.125F);
			double d1 = source.getY() + (double)((float)direction.getYOffset() * 1.125F);
			double d2 = source.getZ() + (double)((float)direction.getZOffset() * 1.125F);
			BlockPos pos = source.getBlockPos().offset(direction);
			double d3;
			
			if(world.getFluidState(pos).isTagged(FluidTags.WATER))
				d3 = 1.0D;
			else
			{
				if (!world.getBlockState(pos).isAir(world, pos) || !world.getFluidState(pos.down()).isTagged(FluidTags.WATER))
					return this.dispense(source, stack);
				
				d3 = 0.0D;
			}
			Entity boat = provider.createBoat(stack, world, d0, d1 + d3, d2);
			boat.rotationYaw = direction.getHorizontalAngle();
			world.addEntity(boat);
			stack.shrink(1);
			return stack;
		}
		@Override
		protected void playDispenseSound(IBlockSource source)
		{
			source.getWorld().playEvent(Constants.WorldEvents.DISPENSER_DISPENSE_SOUND, source.getBlockPos(), 0);
		}
	}
	
	public interface BoatProvider
	{
		Entity createBoat(ItemStack stack, World world, double x, double y, double z);
	}
}