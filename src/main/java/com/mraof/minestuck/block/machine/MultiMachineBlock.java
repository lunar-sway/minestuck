package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.block.MSBlockShapes;
import com.mraof.minestuck.util.CustomVoxelShape;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class MultiMachineBlock extends MachineBlock
{
	protected final MachineMultiblock machine;
	
	public MultiMachineBlock(MachineMultiblock machine, Properties properties)
	{
		super(properties);
		this.machine = machine;
	}
	
	/**
	 * Destroys the blocks that are connected to the given block in the multiblock structure, then repeats the process for those blocks, until the entire structure is destroyed.
	 * @param state The blockstate of the block being currently destroyed.
	 * @param level The server level/world
	 * @param pos The position of the block currently being destroyed.
	 */
	public void findAndDestroyConnected(BlockState state, Level level, BlockPos pos)
	{
		if(state.isAir() || !(level.getBlockState(pos).getBlock() instanceof MultiMachineBlock))
			return;
		else
			level.destroyBlock(pos, false);
		
		BlockPos offsetPos = new BlockPos(1, 0, 0);
		findAndDestroyConnected(level.getBlockState(pos.offset(offsetPos)), level, pos.offset(offsetPos));
		
		offsetPos = new BlockPos(-1, 0, 0);
		findAndDestroyConnected(level.getBlockState(pos.offset(offsetPos)), level, pos.offset(offsetPos));
		
		offsetPos = new BlockPos(0, 1, 0);
		findAndDestroyConnected(level.getBlockState(pos.offset(offsetPos)), level, pos.offset(offsetPos));
		
		offsetPos = new BlockPos(0, -1, 0);
		findAndDestroyConnected(level.getBlockState(pos.offset(offsetPos)), level, pos.offset(offsetPos));
		
		offsetPos = new BlockPos(0, 0, 1);
		findAndDestroyConnected(level.getBlockState(pos.offset(offsetPos)), level, pos.offset(offsetPos));
		
		offsetPos = new BlockPos(0, 0, -1);
		findAndDestroyConnected(level.getBlockState(pos.offset(offsetPos)), level, pos.offset(offsetPos));
		
	}
	
	//TODO Is this class needed? Is there anything that subclasses do that is better off done here?
}