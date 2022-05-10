package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.effects.CreativeShockEffect;
import com.mraof.minestuck.tileentity.redstone.SummonerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

/**
 * Summons an entity stored in tile entity when powered by redstone. If the blockstate untriggerable is set to true, it can summon an entity multiple times
 * Only creative mode players(who are not under the effects of Creative Shock) can change the set mob
 */
public class SummonerBlock extends Block
{
	public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
	public static final BooleanProperty UNTRIGGERABLE = BlockStateProperties.ENABLED;
	
	public static final String UNTRIGGERABLE_CHANGE_MESSAGE = "untriggerable_change_message";
	
	public SummonerBlock(Properties properties)
	{
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(UNTRIGGERABLE, false).setValue(TRIGGERED, false));
	}
	
	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		if(player.isCreative() && !CreativeShockEffect.doesCreativeShockLimit(player, CreativeShockEffect.LIMIT_MACHINE_INTERACTIONS))
		{
			ItemStack stackIn = player.getItemInHand(handIn);
			
			if(stackIn.getItem() instanceof SpawnEggItem)
			{
				TileEntity tileEntity = worldIn.getBlockEntity(pos);
				if(tileEntity instanceof SummonerTileEntity)
				{
					SummonerTileEntity summonerTE = (SummonerTileEntity) tileEntity;
					SpawnEggItem eggItem = (SpawnEggItem) stackIn.getItem();
					
					if(!worldIn.isClientSide)
						summonerTE.setSummonedEntity(eggItem.getType(stackIn.getTag()), player);
				}
			} else if(!worldIn.isClientSide)
			{
				boolean newBooleanState = !worldIn.getBlockState(pos).getValue(UNTRIGGERABLE);
				worldIn.setBlock(pos, worldIn.getBlockState(pos).cycle(SummonerBlock.UNTRIGGERABLE), Constants.BlockFlags.DEFAULT);
				player.displayClientMessage(new TranslationTextComponent(getDescriptionId() + "." + UNTRIGGERABLE_CHANGE_MESSAGE, !newBooleanState), true);
			}
			
			worldIn.playSound(player, pos, SoundEvents.UI_BUTTON_CLICK, SoundCategory.BLOCKS, 0.5F, 1F);
			
			return ActionResultType.SUCCESS;
		}
		
		return ActionResultType.PASS;
	}
	
	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
		checkSummon(state, worldIn, pos);
	}
	
	@Override
	public void onPlace(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		checkSummon(state, worldIn, pos); //made to work with the iterateTracker check in SummonerTileEntity
	}
	
	private void checkSummon(BlockState state, World worldIn, BlockPos pos)
	{
		boolean blockPowered = worldIn.hasNeighborSignal(pos) || worldIn.hasNeighborSignal(pos.above()); //conditions of: 1. block is powered 2. block above is powered 3. shouldnt care if powered
		
		if(!worldIn.isClientSide && blockPowered && (!state.getValue(TRIGGERED) || state.getValue(UNTRIGGERABLE)))
		{
			TileEntity tileEntity = worldIn.getBlockEntity(pos);
			if(tileEntity instanceof SummonerTileEntity)
			{
				SummonerTileEntity summonerTE = (SummonerTileEntity) tileEntity;
				summonerTE.summonEntity(worldIn, pos, summonerTE.getSummonedEntity(), !state.getValue(UNTRIGGERABLE), true);
			}
		}
	}
	
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new SummonerTileEntity();
	}
	
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(TRIGGERED);
		builder.add(UNTRIGGERABLE);
	}
}