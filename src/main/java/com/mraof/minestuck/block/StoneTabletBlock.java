package com.mraof.minestuck.block;

import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.tileentity.ItemStackTileEntity;
import com.mraof.minestuck.util.CustomVoxelShape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
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
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;

import javax.annotation.Nullable;
import java.util.List;

public class StoneTabletBlock extends DecorBlock //stone slab is the same as stone tablet, both are used in different circumstances
{
	//public static final VoxelShape TABLET_SHAPE = Block.makeCuboidShape(3, 0, 5, 13, 2, 11);
	//public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
	public static final BooleanProperty CARVED = MSProperties.CARVED;
	//public static final EnumProperty<Type> TABLET_TYPE = MSProperties.TABLET_BLOCK;
	
	public StoneTabletBlock(Properties properties, CustomVoxelShape shape)
	{
		super(properties, shape);
		//setDefaultState(this.stateContainer.getBaseState().with(TABLET_TYPE, Type.UNCARVED));
		setDefaultState(this.stateContainer.getBaseState().with(CARVED, false));
	}
	
	/*@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return TABLET_SHAPE;
	}*/
	
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
		if(!player.isSneaking())
		{
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if (tileEntity instanceof ItemStackTileEntity)
			{
				ItemStackTileEntity itemStackTE = (ItemStackTileEntity) tileEntity;
				String text = hasText(itemStackTE.getStack()) ? itemStackTE.getStack().getTag().getString("text") : "";
				MSScreenFactories.displayStoneTabletScreen(player, hand, text, false);
			}
		} else
		{
			if(!worldIn.isRemote)
				dropTablet(worldIn, pos);
		}
		
		return ActionResultType.SUCCESS;
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
		if(te instanceof ItemStackTileEntity)
		{
			ItemStack tabletItemStack = ((ItemStackTileEntity) te).getStack();
			if(!tabletItemStack.isEmpty())
				return tabletItemStack.copy();
		}
		return super.getPickBlock(state, target, world, pos, player);
	}
	
	public static void dropTablet(World world, BlockPos pos)
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
	@SuppressWarnings("deprecation")
	public PushReaction getPushReaction(BlockState state)
	{
		return PushReaction.DESTROY;
	}
	
	public enum Type implements IStringSerializable
	{
		UNCARVED,
		CARVED;
		
		@Override
		public String getName()
		{
			return this.name().toLowerCase();
		}
	}
	
	public static boolean hasText(ItemStack stack)
	{
		CompoundNBT nbt = stack.getTag();
		return (nbt != null && nbt.contains("text") && !nbt.getString("text").isEmpty());
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(FACING);
		builder.add(CARVED);
	}
}
