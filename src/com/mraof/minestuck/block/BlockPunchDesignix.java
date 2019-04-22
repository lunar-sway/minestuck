package com.mraof.minestuck.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mraof.minestuck.tileentity.TileEntityPunchDesignix;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Map;

public class BlockPunchDesignix extends BlockLargeMachine
{
	
	public static final Map<EnumFacing, VoxelShape> LEG_SHAPE = Maps.newEnumMap(ImmutableMap.of(EnumFacing.NORTH, Block.makeCuboidShape(0, 0, 0, 16, 16, 12), EnumFacing.SOUTH, Block.makeCuboidShape(0, 0, 4, 16, 16, 16),
																								EnumFacing.WEST, Block.makeCuboidShape(0, 0, 0, 12, 16, 16), EnumFacing.EAST, Block.makeCuboidShape(4, 0, 0, 16, 16, 16)));
	public static final Map<EnumFacing, VoxelShape> SLOT_SHAPE = Maps.newEnumMap(ImmutableMap.of(EnumFacing.NORTH, Block.makeCuboidShape(1, 0, 0, 16, 7, 7), EnumFacing.SOUTH, Block.makeCuboidShape(0, 0, 9, 15, 7, 16),
																								EnumFacing.WEST, Block.makeCuboidShape(0, 0, 0, 7, 7, 15), EnumFacing.EAST, Block.makeCuboidShape(9, 0, 1, 16, 7, 16)));
	public static final Map<EnumFacing, VoxelShape> KEYBOARD_SHAPE = Maps.newEnumMap(ImmutableMap.of(EnumFacing.NORTH, Block.makeCuboidShape(0, 0, 0, 15, 7, 12), EnumFacing.SOUTH, Block.makeCuboidShape(1, 0, 4, 16, 7, 16),
																									EnumFacing.WEST, Block.makeCuboidShape(0, 0, 1, 12, 7, 16), EnumFacing.EAST, Block.makeCuboidShape(4, 0, 0, 16, 7, 15)));
	
	protected final Map<EnumFacing, VoxelShape> SHAPE;
	protected final BlockPos MAIN_POS;
	
	public BlockPunchDesignix(Properties properties, Map<EnumFacing, VoxelShape> shape, BlockPos pos)
	{
		super(properties);
		this.SHAPE = shape;
		this.MAIN_POS = pos;
	}
	
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return SHAPE.get(state.get(FACING));
	}
	
	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (worldIn.isRemote)
			return true;
		BlockPos mainPos = getMainPos(state, pos);
		TileEntity te = worldIn.getTileEntity(mainPos);
		if (te instanceof TileEntityPunchDesignix)
			((TileEntityPunchDesignix) te).onRightClick((EntityPlayerMP) player, state);
		return true;
	}
	
	@Override
	public void onReplaced(IBlockState state, World worldIn, BlockPos pos, IBlockState newState, boolean isMoving)
	{
		BlockPos mainPos = getMainPos(state, pos);
		TileEntity te = worldIn.getTileEntity(mainPos);
		if(te instanceof TileEntityPunchDesignix)
		{
			TileEntityPunchDesignix designix = (TileEntityPunchDesignix) te;
			designix.broken = true;
			if(hasTileEntity(state))
				designix.dropItem(true);
		}
		
		super.onReplaced(state, worldIn, pos, newState, isMoving);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		TileEntity te = worldIn.getTileEntity(pos);
		if(te instanceof TileEntityPunchDesignix)
			((TileEntityPunchDesignix) te).checkStates();
	}
	
    /**
     *returns the block position of the "Main" block
     *aka the block with the TileEntity for the machine
     *@param state the state of the block
     *@param pos the position the block
     */
	public BlockPos getMainPos(IBlockState state, BlockPos pos)
	{
		EnumFacing facing = state.get(FACING);
		Rotation rotation = rotationFromFacing(facing);
		
		return pos.add(this.MAIN_POS.rotate(rotation));
	}
}