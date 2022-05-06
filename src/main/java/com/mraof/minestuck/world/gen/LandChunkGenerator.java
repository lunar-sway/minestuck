package com.mraof.minestuck.world.gen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.skaianet.UnderlingController;
import com.mraof.minestuck.world.biome.LandBiomeHolder;
import com.mraof.minestuck.world.biome.LandBiomeSetWrapper;
import com.mraof.minestuck.world.biome.gen.LandBiomeProvider;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.feature.structure.GateStructure;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.server.ServerWorld;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class LandChunkGenerator extends AbstractChunkGenerator
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final Codec<LandChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.seed),
					LandTypePair.CODEC.fieldOf("land_types").forGetter(generator -> generator.landTypes),
					RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter(generator -> generator.registry))
			.apply(instance, instance.stable(LandChunkGenerator::new)));
	
	public final LandTypePair landTypes;
	public final StructureBlockRegistry blockRegistry;
	public final LandBiomeHolder biomes;
	public final GateStructure.PieceFactory gatePiece;
	private final Registry<Biome> registry;
	
	private ChunkPos landGatePosition;
	
	public LandChunkGenerator(long seed, LandTypePair landTypes, Registry<Biome> registry)
	{
		this(seed, new LandBiomeSetWrapper(landTypes.getTerrain().getBiomeSet(), registry), registry, LandProperties.create(landTypes), new LandGenSettings(landTypes));
	}
	
	private LandChunkGenerator(long seed, LandBiomeSetWrapper baseBiomes, Registry<Biome> registry, LandProperties properties, LandGenSettings genSettings)
	{
		this(seed, new LandBiomeHolder(baseBiomes, genSettings, properties), registry, genSettings);
	}
	
	private LandChunkGenerator(long seed, LandBiomeHolder biomes, Registry<Biome> registry, LandGenSettings genSettings)
	{
		super(new LandBiomeProvider(seed, biomes, genSettings), new LandBiomeProvider(seed, biomes.baseBiomes, genSettings),
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
		return new LandChunkGenerator(seed, landTypes, registry);
	}
	
	@Override
	protected Biome getBiome(WorldGenRegion region, BlockPos pos)
	{
		return biomes.getBiomeFromBase(super.getBiome(region, pos));
	}
	
	@Override
	public List<MobSpawnInfo.Spawners> getMobsAt(Biome biome, StructureManager structures, EntityClassification classification, BlockPos pos)
	{
		if(classification == MSEntityTypes.UNDERLING)
			return UnderlingController.getUnderlingList(pos);
		else return biomes.getBiomeFromBase(biome).getMobSettings().getMobs(classification);
	}
	
	@Nullable
	@Override
	public BlockPos findNearestMapFeature(ServerWorld world, Structure<?> structure, BlockPos pos, int searchSize, boolean checkReference)
	{
		if (structure == MSFeatures.LAND_GATE)
		{
			ChunkPos gatePos = getOrFindLandGatePosition();
			return new BlockPos((gatePos.x << 4) + 8, 32, (gatePos.z << 4) + 8);
		} else
			return super.findNearestMapFeature(world, structure, pos, searchSize, checkReference);
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
		
		Biome normalBiome = biomes.baseBiomes.NORMAL;
		
		for(; radius < 65; radius += 6)
		{
			int posX = (int) Math.round(Math.cos(angle) * radius);
			int posZ = (int) Math.round(Math.sin(angle) * radius);
			
			//TODO Could there be a better way to search for a position? (Look for possible positions with the "surrounded by normal biomes" property rather than pick a random one and then check if it has this property)
			BlockPos pos = getBiomeSource().findBiomeHorizontal((posX << 4) + 8, 0,(posZ << 4) + 8, 96, biome -> biome == normalBiome, worldRand);
			
			if(pos != null && getBiomeSource().getBiomesWithin(pos.getX(), 0, pos.getZ(), 16).stream().allMatch(biome -> biome == normalBiome))
				return new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
		}
		
		int posX = (int) Math.round(Math.cos(angle) * radius);
		int posZ = (int) Math.round(Math.sin(angle) * radius);
		LOGGER.warn("Did not come across a decent location for land gates. Placing it without regard to any biomes.");
		
		BlockPos pos = getBiomeSource().findBiomeHorizontal((posX << 4) + 8, 0, (posZ << 4) + 8, 96, biome -> biome == normalBiome, worldRand);
		
		if(pos != null)
			landGatePosition = new ChunkPos(pos);
		else landGatePosition = new ChunkPos(posX, posZ);
		
		return landGatePosition;
	}
}