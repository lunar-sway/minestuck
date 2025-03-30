package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Locale;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class SimpleTemplateFeature extends Feature<SimpleTemplateFeature.Config>
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public SimpleTemplateFeature(Codec<Config> pCodec)
	{
		super(pCodec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<Config> context)
	{
		Config config = context.config();
		Optional<StructureTemplate> optionalTemplate = context.level().getLevel().getStructureManager().get(config.templateId);
		if(optionalTemplate.isEmpty())
		{
			LOGGER.warn("Tried generating missing template {}", config.templateId);
			return false;
		}
		
		if(!isValidTemplateSize(optionalTemplate.get().getSize()))
		{
			LOGGER.warn("Template {} is too big to be placed as a feature", config.templateId);
			return false;
		}
		
		TemplatePlacement placement = TemplatePlacement.centeredWithRandomRotation(optionalTemplate.get(), context.origin(), context.random());
		
		StructurePlaceSettings settings = new StructurePlaceSettings();
		if(config.useLandBlocks)
			settings.addProcessor(StructureBlockRegistryProcessor.INSTANCE);
		
		if(config.heightPlacement.isPresent())
		{
			int y = config.heightPlacement.get().pickHeight(placement, context.level(), context.random());
			placement.placeAt(y, context, settings);
		} else
		{
			placement.place(context, settings);
		}
		return true;
	}
	
	private static boolean isValidTemplateSize(Vec3i size)
	{
		return size.getX() <= 33 && size.getZ() <= 33;
	}
	
	public record Config(ResourceLocation templateId, boolean useLandBlocks, Optional<TemplateHeightPlacement> heightPlacement) implements FeatureConfiguration
	{
		public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ResourceLocation.CODEC.fieldOf("template").forGetter(Config::templateId),
				Codec.BOOL.fieldOf("land_block_replacement").forGetter(Config::useLandBlocks),
				TemplateHeightPlacement.CODEC.optionalFieldOf("height_placement").forGetter(Config::heightPlacement)
		).apply(instance, Config::new));
		
		public Config(ResourceLocation templateId, boolean useLandBlocks)
		{
			this(templateId, useLandBlocks, Optional.empty());
		}
		
		public Config(ResourceLocation templateId, boolean useLandBlocks, TemplateHeightPlacement heightPlacement)
		{
			this(templateId, useLandBlocks, Optional.of(heightPlacement));
		}
	}
	
	public record TemplateHeightPlacement(HeightQueryType queryType, Heightmap.Types heightmapType, IntProvider offset)
	{
		public static final Codec<TemplateHeightPlacement> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				HeightQueryType.CODEC.fieldOf("query_type").forGetter(TemplateHeightPlacement::queryType),
				Heightmap.Types.CODEC.fieldOf("heightmap").forGetter(TemplateHeightPlacement::heightmapType),
				IntProvider.CODEC.optionalFieldOf("offset", ConstantInt.of(0)).forGetter(TemplateHeightPlacement::offset)
		).apply(instance, TemplateHeightPlacement::new));
		
		public int pickHeight(TemplatePlacement placement, LevelReader level, RandomSource random)
		{
			int queriedHeight = switch(this.queryType)
			{
				case MIN -> placement.minHeight(this.heightmapType, level);
				case MAX -> placement.maxHeight(this.heightmapType, level);
				case BETWEEN ->
				{
					var range = placement.heightRange(this.heightmapType, level);
					yield (range.min() + range.max()) / 2;
				}
			};
			
			return queriedHeight + this.offset.sample(random);
		}
	}
	
	public enum HeightQueryType implements StringRepresentable
	{
		MIN,
		MAX,
		BETWEEN;
		
		public static final Codec<HeightQueryType> CODEC = StringRepresentable.fromEnum(HeightQueryType::values);
		
		@Override
		public String getSerializedName()
		{
			return this.name().toLowerCase(Locale.ROOT);
		}
		
		public TemplateHeightPlacement with(Heightmap.Types heightmapType, IntProvider offset)
		{
			return new TemplateHeightPlacement(this, heightmapType, offset);
		}
	}
}
