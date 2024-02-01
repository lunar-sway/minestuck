package com.mraof.minestuck.world.gen.feature.tree;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.feature.AbstractTemplateFeature;
import com.mraof.minestuck.world.gen.feature.TemplatePlacement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Structure nbt defined shadewood trees
 */
public class OrnateShadewoodTreeFeature extends AbstractTemplateFeature<NoneFeatureConfiguration>
{
	private static final ResourceLocation ORNATE_SHADEWOOD_TREE0 = new ResourceLocation(Minestuck.MOD_ID, "ornate_shadewood_tree0");
	private static final ResourceLocation ORNATE_SHADEWOOD_TREE1 = new ResourceLocation(Minestuck.MOD_ID, "ornate_shadewood_tree1");
	private static final ResourceLocation ORNATE_SHADEWOOD_TREE2 = new ResourceLocation(Minestuck.MOD_ID, "ornate_shadewood_tree2");
	private static final ResourceLocation ORNATE_SHADEWOOD_TREE3 = new ResourceLocation(Minestuck.MOD_ID, "ornate_shadewood_tree3");
	
	public OrnateShadewoodTreeFeature(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec);
	}
	
	@Override
	protected ResourceLocation pickTemplate(RandomSource random)
	{
		int randInt = random.nextInt(4);
		if(randInt == 0)
			return ORNATE_SHADEWOOD_TREE0;
		else if(randInt == 1)
			return ORNATE_SHADEWOOD_TREE1;
		else if(randInt == 2)
			return ORNATE_SHADEWOOD_TREE2;
		else
			return ORNATE_SHADEWOOD_TREE3;
	}
	
	@Override
	protected int pickY(WorldGenLevel level, TemplatePlacement placement, RandomSource random)
	{
		return placement.minHeight(Heightmap.Types.OCEAN_FLOOR, level);
	}
}