package com.mraof.minestuck.entity.ai;

import com.mraof.minestuck.entity.consort.ConsortEntity;
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
	public boolean canUse()
	{
		eventTimer = 0;
		return super.canUse();
	}
	
	@Override
	public void tick()
	{
		super.tick();
		
		if(eventTimer == 0)
			((ConsortEntity) this.mob).updateAndSendAnimation(ConsortEntity.Animation.PANIC, true, true);
		
		if(eventTimer == 10)
		{
			((ConsortEntity) this.mob).updateAndSendAnimation(ConsortEntity.Animation.PANIC_RUN, true, true);
			Debug.debugf("panic run");
			if((eventTimer - 10) % 6 == 0) //.32 sec panic run animation * 20 ticks/sec = 6.4(cuts slightly into itself?)
			{
			
			}
		}
		
		if(canContinueToUse())
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
	public boolean canContinueToUse()
	{
		if(!super.canContinueToUse() && eventTimer >= 10)
		{
			if(this.mob instanceof ConsortEntity)
			{
				Debug.debugf("startExecuting in AnimatedPanicGoal");
				//((ConsortEntity) this.creature).updateAndSendAnimation(ConsortEntity.Animation.IDLE, true);
			}
		}
		
		if(eventTimer < 100)
			return true;
		return super.canContinueToUse();
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
