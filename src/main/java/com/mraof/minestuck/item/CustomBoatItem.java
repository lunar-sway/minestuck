package com.mraof.minestuck.item;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
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

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
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
		
		Entity boat = provider.createBoat(itemstack, level, rayTrace.getLocation().x, rayTrace.getLocation().y, rayTrace.getLocation().z);
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
			Direction direction = source.state().getValue(DispenserBlock.FACING);
			Level level = source.level();
			double x = source.center().x() + (double)((float)direction.getStepX() * 1.125F);
			double y = source.center().y() + (double)((float)direction.getStepY() * 1.125F);
			double z = source.center().z() + (double)((float)direction.getStepZ() * 1.125F);
			BlockPos pos = source.pos().relative(direction);
			double waterOffset = level.getFluidState(pos).is(FluidTags.WATER)? 1 : 0;
			
			if(!level.getBlockState(pos).isAir() || !level.getFluidState(pos.below()).is(FluidTags.WATER))
				return new DefaultDispenseItemBehavior().dispense(source, stack);
			
			Entity boat = provider.createBoat(stack, level, x, y + waterOffset, z);
			boat.setYRot(direction.toYRot());
			level.addFreshEntity(boat);
			stack.shrink(1);
			return stack;
		}
	}
	
	public interface BoatProvider
	{
		Entity createBoat(ItemStack stack, Level level, double x, double y, double z);
	}
}