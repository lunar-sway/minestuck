package com.mraof.minestuck.block;

import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.SburbCodeItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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
		ItemStack itemStack = player.getItemInHand(handIn);
		ItemStack bookStack = new ItemStack(Items.WRITABLE_BOOK);
		
		if(ItemStack.isSame(itemStack, bookStack))
		{
			if(!worldIn.isClientSide)
			{
				int amountInHandStack = itemStack.getCount();
				worldIn.playSound(null, pos, SoundEvents.VILLAGER_WORK_CARTOGRAPHER, SoundCategory.BLOCKS, 1.0F, 1.0F);
				
				ItemStack newStack = MSItems.SBURB_CODE.getDefaultInstance();
				SburbCodeItem.addRecordedInfo(newStack, state.getBlock());
				SburbCodeItem.setParadoxInfo(newStack, false); //since code is being recorded from scratch, it does not contain the paradoxically transferred component
				newStack.setCount(amountInHandStack);
				player.setItemInHand(handIn, newStack);
			}
			return ActionResultType.SUCCESS;
		} else
		{
			return ActionResultType.FAIL;
		}
	}
}