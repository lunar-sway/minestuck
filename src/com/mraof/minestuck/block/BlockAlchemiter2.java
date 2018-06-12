//for the extra block states
package com.mraof.minestuck.block;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;

public class BlockAlchemiter2 extends BlockAlchemiter{

	public static final PropertyEnum<enumParts> PART = PropertyEnum.<enumParts>create("part",enumParts.class);


	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {PART});
    }
	public static enum enumParts implements IStringSerializable
	{
		ZERO_TWO_ZERO,
		ZERO_THREE_ZERO,
		ZERO_FOUR_ZERO;
		
	
		
		public String toString()
		{
			return getName();
		}
		public String getName()
		{
			switch (this){
			case ZERO_TWO_ZERO: return "zero_two_zero";
			case ZERO_THREE_ZERO:return "zero_three_zero";
			case ZERO_FOUR_ZERO:return "zero_four_zero";
			}
			return "null";
		}
		
		

	}
	
	
	
	@Override
	public BlockPos GetMasterPos(IBlockState state, BlockPos pos){
		enumParts part=state.getValue(BlockAlchemiter2.PART);
		switch(part){
		case ZERO_TWO_ZERO:	return pos.north(0).down(1).east(0);
		case ZERO_THREE_ZERO:return pos.north(0).down(2).east(0);
		case ZERO_FOUR_ZERO:return pos.north(0).down(3).east(0);
		}
		return pos;
	}
	@Override
	public IBlockState getStateFromMeta(int meta){
		IBlockState defaultState=getDefaultState();
		switch (meta){
		case 0: return defaultState.withProperty(PART, enumParts.ZERO_TWO_ZERO);
		case 1: return defaultState.withProperty(PART, enumParts.ZERO_THREE_ZERO);
		case 2: return defaultState.withProperty(PART, enumParts.ZERO_FOUR_ZERO);
		
		}
		return null;
	}

	@Override
	public int getMetaFromState(IBlockState state){
		enumParts part=state.getValue(PART);
	switch(part){
	case ZERO_TWO_ZERO:return 0;
	case ZERO_THREE_ZERO:return 1;
	case ZERO_FOUR_ZERO:return 2;
		}
		return 0;
	}

	
	
	
	
}