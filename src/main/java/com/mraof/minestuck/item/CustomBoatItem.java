package com.mraof.minestuck.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class CustomBoatItem extends Item
{
	private static final Predicate<Entity> CAN_COLLIDE_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);
	protected final BoatProvider provider;
	
	public CustomBoatItem(BoatProvider provider, Properties properties)
	{
		super(properties);
		this.provider = provider;
		DispenserBlock.registerBehavior(this, new BehaviorDispenseCustomBoat());
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn)
	{
		float partialTicks = 1.0F;
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		HitResult rayTrace = getPlayerPOVHitResult(level, playerIn, ClipContext.Fluid.ANY);
		
		if(rayTrace.getType() == HitResult.Type.MISS)
			return InteractionResultHolder.pass(itemstack);
		else
		{
			Vec3 lookDirection = playerIn.getViewVector(partialTicks);
			List<Entity> list = level.getEntities(playerIn, playerIn.getBoundingBox().expandTowards(lookDirection.scale(5.0D)).inflate(1.0D), CAN_COLLIDE_PREDICATE);
			
			if(!list.isEmpty())
			{
				Vec3 eyePos = playerIn.getEyePosition(partialTicks);
				for(Entity entity : list)
				{
					AABB axisalignedbb = entity.getBoundingBox().inflate(entity.getPickRadius());
					
					if(axisalignedbb.contains(eyePos))
						return InteractionResultHolder.pass(itemstack);
				}
			}
			
			if(rayTrace.getType() == HitResult.Type.BLOCK)
			{
				Entity boat = provider.createBoat(itemstack, level, rayTrace.getLocation().x, rayTrace.getLocation().y, rayTrace.getLocation().z);
				boat.setYRot(playerIn.getYRot());
				
				if(!level.noCollision(boat, boat.getBoundingBox().inflate(-0.1D)))
					return InteractionResultHolder.fail(itemstack);
				
				if(!level.isClientSide)
					level.addFreshEntity(boat);
				
				if(!playerIn.getAbilities().instabuild)
					itemstack.shrink(1);
				
				playerIn.awardStat(Stats.ITEM_USED.get(this));
				return InteractionResultHolder.success(itemstack);
			}
			
			return InteractionResultHolder.pass(itemstack);
		}
	}
	
	protected class BehaviorDispenseCustomBoat extends DefaultDispenseItemBehavior
	{
		@Override
		public ItemStack execute(BlockSource source, ItemStack stack)
		{
			Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
			Level level = source.getLevel();
			double d0 = source.x() + (double)((float)direction.getStepX() * 1.125F);
			double d1 = source.y() + (double)((float)direction.getStepY() * 1.125F);
			double d2 = source.z() + (double)((float)direction.getStepZ() * 1.125F);
			BlockPos pos = source.getPos().relative(direction);
			double d3;
			
			if(level.getFluidState(pos).is(FluidTags.WATER))
				d3 = 1.0D;
			else
			{
				if (!level.getBlockState(pos).isAir() || !level.getFluidState(pos.below()).is(FluidTags.WATER))
					return this.dispense(source, stack);
				
				d3 = 0.0D;
			}
			Entity boat = provider.createBoat(stack, level, d0, d1 + d3, d2);
			boat.setYRot(direction.toYRot());
			level.addFreshEntity(boat);
			stack.shrink(1);
			return stack;
		}
		@Override
		protected void playSound(BlockSource source)
		{
			source.getLevel().levelEvent(LevelEvent.SOUND_DISPENSER_DISPENSE, source.getPos(), 0);
		}
	}
	
	public interface BoatProvider
	{
		Entity createBoat(ItemStack stack, Level level, double x, double y, double z);
	}
}