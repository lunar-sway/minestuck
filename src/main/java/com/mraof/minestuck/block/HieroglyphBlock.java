package com.mraof.minestuck.block;

import com.mraof.minestuck.item.MSItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class HieroglyphBlock extends Block
{
	public HieroglyphBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		ItemStack itemstack = player.getItemInHand(handIn);
		ItemStack bookStack = new ItemStack(Items.WRITABLE_BOOK);
		
		if(ItemStack.isSame(itemstack, bookStack))
		{
			if(!worldIn.isClientSide)
			{
				int amountInHandStack = itemstack.getCount();
				Direction direction = hit.getDirection();
				Direction direction1 = direction.getAxis() == Direction.Axis.Y ? player.getDirection().getOpposite() : direction;
				worldIn.playSound(null, pos, SoundEvents.VILLAGER_WORK_CARTOGRAPHER, SoundCategory.BLOCKS, 1.0F, 1.0F);
				ItemEntity itementity = new ItemEntity(worldIn, (double) pos.getX() + 0.5D + (double) direction1.getStepX() * 0.65D, (double) pos.getY() + 0.1D, (double) pos.getZ() + 0.5D + (double) direction1.getStepZ() * 0.65D, new ItemStack(MSItems.SBURB_CODE, amountInHandStack));
				itementity.setDeltaMovement(0.05D * (double) direction1.getStepX() + worldIn.random.nextDouble() * 0.02D, 0.05D, 0.05D * (double) direction1.getStepZ() + worldIn.random.nextDouble() * 0.02D);
				worldIn.addFreshEntity(itementity);
				itemstack.shrink(amountInHandStack);
			}
			return ActionResultType.SUCCESS;
		} else
		{
			return ActionResultType.FAIL;
		}
	}
}