package com.mraof.minestuck.block.plant;

import com.mojang.serialization.MapCodec;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WoodenFloraBlock extends BushBlock
{
	public static final VoxelShape GRASS_SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	public static final VoxelShape FLOWER_SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D);
	
	private final VoxelShape shape;
	public WoodenFloraBlock(Properties properties, VoxelShape shape)
	{
		super(properties);
		this.shape = shape;
	}
	
	@Override
	protected MapCodec<WoodenFloraBlock> codec()
	{
		return null; //todo
	}
	
	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos)
	{
		return state.is(BlockTags.PLANKS) || state.is(MSTags.Blocks.WOOD_TERRAIN_BLOCKS);
	}
	
	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext)
	{
		Vec3 vec3 = pState.getOffset(pLevel, pPos);
		return shape.move(vec3.x, vec3.y, vec3.z);
	}
}