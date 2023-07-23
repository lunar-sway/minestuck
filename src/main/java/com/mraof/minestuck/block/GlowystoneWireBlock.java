package com.mraof.minestuck.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class GlowystoneWireBlock extends Block
{
	public static final EnumProperty<RedstoneSide> NORTH = BlockStateProperties.NORTH_REDSTONE;
	public static final EnumProperty<RedstoneSide> EAST = BlockStateProperties.EAST_REDSTONE;
	public static final EnumProperty<RedstoneSide> SOUTH = BlockStateProperties.SOUTH_REDSTONE;
	public static final EnumProperty<RedstoneSide> WEST = BlockStateProperties.WEST_REDSTONE;
	
	public static final Map<Direction, EnumProperty<RedstoneSide>> FACING_PROPERTY_MAP = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, NORTH, Direction.EAST, EAST, Direction.SOUTH, SOUTH, Direction.WEST, WEST));
	protected static final VoxelShape[] SHAPES = new VoxelShape[]{Block.box(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D), Block.box(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 16.0D), Block.box(0.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D), Block.box(0.0D, 0.0D, 3.0D, 13.0D, 1.0D, 16.0D), Block.box(3.0D, 0.0D, 0.0D, 13.0D, 1.0D, 13.0D), Block.box(3.0D, 0.0D, 0.0D, 13.0D, 1.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 13.0D, 1.0D, 13.0D), Block.box(0.0D, 0.0D, 0.0D, 13.0D, 1.0D, 16.0D), Block.box(3.0D, 0.0D, 3.0D, 16.0D, 1.0D, 13.0D), Block.box(3.0D, 0.0D, 3.0D, 16.0D, 1.0D, 16.0D), Block.box(0.0D, 0.0D, 3.0D, 16.0D, 1.0D, 13.0D), Block.box(0.0D, 0.0D, 3.0D, 16.0D, 1.0D, 16.0D), Block.box(3.0D, 0.0D, 0.0D, 16.0D, 1.0D, 13.0D), Block.box(3.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 13.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D)};
	
	/** List of blocks to update with glowystone. */
	private final Set<BlockPos> blocksNeedingUpdate = Sets.newHashSet();
	
	public GlowystoneWireBlock(Properties properties)
	{
		super(properties);
		this.registerDefaultState(defaultBlockState().setValue(NORTH, RedstoneSide.NONE).setValue(EAST, RedstoneSide.NONE).setValue(SOUTH, RedstoneSide.NONE).setValue(WEST, RedstoneSide.NONE));
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return SHAPES[getAABBIndex(state)];
	}
	
	private static int getAABBIndex(BlockState state)
	{
		int i = 0;
		boolean flag = state.getValue(NORTH) != RedstoneSide.NONE;
		boolean flag1 = state.getValue(EAST) != RedstoneSide.NONE;
		boolean flag2 = state.getValue(SOUTH) != RedstoneSide.NONE;
		boolean flag3 = state.getValue(WEST) != RedstoneSide.NONE;
		if (flag || flag2 && !flag && !flag1 && !flag3)
		{
			i |= 1 << Direction.NORTH.get2DDataValue();
		}
		
		if (flag1 || flag3 && !flag && !flag1 && !flag2)
		{
			i |= 1 << Direction.EAST.get2DDataValue();
		}
		
		if (flag2 || flag && !flag1 && !flag2 && !flag3)
		{
			i |= 1 << Direction.SOUTH.get2DDataValue();
		}
		
		if (flag3 || flag1 && !flag && !flag2 && !flag3)
		{
			i |= 1 << Direction.WEST.get2DDataValue();
		}
		
		return i;
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		LevelAccessor level = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		return this.defaultBlockState().setValue(WEST, this.getSide(level, blockpos, Direction.WEST)).setValue(EAST, this.getSide(level, blockpos, Direction.EAST)).setValue(NORTH, this.getSide(level, blockpos, Direction.NORTH)).setValue(SOUTH, this.getSide(level, blockpos, Direction.SOUTH));
	}
	
	
	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos)
	{
		if(facing == Direction.DOWN)
		{
			return stateIn;
		} else
		{
			return facing == Direction.UP ? stateIn.setValue(WEST, this.getSide(level, currentPos, Direction.WEST)).setValue(EAST, this.getSide(level, currentPos, Direction.EAST)).setValue(NORTH, this.getSide(level, currentPos, Direction.NORTH)).setValue(SOUTH, this.getSide(level, currentPos, Direction.SOUTH)) : stateIn.setValue(FACING_PROPERTY_MAP.get(facing), this.getSide(level, currentPos, facing));
		}
	}
	
	private RedstoneSide getSide(LevelReader level, BlockPos pos, Direction face)
	{
		BlockPos blockpos = pos.relative(face);
		BlockState blockstate = level.getBlockState(blockpos);
		BlockPos blockpos1 = pos.above();
		BlockState blockstate1 = level.getBlockState(blockpos1);
		if (!blockstate1.isRedstoneConductor(level, blockpos1)) {
			boolean flag = Block.canSupportCenter(level, blockpos, Direction.UP);
			if (flag && canConnectTo(level.getBlockState(blockpos.above()))) {
				if (isShapeFullBlock(blockstate.getBlockSupportShape(level, blockpos))) {
					return RedstoneSide.UP;
				}
				
				return RedstoneSide.SIDE;
			}
		}
		
		return !canConnectTo(level.getBlockState(blockpos)) && (blockstate.isRedstoneConductor(level, blockpos) || !canConnectTo(level.getBlockState(blockpos.below()))) ? RedstoneSide.NONE : RedstoneSide.SIDE;
	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
	{
		BlockPos blockpos = pos.below();
		BlockState blockstate = level.getBlockState(blockpos);
		return Block.canSupportCenter(level, blockpos, Direction.UP);
	}
	
	private BlockState updateSurroundingGlowystone(Level level, BlockPos pos, BlockState state)
	{
		List<BlockPos> list = Lists.newArrayList(this.blocksNeedingUpdate);
		this.blocksNeedingUpdate.clear();
		
		for(BlockPos blockpos : list) {
			level.updateNeighborsAt(blockpos, this);
		}
		
		return state;
	}
	
	private void notifyWireNeighborsOfStateChange(Level level, BlockPos pos)
	{
		if (level.getBlockState(pos).is(this))
		{
			level.updateNeighborsAt(pos, this);

			for (Direction direction : Direction.values())
			{
				level.updateNeighborsAt(pos.relative(direction), this);
			}
		}
	}
	
	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		if (!level.isClientSide)
		{
			this.updateSurroundingGlowystone(level, pos, state);

			for (Direction direction : Direction.Plane.VERTICAL)
			{
				level.updateNeighborsAt(pos.relative(direction), this);
			}

			for (Direction direction : Direction.Plane.HORIZONTAL)
			{
				this.notifyWireNeighborsOfStateChange(level, pos.relative(direction));
			}

			for (Direction direction : Direction.Plane.HORIZONTAL)
			{
				BlockPos blockpos = pos.relative(direction);

				if (level.getBlockState(blockpos).isRedstoneConductor(level, blockpos))
				{
					this.notifyWireNeighborsOfStateChange(level, blockpos.above());
				}
				else
				{
					this.notifyWireNeighborsOfStateChange(level, blockpos.below());
				}
			}
		}
	}
	
	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
	{
		super.onRemove(state, level, pos, newState, isMoving);

		if (!level.isClientSide)
		{
			for (Direction direction : Direction.values())
			{
				level.updateNeighborsAt(pos.relative(direction), this);
			}

			this.updateSurroundingGlowystone(level, pos, state);

			for (Direction direction : Direction.Plane.HORIZONTAL)
			{
				this.notifyWireNeighborsOfStateChange(level, pos.relative(direction));
			}

			for (Direction direction : Direction.Plane.HORIZONTAL)
			{
				BlockPos blockpos = pos.relative(direction);

				if (level.getBlockState(blockpos).isRedstoneConductor(level, blockpos))
				{
					this.notifyWireNeighborsOfStateChange(level, blockpos.above());
				}
				else
				{
					this.notifyWireNeighborsOfStateChange(level, blockpos.below());
				}
			}
		}
	}
	
	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		if (!level.isClientSide)
		{
			if (this.canSurvive(state, level, pos))
			{
				this.updateSurroundingGlowystone(level, pos, state);
			}
			else
			{
				dropResources(state, level, pos);
				level.removeBlock(pos, false);
			}
		}
	}
	
	protected boolean canConnectTo(BlockState blockState)
	{
		return blockState.is(this);
	}
	
	public static int colorMultiplier()
	{
		float f = 1.0F;
		float f1 = 1.0F;

		float f2 = 0.2F;
		float f3 = 0.0F;

		int i = Mth.clamp((int)(f1 * 255.0F), 0, 255);
		int j = Mth.clamp((int)(f2 * 255.0F), 0, 255);
		int k = Mth.clamp((int)(f3 * 255.0F), 0, 255);
		return -16777216 | i << 16 | j << 8 | k;
	}
	
	@Override
	public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand)
	{
		double x = (double)pos.getX() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
		double y = pos.getY() + 0.0625;
		double z = (double)pos.getZ() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
		
		level.addParticle(new DustParticleOptions(new Vector3f(1F, 0.8F, 0F), 1F), x, y, z, 0, 0, 0);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public BlockState rotate(BlockState state, Rotation rot)
	{
		switch(rot)
		{
			case CLOCKWISE_180:
				return state.setValue(NORTH, state.getValue(SOUTH)).setValue(EAST, state.getValue(WEST)).setValue(SOUTH, state.getValue(NORTH)).setValue(WEST, state.getValue(EAST));
			case COUNTERCLOCKWISE_90:
				return state.setValue(NORTH, state.getValue(EAST)).setValue(EAST, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(WEST)).setValue(WEST, state.getValue(NORTH));
			case CLOCKWISE_90:
				return state.setValue(NORTH, state.getValue(WEST)).setValue(EAST, state.getValue(NORTH)).setValue(SOUTH, state.getValue(EAST)).setValue(WEST, state.getValue(SOUTH));
			default:
				return state;
		}
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public BlockState mirror(BlockState state, Mirror mirrorIn)
	{
		switch(mirrorIn)
		{
			case LEFT_RIGHT:
				return state.setValue(NORTH, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(NORTH));
			case FRONT_BACK:
				return state.setValue(EAST, state.getValue(WEST)).setValue(WEST, state.getValue(EAST));
			default:
				return super.mirror(state, mirrorIn);
		}
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(NORTH, EAST, SOUTH, WEST);
	}
}