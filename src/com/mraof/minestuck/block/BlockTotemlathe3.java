package com.mraof.minestuck.block;


import com.mraof.minestuck.tileentity.TileEntityTotemlathe;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTotemlathe3 extends BlockTotemlathe{


	public static final PropertyEnum<EnumParts> PART = PropertyEnum.create("part", EnumParts.class);
	public static final PropertyDirection DIRECTION = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	public BlockTotemlathe3(){
		setUnlocalizedName("totem_lathe3");
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
		EnumParts part = EnumParts.values()[meta % 4];
		EnumFacing facing = EnumFacing.getHorizontal(meta/4);
		
		return defaultState.withProperty(PART, part).withProperty(DIRECTION, facing);
	}
	@Override
	public int getMetaFromState(IBlockState state)
	{
		EnumParts part = state.getValue(PART);
		EnumFacing facing = state.getValue(DIRECTION);
		return part.ordinal() + facing.getHorizontalIndex()*4;
	}

	
    /**
     *returns the block position of the "Main" block
     *aka the block with the TileEntity for the machine
     */
	public BlockPos getMainPos(IBlockState state, BlockPos pos)
	{
		EnumFacing facing = state.getValue(DIRECTION);
		switch(state.getValue(PART))
		{
			case TOP_MIDRIGHT:return pos.down(2).offset(facing.rotateYCCW(),2);
			case TOP_MIDLEFT:return pos.down(2).offset(facing.rotateYCCW(),1);
			case TOP_LEFT:return pos.down(2);
			}
			return pos;
	}
	
	public static enum EnumParts implements IStringSerializable
	{
		//(new AxisAlignedBB(5/16D, 0.0D, 0.0D, 1.0D, 1.0D, 11/16D), new AxisAlignedBB(5/16D, 0.0D, 5/16D, 1.0D, 1.0D, 1.0D),
		//new AxisAlignedBB(0.0D, 0.0D, 5/16D, 11/16D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 11/16D, 1.0D, 11/16D)),
		// try out code later to see if i can find out how it works
		
		TOP_MIDRIGHT,
		TOP_MIDLEFT,
		TOP_LEFT;
		
	private final AxisAlignedBB[] BOUNDING_BOX;
		
		EnumParts(AxisAlignedBB... bb)
		{
			BOUNDING_BOX = bb;
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
	public boolean hasTileEntity(IBlockState state)
	{
		return false;
	}

}
