package com.mraof.minestuck.entity.consort;

import com.mraof.minestuck.util.MinestuckSoundHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class IguanaEntity extends ConsortEntity
{
	
	public IguanaEntity(EntityType<? extends IguanaEntity> type, World world)
	{
		super(type, world);
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
		return "textures/entity/iguana.png";
	}
	
	@Override
	public EnumConsort getConsortType()
	{
		return EnumConsort.IGUANA;
	}
}