package com.mraof.minestuck.block.machine;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mraof.minestuck.util.CustomVoxelShape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.PushReaction;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

public abstract class MachineBlock extends Block
{
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	
	public MachineBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(FACING);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public PushReaction getPushReaction(BlockState state)
	{
		return PushReaction.BLOCK;
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());	//Should face the player
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public BlockState rotate(BlockState state, Rotation rotation)
	{
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public BlockState mirror(BlockState state, Mirror mirrorIn)
	{
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}
	
	public static Map<Direction, VoxelShape> createRotatedShapes(CustomVoxelShape shape)
	{
		return Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, shape.create(Direction.NORTH), Direction.SOUTH, shape.create(Direction.SOUTH), Direction.WEST, shape.create(Direction.WEST), Direction.EAST, shape.create(Direction.EAST)));
	}
	
	public static Map<Direction, VoxelShape> createRotatedShapes(double x1, double y1, double z1, double x2, double y2, double z2)
	{
		return Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.makeCuboidShape(x1, y1, z1, x2, y2, z2), Direction.SOUTH, Block.makeCuboidShape(16 - x2, y1, 16 - z2, 16 - x1, y2, 16 - z1),
				Direction.WEST, Block.makeCuboidShape(z1, y1, 16 - x2, z2, y2, 16 - x1), Direction.EAST, Block.makeCuboidShape(16 - z2, y1, x1, 16 - z1, y2, x2)));
	}
	
	public static <K extends Enum<K>, V> Map<K, V> createEnumMapping(Class<K> c, K[] keys, Function<K, V> function)
	{
		EnumMap<K, V> map = Maps.newEnumMap(c);
		for(K k : keys)
		{
			map.put(k, function.apply(k));
		}
		return Maps.immutableEnumMap(map);
	}
}