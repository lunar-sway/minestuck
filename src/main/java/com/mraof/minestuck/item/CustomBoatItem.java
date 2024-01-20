package com.mraof.minestuck.item;

import com.mraof.minestuck.entity.item.MetalBoatEntity;
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
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class CustomBoatItem extends Item
{
	private static final Predicate<Entity> CAN_COLLIDE_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);
	protected final MetalBoatEntity.Type boatType;
	
	public CustomBoatItem(MetalBoatEntity.Type boatType, Properties properties)
	{
		super(properties);
		this.boatType = boatType;
		DispenserBlock.registerBehavior(this, new BehaviorDispenseCustomBoat());
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
	{
		float partialTicks = 1.0F;
		ItemStack itemstack = player.getItemInHand(hand);
		HitResult rayTrace = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
		
		if(rayTrace.getType() != HitResult.Type.BLOCK)
			return InteractionResultHolder.pass(itemstack);
		
		Vec3 lookDirection = player.getViewVector(partialTicks);
		List<Entity> list = level.getEntities(player, player.getBoundingBox().expandTowards(lookDirection.scale(5.0D)).inflate(1.0D), CAN_COLLIDE_PREDICATE);
		
		if(!list.isEmpty())
		{
			Vec3 eyePos = player.getEyePosition(partialTicks);
			if(list.stream().anyMatch(entity -> entity.getBoundingBox().inflate(entity.getPickRadius()).contains(eyePos)))
				return InteractionResultHolder.pass(itemstack);
		}
		
		Entity boat = new MetalBoatEntity(level, rayTrace.getLocation().x, rayTrace.getLocation().y, rayTrace.getLocation().z, boatType);
		boat.setYRot(player.getYRot());
		
		if(!level.noCollision(boat, boat.getBoundingBox().inflate(-0.1D)))
			return InteractionResultHolder.fail(itemstack);
		
		if(!level.isClientSide)
			level.addFreshEntity(boat);
		
		if(!player.getAbilities().instabuild)
			itemstack.shrink(1);
		
		player.awardStat(Stats.ITEM_USED.get(this));
		return InteractionResultHolder.success(itemstack);
	}
	
	protected class BehaviorDispenseCustomBoat extends DefaultDispenseItemBehavior
	{
		@Override
		public ItemStack execute(BlockSource source, ItemStack stack)
		{
			Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
			Level level = source.getLevel();
			double x = source.x() + (double)((float)direction.getStepX() * 1.125F);
			double y = source.y() + (double)((float)direction.getStepY() * 1.125F);
			double z = source.z() + (double)((float)direction.getStepZ() * 1.125F);
			BlockPos pos = source.getPos().relative(direction);
			double waterOffset = level.getFluidState(pos).is(FluidTags.WATER)? 1 : 0;
			
			if(!level.getBlockState(pos).isAir() || !level.getFluidState(pos.below()).is(FluidTags.WATER))
				return new DefaultDispenseItemBehavior().dispense(source, stack);
			
			Entity boat = new MetalBoatEntity(level, x, y + waterOffset, z, boatType);
			boat.setYRot(direction.toYRot());
			level.addFreshEntity(boat);
			stack.shrink(1);
			return stack;
		}
	}
}