package com.mraof.minestuck.entity.ai;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.carapacian.EntityCarapacian;
import com.mraof.minestuck.entity.carapacian.EnumEntityKingdom;
import com.mraof.minestuck.util.Debug;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.BlockPos;

public class EntityAIMoveToBattle extends EntityAIBase
{
	
	private EntityCarapacian target;
	protected int x, y, z;
	
	public EntityAIMoveToBattle(EntityCarapacian entity)
	{
		this.target = entity;
		setMutexBits(1);
	}
	
	@Override
	public boolean shouldExecute()
	{	//TODO When the castles are fixed, make the entity possibly head for one if it's closer
		
		if(target.dimension != Minestuck.skaiaDimensionId || this.target.getAttackTarget() != null && !this.target.getNavigator().noPath())
			return false;
		
		EnumEntityKingdom type = target.getKingdom();
		
		if(type == EnumEntityKingdom.DERSITE && target.posX >= 0 || type == EnumEntityKingdom.PROSPITIAN && target.posX <= 0)
			return false;
		
		x = target.getRNG().nextInt(10) + 5;
		x = (int) (target.posX + (type == EnumEntityKingdom.DERSITE ? x : -x));
		z = (int) (target.posZ + target.getRNG().nextInt(8) - 4);
		y = target.worldObj.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).up().getY();	//Wont work around castles
		
		return true;
	}
	
	@Override
	public void startExecuting()
	{
		
		this.target.getNavigator().tryMoveToXYZ(this.x, this.y, this.z, target.getWanderSpeed());
	}
	
	@Override
	public boolean continueExecuting()
	{
		return !this.target.getNavigator().noPath();
	}
	
}