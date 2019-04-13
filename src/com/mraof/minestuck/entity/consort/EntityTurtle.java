package com.mraof.minestuck.entity.consort;

import com.mraof.minestuck.util.MinestuckSoundHandler;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityTurtle extends EntityConsort
{
	public EntityTurtle(World world)
	{
		super(world);
	}
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return MinestuckSoundHandler.soundTurtleHurt;
	}
	protected SoundEvent getDeathSound()
	{
		return MinestuckSoundHandler.soundTurtleDeath;
	}
	
	@Override
	public String getTexture()
	{
		return "textures/mobs/turtle.png";
	}
	
	@Override
	public EnumConsort getConsortType()
	{
		return EnumConsort.TURTLE;
	}
}