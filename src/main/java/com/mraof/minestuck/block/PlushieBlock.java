package com.mraof.minestuck.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class PlushieBlock extends HorizontalDirectionalBlock
{
	public static final MapCodec<PlushieBlock> CODEC = simpleCodec(PlushieBlock::new);
	protected static final CustomVoxelShape DEFAULT_SHAPE = new CustomVoxelShape(new double[]{0, 0, 0, 16, 16, 16});
	
	protected final Map<Direction, VoxelShape> shapes;
	
	protected PlushieBlock(Properties properties)
	{
		this(properties, DEFAULT_SHAPE.createRotatedShapes());
	}
	
	public PlushieBlock(Properties properties, Map<Direction, VoxelShape> shapes)
	{
		super(properties);
		this.shapes = shapes;
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
		
	}
	
	public static BlockBehaviour.Properties defaultProperties()
	{
		return Block.Properties.of().sound(SoundType.WOOL).instabreak().noOcclusion();
	}
	
	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return this.shapes.get(state.getValue(FACING));
	}
	
	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec()
	{
		return CODEC;
	}
	
	@Override
	public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx)
	{
		//~ FIXME: wrong direction being picked when placed at an angle
//		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
		return this.defaultBlockState().setValue(FACING, ctx.getClickedFace().getOpposite());
	}
	
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(FACING);
	}
}
