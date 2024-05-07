package com.mraof.minestuck.block;

import com.mraof.minestuck.blockentity.CassettePlayerBlockEntity;
import com.mraof.minestuck.item.CassetteItem;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class CassettePlayerBlock extends CustomShapeBlock implements EntityBlock
{
	public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
	public static final EnumProperty<EnumCassetteType> CASSETTE = MSProperties.CASSETTE;
	
	public CassettePlayerBlock(Properties properties, CustomVoxelShape shape)
	{
		super(properties, shape);
		this.registerDefaultState(defaultBlockState().setValue(CASSETTE, EnumCassetteType.NONE)); //defaultState set in decor block has waterlogged
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit)
	{
		if(player.isShiftKeyDown())
		{
			state = state.cycle(OPEN);
			level.setBlock(pos, state, 2);
			if(level.getBlockEntity(pos) instanceof CassettePlayerBlockEntity cassettePlayer)
			{
				if(!state.getValue(OPEN))
				{
					ItemStack itemStack = cassettePlayer.getCassette();
					level.levelEvent(LevelEvent.SOUND_PLAY_JUKEBOX_SONG, pos, Item.getId(itemStack.getItem()));
					player.awardStat(Stats.PLAY_RECORD);
				} else if(state.getValue(OPEN))
				{
					level.levelEvent(LevelEvent.SOUND_STOP_JUKEBOX_SONG, pos, 0);
				}
			}
			return InteractionResult.SUCCESS;
		} else if(state.getValue(CASSETTE) != EnumCassetteType.NONE && state.getValue(OPEN))
		{
			this.dropCassette(level, pos);
			state = state.setValue(CASSETTE, EnumCassetteType.NONE);
			level.setBlock(pos, state, 2);
			return InteractionResult.SUCCESS;
		} else
		{
			return InteractionResult.PASS;
		}
	}
	
	public void insertCassette(LevelAccessor level, BlockPos pos, BlockState state, ItemStack cassetteStack)
	{
		if(level.getBlockEntity(pos) instanceof CassettePlayerBlockEntity cassettePlayer
				&& state.getValue(OPEN) && state.getValue(CASSETTE) == EnumCassetteType.NONE)
		{
			cassettePlayer.setCassette(cassetteStack.copy());
			if(cassetteStack.getItem() instanceof CassetteItem cassette)
			{
				level.setBlock(pos, state.setValue(CASSETTE, cassette.cassetteID), 2);
			}
		}
	}
	
	private void dropCassette(Level level, BlockPos pos)
	{
		if(!level.isClientSide)
		{
			if(level.getBlockEntity(pos) instanceof CassettePlayerBlockEntity cassettePlayer)
			{
				ItemStack itemstack = cassettePlayer.getCassette();
				if(!itemstack.isEmpty())
				{
					level.levelEvent(LevelEvent.SOUND_STOP_JUKEBOX_SONG, pos, 0);
					cassettePlayer.clearContent();
					float f = 0.7F;
					double xOffset = f * level.random.nextFloat() + 0.15;
					double yOffset = f * level.random.nextFloat() + 0.66;
					double zOffset = f * level.random.nextFloat() + 0.15;
					ItemStack itemstack1 = itemstack.copy();
					ItemEntity itementity = new ItemEntity(level, (double) pos.getX() + xOffset, (double) pos.getY() + yOffset, (double) pos.getZ() + zOffset, itemstack1);
					itementity.setDefaultPickUpDelay();
					level.addFreshEntity(itementity);
				}
			}
		}
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if(state.getBlock() != newState.getBlock())
		{
			this.dropCassette(level, pos);
			super.onRemove(state, level, pos, newState, isMoving);
		}
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new CassettePlayerBlockEntity(pos, state);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean hasAnalogOutputSignal(BlockState state)
	{
		return true;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos)
	{
		if(level.getBlockEntity(pos) instanceof CassettePlayerBlockEntity cassettePlayer)
		{
			Item item = cassettePlayer.getCassette().getItem();
			if(item instanceof CassetteItem cassette)
			{
				return cassette.getAnalogOutput();
			}
		}
		
		return 0;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(CASSETTE);
		builder.add(OPEN);
	}
}
