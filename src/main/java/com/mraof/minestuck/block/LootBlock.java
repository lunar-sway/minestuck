package com.mraof.minestuck.block;

import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.tileentity.LootBlockTileEntity;
import com.mraof.minestuck.util.CustomVoxelShape;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class LootBlock extends CustomShapeBlock
{
	public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
	//public final ResourceLocation lootTable;
	private Map<Direction, VoxelShape> closedShape;
	public final Map<Direction, VoxelShape> openShape;
	
	public LootBlock(Properties properties, CustomVoxelShape closedShape, CustomVoxelShape openShape/*, ResourceLocation lootTable*/)
	{
		super(properties, closedShape);
		//this.lootTable = lootTable;
		this.closedShape = closedShape.createRotatedShapes();
		this.openShape = openShape.createRotatedShapes();
		registerDefaultState(defaultBlockState().setValue(OPEN, false)); //defaultState set in decor block has waterlogged
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		if(state.getValue(OPEN))
		{
			return openShape.get(state.getValue(FACING));
		}
		return closedShape.get(state.getValue(FACING));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		if(!player.isCrouching() && !state.getValue(OPEN))
		{
			if(worldIn instanceof ServerWorld)
			{
				TileEntity tileentity = worldIn.getBlockEntity(pos);
				if(tileentity instanceof LootBlockTileEntity)
				{
					player.sendMessage(new TranslationTextComponent("Wahoo!"), player.getUUID());
					
					//LootBlockTables.givePlayerItemFromLandTableArrays(worldIn, player, LootBlockTables.TIER_ONE_GENERIC, LootBlockTables.TIER_ONE_TEST, LootBlockTables.TIER_ONE_GENERIC, LootBlockTables.TIER_ONE_GENERIC);
					
					LootTable lootTable = ((ServerWorld) worldIn).getServer().getLootTables().get(((LootBlockTileEntity) tileentity).getLootTable());
					//lootTable.fillInventory(player.inventory, new LootContext.Builder((ServerWorld) interfaceTileEntity.getWorld()).build(LootParameterSets.CHEST));
					//lootTable.generate(new LootContext.Builder((ServerWorld) worldIn).build(LootParameterSets.EMPTY), player::entityDropItem);
					List<ItemStack> loot = lootTable.getRandomItems(new LootContext.Builder((ServerWorld) worldIn).create(LootParameterSets.EMPTY));
					if(loot.isEmpty())
						LOGGER.warn("Tried to generate loot from {}, but no items were generated!", ((LootBlockTileEntity) tileentity).getLootTable());
					
					if(!player.inventory.add(loot.get(worldIn.random.nextInt(loot.size()))))
					{
						ItemEntity itemEntity = player.spawnAtLocation(loot.get(worldIn.random.nextInt(loot.size())), 0.0F);
						if(itemEntity != null)
						{
							itemEntity.setNoPickUpDelay();
							//MSCriteriaTriggers.CONSORT_ITEM.trigger(player, this.lootTable.toString(), itemstack, consort);
						}
					}
					
					if(player.getItemInHand(handIn).getItem() != MSItems.DUNGEON_KEY)
						worldIn.setBlock(pos, state.setValue(OPEN, true), 2);
				}
			}
			worldIn.playSound(null, pos, MSSoundEvents.LOOT_BLOCK_OPEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
			
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
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
		return new LootBlockTileEntity();
	}
	
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(OPEN);
	}
}
