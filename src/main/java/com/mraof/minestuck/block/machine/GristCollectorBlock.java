package com.mraof.minestuck.block.machine;

import com.mojang.serialization.MapCodec;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.block.BlockUtil;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.blockentity.machine.GristCollectorBlockEntity;
import com.mraof.minestuck.player.GristCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
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
import net.neoforged.neoforge.common.util.FakePlayer;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public class GristCollectorBlock extends HorizontalDirectionalBlock implements EntityBlock
{
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	
	public GristCollectorBlock(Properties properties)
	{
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, false));
	}
	
	@Override
	protected MapCodec<GristCollectorBlock> codec()
	{
		return null; //todo
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit)
	{
		//when right-clicked by a player, all the grist collected by the block entity is transferred
		if(player instanceof ServerPlayer serverPlayer && !(player instanceof FakePlayer) && level.getBlockEntity(pos) instanceof GristCollectorBlockEntity collector)
		{
			GristSet storedGrist = collector.getStoredGrist();
			
			if(!storedGrist.isEmpty())
			{
				GristCache.get(serverPlayer).addWithGutter(storedGrist, GristHelper.EnumSource.CLIENT);
				collector.clearStoredGrist();
				
				level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.1F, 0.5F * ((level.random.nextFloat() - level.random.nextFloat()) * 0.7F + 1.8F));
			}
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new GristCollectorBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> placedType)
	{
		return !level.isClientSide ? BlockUtil.checkTypeForTicker(placedType, MSBlockEntityTypes.GRIST_COLLECTOR.get(), GristCollectorBlockEntity::serverTick) : null;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		super.neighborChanged(state, level, pos, blockIn, fromPos, isMoving);
		updatePower(level, pos);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		super.onPlace(state, level, pos, oldState, isMoving);
		updatePower(level, pos);
	}
	
	public void updatePower(Level level, BlockPos pos)
	{
		if(!level.isClientSide)
		{
			BlockState state = level.getBlockState(pos);
			boolean hasPower = level.hasNeighborSignal(pos);
			
			if(state.getValue(POWERED) != hasPower)
				level.setBlockAndUpdate(pos, state.setValue(POWERED, hasPower));
		}
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}
	
	@Override
	public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand)
	{
		//green particles when active/unpowered and redstone particles when inactive/powered
		if(stateIn.getValue(POWERED))
			BlockUtil.spawnParticlesAroundSolidBlock(level, pos, () -> DustParticleOptions.REDSTONE);
		else
			BlockUtil.spawnParticlesAroundSolidBlock(level, pos, () -> new DustParticleOptions(new Vector3f(0F, 20F, 0F), 2F)); //green
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
		builder.add(POWERED);
	}
}