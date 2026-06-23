package com.mraof.minestuck.entity.ai;

import com.mraof.minestuck.entity.KernelspriteEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.Optional;
import java.util.stream.Stream;

public class GoToPoiGoal extends MoveToBlockGoal
{
	private final KernelspriteEntity kernelsprite;
	private final ResourceKey<PoiType> poiKey;
	private final Vec3i posOffset;
	private final double acceptedDistance;
	private final int searchRange;
	private final boolean waitPermanently;
	private int duration;
	
	public GoToPoiGoal(KernelspriteEntity mob, ResourceKey<PoiType> poiKey, Vec3i posOffset, double speedModifier, int duration, int searchRange, double acceptedDistance, boolean waitPermanently)
	{
		//does not make use of original searchRange
		super(mob, speedModifier, 0, 0);
		
		this.kernelsprite = mob;
		this.poiKey = poiKey;
		this.posOffset = posOffset;
		this.duration = duration;
		this.searchRange = searchRange;
		this.acceptedDistance = acceptedDistance;
		this.waitPermanently = waitPermanently;
		this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
	}
	
	@Override
	public boolean canUse()
	{
		return kernelsprite != null && findNearestBlock();
	}
	
	@Override
	public boolean canContinueToUse()
	{
		return super.canContinueToUse() && duration > 0 && !isReachedTarget();
	}
	
	@Override
	public void start()
	{
		super.start();
		
		//try to prioritize air block next to target
		Level level = kernelsprite.level();
		if(level.getBlockState(blockPos).blocksMotion())
		{
			for(BlockPos iteratePos : BlockPos.betweenClosed(blockPos.offset(1, 1, 1), blockPos.offset(-1, -1, -1)))
			{
				if(kernelsprite.isWithinRestriction(iteratePos) && !level.getBlockState(iteratePos).blocksMotion())
				{
					blockPos = iteratePos;
					break;
				}
			}
		}
		
		kernelsprite.getDialogueComponent().setHasReachedTarget(false);
		kernelsprite.clearRestriction();
		kernelsprite.setRandomMoveGoal(false);
	}
	
	@Override
	public void stop()
	{
		super.stop();
		kernelsprite.goalSelector.removeGoal(this);
		kernelsprite.setStayPutGoal(true);
		kernelsprite.setWanderRadius(!waitPermanently);
		
		if(waitPermanently)
			kernelsprite.restrictTo(kernelsprite.blockPosition(), (int) acceptedDistance);
	}
	
	@Override
	protected boolean isReachedTarget()
	{
		boolean reachedTarget = super.isReachedTarget();
		
		if(reachedTarget)
			kernelsprite.getDialogueComponent().setHasReachedTarget(true);
		
		return reachedTarget;
	}
	
	@Override
	public double acceptedDistance()
	{
		return acceptedDistance;
	}
	
	@Override
	protected void moveMobToBlock()
	{
		super.moveMobToBlock();
		kernelsprite.getMoveControl().setWantedPosition(blockPos.getX() + 0.5, blockPos.getY() + 2, blockPos.getZ() + 0.5, 1);
	}
	
	@Override
	public void tick()
	{
		super.tick();
		
		duration--;
	}
	
	@Override
	protected boolean findNearestBlock()
	{
		//complete overhaul
		BlockPos mobPos = kernelsprite.blockPosition();
		PoiManager manager = ((ServerLevel) kernelsprite.level()).getPoiManager();
		Stream<PoiRecord> recordStream = manager.getInRange(holder -> holder.is(poiKey), mobPos, searchRange, PoiManager.Occupancy.ANY);
		Optional<BlockPos> oPos = recordStream.map(PoiRecord::getPos).min(Comparator.comparingDouble(pos -> pos.distSqr(pos)));
		
		oPos.ifPresent(pos -> this.blockPos = pos.offset(posOffset));
		return oPos.isPresent();
	}
	
	@Override
	protected boolean isValidTarget(LevelReader level, BlockPos pos)
	{
		return false;
	}
}
