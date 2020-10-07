package com.mraof.minestuck.client.renderer;

import com.mraof.minestuck.tileentity.IColored;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;

import javax.annotation.Nullable;

public class BlockColorCruxite implements IBlockColor
{
	@Override
	public int getColor(BlockState state, @Nullable IBlockDisplayReader worldIn, @Nullable BlockPos pos, int tintIndex)
	{
		TileEntity tileEntity = worldIn != null && pos != null ? worldIn.getTileEntity(pos) : null;
		if(tileEntity instanceof IColored)
			return handleColorTint(((IColored) tileEntity).getColor(), tintIndex);
		
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