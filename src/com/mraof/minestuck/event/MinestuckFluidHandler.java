package com.mraof.minestuck.event;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.BlockFluidBase;
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

		Block block = world.getBlockState(pos.getBlockPos()).getBlock();

		if (Minestuck.minestuckBucket.fillFluids.contains(block) && ((Integer) world.getBlockState(pos.getBlockPos()).getValue(BlockFluidBase.LEVEL)) == 0) 
		{
			world.setBlockToAir(pos.getBlockPos());
			return new ItemStack(Minestuck.minestuckBucket, 1, Minestuck.minestuckBucket.fillFluids.indexOf(block));
		} else
			return null;

	}
}
