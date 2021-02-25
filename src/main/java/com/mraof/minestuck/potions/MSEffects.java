package com.mraof.minestuck.potions;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@net.minecraftforge.registries.ObjectHolder("minecraft")
public class MSEffects extends Effects
{
	public static final Effect HOPE_AURA = new HopeAuraEffect(EffectType.BENEFICIAL, 8954814);
	public static final Effect TIME_STOP = new TimeStopEffect(EffectType.HARMFUL, 0xFF2106);
	
	@SubscribeEvent
	public static void registerEffects(RegistryEvent.Register<Effect> event)
	{
		IForgeRegistry<Effect> registry = event.getRegistry();
		
		registry.register(HOPE_AURA.setRegistryName("hope_aura"));
		registry.register(TIME_STOP.setRegistryName("time_stop"));
	}
	
	private static Effect register(int id, String key, Effect effectIn) {
		return Registry.register(Registry.EFFECTS, id, key, effectIn);
	}
}
