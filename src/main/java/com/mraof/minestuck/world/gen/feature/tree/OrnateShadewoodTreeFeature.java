package com.mraof.minestuck.world.gen.feature.tree;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.feature.TemplatePlacement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

/**
 * Structure nbt defined shadewood trees
 */
public class OrnateShadewoodTreeFeature extends Feature<NoneFeatureConfiguration>
{
	private static final ResourceLocation ORNATE_SHADEWOOD_TREE0 = new ResourceLocation(Minestuck.MOD_ID, "ornate_shadewood_tree0");
	private static final ResourceLocation ORNATE_SHADEWOOD_TREE1 = new ResourceLocation(Minestuck.MOD_ID, "ornate_shadewood_tree1");
	private static final ResourceLocation ORNATE_SHADEWOOD_TREE2 = new ResourceLocation(Minestuck.MOD_ID, "ornate_shadewood_tree2");
	private static final ResourceLocation ORNATE_SHADEWOOD_TREE3 = new ResourceLocation(Minestuck.MOD_ID, "ornate_shadewood_tree3");
	
	public OrnateShadewoodTreeFeature(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec);
	}
	
	private static ResourceLocation pickTemplate(RandomSource random)
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
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
	{
		RandomSource rand = context.random();
		StructureTemplate template = context.level().getLevel().getStructureManager().getOrCreate(pickTemplate(rand));
		TemplatePlacement placement = TemplatePlacement.centeredWithRandomRotation(template, context.origin(), rand);
		placement.placeWithStructureBlockRegistry(context);
		
		return true;
	}
}