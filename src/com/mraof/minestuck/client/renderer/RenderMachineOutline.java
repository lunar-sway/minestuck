package com.mraof.minestuck.client.renderer;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.block.ItemAlchemiter;
import com.mraof.minestuck.item.block.ItemCruxtruder;
import com.mraof.minestuck.item.block.ItemJumperBlock;
import com.mraof.minestuck.item.block.ItemPunchDesignix;
import com.mraof.minestuck.item.block.ItemTotemLathe;
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
		if(stack.getItem() == Item.getItemFromBlock(MinestuckBlocks.punchDesignix)
				||stack.getItem()==Item.getItemFromBlock(MinestuckBlocks.totemlathe[0])
				||stack.getItem()==Item.getItemFromBlock(MinestuckBlocks.cruxtruder)
				||stack.getItem()==Item.getItemFromBlock(MinestuckBlocks.alchemiter[0]))
				//||stack.getItem()==Item.getItemFromBlock(MinestuckBlocks.jumperBlockExtension[0]))
		{
			BlockPos pos = rayTraceResult.getBlockPos();
			
			Block block = player.world.getBlockState(pos).getBlock();
			boolean flag = block.isReplaceable(player.world, pos);
			
			if (!flag)
				pos = pos.up();
			
			EnumFacing placedFacing = player.getHorizontalFacing().getOpposite();
			double hitX = rayTraceResult.hitVec.x - pos.getX(), hitZ = rayTraceResult.hitVec.z - pos.getZ();
			boolean r = placedFacing.getAxis() == EnumFacing.Axis.Z;
			boolean f = placedFacing== EnumFacing.NORTH || placedFacing==EnumFacing.EAST;
			double d1 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)partialTicks;
			double d2 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)partialTicks;
			double d3 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)partialTicks;
			
			boolean placeable;
			AxisAlignedBB boundingBox;
			
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.glLineWidth(2.0F);
			GlStateManager.disableTexture2D();
			GlStateManager.depthMask(false);	//GL stuff was copied from the standard mouseover bounding box drawing, which is likely why the alpha isn't working
			
			if(stack.getItem() == Item.getItemFromBlock(MinestuckBlocks.punchDesignix))
			{
				if (placedFacing.getFrontOffsetX() > 0 && hitZ >= 0.5F || placedFacing.getFrontOffsetX() < 0 && hitZ < 0.5F
						|| placedFacing.getFrontOffsetZ() > 0 && hitX < 0.5F || placedFacing.getFrontOffsetZ() < 0 && hitX >= 0.5F)
					pos = pos.offset(placedFacing.rotateY());
				
				BlockPos placementPos = pos;
				if (placedFacing == EnumFacing.EAST || placedFacing == EnumFacing.NORTH)
					pos = pos.offset(placedFacing.rotateYCCW());    //The bounding box is symmetrical, so doing this gets rid of some rendering cases
				
				boundingBox = new AxisAlignedBB(0, 0, 0, (r ? 2 : 1), 2, (r ? 1 : 2)).offset(pos).offset(-d1, -d2, -d3).shrink(0.002);
				placeable = ItemPunchDesignix.canPlaceAt(stack, player, player.world, placementPos, placedFacing);
			} else if(stack.getItem() == Item.getItemFromBlock(MinestuckBlocks.totemlathe[0]))
			{
				pos = pos.offset(placedFacing.rotateY());
				
				if (placedFacing.getFrontOffsetX() > 0 && hitZ >= 0.5F || placedFacing.getFrontOffsetX() < 0 && hitZ < 0.5F
						|| placedFacing.getFrontOffsetZ() > 0 && hitX < 0.5F || placedFacing.getFrontOffsetZ() < 0 && hitX >= 0.5F)
					pos = pos.offset(placedFacing.rotateY());
				
				BlockPos placementPos = pos;
				if (placedFacing == EnumFacing.EAST || placedFacing == EnumFacing.NORTH)
					pos = pos.offset(placedFacing.rotateYCCW(), 3);    //The bounding box is symmetrical, so doing this gets rid of some rendering cases
				
				boundingBox = new AxisAlignedBB(0, 0, 0, (r ? 4 : 1), 3, (r ? 1 : 4)).offset(pos).offset(-d1, -d2, -d3).shrink(0.002);
				placeable = ItemTotemLathe.canPlaceAt(stack, player, player.world, placementPos, placedFacing);
			} else if(stack.getItem() == Item.getItemFromBlock(MinestuckBlocks.cruxtruder))
			{
				BlockPos placementPos = pos.offset(placedFacing.rotateY());
				pos = pos.offset(placedFacing.getOpposite()).add(-1, 0, -1);
				
				boundingBox = new AxisAlignedBB(0,0,0, 3, 3, 3).offset(pos).offset(-d1, -d2, -d3).shrink(0.002);
				placeable = ItemCruxtruder.canPlaceAt(stack, player, player.world, placementPos, placedFacing);
			} else if(stack.getItem() == Item.getItemFromBlock(MinestuckBlocks.jumperBlockExtension[0]))
			{
				pos = pos.offset(placedFacing.rotateY());
				
				if (placedFacing.getFrontOffsetX() > 0 && hitZ >= 0.5F || placedFacing.getFrontOffsetX() < 0 && hitZ < 0.5F
						|| placedFacing.getFrontOffsetZ() > 0 && hitX < 0.5F || placedFacing.getFrontOffsetZ() < 0 && hitX >= 0.5F)
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
				placeable = ItemJumperBlock.checkOutline(stack, player, player.world, placementPos, placedFacing);
			} else	//Alchemiter
			{
				pos = pos.offset(placedFacing.rotateY());
				
				if (placedFacing.getFrontOffsetX() > 0 && hitZ >= 0.5F || placedFacing.getFrontOffsetX() < 0 && hitZ < 0.5F
						|| placedFacing.getFrontOffsetZ() > 0 && hitX < 0.5F || placedFacing.getFrontOffsetZ() < 0 && hitX >= 0.5F)
					pos = pos.offset(placedFacing.rotateY());
				
				BlockPos placementPos = pos;
				if (placedFacing == EnumFacing.EAST || placedFacing == EnumFacing.NORTH)
					pos = pos.offset(placedFacing.rotateYCCW(), 3);
				if(placedFacing == EnumFacing.EAST || placedFacing == EnumFacing.SOUTH)
					pos = pos.offset(placedFacing.getOpposite(), 3);
				
				boundingBox = new AxisAlignedBB(0, 0, 0, 4, 4, 4).offset(pos).offset(-d1, -d2, -d3).shrink(0.002);
				placeable = ItemAlchemiter.canPlaceAt(stack, player, player.world, placementPos, placedFacing);
				
				//If you don't want the extra details to the alchemiter outline, comment out the following two lines
				RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(1F/4F,1,1F/4F, 3F/4F, 4, 3F/4F).offset(placementPos).offset(-d1, -d2, -d3).shrink(0.002), placeable ? 0 : 1, placeable ? 1 : 0, 0, 0.5F);
				RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(0,0,0, 4, 1, 4).offset(pos).offset(-d1, -d2, -d3).shrink(0.002), placeable ? 0 : 1, placeable ? 1 : 0, 0, 0.5F);
			}
			
			RenderGlobal.drawSelectionBoundingBox(boundingBox, placeable ? 0 : 1, placeable ? 1 : 0, 0, 0.5F);
			GlStateManager.depthMask(true);
			GlStateManager.enableTexture2D();
			GlStateManager.disableBlend();
		}		
		return true;
	}
}
