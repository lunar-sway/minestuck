package com.mraof.minestuck.world.biome;

import com.mraof.minestuck.world.gen.LandGenSettings;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.gen.layer.LayerUtil;
import net.minecraft.world.gen.layer.VoroniZoomLayer;
import net.minecraft.world.gen.layer.ZoomLayer;

public class LandBiomeLayers //extends GenLayer
{
	public static Layer[] buildLandProcedure(long seed, LandGenSettings settings)
	{
		IAreaFactory<LazyArea> layer = new LandBaseLayer(settings.oceanChance).apply(new LazyAreaLayerContext(25, seed, 413L));
		layer = ZoomLayer.NORMAL.apply(new LazyAreaLayerContext(25, seed, 1000L), layer);
		layer = new LandRoughLayer(settings.roughChance).apply(new LazyAreaLayerContext(25, seed, 414L), layer);
		layer = LayerUtil.repeat(1000L, ZoomLayer.NORMAL, layer, 5, value -> new LazyAreaLayerContext(25, seed, value));
		
		IAreaFactory<LazyArea> blockLayer = VoroniZoomLayer.INSTANCE.apply(new LazyAreaLayerContext(25, seed, 10L), layer);
		
		return new Layer[]{new Layer(layer), new Layer(blockLayer)};
	}
}