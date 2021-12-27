package com.mraof.minestuck.item;

import com.mraof.minestuck.item.crafting.alchemy.AlchemyHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import java.util.List;

public class CaptcharoidCameraItem extends Item
{
	
	public CaptcharoidCameraItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public ActionResultType useOn(ItemUseContext context)
	{
		World worldIn = context.getLevel();
		BlockPos pos = context.getClickedPos();
		PlayerEntity player = context.getPlayer();
		Direction facing = context.getClickedFace();
		Boolean inside = context.isInside();

		//pos.offset(facing).offset(facing.rotateY()).up(), pos.offset(facing.getOpposite()).offset(facing.rotateYCCW()).down()
		if(!worldIn.isClientSide) 
		{
			
			AxisAlignedBB bb = new AxisAlignedBB(pos.relative(facing));
			List<ItemFrameEntity> list = worldIn.getEntitiesOfClass(ItemFrameEntity.class, bb);
			
			if(!list.isEmpty())
			{
				ItemStack item = list.get(0).getItem();
				if(item.isEmpty()) item = new ItemStack(Items.ITEM_FRAME);
				
				player.inventory.add(AlchemyHelper.createGhostCard(item));
				context.getItemInHand().hurtAndBreak(1, player, (playerEntity) -> playerEntity.broadcastBreakEvent(Hand.MAIN_HAND));
			}
			else
			{
				BlockState state = worldIn.getBlockState(pos);
				ItemStack block = state.getPickBlock(new BlockRayTraceResult(context.getClickLocation(), facing, pos, inside), worldIn, pos, player);
				
				player.inventory.add(AlchemyHelper.createGhostCard(block));
				context.getItemInHand().hurtAndBreak(1, player,  (playerEntity) -> playerEntity.broadcastBreakEvent(Hand.MAIN_HAND));
			}
			return ActionResultType.PASS;
		}
		
		return ActionResultType.SUCCESS;
	}
}
