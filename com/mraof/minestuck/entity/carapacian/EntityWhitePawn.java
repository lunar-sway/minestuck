package com.mraof.minestuck.entity.carapacian;

import com.mraof.minestuck.entity.underling.EntityUnderlingPart;

import net.minecraft.world.World;

public class EntityWhitePawn extends EntityPawn
{
	public EntityWhitePawn(World world)
	{
		super(world);
	}
	public EntityWhitePawn(World world, int type)
	{
		super(world, type);
	}
	@Override
	public void setEnemies()
	{
		setEnemies(EnumEntityKingdom.PROSPITIAN);
		this.enemyClasses.add(EntityUnderlingPart.class);
		super.setEnemies();
	}
	@Override
	public void setAllies()
	{
		setAllies(EnumEntityKingdom.PROSPITIAN);
	}
	@Override
	public String getTexture() 
	{
		return "textures/mobs/ProspitianPawn.png";
	}
}
