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

import javax.annotation.Nullable;

public class SummonerBlock extends Block
{
	public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
	public static final BooleanProperty UNTRIGGERABLE = BlockStateProperties.ENABLED;
	
	public static final String UNTRIGGERABLE_CHANGE_MESSAGE = "untriggerable_change_message";
	
	public SummonerBlock(Properties properties)
	{
		super(properties);
		setDefaultState(getDefaultState().with(TRIGGERED, false).with(UNTRIGGERABLE, false));
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		if(!player.isSneaking() && player.isCreative() && !CreativeShockEffect.doesCreativeShockLimit(player, 1, 4))
		{
			ItemStack stackIn = player.getHeldItem(handIn);
			
			if(stackIn.getItem() instanceof SpawnEggItem)
			{
				TileEntity tileEntity = worldIn.getTileEntity(pos);
				if(tileEntity instanceof SummonerTileEntity)
				{
					SummonerTileEntity summonerTE = (SummonerTileEntity) tileEntity;
					SpawnEggItem eggItem = (SpawnEggItem) stackIn.getItem();
					
					summonerTE.setSummonedEntity(eggItem.getType(stackIn.getTag()), player); //TODO does not work for certain mobs(so far, tested mobs that dont work are Drowned and Slimes), may have to do with conditional placement
				}
			} else
			{
				boolean newBooleanState = !worldIn.getBlockState(pos).get(UNTRIGGERABLE);
				worldIn.setBlockState(pos, worldIn.getBlockState(pos).with(SummonerBlock.UNTRIGGERABLE, newBooleanState), 4);
				player.sendStatusMessage(new TranslationTextComponent(getTranslationKey() + "." + UNTRIGGERABLE_CHANGE_MESSAGE, !newBooleanState), true);
			}
			
			worldIn.playSound(null, pos, SoundEvents.UI_BUTTON_CLICK, SoundCategory.BLOCKS, 0.5F, 1F);
			
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
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		checkSummon(state, worldIn, pos); //made to work with the iterateTracker check in SummonerTileEntity
	}
	
	private void checkSummon(BlockState state, World worldIn, BlockPos pos)
	{
		boolean blockPowered = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos.up()); //conditions of: 1. block is powered 2. block above is powered 3. shouldnt care if powered
		
		if(!worldIn.isRemote && blockPowered && (!state.get(TRIGGERED) || state.get(UNTRIGGERABLE)))
		{
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if(tileEntity instanceof SummonerTileEntity)
			{
				SummonerTileEntity summonerTE = (SummonerTileEntity) tileEntity;
				summonerTE.summonEntity(worldIn, pos, summonerTE.getSummonedEntity(), !state.get(UNTRIGGERABLE), true);
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
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		super.fillStateContainer(builder);
		builder.add(TRIGGERED);
		builder.add(UNTRIGGERABLE);
	}
}