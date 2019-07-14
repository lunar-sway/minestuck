package com.mraof.minestuck.entity.consort;

import com.mraof.minestuck.util.MinestuckSoundHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class TurtleEntity extends ConsortEntity
{
	public TurtleEntity(EntityType<? extends TurtleEntity> type, World world)
	{
		super(type, world);
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
		return "textures/entity/turtle.png";
	}
	
	@Override
	public EnumConsort getConsortType()
	{
		return EnumConsort.TURTLE;
	}
}