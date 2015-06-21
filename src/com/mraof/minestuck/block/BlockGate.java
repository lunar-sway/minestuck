package com.mraof.minestuck.block;

import com.mraof.minestuck.tileentity.TileEntityGate;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockGate extends Block implements ITileEntityProvider
{
	
	protected static PropertyBool isMainComponent = PropertyBool.create("mainComponent");
	
	public BlockGate()
	{
		super(Material.portal);
	}
	
	@Override
	public int getRenderType()
	{
		return -1;
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public boolean isFullCube()
	{
		return false;
	}
	
	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, isMainComponent);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((Boolean) state.getValue(isMainComponent)) ? 0 : 1;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(isMainComponent, meta == 0 ? true : false);	//TODO Switch after rendering testing
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityGate();
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return (Boolean) state.getValue(isMainComponent);
	}
	
	protected boolean isValid(BlockPos pos, World world, IBlockState state)
	{
		if((Boolean) state.getValue(isMainComponent))
		{
			for(int x = -1; x <= 1; x++)
				for(int z = -1; z <= 1; z++)
					if(x != 0 || z != 0)
					{
						IBlockState block = world.getBlockState(pos.add(x, 0, z));
						if(block.getBlock() != this || (Boolean) block.getValue(isMainComponent))
							return false;
					}
			
			return true;
		} else
		{
			for(int x = -1; x <= 1; x++)
				for(int z = -1; z <= 1; z++)
					if(x != 0 || z != 0)
					{
						IBlockState block = world.getBlockState(pos.add(x, 0, z));
						if(block.getBlock() == this && (Boolean) block.getValue(isMainComponent))
							return this.isValid(pos.add(x, 0, z), world, block);
					}
			
			return false;
		}
	}
	
}