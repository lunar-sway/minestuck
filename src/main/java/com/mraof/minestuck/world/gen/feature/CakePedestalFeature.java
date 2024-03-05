package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class CakePedestalFeature extends Feature<NoneFeatureConfiguration>
{
	private static final ResourceLocation STRUCTURE_CAKE_PEDESTAL = new ResourceLocation(Minestuck.MOD_ID, "cake_pedestal");
	
	public CakePedestalFeature(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
	{
		StructureTemplate template = context.level().getLevel().getStructureManager().getOrCreate(STRUCTURE_CAKE_PEDESTAL);
		TemplatePlacement placement = TemplatePlacement.centeredWithRandomRotation(template, context.origin(), context.random());
		placement.placeWithStructureBlockRegistry(context);
		
		return true;
	}
}
