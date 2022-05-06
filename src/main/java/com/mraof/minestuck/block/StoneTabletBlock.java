package com.mraof.minestuck.block;

import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.item.block.StoneTabletItem;
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
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class StoneTabletBlock extends CustomShapeBlock //stone slab is the same as stone tablet, both are used in different circumstances
{
	public static final BooleanProperty CARVED = MSProperties.CARVED;
	
	public StoneTabletBlock(Properties properties)
	{
		super(properties, MSBlockShapes.STONE_TABLET);
		registerDefaultState(defaultBlockState().setValue(CARVED, false)); //defaultState set in decor block has waterlogged
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
		TileEntity te = builder.getOptionalParameter(LootParameters.BLOCK_ENTITY);
		if(te instanceof ItemStackTileEntity)
		{
			ItemStackTileEntity itemTE = (ItemStackTileEntity) te;
			builder = builder.withDynamicDrop(ItemStackTileEntity.ITEM_DYNAMIC, (context, consumer) -> consumer.accept(itemTE.getStack()));
		}
		
		return super.getDrops(state, builder);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		if(!player.isShiftKeyDown())
		{
			TileEntity tileEntity = worldIn.getBlockEntity(pos);
			if(tileEntity instanceof ItemStackTileEntity)
			{
				ItemStackTileEntity itemStackTE = (ItemStackTileEntity) tileEntity;
				String text = StoneTabletItem.hasText(itemStackTE.getStack()) ? itemStackTE.getStack().getTag().getString("text") : "";
				MSScreenFactories.displayStoneTabletScreen(player, hand, text, false);
			}
		} else
		{
			if(!worldIn.isClientSide)
				dropTablet(worldIn, pos);
		}
		
		return ActionResultType.SUCCESS;
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return context.getClickedFace() == Direction.UP ? super.getStateForPlacement(context) : null;
	}
	
	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
	{
		TileEntity te = world.getBlockEntity(pos);
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
		TileEntity te = world.getBlockEntity(pos);
		if(te instanceof ItemStackTileEntity)
		{
			ItemStack stack = ((ItemStackTileEntity) te).getStack();
			popResource(world, pos, stack);
		}
		world.removeBlock(pos, false);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public PushReaction getPistonPushReaction(BlockState state)
	{
		return PushReaction.DESTROY;
	}
	
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(CARVED);
	}
}
