package com.mraof.minestuck.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityBlackPawn extends EntityPawn
{
	public EntityBlackPawn(World world) {
		super(world);
		texture = "/com/mraof/minestuck/textures/mobs/Dersite.png";
	}
	
	@Override
	public void initCreature() {
		enemyClasses.add(EntityWhitePawn.class);
		enemyClasses.add(EntitySlime.class);
		super.initCreature();
	}

}
