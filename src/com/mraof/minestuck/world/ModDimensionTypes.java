package com.mraof.minestuck.world;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.lands.LandDimension;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;

@ObjectHolder(Minestuck.MOD_ID)
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModDimensionTypes
{
	
	public static final ModDimension LANDS = getNull();
	public static final ModDimension SKAIA = getNull();
	
	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T getNull()
	{
		return null;
	}
	
	@SubscribeEvent
	public static void registerModDimensions(RegistryEvent.Register<ModDimension> event)
	{
		event.getRegistry().register(new LandDimension.Type().setRegistryName("lands"));
		event.getRegistry().register(new SkaiaDimension.Type().setRegistryName("skaia"));
	}
}
