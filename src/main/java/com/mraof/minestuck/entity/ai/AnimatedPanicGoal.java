package com.mraof.minestuck.entity.ai;

import com.mraof.minestuck.entity.AnimatedCreatureEntity;
import net.minecraft.entity.ai.goal.PanicGoal;

public class AnimatedPanicGoal extends PanicGoal {
    private final AnimatedCreatureEntity entity;

    public AnimatedPanicGoal(AnimatedCreatureEntity entity, double speedModifier) {
        super(entity, speedModifier);
        this.entity = entity;
    }

    @Override
    public void start() {
        this.entity.setCurrentAction(AnimatedCreatureEntity.Actions.PANIC);
        super.start();
    }

    @Override
    public void stop() {
        this.entity.setCurrentAction(AnimatedCreatureEntity.Actions.NONE);
        super.stop();
    }
}
