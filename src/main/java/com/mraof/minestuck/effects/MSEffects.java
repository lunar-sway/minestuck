package com.mraof.minestuck.effects;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * This is an adapted version of Cibernet's code in Minestuck Universe, credit goes to him!
 */
public class MSEffects extends Effects
{
	public static final Effect CREATIVE_SHOCK = new EffectBuildInhibit(EffectType.NEUTRAL,0x993030 /*"disableBuilding"*/);
	//public static final Potion EARTHBOUND = new PotionFlight(true, 0xFFCD70, "disableFlight");
	//public static final Potion SKYHBOUND = new PotionFlight(false, 0x70FFFF, "flight").setBeneficial();
	
	@SubscribeEvent
	public static void registerEffects(RegistryEvent.Register<Effect> event)
	{
		IForgeRegistry<Effect> registry = event.getRegistry();
		
		registry.register(CREATIVE_SHOCK.setRegistryName("creative_shock"));
		//registry.register(EARTHBOUND.setRegistryName("earthbound"));
		//registry.register(SKYHBOUND.setRegistryName("skybound"));
	}
	
	private static Effect register(int id, String key, Effect effectIn) {
		return Registry.register(Registry.EFFECTS, id, key, effectIn);
	}
}
