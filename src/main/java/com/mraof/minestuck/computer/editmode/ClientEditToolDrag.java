package com.mraof.minestuck.computer.editmode;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.machine.EditmodeDestroyable;
import com.mraof.minestuck.block.machine.MachineBlock;
import com.mraof.minestuck.client.renderer.SelectedPreviewRenderer;
import com.mraof.minestuck.client.util.MSKeyHandler;
import com.mraof.minestuck.network.editmode.EditmodeDragPackets;
import com.mraof.minestuck.player.ClientPlayerData;
import com.mraof.minestuck.util.MSAttachments;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.client.Camera;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Matrix4f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/** Class for handling the click-and-drag editmode tools (Revise, Recycle, Select) and the Move/Copy
 * preview + commit flow on the client-side.
 * (Based on code from the Minestuck Universe addon, with Cibernet's permission.)
 * @see EditmodeDragPackets for the tool's server-sided block-placing code.
 * @see ServerEditHandler for server-sided code that handles the sburb-cursor.
 * @author Caldw3ll, Cibernet
 */
@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientEditToolDrag
{
	private static boolean moveKeyWasDown = false;
	private static boolean copyKeyWasDown = false;
	private static boolean selectKeyWasDown = false;
	private static boolean selectClickArmed = false;
	private static boolean wasPreviewingMove = false;
	private static boolean wasPreviewingCopy = false;
	private static Boolean clickModeActiveIsCopy = null; // null = no click-mode session pending; false = move armed; true = copy armed
	private static final RandomSource PREVIEW_RANDOM = RandomSource.create();
	
	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Pre event)
	{
		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;
		if (player == null || !player.isAlive() || !ClientEditmodeData.isInEditmode())
			return;
		
		EditTools cap = player.getData(MSAttachments.EDIT_TOOLS);
		
		ClientEditToolDrag.doRecycleCode(mc, player, cap);
		ClientEditToolDrag.doReviseCode(mc, player, cap);
		ClientEditToolDrag.doSelectCode(mc, player, cap);
		ClientEditToolDrag.doMoveCopyPreviewCode(mc, player, cap);
	}
	
	@SubscribeEvent
	public static void renderWorld(RenderLevelStageEvent event)
	{
		ClientEditToolDrag.renderOutlines(event);
		ClientEditToolDrag.renderBlockPreview(event);
	}
	
	/**
	 * Renders the textured block models of the pending move/copy ghost preview.
	 */
	private static void renderBlockPreview(RenderLevelStageEvent event)
	{
		Minecraft mc = Minecraft.getInstance();
		if(event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS || mc.player == null
				|| mc.getCameraEntity() != mc.player || !mc.player.isAlive() || !ClientEditmodeData.isInEditmode())
			return;
		
		Player player = mc.player;
		EditTools cap = player.getData(MSAttachments.EDIT_TOOLS);
		
		if(!cap.isPreviewing() || cap.getPreviewAnchor() == null || ClientSelectionCache.getEntries().isEmpty())
			return;
		if(ClientSelectionCache.getEntries().size() > 512)
			return;
		
		int sizeX = ClientSelectionCache.getSizeX();
		int sizeZ = ClientSelectionCache.getSizeZ();
		Rotation rot = Rotation.values()[Math.floorMod(cap.getPreviewRotation(), 4)];
		BlockPos minCorner = cap.getPreviewAnchor();
		
		Camera camera = event.getCamera();
		double camX = camera.getPosition().x;
		double camY = camera.getPosition().y;
		double camZ = camera.getPosition().z;
		
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.depthMask(false);
		
		MultiBufferSource.BufferSource previewBuffer = MultiBufferSource.immediate(new ByteBufferBuilder(4096));
		BlockRenderDispatcher blockRenderer = mc.getBlockRenderer();
		PoseStack poseStack = event.getPoseStack();
		Level level = player.level();
		float alpha = 0.55f;
		
		for(ClientSelectionCache.Entry entry : ClientSelectionCache.getEntries())
		{
			BlockPos dest = minCorner.offset(EditmodeDragPackets.rotateOffset(entry.localOffset(), sizeX, sizeZ, rot));
			BlockState state = entry.state().rotate(rot);
			
			poseStack.pushPose();
			poseStack.translate(dest.getX() - camX, dest.getY() - camY, dest.getZ() - camZ);
			
			for(RenderType ignored : ItemBlockRenderTypes.getRenderLayers(state))
			{
				VertexConsumer raw = previewBuffer.getBuffer(RenderType.translucent());
				VertexConsumer wrapped = new SelectedPreviewRenderer(raw, alpha);
				blockRenderer.renderBatched(state, dest, level, poseStack, wrapped, false, PREVIEW_RANDOM);
			}
			
			poseStack.popPose();
		}
		
		previewBuffer.endBatch();
		
		RenderSystem.depthMask(true);
		RenderSystem.disableBlend();
	}
	
	/**
	 * Resets the drag tool, and removes the server-cursor if the given edit tool is active.
	 * @param cap The current edit-tools capability.
	 */
	private static void cancelDrag(EditTools cap)
	{
		PacketDistributor.sendToServer(new EditmodeDragPackets.Reset());
		cap.resetDragTools();
	}
	
	/**
	 * Attempts to get the currently highlighted block.
	 * If the player is highlighting a block, initialize the target edit-tool's parameters.
	 * @param targetTool The tool to begin using. Must be a drag tool (Revise, Recycle or Select).
	 * @param cap The current edit-tools capability.
	 * @param player Current client-side player.
	 * @return True if the ray hits a block. False if it doesn't
	 */
	private static boolean tryBeginDrag(EditTools.ToolMode targetTool, EditTools cap, Player player)
	{
		BlockHitResult blockHit = getPlayerPOVHitResult(player.level(), player);
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
	 * @param targetTool The tool you want to update. Must be a drag tool (Revise, Recycle or Select).
	 * @param cap The current edit-tools capability.
	 * @param player Current client-side player.
	 * @param isActive Whether the tool is currently in-progress, the physical key being held for
	 *                 drag-style tools, or the click-style armed state for tools that support it.
	 */
	private static void updateDragPosition(EditTools.ToolMode targetTool, EditTools cap, Player player, boolean isActive)
	{
		cap.setEditPos2(getSelectionEndPoint(player, cap.getEditReachDistance(), targetTool == EditTools.ToolMode.REVISE));
		PacketDistributor.sendToServer(new EditmodeDragPackets.Cursor(isActive, cap.getEditPos1(), cap.getEditPos2()));
	}
	
	/**
	 * When the player releases the given tool's key, if a selection is active, a packet for filling/destroying the selected blocks and removing the cursor will be sent.
	 * Also creates the particles and block sounds on the client-side, whereas the packet handles broadcasting those to other players.
	 * @param targetTool The tool you want to finish. Must be a drag tool (Revise or Recycle).
	 * @param cap The current edit-tools capability.
	 * @param player Current client-side player.
	 */
	private static void finishDragging(EditTools.ToolMode targetTool, EditTools cap, Player player)
	{
		if(targetTool == EditTools.ToolMode.REVISE)
			PacketDistributor.sendToServer(new EditmodeDragPackets.Fill(false, cap.getEditPos1(), cap.getEditPos2(), cap.getEditTraceHit(), cap.getEditTraceDirection()));
		else
			PacketDistributor.sendToServer(new EditmodeDragPackets.Destroy(false, cap.getEditPos1(), cap.getEditPos2(), cap.getEditTraceHit(), cap.getEditTraceDirection()));
		playSoundAndSetParticles(player, targetTool == EditTools.ToolMode.REVISE, cap.getEditPos1(), cap.getEditPos2());
	
		cap.resetDragTools();
	}
	
	public static boolean isValidDragToolOrNull(EditTools.ToolMode toolMode) { return toolMode == null || isValidDragTool(toolMode); }
	
	public static boolean isValidDragTool(EditTools.ToolMode toolMode) { return toolMode == EditTools.ToolMode.REVISE || toolMode == EditTools.ToolMode.RECYCLE || toolMode == EditTools.ToolMode.SELECT; }
	
	/**
	 * Handles code for the revise tool on the client-side.
	 */
	public static void doReviseCode(Minecraft mc, Player player, EditTools cap)
	{
		//Return early if there IS a tool active and it ISN'T revise.
		if (cap.getToolMode() != null && cap.getToolMode() != EditTools.ToolMode.REVISE)
			return;
		
		KeyMapping toolKey = mc.options.keyUse;
		
		//If key is pressed, and not allowed to recycle, cancel the tool.
		if(toolKey.isDown() && !canEditRevise(player) && (cap.getToolMode() == null || cap.getToolMode() == EditTools.ToolMode.REVISE))
		{
			cancelDrag(cap);
			return;
		}
		
		//If key has just been pressed, begin drag.
		if(toolKey.isDown() && cap.getEditPos1() == null)
			if(!tryBeginDrag(EditTools.ToolMode.REVISE, cap, player))
				return; //Returns if the player is not highlighting a block.
		
		//If the selection has already successfully found a starting point, find the end-point.
		if(cap.getEditPos1() != null)
			updateDragPosition(EditTools.ToolMode.REVISE, cap, player, toolKey.isDown());
		
		//If key has just been released, finish drag.
		if(!toolKey.isDown() && cap.getEditPos1() != null)
			finishDragging(EditTools.ToolMode.REVISE, cap, player);
		
	}
	
	/**
	 * Determines whether the player can use the revise tool when right-clicking, based on the block that they are holding.
	 * @param player The client-side player.
	 * @return True if you are in editmode and holding a non-deployable block, else false.
	 */
	public static boolean canEditRevise(Player player)
	{
		return (ClientEditmodeData.isInEditmode()
				&& !Minecraft.getInstance().isPaused()
				&& !player.getMainHandItem().isEmpty()
				&& player.getMainHandItem().getItem() instanceof BlockItem
				&& !isBlockDeployable(player));
	}
	
	/**
	 * Handles code for the recycle tool on the client-side.
	 */
	public static void doRecycleCode(Minecraft mc, Player player, EditTools cap)
	{
		//Return early if there IS a tool active and it ISN'T recycle.
		if (cap.getToolMode() != null && cap.getToolMode() != EditTools.ToolMode.RECYCLE)
			return;
		
		KeyMapping toolKey = mc.options.keyAttack;
		
		//If key is pressed, and not allowed to recycle, cancel the tool.
		if(toolKey.isDown() && !canEditRecycle(player) && (cap.getToolMode() == null || cap.getToolMode() == EditTools.ToolMode.RECYCLE))
		{
			cancelDrag(cap);
			return;
		}
		
		//If key has just been pressed, begin drag.
		if(toolKey.isDown() && cap.getEditPos1() == null)
			if(!tryBeginDrag(EditTools.ToolMode.RECYCLE, cap, player))
				return; //Returns if the player is not highlighting a block.
		
		//If the selection has already successfully found a starting point, find the end-point.
		if(cap.getEditPos1() != null)
			updateDragPosition(EditTools.ToolMode.RECYCLE, cap, player, toolKey.isDown());
		
		//If key has just been released, finish drag.
		if(!toolKey.isDown() && cap.getEditPos1() != null)
			finishDragging(EditTools.ToolMode.RECYCLE, cap, player);

	}
	
	/**
	 * Handles the box-selection tool.
	 */
	public static void doSelectCode(Minecraft mc, Player player, EditTools cap)
	{
		if(cap.getToolMode() != null && cap.getToolMode() != EditTools.ToolMode.SELECT)
			return;
		
		KeyMapping toolKey = MSKeyHandler.selectKey;
		boolean clickMode = MinestuckConfig.CLIENT.clickToSelect.get();
		boolean pressedEdge = toolKey.isDown() && !selectKeyWasDown;
		
		boolean active;
		boolean shouldCommit;
		
		if(clickMode)
		{
			if(!ClientEditmodeData.isInEditmode() || mc.isPaused())
				selectClickArmed = false; //full cancel on anything that invalidates the session
			
			shouldCommit = false;
			
			if(!selectClickArmed && pressedEdge)
				selectClickArmed = true; //If first press then arm and start growing the box
			else if(selectClickArmed && pressedEdge)
				shouldCommit = true; //If second press then lock the selection in
			
			active = selectClickArmed;
		}
		else
		{
			active = toolKey.isDown();
			shouldCommit = selectKeyWasDown && !toolKey.isDown();
		}
		
		if(active && (!ClientEditmodeData.isInEditmode() || mc.isPaused())
				&& (cap.getToolMode() == null || cap.getToolMode() == EditTools.ToolMode.SELECT))
		{
			cancelDrag(cap);
			selectClickArmed = false;
			selectKeyWasDown = toolKey.isDown();
			return;
		}
		
		if(active && cap.getEditPos1() == null)
		{
			if(!tryBeginDrag(EditTools.ToolMode.SELECT, cap, player))
			{
				selectKeyWasDown = toolKey.isDown();
				return;
			}
		}
		
		if(cap.getEditPos1() != null)
			updateDragPosition(EditTools.ToolMode.SELECT, cap, player, active);
		
		if(shouldCommit && cap.getEditPos1() != null)
		{
			cap.setSelectionPos1(cap.getEditPos1());
			cap.setSelectionPos2(cap.getEditPos2());
			cap.setPreviewRotation(0);
			
			BlockPos a = cap.getEditPos1(), b = cap.getEditPos2();
			BlockPos min = new BlockPos(Math.min(a.getX(), b.getX()), Math.min(a.getY(), b.getY()), Math.min(a.getZ(), b.getZ()));
			BlockPos max = new BlockPos(Math.max(a.getX(), b.getX()), Math.max(a.getY(), b.getY()), Math.max(a.getZ(), b.getZ()));
			cap.setOriginalSelection(min, max);
			ClientSelectionCache.capture(player.level(), min, max);
			
			player.playSound(MSSoundEvents.EVENT_EDIT_TOOL_SELECT.get(), 1.0f, 1.0f);
			
			PacketDistributor.sendToServer(new EditmodeDragPackets.Reset());
			cap.resetDragTools();
			selectClickArmed = false;
		}
		selectKeyWasDown = toolKey.isDown();
	}
	
	/**
	 * Handles the hold-to-preview/release-to-commit behaviour of the Move and Copy tools.
	 */
	public static void doMoveCopyPreviewCode(Minecraft mc, Player player, EditTools cap)
	{
		KeyMapping moveKey = MSKeyHandler.moveKey;
		KeyMapping copyKey = MSKeyHandler.copyKey;
		
		boolean hasSelection = cap.getSelectionPos1() != null && cap.getSelectionPos2() != null;
		boolean canPreview = hasSelection && ClientEditmodeData.isInEditmode() && !mc.isPaused() && cap.getToolMode() == null;
		boolean clickMode = MinestuckConfig.CLIENT.clickToPlace.get();
		
		boolean movePressedEdge = moveKey.isDown() && !moveKeyWasDown;
		boolean copyPressedEdge = copyKey.isDown() && !copyKeyWasDown;
		
		boolean moveDown;
		boolean copyDown;
		boolean commitMove;
		boolean commitCopy;
		
		if(clickMode)
		{
			if(!canPreview)
				clickModeActiveIsCopy = null;
			
			commitMove = false;
			commitCopy = false;
			
			if(clickModeActiveIsCopy == null)
			{
				if(canPreview && movePressedEdge)
					clickModeActiveIsCopy = false; //arm move
				else if(canPreview && copyPressedEdge)
					clickModeActiveIsCopy = true; //arm copy
			}
			else if(!clickModeActiveIsCopy && movePressedEdge)
			{
				commitMove = true;
				clickModeActiveIsCopy = null;
			}
			else if(clickModeActiveIsCopy && copyPressedEdge)
			{
				commitCopy = true;
				clickModeActiveIsCopy = null;
			}
			
			moveDown = clickModeActiveIsCopy != null && !clickModeActiveIsCopy;
			copyDown = clickModeActiveIsCopy != null && clickModeActiveIsCopy;
		}
		else
		{
			moveDown = canPreview && moveKey.isDown() && !copyKey.isDown();
			copyDown = canPreview && copyKey.isDown() && !moveKey.isDown();
			
			commitMove = moveKeyWasDown && !moveKey.isDown();
			commitCopy = copyKeyWasDown && !copyKey.isDown();
		}
		
		boolean previewJustStarted = (moveDown && !wasPreviewingMove) || (copyDown && !wasPreviewingCopy);
		if(previewJustStarted)
		{
			if(cap.getSelectionPos1() != null && cap.getSelectionPos2() != null)
			{
				BlockPos a = cap.getSelectionPos1(), b = cap.getSelectionPos2();
				BlockPos captureMin = new BlockPos(Math.min(a.getX(), b.getX()), Math.min(a.getY(), b.getY()), Math.min(a.getZ(), b.getZ()));
				BlockPos captureMax = new BlockPos(Math.max(a.getX(), b.getX()), Math.max(a.getY(), b.getY()), Math.max(a.getZ(), b.getZ()));
				ClientSelectionCache.capture(player.level(), captureMin, captureMax);
			}
			
			//default placement distance scales with the footprint, so big structures land clear of the player by default
			double diagonal = Math.sqrt((double) ClientSelectionCache.getSizeX() * ClientSelectionCache.getSizeX()
					+ (double) ClientSelectionCache.getSizeZ() * ClientSelectionCache.getSizeZ());
			cap.setPreviewDistance(Math.max(3.0, diagonal / 2.0 + 2.5));
		}
		
		BlockPos lastAnchor = cap.getPreviewAnchor();
		
		if(moveDown || copyDown)
		{
			double distance = cap.getPreviewDistance();
			if(MSKeyHandler.zoomInKey.isDown())
				distance += 0.6;
			if(MSKeyHandler.zoomOutKey.isDown())
				distance -= 0.6;
			cap.setPreviewDistance(Mth.clamp(distance, 1.0, MinestuckConfig.CLIENT.toolsDistance.get()));
			
			Vec3 eye = player.getEyePosition();
			Vec3 look = player.getLookAngle();
			Vec3 target = eye.add(look.scale(cap.getPreviewDistance()));
			BlockPos aimPoint = BlockPos.containing(target.x, target.y, target.z);
			
			Rotation rot = Rotation.values()[Math.floorMod(cap.getPreviewRotation(), 4)];
			boolean swapXZ = rot == Rotation.CLOCKWISE_90 || rot == Rotation.COUNTERCLOCKWISE_90;
			int footprintX = swapXZ ? ClientSelectionCache.getSizeZ() : ClientSelectionCache.getSizeX();
			int footprintZ = swapXZ ? ClientSelectionCache.getSizeX() : ClientSelectionCache.getSizeZ();
			
			BlockPos minCorner = aimPoint.offset(-(footprintX / 2), -(ClientSelectionCache.getSizeY() / 2), -(footprintZ / 2));
			
			cap.setPreview(copyDown, minCorner);
			lastAnchor = minCorner;
		}
		else
		{
			cap.clearPreview();
		}
		
		wasPreviewingMove = moveDown;
		wasPreviewingCopy = copyDown;
		
		if(commitMove)
			commitSelectionAction(cap, false, lastAnchor);
		if(commitCopy)
			commitSelectionAction(cap, true, lastAnchor);
		
		moveKeyWasDown = moveKey.isDown();
		copyKeyWasDown = copyKey.isDown();
	}
	
	public static void clearSelection()
	{
		Minecraft mc = Minecraft.getInstance();
		if(mc.player == null)
			return;
		
		EditTools cap = mc.player.getData(MSAttachments.EDIT_TOOLS);
		if(cap.getSelectionPos1() == null && cap.getSelectionPos2() == null)
			return;
		
		cap.clearSelection();
		cap.clearOriginalSelection();
		cap.clearPreview();
		clickModeActiveIsCopy = null;
		ClientSelectionCache.clear();
		mc.player.playSound(MSSoundEvents.EVENT_EDIT_TOOL_CLEAR.get(), 1.0f, 1.0f);
	}
	
	public static void cycleRotation()
	{
		Minecraft mc = Minecraft.getInstance();
		if(mc.player == null)
			return;
		EditTools cap = mc.player.getData(MSAttachments.EDIT_TOOLS);
		if(!cap.isPreviewing())
			return;
		cap.setPreviewRotation(cap.getPreviewRotation() + 1);
	}
	
	private static void commitSelectionAction(EditTools cap, boolean isCopy, @Nullable BlockPos anchor)
	{
		int rotation = cap.getPreviewRotation();
		cap.clearPreview();
		
		BlockPos oldPos1 = cap.getSelectionPos1();
		BlockPos oldPos2 = cap.getSelectionPos2();
		if(anchor == null || oldPos1 == null || oldPos2 == null)
			return;
		
		if(isCopy)
			PacketDistributor.sendToServer(new EditmodeDragPackets.CopySelection(oldPos1, oldPos2, anchor, rotation));
		else
			PacketDistributor.sendToServer(new EditmodeDragPackets.MoveSelection(oldPos1, oldPos2, anchor, rotation));
		
		Player localPlayer = Minecraft.getInstance().player;
		if(localPlayer != null)
			localPlayer.playSound(isCopy ? MSSoundEvents.EVENT_EDIT_TOOL_COPY.get() : MSSoundEvents.EVENT_EDIT_TOOL_MOVE.get(), 1.0f, 1.0f);
		
		if(!isCopy)
			startMoveTransitionAnimation(oldPos1, oldPos2, anchor, rotation);
	}
	
	private static void startMoveTransitionAnimation(BlockPos oldPos1, BlockPos oldPos2, BlockPos anchor, int rotation)
	{
		BlockPos min = new BlockPos(Math.min(oldPos1.getX(), oldPos2.getX()), Math.min(oldPos1.getY(), oldPos2.getY()), Math.min(oldPos1.getZ(), oldPos2.getZ()));
		int sizeX = ClientSelectionCache.getSizeX(), sizeZ = ClientSelectionCache.getSizeZ();
		Rotation rot = Rotation.values()[Math.floorMod(rotation, 4)];
		
		List<BlockPos> from = new ArrayList<>();
		List<BlockPos> to = new ArrayList<>();
		for(ClientSelectionCache.Entry entry : ClientSelectionCache.getEntries())
		{
			from.add(min.offset(entry.localOffset()));
			to.add(anchor.offset(EditmodeDragPackets.rotateOffset(entry.localOffset(), sizeX, sizeZ, rot)));
		}
		ClientMoveTransitions.start(from, to);
	}
	
	/**
	 * Determines whether the player can use the recycle tool when left-clicking,
	 * based on the block that they are looking at.
	 * @param player The client-side editmode player.
	 * @return True if you are in editmode and NOT looking directly at a multiblock or unbreakable block. Else false.
	 */
	public static boolean canEditRecycle(Player player)
	{
		BlockHitResult blockHit = getPlayerPOVHitResult(player.level(), player);
		BlockState block = player.level().getBlockState(blockHit.getBlockPos());
		
		return (ClientEditmodeData.isInEditmode()
				&& !Minecraft.getInstance().isPaused()
				&& !(block.getDestroySpeed(player.level(), blockHit.getBlockPos()) < 0 || block.is(MSTags.Blocks.EDITMODE_BREAK_BLACKLIST))
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
					if(!fill && !player.level().getBlockState(pos).isAir()
							&& (ClientPlayerData.getGristCache(ClientPlayerData.CacheSource.EDITMODE).canAfford(ServerEditHandler.blockBreakCost())
							|| ClientDeployList.getEntry(player.level().getBlockState(pos).getCloneItemStack(null, player.level(), pos, player)) != null))
					{
						anyBlockEdited = true;
						
						player.level().addDestroyBlockEffect(pos, player.level().getBlockState(pos));
					}
					else if(fill && player.level().getBlockState(pos).canBeReplaced() && ClientPlayerData.getGristCache(ClientPlayerData.CacheSource.EDITMODE).canAfford(ClientEditHandler.itemCost(stack, player.level())))
					{
						anyBlockEdited = true;
					}
				}
			}
		}
		
		//Play edit sound locally, if a block is able to be placed/broken.
		if(anyBlockEdited)
			player.level().playSound(player, positionEnd, fill ? MSSoundEvents.EVENT_EDIT_TOOL_REVISE.get() : MSSoundEvents.EVENT_EDIT_TOOL_RECYCLE.get(), SoundSource.AMBIENT, 1.0f, fill ? 1.0f : 0.85f);
	}
	
	/**
	 * Calculates the second corner of a revise/recycle/select selection,
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
		BlockHitResult blockHit = getPlayerPOVHitResult(player.level(), player);

		//if not looking directly at a block, use the position where the player is looking at with the initial distance of editPos1 from the camera
		if (blockHit.getType() == BlockHitResult.Type.MISS)
		{
			Vec3 eyePosition = player.getEyePosition();
			Vec3 lookDirection = player.getLookAngle();
			Vec3 selectionPosition = eyePosition.add(lookDirection.x * reachDistance, lookDirection.y * reachDistance, lookDirection.z * reachDistance);
			return BlockPos.containing(selectionPosition.x, selectionPosition.y, selectionPosition.z);
		}
		else
		{
			if(shouldBlockOffset)
				return player.level().getBlockState(blockHit.getBlockPos()).canBeReplaced() ? blockHit.getBlockPos() : blockHit.getBlockPos().offset(blockHit.getDirection().getNormal());
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
			ItemStack stack	= player.getMainHandItem();
		
			return ClientDeployList.getEntry(stack) != null && stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof MachineBlock;
	}
	
	/**
	 * Determines whether the block that the player is highlighting is a destroyable multiblock or not.
	 * @param player The client-side editmode player.
	 * @return True if the player is looking at a block, and the block implements EditmodeDestroyable.
	 */
	private static boolean isMultiblock(Player player)
	{
		BlockPos blockLookingAt = getPlayerPOVHitResult(player.level(), player).getBlockPos();
		
		return player.level().getBlockState(blockLookingAt).getBlock() instanceof EditmodeDestroyable;
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
		double reachDistance = player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE);
		Vec3 endVec = eyeVec.add((double) xComponent * reachDistance, (double) yComponent * reachDistance, (double) zComponent * reachDistance);
		return level.clip(new ClipContext(eyeVec, endVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
	}
	
	/**
	 * Renders the outlines the active drag box (revise/recycle/select)
	 * Green if revise, red if recycle, cyan if selected, green if pasted, orange if moved
	 */
	public static void renderOutlines(RenderLevelStageEvent event)
	{
		Minecraft mc = Minecraft.getInstance();
		
		//make sure the stage is after translucent blocks so that the outlines render over everything.
		if(event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS && mc.player != null && mc.getCameraEntity() == mc.player
				&& mc.player.isAlive() && ClientEditmodeData.isInEditmode())
		{
			
			Player player = mc.player;
			Camera info = event.getCamera();
			
			EditTools cap = player.getData(MSAttachments.EDIT_TOOLS);
			
			double d1 = info.getPosition().x;
			double d2 = info.getPosition().y;
			double d3 = info.getPosition().z;
			
			RenderSystem.defaultBlendFunc();
			RenderSystem.lineWidth(2.0F);
			RenderSystem.depthMask(false);
			
			MultiBufferSource.BufferSource renderTypeBuffer = MultiBufferSource.immediate(new ByteBufferBuilder(2048));
			VertexConsumer lineBuffer = renderTypeBuffer.getBuffer(RenderType.LINES);
			
			//:1 selection outline
			if(cap.getSelectionPos1() != null && cap.getSelectionPos2() != null)
			{
				BlockPos selA = cap.getSelectionPos1();
				BlockPos selB = cap.getSelectionPos2();
				AABB selectionBox = new AABB(
						Math.min(selA.getX(), selB.getX()), Math.min(selA.getY(), selB.getY()), Math.min(selA.getZ(), selB.getZ()),
						Math.max(selA.getX(), selB.getX()) + 1, Math.max(selA.getY(), selB.getY()) + 1, Math.max(selA.getZ(), selB.getZ()) + 1)
						.move(-d1, -d2, -d3).deflate(0.002);
				
				drawBoxOutline(event.getPoseStack(), lineBuffer, selectionBox, 0, 1, 1, 1);
			}
			
			//:2 active drag box outline
			if(isValidDragTool(cap.getToolMode()) && cap.getEditPos1() != null && cap.getEditPos2() != null)
			{
				BlockPos posA = cap.getEditPos1();
				BlockPos posB = cap.getEditPos2();
				AABB boundingBox = new AABB(
						Math.min(posA.getX(), posB.getX()), Math.min(posA.getY(), posB.getY()), Math.min(posA.getZ(), posB.getZ()),
						Math.max(posA.getX(), posB.getX()) + 1, Math.max(posA.getY(), posB.getY()) + 1, Math.max(posA.getZ(), posB.getZ()) + 1)
						.move(-d1, -d2, -d3).deflate(0.002);
				
				float red = cap.getToolMode() == EditTools.ToolMode.RECYCLE ? 1 : 0;
				float green = cap.getToolMode() == EditTools.ToolMode.REVISE ? 1 : 0;
				float blue = cap.getToolMode() == EditTools.ToolMode.SELECT ? 1 : 0;
				
				drawBoxOutline(event.getPoseStack(), lineBuffer, boundingBox, red, green, blue, 1);
			}
			
			//:3 floating move/copy ghost preview
			if(cap.isPreviewing() && cap.getPreviewAnchor() != null && !ClientSelectionCache.getEntries().isEmpty())
			{
				int sizeX = ClientSelectionCache.getSizeX();
				int sizeY = ClientSelectionCache.getSizeY();
				int sizeZ = ClientSelectionCache.getSizeZ();
				Rotation rot = Rotation.values()[Math.floorMod(cap.getPreviewRotation(), 4)];
				BlockPos minCorner = cap.getPreviewAnchor(); //already the min-corner centering is applied when this is computed
				
				boolean swapXZ = rot == Rotation.CLOCKWISE_90 || rot == Rotation.COUNTERCLOCKWISE_90;
				int footprintX = swapXZ ? sizeZ : sizeX;
				int footprintZ = swapXZ ? sizeX : sizeZ;
				
				float r = cap.isPreviewCopy() ? 0f : 1f; //move = orange, copy = green
				float g = cap.isPreviewCopy() ? 1f : 0.5f;
				
				AABB footprintBox = new AABB(minCorner.getX(), minCorner.getY(), minCorner.getZ(),
						minCorner.getX() + footprintX, minCorner.getY() + sizeY, minCorner.getZ() + footprintZ)
						.move(-d1, -d2, -d3).deflate(0.002);
				drawBoxOutline(event.getPoseStack(), lineBuffer, footprintBox, r, g, 0f, 1f);
			}
			
			//:4 move animation
			for(double[] pos : ClientMoveTransitions.getInterpolatedPositions())
			{
				AABB box = new AABB(pos[0], pos[1], pos[2], pos[0] + 1, pos[1] + 1, pos[2] + 1).move(-d1, -d2, -d3).deflate(0.03);
				drawBoxOutline(event.getPoseStack(), lineBuffer, box, 1f, 0.8f, 0.2f, 0.7f);
			}
			
			renderTypeBuffer.endBatch();
			
			RenderSystem.depthMask(true);
			RenderSystem.disableBlend();
		}
	}
	
	//taken directly from machineOutlineRenderer's drawPhernaliaPlacementOutline function, which was taken from LevelRenderer's drawShape function
	private static void drawReviseToolOutline(PoseStack poseStack, VertexConsumer bufferIn, VoxelShape shapeIn, double xIn, double yIn, double zIn, float red, float green, float blue, float alpha)
	{
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
			bufferIn.addVertex(matrix4f, (float)(startX + xIn), (float)(startY + yIn), (float)(startZ + zIn))
					.setColor(red, green, blue, alpha)
					.setNormal(pose, dX, dY, dZ);
			bufferIn.addVertex(matrix4f, (float)(endX + xIn), (float)(endY + yIn), (float)(endZ + zIn))
					.setColor(red, green, blue, alpha)
					.setNormal(pose, dX, dY, dZ);
		});
	}
	
	private static void drawBoxOutline(PoseStack poseStack, VertexConsumer buffer, AABB box, float r, float g, float b, float a)
	{
		drawReviseToolOutline(poseStack, buffer, Shapes.create(box), 0, 0, 0, r, g, b, a);
	}
	
	public static void cancelClickSessions()
	{
		clickModeActiveIsCopy = null;
		selectClickArmed = false;
	}
}
