package com.mraof.minestuck.item.crafting.alchemy.generator;

import com.google.gson.JsonElement;
import com.mraof.minestuck.Minestuck;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;

@ObjectHolder(Minestuck.MOD_ID)
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public abstract class InterpreterSerializer<T extends RecipeInterpreter> extends ForgeRegistryEntry<InterpreterSerializer<?>>
{
	public static IForgeRegistry<InterpreterSerializer<?>> REGISTRY;
	
	@SubscribeEvent
	@SuppressWarnings("unchecked")
	public static void onRegistryNewRegistry(final NewRegistryEvent event)
	{
		event.create(new RegistryBuilder<InterpreterSerializer<?>>()
						.setName(new ResourceLocation(Minestuck.MOD_ID, "recipe_interpreter"))
						.setType((Class<InterpreterSerializer<?>>) (Class<?>) InterpreterSerializer.class)
						.disableSaving()
						.disableSync(),
				registry -> REGISTRY = registry);
	}
	
	@SubscribeEvent
	public static void registerSerializers(final RegistryEvent.Register<InterpreterSerializer<?>> event)
	{
		event.getRegistry().register(new DefaultInterpreter.Serializer().setRegistryName("default"));
		event.getRegistry().register(new CookingCostInterpreter.Serializer().setRegistryName("cooking"));
		event.getRegistry().register(new SmithingInterpreter.Serializer().setRegistryName("smithing"));
	}
	
	public abstract T read(JsonElement json);
	
	public abstract JsonElement write(T interpreter);
}