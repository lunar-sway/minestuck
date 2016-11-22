package com.mraof.minestuck.entity.ai;

import com.mraof.minestuck.entity.carapacian.EntityCarapacian;
import com.mraof.minestuck.entity.carapacian.EnumEntityKingdom;
import com.mraof.minestuck.world.MinestuckDimensionHandler;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAIMoveToBattle extends EntityAIBase
{
	
	private EntityCarapacian target;
	protected Vec3d destination;
	
	public EntityAIMoveToBattle(EntityCarapacian entity)
	{
		this.target = entity;
		setMutexBits(1);
	}
	
	@Override
	public boolean shouldExecute()
	{	//TODO When the castles are fixed, make the entity possibly head for one if it's closer
		
		if(target.dimension != MinestuckDimensionHandler.skaiaDimensionId || this.target.getAttackTarget() != null && !this.target.getNavigator().noPath())
			return false;
		
		EnumEntityKingdom type = target.getKingdom();
		
		if(type == EnumEntityKingdom.DERSITE && target.posX >= 0 || type == EnumEntityKingdom.PROSPITIAN && target.posX <= 0)
			return false;
		
		BlockPos pos = target.world.getHeight(new BlockPos(type == EnumEntityKingdom.DERSITE ? 5 : -5, 0, target.posY));
		destination = RandomPositionGenerator.findRandomTargetBlockTowards(target, 10, 7, new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
		
		return destination != null;
	}
	
	@Override
	public void startExecuting()
	{
		
		this.target.getNavigator().tryMoveToXYZ(destination.xCoord, destination.yCoord, destination.zCoord, target.getWanderSpeed());
	}
	
	@Override
	public boolean continueExecuting()
	{
		return !this.target.getNavigator().noPath();
	}
	
}