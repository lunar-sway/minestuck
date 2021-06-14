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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.List;
import java.util.function.Predicate;

public class CustomBoatItem extends Item
{
	private static final Predicate<Entity> CAN_COLLIDE_PREDICATE = EntityPredicates.NO_SPECTATORS.and(Entity::isPickable);
	protected final BoatProvider provider;
	
	public CustomBoatItem(BoatProvider provider, Properties properties)
	{
		super(properties);
		this.provider = provider;
		DispenserBlock.registerBehavior(this, new BehaviorDispenseCustomBoat());
	}
	
	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		float partialTicks = 1.0F;
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		RayTraceResult rayTrace = getPlayerPOVHitResult(worldIn, playerIn, RayTraceContext.FluidMode.ANY);
		
		if(rayTrace.getType() == RayTraceResult.Type.MISS)
			return ActionResult.pass(itemstack);
		else
		{
			Vector3d lookDirection = playerIn.getViewVector(partialTicks);
			List<Entity> list = worldIn.getEntities(playerIn, playerIn.getBoundingBox().expandTowards(lookDirection.scale(5.0D)).inflate(1.0D), CAN_COLLIDE_PREDICATE);
			
			if(!list.isEmpty())
			{
				Vector3d eyePos = playerIn.getEyePosition(partialTicks);
				for(Entity entity : list)
				{
					AxisAlignedBB axisalignedbb = entity.getBoundingBox().inflate(entity.getPickRadius());
					
					if(axisalignedbb.contains(eyePos))
						return ActionResult.pass(itemstack);
				}
			}
			
			if(rayTrace.getType() == RayTraceResult.Type.BLOCK)
			{
				Entity boat = provider.createBoat(itemstack, worldIn, rayTrace.getLocation().x, rayTrace.getLocation().y, rayTrace.getLocation().z);
				boat.yRot = playerIn.yRot;
				
				if(!worldIn.noCollision(boat, boat.getBoundingBox().inflate(-0.1D)))
					return ActionResult.fail(itemstack);
				
				if(!worldIn.isClientSide)
					worldIn.addFreshEntity(boat);
				
				if(!playerIn.abilities.instabuild)
					itemstack.shrink(1);
				
				playerIn.awardStat(Stats.ITEM_USED.get(this));
				return ActionResult.success(itemstack);
			}
			
			return ActionResult.pass(itemstack);
		}
	}
	
	protected class BehaviorDispenseCustomBoat extends DefaultDispenseItemBehavior
	{
		@Override
		public ItemStack execute(IBlockSource source, ItemStack stack)
		{
			Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
			World world = source.getLevel();
			double d0 = source.x() + (double)((float)direction.getStepX() * 1.125F);
			double d1 = source.y() + (double)((float)direction.getStepY() * 1.125F);
			double d2 = source.z() + (double)((float)direction.getStepZ() * 1.125F);
			BlockPos pos = source.getPos().relative(direction);
			double d3;
			
			if(world.getFluidState(pos).is(FluidTags.WATER))
				d3 = 1.0D;
			else
			{
				if (!world.getBlockState(pos).isAir(world, pos) || !world.getFluidState(pos.below()).is(FluidTags.WATER))
					return this.dispense(source, stack);
				
				d3 = 0.0D;
			}
			Entity boat = provider.createBoat(stack, world, d0, d1 + d3, d2);
			boat.yRot = direction.toYRot();
			world.addFreshEntity(boat);
			stack.shrink(1);
			return stack;
		}
		@Override
		protected void playSound(IBlockSource source)
		{
			source.getLevel().levelEvent(Constants.WorldEvents.DISPENSER_DISPENSE_SOUND, source.getPos(), 0);
		}
	}
	
	public interface BoatProvider
	{
		Entity createBoat(ItemStack stack, World world, double x, double y, double z);
	}
}