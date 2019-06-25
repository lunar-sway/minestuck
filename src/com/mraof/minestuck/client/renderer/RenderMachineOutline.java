package com.mraof.minestuck.client.renderer;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.block.ItemAlchemiter;
import com.mraof.minestuck.item.block.ItemCruxtruder;
import com.mraof.minestuck.item.block.ItemPunchDesignix;
import com.mraof.minestuck.item.block.ItemTotemLathe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class RenderMachineOutline
{
	
	@SubscribeEvent
	public static void renderWorld(RenderWorldLastEvent event)
	{
		Minecraft mc = Minecraft.getInstance();
		
		if (mc.player != null && mc.getRenderViewEntity() == mc.player)
		{
			RayTraceResult rayTraceResult = mc.objectMouseOver;
			if (rayTraceResult == null || rayTraceResult.type != RayTraceResult.Type.BLOCK || rayTraceResult.sideHit != EnumFacing.UP)
				return;
			
			if (!renderCheckItem(mc.player, mc.player.getHeldItemMainhand(), event.getContext(), rayTraceResult, event.getPartialTicks()))
				renderCheckItem(mc.player, mc.player.getHeldItemOffhand(), event.getContext(), rayTraceResult, event.getPartialTicks());
		}
	}
	
	private static boolean renderCheckItem(EntityPlayerSP player, ItemStack stack, WorldRenderer render, RayTraceResult rayTraceResult, float partialTicks)
	{
		if(stack.isEmpty())
			return false;
		if(stack.getItem() == MinestuckBlocks.PUNCH_DESIGNIX.asItem()
				|| stack.getItem() == MinestuckBlocks.TOTEM_LATHE.asItem()
				|| stack.getItem() == MinestuckBlocks.CRUXTRUDER.asItem()
				|| stack.getItem() == MinestuckBlocks.ALCHEMITER.asItem())
				//||stack.getItem()==Item.getItemFromBlock(MinestuckBlocks.jumperBlockExtension[0]))
		{
			BlockPos pos = rayTraceResult.getBlockPos();
			
			IBlockState block = player.world.getBlockState(pos);
			boolean flag = block.isReplaceable(new BlockItemUseContext(player.world, player, stack, pos, rayTraceResult.sideHit, (float) rayTraceResult.hitVec.x - pos.getX(), (float) rayTraceResult.hitVec.y - pos.getY(), (float) rayTraceResult.hitVec.z - pos.getZ()));
			
			if (!flag)
				pos = pos.up();
			BlockItemUseContext context = new BlockItemUseContext(player.world, player, stack, pos, rayTraceResult.sideHit, (float) rayTraceResult.hitVec.x - pos.getX(), (float) rayTraceResult.hitVec.y - pos.getY(), (float) rayTraceResult.hitVec.z - pos.getZ());
			
			EnumFacing placedFacing = player.getHorizontalFacing().getOpposite();
			double hitX = rayTraceResult.hitVec.x - pos.getX(), hitZ = rayTraceResult.hitVec.z - pos.getZ();
			boolean r = placedFacing.getAxis() == EnumFacing.Axis.Z;
			boolean f = placedFacing== EnumFacing.NORTH || placedFacing==EnumFacing.EAST;
			double d1 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)partialTicks;
			double d2 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)partialTicks;
			double d3 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)partialTicks;
			
			boolean placeable;
			AxisAlignedBB boundingBox;
			
			GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.lineWidth(2.0F);
			GlStateManager.disableTexture2D();
			GlStateManager.depthMask(false);	//GL stuff was copied from the standard mouseover bounding box drawing, which is likely why the alpha isn't working
			
			if(stack.getItem() == MinestuckBlocks.PUNCH_DESIGNIX.asItem())
			{
				if (placedFacing.getXOffset() > 0 && hitZ >= 0.5F || placedFacing.getXOffset() < 0 && hitZ < 0.5F
						|| placedFacing.getZOffset() > 0 && hitX < 0.5F || placedFacing.getZOffset() < 0 && hitX >= 0.5F)
					pos = pos.offset(placedFacing.rotateY());
				
				BlockPos placementPos = pos;
				if (placedFacing == EnumFacing.EAST || placedFacing == EnumFacing.NORTH)
					pos = pos.offset(placedFacing.rotateYCCW());    //The bounding box is symmetrical, so doing this gets rid of some rendering cases
				
				boundingBox = new AxisAlignedBB(0, 0, 0, (r ? 2 : 1), 2, (r ? 1 : 2)).offset(pos).offset(-d1, -d2, -d3).shrink(0.002);
				placeable = ItemPunchDesignix.canPlaceAt(context, placementPos, placedFacing);
			} else if(stack.getItem() == MinestuckBlocks.TOTEM_LATHE.asItem())
			{
				pos = pos.offset(placedFacing.rotateY());
				
				if (placedFacing.getXOffset() > 0 && hitZ >= 0.5F || placedFacing.getXOffset() < 0 && hitZ < 0.5F
						|| placedFacing.getZOffset() > 0 && hitX < 0.5F || placedFacing.getZOffset() < 0 && hitX >= 0.5F)
					pos = pos.offset(placedFacing.rotateY());
				
				BlockPos placementPos = pos;
				if (placedFacing == EnumFacing.EAST || placedFacing == EnumFacing.NORTH)
					pos = pos.offset(placedFacing.rotateYCCW(), 3);    //The bounding box is symmetrical, so doing this gets rid of some rendering cases
				
				boundingBox = new AxisAlignedBB(0, 0, 0, (r ? 4 : 1), 3, (r ? 1 : 4)).offset(pos).offset(-d1, -d2, -d3).shrink(0.002);
				placeable = ItemTotemLathe.canPlaceAt(context, placementPos, placedFacing);
			} else if(stack.getItem() == MinestuckBlocks.CRUXTRUDER.asItem())
			{
				BlockPos placementPos = pos.offset(placedFacing.rotateY());
				pos = pos.offset(placedFacing.getOpposite()).add(-1, 0, -1);
				
				boundingBox = new AxisAlignedBB(0,0,0, 3, 3, 3).offset(pos).offset(-d1, -d2, -d3).shrink(0.002);
				placeable = ItemCruxtruder.canPlaceAt(context, placementPos, placedFacing);
			} /*else if(MinestuckBlocks.jumperBlockExtension[0].asItem())
			{
				pos = pos.offset(placedFacing.rotateY());
				
				if (placedFacing.getXOffset() > 0 && hitZ >= 0.5F || placedFacing.getXOffset() < 0 && hitZ < 0.5F
						|| placedFacing.getZOffset() > 0 && hitX < 0.5F || placedFacing.getZOffset() < 0 && hitX >= 0.5F)
					pos = pos.offset(placedFacing.rotateY());
				
				BlockPos placementPos = pos;
				if(placedFacing == EnumFacing.WEST)
					pos = pos.offset(placedFacing, 0);
				if (placedFacing == EnumFacing.SOUTH)
					pos = pos.offset(placedFacing.getOpposite(), 3);    
				if (placedFacing == EnumFacing.EAST)
					pos = pos.offset(placedFacing.getOpposite(), 3).offset(placedFacing.rotateYCCW(), 4);    
				if(placedFacing == EnumFacing.NORTH)
					pos = pos.offset(placedFacing.rotateYCCW(), 4);
				
				boundingBox = new AxisAlignedBB(0, 0, 0, (r ? 5 : 4), 1, (r ? 4 : 5)).offset(pos).offset(-d1, -d2, -d3).shrink(0.002);
				placeable = false;//ItemJumperBlock.checkOutline(stack, player, player.world, placementPos, placedFacing);
			} */else	//Alchemiter
			{
				pos = pos.offset(placedFacing.rotateY());
				
				if (placedFacing.getXOffset() > 0 && hitZ >= 0.5F || placedFacing.getXOffset() < 0 && hitZ < 0.5F
						|| placedFacing.getZOffset() > 0 && hitX < 0.5F || placedFacing.getZOffset() < 0 && hitX >= 0.5F)
					pos = pos.offset(placedFacing.rotateY());
				
				BlockPos placementPos = pos;
				if (placedFacing == EnumFacing.EAST || placedFacing == EnumFacing.NORTH)
					pos = pos.offset(placedFacing.rotateYCCW(), 3);
				if(placedFacing == EnumFacing.EAST || placedFacing == EnumFacing.SOUTH)
					pos = pos.offset(placedFacing.getOpposite(), 3);
				
				boundingBox = new AxisAlignedBB(0, 0, 0, 4, 4, 4).offset(pos).offset(-d1, -d2, -d3).shrink(0.002);
				placeable = ItemAlchemiter.canPlaceAt(context, placementPos, placedFacing);
				
				int xOffset = - placedFacing.getXOffset() - placedFacing.rotateY().getXOffset();
				int zOffset = - placedFacing.getZOffset() - placedFacing.rotateY().getZOffset();
				//If you don't want the extra details to the alchemiter outline, comment out the following two lines
				WorldRenderer.drawSelectionBoundingBox(new AxisAlignedBB(3*xOffset + 1F/4F,1,3*zOffset + 1F/4F, 3*xOffset + 3F/4F, 4, 3*zOffset + 3F/4F).offset(placementPos).offset(-d1, -d2, -d3).shrink(0.002), placeable ? 0 : 1, placeable ? 1 : 0, 0, 0.5F);
				WorldRenderer.drawSelectionBoundingBox(new AxisAlignedBB(0,0,0, 4, 1, 4).offset(pos).offset(-d1, -d2, -d3).shrink(0.002), placeable ? 0 : 1, placeable ? 1 : 0, 0, 0.5F);
			}
			
			WorldRenderer.drawSelectionBoundingBox(boundingBox, placeable ? 0 : 1, placeable ? 1 : 0, 0, 0.5F);
			GlStateManager.depthMask(true);
			GlStateManager.enableTexture2D();
			GlStateManager.disableBlend();
			return true;
		}
		return false;
	}
}
