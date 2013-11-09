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
	@SideOnly(Side.CLIENT)
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
	
	@SideOnly(Side.CLIENT)
//	@ForgeSubscribe
	public void onRenderOverlay(RenderGameOverlayEvent.Pre event)
	{
//		Debug.print(event.type);
		if(event.type == ElementType.AIR)
		{
	        int width = event.resolution.getScaledWidth();
	        int height = event.resolution.getScaledHeight();
	        int left = width / 2 + 91;
	        int top = height - GuiIngameForge.right_height;
	        int viewBlockId = playerViewBlockId();
//	        Debug.print(viewBlockId);

	        if (viewBlockId == Minestuck.blockBlood.blockID || viewBlockId == Minestuck.blockOil.blockID)
	        {
	        	event.setCanceled(true);
	            int air = Minecraft.getMinecraft().thePlayer.getAir();
	            int full = MathHelper.ceiling_double_int((double)(air - 2) * 10.0D / 300.0D);
	            int partial = MathHelper.ceiling_double_int((double)air * 10.0D / 300.0D) - full;

	            for (int i = 0; i < full + partial; ++i)
	            {
//	                drawTexturedModalRect(left - i * 8 - 9, top, (i < full ? 16 : 25), 18, 9, 9);
	            }
	            GuiIngameForge.right_height += 10;
	        }

		}
	}
	
	private int playerViewBlockId()
	{
		
		double d0 = Minecraft.getMinecraft().thePlayer.posY + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight();
        int x = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.posX);
        int y = MathHelper.floor_float((float)MathHelper.floor_double(d0));
        int z = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.posZ);
        return Minecraft.getMinecraft().thePlayer.worldObj.getBlockId(x, y, z);
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
