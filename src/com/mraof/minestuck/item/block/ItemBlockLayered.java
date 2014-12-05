package com.mraof.minestuck.item.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import com.mraof.minestuck.block.BlockLayered;

public class ItemBlockLayered extends ItemBlock {

	public Block theBlock;
	public ItemBlockLayered(Block par1)
	{
		super(par1);
		theBlock = ((BlockLayered)par1).fullBlock;
	}
	
	/**
	 * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
	 * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
	 */
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (stack.stackSize == 0)
		{
			return false;
		}
		else if (!playerIn.func_175151_a(pos, side, stack))	//Originally called "canPlayerEdit"
		{
			return false;
		}
		else
		{
			IBlockState state = worldIn.getBlockState(pos);
			
			if (state.getBlock() == this.block)
			{
				int metadata = (Integer) state.getValue(BlockLayered.SIZE);
				
				if (/*depth <= 6 && */worldIn.checkNoEntityCollision(this.block.getCollisionBoundingBox(worldIn, pos, state)) && ((BlockLayered)this.block).changeHeight(worldIn, pos, metadata + 1)) //changes full BlockLayered into full block
				{
					worldIn.playSoundEffect((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, this.block.stepSound.getPlaceSound(), (this.block.stepSound.getVolume() + 1.0F) / 2.0F, this.block.stepSound.getFrequency() * 0.8F);
					--stack.stackSize;
					return true;
				}
			}
			
			return super.onItemUse(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
		}
	}
	
}
