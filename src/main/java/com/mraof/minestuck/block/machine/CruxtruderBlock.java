package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.tileentity.machine.CruxtruderTileEntity;
import com.mraof.minestuck.util.CustomVoxelShape;
import com.mraof.minestuck.util.MSRotationUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Map;

public class CruxtruderBlock extends MultiMachineBlock
{
	protected final Map<Direction, VoxelShape> shape;
	protected final boolean hasTileEntity;
	protected final BlockPos mainPos;
	
	public CruxtruderBlock(MachineMultiblock machine, CustomVoxelShape shape, boolean tileEntity, BlockPos mainPos, Properties properties)
	{
		super(machine, properties);
		this.shape = shape.createRotatedShapes();
		this.hasTileEntity = tileEntity;
		this.mainPos = mainPos;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return shape.get(state.getValue(FACING));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		if(hasTileEntity && (state.getValue(FACING) == hit.getDirection() || hit.getDirection() == Direction.UP))
		{
			if(worldIn.isClientSide)
				return ActionResultType.SUCCESS;
			
			TileEntity te = worldIn.getBlockEntity(pos);
			if(te instanceof CruxtruderTileEntity)
				((CruxtruderTileEntity) te).onRightClick(player, hit.getDirection() == Direction.UP);
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}
	
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return hasTileEntity;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		if(hasTileEntity)
			return new CruxtruderTileEntity();
		else return null;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		BlockPos MainPos = getMainPos(state, pos);
		TileEntity te = worldIn.getBlockEntity(MainPos);
		if(te instanceof CruxtruderTileEntity)
		{
			((CruxtruderTileEntity) te).destroy();
		}
		
		super.onRemove(state, worldIn, pos, newState, isMoving);
	}
	
	public BlockPos getMainPos(BlockState state, BlockPos pos)
	{
		Rotation rotation = MSRotationUtil.fromDirection(state.getValue(FACING));
		
		return pos.offset(mainPos.rotate(rotation));
	}
}