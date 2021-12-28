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

import javax.annotation.Nullable;

public class DungeonDoorInterfaceBlock extends Block
{
	public DungeonDoorInterfaceBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		ItemStack itemstack = player.getItemInHand(handIn);
		
		TileEntity tileentity = worldIn.getBlockEntity(pos);
		if(tileentity instanceof DungeonDoorInterfaceTileEntity && itemstack.getItem() instanceof KeyItem && !worldIn.isClientSide)
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
		world.playSound(null, actionOrigin.getX(), actionOrigin.getY(), actionOrigin.getZ(), SoundEvents.GRINDSTONE_USE, SoundCategory.BLOCKS, 1.3F, 0F);
		
		for(BlockPos blockPos : BlockPos.betweenClosed(actionOrigin.offset(10, 10, 10), actionOrigin.offset(-10, -10, -10)))
		{
			BlockState blockState = world.getBlockState(blockPos);
			if(blockState.getBlock() == MSBlocks.DUNGEON_DOOR)
			{
				world.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);
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