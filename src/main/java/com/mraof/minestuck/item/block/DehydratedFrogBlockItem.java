package com.mraof.minestuck.item.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DehydratedFrogBlockItem extends FrogBlockItem
{
	public DehydratedFrogBlockItem(Block blockIn, Properties builder)
	{
		super(blockIn, builder);
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context)
	{
		World worldIn = context.getWorld();
		BlockPos blockpos = context.getPos();
		BlockState blockstate = worldIn.getBlockState(blockpos);
		
		if(blockstate.getBlock() == Blocks.CAULDRON && blockstate.get(CauldronBlock.LEVEL) >= 1)
		{
			ItemStack itemstack = context.getItem();
			if(!worldIn.isRemote)
			{
				Entity entity = createFrog(worldIn, (double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.8D, (double)blockpos.getZ() + 0.5D, 0);
				worldIn.addEntity(entity);
				worldIn.playSound(null, blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundEvents.ENTITY_HUSK_CONVERTED_TO_ZOMBIE, SoundCategory.AMBIENT, 1.5F, 1.0F);
				itemstack.shrink(1);
			}
			
			return ActionResultType.SUCCESS;
		} else
		{
			return ActionResultType.PASS;
		}
	}
}
