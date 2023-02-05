package com.mraof.minestuck.computer.editmode;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class EditTools implements IEditTools
{
	private BlockPos editPos1 = null;
	private BlockPos editPos2 = null;
	private Vec3 editTraceHit = new Vec3(0,0,0);
	private Direction editTraceDirection = Direction.NORTH;
	private boolean isEditDragging = false;
	
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
	public boolean isEditDragging() {
		return isEditDragging;
	}
	
	@Override
	public void setEditDragging(boolean v) {
		isEditDragging = v;
	}
}
