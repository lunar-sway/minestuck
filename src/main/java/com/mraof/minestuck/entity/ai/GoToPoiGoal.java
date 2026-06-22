package com.mraof.minestuck.entity.ai;

import com.mraof.minestuck.entity.KernelspriteEntity;
import com.mraof.minestuck.entity.dialogue.DialogueEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.LevelReader;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.Optional;
import java.util.stream.Stream;

public class GoToPoiGoal extends MoveToBlockGoal
{
	private final DialogueEntity dialogueMob;
	private final ResourceKey<PoiType> poiKey;
	private final double acceptedDistance;
	private final int searchRange;
	private final boolean waitPermanently;
	private int duration;
	
	public GoToPoiGoal(PathfinderMob mob, ResourceKey<PoiType> poiKey, double speedModifier, int duration, int searchRange, double acceptedDistance, boolean waitPermanently)
	{
		super(mob, speedModifier, 0, 0);
		
		if(mob instanceof DialogueEntity dialogueMob)
			this.dialogueMob = dialogueMob;
		else
			this.dialogueMob = null;
		this.poiKey = poiKey;
		this.duration = duration;
		this.searchRange = searchRange;
		this.acceptedDistance = acceptedDistance;
		this.waitPermanently = waitPermanently;
		this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
	}
	
	@Override
	public boolean canUse()
	{
		return dialogueMob != null && findNearestBlock();
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
		
		dialogueMob.getDialogueComponent().setHasReachedTarget(false);
		mob.clearRestriction();
		
		if(mob instanceof KernelspriteEntity kernelsprite)
			kernelsprite.setRandomMoveGoal(false);
	}
	
	@Override
	public void stop()
	{
		super.stop();
		mob.goalSelector.removeGoal(this);
		
		if(mob instanceof KernelspriteEntity kernelsprite)
		{
			kernelsprite.setStayPutGoal(true);
			kernelsprite.setWanderRadius(!waitPermanently);
		}
		
		if(waitPermanently)
			mob.restrictTo(mob.blockPosition(), (int) acceptedDistance);
	}
	
	@Override
	protected boolean isReachedTarget()
	{
		boolean reachedTarget = super.isReachedTarget();
		
		if(reachedTarget)
			dialogueMob.getDialogueComponent().setHasReachedTarget(true);
		
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
		
		if(mob instanceof KernelspriteEntity kernelsprite)
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
		BlockPos mobPos = mob.blockPosition();
		PoiManager manager = ((ServerLevel) mob.level()).getPoiManager();
		Stream<PoiRecord> recordStream = manager.getInRange(holder -> holder.is(poiKey), mobPos, searchRange, PoiManager.Occupancy.ANY);
		Optional<BlockPos> oPos = recordStream.map(PoiRecord::getPos).min(Comparator.comparingDouble(pos -> pos.distSqr(pos)));
		
		oPos.ifPresent(pos -> this.blockPos = pos);
		return oPos.isPresent();
	}
	
	@Override
	protected boolean isValidTarget(LevelReader level, BlockPos pos)
	{
		return false;
	}
	
	/*private final PathfinderMob mob;
	private final DialogueEntity dialogueMob;
	private final ResourceKey<PoiType> poiKey;
	private final double acceptedDistance;
	private final boolean waitPermanently;
	private final int searchRange;
	
	private int duration;
	private BlockPos blockPos = BlockPos.ZERO;
	
	public GoToPoiGoal(PathfinderMob mob, ResourceKey<PoiType> poiKey, double speedModifier, int duration, int searchRange, double acceptedDistance, boolean waitPermanently)
	{
		this.mob = mob;
		if(mob instanceof DialogueEntity dialogueMob)
			this.dialogueMob = dialogueMob;
		else
			this.dialogueMob = null;
		this.poiKey = poiKey;
		this.duration = duration;
		this.searchRange = searchRange;
		this.acceptedDistance = acceptedDistance;
		this.waitPermanently = waitPermanently;
		this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
	}
	
	@Override
	public boolean canUse()
	{
		return dialogueMob != null && findPoi();
	}
	
	@Override
	public boolean canContinueToUse()
	{
		return super.canContinueToUse() && duration > 0 && !isReachedTarget();
	}
	
	@Override
	public void start()
	{
		//mob.remainingCooldownBeforeLocatingNewHive = 200;
		Optional<BlockPos> poi = findPoi();
		if(poi.isPresent())
		{
			for(BlockPos blockpos : list)
			{
				if(!mob.goToHiveGoal.isTargetBlacklisted(blockpos))
				{
					mob.hivePos = blockpos;
					return;
				}
			}
			
			mob.goToHiveGoal.clearBlacklist();
			mob.hivePos = list.get(0);
		}
	}
	
	@Override
	public void tick() {
		BlockPos blockpos = this.getMoveToTarget();
		if (!blockpos.closerToCenterThan(this.mob.position(), this.acceptedDistance())) {
			this.reachedTarget = false;
			this.tryTicks++;
			if (this.shouldRecalculatePath()) {
				this.mob.getNavigation().moveTo((double)blockpos.getX() + 0.5, (double)blockpos.getY(), (double)blockpos.getZ() + 0.5, this.speedModifier);
			}
		} else {
			this.reachedTarget = true;
			this.tryTicks--;
		}
	}
	
	protected boolean isReachedTarget() {
		return this.reachedTarget;
	}
	
	protected BlockPos getMoveToTarget() {
		return this.blockPos.above();
	}
	
	protected void moveMobToBlock()
	{
		this.mob.getNavigation().moveTo((double) this.blockPos.getX() + 0.5, (double) (this.blockPos.getY() + 1), (double) this.blockPos.getZ() + 0.5, this.speedModifier);
	}
	
	private boolean findPoi()
	{
		BlockPos mobPos = mob.blockPosition();
		PoiManager manager = ((ServerLevel) mob.level()).getPoiManager();
		Stream<PoiRecord> recordStream = manager.getInRange(holder -> holder.is(poiKey), mobPos, searchRange, PoiManager.Occupancy.ANY);
		Optional<BlockPos> oPos = recordStream.map(PoiRecord::getPos).min(Comparator.comparingDouble(pos -> pos.distSqr(pos)));
		
		oPos.ifPresent(pos -> this.blockPos = pos);
		return oPos.isPresent();
	}*/
}
