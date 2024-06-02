package com.mraof.minestuck.world.gen;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.underling.UnderlingSpawnSettings;
import com.mraof.minestuck.world.biome.LandBiomeSource;
import com.mraof.minestuck.world.biome.LandCustomBiomeSettings;
import com.mraof.minestuck.world.biome.RegistryBackedBiomeSet;
import com.mraof.minestuck.world.gen.structure.MSStructures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.structure.gate.GateStructure;
import com.mraof.minestuck.world.lands.LandTypeExtensions;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LandChunkGenerator extends CustomizableNoiseChunkGenerator
{
	public static final Codec<LandChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			RegistryOps.retrieveGetter(Registries.NOISE),
			RegistryOps.retrieveGetter(Registries.DENSITY_FUNCTION),
			RegistryOps.retrieveGetter(Registries.STRUCTURE),
			LandTypePair.Named.CODEC.fieldOf("named_land_types").forGetter(generator -> generator.namedTypes),
			RegistryOps.retrieveGetter(Registries.BIOME),
			RegistryOps.retrieveGetter(Registries.PLACED_FEATURE),
			RegistryOps.retrieveGetter(Registries.CONFIGURED_CARVER)
	).apply(instance, instance.stable(LandChunkGenerator::create)));
	
	public final LandTypePair.Named namedTypes;
	public final StructureBlockRegistry blockRegistry;
	private final LazyBiomeSettings customBiomeSettings;
	public final GateStructure.PieceFactory gatePiece;
	private final HolderGetter<Structure> structureLookup;
	
	public static LandChunkGenerator create(HolderGetter<NormalNoise.NoiseParameters> noises, HolderGetter<DensityFunction> densityFunctions, HolderGetter<Structure> structureLookup,
											LandTypePair.Named namedTypes, HolderGetter<Biome> biomeGetter, HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers)
	{
		RegistryBackedBiomeSet biomes = new RegistryBackedBiomeSet(namedTypes.landTypes().getTerrain().getBiomeSet(), biomeGetter);
		LandGenSettings genSettings = new LandGenSettings(namedTypes.landTypes());
		
		LazyBiomeSettings customBiomeSettings = new LazyBiomeSettings(extensions -> new LandCustomBiomeSettings(biomes, genSettings, extensions, features, carvers));
		
		return new LandChunkGenerator(noises, densityFunctions, structureLookup, namedTypes, biomes, customBiomeSettings, genSettings);
	}
	
	private LandChunkGenerator(HolderGetter<NormalNoise.NoiseParameters> noises, HolderGetter<DensityFunction> densityFunctions, HolderGetter<Structure> structureLookup,
							   LandTypePair.Named namedTypes, RegistryBackedBiomeSet biomes, LazyBiomeSettings customBiomeSettings, LandGenSettings genSettings)
	{
		super(new LandBiomeSource(biomes, genSettings), biome -> customBiomeSettings.get().generationFor(biome),
				genSettings.createDimensionSettings(noises, densityFunctions));
		
		this.customBiomeSettings = customBiomeSettings;
		this.namedTypes = namedTypes;
		this.blockRegistry = genSettings.getBlockRegistry();
		this.gatePiece = genSettings.getGatePiece();
		this.structureLookup = structureLookup;
	}
	
	@Override
	protected Codec<? extends LandChunkGenerator> codec()
	{
		return CODEC;
	}
	
	@Override
	public ChunkGeneratorStructureState createState(HolderLookup<StructureSet> structureSetLookup, RandomState randomState, long seed)
	{
		List<StructureSet> landSpecificStructureSets = new ArrayList<>();
		this.namedTypes.landTypes().getTerrain().addStructureSets(landSpecificStructureSets::add, this.structureLookup);
		this.namedTypes.landTypes().getTitle().addStructureSets(landSpecificStructureSets::add, this.structureLookup);
		
		List<Holder<StructureSet>> structureSets = Stream.concat(
				landSpecificStructureSets.stream().map(Holder::direct),
				structureSetLookup.listElements()
						.filter(structureSet -> hasStructureSet(structureSet.value()))
						.<Holder<StructureSet>>map(holder -> holder)
		).toList();
		return new LandStructureState(randomState, biomeSource, seed, structureSets);
	}
	
	private boolean hasStructureSet(StructureSet structureSet)
	{
		return structureSet.structures().stream().flatMap(entry -> entry.structure().value().biomes().stream()).anyMatch(biomeSource.possibleBiomes()::contains);
	}
	
	@Override
	public WeightedRandomList<MobSpawnSettings.SpawnerData> getMobsAt(Holder<Biome> biome, StructureManager structures, MobCategory category, BlockPos pos)
	{
		if(category == MSEntityTypes.UNDERLING)
			return UnderlingSpawnSettings.getUnderlingList(pos);
		else return customBiomeSettings.get().customMobSpawnsFor(biome).getMobs(category);
	}
	
	@Nullable
	@Override
	public Pair<BlockPos, Holder<Structure>> findNearestMapStructure(ServerLevel level, HolderSet<Structure> structureSet, BlockPos pos, int searchRadius, boolean skipKnownStructures)
	{
		var state = level.getChunkSource().getGeneratorState();
		var result = super.findNearestMapStructure(level, structureSet, pos, searchRadius, skipKnownStructures);
		
		if(!(level.getChunkSource().getGeneratorState() instanceof LandStructureState landStructureState))
			return result;
		
		Optional<Holder<Structure>> optionalGateStructure = structureSet.stream().filter(structure -> hasGatePlacement(state, structure)).findAny();
		
		return optionalGateStructure.map(gateStructure -> {
			BlockPos gatePos = landStructureState.getOrFindLandGatePosition().getBlockAt(8, 64, 8);
			if(result != null && pos.distSqr(result.getFirst()) < pos.distSqr(gatePos))
				return result;
			else
				return Pair.of(gatePos, gateStructure);
		}).orElse(result);
	}
	
	private static boolean hasGatePlacement(ChunkGeneratorStructureState state, Holder<Structure> structure)
	{
		return state.getPlacementsForStructure(structure).stream().anyMatch(placement -> placement.type() == MSStructures.LAND_GATE_PLACEMENT.get());
	}
	
	public boolean tryInit(LandTypeExtensions extensions)
	{
		return this.customBiomeSettings.tryInit(extensions);
	}
	
	private static final class LazyBiomeSettings
	{
		private final Function<LandTypeExtensions, LandCustomBiomeSettings> constructor;
		private LandCustomBiomeSettings value = null;
		
		
		public LazyBiomeSettings(Function<LandTypeExtensions, LandCustomBiomeSettings> constructor)
		{
			this.constructor = constructor;
		}
		
		public LandCustomBiomeSettings get()
		{
			return Objects.requireNonNull(this.value);
		}
		
		public boolean tryInit(LandTypeExtensions extensions)
		{
			if(this.value != null)
				return false;
			this.value = constructor.apply(extensions);
			return true;
		}
	}
}
