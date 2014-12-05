package com.mraof.minestuck.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
//import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.Debug;

public class BlockLayered extends Block
{
	public Block fullBlock;
	public static final PropertyInteger SIZE = PropertyInteger.create("size", 1, 7);
	
	public BlockLayered(Block iconBlock)
	{
		super(iconBlock.getMaterial());
		
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
		this.setCreativeTab(Minestuck.tabMinestuck);
		this.setBlockBoundsForDepth(0);
		this.fullBlock = iconBlock;
		stepSound = fullBlock.stepSound;
	}
	
	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, SIZE);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (Integer) state.getValue(SIZE) - 1;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(SIZE, meta + 1);
	}
	
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
	{
		int l = (((Integer) state.getValue(SIZE)) & 7);
		float f = 0.125F;
		return new AxisAlignedBB(pos.getX() + minX, pos.getY() + minY, pos.getZ() + minZ, pos.getX() + maxX, pos.getY() + l*f, pos.getZ() + maxZ);
	}

	/**
	 * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
	 * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
	 */
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

	/**
	 * Sets the block's bounds for rendering it as an item
	 */
	@Override
	public void setBlockBoundsForItemRender()
	{
		this.setBlockBoundsForDepth(1);
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos)
	{
		this.setBlockBoundsForDepth((Integer) access.getBlockState(pos).getValue(SIZE));
	}

	/**
	 * calls setBlockBounds based on the depth of the snow. Int is any values 0x0-0x7, usually this blocks metadata.
	 */
	protected void setBlockBoundsForDepth(int metadata)
	{
		int depth = metadata & 7;
		float yBound = 2 * depth / 16.0F;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, yBound, 1.0F);
	}

	/**
	 * Checks to see if its valid to put this block at the specified coordinates. Args: world, pos
	 */
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos)
	{
		IBlockState underneathBlock = world.getBlockState(pos.add(0, -1, 0));
		if (underneathBlock.getBlock() == Blocks.air)
			return false;
//		if (underneathBlock.getBlock() == this && (((Integer) underneathBlock.getValue(SIZE)) & 7) == 7)
//			return true;
		if (underneathBlock.getBlock().isLeaves(world, pos.add(0, -1, 0)) && underneathBlock.getBlock().isOpaqueCube())
			return false;
		return underneathBlock.getBlock().getMaterial().blocksMovement();
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
	 * coordinates.  Args: blockAccess, pos, side
	 */
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		return side == EnumFacing.UP ? true : super.shouldSideBeRendered(worldIn, pos, side);
	}
	
	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random)
	{
		return ((Integer) state.getValue(SIZE)) & 7;
	}
	
	/**
	 * Determines if a new block can be replace the space occupied by this one,
	 * Used in the player's placement code to make the block act like water, and lava.
	 *
	 * @param world The current world
	 * @param pos Position
	 * @return True if the block is replaceable by another block
	 */
	@Override
	public boolean isReplaceable(World worldIn, BlockPos pos)
	{
		int meta = (Integer) worldIn.getBlockState(pos).getValue(SIZE);
		return (meta > 7 ? false : blockMaterial.isReplaceable());
	}

	public boolean changeHeight(World world, BlockPos pos, int metadata)
	{
		IBlockState block = ((metadata & 7) > 7 ? getDefaultState().withProperty(SIZE, metadata & 7) : this.fullBlock.getDefaultState());
		return  world.setBlockState(pos, block, 3);
	}

}
