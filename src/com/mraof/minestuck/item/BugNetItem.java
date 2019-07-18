package com.mraof.minestuck.item;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BugNetItem extends Item
{
	public BugNetItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos,
			LivingEntity entityLiving) {
		
		if(entityLiving instanceof PlayerEntity)
		{
			PlayerEntity playerIn = (PlayerEntity) entityLiving;
			if(!playerIn.isCreative() && worldIn.getBlockState(pos).getBlock() == Blocks.TALL_GRASS)
			{
				Random rand = playerIn.getRNG();
				
				if(!worldIn.isRemote)
				{
					if(rand.nextInt(555) == 0)
					{
						ItemEntity item = new ItemEntity(worldIn, pos.getX(), pos.getY() + 0.5, pos.getZ(), new ItemStack(MinestuckItems.GOLDEN_GRASSHOPPER, 1));
						worldIn.addEntity(item);
						playerIn.getHeldItemMainhand().damageItem(1, playerIn, PlayerIn -> playerIn.sendBreakAnimation(Hand.MAIN_HAND));
						
						return true;
					}
					else if(rand.nextInt(5) == 0)
					{
						ItemEntity item = new ItemEntity(worldIn, pos.getX(), pos.getY() + 0.5, pos.getZ(), new ItemStack(MinestuckItems.GRASSHOPPER, 1));
						worldIn.addEntity(item);
						playerIn.getHeldItemMainhand().damageItem(1, playerIn, PlayerIn -> playerIn.sendBreakAnimation(Hand.MAIN_HAND));
						
						return true;
					}
				}
			}
		}
		return false;
	}
}