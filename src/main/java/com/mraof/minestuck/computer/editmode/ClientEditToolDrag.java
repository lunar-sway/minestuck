package com.mraof.minestuck.computer.editmode;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.alchemy.GristTypes;
import com.mraof.minestuck.block.machine.EditmodeDestroyable;
import com.mraof.minestuck.block.machine.MachineBlock;
import com.mraof.minestuck.network.EditmodeDragPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.player.ClientPlayerData;
import com.mraof.minestuck.util.MSCapabilities;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.client.Camera;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

/** Class for handling the click-and-drag editmode tools (Revise and Recycle) on the client-side.
 * (Based on code from the Minestuck Universe addon, with Cibernet's permission.)
 * @see EditmodeDragPacket for the tool's server-sided block-placing code.
 * @see ServerEditHandler for server-sided code that handles the sburb-cursor.
 * @author Caldw3ll, Cibernet
 */
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEditToolDrag
{
	
	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event)
	{
		if(event.side == LogicalSide.CLIENT)
		{
			ClientEditToolDrag.doRecycleCode(event);
			ClientEditToolDrag.doReviseCode(event);
		}
	}
	
	@SubscribeEvent
	public static void renderWorld(RenderLevelStageEvent event)
	{
		ClientEditToolDrag.renderOutlines(event);
	}
	
	/**
	 * Resets the drag tool, and removes the server-cursor if the given edit tool is active.
	 * @param targetTool The tool you want to check. Must be a drag tool (Revise or Recycle).
	 * @param cap The current edit-tools capability.
	 */
	private static void cancelDrag(IEditTools.ToolMode targetTool, IEditTools cap)
	{
		if(!isValidDragTool(targetTool))
			throw new IllegalArgumentException("targetTool in cancelDrag() must be a drag tool (Revise or Recycle)!");
		
		if(cap.getToolMode() == targetTool)
			MSPacketHandler.sendToServer(new EditmodeDragPacket.Reset());
		cap.resetDragTools();
	}
	
	/**
	 * Attempts to get the currently highlighted block.
	 * If the player is highlighting a block, initialize the target edit-tool's parameters.
	 * @param targetTool The tool to begin using. Must be a drag tool (Revise or Recycle).
	 * @param cap The current edit-tools capability.
	 * @param player Current client-side player.
	 * @return True if the ray hits a block. False if it doesn't
	 */
	private static boolean tryBeginDrag(IEditTools.ToolMode targetTool, IEditTools cap, Player player)
	{
		if(!isValidDragTool(targetTool))
			throw new IllegalArgumentException("targetTool in tryBeginDrag() must be a drag tool (Revise or Recycle)!");
		
		BlockHitResult blockHit = getPlayerPOVHitResult(player.getLevel(), player);
		if (blockHit.getType() == BlockHitResult.Type.BLOCK)
		{
			cap.beginDragTools(targetTool, blockHit, player);
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Sets/updates the second selection point according to the given tool,
	 * and sends a packet to create/update the sburb cursor.
	 * @param targetTool The tool you want to update. Must be a drag tool (Revise or Recycle).
	 * @param cap The current edit-tools capability.
	 * @param player Current client-side player.
	 * @param toolKey The given tool's key.
	 */
	private static void updateDragPosition(IEditTools.ToolMode targetTool, IEditTools cap, Player player, KeyMapping toolKey)
	{
		if(!isValidDragTool(targetTool))
			throw new IllegalArgumentException("targetTool in updateDragPosition() must be a drag tool (Revise or Recycle)!");
		
		cap.setEditPos2(getSelectionEndPoint(player, cap.getEditReachDistance(), targetTool == IEditTools.ToolMode.REVISE ? true : false));
		MSPacketHandler.sendToServer(new EditmodeDragPacket.Cursor(toolKey.isDown(), cap.getEditPos1(), cap.getEditPos2()));
	}
	
	/**
	 * When the player releases the given tool's key, if a selection is active, a packet for filling/destroying the selected blocks and removing the cursor will be sent.
	 * Also creates the particles and block sounds on the client-side, whereas the packet handles broadcasting those to other players.
	 * @param targetTool The tool you want to finish. Must be a drag tool (Revise or Recycle).
	 * @param cap The current edit-tools capability.
	 * @param player Current client-side player.
	 */
	private static void finishDragging(IEditTools.ToolMode targetTool, IEditTools cap, Player player)
	{
		if(!isValidDragTool(targetTool))
			throw new IllegalArgumentException("targetTool in finishDragging() must be a drag tool (Revise or Recycle)!");
		
		if(targetTool == IEditTools.ToolMode.REVISE)
			MSPacketHandler.sendToServer(new EditmodeDragPacket.Fill(false, cap.getEditPos1(), cap.getEditPos2(), cap.getEditTraceHit(), cap.getEditTraceDirection()));
		else
			MSPacketHandler.sendToServer(new EditmodeDragPacket.Destroy(false, cap.getEditPos1(), cap.getEditPos2(), cap.getEditTraceHit(), cap.getEditTraceDirection()));
		playSoundAndSetParticles(player, targetTool == IEditTools.ToolMode.REVISE ? true : false, cap.getEditPos1(), cap.getEditPos2());
	
		cap.resetDragTools();
	}
	
	public static boolean isValidDragToolOrNull(IEditTools.ToolMode toolMode) { return toolMode == null || isValidDragTool(toolMode); }
	
	public static boolean isValidDragTool(IEditTools.ToolMode toolMode) { return toolMode == IEditTools.ToolMode.REVISE || toolMode == IEditTools.ToolMode.RECYCLE; }
	
	/**
	 * Handles code for the revise tool on the client-side.
	 */
	public static void doReviseCode(TickEvent.ClientTickEvent event)
	{
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null || event.phase == TickEvent.Phase.END)
			return;
		
		Player player = mc.player;
		IEditTools cap = player.getCapability(MSCapabilities.EDIT_TOOLS_CAPABILITY, null).orElseThrow(() -> new IllegalStateException("EditTool Capability is missing on player " + player.getDisplayName().getString() + " on client-side!"));
		
		//Return early if there IS a tool active and it ISN'T revise.
		if (cap.getToolMode() != null && cap.getToolMode() != IEditTools.ToolMode.REVISE)
			return;
		
		boolean isDragging = cap.isEditDragging();
		KeyMapping toolKey = mc.options.keyUse;
		
		//If key is pressed, and not allowed to recycle, cancel the tool.
		if(toolKey.isDown() && !canEditRevise(player))
		{
			cancelDrag(IEditTools.ToolMode.REVISE, cap);
			return;
		}
		
		//If key has just been pressed, begin drag.
		if(toolKey.isDown() && !isDragging)
			if(!tryBeginDrag(IEditTools.ToolMode.REVISE, cap, player))
				return; //Returns if the player is not highlighting a block.
		
		//If the selection has already successfully found a starting point, find the end-point.
		if(cap.isEditDragging())
			updateDragPosition(IEditTools.ToolMode.REVISE, cap, player, toolKey);
		
		//If key has just been released, finish drag.
		if(!toolKey.isDown() && isDragging)
			finishDragging(IEditTools.ToolMode.REVISE, cap, player);
		
		cap.setEditDragging(toolKey.isDown());
	}
	
	/**
	 * Determines whether the player can use the revise tool when right-clicking, based on the block that they are holding.
	 * @param player The client-side player.
	 * @return True if you are in editmode and holding a non-deployable block, else false.
	 */
	public static boolean canEditRevise(Player player)
	{
		return (ClientEditHandler.isActive()
				&& !Minecraft.getInstance().isPaused()
				&& !isBlockDeployable(player)
				&& (player.getMainHandItem().getItem() instanceof BlockItem) || (player.getOffhandItem().getItem() instanceof BlockItem));
	}
	
	/**
	 * Handles code for the recycle tool on the client-side.
	 */
	public static void doRecycleCode(TickEvent.ClientTickEvent event)
	{
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null || event.phase == TickEvent.Phase.END)
			return;
		
		Player player = mc.player;
		IEditTools cap = player.getCapability(MSCapabilities.EDIT_TOOLS_CAPABILITY, null).orElseThrow(() -> new IllegalStateException("EditTool Capability is missing on player " + player.getDisplayName().getString() + " on client-side!"));
		
		//Return early if there IS a tool active and it ISN'T recycle.
		if (cap.getToolMode() != null && cap.getToolMode() != IEditTools.ToolMode.RECYCLE)
			return;
		
		boolean isDragging = cap.isEditDragging();
		KeyMapping toolKey = mc.options.keyAttack;
		
		//If key is pressed, and not allowed to recycle, cancel the tool.
		if(toolKey.isDown() && !canEditRecycle(player))
		{
			cancelDrag(IEditTools.ToolMode.RECYCLE, cap);
			return;
		}
		
		//If key has just been pressed, begin drag.
		if(toolKey.isDown() && !isDragging)
			if(!tryBeginDrag(IEditTools.ToolMode.RECYCLE, cap, player))
				return; //Returns if the player is not highlighting a block.
		
		//If the selection has already successfully found a starting point, find the end-point.
		if(cap.getEditPos1() != null)
			updateDragPosition(IEditTools.ToolMode.RECYCLE, cap, player, toolKey);
		
		//If key has just been released, finish drag.
		if(!toolKey.isDown() && isDragging)
			finishDragging(IEditTools.ToolMode.RECYCLE, cap, player);
		
		cap.setEditDragging(toolKey.isDown());
	}
	
	/**
	 * Determines whether the player can use the recycle tool when left-clicking,
	 * based on the block that they are looking at.
	 * @param player The client-side editmode player.
	 * @return True if you are in editmode and NOT looking directly at a multiblock or unbreakable block. Else false.
	 */
	public static boolean canEditRecycle(Player player)
	{
		BlockHitResult blockHit = getPlayerPOVHitResult(player.getLevel(), player);
		BlockState block = player.getLevel().getBlockState(blockHit.getBlockPos());
		
		return (ClientEditHandler.isActive()
				&& !Minecraft.getInstance().isPaused()
				&& !(block.getDestroySpeed(player.getLevel(), blockHit.getBlockPos()) < 0 || block.getMaterial() == Material.PORTAL)
				&& !isMultiblock(player));
	}
	
	/**
	 * Sets particles and sounds for local player, since level.playSound only broadcasts to other, non-local players.
	 */
	private static void playSoundAndSetParticles(Player player, boolean fill, BlockPos positionStart, BlockPos positionEnd)
	{
		ItemStack stack = player.getMainHandItem().isEmpty() ? player.getOffhandItem() : player.getMainHandItem();
		
		boolean anyBlockEdited = false;
		for(int x = Math.min(positionStart.getX(), positionEnd.getX()); x <= Math.max(positionStart.getX(), positionEnd.getX()); x++)
		{
			for(int y = Math.min(positionStart.getY(), positionEnd.getY()); y <= Math.max(positionStart.getY(), positionEnd.getY()); y++)
			{
				for(int z = Math.min(positionStart.getZ(), positionEnd.getZ()); z <= Math.max(positionStart.getZ(), positionEnd.getZ()); z++)
				{
					BlockPos pos = new BlockPos(x, y, z);
					if(!fill && !player.getLevel().getBlockState(pos).isAir() && (ClientPlayerData.getClientGrist().getGrist(GristTypes.BUILD) > 0 || ClientDeployList.getEntry(player.getLevel().getBlockState(pos).getCloneItemStack(null, player.getLevel(), pos, player)) != null))
					{
						anyBlockEdited = true;
						
						player.level.addDestroyBlockEffect(pos, player.getLevel().getBlockState(pos));
					}
					else if(fill && player.getLevel().getBlockState(pos).getMaterial().isReplaceable() && GristHelper.canAfford(ClientPlayerData.getClientGrist(), ClientEditHandler.itemCost(stack, player.getLevel())))
					{
						anyBlockEdited = true;
					}
				}
			}
		}
		
		//Play edit sound locally, if a block is able to be placed/broken.
		if(anyBlockEdited)
			player.getLevel().playSound(player, positionEnd, fill ? MSSoundEvents.EVENT_EDIT_TOOL_REVISE.get() : MSSoundEvents.EVENT_EDIT_TOOL_RECYCLE.get(), SoundSource.AMBIENT, 1.0f, fill ? 1.0f : 0.85f);
	}
	
	/**
	 * Calculates the second corner of a revise/recycle selection,
	 * based on whether the player is pointing at a block or not,
	 * and the distance from the player to the block they first highlighted
	 * at the start of the selection.
	 * @param player The client-side editmode player.
	 * @param reachDistance The editReachDistance of the EditTool capability
	 * @param shouldBlockOffset Whether the endpoint should be inside the highlighted block, or off to the side.
	 * @return The BlockPos of the second corner of the revise/recycle selection box.
	 */
	private static BlockPos getSelectionEndPoint(Player player, double reachDistance, boolean shouldBlockOffset)
	{
		BlockHitResult blockHit = getPlayerPOVHitResult(player.getLevel(), player);

		//if not looking directly at a block, use the position where the player is looking at with the initial distance of editPos1 from the camera
		if (blockHit.getType() == BlockHitResult.Type.MISS)
		{
			Vec3 eyePosition = player.getEyePosition();
			Vec3 lookDirection = player.getLookAngle();
			Vec3 selectionPosition = eyePosition.add(lookDirection.x * reachDistance, lookDirection.y * reachDistance, lookDirection.z * reachDistance);
			return new BlockPos(selectionPosition.x, selectionPosition.y, selectionPosition.z);
		}
		else
		{
			if(shouldBlockOffset)
				return player.level.getBlockState(blockHit.getBlockPos()).getMaterial().isReplaceable() ? blockHit.getBlockPos() : blockHit.getBlockPos().offset(blockHit.getDirection().getNormal());
			else
				return blockHit.getBlockPos();
		}
	}
	
	/**
	 * Determines whether the block that the player is holding is deployable or not.
	 * @param player The client-side editmode player.
	 * @return True if item is in the deploy list, a block-item, and a machine.
	 */
	private static boolean isBlockDeployable(Player player)
	{
			ItemStack stack	= player.getMainHandItem().isEmpty() ? player.getOffhandItem() : player.getMainHandItem();
		
			return ClientDeployList.getEntry(stack) != null && stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof MachineBlock;
	}
	
	/**
	 * Determines whether the block that the player is highlighting is a destroyable multiblock or not.
	 * @param player The client-side editmode player.
	 * @return True if the player is looking at a block, and the block implements EditmodeDestroyable.
	 */
	private static boolean isMultiblock(Player player)
	{
			BlockPos blockLookingAt = getPlayerPOVHitResult(player.getLevel(), player).getBlockPos();
			
			return (player.getLevel().getBlockState(blockLookingAt) != null && (player.getLevel().getBlockState(blockLookingAt).getBlock() instanceof EditmodeDestroyable));
	}
	
	/**
	 * Casts a ray from the player's camera, in the direction that they're looking, and returns the result.
	 * The ray has the same length as the player's reach distance.
	 * Based on the Item class function of the same name.
	 * @param level The level the player is in.
	 * @param player The current editmode player.
	 * @return The raycast result of the block the player is highlighting, if there is one within reach.
	 * getType() is BlockHitResult.Type.MISS if no highlighted block.
	 */
	private static BlockHitResult getPlayerPOVHitResult(Level level, Player player)
	{
		float xRot = player.getXRot();
		float yRot = player.getYRot();
		Vec3 eyeVec = player.getEyePosition(1.0F);
		float f2 = Mth.cos(-yRot * ((float) Math.PI / 180F) - (float) Math.PI);
		float f3 = Mth.sin(-yRot * ((float) Math.PI / 180F) - (float) Math.PI);
		float f4 = -Mth.cos(-xRot * ((float) Math.PI / 180F));
		float yComponent = Mth.sin(-xRot * ((float) Math.PI / 180F));
		float xComponent = f3 * f4;
		float zComponent = f2 * f4;
		double reachDistance = player.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue();
		Vec3 endVec = eyeVec.add((double) xComponent * reachDistance, (double) yComponent * reachDistance, (double) zComponent * reachDistance);
		return level.clip(new ClipContext(eyeVec, endVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
	}
	
	
	/**
	 * Renders the outlines of the selection box. Green if revise, red if recycle.
	 */
	public static void renderOutlines(RenderLevelStageEvent event)
	{
		Minecraft mc = Minecraft.getInstance();
		
		//make sure the stage is after translucent blocks so that the outlines render over everything.
		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS && mc.player != null && mc.getCameraEntity() == mc.player)
		{
			
			Player player = mc.player;
			Camera info = event.getCamera();
			
			IEditTools cap = player.getCapability(MSCapabilities.EDIT_TOOLS_CAPABILITY, null).orElseThrow(() -> new IllegalStateException("EditTool Capability is empty on player " + player.getDisplayName().toString() + " on client-side!"));
			
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
