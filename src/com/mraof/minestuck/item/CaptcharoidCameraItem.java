package com.mraof.minestuck.item;

import java.util.List;

import com.mraof.minestuck.item.crafting.alchemy.AlchemyRecipes;

import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

public class CaptcharoidCameraItem extends Item
{
	
	public CaptcharoidCameraItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context)
	{
		World worldIn = context.getWorld();
		BlockPos pos = context.getPos();
		PlayerEntity player = context.getPlayer();
		Direction facing = context.getFace();
		Boolean inside = context.isInside();

		//pos.offset(facing).offset(facing.rotateY()).up(), pos.offset(facing.getOpposite()).offset(facing.rotateYCCW()).down()
		if(!worldIn.isRemote) 
		{
			
			AxisAlignedBB bb = new AxisAlignedBB(pos.offset(facing));
			List<ItemFrameEntity> list = worldIn.getEntitiesWithinAABB(ItemFrameEntity.class, bb);
			
			if(!list.isEmpty())
			{
				ItemStack item = list.get(0).getDisplayedItem();
				if(item.isEmpty()) item = new ItemStack(Items.ITEM_FRAME);
				
				player.inventory.addItemStackToInventory(AlchemyRecipes.createGhostCard(item));
				context.getItem().damageItem(1, player, (playerEntity) -> playerEntity.sendBreakAnimation(Hand.MAIN_HAND));
			}
			else
			{
				BlockState state = worldIn.getBlockState(pos);
				ItemStack block = state.getPickBlock(new BlockRayTraceResult(context.getHitVec(), facing, pos, inside), worldIn, pos, player);
				
				player.inventory.addItemStackToInventory(AlchemyRecipes.createGhostCard(block));
				context.getItem().damageItem(1, player,  (playerEntity) -> playerEntity.sendBreakAnimation(Hand.MAIN_HAND));
			}
			return ActionResultType.PASS;
		}
		
		return ActionResultType.SUCCESS;
	}
}
