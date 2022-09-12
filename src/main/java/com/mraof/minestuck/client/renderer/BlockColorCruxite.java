package com.mraof.minestuck.client.renderer;

import com.mraof.minestuck.blockentity.IColored;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class BlockColorCruxite implements BlockColor
{
	@Override
	public int getColor(BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int tintIndex)
	{
		BlockEntity tileEntity = level != null && pos != null ? level.getBlockEntity(pos) : null;
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