package com.mraof.minestuck.client.renderer;

import com.mraof.minestuck.block.BlockAlchemiter;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.tileentity.TileEntityAlchemiter;
import com.mraof.minestuck.util.ColorCollector;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockColorCruxite implements IBlockColor
{
	@Override
	public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex)
	{
		if(tintIndex == 0)
		{
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			ItemStack dowel = ItemStack.EMPTY;
			if(state.getBlock() == MinestuckBlocks.alchemiter[0])
			{
				if(state.getValue(BlockAlchemiter.PART1) == BlockAlchemiter.EnumParts.TOTEM_PAD && tileEntity instanceof TileEntityAlchemiter)
					dowel = ((TileEntityAlchemiter)tileEntity).getDowel();
			}
			
			if(!dowel.isEmpty())
			{
				return dowel.getMetadata() == 0 ? 0x99D9EA : ColorCollector.getColor(dowel.getMetadata() - 1);
			}
		}
		return -1;
	}
}