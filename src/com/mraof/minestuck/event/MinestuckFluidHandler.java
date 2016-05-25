package com.mraof.minestuck.event;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.mraof.minestuck.item.MinestuckItems;

public class MinestuckFluidHandler 
{
	
	@SubscribeEvent
	public void onBucketFill(FillBucketEvent event) 
	{
		
		ItemStack result = fillCustomBucket(event.getWorld(), event.getTarget());
		
		if (result == null)
			return;
		
		event.setFilledBucket(result);
		event.setResult(Result.ALLOW);
	}
	
	private ItemStack fillCustomBucket(World world, RayTraceResult pos) 
	{
		
		Block block = world.getBlockState(pos.getBlockPos()).getBlock();
		
		if (MinestuckItems.minestuckBucket.fillFluids.contains(block) && ((Integer) world.getBlockState(pos.getBlockPos()).getValue(BlockFluidBase.LEVEL)) == 0) 
		{
			world.setBlockToAir(pos.getBlockPos());
			return new ItemStack(MinestuckItems.minestuckBucket, 1, MinestuckItems.minestuckBucket.fillFluids.indexOf(block));
		} else
			return null;
		
	}
}