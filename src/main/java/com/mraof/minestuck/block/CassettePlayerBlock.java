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
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Supplier;


public class CassettePlayerBlock extends DecorBlock
{
	public static final BooleanProperty HAS_CASSETTE = MSProperties.HAS_CASSETTE;
	public static final BooleanProperty OPEN = MSProperties.OPEN;
	private final Supplier<TileEntityType<?>> tileType;
	
	public CassettePlayerBlock(Properties properties, CustomVoxelShape shape, Supplier<TileEntityType<?>> tileType)
	{
		super(properties, shape);
		this.setDefaultState(this.stateContainer.getBaseState().with(HAS_CASSETTE, false));
		this.tileType = tileType;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		if(player.isSneaking())
		{
			state = state.cycle(OPEN);
			worldIn.setBlockState(pos, state, 2);
			return true;
		} else if(state.get(HAS_CASSETTE) && state.get(OPEN))
		{
			this.dropCassette(worldIn, pos);
			state = state.with(HAS_CASSETTE, false);
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
		if(tileentity instanceof CassettePlayerTileEntity && state.get(OPEN))
		{
			if(state.get(HAS_CASSETTE))
			{
				this.dropCassette((World) worldIn, pos);
			} else
			{
				((CassettePlayerTileEntity) tileentity).setCassette(cassetteStack.copy());
				worldIn.setBlockState(pos, state.with(HAS_CASSETTE, true), 2);
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
				CassettePlayerTileEntity cassetteplayertileentity = (CassettePlayerTileEntity) tileentity;
				ItemStack itemstack = cassetteplayertileentity.getCassette();
				if(!itemstack.isEmpty())
				{
					worldIn.playEvent(1010, pos, 0);
					cassetteplayertileentity.clear();
					float f = 0.7F;
					double randomX = (double) (worldIn.rand.nextFloat() * 0.7F) + (double) 0.15F;
					double randomY = (double) (worldIn.rand.nextFloat() * 0.7F) + (double) 0.060000002F + 0.6D;
					double randomZ = (double) (worldIn.rand.nextFloat() * 0.7F) + (double) 0.15F;
					ItemStack itemstack1 = itemstack.copy();
					ItemEntity itementity = new ItemEntity(worldIn, (double) pos.getX() + randomX, (double) pos.getY() + randomY, (double) pos.getZ() + randomZ, itemstack1);
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
		builder.add(HAS_CASSETTE);
		builder.add(OPEN);
	}
}
