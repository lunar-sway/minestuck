package com.mraof.minestuck.entity.ai;

import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.entity.consort.TutleEntity;
import com.mraof.minestuck.util.Debug;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.PanicGoal;

public class AnimatedPanicGoal extends PanicGoal
{
	private int eventTimer;
	
	public AnimatedPanicGoal(CreatureEntity creature, double speedIn)
	{
		super(creature, speedIn);
	}
	
	@Override
	public void startExecuting()
	{
		super.startExecuting();
		if(this.creature instanceof TutleEntity)
		{
			Debug.debugf("startExecuting in AnimatedPanicGoal");
		}
		eventTimer = 0;
	}
	
	@Override
	public void tick()
	{
		super.tick();
		
		if(eventTimer == 0)
			((ConsortEntity) this.creature).updateAndSendAnimation(ConsortEntity.Animation.PANIC, false);
		
		if(eventTimer == 10)
		{
			((ConsortEntity) this.creature).updateAndSendAnimation(ConsortEntity.Animation.PANIC_RUN, true);
			Debug.debugf("panic run");
			if((eventTimer - 10) % 6 == 0) //.32 sec panic run animation * 20 ticks/sec = 6.4(cuts slightly into itself?)
			{
			
			}
		}
		
		if(shouldContinueExecuting())
		{
		
		}
		
		if(eventTimer >= 10) //.52 sec panic animation * 20 ticks/sec = 10.4(cuts slightly into panic)
		{
			
			/*else
			{
				((TutleEntity) this.creature).updateAndSendAnimation(TutleEntity.Animation.IDLE, true);
				Debug.debugf("idle");
			}*/
		}
		
		eventTimer++;
	}
	
	@Override
	public boolean shouldContinueExecuting()
	{
		if(!super.shouldContinueExecuting() && eventTimer >= 10)
		{
			if(this.creature instanceof ConsortEntity)
			{
				Debug.debugf("startExecuting in AnimatedPanicGoal");
				((ConsortEntity) this.creature).updateAndSendAnimation(ConsortEntity.Animation.IDLE, true);
			}
		}
		
		if(eventTimer < 100)
			return true;
		return super.shouldContinueExecuting();
	}
	
	@Override
	protected boolean findRandomPosition()
	{
		return super.findRandomPosition();
	}
	
	/*@Override
	public void resetTask()
	{
		super.resetTask();
		if(this.creature instanceof TutleEntity)
		{
			Debug.debugf("startExecuting in AnimatedPanicGoal");
			((TutleEntity) this.creature).updateAndSendAnimation(TutleEntity.Animation.IDLE, true);
		}
	}*/
}
