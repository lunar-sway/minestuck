package com.mraof.minestuck.event;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MinestuckFluidHandler
{
	
	@SubscribeEvent
	public void onBucketFill(FillBucketEvent event)
	{
		
		if(event.getEmptyBucket().isEmpty() || event.getEmptyBucket().getItem() != Items.BUCKET || !event.getFilledBucket().isEmpty() || event.getTarget() == null || event.getResult() == Event.Result.DENY)
			return;
		
		ItemStack result = fillCustomBucket(event.getWorld(), event.getTarget());
		
		if (result.isEmpty())
			return;
		
		event.setFilledBucket(result);
		event.setResult(Event.Result.ALLOW);
	}
	
	private ItemStack fillCustomBucket(World world, RayTraceResult pos)
	{
		/*MinestuckBucketItem bucket = (MinestuckBucketItem)(MinestuckItems.minestuckBucket);
		IBlockState block = world.getBlockState(pos.getBlockPos());
		
		//TODO add same checks as used in buckets class
		if (bucket.fillFluids.contains(block)) 
		{
			world.setBlockToAir(pos.getBlockPos());
			return new ItemStack(MinestuckItems.minestuckBucket, 1, bucket.fillFluids.indexOf(block));
		} else*/
			return ItemStack.EMPTY;
		
	}

	@SubscribeEvent
	public void onCreateFluidSource(BlockEvent.CreateFluidSourceEvent event)
	{
		/*if(event.getState().getBlock() instanceof BlockFluidGrist)
		{
		    event.setResult(Result.DENY);
		}*/
	}
}