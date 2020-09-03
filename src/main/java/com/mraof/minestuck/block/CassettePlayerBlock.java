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
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.function.Supplier;


public class CassettePlayerBlock extends DecorBlock
{
	public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
	public static final EnumProperty<EnumCassetteType> CASSETTE = MSProperties.CASSETTE;
	private final Supplier<TileEntityType<?>> tileType;
	
	public CassettePlayerBlock(Properties properties, CustomVoxelShape shape, Supplier<TileEntityType<?>> tileType)
	{
		super(properties, shape);
		this.setDefaultState(this.stateContainer.getBaseState().with(CASSETTE, EnumCassetteType.NONE));
		this.tileType = tileType;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		if(player.isSneaking())
		{
			state = state.cycle(OPEN);
			TileEntity tileentity = worldIn.getTileEntity(pos);
			ItemStack itemStack = ((CassettePlayerTileEntity) tileentity).getCassette();
			worldIn.setBlockState(pos, state, 2);
			if(tileentity instanceof CassettePlayerTileEntity && !state.get(OPEN))
			{
				worldIn.playEvent((PlayerEntity) null, Constants.WorldEvents.PLAY_RECORD_SOUND, pos, Item.getIdFromItem(itemStack.getItem()));
				if(player != null)
				{
					player.addStat(Stats.PLAY_RECORD);
				}
			} else if(tileentity instanceof CassettePlayerTileEntity && state.get(OPEN))
			{
				worldIn.playEvent(1010, pos, 0);
			}
			return true;
		} else if(state.get(CASSETTE) != EnumCassetteType.NONE && state.get(OPEN))
		{
			this.dropCassette(worldIn, pos);
			state = state.with(CASSETTE, EnumCassetteType.NONE);
			worldIn.setBlockState(pos, state, 2);
			return true;
		} else
		{
			return false;
		}
	}
	
	public void insertCassette(IWorld worldIn, BlockPos pos, BlockState state, ItemStack cassetteStack)
	{
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if(tileentity instanceof CassettePlayerTileEntity && state.get(OPEN) && state.get(CASSETTE) == EnumCassetteType.NONE)
		{
			((CassettePlayerTileEntity) tileentity).setCassette(cassetteStack.copy());
			if(cassetteStack.getItem() instanceof CassetteItem)
			{
				worldIn.setBlockState(pos, state.with(CASSETTE, ((CassetteItem) cassetteStack.getItem()).cassetteID), 2);
			}
		}
	}
	
	private void dropCassette(World worldIn, BlockPos pos)
	{
		if(!worldIn.isRemote)
		{
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if(tileentity instanceof CassettePlayerTileEntity)
			{
				CassettePlayerTileEntity cassettePlayer = (CassettePlayerTileEntity) tileentity;
				ItemStack itemstack = cassettePlayer.getCassette();
				if(!itemstack.isEmpty())
				{
					worldIn.playEvent(1010, pos, 0);
					cassettePlayer.clear();
					float f = 0.7F;
					double xOffset = f * worldIn.rand.nextFloat() + 0.15;
					double yOffset = f * worldIn.rand.nextFloat() + 0.66;
					double zOffset = f * worldIn.rand.nextFloat() + 0.15;
					ItemStack itemstack1 = itemstack.copy();
					ItemEntity itementity = new ItemEntity(worldIn, (double) pos.getX() + xOffset, (double) pos.getY() + yOffset, (double) pos.getZ() + zOffset, itemstack1);
					itementity.setDefaultPickupDelay();
					worldIn.addEntity(itementity);
				}
			}
		}
	}
	
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if(state.getBlock() != newState.getBlock())
		{
			this.dropCassette(worldIn, pos);
			super.onReplaced(state, worldIn, pos, newState, isMoving);
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
		return tileType.get().create();
	}
	
	public boolean hasComparatorInputOverride(BlockState state)
	{
		return true;
	}
	
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos)
	{
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if(tileentity instanceof CassettePlayerTileEntity)
		{
			Item item = ((CassettePlayerTileEntity) tileentity).getCassette().getItem();
			if(item instanceof CassetteItem)
			{
				return ((CassetteItem) item).getComparatorValue();
			}
		}
		
		return 0;
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(FACING);
		builder.add(CASSETTE);
		builder.add(OPEN);
	}
}
