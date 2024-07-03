package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.block.BlockUtil;
import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.blockentity.redstone.SummonerBlockEntity;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.effects.CreativeShockEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
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
 * Summons an entity stored in block entity when powered by redstone. If the blockstate untriggerable is set to true, it can summon an entity multiple times
 * Only creative mode players(who are not under the effects of Creative Shock) can change the set mob
 */
public class SummonerBlock extends Block implements EntityBlock
{
	public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
	public static final BooleanProperty UNTRIGGERABLE = MSProperties.UNTRIGGERABLE;
	public static final String SUMMON_TYPE_CHANGE = "block.minestuck.summoner_block.summon_type_change";
	
	public SummonerBlock(Properties properties)
	{
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(UNTRIGGERABLE, false).setValue(TRIGGERED, false));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit)
	{
		if(!canInteract(player) || !(level.getBlockEntity(pos) instanceof SummonerBlockEntity summoner))
			return InteractionResult.PASS;
		
		ItemStack stackIn = player.getItemInHand(handIn);
		
		if(stackIn.getItem() instanceof SpawnEggItem eggItem)
		{
			if(!level.isClientSide)
			{
				summoner.setSummonedEntity(eggItem.getType(stackIn.getTag()));
				player.displayClientMessage(Component.translatable(SUMMON_TYPE_CHANGE, BuiltInRegistries.ENTITY_TYPE.getKey(eggItem.getType(stackIn.getTag()))), true);
			}
			
			level.playSound(player, pos, SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.BLOCKS, 0.5F, 1F);
		} else if(level.isClientSide)
		{
			MSScreenFactories.displaySummonerScreen(summoner);
		}
		
		return InteractionResult.SUCCESS;
	}
	
	public static boolean canInteract(Player player)
	{
		return player.isCreative() && !CreativeShockEffect.doesCreativeShockLimit(player, CreativeShockEffect.LIMIT_MACHINE_INTERACTIONS);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		super.neighborChanged(state, level, pos, blockIn, fromPos, isMoving);
		checkSummon(state, level, pos);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		checkSummon(state, level, pos); //made to work with the iterateTracker check in SummonerBlockEntity
	}
	
	private void checkSummon(BlockState state, Level level, BlockPos pos)
	{
		boolean blockPowered = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above()); //conditions of: 1. block is powered 2. block above is powered 3. shouldnt care if powered
		
		if(!level.isClientSide && blockPowered && (!state.getValue(TRIGGERED) || state.getValue(UNTRIGGERABLE)))
		{
			if(level.getBlockEntity(pos) instanceof SummonerBlockEntity summoner)
			{
				summoner.summonEntity(level, pos, summoner.getSummonedEntity(), !state.getValue(UNTRIGGERABLE), true);
			}
		}
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new SummonerBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> placedType)
	{
		return !level.isClientSide ? BlockUtil.checkTypeForTicker(placedType, MSBlockEntityTypes.SUMMONER.get(), SummonerBlockEntity::serverTick) : null;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(TRIGGERED);
		builder.add(UNTRIGGERABLE);
	}
}