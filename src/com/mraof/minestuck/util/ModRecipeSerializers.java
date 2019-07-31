package com.mraof.minestuck.util;

import com.mraof.minestuck.Minestuck;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;

@ObjectHolder(Minestuck.MOD_ID)
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public final class ModRecipeSerializers {
    public static final IRecipeSerializer<NonMirroredRecipe> NON_MIRRORED = getNull();

    @Nonnull
    @SuppressWarnings("ConstantConditions")
    private static <T> T getNull() {
        return null;
    }

    @SubscribeEvent
    public static void registerSerializers(final RegistryEvent.Register<IRecipeSerializer<?>> event){
        IForgeRegistry<IRecipeSerializer<?>> registry = event.getRegistry();
        registry.register(new NonMirroredRecipe.Serializer().setRegistryName("non_mirrored"));
    }
}



