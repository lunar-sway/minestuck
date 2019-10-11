package com.mraof.minestuck.block;

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
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

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
	public PushReaction getPushReaction(BlockState state)
	{
		return PushReaction.BLOCK;
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}
	
	//Should probably find an utility class for the functions below
	public static Rotation rotationFromDirection(Direction direction)
	{
		Rotation rotation;
		switch(direction)
		{
			case EAST:
				rotation = Rotation.CLOCKWISE_90;
				break;
			case SOUTH:
				rotation = Rotation.CLOCKWISE_180;
				break;
			case WEST:
				rotation = Rotation.COUNTERCLOCKWISE_90;
				break;
			default:
				rotation = Rotation.NONE;
				break;
		}
		return rotation;
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