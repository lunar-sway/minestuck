package com.mraof.minestuck.computer.editmode;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mraof.minestuck.network.EditmodeFillPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.util.MSCapabilities;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EditRevise
{
	public static void doReviseCode(TickEvent.ClientTickEvent event)
	{
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null || event.phase == TickEvent.Phase.END)
			return;
	
		Player player = mc.player;
		IEditTools cap = player.getCapability(MSCapabilities.EDIT_TOOLS_CAPABILITY, null).orElse(new EditTools());
		boolean isDragging = cap.isEditDragging();
		boolean isDown = mc.options.keyUse.isDown();
		
		if (isDown)
		{
			if(!canEditDrag(player))
			{
				cap.setEditDragging(false);
				cap.setEditPos1(null);
				cap.setEditPos2(null);
				return;
			}
			
			if (!isDragging)
			{
				BlockHitResult blockHit = getPlayerPOVHitResult(player.getLevel(), player);
				if (blockHit.getType() == BlockHitResult.Type.BLOCK)
				{
					cap.setEditPos1(player.level.getBlockState(blockHit.getBlockPos()).getMaterial().isReplaceable() ? blockHit.getBlockPos() : blockHit.getBlockPos().offset(blockHit.getDirection().getNormal()));
					cap.setEditTraceHit(blockHit.getLocation());
					cap.setEditTraceDirection(blockHit.getDirection());
					cap.setEditReachDistance(Math.sqrt(cap.getEditPos1().distToLowCornerSqr(player.getEyePosition().x, player.getEyePosition().y, player.getEyePosition().z)));
					
				}
			}
			
			if (cap.getEditPos1() != null) {
				
				BlockHitResult blockHit = getPlayerPOVHitResult(player.getLevel(), player);
				BlockPos pos2;
				//if not looking directly at a block, use the position where the player is looking at with the initial distance of editPos1 from the camera
				if (blockHit.getType() == BlockHitResult.Type.MISS) {
					Vec3 eyePosition = player.getEyePosition();
					Vec3 lookDirection = player.getLookAngle();
					Vec3 selectionPosition = eyePosition.add(lookDirection.x * cap.getEditReachDistance(), lookDirection.y * cap.getEditReachDistance(), lookDirection.z * cap.getEditReachDistance());
					pos2 = new BlockPos(selectionPosition.x, selectionPosition.y, selectionPosition.z);
				} else pos2 = player.level.getBlockState(blockHit.getBlockPos()).getMaterial().isReplaceable() ? blockHit.getBlockPos() : blockHit.getBlockPos().offset(blockHit.getDirection().getNormal());
				
				cap.setEditPos2(pos2);
				MSPacketHandler.sendToServer(new EditmodeFillPacket(isDown, cap.getEditPos1(), cap.getEditPos2(), cap.getEditTraceHit(), cap.getEditTraceDirection()));
			}
		} else if (isDragging)
		{
			if (cap.getEditPos1() != null)
			{
				MSPacketHandler.sendToServer(new EditmodeFillPacket(isDown, cap.getEditPos1(), cap.getEditPos2(), cap.getEditTraceHit(), cap.getEditTraceDirection()));
			}
			cap.setEditPos1(null);
			cap.setEditPos2(null);
		}
		
		cap.setEditDragging(isDown);
	}
	
	public static boolean canEditDrag(Player player)
	{
		return ((ClientEditHandler.isActive() && ClientDeployList.getEntry(player.getMainHandItem().isEmpty() ? player.getOffhandItem() : player.getMainHandItem()) == null && (player.getMainHandItem().getItem() instanceof BlockItem) || (player.getOffhandItem().getItem() instanceof BlockItem)));
	}
	
	//based on the Item class function of the same name
	private static BlockHitResult getPlayerPOVHitResult(Level level, Player playerEntity)
	{
		float xRot = playerEntity.getXRot();
		float yRot = playerEntity.getYRot();
		Vec3 eyeVec = playerEntity.getEyePosition(1.0F);
		float f2 = Mth.cos(-yRot * ((float) Math.PI / 180F) - (float) Math.PI);
		float f3 = Mth.sin(-yRot * ((float) Math.PI / 180F) - (float) Math.PI);
		float f4 = -Mth.cos(-xRot * ((float) Math.PI / 180F));
		float yComponent = Mth.sin(-xRot * ((float) Math.PI / 180F));
		float xComponent = f3 * f4;
		float zComponent = f2 * f4;
		double reachDistance = playerEntity.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue();
		Vec3 endVec = eyeVec.add((double) xComponent * reachDistance, (double) yComponent * reachDistance, (double) zComponent * reachDistance);
		return level.clip(new ClipContext(eyeVec, endVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, playerEntity));
	}
	
	public static void renderOutlines(RenderLevelStageEvent event)
	{
		Minecraft mc = Minecraft.getInstance();
		
		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_CUTOUT_MIPPED_BLOCKS_BLOCKS && mc.player != null && mc.getCameraEntity() == mc.player)
		{
			
			LocalPlayer player = mc.player;
			Camera info = event.getCamera();
			
			IEditTools cap = player.getCapability(MSCapabilities.EDIT_TOOLS_CAPABILITY, null).orElse(new EditTools());
			
			double d1 = info.getPosition().x;
			double d2 = info.getPosition().y;
			double d3 = info.getPosition().z;
			
			if (cap.isEditDragging() && cap.getEditPos1() != null)
			{
				BlockPos posA = cap.getEditPos1();
				BlockPos posB = cap.getEditPos2();
				
				if(posB != null)
				{
					
					AABB boundingBox = new AABB(Math.min(posA.getX(), posB.getX()), Math.min(posA.getY(), posB.getY()), Math.min(posA.getZ(), posB.getZ()),
							Math.max(posA.getX(), posB.getX()) + 1, Math.max(posA.getY(), posB.getY()) + 1, Math.max(posA.getZ(), posB.getZ()) + 1).move(-d1, -d2, -d3).deflate(0.002);
					
					//set blend func to default, set the width of the line, disable textures because we are using lines, and disable the depth mask because it doesn't matter with lines, and is slower if enabled.
					RenderSystem.defaultBlendFunc();
					RenderSystem.lineWidth(2.0F);
					RenderSystem.disableTexture();
					RenderSystem.depthMask(false);    //GL stuff was copied from the standard mouseover bounding box drawing, which is likely why the alpha isn't working
					
					//Create new MultiBufferSource because RenderLevelStageEvent doesn't come with one.
					Tesselator tesselator = Tesselator.getInstance();
					BufferBuilder bufferBuilder = tesselator.getBuilder();
					MultiBufferSource.BufferSource renderTypeBuffer = MultiBufferSource.immediate(bufferBuilder);
					
					drawReviseToolOutline(event.getPoseStack(), renderTypeBuffer.getBuffer(RenderType.LINES), Shapes.create(boundingBox),0, 0, 0, 0, 1, 0, 1);
					renderTypeBuffer.endBatch();
					
					RenderSystem.depthMask(true);
					RenderSystem.enableTexture();
					RenderSystem.disableBlend();
				}
			}
		}
	}
	
	//taken directly from MachineOutlineRenderer's drawPhernaliaPlacementOutline function, which was taken from the LevelRenderer's drawShape function.
	private static void drawReviseToolOutline(PoseStack poseStack, VertexConsumer bufferIn, VoxelShape shapeIn, double xIn, double yIn, double zIn, float red, float green, float blue, float alpha) {
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
