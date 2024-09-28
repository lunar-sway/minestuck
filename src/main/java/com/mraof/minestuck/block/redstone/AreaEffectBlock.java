package com.mraof.minestuck.block.redstone;

import com.mojang.serialization.MapCodec;
import com.mraof.minestuck.block.BlockUtil;
import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.blockentity.redstone.AreaEffectBlockEntity;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.effects.CreativeShockEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;
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

import javax.annotation.Nullable;

/**
 * When powered, the block entity applies an effect to entities in a designated area between two block pos, similar to beacons but with more versatility.
 * Only creative mode players(who are not under the effects of Creative Shock) can change the effect
 */
public class AreaEffectBlock extends HorizontalDirectionalBlock implements EntityBlock
{
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final BooleanProperty ALL_MOBS = MSProperties.MACHINE_TOGGLE; //checks whether just players should be given the effect or if all living entities should be given the effect
	public static final BooleanProperty SHUT_DOWN = MSProperties.SHUT_DOWN;
	public static final String EFFECT_CHANGE_MESSAGE = "effect_change_message";
	
	public AreaEffectBlock(Properties properties)
	{
		super(properties);
		this.registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, false).setValue(ALL_MOBS, false).setValue(SHUT_DOWN, false));
	}
	
	@Override
	protected MapCodec<AreaEffectBlock> codec()
	{
		return null; //todo
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new AreaEffectBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> placedType)
	{
		return !level.isClientSide ? BlockUtil.checkTypeForTicker(placedType, MSBlockEntityTypes.AREA_EFFECT.get(), AreaEffectBlockEntity::serverTick) : null;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		if(!canInteract(player) || !(level.getBlockEntity(pos) instanceof AreaEffectBlockEntity be))
			return InteractionResult.PASS;
		
		ItemStack heldItemStack = player.getItemInHand(hand);
		
		if(heldItemStack.getItem() instanceof PotionItem)
		{
			clickWithPotion(level, pos, player, be, heldItemStack);
		} else
		{
			if(level.isClientSide)
				MSScreenFactories.displayAreaEffectScreen(be);
		}
		
		return InteractionResult.sidedSuccess(level.isClientSide);
	}
	
	public static boolean canInteract(Player player)
	{
		return player.isCreative() && !CreativeShockEffect.doesCreativeShockLimit(player, CreativeShockEffect.LIMIT_MACHINE_INTERACTIONS);
	}
	
	private void clickWithPotion(Level level, BlockPos pos, Player player, AreaEffectBlockEntity be, ItemStack potionStack)
	{
		MobEffectInstance firstEffect = PotionUtils.getPotion(potionStack).getEffects().get(0);
		if(firstEffect != null && !level.isClientSide)
		{
			be.setEffect(firstEffect.getEffect(), firstEffect.getAmplifier());
			
			player.displayClientMessage(Component.translatable(getDescriptionId() + "." + EFFECT_CHANGE_MESSAGE,
					BuiltInRegistries.MOB_EFFECT.getKey(firstEffect.getEffect()), firstEffect.getAmplifier()), true);
			level.playSound(null, pos, SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.BLOCKS, 0.5F, 1F);
		}
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
			boolean hasPower = !state.getValue(SHUT_DOWN) && level.hasNeighborSignal(pos);
			
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
		if(stateIn.getValue(POWERED))
			BlockUtil.spawnParticlesAroundSolidBlock(level, pos, () -> DustParticleOptions.REDSTONE);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
		builder.add(POWERED);
		builder.add(SHUT_DOWN);
		builder.add(ALL_MOBS);
	}
}