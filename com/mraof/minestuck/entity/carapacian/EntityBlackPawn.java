package com.mraof.minestuck.entity.carapacian;

import net.minecraft.world.World;

public class EntityBlackPawn extends EntityPawn
{
	public EntityBlackPawn(World world) 
	{
		super(world);
		texture = "/mods/Minestuck/textures/mobs/DersitePawn.png";
	}
	public EntityBlackPawn(World world, int type) 
	{
		super(world, type);
		texture = "/mods/Minestuck/textures/mobs/DersitePawn.png";
	}
	@Override
	public void setEnemies() 
	{
		setEnemies(EnumEntityKingdom.DERSITE);
		super.setEnemies();
	}
	@Override
	public void setAllies() 
	{
		setAllies(EnumEntityKingdom.DERSITE);
	}

}
