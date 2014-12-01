package com.mraof.minestuck.event;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.mraof.minestuck.Minestuck;

public class MinestuckFluidHandler 
{

	@SubscribeEvent
	public void onBucketFill(FillBucketEvent event) 
	{

		ItemStack result = fillCustomBucket(event.world, event.target);

		if (result == null)
			return;

		event.result = result;
		event.setResult(Result.ALLOW);
	}
	

	private ItemStack fillCustomBucket(World world, MovingObjectPosition pos) 
	{

		Block block = world.getBlockState(pos.func_178782_a()).getBlock();

		if (Minestuck.minestuckBucket.fillFluids.contains(block) /*&& world.getBlockState(pos.func_178782_a()).getValue() == 0*/) 
		{
			world.setBlockToAir(pos.func_178782_a());
			return new ItemStack(Minestuck.minestuckBucket, 1, Minestuck.minestuckBucket.FillFluidIds.get(block));
		} else
			return null;

	}
}
