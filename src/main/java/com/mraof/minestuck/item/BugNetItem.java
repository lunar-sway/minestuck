package com.mraof.minestuck.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BugNetItem extends Item
{
	public BugNetItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public boolean mineBlock(ItemStack stack, World worldIn, BlockState state, BlockPos pos,
			LivingEntity entityLiving) {
		
		if(entityLiving instanceof PlayerEntity)
		{
			PlayerEntity playerIn = (PlayerEntity) entityLiving;
			if(!playerIn.isCreative() && worldIn.getBlockState(pos).getBlock() == Blocks.TALL_GRASS)
			{
				Random rand = playerIn.getRandom();
				
				if(!worldIn.isClientSide)
				{
					if(rand.nextInt(555) == 0)
					{
						ItemEntity item = new ItemEntity(worldIn, pos.getX(), pos.getY() + 0.5, pos.getZ(), new ItemStack(MSItems.GOLDEN_GRASSHOPPER, 1));
						worldIn.addFreshEntity(item);
						playerIn.getMainHandItem().hurtAndBreak(1, playerIn, PlayerIn -> playerIn.broadcastBreakEvent(Hand.MAIN_HAND));
						
						return true;
					}
					else if(rand.nextInt(5) == 0)
					{
						ItemEntity item = new ItemEntity(worldIn, pos.getX(), pos.getY() + 0.5, pos.getZ(), new ItemStack(MSItems.GRASSHOPPER, 1));
						worldIn.addFreshEntity(item);
						playerIn.getMainHandItem().hurtAndBreak(1, playerIn, PlayerIn -> playerIn.broadcastBreakEvent(Hand.MAIN_HAND));
						
						return true;
					}
				}
			}
		}
		return false;
	}
}