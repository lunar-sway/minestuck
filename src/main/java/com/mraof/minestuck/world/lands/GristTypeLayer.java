package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.item.crafting.alchemy.GristTypes;

import javax.annotation.Nullable;

public class GristTypeLayer
{/*
	private final LazyArea area;
	
	private GristTypeLayer(LazyArea area)
	{
		this.area = area;
	}
	*/
	public static GristTypeLayer createLayer(GristType.SpawnCategory category, int index, long seed, int zoomLevel, @Nullable GristType baseType)
	{/*
		LongFunction<LazyAreaLayerContext> layerContextCreator = modifier -> new LazyAreaLayerContext(25, seed, modifier + index);
		
		IAreaFactory<LazyArea> layer = new BaseLayer(category, baseType).run(layerContextCreator.apply(250L));
		layer = LayerUtil.zoom(2000L, ZoomLayer.NORMAL, layer, zoomLevel, layerContextCreator);
		
		return new GristTypeLayer(layer.make());*/
		return new GristTypeLayer();
	}
	
	public GristType getTypeAt(int posX, int posZ)
	{
		/*int gristId = area.get(posX, posZ);
		return ((ForgeRegistry<GristType>) GristTypes.getRegistry()).getValue(gristId);*/
		return GristTypes.ARTIFACT.get();
	}
	/*
	private static class BaseLayer implements IAreaTransformer0
	{
		final List<GristEntry> gristTypes;
		final int weightSum;
		
		final int baseGristType;
		
		public BaseLayer(GristType.SpawnCategory category, @Nullable GristType type)
		{
			this.baseGristType = type == null ? -1 : ((ForgeRegistry<GristType>) GristTypes.getRegistry()).getID(type);
			gristTypes = GristTypes.values().stream().filter(GristType::isUnderlingType)
					.filter(gristType -> gristType.isInCategory(category)).map(GristEntry::new).collect(Collectors.toList());
			weightSum = WeightedRandom.getTotalWeight(gristTypes);
		}
		
		@Override
		public int applyPixel(INoiseRandom context, int x, int z)
		{
			if(baseGristType != -1 && x * x + z * z <= 1)
				return baseGristType;
			
			return WeightedRandom.getWeightedItem(gristTypes, context.nextRandom(weightSum)).gristId;
		}
		
		private static class GristEntry extends WeightedRandom.Item
		{
			private final int gristId;
			
			public GristEntry(GristType type)
			{
				super(Math.round(type.getRarity() * 100));
				this.gristId = ((ForgeRegistry<GristType>) GristTypes.getRegistry()).getID(type);
			}
		}
	}*/
}