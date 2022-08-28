package com.mraof.minestuck.block;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.tileentity.GateTileEntity;
import com.mraof.minestuck.world.GateHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.CommonLevelAccessor;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GateBlock extends AbstractGateBlock
{
	private static final Logger LOGGER = LogManager.getLogger();
	
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
	
	public static void placeGate(CommonLevelAccessor level, BlockPos centerPos, GateHandler.Type type, int blockFlag)
	{
		for(int offsetX = -1; offsetX <= 1; offsetX++)
		{
			for(int offsetZ = -1; offsetZ <= 1; offsetZ++)
			{
				if(offsetX == 0 && offsetZ == 0)
				{
					level.setBlock(centerPos, MSBlocks.GATE.get().defaultBlockState().setValue(GateBlock.MAIN, true), blockFlag);
					BlockEntity tileEntity = level.getBlockEntity(centerPos);
					if(tileEntity instanceof GateTileEntity gate)
						gate.gateType = type;
					else
						LOGGER.error("Expected a gate tile entity after placing a gate block, but got {}!", tileEntity);
				} else
					level.setBlock(centerPos.offset(offsetX, 0, offsetZ), MSBlocks.GATE.get().defaultBlockState(), blockFlag);
			}
		}
	}
}