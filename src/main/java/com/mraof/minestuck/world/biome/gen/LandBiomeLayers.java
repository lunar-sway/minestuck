package com.mraof.minestuck.world.biome.gen;

import com.mraof.minestuck.world.biome.ILandBiomeSet;
import com.mraof.minestuck.world.biome.LandBiomeType;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.gen.layer.LayerUtil;
import net.minecraft.world.gen.layer.ZoomLayer;

import java.util.Objects;
import java.util.function.LongFunction;

public class LandBiomeLayers
{
	static Layer buildLandProcedure(long seed, ILandBiomeSet biomes, float oceanChance, float roughChance)
	{
		IAreaFactory<LazyArea> layer = makeAreaFactory(oceanChance, roughChance, value ->
				new LazyAreaLayerContext(25, seed, value));
		
		return new LandLayer(layer, biomes);
	}
	
	private static <T extends IArea, C extends IExtendedNoiseRandom<T>>IAreaFactory<T> makeAreaFactory(float oceanChance, float roughChance, LongFunction<C> randomProvider)
	{
		IAreaFactory<T> layer = new LandBaseLayer(oceanChance).run(randomProvider.apply(413L));
		layer = ZoomLayer.NORMAL.run(randomProvider.apply(1000L), layer);
		layer = new LandRoughLayer(roughChance).run(randomProvider.apply(414L), layer);
		layer = LayerUtil.zoom(1000L, ZoomLayer.NORMAL, layer, 5, randomProvider);
		
		return layer;
	}
	
	private static class LandLayer extends Layer
	{
		private final LazyArea area;
		private final ILandBiomeSet biomes;
		
		public LandLayer(IAreaFactory<LazyArea> area, ILandBiomeSet biomes)
		{
			super(area);
			this.area = area.make();
			this.biomes = biomes;
		}
		
		@Override
		public Biome get(Registry<Biome> registry, int x, int z)
		{
			int id = area.get(x, z);
			LandBiomeType[] types = LandBiomeType.values();
			if(0 <= id && id < types.length)
			{
				return Objects.requireNonNull(biomes.fromType(types[id]));
			} else
			{
				throw new IllegalStateException("Unknown land biome id: " + id);
			}
		}
	}
}