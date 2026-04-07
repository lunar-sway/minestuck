package com.mraof.minestuck.world.lands.title;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.world.biome.LandBiomeSetType;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.ILandType;
import com.mraof.minestuck.world.lands.LandBiomeGenBuilder;
import com.mraof.minestuck.world.lands.LandTypeExtensions;
import com.mraof.minestuck.world.lands.LandTypeExtensions.StructureSetExtension;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Base class for land types that are associated with some aspect.
 * These land types make up the second half of a land together with a {@link TerrainLandType}.
 */
public abstract class TitleLandType implements ILandType
{
	public static final Codec<TitleLandType> CODEC = LandTypes.TITLE_REGISTRY.byNameCodec();
	public static final StreamCodec<RegistryFriendlyByteBuf, TitleLandType> STREAM_CODEC = ByteBufCodecs.registry(LandTypes.TITLE_KEY);
	
	private final List<LandTypeExtensions.FeatureExtension> featureExtensions = new ArrayList<>();
	private final List<LandTypeExtensions.CarverExtension> carverExtensions = new ArrayList<>();
	private final List<LandTypeExtensions.MobSpawnExtension> spawnExtensions = new ArrayList<>();
	private final List<LandTypeExtensions.StructureSetExtension> structureExtensions = new ArrayList<>();
	
	/**
	 * Returns true if the given land type may be randomly chosen together with this land type.
	 */
	public boolean isAspectCompatible(TerrainLandType otherType)
	{
		return true;
	}
	
	public void addBiomeGeneration(LandBiomeGenBuilder builder, StructureBlockRegistry blocks, LandBiomeSetType biomeSet)
	{
	}
	
	public void addExtensions(HolderLookup.Provider provider, StructureBlockRegistry blocks)
	{
	}
	
	public void addFeatureExtension(HolderLookup.RegistryLookup<PlacedFeature> features, GenerationStep.Decoration step, ResourceKey<PlacedFeature> feature, LandBiomeType... biomeTypes)
	{
		featureExtensions.add(new LandTypeExtensions.FeatureExtension(step, features.getOrThrow(feature), Arrays.stream(biomeTypes).toList()));
	}
	
	public void addFeatureExtension(GenerationStep.Decoration step, PlacedFeature feature, LandBiomeType... biomeTypes)
	{
		featureExtensions.add(new LandTypeExtensions.FeatureExtension(step, Holder.direct(feature), Arrays.stream(biomeTypes).toList()));
	}
	
	public void addCarverExtension(GenerationStep.Carving step, ConfiguredWorldCarver<?> carver, LandBiomeType... biomeTypes)
	{
		carverExtensions.add(new LandTypeExtensions.CarverExtension(step, Holder.direct(carver), Arrays.stream(biomeTypes).toList()));
	}
	
	public void addMobSpawnExtension(MobCategory category, MobSpawnSettings.SpawnerData spawnerData, LandBiomeType... biomeTypes)
	{
		spawnExtensions.add(new LandTypeExtensions.MobSpawnExtension(category, spawnerData, Arrays.stream(biomeTypes).toList()));
	}
	
	public void addStructureExtension(StructureSet structureSet)
	{
		structureExtensions.add(new LandTypeExtensions.StructureSetExtension(structureSet));
	}
	
	public LandTypeExtensions.ParsedExtension getExtensions(HolderLookup.Provider provider, StructureBlockRegistry blocks)
	{
		addExtensions(provider, blocks);
		return new LandTypeExtensions.ParsedExtension(featureExtensions, carverExtensions, spawnExtensions, structureExtensions);
	}
	
	@Override
	public void addStructureSets(Consumer<StructureSet> consumer, HolderGetter<Structure> structureLookup)
	{
		LandTypeExtensions landTypeExtensions = LandTypeExtensions.get();
		List<StructureSetExtension> structureSets = landTypeExtensions.getStructureSetsFor(this);
		for(StructureSetExtension structureSetExtension : structureSets)
		{
			consumer.accept(structureSetExtension.structureSet());
		}
	}
	
	public final boolean is(TagKey<TitleLandType> tag)
	{
		return this.is(LandTypes.TITLE_REGISTRY.getOrCreateTag(tag));
	}
	
	public final boolean is(HolderSet<TitleLandType> set)
	{
		return set.stream().anyMatch(holder -> holder.value() == this);
	}
}
