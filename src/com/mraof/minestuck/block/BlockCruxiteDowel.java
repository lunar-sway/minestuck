package com.mraof.minestuck.block;

import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.tileentity.TileEntityItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockCruxiteDowel extends Block
{
	public static final VoxelShape CRUXTRUDER_SHAPE = Block.makeCuboidShape(5, 0, 5, 11, 5, 11);
	public static final VoxelShape DOWEL_SHAPE = Block.makeCuboidShape(5, 0, 5, 11, 8, 11);
	
	public static final EnumProperty<Type> DOWEL_TYPE = MinestuckProperties.DOWEL_BLOCK;
	
	public BlockCruxiteDowel(Properties properties)
	{
		super(properties);
		setDefaultState(getDefaultState().with(DOWEL_TYPE, Type.DOWEL));
	}
	
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return state.get(DOWEL_TYPE) == Type.CRUXTRUDER ? CRUXTRUDER_SHAPE : DOWEL_SHAPE;
	}
	
	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
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
		}
	}
	
	@Override
	public void getDrops(IBlockState state, NonNullList<ItemStack> drops, World world, BlockPos pos, int fortune)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityItemStack)
		{
			ItemStack stack = ((TileEntityItemStack) te).getStack();
			drops.add(stack);
		}
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
		TileEntityItemStack te = new TileEntityItemStack();
		te.setStack(new ItemStack(MinestuckItems.cruxiteDowel));
		return te;
	}
	
	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if(!worldIn.isRemote)
			dropDowel(worldIn, pos);
		return true;
	}
	
	@Override
	public IBlockState getStateForPlacement(IBlockState state, EnumFacing facing, IBlockState state2, IWorld world, BlockPos pos1, BlockPos pos2, EnumHand hand)
	{
		return facing == EnumFacing.UP ? state : Blocks.AIR.getDefaultState();
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, EntityPlayer player)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityItemStack)
		{
			ItemStack dowel = ((TileEntityItemStack) te).getStack();
			if(!dowel.isEmpty())
				return dowel.copy();
		}
		return super.getPickBlock(state, target, world, pos, player);
	}
	
	public static void dropDowel(World world, BlockPos pos)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityItemStack)
		{
			ItemStack stack = ((TileEntityItemStack) te).getStack();
			spawnAsEntity(world, pos, stack);
		}
		world.removeBlock(pos);
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder)
	{
		super.fillStateContainer(builder);
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return BlockFaceShape.UNDEFINED;
	}
	
	@Override
	public EnumPushReaction getPushReaction(IBlockState state)
	{
		return EnumPushReaction.DESTROY;
	}
	
	public enum Type implements IStringSerializable
	{
		CRUXTRUDER,
		DOWEL,
		TOTEM;
		
		
		@Override
		public String getName()
		{
			return this.name().toLowerCase();
		}
	}
}
