package com.mraof.minestuck.util;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;

public class MSDamageSources
{
	public static final ResourceKey<DamageType> SPIKE = key("spike");
	public static final ResourceKey<DamageType> DECAPITATION = key("decapitation");
	public static final ResourceKey<DamageType> ARMOR_PIERCE = key("armor_pierce");
	
	private static ResourceKey<DamageType> key(String name)
	{
		return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Minestuck.MOD_ID, name));
	}
	
	public static DamageSource spike(RegistryAccess registryAccess)
	{
		return new DamageSource(getType(registryAccess, SPIKE));
	}
	
	public static DamageSource decapitation(RegistryAccess registryAccess)
	{
		return new DamageSource(getType(registryAccess, DECAPITATION));
	}
	
	public static DamageSource armorPierce(RegistryAccess registryAccess)
	{
		return new DamageSource(getType(registryAccess, ARMOR_PIERCE));
	}
	
	private static Holder<DamageType> getType(RegistryAccess registryAccess, ResourceKey<DamageType> key)
	{
		return registryAccess.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key);
	}
}
