package com.mraof.minestuck.item;

import java.util.List;

import com.mraof.minestuck.alchemy.AlchemyRecipes;

import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
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
				context.getItem().damageItem(1, player);
			}
			else
			{
				BlockState state = worldIn.getBlockState(pos);
				ItemStack block = state.getPickBlock(new RayTraceResult(new Vec3d(context.getHitX(), context.getHitY(), context.getHitZ()), facing, pos), worldIn, pos, player);
				
				player.inventory.addItemStackToInventory(AlchemyRecipes.createGhostCard(block));
				context.getItem().damageItem(1, player);
			}
			return ActionResultType.PASS;
		}
		
		return ActionResultType.SUCCESS;
	}
}
