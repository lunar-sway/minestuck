package com.mraof.minestuck.block;

import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mraof.minestuck.item.MinestuckItems;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGlowystoneWire extends Block
{
	public static final PropertyEnum<BlockGlowystoneWire.EnumAttachPosition> NORTH = PropertyEnum.<BlockGlowystoneWire.EnumAttachPosition>create("north", BlockGlowystoneWire.EnumAttachPosition.class);
	public static final PropertyEnum<BlockGlowystoneWire.EnumAttachPosition> EAST = PropertyEnum.<BlockGlowystoneWire.EnumAttachPosition>create("east", BlockGlowystoneWire.EnumAttachPosition.class);
	public static final PropertyEnum<BlockGlowystoneWire.EnumAttachPosition> SOUTH = PropertyEnum.<BlockGlowystoneWire.EnumAttachPosition>create("south", BlockGlowystoneWire.EnumAttachPosition.class);
	public static final PropertyEnum<BlockGlowystoneWire.EnumAttachPosition> WEST = PropertyEnum.<BlockGlowystoneWire.EnumAttachPosition>create("west", BlockGlowystoneWire.EnumAttachPosition.class);
	
	protected static final AxisAlignedBB[] GLOWYSTONE_WIRE_AABB = new AxisAlignedBB[] {new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 0.8125D), new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 0.8125D), new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 0.8125D, 0.0625D, 0.8125D), new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.8125D, 0.0625D, 0.8125D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 1.0D, 0.0625D, 0.8125D), new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 1.0D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 1.0D, 0.0625D, 0.8125D), new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 1.0D, 0.0625D, 1.0D), new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 1.0D, 0.0625D, 0.8125D), new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 0.8125D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D)};
	private boolean canProvidePower = false;
	
	/** List of blocks to update with glowystone. */
	private final Set<BlockPos> blocksNeedingUpdate = Sets.<BlockPos>newHashSet();

	public BlockGlowystoneWire()
	{
		super(Material.CIRCUITS);
		this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, BlockGlowystoneWire.EnumAttachPosition.NONE).withProperty(EAST, BlockGlowystoneWire.EnumAttachPosition.NONE).withProperty(SOUTH, BlockGlowystoneWire.EnumAttachPosition.NONE).withProperty(WEST, BlockGlowystoneWire.EnumAttachPosition.NONE));
		setLightLevel(1.0F);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return GLOWYSTONE_WIRE_AABB[getAABBIndex(state.getActualState(source, pos))];
	}

	private static int getAABBIndex(IBlockState state)
	{
		int i = 0;
		boolean flag = state.getValue(NORTH) != BlockGlowystoneWire.EnumAttachPosition.NONE;
		boolean flag1 = state.getValue(EAST) != BlockGlowystoneWire.EnumAttachPosition.NONE;
		boolean flag2 = state.getValue(SOUTH) != BlockGlowystoneWire.EnumAttachPosition.NONE;
		boolean flag3 = state.getValue(WEST) != BlockGlowystoneWire.EnumAttachPosition.NONE;

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

	/**
	 * Get the actual Block state of this Block at the given position. This applies properties not visible in the
	 * metadata, such as fence connections.
	 */
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		state = state.withProperty(WEST, this.getAttachPosition(worldIn, pos, EnumFacing.WEST));
		state = state.withProperty(EAST, this.getAttachPosition(worldIn, pos, EnumFacing.EAST));
		state = state.withProperty(NORTH, this.getAttachPosition(worldIn, pos, EnumFacing.NORTH));
		state = state.withProperty(SOUTH, this.getAttachPosition(worldIn, pos, EnumFacing.SOUTH));
		return state;
	}

	private BlockGlowystoneWire.EnumAttachPosition getAttachPosition(IBlockAccess worldIn, BlockPos pos, EnumFacing direction)
	{
		BlockPos blockpos = pos.offset(direction);
		IBlockState iblockstate = worldIn.getBlockState(pos.offset(direction));

		if (!canConnectTo(worldIn.getBlockState(blockpos), direction, worldIn, blockpos) && (iblockstate.isNormalCube() || !canConnectUpwardsTo(worldIn, blockpos.down())))
		{
			IBlockState iblockstate1 = worldIn.getBlockState(pos.up());

			if (!iblockstate1.isNormalCube())
			{
				boolean flag = worldIn.getBlockState(blockpos).isSideSolid(worldIn, blockpos, EnumFacing.UP) || worldIn.getBlockState(blockpos).getBlock() == Blocks.GLOWSTONE;

				if (flag && canConnectUpwardsTo(worldIn, blockpos.up()))
				{
					if (iblockstate.isBlockNormalCube())
					{
						return BlockGlowystoneWire.EnumAttachPosition.UP;
					}

					return BlockGlowystoneWire.EnumAttachPosition.SIDE;
				}
			}

			return BlockGlowystoneWire.EnumAttachPosition.NONE;
		}
		else
		{
			return BlockGlowystoneWire.EnumAttachPosition.SIDE;
		}
	}

	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
	{
		return NULL_AABB;
	}

	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks for render
	 */
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		return worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos, EnumFacing.UP) || worldIn.getBlockState(pos.down()).getBlock() == Blocks.GLOWSTONE;
	}

	private IBlockState updateSurroundingGlowystone(World worldIn, BlockPos pos, IBlockState state)
	{
		state = this.calculateCurrentChanges(worldIn, pos, pos, state);
		List<BlockPos> list = Lists.newArrayList(this.blocksNeedingUpdate);
		this.blocksNeedingUpdate.clear();

		for (BlockPos blockpos : list)
		{
			worldIn.notifyNeighborsOfStateChange(blockpos, this, false);
		}

		return state;
	}

	private IBlockState calculateCurrentChanges(World worldIn, BlockPos pos1, BlockPos pos2, IBlockState state)
	{
		return state;
	}

	/**
	 * Calls World.notifyNeighborsOfStateChange() for all neighboring blocks, but only if the given block is a glowystone
	 * wire.
	 */
	private void notifyWireNeighborsOfStateChange(World worldIn, BlockPos pos)
	{
		if (worldIn.getBlockState(pos).getBlock() == this)
		{
			worldIn.notifyNeighborsOfStateChange(pos, this, false);

			for (EnumFacing enumfacing : EnumFacing.values())
			{
				worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
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
				worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
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

	/**
	 * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
	 */
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		super.breakBlock(worldIn, pos, state);

		if (!worldIn.isRemote)
		{
			for (EnumFacing enumfacing : EnumFacing.values())
			{
				worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
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

	private int getMaxCurrentStrength(World worldIn, BlockPos pos, int strength)
	{
		return strength;
	}

	/**
	 * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
	 * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
	 * block, etc.
	 */
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		if (!worldIn.isRemote)
		{
			if (this.canPlaceBlockAt(worldIn, pos))
			{
				this.updateSurroundingGlowystone(worldIn, pos, state);
			}
			else
			{
				this.dropBlockAsItem(worldIn, pos, state, 0);
				worldIn.setBlockToAir(pos);
			}
		}
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return MinestuckItems.glowystoneDust;
	}

	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return !this.canProvidePower ? 0 : blockState.getWeakPower(blockAccess, pos, side);
	}

	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return 0;
	}

	private boolean isPowerSourceAt(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		BlockPos blockpos = pos.offset(side);
		IBlockState iblockstate = worldIn.getBlockState(blockpos);
		boolean flag = iblockstate.isNormalCube();
		boolean flag1 = worldIn.getBlockState(pos.up()).isNormalCube();
		return !flag1 && flag && canConnectUpwardsTo(worldIn, blockpos.up()) ? true : (canConnectTo(iblockstate, side, worldIn, pos) ? true : (!flag && canConnectUpwardsTo(worldIn, blockpos.down())));
	}

	protected static boolean canConnectUpwardsTo(IBlockAccess worldIn, BlockPos pos)
	{
		return canConnectTo(worldIn.getBlockState(pos), null, worldIn, pos);
	}

	protected static boolean canConnectTo(IBlockState blockState, @Nullable EnumFacing side, IBlockAccess world, BlockPos pos )
	{
		return blockState.getBlock() == MinestuckBlocks.glowystoneWire;
	}

	/**
	 * Can this block provide power. Only wire currently seems to have this change based on its state.
	 */
	public boolean canProvidePower(IBlockState state)
	{
		return this.canProvidePower;
	}

	@SideOnly(Side.CLIENT)
	public static int colorMultiplier(int ignored)
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

	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		double d0 = (double)pos.getX() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
		double d1 = (double)((float)pos.getY() + 0.0625F);
		double d2 = (double)pos.getZ() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
			
		worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0, d1, d2, 1.0, 0.8, 0, new int[0]);
	}

	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
	{
		return new ItemStack(MinestuckItems.glowystoneDust);
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState();
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}

	/**
	 * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 */
	public IBlockState withRotation(IBlockState state, Rotation rot)
	{
		switch (rot)
		{
			case CLOCKWISE_180:
				return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(EAST, state.getValue(WEST)).withProperty(SOUTH, state.getValue(NORTH)).withProperty(WEST, state.getValue(EAST));
			case COUNTERCLOCKWISE_90:
				return state.withProperty(NORTH, state.getValue(EAST)).withProperty(EAST, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(WEST)).withProperty(WEST, state.getValue(NORTH));
			case CLOCKWISE_90:
				return state.withProperty(NORTH, state.getValue(WEST)).withProperty(EAST, state.getValue(NORTH)).withProperty(SOUTH, state.getValue(EAST)).withProperty(WEST, state.getValue(SOUTH));
			default:
				return state;
		}
	}

	/**
	 * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 */
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
	{
		switch (mirrorIn)
		{
			case LEFT_RIGHT:
				return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(NORTH));
			case FRONT_BACK:
				return state.withProperty(EAST, state.getValue(WEST)).withProperty(WEST, state.getValue(EAST));
			default:
				return super.withMirror(state, mirrorIn);
		}
	}

	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] {NORTH, EAST, SOUTH, WEST});
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return BlockFaceShape.UNDEFINED;
	}
	
	public enum EnumAttachPosition implements IStringSerializable
	{
		UP("up"),
		SIDE("side"),
		NONE("none");

		private final String name;

		EnumAttachPosition(String name)
		{
			this.name = name;
		}

		public String toString()
		{
			return this.getName();
		}

		public String getName()
		{
			return this.name;
		}
	}
}
