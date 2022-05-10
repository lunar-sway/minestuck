package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.tileentity.HolopadTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Map;

public class HolopadBlock extends MachineBlock
{
	public static final Map<Direction, VoxelShape> SHAPE = createRotatedShapes(2, 0, 1, 14, 6, 13);
	public static final Map<Direction, VoxelShape> COLLISION_SHAPE;
	protected static final AxisAlignedBB HOLOPAD_TOP_AABB = new AxisAlignedBB(3/16F, 6/16F, 2.6/16F, 13/16F, 7/16F, 12.6/16F);
	protected static final AxisAlignedBB HOLOPAD_CARDSLOT_AABB = new AxisAlignedBB(4/16F, 0F, 13.8/16F, 12/16F, 10.1/16F, 15.94/16F);
	
	public static final BooleanProperty HAS_CARD = MSProperties.HAS_CARD;
	
	static
	{
		VoxelShape topShape = Block.box(3, 6, 3, 13, 7, 13);
		COLLISION_SHAPE = createRotatedShapes(4, 0, 14, 12, 10, 16);
		COLLISION_SHAPE.replaceAll((enumFacing, shape) -> VoxelShapes.or(shape, topShape));
	}
	
	public HolopadBlock(Properties builder)
	{
		super(builder);
		registerDefaultState(defaultBlockState().setValue(HAS_CARD, false));
	}
	
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new HolopadTileEntity();
	}
	
	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		if(player.isShiftKeyDown()) return ActionResultType.PASS;
		if(worldIn.isClientSide)
			return ActionResultType.SUCCESS;
		TileEntity te = worldIn.getBlockEntity(pos);
		
		if(te instanceof HolopadTileEntity)
			((HolopadTileEntity) te).onRightClick(player);
		return ActionResultType.SUCCESS;
	}
	
	@Override
	public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player)
	{
		HolopadTileEntity te = (HolopadTileEntity) worldIn.getBlockEntity(pos);
		
		if(te != null && !worldIn.isClientSide)
		{
			te.dropItem(true, worldIn, pos, te.getCard());
		}
		
		super.playerWillDestroy(worldIn, pos, state, player);
	}
	
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(HAS_CARD);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPE.get(state.getValue(FACING));
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return COLLISION_SHAPE.get(state.getValue(FACING));
	}
	
	
}