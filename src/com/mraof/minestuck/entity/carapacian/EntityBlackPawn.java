package com.mraof.minestuck.entity.carapacian;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityBlackPawn extends EntityPawn
{
	public EntityBlackPawn(World world) 
	{
		super(world);
	}
	public EntityBlackPawn(World world, int type) 
	{
		super(world, type);
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
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase entitylivingbase, float f) 
	{
		
	}
	@Override
	public String getTexture() 
	{
		return "textures/mobs/DersitePawn.png";
	}

}
