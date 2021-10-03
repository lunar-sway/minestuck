package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.tileentity.redstone.AreaEffectTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class AreaEffectBlock extends Block
{
	public AreaEffectBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new AreaEffectTileEntity();
	}
}
