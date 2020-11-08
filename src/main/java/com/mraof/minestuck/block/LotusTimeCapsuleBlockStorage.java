package com.mraof.minestuck.block;

import com.mraof.minestuck.item.CassetteItem;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.tileentity.CassettePlayerTileEntity;
import com.mraof.minestuck.tileentity.LotusTimeCapsuleTileEntity;
import com.mraof.minestuck.util.CustomVoxelShape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class LotusTimeCapsuleBlockStorage extends Block
{
	public static final BooleanProperty UNACTIVATED = MSProperties.UNACTIVATED;
	
	public LotusTimeCapsuleBlockStorage(Properties properties, CustomVoxelShape shape)
	{
		super(properties); //", shape" removed
		this.setDefaultState(this.stateContainer.getBaseState());
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		state = state.getBlockState();
		TileEntity tileentity = worldIn.getTileEntity(pos);
		worldIn.setBlockState(pos, state, 2);
		if(tileentity instanceof LotusTimeCapsuleTileEntity && !state.get(UNACTIVATED))
		{
			ItemStack itemStack = new ItemStack(MSItems.SERVER_DISK);
			worldIn.playEvent(Constants.WorldEvents.PLAY_RECORD_SOUND, pos, Item.getIdFromItem(itemStack.getItem()));
			if(player != null)
			{
				player.addStat(Stats.PLAY_RECORD);
			}
		} else if(tileentity instanceof LotusTimeCapsuleTileEntity && state.get(UNACTIVATED))
		{
			worldIn.playEvent(Constants.WorldEvents.PLAY_RECORD_SOUND, pos, 0);
		}
		return true;
	}
	
	//public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
	//		if(UNACTIVATED.equals(false))
	//		{
	//			ItemStack itemstack = player.getHeldItem(handIn);
	//			if(itemstack.isEmpty())
	//			{
	//				if(!worldIn.isRemote)
	//				{
	//					Direction direction = hit.getFace();
	//					Direction direction1 = direction.getAxis() == Direction.Axis.Y ? player.getHorizontalFacing().getOpposite() : direction;
	//					worldIn.playSound((PlayerEntity) null, pos, MSSoundEvents.EVENT_ECHELADDER_INCREASE, SoundCategory.BLOCKS, 1.0F, 1.0F);
	//					worldIn.setBlockState(pos, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.getDefaultState().with(LotusTimeCapsuleBlock.UNACTIVATED, false), 11);
	//					ItemEntity itementity = new ItemEntity(worldIn, (double) pos.getX() + 0.5D + (double) direction1.getXOffset() * 0.65D, (double) pos.getY() + 0.1D, (double) pos.getZ() + 0.5D + (double) direction1.getZOffset() * 0.65D, new ItemStack(MSItems.SERVER_DISK, 1));
	//					itementity.setMotion(0.05D * (double) direction1.getXOffset() + worldIn.rand.nextDouble() * 0.02D, 0.05D, 0.05D * (double) direction1.getZOffset() + worldIn.rand.nextDouble() * 0.02D);
	//					worldIn.addEntity(itementity);
	//				}
	//				return true;
	//			} else
	//			{
	//				return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
	//			}
	//		}
	//		return true;
	//	}
	
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
					worldIn.playEvent(Constants.WorldEvents.PLAY_RECORD_SOUND, pos, 0);
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
		return new CassettePlayerTileEntity();
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
		builder.add(UNACTIVATED);
	}
}
	
	
	//ItemStack itemstack = player.getHeldItem(handIn);
	//		if (itemstack.isEmpty()) {
	//			if (!worldIn.isRemote) {
	//				Direction direction = hit.getFace();
	//				Direction direction1 = direction.getAxis() == Direction.Axis.Y ? player.getHorizontalFacing().getOpposite() : direction;
	//				worldIn.playSound((PlayerEntity)null, pos, MSSoundEvents.EVENT_ECHELADDER_INCREASE, SoundCategory.BLOCKS, 1.0F, 1.0F);
	//				//worldIn.setBlockState(pos, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.getDefaultState().with(LotusTimeCapsuleBlock.FACING, direction1), 11);
	//				worldIn.setBlockState(pos, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.stateContainer
	//						ItemEntity itementity = new ItemEntity(worldIn, (double)pos.getX() + 0.5D + (double)direction1.getXOffset() * 0.65D, (double)pos.getY() + 0.1D, (double)pos.getZ() + 0.5D + (double)direction1.getZOffset() * 0.65D, new ItemStack(Items.PUMPKIN_SEEDS, 4));
	//				itementity.setMotion(0.05D * (double)direction1.getXOffset() + worldIn.rand.nextDouble() * 0.02D, 0.05D, 0.05D * (double)direction1.getZOffset() + worldIn.rand.nextDouble() * 0.02D);
	//				worldIn.addEntity(itementity);
	//			}
	//			return true;
	//		} else {
	//			return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
	//		}
	
