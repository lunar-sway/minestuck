package com.mraof.minestuck.effects;

import com.mraof.minestuck.Minestuck;
import net.minecraft.potion.*;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * This is an adapted version of Cibernet's code in Minestuck Universe, credit goes to him!
 */
public class MSEffects
{
	public static final DeferredRegister<Effect> REGISTER = DeferredRegister.create(ForgeRegistries.POTIONS, Minestuck.MOD_ID);
	
	public static final RegistryObject<CreativeShockEffect> CREATIVE_SHOCK = REGISTER.register("creative_shock", CreativeShockEffect::new);
}