package com.mraof.minestuck.entity.ai;

import com.mraof.minestuck.entity.carapacian.CarapacianEntity;
import com.mraof.minestuck.entity.carapacian.EnumEntityKingdom;
import com.mraof.minestuck.world.MSDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class EntityAIMoveToBattle extends Goal
{
	
	private CarapacianEntity target;
	protected Vec3 destination;
	
	public EntityAIMoveToBattle(CarapacianEntity entity)
	{
		this.target = entity;
		setFlags(EnumSet.of(Flag.MOVE));
	}
	
	@Override
	public boolean canUse()
	{	//TODO When the castles are fixed, make the entity possibly head for one if it's closer
		
		if(!MSDimensions.isSkaia(target.level.dimension()) || this.target.getTarget() != null && !this.target.getNavigation().isDone())
			return false;
		
		EnumEntityKingdom type = target.getKingdom();
		
		if(type == EnumEntityKingdom.DERSITE && target.getX() >= 0 || type == EnumEntityKingdom.PROSPITIAN && target.getX() <= 0)
			return false;
		
		BlockPos pos = target.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new BlockPos(type == EnumEntityKingdom.DERSITE ? 5 : -5, 0, target.getY()));
		destination = DefaultRandomPos.getPosTowards(target, 10, 7, new Vec3(pos.getX(), pos.getY(), pos.getZ()), Math.PI / 2);
		
		return destination != null;
	}
	
	@Override
	public void start()
	{
		
		this.target.getNavigation().moveTo(destination.x, destination.y, destination.z, 1.0F);
	}
	
	@Override
	public boolean canContinueToUse()
	{
		return !this.target.getNavigation().isDone();
	}
	
}