package com.mraof.minestuck.block;

import com.mraof.minestuck.MinestuckConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class GateBlock extends AbstractGateBlock
{
	public GateBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	protected boolean isValid(BlockPos pos, Level level)
	{
		for(int x = -1; x <= 1; x++)
			for(int z = -1; z <= 1; z++)
				if(x != 0 || z != 0)
				{
					BlockState block = level.getBlockState(pos.offset(x, 0, z));
					if(block.getBlock() != this || block.getValue(MAIN))
						return false;
				}
		
		return true;
	}
	
	@Override
	protected BlockPos findMainComponent(BlockPos pos, Level level)
	{
		for(int x = -1; x <= 1; x++)
			for(int z = -1; z <= 1; z++)
				if(x != 0 || z != 0)
				{
					BlockState block = level.getBlockState(pos.offset(x, 0, z));
					if(block.is(this) && block.getValue(MAIN))
						return pos.offset(x, 0, z);
				}
		
		return null;
	}
	
	@Override
	protected void removePortal(BlockPos pos, Level level)
	{
		for(int x = -1; x <= 1; x++)
			for(int z = -1; z <= 1; z++)
				if(level.getBlockState(pos.offset(x, 0, z)).is(this))
					level.removeBlock(pos.offset(x, 0, z), false);
	}
	
	@Override
	public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion)
	{
		if(MinestuckConfig.SERVER.canBreakGates.get())
			return super.getExplosionResistance(state, level, pos, explosion);
		else return 3600000.0F;
	}
}