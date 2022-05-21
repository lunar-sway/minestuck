package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.manager.AnimationData;

public class WyrmEntity extends UnderlingEntity
{
	public WyrmEntity(EntityType<? extends UnderlingEntity> type, World world)
	{
		super(type, world, 0); //TODO everything
	}
	
	public static AttributeModifierMap.MutableAttribute wyrmAttributes()
	{
		return UnderlingEntity.underlingAttributes().add(Attributes.MAX_HEALTH, 85)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.6).add(Attributes.MOVEMENT_SPEED, 0.25)
				.add(Attributes.ATTACK_DAMAGE, 6);
	}
	
	@Override
	public GristSet getGristSpoils()
	{
		return null;
	}
	
	@Override
	protected int getVitalityGel()
	{
		return 0;
	}
	
	@Override
	public void registerControllers(AnimationData data)
	{
	
	}
}
