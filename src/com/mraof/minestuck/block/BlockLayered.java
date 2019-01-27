package com.mraof.minestuck.block;

import com.mraof.minestuck.item.TabMinestuck;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockLayered extends Block
{
	protected static final AxisAlignedBB[] LAYERED_AABB = {new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1/8D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 2/8D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 3/8D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 4/8D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 5/8D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 6/8D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 7/8D, 1.0D)};
	
	public IBlockState sourceBlock;
	public static final PropertyInteger SIZE = PropertyInteger.create("size", 1, 7);
	
	public BlockLayered(IBlockState iconBlock)
	{
		super(iconBlock.getMaterial());
		
		this.setCreativeTab(TabMinestuck.instance);
		this.sourceBlock = iconBlock;
		setSoundType(sourceBlock.getBlock().getSoundType());
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, SIZE);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(SIZE) - 1;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(SIZE, meta + 1);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		int size = state.getValue(SIZE);
		
		return LAYERED_AABB[size - 1];
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}
	
	/**
	 * Checks to see if its valid to put this block at the specified coordinates. Args: world, pos
	 */
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos)
	{
		IBlockState underneathBlock = world.getBlockState(pos.down());
		if (underneathBlock.getBlock().isAir(underneathBlock, world, pos.down()))
			return false;
		if (underneathBlock.getBlock().isLeaves(underneathBlock, world, pos.add(0, -1, 0)) && underneathBlock.isOpaqueCube())
			return false;
		return underneathBlock.getBlock().getMaterial(underneathBlock).blocksMovement();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return side == EnumFacing.UP ? true : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}
	
	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random)
	{
		return state.getValue(SIZE) & 7;
	}
	
	@Override
	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
	{
		int meta = worldIn.getBlockState(pos).getValue(SIZE);
		return meta >= 7 && blockMaterial.isReplaceable();
	}

	public boolean changeHeight(World world, BlockPos pos, int metadata)
	{
		IBlockState block = (metadata <= 7 ? getDefaultState().withProperty(SIZE, metadata) : this.sourceBlock);
		return  world.setBlockState(pos, block, 3);
	}

}
