package com.mraof.minestuck.event;

import com.google.common.collect.ImmutableMap;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.MutableGristSet;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

import java.util.Map;

@SuppressWarnings("unused")
public class GristDropsEvent extends LivingEvent implements ICancellableEvent
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
		newDrops = originalDrops.mutableCopy();
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
	
	public void setNewDrops(GristSet newDrops)
	{
		this.newDrops = newDrops.mutableCopy();
	}
}