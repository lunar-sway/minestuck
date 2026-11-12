package com.mraof.minestuck.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class PlushieWallBlock extends PlushieBlock
{
	public static final MapCodec<PlushieWallBlock> CODEC = simpleCodec(PlushieWallBlock::new);
	
	protected PlushieWallBlock(Properties properties)
	{
		super(properties);
	}
	
	public PlushieWallBlock(Properties properties, Map<Direction, VoxelShape> shapes)
	{
		super(properties, shapes);
	}
	
	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return this.shapes.get(state.getValue(FACING));
	}
	
	@Override
	public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx)
	{
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}
	
	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec()
	{
		return CODEC;
	}
	
	public VoxelShape getShape(BlockState state) {
		return shapes.get(state.getValue(FACING));
	}
	
}
