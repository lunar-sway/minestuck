package com.mraof.minestuck.block;

import com.mraof.minestuck.item.IncompleteSburbCodeItem;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
		ItemStack handStack = player.getItemInHand(handIn);
		ItemStack bookStack = new ItemStack(Items.WRITABLE_BOOK);
		
		if(ItemStack.isSameItem(handStack, bookStack))
		{
			if(!level.isClientSide)
			{
				int amountInHandStack = handStack.getCount();
				level.playSound(null, pos, SoundEvents.VILLAGER_WORK_CARTOGRAPHER, SoundSource.BLOCKS, 1.0F, 1.0F);
				
				ItemStack newStack = MSItems.SBURB_CODE.get().getDefaultInstance();
				IncompleteSburbCodeItem.addRecordedInfo(newStack, state.getBlock());
				IncompleteSburbCodeItem.setParadoxInfo(newStack, false); //since code is being recorded from scratch, it does not contain the paradoxically transferred component
				newStack.setCount(amountInHandStack);
				player.setItemInHand(handIn, newStack);
			}
			return InteractionResult.SUCCESS;
		} else
		{
			return InteractionResult.FAIL;
		}
	}
}