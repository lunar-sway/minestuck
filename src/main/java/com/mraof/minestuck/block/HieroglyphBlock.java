package com.mraof.minestuck.block;

import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.components.HieroglyphCode;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
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
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
	{
		
		if(!stack.is(Items.WRITABLE_BOOK))
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		
		if(!level.isClientSide)
		{
			int amountInHandStack = stack.getCount();
			level.playSound(null, pos, SoundEvents.VILLAGER_WORK_CARTOGRAPHER, SoundSource.BLOCKS, 1.0F, 1.0F);
			
			ItemStack newStack = new ItemStack(MSItems.SBURB_CODE.get());
			HieroglyphCode.addBlock(newStack, state.getBlock());
			newStack.setCount(amountInHandStack);
			player.setItemInHand(hand, newStack);
		}
		return ItemInteractionResult.SUCCESS;
	}
}
