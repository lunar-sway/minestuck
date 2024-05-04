package com.mraof.minestuck.effects;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * This is an adapted version of Cibernet's code in Minestuck Universe, credit goes to him!
 */
public class MSEffects
{
	public static final DeferredRegister<MobEffect> REGISTER = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, Minestuck.MOD_ID);
	
	public static final Supplier<CreativeShockEffect> CREATIVE_SHOCK = REGISTER.register("creative_shock", CreativeShockEffect::new);
	
	public static final Supplier<SuspicionEffect> SUSPICION = REGISTER.register("suspicion", SuspicionEffect::new);
	
	public static final Supplier<SoporSicknessEffect> SOPOR_SICKNESS = REGISTER.register("sopor_sickness", SoporSicknessEffect::new);
}