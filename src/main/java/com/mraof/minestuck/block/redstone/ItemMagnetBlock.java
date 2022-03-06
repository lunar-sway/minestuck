package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.block.DirectionalCustomModelBlock;
import com.mraof.minestuck.tileentity.redstone.ItemMagnetTileEntity;
import com.mraof.minestuck.util.CustomVoxelShape;
import com.mraof.minestuck.util.ParticlesAroundSolidBlock;
import net.minecraft.block.BlockState;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * When powered, the tile entity for this block pulls item entities towards it
 */
public class ItemMagnetBlock extends DirectionalCustomModelBlock
{
	
	public ItemMagnetBlock(Properties properties, CustomVoxelShape shape)
	{
		super(properties, shape);
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.UP));
	}
	
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new ItemMagnetTileEntity();
	}
	
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if(worldIn.getBestNeighborSignal(pos) > 0)
		{
			if(rand.nextInt(16 - worldIn.getBestNeighborSignal(pos)) == 0)
				ParticlesAroundSolidBlock.spawnParticles(worldIn, pos, () -> RedstoneParticleData.REDSTONE);
		}
	}
}