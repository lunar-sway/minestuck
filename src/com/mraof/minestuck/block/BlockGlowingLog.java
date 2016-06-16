package com.mraof.minestuck.block;

import com.mraof.minestuck.Minestuck;

import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockGlowingLog extends BlockLog
{

	public BlockGlowingLog()
	{
		super();
		setCreativeTab(Minestuck.tabMinestuck);
		setUnlocalizedName("glowingLog");
		setLightLevel(0.75F);
		setDefaultState(blockState.getBaseState().withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
	}
	
	@Override
	public boolean canSustainLeaves(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return false;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState state = getDefaultState();
		EnumAxis axis = EnumAxis.values()[meta&3];
		state = state.withProperty(LOG_AXIS, axis);
		return state;
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		EnumAxis axis = (EnumAxis) state.getValue(LOG_AXIS);
		int meta = axis.ordinal();
		return meta;
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] {LOG_AXIS});
	}
	
}