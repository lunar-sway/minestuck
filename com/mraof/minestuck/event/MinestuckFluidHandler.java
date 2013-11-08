package com.mraof.minestuck.event;

import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.FillBucketEvent;

import com.mraof.minestuck.Minestuck;

public class MinestuckFluidHandler 
{
	@ForgeSubscribe
	public void postStitch(TextureStitchEvent.Post event)
	{
		Minestuck.fluidOil.setIcons(Minestuck.blockOil.getBlockTextureFromSide(0), Minestuck.blockOil.getBlockTextureFromSide(1));
		Minestuck.fluidBlood.setIcons(Minestuck.blockBlood.getBlockTextureFromSide(0), Minestuck.blockBlood.getBlockTextureFromSide(1));
	}

	@ForgeSubscribe
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

		int blockID = world.getBlockId(pos.blockX, pos.blockY, pos.blockZ);

		if (Minestuck.minestuckBucket.fillFluids.contains(blockID) && world.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ) == 0) 
		{
			world.setBlock(pos.blockX, pos.blockY, pos.blockZ, 0);
			return new ItemStack(Minestuck.minestuckBucket, 1, blockID);
		} else
			return null;

	}
}
