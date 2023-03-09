package com.mraof.minestuck.computer.editmode;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mraof.minestuck.block.machine.MachineBlock;
import com.mraof.minestuck.block.machine.MultiMachineBlock;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.ServerCursorEntity;
import com.mraof.minestuck.network.EditmodeFillPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.util.MSCapabilities;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
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

import java.util.UUID;

public class EditToolDrag
{
	public static void doReviseCode(TickEvent.ClientTickEvent event)
	{
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null || event.phase == TickEvent.Phase.END)
			return;
		
		Player player = mc.player;
		IEditTools cap = player.getCapability(MSCapabilities.EDIT_TOOLS_CAPABILITY, null).orElse(new EditTools());
		
		if (cap.getToolMode() != null && cap.getToolMode() != IEditTools.ToolMode.REVISE)
			return;
		
		boolean isDragging = cap.isEditDragging();
		boolean isDown = mc.options.keyUse.isDown();
		
		if (isDown)
		{
			if(!canEditRevise(player))
			{
				cap.setToolMode(null);
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
					cap.setToolMode(IEditTools.ToolMode.REVISE);
					
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
				MSPacketHandler.sendToServer(new EditmodeFillPacket(true, isDown, cap.getEditPos1(), cap.getEditPos2(), cap.getEditTraceHit(), cap.getEditTraceDirection()));
			}
		} else if (isDragging)
		{
			if (cap.getEditPos1() != null)
			{
				MSPacketHandler.sendToServer(new EditmodeFillPacket(true, isDown, cap.getEditPos1(), cap.getEditPos2(), cap.getEditTraceHit(), cap.getEditTraceDirection()));
			}
			cap.setToolMode(null);
			
			cap.setEditPos1(null);
			cap.setEditPos2(null);
		}
		
		cap.setEditDragging(isDown);
	}
	
	public static boolean canEditRevise(Player player)
	{
		return (ClientEditHandler.isActive()
				&& !isBlockDeployable(player)
				&& (player.getMainHandItem().getItem() instanceof BlockItem) || (player.getOffhandItem().getItem() instanceof BlockItem));
	}
	
	
	public static void doRecycleCode(TickEvent.ClientTickEvent event)
	{
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null || event.phase == TickEvent.Phase.END)
			return;
		
		Player player = mc.player;
		IEditTools cap = player.getCapability(MSCapabilities.EDIT_TOOLS_CAPABILITY, null).orElse(new EditTools());
		
		if (cap.getToolMode() != null && cap.getToolMode() != IEditTools.ToolMode.RECYCLE)
			return;
		
		boolean isDragging = cap.isEditDragging();
		boolean isDown = mc.options.keyAttack.isDown();
		
		if (isDown)
		{
			if(!canEditRecycle(player))
			{
				cap.setToolMode(null);
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
					cap.setToolMode(IEditTools.ToolMode.RECYCLE);
					
					cap.setEditPos1(blockHit.getBlockPos());
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
				} else pos2 = blockHit.getBlockPos();
				
				cap.setEditPos2(pos2);
				MSPacketHandler.sendToServer(new EditmodeFillPacket(false, isDown, cap.getEditPos1(), cap.getEditPos2(), cap.getEditTraceHit(), cap.getEditTraceDirection()));
			}
		} else if (isDragging)
		{
			if (cap.getEditPos1() != null)
			{
				MSPacketHandler.sendToServer(new EditmodeFillPacket(false, isDown, cap.getEditPos1(), cap.getEditPos2(), cap.getEditTraceHit(), cap.getEditTraceDirection()));
			}
			
			cap.setToolMode(null);
			
			cap.setEditPos1(null);
			cap.setEditPos2(null);
		}
		
		cap.setEditDragging(isDown);
	}
	
	public static boolean canEditRecycle(Player player)
	{
		return (ClientEditHandler.isActive() && !isMultiblock(player));
	}
	
	private static boolean isBlockDeployable(Player player)
	{
		ItemStack stack	= player.getMainHandItem().isEmpty() ? player.getOffhandItem() : player.getMainHandItem();
	
		return ClientDeployList.getEntry(stack) != null && stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() instanceof MachineBlock;
	}

	private static boolean isMultiblock(Player player)
	{
		BlockPos blockLookingAt = getPlayerPOVHitResult(player.getLevel(), player).getBlockPos();
		
		return (player.getLevel().getBlockState(blockLookingAt) != null && (player.getLevel().getBlockState(blockLookingAt).getBlock() instanceof MultiMachineBlock));
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
	
	public static void updateEditToolsServer(ServerPlayer player, boolean isDragging, BlockPos pos1, BlockPos pos2, Direction side)
	{
		IEditTools cap = player.getCapability(MSCapabilities.EDIT_TOOLS_CAPABILITY, null).orElse(new EditTools());
		cap.setEditDragging(isDragging);
		cap.setEditPos1(pos1);
		cap.setEditPos2(pos2);
		
		boolean signX = pos1.getX() < pos2.getX();
		boolean signY = pos1.getY() > pos2.getY();
		boolean signZ = pos1.getZ() < pos2.getZ();
		
		double posX = pos2.getX() + (signX ? 1 : 0);
		double posY = pos2.getY() + (signY ? 0 : 1);
		double posZ = pos2.getZ() + (signZ ? 1 : 0);
		boolean flipCursor = signY;
		
		float cursorLean = 0f;
		if (signX && !signZ)
			cursorLean = 360.0f; //+X -Z = 0
		if (signX && signZ)
			cursorLean = 90.0f; //+X +Z = 90
		if (!signX && signZ)
			cursorLean = 180.0f; //-X +Z = 180
		if (!signX && !signZ)
			cursorLean = 270.0f; //-X -Z = 270
		
		if(cap.getEditCursorID() == null)
		{
			cap.setEditCursorID(createCursorEntity(player, new Vec3(posX,posY,posZ), cursorLean, flipCursor));
		} else
		{
			updateCursorEntity(player, new Vec3(posX,posY,posZ), cursorLean, flipCursor, cap.getEditCursorID());
		}
	}
	
	public static UUID createCursorEntity(ServerPlayer player, Vec3 startPosition, float cursorLean, boolean flip)
	{
		ServerCursorEntity cursor = MSEntityTypes.SERVER_CURSOR.get().create(player.getLevel());
		cursor.noPhysics = true;
		cursor.setNoGravity(true);
		cursor.setInvulnerable(true);
		
		cursor.moveTo(startPosition.x, startPosition.y, startPosition.z, cursorLean - 45.0f, flip ? 135f : 45f);
		cursor.setAnimation(ServerCursorEntity.Animation.CLICK);
		player.getLevel().addFreshEntity(cursor);
		
		return cursor.getUUID();
	}
	
	public static void updateCursorEntity(ServerPlayer player, Vec3 newPosition, float cursorLean, boolean flip, UUID uuid)
	{
		ServerCursorEntity cursor = (ServerCursorEntity) player.getLevel().getEntity(uuid);
		
		cursor.moveTo(newPosition.x, newPosition.y, newPosition.z, cursorLean - 45.0f, flip ? 135f : 45f);
		cursor.setAnimation(ServerCursorEntity.Animation.IDLE);
	}
	
	public static void removeCursorEntity(ServerPlayer player)
	{
		IEditTools cap = player.getCapability(MSCapabilities.EDIT_TOOLS_CAPABILITY, null).orElse(new EditTools());
		
		if(cap.getEditCursorID() != null)
		{
			ServerCursorEntity cursor = (ServerCursorEntity) player.getLevel().getEntity(cap.getEditCursorID());
			cursor.setAnimation(ServerCursorEntity.Animation.CLICK);
			cursor.remove(Entity.RemovalReason.DISCARDED);
		}
		
		cap.setEditCursorID(null);
	}
	
	public static void renderOutlines(RenderLevelStageEvent event)
	{
		Minecraft mc = Minecraft.getInstance();
		
		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS && mc.player != null && mc.getCameraEntity() == mc.player)
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
					
					drawReviseToolOutline(event.getPoseStack(), renderTypeBuffer.getBuffer(RenderType.LINES), Shapes.create(boundingBox),0, 0, 0, cap.getToolMode() == IEditTools.ToolMode.RECYCLE ? 1 : 0, cap.getToolMode() == IEditTools.ToolMode.REVISE ? 1 : 0, 0, 1);
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
