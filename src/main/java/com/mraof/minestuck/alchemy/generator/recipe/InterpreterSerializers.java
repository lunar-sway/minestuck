package com.mraof.minestuck.alchemy.generator.recipe;

import com.mraof.minestuck.Minestuck;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public final class InterpreterSerializers
{
	public static final DeferredRegister<InterpreterSerializer<?>> REGISTER = DeferredRegister.create(new ResourceLocation(Minestuck.MOD_ID, "recipe_interpreter"), Minestuck.MOD_ID);
	
	@SuppressWarnings("unchecked")
	public static final Supplier<IForgeRegistry<InterpreterSerializer<?>>> REGISTRY = REGISTER.makeRegistry((Class<InterpreterSerializer<?>>) (Object) InterpreterSerializer.class,
			() -> new RegistryBuilder<InterpreterSerializer<?>>().disableSaving().disableSync());
	
	public static final RegistryObject<InterpreterSerializer<DefaultInterpreter>> DEFAULT = REGISTER.register("default", DefaultInterpreter.Serializer::new);
	public static final RegistryObject<InterpreterSerializer<CookingCostInterpreter>> COOKING = REGISTER.register("cooking", CookingCostInterpreter.Serializer::new);
	public static final RegistryObject<InterpreterSerializer<SmithingInterpreter>> SMITHING = REGISTER.register("smithing", SmithingInterpreter.Serializer::new);
}
