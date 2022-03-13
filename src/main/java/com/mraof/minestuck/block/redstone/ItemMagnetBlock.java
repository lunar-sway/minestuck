package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.block.DirectionalCustomModelBlock;
import com.mraof.minestuck.tileentity.redstone.ItemMagnetTileEntity;
import com.mraof.minestuck.util.CustomVoxelShape;
import com.mraof.minestuck.util.ParticlesAroundSolidBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
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
	public static final IntegerProperty POWER = BlockStateProperties.POWER;
	
	public ItemMagnetBlock(Properties properties, CustomVoxelShape shape)
	{
		super(properties, shape);
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.UP).setValue(POWER, 0));
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
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
		updatePower(worldIn, pos);
	}
	
	public void updatePower(World worldIn, BlockPos pos)
	{
		if(!worldIn.isClientSide)
		{
			BlockState state = worldIn.getBlockState(pos);
			int powerInt = worldIn.getBestNeighborSignal(pos);
			
			if(state.getValue(POWER) != powerInt)
				worldIn.setBlockAndUpdate(pos, state.setValue(POWER, powerInt));
			else worldIn.sendBlockUpdated(pos, state, state, 2);
		}
	}
	
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if(stateIn.getValue(POWER) > 0)
		{
			if(rand.nextInt(16 - stateIn.getValue(POWER)) == 0)
				ParticlesAroundSolidBlock.spawnParticles(worldIn, pos, () -> RedstoneParticleData.REDSTONE);
		}
	}
	
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(POWER);
	}
}