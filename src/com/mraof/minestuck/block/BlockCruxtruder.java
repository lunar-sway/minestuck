package com.mraof.minestuck.block;

import com.mraof.minestuck.tileentity.TileEntityCruxtruder;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCruxtruder extends BlockLargeMachine
{
	public static final PropertyEnum<EnumParts> PART = PropertyEnum.create("part", EnumParts.class);
	public static final PropertyDirection DIRECTION = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	public BlockCruxtruder()
	{
		setUnlocalizedName("cruxtruder");
	}
	//not sure how to do this.
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state,IBlockAccess source,BlockPos pos)
	{
		EnumParts parts = state.getValue(PART);
		return parts.getBoundingBox(state.getValue(DIRECTION));
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(state.getValue(PART) == EnumParts.TUBE && (state.getValue(DIRECTION) == facing || facing == EnumFacing.UP))
		{
			if(worldIn.isRemote)
				return true;
			
			TileEntity te = worldIn.getTileEntity(pos);
			if(te instanceof TileEntityCruxtruder)
				((TileEntityCruxtruder) te).onRightClick(playerIn, facing == EnumFacing.UP);
			return true;
		}
		return false;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		if(meta/4 == EnumParts.TUBE.ordinal())
			return new TileEntityCruxtruder();
		else return null;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		BlockPos MainPos = getMainPos(state, pos);
		TileEntity te = worldIn.getTileEntity(MainPos);
		if(te instanceof TileEntityCruxtruder)
		{
			((TileEntityCruxtruder) te).destroy();
		}
		
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return state.getValue(PART) == EnumParts.CENTER;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return state.getValue(PART) == EnumParts.CENTER;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		if(state.getValue(PART) == EnumParts.CENTER)
			return BlockFaceShape.SOLID;
		return BlockFaceShape.UNDEFINED;
	}
	
	public enum EnumParts implements IStringSerializable
	{
		BASE_CORNER(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1D, 1D, 1D)),
		BASE_SIDE(new AxisAlignedBB (0.0D, 0.0D, 0.0D, 1D, 1D, 1D)),
		CENTER(new AxisAlignedBB( 0, 0, 0, 1, 1, 1)),
		TUBE(new AxisAlignedBB(2/16D, 0D, 2/16D, 14/16D, 1D, 14/16D));
		
		private final AxisAlignedBB[] BOUNDING_BOX;
		
		EnumParts(AxisAlignedBB... bb)
		{
			BOUNDING_BOX = bb;
		}
		
		public AxisAlignedBB getBoundingBox(EnumFacing facing)
		{
			int i = facing.getHorizontalIndex();
			return BOUNDING_BOX[i%BOUNDING_BOX.length];
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
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, PART, DIRECTION);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState defaultState = getDefaultState();
		EnumParts part = EnumParts.values()[meta/4];
		EnumFacing facing = EnumFacing.getHorizontal(meta%4);
		
		return defaultState.withProperty(PART, part).withProperty(DIRECTION, facing);
	}
	@Override
	public int getMetaFromState(IBlockState state)
	{
		EnumParts part = state.getValue(PART);
		EnumFacing facing = state.getValue(DIRECTION);
		return part.ordinal()*4 + facing.getHorizontalIndex();
	}

	public BlockPos getMainPos(IBlockState state, BlockPos pos)
	{
			EnumParts part = state.getValue(PART);
			EnumFacing facing = state.getValue(DIRECTION);
			switch(part)
			{
				case BASE_CORNER:
					return pos.offset(facing.rotateY()).offset(facing.getOpposite()).up();
				case BASE_SIDE:
					return pos.offset(facing.getOpposite()).up();
				case CENTER:
					return pos.up();
				default:
					return pos;
			}
	}
	@Override
	public Item getItemFromMachine() 
	{
		return new ItemStack(MinestuckBlocks.cruxtruder).getItem();
	}
}