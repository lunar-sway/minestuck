package com.mraof.minestuck.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.mraof.minestuck.item.MinestuckItems;

public class MinestuckFluidHandler
{
	
	@SubscribeEvent
	public void onBucketFill(FillBucketEvent event)
	{
		
		if(event.getEmptyBucket() == null || event.getEmptyBucket().getItem() != Items.bucket || event.getFilledBucket() != null || event.getTarget() == null || event.getResult() == Result.DENY)
			return;
		
		ItemStack result = fillCustomBucket(event.getWorld(), event.getTarget());
		
		if (result == null)
			return;
		
		event.setFilledBucket(result);
		event.setResult(Result.ALLOW);
	}
	
	private ItemStack fillCustomBucket(World world, RayTraceResult pos)
	{
		
		IBlockState block = world.getBlockState(pos.getBlockPos());
		//TODO add same checks as used in buckets class
		if (MinestuckItems.minestuckBucket.fillFluids.contains(block)) 
		{
			world.setBlockToAir(pos.getBlockPos());
			return new ItemStack(MinestuckItems.minestuckBucket, 1, MinestuckItems.minestuckBucket.fillFluids.indexOf(block));
		} else
			return null;
		
	}
}