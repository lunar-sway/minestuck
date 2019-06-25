package com.mraof.minestuck.block;

import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.util.ComputerProgram;
import com.mraof.minestuck.util.IdentifierHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Map;

public class BlockComputerOff extends BlockMachine
{
	public static final Map<EnumFacing, VoxelShape> COMPUTER_SHAPE = createRotatedShapes(1, 0, 1, 15, 2, 15);
	public static final Map<EnumFacing, VoxelShape> LAPTOP_SHAPE = createRotatedShapes(1, 0, 4, 15, 1, 12);
	public static final Map<EnumFacing, VoxelShape> LUNCHTOP_SHAPE = createRotatedShapes(5, 0, 5, 11, 4, 10);
	public static final Map<EnumFacing, VoxelShape> COMPUTER_COLLISION_SHAPE;
	public static final Map<EnumFacing, VoxelShape> LAPTOP_COLLISION_SHAPE;
	
	static
	{
		COMPUTER_COLLISION_SHAPE = createRotatedShapes(0, 0, 6, 16, 13, 8);
		COMPUTER_COLLISION_SHAPE.replaceAll((enumFacing, shape) -> VoxelShapes.or(shape, COMPUTER_SHAPE.get(enumFacing)));
		LAPTOP_COLLISION_SHAPE = createRotatedShapes(0, 0, 12, 16, 10, 13);
		LAPTOP_COLLISION_SHAPE.replaceAll((enumFacing, shape) -> VoxelShapes.or(shape, LAPTOP_SHAPE.get(enumFacing)));
	}
	
	public final Block computerOn;
	public final Map<EnumFacing, VoxelShape> shape, collisionShape;
	
	public BlockComputerOff(Properties properties, Block computerOn, Map<EnumFacing, VoxelShape> shape, Map<EnumFacing, VoxelShape> collisionShape)
	{
		super(properties);
		this.computerOn = computerOn;
		this.shape = shape;
		this.collisionShape = collisionShape;
	}
	
	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		ItemStack heldItem = player.getHeldItem(hand);
		if(player.isSneaking() || !EnumFacing.UP.equals(side) || !heldItem.isEmpty() && ComputerProgram.getProgramID(heldItem) == -2)
			return false;
		
		if(!worldIn.isRemote && computerOn != null)
		{
			IBlockState newState = computerOn.getDefaultState().with(FACING, state.get(FACING));
			worldIn.setBlockState(pos, newState, 2);
			
			TileEntity te = worldIn.getTileEntity(pos);
			if(te instanceof TileEntityComputer)
				((TileEntityComputer) te).owner = IdentifierHandler.encode(player);
			newState.onBlockActivated(worldIn, pos, player, hand, side, hitX, hitY, hitZ);
		}
		
		return true;
	}
	
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return shape.get(state.get(FACING));
	}
	
	@Override
	public VoxelShape getCollisionShape(IBlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return collisionShape.get(state.get(FACING));
	}
}