package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.tileentity.machine.PunchDesignixTileEntity;
import com.mraof.minestuck.util.CustomVoxelShape;
import com.mraof.minestuck.util.MSRotationUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
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

public class PunchDesignixBlock extends MultiMachineBlock
{
	protected final Map<Direction, VoxelShape> shape;
	protected final BlockPos mainPos;
	
	public PunchDesignixBlock(MachineMultiblock machine, CustomVoxelShape shape, BlockPos pos, Properties properties)
	{
		super(machine, properties);
		this.shape = shape.createRotatedShapes();
		this.mainPos = pos;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return shape.get(state.get(FACING));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		if (worldIn.isRemote)
			return ActionResultType.SUCCESS;
		BlockPos mainPos = getMainPos(state, pos);
		TileEntity te = worldIn.getTileEntity(mainPos);
		if (te instanceof PunchDesignixTileEntity)
			((PunchDesignixTileEntity) te).onRightClick((ServerPlayerEntity) player, state);
		return ActionResultType.SUCCESS;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if(state.getBlock() != newState.getBlock())
		{
			BlockPos mainPos = getMainPos(state, pos);
			TileEntity te = worldIn.getTileEntity(mainPos);
			if(te instanceof PunchDesignixTileEntity)
			{
				PunchDesignixTileEntity designix = (PunchDesignixTileEntity) te;
				designix.breakMachine();
				if(hasTileEntity(state))
					designix.dropItem(true);
			}
			
			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}
	
    /**
     *returns the block position of the "Main" block
     *aka the block with the TileEntity for the machine
     *@param state the state of the block
     *@param pos the position the block
     */
	public BlockPos getMainPos(BlockState state, BlockPos pos)
	{
		Direction direction = state.get(FACING);
		Rotation rotation = MSRotationUtil.fromDirection(direction);
		
		return pos.add(this.mainPos.rotate(rotation));
	}
	
	public static class Slot extends PunchDesignixBlock
	{
		public static final BooleanProperty HAS_CARD = MSProperties.HAS_CARD;
		
		public Slot(MachineMultiblock machine, CustomVoxelShape shape, Properties properties)
		{
			super(machine, shape, new BlockPos(0, 0, 0), properties);
			setDefaultState(this.stateContainer.getBaseState().with(HAS_CARD, false));
		}
		
		@Override
		protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
		{
			super.fillStateContainer(builder);
			builder.add(HAS_CARD);
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
			return new PunchDesignixTileEntity();
		}
	}
}