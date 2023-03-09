package com.mraof.minestuck.item;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BugNetItem extends Item
{
	public BugNetItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos,
							 LivingEntity entityLiving) {
		
		if(entityLiving instanceof Player)
		{
			Player playerIn = (Player) entityLiving;
			if(!playerIn.isCreative() && level.getBlockState(pos).getBlock() == Blocks.TALL_GRASS)
			{
				RandomSource rand = playerIn.getRandom();
				
				if(!level.isClientSide)
				{
					if(rand.nextInt(555) == 0)
					{
						ItemEntity item = new ItemEntity(level, pos.getX(), pos.getY() + 0.5, pos.getZ(), new ItemStack(MSItems.GOLDEN_GRASSHOPPER.get(), 1));
						level.addFreshEntity(item);
						playerIn.getMainHandItem().hurtAndBreak(1, playerIn, PlayerIn -> playerIn.broadcastBreakEvent(InteractionHand.MAIN_HAND));
						
						return true;
					}
					else if(rand.nextInt(5) == 0)
					{
						ItemEntity item = new ItemEntity(level, pos.getX(), pos.getY() + 0.5, pos.getZ(), new ItemStack(MSItems.GRASSHOPPER.get(), 1));
						level.addFreshEntity(item);
						playerIn.getMainHandItem().hurtAndBreak(1, playerIn, PlayerIn -> playerIn.broadcastBreakEvent(InteractionHand.MAIN_HAND));
						
						return true;
					}
				}
			}
		}
		return false;
	}
}