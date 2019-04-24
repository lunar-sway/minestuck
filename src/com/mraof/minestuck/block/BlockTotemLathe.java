package com.mraof.minestuck.block;

import com.mraof.minestuck.tileentity.TileEntityItemStack;
import com.mraof.minestuck.tileentity.TileEntityTotemLathe;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Map;

public class BlockTotemLathe extends BlockMachine
{
	public static final Map<EnumFacing, VoxelShape> CARD_SLOT_SHAPE = createRotatedShapes(0, 0, 1, 16, 16, 13);
	public static final Map<EnumFacing, VoxelShape> BOTTOM_LEFT_SHAPE = createRotatedShapes(0, 0, 0, 16, 16, 12);
	public static final Map<EnumFacing, VoxelShape> BOTTOM_RIGHT_SHAPE = createRotatedShapes(0, 0, 0, 16, 12, 12);
	public static final Map<EnumFacing, VoxelShape> BOTTOM_CORNER_SHAPE = createRotatedShapes(0, 0, 1, 12, 16, 11);
	public static final Map<EnumFacing, VoxelShape> MIDDLE_SHAPE = createRotatedShapes(0, 0, 1, 16, 16, 12);
	public static final Map<EnumFacing, VoxelShape> WHEEL_SHAPE = createRotatedShapes(0, 0, 3, 14, 10, 9);
	public static final Map<EnumFacing, VoxelShape> ROD_SHAPE = createRotatedShapes(0, 4, 3, 16, 10, 9);
	public static final Map<EnumFacing, VoxelShape> TOP_CORNER_SHAPE = createRotatedShapes(0, 0, 1, 16, 16, 11);
	public static final Map<EnumFacing, VoxelShape> TOP_SHAPE = createRotatedShapes(0, 3, 1, 16, 16, 11);
	public static final Map<EnumFacing, VoxelShape> CARVER_SHAPE = createRotatedShapes(10, 0, 1, 16, 16, 11);
	
	
	protected final Map<EnumFacing, VoxelShape> shape;
	protected final BlockPos mainPos;
	
	public BlockTotemLathe(Properties properties, Map<EnumFacing, VoxelShape> shape, BlockPos mainPos)
	{
		super(properties);
		this.shape = shape;
		this.mainPos = mainPos;
	}
	
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return shape.get(state.get(FACING));
	}
	
	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if(worldIn.isRemote)
			return true;
		
		BlockPos mainPos = getMainPos(state, pos);
		TileEntity te = worldIn.getTileEntity(mainPos);
		if(te instanceof TileEntityTotemLathe)
			((TileEntityTotemLathe) te).onRightClick(player, state);
		return true;
	}
	
	@Override
	public void onReplaced(IBlockState state, World worldIn, BlockPos pos, IBlockState newState, boolean isMoving)
	{
		if(!hasTileEntity(state))
		{
			BlockPos mainPos = getMainPos(state, pos);
			TileEntity te = worldIn.getTileEntity(mainPos);
			IBlockState otherState = worldIn.getBlockState(mainPos);
			if(te instanceof TileEntityTotemLathe && otherState.get(FACING) == state.get(FACING))
			{
				((TileEntityTotemLathe) te).setBroken();
			}
		}
		
		super.onReplaced(state, worldIn, pos, newState, isMoving);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		TileEntity te = worldIn.getTileEntity(pos);
		if(te instanceof TileEntityTotemLathe)
			((TileEntityTotemLathe) te).checkStates();
	}
	
	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
	{
		player.addStat(StatList.BLOCK_MINED.get(this));
		player.addExhaustion(0.005F);
		
		if(te instanceof TileEntityItemStack)
		{
			int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
			NonNullList<ItemStack> items = NonNullList.create();
			items.add(((TileEntityItemStack) te).getStack());
			net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, fortune, 1.0f, false, harvesters.get());
			
			for (ItemStack item : items)
			{
				spawnAsEntity(worldIn, pos, item);
			}
		} else if(te instanceof TileEntityTotemLathe)
		{
			int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
			NonNullList<ItemStack> items = NonNullList.create();
			items.add(((TileEntityTotemLathe) te).getCard1());
			items.add(((TileEntityTotemLathe) te).getCard2());
			net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, fortune, 1.0f, false, harvesters.get());
			
			for (ItemStack item : items)
			{
				spawnAsEntity(worldIn, pos, item);
			}
		}
	}
	
	@Override
	public void getDrops(IBlockState state, NonNullList<ItemStack> drops, World world, BlockPos pos, int fortune)
	{}
	
    /**
     *returns the block position of the "Main" block
     *aka the block with the TileEntity for the machine
     */
	public BlockPos getMainPos(IBlockState state, BlockPos pos)
	{
		Rotation rotation = rotationFromFacing(state.get(FACING));
		
		return pos.add(mainPos.rotate(rotation));
	}
	
	public static class Rod extends BlockTotemLathe
	{
		public static final BooleanProperty ACTIVE = MinestuckProperties.ACTIVE;
		
		public Rod(Properties properties, Map<EnumFacing, VoxelShape> shape, BlockPos mainPos)
		{
			super(properties, shape, mainPos);
		}
		
		@Override
		protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder)
		{
			super.fillStateContainer(builder);
			builder.add(ACTIVE);
		}
	}
	
	public static class DowelRod extends BlockTotemLathe
	{
		public static final EnumProperty<EnumDowelType> DOWEL = MinestuckProperties.DOWEL_OR_NONE;
		
		public DowelRod(Properties properties, Map<EnumFacing, VoxelShape> shape, BlockPos mainPos)
		{
			super(properties, shape, mainPos);
		}
		
		@Override
		public boolean hasTileEntity(IBlockState state)
		{
			return true;
		}
		
		@Nullable
		@Override
		public TileEntity createTileEntity(IBlockState state, IBlockReader world)
		{
			return new TileEntityItemStack();
		}
		
		@Override
		protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder)
		{
			super.fillStateContainer(builder);
			builder.add(DOWEL);
		}
	}
	
	public static class Slot extends BlockTotemLathe
	{
		public static final IntegerProperty COUNT = MinestuckProperties.COUNT_0_2;
		
		public Slot(Properties properties, Map<EnumFacing, VoxelShape> shape)
		{
			super(properties, shape, new BlockPos(0, 0, 0));
		}
		
		@Override
		public boolean hasTileEntity(IBlockState state)
		{
			return true;
		}
		
		@Nullable
		@Override
		public TileEntity createTileEntity(IBlockState state, IBlockReader world)
		{
			return new TileEntityTotemLathe();
		}
		
		@Override
		protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder)
		{
			super.fillStateContainer(builder);
			builder.add(COUNT);
		}
		
		@Override
		public BlockRenderLayer getRenderLayer()
		{
			return BlockRenderLayer.CUTOUT_MIPPED;
		}
	}
}