package com.mraof.minestuck.world.gen;

import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.skaianet.SburbHandler;
import com.mraof.minestuck.skaianet.UnderlingController;
import com.mraof.minestuck.world.biome.LandBiomeHolder;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.GristTypeLayer;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.NoiseChunkGenerator;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class LandChunkGenerator extends NoiseChunkGenerator<LandGenSettings>
{
	public static final String GRIST_LAYER_INFO = "grist_layer.info";
	
	private static final float[] biomeWeight;
	
	static {
		biomeWeight = new float[25];
		for(int x = -2; x <= 2; x++)
			for(int z = -2; z <= 2; z++)
				biomeWeight[(x + 2)*5 + z + 2] = 10.0F / MathHelper.sqrt((float)(x * x + z * z) + 0.2F);
	}
	
	public final LandTypePair landTypes;
	public final StructureBlockRegistry blockRegistry;
	public final LandBiomeHolder biomeHolder;
	private final GristTypeLayer anyGristLayer, commonGristLayer, uncommonGristLayer;
	
	public LandChunkGenerator(IWorld worldIn, BiomeProvider biomeProviderIn, LandGenSettings settings)
	{
		super(worldIn, biomeProviderIn, 4, 8, 256, settings, false);
		
		landTypes = Objects.requireNonNull(settings.getLandTypes());
		blockRegistry = Objects.requireNonNull(settings.getBlockRegistry());
		
		biomeHolder = Objects.requireNonNull(settings.getBiomeHolder());
		//Server not available from the world as it is still being constructed. Is a different solution reliable here?
		GristType baseType = SburbHandler.getConnectionForDimension(ServerLifecycleHooks.getCurrentServer(), worldIn.getDimension().getType()).getBaseGrist();
		
		commonGristLayer = GristTypeLayer.createLayer(GristType.SpawnCategory.COMMON, 0, worldIn.getSeed(), 10, null);
		anyGristLayer = GristTypeLayer.createLayer(GristType.SpawnCategory.ANY, 1, worldIn.getSeed(), 8, baseType);
		uncommonGristLayer = GristTypeLayer.createLayer(GristType.SpawnCategory.UNCOMMON, 2, worldIn.getSeed(), 7, baseType);
	}
	
	public GristTypeLayer randomLayer(Random rand)
	{
		switch(rand.nextInt(3))
		{
			case 0: return commonGristLayer;
			case 1: return uncommonGristLayer;
			default: return anyGristLayer;
		}
	}
	public ITextComponent getGristLayerInfo(int x, int z)
	{
		GristType commonType = commonGristLayer.getTypeAt(x, z);
		GristType uncommonType = uncommonGristLayer.getTypeAt(x, z);
		GristType anyType = anyGristLayer.getTypeAt(x, z);
		
		return new TranslationTextComponent(GRIST_LAYER_INFO, commonType.getDisplayName(), uncommonType.getDisplayName(), anyType.getDisplayName());
	}
	
	@Override
	protected double[] getBiomeNoiseColumn(int columnX, int columnZ)
	{
		float baseDepth = biomeHolder.localBiomeFrom(biomeProvider.getNoiseBiome(columnX, 0, columnZ)).getDepth();
		
		float depthSum = 0, scaleSum = 0, weightSum = 0;
		for(int x = -2; x <= 2; x++)
		{
			for(int z = -2; z <= 2; z++)
			{
				Biome biome = biomeHolder.localBiomeFrom(biomeProvider.getNoiseBiome(columnX + x, 0, columnZ + z));
				float weight = biomeWeight[(x + 2)*5 + z + 2] / (biome.getDepth() + 2);
				if(biome.getDepth() > baseDepth)
					weight /= 2;
				
				depthSum += biome.getDepth() * weight;
				scaleSum += biome.getScale() * weight;
				weightSum += weight;
			}
		}
		
		float depth = (depthSum / weightSum) * 0.5F - 1/8F;
		float scale = (scaleSum / weightSum) * 0.9F + 0.1F;
		return new double[]{depth, scale};
	}
	
	@Override
	protected double func_222545_a(double depth, double scale, int height)
	{
		double modifier = ((double)height - (8.5D + depth * 8.5D / 8.0D * 4.0D)) * 12.0D * 128.0D / 256.0D / scale;
		if (modifier < 0.0D)
			modifier *= 4.0D;
		
		return modifier;
	}
	
	@Override
	protected void fillNoiseColumn(double[] noiseColumn, int columnX, int columnZ)
	{
		double horizontal = 684.412D;
		double vertical = 684.412D;
		double horizontal2 = 17.1103D;
		double vertical2 = 4.277575D;
		int lerpModifier = 3;
		int skyValueTarget = -10;
		this.calcNoiseColumn(noiseColumn, columnX, columnZ, horizontal, vertical, horizontal2, vertical2, lerpModifier, skyValueTarget);
	}

	@Override
	public void generateSurface(WorldGenRegion worldGenRegion, IChunk chunkIn) {
		SharedSeedRandom sharedRandom = new SharedSeedRandom();
		sharedRandom.setBaseChunkSeed(chunkIn.getPos().x, chunkIn.getPos().z);

		int xOffset = chunkIn.getPos().getXStart(), zOffset = chunkIn.getPos().getZStart();
		BlockPos.Mutable mutable = new BlockPos.Mutable();

		for(int x = 0; x < 16; x++)
		{
			for(int z = 0; z < 16; z++)
			{
				int y = chunkIn.getTopBlockY(Heightmap.Type.WORLD_SURFACE_WG, x, z);
				//So far we've skipped providing a noise value, but perhaps that's something we might actually want in the future?
				biomeHolder.localBiomeFrom(worldGenRegion.getBiome(mutable.setPos(x + xOffset, y, z + zOffset)))
						.buildSurface(sharedRandom, chunkIn, x + xOffset, z + zOffset, y, 0, getSettings().getDefaultBlock(), getSettings().getDefaultFluid(), getSeaLevel(),  world.getSeed());
			}
		}

		this.makeBedrock(chunkIn, sharedRandom);
	}
	
	@Override
	public void spawnMobs(WorldGenRegion region)
	{
		int x = region.getMainChunkX();
		int z = region.getMainChunkZ();
		Biome biome = biomeHolder.localBiomeFrom(region.getBiome((new ChunkPos(x, z)).asBlockPos()));
		SharedSeedRandom rand = new SharedSeedRandom();
		rand.setDecorationSeed(region.getSeed(), x << 4, z << 4);
		WorldEntitySpawner.performWorldGenSpawning(region, biome, x, z, rand);
	}
	
	@Override
	public List<Biome.SpawnListEntry> getPossibleCreatures(EntityClassification creatureType, BlockPos pos)
	{
		if(creatureType == MSEntityTypes.UNDERLING)
			return UnderlingController.getUnderlingList(pos, world.getWorld());
		else return getBiome(world.getBiomeManager(), pos).getSpawns(creatureType);
	}
	
	@Override
	public int getGroundHeight()
	{
		return 64;
	}
	
	@Override
	protected Biome getBiome(BiomeManager biomeManagerIn, BlockPos posIn)
	{
		return biomeHolder.localBiomeFrom(super.getBiome(biomeManagerIn, posIn));
	}
	
	@Override
	public boolean hasStructure(Biome biomeIn, Structure<? extends IFeatureConfig> structureIn)
	{
		return biomeHolder.localBiomeFrom(biomeIn).hasStructure(structureIn);
	}
	
	@Nullable
	@Override
	public <C extends IFeatureConfig> C getStructureConfig(Biome biomeIn, Structure<C> structureIn)
	{
		return biomeHolder.localBiomeFrom(biomeIn).getStructureConfig(structureIn);
	}
	
	public DimensionType getDimensionType()
	{
		return world.getDimension().getType();
	}
}