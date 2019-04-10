package com.mraof.minestuck.entity.consort;

import com.mraof.minestuck.util.MinestuckSoundHandler;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityIguana extends EntityConsort
{
	
	public EntityIguana(World world)
	{
		super(world);
	}
	
	protected SoundEvent getAmbientSound()
	{
		return MinestuckSoundHandler.soundIguanaAmbient;
	}
	
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return MinestuckSoundHandler.soundIguanaHurt;
	}
	
	protected SoundEvent getDeathSound()
	{
		return MinestuckSoundHandler.soundIguanaDeath;
	}
	
	@Override
	public String getTexture()
	{
		return "textures/mobs/iguana.png";
	}
	
	@Override
	public EnumConsort getConsortType()
	{
		return EnumConsort.IGUANA;
	}
}