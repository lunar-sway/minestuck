package com.mraof.minestuck.block;

import com.mraof.minestuck.block.multiblock.MachineMultiblock;
import com.mraof.minestuck.tileentity.PunchDesignixTileEntity;
import com.mraof.minestuck.util.CustomVoxelShape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
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
	
	public static final Map<Direction, VoxelShape> LEG_SHAPE = createRotatedShapes(0, 0, 4, 16, 16, 16);
	public static final Map<Direction, VoxelShape> SLOT_SHAPE = createRotatedShapes(0, 0, 9, 15, 7, 16);
	public static final Map<Direction, VoxelShape> KEYBOARD_SHAPE = createRotatedShapes(1, 0, 4, 16, 7, 16);
	
	protected final Map<Direction, VoxelShape> shape;
	protected final BlockPos mainPos;
	
	public PunchDesignixBlock(MachineMultiblock machine, CustomVoxelShape shape, BlockPos pos, Properties properties)
	{
		super(machine, properties);
		this.shape = shape.createRotatedShapes();
		this.mainPos = pos;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return shape.get(state.get(FACING));
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		if (worldIn.isRemote)
			return true;
		BlockPos mainPos = getMainPos(state, pos);
		TileEntity te = worldIn.getTileEntity(mainPos);
		if (te instanceof PunchDesignixTileEntity)
			((PunchDesignixTileEntity) te).onRightClick((ServerPlayerEntity) player, state);
		return true;
	}
	
	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if(state.getBlock() != newState.getBlock())
		{
			BlockPos mainPos = getMainPos(state, pos);
			TileEntity te = worldIn.getTileEntity(mainPos);
			if(te instanceof PunchDesignixTileEntity)
			{
				PunchDesignixTileEntity designix = (PunchDesignixTileEntity) te;
				designix.broken = true;
				if(hasTileEntity(state))
					designix.dropItem(true);
			}
			
			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}
	
	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		TileEntity te = worldIn.getTileEntity(pos);
		if(te instanceof PunchDesignixTileEntity)
			((PunchDesignixTileEntity) te).checkStates();
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
		Rotation rotation = rotationFromDirection(direction);
		
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