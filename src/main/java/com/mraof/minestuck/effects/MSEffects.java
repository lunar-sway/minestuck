package com.mraof.minestuck.effects;

import com.mraof.minestuck.Minestuck;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * This is an adapted version of Cibernet's code in Minestuck Universe, credit goes to him!
 */
public class MSEffects
{
	public static final DeferredRegister<MobEffect> REGISTER = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Minestuck.MOD_ID);
	
	public static final RegistryObject<CreativeShockEffect> CREATIVE_SHOCK = REGISTER.register("creative_shock", CreativeShockEffect::new);
	
	public static final RegistryObject<UnionBustEffect> UNION_BUST = REGISTER.register("union_bust", UnionBustEffect::new);
}