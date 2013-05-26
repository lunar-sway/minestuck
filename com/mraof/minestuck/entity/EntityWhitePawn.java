package com.mraof.minestuck.entity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.world.World;

public class EntityWhitePawn extends EntityPawn {
	public EntityWhitePawn(World world) {
		super(world);
		texture = "/com/mraof/minestuck/textures/mobs/Prospitian.png";
		
	}
	
	@Override
	public void initCreature() {
		enemyClasses.add(EntityBlackPawn.class);
		super.initCreature();
	}
	
	

}
