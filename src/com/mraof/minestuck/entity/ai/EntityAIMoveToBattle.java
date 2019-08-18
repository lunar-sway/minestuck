package com.mraof.minestuck.entity.ai;

import com.mraof.minestuck.entity.carapacian.CarapacianEntity;
import com.mraof.minestuck.entity.carapacian.EnumEntityKingdom;
import com.mraof.minestuck.world.MinestuckDimensions;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.Heightmap;

import java.util.EnumSet;

public class EntityAIMoveToBattle extends Goal
{
	
	private CarapacianEntity target;
	protected Vec3d destination;
	
	public EntityAIMoveToBattle(CarapacianEntity entity)
	{
		this.target = entity;
		setMutexFlags(EnumSet.of(Flag.MOVE));
	}
	
	@Override
	public boolean shouldExecute()
	{	//TODO When the castles are fixed, make the entity possibly head for one if it's closer
		
		if(target.dimension != MinestuckDimensions.skaiaDimension || this.target.getAttackTarget() != null && !this.target.getNavigator().noPath())
			return false;
		
		EnumEntityKingdom type = target.getKingdom();
		
		if(type == EnumEntityKingdom.DERSITE && target.posX >= 0 || type == EnumEntityKingdom.PROSPITIAN && target.posX <= 0)
			return false;
		
		BlockPos pos = target.world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, new BlockPos(type == EnumEntityKingdom.DERSITE ? 5 : -5, 0, target.posY));
		destination = RandomPositionGenerator.findRandomTargetBlockTowards(target, 10, 7, new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
		
		return destination != null;
	}
	
	@Override
	public void startExecuting()
	{
		
		this.target.getNavigator().tryMoveToXYZ(destination.x, destination.y, destination.z, target.getWanderSpeed());
	}
	
	@Override
	public boolean shouldContinueExecuting()
	{
		return !this.target.getNavigator().noPath();
	}
	
}