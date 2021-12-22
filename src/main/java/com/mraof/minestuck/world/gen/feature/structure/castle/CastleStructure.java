package com.mraof.minestuck.world.gen.feature.structure.castle;

import com.mojang.serialization.Codec;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;

/**
 * @author mraof
 *
 */
public class CastleStructure extends Structure<NoFeatureConfig>
{
	public CastleStructure(Codec<NoFeatureConfig> configCodec)
	{
		super(configCodec);
	}
	
	@Override
	protected boolean isFeatureChunk(ChunkGenerator generator, BiomeProvider biomeProvider, long seed, SharedSeedRandom rand, int chunkX, int chunkZ, Biome biome, ChunkPos pos, NoFeatureConfig config)
	{
        int var3 = chunkX >> 4;
        int var4 = chunkZ >> 4;
        rand.setSeed((long)(var3 ^ var4 << 4) ^ seed);
        rand.nextInt();
        return chunkX == 1 && chunkZ == 0;//this.rand.nextInt(3) != 0 ? false : (par1 != (var3 << 4) + 4 + this.rand.nextInt(8) ? false : par2 == (var4 << 4) + 4 + this.rand.nextInt(8));
    }
	
	@Override
	public GenerationStage.Decoration step()
	{
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}
	
	@Override
	public IStartFactory<NoFeatureConfig> getStartFactory()
	{
        return StructureCastleStart::new;
    }
}