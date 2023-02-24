package com.mraof.minestuck.computer.editmode;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class EditTools implements IEditTools
{
	
	
	private ToolMode toolMode = null;
	private BlockPos editPos1 = null;
	private BlockPos editPos2 = null;
	private Vec3 editTraceHit = new Vec3(0,0,0);
	private Direction editTraceDirection = Direction.NORTH;
	private double editReachDistance = 0;
	private boolean isEditDragging = false;
	private UUID editCursorID = null;
	
	@Override
	public ToolMode getToolMode() { return toolMode; }
	
	@Override
	public BlockPos getEditPos1() {
		return editPos1;
	}
	
	@Override
	public BlockPos getEditPos2() {
		return editPos2;
	}
	
	@Override
	public Vec3 getEditTraceHit() {
		return editTraceHit;
	}
	
	@Override
	public Direction getEditTraceDirection() {
		return editTraceDirection;
	}
	
	@Override
	public double getEditReachDistance() { return editReachDistance; }
	
	@Override
	public UUID getEditCursorID() { return editCursorID; }
	
	@Override
	public void setToolMode(ToolMode mode) { toolMode = mode; }
	
	@Override
	public void setEditPos1(BlockPos pos) {
		editPos1 = pos;
	}
	
	@Override
	public void setEditPos2(BlockPos pos) {
		editPos2 = pos;
	}
	
	@Override
	public void setEditTraceHit(Vec3 hit) {
		editTraceHit = hit;
	}
	
	@Override
	public void setEditTraceDirection(Direction direction) {
		editTraceDirection = direction;
	}
	
	@Override
	public void setEditReachDistance(double reachDistance) { editReachDistance = reachDistance; }
	
	@Override
	public void setEditCursorID(UUID uuid) { editCursorID = uuid; }
	
	@Override
	public boolean isEditDragging() {
		return isEditDragging;
	}
	
	@Override
	public void setEditDragging(boolean v) {
		isEditDragging = v;
	}
}
