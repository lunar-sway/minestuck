package com.mraof.minestuck.world.gen;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.skaianet.UnderlingController;
import com.mraof.minestuck.world.biome.LandBiomeHolder;
import com.mraof.minestuck.world.biome.LandBiomeSetWrapper;
import com.mraof.minestuck.world.biome.LandBiomeSource;
import com.mraof.minestuck.world.gen.structure.GateStructure;
import com.mraof.minestuck.world.gen.structure.LandGatePlacement;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
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
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

public class LandChunkGenerator extends AbstractChunkGenerator
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final Codec<LandChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> commonCodec(instance).and(instance.group(
					RegistryOps.retrieveRegistry(Registry.NOISE_REGISTRY).forGetter(generator -> generator.noises),
					Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.seed),
					LandTypePair.CODEC.fieldOf("land_types").forGetter(generator -> generator.landTypes),
					RegistryOps.retrieveRegistry(Registry.BIOME_REGISTRY).forGetter(generator -> generator.registry)))
			.apply(instance, instance.stable(LandChunkGenerator::new)));
	
	public final LandTypePair landTypes;
	public final StructureBlockRegistry blockRegistry;
	public final LandBiomeHolder biomes;
	public final GateStructure.PieceFactory gatePiece;
	private final Registry<Biome> registry;
	
	private ChunkPos landGatePosition;
	
	public LandChunkGenerator(Registry<StructureSet> structureSets, Registry<NormalNoise.NoiseParameters> noises, long seed, LandTypePair landTypes, Registry<Biome> registry)
	{
		this(structureSets, noises, seed, new LandBiomeSetWrapper(landTypes.getTerrain().getBiomeSet(), registry), registry, LandProperties.create(landTypes), new LandGenSettings(landTypes));
	}
	
	private LandChunkGenerator(Registry<StructureSet> structureSets, Registry<NormalNoise.NoiseParameters> noises, long seed, LandBiomeSetWrapper baseBiomes, Registry<Biome> registry, LandProperties properties, LandGenSettings genSettings)
	{
		this(structureSets, noises, seed, new LandBiomeHolder(baseBiomes, genSettings, properties), registry, genSettings);
	}
	
	private LandChunkGenerator(Registry<StructureSet> structureSets, Registry<NormalNoise.NoiseParameters> noises, long seed, LandBiomeHolder biomes, Registry<Biome> registry, LandGenSettings genSettings)
	{
		super(structureSets, noises, Optional.empty(), new LandBiomeSource(biomes, genSettings), new LandBiomeSource(biomes.baseBiomes, genSettings),
				seed, genSettings.createDimensionSettings());
		
		this.biomes = biomes;
		this.registry = registry;
		landTypes = genSettings.getLandTypes();
		blockRegistry = genSettings.getBlockRegistry();
		gatePiece = genSettings.getGatePiece();
	}
	
	@Override
	protected Codec<? extends ChunkGenerator> codec()
	{
		return CODEC;
	}
	
	@Override
	public ChunkGenerator withSeed(long seed)
	{
		return new LandChunkGenerator(this.structureSets, this.noises, seed, landTypes, registry);
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
			BlockPos pos = getBiomeSource().findBiomeHorizontal((posX << 4) + 8, 0,(posZ << 4) + 8, 96, biome -> biome == normalBiome, worldRand, climateSampler()).getFirst();
			
			if(pos != null && getBiomeSource().getBiomesWithin(pos.getX(), 0, pos.getZ(), 16, climateSampler()).stream().allMatch(biome -> biome == normalBiome))
				return new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
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