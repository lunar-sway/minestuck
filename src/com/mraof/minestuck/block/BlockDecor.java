package com.mraof.minestuck.block;

import com.mraof.minestuck.item.TabMinestuck;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockDecor extends Block
{

	private EnumBB bb = EnumBB.CHESSBOARD;
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	protected BlockDecor(String unlocalizedName, SoundType sound) {
		super(Material.ROCK);
		setSoundType(sound);
		setUnlocalizedName(unlocalizedName);
		setCreativeTab(TabMinestuck.instance);
		setHardness(0.5f);
	}

	
	protected BlockDecor(String unlocalizedName)
	{
		this(unlocalizedName, SoundType.STONE);
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		switch(getBBFromName())
		{
			case CHESSBOARD:
				return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
			case BLENDER: case FROG_STATUE: default:
				return BlockFaceShape.UNDEFINED;
		}
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean isFullBlock(IBlockState state) 
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	/**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withProperty(FACING, mirrorIn.mirror((EnumFacing)state.getValue(FACING)));
    }

    /**
     * Checks if this block can be placed exactly at the given position.
     */
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos.down()).isFullCube();
    }

    /**
     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
     * IBlockstate
     */
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
    	return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta % 4));
    }

    public int getMetaFromState(IBlockState state)
    {
    	return (state.getValue(FACING)).getHorizontalIndex();
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }

    @Override
	public AxisAlignedBB getBoundingBox(IBlockState state,IBlockAccess source,BlockPos pos)
	{
		EnumFacing facing = state.getValue(FACING);

		EnumBB boundingBox = getBBFromName();

		return boundingBox.BOUNDING_BOX[facing.getHorizontalIndex()];
	}

    public AxisAlignedBB modifyAABBForDirection(EnumFacing facing, AxisAlignedBB bb)
	{
		AxisAlignedBB out = null;
		switch(facing.ordinal())
		{
		case 2:	//North
			out = new AxisAlignedBB(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
			break;
		case 3:	//South
			out = new AxisAlignedBB(1-bb.maxX, bb.minY, 1-bb.maxZ, 1-bb.minX, bb.maxY, 1-bb.minZ);
			break;
		case 4:	//West
			out = new AxisAlignedBB(bb.minZ, bb.minY, 1-bb.maxX, bb.maxZ, bb.maxY, 1-bb.minX);
			break;
		case 5:	//East
			out = new AxisAlignedBB(1-bb.maxZ, bb.minY, bb.minX, 1-bb.minZ, bb.maxY, bb.maxX);
			break;
		}
		return out;
	}

    public EnumBB getBBFromName()
    {
    	String unlocalizedName = getUnlocalizedName();
    	EnumBB boundingBox = EnumBB.CHESSBOARD;

    	switch(unlocalizedName)
    	{
    	case "tile.chessboard": boundingBox = EnumBB.CHESSBOARD; break;
    	case "tile.frogStatueReplica": boundingBox = EnumBB.FROG_STATUE; break;
    	case "tile.blender": boundingBox = EnumBB.BLENDER; break;
    	}
		return boundingBox;

    }

	public enum EnumBB implements IStringSerializable
	{
		DEFAULT		(FULL_BLOCK_AABB),
		CHESSBOARD	(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1/16D, 1.0D)),
		FROG_STATUE	(new AxisAlignedBB(1/32D,0.0D,3/32D,31/32D,15/16D,29/32D)),
		BLENDER		(new AxisAlignedBB(3/16D,0D,3/16D,13/16D,1.0D,13/16D));

		private final AxisAlignedBB[] BOUNDING_BOX;

		EnumBB(AxisAlignedBB bb)
		{
			BOUNDING_BOX = new AxisAlignedBB[4];
			BOUNDING_BOX[0] = bb;
			BOUNDING_BOX[1] = new AxisAlignedBB(1 - bb.maxZ, bb.minY, bb.minX, 1 - bb.minZ, bb.maxY, bb.maxX);
			BOUNDING_BOX[2] = new AxisAlignedBB(1 - bb.maxX, bb.minY, 1- bb.maxZ, 1 - bb.minX, bb.maxY, 1 - bb.minZ);
			BOUNDING_BOX[3] = new AxisAlignedBB(bb.minZ, bb.minY, 1 - bb.maxX, bb.maxZ, bb.maxY, 1 - bb.minX);

		}

		@Override
		public String toString()
		{
			return getName();
		}

		@Override
		public String getName()
		{
			return name().toLowerCase();
		}
	}
}
