package com.mraof.minestuck.block;

import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public abstract class BlockLargeMachine extends BlockContainer
{
	public static final PropertyDirection DIRECTION = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, DIRECTION);
		
	}
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	public BlockLargeMachine() 
	{
		super(Material.ROCK);
		this.setHardness(2);
		this.setCreativeTab(MinestuckItems.tabMinestuck);
	}	
	//keeps the blocks from dropping something
	@Override
	public List<ItemStack> getDrops(IBlockAccess world,BlockPos pos,IBlockState state,int fortune)
	{
		return null;	
	}

	@Override
	public IBlockState getStateFromMeta(int meta) 
	{
	    return getDefaultState();
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state){
		return EnumBlockRenderType.MODEL;
	}
	@Override
	public boolean isOpaqueCube(IBlockState state){
		return false;
	}
}
