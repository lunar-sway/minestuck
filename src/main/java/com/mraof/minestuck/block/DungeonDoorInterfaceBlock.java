package com.mraof.minestuck.block;

import com.mraof.minestuck.item.KeyItem;
import com.mraof.minestuck.tileentity.DungeonDoorInterfaceTileEntity;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.storage.loot.MSLootTables;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootTable;

import javax.annotation.Nullable;

public class DungeonDoorInterfaceBlock extends Block
{
	public DungeonDoorInterfaceBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		ItemStack itemstack = player.getHeldItem(handIn);
		
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if(tileentity instanceof DungeonDoorInterfaceTileEntity && itemstack.getItem() instanceof KeyItem && !worldIn.isRemote)
		{
			DungeonDoorInterfaceTileEntity interfaceTileEntity = (DungeonDoorInterfaceTileEntity) tileentity;
			EnumKeyType itemKeyType = EnumKeyType.fromInt(KeyItem.getKeyType(itemstack));
			EnumKeyType tileEntityKeyType = interfaceTileEntity.getKey();
			
			Debug.debugf("DoorBlock activated. itemKeyType = %s, tileEntityKeyType = %s", itemKeyType, tileEntityKeyType);
			
			if(!interfaceTileEntity.getAlreadyActivated() && itemKeyType == tileEntityKeyType)
			{
				removeDoorBlocks(worldIn, pos);
				interfaceTileEntity.setAlreadyActivated(true);
				if(!player.isCreative())
					itemstack.shrink(1);
				
				return ActionResultType.SUCCESS;
			} else if(itemKeyType == tileEntityKeyType) //tile entity already activated
			{
			
			} else if(!interfaceTileEntity.getAlreadyActivated()) //enums dont match
			{
			
			}
			
			if(interfaceTileEntity.getWorld() instanceof ServerWorld)
			{
				LootTable lootTable = ((ServerWorld) interfaceTileEntity.getWorld()).getServer().getLootTableManager().getLootTableFromLocation(MSLootTables.TIER_ONE_MEDIUM_CHEST);
				//lootTable.fillInventory(player.inventory, new LootContext.Builder((ServerWorld) interfaceTileEntity.getWorld()).build(LootParameterSets.CHEST));
				lootTable.generate(new LootContext.Builder((ServerWorld) interfaceTileEntity.getWorld()).build(LootParameterSets.EMPTY), player::entityDropItem);
			}
			
		}
		
		return ActionResultType.FAIL;
	}
	
	/*@Override
	public BlockRenderType getRenderType(BlockState state)
	{
		return super.getRenderType(state);
	}*/
	
	
	public void removeDoorBlocks(World world, BlockPos actionOrigin)
	{
		world.playSound(null, actionOrigin.getX(), actionOrigin.getY(), actionOrigin.getZ(), SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.BLOCKS, 1.3F, 0F);
		
		for(BlockPos blockPos : BlockPos.getAllInBoxMutable(actionOrigin.add(10, 10, 10), actionOrigin.add(-10, -10, -10)))
		{
			BlockState blockState = world.getBlockState(blockPos);
			if(blockState.getBlock() == MSBlocks.DUNGEON_DOOR)
			{
				world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
			}
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
		return new DungeonDoorInterfaceTileEntity();
	}
}