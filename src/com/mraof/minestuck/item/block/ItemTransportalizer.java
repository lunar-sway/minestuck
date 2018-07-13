package com.mraof.minestuck.item.block;

import com.mraof.minestuck.tileentity.TileEntityTransportalizer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemTransportalizer  extends ItemBlock
{
	public ItemTransportalizer(Block block)
	{
		super(block);
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState)
	{
		if(super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState))
		{
			if(stack.hasDisplayName() && stack.getDisplayName().length() == 4)
			{
				TileEntity te = world.getTileEntity(pos);
				if(te instanceof TileEntityTransportalizer)
					((TileEntityTransportalizer)te).setId(stack.getDisplayName().toUpperCase());
			}
			return true;
		}
		return false;
	}
}