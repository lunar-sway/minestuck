package com.mraof.minestuck.event;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;

import com.mraof.minestuck.Minestuck;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class MinestuckFluidHandler 
{

	@SubscribeEvent
	public void onBucketFill(FillBucketEvent event) 
	{

		ItemStack result = fillCustomBucket(event.world, event.target);

		if (result == null)
			return;

		event.result = result;
		event.setResult(Event.Result.ALLOW);
	}
	

	private ItemStack fillCustomBucket(World world, MovingObjectPosition pos) 
	{

		Block block = world.getBlock(pos.blockX, pos.blockY, pos.blockZ);

		if (Minestuck.minestuckBucket.fillFluids.contains(block) && world.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ) == 0) 
		{
			world.setBlockToAir(pos.blockX, pos.blockY, pos.blockZ);
			return new ItemStack(Minestuck.minestuckBucket, 1, Minestuck.minestuckBucket.FillFluidIds.get(block));
		} else
			return null;

	}
}
