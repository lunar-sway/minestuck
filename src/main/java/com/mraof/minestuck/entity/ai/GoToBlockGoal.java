package com.mraof.minestuck.entity.ai;

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
	private int duration;
	
	public GoToBlockGoal(PathfinderMob mob, BlockPredicate blockPredicate, double speedModifier, int duration, int searchRange)
	{
		super(mob, speedModifier, searchRange, searchRange);
		
		if(mob instanceof DialogueEntity dialogueMob)
			this.dialogueMob = dialogueMob;
		else
			this.dialogueMob = null;
		this.blockPredicate = blockPredicate;
		this.duration = duration;
		this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
	}
	
	@Override
	public boolean canUse()
	{
		return dialogueMob != null && super.canUse();
	}
	
	@Override
	public void start()
	{
		super.start();
		dialogueMob.getDialogueComponent().setHasReachedTarget(false);
	}
	
	@Override
	public void stop()
	{
		super.stop();
		mob.goalSelector.removeGoal(this);
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
	public boolean canContinueToUse()
	{
		return super.canContinueToUse() && duration > 0;
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
