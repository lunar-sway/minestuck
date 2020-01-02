package com.mraof.minestuck.block;

import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.network.skaianet.SkaiaClient;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
import com.mraof.minestuck.util.ComputerProgram;
import com.mraof.minestuck.util.IdentifierHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class ComputerBlock extends MachineBlock
{
	public static final Map<Direction, VoxelShape> COMPUTER_SHAPE = createRotatedShapes(1, 0, 1, 15, 2, 15);
	public static final Map<Direction, VoxelShape> LAPTOP_SHAPE = createRotatedShapes(1, 0, 4, 15, 1, 12);
	public static final Map<Direction, VoxelShape> LUNCHTOP_SHAPE = createRotatedShapes(5, 0, 5, 11, 4, 10);
	public static final Map<Direction, VoxelShape> COMPUTER_COLLISION_SHAPE;
	public static final Map<Direction, VoxelShape> LAPTOP_COLLISION_SHAPE;
	
	public static final EnumProperty<State> STATE = MSProperties.COMPUTER_STATE;
	
	static
	{
		COMPUTER_COLLISION_SHAPE = createRotatedShapes(0, 0, 6, 16, 13, 8);
		COMPUTER_COLLISION_SHAPE.replaceAll((enumFacing, shape) -> VoxelShapes.or(shape, COMPUTER_SHAPE.get(enumFacing)));
		LAPTOP_COLLISION_SHAPE = createRotatedShapes(0, 0, 12, 16, 10, 13);
		LAPTOP_COLLISION_SHAPE.replaceAll((enumFacing, shape) -> VoxelShapes.or(shape, LAPTOP_SHAPE.get(enumFacing)));
	}
	
	public final Map<Direction, VoxelShape> shape, collisionShape;
	
	public ComputerBlock(Map<Direction, VoxelShape> shape, Map<Direction, VoxelShape> collisionShape, Properties properties)
	{
		super(properties);
		setDefaultState(getDefaultState().with(STATE, State.OFF));
		this.shape = shape;
		this.collisionShape = collisionShape;
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		super.fillStateContainer(builder);
		builder.add(STATE);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		//TODO This function probably need to be cleaned up
		ItemStack heldItem = player.getHeldItem(handIn);
		if(state.get(STATE) == State.OFF)
		{
			if(player.isSneaking() || !Direction.UP.equals(hit.getFace()) || !heldItem.isEmpty() && ComputerProgram.getProgramID(heldItem) == -2)
				return false;
			
			if(!worldIn.isRemote)
			{
				BlockState newState = state.with(STATE, State.ON);
				worldIn.setBlockState(pos, newState, 2);
				
				TileEntity te = worldIn.getTileEntity(pos);
				if(te instanceof ComputerTileEntity)
					((ComputerTileEntity) te).owner = IdentifierHandler.encode(player);
				newState.onBlockActivated(worldIn, player, handIn, hit);
			}
			
			return true;
		} else
		{
			ComputerTileEntity tileEntity = (ComputerTileEntity) worldIn.getTileEntity(pos);
			
			if (tileEntity == null || player.isSneaking())
			{
				return false;
			}
			
			int id = ComputerProgram.getProgramID(player.getHeldItem(handIn));
			if(id != -2 && !tileEntity.hasProgram(id) && tileEntity.installedPrograms.size() < 2 && !tileEntity.hasProgram(-1))
			{
				if(worldIn.isRemote)
					return true;
				player.setHeldItem(handIn, ItemStack.EMPTY);
				if(id == -1)
				{
					tileEntity.closeAll();
					worldIn.setBlockState(pos, state.with(STATE, State.BROKEN), 2);
				}
				else tileEntity.installedPrograms.put(id, true);
				tileEntity.markDirty();
				worldIn.notifyBlockUpdate(pos, state, state, 3);
				return true;
			}
			
			if(worldIn.isRemote && SkaiaClient.requestData(tileEntity))
				MSScreenFactories.displayComputerScreen(tileEntity);
			
			return true;
		}
	}
	
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return state.get(STATE) != State.OFF;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new ComputerTileEntity();
	}
	
	
	@Override
	@SuppressWarnings("deprecation")
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		dropItems(worldIn, pos.getX(), pos.getY(), pos.getZ(), state);
		super.onReplaced(state, worldIn, pos, newState, isMoving);
	}
	
	private void dropItems(World world, int x, int y, int z, BlockState state)
	{
		Random rand = new Random();
		ComputerTileEntity te = (ComputerTileEntity) world.getTileEntity(new BlockPos(x, y, z));
		if (te == null)
		{
			return;
		}
		te.closeAll();
		float factor = 0.05F;
		
		Iterator<Map.Entry<Integer, Boolean>> it = te.installedPrograms.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry<Integer, Boolean> pairs = it.next();
			if(!pairs.getValue())
				continue;
			int program = pairs.getKey();
			
			float rx = rand.nextFloat() * 0.8F + 0.1F;
			float ry = rand.nextFloat() * 0.8F + 0.1F;
			float rz = rand.nextFloat() * 0.8F + 0.1F;
			ItemEntity entityItem = new ItemEntity(world, x + rx, y + ry, z + rz, ComputerProgram.getItem(program));
			entityItem.setMotion(rand.nextGaussian() * factor, rand.nextGaussian() * factor + 0.2F, rand.nextGaussian() * factor);
			world.addEntity(entityItem);
		}
		if(state.get(STATE) == State.BROKEN)
		{
			float rx = rand.nextFloat() * 0.8F + 0.1F;
			float ry = rand.nextFloat() * 0.8F + 0.1F;
			float rz = rand.nextFloat() * 0.8F + 0.1F;
			ItemEntity entityItem = new ItemEntity(world, x + rx, y + ry, z + rz, ComputerProgram.getItem(-1));
			entityItem.setMotion(rand.nextGaussian() * factor, rand.nextGaussian() * factor + 0.2F, rand.nextGaussian() * factor);
			world.addEntity(entityItem);
		}
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return shape.get(state.get(FACING));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return collisionShape.get(state.get(FACING));
	}
	
	public enum State implements IStringSerializable
	{
		OFF,
		ON,
		BROKEN;
		
		@Override
		public String getName()
		{
			return name().toLowerCase();
		}
	}
}