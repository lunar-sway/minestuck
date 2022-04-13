package com.mraof.minestuck.block;

import com.mraof.minestuck.item.CassetteItem;
import com.mraof.minestuck.tileentity.CassettePlayerTileEntity;
import com.mraof.minestuck.util.CustomVoxelShape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class CassettePlayerBlock extends CustomShapeBlock
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
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		if(player.isShiftKeyDown())
		{
			state = state.cycle(OPEN);
			TileEntity tileentity = worldIn.getBlockEntity(pos);
			worldIn.setBlock(pos, state, 2);
			if(tileentity instanceof CassettePlayerTileEntity && !state.getValue(OPEN))
			{
				ItemStack itemStack = ((CassettePlayerTileEntity) tileentity).getCassette();
				worldIn.levelEvent(Constants.WorldEvents.PLAY_RECORD_SOUND, pos, Item.getId(itemStack.getItem()));
				if(player != null)
				{
					player.awardStat(Stats.PLAY_RECORD);
				}
			} else if(tileentity instanceof CassettePlayerTileEntity && state.getValue(OPEN))
			{
				worldIn.levelEvent(Constants.WorldEvents.PLAY_RECORD_SOUND, pos, 0);
			}
			return ActionResultType.SUCCESS;
		} else if(state.getValue(CASSETTE) != EnumCassetteType.NONE && state.getValue(OPEN))
		{
			this.dropCassette(worldIn, pos);
			state = state.setValue(CASSETTE, EnumCassetteType.NONE);
			worldIn.setBlock(pos, state, 2);
			return ActionResultType.SUCCESS;
		} else
		{
			return ActionResultType.PASS;
		}
	}
	
	public void insertCassette(IWorld worldIn, BlockPos pos, BlockState state, ItemStack cassetteStack)
	{
		TileEntity tileentity = worldIn.getBlockEntity(pos);
		if(tileentity instanceof CassettePlayerTileEntity && state.getValue(OPEN) && state.getValue(CASSETTE) == EnumCassetteType.NONE)
		{
			((CassettePlayerTileEntity) tileentity).setCassette(cassetteStack.copy());
			if(cassetteStack.getItem() instanceof CassetteItem)
			{
				worldIn.setBlock(pos, state.setValue(CASSETTE, ((CassetteItem) cassetteStack.getItem()).cassetteID), 2);
			}
		}
	}
	
	private void dropCassette(World worldIn, BlockPos pos)
	{
		if(!worldIn.isClientSide)
		{
			TileEntity tileentity = worldIn.getBlockEntity(pos);
			if(tileentity instanceof CassettePlayerTileEntity)
			{
				CassettePlayerTileEntity cassettePlayer = (CassettePlayerTileEntity) tileentity;
				ItemStack itemstack = cassettePlayer.getCassette();
				if(!itemstack.isEmpty())
				{
					worldIn.levelEvent(Constants.WorldEvents.PLAY_RECORD_SOUND, pos, 0);
					cassettePlayer.clearContent();
					float f = 0.7F;
					double xOffset = f * worldIn.random.nextFloat() + 0.15;
					double yOffset = f * worldIn.random.nextFloat() + 0.66;
					double zOffset = f * worldIn.random.nextFloat() + 0.15;
					ItemStack itemstack1 = itemstack.copy();
					ItemEntity itementity = new ItemEntity(worldIn, (double) pos.getX() + xOffset, (double) pos.getY() + yOffset, (double) pos.getZ() + zOffset, itemstack1);
					itementity.setDefaultPickUpDelay();
					worldIn.addFreshEntity(itementity);
				}
			}
		}
	}
	
	public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if(state.getBlock() != newState.getBlock())
		{
			this.dropCassette(worldIn, pos);
			super.onRemove(state, worldIn, pos, newState, isMoving);
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
		return new CassettePlayerTileEntity();
	}
	
	public boolean hasAnalogOutputSignal(BlockState state)
	{
		return true;
	}
	
	public int getAnalogOutputSignal(BlockState blockState, World worldIn, BlockPos pos)
	{
		TileEntity tileentity = worldIn.getBlockEntity(pos);
		if(tileentity instanceof CassettePlayerTileEntity)
		{
			Item item = ((CassettePlayerTileEntity) tileentity).getCassette().getItem();
			if(item instanceof CassetteItem)
			{
				return ((CassetteItem) item).getAnalogOutput();
			}
		}
		
		return 0;
	}
	
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(CASSETTE);
		builder.add(OPEN);
	}
}
