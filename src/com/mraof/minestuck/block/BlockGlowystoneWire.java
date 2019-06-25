package com.mraof.minestuck.block;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mraof.minestuck.item.MinestuckItems;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.RedstoneSide;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockGlowystoneWire extends Block
{
	public static final EnumProperty<RedstoneSide> NORTH = BlockStateProperties.REDSTONE_NORTH;
	public static final EnumProperty<RedstoneSide> EAST = BlockStateProperties.REDSTONE_EAST;
	public static final EnumProperty<RedstoneSide> SOUTH = BlockStateProperties.REDSTONE_SOUTH;
	public static final EnumProperty<RedstoneSide> WEST = BlockStateProperties.REDSTONE_WEST;
	
	public static final Map<EnumFacing, EnumProperty<RedstoneSide>> FACING_PROPERTY_MAP = Maps.newEnumMap(ImmutableMap.of(EnumFacing.NORTH, NORTH, EnumFacing.EAST, EAST, EnumFacing.SOUTH, SOUTH, EnumFacing.WEST, WEST));
	protected static final VoxelShape[] SHAPES = new VoxelShape[]{Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D), Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D), Block.makeCuboidShape(0.0D, 0.0D, 3.0D, 13.0D, 1.0D, 16.0D), Block.makeCuboidShape(3.0D, 0.0D, 0.0D, 13.0D, 1.0D, 13.0D), Block.makeCuboidShape(3.0D, 0.0D, 0.0D, 13.0D, 1.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 13.0D, 1.0D, 13.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 13.0D, 1.0D, 16.0D), Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 16.0D, 1.0D, 13.0D), Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 16.0D, 1.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 3.0D, 16.0D, 1.0D, 13.0D), Block.makeCuboidShape(0.0D, 0.0D, 3.0D, 16.0D, 1.0D, 16.0D), Block.makeCuboidShape(3.0D, 0.0D, 0.0D, 16.0D, 1.0D, 13.0D), Block.makeCuboidShape(3.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 13.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D)};
	
	/** List of blocks to update with glowystone. */
	private final Set<BlockPos> blocksNeedingUpdate = Sets.newHashSet();
	
	public BlockGlowystoneWire(Properties properties)
	{
		super(properties);
		this.setDefaultState(getDefaultState().with(NORTH, RedstoneSide.NONE).with(EAST, RedstoneSide.NONE).with(SOUTH, RedstoneSide.NONE).with(WEST, RedstoneSide.NONE));
	}
	
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return SHAPES[getAABBIndex(state)];
	}
	
	private static int getAABBIndex(IBlockState state)
	{
		int i = 0;
		boolean flag = state.get(NORTH) != RedstoneSide.NONE;
		boolean flag1 = state.get(EAST) != RedstoneSide.NONE;
		boolean flag2 = state.get(SOUTH) != RedstoneSide.NONE;
		boolean flag3 = state.get(WEST) != RedstoneSide.NONE;
		if (flag || flag2 && !flag && !flag1 && !flag3)
		{
			i |= 1 << EnumFacing.NORTH.getHorizontalIndex();
		}
		
		if (flag1 || flag3 && !flag && !flag1 && !flag2)
		{
			i |= 1 << EnumFacing.EAST.getHorizontalIndex();
		}
		
		if (flag2 || flag && !flag1 && !flag2 && !flag3)
		{
			i |= 1 << EnumFacing.SOUTH.getHorizontalIndex();
		}
		
		if (flag3 || flag1 && !flag && !flag2 && !flag3)
		{
			i |= 1 << EnumFacing.WEST.getHorizontalIndex();
		}
		
		return i;
	}
	
	@Override
	public IBlockState getStateForPlacement(BlockItemUseContext context)
	{
		IBlockReader iblockreader = context.getWorld();
		BlockPos blockpos = context.getPos();
		return this.getDefaultState().with(WEST, this.getSide(iblockreader, blockpos, EnumFacing.WEST)).with(EAST, this.getSide(iblockreader, blockpos, EnumFacing.EAST)).with(NORTH, this.getSide(iblockreader, blockpos, EnumFacing.NORTH)).with(SOUTH, this.getSide(iblockreader, blockpos, EnumFacing.SOUTH));
	}
	
	@Override
	public IBlockState updatePostPlacement(IBlockState stateIn, EnumFacing facing, IBlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
	{
		if(facing == EnumFacing.DOWN)
		{
			return stateIn;
		} else
		{
			return facing == EnumFacing.UP ? stateIn.with(WEST, this.getSide(worldIn, currentPos, EnumFacing.WEST)).with(EAST, this.getSide(worldIn, currentPos, EnumFacing.EAST)).with(NORTH, this.getSide(worldIn, currentPos, EnumFacing.NORTH)).with(SOUTH, this.getSide(worldIn, currentPos, EnumFacing.SOUTH)) : stateIn.with(FACING_PROPERTY_MAP.get(facing), this.getSide(worldIn, currentPos, facing));
		}
	}
	
	private RedstoneSide getSide(IBlockReader worldIn, BlockPos pos, EnumFacing face) {
		BlockPos blockpos = pos.offset(face);
		IBlockState iblockstate = worldIn.getBlockState(pos.offset(face));
		IBlockState iblockstate1 = worldIn.getBlockState(pos.up());
		if (!iblockstate1.isNormalCube()) {
			boolean flag = iblockstate.getBlockFaceShape(worldIn, blockpos, EnumFacing.UP) == BlockFaceShape.SOLID;
			if (flag && canConnectTo(worldIn.getBlockState(blockpos.up()))) {
				if (iblockstate.isBlockNormalCube()) {
					return RedstoneSide.UP;
				}
				
				return RedstoneSide.SIDE;
			}
		}
		
		return !canConnectTo(worldIn.getBlockState(blockpos)) && (iblockstate.isNormalCube() || !canConnectTo(worldIn.getBlockState(blockpos.down()))) ? RedstoneSide.NONE : RedstoneSide.SIDE;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	public boolean isValidPosition(IBlockState state, IWorldReaderBase worldIn, BlockPos pos) {
		IBlockState iblockstate = worldIn.getBlockState(pos.down());
		return iblockstate.isTopSolid() || iblockstate.getBlockFaceShape(worldIn, pos.down(), EnumFacing.UP) == BlockFaceShape.SOLID;
	}
	
	

	private IBlockState updateSurroundingGlowystone(World worldIn, BlockPos pos, IBlockState state)
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

			for (EnumFacing enumfacing : EnumFacing.values())
			{
				worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
			}
		}
	}

	/**
	 * Called after the block is set in the Chunk data, but before the Tile Entity is set
	 */
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!worldIn.isRemote)
		{
			this.updateSurroundingGlowystone(worldIn, pos, state);

			for (EnumFacing enumfacing : EnumFacing.Plane.VERTICAL)
			{
				worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
			}

			for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL)
			{
				this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(enumfacing1));
			}

			for (EnumFacing enumfacing2 : EnumFacing.Plane.HORIZONTAL)
			{
				BlockPos blockpos = pos.offset(enumfacing2);

				if (worldIn.getBlockState(blockpos).isNormalCube())
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
	public void onReplaced(IBlockState state, World worldIn, BlockPos pos, IBlockState newState, boolean isMoving)
	{
		super.onReplaced(state, worldIn, pos, newState, isMoving);

		if (!worldIn.isRemote)
		{
			for (EnumFacing enumfacing : EnumFacing.values())
			{
				worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
			}

			this.updateSurroundingGlowystone(worldIn, pos, state);

			for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL)
			{
				this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(enumfacing1));
			}

			for (EnumFacing enumfacing2 : EnumFacing.Plane.HORIZONTAL)
			{
				BlockPos blockpos = pos.offset(enumfacing2);

				if (worldIn.getBlockState(blockpos).isNormalCube())
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
	
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		if (!worldIn.isRemote)
		{
			if (this.isValidPosition(state, worldIn, pos))
			{
				this.updateSurroundingGlowystone(worldIn, pos, state);
			}
			else
			{
				state.dropBlockAsItem(worldIn, pos, 0);
				worldIn.removeBlock(pos);
			}
		}
	}
	
	protected static boolean canConnectTo(IBlockState blockState)
	{
		return blockState.getBlock() == MinestuckBlocks.GLOWYSTONE_WIRE;
	}

	@OnlyIn(Dist.CLIENT)
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
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		double d0 = (double)pos.getX() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
		double d1 = (double)((float)pos.getY() + 0.0625F);
		double d2 = (double)pos.getZ() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
			
		worldIn.spawnParticle(new RedstoneParticleData(1F, 0.8F, 0F, 1F), d0, d1, d2, 0, 0, 0);
	}
	
	public IBlockState rotate(IBlockState state, Rotation rot)
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
	
	public IBlockState mirror(IBlockState state, Mirror mirrorIn)
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
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder)
	{
		builder.add(NORTH, EAST, SOUTH, WEST);
	}
	
	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return BlockFaceShape.UNDEFINED;
	}
}