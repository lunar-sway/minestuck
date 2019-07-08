package com.mraof.minestuck.client.renderer;

import com.mraof.minestuck.tileentity.TileEntityAlchemiter;
import com.mraof.minestuck.tileentity.TileEntityItemStack;
import com.mraof.minestuck.util.ColorCollector;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReaderBase;

import javax.annotation.Nullable;

public class BlockColorCruxite implements IBlockColor
{
	@Override
	public int getColor(IBlockState state, @Nullable IWorldReaderBase worldIn, @Nullable BlockPos pos, int tintIndex)
	{
		ItemStack dowel = ItemStack.EMPTY;
		TileEntity tileEntity = worldIn != null && pos != null ? worldIn.getTileEntity(pos) : null;
		if(tileEntity instanceof TileEntityAlchemiter)
			dowel = ((TileEntityAlchemiter) tileEntity).getDowel();
		if(tileEntity instanceof TileEntityItemStack)
			dowel = ((TileEntityItemStack) tileEntity).getStack();
		
		if(!dowel.isEmpty())
		{
			int color = dowel.hasTag() && dowel.getTag().contains("color") ? ColorCollector.getColor(dowel.getTag().getInt("color") - 1) : 0x99D9EA;
			return handleColorTint(color, tintIndex);
		}
		return -1;
	}
	
	public static int handleColorTint(int color, int tintIndex)
	{
		if(tintIndex == 0 || tintIndex == 1)
		{
			if(tintIndex == 1)
			{
				int i0 = ((color & 255) + 255)/2;
				int i1 = (((color >> 8) & 255) + 255)/2;
				int i2 = (((color >> 16) & 255) + 255)/2;
				color = i0 | (i1 << 8) | (i2 << 16);
			}
			return color;
		}
		return -1;
	}
}