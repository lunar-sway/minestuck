package com.mraof.minestuck.block.plant;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GlowingMushroomVinesBlock extends Block
{
	protected static final VoxelShape SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D);
	
	public GlowingMushroomVinesBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand)
	{
		BlockPos belowPos = pos.below();
		if(rand.nextInt(90) == 0 && level.getBlockState(belowPos).isAir())
		{
			level.setBlock(belowPos, MSBlocks.GLOWING_MUSHROOM_VINES.get().defaultBlockState(), Block.UPDATE_ALL);
		}
	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
	{
		BlockState aboveState = level.getBlockState(pos.above());
		return aboveState.is(this) || aboveState.is(MSTags.Blocks.SHADEWOOD_LEAVES);
	}
	
	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext)
	{
		Vec3 vec3 = pState.getOffset(pLevel, pPos);
		return SHAPE.move(vec3.x, vec3.y, vec3.z);
	}
}