package com.mraof.minestuck.client.renderer;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.block.ItemPunchDesignix;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMachineOutline
{
	
	@SubscribeEvent
	public static void renderWorld(RenderWorldLastEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		
		if (mc.player != null && mc.getRenderViewEntity() == mc.player)
		{
			RayTraceResult rayTraceResult = mc.objectMouseOver;
			if (rayTraceResult == null || rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK || rayTraceResult.sideHit != EnumFacing.UP)
				return;
			
			if (!renderCheckItem(mc.player, mc.player.getHeldItemMainhand(), event.getContext(), rayTraceResult, event.getPartialTicks()))
				renderCheckItem(mc.player, mc.player.getHeldItemOffhand(), event.getContext(), rayTraceResult, event.getPartialTicks());
		}
	}
	
	private static boolean renderCheckItem(EntityPlayerSP player, ItemStack stack, RenderGlobal render, RayTraceResult rayTraceResult, float partialTicks)
	{
		if(stack.getItem() == Item.getItemFromBlock(MinestuckBlocks.punchDesignix))
		{
			BlockPos pos = rayTraceResult.getBlockPos();
			
			Block block = player.world.getBlockState(pos).getBlock();
			boolean flag = block.isReplaceable(player.world, pos);
			
			if (!flag)
				pos = pos.up();
			
			int i = MathHelper.floor((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
			EnumFacing placedFacing = EnumFacing.getHorizontal(i).getOpposite();
			double hitX = rayTraceResult.hitVec.x - pos.getX(), hitZ = rayTraceResult.hitVec.z - pos.getZ();
			
			if(placedFacing.getFrontOffsetX() > 0 && hitZ >= 0.5F || placedFacing.getFrontOffsetX() < 0 && hitZ < 0.5F
					|| placedFacing.getFrontOffsetZ() > 0 && hitX < 0.5F || placedFacing.getFrontOffsetZ() < 0 && hitX >= 0.5F)
				pos = pos.offset(placedFacing.rotateY());
			
			boolean placeable = ItemPunchDesignix.canPlaceAt(stack, player, player.world, pos, placedFacing);
			
			if(placedFacing == EnumFacing.EAST || placedFacing == EnumFacing.NORTH)
				pos = pos.offset(placedFacing.rotateYCCW());	//The bounding box is symmetrical, so doing this gets rid of some rendering cases
			
			boolean r = placedFacing.getAxis() == EnumFacing.Axis.Z;
			
			double d1 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)partialTicks;
			double d2 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)partialTicks;
			double d3 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)partialTicks;
			AxisAlignedBB boundingBox = new AxisAlignedBB(0,0,0, (r ? 2 : 1), 2, (r ? 1 : 2)).offset(pos).offset(-d1, -d2, -d3).shrink(0.002);
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.glLineWidth(2.0F);
			GlStateManager.disableTexture2D();
			GlStateManager.depthMask(false);	//GL stuff was copied from the standard mouseover bounding box drawing, which is likely why the alpha isn't working
			RenderGlobal.drawSelectionBoundingBox(boundingBox, placeable ? 0 : 1, placeable ? 1 : 0, 0, 0.5F);
			GlStateManager.depthMask(true);
			GlStateManager.enableTexture2D();
			GlStateManager.disableBlend();
		}
		else return false;
		
		return true;
	}
}
