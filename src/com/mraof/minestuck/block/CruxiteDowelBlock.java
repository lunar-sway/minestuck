package com.mraof.minestuck.block;

import com.mraof.minestuck.tileentity.TileEntityItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.PushReaction;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class CruxiteDowelBlock extends Block
{
	public static final VoxelShape CRUXTRUDER_SHAPE = Block.makeCuboidShape(5, 0, 5, 11, 5, 11);
	public static final VoxelShape DOWEL_SHAPE = Block.makeCuboidShape(5, 0, 5, 11, 8, 11);
	
	public static final EnumProperty<Type> DOWEL_TYPE = MinestuckProperties.DOWEL_BLOCK;
	
	public CruxiteDowelBlock(Properties properties)
	{
		super(properties);
		setDefaultState(this.stateContainer.getBaseState().with(DOWEL_TYPE, Type.DOWEL));
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return state.get(DOWEL_TYPE) == Type.CRUXTRUDER ? CRUXTRUDER_SHAPE : DOWEL_SHAPE;
	}
	
	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
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
		TileEntityItemStack te = new TileEntityItemStack();
		te.setStack(new ItemStack(this));
		return te;
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		if(!worldIn.isRemote)
			dropDowel(worldIn, pos);
		return true;
	}
	
	@Override
	public BlockState getStateForPlacement(BlockState state, Direction facing, BlockState state2, IWorld world, BlockPos pos1, BlockPos pos2, Hand hand)
	{
		return facing == Direction.UP ? state : Blocks.AIR.getDefaultState();
	}
	
	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
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
		world.removeBlock(pos, false);
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(DOWEL_TYPE);
	}
	
	@Override
	public PushReaction getPushReaction(BlockState state)
	{
		return PushReaction.DESTROY;
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
