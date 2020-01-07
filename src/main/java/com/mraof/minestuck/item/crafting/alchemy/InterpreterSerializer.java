package com.mraof.minestuck.item.crafting.alchemy;

import com.google.gson.JsonElement;
import com.mraof.minestuck.Minestuck;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegistryBuilder;

@ObjectHolder(Minestuck.MOD_ID)
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public abstract class InterpreterSerializer<T extends GristCostGenerator.RecipeInterpreter> extends ForgeRegistryEntry<InterpreterSerializer<?>>
{
	public static IForgeRegistry<InterpreterSerializer<?>> REGISTRY;
	
	@SubscribeEvent
	@SuppressWarnings("unchecked")
	public static void onRegistryNewRegistry(final RegistryEvent.NewRegistry event)
	{
		REGISTRY = new RegistryBuilder<InterpreterSerializer<?>>()
				.setName(new ResourceLocation(Minestuck.MOD_ID, "recipe_interpreter"))
				.setType((Class<InterpreterSerializer<?>>) (Class<?>) InterpreterSerializer.class)
				.disableSaving()
				.disableSync()
				.create();
	}
	
	@SubscribeEvent
	public static void registerSerializers(final RegistryEvent.Register<InterpreterSerializer<?>> event)
	{
		event.getRegistry().register(new DefaultInterpreter.Serializer().setRegistryName("default"));
	}
	
	protected abstract GristCostGenerator.RecipeInterpreter read(JsonElement json);
	
	protected abstract JsonElement write(T interpreter);
}