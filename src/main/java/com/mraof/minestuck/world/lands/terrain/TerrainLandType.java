package com.mraof.minestuck.world.lands.terrain;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeSetType;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.structure.MSStructures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.ILandType;
import com.mraof.minestuck.world.lands.LandBiomeGenBuilder;
import com.mraof.minestuck.world.lands.LandTypeExtensions;
import com.mraof.minestuck.world.lands.LandTypeExtensions.StructureSetExtension;
import com.mraof.minestuck.world.lands.LandTypes;
import net.minecraft.core.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Base class for land types that make up the base of a land.
 */
public abstract class TerrainLandType implements ILandType
{
	public static final Codec<TerrainLandType> CODEC = LandTypes.TERRAIN_REGISTRY.byNameCodec();
	public static final StreamCodec<RegistryFriendlyByteBuf, TerrainLandType> STREAM_CODEC = ByteBufCodecs.registry(LandTypes.TERRAIN_KEY);
	private static final Logger LOGGER = LogManager.getLogger();
	
	protected static final RandomSpreadStructurePlacement SMALL_RUIN_PLACEMENT = new RandomSpreadStructurePlacement(16, 4, RandomSpreadType.LINEAR, 59273643);
	protected static final RandomSpreadStructurePlacement IMP_DUNGEON_PLACEMENT = new RandomSpreadStructurePlacement(16, 4, RandomSpreadType.LINEAR, 34527185);
	protected static final RandomSpreadStructurePlacement CONSORT_VILLAGE_PLACEMENT = new RandomSpreadStructurePlacement(24, 5, RandomSpreadType.LINEAR, 10387312);
	
	private final String[] names;
	
	private final Supplier<? extends EntityType<? extends ConsortEntity>> consortType;
	private final float skylightBase;
	private final Vec3 fogColor, skyColor;
	
	private LandTypeExtensions.ParsedExtension extensions;
	private final List<LandTypeExtensions.FeatureExtension> featureExtensions = new ArrayList<>();
	
	private final LandBiomeSetType biomeSet;
	private final Supplier<SoundEvent> backgroundMusic;
	
	protected TerrainLandType(Builder builder)
	{
		this.names = Objects.requireNonNull(builder.names);
		
		this.consortType = builder.consortType;
		this.skylightBase = builder.skylightBase;
		this.fogColor = builder.fogColor;
		this.skyColor = builder.skyColor;
		this.biomeSet = builder.biomeSet;
		this.backgroundMusic = builder.backgroundMusic;
	}
	
	public final float getSkylightBase()
	{
		return this.skylightBase;
	}
	
	public final Vec3 getFogColor()
	{
		return this.fogColor;
	}
	
	public final Vec3 getSkyColor()
	{
		return this.skyColor;
	}
	
	@Override
	public final String[] getNames()
	{
		return this.names;
	}
	
	public final EntityType<? extends ConsortEntity> getConsortType()
	{
		return this.consortType.get();
	}
	
	public final LandBiomeSetType getBiomeSet()
	{
		return this.biomeSet;
	}
	
	@Override
	public final SoundEvent getBackgroundMusic()
	{
		return this.backgroundMusic.get();
	}
	
	public SurfaceRules.RuleSource getSurfaceRule(StructureBlockRegistry blocks)
	{
		ResourceKey<Biome> roughBiome = this.getBiomeSet().ROUGH;
		
		SurfaceRules.RuleSource surfaceBlock = SurfaceRules.sequence(
				SurfaceRules.ifTrue(SurfaceRules.isBiome(roughBiome), SurfaceRules.state(blocks.getBlockState(StructureBlockRegistry.SURFACE_ROUGH))),
				SurfaceRules.state(blocks.getBlockState(StructureBlockRegistry.SURFACE)));
		SurfaceRules.RuleSource surface = SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(SurfaceRules.waterBlockCheck(0, 0), surfaceBlock));
		
		SurfaceRules.RuleSource upper = SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SurfaceRules.ifTrue(SurfaceRules.waterStartCheck(-6, -1), SurfaceRules.state(blocks.getBlockState(StructureBlockRegistry.UPPER))));
		// This surface rule targets the ocean surface by being positioned after "surface" and "upper", thus only placing blocks on surfaces where "surface" or "upper" doesn't
		SurfaceRules.RuleSource ocean_surface = SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.state(blocks.getBlockState(StructureBlockRegistry.OCEAN_SURFACE)));
		
		return SurfaceRules.sequence(surface, upper, ocean_surface);
	}
	
	public void addBiomeGeneration(LandBiomeGenBuilder builder, StructureBlockRegistry blocks)
	{
	}
	
	public void addExtensions(Registry<PlacedFeature> features, StructureBlockRegistry blocks)
	{
	}
	
	public void addFeatureExtension(Registry<PlacedFeature> features, GenerationStep.Decoration step, ResourceKey<PlacedFeature> feature, LandBiomeType... biomeTypes)
	{
		//Holder<PlacedFeature> featureHolder = featureLookup.get(feature).orElseThrow().getDelegate();
		//featureExtensions.add(new LandTypeExtensions.FeatureExtension(step, featureHolder, Arrays.stream(biomeTypes).toList()));
		
		//featureExtensions.add(new LandTypeExtensions.FeatureExtension(step, Holder.direct(featureLookup.get(feature).orElseThrow().getDelegate().value()), Arrays.stream(biomeTypes).toList()));
		
		//Holder<PlacedFeature> featureHolder = featureLookup.get(feature).orElseThrow().getDelegate();
		//featureExtensions.add(new LandTypeExtensions.FeatureExtension(step, featureHolder, Arrays.stream(biomeTypes).toList()));
		
		//featureExtensions.add(new LandTypeExtensions.FeatureExtension(step, features.getOrThrow(feature), Arrays.stream(biomeTypes).toList()));
		
		featureExtensions.add(new LandTypeExtensions.FeatureExtension(step, features.wrapAsHolder(features.getOrThrow(feature)), Arrays.stream(biomeTypes).toList()));
		//regard'ess of approach, error line 73 in provider, java.lang.IllegalStateException: Can't access registry ResourceKey[minecraft:root / minecraft:block];
	}
	
	public void addFeatureExtension(GenerationStep.Decoration step, PlacedFeature feature, LandBiomeType... biomeTypes)
	{
		featureExtensions.add(new LandTypeExtensions.FeatureExtension(step, Holder.direct(feature), Arrays.stream(biomeTypes).toList()));
	}
	
	public LandTypeExtensions.ParsedExtension getExtensions(Registry<PlacedFeature> features, StructureBlockRegistry blocks)
	{
		addExtensions(features, blocks);
		return new LandTypeExtensions.ParsedExtension(featureExtensions, List.of(), List.of(), List.of());
	}
	
	@Override
	public void addStructureSets(Consumer<StructureSet> consumer, HolderGetter<Structure> structureLookup)
	{
		consumer.accept(new StructureSet(structureLookup.getOrThrow(MSStructures.SMALL_RUIN), SMALL_RUIN_PLACEMENT));
		consumer.accept(new StructureSet(structureLookup.getOrThrow(MSStructures.ImpDungeon.KEY), IMP_DUNGEON_PLACEMENT));
		consumer.accept(new StructureSet(structureLookup.getOrThrow(MSStructures.ConsortVillage.KEY), CONSORT_VILLAGE_PLACEMENT));
		
		LandTypeExtensions landTypeExtensions = LandTypeExtensions.get();
		List<StructureSetExtension> structureSets = landTypeExtensions.getStructureSetsFor(this);
		for(StructureSetExtension structureSetExtension : structureSets)
		{
			consumer.accept(structureSetExtension.structureSet());
		}
	}
	
	public final boolean is(TagKey<TerrainLandType> tag)
	{
		return this.is(LandTypes.TERRAIN_REGISTRY.getOrCreateTag(tag));
	}
	
	public final boolean is(HolderSet<TerrainLandType> set)
	{
		return set.stream().anyMatch(holder -> holder.value() == this);
	}
	
	public static class Builder
	{
		private String[] names;
		
		private final Supplier<? extends EntityType<? extends ConsortEntity>> consortType;
		private float skylightBase = 1F;
		private Vec3 fogColor = new Vec3(0, 0, 0);
		private Vec3 skyColor = new Vec3(0, 0, 0);
		private LandBiomeSetType biomeSet = MSBiomes.DEFAULT_LAND;
		private Supplier<SoundEvent> backgroundMusic = MSSoundEvents.MUSIC_DEFAULT;
		
		public Builder(Supplier<? extends EntityType<? extends ConsortEntity>> consortType)
		{
			this.consortType = consortType;
		}
		
		public Builder names(String... names)
		{
			this.names = names;
			return this;
		}
		
		public Builder skylight(float skylightBase)
		{
			this.skylightBase = skylightBase;
			return this;
		}
		
		public Builder fogColor(double r, double g, double b)
		{
			this.fogColor = new Vec3(r, g, b);
			return this;
		}
		
		public Builder skyColor(double r, double g, double b)
		{
			this.skyColor = new Vec3(r, g, b);
			return this;
		}
		
		public Builder biomeSet(LandBiomeSetType biomeSet)
		{
			this.biomeSet = biomeSet;
			return this;
		}
		
		public Builder music(Supplier<SoundEvent> backgroundMusic)
		{
			this.backgroundMusic = backgroundMusic;
			return this;
		}
	}
}
