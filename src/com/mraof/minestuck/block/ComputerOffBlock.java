package com.mraof.minestuck.block;

import com.mraof.minestuck.tileentity.ComputerTileEntity;
import com.mraof.minestuck.util.ComputerProgram;
import com.mraof.minestuck.util.IdentifierHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Map;
import java.util.function.Supplier;

public class ComputerOffBlock extends MachineBlock
{
	public static final Map<Direction, VoxelShape> COMPUTER_SHAPE = createRotatedShapes(1, 0, 1, 15, 2, 15);
	public static final Map<Direction, VoxelShape> LAPTOP_SHAPE = createRotatedShapes(1, 0, 4, 15, 1, 12);
	public static final Map<Direction, VoxelShape> LUNCHTOP_SHAPE = createRotatedShapes(5, 0, 5, 11, 4, 10);
	public static final Map<Direction, VoxelShape> COMPUTER_COLLISION_SHAPE;
	public static final Map<Direction, VoxelShape> LAPTOP_COLLISION_SHAPE;
	
	static
	{
		COMPUTER_COLLISION_SHAPE = createRotatedShapes(0, 0, 6, 16, 13, 8);
		COMPUTER_COLLISION_SHAPE.replaceAll((enumFacing, shape) -> VoxelShapes.or(shape, COMPUTER_SHAPE.get(enumFacing)));
		LAPTOP_COLLISION_SHAPE = createRotatedShapes(0, 0, 12, 16, 10, 13);
		LAPTOP_COLLISION_SHAPE.replaceAll((enumFacing, shape) -> VoxelShapes.or(shape, LAPTOP_SHAPE.get(enumFacing)));
	}
	
	public final Supplier<Block> computerOn;
	public final Map<Direction, VoxelShape> shape, collisionShape;
	
	public ComputerOffBlock(Properties properties, Supplier<Block> computerOn, Map<Direction, VoxelShape> shape, Map<Direction, VoxelShape> collisionShape)
	{
		super(properties);
		this.computerOn = computerOn;
		this.shape = shape;
		this.collisionShape = collisionShape;
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		ItemStack heldItem = player.getHeldItem(handIn);
		if(player.isSneaking() || !Direction.UP.equals(hit.getFace()) || !heldItem.isEmpty() && ComputerProgram.getProgramID(heldItem) == -2)
			return false;
		
		if(!worldIn.isRemote)
		{
			BlockState newState = computerOn.get().getDefaultState().with(FACING, state.get(FACING));
			worldIn.setBlockState(pos, newState, 2);
			
			TileEntity te = worldIn.getTileEntity(pos);
			if(te instanceof ComputerTileEntity)
				((ComputerTileEntity) te).owner = IdentifierHandler.encode(player);
			newState.onBlockActivated(worldIn, player, handIn, hit);
		}
		
		return true;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return shape.get(state.get(FACING));
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return collisionShape.get(state.get(FACING));
	}
}