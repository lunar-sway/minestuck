package com.mraof.minestuck.block;

import com.google.common.collect.ImmutableMap;
import com.mraof.minestuck.util.CustomVoxelShape;
import com.mraof.minestuck.util.Debug;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class PipeBlock extends MSDirectionalBlock
{
	public final ImmutableMap<Direction, VoxelShape> shape;
	
	public PipeBlock(Properties properties, CustomVoxelShape shape)
	{
		super(properties);
		this.shape = shape.createRotatedShapesAllDirections();
		this.setDefaultState(stateContainer.getBaseState().with(FACING, Direction.DOWN));
	}
	
	@Override
	public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction)
	{
		return state.with(FACING, direction.rotate(state.get(FACING)));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return shape.get(state.get(FACING));
	}
	
	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
	{
		super.onEntityCollision(state, worldIn, pos, entityIn);
		/*if(state.get(POWERED)){
			//causes error
		}*/
		int directionX = state.get(FACING).getDirectionVec().getX();
		int directionY = state.get(FACING).getDirectionVec().getY();
		int directionZ = state.get(FACING).getDirectionVec().getZ();
		//make particle
		entityIn.setMotion(directionX*.5, directionY*.5, directionZ*.5);
		Debug.debugf("%s is going %s", entityIn, entityIn.getMotion());
	}
}