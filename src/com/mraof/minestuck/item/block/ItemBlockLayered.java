package com.mraof.minestuck.item.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.mraof.minestuck.block.BlockLayered;

public class ItemBlockLayered extends ItemBlock
{
	
	public IBlockState theBlock;
	public ItemBlockLayered(Block par1)
	{
		super(par1);
		theBlock = ((BlockLayered)par1).fullBlock;
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (stack.stackSize == 0)
		{
			return EnumActionResult.PASS;
		}
		else if (!playerIn.canPlayerEdit(pos, facing, stack))
		{
			return EnumActionResult.FAIL;
		}
		else
		{
			IBlockState state = worldIn.getBlockState(pos);
			
			if (state.getBlock() == this.block)
			{
				int metadata = (Integer) state.getValue(BlockLayered.SIZE);
				
				if (/*depth <= 6 && */worldIn.checkNoEntityCollision(this.block.getBoundingBox(state, worldIn, pos)) && ((BlockLayered)this.block).changeHeight(worldIn, pos, metadata + 1)) //changes full BlockLayered into full block
				{
					worldIn.playSound(null, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, this.block.getStepSound().getPlaceSound(), SoundCategory.BLOCKS, (this.block.getStepSound().getVolume() + 1.0F) / 2.0F, this.block.getStepSound().getPitch() * 0.8F);
					--stack.stackSize;
					return EnumActionResult.SUCCESS;
				}
			}
			
			return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		}
	}
	
}