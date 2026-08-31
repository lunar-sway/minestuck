package com.mraof.minestuck.computer.editmode;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

/**
 * The Edit Tools capability keeps track of the positional and raytrace data
 * for the Sburb Editmode building tools. This includes the starting position of the revise tool,
 * whether the cursor is being dragged after pressing right click, and the position and facing
 * of raytraces.
 *
 * @see ClientEditHandler
 */
public final class EditTools
{
	enum ToolMode
	{
		REVISE,
		RECYCLE,
		SELECT
	}
	
	private ToolMode toolMode = null;
	private BlockPos editPos1 = null;
	private BlockPos editPos2 = null;
	private Vec3 editTraceHit = new Vec3(0, 0, 0);
	private Direction editTraceDirection = Direction.NORTH;
	private double editReachDistance = 0;
	private UUID editCursorID = null;
	private BlockPos originalSelectionPos1 = null;
	private BlockPos originalSelectionPos2 = null;
	private BlockPos selectionPos1 = null;
	private BlockPos selectionPos2 = null;
	private boolean isEditDragging = false;
	@Nullable
	private Boolean previewIsCopy = null;
	@Nullable
	private BlockPos previewAnchor = null;
	private int previewRotation = 0;
	private double previewDistance = 4.0;
	
	public boolean isPreviewing() { return previewIsCopy != null; }
	public boolean isPreviewCopy() { return Boolean.TRUE.equals(previewIsCopy); }
	
	@Nullable
	public BlockPos getPreviewAnchor() { return previewAnchor; }
	
	public void setPreview(boolean isCopy, BlockPos anchor)
	{
		previewIsCopy = isCopy;
		previewAnchor = anchor;
	}
	
	public int getPreviewRotation() { return previewRotation; }
	
	public double getPreviewDistance() { return previewDistance; }
	
	public void setPreviewRotation(int rotation) { previewRotation = Math.floorMod(rotation, 4); }
	
	public void setPreviewDistance(double distance) { previewDistance = distance; }
	
	@Nullable
	public ToolMode getToolMode()
	{
		return toolMode;
	}
	
	@Nullable
	public BlockPos getEditPos1()
	{
		return editPos1;
	}
	
	@Nullable
	public BlockPos getEditPos2()
	{
		return editPos2;
	}
	
	@Nonnull
	public Vec3 getEditTraceHit()
	{
		return editTraceHit;
	}
	
	@Nonnull
	public Direction getEditTraceDirection()
	{
		return editTraceDirection;
	}
	
	public double getEditReachDistance()
	{
		return editReachDistance;
	}
	
	@Nullable
	public UUID getEditCursorID()
	{
		return editCursorID;
	}
	
	@Nullable
	public BlockPos getOriginalSelectionPos1() { return originalSelectionPos1; }
	
	@Nullable
	public BlockPos getOriginalSelectionPos2() { return originalSelectionPos2; }
	
	
	@Nullable
	public BlockPos getSelectionPos1() { return selectionPos1; }
	
	@Nullable
	public BlockPos getSelectionPos2() { return selectionPos2; }
	
	public void setToolMode(ToolMode mode)
	{
		toolMode = mode;
	}
	
	public void setEditPos1(BlockPos pos)
	{
		editPos1 = pos;
	}
	
	public void setEditPos2(BlockPos pos)
	{
		editPos2 = pos;
	}
	
	public void setOriginalSelection(BlockPos min, BlockPos max)
	{
		originalSelectionPos1 = min;
		originalSelectionPos2 = max;
	}
	
	public void setSelectionPos1(BlockPos pos) { selectionPos1 = pos; }
	public void setSelectionPos2(BlockPos pos) { selectionPos2 = pos; }
	
	public void setEditTrace(Vec3 hit, Direction direction)
	{
		setEditTraceHit(hit);
		setEditTraceDirection(direction);
	}
	
	public void clearPreview()
	{
		previewIsCopy = null;
		previewAnchor = null;
	}
	
	public void clearOriginalSelection()
	{
		originalSelectionPos1 = null;
		originalSelectionPos2 = null;
	}
	
	public void clearSelection()
	{
		selectionPos1 = null;
		selectionPos2 = null;
		clearOriginalSelection();
	}
	
	private void setEditTraceHit(Vec3 hit)
	{
		editTraceHit = hit;
	}
	
	private void setEditTraceDirection(Direction direction)
	{
		editTraceDirection = direction;
	}
	
	public void setEditReachDistance(double reachDistance)
	{
		editReachDistance = reachDistance;
	}
	
	public void setEditCursorID(UUID uuid)
	{
		editCursorID = uuid;
	}
	
	
	public void beginDragTools(ToolMode toolMode, BlockHitResult blockHit, Player player)
	{
		setToolMode(toolMode);
		if(toolMode == ToolMode.REVISE)
			setEditPos1(player.level().getBlockState(blockHit.getBlockPos()).canBeReplaced() ? blockHit.getBlockPos() : blockHit.getBlockPos().offset(blockHit.getDirection().getNormal()));
		else
			setEditPos1(blockHit.getBlockPos());
		setEditTrace(blockHit.getLocation(), blockHit.getDirection());
		setEditReachDistance(blockHit.getLocation().distanceTo(player.getEyePosition()));
	}
	
	public void resetDragTools()
	{
		setToolMode(null);
		setEditPos1(null);
		setEditPos2(null);
		setEditTrace(new Vec3(0, 0, 0), Direction.NORTH);
		setEditReachDistance(0);
		setEditCursorID(null);
	}
}
