package com.mraof.minestuck.entity.ai;

import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.entity.consort.TutleEntity;
import com.mraof.minestuck.util.Debug;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class AnimatedMoveTowardsRestrictionGoal extends MoveTowardsRestrictionGoal
{
   protected final CreatureEntity creature;
   
   public AnimatedMoveTowardsRestrictionGoal(CreatureEntity creatureIn, double speedIn) {
      super(creatureIn, speedIn);
      this.creature = creatureIn;
   }
   
   @Override
   public void startExecuting()
   {
      super.startExecuting();
      if(this.creature instanceof ConsortEntity)
      {
         ((ConsortEntity) this.creature).updateAndSendAnimation(ConsortEntity.Animation.WALK_ARMS, true);
         Debug.debugf("startExecuting in AnimatedMoveTowardsRestrictionGoal");
      }
   }
   
   @Override
   public void tick()
   {
      super.tick();
      Debug.debugf("walk tick");
      if(!shouldContinueExecuting())
      {
         ((ConsortEntity) this.creature).updateAndSendAnimation(ConsortEntity.Animation.IDLE, true);
         Debug.debugf("idle");
      }
   }
}
