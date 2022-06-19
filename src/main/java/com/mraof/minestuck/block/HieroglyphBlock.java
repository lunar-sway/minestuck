package com.mraof.minestuck.block;

import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.SburbCodeItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class HieroglyphBlock extends Block
{
	public HieroglyphBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit)
	{
		ItemStack itemStack = player.getItemInHand(handIn);
		ItemStack bookStack = new ItemStack(Items.WRITABLE_BOOK);
		
		if(ItemStack.isSame(itemStack, bookStack))
		{
			if(!level.isClientSide)
			{
				int amountInHandStack = itemStack.getCount();
				worldIn.playSound(null, pos, SoundEvents.VILLAGER_WORK_CARTOGRAPHER, SoundCategory.BLOCKS, 1.0F, 1.0F);
				
				ItemStack newStack = MSItems.SBURB_CODE.getDefaultInstance();
				SburbCodeItem.addRecordedInfo(newStack, state.getBlock());
				newStack.setCount(amountInHandStack);
				player.setItemInHand(handIn, newStack);
				
				/*int amountInHandStack = itemstack.getCount();
				Direction direction = hit.getDirection();
				Direction direction1 = direction.getAxis() == Direction.Axis.Y ? player.getDirection().getOpposite() : direction;
				level.playSound(null, pos, SoundEvents.VILLAGER_WORK_CARTOGRAPHER, SoundSource.BLOCKS, 1.0F, 1.0F);
				ItemEntity itementity = new ItemEntity(level, (double) pos.getX() + 0.5D + (double) direction1.getStepX() * 0.65D, (double) pos.getY() + 0.1D, (double) pos.getZ() + 0.5D + (double) direction1.getStepZ() * 0.65D, new ItemStack(MSItems.SBURB_CODE.get(), amountInHandStack));
				itementity.setDeltaMovement(0.05D * (double) direction1.getStepX() + level.random.nextDouble() * 0.02D, 0.05D, 0.05D * (double) direction1.getStepZ() + level.random.nextDouble() * 0.02D);
				level.addFreshEntity(itementity);
				itemstack.shrink(amountInHandStack);*/
			}
			return InteractionResult.SUCCESS;
		} else
		{
			return InteractionResult.FAIL;
		}
	}
}