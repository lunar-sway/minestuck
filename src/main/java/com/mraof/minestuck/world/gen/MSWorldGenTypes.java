package com.mraof.minestuck.world.gen;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Minestuck.MOD_ID)
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class MSWorldGenTypes
{
	public static void register()
	{
		Registry.register(Registry.CHUNK_GENERATOR, new ResourceLocation(Minestuck.MOD_ID, "land"), LandChunkGenerator.CODEC);
	}
}