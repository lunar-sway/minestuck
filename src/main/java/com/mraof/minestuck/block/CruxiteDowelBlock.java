package com.mraof.minestuck.block;

import com.mraof.minestuck.tileentity.ItemStackTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class CruxiteDowelBlock extends Block
{
	public static final VoxelShape CRUXTRUDER_SHAPE = Block.makeCuboidShape(5, 0, 5, 11, 5, 11);
	public static final VoxelShape DOWEL_SHAPE = Block.makeCuboidShape(5, 0, 5, 11, 8, 11);
	
	public static final EnumProperty<Type> DOWEL_TYPE = MSProperties.DOWEL_BLOCK;
	
	public CruxiteDowelBlock(Properties properties)
	{
		super(properties);
		setDefaultState(this.stateContainer.getBaseState().with(DOWEL_TYPE, Type.DOWEL));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return state.get(DOWEL_TYPE) == Type.CRUXTRUDER ? CRUXTRUDER_SHAPE : DOWEL_SHAPE;
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
		ItemStackTileEntity te = new ItemStackTileEntity();
		te.setStack(new ItemStack(this));
		return te;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder)
	{
		TileEntity te = builder.get(LootParameters.BLOCK_ENTITY);
		if (te instanceof ItemStackTileEntity)
		{
			ItemStackTileEntity itemTE = (ItemStackTileEntity)te;
			builder = builder.withDynamicDrop(ItemStackTileEntity.ITEM_DYNAMIC, (context, consumer) -> consumer.accept(itemTE.getStack()));
		}
		
		return super.getDrops(state, builder);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		if(!worldIn.isRemote)
			dropDowel(worldIn, pos);
		return  ActionResultType.SUCCESS;
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return context.getFace() == Direction.UP ? getDefaultState() : Blocks.AIR.getDefaultState();
	}
	
	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof ItemStackTileEntity)
		{
			ItemStack dowel = ((ItemStackTileEntity) te).getStack();
			if(!dowel.isEmpty())
				return dowel.copy();
		}
		return super.getPickBlock(state, target, world, pos, player);
	}
	
	public static void dropDowel(World world, BlockPos pos)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof ItemStackTileEntity)
		{
			ItemStack stack = ((ItemStackTileEntity) te).getStack();
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
	@SuppressWarnings("deprecation")
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
		public String getString()
		{
			return this.name().toLowerCase();
		}
	}
}
