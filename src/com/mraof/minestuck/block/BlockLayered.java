package com.mraof.minestuck.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.mraof.minestuck.Minestuck;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockLayered extends Block
{
	public Block fullBlock;

	public BlockLayered(Block iconBlock)
	{
		super(iconBlock.getMaterial());	//May work. Will atleast prevent snowy texture when placed on grass.

		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
		this.setCreativeTab(Minestuck.tabMinestuck);
		this.setBlockBoundsForDepth(0);
		this.fullBlock = iconBlock;
	}

	@Override
	@SideOnly(Side.CLIENT)

	/**
	 * When this method is called, your block should register all the icons it needs with the given IconRegister. This
	 * is the only chance you get to register icons.
	 */
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		this.blockIcon = fullBlock.getIcon(0, 0);
	}

	/**
	 * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
	 * cleared to be reused)
	 */
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	{
		int l = par1World.getBlockMetadata(par2, par3, par4) & 7;
		float f = 0.125F;
		return AxisAlignedBB.getAABBPool().getAABB(par2 + this.minX, par3 + this.minY, par4 + this.minZ, par2 + this.maxX, par3 + l * f, par4 + this.maxZ);
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

	/**
	 * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
	 */
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	/**
	 * Sets the block's bounds for rendering it as an item
	 */
	@Override
	public void setBlockBoundsForItemRender()
	{
		this.setBlockBoundsForDepth(0);
	}

	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z)
	{
		this.setBlockBoundsForDepth(blockAccess.getBlockMetadata(x, y, z));
	}

	/**
	 * calls setBlockBounds based on the depth of the snow. Int is any values 0x0-0x7, usually this blocks metadata.
	 */
	protected void setBlockBoundsForDepth(int metadata)
	{
		int depth = metadata & 7;
		float yBound = 2 * (1 + depth) / 16.0F;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, yBound, 1.0F);
	}

	/**
	 * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
	 */
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		Block underneathBlock = world.getBlock(x, y - 1, z);
		if (underneathBlock == Blocks.air)
			return false;
		if (underneathBlock == this && (world.getBlockMetadata(x, y - 1, z) & 7) == 7)
			return true;
		if (underneathBlock.isLeaves(world, x, y - 1, z) && underneathBlock.isOpaqueCube())
			return false;
		return underneathBlock.getMaterial().blocksMovement();
	}

	@Override
	@SideOnly(Side.CLIENT)

	/**
	 * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
	 * coordinates.  Args: blockAccess, x, y, z, side
	 */
	public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side)
	{
		return side == 1 ? true : super.shouldSideBeRendered(blockAccess, x, y, z, side);
	}

	@Override
	public int quantityDropped(int meta, int fortune, Random random)
	{
		return (meta & 7) + 1;
	}

	/**
	 * Determines if a new block can be replace the space occupied by this one,
	 * Used in the player's placement code to make the block act like water, and lava.
	 *
	 * @param world The current world
	 * @param x X Position
	 * @param y Y position
	 * @param z Z position
	 * @return True if the block is replaceable by another block
	 */
	@Override
	public boolean isReplaceable(IBlockAccess world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);
		return (meta >= 7 ? false : blockMaterial.isReplaceable());
	}

	public boolean changeHeight(World world, int x, int y, int z, int metadata)
	{
		int depth = metadata & 7;
		//    	Debug.print("Setting ");
		return (depth != 7 ? world.setBlockMetadataWithNotify(x, y, z, depth, 2) : world.setBlock(x, y, z, this.fullBlock));
	}

}
