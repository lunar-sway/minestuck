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
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = player.getHeldItem(hand);
		if (stack.isEmpty())
		{
			return EnumActionResult.PASS;
		}
		else if (!player.canPlayerEdit(pos, facing, stack))
		{
			return EnumActionResult.FAIL;
		}
		else
		{
			IBlockState state = worldIn.getBlockState(pos);
			
			if (state.getBlock() == this.block)
			{
				int metadata = (Integer) state.getValue(BlockLayered.SIZE);
				
				if (/*depth <= 6 && */worldIn.checkNoEntityCollision(state.getBoundingBox(worldIn, pos)) && ((BlockLayered)this.block).changeHeight(worldIn, pos, metadata + 1)) //changes full BlockLayered into full block
				{
					worldIn.playSound(null, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, this.block.getSoundType(state, worldIn, pos, player).getPlaceSound(), SoundCategory.BLOCKS, (this.block.getSoundType().getVolume() + 1.0F) / 2.0F, this.block.getSoundType().getPitch() * 0.8F);
					stack.shrink(1);
					return EnumActionResult.SUCCESS;
				}
			}
			
			return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		}
	}
	
}