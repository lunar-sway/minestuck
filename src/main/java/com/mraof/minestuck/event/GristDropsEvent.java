package com.mraof.minestuck.event;

import com.google.common.collect.ImmutableMap;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.alchemy.MutableGristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;

import java.util.Map;

@Cancelable
@SuppressWarnings("unused")
public class GristDropsEvent extends LivingEvent
{
	private final UnderlingEntity underling;
	private final Map<PlayerIdentifier, Double> damageMap;
	private final GristSet originalDrops;
	private final GristType primaryType, bonusType;
	private final double originalMultiplier;
	private MutableGristSet newDrops;
	
	public GristDropsEvent(UnderlingEntity underling, Map<PlayerIdentifier, Double> damageMap, GristSet originalDrops, GristType primaryType, GristType bonusType, double multiplier)
	{
		super(underling);
		this.underling = underling;
		this.damageMap = ImmutableMap.copyOf(damageMap);
		this.originalDrops = originalDrops.asImmutable();
		this.primaryType = primaryType;
		this.bonusType = bonusType;
		originalMultiplier = multiplier;
		newDrops = new MutableGristSet(originalDrops);
	}
	
	public UnderlingEntity getUnderling()
	{
		return underling;
	}
	
	public EntityType<?> getUnderlingType()
	{
		return underling.getType();
	}
	
	public Map<PlayerIdentifier, Double> getDamageMap()
	{
		return damageMap;
	}
	
	public GristSet getOriginalDrops()
	{
		return originalDrops;
	}
	
	public GristType getPrimaryType()
	{
		return primaryType;
	}
	
	public GristType getBonusType()
	{
		return bonusType;
	}
	
	public double getOriginalMultiplier()
	{
		return originalMultiplier;
	}
	
	public MutableGristSet getNewDrops()
	{
		return newDrops;
	}
	
	public void setNewDrops(MutableGristSet newDrops)
	{
		this.newDrops = newDrops;
	}
}