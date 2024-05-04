package com.mraof.minestuck.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.block.MultiblockItem;
import com.mraof.minestuck.util.MSRotationUtil;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;
import org.joml.Matrix4f;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class MachineOutlineRenderer
{
	@SubscribeEvent
	public static void renderWorld(RenderHighlightEvent.Block event)
	{
		Minecraft mc = Minecraft.getInstance();
		BlockHitResult rayTrace = event.getTarget();
		
		if (mc.player != null && mc.getCameraEntity() == mc.player)
		{
			if (rayTrace.getDirection() != Direction.UP)
				return;
			
			if (!renderCheckItem(event.getPoseStack(), event.getMultiBufferSource(), mc.player, InteractionHand.MAIN_HAND, mc.player.getMainHandItem(), rayTrace, event.getCamera()))
				 renderCheckItem(event.getPoseStack(), event.getMultiBufferSource(), mc.player, InteractionHand.OFF_HAND, mc.player.getOffhandItem(), rayTrace, event.getCamera());
		}
	}
	
	private static boolean renderCheckItem(PoseStack poseStack, MultiBufferSource bufferIn, LocalPlayer player, InteractionHand hand, ItemStack stack, BlockHitResult rayTraceResult, Camera info)
	{
		if(stack.isEmpty())
			return false;
		if(stack.getItem() instanceof MultiblockItem item)
		{
			VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.lines());
			
			BlockPos pos = rayTraceResult.getBlockPos();
			BlockPlaceContext context = new BlockPlaceContext(new UseOnContext(player, hand, rayTraceResult));

			BlockState block = player.level().getBlockState(pos);
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
			AABB boundingBox;

			RenderSystem.defaultBlendFunc();
			RenderSystem.lineWidth(2.0F);
			RenderSystem.depthMask(false);	//GL stuff was copied from the standard mouseover bounding box drawing, which is likely why the alpha isn't working
			
			pos = item.getPlacementPos(pos, placedFacing, hitX, hitZ);
			
			placeable = item.canPlaceAt(context, pos, placedFacing);

			if(item == MSItems.ALCHEMITER.get())//Alchemiter
			{
				AABB rod = GuiUtil.rotateAround(new AABB(3.25, 1, 3.25, 3.75, 4, 3.75), 0.5, 0.5, rotation).move(pos).move(-d1, -d2, -d3).deflate(0.002);
				AABB pad = GuiUtil.rotateAround(new AABB(0, 0, 0, 4, 1, 4), 0.5, 0.5, rotation).move(pos).move(-d1, -d2, -d3).deflate(0.002);
				//If you don't want the extra details to the alchemiter outline, comment out the following two lines
				drawPhernaliaPlacementOutline(poseStack, ivertexbuilder, Shapes.create(rod), 0, 0, 0, placeable ? 0 : 1, placeable ? 1 : 0, 0, 0.5F);
				drawPhernaliaPlacementOutline(poseStack, ivertexbuilder, Shapes.create(pad), 0, 0, 0, placeable ? 0 : 1, placeable ? 1 : 0, 0, 0.5F);
			}
			
			boundingBox = GuiUtil.fromBoundingBox(item.getMultiblock().getBoundingBox(rotation)).move(pos).move(-d1, -d2, -d3).deflate(0.002);

			drawPhernaliaPlacementOutline(poseStack, ivertexbuilder, Shapes.create(boundingBox), 0, 0, 0, placeable ? 0 : 1, placeable ? 1 : 0, 0, 0.5F);
			
			RenderSystem.depthMask(true);
			RenderSystem.disableBlend();
			return true;
		}
		return false;
	}

	private static void drawPhernaliaPlacementOutline(PoseStack poseStack, VertexConsumer bufferIn, VoxelShape shapeIn, double xIn, double yIn, double zIn, float red, float green, float blue, float alpha) {
		PoseStack.Pose pose = poseStack.last();
		Matrix4f matrix4f = pose.pose();
		shapeIn.forAllEdges((startX, startY, startZ, endX, endY, endZ) -> {
			float dX = (float)(endX - startX);
			float dY = (float)(endY - startY);
			float dZ = (float)(endZ - startZ);
			float length = Mth.sqrt(dX * dX + dY * dY + dZ * dZ);
			dX /= length;
			dY /= length;
			dZ /= length;
			bufferIn.vertex(matrix4f, (float)(startX + xIn), (float)(startY + yIn), (float)(startZ + zIn)).color(red, green, blue, alpha).normal(pose.normal(), dX, dY, dZ).endVertex();
			bufferIn.vertex(matrix4f, (float)(endX + xIn), (float)(endY + yIn), (float)(endZ + zIn)).color(red, green, blue, alpha).normal(pose.normal(), dX, dY, dZ).endVertex();
		});
	}
}
