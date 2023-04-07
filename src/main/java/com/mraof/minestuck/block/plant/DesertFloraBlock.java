package com.mraof.minestuck.block.plant;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DesertFloraBlock extends BushBlock
{
	protected static final VoxelShape SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 13.0D, 13.0D);
	
	public DesertFloraBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos)
	{
		return state.is(BlockTags.SAND);
	}
	
	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext)
	{
		Vec3 vec3 = pState.getOffset(pLevel, pPos);
		return SHAPE.move(vec3.x, vec3.y, vec3.z);
	}
}