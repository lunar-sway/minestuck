package com.mraof.minestuck.block;

import com.mraof.minestuck.block.multiblock.MachineMultiblock;
import com.mraof.minestuck.item.CassetteItem;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.tileentity.CassettePlayerTileEntity;
import com.mraof.minestuck.tileentity.CruxtruderTileEntity;
import com.mraof.minestuck.tileentity.LotusTimeCapsuleTileEntity;
import com.mraof.minestuck.util.CustomVoxelShape;
import com.mraof.minestuck.util.MSRotationUtil;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.block.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Random;

public class LotusTimeCapsuleBlock extends Block
{
	public static final BooleanProperty UNACTIVATED = MSProperties.UNACTIVATED;
	
	protected LotusTimeCapsuleBlock(Block.Properties builder) {
		super(builder);
	}
	
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if(UNACTIVATED.equals(false))
		{
			ItemStack itemstack = player.getHeldItem(handIn);
			if(itemstack.isEmpty())
			{
				if(!worldIn.isRemote)
				{
					Direction direction = hit.getFace();
					Direction direction1 = direction.getAxis() == Direction.Axis.Y ? player.getHorizontalFacing().getOpposite() : direction;
					worldIn.playSound((PlayerEntity) null, pos, MSSoundEvents.EVENT_ECHELADDER_INCREASE, SoundCategory.BLOCKS, 1.0F, 1.0F);
					worldIn.setBlockState(pos, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.getDefaultState().with(LotusTimeCapsuleBlock.UNACTIVATED, false), 11);
					ItemEntity itementity = new ItemEntity(worldIn, (double) pos.getX() + 0.5D + (double) direction1.getXOffset() * 0.65D, (double) pos.getY() + 0.1D, (double) pos.getZ() + 0.5D + (double) direction1.getZOffset() * 0.65D, new ItemStack(MSItems.SERVER_DISK, 1));
					itementity.setMotion(0.05D * (double) direction1.getXOffset() + worldIn.rand.nextDouble() * 0.02D, 0.05D, 0.05D * (double) direction1.getZOffset() + worldIn.rand.nextDouble() * 0.02D);
					worldIn.addEntity(itementity);
				}
				return true;
			} else
			{
				return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
			}
		}
		return true;
	}
	
	public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
		super.tick(state, worldIn, pos, random);
		if (random.nextInt(7) == 0) {
			//worldIn.setBlockState(pos, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.getDefaultState().with(LotusTimeCapsuleBlock.UNACTIVATED, true), 11);
			worldIn.setBlockState(pos, MSBlocks.MINI_FROG_STATUE.getDefaultState());
		}
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
	
