package com.mraof.minestuck.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.block.MultiblockItem;
import com.mraof.minestuck.util.MSRotationUtil;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class MachineOutlineRenderer
{
	@SubscribeEvent
	public static void renderWorld(DrawBlockHighlightEvent.HighlightBlock event)
	{
		Minecraft mc = Minecraft.getInstance();
		BlockRayTraceResult rayTrace = event.getTarget();
		
		if (mc.player != null && mc.getRenderViewEntity() == mc.player)
		{
			if (rayTrace.getFace() != Direction.UP)
				return;
			
			if (!renderCheckItem(mc.player, Hand.MAIN_HAND, mc.player.getHeldItemMainhand(), rayTrace, event.getInfo()))
				 renderCheckItem(mc.player, Hand.OFF_HAND, mc.player.getHeldItemOffhand(), rayTrace, event.getInfo());
		}
	}
	
	private static boolean renderCheckItem(ClientPlayerEntity player, Hand hand, ItemStack stack, BlockRayTraceResult rayTraceResult, ActiveRenderInfo info)
	{
		
		if(stack.isEmpty())
			return false;
		if(stack.getItem() instanceof MultiblockItem)
		{
			MultiblockItem item = (MultiblockItem) stack.getItem();
			BlockPos pos = rayTraceResult.getPos();
			BlockItemUseContext context = new BlockItemUseContext(new ItemUseContext(player, hand, rayTraceResult));

			BlockState block = player.world.getBlockState(pos);
			boolean flag = block.isReplaceable(context);
					//(player.world, player, stack, pos, rayTraceResult.getFace(), (float) rayTraceResult.getPos().getZ() - pos.getX(), (float) rayTraceResult.getPos().getY() - pos.getY(), (float) rayTraceResult.getPos().getZ() - pos.getZ()));
			
			if (!flag)
				pos = pos.up();

			Direction placedFacing = player.getHorizontalFacing().getOpposite();
			Rotation rotation = MSRotationUtil.fromDirection(placedFacing);
			
			double hitX = rayTraceResult.getHitVec().getX() - pos.getX(), hitZ = rayTraceResult.getHitVec().getZ() - pos.getZ();
			
			double d1 = info.getProjectedView().x;
			double d2 = info.getProjectedView().y;
			double d3 = info.getProjectedView().z;
			
			boolean placeable;
			AxisAlignedBB boundingBox;
			
			GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.lineWidth(2.0F);
			GlStateManager.disableTexture();
			GlStateManager.depthMask(false);	//GL stuff was copied from the standard mouseover bounding box drawing, which is likely why the alpha isn't working
			
			pos = item.getPlacementPos(pos, placedFacing, hitX, hitZ);
			
			placeable = item.canPlaceAt(context, pos, placedFacing);
			
			if(item == MSItems.ALCHEMITER)//Alchemiter
			{
				AxisAlignedBB rod = GuiUtil.rotateAround(new AxisAlignedBB(3.25, 1, 3.25, 3.75, 4, 3.75), 0.5, 0.5, rotation).offset(pos).offset(-d1, -d2, -d3).shrink(0.002);
				AxisAlignedBB pad = GuiUtil.rotateAround(new AxisAlignedBB(0, 0, 0, 4, 1, 4), 0.5, 0.5, rotation).offset(pos).offset(-d1, -d2, -d3).shrink(0.002);
				//If you don't want the extra details to the alchemiter outline, comment out the following two lines
				WorldRenderer.drawSelectionBoundingBox(rod, placeable ? 0 : 1, placeable ? 1 : 0, 0, 0.5F);
				WorldRenderer.drawSelectionBoundingBox(pad, placeable ? 0 : 1, placeable ? 1 : 0, 0, 0.5F);
			}
			
			boundingBox = GuiUtil.fromBoundingBox(item.getMultiblock().getBoundingBox(rotation)).offset(pos).offset(-d1, -d2, -d3).shrink(0.002);
			
			WorldRenderer.drawSelectionBoundingBox(boundingBox, placeable ? 0 : 1, placeable ? 1 : 0, 0, 0.5F);
			GlStateManager.depthMask(true);
			GlStateManager.enableTexture();
			GlStateManager.disableBlend();
			return true;
		}
		return false;
	}
}
