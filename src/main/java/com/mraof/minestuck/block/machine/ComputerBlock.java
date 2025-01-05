package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.block.CustomVoxelShape;
import com.mraof.minestuck.block.MSBlockShapes;
import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.computer.ProgramType;
import com.mraof.minestuck.computer.theme.MSComputerThemes;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.skaianet.client.SkaiaClient;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ComputerBlock extends MachineBlock implements EntityBlock
{
	public static final Map<Direction, VoxelShape> COMPUTER_SHAPE = MSBlockShapes.COMPUTER.createRotatedShapes();
	public static final Map<Direction, VoxelShape> LAPTOP_CLOSED_SHAPE = MSBlockShapes.LAPTOP_CLOSED.createRotatedShapes();
	public static final Map<Direction, VoxelShape> LAPTOP_OPEN_SHAPE = MSBlockShapes.LAPTOP_OPEN.createRotatedShapes();
	public static final Map<Direction, VoxelShape> LUNCHTOP_CLOSED_SHAPE = new CustomVoxelShape(new double[]{4, 0, 1.5, 12, 5, 8.5}).createRotatedShapes();
	public static final Map<Direction, VoxelShape> LUNCHTOP_OPEN_SHAPE = MSBlockShapes.LUNCHTOP_OPEN.createRotatedShapes();
	public static final Map<Direction, VoxelShape> OLD_COMPUTER_SHAPE = MSBlockShapes.OLD_COMPUTER.createRotatedShapes();
	
	public static final EnumProperty<State> STATE = MSProperties.COMPUTER_STATE;
	
	public final Map<Direction, VoxelShape> shapeOn, shapeOff;
	
	public final ResourceLocation defaultTheme;
	
	public ComputerBlock(Map<Direction, VoxelShape> shapeOn, Map<Direction, VoxelShape> shapeOff, Properties properties)
	{
		this(shapeOn, shapeOff, MSComputerThemes.DEFAULT, properties);
	}
	
	public ComputerBlock(Map<Direction, VoxelShape> shapeOn, Map<Direction, VoxelShape> shapeOff, ResourceLocation defaultTheme, Properties properties)
	{
		super(properties);
		registerDefaultState(defaultBlockState().setValue(STATE, State.OFF));
		this.shapeOn = shapeOn;
		this.shapeOff = shapeOff;
		this.defaultTheme = defaultTheme;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(STATE);
	}
	
	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
	{
		if(player.isShiftKeyDown())
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		
		ItemStack heldItem = player.getItemInHand(hand);
		if(state.getValue(STATE) == State.OFF)
		{
			if(ProgramType.getForDisk(heldItem).isEmpty())
				return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
			
			turnOn(state, level, pos, player);
		}
		
		if(!(level.getBlockEntity(pos) instanceof ComputerBlockEntity blockEntity))
			return ItemInteractionResult.FAIL;
		
		if(blockEntity.insertDisk(player.getItemInHand(hand)))
			return ItemInteractionResult.SUCCESS;
		//insertion of code handled in ReadableSburbCodeItem onItemUseFirst()
		
		if(level.isClientSide && SkaiaClient.requestData(blockEntity))
			MSScreenFactories.displayComputerScreen(blockEntity);
		
		return ItemInteractionResult.SUCCESS;
	}
	
	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult)
	{
		if(player.isShiftKeyDown())
			return InteractionResult.PASS;
		
		if(state.getValue(STATE) == State.OFF)
			turnOn(state, level, pos, player);
		
		if(!(level.getBlockEntity(pos) instanceof ComputerBlockEntity blockEntity))
			return InteractionResult.FAIL;
		
		if(level.isClientSide && SkaiaClient.requestData(blockEntity))
			MSScreenFactories.displayComputerScreen(blockEntity);
		
		return InteractionResult.SUCCESS;
	}
	
	private void turnOn(BlockState state, Level level, BlockPos pos, Player player)
	{
		if(level.isClientSide)
			return;
		
		BlockState newState = state.setValue(STATE, State.ON);
		level.setBlock(pos, newState, Block.UPDATE_CLIENTS);
		
		if(level.getBlockEntity(pos) instanceof ComputerBlockEntity computer)
		{
			computer.initializeOwner(Objects.requireNonNull(IdentifierHandler.encode(player)));
			
			computer.setTheme(defaultTheme);
		}
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return state.getValue(STATE) != State.OFF ? new ComputerBlockEntity(pos, state) : null;
	}
	
	@Override
	protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if(!newState.is(state.getBlock()))
			dropItems(level, pos.getX(), pos.getY(), pos.getZ(), state);
		super.onRemove(state, level, pos, newState, isMoving);
	}
	
	private void dropItems(Level level, int x, int y, int z, BlockState state)
	{
		ComputerBlockEntity be = (ComputerBlockEntity) level.getBlockEntity(new BlockPos(x, y, z));
		if(be == null)
		{
			return;
		}
		be.closeAll();
		
		//program disks
		be.installedPrograms().forEach(programType -> programType.diskItem()
				.ifPresent(disk -> Containers.dropItemStack(level, x, y, z, disk.getDefaultInstance())));
		
		//blank disks
		Containers.dropItemStack(level, x, y, z, new ItemStack(MSItems.BLANK_DISK.get(), be.blankDisksStored));
		
		//music disc
		if(state.getValue(STATE) == State.BROKEN)
			Containers.dropItemStack(level, x, y, z, new ItemStack(Items.MUSIC_DISC_11));
	}
	
	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
	{
		if(state.getValue(STATE) == State.OFF)
			return shapeOff.get(state.getValue(FACING));
		else return shapeOn.get(state.getValue(FACING));
	}
	
	
	public enum State implements StringRepresentable
	{
		OFF,
		ON,
		GAME_LOADED,
		BROKEN;
		
		@Override
		public String getSerializedName()
		{
			return name().toLowerCase();
		}
	}
}
