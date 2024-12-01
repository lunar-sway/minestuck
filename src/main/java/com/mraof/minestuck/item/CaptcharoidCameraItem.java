package com.mraof.minestuck.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class CaptcharoidCameraItem extends Item
{
	
	public CaptcharoidCameraItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		Player player = context.getPlayer();
		Direction facing = context.getClickedFace();
		Boolean inside = context.isInside();

		//pos.offset(facing).offset(facing.rotateY()).up(), pos.offset(facing.getOpposite()).offset(facing.rotateYCCW()).down()
		if(level instanceof ServerLevel serverLevel)
		{
			
			AABB bb = new AABB(pos.relative(facing));
			List<ItemFrame> list = level.getEntitiesOfClass(ItemFrame.class, bb);
			
			if(!list.isEmpty())
			{
				ItemStack item = list.get(0).getItem();
				if(item.isEmpty()) item = new ItemStack(Items.ITEM_FRAME);
				
				player.getInventory().add(CaptchaCardItem.createGhostCard(item, serverLevel.getServer()));
				context.getItemInHand().hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
			}
			else
			{
				BlockState state = level.getBlockState(pos);
				ItemStack block = state.getCloneItemStack(new BlockHitResult(context.getClickLocation(), facing, pos, inside), level, pos, player);
				
				player.getInventory().add(CaptchaCardItem.createGhostCard(block, serverLevel.getServer()));
				context.getItemInHand().hurtAndBreak(1, player,  EquipmentSlot.MAINHAND);
			}
			return InteractionResult.PASS;
		}
		
		return InteractionResult.SUCCESS;
	}
}
