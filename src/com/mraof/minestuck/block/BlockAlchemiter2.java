//for the extra block states
package com.mraof.minestuck.block;

import com.mraof.minestuck.block.BlockAlchemiter.EnumParts;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;

public class BlockAlchemiter2 extends BlockAlchemiter{

	public static final PropertyEnum<EnumParts> PART = PropertyEnum.<EnumParts>create("part",EnumParts.class);
	public static final PropertyDirection DIRECTION = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);


	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, PART,DIRECTION);
    }
	public static enum EnumParts implements IStringSerializable
	{
		ZERO_ONE_ONE,
		ZERO_ONE_TWO,
		ZERO_ONE_THREE,
		ONE_ONE_ONE;
		
	
		
		public String toString()
		{
			return getName();
		}
		public String getName()
		{
			return name().toLowerCase();
		}
		
		
		
	}
	//makes things easier on me if the base of the alchemiter does nothing
	@Override
	public BlockPos getMainPos(IBlockState state, BlockPos pos){
		return pos;
//		EnumParts part=state.getValue(BlockAlchemiter2.PART);
//		switch(part){		
//		case ZERO_ONE_ONE:	return pos.north(0).down(0).east(1);
//		case ZERO_ONE_TWO:	return pos.north(0).down(0).east(2);
//		case ZERO_ONE_THREE:return pos.north(0).down(0).east(3);
//		case ONE_ONE_ONE:	return pos.north(1).down(0).east(1);
//		}
//		return pos;
	}
	@Override
	public IBlockState getStateFromMeta(int meta){
		IBlockState defaultState=getDefaultState();		
		EnumParts part = EnumParts.values()[meta % 4];
		EnumFacing facing = EnumFacing.getHorizontal(meta/4);
		

		return defaultState.withProperty(PART, part).withProperty(DIRECTION, facing);
	}

	@Override
	public int getMetaFromState(IBlockState state){
		EnumParts part = state.getValue(PART);
		EnumFacing facing = state.getValue(DIRECTION);
		return part.ordinal()+facing.getHorizontalIndex()*4;
	}
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return false;
	}

	
	
	
	
}