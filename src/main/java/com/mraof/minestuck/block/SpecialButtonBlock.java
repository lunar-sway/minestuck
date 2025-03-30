package com.mraof.minestuck.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SpecialButtonBlock extends ButtonBlock
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public final boolean explosive;
	
	public SpecialButtonBlock(boolean explosive, BlockSetType type, int ticksToStayPressed, Properties properties)
	{
		super(type, ticksToStayPressed, properties);
		this.explosive = explosive;
	}
	
	/* todo
	@Override
	public MapCodec<SpecialButtonBlock> codec()
	{
		return null;
	}
	*/
	
	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
	{
		boolean b = state.getValue(POWERED);
		super.tick(state, level, pos, random);
		if(level.getBlockState(pos).getBlock() != this)
		{
			LOGGER.warn("Tick update without the correct block/position?");
			return;
		}
		boolean b1 = level.getBlockState(pos).getValue(POWERED);
		if(explosive && b && !b1)
		{
			level.removeBlock(pos, false);
			level.explode(null, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, 1.5F, Level.ExplosionInteraction.BLOCK);
		}
	}
}