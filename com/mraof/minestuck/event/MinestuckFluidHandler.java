package com.mraof.minestuck.event;

import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.AIR;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.FillBucketEvent;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.Debug;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MinestuckFluidHandler 
{

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
