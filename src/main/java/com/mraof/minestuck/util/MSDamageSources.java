package com.mraof.minestuck.util;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;

public class MSDamageSources
{
	public static final ResourceKey<DamageType> SPIKE = key("spike");
	public static final ResourceKey<DamageType> DECAPITATION = key("decapitation");
	public static final ResourceKey<DamageType> ARMOR_PIERCE = key("armor_pierce");
	
	private static ResourceKey<DamageType> key(String name)
	{
		return ResourceKey.create(Registries.DAMAGE_TYPE, Minestuck.id(name));
	}
	
	public static DamageSource spike(RegistryAccess registryAccess)
	{
		return new DamageSource(getType(registryAccess, SPIKE));
	}
	
	public static DamageSource decapitation(RegistryAccess registryAccess)
	{
		return new DamageSource(getType(registryAccess, DECAPITATION));
	}
	
	public static DamageSource armorPierce(RegistryAccess registryAccess, Entity causingEntity)
	{
		return new DamageSource(getType(registryAccess, ARMOR_PIERCE), causingEntity);
	}
	
	private static Holder<DamageType> getType(RegistryAccess registryAccess, ResourceKey<DamageType> key)
	{
		return registryAccess.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key);
	}
}
