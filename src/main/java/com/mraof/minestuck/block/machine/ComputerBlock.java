package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.block.MSBlockShapes;
import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.computer.ProgramData;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.skaianet.client.SkaiaClient;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Random;

public class ComputerBlock extends MachineBlock
{
	public static final Map<Direction, VoxelShape> COMPUTER_SHAPE = MSBlockShapes.COMPUTER.createRotatedShapes();
	public static final Map<Direction, VoxelShape> LAPTOP_CLOSED_SHAPE = MSBlockShapes.LAPTOP_CLOSED.createRotatedShapes();
	public static final Map<Direction, VoxelShape> LAPTOP_OPEN_SHAPE = MSBlockShapes.LAPTOP_OPEN.createRotatedShapes();
	public static final Map<Direction, VoxelShape> LUNCHTOP_CLOSED_SHAPE = createRotatedShapes(4, 0, 1.5, 12, 5, 8.5);
	public static final Map<Direction, VoxelShape> LUNCHTOP_OPEN_SHAPE = MSBlockShapes.LUNCHTOP_OPEN.createRotatedShapes();
	public static final Map<Direction, VoxelShape> OLD_COMPUTER_SHAPE = MSBlockShapes.OLD_COMPUTER.createRotatedShapes();
	
	public static final EnumProperty<State> STATE = MSProperties.COMPUTER_STATE;
	
	public final Map<Direction, VoxelShape> shapeOn, shapeOff;
	
	public ComputerBlock(Map<Direction, VoxelShape> shapeOn, Map<Direction, VoxelShape> shapeOff, Properties properties)
	{
		super(properties);
		registerDefaultState(defaultBlockState().setValue(STATE, State.OFF));
		this.shapeOn = shapeOn;
		this.shapeOff = shapeOff;
	}
	
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(STATE);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		if(player.isShiftKeyDown())
			return ActionResultType.PASS;
		
		ItemStack heldItem = player.getItemInHand(handIn);
		if(state.getValue(STATE) == State.OFF)
		{
			if(!heldItem.isEmpty() && ProgramData.getProgramID(heldItem) == -2)
				return ActionResultType.PASS;
			
			turnOn(state, worldIn, pos, player, handIn, hit);
			
			return ActionResultType.SUCCESS;
		} else
		{
			ComputerTileEntity tileEntity = (ComputerTileEntity) worldIn.getBlockEntity(pos);
			
			
			if(tileEntity == null)
				return ActionResultType.FAIL;
			
			if(insertDisk(tileEntity, state, worldIn, pos, player, handIn))
				return ActionResultType.SUCCESS;
			
			if(worldIn.isClientSide && SkaiaClient.requestData(tileEntity))
				MSScreenFactories.displayComputerScreen(tileEntity);
			
			return ActionResultType.SUCCESS;
		}
	}
	
	private void turnOn(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		if(!worldIn.isClientSide)
		{
			BlockState newState = state.setValue(STATE, State.ON);
			worldIn.setBlock(pos, newState, Constants.BlockFlags.BLOCK_UPDATE);
			
			TileEntity te = worldIn.getBlockEntity(pos);
			if(te instanceof ComputerTileEntity)
				((ComputerTileEntity) te).owner = IdentifierHandler.encode(player);
			newState.use(worldIn, player, handIn, hit);
		}
	}
	
	private boolean insertDisk(ComputerTileEntity tileEntity, BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn)
	{
		int id = ProgramData.getProgramID(player.getItemInHand(handIn));
		if(id != -2 && !tileEntity.hasProgram(id) && tileEntity.installedPrograms.size() < 2 && !tileEntity.hasProgram(-1))
		{
			if(worldIn.isClientSide)
				return true;
			player.setItemInHand(handIn, ItemStack.EMPTY);
			if(id == -1)
			{
				tileEntity.closeAll();
				worldIn.setBlock(pos, state.setValue(STATE, State.BROKEN), Constants.BlockFlags.BLOCK_UPDATE);
			}
			else tileEntity.installedPrograms.put(id, true);
			tileEntity.setChanged();
			worldIn.sendBlockUpdated(pos, state, state, 3);
			return true;
		} else return false;
	}
	
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return state.getValue(STATE) != State.OFF;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new ComputerTileEntity();
	}
	
	
	@Override
	@SuppressWarnings("deprecation")
	public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		dropItems(worldIn, pos.getX(), pos.getY(), pos.getZ(), state);
		super.onRemove(state, worldIn, pos, newState, isMoving);
	}
	
	private void dropItems(World world, int x, int y, int z, BlockState state)
	{
		Random rand = new Random();
		ComputerTileEntity te = (ComputerTileEntity) world.getBlockEntity(new BlockPos(x, y, z));
		if (te == null)
		{
			return;
		}
		te.closeAll();
		float factor = 0.05F;
		
		for(Map.Entry<Integer, Boolean> pairs : te.installedPrograms.entrySet())
		{
			if(!pairs.getValue())
				continue;
			int program = pairs.getKey();
			
			float rx = rand.nextFloat() * 0.8F + 0.1F;
			float ry = rand.nextFloat() * 0.8F + 0.1F;
			float rz = rand.nextFloat() * 0.8F + 0.1F;
			ItemEntity entityItem = new ItemEntity(world, x + rx, y + ry, z + rz, ProgramData.getItem(program));
			entityItem.setDeltaMovement(rand.nextGaussian() * factor, rand.nextGaussian() * factor + 0.2F, rand.nextGaussian() * factor);
			world.addFreshEntity(entityItem);
		}
		if(state.getValue(STATE) == State.BROKEN)
		{
			float rx = rand.nextFloat() * 0.8F + 0.1F;
			float ry = rand.nextFloat() * 0.8F + 0.1F;
			float rz = rand.nextFloat() * 0.8F + 0.1F;
			ItemEntity entityItem = new ItemEntity(world, x + rx, y + ry, z + rz, ProgramData.getItem(-1));
			entityItem.setDeltaMovement(rand.nextGaussian() * factor, rand.nextGaussian() * factor + 0.2F, rand.nextGaussian() * factor);
			world.addFreshEntity(entityItem);
		}
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		if(state.getValue(STATE) == State.OFF)
			return shapeOff.get(state.getValue(FACING));
		else return shapeOn.get(state.getValue(FACING));
	}
	
	
	public enum State implements IStringSerializable
	{
		OFF,
		ON,
		BROKEN;
		
		@Override
		public String getSerializedName()
		{
			return name().toLowerCase();
		}
	}
}