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
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.List;

public class LargeCakeFeature extends Feature<NoneFeatureConfiguration>
{
	public LargeCakeFeature(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec);
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
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
	{
		WorldGenLevel level = context.level();
		RandomSource rand = context.random();
		
		ResourceLocation templateId = WeightedRandom.getRandomItem(rand, buildWeightedList()).orElseThrow().getData();
		StructureTemplate template = level.getLevel().getStructureManager().getOrCreate(templateId);
		TemplatePlacement placement = TemplatePlacement.centeredWithRandomRotation(template, context.origin(), rand);
		
		int y = Math.max(0, placement.minHeight(Heightmap.Types.WORLD_SURFACE_WG, level) - rand.nextInt(1));
		placement.placeWithStructureBlockRegistryAt(y, context);
		
		return true;
	}
}