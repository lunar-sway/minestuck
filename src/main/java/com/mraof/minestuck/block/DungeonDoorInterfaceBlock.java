package com.mraof.minestuck.block;

import com.mraof.minestuck.item.MSItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.EnumProperty;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class DungeonDoorInterfaceBlock extends Block
{
	private Item keyItem;
	private EnumKeyType keyType;
	public static final EnumProperty<EnumKeyType> KEY = MSProperties.KEY;
	
	public DungeonDoorInterfaceBlock(Properties properties)
	{
		super(properties);
		this.setDefaultState(getDefaultState().with(KEY, EnumKeyType.TIER_ONE)); //defaultState set in decor block has waterlogged
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		ItemStack itemstack = player.getHeldItem(handIn);
		
		keyItem = getKeyItem();
		if(keyItem == null)
		{
			keyItem = MSItems.BLANK_DISK;
		}
		
		ItemStack keyStack = new ItemStack(keyItem);
		
		if(ItemStack.areItemsEqual(itemstack, keyStack))
		{
			if(!worldIn.isRemote)
			{
				removeDoorBlocks(worldIn, pos);
				itemstack.shrink(1);
			}
			return ActionResultType.SUCCESS;
		} else
		{
			return ActionResultType.FAIL;
		}
	}
	
	/*@Override
	public BlockRenderType getRenderType(BlockState state)
	{
		return super.getRenderType(state);
	}*/
	
	
	
	void removeDoorBlocks(World world, BlockPos actionOrigin)
	{
		world.playSound(null, actionOrigin.getX(), actionOrigin.getY(), actionOrigin.getZ(), SoundEvents.ENTITY_ENDER_DRAGON_FLAP, SoundCategory.BLOCKS, 1.0F, 1.4F);
		
		for(BlockPos blockPos : BlockPos.getAllInBoxMutable(actionOrigin.add(10, 10, 10), actionOrigin.add(-10, -10, -10)))
		{
			BlockState blockState = world.getBlockState(blockPos);
			if(blockState.getBlock() == MSBlocks.DUNGEON_DOOR)
			{
				world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
			}
		}
	}
	
	public void setKeyItem(Item key)
	{
		keyItem = key;
		//key.getName();
		//this.setDefaultState(getDefaultState().with(KEY, key));
	}
	
	public EnumKeyType getKeyType()
	{
		return keyType;
	}
	
	public Item getKeyItem()
	{
		for(int i = 0; i < EnumKeyType.values().length; i++)
		{
			if(keyType.getName());
		}
		return keyItem;
	}
}