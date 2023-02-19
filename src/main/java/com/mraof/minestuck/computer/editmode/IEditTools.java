package com.mraof.minestuck.computer.editmode;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

/**
 * The Edit Tools capability keeps track of the positional and raytrace data
 * for the Sburb Editmode building tools. This includes the starting position of the revise tool,
 * whether the cursor is being dragged after pressing right click, and the position and facing
 * of raytraces.
 *
 * @see ClientEditHandler
 */

public interface IEditTools
{
	BlockPos getEditPos1();
	BlockPos getEditPos2();
	Vec3 getEditTraceHit();
	Direction getEditTraceDirection();
	double getEditReachDistance();
	UUID getEditCursorID();
	void setEditPos1(BlockPos pos);
	void setEditPos2(BlockPos pos);
	void setEditTraceHit(Vec3 hit);
	void setEditTraceDirection(Direction direction);
	void setEditReachDistance(double reachDistance);
	void setEditCursorID(UUID uuid);
	
	boolean isEditDragging();
	void setEditDragging(boolean v);
}
