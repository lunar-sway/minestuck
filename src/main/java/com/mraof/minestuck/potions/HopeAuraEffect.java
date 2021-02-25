package com.mraof.minestuck.potions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.math.Vec3d;
import org.apache.logging.log4j.LogManager;

import javax.annotation.ParametersAreNullableByDefault;
import java.util.List;

public class HopeAuraEffect extends Effect
{
	protected HopeAuraEffect(EffectType effectType, int liquidColorIn)
	{
		super(effectType, liquidColorIn);
	}
	
	@Override
	@ParametersAreNullableByDefault
	public void performEffect(LivingEntity livingEntityIn, int amplifier)
	{
		super.performEffect(livingEntityIn, amplifier);
		
		List<Entity> entityList = livingEntityIn.world.getEntitiesWithinAABBExcludingEntity(livingEntityIn, livingEntityIn.getBoundingBox().grow(10.0D));
		
		if(!entityList.isEmpty())
		{
			for(Entity eventEntity : entityList)
			{
				double distanceSq = eventEntity.getDistanceSq(livingEntityIn);
				//Vec3d distanceVec = eventEntity.getPositionVec();
				//Float distanceFloat = eventEntity.getDistance(livingEntityIn);
				double motionX = (eventEntity.getPosX() - livingEntityIn.getPosX());
				double motionY = (eventEntity.getPosY() - livingEntityIn.getPosY());
				double motionZ = (eventEntity.getPosZ() - livingEntityIn.getPosZ());
				if(motionX != 0)
					motionX = (.05+amplifier*.01) / motionX;
				if(motionY != 0)
					motionY = (.05+amplifier*.01) / motionY;
				if(motionZ != 0)
					motionZ = (.05+amplifier*.01) / motionZ;
				Vec3d motionVec = new Vec3d(motionX, motionY, motionZ);
				double horizontalMath = Math.max(motionX * motionX + motionZ * motionZ, 0.001D);
				//eventEntity.moveRelative(1, livingEntityIn.getPositionVec());
				LogManager.getLogger().debug("motionVec is {}", motionVec);
				
				eventEntity.addVelocity(motionX*.5, motionY*.4, motionZ*.5);
				
				//eventEntity.setMotion(eventEntity.getMotion().x + motionX, eventEntity.getMotion().y + motionY, eventEntity.getMotion().z + motionZ);
				LogManager.getLogger().debug("{}'s velocity is {}", eventEntity.getName().getFormattedText(), eventEntity.getMotion());
				//eventEntity.setMotion(eventEntity.getMotion().x + 1 / distanceVec., eventEntity.getMotion().y + 1 / distanceVec.y, eventEntity.getMotion().z + 1 / distanceVec.z);
			}
		}
		//entityLivingBaseIn.setMotion(0,0,0);
	}
	
	@Override
	public boolean isReady(int duration, int amplifier)
	{
		return true;
	}
}
