package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.block.MSBlockShapes;
import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.computer.ProgramData;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.ReadableSburbCodeItem;
import com.mraof.minestuck.item.SburbCodeItem;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.skaianet.client.SkaiaClient;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ComputerBlock extends MachineBlock implements EntityBlock
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
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(STATE);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit)
	{
		if(player.isShiftKeyDown())
			return InteractionResult.PASS;
		
		ItemStack heldItem = player.getItemInHand(handIn);
		if(state.getValue(STATE) == State.OFF)
		{
			if(!heldItem.isEmpty() && ProgramData.getProgramID(heldItem) == -2)
				return InteractionResult.PASS;
			
			turnOn(state, level, pos, player, handIn, hit);
			
			return InteractionResult.SUCCESS;
		} else
		{
			ComputerBlockEntity blockEntity = (ComputerBlockEntity) level.getBlockEntity(pos);
			
			
			if(blockEntity == null)
				return InteractionResult.FAIL;
			
			if(insertDisk(blockEntity, state, level, pos, player, handIn))
				return InteractionResult.SUCCESS;
			else if(inputCode(blockEntity, state, level, pos, player, handIn))
				return InteractionResult.SUCCESS;
			
			if(level.isClientSide && SkaiaClient.requestData(blockEntity))
				MSScreenFactories.displayComputerScreen(blockEntity);
			
			return InteractionResult.SUCCESS;
		}
	}
	
	private void turnOn(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit)
	{
		if(!level.isClientSide)
		{
			BlockState newState = state.setValue(STATE, State.ON);
			level.setBlock(pos, newState, Block.UPDATE_CLIENTS);
			
			if(level.getBlockEntity(pos) instanceof ComputerBlockEntity computer)
				computer.owner = IdentifierHandler.encode(player);
			newState.use(level, player, handIn, hit);
		}
	}
	
	private boolean insertDisk(ComputerBlockEntity blockEntity, BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn)
	{
		ItemStack stackInHand = player.getItemInHand(handIn);
		int id = ProgramData.getProgramID(stackInHand);
		
		if(stackInHand.is(MSItems.BLANK_DISK.get().asItem()))
		{
			if(blockEntity.blankDisksStored < 2) //only allow two blank disks to be burned at a time
			{
				stackInHand.shrink(1);
				blockEntity.blankDisksStored++;
				blockEntity.setChanged();
				level.sendBlockUpdated(pos, state, state, 3);
				return true;
			}
		} else if(id != -2 && !blockEntity.hasProgram(id) && blockEntity.installedPrograms.size() < 3 && !blockEntity.hasProgram(-1))
		{
			if(level.isClientSide)
				return true;
			player.setItemInHand(handIn, ItemStack.EMPTY);
			if(id == -1)
			{
				blockEntity.closeAll();
				level.setBlock(pos, state.setValue(STATE, State.BROKEN), Block.UPDATE_CLIENTS);
			}
			else blockEntity.installedPrograms.put(id, true);
			blockEntity.setChanged();
			level.sendBlockUpdated(pos, state, state, 3);
			return true;
		}
		
		return false;
	}
	
	private boolean inputCode(ComputerBlockEntity blockEntity, BlockState state, Level levelIn, BlockPos pos, Player player, InteractionHand handIn)
	{
		ItemStack heldStack = player.getItemInHand(handIn);
		if(heldStack.getItem() instanceof ReadableSburbCodeItem)
		{
			List<Block> hieroglyphList = ReadableSburbCodeItem.getRecordedBlocks(heldStack);
			boolean newInfo = false;
			
			if(heldStack.getItem() == MSItems.COMPLETED_SBURB_CODE.get() || (SburbCodeItem.getParadoxInfo(heldStack) && !blockEntity.hasParadoxInfoStored))
			{
				newInfo = true;
				blockEntity.hasParadoxInfoStored = true;
			}
			
			if(!hieroglyphList.isEmpty())
			{
				for(Block iterateBlock : hieroglyphList) //for each block in the item's list, adds it to the block entities block should it not exist yet
				{
					if(blockEntity.hieroglyphsStored != null && iterateBlock.defaultBlockState().is(MSTags.Blocks.GREEN_HIEROGLYPHS) && !blockEntity.hieroglyphsStored.contains(iterateBlock))
					{
						blockEntity.hieroglyphsStored.add(iterateBlock);
						newInfo = true;
					}
				}
				
				//checks additionally if the item is also a SburbCodeItem, and does the reverse process of adding any new blocks from the block entities list to the item's
				if(heldStack.getItem() instanceof SburbCodeItem)
				{
					if(blockEntity.hasParadoxInfoStored)
						SburbCodeItem.setParadoxInfo(heldStack, true); //put before attemptConversionToCompleted in case it just received the paradox info
					
					if(blockEntity.hieroglyphsStored != null)
					{
						for(Block iterateBlock : blockEntity.hieroglyphsStored)
						{
							SburbCodeItem.addRecordedInfo(heldStack, iterateBlock);
							SburbCodeItem.attemptConversionToCompleted(player, handIn);
						}
					}
				}
			}
			
			if(newInfo)
			{
				blockEntity.setChanged();
				levelIn.sendBlockUpdated(pos, state, state, 3);
				
				return true;
			}
		}
		
		return false;
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		ComputerBlockEntity be = new ComputerBlockEntity(pos, state);
		be.installedPrograms.put(2, true); //the program disk burner has no associated item and should always exist on the computer
		return state.getValue(STATE) != State.OFF ? be : null;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
	{
		dropItems(level, pos.getX(), pos.getY(), pos.getZ(), state);
		super.onRemove(state, level, pos, newState, isMoving);
	}
	
	private void dropItems(Level level, int x, int y, int z, BlockState state)
	{
		Random rand = new Random();
		ComputerBlockEntity be = (ComputerBlockEntity) level.getBlockEntity(new BlockPos(x, y, z));
		if(be == null)
		{
			return;
		}
		be.closeAll();
		float factor = 0.05F;
		
		for(Map.Entry<Integer, Boolean> pairs : be.installedPrograms.entrySet())
		{
			if(!pairs.getValue())
				continue;
			int program = pairs.getKey();
			
			float rx = rand.nextFloat() * 0.8F + 0.1F;
			float ry = rand.nextFloat() * 0.8F + 0.1F;
			float rz = rand.nextFloat() * 0.8F + 0.1F;
			ItemStack diskStack = ProgramData.getItem(program);
			if(diskStack != null && !diskStack.isEmpty())
			{
				ItemEntity entityItem = new ItemEntity(level, x + rx, y + ry, z + rz, diskStack);
				entityItem.setDeltaMovement(rand.nextGaussian() * factor, rand.nextGaussian() * factor + 0.2F, rand.nextGaussian() * factor);
				level.addFreshEntity(entityItem);
			}
		}
		
		for(int iterate = 0; iterate < be.blankDisksStored; iterate++)
		{
			float rx = rand.nextFloat() * 0.8F + 0.1F;
			float ry = rand.nextFloat() * 0.8F + 0.1F;
			float rz = rand.nextFloat() * 0.8F + 0.1F;
			ItemEntity entityItem = new ItemEntity(level, x + rx, y + ry, z + rz, MSItems.BLANK_DISK.get().getDefaultInstance());
			entityItem.setDeltaMovement(rand.nextGaussian() * factor, rand.nextGaussian() * factor + 0.2F, rand.nextGaussian() * factor);
			level.addFreshEntity(entityItem);
		}
		
		if(state.getValue(STATE) == State.BROKEN)
		{
			float rx = rand.nextFloat() * 0.8F + 0.1F;
			float ry = rand.nextFloat() * 0.8F + 0.1F;
			float rz = rand.nextFloat() * 0.8F + 0.1F;
			ItemStack diskStack = ProgramData.getItem(-1);
			if(diskStack != null)
			{
				ItemEntity entityItem = new ItemEntity(level, x + rx, y + ry, z + rz, diskStack);
				entityItem.setDeltaMovement(rand.nextGaussian() * factor, rand.nextGaussian() * factor + 0.2F, rand.nextGaussian() * factor);
				level.addFreshEntity(entityItem);
			}
		}
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
	{
		if(state.getValue(STATE) == State.OFF)
			return shapeOff.get(state.getValue(FACING));
		else return shapeOn.get(state.getValue(FACING));
	}
	
	
	public enum State implements StringRepresentable
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