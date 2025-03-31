package com.mraof.minestuck.block.machine;

import com.mraof.minestuck.block.CustomVoxelShape;
import com.mraof.minestuck.block.MSBlockShapes;
import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.computer.ProgramTypes;
import com.mraof.minestuck.computer.theme.MSComputerThemes;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.skaianet.client.SkaiaClient;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
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
		if(hand == InteractionHand.OFF_HAND)
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		
		if(player.isShiftKeyDown())
		{
			BlockState newState = state.setValue(STATE, State.OFF);
			level.setBlock(pos, newState, Block.UPDATE_CLIENTS);
			
			return ItemInteractionResult.SUCCESS;
		}
		
		if(state.getValue(STATE) == State.OFF)
		{
			turnOn(state, level, pos, player);
			return ItemInteractionResult.SUCCESS; //do not allow additional actions if computer is just being turned on
		}
		
		if(!(level.getBlockEntity(pos) instanceof ComputerBlockEntity blockEntity))
			return ItemInteractionResult.FAIL;
		
		ItemStack heldItem = player.getItemInHand(hand);
		if(blockEntity.tryInsertDisk(player, heldItem))
			return ItemInteractionResult.SUCCESS;
		//insertion of code handled in ReadableSburbCodeItem onItemUseFirst()
		
		if(level.isClientSide && SkaiaClient.requestData(blockEntity))
			MSScreenFactories.displayComputerScreen(blockEntity);
		
		return ItemInteractionResult.SUCCESS;
	}
	
	private void turnOn(BlockState state, Level level, BlockPos pos, Player player)
	{
		if(level.isClientSide)
			return;
		
		level.playSound(null, pos, MSSoundEvents.COMPUTER_BOOT.get(), SoundSource.BLOCKS);
		
		if(level.getBlockEntity(pos) instanceof ComputerBlockEntity computer)
		{
			//defaults to ON, unless the computer is broken or has a game disk
			ComputerBlock.State computerState = State.ON;
			boolean hasSBURBProgram = computer.installedPrograms().anyMatch(programType -> programType == ProgramTypes.SBURB_CLIENT.get() || programType == ProgramTypes.SBURB_CLIENT.get());
			boolean isBSOD = computer.getDisks().stream().anyMatch(disk -> disk.is(Items.MUSIC_DISC_11));
			
			if(hasSBURBProgram)
				computerState = State.GAME_LOADED;
			if(isBSOD)
				computerState = State.BROKEN;
			
			BlockState newState = state.setValue(STATE, computerState);
			level.setBlock(pos, newState, Block.UPDATE_CLIENTS);
			
			if(computer.getOwner() == null)
			{
				//TODO add 1 in 10 chance of a different theme if the computers default theme is actually the default
				computer.initializeOwner(Objects.requireNonNull(IdentifierHandler.encode(player)));
				computer.setTheme(defaultTheme);
			}
		}
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new ComputerBlockEntity(pos, state);
	}
	
	@Override
	protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if(!newState.is(state.getBlock()))
			dropItems(level, pos);
		super.onRemove(state, level, pos, newState, isMoving);
	}
	
	private void dropItems(Level level, BlockPos pos)
	{
		if(level.getBlockEntity(pos) instanceof ComputerBlockEntity computer)
		{
			computer.closeAll();
			computer.dropAllDisks(true);
		}
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
