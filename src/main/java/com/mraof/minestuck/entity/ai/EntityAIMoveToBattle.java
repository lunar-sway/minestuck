package com.mraof.minestuck.entity.ai;

import com.mraof.minestuck.entity.carapacian.CarapacianEntity;
import com.mraof.minestuck.entity.carapacian.EnumEntityKingdom;
import com.mraof.minestuck.world.MSDimensions;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.gen.Heightmap;

import java.util.EnumSet;

public class EntityAIMoveToBattle extends Goal
{
	
	private CarapacianEntity target;
	protected Vector3d destination;
	
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
		
		BlockPos pos = target.level.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, new BlockPos(type == EnumEntityKingdom.DERSITE ? 5 : -5, 0, target.getY()));
		destination = RandomPositionGenerator.getPosTowards(target, 10, 7, new Vector3d(pos.getX(), pos.getY(), pos.getZ()));
		
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