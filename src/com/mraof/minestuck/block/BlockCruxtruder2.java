package com.mraof.minestuck.block;

import com.mraof.minestuck.block.BlockTotemlathe.EnumParts;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;

public class BlockCruxtruder2 extends BlockCruxtruder {


	public static final PropertyEnum<enumParts> PART = PropertyEnum.<enumParts>create("part",enumParts.class);
	
	BlockCruxtruder2(){
		setUnlocalizedName("cruxtruder2");
	}
	
	@Override
	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {PART});
    }

	//Block state handling
	public static enum enumParts implements IStringSerializable
	{


		ZERO_TWO_ZERO,
		ZERO_TWO_ONE,
		ZERO_TWO_TWO,
		ONE_TWO_ZERO,
		ONE_TWO_ONE,
		ONE_TWO_TWO,
		ONE_THREE_ONE,
		TWO_TWO_ZERO,
		TWO_TWO_ONE,
		TWO_TWO_TWO;
	
		
		public String toString()
		{
			return getName();
		}
		public String getName()
		{
			return name().toLowerCase();
		}
		
		

	}
	@Override
	public BlockPos GetMasterPos(IBlockState state, BlockPos pos){
		enumParts part=state.getValue(PART);
		switch(part){
		case ZERO_TWO_ZERO:	return pos.north(0).down(1).west(0);
		case ZERO_TWO_ONE:	return pos.north(0).down(1).west(1);
		case ZERO_TWO_TWO:	return pos.north(0).down(1).west(2);
		case ONE_TWO_ZERO:	return pos.north(1).down(1).west(0);
		case ONE_TWO_ONE:	return pos.north(1).down(1).west(1);
		case ONE_TWO_TWO:	return pos.north(1).down(1).west(2);
		case ONE_THREE_ONE:	return pos.north(1).down(2).west(1);
		case TWO_TWO_ZERO:	return pos.north(2).down(1).west(0);
		case TWO_TWO_ONE:	return pos.north(2).down(1).west(1);
		case TWO_TWO_TWO:	return pos.north(2).down(1).west(2);
		}
		return pos;
	}
	@Override
	public IBlockState getStateFromMeta(int meta){
		IBlockState defaultState=getDefaultState();
		switch (meta){
		case 0: return defaultState.withProperty(PART, enumParts.ZERO_TWO_ZERO);
		case 1: return defaultState.withProperty(PART, enumParts.ZERO_TWO_ONE);
		case 2: return defaultState.withProperty(PART, enumParts.ZERO_TWO_TWO);
		case 3: return defaultState.withProperty(PART, enumParts.ONE_TWO_ZERO);
		case 4: return defaultState.withProperty(PART, enumParts.ONE_TWO_ONE);
		case 5: return defaultState.withProperty(PART, enumParts.ONE_TWO_TWO);
		case 6: return defaultState.withProperty(PART, enumParts.ONE_THREE_ONE);
		case 7: return defaultState.withProperty(PART, enumParts.TWO_TWO_ZERO);
		case 8: return defaultState.withProperty(PART, enumParts.TWO_TWO_ONE);
		case 9: return defaultState.withProperty(PART, enumParts.TWO_TWO_TWO);
		
		
		}
		return null;
	}

	@Override
	public int getMetaFromState(IBlockState state){
		enumParts part=state.getValue(PART);
	switch(part){
	case ZERO_TWO_ZERO: return 0;
	case ZERO_TWO_ONE: 	return 1;
	case ZERO_TWO_TWO:	return 2;
	case ONE_TWO_ZERO:	return 3;
	case ONE_TWO_ONE:	return 4;
	case ONE_TWO_TWO:	return 5;
	case ONE_THREE_ONE:	return 6;
	case TWO_TWO_ZERO:	return 7;
	case TWO_TWO_ONE:	return 8;
	case TWO_TWO_TWO:	return 9;
	}
		return 0;
	}
	

	
	
}

