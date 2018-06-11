package com.mraof.minestuck.client.renderer;

import com.mraof.minestuck.block.BlockAlchemiter;
import com.mraof.minestuck.block.BlockTotemLathe;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.tileentity.TileEntityAlchemiter;
import com.mraof.minestuck.tileentity.TileEntityItemStack;
import com.mraof.minestuck.tileentity.TileEntityTotemLathe;
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
		if(tintIndex == 0 || tintIndex == 1)
		{
			ItemStack dowel = ItemStack.EMPTY;
			if(state.getBlock() == MinestuckBlocks.alchemiter[0])
			{
				TileEntity tileEntity = worldIn.getTileEntity(pos);
				if(state.getValue(BlockAlchemiter.PART1).isTotemPad() && tileEntity instanceof TileEntityAlchemiter)
					dowel = ((TileEntityAlchemiter) tileEntity).getDowel();
			} else if(state.getBlock() == MinestuckBlocks.totemlathe[1])
			{
				BlockPos mainPos = ((BlockTotemLathe) state.getBlock()).getMainPos(state, pos);
				TileEntity tileEntity = worldIn.getTileEntity(mainPos);
				if(tileEntity instanceof TileEntityTotemLathe)
					dowel = ((TileEntityTotemLathe) tileEntity).getDowel();
			} else if(state.getBlock() == MinestuckBlocks.blockCruxiteDowel)
			{
				TileEntity tileEntity = worldIn.getTileEntity(pos);
				if(tileEntity instanceof TileEntityItemStack)
					dowel = ((TileEntityItemStack) tileEntity).getStack();
			}
			
			if(!dowel.isEmpty())
			{
				int color = dowel.getMetadata() == 0 ? 0x99D9EA : ColorCollector.getColor(dowel.getMetadata() - 1);
				if(tintIndex == 1)
				{
					int i0 = ((color & 255) + 255)/2;
					int i1 = (((color >> 8) & 255) + 255)/2;
					int i2 = (((color >> 16) & 255) + 255)/2;
					color = i0 | (i1 << 8) | (i2 << 16);
				}
				return color;
			}
		}
		return -1;
	}
}