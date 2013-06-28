package com.mraof.minestuck.entity.carapacian;

import net.minecraft.world.World;

public class EntityWhitePawn extends EntityPawn
{
	public EntityWhitePawn(World world)
	{
		super(world);
		texture = "/mods/Minestuck/textures/mobs/ProspitianPawn.png";
	}
	public EntityWhitePawn(World world, int type)
	{
		super(world, type);
		texture = "/mods/Minestuck/textures/mobs/ProspitianPawn.png";
	}
	@Override
	public void setEnemies()
	{
		setEnemies(EnumEntityKingdom.PROSPITIAN);
		super.setEnemies();
	}
	@Override
	public void setAllies()
	{
		setAllies(EnumEntityKingdom.PROSPITIAN);
	}
}
