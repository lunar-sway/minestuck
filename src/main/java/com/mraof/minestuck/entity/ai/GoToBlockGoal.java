package com.mraof.minestuck.entity.ai;

import com.mraof.minestuck.entity.KernelspriteEntity;
import com.mraof.minestuck.entity.dialogue.DialogueEntity;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

import java.util.EnumSet;

public class GoToBlockGoal extends MoveToBlockGoal
{
	private final BlockPredicate blockPredicate;
	private final DialogueEntity dialogueMob;
	private final double acceptedDistance;
	private final boolean waitPermanently;
	private int duration;
	
	public GoToBlockGoal(PathfinderMob mob, BlockPredicate blockPredicate, double speedModifier, int duration, int searchRange, double acceptedDistance, boolean waitPermanently)
	{
		super(mob, speedModifier, searchRange, (int) (searchRange * 0.5F));
		
		if(mob instanceof DialogueEntity dialogueMob)
			this.dialogueMob = dialogueMob;
		else
			this.dialogueMob = null;
		this.blockPredicate = blockPredicate;
		this.duration = duration;
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
	protected boolean isValidTarget(LevelReader level, BlockPos pos)
	{
		return blockPredicate.matches(new BlockInWorld(level, pos, false));
	}
}
