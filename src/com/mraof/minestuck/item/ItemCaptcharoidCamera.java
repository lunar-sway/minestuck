package com.mraof.minestuck.item;

import java.util.List;

import com.mraof.minestuck.alchemy.AlchemyRecipes;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemCaptcharoidCamera extends Item
{
	
	public ItemCaptcharoidCamera(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public EnumActionResult onItemUse(ItemUseContext context)
	{
		World worldIn = context.getWorld();
		BlockPos pos = context.getPos();
		EntityPlayer player = context.getPlayer();
		EnumFacing facing = context.getFace();
		//pos.offset(facing).offset(facing.rotateY()).up(), pos.offset(facing.getOpposite()).offset(facing.rotateYCCW()).down()
		if(!worldIn.isRemote) 
		{
			
			AxisAlignedBB bb = new AxisAlignedBB(pos.offset(facing));
			List<EntityItemFrame> list = worldIn.getEntitiesWithinAABB(EntityItemFrame.class, bb);
			
			if(!list.isEmpty())
			{
				ItemStack item = list.get(0).getDisplayedItem();
				if(item.isEmpty()) item = new ItemStack(Items.ITEM_FRAME);
				
				player.inventory.addItemStackToInventory(AlchemyRecipes.createGhostCard(item));
				context.getItem().damageItem(1, player);
			}
			else
			{
				IBlockState state = worldIn.getBlockState(pos);
				ItemStack block = state.getPickBlock(new RayTraceResult(new Vec3d(context.getHitX(), context.getHitY(), context.getHitZ()), facing, pos), worldIn, pos, player);
				
				player.inventory.addItemStackToInventory(AlchemyRecipes.createGhostCard(block));
				context.getItem().damageItem(1, player);
			}
			return EnumActionResult.PASS;
		}
		
		return EnumActionResult.SUCCESS;
	}
}
