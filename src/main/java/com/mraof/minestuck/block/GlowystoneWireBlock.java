package com.mraof.minestuck.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.RedstoneSide;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.Random;
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
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
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
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		IWorld world = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		return this.defaultBlockState().setValue(WEST, this.getSide(world, blockpos, Direction.WEST)).setValue(EAST, this.getSide(world, blockpos, Direction.EAST)).setValue(NORTH, this.getSide(world, blockpos, Direction.NORTH)).setValue(SOUTH, this.getSide(world, blockpos, Direction.SOUTH));
	}
	
	
	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
	{
		if(facing == Direction.DOWN)
		{
			return stateIn;
		} else
		{
			return facing == Direction.UP ? stateIn.setValue(WEST, this.getSide(worldIn, currentPos, Direction.WEST)).setValue(EAST, this.getSide(worldIn, currentPos, Direction.EAST)).setValue(NORTH, this.getSide(worldIn, currentPos, Direction.NORTH)).setValue(SOUTH, this.getSide(worldIn, currentPos, Direction.SOUTH)) : stateIn.setValue(FACING_PROPERTY_MAP.get(facing), this.getSide(worldIn, currentPos, facing));
		}
	}
	
	private RedstoneSide getSide(IWorldReader worldIn, BlockPos pos, Direction face)
	{
		BlockPos blockpos = pos.relative(face);
		BlockState blockstate = worldIn.getBlockState(blockpos);
		BlockPos blockpos1 = pos.above();
		BlockState blockstate1 = worldIn.getBlockState(blockpos1);
		if (!blockstate1.isRedstoneConductor(worldIn, blockpos1)) {
			boolean flag = Block.canSupportCenter(worldIn, blockpos, Direction.UP);
			if (flag && canConnectTo(worldIn.getBlockState(blockpos.above()))) {
				if (isShapeFullBlock(blockstate.getBlockSupportShape(worldIn, blockpos))) {
					return RedstoneSide.UP;
				}
				
				return RedstoneSide.SIDE;
			}
		}
		
		return !canConnectTo(worldIn.getBlockState(blockpos)) && (blockstate.isRedstoneConductor(worldIn, blockpos) || !canConnectTo(worldIn.getBlockState(blockpos.below()))) ? RedstoneSide.NONE : RedstoneSide.SIDE;
	}
	
	@Override
	public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos)
	{
		BlockPos blockpos = pos.below();
		BlockState blockstate = worldIn.getBlockState(blockpos);
		return Block.canSupportCenter(worldIn, blockpos, Direction.UP);
	}
	
	private BlockState updateSurroundingGlowystone(World worldIn, BlockPos pos, BlockState state)
	{
		List<BlockPos> list = Lists.newArrayList(this.blocksNeedingUpdate);
		this.blocksNeedingUpdate.clear();
		
		for(BlockPos blockpos : list) {
			worldIn.updateNeighborsAt(blockpos, this);
		}
		
		return state;
	}
	
	private void notifyWireNeighborsOfStateChange(World worldIn, BlockPos pos)
	{
		if (worldIn.getBlockState(pos).getBlock() == this)
		{
			worldIn.updateNeighborsAt(pos, this);

			for (Direction direction : Direction.values())
			{
				worldIn.updateNeighborsAt(pos.relative(direction), this);
			}
		}
	}
	
	@Override
	public void onPlace(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		if (!worldIn.isClientSide)
		{
			this.updateSurroundingGlowystone(worldIn, pos, state);

			for (Direction direction : Direction.Plane.VERTICAL)
			{
				worldIn.updateNeighborsAt(pos.relative(direction), this);
			}

			for (Direction direction : Direction.Plane.HORIZONTAL)
			{
				this.notifyWireNeighborsOfStateChange(worldIn, pos.relative(direction));
			}

			for (Direction direction : Direction.Plane.HORIZONTAL)
			{
				BlockPos blockpos = pos.relative(direction);

				if (worldIn.getBlockState(blockpos).isRedstoneConductor(worldIn, blockpos))
				{
					this.notifyWireNeighborsOfStateChange(worldIn, blockpos.above());
				}
				else
				{
					this.notifyWireNeighborsOfStateChange(worldIn, blockpos.below());
				}
			}
		}
	}
	
	@Override
	public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		super.onRemove(state, worldIn, pos, newState, isMoving);

		if (!worldIn.isClientSide)
		{
			for (Direction direction : Direction.values())
			{
				worldIn.updateNeighborsAt(pos.relative(direction), this);
			}

			this.updateSurroundingGlowystone(worldIn, pos, state);

			for (Direction direction : Direction.Plane.HORIZONTAL)
			{
				this.notifyWireNeighborsOfStateChange(worldIn, pos.relative(direction));
			}

			for (Direction direction : Direction.Plane.HORIZONTAL)
			{
				BlockPos blockpos = pos.relative(direction);

				if (worldIn.getBlockState(blockpos).isRedstoneConductor(worldIn, blockpos))
				{
					this.notifyWireNeighborsOfStateChange(worldIn, blockpos.above());
				}
				else
				{
					this.notifyWireNeighborsOfStateChange(worldIn, blockpos.below());
				}
			}
		}
	}
	
	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		if (!worldIn.isClientSide)
		{
			if (this.canSurvive(state, worldIn, pos))
			{
				this.updateSurroundingGlowystone(worldIn, pos, state);
			}
			else
			{
				dropResources(state, worldIn, pos);
				worldIn.removeBlock(pos, false);
			}
		}
	}
	
	protected static boolean canConnectTo(BlockState blockState)
	{
		return blockState.getBlock() == MSBlocks.GLOWYSTONE_DUST;
	}
	
	public static int colorMultiplier()
	{
		float f = 1.0F;
		float f1 = 1.0F;

		float f2 = 0.2F;
		float f3 = 0.0F;

		int i = MathHelper.clamp((int)(f1 * 255.0F), 0, 255);
		int j = MathHelper.clamp((int)(f2 * 255.0F), 0, 255);
		int k = MathHelper.clamp((int)(f3 * 255.0F), 0, 255);
		return -16777216 | i << 16 | j << 8 | k;
	}
	
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		double d0 = (double)pos.getX() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
		double d1 = (double)((float)pos.getY() + 0.0625F);
		double d2 = (double)pos.getZ() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
		
		worldIn.addParticle(new RedstoneParticleData(1F, 0.8F, 0F, 1F), d0, d1, d2, 0, 0, 0);
	}
	
	@Override
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
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(NORTH, EAST, SOUTH, WEST);
	}
}