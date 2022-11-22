package com.mraof.minestuck.world.gen;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.skaianet.UnderlingController;
import com.mraof.minestuck.world.biome.WorldGenBiomeSet;
import com.mraof.minestuck.world.biome.RegistryBackedBiomeSet;
import com.mraof.minestuck.world.biome.LandBiomeSource;
import com.mraof.minestuck.world.gen.structure.gate.GateStructure;
import com.mraof.minestuck.world.gen.structure.gate.LandGatePlacement;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

public class LandChunkGenerator extends CustomizableNoiseChunkGenerator
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final Codec<LandChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> commonCodec(instance).and(instance.group(
					RegistryOps.retrieveRegistry(Registry.NOISE_REGISTRY).forGetter(generator -> generator.noises),
					RegistryOps.retrieveRegistry(Registry.DENSITY_FUNCTION_REGISTRY).forGetter(generator -> generator.densityFunctions),
					Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.seed),
					LandTypePair.Named.CODEC.fieldOf("named_land_types").forGetter(generator -> generator.namedTypes),
					RegistryOps.retrieveRegistry(Registry.BIOME_REGISTRY).forGetter(generator -> generator.registry)))
			.apply(instance, instance.stable(LandChunkGenerator::create)));
	
	public final LandTypePair.Named namedTypes;
	public final StructureBlockRegistry blockRegistry;
	public final WorldGenBiomeSet biomes;
	public final GateStructure.PieceFactory gatePiece;
	private final Registry<Biome> registry;
	
	private ChunkPos landGatePosition;
	protected final Registry<DensityFunction> densityFunctions;
	
	public static LandChunkGenerator create(Registry<StructureSet> structureSets, Registry<NormalNoise.NoiseParameters> noises, Registry<DensityFunction> densityFunctions, long seed, LandTypePair.Named namedTypes, Registry<Biome> registry)
	{
		RegistryBackedBiomeSet biomeSetWrapper = new RegistryBackedBiomeSet(namedTypes.landTypes().getTerrain().getBiomeSet(), registry);
		LandGenSettings genSettings = new LandGenSettings(namedTypes.landTypes());
		
		WorldGenBiomeSet biomeHolder = new WorldGenBiomeSet(biomeSetWrapper, genSettings);
		
		return new LandChunkGenerator(structureSets, noises, densityFunctions, seed, namedTypes, biomeHolder, registry, genSettings);
	}
	
	private LandChunkGenerator(Registry<StructureSet> structureSets, Registry<NormalNoise.NoiseParameters> noises, Registry<DensityFunction> densityFunctions, long seed, LandTypePair.Named namedTypes, WorldGenBiomeSet biomes, Registry<Biome> registry, LandGenSettings genSettings)
	{
		super(structureSets, noises, new LandBiomeSource(biomes, genSettings), new LandBiomeSource(biomes.baseBiomes, genSettings),
				seed, genSettings.createDimensionSettings(densityFunctions));
		
		this.densityFunctions = densityFunctions;
		this.biomes = biomes;
		this.registry = registry;
		this.namedTypes = namedTypes;
		this.blockRegistry = genSettings.getBlockRegistry();
		this.gatePiece = genSettings.getGatePiece();
	}
	
	@Override
	protected Codec<? extends LandChunkGenerator> codec()
	{
		return CODEC;
	}
	
	@Override
	public ChunkGenerator withSeed(long seed)
	{
		return LandChunkGenerator.create(this.structureSets, this.noises, this.densityFunctions, seed, namedTypes, registry);
	}
	
	@Override
	protected Holder<Biome> getWorldGenBiome(Holder<Biome> actualBiome)
	{
		return biomes.getBiomeFromBase(actualBiome);
	}
	
	@Override
	public WeightedRandomList<MobSpawnSettings.SpawnerData> getMobsAt(Holder<Biome> biome, StructureFeatureManager structures, MobCategory category, BlockPos pos)
	{
		if(category == MSEntityTypes.UNDERLING)
			return UnderlingController.getUnderlingList(pos);
		else return biomes.getBiomeFromBase(biome).value().getMobSettings().getMobs(category);
	}
	
	@Nullable
	@Override
	public Pair<BlockPos, Holder<ConfiguredStructureFeature<?, ?>>> findNearestMapFeature(ServerLevel level, HolderSet<ConfiguredStructureFeature<?, ?>> structureSet, BlockPos pos, int searchRadius, boolean skipKnownStructures)
	{
		var result = super.findNearestMapFeature(level, structureSet, pos, searchRadius, skipKnownStructures);
		
		Optional<Holder<ConfiguredStructureFeature<?, ?>>> optionalGateStructure = this.possibleStructureSets().map(Holder::value)
				.filter(set -> set.placement() == LandGatePlacement.INSTANCE)
				.flatMap(set -> set.structures().stream().filter(entry -> structureSet.contains(entry.structure())))
				.findAny().map(StructureSet.StructureSelectionEntry::structure);
		
		return optionalGateStructure.map(gateStructure -> {
			BlockPos gatePos = getOrFindLandGatePosition().getBlockAt(8, 64, 8);
			if(result != null && pos.distSqr(result.getFirst()) < pos.distSqr(gatePos))
				return result;
			else
				return Pair.of(gatePos, gateStructure);
		}).orElse(result);
	}
	
	public long getSeed()
	{
		return seed;
	}
	
	public ChunkPos getOrFindLandGatePosition()
	{
		if (landGatePosition != null)
			return landGatePosition;
		
		Random worldRand = new Random(seed);
		
		double angle = 2 * Math.PI * worldRand.nextDouble();
		int radius = 38 + worldRand.nextInt(12);
		
		Holder<Biome> normalBiome = biomes.baseBiomes.NORMAL;
		
		for(; radius < 65; radius += 6)
		{
			int posX = (int) Math.round(Math.cos(angle) * radius);
			int posZ = (int) Math.round(Math.sin(angle) * radius);
			
			//TODO Could there be a better way to search for a position? (Look for possible positions with the "surrounded by normal biomes" property rather than pick a random one and then check if it has this property)
			Pair<BlockPos, Holder<Biome>> result = getBiomeSource().findBiomeHorizontal((posX << 4) + 8, 0,(posZ << 4) + 8, 96, biome -> biome == normalBiome, worldRand, climateSampler());
			
			if(result != null)
			{
				BlockPos pos = result.getFirst();
				if(getBiomeSource().getBiomesWithin(pos.getX(), 0, pos.getZ(), 16, climateSampler()).stream().allMatch(biome -> biome == normalBiome))
					return new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
			}
		}
		
		int posX = (int) Math.round(Math.cos(angle) * radius);
		int posZ = (int) Math.round(Math.sin(angle) * radius);
		LOGGER.warn("Did not come across a decent location for land gates. Placing it without regard to any biomes.");
		
		BlockPos pos = getBiomeSource().findBiomeHorizontal((posX << 4) + 8, 0, (posZ << 4) + 8, 96, biome -> biome == normalBiome, worldRand, climateSampler()).getFirst();
		
		if(pos != null)
			landGatePosition = new ChunkPos(pos);
		else landGatePosition = new ChunkPos(posX, posZ);
		
		return landGatePosition;
	}
}