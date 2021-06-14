package com.mraof.minestuck.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.block.MultiblockItem;
import com.mraof.minestuck.util.MSRotationUtil;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.DrawHighlightEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class MachineOutlineRenderer
{
	@SubscribeEvent
	public static void renderWorld(DrawHighlightEvent.HighlightBlock event)
	{
		Minecraft mc = Minecraft.getInstance();
		BlockRayTraceResult rayTrace = event.getTarget();
		
		if (mc.player != null && mc.getCameraEntity() == mc.player)
		{
			if (rayTrace.getDirection() != Direction.UP)
				return;
			
			if (!renderCheckItem(event.getMatrix(), event.getBuffers(), mc.player, Hand.MAIN_HAND, mc.player.getMainHandItem(), rayTrace, event.getInfo()))
				 renderCheckItem(event.getMatrix(), event.getBuffers(), mc.player, Hand.OFF_HAND, mc.player.getOffhandItem(), rayTrace, event.getInfo());
		}
	}
	
	private static boolean renderCheckItem(MatrixStack matrixStack, IRenderTypeBuffer bufferIn, ClientPlayerEntity player, Hand hand, ItemStack stack, BlockRayTraceResult rayTraceResult, ActiveRenderInfo info)
	{
		if(stack.isEmpty())
			return false;
		if(stack.getItem() instanceof MultiblockItem)
		{
			IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.lines());

			MultiblockItem item = (MultiblockItem) stack.getItem();
			BlockPos pos = rayTraceResult.getBlockPos();
			BlockItemUseContext context = new BlockItemUseContext(new ItemUseContext(player, hand, rayTraceResult));

			BlockState block = player.level.getBlockState(pos);
			boolean flag = block.canBeReplaced(context);
					//(player.world, player, stack, pos, rayTraceResult.getFace(), (float) rayTraceResult.getPos().getZ() - pos.getX(), (float) rayTraceResult.getPos().getY() - pos.getY(), (float) rayTraceResult.getPos().getZ() - pos.getZ()));
			
			if (!flag)
				pos = pos.above();

			Direction placedFacing = player.getDirection().getOpposite();
			Rotation rotation = MSRotationUtil.fromDirection(placedFacing);
			
			double hitX = rayTraceResult.getLocation().x() - pos.getX(), hitZ = rayTraceResult.getLocation().z() - pos.getZ();
			
			double d1 = info.getPosition().x;
			double d2 = info.getPosition().y;
			double d3 = info.getPosition().z;
			
			boolean placeable;
			AxisAlignedBB boundingBox;

			RenderSystem.defaultBlendFunc();
			RenderSystem.lineWidth(2.0F);
			RenderSystem.disableTexture();
			RenderSystem.depthMask(false);	//GL stuff was copied from the standard mouseover bounding box drawing, which is likely why the alpha isn't working
			
			pos = item.getPlacementPos(pos, placedFacing, hitX, hitZ);
			
			placeable = item.canPlaceAt(context, pos, placedFacing);

			if(item == MSItems.ALCHEMITER)//Alchemiter
			{
				AxisAlignedBB rod = GuiUtil.rotateAround(new AxisAlignedBB(3.25, 1, 3.25, 3.75, 4, 3.75), 0.5, 0.5, rotation).move(pos).move(-d1, -d2, -d3).deflate(0.002);
				AxisAlignedBB pad = GuiUtil.rotateAround(new AxisAlignedBB(0, 0, 0, 4, 1, 4), 0.5, 0.5, rotation).move(pos).move(-d1, -d2, -d3).deflate(0.002);
				//If you don't want the extra details to the alchemiter outline, comment out the following two lines
				drawPhernaliaPlacementOutline(matrixStack, ivertexbuilder, VoxelShapes.create(rod), 0, 0, 0, placeable ? 0 : 1, placeable ? 1 : 0, 0, 0.5F);
				drawPhernaliaPlacementOutline(matrixStack, ivertexbuilder, VoxelShapes.create(pad), 0, 0, 0, placeable ? 0 : 1, placeable ? 1 : 0, 0, 0.5F);
			}
			
			boundingBox = GuiUtil.fromBoundingBox(item.getMultiblock().getBoundingBox(rotation)).move(pos).move(-d1, -d2, -d3).deflate(0.002);

			drawPhernaliaPlacementOutline(matrixStack, ivertexbuilder, VoxelShapes.create(boundingBox), 0, 0, 0, placeable ? 0 : 1, placeable ? 1 : 0, 0, 0.5F);
			
			RenderSystem.depthMask(true);
			RenderSystem.enableTexture();
			RenderSystem.disableBlend();
			return true;
		}
		return false;
	}

	private static void drawPhernaliaPlacementOutline(MatrixStack matrixStackIn, IVertexBuilder bufferIn, VoxelShape shapeIn, double xIn, double yIn, double zIn, float red, float green, float blue, float alpha) {
		Matrix4f matrix4f = matrixStackIn.last().pose();
		shapeIn.forAllEdges((p_230013_12_, p_230013_14_, p_230013_16_, p_230013_18_, p_230013_20_, p_230013_22_) -> {
			bufferIn.vertex(matrix4f, (float)(p_230013_12_ + xIn), (float)(p_230013_14_ + yIn), (float)(p_230013_16_ + zIn)).color(red, green, blue, alpha).endVertex();
			bufferIn.vertex(matrix4f, (float)(p_230013_18_ + xIn), (float)(p_230013_20_ + yIn), (float)(p_230013_22_ + zIn)).color(red, green, blue, alpha).endVertex();
		});
	}
}
