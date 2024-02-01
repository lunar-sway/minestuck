package com.mraof.minestuck.world.gen.feature;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.List;

public class LargeCakeFeature extends AbstractTemplateFeature<NoneFeatureConfiguration>
{
	public LargeCakeFeature(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec);
	}
	
	@Override
	protected int pickY(WorldGenLevel level, TemplatePlacement placement, RandomSource random)
	{
		return Math.max(0, placement.minHeight(Heightmap.Types.WORLD_SURFACE_WG, level) - random.nextInt(1));
	}
	
	@Override
	public ResourceLocation pickTemplate(RandomSource random)
	{
		List<WeightedEntry.Wrapper<ResourceLocation>> weightedStructureList = buildWeightedList();
		int totalWeight = WeightedRandom.getTotalWeight(weightedStructureList);
		
		WeightedEntry.Wrapper<ResourceLocation> wrappedStructure = WeightedRandom.getRandomItem(random, weightedStructureList, totalWeight).orElseThrow();
		
		return wrappedStructure.getData();
	}
	
	private static List<WeightedEntry.Wrapper<ResourceLocation>> buildWeightedList()
	{
		List<WeightedEntry.Wrapper<ResourceLocation>> weightedStructureList = Lists.newArrayList();
		
		weightedStructureList.add(WeightedEntry.wrap(new ResourceLocation(Minestuck.MOD_ID, "large_cake_round"), 5));
		weightedStructureList.add(WeightedEntry.wrap(new ResourceLocation(Minestuck.MOD_ID, "large_cake_round_extra_large"), 2));
		weightedStructureList.add(WeightedEntry.wrap(new ResourceLocation(Minestuck.MOD_ID, "large_cake_double_layer_round"), 1));
		weightedStructureList.add(WeightedEntry.wrap(new ResourceLocation(Minestuck.MOD_ID, "large_cake_eaten"), 1));
		weightedStructureList.add(WeightedEntry.wrap(new ResourceLocation(Minestuck.MOD_ID, "large_cake_birthday0"), 1));
		weightedStructureList.add(WeightedEntry.wrap(new ResourceLocation(Minestuck.MOD_ID, "large_cake_birthday1"), 1));
		weightedStructureList.add(WeightedEntry.wrap(new ResourceLocation(Minestuck.MOD_ID, "large_cake_birthday2"), 1));
		
		return weightedStructureList;
	}
}