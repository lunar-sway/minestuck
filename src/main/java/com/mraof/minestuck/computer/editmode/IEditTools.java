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
@Deprecated //todo switch to just EditTools
public interface IEditTools
{
	enum ToolMode
	{
		REVISE,
		RECYCLE
	}
	
	@Nullable
	ToolMode getToolMode();
	@Nullable
	BlockPos getEditPos1();
	@Nullable
	BlockPos getEditPos2();
	@Nonnull
	Vec3 getEditTraceHit();
	@Nonnull
	Direction getEditTraceDirection();
	double getEditReachDistance();
	@Nullable
	UUID getEditCursorID();
	
	void setToolMode(ToolMode mode);
	void setEditPos1(BlockPos pos);
	void setEditPos2(BlockPos pos);
	void setEditTrace(Vec3 hit, Direction direction);
	void setEditReachDistance(double reachDistance);
	void setEditCursorID(UUID uuid);
	
	void beginDragTools(ToolMode toolMode, BlockHitResult blockHit, Player player);
	void resetDragTools();
}
