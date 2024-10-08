package com.mraof.minestuck.block.redstone;

import com.mojang.serialization.MapCodec;
import com.mraof.minestuck.block.BlockUtil;
import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.blockentity.redstone.StructureCoreBlockEntity;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.effects.CreativeShockEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

/**
 * When ACTIVE is true, the block entity will perform one of several tasks outlined in its ActionType enum to either send nbt to a CoreCompatibleScatteredStructurePiece or act on nbt it receives from said Piece type.
 * It is made to work with various structures in which recording whether it has been completed or not will be important, such as for updating quest completion(quest system pending) or preventing area effect blocks/summoners from remaining in use after completion of a dungeon
 */
public class StructureCoreBlock extends HorizontalDirectionalBlock implements EntityBlock
{
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final BooleanProperty ACTIVE = MSProperties.MACHINE_TOGGLE;
	
	public StructureCoreBlock(Properties properties)
	{
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(POWERED, false).setValue(ACTIVE, false));
	}
	
	@Override
	protected MapCodec<StructureCoreBlock> codec()
	{
		return null; //todo
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		if(!canInteract(player) || !(level.getBlockEntity(pos) instanceof StructureCoreBlockEntity structureCore))
			return InteractionResult.FAIL;
		
		if(player.isCrouching())
		{
			structureCore.prepForUpdate(); //sets tickCycle to 600 so next tick an update will occur
			
			boolean startedOffActive = state.getValue(ACTIVE);
			
			if(startedOffActive)
			{
				level.setBlockAndUpdate(pos, state.cycle(ACTIVE).setValue(POWERED, false));
				level.playSound(null, pos, SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 0.5F, 1.2F);
			} else
			{
				level.setBlockAndUpdate(pos, state.cycle(ACTIVE));
				level.playSound(null, pos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 0.5F, 1.2F);
			}
		}
		
		if(!player.isCrouching() && level.isClientSide)
		{
			MSScreenFactories.displayStructureCoreScreen(structureCore);
		}
		
		return InteractionResult.sidedSuccess(level.isClientSide);
	}
	
	public static boolean canInteract(Player player)
	{
		return !CreativeShockEffect.doesCreativeShockLimit(player, CreativeShockEffect.LIMIT_MACHINE_INTERACTIONS);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		super.neighborChanged(state, level, pos, blockIn, fromPos, isMoving);
		updateBlockEntityWithBlock(level, pos);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		super.onPlace(state, level, pos, oldState, isMoving);
		updateBlockEntityWithBlock(level, pos);
	}
	
	/**
	 * Used for cases in which the action type of the BE would benefit from instant change
	 */
	public void updateBlockEntityWithBlock(Level level, BlockPos pos)
	{
		if(level.getBlockEntity(pos) instanceof StructureCoreBlockEntity structureCoreBlockEntity)
		{
			//having the variable hasBeenCompleted changed to true as soon as possible will improve the response speed of READ_AND_WIPE functionality
			if(structureCoreBlockEntity.getBlockState().getValue(ACTIVE) && structureCoreBlockEntity.getActionType() == StructureCoreBlockEntity.ActionType.WRITE)
			{
				structureCoreBlockEntity.prepForUpdate();
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean isSignalSource(BlockState state)
	{
		return state.getValue(POWERED);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side)
	{
		return blockState.getValue(POWERED) ? 15 : 0;
	}
	
	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction side)
	{
		return true;
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new StructureCoreBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> placedType)
	{
		return !level.isClientSide ? BlockUtil.checkTypeForTicker(placedType, MSBlockEntityTypes.STRUCTURE_CORE.get(), StructureCoreBlockEntity::serverTick) : null;
	}
	
	@Override
	public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand)
	{
		if(stateIn.getValue(POWERED))
			BlockUtil.spawnParticlesAroundSolidBlock(level, pos, () -> DustParticleOptions.REDSTONE);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
		builder.add(POWERED);
		builder.add(ACTIVE);
	}
}