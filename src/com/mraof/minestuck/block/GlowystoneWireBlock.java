package com.mraof.minestuck.block;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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
import net.minecraft.util.BlockRenderLayer;
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

public class GlowystoneWireBlock extends Block
{
	public static final EnumProperty<RedstoneSide> NORTH = BlockStateProperties.REDSTONE_NORTH;
	public static final EnumProperty<RedstoneSide> EAST = BlockStateProperties.REDSTONE_EAST;
	public static final EnumProperty<RedstoneSide> SOUTH = BlockStateProperties.REDSTONE_SOUTH;
	public static final EnumProperty<RedstoneSide> WEST = BlockStateProperties.REDSTONE_WEST;
	
	public static final Map<Direction, EnumProperty<RedstoneSide>> FACING_PROPERTY_MAP = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, NORTH, Direction.EAST, EAST, Direction.SOUTH, SOUTH, Direction.WEST, WEST));
	protected static final VoxelShape[] SHAPES = new VoxelShape[]{Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D), Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D), Block.makeCuboidShape(0.0D, 0.0D, 3.0D, 13.0D, 1.0D, 16.0D), Block.makeCuboidShape(3.0D, 0.0D, 0.0D, 13.0D, 1.0D, 13.0D), Block.makeCuboidShape(3.0D, 0.0D, 0.0D, 13.0D, 1.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 13.0D, 1.0D, 13.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 13.0D, 1.0D, 16.0D), Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 16.0D, 1.0D, 13.0D), Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 16.0D, 1.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 3.0D, 16.0D, 1.0D, 13.0D), Block.makeCuboidShape(0.0D, 0.0D, 3.0D, 16.0D, 1.0D, 16.0D), Block.makeCuboidShape(3.0D, 0.0D, 0.0D, 16.0D, 1.0D, 13.0D), Block.makeCuboidShape(3.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 13.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D)};
	
	/** List of blocks to update with glowystone. */
	private final Set<BlockPos> blocksNeedingUpdate = Sets.newHashSet();
	
	public GlowystoneWireBlock(Properties properties)
	{
		super(properties);
		this.setDefaultState(getDefaultState().with(NORTH, RedstoneSide.NONE).with(EAST, RedstoneSide.NONE).with(SOUTH, RedstoneSide.NONE).with(WEST, RedstoneSide.NONE));
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPES[getAABBIndex(state)];
	}
	
	private static int getAABBIndex(BlockState state)
	{
		int i = 0;
		boolean flag = state.get(NORTH) != RedstoneSide.NONE;
		boolean flag1 = state.get(EAST) != RedstoneSide.NONE;
		boolean flag2 = state.get(SOUTH) != RedstoneSide.NONE;
		boolean flag3 = state.get(WEST) != RedstoneSide.NONE;
		if (flag || flag2 && !flag && !flag1 && !flag3)
		{
			i |= 1 << Direction.NORTH.getHorizontalIndex();
		}
		
		if (flag1 || flag3 && !flag && !flag1 && !flag2)
		{
			i |= 1 << Direction.EAST.getHorizontalIndex();
		}
		
		if (flag2 || flag && !flag1 && !flag2 && !flag3)
		{
			i |= 1 << Direction.SOUTH.getHorizontalIndex();
		}
		
		if (flag3 || flag1 && !flag && !flag2 && !flag3)
		{
			i |= 1 << Direction.WEST.getHorizontalIndex();
		}
		
		return i;
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		IBlockReader iblockreader = context.getWorld();
		BlockPos blockpos = context.getPos();
		return this.getDefaultState().with(WEST, this.getSide(iblockreader, blockpos, Direction.WEST)).with(EAST, this.getSide(iblockreader, blockpos, Direction.EAST)).with(NORTH, this.getSide(iblockreader, blockpos, Direction.NORTH)).with(SOUTH, this.getSide(iblockreader, blockpos, Direction.SOUTH));
	}
	
	
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
	{
		if(facing == Direction.DOWN)
		{
			return stateIn;
		} else
		{
			return facing == Direction.UP ? stateIn.with(WEST, this.getSide(worldIn, currentPos, Direction.WEST)).with(EAST, this.getSide(worldIn, currentPos, Direction.EAST)).with(NORTH, this.getSide(worldIn, currentPos, Direction.NORTH)).with(SOUTH, this.getSide(worldIn, currentPos, Direction.SOUTH)) : stateIn.with(FACING_PROPERTY_MAP.get(facing), this.getSide(worldIn, currentPos, facing));
		}
	}
	
	private RedstoneSide getSide(IBlockReader worldIn, BlockPos pos, Direction face)
	{
		BlockPos blockpos = pos.offset(face);
		BlockState blockstate = worldIn.getBlockState(blockpos);
		BlockPos blockpos1 = pos.up();
		BlockState blockstate1 = worldIn.getBlockState(blockpos1);
		if (!blockstate1.isNormalCube(worldIn, blockpos1)) {
			boolean flag = Block.hasSolidSide(blockstate, worldIn, blockpos, Direction.UP);
			if (flag && canConnectTo(worldIn.getBlockState(blockpos.up()))) {
				if (isOpaque(blockstate.getCollisionShape(worldIn, blockpos))) {
					return RedstoneSide.UP;
				}
				
				return RedstoneSide.SIDE;
			}
		}
		
		return !canConnectTo(worldIn.getBlockState(blockpos)) && (blockstate.isNormalCube(worldIn, blockpos) || !canConnectTo(worldIn.getBlockState(blockpos.down()))) ? RedstoneSide.NONE : RedstoneSide.SIDE;
	}
	
	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
	{
		BlockPos blockpos = pos.down();
		BlockState blockstate = worldIn.getBlockState(blockpos);
		return Block.hasSolidSide(blockstate, worldIn, blockpos, Direction.UP);
	}
	
	private BlockState updateSurroundingGlowystone(World worldIn, BlockPos pos, BlockState state)
	{
		List<BlockPos> list = Lists.newArrayList(this.blocksNeedingUpdate);
		this.blocksNeedingUpdate.clear();
		
		for(BlockPos blockpos : list) {
			worldIn.notifyNeighborsOfStateChange(blockpos, this);
		}
		
		return state;
	}
	
	private void notifyWireNeighborsOfStateChange(World worldIn, BlockPos pos)
	{
		if (worldIn.getBlockState(pos).getBlock() == this)
		{
			worldIn.notifyNeighborsOfStateChange(pos, this);

			for (Direction direction : Direction.values())
			{
				worldIn.notifyNeighborsOfStateChange(pos.offset(direction), this);
			}
		}
	}
	
	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		if (!worldIn.isRemote)
		{
			this.updateSurroundingGlowystone(worldIn, pos, state);

			for (Direction direction : Direction.Plane.VERTICAL)
			{
				worldIn.notifyNeighborsOfStateChange(pos.offset(direction), this);
			}

			for (Direction direction : Direction.Plane.HORIZONTAL)
			{
				this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(direction));
			}

			for (Direction direction : Direction.Plane.HORIZONTAL)
			{
				BlockPos blockpos = pos.offset(direction);

				if (worldIn.getBlockState(blockpos).isNormalCube(worldIn, blockpos))
				{
					this.notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
				}
				else
				{
					this.notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
				}
			}
		}
	}
	
	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		super.onReplaced(state, worldIn, pos, newState, isMoving);

		if (!worldIn.isRemote)
		{
			for (Direction direction : Direction.values())
			{
				worldIn.notifyNeighborsOfStateChange(pos.offset(direction), this);
			}

			this.updateSurroundingGlowystone(worldIn, pos, state);

			for (Direction direction : Direction.Plane.HORIZONTAL)
			{
				this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(direction));
			}

			for (Direction direction : Direction.Plane.HORIZONTAL)
			{
				BlockPos blockpos = pos.offset(direction);

				if (worldIn.getBlockState(blockpos).isNormalCube(worldIn, blockpos))
				{
					this.notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
				}
				else
				{
					this.notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
				}
			}
		}
	}
	
	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		if (!worldIn.isRemote)
		{
			if (this.isValidPosition(state, worldIn, pos))
			{
				this.updateSurroundingGlowystone(worldIn, pos, state);
			}
			else
			{
				spawnDrops(state, worldIn, pos);
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
				return state.with(NORTH, state.get(SOUTH)).with(EAST, state.get(WEST)).with(SOUTH, state.get(NORTH)).with(WEST, state.get(EAST));
			case COUNTERCLOCKWISE_90:
				return state.with(NORTH, state.get(EAST)).with(EAST, state.get(SOUTH)).with(SOUTH, state.get(WEST)).with(WEST, state.get(NORTH));
			case CLOCKWISE_90:
				return state.with(NORTH, state.get(WEST)).with(EAST, state.get(NORTH)).with(SOUTH, state.get(EAST)).with(WEST, state.get(SOUTH));
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
				return state.with(NORTH, state.get(SOUTH)).with(SOUTH, state.get(NORTH));
			case FRONT_BACK:
				return state.with(EAST, state.get(WEST)).with(WEST, state.get(EAST));
			default:
				return super.mirror(state, mirrorIn);
		}
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(NORTH, EAST, SOUTH, WEST);
	}
	
	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}
}